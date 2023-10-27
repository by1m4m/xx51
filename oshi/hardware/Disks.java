package oshi.hardware;

import java.io.Serializable;

@FunctionalInterface
public abstract interface Disks
  extends Serializable
{
  public abstract HWDiskStore[] getDisks();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\Disks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */