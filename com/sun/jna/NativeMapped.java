package com.sun.jna;

public abstract interface NativeMapped
{
  public abstract Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);
  
  public abstract Object toNative();
  
  public abstract Class nativeType();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\NativeMapped.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */