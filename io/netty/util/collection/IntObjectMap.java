package io.netty.util.collection;

import java.util.Map;

public abstract interface IntObjectMap<V>
  extends Map<Integer, V>
{
  public abstract V get(int paramInt);
  
  public abstract V put(int paramInt, V paramV);
  
  public abstract V remove(int paramInt);
  
  public abstract Iterable<PrimitiveEntry<V>> entries();
  
  public abstract boolean containsKey(int paramInt);
  
  public static abstract interface PrimitiveEntry<V>
  {
    public abstract int key();
    
    public abstract V value();
    
    public abstract void setValue(V paramV);
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\collection\IntObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */