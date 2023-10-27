package org.apache.http;

import org.apache.http.protocol.HttpContext;

public abstract interface HttpResponseFactory
{
  public abstract HttpResponse newHttpResponse(ProtocolVersion paramProtocolVersion, int paramInt, HttpContext paramHttpContext);
  
  public abstract HttpResponse newHttpResponse(StatusLine paramStatusLine, HttpContext paramHttpContext);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpResponseFactory.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */