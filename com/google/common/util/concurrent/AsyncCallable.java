package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@Beta
@GwtCompatible
public abstract interface AsyncCallable<V>
{
  public abstract ListenableFuture<V> call()
    throws Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AsyncCallable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */