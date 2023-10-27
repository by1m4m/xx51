package org.eclipse.jetty.util.component;

import java.util.concurrent.Future;

public abstract interface Graceful
{
  public abstract Future<Void> shutdown();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\Graceful.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */