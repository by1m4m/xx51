package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public abstract interface UnrecognizedElementHandler
{
  public abstract boolean parseUnrecognizedElement(Element paramElement, Properties paramProperties)
    throws Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\xml\UnrecognizedElementHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */