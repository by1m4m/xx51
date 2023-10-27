/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashSet<E>
/*     */   extends CompactHashSet<E>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   private transient int[] predecessor;
/*     */   private transient int[] successor;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create()
/*     */   {
/*  58 */     return new CompactLinkedHashSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> CompactLinkedHashSet<E> create(Collection<? extends E> collection)
/*     */   {
/*  69 */     CompactLinkedHashSet<E> set = createWithExpectedSize(collection.size());
/*  70 */     set.addAll(collection);
/*  71 */     return set;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> CompactLinkedHashSet<E> create(E... elements)
/*     */   {
/*  82 */     CompactLinkedHashSet<E> set = createWithExpectedSize(elements.length);
/*  83 */     Collections.addAll(set, elements);
/*  84 */     return set;
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
/*     */   public static <E> CompactLinkedHashSet<E> createWithExpectedSize(int expectedSize)
/*     */   {
/*  98 */     return new CompactLinkedHashSet(expectedSize);
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
/*     */   CompactLinkedHashSet() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CompactLinkedHashSet(int expectedSize)
/*     */   {
/* 127 */     super(expectedSize);
/*     */   }
/*     */   
/*     */   void init(int expectedSize, float loadFactor)
/*     */   {
/* 132 */     super.init(expectedSize, loadFactor);
/* 133 */     this.predecessor = new int[expectedSize];
/* 134 */     this.successor = new int[expectedSize];
/*     */     
/* 136 */     Arrays.fill(this.predecessor, -1);
/* 137 */     Arrays.fill(this.successor, -1);
/* 138 */     this.firstEntry = -2;
/* 139 */     this.lastEntry = -2;
/*     */   }
/*     */   
/*     */   private void succeeds(int pred, int succ) {
/* 143 */     if (pred == -2) {
/* 144 */       this.firstEntry = succ;
/*     */     } else {
/* 146 */       this.successor[pred] = succ;
/*     */     }
/*     */     
/* 149 */     if (succ == -2) {
/* 150 */       this.lastEntry = pred;
/*     */     } else {
/* 152 */       this.predecessor[succ] = pred;
/*     */     }
/*     */   }
/*     */   
/*     */   void insertEntry(int entryIndex, E object, int hash)
/*     */   {
/* 158 */     super.insertEntry(entryIndex, object, hash);
/* 159 */     succeeds(this.lastEntry, entryIndex);
/* 160 */     succeeds(entryIndex, -2);
/*     */   }
/*     */   
/*     */   void moveEntry(int dstIndex)
/*     */   {
/* 165 */     int srcIndex = size() - 1;
/* 166 */     super.moveEntry(dstIndex);
/*     */     
/* 168 */     succeeds(this.predecessor[dstIndex], this.successor[dstIndex]);
/* 169 */     if (srcIndex != dstIndex) {
/* 170 */       succeeds(this.predecessor[srcIndex], dstIndex);
/* 171 */       succeeds(dstIndex, this.successor[srcIndex]);
/*     */     }
/* 173 */     this.predecessor[srcIndex] = -1;
/* 174 */     this.successor[srcIndex] = -1;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 179 */     super.clear();
/* 180 */     this.firstEntry = -2;
/* 181 */     this.lastEntry = -2;
/* 182 */     Arrays.fill(this.predecessor, -1);
/* 183 */     Arrays.fill(this.successor, -1);
/*     */   }
/*     */   
/*     */   void resizeEntries(int newCapacity)
/*     */   {
/* 188 */     super.resizeEntries(newCapacity);
/* 189 */     int oldCapacity = this.predecessor.length;
/* 190 */     this.predecessor = Arrays.copyOf(this.predecessor, newCapacity);
/* 191 */     this.successor = Arrays.copyOf(this.successor, newCapacity);
/*     */     
/* 193 */     if (oldCapacity < newCapacity) {
/* 194 */       Arrays.fill(this.predecessor, oldCapacity, newCapacity, -1);
/* 195 */       Arrays.fill(this.successor, oldCapacity, newCapacity, -1);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/* 201 */     return ObjectArrays.toArrayImpl(this);
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] a)
/*     */   {
/* 206 */     return ObjectArrays.toArrayImpl(this, a);
/*     */   }
/*     */   
/*     */   int firstEntryIndex()
/*     */   {
/* 211 */     return this.firstEntry;
/*     */   }
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved)
/*     */   {
/* 216 */     return indexBeforeRemove == size() ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */   
/*     */   int getSuccessor(int entryIndex)
/*     */   {
/* 221 */     return this.successor[entryIndex];
/*     */   }
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 225 */     return Spliterators.spliterator(this, 17);
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/* 229 */     Preconditions.checkNotNull(action);
/* 230 */     for (int i = this.firstEntry; i != -2; i = this.successor[i]) {
/* 231 */       action.accept(this.elements[i]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\CompactLinkedHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */