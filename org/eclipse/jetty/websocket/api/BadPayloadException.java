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
/*    */ 
/*    */ public class BadPayloadException
/*    */   extends CloseException
/*    */ {
/*    */   public BadPayloadException(String message)
/*    */   {
/* 32 */     super(1007, message);
/*    */   }
/*    */   
/*    */   public BadPayloadException(String message, Throwable t)
/*    */   {
/* 37 */     super(1007, message, t);
/*    */   }
/*    */   
/*    */   public BadPayloadException(Throwable t)
/*    */   {
/* 42 */     super(1007, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\BadPayloadException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */