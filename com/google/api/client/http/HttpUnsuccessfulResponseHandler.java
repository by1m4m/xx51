package com.google.api.client.http;

import java.io.IOException;

public abstract interface HttpUnsuccessfulResponseHandler
{
  public abstract boolean handleResponse(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, boolean paramBoolean)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpUnsuccessfulResponseHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */