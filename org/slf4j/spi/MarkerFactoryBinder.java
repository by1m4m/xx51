package org.slf4j.spi;

import org.slf4j.IMarkerFactory;

public abstract interface MarkerFactoryBinder
{
  public abstract IMarkerFactory getMarkerFactory();
  
  public abstract String getMarkerFactoryClassStr();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\slf4j\spi\MarkerFactoryBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */