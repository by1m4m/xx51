package io.netty.handler.codec.http2;

public abstract interface Http2PingFrame
  extends Http2Frame
{
  public abstract boolean ack();
  
  public abstract long content();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2PingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */