package com.google.api.client.http;

import com.google.api.client.util.Beta;
import java.io.IOException;

@Beta
public abstract interface HttpIOExceptionHandler
{
  public abstract boolean handleIOException(HttpRequest paramHttpRequest, boolean paramBoolean)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpIOExceptionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */