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
/*    */ public class CloseException
/*    */   extends WebSocketException
/*    */ {
/*    */   private int statusCode;
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
/*    */   public CloseException(int closeCode, String message)
/*    */   {
/* 28 */     super(message);
/* 29 */     this.statusCode = closeCode;
/*    */   }
/*    */   
/*    */   public CloseException(int closeCode, String message, Throwable cause)
/*    */   {
/* 34 */     super(message, cause);
/* 35 */     this.statusCode = closeCode;
/*    */   }
/*    */   
/*    */   public CloseException(int closeCode, Throwable cause)
/*    */   {
/* 40 */     super(cause);
/* 41 */     this.statusCode = closeCode;
/*    */   }
/*    */   
/*    */   public int getStatusCode()
/*    */   {
/* 46 */     return this.statusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\CloseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */