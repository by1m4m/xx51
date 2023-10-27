/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DefaultPriorityQueue<T extends PriorityQueueNode>
/*     */   extends AbstractQueue<T>
/*     */   implements PriorityQueue<T>
/*     */ {
/*  33 */   private static final PriorityQueueNode[] EMPTY_ARRAY = new PriorityQueueNode[0];
/*     */   private final Comparator<T> comparator;
/*     */   private T[] queue;
/*     */   private int size;
/*     */   
/*     */   public DefaultPriorityQueue(Comparator<T> comparator, int initialSize)
/*     */   {
/*  40 */     this.comparator = ((Comparator)ObjectUtil.checkNotNull(comparator, "comparator"));
/*  41 */     this.queue = ((PriorityQueueNode[])(initialSize != 0 ? new PriorityQueueNode[initialSize] : EMPTY_ARRAY));
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  46 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  51 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   public boolean contains(Object o)
/*     */   {
/*  56 */     if (!(o instanceof PriorityQueueNode)) {
/*  57 */       return false;
/*     */     }
/*  59 */     PriorityQueueNode node = (PriorityQueueNode)o;
/*  60 */     return contains(node, node.priorityQueueIndex(this));
/*     */   }
/*     */   
/*     */   public boolean containsTyped(T node)
/*     */   {
/*  65 */     return contains(node, node.priorityQueueIndex(this));
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  70 */     for (int i = 0; i < this.size; i++) {
/*  71 */       T node = this.queue[i];
/*  72 */       if (node != null) {
/*  73 */         node.priorityQueueIndex(this, -1);
/*  74 */         this.queue[i] = null;
/*     */       }
/*     */     }
/*  77 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public void clearIgnoringIndexes()
/*     */   {
/*  82 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public boolean offer(T e)
/*     */   {
/*  87 */     if (e.priorityQueueIndex(this) != -1) {
/*  88 */       throw new IllegalArgumentException("e.priorityQueueIndex(): " + e.priorityQueueIndex(this) + " (expected: " + -1 + ") + e: " + e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  93 */     if (this.size >= this.queue.length)
/*     */     {
/*     */ 
/*  96 */       this.queue = ((PriorityQueueNode[])Arrays.copyOf(this.queue, this.queue.length + (this.queue.length < 64 ? this.queue.length + 2 : this.queue.length >>> 1)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 101 */     bubbleUp(this.size++, e);
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public T poll()
/*     */   {
/* 107 */     if (this.size == 0) {
/* 108 */       return null;
/*     */     }
/* 110 */     T result = this.queue[0];
/* 111 */     result.priorityQueueIndex(this, -1);
/*     */     
/* 113 */     T last = this.queue[(--this.size)];
/* 114 */     this.queue[this.size] = null;
/* 115 */     if (this.size != 0) {
/* 116 */       bubbleDown(0, last);
/*     */     }
/*     */     
/* 119 */     return result;
/*     */   }
/*     */   
/*     */   public T peek()
/*     */   {
/* 124 */     return this.size == 0 ? null : this.queue[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/*     */     try
/*     */     {
/* 132 */       node = (PriorityQueueNode)o;
/*     */     } catch (ClassCastException e) { T node;
/* 134 */       return false; }
/*     */     T node;
/* 136 */     return removeTyped(node);
/*     */   }
/*     */   
/*     */   public boolean removeTyped(T node)
/*     */   {
/* 141 */     int i = node.priorityQueueIndex(this);
/* 142 */     if (!contains(node, i)) {
/* 143 */       return false;
/*     */     }
/*     */     
/* 146 */     node.priorityQueueIndex(this, -1);
/* 147 */     if ((--this.size == 0) || (this.size == i))
/*     */     {
/* 149 */       this.queue[i] = null;
/* 150 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 154 */     T moved = this.queue[i] = this.queue[this.size];
/* 155 */     this.queue[this.size] = null;
/*     */     
/*     */ 
/*     */ 
/* 159 */     if (this.comparator.compare(node, moved) < 0) {
/* 160 */       bubbleDown(i, moved);
/*     */     } else {
/* 162 */       bubbleUp(i, moved);
/*     */     }
/* 164 */     return true;
/*     */   }
/*     */   
/*     */   public void priorityChanged(T node)
/*     */   {
/* 169 */     int i = node.priorityQueueIndex(this);
/* 170 */     if (!contains(node, i)) {
/* 171 */       return;
/*     */     }
/*     */     
/*     */ 
/* 175 */     if (i == 0) {
/* 176 */       bubbleDown(i, node);
/*     */     }
/*     */     else {
/* 179 */       int iParent = i - 1 >>> 1;
/* 180 */       T parent = this.queue[iParent];
/* 181 */       if (this.comparator.compare(node, parent) < 0) {
/* 182 */         bubbleUp(i, node);
/*     */       } else {
/* 184 */         bubbleDown(i, node);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/* 191 */     return Arrays.copyOf(this.queue, this.size);
/*     */   }
/*     */   
/*     */ 
/*     */   public <X> X[] toArray(X[] a)
/*     */   {
/* 197 */     if (a.length < this.size) {
/* 198 */       return (Object[])Arrays.copyOf(this.queue, this.size, a.getClass());
/*     */     }
/* 200 */     System.arraycopy(this.queue, 0, a, 0, this.size);
/* 201 */     if (a.length > this.size) {
/* 202 */       a[this.size] = null;
/*     */     }
/* 204 */     return a;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/* 212 */     return new PriorityQueueIterator(null);
/*     */   }
/*     */   
/*     */   private final class PriorityQueueIterator implements Iterator<T> {
/*     */     private int index;
/*     */     
/*     */     private PriorityQueueIterator() {}
/*     */     
/* 220 */     public boolean hasNext() { return this.index < DefaultPriorityQueue.this.size; }
/*     */     
/*     */ 
/*     */     public T next()
/*     */     {
/* 225 */       if (this.index >= DefaultPriorityQueue.this.size) {
/* 226 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 229 */       return DefaultPriorityQueue.this.queue[(this.index++)];
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 234 */       throw new UnsupportedOperationException("remove");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean contains(PriorityQueueNode node, int i) {
/* 239 */     return (i >= 0) && (i < this.size) && (node.equals(this.queue[i]));
/*     */   }
/*     */   
/*     */   private void bubbleDown(int k, T node) {
/* 243 */     int half = this.size >>> 1;
/* 244 */     while (k < half)
/*     */     {
/* 246 */       int iChild = (k << 1) + 1;
/* 247 */       T child = this.queue[iChild];
/*     */       
/*     */ 
/* 250 */       int rightChild = iChild + 1;
/* 251 */       if ((rightChild < this.size) && (this.comparator.compare(child, this.queue[rightChild]) > 0)) {
/* 252 */         child = this.queue[(iChild = rightChild)];
/*     */       }
/*     */       
/*     */ 
/* 256 */       if (this.comparator.compare(node, child) <= 0) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 261 */       this.queue[k] = child;
/* 262 */       child.priorityQueueIndex(this, k);
/*     */       
/*     */ 
/* 265 */       k = iChild;
/*     */     }
/*     */     
/*     */ 
/* 269 */     this.queue[k] = node;
/* 270 */     node.priorityQueueIndex(this, k);
/*     */   }
/*     */   
/*     */   private void bubbleUp(int k, T node) {
/* 274 */     while (k > 0) {
/* 275 */       int iParent = k - 1 >>> 1;
/* 276 */       T parent = this.queue[iParent];
/*     */       
/*     */ 
/*     */ 
/* 280 */       if (this.comparator.compare(node, parent) >= 0) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 285 */       this.queue[k] = parent;
/* 286 */       parent.priorityQueueIndex(this, k);
/*     */       
/*     */ 
/* 289 */       k = iParent;
/*     */     }
/*     */     
/*     */ 
/* 293 */     this.queue[k] = node;
/* 294 */     node.priorityQueueIndex(this, k);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\DefaultPriorityQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */