/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class EnumHashBiMap<K extends Enum<K>, V>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> keyType)
/*     */   {
/*  53 */     return new EnumHashBiMap(keyType);
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
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> map)
/*     */   {
/*  67 */     EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyType(map));
/*  68 */     bimap.putAll(map);
/*  69 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumHashBiMap(Class<K> keyType) {
/*  73 */     super(
/*  74 */       WellBehavedMap.wrap(new EnumMap(keyType)), 
/*  75 */       Maps.newHashMapWithExpectedSize(((Enum[])keyType.getEnumConstants()).length));
/*  76 */     this.keyType = keyType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   K checkKey(K key)
/*     */   {
/*  83 */     return (Enum)Preconditions.checkNotNull(key);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value)
/*     */   {
/*  89 */     return (V)super.put(key, value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value)
/*     */   {
/*  95 */     return (V)super.forcePut(key, value);
/*     */   }
/*     */   
/*     */   public Class<K> keyType()
/*     */   {
/* 100 */     return this.keyType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream)
/*     */     throws IOException
/*     */   {
/* 109 */     stream.defaultWriteObject();
/* 110 */     stream.writeObject(this.keyType);
/* 111 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
/*     */   {
/* 117 */     stream.defaultReadObject();
/* 118 */     this.keyType = ((Class)stream.readObject());
/* 119 */     setDelegates(
/* 120 */       WellBehavedMap.wrap(new EnumMap(this.keyType)), new HashMap(
/* 121 */       ((Enum[])this.keyType.getEnumConstants()).length * 3 / 2));
/* 122 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\EnumHashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */