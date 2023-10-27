package org.eclipse.jetty.client.api;

import org.eclipse.jetty.util.Promise;

public abstract interface Destination
{
  public abstract String getScheme();
  
  public abstract String getHost();
  
  public abstract int getPort();
  
  public abstract void newConnection(Promise<Connection> paramPromise);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\api\Destination.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */