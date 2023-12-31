package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Deprecated
@Beta
@CanIgnoreReturnValue
@GwtCompatible
public abstract interface CheckedFuture<V, X extends Exception>
  extends ListenableFuture<V>
{
  public abstract V checkedGet()
    throws Exception;
  
  public abstract V checkedGet(long paramLong, TimeUnit paramTimeUnit)
    throws TimeoutException, Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\CheckedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */