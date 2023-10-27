package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

abstract interface GraphConnections<N, V>
{
  public abstract Set<N> adjacentNodes();
  
  public abstract Set<N> predecessors();
  
  public abstract Set<N> successors();
  
  public abstract V value(N paramN);
  
  public abstract void removePredecessor(N paramN);
  
  @CanIgnoreReturnValue
  public abstract V removeSuccessor(N paramN);
  
  public abstract void addPredecessor(N paramN, V paramV);
  
  @CanIgnoreReturnValue
  public abstract V addSuccessor(N paramN, V paramV);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\GraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */