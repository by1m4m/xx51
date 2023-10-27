package org.eclipse.jetty.websocket.api;

import java.nio.ByteBuffer;

public abstract interface WebSocketPingPongListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketPing(ByteBuffer paramByteBuffer);
  
  public abstract void onWebSocketPong(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketPingPongListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */