package org.eclipse.jetty.client;

import java.util.EventListener;
import org.eclipse.jetty.client.api.ContentProvider;

public abstract interface AsyncContentProvider
  extends ContentProvider
{
  public abstract void setListener(Listener paramListener);
  
  public static abstract interface Listener
    extends EventListener
  {
    public abstract void onContent();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\AsyncContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */