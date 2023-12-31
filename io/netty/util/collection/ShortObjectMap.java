package io.netty.util.collection;

import java.util.Map;

public abstract interface ShortObjectMap<V>
  extends Map<Short, V>
{
  public abstract V get(short paramShort);
  
  public abstract V put(short paramShort, V paramV);
  
  public abstract V remove(short paramShort);
  
  public abstract Iterable<PrimitiveEntry<V>> entries();
  
  public abstract boolean containsKey(short paramShort);
  
  public static abstract interface PrimitiveEntry<V>
  {
    public abstract short key();
    
    public abstract V value();
    
    public abstract void setValue(V paramV);
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\collection\ShortObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */