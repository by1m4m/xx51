package io.netty.handler.codec.http2;

public abstract interface Http2ResetFrame
  extends Http2StreamFrame
{
  public abstract long errorCode();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2ResetFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */