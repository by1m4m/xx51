package org.eclipse.jetty.client;

import java.io.Closeable;
import org.eclipse.jetty.client.api.Connection;

public abstract interface ConnectionPool
  extends Closeable
{
  public abstract boolean isActive(Connection paramConnection);
  
  public abstract boolean isEmpty();
  
  public abstract boolean isClosed();
  
  public abstract Connection acquire();
  
  public abstract boolean release(Connection paramConnection);
  
  public abstract boolean remove(Connection paramConnection);
  
  public abstract void close();
  
  public static abstract interface Factory
  {
    public abstract ConnectionPool newConnectionPool(HttpDestination paramHttpDestination);
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ConnectionPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */