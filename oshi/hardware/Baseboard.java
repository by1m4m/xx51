package oshi.hardware;

import java.io.Serializable;

public abstract interface Baseboard
  extends Serializable
{
  public abstract String getManufacturer();
  
  public abstract String getModel();
  
  public abstract String getVersion();
  
  public abstract String getSerialNumber();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\Baseboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */