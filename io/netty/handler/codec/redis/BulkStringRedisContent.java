package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public abstract interface BulkStringRedisContent
  extends RedisMessage, ByteBufHolder
{
  public abstract BulkStringRedisContent copy();
  
  public abstract BulkStringRedisContent duplicate();
  
  public abstract BulkStringRedisContent retainedDuplicate();
  
  public abstract BulkStringRedisContent replace(ByteBuf paramByteBuf);
  
  public abstract BulkStringRedisContent retain();
  
  public abstract BulkStringRedisContent retain(int paramInt);
  
  public abstract BulkStringRedisContent touch();
  
  public abstract BulkStringRedisContent touch(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\redis\BulkStringRedisContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */