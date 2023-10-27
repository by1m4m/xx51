package org.apache.http;

import org.apache.http.protocol.HttpContext;

public abstract interface ConnectionReuseStrategy
{
  public abstract boolean keepAlive(HttpResponse paramHttpResponse, HttpContext paramHttpContext);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\ConnectionReuseStrategy.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */