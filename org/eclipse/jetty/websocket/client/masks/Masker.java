package org.eclipse.jetty.websocket.client.masks;

import org.eclipse.jetty.websocket.common.WebSocketFrame;

public abstract interface Masker
{
  public abstract void setMask(WebSocketFrame paramWebSocketFrame);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\masks\Masker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */