package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract interface Table<R, C, V>
{
  public abstract boolean contains(@CompatibleWith("R") Object paramObject1, @CompatibleWith("C") Object paramObject2);
  
  public abstract boolean containsRow(@CompatibleWith("R") Object paramObject);
  
  public abstract boolean containsColumn(@CompatibleWith("C") Object paramObject);
  
  public abstract boolean containsValue(@CompatibleWith("V") Object paramObject);
  
  public abstract V get(@CompatibleWith("R") Object paramObject1, @CompatibleWith("C") Object paramObject2);
  
  public abstract boolean isEmpty();
  
  public abstract int size();
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
  
  public abstract void clear();
  
  @CanIgnoreReturnValue
  public abstract V put(R paramR, C paramC, V paramV);
  
  public abstract void putAll(Table<? extends R, ? extends C, ? extends V> paramTable);
  
  @CanIgnoreReturnValue
  public abstract V remove(@CompatibleWith("R") Object paramObject1, @CompatibleWith("C") Object paramObject2);
  
  public abstract Map<C, V> row(R paramR);
  
  public abstract Map<R, V> column(C paramC);
  
  public abstract Set<Cell<R, C, V>> cellSet();
  
  public abstract Set<R> rowKeySet();
  
  public abstract Set<C> columnKeySet();
  
  public abstract Collection<V> values();
  
  public abstract Map<R, Map<C, V>> rowMap();
  
  public abstract Map<C, Map<R, V>> columnMap();
  
  public static abstract interface Cell<R, C, V>
  {
    public abstract R getRowKey();
    
    public abstract C getColumnKey();
    
    public abstract V getValue();
    
    public abstract boolean equals(Object paramObject);
    
    public abstract int hashCode();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */