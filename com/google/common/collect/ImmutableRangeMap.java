/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.Function;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   implements RangeMap<K, V>, Serializable
/*     */ {
/*  47 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
/*  48 */     ImmutableList.of(), ImmutableList.of());
/*     */   
/*     */ 
/*     */   private final transient ImmutableList<Range<K>> ranges;
/*     */   
/*     */   private final transient ImmutableList<V> values;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */ 
/*     */   @Beta
/*     */   public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction)
/*     */   {
/*  61 */     return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
/*     */   }
/*     */   
/*     */ 
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of()
/*     */   {
/*  67 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value)
/*     */   {
/*  72 */     return new ImmutableRangeMap(ImmutableList.of(range), ImmutableList.of(value));
/*     */   }
/*     */   
/*     */ 
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap)
/*     */   {
/*  78 */     if ((rangeMap instanceof ImmutableRangeMap)) {
/*  79 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  81 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  82 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(map.size());
/*  83 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(map.size());
/*  84 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  85 */       rangesBuilder.add(entry.getKey());
/*  86 */       valuesBuilder.add(entry.getValue());
/*     */     }
/*  88 */     return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> builder()
/*     */   {
/*  93 */     return new Builder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */   {
/*     */     private final List<Map.Entry<Range<K>, V>> entries;
/*     */     
/*     */ 
/*     */     public Builder()
/*     */     {
/* 105 */       this.entries = Lists.newArrayList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Range<K> range, V value)
/*     */     {
/* 115 */       Preconditions.checkNotNull(range);
/* 116 */       Preconditions.checkNotNull(value);
/* 117 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
/* 118 */       this.entries.add(Maps.immutableEntry(range, value));
/* 119 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap)
/*     */     {
/* 125 */       for (Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
/* 126 */         put((Range)entry.getKey(), entry.getValue());
/*     */       }
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> builder) {
/* 133 */       this.entries.addAll(builder.entries);
/* 134 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableRangeMap<K, V> build()
/*     */     {
/* 144 */       Collections.sort(this.entries, Range.rangeLexOrdering().onKeys());
/* 145 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(this.entries.size());
/* 146 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(this.entries.size());
/* 147 */       for (int i = 0; i < this.entries.size(); i++) {
/* 148 */         Range<K> range = (Range)((Map.Entry)this.entries.get(i)).getKey();
/* 149 */         if (i > 0) {
/* 150 */           Range<K> prevRange = (Range)((Map.Entry)this.entries.get(i - 1)).getKey();
/* 151 */           if ((range.isConnected(prevRange)) && (!range.intersection(prevRange).isEmpty())) {
/* 152 */             throw new IllegalArgumentException("Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
/*     */           }
/*     */         }
/*     */         
/* 156 */         rangesBuilder.add(range);
/* 157 */         valuesBuilder.add(((Map.Entry)this.entries.get(i)).getValue());
/*     */       }
/* 159 */       return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values)
/*     */   {
/* 167 */     this.ranges = ranges;
/* 168 */     this.values = values;
/*     */   }
/*     */   
/*     */ 
/*     */   public V get(K key)
/*     */   {
/* 174 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 176 */       Range.lowerBoundFn(), 
/* 177 */       Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 180 */     if (index == -1) {
/* 181 */       return null;
/*     */     }
/* 183 */     Range<K> range = (Range)this.ranges.get(index);
/* 184 */     return (V)(range.contains(key) ? this.values.get(index) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map.Entry<Range<K>, V> getEntry(K key)
/*     */   {
/* 191 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 193 */       Range.lowerBoundFn(), 
/* 194 */       Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/*     */ 
/* 197 */     if (index == -1) {
/* 198 */       return null;
/*     */     }
/* 200 */     Range<K> range = (Range)this.ranges.get(index);
/* 201 */     return range.contains(key) ? Maps.immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Range<K> span()
/*     */   {
/* 207 */     if (this.ranges.isEmpty()) {
/* 208 */       throw new NoSuchElementException();
/*     */     }
/* 210 */     Range<K> firstRange = (Range)this.ranges.get(0);
/* 211 */     Range<K> lastRange = (Range)this.ranges.get(this.ranges.size() - 1);
/* 212 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void put(Range<K> range, V value)
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
/*     */   public void putCoalescing(Range<K> range, V value)
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
/*     */   public void putAll(RangeMap<K, V> rangeMap)
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
/*     */   public void clear()
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
/*     */   public void remove(Range<K> range)
/*     */   {
/* 272 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges()
/*     */   {
/* 277 */     if (this.ranges.isEmpty()) {
/* 278 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 281 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(this.ranges, Range.rangeLexOrdering());
/* 282 */     return new ImmutableSortedMap(rangeSet, this.values);
/*     */   }
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges()
/*     */   {
/* 287 */     if (this.ranges.isEmpty()) {
/* 288 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 291 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(this.ranges.reverse(), Range.rangeLexOrdering().reverse());
/* 292 */     return new ImmutableSortedMap(rangeSet, this.values.reverse());
/*     */   }
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range)
/*     */   {
/* 297 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 298 */       return of();
/* 299 */     if ((this.ranges.isEmpty()) || (range.encloses(span()))) {
/* 300 */       return this;
/*     */     }
/*     */     
/* 303 */     int lowerIndex = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 305 */       Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 310 */     int upperIndex = SortedLists.binarySearch(this.ranges, 
/*     */     
/* 312 */       Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */ 
/*     */ 
/* 316 */     if (lowerIndex >= upperIndex) {
/* 317 */       return of();
/*     */     }
/* 319 */     final int off = lowerIndex;
/* 320 */     final int len = upperIndex - lowerIndex;
/* 321 */     ImmutableList<Range<K>> subRanges = new ImmutableList()
/*     */     {
/*     */       public int size()
/*     */       {
/* 325 */         return len;
/*     */       }
/*     */       
/*     */       public Range<K> get(int index)
/*     */       {
/* 330 */         Preconditions.checkElementIndex(index, len);
/* 331 */         if ((index == 0) || (index == len - 1)) {
/* 332 */           return ((Range)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
/*     */         }
/* 334 */         return (Range)ImmutableRangeMap.this.ranges.get(index + off);
/*     */       }
/*     */       
/*     */ 
/*     */       boolean isPartialView()
/*     */       {
/* 340 */         return true;
/*     */       }
/* 342 */     };
/* 343 */     final ImmutableRangeMap<K, V> outer = this;
/* 344 */     new ImmutableRangeMap(subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */     {
/*     */       public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 347 */         if (range.isConnected(subRange)) {
/* 348 */           return outer.subRangeMap(subRange.intersection(range));
/*     */         }
/* 350 */         return ImmutableRangeMap.of();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 358 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 363 */     if ((o instanceof RangeMap)) {
/* 364 */       RangeMap<?, ?> rangeMap = (RangeMap)o;
/* 365 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     }
/* 367 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 372 */     return asMapOfRanges().toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class SerializedForm<K extends Comparable<?>, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final ImmutableMap<Range<K>, V> mapOfRanges;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges)
/*     */     {
/* 384 */       this.mapOfRanges = mapOfRanges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 388 */       if (this.mapOfRanges.isEmpty()) {
/* 389 */         return ImmutableRangeMap.of();
/*     */       }
/* 391 */       return createRangeMap();
/*     */     }
/*     */     
/*     */     Object createRangeMap()
/*     */     {
/* 396 */       ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder();
/* 397 */       for (UnmodifiableIterator localUnmodifiableIterator = this.mapOfRanges.entrySet().iterator(); localUnmodifiableIterator.hasNext();) { Map.Entry<Range<K>, V> entry = (Map.Entry)localUnmodifiableIterator.next();
/* 398 */         builder.put((Range)entry.getKey(), entry.getValue());
/*     */       }
/* 400 */       return builder.build();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 407 */     return new SerializedForm(asMapOfRanges());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */