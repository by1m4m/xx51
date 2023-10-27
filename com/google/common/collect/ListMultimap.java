package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@GwtCompatible
public abstract interface ListMultimap<K, V>
  extends Multimap<K, V>
{
  public abstract List<V> get(K paramK);
  
  @CanIgnoreReturnValue
  public abstract List<V> removeAll(Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract List<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract boolean equals(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */