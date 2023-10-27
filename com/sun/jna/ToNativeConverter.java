package com.sun.jna;

public abstract interface ToNativeConverter
{
  public abstract Object toNative(Object paramObject, ToNativeContext paramToNativeContext);
  
  public abstract Class nativeType();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\ToNativeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */