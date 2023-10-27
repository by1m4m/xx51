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
/*    */ public class WebSocketException
/*    */   extends RuntimeException
/*    */ {
/*    */   public WebSocketException() {}
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
/*    */   public WebSocketException(String message)
/*    */   {
/* 34 */     super(message);
/*    */   }
/*    */   
/*    */   public WebSocketException(String message, Throwable cause)
/*    */   {
/* 39 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public WebSocketException(Throwable cause)
/*    */   {
/* 44 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */