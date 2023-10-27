package org.apache.http.conn;

import java.io.IOException;

public abstract interface ConnectionReleaseTrigger
{
  public abstract void releaseConnection()
    throws IOException;
  
  public abstract void abortConnection()
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\ConnectionReleaseTrigger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */