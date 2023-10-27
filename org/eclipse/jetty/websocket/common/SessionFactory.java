package org.eclipse.jetty.websocket.common;

import java.net.URI;
import org.eclipse.jetty.websocket.common.events.EventDriver;

public abstract interface SessionFactory
{
  public abstract boolean supports(EventDriver paramEventDriver);
  
  public abstract WebSocketSession createSession(URI paramURI, EventDriver paramEventDriver, LogicalConnection paramLogicalConnection);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\SessionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */