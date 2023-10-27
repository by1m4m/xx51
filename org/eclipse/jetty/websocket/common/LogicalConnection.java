package org.eclipse.jetty.websocket.common;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.websocket.api.SuspendToken;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.io.IOState;

public abstract interface LogicalConnection
  extends OutgoingFrames, SuspendToken
{
  public abstract void onLocalClose(CloseInfo paramCloseInfo);
  
  public abstract void disconnect();
  
  public abstract ByteBufferPool getBufferPool();
  
  public abstract Executor getExecutor();
  
  public abstract long getIdleTimeout();
  
  public abstract IOState getIOState();
  
  public abstract InetSocketAddress getLocalAddress();
  
  public abstract long getMaxIdleTimeout();
  
  public abstract WebSocketPolicy getPolicy();
  
  public abstract InetSocketAddress getRemoteAddress();
  
  public abstract boolean isOpen();
  
  public abstract boolean isReading();
  
  public abstract void setMaxIdleTimeout(long paramLong);
  
  public abstract void setNextIncomingFrames(IncomingFrames paramIncomingFrames);
  
  public abstract void setSession(WebSocketSession paramWebSocketSession);
  
  public abstract SuspendToken suspend();
  
  public abstract String getId();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\LogicalConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */