package io.netty.channel.unix;

import io.netty.channel.socket.DuplexChannel;

public abstract interface DomainSocketChannel
  extends UnixChannel, DuplexChannel
{
  public abstract DomainSocketAddress remoteAddress();
  
  public abstract DomainSocketAddress localAddress();
  
  public abstract DomainSocketChannelConfig config();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\unix\DomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */