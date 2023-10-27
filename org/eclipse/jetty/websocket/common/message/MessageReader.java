/*    */ package org.eclipse.jetty.websocket.common.message;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageReader
/*    */   extends InputStreamReader
/*    */   implements MessageAppender
/*    */ {
/*    */   private final MessageInputStream stream;
/*    */   
/*    */   public MessageReader(MessageInputStream stream)
/*    */   {
/* 38 */     super(stream, StandardCharsets.UTF_8);
/* 39 */     this.stream = stream;
/*    */   }
/*    */   
/*    */   public void appendFrame(ByteBuffer payload, boolean isLast)
/*    */     throws IOException
/*    */   {
/* 45 */     this.stream.appendFrame(payload, isLast);
/*    */   }
/*    */   
/*    */ 
/*    */   public void messageComplete()
/*    */   {
/* 51 */     this.stream.messageComplete();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\MessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */