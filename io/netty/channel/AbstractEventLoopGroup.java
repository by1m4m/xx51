package io.netty.channel;

import io.netty.util.concurrent.AbstractEventExecutorGroup;

public abstract class AbstractEventLoopGroup
  extends AbstractEventExecutorGroup
  implements EventLoopGroup
{
  public abstract EventLoop next();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\AbstractEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */