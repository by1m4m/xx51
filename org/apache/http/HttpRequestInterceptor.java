package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public abstract interface HttpRequestInterceptor
{
  public abstract void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpRequestInterceptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */