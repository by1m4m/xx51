package org.apache.http.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public abstract interface ContentLengthStrategy
{
  public static final int IDENTITY = -1;
  public static final int CHUNKED = -2;
  
  public abstract long determineLength(HttpMessage paramHttpMessage)
    throws HttpException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\entity\ContentLengthStrategy.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */