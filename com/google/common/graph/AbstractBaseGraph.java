/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.Sets.SetView;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.AbstractSet;
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
/*     */ abstract class AbstractBaseGraph<N>
/*     */   implements BaseGraph<N>
/*     */ {
/*     */   protected long edgeCount()
/*     */   {
/*  51 */     long degreeSum = 0L;
/*  52 */     for (N node : nodes()) {
/*  53 */       degreeSum += degree(node);
/*     */     }
/*     */     
/*  56 */     Preconditions.checkState((degreeSum & 1L) == 0L);
/*  57 */     return degreeSum >>> 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<EndpointPair<N>> edges()
/*     */   {
/*  66 */     new AbstractSet()
/*     */     {
/*     */       public UnmodifiableIterator<EndpointPair<N>> iterator() {
/*  69 */         return EndpointPairIterator.of(AbstractBaseGraph.this);
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/*  74 */         return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
/*     */       }
/*     */       
/*     */       public boolean remove(Object o)
/*     */       {
/*  79 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public boolean contains(Object obj)
/*     */       {
/*  88 */         if (!(obj instanceof EndpointPair)) {
/*  89 */           return false;
/*     */         }
/*  91 */         EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  92 */         return (AbstractBaseGraph.this.isDirected() == endpointPair.isOrdered()) && 
/*  93 */           (AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU())) && 
/*  94 */           (AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node)
/*     */   {
/* 101 */     Preconditions.checkNotNull(node);
/* 102 */     Preconditions.checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 103 */     return IncidentEdgeSet.of(this, node);
/*     */   }
/*     */   
/*     */   public int degree(N node)
/*     */   {
/* 108 */     if (isDirected()) {
/* 109 */       return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
/*     */     }
/* 111 */     Set<N> neighbors = adjacentNodes(node);
/* 112 */     int selfLoopCount = (allowsSelfLoops()) && (neighbors.contains(node)) ? 1 : 0;
/* 113 */     return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
/*     */   }
/*     */   
/*     */ 
/*     */   public int inDegree(N node)
/*     */   {
/* 119 */     return isDirected() ? predecessors(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public int outDegree(N node)
/*     */   {
/* 124 */     return isDirected() ? successors(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV)
/*     */   {
/* 129 */     Preconditions.checkNotNull(nodeU);
/* 130 */     Preconditions.checkNotNull(nodeV);
/* 131 */     return (nodes().contains(nodeU)) && (successors(nodeU).contains(nodeV));
/*     */   }
/*     */   
/*     */   private static abstract class IncidentEdgeSet<N> extends AbstractSet<EndpointPair<N>> {
/*     */     protected final N node;
/*     */     protected final BaseGraph<N> graph;
/*     */     
/*     */     public static <N> IncidentEdgeSet<N> of(BaseGraph<N> graph, N node) {
/* 139 */       return graph.isDirected() ? new Directed(graph, node, null) : new Undirected(graph, node, null);
/*     */     }
/*     */     
/*     */     private IncidentEdgeSet(BaseGraph<N> graph, N node) {
/* 143 */       this.graph = graph;
/* 144 */       this.node = node;
/*     */     }
/*     */     
/*     */     public boolean remove(Object o)
/*     */     {
/* 149 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     private static final class Directed<N> extends AbstractBaseGraph.IncidentEdgeSet<N>
/*     */     {
/*     */       private Directed(BaseGraph<N> graph, N node) {
/* 155 */         super(node, null);
/*     */       }
/*     */       
/*     */       public UnmodifiableIterator<EndpointPair<N>> iterator()
/*     */       {
/* 160 */         Iterators.unmodifiableIterator(
/* 161 */           Iterators.concat(
/* 162 */           Iterators.transform(this.graph
/* 163 */           .predecessors(this.node).iterator(), new Function()
/*     */           {
/*     */             public EndpointPair<N> apply(N predecessor)
/*     */             {
/* 167 */               return EndpointPair.ordered(predecessor, AbstractBaseGraph.IncidentEdgeSet.Directed.this.node);
/*     */             }
/*     */             
/* 170 */           }), Iterators.transform(
/*     */           
/* 172 */           Sets.difference(this.graph.successors(this.node), ImmutableSet.of(this.node)).iterator(), new Function()
/*     */           {
/*     */             public EndpointPair<N> apply(N successor)
/*     */             {
/* 176 */               return EndpointPair.ordered(AbstractBaseGraph.IncidentEdgeSet.Directed.this.node, successor);
/*     */             }
/*     */           })));
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 183 */         return 
/*     */         
/* 185 */           this.graph.inDegree(this.node) + this.graph.outDegree(this.node) - (this.graph.successors(this.node).contains(this.node) ? 1 : 0);
/*     */       }
/*     */       
/*     */       public boolean contains(Object obj)
/*     */       {
/* 190 */         if (!(obj instanceof EndpointPair)) {
/* 191 */           return false;
/*     */         }
/*     */         
/* 194 */         EndpointPair<?> endpointPair = (EndpointPair)obj;
/* 195 */         if (!endpointPair.isOrdered()) {
/* 196 */           return false;
/*     */         }
/*     */         
/* 199 */         Object source = endpointPair.source();
/* 200 */         Object target = endpointPair.target();
/* 201 */         return ((this.node.equals(source)) && (this.graph.successors(this.node).contains(target))) || (
/* 202 */           (this.node.equals(target)) && (this.graph.predecessors(this.node).contains(source)));
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class Undirected<N> extends AbstractBaseGraph.IncidentEdgeSet<N> {
/*     */       private Undirected(BaseGraph<N> graph, N node) {
/* 208 */         super(node, null);
/*     */       }
/*     */       
/*     */       public UnmodifiableIterator<EndpointPair<N>> iterator()
/*     */       {
/* 213 */         Iterators.unmodifiableIterator(
/* 214 */           Iterators.transform(this.graph
/* 215 */           .adjacentNodes(this.node).iterator(), new Function()
/*     */           {
/*     */             public EndpointPair<N> apply(N adjacentNode)
/*     */             {
/* 219 */               return EndpointPair.unordered(AbstractBaseGraph.IncidentEdgeSet.Undirected.this.node, adjacentNode);
/*     */             }
/*     */           }));
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 226 */         return this.graph.adjacentNodes(this.node).size();
/*     */       }
/*     */       
/*     */       public boolean contains(Object obj)
/*     */       {
/* 231 */         if (!(obj instanceof EndpointPair)) {
/* 232 */           return false;
/*     */         }
/*     */         
/* 235 */         EndpointPair<?> endpointPair = (EndpointPair)obj;
/* 236 */         if (endpointPair.isOrdered()) {
/* 237 */           return false;
/*     */         }
/* 239 */         Set<N> adjacent = this.graph.adjacentNodes(this.node);
/* 240 */         Object nodeU = endpointPair.nodeU();
/* 241 */         Object nodeV = endpointPair.nodeV();
/*     */         
/* 243 */         return ((this.node.equals(nodeV)) && (adjacent.contains(nodeU))) || (
/* 244 */           (this.node.equals(nodeU)) && (adjacent.contains(nodeV)));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\AbstractBaseGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */