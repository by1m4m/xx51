package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract interface ListenableFuture<V>
  extends Future<V>
{
  public abstract void addListener(Runnable paramRunnable, Executor paramExecutor);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\ListenableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */