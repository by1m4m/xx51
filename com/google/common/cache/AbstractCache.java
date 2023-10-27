/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractCache<K, V>
/*     */   implements Cache<K, V>
/*     */ {
/*     */   public V get(K key, Callable<? extends V> valueLoader)
/*     */     throws ExecutionException
/*     */   {
/*  49 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys)
/*     */   {
/*  63 */     Map<K, V> result = Maps.newLinkedHashMap();
/*  64 */     for (Object key : keys) {
/*  65 */       if (!result.containsKey(key))
/*     */       {
/*  67 */         K castKey = (K)key;
/*  68 */         V value = getIfPresent(key);
/*  69 */         if (value != null) {
/*  70 */           result.put(castKey, value);
/*     */         }
/*     */       }
/*     */     }
/*  74 */     return ImmutableMap.copyOf(result);
/*     */   }
/*     */   
/*     */ 
/*     */   public void put(K key, V value)
/*     */   {
/*  80 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   public void putAll(Map<? extends K, ? extends V> m)
/*     */   {
/*  86 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/*  87 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void cleanUp() {}
/*     */   
/*     */   public long size()
/*     */   {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void invalidate(Object key)
/*     */   {
/* 101 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   public void invalidateAll(Iterable<?> keys)
/*     */   {
/* 107 */     for (Object key : keys) {
/* 108 */       invalidate(key);
/*     */     }
/*     */   }
/*     */   
/*     */   public void invalidateAll()
/*     */   {
/* 114 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public CacheStats stats()
/*     */   {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ConcurrentMap<K, V> asMap()
/*     */   {
/* 124 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class SimpleStatsCounter
/*     */     implements AbstractCache.StatsCounter
/*     */   {
/* 195 */     private final LongAddable hitCount = LongAddables.create();
/* 196 */     private final LongAddable missCount = LongAddables.create();
/* 197 */     private final LongAddable loadSuccessCount = LongAddables.create();
/* 198 */     private final LongAddable loadExceptionCount = LongAddables.create();
/* 199 */     private final LongAddable totalLoadTime = LongAddables.create();
/* 200 */     private final LongAddable evictionCount = LongAddables.create();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void recordHits(int count)
/*     */     {
/* 208 */       this.hitCount.add(count);
/*     */     }
/*     */     
/*     */ 
/*     */     public void recordMisses(int count)
/*     */     {
/* 214 */       this.missCount.add(count);
/*     */     }
/*     */     
/*     */     public void recordLoadSuccess(long loadTime)
/*     */     {
/* 219 */       this.loadSuccessCount.increment();
/* 220 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */     
/*     */     public void recordLoadException(long loadTime)
/*     */     {
/* 225 */       this.loadExceptionCount.increment();
/* 226 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */     
/*     */     public void recordEviction()
/*     */     {
/* 231 */       this.evictionCount.increment();
/*     */     }
/*     */     
/*     */     public CacheStats snapshot()
/*     */     {
/* 236 */       return new CacheStats(this.hitCount
/* 237 */         .sum(), this.missCount
/* 238 */         .sum(), this.loadSuccessCount
/* 239 */         .sum(), this.loadExceptionCount
/* 240 */         .sum(), this.totalLoadTime
/* 241 */         .sum(), this.evictionCount
/* 242 */         .sum());
/*     */     }
/*     */     
/*     */     public void incrementBy(AbstractCache.StatsCounter other)
/*     */     {
/* 247 */       CacheStats otherStats = other.snapshot();
/* 248 */       this.hitCount.add(otherStats.hitCount());
/* 249 */       this.missCount.add(otherStats.missCount());
/* 250 */       this.loadSuccessCount.add(otherStats.loadSuccessCount());
/* 251 */       this.loadExceptionCount.add(otherStats.loadExceptionCount());
/* 252 */       this.totalLoadTime.add(otherStats.totalLoadTime());
/* 253 */       this.evictionCount.add(otherStats.evictionCount());
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface StatsCounter
/*     */   {
/*     */     public abstract void recordHits(int paramInt);
/*     */     
/*     */     public abstract void recordMisses(int paramInt);
/*     */     
/*     */     public abstract void recordLoadSuccess(long paramLong);
/*     */     
/*     */     public abstract void recordLoadException(long paramLong);
/*     */     
/*     */     public abstract void recordEviction();
/*     */     
/*     */     public abstract CacheStats snapshot();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\AbstractCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */