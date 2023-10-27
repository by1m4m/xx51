package org.apache.http.client;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.protocol.HttpContext;

public abstract interface RedirectHandler
{
  public abstract boolean isRedirectRequested(HttpResponse paramHttpResponse, HttpContext paramHttpContext);
  
  public abstract URI getLocationURI(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws ProtocolException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\RedirectHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */