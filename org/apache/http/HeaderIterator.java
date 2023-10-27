package org.apache.http;

import java.util.Iterator;

public abstract interface HeaderIterator
  extends Iterator
{
  public abstract boolean hasNext();
  
  public abstract Header nextHeader();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HeaderIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */