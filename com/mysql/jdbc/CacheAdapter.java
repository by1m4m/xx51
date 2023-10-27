package com.mysql.jdbc;

import java.util.Set;

public abstract interface CacheAdapter<K, V>
{
  public abstract V get(K paramK);
  
  public abstract void put(K paramK, V paramV);
  
  public abstract void invalidate(K paramK);
  
  public abstract void invalidateAll(Set<K> paramSet);
  
  public abstract void invalidateAll();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\CacheAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */