package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public abstract interface RewritePolicy
{
  public abstract LoggingEvent rewrite(LoggingEvent paramLoggingEvent);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\rewrite\RewritePolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */