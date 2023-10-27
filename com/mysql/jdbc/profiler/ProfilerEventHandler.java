package com.mysql.jdbc.profiler;

import com.mysql.jdbc.Extension;

public abstract interface ProfilerEventHandler
  extends Extension
{
  public abstract void consumeEvent(ProfilerEvent paramProfilerEvent);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\profiler\ProfilerEventHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */