package org.apache.commons.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract interface DatagramSocketFactory
{
  public abstract DatagramSocket createDatagramSocket()
    throws SocketException;
  
  public abstract DatagramSocket createDatagramSocket(int paramInt)
    throws SocketException;
  
  public abstract DatagramSocket createDatagramSocket(int paramInt, InetAddress paramInetAddress)
    throws SocketException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\DatagramSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */