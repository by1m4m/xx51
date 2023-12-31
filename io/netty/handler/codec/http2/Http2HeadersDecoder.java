package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public abstract interface Http2HeadersDecoder
{
  public abstract Http2Headers decodeHeaders(int paramInt, ByteBuf paramByteBuf)
    throws Http2Exception;
  
  public abstract Configuration configuration();
  
  public static abstract interface Configuration
  {
    public abstract void maxHeaderTableSize(long paramLong)
      throws Http2Exception;
    
    public abstract long maxHeaderTableSize();
    
    public abstract void maxHeaderListSize(long paramLong1, long paramLong2)
      throws Http2Exception;
    
    public abstract long maxHeaderListSize();
    
    public abstract long maxHeaderListSizeGoAway();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2HeadersDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */