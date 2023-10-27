package com.google.common.graph;

import com.google.common.annotations.Beta;

@Beta
public abstract interface SuccessorsFunction<N>
{
  public abstract Iterable<? extends N> successors(N paramN);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\SuccessorsFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */