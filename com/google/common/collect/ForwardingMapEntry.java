/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Map.Entry;
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
/*     */ public abstract class ForwardingMapEntry<K, V>
/*     */   extends ForwardingObject
/*     */   implements Map.Entry<K, V>
/*     */ {
/*     */   protected abstract Map.Entry<K, V> delegate();
/*     */   
/*     */   public K getKey()
/*     */   {
/*  61 */     return (K)delegate().getKey();
/*     */   }
/*     */   
/*     */   public V getValue()
/*     */   {
/*  66 */     return (V)delegate().getValue();
/*     */   }
/*     */   
/*     */   public V setValue(V value)
/*     */   {
/*  71 */     return (V)delegate().setValue(value);
/*     */   }
/*     */   
/*     */   public boolean equals(Object object)
/*     */   {
/*  76 */     return delegate().equals(object);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  81 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean standardEquals(Object object)
/*     */   {
/*  92 */     if ((object instanceof Map.Entry)) {
/*  93 */       Map.Entry<?, ?> that = (Map.Entry)object;
/*  94 */       return (Objects.equal(getKey(), that.getKey())) && 
/*  95 */         (Objects.equal(getValue(), that.getValue()));
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int standardHashCode()
/*     */   {
/* 108 */     K k = getKey();
/* 109 */     V v = getValue();
/* 110 */     return (k == null ? 0 : k.hashCode()) ^ (v == null ? 0 : v.hashCode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   protected String standardToString()
/*     */   {
/* 122 */     return getKey() + "=" + getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ForwardingMapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */