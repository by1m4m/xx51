/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class Ordering<T>
/*     */   implements Comparator<T>
/*     */ {
/*     */   static final int LEFT_IS_GREATER = 1;
/*     */   static final int RIGHT_IS_GREATER = -1;
/*     */   
/*     */   @GwtCompatible(serializable=true)
/*     */   public static <C extends Comparable> Ordering<C> natural()
/*     */   {
/* 159 */     return NaturalOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public static <T> Ordering<T> from(Comparator<T> comparator)
/*     */   {
/* 179 */     return (comparator instanceof Ordering) ? (Ordering)comparator : new ComparatorOrdering(comparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @GwtCompatible(serializable=true)
/*     */   public static <T> Ordering<T> from(Ordering<T> ordering)
/*     */   {
/* 192 */     return (Ordering)Preconditions.checkNotNull(ordering);
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public static <T> Ordering<T> explicit(List<T> valuesInOrder)
/*     */   {
/* 217 */     return new ExplicitOrdering(valuesInOrder);
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder)
/*     */   {
/* 243 */     return explicit(Lists.asList(leastValue, remainingValuesInOrder));
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public static Ordering<Object> allEqual()
/*     */   {
/* 279 */     return AllEqualOrdering.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtCompatible(serializable=true)
/*     */   public static Ordering<Object> usingToString()
/*     */   {
/* 292 */     return UsingToStringOrdering.INSTANCE;
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
/*     */   public static Ordering<Object> arbitrary()
/*     */   {
/* 312 */     return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
/*     */   }
/*     */   
/*     */   private static class ArbitraryOrderingHolder {
/* 316 */     static final Ordering<Object> ARBITRARY_ORDERING = new Ordering.ArbitraryOrdering();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ArbitraryOrdering extends Ordering<Object> { private final AtomicInteger counter;
/*     */     
/* 322 */     ArbitraryOrdering() { this.counter = new AtomicInteger(0); }
/*     */     
/* 324 */     private final ConcurrentMap<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeMap();
/*     */     
/*     */     private Integer getUid(Object obj) {
/* 327 */       Integer uid = (Integer)this.uids.get(obj);
/* 328 */       if (uid == null)
/*     */       {
/*     */ 
/*     */ 
/* 332 */         uid = Integer.valueOf(this.counter.getAndIncrement());
/* 333 */         Integer alreadySet = (Integer)this.uids.putIfAbsent(obj, uid);
/* 334 */         if (alreadySet != null) {
/* 335 */           uid = alreadySet;
/*     */         }
/*     */       }
/* 338 */       return uid;
/*     */     }
/*     */     
/*     */     public int compare(Object left, Object right)
/*     */     {
/* 343 */       if (left == right)
/* 344 */         return 0;
/* 345 */       if (left == null)
/* 346 */         return -1;
/* 347 */       if (right == null) {
/* 348 */         return 1;
/*     */       }
/* 350 */       int leftCode = identityHashCode(left);
/* 351 */       int rightCode = identityHashCode(right);
/* 352 */       if (leftCode != rightCode) {
/* 353 */         return leftCode < rightCode ? -1 : 1;
/*     */       }
/*     */       
/*     */ 
/* 357 */       int result = getUid(left).compareTo(getUid(right));
/* 358 */       if (result == 0) {
/* 359 */         throw new AssertionError();
/*     */       }
/* 361 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 366 */       return "Ordering.arbitrary()";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int identityHashCode(Object object)
/*     */     {
/* 378 */       return System.identityHashCode(object);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtCompatible(serializable=true)
/*     */   public <S extends T> Ordering<S> reverse()
/*     */   {
/* 402 */     return new ReverseOrdering(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtCompatible(serializable=true)
/*     */   public <S extends T> Ordering<S> nullsFirst()
/*     */   {
/* 415 */     return new NullsFirstOrdering(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtCompatible(serializable=true)
/*     */   public <S extends T> Ordering<S> nullsLast()
/*     */   {
/* 428 */     return new NullsLastOrdering(this);
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public <F> Ordering<F> onResultOf(Function<F, ? extends T> function)
/*     */   {
/* 446 */     return new ByFunctionOrdering(function, this);
/*     */   }
/*     */   
/*     */   <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
/* 450 */     return onResultOf(Maps.keyFunction());
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator)
/*     */   {
/* 468 */     return new CompoundOrdering(this, (Comparator)Preconditions.checkNotNull(secondaryComparator));
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators)
/*     */   {
/* 492 */     return new CompoundOrdering(comparators);
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
/*     */   @GwtCompatible(serializable=true)
/*     */   public <S extends T> Ordering<Iterable<S>> lexicographical()
/*     */   {
/* 522 */     return new LexicographicalOrdering(this);
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
/*     */   @CanIgnoreReturnValue
/*     */   public abstract int compare(T paramT1, T paramT2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(Iterator<E> iterator)
/*     */   {
/* 550 */     E minSoFar = iterator.next();
/*     */     
/* 552 */     while (iterator.hasNext()) {
/* 553 */       minSoFar = min(minSoFar, iterator.next());
/*     */     }
/*     */     
/* 556 */     return minSoFar;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(Iterable<E> iterable)
/*     */   {
/* 576 */     return (E)min(iterable.iterator());
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(E a, E b)
/*     */   {
/* 596 */     return compare(a, b) <= 0 ? a : b;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(E a, E b, E c, E... rest)
/*     */   {
/* 615 */     E minSoFar = min(min(a, b), c);
/*     */     
/* 617 */     for (E r : rest) {
/* 618 */       minSoFar = min(minSoFar, r);
/*     */     }
/*     */     
/* 621 */     return minSoFar;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(Iterator<E> iterator)
/*     */   {
/* 642 */     E maxSoFar = iterator.next();
/*     */     
/* 644 */     while (iterator.hasNext()) {
/* 645 */       maxSoFar = max(maxSoFar, iterator.next());
/*     */     }
/*     */     
/* 648 */     return maxSoFar;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(Iterable<E> iterable)
/*     */   {
/* 668 */     return (E)max(iterable.iterator());
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(E a, E b)
/*     */   {
/* 688 */     return compare(a, b) >= 0 ? a : b;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(E a, E b, E c, E... rest)
/*     */   {
/* 707 */     E maxSoFar = max(max(a, b), c);
/*     */     
/* 709 */     for (E r : rest) {
/* 710 */       maxSoFar = max(maxSoFar, r);
/*     */     }
/*     */     
/* 713 */     return maxSoFar;
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
/*     */   public <E extends T> List<E> leastOf(Iterable<E> iterable, int k)
/*     */   {
/* 733 */     if ((iterable instanceof Collection)) {
/* 734 */       Collection<E> collection = (Collection)iterable;
/* 735 */       if (collection.size() <= 2L * k)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 741 */         E[] array = (Object[])collection.toArray();
/* 742 */         Arrays.sort(array, this);
/* 743 */         if (array.length > k) {
/* 744 */           array = Arrays.copyOf(array, k);
/*     */         }
/* 746 */         return Collections.unmodifiableList(Arrays.asList(array));
/*     */       }
/*     */     }
/* 749 */     return leastOf(iterable.iterator(), k);
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
/*     */   public <E extends T> List<E> leastOf(Iterator<E> iterator, int k)
/*     */   {
/* 769 */     Preconditions.checkNotNull(iterator);
/* 770 */     CollectPreconditions.checkNonnegative(k, "k");
/*     */     
/* 772 */     if ((k == 0) || (!iterator.hasNext()))
/* 773 */       return Collections.emptyList();
/* 774 */     if (k >= 1073741823)
/*     */     {
/* 776 */       ArrayList<E> list = Lists.newArrayList(iterator);
/* 777 */       Collections.sort(list, this);
/* 778 */       if (list.size() > k) {
/* 779 */         list.subList(k, list.size()).clear();
/*     */       }
/* 781 */       list.trimToSize();
/* 782 */       return Collections.unmodifiableList(list);
/*     */     }
/* 784 */     TopKSelector<E> selector = TopKSelector.least(k, this);
/* 785 */     selector.offerAll(iterator);
/* 786 */     return selector.topK();
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
/*     */   public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k)
/*     */   {
/* 809 */     return reverse().leastOf(iterable, k);
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
/*     */   public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k)
/*     */   {
/* 829 */     return reverse().leastOf(iterator, k);
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> List<E> sortedCopy(Iterable<E> elements)
/*     */   {
/* 851 */     E[] array = (Object[])Iterables.toArray(elements);
/* 852 */     Arrays.sort(array, this);
/* 853 */     return Lists.newArrayList(Arrays.asList(array));
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements)
/*     */   {
/* 874 */     return ImmutableList.sortedCopyOf(this, elements);
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
/*     */   public boolean isOrdered(Iterable<? extends T> iterable)
/*     */   {
/* 887 */     Iterator<? extends T> it = iterable.iterator();
/* 888 */     if (it.hasNext()) {
/* 889 */       T prev = it.next();
/* 890 */       while (it.hasNext()) {
/* 891 */         T next = it.next();
/* 892 */         if (compare(prev, next) > 0) {
/* 893 */           return false;
/*     */         }
/* 895 */         prev = next;
/*     */       }
/*     */     }
/* 898 */     return true;
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
/*     */   public boolean isStrictlyOrdered(Iterable<? extends T> iterable)
/*     */   {
/* 911 */     Iterator<? extends T> it = iterable.iterator();
/* 912 */     if (it.hasNext()) {
/* 913 */       T prev = it.next();
/* 914 */       while (it.hasNext()) {
/* 915 */         T next = it.next();
/* 916 */         if (compare(prev, next) >= 0) {
/* 917 */           return false;
/*     */         }
/* 919 */         prev = next;
/*     */       }
/*     */     }
/* 922 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int binarySearch(List<? extends T> sortedList, T key)
/*     */   {
/* 935 */     return Collections.binarySearch(sortedList, key, this);
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class IncomparableValueException
/*     */     extends ClassCastException
/*     */   {
/*     */     final Object value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IncomparableValueException(Object value)
/*     */     {
/* 948 */       super();
/* 949 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\Ordering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */