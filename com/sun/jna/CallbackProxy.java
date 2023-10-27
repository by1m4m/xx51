package com.sun.jna;

public abstract interface CallbackProxy
  extends Callback
{
  public abstract Object callback(Object[] paramArrayOfObject);
  
  public abstract Class[] getParameterTypes();
  
  public abstract Class getReturnType();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\CallbackProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */