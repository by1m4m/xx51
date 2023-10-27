package oshi.software.os;

import java.io.Serializable;

public abstract interface OperatingSystemVersion
  extends Serializable
{
  public abstract String getVersion();
  
  public abstract void setVersion(String paramString);
  
  public abstract String getCodeName();
  
  public abstract void setCodeName(String paramString);
  
  public abstract String getBuildNumber();
  
  public abstract void setBuildNumber(String paramString);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\OperatingSystemVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */