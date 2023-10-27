package io.netty.buffer;

public abstract interface PoolChunkMetric
{
  public abstract int usage();
  
  public abstract int chunkSize();
  
  public abstract int freeBytes();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\PoolChunkMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */