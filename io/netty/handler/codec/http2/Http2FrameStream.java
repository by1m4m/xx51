package io.netty.handler.codec.http2;

public abstract interface Http2FrameStream
{
  public abstract int id();
  
  public abstract Http2Stream.State state();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2FrameStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */