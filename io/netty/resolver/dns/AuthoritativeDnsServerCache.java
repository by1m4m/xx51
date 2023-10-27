package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import java.net.InetSocketAddress;

public abstract interface AuthoritativeDnsServerCache
{
  public abstract DnsServerAddressStream get(String paramString);
  
  public abstract void cache(String paramString, InetSocketAddress paramInetSocketAddress, long paramLong, EventLoop paramEventLoop);
  
  public abstract void clear();
  
  public abstract boolean clear(String paramString);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\AuthoritativeDnsServerCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */