package org.eclipse.jetty.xml;

import java.net.URL;

public abstract interface ConfigurationProcessor
{
  public abstract void init(URL paramURL, XmlParser.Node paramNode, XmlConfiguration paramXmlConfiguration);
  
  public abstract Object configure(Object paramObject)
    throws Exception;
  
  public abstract Object configure()
    throws Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\xml\ConfigurationProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */