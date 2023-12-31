/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
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
/*     */ 
/*     */ public class RequestContent
/*     */   implements HttpRequestInterceptor
/*     */ {
/*     */   public void process(HttpRequest request, HttpContext context)
/*     */     throws HttpException, IOException
/*     */   {
/*  66 */     if (request == null) {
/*  67 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  69 */     if ((request instanceof HttpEntityEnclosingRequest)) {
/*  70 */       if (request.containsHeader("Transfer-Encoding")) {
/*  71 */         throw new ProtocolException("Transfer-encoding header already present");
/*     */       }
/*  73 */       if (request.containsHeader("Content-Length")) {
/*  74 */         throw new ProtocolException("Content-Length header already present");
/*     */       }
/*  76 */       ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/*  77 */       HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/*  78 */       if (entity == null) {
/*  79 */         request.addHeader("Content-Length", "0");
/*  80 */         return;
/*     */       }
/*     */       
/*  83 */       if ((entity.isChunked()) || (entity.getContentLength() < 0L)) {
/*  84 */         if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
/*  85 */           throw new ProtocolException("Chunked transfer encoding not allowed for " + ver);
/*     */         }
/*     */         
/*  88 */         request.addHeader("Transfer-Encoding", "chunked");
/*     */       } else {
/*  90 */         request.addHeader("Content-Length", Long.toString(entity.getContentLength()));
/*     */       }
/*     */       
/*  93 */       if ((entity.getContentType() != null) && (!request.containsHeader("Content-Type")))
/*     */       {
/*  95 */         request.addHeader(entity.getContentType());
/*     */       }
/*     */       
/*  98 */       if ((entity.getContentEncoding() != null) && (!request.containsHeader("Content-Encoding")))
/*     */       {
/* 100 */         request.addHeader(entity.getContentEncoding());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\RequestContent.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */