package org.apache.log4j.spi;

import java.io.Writer;

/**
 * @deprecated
 */
class NullWriter
  extends Writer
{
  public void close() {}
  
  public void flush() {}
  
  public void write(char[] cbuf, int off, int len) {}
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\spi\NullWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */