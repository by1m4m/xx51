/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.StatusLine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseConnControl
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   public void process(HttpResponse response, HttpContext context)
/*     */     throws HttpException, IOException
/*     */   {
/*  65 */     if (response == null) {
/*  66 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/*  68 */     if (context == null) {
/*  69 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*  72 */     int status = response.getStatusLine().getStatusCode();
/*  73 */     if ((status == 400) || (status == 408) || (status == 411) || (status == 413) || (status == 414) || (status == 503) || (status == 501))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */       response.setHeader("Connection", "Close");
/*  81 */       return;
/*     */     }
/*     */     
/*     */ 
/*  85 */     HttpEntity entity = response.getEntity();
/*  86 */     if (entity != null) {
/*  87 */       ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
/*  88 */       if ((entity.getContentLength() < 0L) && ((!entity.isChunked()) || (ver.lessEquals(HttpVersion.HTTP_1_0))))
/*     */       {
/*  90 */         response.setHeader("Connection", "Close");
/*  91 */         return;
/*     */       }
/*     */     }
/*     */     
/*  95 */     HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */     
/*  97 */     if (request != null) {
/*  98 */       Header header = request.getFirstHeader("Connection");
/*  99 */       if (header != null) {
/* 100 */         response.setHeader("Connection", header.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\ResponseConnControl.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */