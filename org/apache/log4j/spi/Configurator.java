package org.apache.log4j.spi;

import java.io.InputStream;
import java.net.URL;

public abstract interface Configurator
{
  public static final String INHERITED = "inherited";
  public static final String NULL = "null";
  
  public abstract void doConfigure(InputStream paramInputStream, LoggerRepository paramLoggerRepository);
  
  public abstract void doConfigure(URL paramURL, LoggerRepository paramLoggerRepository);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\spi\Configurator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */