package oshi.hardware;

import java.io.Serializable;

@FunctionalInterface
public abstract interface Display
  extends Serializable
{
  public abstract byte[] getEdid();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\Display.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */