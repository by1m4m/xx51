package com.google.api.client.http;

import java.io.IOException;

public abstract interface HttpExecuteInterceptor
{
  public abstract void intercept(HttpRequest paramHttpRequest)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpExecuteInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */