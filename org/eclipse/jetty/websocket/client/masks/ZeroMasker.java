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
/*    */ public class ZeroMasker
/*    */   implements Masker
/*    */ {
/*    */   private final byte[] mask;
/*    */   
/*    */   public ZeroMasker()
/*    */   {
/* 31 */     this.mask = new byte[4];
/* 32 */     Arrays.fill(this.mask, (byte)0);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setMask(WebSocketFrame frame)
/*    */   {
/* 38 */     frame.setMask(this.mask);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\masks\ZeroMasker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */