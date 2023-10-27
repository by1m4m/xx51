package io.netty.util;

public abstract interface ResourceLeakTracker<T>
{
  public abstract void record();
  
  public abstract void record(Object paramObject);
  
  public abstract boolean close(T paramT);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\ResourceLeakTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */