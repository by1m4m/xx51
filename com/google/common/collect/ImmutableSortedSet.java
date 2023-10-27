/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators.AbstractSpliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable=true, emulated=true)
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements NavigableSet<E>, SortedIterable<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1301;
/*     */   final transient Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @GwtIncompatible
/*     */   transient ImmutableSortedSet<E> descendingSet;
/*     */   
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator)
/*     */   {
/*  81 */     return CollectCollectors.toImmutableSortedSet(comparator);
/*     */   }
/*     */   
/*     */   static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/*  85 */     if (Ordering.natural().equals(comparator)) {
/*  86 */       return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */     }
/*  88 */     return new RegularImmutableSortedSet(ImmutableList.of(), comparator);
/*     */   }
/*     */   
/*     */ 
/*     */   public static <E> ImmutableSortedSet<E> of()
/*     */   {
/*  94 */     return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */   }
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element)
/*     */   {
/*  99 */     return new RegularImmutableSortedSet(ImmutableList.of(element), Ordering.natural());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2)
/*     */   {
/* 111 */     return construct(Ordering.natural(), 2, new Comparable[] { e1, e2 });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3)
/*     */   {
/* 123 */     return construct(Ordering.natural(), 3, new Comparable[] { e1, e2, e3 });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4)
/*     */   {
/* 135 */     return construct(Ordering.natural(), 4, new Comparable[] { e1, e2, e3, e4 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5)
/*     */   {
/* 148 */     return construct(Ordering.natural(), 5, new Comparable[] { e1, e2, e3, e4, e5 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining)
/*     */   {
/* 162 */     Comparable[] contents = new Comparable[6 + remaining.length];
/* 163 */     contents[0] = e1;
/* 164 */     contents[1] = e2;
/* 165 */     contents[2] = e3;
/* 166 */     contents[3] = e4;
/* 167 */     contents[4] = e5;
/* 168 */     contents[5] = e6;
/* 169 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/* 170 */     return construct(Ordering.natural(), contents.length, (Comparable[])contents);
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements)
/*     */   {
/* 184 */     return construct(Ordering.natural(), elements.length, (Object[])elements.clone());
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements)
/*     */   {
/* 212 */     Ordering<E> naturalOrder = Ordering.natural();
/* 213 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements)
/*     */   {
/* 244 */     Ordering<E> naturalOrder = Ordering.natural();
/* 245 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements)
/*     */   {
/* 263 */     Ordering<E> naturalOrder = Ordering.natural();
/* 264 */     return copyOf(naturalOrder, elements);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements)
/*     */   {
/* 276 */     return new Builder(comparator).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements)
/*     */   {
/* 292 */     Preconditions.checkNotNull(comparator);
/* 293 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*     */     
/* 295 */     if ((hasSameComparator) && ((elements instanceof ImmutableSortedSet)))
/*     */     {
/* 297 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/* 298 */       if (!original.isPartialView()) {
/* 299 */         return original;
/*     */       }
/*     */     }
/*     */     
/* 303 */     E[] array = (Object[])Iterables.toArray(elements);
/* 304 */     return construct(comparator, array.length, array);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements)
/*     */   {
/* 324 */     return copyOf(comparator, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet)
/*     */   {
/* 342 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/* 343 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/* 344 */     if (list.isEmpty()) {
/* 345 */       return emptySet(comparator);
/*     */     }
/* 347 */     return new RegularImmutableSortedSet(list, comparator);
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
/*     */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents)
/*     */   {
/* 364 */     if (n == 0) {
/* 365 */       return emptySet(comparator);
/*     */     }
/* 367 */     ObjectArrays.checkElementsNotNull(contents, n);
/* 368 */     Arrays.sort(contents, 0, n, comparator);
/* 369 */     int uniques = 1;
/* 370 */     for (int i = 1; i < n; i++) {
/* 371 */       E cur = contents[i];
/* 372 */       E prev = contents[(uniques - 1)];
/* 373 */       if (comparator.compare(cur, prev) != 0) {
/* 374 */         contents[(uniques++)] = cur;
/*     */       }
/*     */     }
/* 377 */     Arrays.fill(contents, uniques, n, null);
/* 378 */     return new RegularImmutableSortedSet(
/* 379 */       ImmutableList.asImmutableList(contents, uniques), comparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator)
/*     */   {
/* 391 */     return new Builder(comparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder()
/*     */   {
/* 399 */     return new Builder(Collections.reverseOrder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder()
/*     */   {
/* 409 */     return new Builder(Ordering.natural());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private E[] elements;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private int n;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder(Comparator<? super E> comparator)
/*     */     {
/* 439 */       super();
/* 440 */       this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
/* 441 */       this.elements = ((Object[])new Object[4]);
/* 442 */       this.n = 0;
/*     */     }
/*     */     
/*     */     void copy()
/*     */     {
/* 447 */       this.elements = Arrays.copyOf(this.elements, this.elements.length);
/*     */     }
/*     */     
/*     */     private void sortAndDedup() {
/* 451 */       if (this.n == 0) {
/* 452 */         return;
/*     */       }
/* 454 */       Arrays.sort(this.elements, 0, this.n, this.comparator);
/* 455 */       int unique = 1;
/* 456 */       for (int i = 1; i < this.n; i++) {
/* 457 */         int cmp = this.comparator.compare(this.elements[(unique - 1)], this.elements[i]);
/* 458 */         if (cmp < 0) {
/* 459 */           this.elements[(unique++)] = this.elements[i];
/* 460 */         } else if (cmp > 0) {
/* 461 */           throw new AssertionError("Comparator " + this.comparator + " compare method violates its contract");
/*     */         }
/*     */       }
/*     */       
/* 465 */       Arrays.fill(this.elements, unique, this.n, null);
/* 466 */       this.n = unique;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element)
/*     */     {
/* 481 */       Preconditions.checkNotNull(element);
/* 482 */       copyIfNecessary();
/* 483 */       if (this.n == this.elements.length) {
/* 484 */         sortAndDedup();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 490 */         int newLength = ImmutableCollection.Builder.expandedCapacity(this.n, this.n + 1);
/* 491 */         if (newLength > this.elements.length) {
/* 492 */           this.elements = Arrays.copyOf(this.elements, newLength);
/*     */         }
/*     */       }
/* 495 */       this.elements[(this.n++)] = element;
/* 496 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements)
/*     */     {
/* 510 */       ObjectArrays.checkElementsNotNull(elements);
/* 511 */       for (E e : elements) {
/* 512 */         add(e);
/*     */       }
/* 514 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements)
/*     */     {
/* 528 */       super.addAll(elements);
/* 529 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements)
/*     */     {
/* 543 */       super.addAll(elements);
/* 544 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableSet.Builder<E> builder)
/*     */     {
/* 550 */       copyIfNecessary();
/* 551 */       Builder<E> other = (Builder)builder;
/* 552 */       for (int i = 0; i < other.n; i++) {
/* 553 */         add(other.elements[i]);
/*     */       }
/* 555 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableSortedSet<E> build()
/*     */     {
/* 564 */       sortAndDedup();
/* 565 */       if (this.n == 0) {
/* 566 */         return ImmutableSortedSet.emptySet(this.comparator);
/*     */       }
/* 568 */       this.forceCopy = true;
/* 569 */       return new RegularImmutableSortedSet(
/* 570 */         ImmutableList.asImmutableList(this.elements, this.n), this.comparator);
/*     */     }
/*     */   }
/*     */   
/*     */   int unsafeCompare(Object a, Object b)
/*     */   {
/* 576 */     return unsafeCompare(this.comparator, a, b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b)
/*     */   {
/* 584 */     Comparator<Object> unsafeComparator = comparator;
/* 585 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */   
/*     */ 
/*     */   ImmutableSortedSet(Comparator<? super E> comparator)
/*     */   {
/* 591 */     this.comparator = comparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 601 */     return this.comparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableSortedSet<E> headSet(E toElement)
/*     */   {
/* 619 */     return headSet(toElement, false);
/*     */   }
/*     */   
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive)
/*     */   {
/* 626 */     return headSetImpl(Preconditions.checkNotNull(toElement), inclusive);
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
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement)
/*     */   {
/* 643 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*     */   {
/* 651 */     Preconditions.checkNotNull(fromElement);
/* 652 */     Preconditions.checkNotNull(toElement);
/* 653 */     Preconditions.checkArgument(this.comparator.compare(fromElement, toElement) <= 0);
/* 654 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement)
/*     */   {
/* 669 */     return tailSet(fromElement, true);
/*     */   }
/*     */   
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive)
/*     */   {
/* 676 */     return tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*     */   
/*     */ 
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E lower(E e)
/*     */   {
/* 694 */     return (E)Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */   
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E floor(E e)
/*     */   {
/* 701 */     return (E)Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */   
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E ceiling(E e)
/*     */   {
/* 708 */     return (E)Iterables.getFirst(tailSet(e, true), null);
/*     */   }
/*     */   
/*     */ 
/*     */   @GwtIncompatible
/*     */   public E higher(E e)
/*     */   {
/* 715 */     return (E)Iterables.getFirst(tailSet(e, false), null);
/*     */   }
/*     */   
/*     */   public E first()
/*     */   {
/* 720 */     return (E)iterator().next();
/*     */   }
/*     */   
/*     */   public E last()
/*     */   {
/* 725 */     return (E)descendingIterator().next();
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollFirst()
/*     */   {
/* 740 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollLast()
/*     */   {
/* 755 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> descendingSet()
/*     */   {
/* 767 */     ImmutableSortedSet<E> result = this.descendingSet;
/* 768 */     if (result == null) {
/* 769 */       result = this.descendingSet = createDescendingSet();
/* 770 */       result.descendingSet = this;
/*     */     }
/* 772 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   abstract ImmutableSortedSet<E> createDescendingSet();
/*     */   
/*     */ 
/*     */   public Spliterator<E> spliterator()
/*     */   {
/* 783 */     new Spliterators.AbstractSpliterator(
/* 784 */       size(), 1365)
/*     */       {
/* 785 */         final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
/*     */         
/*     */         public boolean tryAdvance(Consumer<? super E> action)
/*     */         {
/* 789 */           if (this.iterator.hasNext()) {
/* 790 */             action.accept(this.iterator.next());
/* 791 */             return true;
/*     */           }
/* 793 */           return false;
/*     */         }
/*     */         
/*     */ 
/*     */         public Comparator<? super E> getComparator()
/*     */         {
/* 799 */           return ImmutableSortedSet.this.comparator;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */     @GwtIncompatible
/*     */     public abstract UnmodifiableIterator<E> descendingIterator();
/*     */     
/*     */ 
/*     */     abstract int indexOf(Object paramObject);
/*     */     
/*     */ 
/*     */     private static class SerializedForm<E>
/*     */       implements Serializable
/*     */     {
/*     */       final Comparator<? super E> comparator;
/*     */       
/*     */       final Object[] elements;
/*     */       
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */       public SerializedForm(Comparator<? super E> comparator, Object[] elements)
/*     */       {
/* 823 */         this.comparator = comparator;
/* 824 */         this.elements = elements;
/*     */       }
/*     */       
/*     */       Object readResolve()
/*     */       {
/* 829 */         return new ImmutableSortedSet.Builder(this.comparator).add((Object[])this.elements).build();
/*     */       }
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream stream)
/*     */       throws InvalidObjectException
/*     */     {
/* 836 */       throw new InvalidObjectException("Use SerializedForm");
/*     */     }
/*     */     
/*     */     Object writeReplace()
/*     */     {
/* 841 */       return new SerializedForm(this.comparator, toArray());
/*     */     }
/*     */   }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */