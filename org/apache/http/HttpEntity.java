package org.apache.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract interface HttpEntity
{
  public abstract boolean isRepeatable();
  
  public abstract boolean isChunked();
  
  public abstract long getContentLength();
  
  public abstract Header getContentType();
  
  public abstract Header getContentEncoding();
  
  public abstract InputStream getContent()
    throws IOException, IllegalStateException;
  
  public abstract void writeTo(OutputStream paramOutputStream)
    throws IOException;
  
  public abstract boolean isStreaming();
  
  public abstract void consumeContent()
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpEntity.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */