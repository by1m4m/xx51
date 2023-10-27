package io.netty.util.concurrent;

public abstract interface ThreadProperties
{
  public abstract Thread.State state();
  
  public abstract int priority();
  
  public abstract boolean isInterrupted();
  
  public abstract boolean isDaemon();
  
  public abstract String name();
  
  public abstract long id();
  
  public abstract StackTraceElement[] stackTrace();
  
  public abstract boolean isAlive();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\ThreadProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */