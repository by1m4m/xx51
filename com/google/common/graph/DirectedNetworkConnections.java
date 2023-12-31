/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.collect.BiMap;
/*    */ import com.google.common.collect.HashBiMap;
/*    */ import com.google.common.collect.ImmutableBiMap;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DirectedNetworkConnections<N, E>
/*    */   extends AbstractDirectedNetworkConnections<N, E>
/*    */ {
/*    */   protected DirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount)
/*    */   {
/* 39 */     super(inEdgeMap, outEdgeMap, selfLoopCount);
/*    */   }
/*    */   
/*    */   static <N, E> DirectedNetworkConnections<N, E> of() {
/* 43 */     return new DirectedNetworkConnections(
/* 44 */       HashBiMap.create(2), HashBiMap.create(2), 0);
/*    */   }
/*    */   
/*    */   static <N, E> DirectedNetworkConnections<N, E> ofImmutable(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount)
/*    */   {
/* 49 */     return new DirectedNetworkConnections(
/* 50 */       ImmutableBiMap.copyOf(inEdges), ImmutableBiMap.copyOf(outEdges), selfLoopCount);
/*    */   }
/*    */   
/*    */   public Set<N> predecessors()
/*    */   {
/* 55 */     return Collections.unmodifiableSet(((BiMap)this.inEdgeMap).values());
/*    */   }
/*    */   
/*    */   public Set<N> successors()
/*    */   {
/* 60 */     return Collections.unmodifiableSet(((BiMap)this.outEdgeMap).values());
/*    */   }
/*    */   
/*    */   public Set<E> edgesConnecting(N node)
/*    */   {
/* 65 */     return new EdgesConnecting(((BiMap)this.outEdgeMap).inverse(), node);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\DirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */