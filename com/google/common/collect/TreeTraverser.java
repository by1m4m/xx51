/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public abstract class TreeTraverser<T>
/*     */ {
/*     */   @Deprecated
/*     */   public static <T> TreeTraverser<T> using(Function<T, ? extends Iterable<T>> nodeToChildrenFunction)
/*     */   {
/*  92 */     Preconditions.checkNotNull(nodeToChildrenFunction);
/*  93 */     new TreeTraverser()
/*     */     {
/*     */       public Iterable<T> children(T root) {
/*  96 */         return (Iterable)this.val$nodeToChildrenFunction.apply(root);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Iterable<T> children(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final FluentIterable<T> preOrderTraversal(final T root)
/*     */   {
/* 116 */     Preconditions.checkNotNull(root);
/* 117 */     new FluentIterable()
/*     */     {
/*     */       public UnmodifiableIterator<T> iterator() {
/* 120 */         return TreeTraverser.this.preOrderIterator(root);
/*     */       }
/*     */       
/*     */       public void forEach(final Consumer<? super T> action)
/*     */       {
/* 125 */         Preconditions.checkNotNull(action);
/* 126 */         new Consumer()
/*     */         {
/*     */           public void accept(T t) {
/* 129 */             action.accept(t);
/* 130 */             TreeTraverser.this.children(t).forEach(this); } }
/*     */         
/* 132 */           .accept(root);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   UnmodifiableIterator<T> preOrderIterator(T root) {
/* 138 */     return new PreOrderIterator(root);
/*     */   }
/*     */   
/*     */   private final class PreOrderIterator extends UnmodifiableIterator<T> {
/*     */     private final Deque<Iterator<T>> stack;
/*     */     
/*     */     PreOrderIterator() {
/* 145 */       this.stack = new ArrayDeque();
/* 146 */       this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(root)));
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 151 */       return !this.stack.isEmpty();
/*     */     }
/*     */     
/*     */     public T next()
/*     */     {
/* 156 */       Iterator<T> itr = (Iterator)this.stack.getLast();
/* 157 */       T result = Preconditions.checkNotNull(itr.next());
/* 158 */       if (!itr.hasNext()) {
/* 159 */         this.stack.removeLast();
/*     */       }
/* 161 */       Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
/* 162 */       if (childItr.hasNext()) {
/* 163 */         this.stack.addLast(childItr);
/*     */       }
/* 165 */       return result;
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
/*     */   @Deprecated
/*     */   public final FluentIterable<T> postOrderTraversal(final T root)
/*     */   {
/* 181 */     Preconditions.checkNotNull(root);
/* 182 */     new FluentIterable()
/*     */     {
/*     */       public UnmodifiableIterator<T> iterator() {
/* 185 */         return TreeTraverser.this.postOrderIterator(root);
/*     */       }
/*     */       
/*     */       public void forEach(final Consumer<? super T> action)
/*     */       {
/* 190 */         Preconditions.checkNotNull(action);
/* 191 */         new Consumer()
/*     */         {
/*     */           public void accept(T t) {
/* 194 */             TreeTraverser.this.children(t).forEach(this);
/* 195 */             action.accept(t); } }
/*     */         
/* 197 */           .accept(root);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   UnmodifiableIterator<T> postOrderIterator(T root) {
/* 203 */     return new PostOrderIterator(root);
/*     */   }
/*     */   
/*     */   private static final class PostOrderNode<T> {
/*     */     final T root;
/*     */     final Iterator<T> childIterator;
/*     */     
/*     */     PostOrderNode(T root, Iterator<T> childIterator) {
/* 211 */       this.root = Preconditions.checkNotNull(root);
/* 212 */       this.childIterator = ((Iterator)Preconditions.checkNotNull(childIterator));
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PostOrderIterator extends AbstractIterator<T> {
/*     */     private final ArrayDeque<TreeTraverser.PostOrderNode<T>> stack;
/*     */     
/*     */     PostOrderIterator() {
/* 220 */       this.stack = new ArrayDeque();
/* 221 */       this.stack.addLast(expand(root));
/*     */     }
/*     */     
/*     */     protected T computeNext()
/*     */     {
/* 226 */       while (!this.stack.isEmpty()) {
/* 227 */         TreeTraverser.PostOrderNode<T> top = (TreeTraverser.PostOrderNode)this.stack.getLast();
/* 228 */         if (top.childIterator.hasNext()) {
/* 229 */           T child = top.childIterator.next();
/* 230 */           this.stack.addLast(expand(child));
/*     */         } else {
/* 232 */           this.stack.removeLast();
/* 233 */           return (T)top.root;
/*     */         }
/*     */       }
/* 236 */       return (T)endOfData();
/*     */     }
/*     */     
/*     */     private TreeTraverser.PostOrderNode<T> expand(T t) {
/* 240 */       return new TreeTraverser.PostOrderNode(t, TreeTraverser.this.children(t).iterator());
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
/*     */   @Deprecated
/*     */   public final FluentIterable<T> breadthFirstTraversal(final T root)
/*     */   {
/* 256 */     Preconditions.checkNotNull(root);
/* 257 */     new FluentIterable()
/*     */     {
/*     */       public UnmodifiableIterator<T> iterator() {
/* 260 */         return new TreeTraverser.BreadthFirstIterator(TreeTraverser.this, root);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T>
/*     */   {
/*     */     private final Queue<T> queue;
/*     */     
/*     */     BreadthFirstIterator() {
/* 270 */       this.queue = new ArrayDeque();
/* 271 */       this.queue.add(root);
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 276 */       return !this.queue.isEmpty();
/*     */     }
/*     */     
/*     */     public T peek()
/*     */     {
/* 281 */       return (T)this.queue.element();
/*     */     }
/*     */     
/*     */     public T next()
/*     */     {
/* 286 */       T result = this.queue.remove();
/* 287 */       Iterables.addAll(this.queue, TreeTraverser.this.children(result));
/* 288 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\TreeTraverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */