package org.eclipse.jetty.util.component;

import java.util.Collection;

public abstract interface Container
{
  public abstract boolean addBean(Object paramObject);
  
  public abstract Collection<Object> getBeans();
  
  public abstract <T> Collection<T> getBeans(Class<T> paramClass);
  
  public abstract <T> T getBean(Class<T> paramClass);
  
  public abstract boolean removeBean(Object paramObject);
  
  public abstract void addEventListener(Listener paramListener);
  
  public abstract void removeEventListener(Listener paramListener);
  
  public abstract void unmanage(Object paramObject);
  
  public abstract void manage(Object paramObject);
  
  public abstract boolean isManaged(Object paramObject);
  
  public abstract boolean addBean(Object paramObject, boolean paramBoolean);
  
  public abstract <T> Collection<T> getContainedBeans(Class<T> paramClass);
  
  public static abstract interface InheritedListener
    extends Container.Listener
  {}
  
  public static abstract interface Listener
  {
    public abstract void beanAdded(Container paramContainer, Object paramObject);
    
    public abstract void beanRemoved(Container paramContainer, Object paramObject);
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\Container.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */