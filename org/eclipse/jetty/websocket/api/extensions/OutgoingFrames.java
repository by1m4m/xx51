package org.eclipse.jetty.websocket.api.extensions;

import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;

public abstract interface OutgoingFrames
{
  public abstract void outgoingFrame(Frame paramFrame, WriteCallback paramWriteCallback, BatchMode paramBatchMode);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\extensions\OutgoingFrames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */