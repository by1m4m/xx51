package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;

@Beta
@GwtIncompatible
public abstract interface LineProcessor<T>
{
  @CanIgnoreReturnValue
  public abstract boolean processLine(String paramString)
    throws IOException;
  
  public abstract T getResult();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\LineProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */