package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;

public abstract interface HostNameResolver
{
  public abstract InetAddress resolve(String paramString)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\scheme\HostNameResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */