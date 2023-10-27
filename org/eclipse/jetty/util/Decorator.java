package org.eclipse.jetty.util;

public abstract interface Decorator
{
  public abstract <T> T decorate(T paramT);
  
  public abstract void destroy(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Decorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */