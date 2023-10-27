package io.netty.handler.codec.http2;

public abstract interface Http2HeadersFrame
  extends Http2StreamFrame
{
  public abstract Http2Headers headers();
  
  public abstract int padding();
  
  public abstract boolean isEndStream();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2HeadersFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */