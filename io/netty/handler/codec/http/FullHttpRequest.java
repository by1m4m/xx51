package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public abstract interface FullHttpRequest
  extends HttpRequest, FullHttpMessage
{
  public abstract FullHttpRequest copy();
  
  public abstract FullHttpRequest duplicate();
  
  public abstract FullHttpRequest retainedDuplicate();
  
  public abstract FullHttpRequest replace(ByteBuf paramByteBuf);
  
  public abstract FullHttpRequest retain(int paramInt);
  
  public abstract FullHttpRequest retain();
  
  public abstract FullHttpRequest touch();
  
  public abstract FullHttpRequest touch(Object paramObject);
  
  public abstract FullHttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
  
  public abstract FullHttpRequest setMethod(HttpMethod paramHttpMethod);
  
  public abstract FullHttpRequest setUri(String paramString);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\FullHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */