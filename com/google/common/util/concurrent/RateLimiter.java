/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ public abstract class RateLimiter
/*     */ {
/*     */   private final SleepingStopwatch stopwatch;
/*     */   private volatile Object mutexDoNotUseDirectly;
/*     */   
/*     */   public static RateLimiter create(double permitsPerSecond)
/*     */   {
/* 128 */     return create(permitsPerSecond, SleepingStopwatch.createFromSystemTimer());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(double permitsPerSecond, SleepingStopwatch stopwatch) {
/* 133 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
/* 134 */     rateLimiter.setRate(permitsPerSecond);
/* 135 */     return rateLimiter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit)
/*     */   {
/* 163 */     Preconditions.checkArgument(warmupPeriod >= 0L, "warmupPeriod must not be negative: %s", warmupPeriod);
/* 164 */     return create(permitsPerSecond, warmupPeriod, unit, 3.0D, 
/* 165 */       SleepingStopwatch.createFromSystemTimer());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor, SleepingStopwatch stopwatch)
/*     */   {
/* 175 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
/* 176 */     rateLimiter.setRate(permitsPerSecond);
/* 177 */     return rateLimiter;
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
/*     */   private Object mutex()
/*     */   {
/* 190 */     Object mutex = this.mutexDoNotUseDirectly;
/* 191 */     if (mutex == null) {
/* 192 */       synchronized (this) {
/* 193 */         mutex = this.mutexDoNotUseDirectly;
/* 194 */         if (mutex == null) {
/* 195 */           this.mutexDoNotUseDirectly = (mutex = new Object());
/*     */         }
/*     */       }
/*     */     }
/* 199 */     return mutex;
/*     */   }
/*     */   
/*     */   RateLimiter(SleepingStopwatch stopwatch) {
/* 203 */     this.stopwatch = ((SleepingStopwatch)Preconditions.checkNotNull(stopwatch));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setRate(double permitsPerSecond)
/*     */   {
/* 225 */     Preconditions.checkArgument((permitsPerSecond > 0.0D) && 
/* 226 */       (!Double.isNaN(permitsPerSecond)), "rate must be positive");
/* 227 */     synchronized (mutex()) {
/* 228 */       doSetRate(permitsPerSecond, this.stopwatch.readMicros());
/*     */     }
/*     */   }
/*     */   
/*     */   abstract void doSetRate(double paramDouble, long paramLong);
/*     */   
/*     */   /* Error */
/*     */   public final double getRate()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 22	com/google/common/util/concurrent/RateLimiter:mutex	()Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 25	com/google/common/util/concurrent/RateLimiter:doGetRate	()D
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: dreturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #241	-> byte code offset #0
/*     */     //   Java source line #242	-> byte code offset #7
/*     */     //   Java source line #243	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	RateLimiter
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   abstract double doGetRate();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire()
/*     */   {
/* 259 */     return acquire(1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire(int permits)
/*     */   {
/* 273 */     long microsToWait = reserve(permits);
/* 274 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 275 */     return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   final long reserve(int permits)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: iload_1
/*     */     //   1: invokestatic 31	com/google/common/util/concurrent/RateLimiter:checkPermits	(I)V
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 22	com/google/common/util/concurrent/RateLimiter:mutex	()Ljava/lang/Object;
/*     */     //   8: dup
/*     */     //   9: astore_2
/*     */     //   10: monitorenter
/*     */     //   11: aload_0
/*     */     //   12: iload_1
/*     */     //   13: aload_0
/*     */     //   14: getfield 18	com/google/common/util/concurrent/RateLimiter:stopwatch	Lcom/google/common/util/concurrent/RateLimiter$SleepingStopwatch;
/*     */     //   17: invokevirtual 23	com/google/common/util/concurrent/RateLimiter$SleepingStopwatch:readMicros	()J
/*     */     //   20: invokevirtual 32	com/google/common/util/concurrent/RateLimiter:reserveAndGetWaitLength	(IJ)J
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: lreturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #285	-> byte code offset #0
/*     */     //   Java source line #286	-> byte code offset #4
/*     */     //   Java source line #287	-> byte code offset #11
/*     */     //   Java source line #288	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	RateLimiter
/*     */     //   0	31	1	permits	int
/*     */     //   9	19	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   public boolean tryAcquire(long timeout, TimeUnit unit)
/*     */   {
/* 304 */     return tryAcquire(1, timeout, unit);
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
/*     */   public boolean tryAcquire(int permits)
/*     */   {
/* 318 */     return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire()
/*     */   {
/* 331 */     return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
/*     */   {
/* 346 */     long timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
/* 347 */     checkPermits(permits);
/*     */     long microsToWait;
/* 349 */     synchronized (mutex()) {
/* 350 */       long nowMicros = this.stopwatch.readMicros();
/* 351 */       if (!canAcquire(nowMicros, timeoutMicros)) {
/* 352 */         return false;
/*     */       }
/* 354 */       microsToWait = reserveAndGetWaitLength(permits, nowMicros);
/*     */     }
/*     */     long microsToWait;
/* 357 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 358 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canAcquire(long nowMicros, long timeoutMicros) {
/* 362 */     return queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final long reserveAndGetWaitLength(int permits, long nowMicros)
/*     */   {
/* 371 */     long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
/* 372 */     return Math.max(momentAvailable - nowMicros, 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract long queryEarliestAvailable(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract long reserveEarliestAvailable(int paramInt, long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 394 */     return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static abstract class SleepingStopwatch
/*     */   {
/*     */     protected abstract long readMicros();
/*     */     
/*     */ 
/*     */ 
/*     */     protected abstract void sleepMicrosUninterruptibly(long paramLong);
/*     */     
/*     */ 
/*     */ 
/*     */     public static SleepingStopwatch createFromSystemTimer()
/*     */     {
/* 411 */       new SleepingStopwatch() {
/* 412 */         final Stopwatch stopwatch = Stopwatch.createStarted();
/*     */         
/*     */         protected long readMicros()
/*     */         {
/* 416 */           return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
/*     */         }
/*     */         
/*     */         protected void sleepMicrosUninterruptibly(long micros)
/*     */         {
/* 421 */           if (micros > 0L) {
/* 422 */             Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkPermits(int permits) {
/* 430 */     Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\RateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */