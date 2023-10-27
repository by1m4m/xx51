package org.eclipse.jetty.client.http;

import org.eclipse.jetty.client.HttpResponse;

public abstract interface HttpConnectionUpgrader
{
  public abstract void upgrade(HttpResponse paramHttpResponse, HttpConnectionOverHTTP paramHttpConnectionOverHTTP);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\http\HttpConnectionUpgrader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */