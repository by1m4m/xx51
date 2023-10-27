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
/*    */ public class MessageTooLargeException
/*    */   extends CloseException
/*    */ {
/*    */   public MessageTooLargeException(String message)
/*    */   {
/* 31 */     super(1009, message);
/*    */   }
/*    */   
/*    */   public MessageTooLargeException(String message, Throwable t)
/*    */   {
/* 36 */     super(1009, message, t);
/*    */   }
/*    */   
/*    */   public MessageTooLargeException(Throwable t)
/*    */   {
/* 41 */     super(1009, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\MessageTooLargeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */