package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public abstract interface StompContentSubframe
  extends ByteBufHolder, StompSubframe
{
  public abstract StompContentSubframe copy();
  
  public abstract StompContentSubframe duplicate();
  
  public abstract StompContentSubframe retainedDuplicate();
  
  public abstract StompContentSubframe replace(ByteBuf paramByteBuf);
  
  public abstract StompContentSubframe retain();
  
  public abstract StompContentSubframe retain(int paramInt);
  
  public abstract StompContentSubframe touch();
  
  public abstract StompContentSubframe touch(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\stomp\StompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */