package org.eclipse.jetty.util;

import java.util.Enumeration;

public abstract interface Attributes
{
  public abstract void removeAttribute(String paramString);
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract Object getAttribute(String paramString);
  
  public abstract Enumeration<String> getAttributeNames();
  
  public abstract void clearAttributes();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Attributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */