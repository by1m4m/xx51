package io.netty.util.concurrent;

public abstract interface EventExecutorChooserFactory
{
  public abstract EventExecutorChooser newChooser(EventExecutor[] paramArrayOfEventExecutor);
  
  public static abstract interface EventExecutorChooser
  {
    public abstract EventExecutor next();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\EventExecutorChooserFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */