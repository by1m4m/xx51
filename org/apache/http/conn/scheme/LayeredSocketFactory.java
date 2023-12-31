package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract interface LayeredSocketFactory
  extends SocketFactory
{
  public abstract Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException, UnknownHostException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\scheme\LayeredSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */