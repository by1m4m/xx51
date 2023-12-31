package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
@GwtIncompatible
public abstract interface TimeLimiter
{
  public abstract <T> T newProxy(T paramT, Class<T> paramClass, long paramLong, TimeUnit paramTimeUnit);
  
  @CanIgnoreReturnValue
  public abstract <T> T callWithTimeout(Callable<T> paramCallable, long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, InterruptedException, ExecutionException;
  
  @CanIgnoreReturnValue
  public abstract <T> T callUninterruptiblyWithTimeout(Callable<T> paramCallable, long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, ExecutionException;
  
  public abstract void runWithTimeout(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, InterruptedException;
  
  public abstract void runUninterruptiblyWithTimeout(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\TimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */