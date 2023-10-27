package org.eclipse.jetty.util.thread;

public abstract interface ExecutionStrategy
{
  public abstract void dispatch();
  
  public abstract void produce();
  
  public static abstract interface Producer
  {
    public abstract Runnable produce();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\ExecutionStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */