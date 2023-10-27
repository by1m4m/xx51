/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
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
/*     */ @Beta
/*     */ public abstract class AbstractValueGraph<N, V>
/*     */   extends AbstractBaseGraph<N>
/*     */   implements ValueGraph<N, V>
/*     */ {
/*     */   public Graph<N> asGraph()
/*     */   {
/*  45 */     new AbstractGraph()
/*     */     {
/*     */       public Set<N> nodes() {
/*  48 */         return AbstractValueGraph.this.nodes();
/*     */       }
/*     */       
/*     */       public Set<EndpointPair<N>> edges()
/*     */       {
/*  53 */         return AbstractValueGraph.this.edges();
/*     */       }
/*     */       
/*     */       public boolean isDirected()
/*     */       {
/*  58 */         return AbstractValueGraph.this.isDirected();
/*     */       }
/*     */       
/*     */       public boolean allowsSelfLoops()
/*     */       {
/*  63 */         return AbstractValueGraph.this.allowsSelfLoops();
/*     */       }
/*     */       
/*     */       public ElementOrder<N> nodeOrder()
/*     */       {
/*  68 */         return AbstractValueGraph.this.nodeOrder();
/*     */       }
/*     */       
/*     */       public Set<N> adjacentNodes(N node)
/*     */       {
/*  73 */         return AbstractValueGraph.this.adjacentNodes(node);
/*     */       }
/*     */       
/*     */       public Set<N> predecessors(N node)
/*     */       {
/*  78 */         return AbstractValueGraph.this.predecessors(node);
/*     */       }
/*     */       
/*     */       public Set<N> successors(N node)
/*     */       {
/*  83 */         return AbstractValueGraph.this.successors(node);
/*     */       }
/*     */       
/*     */       public int degree(N node)
/*     */       {
/*  88 */         return AbstractValueGraph.this.degree(node);
/*     */       }
/*     */       
/*     */       public int inDegree(N node)
/*     */       {
/*  93 */         return AbstractValueGraph.this.inDegree(node);
/*     */       }
/*     */       
/*     */       public int outDegree(N node)
/*     */       {
/*  98 */         return AbstractValueGraph.this.outDegree(node);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public Optional<V> edgeValue(N nodeU, N nodeV)
/*     */   {
/* 105 */     return Optional.ofNullable(edgeValueOrDefault(nodeU, nodeV, null));
/*     */   }
/*     */   
/*     */   public final boolean equals(Object obj)
/*     */   {
/* 110 */     if (obj == this) {
/* 111 */       return true;
/*     */     }
/* 113 */     if (!(obj instanceof ValueGraph)) {
/* 114 */       return false;
/*     */     }
/* 116 */     ValueGraph<?, ?> other = (ValueGraph)obj;
/*     */     
/* 118 */     return (isDirected() == other.isDirected()) && 
/* 119 */       (nodes().equals(other.nodes())) && 
/* 120 */       (edgeValueMap(this).equals(edgeValueMap(other)));
/*     */   }
/*     */   
/*     */   public final int hashCode()
/*     */   {
/* 125 */     return edgeValueMap(this).hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 131 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */       "isDirected: " + isDirected() + ", allowsSelfLoops: " + allowsSelfLoops() + ", nodes: " + nodes() + ", edges: " + edgeValueMap(this);
/*     */   }
/*     */   
/*     */   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(ValueGraph<N, V> graph) {
/* 142 */     Function<EndpointPair<N>, V> edgeToValueFn = new Function()
/*     */     {
/*     */       public V apply(EndpointPair<N> edge)
/*     */       {
/* 146 */         return (V)this.val$graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null);
/*     */       }
/* 148 */     };
/* 149 */     return Maps.asMap(graph.edges(), edgeToValueFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\AbstractValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */