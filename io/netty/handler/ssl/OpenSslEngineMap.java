package io.netty.handler.ssl;

abstract interface OpenSslEngineMap
{
  public abstract ReferenceCountedOpenSslEngine remove(long paramLong);
  
  public abstract void add(ReferenceCountedOpenSslEngine paramReferenceCountedOpenSslEngine);
  
  public abstract ReferenceCountedOpenSslEngine get(long paramLong);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslEngineMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */