package org.apache.http.conn;

import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;

public abstract interface ClientConnectionManagerFactory
{
  public abstract ClientConnectionManager newInstance(HttpParams paramHttpParams, SchemeRegistry paramSchemeRegistry);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\ClientConnectionManagerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */