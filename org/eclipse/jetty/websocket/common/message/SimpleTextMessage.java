/*    */ package org.eclipse.jetty.websocket.common.message;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.util.Utf8StringBuilder;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*    */ import org.eclipse.jetty.websocket.common.events.EventDriver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTextMessage
/*    */   implements MessageAppender
/*    */ {
/*    */   private final EventDriver onEvent;
/*    */   protected final Utf8StringBuilder utf;
/* 31 */   private int size = 0;
/*    */   protected boolean finished;
/*    */   
/*    */   public SimpleTextMessage(EventDriver onEvent)
/*    */   {
/* 36 */     this.onEvent = onEvent;
/* 37 */     this.utf = new Utf8StringBuilder(1024);
/* 38 */     this.size = 0;
/* 39 */     this.finished = false;
/*    */   }
/*    */   
/*    */   public void appendFrame(ByteBuffer payload, boolean isLast)
/*    */     throws IOException
/*    */   {
/* 45 */     if (this.finished)
/*    */     {
/* 47 */       throw new IOException("Cannot append to finished buffer");
/*    */     }
/*    */     
/* 50 */     if (payload == null)
/*    */     {
/*    */ 
/* 53 */       return;
/*    */     }
/*    */     
/* 56 */     this.onEvent.getPolicy().assertValidTextMessageSize(this.size + payload.remaining());
/* 57 */     this.size += payload.remaining();
/*    */     
/*    */ 
/* 60 */     this.utf.append(payload);
/*    */   }
/*    */   
/*    */ 
/*    */   public void messageComplete()
/*    */   {
/* 66 */     this.finished = true;
/*    */     
/*    */ 
/* 69 */     this.onEvent.onTextMessage(this.utf.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\SimpleTextMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */