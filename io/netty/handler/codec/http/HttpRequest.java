package io.netty.handler.codec.http;

public abstract interface HttpRequest
  extends HttpMessage
{
  @Deprecated
  public abstract HttpMethod getMethod();
  
  public abstract HttpMethod method();
  
  public abstract HttpRequest setMethod(HttpMethod paramHttpMethod);
  
  @Deprecated
  public abstract String getUri();
  
  public abstract String uri();
  
  public abstract HttpRequest setUri(String paramString);
  
  public abstract HttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */