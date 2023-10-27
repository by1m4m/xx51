/*     */ package org.eclipse.jetty.websocket.common.events;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketConnectionListener;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketFrameListener;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketListener;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPartialListener;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPingPongListener;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
/*     */ import org.eclipse.jetty.websocket.common.CloseInfo;
/*     */ import org.eclipse.jetty.websocket.common.frames.ReadOnlyDelegatedFrame;
/*     */ import org.eclipse.jetty.websocket.common.message.SimpleBinaryMessage;
/*     */ import org.eclipse.jetty.websocket.common.message.SimpleTextMessage;
/*     */ import org.eclipse.jetty.websocket.common.util.Utf8PartialBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JettyListenerEventDriver
/*     */   extends AbstractEventDriver
/*     */ {
/*  47 */   private static final Logger LOG = Log.getLogger(JettyListenerEventDriver.class);
/*     */   private final WebSocketConnectionListener listener;
/*     */   private Utf8PartialBuilder utf8Partial;
/*  50 */   private boolean hasCloseBeenCalled = false;
/*     */   
/*     */   public JettyListenerEventDriver(WebSocketPolicy policy, WebSocketConnectionListener listener)
/*     */   {
/*  54 */     super(policy, listener);
/*  55 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   public void onBinaryFrame(ByteBuffer buffer, boolean fin)
/*     */     throws IOException
/*     */   {
/*  61 */     if ((this.listener instanceof WebSocketListener))
/*     */     {
/*  63 */       if (this.activeMessage == null)
/*     */       {
/*  65 */         this.activeMessage = new SimpleBinaryMessage(this);
/*     */       }
/*     */       
/*  68 */       appendMessage(buffer, fin);
/*     */     }
/*     */     
/*  71 */     if ((this.listener instanceof WebSocketPartialListener))
/*     */     {
/*  73 */       ((WebSocketPartialListener)this.listener).onWebSocketPartialBinary(buffer.slice().asReadOnlyBuffer(), fin);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBinaryMessage(byte[] data)
/*     */   {
/*  80 */     if ((this.listener instanceof WebSocketListener))
/*     */     {
/*  82 */       ((WebSocketListener)this.listener).onWebSocketBinary(data, 0, data.length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onClose(CloseInfo close)
/*     */   {
/*  89 */     if (this.hasCloseBeenCalled)
/*     */     {
/*     */ 
/*  92 */       return;
/*     */     }
/*  94 */     this.hasCloseBeenCalled = true;
/*     */     
/*  96 */     int statusCode = close.getStatusCode();
/*  97 */     String reason = close.getReason();
/*  98 */     this.listener.onWebSocketClose(statusCode, reason);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onConnect()
/*     */   {
/* 104 */     if (LOG.isDebugEnabled())
/* 105 */       LOG.debug("onConnect({})", new Object[] { this.session });
/* 106 */     this.listener.onWebSocketConnect(this.session);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onError(Throwable cause)
/*     */   {
/* 112 */     this.listener.onWebSocketError(cause);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onFrame(Frame frame)
/*     */   {
/* 118 */     if ((this.listener instanceof WebSocketFrameListener))
/*     */     {
/* 120 */       ((WebSocketFrameListener)this.listener).onWebSocketFrame(new ReadOnlyDelegatedFrame(frame));
/*     */     }
/*     */     
/* 123 */     if ((this.listener instanceof WebSocketPingPongListener))
/*     */     {
/* 125 */       if (frame.getType() == Frame.Type.PING)
/*     */       {
/* 127 */         ((WebSocketPingPongListener)this.listener).onWebSocketPing(frame.getPayload().asReadOnlyBuffer());
/*     */       }
/* 129 */       else if (frame.getType() == Frame.Type.PONG)
/*     */       {
/* 131 */         ((WebSocketPingPongListener)this.listener).onWebSocketPong(frame.getPayload().asReadOnlyBuffer());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onInputStream(InputStream stream) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onReader(Reader reader) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onTextFrame(ByteBuffer buffer, boolean fin)
/*     */     throws IOException
/*     */   {
/* 151 */     if ((this.listener instanceof WebSocketListener))
/*     */     {
/* 153 */       if (this.activeMessage == null)
/*     */       {
/* 155 */         this.activeMessage = new SimpleTextMessage(this);
/*     */       }
/*     */       
/* 158 */       appendMessage(buffer, fin);
/*     */     }
/*     */     
/* 161 */     if ((this.listener instanceof WebSocketPartialListener))
/*     */     {
/* 163 */       if (this.utf8Partial == null)
/*     */       {
/* 165 */         this.utf8Partial = new Utf8PartialBuilder();
/*     */       }
/*     */       
/* 168 */       String partial = this.utf8Partial.toPartialString(buffer);
/*     */       
/* 170 */       ((WebSocketPartialListener)this.listener).onWebSocketPartialText(partial, fin);
/*     */       
/* 172 */       if (fin)
/*     */       {
/* 174 */         partial = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onTextMessage(String message)
/*     */   {
/* 187 */     if ((this.listener instanceof WebSocketListener))
/*     */     {
/* 189 */       ((WebSocketListener)this.listener).onWebSocketText(message);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 196 */     return String.format("%s[%s]", new Object[] { JettyListenerEventDriver.class.getSimpleName(), this.listener.getClass().getName() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\JettyListenerEventDriver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */