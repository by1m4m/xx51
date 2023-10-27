package io.netty.buffer;

public abstract interface ByteBufAllocatorMetric
{
  public abstract long usedHeapMemory();
  
  public abstract long usedDirectMemory();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\ByteBufAllocatorMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */