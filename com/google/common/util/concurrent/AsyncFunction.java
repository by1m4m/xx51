package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public abstract interface AsyncFunction<I, O>
{
  public abstract ListenableFuture<O> apply(I paramI)
    throws Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\AsyncFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */