package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsQuestion;

public abstract interface DnsQueryLifecycleObserverFactory
{
  public abstract DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion paramDnsQuestion);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsQueryLifecycleObserverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */