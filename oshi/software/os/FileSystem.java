package oshi.software.os;

import java.io.Serializable;

public abstract interface FileSystem
  extends Serializable
{
  public abstract OSFileStore[] getFileStores();
  
  public abstract long getOpenFileDescriptors();
  
  public abstract long getMaxFileDescriptors();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\FileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */