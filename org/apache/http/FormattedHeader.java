package org.apache.http;

import org.apache.http.util.CharArrayBuffer;

public abstract interface FormattedHeader
  extends Header
{
  public abstract CharArrayBuffer getBuffer();
  
  public abstract int getValuePos();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\FormattedHeader.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */