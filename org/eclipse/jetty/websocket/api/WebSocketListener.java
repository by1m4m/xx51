package org.eclipse.jetty.websocket.api;

public abstract interface WebSocketListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketBinary(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract void onWebSocketText(String paramString);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */