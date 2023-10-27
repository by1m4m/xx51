package org.eclipse.jetty.websocket.common;

import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;

public abstract interface RemoteEndpointFactory
{
  public abstract RemoteEndpoint newRemoteEndpoint(LogicalConnection paramLogicalConnection, OutgoingFrames paramOutgoingFrames, BatchMode paramBatchMode);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\RemoteEndpointFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */