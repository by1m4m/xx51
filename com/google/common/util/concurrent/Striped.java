/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ public abstract class Striped<L>
/*     */ {
/*     */   private static final int LARGE_LAZY_CUTOFF = 1024;
/*     */   
/*     */   public abstract L get(Object paramObject);
/*     */   
/*     */   public abstract L getAt(int paramInt);
/*     */   
/*     */   abstract int indexFor(Object paramObject);
/*     */   
/*     */   public abstract int size();
/*     */   
/*     */   public Iterable<L> bulkGet(Iterable<?> keys)
/*     */   {
/* 141 */     Object[] array = Iterables.toArray(keys, Object.class);
/* 142 */     if (array.length == 0) {
/* 143 */       return ImmutableList.of();
/*     */     }
/* 145 */     int[] stripes = new int[array.length];
/* 146 */     for (int i = 0; i < array.length; i++) {
/* 147 */       stripes[i] = indexFor(array[i]);
/*     */     }
/* 149 */     Arrays.sort(stripes);
/*     */     
/* 151 */     int previousStripe = stripes[0];
/* 152 */     array[0] = getAt(previousStripe);
/* 153 */     for (int i = 1; i < array.length; i++) {
/* 154 */       int currentStripe = stripes[i];
/* 155 */       if (currentStripe == previousStripe) {
/* 156 */         array[i] = array[(i - 1)];
/*     */       } else {
/* 158 */         array[i] = getAt(currentStripe);
/* 159 */         previousStripe = currentStripe;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */     List<L> asList = Arrays.asList(array);
/* 181 */     return Collections.unmodifiableList(asList);
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
/*     */   static <L> Striped<L> custom(int stripes, Supplier<L> supplier)
/*     */   {
/* 195 */     return new CompactStriped(stripes, supplier, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<Lock> lock(int stripes)
/*     */   {
/* 206 */     custom(stripes, new Supplier()
/*     */     {
/*     */       public Lock get() {
/* 209 */         return new Striped.PaddedLock();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<Lock> lazyWeakLock(int stripes)
/*     */   {
/* 222 */     lazy(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */       public Lock get()
/*     */       {
/* 227 */         return new ReentrantLock(false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
/* 233 */     return stripes < 1024 ? new SmallLazyStriped(stripes, supplier) : new LargeLazyStriped(stripes, supplier);
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
/*     */   public static Striped<Semaphore> semaphore(int stripes, int permits)
/*     */   {
/* 247 */     custom(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */       public Semaphore get()
/*     */       {
/* 252 */         return new Striped.PaddedSemaphore(this.val$permits);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<Semaphore> lazyWeakSemaphore(int stripes, int permits)
/*     */   {
/* 266 */     lazy(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */       public Semaphore get()
/*     */       {
/* 271 */         return new Semaphore(this.val$permits, false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<ReadWriteLock> readWriteLock(int stripes)
/*     */   {
/* 284 */     return custom(stripes, READ_WRITE_LOCK_SUPPLIER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes)
/*     */   {
/* 295 */     return lazy(stripes, WEAK_SAFE_READ_WRITE_LOCK_SUPPLIER);
/*     */   }
/*     */   
/* 298 */   private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new Supplier()
/*     */   {
/*     */     public ReadWriteLock get()
/*     */     {
/* 302 */       return new ReentrantReadWriteLock();
/*     */     }
/*     */   };
/*     */   
/* 306 */   private static final Supplier<ReadWriteLock> WEAK_SAFE_READ_WRITE_LOCK_SUPPLIER = new Supplier()
/*     */   {
/*     */     public ReadWriteLock get()
/*     */     {
/* 310 */       return new Striped.WeakSafeReadWriteLock();
/*     */     }
/*     */   };
/*     */   
/*     */   private static final int ALL_SET = -1;
/*     */   
/*     */   private static final class WeakSafeReadWriteLock
/*     */     implements ReadWriteLock
/*     */   {
/*     */     private final ReadWriteLock delegate;
/*     */     
/*     */     WeakSafeReadWriteLock()
/*     */     {
/* 323 */       this.delegate = new ReentrantReadWriteLock();
/*     */     }
/*     */     
/*     */     public Lock readLock()
/*     */     {
/* 328 */       return new Striped.WeakSafeLock(this.delegate.readLock(), this);
/*     */     }
/*     */     
/*     */     public Lock writeLock()
/*     */     {
/* 333 */       return new Striped.WeakSafeLock(this.delegate.writeLock(), this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class WeakSafeLock
/*     */     extends ForwardingLock
/*     */   {
/*     */     private final Lock delegate;
/*     */     private final Striped.WeakSafeReadWriteLock strongReference;
/*     */     
/*     */     WeakSafeLock(Lock delegate, Striped.WeakSafeReadWriteLock strongReference)
/*     */     {
/* 345 */       this.delegate = delegate;
/* 346 */       this.strongReference = strongReference;
/*     */     }
/*     */     
/*     */     Lock delegate()
/*     */     {
/* 351 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public Condition newCondition()
/*     */     {
/* 356 */       return new Striped.WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class WeakSafeCondition
/*     */     extends ForwardingCondition
/*     */   {
/*     */     private final Condition delegate;
/*     */     private final Striped.WeakSafeReadWriteLock strongReference;
/*     */     
/*     */     WeakSafeCondition(Condition delegate, Striped.WeakSafeReadWriteLock strongReference)
/*     */     {
/* 368 */       this.delegate = delegate;
/* 369 */       this.strongReference = strongReference;
/*     */     }
/*     */     
/*     */     Condition delegate()
/*     */     {
/* 374 */       return this.delegate;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
/*     */     final int mask;
/*     */     
/*     */     PowerOfTwoStriped(int stripes) {
/* 382 */       super();
/* 383 */       Preconditions.checkArgument(stripes > 0, "Stripes must be positive");
/* 384 */       this.mask = (stripes > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(stripes) - 1);
/*     */     }
/*     */     
/*     */     final int indexFor(Object key)
/*     */     {
/* 389 */       int hash = Striped.smear(key.hashCode());
/* 390 */       return hash & this.mask;
/*     */     }
/*     */     
/*     */     public final L get(Object key)
/*     */     {
/* 395 */       return (L)getAt(indexFor(key));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class CompactStriped<L>
/*     */     extends Striped.PowerOfTwoStriped<L>
/*     */   {
/*     */     private final Object[] array;
/*     */     
/*     */ 
/*     */     private CompactStriped(int stripes, Supplier<L> supplier)
/*     */     {
/* 408 */       super();
/* 409 */       Preconditions.checkArgument(stripes <= 1073741824, "Stripes must be <= 2^30)");
/*     */       
/* 411 */       this.array = new Object[this.mask + 1];
/* 412 */       for (int i = 0; i < this.array.length; i++) {
/* 413 */         this.array[i] = supplier.get();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public L getAt(int index)
/*     */     {
/* 420 */       return (L)this.array[index];
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 425 */       return this.array.length;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class SmallLazyStriped<L>
/*     */     extends Striped.PowerOfTwoStriped<L>
/*     */   {
/*     */     final AtomicReferenceArray<ArrayReference<? extends L>> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     
/*     */     final int size;
/* 439 */     final ReferenceQueue<L> queue = new ReferenceQueue();
/*     */     
/*     */     SmallLazyStriped(int stripes, Supplier<L> supplier) {
/* 442 */       super();
/* 443 */       this.size = (this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1);
/* 444 */       this.locks = new AtomicReferenceArray(this.size);
/* 445 */       this.supplier = supplier;
/*     */     }
/*     */     
/*     */     public L getAt(int index)
/*     */     {
/* 450 */       if (this.size != Integer.MAX_VALUE) {
/* 451 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 453 */       ArrayReference<? extends L> existingRef = (ArrayReference)this.locks.get(index);
/* 454 */       L existing = existingRef == null ? null : existingRef.get();
/* 455 */       if (existing != null) {
/* 456 */         return existing;
/*     */       }
/* 458 */       L created = this.supplier.get();
/* 459 */       ArrayReference<L> newRef = new ArrayReference(created, index, this.queue);
/* 460 */       while (!this.locks.compareAndSet(index, existingRef, newRef))
/*     */       {
/* 462 */         existingRef = (ArrayReference)this.locks.get(index);
/* 463 */         existing = existingRef == null ? null : existingRef.get();
/* 464 */         if (existing != null) {
/* 465 */           return existing;
/*     */         }
/*     */       }
/* 468 */       drainQueue();
/* 469 */       return created;
/*     */     }
/*     */     
/*     */ 
/*     */     private void drainQueue()
/*     */     {
/*     */       Reference<? extends L> ref;
/*     */       
/* 477 */       while ((ref = this.queue.poll()) != null)
/*     */       {
/* 479 */         ArrayReference<? extends L> arrayRef = (ArrayReference)ref;
/*     */         
/*     */ 
/* 482 */         this.locks.compareAndSet(arrayRef.index, arrayRef, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 488 */       return this.size;
/*     */     }
/*     */     
/*     */     private static final class ArrayReference<L> extends WeakReference<L> {
/*     */       final int index;
/*     */       
/*     */       ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
/* 495 */         super(queue);
/* 496 */         this.index = index;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class LargeLazyStriped<L>
/*     */     extends Striped.PowerOfTwoStriped<L>
/*     */   {
/*     */     final ConcurrentMap<Integer, L> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     final int size;
/*     */     
/*     */     LargeLazyStriped(int stripes, Supplier<L> supplier)
/*     */     {
/* 513 */       super();
/* 514 */       this.size = (this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1);
/* 515 */       this.supplier = supplier;
/* 516 */       this.locks = new MapMaker().weakValues().makeMap();
/*     */     }
/*     */     
/*     */     public L getAt(int index)
/*     */     {
/* 521 */       if (this.size != Integer.MAX_VALUE) {
/* 522 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 524 */       L existing = this.locks.get(Integer.valueOf(index));
/* 525 */       if (existing != null) {
/* 526 */         return existing;
/*     */       }
/* 528 */       L created = this.supplier.get();
/* 529 */       existing = this.locks.putIfAbsent(Integer.valueOf(index), created);
/* 530 */       return (L)MoreObjects.firstNonNull(existing, created);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 535 */       return this.size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int ceilToPowerOfTwo(int x)
/*     */   {
/* 543 */     return 1 << IntMath.log2(x, RoundingMode.CEILING);
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
/*     */   private static int smear(int hashCode)
/*     */   {
/* 556 */     hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
/* 557 */     return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class PaddedLock
/*     */     extends ReentrantLock
/*     */   {
/*     */     long unused1;
/*     */     
/*     */     long unused2;
/*     */     long unused3;
/*     */     
/*     */     PaddedLock()
/*     */     {
/* 571 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PaddedSemaphore extends Semaphore
/*     */   {
/*     */     long unused1;
/*     */     long unused2;
/*     */     long unused3;
/*     */     
/*     */     PaddedSemaphore(int permits) {
/* 582 */       super(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\Striped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */