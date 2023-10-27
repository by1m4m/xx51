package io.netty.handler.codec.http2;

public abstract interface Http2StreamVisitor
{
  public abstract boolean visit(Http2Stream paramHttp2Stream)
    throws Http2Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2StreamVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */