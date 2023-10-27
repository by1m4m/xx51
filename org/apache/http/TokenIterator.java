package org.apache.http;

import java.util.Iterator;

public abstract interface TokenIterator
  extends Iterator
{
  public abstract boolean hasNext();
  
  public abstract String nextToken();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\TokenIterator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */