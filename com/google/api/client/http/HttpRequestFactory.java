/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public final class HttpRequestFactory
/*     */ {
/*     */   private final HttpTransport transport;
/*     */   private final HttpRequestInitializer initializer;
/*     */   
/*     */   HttpRequestFactory(HttpTransport transport, HttpRequestInitializer initializer)
/*     */   {
/*  54 */     this.transport = transport;
/*  55 */     this.initializer = initializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpTransport getTransport()
/*     */   {
/*  64 */     return this.transport;
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
/*     */   public HttpRequestInitializer getInitializer()
/*     */   {
/*  77 */     return this.initializer;
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
/*     */   public HttpRequest buildRequest(String requestMethod, GenericUrl url, HttpContent content)
/*     */     throws IOException
/*     */   {
/*  91 */     HttpRequest request = this.transport.buildRequest();
/*  92 */     if (this.initializer != null) {
/*  93 */       this.initializer.initialize(request);
/*     */     }
/*  95 */     request.setRequestMethod(requestMethod);
/*  96 */     if (url != null) {
/*  97 */       request.setUrl(url);
/*     */     }
/*  99 */     if (content != null) {
/* 100 */       request.setContent(content);
/*     */     }
/* 102 */     return request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest buildDeleteRequest(GenericUrl url)
/*     */     throws IOException
/*     */   {
/* 112 */     return buildRequest("DELETE", url, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest buildGetRequest(GenericUrl url)
/*     */     throws IOException
/*     */   {
/* 122 */     return buildRequest("GET", url, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest buildPostRequest(GenericUrl url, HttpContent content)
/*     */     throws IOException
/*     */   {
/* 133 */     return buildRequest("POST", url, content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest buildPutRequest(GenericUrl url, HttpContent content)
/*     */     throws IOException
/*     */   {
/* 144 */     return buildRequest("PUT", url, content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest buildPatchRequest(GenericUrl url, HttpContent content)
/*     */     throws IOException
/*     */   {
/* 155 */     return buildRequest("PATCH", url, content);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequest buildHeadRequest(GenericUrl url)
/*     */     throws IOException
/*     */   {
/* 165 */     return buildRequest("HEAD", url, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpRequestFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */