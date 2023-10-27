package org.eclipse.jetty.websocket.api;

public abstract interface WriteCallback
{
  public abstract void writeFailed(Throwable paramThrowable);
  
  public abstract void writeSuccess();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WriteCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */