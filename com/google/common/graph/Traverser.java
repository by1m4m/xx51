/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ public abstract class Traverser<N>
/*     */ {
/*     */   public static <N> Traverser<N> forGraph(SuccessorsFunction<N> graph)
/*     */   {
/*  93 */     Preconditions.checkNotNull(graph);
/*  94 */     return new GraphTraverser(graph);
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
/*     */   public static <N> Traverser<N> forTree(SuccessorsFunction<N> tree)
/*     */   {
/* 171 */     Preconditions.checkNotNull(tree);
/* 172 */     if ((tree instanceof BaseGraph)) {
/* 173 */       Preconditions.checkArgument(((BaseGraph)tree).isDirected(), "Undirected graphs can never be trees.");
/*     */     }
/* 175 */     if ((tree instanceof Network)) {
/* 176 */       Preconditions.checkArgument(((Network)tree).isDirected(), "Undirected networks can never be trees.");
/*     */     }
/* 178 */     return new TreeTraverser(tree);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Iterable<N> breadthFirst(N paramN);
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
/*     */   public abstract Iterable<N> breadthFirst(Iterable<? extends N> paramIterable);
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
/*     */   public abstract Iterable<N> depthFirstPreOrder(N paramN);
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
/*     */   public abstract Iterable<N> depthFirstPreOrder(Iterable<? extends N> paramIterable);
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
/*     */   public abstract Iterable<N> depthFirstPostOrder(N paramN);
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
/*     */   public abstract Iterable<N> depthFirstPostOrder(Iterable<? extends N> paramIterable);
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
/*     */   private static final class GraphTraverser<N>
/*     */     extends Traverser<N>
/*     */   {
/*     */     private final SuccessorsFunction<N> graph;
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
/*     */     GraphTraverser(SuccessorsFunction<N> graph)
/*     */     {
/* 322 */       super();
/* 323 */       this.graph = ((SuccessorsFunction)Preconditions.checkNotNull(graph));
/*     */     }
/*     */     
/*     */     public Iterable<N> breadthFirst(N startNode)
/*     */     {
/* 328 */       Preconditions.checkNotNull(startNode);
/* 329 */       return breadthFirst(ImmutableSet.of(startNode));
/*     */     }
/*     */     
/*     */     public Iterable<N> breadthFirst(final Iterable<? extends N> startNodes)
/*     */     {
/* 334 */       Preconditions.checkNotNull(startNodes);
/* 335 */       if (Iterables.isEmpty(startNodes)) {
/* 336 */         return ImmutableSet.of();
/*     */       }
/* 338 */       for (N startNode : startNodes) {
/* 339 */         checkThatNodeIsInGraph(startNode);
/*     */       }
/* 341 */       new Iterable()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 344 */           return new Traverser.GraphTraverser.BreadthFirstIterator(Traverser.GraphTraverser.this, startNodes);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(N startNode)
/*     */     {
/* 351 */       Preconditions.checkNotNull(startNode);
/* 352 */       return depthFirstPreOrder(ImmutableSet.of(startNode));
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes)
/*     */     {
/* 357 */       Preconditions.checkNotNull(startNodes);
/* 358 */       if (Iterables.isEmpty(startNodes)) {
/* 359 */         return ImmutableSet.of();
/*     */       }
/* 361 */       for (N startNode : startNodes) {
/* 362 */         checkThatNodeIsInGraph(startNode);
/*     */       }
/* 364 */       new Iterable()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 367 */           return new Traverser.GraphTraverser.DepthFirstIterator(Traverser.GraphTraverser.this, startNodes, Traverser.Order.PREORDER);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(N startNode)
/*     */     {
/* 374 */       Preconditions.checkNotNull(startNode);
/* 375 */       return depthFirstPostOrder(ImmutableSet.of(startNode));
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes)
/*     */     {
/* 380 */       Preconditions.checkNotNull(startNodes);
/* 381 */       if (Iterables.isEmpty(startNodes)) {
/* 382 */         return ImmutableSet.of();
/*     */       }
/* 384 */       for (N startNode : startNodes) {
/* 385 */         checkThatNodeIsInGraph(startNode);
/*     */       }
/* 387 */       new Iterable()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 390 */           return new Traverser.GraphTraverser.DepthFirstIterator(Traverser.GraphTraverser.this, startNodes, Traverser.Order.POSTORDER);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void checkThatNodeIsInGraph(N startNode)
/*     */     {
/* 399 */       this.graph.successors(startNode);
/*     */     }
/*     */     
/*     */     private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
/* 403 */       private final Queue<N> queue = new ArrayDeque();
/* 404 */       private final Set<N> visited = new HashSet();
/*     */       
/*     */       BreadthFirstIterator() {
/* 407 */         for (N root : roots)
/*     */         {
/* 409 */           if (this.visited.add(root)) {
/* 410 */             this.queue.add(root);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean hasNext()
/*     */       {
/* 417 */         return !this.queue.isEmpty();
/*     */       }
/*     */       
/*     */       public N next()
/*     */       {
/* 422 */         N current = this.queue.remove();
/* 423 */         for (N neighbor : Traverser.GraphTraverser.this.graph.successors(current)) {
/* 424 */           if (this.visited.add(neighbor)) {
/* 425 */             this.queue.add(neighbor);
/*     */           }
/*     */         }
/* 428 */         return current;
/*     */       }
/*     */     }
/*     */     
/*     */     private final class DepthFirstIterator extends AbstractIterator<N> {
/* 433 */       private final Deque<Traverser.GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors> stack = new ArrayDeque();
/* 434 */       private final Set<N> visited = new HashSet();
/*     */       private final Traverser.Order order;
/*     */       
/*     */       DepthFirstIterator(Traverser.Order roots) {
/* 438 */         this.stack.push(new NodeAndSuccessors(null, roots));
/* 439 */         this.order = order;
/*     */       }
/*     */       
/*     */       protected N computeNext()
/*     */       {
/*     */         for (;;) {
/* 445 */           if (this.stack.isEmpty()) {
/* 446 */             return (N)endOfData();
/*     */           }
/* 448 */           Traverser.GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors nodeAndSuccessors = (NodeAndSuccessors)this.stack.getFirst();
/* 449 */           boolean firstVisit = this.visited.add(nodeAndSuccessors.node);
/* 450 */           boolean lastVisit = !nodeAndSuccessors.successorIterator.hasNext();
/* 451 */           boolean produceNode = ((firstVisit) && (this.order == Traverser.Order.PREORDER)) || ((lastVisit) && (this.order == Traverser.Order.POSTORDER));
/*     */           
/* 453 */           if (lastVisit) {
/* 454 */             this.stack.pop();
/*     */           }
/*     */           else {
/* 457 */             N successor = nodeAndSuccessors.successorIterator.next();
/* 458 */             if (!this.visited.contains(successor)) {
/* 459 */               this.stack.push(withSuccessors(successor));
/*     */             }
/*     */           }
/* 462 */           if ((produceNode) && (nodeAndSuccessors.node != null)) {
/* 463 */             return (N)nodeAndSuccessors.node;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       Traverser.GraphTraverser<N>.DepthFirstIterator.NodeAndSuccessors withSuccessors(N node) {
/* 469 */         return new NodeAndSuccessors(node, Traverser.GraphTraverser.this.graph.successors(node));
/*     */       }
/*     */       
/*     */       private final class NodeAndSuccessors
/*     */       {
/*     */         final N node;
/*     */         final Iterator<? extends N> successorIterator;
/*     */         
/*     */         NodeAndSuccessors(Iterable<? extends N> node) {
/* 478 */           this.node = node;
/* 479 */           this.successorIterator = successors.iterator();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeTraverser<N> extends Traverser<N> {
/*     */     private final SuccessorsFunction<N> tree;
/*     */     
/* 488 */     TreeTraverser(SuccessorsFunction<N> tree) { super();
/* 489 */       this.tree = ((SuccessorsFunction)Preconditions.checkNotNull(tree));
/*     */     }
/*     */     
/*     */     public Iterable<N> breadthFirst(N startNode)
/*     */     {
/* 494 */       Preconditions.checkNotNull(startNode);
/* 495 */       return breadthFirst(ImmutableSet.of(startNode));
/*     */     }
/*     */     
/*     */     public Iterable<N> breadthFirst(final Iterable<? extends N> startNodes)
/*     */     {
/* 500 */       Preconditions.checkNotNull(startNodes);
/* 501 */       if (Iterables.isEmpty(startNodes)) {
/* 502 */         return ImmutableSet.of();
/*     */       }
/* 504 */       for (N startNode : startNodes) {
/* 505 */         checkThatNodeIsInTree(startNode);
/*     */       }
/* 507 */       new Iterable()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 510 */           return new Traverser.TreeTraverser.BreadthFirstIterator(Traverser.TreeTraverser.this, startNodes);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(N startNode)
/*     */     {
/* 517 */       Preconditions.checkNotNull(startNode);
/* 518 */       return depthFirstPreOrder(ImmutableSet.of(startNode));
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes)
/*     */     {
/* 523 */       Preconditions.checkNotNull(startNodes);
/* 524 */       if (Iterables.isEmpty(startNodes)) {
/* 525 */         return ImmutableSet.of();
/*     */       }
/* 527 */       for (N node : startNodes) {
/* 528 */         checkThatNodeIsInTree(node);
/*     */       }
/* 530 */       new Iterable()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 533 */           return new Traverser.TreeTraverser.DepthFirstPreOrderIterator(Traverser.TreeTraverser.this, startNodes);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(N startNode)
/*     */     {
/* 540 */       Preconditions.checkNotNull(startNode);
/* 541 */       return depthFirstPostOrder(ImmutableSet.of(startNode));
/*     */     }
/*     */     
/*     */     public Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes)
/*     */     {
/* 546 */       Preconditions.checkNotNull(startNodes);
/* 547 */       if (Iterables.isEmpty(startNodes)) {
/* 548 */         return ImmutableSet.of();
/*     */       }
/* 550 */       for (N startNode : startNodes) {
/* 551 */         checkThatNodeIsInTree(startNode);
/*     */       }
/* 553 */       new Iterable()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 556 */           return new Traverser.TreeTraverser.DepthFirstPostOrderIterator(Traverser.TreeTraverser.this, startNodes);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void checkThatNodeIsInTree(N startNode)
/*     */     {
/* 565 */       this.tree.successors(startNode);
/*     */     }
/*     */     
/*     */     private final class BreadthFirstIterator extends UnmodifiableIterator<N> {
/* 569 */       private final Queue<N> queue = new ArrayDeque();
/*     */       
/*     */       BreadthFirstIterator() {
/* 572 */         for (N root : roots) {
/* 573 */           this.queue.add(root);
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean hasNext()
/*     */       {
/* 579 */         return !this.queue.isEmpty();
/*     */       }
/*     */       
/*     */       public N next()
/*     */       {
/* 584 */         N current = this.queue.remove();
/* 585 */         Iterables.addAll(this.queue, Traverser.TreeTraverser.this.tree.successors(current));
/* 586 */         return current;
/*     */       }
/*     */     }
/*     */     
/*     */     private final class DepthFirstPreOrderIterator extends UnmodifiableIterator<N> {
/* 591 */       private final Deque<Iterator<? extends N>> stack = new ArrayDeque();
/*     */       
/*     */       DepthFirstPreOrderIterator() {
/* 594 */         this.stack.addLast(roots.iterator());
/*     */       }
/*     */       
/*     */       public boolean hasNext()
/*     */       {
/* 599 */         return !this.stack.isEmpty();
/*     */       }
/*     */       
/*     */       public N next()
/*     */       {
/* 604 */         Iterator<? extends N> iterator = (Iterator)this.stack.getLast();
/* 605 */         N result = Preconditions.checkNotNull(iterator.next());
/* 606 */         if (!iterator.hasNext()) {
/* 607 */           this.stack.removeLast();
/*     */         }
/* 609 */         Iterator<? extends N> childIterator = Traverser.TreeTraverser.this.tree.successors(result).iterator();
/* 610 */         if (childIterator.hasNext()) {
/* 611 */           this.stack.addLast(childIterator);
/*     */         }
/* 613 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */     private final class DepthFirstPostOrderIterator extends AbstractIterator<N> {
/* 618 */       private final ArrayDeque<Traverser.TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren> stack = new ArrayDeque();
/*     */       
/*     */       DepthFirstPostOrderIterator() {
/* 621 */         this.stack.addLast(new NodeAndChildren(null, roots));
/*     */       }
/*     */       
/*     */       protected N computeNext()
/*     */       {
/* 626 */         while (!this.stack.isEmpty()) {
/* 627 */           Traverser.TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren top = (NodeAndChildren)this.stack.getLast();
/* 628 */           if (top.childIterator.hasNext()) {
/* 629 */             N child = top.childIterator.next();
/* 630 */             this.stack.addLast(withChildren(child));
/*     */           } else {
/* 632 */             this.stack.removeLast();
/* 633 */             if (top.node != null) {
/* 634 */               return (N)top.node;
/*     */             }
/*     */           }
/*     */         }
/* 638 */         return (N)endOfData();
/*     */       }
/*     */       
/*     */       Traverser.TreeTraverser<N>.DepthFirstPostOrderIterator.NodeAndChildren withChildren(N node) {
/* 642 */         return new NodeAndChildren(node, Traverser.TreeTraverser.this.tree.successors(node));
/*     */       }
/*     */       
/*     */       private final class NodeAndChildren
/*     */       {
/*     */         final N node;
/*     */         final Iterator<? extends N> childIterator;
/*     */         
/*     */         NodeAndChildren(Iterable<? extends N> node) {
/* 651 */           this.node = node;
/* 652 */           this.childIterator = children.iterator();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum Order {
/* 659 */     PREORDER, 
/* 660 */     POSTORDER;
/*     */     
/*     */     private Order() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\Traverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */