package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public abstract interface Http2LocalFlowController
  extends Http2FlowController
{
  public abstract Http2LocalFlowController frameWriter(Http2FrameWriter paramHttp2FrameWriter);
  
  public abstract void receiveFlowControlledFrame(Http2Stream paramHttp2Stream, ByteBuf paramByteBuf, int paramInt, boolean paramBoolean)
    throws Http2Exception;
  
  public abstract boolean consumeBytes(Http2Stream paramHttp2Stream, int paramInt)
    throws Http2Exception;
  
  public abstract int unconsumedBytes(Http2Stream paramHttp2Stream);
  
  public abstract int initialWindowSize(Http2Stream paramHttp2Stream);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2LocalFlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */