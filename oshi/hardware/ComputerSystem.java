package oshi.hardware;

import java.io.Serializable;

public abstract interface ComputerSystem
  extends Serializable
{
  public abstract String getManufacturer();
  
  public abstract String getModel();
  
  public abstract String getSerialNumber();
  
  public abstract Firmware getFirmware();
  
  public abstract Baseboard getBaseboard();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\ComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */