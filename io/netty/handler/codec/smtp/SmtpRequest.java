package io.netty.handler.codec.smtp;

import java.util.List;

public abstract interface SmtpRequest
{
  public abstract SmtpCommand command();
  
  public abstract List<CharSequence> parameters();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\smtp\SmtpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */