/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingMap;
/*     */ import com.google.common.collect.ForwardingMapEntry;
/*     */ import com.google.common.collect.ForwardingSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @Beta
/*     */ public final class MutableTypeToInstanceMap<B>
/*     */   extends ForwardingMap<TypeToken<? extends B>, B>
/*     */   implements TypeToInstanceMap<B>
/*     */ {
/*  42 */   private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();
/*     */   
/*     */   public <T extends B> T getInstance(Class<T> type)
/*     */   {
/*  46 */     return (T)trustedGet(TypeToken.of(type));
/*     */   }
/*     */   
/*     */   public <T extends B> T getInstance(TypeToken<T> type)
/*     */   {
/*  51 */     return (T)trustedGet(type.rejectTypeVariables());
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, T value)
/*     */   {
/*  57 */     return (T)trustedPut(TypeToken.of(type), value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(TypeToken<T> type, T value)
/*     */   {
/*  63 */     return (T)trustedPut(type.rejectTypeVariables(), value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public B put(TypeToken<? extends B> key, B value)
/*     */   {
/*  76 */     throw new UnsupportedOperationException("Please use putInstance() instead.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map)
/*     */   {
/*  88 */     throw new UnsupportedOperationException("Please use putInstance() instead.");
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet()
/*     */   {
/*  93 */     return UnmodifiableEntry.transformEntries(super.entrySet());
/*     */   }
/*     */   
/*     */   protected Map<TypeToken<? extends B>, B> delegate()
/*     */   {
/*  98 */     return this.backingMap;
/*     */   }
/*     */   
/*     */   private <T extends B> T trustedPut(TypeToken<T> type, T value)
/*     */   {
/* 103 */     return (T)this.backingMap.put(type, value);
/*     */   }
/*     */   
/*     */   private <T extends B> T trustedGet(TypeToken<T> type)
/*     */   {
/* 108 */     return (T)this.backingMap.get(type);
/*     */   }
/*     */   
/*     */   private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V>
/*     */   {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     static <K, V> Set<Map.Entry<K, V>> transformEntries(Set<Map.Entry<K, V>> entries) {
/* 116 */       new ForwardingSet()
/*     */       {
/*     */         protected Set<Map.Entry<K, V>> delegate() {
/* 119 */           return this.val$entries;
/*     */         }
/*     */         
/*     */         public Iterator<Map.Entry<K, V>> iterator()
/*     */         {
/* 124 */           return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.iterator());
/*     */         }
/*     */         
/*     */         public Object[] toArray()
/*     */         {
/* 129 */           return standardToArray();
/*     */         }
/*     */         
/*     */         public <T> T[] toArray(T[] array)
/*     */         {
/* 134 */           return standardToArray(array);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     private static <K, V> Iterator<Map.Entry<K, V>> transformEntries(Iterator<Map.Entry<K, V>> entries) {
/* 140 */       Iterators.transform(entries, new Function()
/*     */       {
/*     */ 
/*     */         public Map.Entry<K, V> apply(Map.Entry<K, V> entry)
/*     */         {
/* 145 */           return new MutableTypeToInstanceMap.UnmodifiableEntry(entry, null);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     private UnmodifiableEntry(Map.Entry<K, V> delegate) {
/* 151 */       this.delegate = ((Map.Entry)Preconditions.checkNotNull(delegate));
/*     */     }
/*     */     
/*     */     protected Map.Entry<K, V> delegate()
/*     */     {
/* 156 */       return this.delegate;
/*     */     }
/*     */     
/*     */     public V setValue(V value)
/*     */     {
/* 161 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\reflect\MutableTypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */