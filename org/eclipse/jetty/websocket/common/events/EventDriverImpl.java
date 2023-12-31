package org.eclipse.jetty.websocket.common.events;

import org.eclipse.jetty.websocket.api.WebSocketPolicy;

public abstract interface EventDriverImpl
{
  public abstract EventDriver create(Object paramObject, WebSocketPolicy paramWebSocketPolicy)
    throws Throwable;
  
  public abstract String describeRule();
  
  public abstract boolean supports(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\EventDriverImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */