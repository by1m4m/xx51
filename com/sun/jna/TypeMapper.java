package com.sun.jna;

public abstract interface TypeMapper
{
  public abstract FromNativeConverter getFromNativeConverter(Class paramClass);
  
  public abstract ToNativeConverter getToNativeConverter(Class paramClass);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\TypeMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */