package io.netty.util.concurrent;

public abstract interface RejectedExecutionHandler
{
  public abstract void rejected(Runnable paramRunnable, SingleThreadEventExecutor paramSingleThreadEventExecutor);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\RejectedExecutionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */