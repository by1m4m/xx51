package org.eclipse.jetty.io;

import java.net.Socket;
import java.nio.ByteBuffer;

public abstract interface NetworkTrafficListener
{
  public abstract void opened(Socket paramSocket);
  
  public abstract void incoming(Socket paramSocket, ByteBuffer paramByteBuffer);
  
  public abstract void outgoing(Socket paramSocket, ByteBuffer paramByteBuffer);
  
  public abstract void closed(Socket paramSocket);
  
  public static class Adapter
    implements NetworkTrafficListener
  {
    public void opened(Socket socket) {}
    
    public void incoming(Socket socket, ByteBuffer bytes) {}
    
    public void outgoing(Socket socket, ByteBuffer bytes) {}
    
    public void closed(Socket socket) {}
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\NetworkTrafficListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */