package io.netty.buffer;

public abstract interface PoolChunkListMetric
  extends Iterable<PoolChunkMetric>
{
  public abstract int minUsage();
  
  public abstract int maxUsage();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\PoolChunkListMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */