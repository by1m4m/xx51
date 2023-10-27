/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ImmutableRangeSet<C extends Comparable>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*  53 */   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(
/*  54 */     ImmutableList.of());
/*     */   
/*  56 */   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(
/*  57 */     ImmutableList.of(Range.all()));
/*     */   
/*     */ 
/*     */   private final transient ImmutableList<Range<C>> ranges;
/*     */   
/*     */   @LazyInit
/*     */   private transient ImmutableRangeSet<C> complement;
/*     */   
/*     */ 
/*     */   @Beta
/*     */   public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet()
/*     */   {
/*  69 */     return CollectCollectors.toImmutableRangeSet();
/*     */   }
/*     */   
/*     */ 
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of()
/*     */   {
/*  75 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range)
/*     */   {
/*  83 */     Preconditions.checkNotNull(range);
/*  84 */     if (range.isEmpty())
/*  85 */       return of();
/*  86 */     if (range.equals(Range.all())) {
/*  87 */       return all();
/*     */     }
/*  89 */     return new ImmutableRangeSet(ImmutableList.of(range));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static <C extends Comparable> ImmutableRangeSet<C> all()
/*     */   {
/*  96 */     return ALL;
/*     */   }
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet)
/*     */   {
/* 101 */     Preconditions.checkNotNull(rangeSet);
/* 102 */     if (rangeSet.isEmpty())
/* 103 */       return of();
/* 104 */     if (rangeSet.encloses(Range.all())) {
/* 105 */       return all();
/*     */     }
/*     */     
/* 108 */     if ((rangeSet instanceof ImmutableRangeSet)) {
/* 109 */       ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet)rangeSet;
/* 110 */       if (!immutableRangeSet.isPartialView()) {
/* 111 */         return immutableRangeSet;
/*     */       }
/*     */     }
/* 114 */     return new ImmutableRangeSet(ImmutableList.copyOf(rangeSet.asRanges()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges)
/*     */   {
/* 126 */     return new Builder().addAll(ranges).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges)
/*     */   {
/* 138 */     return copyOf(TreeRangeSet.create(ranges));
/*     */   }
/*     */   
/*     */   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
/* 142 */     this.ranges = ranges;
/*     */   }
/*     */   
/*     */   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
/* 146 */     this.ranges = ranges;
/* 147 */     this.complement = complement;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean intersects(Range<C> otherRange)
/*     */   {
/* 155 */     int ceilingIndex = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 157 */       Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */       
/* 159 */       Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */ 
/* 162 */     if ((ceilingIndex < this.ranges.size()) && 
/* 163 */       (((Range)this.ranges.get(ceilingIndex)).isConnected(otherRange)) && 
/* 164 */       (!((Range)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty())) {
/* 165 */       return true;
/*     */     }
/* 167 */     return (ceilingIndex > 0) && 
/* 168 */       (((Range)this.ranges.get(ceilingIndex - 1)).isConnected(otherRange)) && 
/* 169 */       (!((Range)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean encloses(Range<C> otherRange)
/*     */   {
/* 175 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 177 */       Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */       
/* 179 */       Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 182 */     return (index != -1) && (((Range)this.ranges.get(index)).encloses(otherRange));
/*     */   }
/*     */   
/*     */ 
/*     */   public Range<C> rangeContaining(C value)
/*     */   {
/* 188 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 190 */       Range.lowerBoundFn(), 
/* 191 */       Cut.belowValue(value), 
/* 192 */       Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 195 */     if (index != -1) {
/* 196 */       Range<C> range = (Range)this.ranges.get(index);
/* 197 */       return range.contains(value) ? range : null;
/*     */     }
/* 199 */     return null;
/*     */   }
/*     */   
/*     */   public Range<C> span()
/*     */   {
/* 204 */     if (this.ranges.isEmpty()) {
/* 205 */       throw new NoSuchElementException();
/*     */     }
/* 207 */     return Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 212 */     return this.ranges.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void add(Range<C> range)
/*     */   {
/* 224 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void addAll(RangeSet<C> other)
/*     */   {
/* 236 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void addAll(Iterable<Range<C>> other)
/*     */   {
/* 248 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void remove(Range<C> range)
/*     */   {
/* 260 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void removeAll(RangeSet<C> other)
/*     */   {
/* 272 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void removeAll(Iterable<Range<C>> other)
/*     */   {
/* 284 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ImmutableSet<Range<C>> asRanges()
/*     */   {
/* 289 */     if (this.ranges.isEmpty()) {
/* 290 */       return ImmutableSet.of();
/*     */     }
/* 292 */     return new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering());
/*     */   }
/*     */   
/*     */   public ImmutableSet<Range<C>> asDescendingSetOfRanges()
/*     */   {
/* 297 */     if (this.ranges.isEmpty()) {
/* 298 */       return ImmutableSet.of();
/*     */     }
/* 300 */     return new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse());
/*     */   }
/*     */   
/*     */ 
/*     */   private final class ComplementRanges
/*     */     extends ImmutableList<Range<C>>
/*     */   {
/*     */     private final boolean positiveBoundedBelow;
/*     */     
/*     */     private final boolean positiveBoundedAbove;
/*     */     
/*     */     private final int size;
/*     */     
/*     */     ComplementRanges()
/*     */     {
/* 315 */       this.positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
/* 316 */       this.positiveBoundedAbove = ((Range)Iterables.getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
/*     */       
/* 318 */       int size = ImmutableRangeSet.this.ranges.size() - 1;
/* 319 */       if (this.positiveBoundedBelow) {
/* 320 */         size++;
/*     */       }
/* 322 */       if (this.positiveBoundedAbove) {
/* 323 */         size++;
/*     */       }
/* 325 */       this.size = size;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 330 */       return this.size;
/*     */     }
/*     */     
/*     */     public Range<C> get(int index)
/*     */     {
/* 335 */       Preconditions.checkElementIndex(index, this.size);
/*     */       Cut<C> lowerBound;
/*     */       Cut<C> lowerBound;
/* 338 */       if (this.positiveBoundedBelow) {
/* 339 */         lowerBound = index == 0 ? Cut.belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
/*     */       } else {
/* 341 */         lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
/*     */       }
/*     */       Cut<C> upperBound;
/*     */       Cut<C> upperBound;
/* 345 */       if ((this.positiveBoundedAbove) && (index == this.size - 1)) {
/* 346 */         upperBound = Cut.aboveAll();
/*     */       } else {
/* 348 */         upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
/*     */       }
/*     */       
/* 351 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 356 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public ImmutableRangeSet<C> complement()
/*     */   {
/* 362 */     ImmutableRangeSet<C> result = this.complement;
/* 363 */     if (result != null)
/* 364 */       return result;
/* 365 */     if (this.ranges.isEmpty())
/* 366 */       return this.complement = all();
/* 367 */     if ((this.ranges.size() == 1) && (((Range)this.ranges.get(0)).equals(Range.all()))) {
/* 368 */       return this.complement = of();
/*     */     }
/* 370 */     ImmutableList<Range<C>> complementRanges = new ComplementRanges();
/* 371 */     result = this.complement = new ImmutableRangeSet(complementRanges, this);
/*     */     
/* 373 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableRangeSet<C> union(RangeSet<C> other)
/*     */   {
/* 385 */     return unionOf(Iterables.concat(asRanges(), other.asRanges()));
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
/*     */   public ImmutableRangeSet<C> intersection(RangeSet<C> other)
/*     */   {
/* 398 */     RangeSet<C> copy = TreeRangeSet.create(this);
/* 399 */     copy.removeAll(other.complement());
/* 400 */     return copyOf(copy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableRangeSet<C> difference(RangeSet<C> other)
/*     */   {
/* 412 */     RangeSet<C> copy = TreeRangeSet.create(this);
/* 413 */     copy.removeAll(other);
/* 414 */     return copyOf(copy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ImmutableList<Range<C>> intersectRanges(final Range<C> range)
/*     */   {
/* 422 */     if ((this.ranges.isEmpty()) || (range.isEmpty()))
/* 423 */       return ImmutableList.of();
/* 424 */     if (range.encloses(span())) {
/* 425 */       return this.ranges;
/*     */     }
/*     */     int fromIndex;
/*     */     final int fromIndex;
/* 429 */     if (range.hasLowerBound())
/*     */     {
/* 431 */       fromIndex = SortedLists.binarySearch(this.ranges, 
/*     */       
/* 433 */         Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 438 */       fromIndex = 0;
/*     */     }
/*     */     int toIndex;
/*     */     int toIndex;
/* 442 */     if (range.hasUpperBound())
/*     */     {
/* 444 */       toIndex = SortedLists.binarySearch(this.ranges, 
/*     */       
/* 446 */         Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 451 */       toIndex = this.ranges.size();
/*     */     }
/* 453 */     final int length = toIndex - fromIndex;
/* 454 */     if (length == 0) {
/* 455 */       return ImmutableList.of();
/*     */     }
/* 457 */     new ImmutableList()
/*     */     {
/*     */       public int size() {
/* 460 */         return length;
/*     */       }
/*     */       
/*     */       public Range<C> get(int index)
/*     */       {
/* 465 */         Preconditions.checkElementIndex(index, length);
/* 466 */         if ((index == 0) || (index == length - 1)) {
/* 467 */           return ((Range)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
/*     */         }
/* 469 */         return (Range)ImmutableRangeSet.this.ranges.get(index + fromIndex);
/*     */       }
/*     */       
/*     */ 
/*     */       boolean isPartialView()
/*     */       {
/* 475 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ImmutableRangeSet<C> subRangeSet(Range<C> range)
/*     */   {
/* 484 */     if (!isEmpty()) {
/* 485 */       Range<C> span = span();
/* 486 */       if (range.encloses(span))
/* 487 */         return this;
/* 488 */       if (range.isConnected(span)) {
/* 489 */         return new ImmutableRangeSet(intersectRanges(range));
/*     */       }
/*     */     }
/* 492 */     return of();
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
/*     */   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain)
/*     */   {
/* 515 */     Preconditions.checkNotNull(domain);
/* 516 */     if (isEmpty()) {
/* 517 */       return ImmutableSortedSet.of();
/*     */     }
/* 519 */     Range<C> span = span().canonical(domain);
/* 520 */     if (!span.hasLowerBound())
/*     */     {
/*     */ 
/* 523 */       throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
/*     */     }
/* 525 */     if (!span.hasUpperBound()) {
/*     */       try {
/* 527 */         domain.maxValue();
/*     */       } catch (NoSuchElementException e) {
/* 529 */         throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 534 */     return new AsSet(domain);
/*     */   }
/*     */   
/*     */   private final class AsSet extends ImmutableSortedSet<C> {
/*     */     private final DiscreteDomain<C> domain;
/*     */     private transient Integer size;
/*     */     
/* 541 */     AsSet() { super();
/* 542 */       this.domain = domain;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int size()
/*     */     {
/* 550 */       Integer result = this.size;
/* 551 */       if (result == null) {
/* 552 */         long total = 0L;
/* 553 */         for (UnmodifiableIterator localUnmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); localUnmodifiableIterator.hasNext();) { Range<C> range = (Range)localUnmodifiableIterator.next();
/* 554 */           total += ContiguousSet.create(range, this.domain).size();
/* 555 */           if (total >= 2147483647L) {
/*     */             break;
/*     */           }
/*     */         }
/* 559 */         result = this.size = Integer.valueOf(Ints.saturatedCast(total));
/*     */       }
/* 561 */       return result.intValue();
/*     */     }
/*     */     
/*     */     public UnmodifiableIterator<C> iterator()
/*     */     {
/* 566 */       new AbstractIterator() {
/* 567 */         final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
/* 568 */         Iterator<C> elemItr = Iterators.emptyIterator();
/*     */         
/*     */         protected C computeNext()
/*     */         {
/* 572 */           while (!this.elemItr.hasNext()) {
/* 573 */             if (this.rangeItr.hasNext()) {
/* 574 */               this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator();
/*     */             } else {
/* 576 */               return (Comparable)endOfData();
/*     */             }
/*     */           }
/* 579 */           return (Comparable)this.elemItr.next();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     @GwtIncompatible("NavigableSet")
/*     */     public UnmodifiableIterator<C> descendingIterator()
/*     */     {
/* 587 */       new AbstractIterator() {
/* 588 */         final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
/* 589 */         Iterator<C> elemItr = Iterators.emptyIterator();
/*     */         
/*     */         protected C computeNext()
/*     */         {
/* 593 */           while (!this.elemItr.hasNext()) {
/* 594 */             if (this.rangeItr.hasNext()) {
/* 595 */               this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator();
/*     */             } else {
/* 597 */               return (Comparable)endOfData();
/*     */             }
/*     */           }
/* 600 */           return (Comparable)this.elemItr.next();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> subSet(Range<C> range) {
/* 606 */       return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive)
/*     */     {
/* 611 */       return subSet(Range.upTo(toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */ 
/*     */     ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
/*     */     {
/* 617 */       if ((!fromInclusive) && (!toInclusive) && (Range.compareOrThrow(fromElement, toElement) == 0)) {
/* 618 */         return ImmutableSortedSet.of();
/*     */       }
/* 620 */       return subSet(
/* 621 */         Range.range(fromElement, 
/* 622 */         BoundType.forBoolean(fromInclusive), toElement, 
/* 623 */         BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive)
/*     */     {
/* 628 */       return subSet(Range.downTo(fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */     
/*     */     public boolean contains(Object o)
/*     */     {
/* 633 */       if (o == null) {
/* 634 */         return false;
/*     */       }
/*     */       try
/*     */       {
/* 638 */         C c = (Comparable)o;
/* 639 */         return ImmutableRangeSet.this.contains(c);
/*     */       } catch (ClassCastException e) {}
/* 641 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     int indexOf(Object target)
/*     */     {
/* 647 */       if (contains(target))
/*     */       {
/* 649 */         C c = (Comparable)target;
/* 650 */         long total = 0L;
/* 651 */         for (UnmodifiableIterator localUnmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); localUnmodifiableIterator.hasNext();) { Range<C> range = (Range)localUnmodifiableIterator.next();
/* 652 */           if (range.contains(c)) {
/* 653 */             return Ints.saturatedCast(total + ContiguousSet.create(range, this.domain).indexOf(c));
/*     */           }
/* 655 */           total += ContiguousSet.create(range, this.domain).size();
/*     */         }
/*     */         
/* 658 */         throw new AssertionError("impossible");
/*     */       }
/* 660 */       return -1;
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> createDescendingSet()
/*     */     {
/* 665 */       return new DescendingImmutableSortedSet(this);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 670 */       return ImmutableRangeSet.this.ranges.isPartialView();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 675 */       return ImmutableRangeSet.this.ranges.toString();
/*     */     }
/*     */     
/*     */     Object writeReplace()
/*     */     {
/* 680 */       return new ImmutableRangeSet.AsSetSerializedForm(ImmutableRangeSet.this.ranges, this.domain);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
/* 689 */       this.ranges = ranges;
/* 690 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 694 */       return new ImmutableRangeSet(this.ranges).asSet(this.domain);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isPartialView()
/*     */   {
/* 705 */     return this.ranges.isPartialView();
/*     */   }
/*     */   
/*     */   public static <C extends Comparable<?>> Builder<C> builder()
/*     */   {
/* 710 */     return new Builder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Builder<C extends Comparable<?>>
/*     */   {
/*     */     private final List<Range<C>> ranges;
/*     */     
/*     */ 
/*     */     public Builder()
/*     */     {
/* 722 */       this.ranges = Lists.newArrayList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> add(Range<C> range)
/*     */     {
/* 735 */       Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
/* 736 */       this.ranges.add(range);
/* 737 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(RangeSet<C> ranges)
/*     */     {
/* 747 */       return addAll(ranges.asRanges());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(Iterable<Range<C>> ranges)
/*     */     {
/* 759 */       for (Range<C> range : ranges) {
/* 760 */         add(range);
/*     */       }
/* 762 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<C> combine(Builder<C> builder) {
/* 767 */       addAll(builder.ranges);
/* 768 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableRangeSet<C> build()
/*     */     {
/* 778 */       ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder(this.ranges.size());
/* 779 */       Collections.sort(this.ranges, Range.rangeLexOrdering());
/* 780 */       PeekingIterator<Range<C>> peekingItr = Iterators.peekingIterator(this.ranges.iterator());
/* 781 */       while (peekingItr.hasNext()) {
/* 782 */         Range<C> range = (Range)peekingItr.next();
/* 783 */         while (peekingItr.hasNext()) {
/* 784 */           Range<C> nextRange = (Range)peekingItr.peek();
/* 785 */           if (!range.isConnected(nextRange)) break;
/* 786 */           Preconditions.checkArgument(
/* 787 */             range.intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
/*     */           
/*     */ 
/*     */ 
/* 791 */           range = range.span((Range)peekingItr.next());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 796 */         mergedRangesBuilder.add(range);
/*     */       }
/* 798 */       ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
/* 799 */       if (mergedRanges.isEmpty())
/* 800 */         return ImmutableRangeSet.of();
/* 801 */       if ((mergedRanges.size() == 1) && 
/* 802 */         (((Range)Iterables.getOnlyElement(mergedRanges)).equals(Range.all()))) {
/* 803 */         return ImmutableRangeSet.all();
/*     */       }
/* 805 */       return new ImmutableRangeSet(mergedRanges);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable
/*     */   {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     
/*     */     SerializedForm(ImmutableList<Range<C>> ranges) {
/* 814 */       this.ranges = ranges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 818 */       if (this.ranges.isEmpty())
/* 819 */         return ImmutableRangeSet.of();
/* 820 */       if (this.ranges.equals(ImmutableList.of(Range.all()))) {
/* 821 */         return ImmutableRangeSet.all();
/*     */       }
/* 823 */       return new ImmutableRangeSet(this.ranges);
/*     */     }
/*     */   }
/*     */   
/*     */   Object writeReplace()
/*     */   {
/* 829 */     return new SerializedForm(this.ranges);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */