package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract interface PatternCompiler
{
  public abstract CommonPattern compile(String paramString);
  
  public abstract boolean isPcreLike();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\PatternCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */