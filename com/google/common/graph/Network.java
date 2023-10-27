package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Optional;
import java.util.Set;

@Beta
public abstract interface Network<N, E>
  extends SuccessorsFunction<N>, PredecessorsFunction<N>
{
  public abstract Set<N> nodes();
  
  public abstract Set<E> edges();
  
  public abstract Graph<N> asGraph();
  
  public abstract boolean isDirected();
  
  public abstract boolean allowsParallelEdges();
  
  public abstract boolean allowsSelfLoops();
  
  public abstract ElementOrder<N> nodeOrder();
  
  public abstract ElementOrder<E> edgeOrder();
  
  public abstract Set<N> adjacentNodes(N paramN);
  
  public abstract Set<N> predecessors(N paramN);
  
  public abstract Set<N> successors(N paramN);
  
  public abstract Set<E> incidentEdges(N paramN);
  
  public abstract Set<E> inEdges(N paramN);
  
  public abstract Set<E> outEdges(N paramN);
  
  public abstract int degree(N paramN);
  
  public abstract int inDegree(N paramN);
  
  public abstract int outDegree(N paramN);
  
  public abstract EndpointPair<N> incidentNodes(E paramE);
  
  public abstract Set<E> adjacentEdges(E paramE);
  
  public abstract Set<E> edgesConnecting(N paramN1, N paramN2);
  
  public abstract Optional<E> edgeConnecting(N paramN1, N paramN2);
  
  public abstract E edgeConnectingOrNull(N paramN1, N paramN2);
  
  public abstract boolean hasEdgeConnecting(N paramN1, N paramN2);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\Network.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */