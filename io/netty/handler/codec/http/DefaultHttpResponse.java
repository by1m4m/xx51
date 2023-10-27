/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class DefaultHttpResponse
/*     */   extends DefaultHttpMessage
/*     */   implements HttpResponse
/*     */ {
/*     */   private HttpResponseStatus status;
/*     */   
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status)
/*     */   {
/*  34 */     this(version, status, true, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders)
/*     */   {
/*  45 */     this(version, status, validateHeaders, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleFieldHeaders)
/*     */   {
/*  62 */     super(version, validateHeaders, singleFieldHeaders);
/*  63 */     this.status = ((HttpResponseStatus)ObjectUtil.checkNotNull(status, "status"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, HttpHeaders headers)
/*     */   {
/*  74 */     super(version, headers);
/*  75 */     this.status = ((HttpResponseStatus)ObjectUtil.checkNotNull(status, "status"));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public HttpResponseStatus getStatus()
/*     */   {
/*  81 */     return status();
/*     */   }
/*     */   
/*     */   public HttpResponseStatus status()
/*     */   {
/*  86 */     return this.status;
/*     */   }
/*     */   
/*     */   public HttpResponse setStatus(HttpResponseStatus status)
/*     */   {
/*  91 */     if (status == null) {
/*  92 */       throw new NullPointerException("status");
/*     */     }
/*  94 */     this.status = status;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public HttpResponse setProtocolVersion(HttpVersion version)
/*     */   {
/* 100 */     super.setProtocolVersion(version);
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 106 */     return HttpMessageUtil.appendResponse(new StringBuilder(256), this).toString();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 111 */     int result = 1;
/* 112 */     result = 31 * result + this.status.hashCode();
/* 113 */     result = 31 * result + super.hashCode();
/* 114 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 119 */     if (!(o instanceof DefaultHttpResponse)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     DefaultHttpResponse other = (DefaultHttpResponse)o;
/*     */     
/* 125 */     return (this.status.equals(other.status())) && (super.equals(o));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\DefaultHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */