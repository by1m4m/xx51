package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public abstract interface HttpResponseInterceptor
{
  public abstract void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpResponseInterceptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */