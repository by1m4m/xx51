package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum RemovalCause
{
  EXPLICIT,  REPLACED,  COLLECTED,  EXPIRED,  SIZE;
  
  private RemovalCause() {}
  
  abstract boolean wasEvicted();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\RemovalCause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */