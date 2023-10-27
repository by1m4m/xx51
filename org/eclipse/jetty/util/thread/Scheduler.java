package org.eclipse.jetty.util.thread;

import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.util.component.LifeCycle;

public abstract interface Scheduler
  extends LifeCycle
{
  public abstract Task schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit);
  
  public static abstract interface Task
  {
    public abstract boolean cancel();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\Scheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */