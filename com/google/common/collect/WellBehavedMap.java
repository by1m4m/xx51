/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible
/*    */ final class WellBehavedMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */ {
/*    */   private final Map<K, V> delegate;
/*    */   private Set<Map.Entry<K, V>> entrySet;
/*    */   
/*    */   private WellBehavedMap(Map<K, V> delegate)
/*    */   {
/* 41 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static <K, V> WellBehavedMap<K, V> wrap(Map<K, V> delegate)
/*    */   {
/* 50 */     return new WellBehavedMap(delegate);
/*    */   }
/*    */   
/*    */   protected Map<K, V> delegate()
/*    */   {
/* 55 */     return this.delegate;
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<K, V>> entrySet()
/*    */   {
/* 60 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 61 */     if (es != null) {
/* 62 */       return es;
/*    */     }
/* 64 */     return this.entrySet = new EntrySet(null);
/*    */   }
/*    */   
/*    */   private final class EntrySet extends Maps.EntrySet<K, V> {
/*    */     private EntrySet() {}
/*    */     
/*    */     Map<K, V> map() {
/* 71 */       return WellBehavedMap.this;
/*    */     }
/*    */     
/*    */     public Iterator<Map.Entry<K, V>> iterator()
/*    */     {
/* 76 */       new TransformedIterator(WellBehavedMap.this.keySet().iterator())
/*    */       {
/*    */         Map.Entry<K, V> transform(final K key) {
/* 79 */           new AbstractMapEntry()
/*    */           {
/*    */             public K getKey() {
/* 82 */               return (K)key;
/*    */             }
/*    */             
/*    */             public V getValue()
/*    */             {
/* 87 */               return (V)WellBehavedMap.this.get(key);
/*    */             }
/*    */             
/*    */             public V setValue(V value)
/*    */             {
/* 92 */               return (V)WellBehavedMap.this.put(key, value);
/*    */             }
/*    */           };
/*    */         }
/*    */       };
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\WellBehavedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */