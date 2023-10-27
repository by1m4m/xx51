package com.google.api.client.http;

import java.io.IOException;

public abstract interface HttpRequestInitializer
{
  public abstract void initialize(HttpRequest paramHttpRequest)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpRequestInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */