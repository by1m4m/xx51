package org.eclipse.jetty.websocket.api;

public abstract interface WebSocketConnectionListener
{
  public abstract void onWebSocketClose(int paramInt, String paramString);
  
  public abstract void onWebSocketConnect(Session paramSession);
  
  public abstract void onWebSocketError(Throwable paramThrowable);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketConnectionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */