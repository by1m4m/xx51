package com.google.api.client.util;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface StreamingContent
{
  public abstract void writeTo(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\StreamingContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */