package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract interface BiMap<K, V>
  extends Map<K, V>
{
  @CanIgnoreReturnValue
  public abstract V put(K paramK, V paramV);
  
  @CanIgnoreReturnValue
  public abstract V forcePut(K paramK, V paramV);
  
  public abstract void putAll(Map<? extends K, ? extends V> paramMap);
  
  public abstract Set<V> values();
  
  public abstract BiMap<V, K> inverse();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\BiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */