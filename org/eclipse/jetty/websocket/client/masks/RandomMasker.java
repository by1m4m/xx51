/*    */ package org.eclipse.jetty.websocket.client.masks;
/*    */ 
/*    */ import java.util.Random;
/*    */ import org.eclipse.jetty.websocket.common.WebSocketFrame;
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
/*    */ public class RandomMasker
/*    */   implements Masker
/*    */ {
/*    */   private final Random random;
/*    */   
/*    */   public RandomMasker()
/*    */   {
/* 31 */     this(new Random());
/*    */   }
/*    */   
/*    */   public RandomMasker(Random random)
/*    */   {
/* 36 */     this.random = random;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setMask(WebSocketFrame frame)
/*    */   {
/* 42 */     byte[] mask = new byte[4];
/* 43 */     this.random.nextBytes(mask);
/* 44 */     frame.setMask(mask);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\masks\RandomMasker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */