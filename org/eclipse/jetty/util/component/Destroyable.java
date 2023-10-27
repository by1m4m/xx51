package org.eclipse.jetty.util.component;

import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.annotation.ManagedOperation;

@ManagedObject
public abstract interface Destroyable
{
  @ManagedOperation(value="Destroys this component", impact="ACTION")
  public abstract void destroy();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\Destroyable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */