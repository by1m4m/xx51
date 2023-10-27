package io.netty.handler.codec.smtp;

import java.util.List;

public abstract interface SmtpResponse
{
  public abstract int code();
  
  public abstract List<CharSequence> details();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\smtp\SmtpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */