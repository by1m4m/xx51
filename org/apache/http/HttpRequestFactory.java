package org.apache.http;

public abstract interface HttpRequestFactory
{
  public abstract HttpRequest newHttpRequest(RequestLine paramRequestLine)
    throws MethodNotSupportedException;
  
  public abstract HttpRequest newHttpRequest(String paramString1, String paramString2)
    throws MethodNotSupportedException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpRequestFactory.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */