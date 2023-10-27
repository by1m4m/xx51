package org.apache.http;

import java.util.Iterator;

public abstract interface HeaderElementIterator
  extends Iterator
{
  public abstract boolean hasNext();
  
  public abstract HeaderElement nextElement();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HeaderElementIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */