/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import org.eclipse.jetty.client.api.Request;
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
/*    */ public class HttpRequestException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final Request request;
/*    */   
/*    */   public HttpRequestException(String message, Request request)
/*    */   {
/* 29 */     super(message);
/* 30 */     this.request = request;
/*    */   }
/*    */   
/*    */   public Request getRequest()
/*    */   {
/* 35 */     return this.request;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpRequestException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */