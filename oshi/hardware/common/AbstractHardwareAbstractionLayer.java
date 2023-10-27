package oshi.hardware.common;

import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

public abstract class AbstractHardwareAbstractionLayer
  implements HardwareAbstractionLayer
{
  private static final long serialVersionUID = 1L;
  protected ComputerSystem computerSystem;
  protected CentralProcessor processor;
  protected GlobalMemory memory;
  protected Sensors sensors;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractHardwareAbstractionLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */