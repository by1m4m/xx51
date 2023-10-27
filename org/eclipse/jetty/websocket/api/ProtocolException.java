/*    */ package org.eclipse.jetty.websocket.api;
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
/*    */ public class ProtocolException
/*    */   extends CloseException
/*    */ {
/*    */   public ProtocolException(String message)
/*    */   {
/* 29 */     super(1002, message);
/*    */   }
/*    */   
/*    */   public ProtocolException(String message, Throwable t)
/*    */   {
/* 34 */     super(1002, message, t);
/*    */   }
/*    */   
/*    */   public ProtocolException(Throwable t)
/*    */   {
/* 39 */     super(1002, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\ProtocolException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */