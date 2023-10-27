package oshi.software.os;

import java.io.Serializable;

public abstract interface NetworkParams
  extends Serializable
{
  public abstract String getHostName();
  
  public abstract String getDomainName();
  
  public abstract String[] getDnsServers();
  
  public abstract String getIpv4DefaultGateway();
  
  public abstract String getIpv6DefaultGateway();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\NetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */