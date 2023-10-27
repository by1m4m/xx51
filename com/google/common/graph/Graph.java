package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Set;

@Beta
public abstract interface Graph<N>
  extends BaseGraph<N>
{
  public abstract Set<N> nodes();
  
  public abstract Set<EndpointPair<N>> edges();
  
  public abstract boolean isDirected();
  
  public abstract boolean allowsSelfLoops();
  
  public abstract ElementOrder<N> nodeOrder();
  
  public abstract Set<N> adjacentNodes(N paramN);
  
  public abstract Set<N> predecessors(N paramN);
  
  public abstract Set<N> successors(N paramN);
  
  public abstract Set<EndpointPair<N>> incidentEdges(N paramN);
  
  public abstract int degree(N paramN);
  
  public abstract int inDegree(N paramN);
  
  public abstract int outDegree(N paramN);
  
  public abstract boolean hasEdgeConnecting(N paramN1, N paramN2);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\Graph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */