/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingLoadingCache<K, V>
/*    */   extends ForwardingCache<K, V>
/*    */   implements LoadingCache<K, V>
/*    */ {
/*    */   protected abstract LoadingCache<K, V> delegate();
/*    */   
/*    */   public V get(K key)
/*    */     throws ExecutionException
/*    */   {
/* 45 */     return (V)delegate().get(key);
/*    */   }
/*    */   
/*    */   public V getUnchecked(K key)
/*    */   {
/* 50 */     return (V)delegate().getUnchecked(key);
/*    */   }
/*    */   
/*    */   public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException
/*    */   {
/* 55 */     return delegate().getAll(keys);
/*    */   }
/*    */   
/*    */   public V apply(K key)
/*    */   {
/* 60 */     return (V)delegate().apply(key);
/*    */   }
/*    */   
/*    */   public void refresh(K key)
/*    */   {
/* 65 */     delegate().refresh(key);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static abstract class SimpleForwardingLoadingCache<K, V>
/*    */     extends ForwardingLoadingCache<K, V>
/*    */   {
/*    */     private final LoadingCache<K, V> delegate;
/*    */     
/*    */ 
/*    */ 
/*    */     protected SimpleForwardingLoadingCache(LoadingCache<K, V> delegate)
/*    */     {
/* 79 */       this.delegate = ((LoadingCache)Preconditions.checkNotNull(delegate));
/*    */     }
/*    */     
/*    */     protected final LoadingCache<K, V> delegate()
/*    */     {
/* 84 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\ForwardingLoadingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */