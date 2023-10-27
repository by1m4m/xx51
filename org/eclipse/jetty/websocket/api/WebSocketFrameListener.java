package org.eclipse.jetty.websocket.api;

import org.eclipse.jetty.websocket.api.extensions.Frame;

public abstract interface WebSocketFrameListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketFrame(Frame paramFrame);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketFrameListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */