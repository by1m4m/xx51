package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import java.io.IOException;
import java.net.InetAddress;

public abstract interface GeoIp2Provider
{
  public abstract CountryResponse country(InetAddress paramInetAddress)
    throws IOException, GeoIp2Exception;
  
  public abstract CityResponse city(InetAddress paramInetAddress)
    throws IOException, GeoIp2Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\GeoIp2Provider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */