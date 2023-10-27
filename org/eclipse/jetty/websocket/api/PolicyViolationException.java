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
/*    */ 
/*    */ 
/*    */ public class PolicyViolationException
/*    */   extends CloseException
/*    */ {
/*    */   public PolicyViolationException(String message)
/*    */   {
/* 31 */     super(1008, message);
/*    */   }
/*    */   
/*    */   public PolicyViolationException(String message, Throwable t)
/*    */   {
/* 36 */     super(1008, message, t);
/*    */   }
/*    */   
/*    */   public PolicyViolationException(Throwable t)
/*    */   {
/* 41 */     super(1008, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\PolicyViolationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */