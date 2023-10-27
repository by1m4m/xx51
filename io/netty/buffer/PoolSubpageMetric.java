package io.netty.buffer;

public abstract interface PoolSubpageMetric
{
  public abstract int maxNumElements();
  
  public abstract int numAvailable();
  
  public abstract int elementSize();
  
  public abstract int pageSize();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\PoolSubpageMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */