package com.sun.jna;

import java.lang.reflect.Method;

public abstract interface FunctionMapper
{
  public abstract String getFunctionName(NativeLibrary paramNativeLibrary, Method paramMethod);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\FunctionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */