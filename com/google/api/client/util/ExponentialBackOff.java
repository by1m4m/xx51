/*     */ package com.google.api.client.util;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExponentialBackOff
/*     */   implements BackOff
/*     */ {
/*     */   public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
/*     */   public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5D;
/*     */   public static final double DEFAULT_MULTIPLIER = 1.5D;
/*     */   public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
/*     */   public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
/*     */   private int currentIntervalMillis;
/*     */   private final int initialIntervalMillis;
/*     */   private final double randomizationFactor;
/*     */   private final double multiplier;
/*     */   private final int maxIntervalMillis;
/*     */   long startTimeNanos;
/*     */   private final int maxElapsedTimeMillis;
/*     */   private final NanoClock nanoClock;
/*     */   
/*     */   public ExponentialBackOff()
/*     */   {
/* 153 */     this(new Builder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ExponentialBackOff(Builder builder)
/*     */   {
/* 160 */     this.initialIntervalMillis = builder.initialIntervalMillis;
/* 161 */     this.randomizationFactor = builder.randomizationFactor;
/* 162 */     this.multiplier = builder.multiplier;
/* 163 */     this.maxIntervalMillis = builder.maxIntervalMillis;
/* 164 */     this.maxElapsedTimeMillis = builder.maxElapsedTimeMillis;
/* 165 */     this.nanoClock = builder.nanoClock;
/* 166 */     Preconditions.checkArgument(this.initialIntervalMillis > 0);
/* 167 */     Preconditions.checkArgument((0.0D <= this.randomizationFactor) && (this.randomizationFactor < 1.0D));
/* 168 */     Preconditions.checkArgument(this.multiplier >= 1.0D);
/* 169 */     Preconditions.checkArgument(this.maxIntervalMillis >= this.initialIntervalMillis);
/* 170 */     Preconditions.checkArgument(this.maxElapsedTimeMillis > 0);
/* 171 */     reset();
/*     */   }
/*     */   
/*     */   public final void reset()
/*     */   {
/* 176 */     this.currentIntervalMillis = this.initialIntervalMillis;
/* 177 */     this.startTimeNanos = this.nanoClock.nanoTime();
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
/*     */   public long nextBackOffMillis()
/*     */     throws IOException
/*     */   {
/* 194 */     if (getElapsedTimeMillis() > this.maxElapsedTimeMillis) {
/* 195 */       return -1L;
/*     */     }
/* 197 */     int randomizedInterval = getRandomValueFromInterval(this.randomizationFactor, Math.random(), this.currentIntervalMillis);
/*     */     
/* 199 */     incrementCurrentInterval();
/* 200 */     return randomizedInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int getRandomValueFromInterval(double randomizationFactor, double random, int currentIntervalMillis)
/*     */   {
/* 209 */     double delta = randomizationFactor * currentIntervalMillis;
/* 210 */     double minInterval = currentIntervalMillis - delta;
/* 211 */     double maxInterval = currentIntervalMillis + delta;
/*     */     
/*     */ 
/*     */ 
/* 215 */     int randomValue = (int)(minInterval + random * (maxInterval - minInterval + 1.0D));
/* 216 */     return randomValue;
/*     */   }
/*     */   
/*     */   public final int getInitialIntervalMillis()
/*     */   {
/* 221 */     return this.initialIntervalMillis;
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
/* 233 */     return this.randomizationFactor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getCurrentIntervalMillis()
/*     */   {
/* 240 */     return this.currentIntervalMillis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final double getMultiplier()
/*     */   {
/* 247 */     return this.multiplier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getMaxIntervalMillis()
/*     */   {
/* 255 */     return this.maxIntervalMillis;
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
/* 268 */     return this.maxElapsedTimeMillis;
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
/* 280 */     return (this.nanoClock.nanoTime() - this.startTimeNanos) / 1000000L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void incrementCurrentInterval()
/*     */   {
/* 288 */     if (this.currentIntervalMillis >= this.maxIntervalMillis / this.multiplier) {
/* 289 */       this.currentIntervalMillis = this.maxIntervalMillis;
/*     */     } else {
/* 291 */       this.currentIntervalMillis = ((int)(this.currentIntervalMillis * this.multiplier));
/*     */     }
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
/*     */   public static class Builder
/*     */   {
/* 305 */     int initialIntervalMillis = 500;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 315 */     double randomizationFactor = 0.5D;
/*     */     
/*     */ 
/* 318 */     double multiplier = 1.5D;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 324 */     int maxIntervalMillis = 60000;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 331 */     int maxElapsedTimeMillis = 900000;
/*     */     
/*     */ 
/* 334 */     NanoClock nanoClock = NanoClock.SYSTEM;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ExponentialBackOff build()
/*     */     {
/* 341 */       return new ExponentialBackOff(this);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final int getInitialIntervalMillis()
/*     */     {
/* 349 */       return this.initialIntervalMillis;
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
/* 362 */       this.initialIntervalMillis = initialIntervalMillis;
/* 363 */       return this;
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
/* 381 */       return this.randomizationFactor;
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
/* 400 */       this.randomizationFactor = randomizationFactor;
/* 401 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final double getMultiplier()
/*     */     {
/* 409 */       return this.multiplier;
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
/* 422 */       this.multiplier = multiplier;
/* 423 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final int getMaxIntervalMillis()
/*     */     {
/* 432 */       return this.maxIntervalMillis;
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
/* 446 */       this.maxIntervalMillis = maxIntervalMillis;
/* 447 */       return this;
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
/* 461 */       return this.maxElapsedTimeMillis;
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
/* 480 */       this.maxElapsedTimeMillis = maxElapsedTimeMillis;
/* 481 */       return this;
/*     */     }
/*     */     
/*     */     public final NanoClock getNanoClock()
/*     */     {
/* 486 */       return this.nanoClock;
/*     */     }
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
/* 498 */       this.nanoClock = ((NanoClock)Preconditions.checkNotNull(nanoClock));
/* 499 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ExponentialBackOff.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */