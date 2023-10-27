package oshi.hardware;

import java.io.Serializable;

public abstract interface UsbDevice
  extends Serializable, Comparable<UsbDevice>
{
  public abstract String getName();
  
  public abstract String getVendor();
  
  public abstract String getVendorId();
  
  public abstract String getProductId();
  
  public abstract String getSerialNumber();
  
  public abstract UsbDevice[] getConnectedDevices();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\UsbDevice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */