package org.apache.http.protocol;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public abstract interface HttpExpectationVerifier
{
  public abstract void verify(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\HttpExpectationVerifier.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */