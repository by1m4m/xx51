package io.netty.handler.ssl;

import io.netty.util.ReferenceCounted;

abstract interface OpenSslKeyMaterial
  extends ReferenceCounted
{
  public abstract long certificateChainAddress();
  
  public abstract long privateKeyAddress();
  
  public abstract OpenSslKeyMaterial retain();
  
  public abstract OpenSslKeyMaterial retain(int paramInt);
  
  public abstract OpenSslKeyMaterial touch();
  
  public abstract OpenSslKeyMaterial touch(Object paramObject);
  
  public abstract boolean release();
  
  public abstract boolean release(int paramInt);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslKeyMaterial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */