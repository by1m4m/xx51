/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingSortedMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*     */   protected abstract SortedMap<K, V> delegate();
/*     */   
/*     */   public Comparator<? super K> comparator()
/*     */   {
/*  66 */     return delegate().comparator();
/*     */   }
/*     */   
/*     */   public K firstKey()
/*     */   {
/*  71 */     return (K)delegate().firstKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey)
/*     */   {
/*  76 */     return delegate().headMap(toKey);
/*     */   }
/*     */   
/*     */   public K lastKey()
/*     */   {
/*  81 */     return (K)delegate().lastKey();
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey)
/*     */   {
/*  86 */     return delegate().subMap(fromKey, toKey);
/*     */   }
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey)
/*     */   {
/*  91 */     return delegate().tailMap(fromKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.SortedKeySet<K, V>
/*     */   {
/*     */     public StandardKeySet()
/*     */     {
/* 105 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private int unsafeCompare(Object k1, Object k2)
/*     */   {
/* 112 */     Comparator<? super K> comparator = comparator();
/* 113 */     if (comparator == null) {
/* 114 */       return ((Comparable)k1).compareTo(k2);
/*     */     }
/* 116 */     return comparator.compare(k1, k2);
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
/*     */   @Beta
/*     */   protected boolean standardContainsKey(Object key)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       SortedMap<Object, V> self = this;
/* 134 */       Object ceilingKey = self.tailMap(key).firstKey();
/* 135 */       return unsafeCompare(ceilingKey, key) == 0;
/*     */     } catch (ClassCastException|NoSuchElementException|NullPointerException e) {}
/* 137 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey)
/*     */   {
/* 150 */     Preconditions.checkArgument(unsafeCompare(fromKey, toKey) <= 0, "fromKey must be <= toKey");
/* 151 */     return tailMap(fromKey).headMap(toKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ForwardingSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */