package io.netty.handler.ssl;

@Deprecated
public abstract interface OpenSslApplicationProtocolNegotiator
  extends ApplicationProtocolNegotiator
{
  public abstract ApplicationProtocolConfig.Protocol protocol();
  
  public abstract ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior();
  
  public abstract ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */