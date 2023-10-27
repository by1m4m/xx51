package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;

@GwtCompatible
public abstract interface PeekingIterator<E>
  extends Iterator<E>
{
  public abstract E peek();
  
  @CanIgnoreReturnValue
  public abstract E next();
  
  public abstract void remove();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\PeekingIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */