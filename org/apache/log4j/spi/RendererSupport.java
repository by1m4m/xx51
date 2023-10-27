package org.apache.log4j.spi;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;

public abstract interface RendererSupport
{
  public abstract RendererMap getRendererMap();
  
  public abstract void setRenderer(Class paramClass, ObjectRenderer paramObjectRenderer);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\spi\RendererSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */