package io.netty.channel.pool;

public abstract interface ChannelPoolMap<K, P extends ChannelPool>
{
  public abstract P get(K paramK);
  
  public abstract boolean contains(K paramK);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\pool\ChannelPoolMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */