/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.RequestLine;
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
/*    */ public class RequestConnControl
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context)
/*    */     throws HttpException, IOException
/*    */   {
/* 59 */     if (request == null) {
/* 60 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/*    */     
/* 63 */     String method = request.getRequestLine().getMethod();
/* 64 */     if (method.equalsIgnoreCase("CONNECT")) {
/* 65 */       return;
/*    */     }
/*    */     
/* 68 */     if (!request.containsHeader("Connection"))
/*    */     {
/*    */ 
/* 71 */       request.addHeader("Connection", "Keep-Alive");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\RequestConnControl.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */