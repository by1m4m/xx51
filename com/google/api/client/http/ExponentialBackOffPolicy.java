/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.ExponentialBackOff;
/*     */ import com.google.api.client.util.ExponentialBackOff.Builder;
/*     */ import com.google.api.client.util.NanoClock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Beta
/*     */ public class ExponentialBackOffPolicy
/*     */   implements BackOffPolicy
/*     */ {
/*     */   public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
/*     */   public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5D;
/*     */   public static final double DEFAULT_MULTIPLIER = 1.5D;
/*     */   public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
/*     */   public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
/*     */   private final ExponentialBackOff exponentialBackOff;
/*     */   
/*     */   public ExponentialBackOffPolicy()
/*     */   {
/* 130 */     this(new Builder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ExponentialBackOffPolicy(Builder builder)
/*     */   {
/* 139 */     this.exponentialBackOff = builder.exponentialBackOffBuilder.build();
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
/*     */   public boolean isBackOffRequired(int statusCode)
/*     */   {
/* 156 */     switch (statusCode) {
/*     */     case 500: 
/*     */     case 503: 
/* 159 */       return true;
/*     */     }
/* 161 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 169 */     this.exponentialBackOff.reset();
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
/*     */ 
/*     */   public long getNextBackOffMillis()
/*     */     throws IOException
/*     */   {
/* 189 */     return this.exponentialBackOff.nextBackOffMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getInitialIntervalMillis()
/*     */   {
/* 196 */     return this.exponentialBackOff.getInitialIntervalMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final double getRandomizationFactor()
/*     */   {
/* 208 */     return this.exponentialBackOff.getRandomizationFactor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getCurrentIntervalMillis()
/*     */   {
/* 215 */     return this.exponentialBackOff.getCurrentIntervalMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final double getMultiplier()
/*     */   {
/* 222 */     return this.exponentialBackOff.getMultiplier();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getMaxIntervalMillis()
/*     */   {
/* 230 */     return this.exponentialBackOff.getMaxIntervalMillis();
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
/*     */   public final int getMaxElapsedTimeMillis()
/*     */   {
/* 243 */     return this.exponentialBackOff.getMaxElapsedTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long getElapsedTimeMillis()
/*     */   {
/* 255 */     return this.exponentialBackOff.getElapsedTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Builder builder()
/*     */   {
/* 262 */     return new Builder();
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static class Builder
/*     */   {
/* 280 */     final ExponentialBackOff.Builder exponentialBackOffBuilder = new ExponentialBackOff.Builder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ExponentialBackOffPolicy build()
/*     */     {
/* 287 */       return new ExponentialBackOffPolicy(this);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final int getInitialIntervalMillis()
/*     */     {
/* 295 */       return this.exponentialBackOffBuilder.getInitialIntervalMillis();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setInitialIntervalMillis(int initialIntervalMillis)
/*     */     {
/* 308 */       this.exponentialBackOffBuilder.setInitialIntervalMillis(initialIntervalMillis);
/* 309 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final double getRandomizationFactor()
/*     */     {
/* 327 */       return this.exponentialBackOffBuilder.getRandomizationFactor();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setRandomizationFactor(double randomizationFactor)
/*     */     {
/* 346 */       this.exponentialBackOffBuilder.setRandomizationFactor(randomizationFactor);
/* 347 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final double getMultiplier()
/*     */     {
/* 355 */       return this.exponentialBackOffBuilder.getMultiplier();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setMultiplier(double multiplier)
/*     */     {
/* 368 */       this.exponentialBackOffBuilder.setMultiplier(multiplier);
/* 369 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final int getMaxIntervalMillis()
/*     */     {
/* 378 */       return this.exponentialBackOffBuilder.getMaxIntervalMillis();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setMaxIntervalMillis(int maxIntervalMillis)
/*     */     {
/* 392 */       this.exponentialBackOffBuilder.setMaxIntervalMillis(maxIntervalMillis);
/* 393 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final int getMaxElapsedTimeMillis()
/*     */     {
/* 407 */       return this.exponentialBackOffBuilder.getMaxElapsedTimeMillis();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setMaxElapsedTimeMillis(int maxElapsedTimeMillis)
/*     */     {
/* 426 */       this.exponentialBackOffBuilder.setMaxElapsedTimeMillis(maxElapsedTimeMillis);
/* 427 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final NanoClock getNanoClock()
/*     */     {
/* 436 */       return this.exponentialBackOffBuilder.getNanoClock();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setNanoClock(NanoClock nanoClock)
/*     */     {
/* 450 */       this.exponentialBackOffBuilder.setNanoClock(nanoClock);
/* 451 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\ExponentialBackOffPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */