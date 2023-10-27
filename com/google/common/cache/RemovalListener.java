package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public abstract interface RemovalListener<K, V>
{
  public abstract void onRemoval(RemovalNotification<K, V> paramRemovalNotification);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\RemovalListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */