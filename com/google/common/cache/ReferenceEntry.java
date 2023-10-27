package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract interface ReferenceEntry<K, V>
{
  public abstract LocalCache.ValueReference<K, V> getValueReference();
  
  public abstract void setValueReference(LocalCache.ValueReference<K, V> paramValueReference);
  
  public abstract ReferenceEntry<K, V> getNext();
  
  public abstract int getHash();
  
  public abstract K getKey();
  
  public abstract long getAccessTime();
  
  public abstract void setAccessTime(long paramLong);
  
  public abstract ReferenceEntry<K, V> getNextInAccessQueue();
  
  public abstract void setNextInAccessQueue(ReferenceEntry<K, V> paramReferenceEntry);
  
  public abstract ReferenceEntry<K, V> getPreviousInAccessQueue();
  
  public abstract void setPreviousInAccessQueue(ReferenceEntry<K, V> paramReferenceEntry);
  
  public abstract long getWriteTime();
  
  public abstract void setWriteTime(long paramLong);
  
  public abstract ReferenceEntry<K, V> getNextInWriteQueue();
  
  public abstract void setNextInWriteQueue(ReferenceEntry<K, V> paramReferenceEntry);
  
  public abstract ReferenceEntry<K, V> getPreviousInWriteQueue();
  
  public abstract void setPreviousInWriteQueue(ReferenceEntry<K, V> paramReferenceEntry);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\ReferenceEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */