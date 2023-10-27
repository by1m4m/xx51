package org.apache.commons.net.io;

import java.util.EventListener;

public abstract interface CopyStreamListener
  extends EventListener
{
  public abstract void bytesTransferred(CopyStreamEvent paramCopyStreamEvent);
  
  public abstract void bytesTransferred(long paramLong1, int paramInt, long paramLong2);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\io\CopyStreamListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */