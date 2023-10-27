package org.eclipse.jetty.websocket.client.io;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;

public abstract interface UpgradeListener
{
  public abstract void onHandshakeRequest(UpgradeRequest paramUpgradeRequest);
  
  public abstract void onHandshakeResponse(UpgradeResponse paramUpgradeResponse);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\io\UpgradeListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */