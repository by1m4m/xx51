package io.netty.handler.ssl;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

abstract interface OpenSslSession
  extends SSLSession
{
  public abstract void handshakeFinished()
    throws SSLException;
  
  public abstract void tryExpandApplicationBufferSize(int paramInt);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */