package io.netty.resolver.dns;

import java.net.InetSocketAddress;

public abstract interface DnsServerAddressStream
{
  public abstract InetSocketAddress next();
  
  public abstract int size();
  
  public abstract DnsServerAddressStream duplicate();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsServerAddressStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */