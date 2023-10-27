package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;

@Beta
public abstract interface TypeToInstanceMap<B>
  extends Map<TypeToken<? extends B>, B>
{
  public abstract <T extends B> T getInstance(Class<T> paramClass);
  
  public abstract <T extends B> T getInstance(TypeToken<T> paramTypeToken);
  
  @CanIgnoreReturnValue
  public abstract <T extends B> T putInstance(Class<T> paramClass, T paramT);
  
  @CanIgnoreReturnValue
  public abstract <T extends B> T putInstance(TypeToken<T> paramTypeToken, T paramT);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\reflect\TypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */