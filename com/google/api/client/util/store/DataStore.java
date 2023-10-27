package com.google.api.client.util.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public abstract interface DataStore<V extends Serializable>
{
  public abstract DataStoreFactory getDataStoreFactory();
  
  public abstract String getId();
  
  public abstract int size()
    throws IOException;
  
  public abstract boolean isEmpty()
    throws IOException;
  
  public abstract boolean containsKey(String paramString)
    throws IOException;
  
  public abstract boolean containsValue(V paramV)
    throws IOException;
  
  public abstract Set<String> keySet()
    throws IOException;
  
  public abstract Collection<V> values()
    throws IOException;
  
  public abstract V get(String paramString)
    throws IOException;
  
  public abstract DataStore<V> set(String paramString, V paramV)
    throws IOException;
  
  public abstract DataStore<V> clear()
    throws IOException;
  
  public abstract DataStore<V> delete(String paramString)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\DataStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */