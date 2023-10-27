package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.SortedSet;

@GwtIncompatible
abstract interface SortedMultisetBridge<E>
  extends Multiset<E>
{
  public abstract SortedSet<E> elementSet();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SortedMultisetBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */