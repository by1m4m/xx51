package io.netty.channel.unix;

import io.netty.channel.Channel;

public abstract interface UnixChannel
  extends Channel
{
  public abstract FileDescriptor fd();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\unix\UnixChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */