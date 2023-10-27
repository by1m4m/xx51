/*     */ package org.eclipse.jetty.client.api;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Result
/*     */ {
/*     */   private final Request request;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Throwable requestFailure;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Response response;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Throwable responseFailure;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Result(Request request, Response response)
/*     */   {
/*  34 */     this(request, null, response, null);
/*     */   }
/*     */   
/*     */   public Result(Request request, Response response, Throwable responseFailure)
/*     */   {
/*  39 */     this(request, null, response, responseFailure);
/*     */   }
/*     */   
/*     */   public Result(Request request, Throwable requestFailure, Response response)
/*     */   {
/*  44 */     this(request, requestFailure, response, null);
/*     */   }
/*     */   
/*     */   public Result(Request request, Throwable requestFailure, Response response, Throwable responseFailure)
/*     */   {
/*  49 */     this.request = request;
/*  50 */     this.requestFailure = requestFailure;
/*  51 */     this.response = response;
/*  52 */     this.responseFailure = responseFailure;
/*     */   }
/*     */   
/*     */   public Result(Result result, Throwable responseFailure)
/*     */   {
/*  57 */     this.request = result.request;
/*  58 */     this.requestFailure = result.requestFailure;
/*  59 */     this.response = result.response;
/*  60 */     this.responseFailure = responseFailure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Request getRequest()
/*     */   {
/*  68 */     return this.request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getRequestFailure()
/*     */   {
/*  76 */     return this.requestFailure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Response getResponse()
/*     */   {
/*  84 */     return this.response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getResponseFailure()
/*     */   {
/*  92 */     return this.responseFailure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSucceeded()
/*     */   {
/* 100 */     return getFailure() == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFailed()
/*     */   {
/* 108 */     return !isSucceeded();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getFailure()
/*     */   {
/* 116 */     return this.responseFailure != null ? this.responseFailure : this.requestFailure;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 122 */     return String.format("%s[%s > %s] %s", new Object[] {Result.class
/* 123 */       .getSimpleName(), this.request, this.response, 
/*     */       
/*     */ 
/* 126 */       getFailure() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\api\Result.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */