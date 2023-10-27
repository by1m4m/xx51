package org.eclipse.jetty.websocket.common.scopes;

import org.eclipse.jetty.websocket.common.WebSocketSession;

public abstract interface WebSocketSessionScope
{
  public abstract WebSocketSession getWebSocketSession();
  
  public abstract WebSocketContainerScope getContainerScope();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\scopes\WebSocketSessionScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */