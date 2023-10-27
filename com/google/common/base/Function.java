package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@FunctionalInterface
@GwtCompatible
public abstract interface Function<F, T>
  extends java.util.function.Function<F, T>
{
  @CanIgnoreReturnValue
  public abstract T apply(F paramF);
  
  public abstract boolean equals(Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */