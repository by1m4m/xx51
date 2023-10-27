package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.IspResponse;
import java.io.IOException;
import java.net.InetAddress;

public abstract interface DatabaseProvider
  extends GeoIp2Provider
{
  public abstract AnonymousIpResponse anonymousIp(InetAddress paramInetAddress)
    throws IOException, GeoIp2Exception;
  
  public abstract ConnectionTypeResponse connectionType(InetAddress paramInetAddress)
    throws IOException, GeoIp2Exception;
  
  public abstract DomainResponse domain(InetAddress paramInetAddress)
    throws IOException, GeoIp2Exception;
  
  public abstract IspResponse isp(InetAddress paramInetAddress)
    throws IOException, GeoIp2Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\DatabaseProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */