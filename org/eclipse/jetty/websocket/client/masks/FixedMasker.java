/*    */ package org.eclipse.jetty.websocket.client.masks;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ public class FixedMasker
/*    */   implements Masker
/*    */ {
/*    */   private final byte[] mask;
/*    */   
/*    */   public FixedMasker()
/*    */   {
/* 31 */     this(new byte[] { -1, -1, -1, -1 });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FixedMasker(byte[] mask)
/*    */   {
/* 39 */     this.mask = Arrays.copyOf(mask, 4);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setMask(WebSocketFrame frame)
/*    */   {
/* 45 */     frame.setMask(this.mask);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\masks\FixedMasker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */