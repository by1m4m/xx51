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
/*     */ @Beta
/*     */ public class HttpBackOffIOExceptionHandler
/*     */   implements HttpIOExceptionHandler
/*     */ {
/*     */   private final BackOff backOff;
/*  62 */   private Sleeper sleeper = Sleeper.DEFAULT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpBackOffIOExceptionHandler(BackOff backOff)
/*     */   {
/*  70 */     this.backOff = ((BackOff)Preconditions.checkNotNull(backOff));
/*     */   }
/*     */   
/*     */   public final BackOff getBackOff()
/*     */   {
/*  75 */     return this.backOff;
/*     */   }
/*     */   
/*     */   public final Sleeper getSleeper()
/*     */   {
/*  80 */     return this.sleeper;
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
/*     */   public HttpBackOffIOExceptionHandler setSleeper(Sleeper sleeper)
/*     */   {
/*  96 */     this.sleeper = ((Sleeper)Preconditions.checkNotNull(sleeper));
/*  97 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean handleIOException(HttpRequest request, boolean supportsRetry)
/*     */     throws IOException
/*     */   {
/* 109 */     if (!supportsRetry) {
/* 110 */       return false;
/*     */     }
/*     */     try {
/* 113 */       return BackOffUtils.next(this.sleeper, this.backOff);
/*     */     } catch (InterruptedException exception) {}
/* 115 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpBackOffIOExceptionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */