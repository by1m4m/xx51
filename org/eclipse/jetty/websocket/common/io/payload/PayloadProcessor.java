package org.eclipse.jetty.websocket.common.io.payload;

import java.nio.ByteBuffer;
import org.eclipse.jetty.websocket.api.extensions.Frame;

public abstract interface PayloadProcessor
{
  public abstract void process(ByteBuffer paramByteBuffer);
  
  public abstract void reset(Frame paramFrame);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\payload\PayloadProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */