package org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract interface MessageAppender
{
  public abstract void appendFrame(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws IOException;
  
  public abstract void messageComplete();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\MessageAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */