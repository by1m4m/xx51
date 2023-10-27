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
/*    */ public class WebSocketTimeoutException
/*    */   extends WebSocketException
/*    */ {
/*    */   private static final long serialVersionUID = -6145098200250676673L;
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
/*    */   public WebSocketTimeoutException(String message)
/*    */   {
/* 30 */     super(message);
/*    */   }
/*    */   
/*    */   public WebSocketTimeoutException(String message, Throwable cause)
/*    */   {
/* 35 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public WebSocketTimeoutException(Throwable cause)
/*    */   {
/* 40 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */