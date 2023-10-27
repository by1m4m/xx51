package oshi.hardware;

import java.io.Serializable;
import java.time.LocalDate;

public abstract interface Firmware
  extends Serializable
{
  public abstract String getManufacturer();
  
  public abstract String getName();
  
  public abstract String getDescription();
  
  public abstract String getVersion();
  
  public abstract LocalDate getReleaseDate();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\Firmware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */