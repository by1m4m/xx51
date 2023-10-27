package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public abstract interface Http2FlowController
{
  public abstract void channelHandlerContext(ChannelHandlerContext paramChannelHandlerContext)
    throws Http2Exception;
  
  public abstract void initialWindowSize(int paramInt)
    throws Http2Exception;
  
  public abstract int initialWindowSize();
  
  public abstract int windowSize(Http2Stream paramHttp2Stream);
  
  public abstract void incrementWindowSize(Http2Stream paramHttp2Stream, int paramInt)
    throws Http2Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2FlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */