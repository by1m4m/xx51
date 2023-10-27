package io.netty.handler.codec.dns;

public abstract interface DnsOptEcsRecord
  extends DnsOptPseudoRecord
{
  public abstract int sourcePrefixLength();
  
  public abstract int scopePrefixLength();
  
  public abstract byte[] address();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\dns\DnsOptEcsRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */