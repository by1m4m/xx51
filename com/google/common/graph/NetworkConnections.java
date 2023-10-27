package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

abstract interface NetworkConnections<N, E>
{
  public abstract Set<N> adjacentNodes();
  
  public abstract Set<N> predecessors();
  
  public abstract Set<N> successors();
  
  public abstract Set<E> incidentEdges();
  
  public abstract Set<E> inEdges();
  
  public abstract Set<E> outEdges();
  
  public abstract Set<E> edgesConnecting(N paramN);
  
  public abstract N adjacentNode(E paramE);
  
  @CanIgnoreReturnValue
  public abstract N removeInEdge(E paramE, boolean paramBoolean);
  
  @CanIgnoreReturnValue
  public abstract N removeOutEdge(E paramE);
  
  public abstract void addInEdge(E paramE, N paramN, boolean paramBoolean);
  
  public abstract void addOutEdge(E paramE, N paramN);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\NetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */