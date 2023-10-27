package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@GwtCompatible
public abstract interface SetMultimap<K, V>
  extends Multimap<K, V>
{
  public abstract Set<V> get(K paramK);
  
  @CanIgnoreReturnValue
  public abstract Set<V> removeAll(Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract Set<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  public abstract Set<Map.Entry<K, V>> entries();
  
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract boolean equals(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */