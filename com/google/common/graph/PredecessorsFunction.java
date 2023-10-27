package com.google.common.graph;

import com.google.common.annotations.Beta;

@Beta
public abstract interface PredecessorsFunction<N>
{
  public abstract Iterable<? extends N> predecessors(N paramN);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\PredecessorsFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */