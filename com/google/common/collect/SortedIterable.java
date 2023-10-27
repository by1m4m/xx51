package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;

@GwtCompatible
abstract interface SortedIterable<T>
  extends Iterable<T>
{
  public abstract Comparator<? super T> comparator();
  
  public abstract Iterator<T> iterator();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SortedIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */