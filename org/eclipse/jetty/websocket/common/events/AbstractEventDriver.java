/*     */ package org.eclipse.jetty.websocket.common.events;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.DecoratedObjectFactory;
/*     */ import org.eclipse.jetty.util.Utf8Appendable.NotUtf8Exception;
/*     */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.CloseException;
/*     */ import org.eclipse.jetty.websocket.api.RemoteEndpoint;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*     */ import org.eclipse.jetty.websocket.common.CloseInfo;
/*     */ import org.eclipse.jetty.websocket.common.LogicalConnection;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketSession;
/*     */ import org.eclipse.jetty.websocket.common.frames.CloseFrame;
/*     */ import org.eclipse.jetty.websocket.common.io.IOState;
/*     */ import org.eclipse.jetty.websocket.common.message.MessageAppender;
/*     */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEventDriver
/*     */   extends AbstractLifeCycle
/*     */   implements IncomingFrames, EventDriver
/*     */ {
/*  46 */   private static final Logger LOG = Log.getLogger(AbstractEventDriver.class);
/*     */   protected final Logger TARGET_LOG;
/*     */   protected WebSocketPolicy policy;
/*     */   protected final Object websocket;
/*     */   protected WebSocketSession session;
/*     */   protected MessageAppender activeMessage;
/*     */   
/*     */   public AbstractEventDriver(WebSocketPolicy policy, Object websocket)
/*     */   {
/*  55 */     this.policy = policy;
/*  56 */     this.websocket = websocket;
/*  57 */     this.TARGET_LOG = Log.getLogger(websocket.getClass());
/*     */   }
/*     */   
/*     */   protected void appendMessage(ByteBuffer buffer, boolean fin) throws IOException
/*     */   {
/*  62 */     this.activeMessage.appendFrame(buffer, fin);
/*     */     
/*  64 */     if (fin)
/*     */     {
/*  66 */       this.activeMessage.messageComplete();
/*  67 */       this.activeMessage = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void dispatch(Runnable runnable)
/*     */   {
/*  73 */     this.session.dispatch(runnable);
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/*  79 */     return this.policy;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketSession getSession()
/*     */   {
/*  85 */     return this.session;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void incomingError(Throwable e)
/*     */   {
/*  91 */     if (LOG.isDebugEnabled())
/*     */     {
/*  93 */       LOG.debug("incomingError(" + e.getClass().getName() + ")", e);
/*     */     }
/*     */     
/*  96 */     onError(e);
/*     */   }
/*     */   
/*     */ 
/*     */   public void incomingFrame(Frame frame)
/*     */   {
/* 102 */     if (LOG.isDebugEnabled())
/*     */     {
/* 104 */       LOG.debug("incomingFrame({})", new Object[] { frame });
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 109 */       onFrame(frame);
/*     */       
/* 111 */       byte opcode = frame.getOpCode();
/* 112 */       switch (opcode)
/*     */       {
/*     */ 
/*     */       case 8: 
/* 116 */         boolean validate = true;
/* 117 */         CloseFrame closeframe = (CloseFrame)frame;
/* 118 */         CloseInfo close = new CloseInfo(closeframe, validate);
/*     */         
/*     */ 
/* 121 */         this.session.getConnection().getIOState().onCloseRemote(close);
/*     */         
/* 123 */         return;
/*     */       
/*     */ 
/*     */       case 9: 
/* 127 */         if (LOG.isDebugEnabled())
/*     */         {
/* 129 */           LOG.debug("PING: {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
/*     */         }
/*     */         ByteBuffer pongBuf;
/* 132 */         if (frame.hasPayload())
/*     */         {
/* 134 */           ByteBuffer pongBuf = ByteBuffer.allocate(frame.getPayload().remaining());
/* 135 */           BufferUtil.put(frame.getPayload().slice(), pongBuf);
/* 136 */           BufferUtil.flipToFlush(pongBuf, 0);
/*     */         }
/*     */         else
/*     */         {
/* 140 */           pongBuf = ByteBuffer.allocate(0);
/*     */         }
/* 142 */         onPing(frame.getPayload());
/* 143 */         this.session.getRemote().sendPong(pongBuf);
/* 144 */         break;
/*     */       
/*     */ 
/*     */       case 10: 
/* 148 */         if (LOG.isDebugEnabled())
/*     */         {
/* 150 */           LOG.debug("PONG: {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
/*     */         }
/* 152 */         onPong(frame.getPayload());
/* 153 */         break;
/*     */       
/*     */ 
/*     */       case 2: 
/* 157 */         onBinaryFrame(frame.getPayload(), frame.isFin());
/* 158 */         return;
/*     */       
/*     */ 
/*     */       case 1: 
/* 162 */         onTextFrame(frame.getPayload(), frame.isFin());
/* 163 */         return;
/*     */       
/*     */ 
/*     */       case 0: 
/* 167 */         onContinuationFrame(frame.getPayload(), frame.isFin());
/* 168 */         return;
/*     */       case 3: case 4: 
/*     */       case 5: case 6: 
/*     */       case 7: default: 
/* 172 */         if (LOG.isDebugEnabled()) {
/* 173 */           LOG.debug("Unhandled OpCode: {}", opcode);
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/*     */     } catch (Utf8Appendable.NotUtf8Exception e) {
/* 179 */       terminateConnection(1007, e.getMessage());
/*     */     }
/*     */     catch (CloseException e)
/*     */     {
/* 183 */       terminateConnection(e.getStatusCode(), e.getMessage());
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 187 */       unhandled(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onContinuationFrame(ByteBuffer buffer, boolean fin)
/*     */     throws IOException
/*     */   {
/* 194 */     if (this.activeMessage == null)
/*     */     {
/* 196 */       throw new IOException("Out of order Continuation frame encountered");
/*     */     }
/*     */     
/* 199 */     appendMessage(buffer, fin);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onPong(ByteBuffer buffer) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onPing(ByteBuffer buffer) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BatchMode getBatchMode()
/*     */   {
/* 217 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void openSession(WebSocketSession session)
/*     */   {
/* 223 */     if (LOG.isDebugEnabled())
/*     */     {
/* 225 */       LOG.debug("openSession({})", new Object[] { session });
/* 226 */       LOG.debug("objectFactory={}", new Object[] { session.getContainerScope().getObjectFactory() });
/*     */     }
/* 228 */     this.session = session;
/* 229 */     this.session.getContainerScope().getObjectFactory().decorate(this.websocket);
/*     */     
/*     */     try
/*     */     {
/* 233 */       onConnect();
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 237 */       this.session.notifyError(t);
/* 238 */       throw t;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void terminateConnection(int statusCode, String rawreason)
/*     */   {
/* 244 */     if (LOG.isDebugEnabled())
/* 245 */       LOG.debug("terminateConnection({},{})", new Object[] { Integer.valueOf(statusCode), rawreason });
/* 246 */     this.session.close(statusCode, CloseFrame.truncate(rawreason));
/*     */   }
/*     */   
/*     */   private void unhandled(Throwable t)
/*     */   {
/* 251 */     this.TARGET_LOG.warn("Unhandled Error (closing connection)", t);
/* 252 */     onError(t);
/*     */     
/* 254 */     if ((t instanceof CloseException))
/*     */     {
/* 256 */       terminateConnection(((CloseException)t).getStatusCode(), t.getClass().getSimpleName());
/* 257 */       return;
/*     */     }
/*     */     
/*     */ 
/* 261 */     switch (this.policy.getBehavior())
/*     */     {
/*     */     case SERVER: 
/* 264 */       terminateConnection(1011, t.getClass().getSimpleName());
/* 265 */       break;
/*     */     case CLIENT: 
/* 267 */       terminateConnection(1008, t.getClass().getSimpleName());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\AbstractEventDriver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */