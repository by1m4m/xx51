package org.slf4j;

public abstract interface IMarkerFactory
{
  public abstract Marker getMarker(String paramString);
  
  public abstract boolean exists(String paramString);
  
  public abstract boolean detachMarker(String paramString);
  
  public abstract Marker getDetachedMarker(String paramString);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\slf4j\IMarkerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */