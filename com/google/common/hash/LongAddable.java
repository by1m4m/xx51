package com.google.common.hash;

abstract interface LongAddable
{
  public abstract void increment();
  
  public abstract void add(long paramLong);
  
  public abstract long sum();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\LongAddable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */