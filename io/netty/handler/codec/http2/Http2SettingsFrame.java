package io.netty.handler.codec.http2;

public abstract interface Http2SettingsFrame
  extends Http2Frame
{
  public abstract Http2Settings settings();
  
  public abstract String name();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2SettingsFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */