package io.netty.resolver.dns;

import io.netty.channel.EventLoop;

public abstract interface DnsCnameCache
{
  public abstract String get(String paramString);
  
  public abstract void cache(String paramString1, String paramString2, long paramLong, EventLoop paramEventLoop);
  
  public abstract void clear();
  
  public abstract boolean clear(String paramString);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsCnameCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */