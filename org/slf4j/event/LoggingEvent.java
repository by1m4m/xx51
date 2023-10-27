package org.slf4j.event;

import org.slf4j.Marker;

public abstract interface LoggingEvent
{
  public abstract Level getLevel();
  
  public abstract Marker getMarker();
  
  public abstract String getLoggerName();
  
  public abstract String getMessage();
  
  public abstract String getThreadName();
  
  public abstract Object[] getArgumentArray();
  
  public abstract long getTimeStamp();
  
  public abstract Throwable getThrowable();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\slf4j\event\LoggingEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */