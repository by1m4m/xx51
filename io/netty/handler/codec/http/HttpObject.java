package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.DecoderResultProvider;

public abstract interface HttpObject
  extends DecoderResultProvider
{
  @Deprecated
  public abstract DecoderResult getDecoderResult();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */