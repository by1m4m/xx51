package org.apache.commons.net;

import java.util.EventListener;

public abstract interface ProtocolCommandListener
  extends EventListener
{
  public abstract void protocolCommandSent(ProtocolCommandEvent paramProtocolCommandEvent);
  
  public abstract void protocolReplyReceived(ProtocolCommandEvent paramProtocolCommandEvent);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ProtocolCommandListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */