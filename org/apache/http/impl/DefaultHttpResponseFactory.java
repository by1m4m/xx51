/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.ReasonPhraseCatalog;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.message.BasicHttpResponse;
/*     */ import org.apache.http.message.BasicStatusLine;
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
/*     */ public class DefaultHttpResponseFactory
/*     */   implements HttpResponseFactory
/*     */ {
/*     */   protected final ReasonPhraseCatalog reasonCatalog;
/*     */   
/*     */   public DefaultHttpResponseFactory(ReasonPhraseCatalog catalog)
/*     */   {
/*  66 */     if (catalog == null) {
/*  67 */       throw new IllegalArgumentException("Reason phrase catalog must not be null.");
/*     */     }
/*     */     
/*  70 */     this.reasonCatalog = catalog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpResponseFactory()
/*     */   {
/*  78 */     this(EnglishReasonPhraseCatalog.INSTANCE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpResponse newHttpResponse(ProtocolVersion ver, int status, HttpContext context)
/*     */   {
/*  86 */     if (ver == null) {
/*  87 */       throw new IllegalArgumentException("HTTP version may not be null");
/*     */     }
/*  89 */     Locale loc = determineLocale(context);
/*  90 */     String reason = this.reasonCatalog.getReason(status, loc);
/*  91 */     StatusLine statusline = new BasicStatusLine(ver, status, reason);
/*  92 */     return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpResponse newHttpResponse(StatusLine statusline, HttpContext context)
/*     */   {
/*  99 */     if (statusline == null) {
/* 100 */       throw new IllegalArgumentException("Status line may not be null");
/*     */     }
/* 102 */     Locale loc = determineLocale(context);
/* 103 */     return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
/*     */   }
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
/*     */   protected Locale determineLocale(HttpContext context)
/*     */   {
/* 117 */     return Locale.getDefault();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\DefaultHttpResponseFactory.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */