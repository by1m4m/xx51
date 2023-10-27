package io.netty.handler.codec.http2;

public abstract interface Http2WindowUpdateFrame
  extends Http2StreamFrame
{
  public abstract int windowSizeIncrement();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2WindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */