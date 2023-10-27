package com.sun.jna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract interface InvocationMapper
{
  public abstract InvocationHandler getInvocationHandler(NativeLibrary paramNativeLibrary, Method paramMethod);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\InvocationMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */