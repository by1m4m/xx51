package org.eclipse.jetty.util.component;

import java.io.IOException;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.annotation.ManagedOperation;

@ManagedObject("Dumpable Object")
public abstract interface Dumpable
{
  @ManagedOperation(value="Dump the nested Object state as a String", impact="INFO")
  public abstract String dump();
  
  public abstract void dump(Appendable paramAppendable, String paramString)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\Dumpable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */