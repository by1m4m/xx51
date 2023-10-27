package com.google.api.client.http;

import com.google.api.client.util.StreamingContent;
import java.io.IOException;
import java.io.OutputStream;

public abstract interface HttpEncoding
{
  public abstract String getName();
  
  public abstract void encode(StreamingContent paramStreamingContent, OutputStream paramOutputStream)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */