package org.eclipse.jetty.websocket.api.extensions;

public abstract interface Extension
  extends IncomingFrames, OutgoingFrames
{
  public abstract ExtensionConfig getConfig();
  
  public abstract String getName();
  
  public abstract boolean isRsv1User();
  
  public abstract boolean isRsv2User();
  
  public abstract boolean isRsv3User();
  
  public abstract void setNextIncomingFrames(IncomingFrames paramIncomingFrames);
  
  public abstract void setNextOutgoingFrames(OutgoingFrames paramOutgoingFrames);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\extensions\Extension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */