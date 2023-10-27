/*    */ package org.eclipse.jetty.websocket.common.message;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.util.BufferUtil;
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
/*    */ public class SimpleBinaryMessage
/*    */   implements MessageAppender
/*    */ {
/*    */   private static final int BUFFER_SIZE = 65535;
/*    */   private final EventDriver onEvent;
/*    */   protected final ByteArrayOutputStream out;
/*    */   private int size;
/*    */   protected boolean finished;
/*    */   
/*    */   public SimpleBinaryMessage(EventDriver onEvent)
/*    */   {
/* 38 */     this.onEvent = onEvent;
/* 39 */     this.out = new ByteArrayOutputStream(65535);
/* 40 */     this.finished = false;
/*    */   }
/*    */   
/*    */   public void appendFrame(ByteBuffer payload, boolean isLast)
/*    */     throws IOException
/*    */   {
/* 46 */     if (this.finished)
/*    */     {
/* 48 */       throw new IOException("Cannot append to finished buffer");
/*    */     }
/*    */     
/* 51 */     if (payload == null)
/*    */     {
/*    */ 
/* 54 */       return;
/*    */     }
/*    */     
/* 57 */     this.onEvent.getPolicy().assertValidBinaryMessageSize(this.size + payload.remaining());
/* 58 */     this.size += payload.remaining();
/*    */     
/* 60 */     BufferUtil.writeTo(payload, this.out);
/*    */   }
/*    */   
/*    */ 
/*    */   public void messageComplete()
/*    */   {
/* 66 */     this.finished = true;
/* 67 */     byte[] data = this.out.toByteArray();
/* 68 */     this.onEvent.onBinaryMessage(data);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\SimpleBinaryMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */