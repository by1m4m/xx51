package org.apache.http;

public abstract interface StatusLine
{
  public abstract ProtocolVersion getProtocolVersion();
  
  public abstract int getStatusCode();
  
  public abstract String getReasonPhrase();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\StatusLine.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */