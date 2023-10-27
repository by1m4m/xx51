/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Graphs
/*     */ {
/*     */   public static <N> boolean hasCycle(Graph<N> graph)
/*     */   {
/*  60 */     int numEdges = graph.edges().size();
/*  61 */     if (numEdges == 0) {
/*  62 */       return false;
/*     */     }
/*  64 */     if ((!graph.isDirected()) && (numEdges >= graph.nodes().size())) {
/*  65 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  69 */     Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
/*  70 */     for (N node : graph.nodes()) {
/*  71 */       if (subgraphHasCycle(graph, visitedNodes, node, null)) {
/*  72 */         return true;
/*     */       }
/*     */     }
/*  75 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasCycle(Network<?, ?> network)
/*     */   {
/*  88 */     if ((!network.isDirected()) && 
/*  89 */       (network.allowsParallelEdges()) && 
/*  90 */       (network.edges().size() > network.asGraph().edges().size())) {
/*  91 */       return true;
/*     */     }
/*  93 */     return hasCycle(network.asGraph());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, NodeVisitState> visitedNodes, N node, N previousNode)
/*     */   {
/* 103 */     NodeVisitState state = (NodeVisitState)visitedNodes.get(node);
/* 104 */     if (state == NodeVisitState.COMPLETE) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (state == NodeVisitState.PENDING) {
/* 108 */       return true;
/*     */     }
/*     */     
/* 111 */     visitedNodes.put(node, NodeVisitState.PENDING);
/* 112 */     for (N nextNode : graph.successors(node)) {
/* 113 */       if ((canTraverseWithoutReusingEdge(graph, nextNode, previousNode)) && 
/* 114 */         (subgraphHasCycle(graph, visitedNodes, nextNode, node))) {
/* 115 */         return true;
/*     */       }
/*     */     }
/* 118 */     visitedNodes.put(node, NodeVisitState.COMPLETE);
/* 119 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, Object previousNode)
/*     */   {
/* 130 */     if ((graph.isDirected()) || (!Objects.equal(previousNode, nextNode))) {
/* 131 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 135 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> Graph<N> transitiveClosure(Graph<N> graph)
/*     */   {
/* 149 */     MutableGraph<N> transitiveClosure = GraphBuilder.from(graph).allowsSelfLoops(true).build();
/*     */     Iterator localIterator1;
/*     */     N node;
/*     */     Object visitedNodes;
/* 153 */     if (graph.isDirected())
/*     */     {
/* 155 */       for (localIterator1 = graph.nodes().iterator(); localIterator1.hasNext();) { node = localIterator1.next();
/* 156 */         for (N reachableNode : reachableNodes(graph, node)) {
/* 157 */           transitiveClosure.putEdge(node, reachableNode);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 163 */       visitedNodes = new HashSet();
/* 164 */       for (Object node : graph.nodes())
/* 165 */         if (!((Set)visitedNodes).contains(node)) {
/* 166 */           reachableNodes = reachableNodes(graph, node);
/* 167 */           ((Set)visitedNodes).addAll(reachableNodes);
/* 168 */           pairwiseMatch = 1;
/* 169 */           for (localIterator3 = reachableNodes.iterator(); localIterator3.hasNext();) { nodeU = localIterator3.next();
/* 170 */             for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++))
/* 171 */               transitiveClosure.putEdge(nodeU, nodeV);
/*     */           }
/*     */         } }
/*     */     Set<N> reachableNodes;
/*     */     int pairwiseMatch;
/*     */     Iterator localIterator3;
/*     */     N nodeU;
/* 178 */     return transitiveClosure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> Set<N> reachableNodes(Graph<N> graph, N node)
/*     */   {
/* 193 */     Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 194 */     Set<N> visitedNodes = new LinkedHashSet();
/* 195 */     Queue<N> queuedNodes = new ArrayDeque();
/* 196 */     visitedNodes.add(node);
/* 197 */     queuedNodes.add(node);
/*     */     
/* 199 */     while (!queuedNodes.isEmpty()) {
/* 200 */       N currentNode = queuedNodes.remove();
/* 201 */       for (N successor : graph.successors(currentNode)) {
/* 202 */         if (visitedNodes.add(successor)) {
/* 203 */           queuedNodes.add(successor);
/*     */         }
/*     */       }
/*     */     }
/* 207 */     return Collections.unmodifiableSet(visitedNodes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> Graph<N> transpose(Graph<N> graph)
/*     */   {
/* 219 */     if (!graph.isDirected()) {
/* 220 */       return graph;
/*     */     }
/*     */     
/* 223 */     if ((graph instanceof TransposedGraph)) {
/* 224 */       return ((TransposedGraph)graph).graph;
/*     */     }
/*     */     
/* 227 */     return new TransposedGraph(graph);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph)
/*     */   {
/* 235 */     if (!graph.isDirected()) {
/* 236 */       return graph;
/*     */     }
/*     */     
/* 239 */     if ((graph instanceof TransposedValueGraph)) {
/* 240 */       return ((TransposedValueGraph)graph).graph;
/*     */     }
/*     */     
/* 243 */     return new TransposedValueGraph(graph);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, E> Network<N, E> transpose(Network<N, E> network)
/*     */   {
/* 251 */     if (!network.isDirected()) {
/* 252 */       return network;
/*     */     }
/*     */     
/* 255 */     if ((network instanceof TransposedNetwork)) {
/* 256 */       return ((TransposedNetwork)network).network;
/*     */     }
/*     */     
/* 259 */     return new TransposedNetwork(network);
/*     */   }
/*     */   
/*     */   private static class TransposedGraph<N> extends ForwardingGraph<N>
/*     */   {
/*     */     private final Graph<N> graph;
/*     */     
/*     */     TransposedGraph(Graph<N> graph)
/*     */     {
/* 268 */       this.graph = graph;
/*     */     }
/*     */     
/*     */     protected Graph<N> delegate()
/*     */     {
/* 273 */       return this.graph;
/*     */     }
/*     */     
/*     */     public Set<N> predecessors(N node)
/*     */     {
/* 278 */       return delegate().successors(node);
/*     */     }
/*     */     
/*     */     public Set<N> successors(N node)
/*     */     {
/* 283 */       return delegate().predecessors(node);
/*     */     }
/*     */     
/*     */     public int inDegree(N node)
/*     */     {
/* 288 */       return delegate().outDegree(node);
/*     */     }
/*     */     
/*     */     public int outDegree(N node)
/*     */     {
/* 293 */       return delegate().inDegree(node);
/*     */     }
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV)
/*     */     {
/* 298 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedValueGraph<N, V> extends ForwardingValueGraph<N, V>
/*     */   {
/*     */     private final ValueGraph<N, V> graph;
/*     */     
/*     */     TransposedValueGraph(ValueGraph<N, V> graph)
/*     */     {
/* 308 */       this.graph = graph;
/*     */     }
/*     */     
/*     */     protected ValueGraph<N, V> delegate()
/*     */     {
/* 313 */       return this.graph;
/*     */     }
/*     */     
/*     */     public Set<N> predecessors(N node)
/*     */     {
/* 318 */       return delegate().successors(node);
/*     */     }
/*     */     
/*     */     public Set<N> successors(N node)
/*     */     {
/* 323 */       return delegate().predecessors(node);
/*     */     }
/*     */     
/*     */     public int inDegree(N node)
/*     */     {
/* 328 */       return delegate().outDegree(node);
/*     */     }
/*     */     
/*     */     public int outDegree(N node)
/*     */     {
/* 333 */       return delegate().inDegree(node);
/*     */     }
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV)
/*     */     {
/* 338 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */     
/*     */     public Optional<V> edgeValue(N nodeU, N nodeV)
/*     */     {
/* 343 */       return delegate().edgeValue(nodeV, nodeU);
/*     */     }
/*     */     
/*     */     public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue)
/*     */     {
/* 348 */       return (V)delegate().edgeValueOrDefault(nodeV, nodeU, defaultValue);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
/*     */     private final Network<N, E> network;
/*     */     
/*     */     TransposedNetwork(Network<N, E> network) {
/* 356 */       this.network = network;
/*     */     }
/*     */     
/*     */     protected Network<N, E> delegate()
/*     */     {
/* 361 */       return this.network;
/*     */     }
/*     */     
/*     */     public Set<N> predecessors(N node)
/*     */     {
/* 366 */       return delegate().successors(node);
/*     */     }
/*     */     
/*     */     public Set<N> successors(N node)
/*     */     {
/* 371 */       return delegate().predecessors(node);
/*     */     }
/*     */     
/*     */     public int inDegree(N node)
/*     */     {
/* 376 */       return delegate().outDegree(node);
/*     */     }
/*     */     
/*     */     public int outDegree(N node)
/*     */     {
/* 381 */       return delegate().inDegree(node);
/*     */     }
/*     */     
/*     */     public Set<E> inEdges(N node)
/*     */     {
/* 386 */       return delegate().outEdges(node);
/*     */     }
/*     */     
/*     */     public Set<E> outEdges(N node)
/*     */     {
/* 391 */       return delegate().inEdges(node);
/*     */     }
/*     */     
/*     */     public EndpointPair<N> incidentNodes(E edge)
/*     */     {
/* 396 */       EndpointPair<N> endpointPair = delegate().incidentNodes(edge);
/* 397 */       return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
/*     */     }
/*     */     
/*     */     public Set<E> edgesConnecting(N nodeU, N nodeV)
/*     */     {
/* 402 */       return delegate().edgesConnecting(nodeV, nodeU);
/*     */     }
/*     */     
/*     */     public Optional<E> edgeConnecting(N nodeU, N nodeV)
/*     */     {
/* 407 */       return delegate().edgeConnecting(nodeV, nodeU);
/*     */     }
/*     */     
/*     */     public E edgeConnectingOrNull(N nodeU, N nodeV)
/*     */     {
/* 412 */       return (E)delegate().edgeConnectingOrNull(nodeV, nodeU);
/*     */     }
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV)
/*     */     {
/* 417 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes)
/*     */   {
/* 434 */     MutableGraph<N> subgraph = (nodes instanceof Collection) ? GraphBuilder.from(graph).expectedNodeCount(((Collection)nodes).size()).build() : GraphBuilder.from(graph).build();
/* 435 */     for (N node : nodes) {
/* 436 */       subgraph.addNode(node);
/*     */     }
/* 438 */     for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
/* 439 */       for (N successorNode : graph.successors(node)) {
/* 440 */         if (subgraph.nodes().contains(successorNode))
/* 441 */           subgraph.putEdge(node, successorNode);
/*     */       }
/*     */     }
/*     */     N node;
/* 445 */     return subgraph;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes)
/*     */   {
/* 461 */     MutableValueGraph<N, V> subgraph = (nodes instanceof Collection) ? ValueGraphBuilder.from(graph).expectedNodeCount(((Collection)nodes).size()).build() : ValueGraphBuilder.from(graph).build();
/* 462 */     for (N node : nodes) {
/* 463 */       subgraph.addNode(node);
/*     */     }
/* 465 */     for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
/* 466 */       for (N successorNode : graph.successors(node)) {
/* 467 */         if (subgraph.nodes().contains(successorNode))
/* 468 */           subgraph.putEdgeValue(node, successorNode, graph
/* 469 */             .edgeValueOrDefault(node, successorNode, null));
/*     */       }
/*     */     }
/*     */     N node;
/* 473 */     return subgraph;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes)
/*     */   {
/* 489 */     MutableNetwork<N, E> subgraph = (nodes instanceof Collection) ? NetworkBuilder.from(network).expectedNodeCount(((Collection)nodes).size()).build() : NetworkBuilder.from(network).build();
/* 490 */     for (N node : nodes) {
/* 491 */       subgraph.addNode(node);
/*     */     }
/* 493 */     for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
/* 494 */       for (E edge : network.outEdges(node)) {
/* 495 */         N successorNode = network.incidentNodes(edge).adjacentNode(node);
/* 496 */         if (subgraph.nodes().contains(successorNode))
/* 497 */           subgraph.addEdge(node, successorNode, edge);
/*     */       }
/*     */     }
/*     */     N node;
/* 501 */     return subgraph;
/*     */   }
/*     */   
/*     */   public static <N> MutableGraph<N> copyOf(Graph<N> graph)
/*     */   {
/* 506 */     MutableGraph<N> copy = GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 507 */     for (N node : graph.nodes()) {
/* 508 */       copy.addNode(node);
/*     */     }
/* 510 */     for (EndpointPair<N> edge : graph.edges()) {
/* 511 */       copy.putEdge(edge.nodeU(), edge.nodeV());
/*     */     }
/* 513 */     return copy;
/*     */   }
/*     */   
/*     */ 
/*     */   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph)
/*     */   {
/* 519 */     MutableValueGraph<N, V> copy = ValueGraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 520 */     for (N node : graph.nodes()) {
/* 521 */       copy.addNode(node);
/*     */     }
/* 523 */     for (EndpointPair<N> edge : graph.edges()) {
/* 524 */       copy.putEdgeValue(edge
/* 525 */         .nodeU(), edge.nodeV(), graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null));
/*     */     }
/* 527 */     return copy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network)
/*     */   {
/* 536 */     MutableNetwork<N, E> copy = NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
/* 537 */     for (N node : network.nodes()) {
/* 538 */       copy.addNode(node);
/*     */     }
/* 540 */     for (E edge : network.edges()) {
/* 541 */       EndpointPair<N> endpointPair = network.incidentNodes(edge);
/* 542 */       copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
/*     */     }
/* 544 */     return copy;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(int value) {
/* 549 */     Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
/* 550 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(long value) {
/* 555 */     Preconditions.checkArgument(value >= 0L, "Not true that %s is non-negative.", value);
/* 556 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(int value) {
/* 561 */     Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
/* 562 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(long value) {
/* 567 */     Preconditions.checkArgument(value > 0L, "Not true that %s is positive.", value);
/* 568 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static enum NodeVisitState
/*     */   {
/* 577 */     PENDING, 
/* 578 */     COMPLETE;
/*     */     
/*     */     private NodeVisitState() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\Graphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */