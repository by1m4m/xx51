package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;

@GwtCompatible
public abstract interface ClassToInstanceMap<B>
  extends Map<Class<? extends B>, B>
{
  @CanIgnoreReturnValue
  public abstract <T extends B> T getInstance(Class<T> paramClass);
  
  @CanIgnoreReturnValue
  public abstract <T extends B> T putInstance(Class<T> paramClass, T paramT);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */