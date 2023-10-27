/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpConnection;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpInetConnection;
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
/*     */ public class RequestTargetHost
/*     */   implements HttpRequestInterceptor
/*     */ {
/*     */   public void process(HttpRequest request, HttpContext context)
/*     */     throws HttpException, IOException
/*     */   {
/*  64 */     if (request == null) {
/*  65 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  67 */     if (context == null) {
/*  68 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*  71 */     ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/*  72 */     String method = request.getRequestLine().getMethod();
/*  73 */     if ((method.equalsIgnoreCase("CONNECT")) && (ver.lessEquals(HttpVersion.HTTP_1_0))) {
/*  74 */       return;
/*     */     }
/*     */     
/*  77 */     if (!request.containsHeader("Host")) {
/*  78 */       HttpHost targethost = (HttpHost)context.getAttribute("http.target_host");
/*     */       
/*  80 */       if (targethost == null) {
/*  81 */         HttpConnection conn = (HttpConnection)context.getAttribute("http.connection");
/*     */         
/*  83 */         if ((conn instanceof HttpInetConnection))
/*     */         {
/*     */ 
/*  86 */           InetAddress address = ((HttpInetConnection)conn).getRemoteAddress();
/*  87 */           int port = ((HttpInetConnection)conn).getRemotePort();
/*  88 */           if (address != null) {
/*  89 */             targethost = new HttpHost(address.getHostName(), port);
/*     */           }
/*     */         }
/*  92 */         if (targethost == null) {
/*  93 */           if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
/*  94 */             return;
/*     */           }
/*  96 */           throw new ProtocolException("Target host missing");
/*     */         }
/*     */       }
/*     */       
/* 100 */       request.addHeader("Host", targethost.toHostString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\RequestTargetHost.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */