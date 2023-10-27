package org.eclipse.jetty.client.api;

import java.io.Closeable;

public abstract interface Connection
  extends Closeable
{
  public abstract void send(Request paramRequest, Response.CompleteListener paramCompleteListener);
  
  public abstract void close();
  
  public abstract boolean isClosed();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\api\Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */