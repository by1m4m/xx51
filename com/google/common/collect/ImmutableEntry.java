/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable=true)
/*    */ class ImmutableEntry<K, V>
/*    */   extends AbstractMapEntry<K, V>
/*    */   implements Serializable
/*    */ {
/*    */   final K key;
/*    */   final V value;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ImmutableEntry(K key, V value)
/*    */   {
/* 30 */     this.key = key;
/* 31 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final K getKey()
/*    */   {
/* 36 */     return (K)this.key;
/*    */   }
/*    */   
/*    */   public final V getValue()
/*    */   {
/* 41 */     return (V)this.value;
/*    */   }
/*    */   
/*    */   public final V setValue(V value)
/*    */   {
/* 46 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */