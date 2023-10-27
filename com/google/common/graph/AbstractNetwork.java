/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public abstract class AbstractNetwork<N, E>
/*     */   implements Network<N, E>
/*     */ {
/*     */   public Graph<N> asGraph()
/*     */   {
/*  54 */     new AbstractGraph()
/*     */     {
/*     */       public Set<N> nodes() {
/*  57 */         return AbstractNetwork.this.nodes();
/*     */       }
/*     */       
/*     */       public Set<EndpointPair<N>> edges()
/*     */       {
/*  62 */         if (AbstractNetwork.this.allowsParallelEdges()) {
/*  63 */           return super.edges();
/*     */         }
/*     */         
/*     */ 
/*  67 */         new AbstractSet()
/*     */         {
/*     */           public Iterator<EndpointPair<N>> iterator() {
/*  70 */             Iterators.transform(AbstractNetwork.this
/*  71 */               .edges().iterator(), new Function()
/*     */               {
/*     */                 public EndpointPair<N> apply(E edge)
/*     */                 {
/*  75 */                   return AbstractNetwork.this.incidentNodes(edge);
/*     */                 }
/*     */               });
/*     */           }
/*     */           
/*     */           public int size()
/*     */           {
/*  82 */             return AbstractNetwork.this.edges().size();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           public boolean contains(Object obj)
/*     */           {
/*  91 */             if (!(obj instanceof EndpointPair)) {
/*  92 */               return false;
/*     */             }
/*  94 */             EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  95 */             return (AbstractNetwork.1.this.isDirected() == endpointPair.isOrdered()) && 
/*  96 */               (AbstractNetwork.1.this.nodes().contains(endpointPair.nodeU())) && 
/*  97 */               (AbstractNetwork.1.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */           }
/*     */         };
/*     */       }
/*     */       
/*     */       public ElementOrder<N> nodeOrder()
/*     */       {
/* 104 */         return AbstractNetwork.this.nodeOrder();
/*     */       }
/*     */       
/*     */       public boolean isDirected()
/*     */       {
/* 109 */         return AbstractNetwork.this.isDirected();
/*     */       }
/*     */       
/*     */       public boolean allowsSelfLoops()
/*     */       {
/* 114 */         return AbstractNetwork.this.allowsSelfLoops();
/*     */       }
/*     */       
/*     */       public Set<N> adjacentNodes(N node)
/*     */       {
/* 119 */         return AbstractNetwork.this.adjacentNodes(node);
/*     */       }
/*     */       
/*     */       public Set<N> predecessors(N node)
/*     */       {
/* 124 */         return AbstractNetwork.this.predecessors(node);
/*     */       }
/*     */       
/*     */       public Set<N> successors(N node)
/*     */       {
/* 129 */         return AbstractNetwork.this.successors(node);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int degree(N node)
/*     */   {
/* 138 */     if (isDirected()) {
/* 139 */       return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
/*     */     }
/* 141 */     return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
/*     */   }
/*     */   
/*     */ 
/*     */   public int inDegree(N node)
/*     */   {
/* 147 */     return isDirected() ? inEdges(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public int outDegree(N node)
/*     */   {
/* 152 */     return isDirected() ? outEdges(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public Set<E> adjacentEdges(E edge)
/*     */   {
/* 157 */     EndpointPair<N> endpointPair = incidentNodes(edge);
/*     */     
/* 159 */     Set<E> endpointPairIncidentEdges = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
/* 160 */     return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
/*     */   }
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV)
/*     */   {
/* 165 */     Set<E> outEdgesU = outEdges(nodeU);
/* 166 */     Set<E> inEdgesV = inEdges(nodeV);
/* 167 */     return outEdgesU.size() <= inEdgesV.size() ? 
/* 168 */       Collections.unmodifiableSet(Sets.filter(outEdgesU, connectedPredicate(nodeU, nodeV))) : 
/* 169 */       Collections.unmodifiableSet(Sets.filter(inEdgesV, connectedPredicate(nodeV, nodeU)));
/*     */   }
/*     */   
/*     */   private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
/* 173 */     new Predicate()
/*     */     {
/*     */       public boolean apply(E edge) {
/* 176 */         return AbstractNetwork.this.incidentNodes(edge).adjacentNode(nodePresent).equals(nodeToCheck);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public Optional<E> edgeConnecting(N nodeU, N nodeV)
/*     */   {
/* 183 */     Set<E> edgesConnecting = edgesConnecting(nodeU, nodeV);
/* 184 */     switch (edgesConnecting.size()) {
/*     */     case 0: 
/* 186 */       return Optional.empty();
/*     */     case 1: 
/* 188 */       return Optional.of(edgesConnecting.iterator().next());
/*     */     }
/* 190 */     throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", new Object[] { nodeU, nodeV }));
/*     */   }
/*     */   
/*     */ 
/*     */   public E edgeConnectingOrNull(N nodeU, N nodeV)
/*     */   {
/* 196 */     return (E)edgeConnecting(nodeU, nodeV).orElse(null);
/*     */   }
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV)
/*     */   {
/* 201 */     return !edgesConnecting(nodeU, nodeV).isEmpty();
/*     */   }
/*     */   
/*     */   public final boolean equals(Object obj)
/*     */   {
/* 206 */     if (obj == this) {
/* 207 */       return true;
/*     */     }
/* 209 */     if (!(obj instanceof Network)) {
/* 210 */       return false;
/*     */     }
/* 212 */     Network<?, ?> other = (Network)obj;
/*     */     
/* 214 */     return (isDirected() == other.isDirected()) && 
/* 215 */       (nodes().equals(other.nodes())) && 
/* 216 */       (edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other)));
/*     */   }
/*     */   
/*     */   public final int hashCode()
/*     */   {
/* 221 */     return edgeIncidentNodesMap(this).hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 227 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 236 */       "isDirected: " + isDirected() + ", allowsParallelEdges: " + allowsParallelEdges() + ", allowsSelfLoops: " + allowsSelfLoops() + ", nodes: " + nodes() + ", edges: " + edgeIncidentNodesMap(this);
/*     */   }
/*     */   
/*     */   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(Network<N, E> network) {
/* 240 */     Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function()
/*     */     {
/*     */       public EndpointPair<N> apply(E edge)
/*     */       {
/* 244 */         return this.val$network.incidentNodes(edge);
/*     */       }
/* 246 */     };
/* 247 */     return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\AbstractNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */