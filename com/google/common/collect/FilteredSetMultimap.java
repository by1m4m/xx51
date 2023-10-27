package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract interface FilteredSetMultimap<K, V>
  extends FilteredMultimap<K, V>, SetMultimap<K, V>
{
  public abstract SetMultimap<K, V> unfiltered();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\FilteredSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */