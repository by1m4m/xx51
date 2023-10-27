package org.eclipse.jetty.client;

import java.net.InetSocketAddress;
import java.util.Map;
import org.eclipse.jetty.io.ClientConnectionFactory;

public abstract interface HttpClientTransport
  extends ClientConnectionFactory
{
  public static final String HTTP_DESTINATION_CONTEXT_KEY = "http.destination";
  public static final String HTTP_CONNECTION_PROMISE_CONTEXT_KEY = "http.connection.promise";
  
  public abstract void setHttpClient(HttpClient paramHttpClient);
  
  public abstract HttpDestination newHttpDestination(Origin paramOrigin);
  
  public abstract void connect(InetSocketAddress paramInetSocketAddress, Map<String, Object> paramMap);
  
  public abstract ConnectionPool.Factory getConnectionPoolFactory();
  
  public abstract void setConnectionPoolFactory(ConnectionPool.Factory paramFactory);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpClientTransport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */