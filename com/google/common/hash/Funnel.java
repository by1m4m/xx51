package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
public abstract interface Funnel<T>
  extends Serializable
{
  public abstract void funnel(T paramT, PrimitiveSink paramPrimitiveSink);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\Funnel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */