package org.apache.http.conn.params;

import org.apache.http.conn.routing.HttpRoute;

public abstract interface ConnPerRoute
{
  public abstract int getMaxForRoute(HttpRoute paramHttpRoute);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\params\ConnPerRoute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */