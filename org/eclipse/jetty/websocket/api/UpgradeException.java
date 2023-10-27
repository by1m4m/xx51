/*    */ package org.eclipse.jetty.websocket.api;
/*    */ 
/*    */ import java.net.URI;
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
/*    */ public class UpgradeException
/*    */   extends WebSocketException
/*    */ {
/*    */   private final URI requestURI;
/*    */   private final int responseStatusCode;
/*    */   
/*    */   public UpgradeException(URI requestURI, int responseStatusCode, String message)
/*    */   {
/* 34 */     super(message);
/* 35 */     this.requestURI = requestURI;
/* 36 */     this.responseStatusCode = responseStatusCode;
/*    */   }
/*    */   
/*    */   public UpgradeException(URI requestURI, int responseStatusCode, String message, Throwable cause)
/*    */   {
/* 41 */     super(message, cause);
/* 42 */     this.requestURI = requestURI;
/* 43 */     this.responseStatusCode = responseStatusCode;
/*    */   }
/*    */   
/*    */   public UpgradeException(URI requestURI, Throwable cause)
/*    */   {
/* 48 */     super(cause);
/* 49 */     this.requestURI = requestURI;
/* 50 */     this.responseStatusCode = -1;
/*    */   }
/*    */   
/*    */   public URI getRequestURI()
/*    */   {
/* 55 */     return this.requestURI;
/*    */   }
/*    */   
/*    */   public int getResponseStatusCode()
/*    */   {
/* 60 */     return this.responseStatusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\UpgradeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */