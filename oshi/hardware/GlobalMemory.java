package oshi.hardware;

import java.io.Serializable;

public abstract interface GlobalMemory
  extends Serializable
{
  public abstract long getTotal();
  
  public abstract long getAvailable();
  
  public abstract long getSwapTotal();
  
  public abstract long getSwapUsed();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\GlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */