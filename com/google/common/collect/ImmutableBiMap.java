/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableBiMapFauxverideShim<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
/*     */   {
/*  60 */     return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <K, V> ImmutableBiMap<K, V> of()
/*     */   {
/*  67 */     return RegularImmutableBiMap.EMPTY;
/*     */   }
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1)
/*     */   {
/*  72 */     return new SingletonImmutableBiMap(k1, v1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2)
/*     */   {
/*  81 */     return RegularImmutableBiMap.fromEntries(new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3)
/*     */   {
/*  90 */     return RegularImmutableBiMap.fromEntries(new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
/*     */   {
/*  99 */     return RegularImmutableBiMap.fromEntries(new Map.Entry[] {
/* 100 */       entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
/*     */   {
/* 110 */     return RegularImmutableBiMap.fromEntries(new Map.Entry[] {
/* 111 */       entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> Builder<K, V> builder()
/*     */   {
/* 121 */     return new Builder();
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
/*     */   @Beta
/*     */   public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize)
/*     */   {
/* 138 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 139 */     return new Builder(expectedSize);
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Builder(int size)
/*     */     {
/* 179 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value)
/*     */     {
/* 189 */       super.put(key, value);
/* 190 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry)
/*     */     {
/* 202 */       super.put(entry);
/* 203 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map)
/*     */     {
/* 215 */       super.putAll(map);
/* 216 */       return this;
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
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries)
/*     */     {
/* 230 */       super.putAll(entries);
/* 231 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator)
/*     */     {
/* 248 */       super.orderEntriesByValue(valueComparator);
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> builder)
/*     */     {
/* 255 */       super.combine(builder);
/* 256 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableBiMap<K, V> build()
/*     */     {
/* 268 */       switch (this.size) {
/*     */       case 0: 
/* 270 */         return ImmutableBiMap.of();
/*     */       case 1: 
/* 272 */         return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       }
/*     */       
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 281 */       if (this.valueComparator != null) {
/* 282 */         if (this.entriesUsed) {
/* 283 */           this.entries = ((Map.Entry[])Arrays.copyOf(this.entries, this.size));
/*     */         }
/* 285 */         Arrays.sort(this.entries, 0, this.size, 
/*     */         
/*     */ 
/*     */ 
/* 289 */           Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       }
/* 291 */       this.entriesUsed = true;
/* 292 */       return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
/*     */     }
/*     */     
/*     */ 
/*     */     @VisibleForTesting
/*     */     ImmutableBiMap<K, V> buildJdkBacked()
/*     */     {
/* 299 */       Preconditions.checkState(this.valueComparator == null, "buildJdkBacked is for tests only, doesn't support orderEntriesByValue");
/*     */       
/*     */ 
/* 302 */       switch (this.size) {
/*     */       case 0: 
/* 304 */         return ImmutableBiMap.of();
/*     */       case 1: 
/* 306 */         return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       }
/* 308 */       this.entriesUsed = true;
/* 309 */       return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map)
/*     */   {
/* 331 */     if ((map instanceof ImmutableBiMap))
/*     */     {
/* 333 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/*     */       
/*     */ 
/* 336 */       if (!bimap.isPartialView()) {
/* 337 */         return bimap;
/*     */       }
/*     */     }
/* 340 */     return copyOf(map.entrySet());
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries)
/*     */   {
/* 356 */     Map.Entry<K, V>[] entryArray = (Map.Entry[])Iterables.toArray(entries, EMPTY_ENTRY_ARRAY);
/* 357 */     switch (entryArray.length) {
/*     */     case 0: 
/* 359 */       return of();
/*     */     case 1: 
/* 361 */       Map.Entry<K, V> entry = entryArray[0];
/* 362 */       return of(entry.getKey(), entry.getValue());
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/* 368 */     return RegularImmutableBiMap.fromEntries(entryArray);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableSet<V> values()
/*     */   {
/* 388 */     return inverse().keySet();
/*     */   }
/*     */   
/*     */   final ImmutableSet<V> createValues()
/*     */   {
/* 393 */     throw new AssertionError("should never be called");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value)
/*     */   {
/* 406 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */ 
/*     */     SerializedForm(ImmutableBiMap<?, ?> bimap)
/*     */     {
/* 419 */       super();
/*     */     }
/*     */     
/*     */     Object readResolve()
/*     */     {
/* 424 */       ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder();
/* 425 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 433 */     return new SerializedForm(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */