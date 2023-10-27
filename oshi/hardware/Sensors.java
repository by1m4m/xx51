package oshi.hardware;

import java.io.Serializable;

public abstract interface Sensors
  extends Serializable
{
  public abstract double getCpuTemperature();
  
  public abstract int[] getFanSpeeds();
  
  public abstract double getCpuVoltage();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\Sensors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */