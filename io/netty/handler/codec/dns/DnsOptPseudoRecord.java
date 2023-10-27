package io.netty.handler.codec.dns;

public abstract interface DnsOptPseudoRecord
  extends DnsRecord
{
  public abstract int extendedRcode();
  
  public abstract int version();
  
  public abstract int flags();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\dns\DnsOptPseudoRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */