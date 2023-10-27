package io.netty.handler.codec.stomp;

public abstract interface StompHeadersSubframe
  extends StompSubframe
{
  public abstract StompCommand command();
  
  public abstract StompHeaders headers();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\stomp\StompHeadersSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */