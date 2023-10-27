/*    */ package io.netty.channel.pool;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import io.netty.util.internal.ReadOnlyIterator;
/*    */ import java.io.Closeable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ public abstract class AbstractChannelPoolMap<K, P extends ChannelPool>
/*    */   implements ChannelPoolMap<K, P>, Iterable<Map.Entry<K, P>>, Closeable
/*    */ {
/* 34 */   private final ConcurrentMap<K, P> map = PlatformDependent.newConcurrentHashMap();
/*    */   
/*    */   public final P get(K key)
/*    */   {
/* 38 */     P pool = (ChannelPool)this.map.get(ObjectUtil.checkNotNull(key, "key"));
/* 39 */     if (pool == null) {
/* 40 */       pool = newPool(key);
/* 41 */       P old = (ChannelPool)this.map.putIfAbsent(key, pool);
/* 42 */       if (old != null)
/*    */       {
/* 44 */         pool.close();
/* 45 */         pool = old;
/*    */       }
/*    */     }
/* 48 */     return pool;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final boolean remove(K key)
/*    */   {
/* 57 */     P pool = (ChannelPool)this.map.remove(ObjectUtil.checkNotNull(key, "key"));
/* 58 */     if (pool != null) {
/* 59 */       pool.close();
/* 60 */       return true;
/*    */     }
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   public final Iterator<Map.Entry<K, P>> iterator()
/*    */   {
/* 67 */     return new ReadOnlyIterator(this.map.entrySet().iterator());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final int size()
/*    */   {
/* 74 */     return this.map.size();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final boolean isEmpty()
/*    */   {
/* 81 */     return this.map.isEmpty();
/*    */   }
/*    */   
/*    */   public final boolean contains(K key)
/*    */   {
/* 86 */     return this.map.containsKey(ObjectUtil.checkNotNull(key, "key"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected abstract P newPool(K paramK);
/*    */   
/*    */ 
/*    */   public final void close()
/*    */   {
/* 96 */     for (K key : this.map.keySet()) {
/* 97 */       remove(key);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\pool\AbstractChannelPoolMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */