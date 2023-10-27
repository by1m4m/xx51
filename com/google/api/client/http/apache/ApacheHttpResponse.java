/*     */ package com.google.api.client.http.apache;
/*     */ 
/*     */ import com.google.api.client.http.LowLevelHttpResponse;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
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
/*     */ final class ApacheHttpResponse
/*     */   extends LowLevelHttpResponse
/*     */ {
/*     */   private final HttpRequestBase request;
/*     */   private final HttpResponse response;
/*     */   private final Header[] allHeaders;
/*     */   
/*     */   ApacheHttpResponse(HttpRequestBase request, HttpResponse response)
/*     */   {
/*  35 */     this.request = request;
/*  36 */     this.response = response;
/*  37 */     this.allHeaders = response.getAllHeaders();
/*     */   }
/*     */   
/*     */   public int getStatusCode()
/*     */   {
/*  42 */     StatusLine statusLine = this.response.getStatusLine();
/*  43 */     return statusLine == null ? 0 : statusLine.getStatusCode();
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException
/*     */   {
/*  48 */     HttpEntity entity = this.response.getEntity();
/*  49 */     return entity == null ? null : entity.getContent();
/*     */   }
/*     */   
/*     */   public String getContentEncoding()
/*     */   {
/*  54 */     HttpEntity entity = this.response.getEntity();
/*  55 */     if (entity != null) {
/*  56 */       Header contentEncodingHeader = entity.getContentEncoding();
/*  57 */       if (contentEncodingHeader != null) {
/*  58 */         return contentEncodingHeader.getValue();
/*     */       }
/*     */     }
/*  61 */     return null;
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/*  66 */     HttpEntity entity = this.response.getEntity();
/*  67 */     return entity == null ? -1L : entity.getContentLength();
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/*  72 */     HttpEntity entity = this.response.getEntity();
/*  73 */     if (entity != null) {
/*  74 */       Header contentTypeHeader = entity.getContentType();
/*  75 */       if (contentTypeHeader != null) {
/*  76 */         return contentTypeHeader.getValue();
/*     */       }
/*     */     }
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public String getReasonPhrase()
/*     */   {
/*  84 */     StatusLine statusLine = this.response.getStatusLine();
/*  85 */     return statusLine == null ? null : statusLine.getReasonPhrase();
/*     */   }
/*     */   
/*     */   public String getStatusLine()
/*     */   {
/*  90 */     StatusLine statusLine = this.response.getStatusLine();
/*  91 */     return statusLine == null ? null : statusLine.toString();
/*     */   }
/*     */   
/*     */   public String getHeaderValue(String name) {
/*  95 */     return this.response.getLastHeader(name).getValue();
/*     */   }
/*     */   
/*     */   public int getHeaderCount()
/*     */   {
/* 100 */     return this.allHeaders.length;
/*     */   }
/*     */   
/*     */   public String getHeaderName(int index)
/*     */   {
/* 105 */     return this.allHeaders[index].getName();
/*     */   }
/*     */   
/*     */   public String getHeaderValue(int index)
/*     */   {
/* 110 */     return this.allHeaders[index].getValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 120 */     this.request.abort();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\apache\ApacheHttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */