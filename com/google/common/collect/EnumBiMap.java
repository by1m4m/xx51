/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   private transient Class<V> valueType;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType)
/*     */   {
/*  53 */     return new EnumBiMap(keyType, valueType);
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
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map)
/*     */   {
/*  66 */     EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
/*  67 */     bimap.putAll(map);
/*  68 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumBiMap(Class<K> keyType, Class<V> valueType) {
/*  72 */     super(
/*  73 */       WellBehavedMap.wrap(new EnumMap(keyType)), 
/*  74 */       WellBehavedMap.wrap(new EnumMap(valueType)));
/*  75 */     this.keyType = keyType;
/*  76 */     this.valueType = valueType;
/*     */   }
/*     */   
/*     */   static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
/*  80 */     if ((map instanceof EnumBiMap)) {
/*  81 */       return ((EnumBiMap)map).keyType();
/*     */     }
/*  83 */     if ((map instanceof EnumHashBiMap)) {
/*  84 */       return ((EnumHashBiMap)map).keyType();
/*     */     }
/*  86 */     Preconditions.checkArgument(!map.isEmpty());
/*  87 */     return ((Enum)map.keySet().iterator().next()).getDeclaringClass();
/*     */   }
/*     */   
/*     */   private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
/*  91 */     if ((map instanceof EnumBiMap)) {
/*  92 */       return ((EnumBiMap)map).valueType;
/*     */     }
/*  94 */     Preconditions.checkArgument(!map.isEmpty());
/*  95 */     return ((Enum)map.values().iterator().next()).getDeclaringClass();
/*     */   }
/*     */   
/*     */   public Class<K> keyType()
/*     */   {
/* 100 */     return this.keyType;
/*     */   }
/*     */   
/*     */   public Class<V> valueType()
/*     */   {
/* 105 */     return this.valueType;
/*     */   }
/*     */   
/*     */   K checkKey(K key)
/*     */   {
/* 110 */     return (Enum)Preconditions.checkNotNull(key);
/*     */   }
/*     */   
/*     */   V checkValue(V value)
/*     */   {
/* 115 */     return (Enum)Preconditions.checkNotNull(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream)
/*     */     throws IOException
/*     */   {
/* 124 */     stream.defaultWriteObject();
/* 125 */     stream.writeObject(this.keyType);
/* 126 */     stream.writeObject(this.valueType);
/* 127 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
/*     */   {
/* 133 */     stream.defaultReadObject();
/* 134 */     this.keyType = ((Class)stream.readObject());
/* 135 */     this.valueType = ((Class)stream.readObject());
/* 136 */     setDelegates(
/* 137 */       WellBehavedMap.wrap(new EnumMap(this.keyType)), 
/* 138 */       WellBehavedMap.wrap(new EnumMap(this.valueType)));
/* 139 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\EnumBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */