package io.netty.handler.codec.http;

public abstract interface HttpMessage
  extends HttpObject
{
  @Deprecated
  public abstract HttpVersion getProtocolVersion();
  
  public abstract HttpVersion protocolVersion();
  
  public abstract HttpMessage setProtocolVersion(HttpVersion paramHttpVersion);
  
  public abstract HttpHeaders headers();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */