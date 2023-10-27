package io.netty.channel;

import java.util.Map.Entry;

public abstract interface MaxBytesRecvByteBufAllocator
  extends RecvByteBufAllocator
{
  public abstract int maxBytesPerRead();
  
  public abstract MaxBytesRecvByteBufAllocator maxBytesPerRead(int paramInt);
  
  public abstract int maxBytesPerIndividualRead();
  
  public abstract MaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(int paramInt);
  
  public abstract Map.Entry<Integer, Integer> maxBytesPerReadPair();
  
  public abstract MaxBytesRecvByteBufAllocator maxBytesPerReadPair(int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\MaxBytesRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */