package io.netty.handler.codec.dns;

public abstract interface DnsRecord
{
  public static final int CLASS_IN = 1;
  public static final int CLASS_CSNET = 2;
  public static final int CLASS_CHAOS = 3;
  public static final int CLASS_HESIOD = 4;
  public static final int CLASS_NONE = 254;
  public static final int CLASS_ANY = 255;
  
  public abstract String name();
  
  public abstract DnsRecordType type();
  
  public abstract int dnsClass();
  
  public abstract long timeToLive();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\dns\DnsRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */