package org.eclipse.jetty.websocket.api.extensions;

public abstract interface IncomingFrames
{
  public abstract void incomingError(Throwable paramThrowable);
  
  public abstract void incomingFrame(Frame paramFrame);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\extensions\IncomingFrames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */