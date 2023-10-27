package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract interface FutureCallback<V>
{
  public abstract void onSuccess(V paramV);
  
  public abstract void onFailure(Throwable paramThrowable);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\FutureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */