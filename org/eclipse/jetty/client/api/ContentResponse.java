package org.eclipse.jetty.client.api;

public abstract interface ContentResponse
  extends Response
{
  public abstract String getMediaType();
  
  public abstract String getEncoding();
  
  public abstract byte[] getContent();
  
  public abstract String getContentAsString();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\api\ContentResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */