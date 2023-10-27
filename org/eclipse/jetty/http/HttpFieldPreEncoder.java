package org.eclipse.jetty.http;

public abstract interface HttpFieldPreEncoder
{
  public abstract HttpVersion getHttpVersion();
  
  public abstract byte[] getEncodedField(HttpHeader paramHttpHeader, String paramString1, String paramString2);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpFieldPreEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */