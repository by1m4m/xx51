/*     */ package org.eclipse.jetty.websocket.common.events;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.annotations.WebSocket;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.common.CloseInfo;
/*     */ import org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
/*     */ import org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;
/*     */ import org.eclipse.jetty.websocket.common.message.MessageAppender;
/*     */ import org.eclipse.jetty.websocket.common.message.MessageInputStream;
/*     */ import org.eclipse.jetty.websocket.common.message.MessageReader;
/*     */ import org.eclipse.jetty.websocket.common.message.SimpleBinaryMessage;
/*     */ import org.eclipse.jetty.websocket.common.message.SimpleTextMessage;
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
/*     */ public class JettyAnnotatedEventDriver
/*     */   extends AbstractEventDriver
/*     */ {
/*     */   private final JettyAnnotatedMetadata events;
/*  43 */   private boolean hasCloseBeenCalled = false;
/*     */   private BatchMode batchMode;
/*     */   
/*     */   public JettyAnnotatedEventDriver(WebSocketPolicy policy, Object websocket, JettyAnnotatedMetadata events)
/*     */   {
/*  48 */     super(policy, websocket);
/*  49 */     this.events = events;
/*     */     
/*  51 */     WebSocket anno = (WebSocket)websocket.getClass().getAnnotation(WebSocket.class);
/*     */     
/*  53 */     if (anno.maxTextMessageSize() > 0)
/*     */     {
/*  55 */       this.policy.setMaxTextMessageSize(anno.maxTextMessageSize());
/*     */     }
/*  57 */     if (anno.maxBinaryMessageSize() > 0)
/*     */     {
/*  59 */       this.policy.setMaxBinaryMessageSize(anno.maxBinaryMessageSize());
/*     */     }
/*  61 */     if (anno.inputBufferSize() > 0)
/*     */     {
/*  63 */       this.policy.setInputBufferSize(anno.inputBufferSize());
/*     */     }
/*  65 */     if (anno.maxIdleTime() > 0)
/*     */     {
/*  67 */       this.policy.setIdleTimeout(anno.maxIdleTime());
/*     */     }
/*  69 */     this.batchMode = anno.batchMode();
/*     */   }
/*     */   
/*     */ 
/*     */   public BatchMode getBatchMode()
/*     */   {
/*  75 */     return this.batchMode;
/*     */   }
/*     */   
/*     */   public void onBinaryFrame(ByteBuffer buffer, boolean fin)
/*     */     throws IOException
/*     */   {
/*  81 */     if (this.events.onBinary == null)
/*     */     {
/*     */ 
/*  84 */       return;
/*     */     }
/*     */     
/*  87 */     if (this.activeMessage == null)
/*     */     {
/*  89 */       if (this.events.onBinary.isStreaming())
/*     */       {
/*  91 */         this.activeMessage = new MessageInputStream();
/*  92 */         final MessageAppender msg = this.activeMessage;
/*  93 */         dispatch(new Runnable()
/*     */         {
/*     */ 
/*     */           public void run()
/*     */           {
/*     */             try
/*     */             {
/* 100 */               JettyAnnotatedEventDriver.this.events.onBinary.call(JettyAnnotatedEventDriver.this.websocket, JettyAnnotatedEventDriver.this.session, new Object[] { msg });
/*     */ 
/*     */             }
/*     */             catch (Throwable t)
/*     */             {
/* 105 */               JettyAnnotatedEventDriver.this.onError(t);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */       else
/*     */       {
/* 112 */         this.activeMessage = new SimpleBinaryMessage(this);
/*     */       }
/*     */     }
/*     */     
/* 116 */     appendMessage(buffer, fin);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBinaryMessage(byte[] data)
/*     */   {
/* 122 */     if (this.events.onBinary != null)
/*     */     {
/* 124 */       this.events.onBinary.call(this.websocket, this.session, new Object[] { data, Integer.valueOf(0), Integer.valueOf(data.length) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onClose(CloseInfo close)
/*     */   {
/* 131 */     if (this.hasCloseBeenCalled)
/*     */     {
/*     */ 
/* 134 */       return;
/*     */     }
/* 136 */     this.hasCloseBeenCalled = true;
/* 137 */     if (this.events.onClose != null)
/*     */     {
/* 139 */       this.events.onClose.call(this.websocket, this.session, new Object[] { Integer.valueOf(close.getStatusCode()), close.getReason() });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onConnect()
/*     */   {
/* 146 */     if (this.events.onConnect != null)
/*     */     {
/* 148 */       this.events.onConnect.call(this.websocket, new Object[] { this.session });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onError(Throwable cause)
/*     */   {
/* 155 */     if (this.events.onError != null)
/*     */     {
/* 157 */       this.events.onError.call(this.websocket, this.session, new Object[] { cause });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onFrame(Frame frame)
/*     */   {
/* 164 */     if (this.events.onFrame != null)
/*     */     {
/* 166 */       this.events.onFrame.call(this.websocket, this.session, new Object[] { frame });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onInputStream(InputStream stream)
/*     */   {
/* 173 */     if (this.events.onBinary != null)
/*     */     {
/* 175 */       this.events.onBinary.call(this.websocket, this.session, new Object[] { stream });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onReader(Reader reader)
/*     */   {
/* 182 */     if (this.events.onText != null)
/*     */     {
/* 184 */       this.events.onText.call(this.websocket, this.session, new Object[] { reader });
/*     */     }
/*     */   }
/*     */   
/*     */   public void onTextFrame(ByteBuffer buffer, boolean fin)
/*     */     throws IOException
/*     */   {
/* 191 */     if (this.events.onText == null)
/*     */     {
/*     */ 
/* 194 */       return;
/*     */     }
/*     */     
/* 197 */     if (this.activeMessage == null)
/*     */     {
/* 199 */       if (this.events.onText.isStreaming())
/*     */       {
/* 201 */         this.activeMessage = new MessageReader(new MessageInputStream());
/* 202 */         final MessageAppender msg = this.activeMessage;
/* 203 */         dispatch(new Runnable()
/*     */         {
/*     */ 
/*     */           public void run()
/*     */           {
/*     */             try
/*     */             {
/* 210 */               JettyAnnotatedEventDriver.this.events.onText.call(JettyAnnotatedEventDriver.this.websocket, JettyAnnotatedEventDriver.this.session, new Object[] { msg });
/*     */ 
/*     */             }
/*     */             catch (Throwable t)
/*     */             {
/* 215 */               JettyAnnotatedEventDriver.this.onError(t);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */       else
/*     */       {
/* 222 */         this.activeMessage = new SimpleTextMessage(this);
/*     */       }
/*     */     }
/*     */     
/* 226 */     appendMessage(buffer, fin);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onTextMessage(String message)
/*     */   {
/* 232 */     if (this.events.onText != null)
/*     */     {
/* 234 */       this.events.onText.call(this.websocket, this.session, new Object[] { message });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 241 */     return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), this.websocket });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\JettyAnnotatedEventDriver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */