package oshi.hardware;

import java.io.Serializable;

public abstract interface PowerSource
  extends Serializable
{
  public abstract String getName();
  
  public abstract double getRemainingCapacity();
  
  public abstract double getTimeRemaining();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\PowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */