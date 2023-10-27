package com.google.api.client.http;

import com.google.api.client.util.StreamingContent;
import java.io.IOException;
import java.io.OutputStream;

public abstract interface HttpContent
  extends StreamingContent
{
  public abstract long getLength()
    throws IOException;
  
  public abstract String getType();
  
  public abstract boolean retrySupported();
  
  public abstract void writeTo(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */