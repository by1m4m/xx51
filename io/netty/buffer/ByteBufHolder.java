package io.netty.buffer;

import io.netty.util.ReferenceCounted;

public abstract interface ByteBufHolder
  extends ReferenceCounted
{
  public abstract ByteBuf content();
  
  public abstract ByteBufHolder copy();
  
  public abstract ByteBufHolder duplicate();
  
  public abstract ByteBufHolder retainedDuplicate();
  
  public abstract ByteBufHolder replace(ByteBuf paramByteBuf);
  
  public abstract ByteBufHolder retain();
  
  public abstract ByteBufHolder retain(int paramInt);
  
  public abstract ByteBufHolder touch();
  
  public abstract ByteBufHolder touch(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\ByteBufHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */