package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
public abstract interface RowSortedTable<R, C, V>
  extends Table<R, C, V>
{
  public abstract SortedSet<R> rowKeySet();
  
  public abstract SortedMap<R, Map<C, V>> rowMap();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\RowSortedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */