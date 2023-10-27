/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.RequestLine;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ @Immutable
/*    */ public class RequestDefaultHeaders
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context)
/*    */     throws HttpException, IOException
/*    */   {
/* 56 */     if (request == null) {
/* 57 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/*    */     
/* 60 */     String method = request.getRequestLine().getMethod();
/* 61 */     if (method.equalsIgnoreCase("CONNECT")) {
/* 62 */       return;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 67 */     Collection<Header> defHeaders = (Collection)request.getParams().getParameter("http.default-headers");
/*    */     
/*    */ 
/* 70 */     if (defHeaders != null) {
/* 71 */       for (Header defHeader : defHeaders) {
/* 72 */         request.addHeader(defHeader);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\protocol\RequestDefaultHeaders.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */