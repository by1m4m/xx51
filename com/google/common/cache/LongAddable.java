package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract interface LongAddable
{
  public abstract void increment();
  
  public abstract void add(long paramLong);
  
  public abstract long sum();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\LongAddable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */