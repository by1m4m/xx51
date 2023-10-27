package org.eclipse.jetty.websocket.api;

import java.nio.ByteBuffer;

public abstract interface WebSocketPartialListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketPartialBinary(ByteBuffer paramByteBuffer, boolean paramBoolean);
  
  public abstract void onWebSocketPartialText(String paramString, boolean paramBoolean);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketPartialListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */