package io.netty.util;

@Deprecated
public abstract interface ResourceLeak
{
  public abstract void record();
  
  public abstract void record(Object paramObject);
  
  public abstract boolean close();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\ResourceLeak.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */