package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;

public abstract interface RedisMessagePool
{
  public abstract SimpleStringRedisMessage getSimpleString(String paramString);
  
  public abstract SimpleStringRedisMessage getSimpleString(ByteBuf paramByteBuf);
  
  public abstract ErrorRedisMessage getError(String paramString);
  
  public abstract ErrorRedisMessage getError(ByteBuf paramByteBuf);
  
  public abstract IntegerRedisMessage getInteger(long paramLong);
  
  public abstract IntegerRedisMessage getInteger(ByteBuf paramByteBuf);
  
  public abstract byte[] getByteBufOfInteger(long paramLong);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\redis\RedisMessagePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */