package io.netty.channel.pool;

import io.netty.channel.Channel;

public abstract interface ChannelPoolHandler
{
  public abstract void channelReleased(Channel paramChannel)
    throws Exception;
  
  public abstract void channelAcquired(Channel paramChannel)
    throws Exception;
  
  public abstract void channelCreated(Channel paramChannel)
    throws Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\pool\ChannelPoolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */