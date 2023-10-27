/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ 
/*     */ class ConfigurableValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
/*     */   protected long edgeCount;
/*     */   
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder)
/*     */   {
/*  56 */     this(builder, builder.nodeOrder
/*     */     
/*  58 */       .createMap(
/*  59 */       ((Integer)builder.expectedNodeCount.or(Integer.valueOf(10))).intValue()), 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount)
/*     */   {
/*  71 */     this.isDirected = builder.directed;
/*  72 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  73 */     this.nodeOrder = builder.nodeOrder.cast();
/*     */     
/*  75 */     this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections));
/*     */     
/*     */ 
/*     */ 
/*  79 */     this.edgeCount = Graphs.checkNonNegative(edgeCount);
/*     */   }
/*     */   
/*     */   public Set<N> nodes()
/*     */   {
/*  84 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */   
/*     */   public boolean isDirected()
/*     */   {
/*  89 */     return this.isDirected;
/*     */   }
/*     */   
/*     */   public boolean allowsSelfLoops()
/*     */   {
/*  94 */     return this.allowsSelfLoops;
/*     */   }
/*     */   
/*     */   public ElementOrder<N> nodeOrder()
/*     */   {
/*  99 */     return this.nodeOrder;
/*     */   }
/*     */   
/*     */   public Set<N> adjacentNodes(N node)
/*     */   {
/* 104 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */   
/*     */   public Set<N> predecessors(N node)
/*     */   {
/* 109 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */   
/*     */   public Set<N> successors(N node)
/*     */   {
/* 114 */     return checkedConnections(node).successors();
/*     */   }
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV)
/*     */   {
/* 119 */     Preconditions.checkNotNull(nodeU);
/* 120 */     Preconditions.checkNotNull(nodeV);
/* 121 */     GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
/* 122 */     return (connectionsU != null) && (connectionsU.successors().contains(nodeV));
/*     */   }
/*     */   
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue)
/*     */   {
/* 127 */     Preconditions.checkNotNull(nodeU);
/* 128 */     Preconditions.checkNotNull(nodeV);
/* 129 */     GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
/* 130 */     V value = connectionsU == null ? null : connectionsU.value(nodeV);
/* 131 */     return value == null ? defaultValue : value;
/*     */   }
/*     */   
/*     */   protected long edgeCount()
/*     */   {
/* 136 */     return this.edgeCount;
/*     */   }
/*     */   
/*     */   protected final GraphConnections<N, V> checkedConnections(N node) {
/* 140 */     GraphConnections<N, V> connections = (GraphConnections)this.nodeConnections.get(node);
/* 141 */     if (connections == null) {
/* 142 */       Preconditions.checkNotNull(node);
/* 143 */       throw new IllegalArgumentException("Node " + node + " is not an element of this graph.");
/*     */     }
/* 145 */     return connections;
/*     */   }
/*     */   
/*     */   protected final boolean containsNode(N node) {
/* 149 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\ConfigurableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */