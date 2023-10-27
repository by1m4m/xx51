package com.google.api.client.http;

import java.io.IOException;

public abstract interface HttpResponseInterceptor
{
  public abstract void interceptResponse(HttpResponse paramHttpResponse)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpResponseInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */