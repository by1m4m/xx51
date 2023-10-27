package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;

@GwtCompatible
public abstract interface SortedSetMultimap<K, V>
  extends SetMultimap<K, V>
{
  public abstract SortedSet<V> get(K paramK);
  
  @CanIgnoreReturnValue
  public abstract SortedSet<V> removeAll(Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract SortedSet<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract Comparator<? super V> valueComparator();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */