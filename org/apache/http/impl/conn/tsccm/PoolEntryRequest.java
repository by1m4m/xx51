package org.apache.http.impl.conn.tsccm;

import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ConnectionPoolTimeoutException;

public abstract interface PoolEntryRequest
{
  public abstract BasicPoolEntry getPoolEntry(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ConnectionPoolTimeoutException;
  
  public abstract void abortRequest();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\tsccm\PoolEntryRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */