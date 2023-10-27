/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class HttpTransport
/*     */ {
/*  81 */   static final Logger LOGGER = Logger.getLogger(HttpTransport.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private static final String[] SUPPORTED_METHODS = { "DELETE", "GET", "POST", "PUT" };
/*     */   
/*     */   static {
/*  90 */     Arrays.sort(SUPPORTED_METHODS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final HttpRequestFactory createRequestFactory()
/*     */   {
/* 100 */     return createRequestFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final HttpRequestFactory createRequestFactory(HttpRequestInitializer initializer)
/*     */   {
/* 112 */     return new HttpRequestFactory(this, initializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   HttpRequest buildRequest()
/*     */   {
/* 121 */     return new HttpRequest(this, null);
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
/*     */   public boolean supportsMethod(String method)
/*     */     throws IOException
/*     */   {
/* 137 */     return Arrays.binarySearch(SUPPORTED_METHODS, method) >= 0;
/*     */   }
/*     */   
/*     */   protected abstract LowLevelHttpRequest buildRequest(String paramString1, String paramString2)
/*     */     throws IOException;
/*     */   
/*     */   public void shutdown()
/*     */     throws IOException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpTransport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */