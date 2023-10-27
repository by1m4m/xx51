package oshi.hardware;

import java.io.Serializable;

@FunctionalInterface
public abstract interface Networks
  extends Serializable
{
  public abstract NetworkIF[] getNetworks();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\Networks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */