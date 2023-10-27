package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public abstract interface HttpRequestHandler
{
  public abstract void handle(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\HttpRequestHandler.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */