/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.BackOff;
/*     */ import com.google.api.client.util.BackOffUtils;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Sleeper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public class HttpBackOffUnsuccessfulResponseHandler
/*     */   implements HttpUnsuccessfulResponseHandler
/*     */ {
/*     */   private final BackOff backOff;
/*  63 */   private BackOffRequired backOffRequired = BackOffRequired.ON_SERVER_ERROR;
/*     */   
/*     */ 
/*  66 */   private Sleeper sleeper = Sleeper.DEFAULT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpBackOffUnsuccessfulResponseHandler(BackOff backOff)
/*     */   {
/*  74 */     this.backOff = ((BackOff)Preconditions.checkNotNull(backOff));
/*     */   }
/*     */   
/*     */   public final BackOff getBackOff()
/*     */   {
/*  79 */     return this.backOff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final BackOffRequired getBackOffRequired()
/*     */   {
/*  87 */     return this.backOffRequired;
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
/*     */ 
/*     */   public HttpBackOffUnsuccessfulResponseHandler setBackOffRequired(BackOffRequired backOffRequired)
/*     */   {
/* 105 */     this.backOffRequired = ((BackOffRequired)Preconditions.checkNotNull(backOffRequired));
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public final Sleeper getSleeper()
/*     */   {
/* 111 */     return this.sleeper;
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
/*     */   public HttpBackOffUnsuccessfulResponseHandler setSleeper(Sleeper sleeper)
/*     */   {
/* 127 */     this.sleeper = ((Sleeper)Preconditions.checkNotNull(sleeper));
/* 128 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean handleResponse(HttpRequest request, HttpResponse response, boolean supportsRetry)
/*     */     throws IOException
/*     */   {
/* 141 */     if (!supportsRetry) {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     if (this.backOffRequired.isRequired(response)) {
/*     */       try {
/* 147 */         return BackOffUtils.next(this.sleeper, this.backOff);
/*     */       }
/*     */       catch (InterruptedException exception) {}
/*     */     }
/*     */     
/* 152 */     return false;
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
/*     */ 
/*     */   @Beta
/*     */   public static abstract interface BackOffRequired
/*     */   {
/* 171 */     public static final BackOffRequired ALWAYS = new BackOffRequired() {
/*     */       public boolean isRequired(HttpResponse response) {
/* 173 */         return true;
/*     */       }
/*     */     };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */     public static final BackOffRequired ON_SERVER_ERROR = new BackOffRequired() {
/*     */       public boolean isRequired(HttpResponse response) {
/* 183 */         return response.getStatusCode() / 100 == 5;
/*     */       }
/*     */     };
/*     */     
/*     */     public abstract boolean isRequired(HttpResponse paramHttpResponse);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpBackOffUnsuccessfulResponseHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */