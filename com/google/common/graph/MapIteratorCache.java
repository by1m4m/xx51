/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MapIteratorCache<K, V>
/*     */ {
/*     */   private final Map<K, V> backingMap;
/*     */   private transient Map.Entry<K, V> entrySetCache;
/*     */   
/*     */   MapIteratorCache(Map<K, V> backingMap)
/*     */   {
/*  53 */     this.backingMap = ((Map)Preconditions.checkNotNull(backingMap));
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/*  58 */     clearCache();
/*  59 */     return (V)this.backingMap.put(key, value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object key) {
/*  64 */     clearCache();
/*  65 */     return (V)this.backingMap.remove(key);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  69 */     clearCache();
/*  70 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/*  74 */     V value = getIfCached(key);
/*  75 */     return value != null ? value : getWithoutCaching(key);
/*     */   }
/*     */   
/*     */   public final V getWithoutCaching(Object key) {
/*  79 */     return (V)this.backingMap.get(key);
/*     */   }
/*     */   
/*     */   public final boolean containsKey(Object key) {
/*  83 */     return (getIfCached(key) != null) || (this.backingMap.containsKey(key));
/*     */   }
/*     */   
/*     */   public final Set<K> unmodifiableKeySet() {
/*  87 */     new AbstractSet()
/*     */     {
/*     */       public UnmodifiableIterator<K> iterator() {
/*  90 */         final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
/*     */         
/*  92 */         new UnmodifiableIterator()
/*     */         {
/*     */           public boolean hasNext() {
/*  95 */             return entryIterator.hasNext();
/*     */           }
/*     */           
/*     */           public K next()
/*     */           {
/* 100 */             Map.Entry<K, V> entry = (Map.Entry)entryIterator.next();
/* 101 */             MapIteratorCache.this.entrySetCache = entry;
/* 102 */             return (K)entry.getKey();
/*     */           }
/*     */         };
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 109 */         return MapIteratorCache.this.backingMap.size();
/*     */       }
/*     */       
/*     */       public boolean contains(Object key)
/*     */       {
/* 114 */         return MapIteratorCache.this.containsKey(key);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   protected V getIfCached(Object key)
/*     */   {
/* 122 */     Map.Entry<K, V> entry = this.entrySetCache;
/*     */     
/*     */ 
/* 125 */     if ((entry != null) && (entry.getKey() == key)) {
/* 126 */       return (V)entry.getValue();
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   protected void clearCache() {
/* 132 */     this.entrySetCache = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\MapIteratorCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */