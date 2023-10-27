package oshi.hardware;

import java.io.Serializable;

public abstract interface HardwareAbstractionLayer
  extends Serializable
{
  public abstract ComputerSystem getComputerSystem();
  
  public abstract CentralProcessor getProcessor();
  
  public abstract GlobalMemory getMemory();
  
  public abstract PowerSource[] getPowerSources();
  
  public abstract HWDiskStore[] getDiskStores();
  
  public abstract NetworkIF[] getNetworkIFs();
  
  public abstract Display[] getDisplays();
  
  public abstract Sensors getSensors();
  
  public abstract UsbDevice[] getUsbDevices(boolean paramBoolean);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\HardwareAbstractionLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */