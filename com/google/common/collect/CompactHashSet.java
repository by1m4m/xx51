/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtIncompatible
/*     */ class CompactHashSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*     */   private static final float DEFAULT_LOAD_FACTOR = 1.0F;
/*     */   private static final long NEXT_MASK = 4294967295L;
/*     */   private static final long HASH_MASK = -4294967296L;
/*     */   private static final int DEFAULT_SIZE = 3;
/*     */   static final int UNSET = -1;
/*     */   private transient int[] table;
/*     */   private transient long[] entries;
/*     */   transient Object[] elements;
/*     */   transient float loadFactor;
/*     */   transient int modCount;
/*     */   private transient int threshold;
/*     */   private transient int size;
/*     */   
/*     */   public static <E> CompactHashSet<E> create()
/*     */   {
/*  76 */     return new CompactHashSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> CompactHashSet<E> create(Collection<? extends E> collection)
/*     */   {
/*  87 */     CompactHashSet<E> set = createWithExpectedSize(collection.size());
/*  88 */     set.addAll(collection);
/*  89 */     return set;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> CompactHashSet<E> create(E... elements)
/*     */   {
/* 100 */     CompactHashSet<E> set = createWithExpectedSize(elements.length);
/* 101 */     Collections.addAll(set, elements);
/* 102 */     return set;
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
/*     */   public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize)
/*     */   {
/* 115 */     return new CompactHashSet(expectedSize);
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
/*     */   CompactHashSet()
/*     */   {
/* 173 */     init(3, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CompactHashSet(int expectedSize)
/*     */   {
/* 182 */     init(expectedSize, 1.0F);
/*     */   }
/*     */   
/*     */   void init(int expectedSize, float loadFactor)
/*     */   {
/* 187 */     Preconditions.checkArgument(expectedSize >= 0, "Initial capacity must be non-negative");
/* 188 */     Preconditions.checkArgument(loadFactor > 0.0F, "Illegal load factor");
/* 189 */     int buckets = Hashing.closedTableSize(expectedSize, loadFactor);
/* 190 */     this.table = newTable(buckets);
/* 191 */     this.loadFactor = loadFactor;
/* 192 */     this.elements = new Object[expectedSize];
/* 193 */     this.entries = newEntries(expectedSize);
/* 194 */     this.threshold = Math.max(1, (int)(buckets * loadFactor));
/*     */   }
/*     */   
/*     */   private static int[] newTable(int size) {
/* 198 */     int[] array = new int[size];
/* 199 */     Arrays.fill(array, -1);
/* 200 */     return array;
/*     */   }
/*     */   
/*     */   private static long[] newEntries(int size) {
/* 204 */     long[] array = new long[size];
/* 205 */     Arrays.fill(array, -1L);
/* 206 */     return array;
/*     */   }
/*     */   
/*     */   private static int getHash(long entry) {
/* 210 */     return (int)(entry >>> 32);
/*     */   }
/*     */   
/*     */   private static int getNext(long entry)
/*     */   {
/* 215 */     return (int)entry;
/*     */   }
/*     */   
/*     */   private static long swapNext(long entry, int newNext)
/*     */   {
/* 220 */     return 0xFFFFFFFF00000000 & entry | 0xFFFFFFFF & newNext;
/*     */   }
/*     */   
/*     */   private int hashTableMask() {
/* 224 */     return this.table.length - 1;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E object)
/*     */   {
/* 230 */     long[] entries = this.entries;
/* 231 */     Object[] elements = this.elements;
/* 232 */     int hash = Hashing.smearedHash(object);
/* 233 */     int tableIndex = hash & hashTableMask();
/* 234 */     int newEntryIndex = this.size;
/* 235 */     int next = this.table[tableIndex];
/* 236 */     if (next == -1) {
/* 237 */       this.table[tableIndex] = newEntryIndex;
/*     */     } else {
/*     */       int last;
/*     */       long entry;
/*     */       do {
/* 242 */         last = next;
/* 243 */         entry = entries[next];
/* 244 */         if ((getHash(entry) == hash) && (Objects.equal(object, elements[next]))) {
/* 245 */           return false;
/*     */         }
/* 247 */         next = getNext(entry);
/* 248 */       } while (next != -1);
/* 249 */       entries[last] = swapNext(entry, newEntryIndex);
/*     */     }
/* 251 */     if (newEntryIndex == Integer.MAX_VALUE) {
/* 252 */       throw new IllegalStateException("Cannot contain more than Integer.MAX_VALUE elements!");
/*     */     }
/* 254 */     int newSize = newEntryIndex + 1;
/* 255 */     resizeMeMaybe(newSize);
/* 256 */     insertEntry(newEntryIndex, object, hash);
/* 257 */     this.size = newSize;
/* 258 */     if (newEntryIndex >= this.threshold) {
/* 259 */       resizeTable(2 * this.table.length);
/*     */     }
/* 261 */     this.modCount += 1;
/* 262 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void insertEntry(int entryIndex, E object, int hash)
/*     */   {
/* 269 */     this.entries[entryIndex] = (hash << 32 | 0xFFFFFFFF);
/* 270 */     this.elements[entryIndex] = object;
/*     */   }
/*     */   
/*     */   private void resizeMeMaybe(int newSize)
/*     */   {
/* 275 */     int entriesSize = this.entries.length;
/* 276 */     if (newSize > entriesSize) {
/* 277 */       int newCapacity = entriesSize + Math.max(1, entriesSize >>> 1);
/* 278 */       if (newCapacity < 0) {
/* 279 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 281 */       if (newCapacity != entriesSize) {
/* 282 */         resizeEntries(newCapacity);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void resizeEntries(int newCapacity)
/*     */   {
/* 292 */     this.elements = Arrays.copyOf(this.elements, newCapacity);
/* 293 */     long[] entries = this.entries;
/* 294 */     int oldSize = entries.length;
/* 295 */     entries = Arrays.copyOf(entries, newCapacity);
/* 296 */     if (newCapacity > oldSize) {
/* 297 */       Arrays.fill(entries, oldSize, newCapacity, -1L);
/*     */     }
/* 299 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   private void resizeTable(int newCapacity) {
/* 303 */     int[] oldTable = this.table;
/* 304 */     int oldCapacity = oldTable.length;
/* 305 */     if (oldCapacity >= 1073741824) {
/* 306 */       this.threshold = Integer.MAX_VALUE;
/* 307 */       return;
/*     */     }
/* 309 */     int newThreshold = 1 + (int)(newCapacity * this.loadFactor);
/* 310 */     int[] newTable = newTable(newCapacity);
/* 311 */     long[] entries = this.entries;
/*     */     
/* 313 */     int mask = newTable.length - 1;
/* 314 */     for (int i = 0; i < this.size; i++) {
/* 315 */       long oldEntry = entries[i];
/* 316 */       int hash = getHash(oldEntry);
/* 317 */       int tableIndex = hash & mask;
/* 318 */       int next = newTable[tableIndex];
/* 319 */       newTable[tableIndex] = i;
/* 320 */       entries[i] = (hash << 32 | 0xFFFFFFFF & next);
/*     */     }
/*     */     
/* 323 */     this.threshold = newThreshold;
/* 324 */     this.table = newTable;
/*     */   }
/*     */   
/*     */   public boolean contains(Object object)
/*     */   {
/* 329 */     int hash = Hashing.smearedHash(object);
/* 330 */     int next = this.table[(hash & hashTableMask())];
/* 331 */     while (next != -1) {
/* 332 */       long entry = this.entries[next];
/* 333 */       if ((getHash(entry) == hash) && (Objects.equal(object, this.elements[next]))) {
/* 334 */         return true;
/*     */       }
/* 336 */       next = getNext(entry);
/*     */     }
/* 338 */     return false;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object)
/*     */   {
/* 344 */     return remove(object, Hashing.smearedHash(object));
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private boolean remove(Object object, int hash) {
/* 349 */     int tableIndex = hash & hashTableMask();
/* 350 */     int next = this.table[tableIndex];
/* 351 */     if (next == -1) {
/* 352 */       return false;
/*     */     }
/* 354 */     int last = -1;
/*     */     do {
/* 356 */       if ((getHash(this.entries[next]) == hash) && (Objects.equal(object, this.elements[next]))) {
/* 357 */         if (last == -1)
/*     */         {
/* 359 */           this.table[tableIndex] = getNext(this.entries[next]);
/*     */         }
/*     */         else {
/* 362 */           this.entries[last] = swapNext(this.entries[last], getNext(this.entries[next]));
/*     */         }
/*     */         
/* 365 */         moveEntry(next);
/* 366 */         this.size -= 1;
/* 367 */         this.modCount += 1;
/* 368 */         return true;
/*     */       }
/* 370 */       last = next;
/* 371 */       next = getNext(this.entries[next]);
/* 372 */     } while (next != -1);
/* 373 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void moveEntry(int dstIndex)
/*     */   {
/* 380 */     int srcIndex = size() - 1;
/* 381 */     if (dstIndex < srcIndex)
/*     */     {
/* 383 */       this.elements[dstIndex] = this.elements[srcIndex];
/* 384 */       this.elements[srcIndex] = null;
/*     */       
/*     */ 
/* 387 */       long lastEntry = this.entries[srcIndex];
/* 388 */       this.entries[dstIndex] = lastEntry;
/* 389 */       this.entries[srcIndex] = -1L;
/*     */       
/*     */ 
/*     */ 
/* 393 */       int tableIndex = getHash(lastEntry) & hashTableMask();
/* 394 */       int lastNext = this.table[tableIndex];
/* 395 */       if (lastNext == srcIndex)
/*     */       {
/* 397 */         this.table[tableIndex] = dstIndex;
/*     */       }
/*     */       else {
/*     */         int previous;
/*     */         long entry;
/*     */         do {
/* 403 */           previous = lastNext;
/* 404 */           lastNext = getNext(entry = this.entries[lastNext]);
/* 405 */         } while (lastNext != srcIndex);
/*     */         
/* 407 */         this.entries[previous] = swapNext(entry, dstIndex);
/*     */       }
/*     */     } else {
/* 410 */       this.elements[dstIndex] = null;
/* 411 */       this.entries[dstIndex] = -1L;
/*     */     }
/*     */   }
/*     */   
/*     */   int firstEntryIndex() {
/* 416 */     return isEmpty() ? -1 : 0;
/*     */   }
/*     */   
/*     */   int getSuccessor(int entryIndex) {
/* 420 */     return entryIndex + 1 < this.size ? entryIndex + 1 : -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved)
/*     */   {
/* 429 */     return indexBeforeRemove - 1;
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator()
/*     */   {
/* 434 */     new Iterator() {
/* 435 */       int expectedModCount = CompactHashSet.this.modCount;
/* 436 */       int index = CompactHashSet.this.firstEntryIndex();
/* 437 */       int indexToRemove = -1;
/*     */       
/*     */       public boolean hasNext()
/*     */       {
/* 441 */         return this.index >= 0;
/*     */       }
/*     */       
/*     */ 
/*     */       public E next()
/*     */       {
/* 447 */         checkForConcurrentModification();
/* 448 */         if (!hasNext()) {
/* 449 */           throw new NoSuchElementException();
/*     */         }
/* 451 */         this.indexToRemove = this.index;
/* 452 */         E result = CompactHashSet.this.elements[this.index];
/* 453 */         this.index = CompactHashSet.this.getSuccessor(this.index);
/* 454 */         return result;
/*     */       }
/*     */       
/*     */       public void remove()
/*     */       {
/* 459 */         checkForConcurrentModification();
/* 460 */         CollectPreconditions.checkRemove(this.indexToRemove >= 0);
/* 461 */         this.expectedModCount += 1;
/* 462 */         CompactHashSet.this.remove(CompactHashSet.this.elements[this.indexToRemove], CompactHashSet.access$100(CompactHashSet.this.entries[this.indexToRemove]));
/* 463 */         this.index = CompactHashSet.this.adjustAfterRemove(this.index, this.indexToRemove);
/* 464 */         this.indexToRemove = -1;
/*     */       }
/*     */       
/*     */       private void checkForConcurrentModification() {
/* 468 */         if (CompactHashSet.this.modCount != this.expectedModCount) {
/* 469 */           throw new ConcurrentModificationException();
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public Spliterator<E> spliterator()
/*     */   {
/* 477 */     return Spliterators.spliterator(this.elements, 0, this.size, 17);
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<? super E> action)
/*     */   {
/* 482 */     Preconditions.checkNotNull(action);
/* 483 */     for (int i = 0; i < this.size; i++) {
/* 484 */       action.accept(this.elements[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 490 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 495 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/* 500 */     return Arrays.copyOf(this.elements, this.size);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] a)
/*     */   {
/* 506 */     return ObjectArrays.toArrayImpl(this.elements, 0, this.size, a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void trimToSize()
/*     */   {
/* 514 */     int size = this.size;
/* 515 */     if (size < this.entries.length) {
/* 516 */       resizeEntries(size);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 522 */     int minimumTableSize = Math.max(1, Integer.highestOneBit((int)(size / this.loadFactor)));
/* 523 */     if (minimumTableSize < 1073741824) {
/* 524 */       double load = size / minimumTableSize;
/* 525 */       if (load > this.loadFactor) {
/* 526 */         minimumTableSize <<= 1;
/*     */       }
/*     */     }
/*     */     
/* 530 */     if (minimumTableSize < this.table.length) {
/* 531 */       resizeTable(minimumTableSize);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 537 */     this.modCount += 1;
/* 538 */     Arrays.fill(this.elements, 0, this.size, null);
/* 539 */     Arrays.fill(this.table, -1);
/* 540 */     Arrays.fill(this.entries, -1L);
/* 541 */     this.size = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeObject(ObjectOutputStream stream)
/*     */     throws IOException
/*     */   {
/* 549 */     stream.defaultWriteObject();
/* 550 */     stream.writeInt(this.size);
/* 551 */     for (E e : this) {
/* 552 */       stream.writeObject(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
/*     */   {
/* 558 */     stream.defaultReadObject();
/* 559 */     init(3, 1.0F);
/* 560 */     int elementCount = stream.readInt();
/* 561 */     int i = elementCount; for (;;) { i--; if (i < 0) break;
/* 562 */       E element = stream.readObject();
/* 563 */       add(element);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\CompactHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */