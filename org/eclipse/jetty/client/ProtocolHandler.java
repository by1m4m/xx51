package org.eclipse.jetty.client;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Response.Listener;

public abstract interface ProtocolHandler
{
  public abstract String getName();
  
  public abstract boolean accept(Request paramRequest, Response paramResponse);
  
  public abstract Response.Listener getResponseListener();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */