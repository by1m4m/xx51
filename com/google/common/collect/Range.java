/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Range<C extends Comparable>
/*     */   extends RangeGwtSerializationDependencies
/*     */   implements Predicate<C>, Serializable
/*     */ {
/*     */   static class LowerBoundFn
/*     */     implements Function<Range, Cut>
/*     */   {
/* 123 */     static final LowerBoundFn INSTANCE = new LowerBoundFn();
/*     */     
/*     */     public Cut apply(Range range)
/*     */     {
/* 127 */       return range.lowerBound;
/*     */     }
/*     */   }
/*     */   
/*     */   static class UpperBoundFn implements Function<Range, Cut> {
/* 132 */     static final UpperBoundFn INSTANCE = new UpperBoundFn();
/*     */     
/*     */     public Cut apply(Range range)
/*     */     {
/* 136 */       return range.upperBound;
/*     */     }
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn()
/*     */   {
/* 142 */     return LowerBoundFn.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn()
/*     */   {
/* 147 */     return UpperBoundFn.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
/* 151 */     return RangeLexOrdering.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 155 */     return new Range(lowerBound, upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper)
/*     */   {
/* 167 */     return create(Cut.aboveValue(lower), Cut.belowValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper)
/*     */   {
/* 178 */     return create(Cut.belowValue(lower), Cut.aboveValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper)
/*     */   {
/* 189 */     return create(Cut.belowValue(lower), Cut.belowValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper)
/*     */   {
/* 200 */     return create(Cut.aboveValue(lower), Cut.aboveValue(upper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType)
/*     */   {
/* 212 */     Preconditions.checkNotNull(lowerType);
/* 213 */     Preconditions.checkNotNull(upperType);
/*     */     
/*     */ 
/* 216 */     Cut<C> lowerBound = lowerType == BoundType.OPEN ? Cut.aboveValue(lower) : Cut.belowValue(lower);
/*     */     
/* 218 */     Cut<C> upperBound = upperType == BoundType.OPEN ? Cut.belowValue(upper) : Cut.aboveValue(upper);
/* 219 */     return create(lowerBound, upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint)
/*     */   {
/* 228 */     return create(Cut.belowAll(), Cut.belowValue(endpoint));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint)
/*     */   {
/* 237 */     return create(Cut.belowAll(), Cut.aboveValue(endpoint));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType)
/*     */   {
/* 247 */     switch (boundType) {
/*     */     case OPEN: 
/* 249 */       return lessThan(endpoint);
/*     */     case CLOSED: 
/* 251 */       return atMost(endpoint);
/*     */     }
/* 253 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint)
/*     */   {
/* 263 */     return create(Cut.aboveValue(endpoint), Cut.aboveAll());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint)
/*     */   {
/* 272 */     return create(Cut.belowValue(endpoint), Cut.aboveAll());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType)
/*     */   {
/* 282 */     switch (boundType) {
/*     */     case OPEN: 
/* 284 */       return greaterThan(endpoint);
/*     */     case CLOSED: 
/* 286 */       return atLeast(endpoint);
/*     */     }
/* 288 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/* 292 */   private static final Range<Comparable> ALL = new Range(Cut.belowAll(), Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all()
/*     */   {
/* 301 */     return ALL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value)
/*     */   {
/* 311 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values)
/*     */   {
/* 324 */     Preconditions.checkNotNull(values);
/* 325 */     if ((values instanceof SortedSet)) {
/* 326 */       SortedSet<? extends C> set = cast(values);
/* 327 */       Comparator<?> comparator = set.comparator();
/* 328 */       if ((Ordering.natural().equals(comparator)) || (comparator == null)) {
/* 329 */         return closed((Comparable)set.first(), (Comparable)set.last());
/*     */       }
/*     */     }
/* 332 */     Iterator<C> valueIterator = values.iterator();
/* 333 */     C min = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 334 */     C max = min;
/* 335 */     while (valueIterator.hasNext()) {
/* 336 */       C value = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 337 */       min = (Comparable)Ordering.natural().min(min, value);
/* 338 */       max = (Comparable)Ordering.natural().max(max, value);
/*     */     }
/* 340 */     return closed(min, max);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound)
/*     */   {
/* 347 */     this.lowerBound = ((Cut)Preconditions.checkNotNull(lowerBound));
/* 348 */     this.upperBound = ((Cut)Preconditions.checkNotNull(upperBound));
/* 349 */     if ((lowerBound.compareTo(upperBound) > 0) || 
/* 350 */       (lowerBound == Cut.aboveAll()) || 
/* 351 */       (upperBound == Cut.belowAll())) {
/* 352 */       throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasLowerBound()
/*     */   {
/* 358 */     return this.lowerBound != Cut.belowAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public C lowerEndpoint()
/*     */   {
/* 368 */     return this.lowerBound.endpoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundType lowerBoundType()
/*     */   {
/* 379 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */   
/*     */   public boolean hasUpperBound()
/*     */   {
/* 384 */     return this.upperBound != Cut.aboveAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public C upperEndpoint()
/*     */   {
/* 394 */     return this.upperBound.endpoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundType upperBoundType()
/*     */   {
/* 405 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty()
/*     */   {
/* 418 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(C value)
/*     */   {
/* 427 */     Preconditions.checkNotNull(value);
/*     */     
/* 429 */     return (this.lowerBound.isLessThan(value)) && (!this.upperBound.isLessThan(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean apply(C input)
/*     */   {
/* 439 */     return contains(input);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsAll(Iterable<? extends C> values)
/*     */   {
/* 447 */     if (Iterables.isEmpty(values)) {
/* 448 */       return true;
/*     */     }
/*     */     
/*     */     SortedSet<? extends C> set;
/* 452 */     if ((values instanceof SortedSet)) {
/* 453 */       set = cast(values);
/* 454 */       Comparator<?> comparator = set.comparator();
/* 455 */       if ((Ordering.natural().equals(comparator)) || (comparator == null)) {
/* 456 */         return (contains((Comparable)set.first())) && (contains((Comparable)set.last()));
/*     */       }
/*     */     }
/*     */     
/* 460 */     for (C value : values) {
/* 461 */       if (!contains(value)) {
/* 462 */         return false;
/*     */       }
/*     */     }
/* 465 */     return true;
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
/*     */   public boolean encloses(Range<C> other)
/*     */   {
/* 492 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0) && 
/* 493 */       (this.upperBound.compareTo(other.upperBound) >= 0);
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
/*     */   public boolean isConnected(Range<C> other)
/*     */   {
/* 522 */     return (this.lowerBound.compareTo(other.upperBound) <= 0) && 
/* 523 */       (other.lowerBound.compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange)
/*     */   {
/* 543 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 544 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 545 */     if ((lowerCmp >= 0) && (upperCmp <= 0))
/* 546 */       return this;
/* 547 */     if ((lowerCmp <= 0) && (upperCmp >= 0)) {
/* 548 */       return connectedRange;
/*     */     }
/* 550 */     Cut<C> newLower = lowerCmp >= 0 ? this.lowerBound : connectedRange.lowerBound;
/* 551 */     Cut<C> newUpper = upperCmp <= 0 ? this.upperBound : connectedRange.upperBound;
/* 552 */     return create(newLower, newUpper);
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
/*     */   public Range<C> gap(Range<C> otherRange)
/*     */   {
/* 574 */     boolean isThisFirst = this.lowerBound.compareTo(otherRange.lowerBound) < 0;
/* 575 */     Range<C> firstRange = isThisFirst ? this : otherRange;
/* 576 */     Range<C> secondRange = isThisFirst ? otherRange : this;
/* 577 */     return create(firstRange.upperBound, secondRange.lowerBound);
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
/*     */   public Range<C> span(Range<C> other)
/*     */   {
/* 592 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 593 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 594 */     if ((lowerCmp <= 0) && (upperCmp >= 0))
/* 595 */       return this;
/* 596 */     if ((lowerCmp >= 0) && (upperCmp <= 0)) {
/* 597 */       return other;
/*     */     }
/* 599 */     Cut<C> newLower = lowerCmp <= 0 ? this.lowerBound : other.lowerBound;
/* 600 */     Cut<C> newUpper = upperCmp >= 0 ? this.upperBound : other.upperBound;
/* 601 */     return create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain)
/*     */   {
/* 630 */     Preconditions.checkNotNull(domain);
/* 631 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 632 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 633 */     return (lower == this.lowerBound) && (upper == this.upperBound) ? this : create(lower, upper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object object)
/*     */   {
/* 645 */     if ((object instanceof Range)) {
/* 646 */       Range<?> other = (Range)object;
/* 647 */       return (this.lowerBound.equals(other.lowerBound)) && (this.upperBound.equals(other.upperBound));
/*     */     }
/* 649 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 655 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 664 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 668 */     StringBuilder sb = new StringBuilder(16);
/* 669 */     lowerBound.describeAsLowerBound(sb);
/* 670 */     sb.append("..");
/* 671 */     upperBound.describeAsUpperBound(sb);
/* 672 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static <T> SortedSet<T> cast(Iterable<T> iterable)
/*     */   {
/* 677 */     return (SortedSet)iterable;
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 681 */     if (equals(ALL)) {
/* 682 */       return all();
/*     */     }
/* 684 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   static int compareOrThrow(Comparable left, Comparable right)
/*     */   {
/* 690 */     return left.compareTo(right);
/*     */   }
/*     */   
/*     */   private static class RangeLexOrdering extends Ordering<Range<?>> implements Serializable
/*     */   {
/* 695 */     static final Ordering<Range<?>> INSTANCE = new RangeLexOrdering();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int compare(Range<?> left, Range<?> right) {
/* 699 */       return 
/*     */       
/*     */ 
/* 702 */         ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */