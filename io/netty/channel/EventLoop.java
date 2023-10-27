package io.netty.channel;

import io.netty.util.concurrent.OrderedEventExecutor;

public abstract interface EventLoop
  extends OrderedEventExecutor, EventLoopGroup
{
  public abstract EventLoopGroup parent();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\EventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */