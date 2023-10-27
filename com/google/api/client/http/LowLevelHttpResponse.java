package com.google.api.client.http;

import java.io.IOException;
import java.io.InputStream;

public abstract class LowLevelHttpResponse
{
  public abstract InputStream getContent()
    throws IOException;
  
  public abstract String getContentEncoding()
    throws IOException;
  
  public abstract long getContentLength()
    throws IOException;
  
  public abstract String getContentType()
    throws IOException;
  
  public abstract String getStatusLine()
    throws IOException;
  
  public abstract int getStatusCode()
    throws IOException;
  
  public abstract String getReasonPhrase()
    throws IOException;
  
  public abstract int getHeaderCount()
    throws IOException;
  
  public abstract String getHeaderName(int paramInt)
    throws IOException;
  
  public abstract String getHeaderValue(int paramInt)
    throws IOException;
  
  public void disconnect()
    throws IOException
  {}
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\LowLevelHttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */