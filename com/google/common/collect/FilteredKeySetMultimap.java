/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
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
/*    */ @GwtCompatible
/*    */ final class FilteredKeySetMultimap<K, V>
/*    */   extends FilteredKeyMultimap<K, V>
/*    */   implements FilteredSetMultimap<K, V>
/*    */ {
/*    */   FilteredKeySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate)
/*    */   {
/* 35 */     super(unfiltered, keyPredicate);
/*    */   }
/*    */   
/*    */   public SetMultimap<K, V> unfiltered()
/*    */   {
/* 40 */     return (SetMultimap)this.unfiltered;
/*    */   }
/*    */   
/*    */   public Set<V> get(K key)
/*    */   {
/* 45 */     return (Set)super.get(key);
/*    */   }
/*    */   
/*    */   public Set<V> removeAll(Object key)
/*    */   {
/* 50 */     return (Set)super.removeAll(key);
/*    */   }
/*    */   
/*    */   public Set<V> replaceValues(K key, Iterable<? extends V> values)
/*    */   {
/* 55 */     return (Set)super.replaceValues(key, values);
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<K, V>> entries()
/*    */   {
/* 60 */     return (Set)super.entries();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 65 */   Set<Map.Entry<K, V>> createEntries() { return new EntrySet(); }
/*    */   
/*    */   class EntrySet extends FilteredKeyMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> {
/* 68 */     EntrySet() { super(); }
/*    */     
/*    */     public int hashCode() {
/* 71 */       return Sets.hashCodeImpl(this);
/*    */     }
/*    */     
/*    */     public boolean equals(Object o)
/*    */     {
/* 76 */       return Sets.equalsImpl(this, o);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\FilteredKeySetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */