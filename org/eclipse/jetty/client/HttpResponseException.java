/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import org.eclipse.jetty.client.api.Response;
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
/*    */ public class HttpResponseException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final Response response;
/*    */   
/*    */   public HttpResponseException(String message, Response response)
/*    */   {
/* 29 */     super(message);
/* 30 */     this.response = response;
/*    */   }
/*    */   
/*    */   public Response getResponse()
/*    */   {
/* 35 */     return this.response;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpResponseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */