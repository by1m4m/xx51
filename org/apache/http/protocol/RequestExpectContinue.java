/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.RequestLine;
/*    */ import org.apache.http.params.HttpProtocolParams;
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
/*    */ public class RequestExpectContinue
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context)
/*    */     throws HttpException, IOException
/*    */   {
/* 63 */     if (request == null) {
/* 64 */       throw new IllegalArgumentException("HTTP request may not be null");
/*    */     }
/* 66 */     if ((request instanceof HttpEntityEnclosingRequest)) {
/* 67 */       HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/*    */       
/* 69 */       if ((entity != null) && (entity.getContentLength() != 0L)) {
/* 70 */         ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/* 71 */         if ((HttpProtocolParams.useExpectContinue(request.getParams())) && (!ver.lessEquals(HttpVersion.HTTP_1_0)))
/*    */         {
/* 73 */           request.addHeader("Expect", "100-Continue");
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\RequestExpectContinue.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */