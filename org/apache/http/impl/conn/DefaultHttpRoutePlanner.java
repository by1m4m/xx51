/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.params.ConnRouteParams;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class DefaultHttpRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   
/*     */   public DefaultHttpRoutePlanner(SchemeRegistry schreg)
/*     */   {
/*  75 */     if (schreg == null) {
/*  76 */       throw new IllegalArgumentException("SchemeRegistry must not be null.");
/*     */     }
/*     */     
/*  79 */     this.schemeRegistry = schreg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context)
/*     */     throws HttpException
/*     */   {
/*  87 */     if (request == null) {
/*  88 */       throw new IllegalStateException("Request must not be null.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  93 */     HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
/*     */     
/*  95 */     if (route != null) {
/*  96 */       return route;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 101 */     if (target == null) {
/* 102 */       throw new IllegalStateException("Target host must not be null.");
/*     */     }
/*     */     
/*     */ 
/* 106 */     InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
/*     */     
/* 108 */     HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());
/*     */     
/*     */ 
/* 111 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/*     */     
/*     */ 
/* 114 */     boolean secure = schm.isLayered();
/*     */     
/* 116 */     if (proxy == null) {
/* 117 */       route = new HttpRoute(target, local, secure);
/*     */     } else {
/* 119 */       route = new HttpRoute(target, local, proxy, secure);
/*     */     }
/* 121 */     return route;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\DefaultHttpRoutePlanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */