package org.apache.http;

public abstract interface Header
{
  public abstract String getName();
  
  public abstract String getValue();
  
  public abstract HeaderElement[] getElements()
    throws ParseException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\Header.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */