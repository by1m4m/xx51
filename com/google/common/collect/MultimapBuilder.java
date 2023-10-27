/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ public abstract class MultimapBuilder<K0, V0>
/*     */ {
/*     */   private static final int DEFAULT_EXPECTED_KEYS = 8;
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys()
/*     */   {
/*  78 */     return hashKeys(8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys(int expectedKeys)
/*     */   {
/*  88 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/*  89 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */       <K, V> Map<K, Collection<V>> createMap() {
/*  92 */         return Platform.newHashMapWithExpectedSize(this.val$expectedKeys);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys()
/*     */   {
/* 106 */     return linkedHashKeys(8);
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys(int expectedKeys)
/*     */   {
/* 119 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/* 120 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */       <K, V> Map<K, Collection<V>> createMap() {
/* 123 */         return Platform.newLinkedHashMapWithExpectedSize(this.val$expectedKeys);
/*     */       }
/*     */     };
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
/*     */   public static MultimapBuilderWithKeys<Comparable> treeKeys()
/*     */   {
/* 140 */     return treeKeys(Ordering.natural());
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
/*     */   public static <K0> MultimapBuilderWithKeys<K0> treeKeys(Comparator<K0> comparator)
/*     */   {
/* 157 */     Preconditions.checkNotNull(comparator);
/* 158 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */       <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 161 */         return new TreeMap(this.val$comparator);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(Class<K0> keyClass)
/*     */   {
/* 173 */     Preconditions.checkNotNull(keyClass);
/* 174 */     new MultimapBuilderWithKeys()
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 180 */         return new EnumMap(this.val$keyClass); }
/*     */     };
/*     */   }
/*     */   
/*     */   public abstract <K extends K0, V extends V0> Multimap<K, V> build();
/*     */   
/*     */   private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/* 189 */     ArrayListSupplier(int expectedValuesPerKey) { this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey"); }
/*     */     
/*     */ 
/*     */     public List<V> get()
/*     */     {
/* 194 */       return new ArrayList(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum LinkedListSupplier implements Supplier<List<Object>> {
/* 199 */     INSTANCE;
/*     */     
/*     */     private LinkedListSupplier() {}
/*     */     
/*     */     public static <V> Supplier<List<V>> instance() {
/* 204 */       Supplier<List<V>> result = INSTANCE;
/* 205 */       return result;
/*     */     }
/*     */     
/*     */     public List<Object> get()
/*     */     {
/* 210 */       return new LinkedList();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     HashSetSupplier(int expectedValuesPerKey) {
/* 218 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */     
/*     */     public Set<V> get()
/*     */     {
/* 223 */       return Platform.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     LinkedHashSetSupplier(int expectedValuesPerKey) {
/* 231 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */     
/*     */     public Set<V> get()
/*     */     {
/* 236 */       return Platform.newLinkedHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable {
/*     */     private final Comparator<? super V> comparator;
/*     */     
/*     */     TreeSetSupplier(Comparator<? super V> comparator) {
/* 244 */       this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
/*     */     }
/*     */     
/*     */     public SortedSet<V> get()
/*     */     {
/* 249 */       return new TreeSet(this.comparator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EnumSetSupplier<V extends Enum<V>> implements Supplier<Set<V>>, Serializable
/*     */   {
/*     */     private final Class<V> clazz;
/*     */     
/*     */     EnumSetSupplier(Class<V> clazz) {
/* 258 */       this.clazz = ((Class)Preconditions.checkNotNull(clazz));
/*     */     }
/*     */     
/*     */     public Set<V> get()
/*     */     {
/* 263 */       return EnumSet.noneOf(this.clazz);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class MultimapBuilderWithKeys<K0>
/*     */   {
/*     */     private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract <K extends K0, V> Map<K, Collection<V>> createMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues()
/*     */     {
/* 284 */       return arrayListValues(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey)
/*     */     {
/* 294 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 295 */       new MultimapBuilder.ListMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> ListMultimap<K, V> build() {
/* 298 */           return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 299 */             .createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues()
/*     */     {
/* 307 */       new MultimapBuilder.ListMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> ListMultimap<K, V> build() {
/* 310 */           return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 311 */             .createMap(), MultimapBuilder.LinkedListSupplier.instance());
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues()
/*     */     {
/* 318 */       return hashSetValues(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey)
/*     */     {
/* 328 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 329 */       new MultimapBuilder.SetMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> SetMultimap<K, V> build() {
/* 332 */           return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 333 */             .createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues()
/*     */     {
/* 341 */       return linkedHashSetValues(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey)
/*     */     {
/* 351 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 352 */       new MultimapBuilder.SetMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V> SetMultimap<K, V> build() {
/* 355 */           return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 356 */             .createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues()
/*     */     {
/* 365 */       return treeSetValues(Ordering.natural());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator)
/*     */     {
/* 375 */       Preconditions.checkNotNull(comparator, "comparator");
/* 376 */       new MultimapBuilder.SortedSetMultimapBuilder()
/*     */       {
/*     */         public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
/* 379 */           return Multimaps.newSortedSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 380 */             .createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */     public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass)
/*     */     {
/* 388 */       Preconditions.checkNotNull(valueClass, "valueClass");
/* 389 */       new MultimapBuilder.SetMultimapBuilder()
/*     */       {
/*     */ 
/*     */         public <K extends K0, V extends V0> SetMultimap<K, V> build()
/*     */         {
/*     */ 
/* 395 */           Supplier<Set<V>> factory = new MultimapBuilder.EnumSetSupplier(valueClass);
/* 396 */           return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), factory);
/*     */         }
/*     */       };
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
/*     */   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */   {
/* 411 */     Multimap<K, V> result = build();
/* 412 */     result.putAll(multimap);
/* 413 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract class ListMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     ListMultimapBuilder()
/*     */     {
/* 422 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
/*     */     
/*     */     public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */     {
/* 430 */       return (ListMultimap)super.build(multimap);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract class SetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     SetMultimapBuilder()
/*     */     {
/* 440 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
/*     */     
/*     */     public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */     {
/* 448 */       return (SetMultimap)super.build(multimap);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class SortedSetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder.SetMultimapBuilder<K0, V0>
/*     */   {
/*     */     public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap)
/*     */     {
/* 466 */       return (SortedSetMultimap)super.build(multimap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\MultimapBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */