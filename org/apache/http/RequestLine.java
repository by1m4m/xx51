package org.apache.http;

public abstract interface RequestLine
{
  public abstract String getMethod();
  
  public abstract ProtocolVersion getProtocolVersion();
  
  public abstract String getUri();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\RequestLine.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */