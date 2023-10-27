/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.function.BiConsumer;
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
/*     */ @GwtCompatible(serializable=true, emulated=true)
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*  44 */   static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap((Map.Entry[])ImmutableMap.EMPTY_ENTRY_ARRAY, null, 0);
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final int MAX_HASH_BUCKET_LENGTH = 8;
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   final transient Map.Entry<K, V>[] entries;
/*     */   
/*     */ 
/*     */   private final transient ImmutableMapEntry<K, V>[] table;
/*     */   
/*     */ 
/*     */   private final transient int mask;
/*     */   
/*     */ 
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */ 
/*     */   static <K, V> ImmutableMap<K, V> fromEntries(Map.Entry<K, V>... entries)
/*     */   {
/*  74 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <K, V> ImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray)
/*     */   {
/*  83 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  84 */     if (n == 0)
/*  85 */       return (RegularImmutableMap)EMPTY;
/*     */     Map.Entry<K, V>[] entries;
/*     */     Map.Entry<K, V>[] entries;
/*  88 */     if (n == entryArray.length) {
/*  89 */       entries = entryArray;
/*     */     } else {
/*  91 */       entries = ImmutableMapEntry.createEntryArray(n);
/*     */     }
/*  93 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  94 */     ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
/*  95 */     int mask = tableSize - 1;
/*  96 */     for (int entryIndex = 0; entryIndex < n; entryIndex++) {
/*  97 */       Map.Entry<K, V> entry = entryArray[entryIndex];
/*  98 */       K key = entry.getKey();
/*  99 */       V value = entry.getValue();
/* 100 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 101 */       int tableIndex = Hashing.smear(key.hashCode()) & mask;
/* 102 */       ImmutableMapEntry<K, V> existing = table[tableIndex];
/*     */       
/*     */ 
/*     */ 
/* 106 */       ImmutableMapEntry<K, V> newEntry = existing == null ? makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableMapEntry(key, value, existing);
/*     */       
/* 108 */       table[tableIndex] = newEntry;
/* 109 */       entries[entryIndex] = newEntry;
/* 110 */       int bucketSize = checkNoConflictInKeyBucket(key, newEntry, existing);
/* 111 */       if (bucketSize > 8)
/*     */       {
/*     */ 
/* 114 */         return JdkBackedImmutableMap.create(n, entryArray);
/*     */       }
/*     */     }
/* 117 */     return new RegularImmutableMap(entries, table, mask);
/*     */   }
/*     */   
/*     */ 
/*     */   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Map.Entry<K, V> entry, K key, V value)
/*     */   {
/* 123 */     boolean reusable = ((entry instanceof ImmutableMapEntry)) && (((ImmutableMapEntry)entry).isReusable());
/* 124 */     return reusable ? (ImmutableMapEntry)entry : new ImmutableMapEntry(key, value);
/*     */   }
/*     */   
/*     */   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Map.Entry<K, V> entry)
/*     */   {
/* 129 */     return makeImmutable(entry, entry.getKey(), entry.getValue());
/*     */   }
/*     */   
/*     */   private RegularImmutableMap(Map.Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
/* 133 */     this.entries = entries;
/* 134 */     this.table = table;
/* 135 */     this.mask = mask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNoConflictInKeyBucket(Object key, Map.Entry<?, ?> entry, ImmutableMapEntry<?, ?> keyBucketHead)
/*     */   {
/* 145 */     int bucketSize = 0;
/* 146 */     for (; keyBucketHead != null; keyBucketHead = keyBucketHead.getNextInKeyBucket()) {
/* 147 */       checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
/* 148 */       bucketSize++;
/*     */     }
/* 150 */     return bucketSize;
/*     */   }
/*     */   
/*     */   public V get(Object key)
/*     */   {
/* 155 */     return (V)get(key, this.table, this.mask);
/*     */   }
/*     */   
/*     */   static <V> V get(Object key, ImmutableMapEntry<?, V>[] keyTable, int mask)
/*     */   {
/* 160 */     if ((key == null) || (keyTable == null)) {
/* 161 */       return null;
/*     */     }
/* 163 */     int index = Hashing.smear(key.hashCode()) & mask;
/* 164 */     for (ImmutableMapEntry<?, V> entry = keyTable[index]; 
/* 165 */         entry != null; 
/* 166 */         entry = entry.getNextInKeyBucket()) {
/* 167 */       Object candidateKey = entry.getKey();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */       if (key.equals(candidateKey)) {
/* 176 */         return (V)entry.getValue();
/*     */       }
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action)
/*     */   {
/* 184 */     Preconditions.checkNotNull(action);
/* 185 */     for (Map.Entry<K, V> entry : this.entries) {
/* 186 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 192 */     return this.entries.length;
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet()
/*     */   {
/* 202 */     return new ImmutableMapEntrySet.RegularEntrySet(this, this.entries);
/*     */   }
/*     */   
/*     */   ImmutableSet<K> createKeySet()
/*     */   {
/* 207 */     return new KeySet(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated=true)
/*     */   private static final class KeySet<K, V> extends IndexedImmutableSet<K> {
/*     */     @Weak
/*     */     private final RegularImmutableMap<K, V> map;
/*     */     
/* 215 */     KeySet(RegularImmutableMap<K, V> map) { this.map = map; }
/*     */     
/*     */ 
/*     */     K get(int index)
/*     */     {
/* 220 */       return (K)this.map.entries[index].getKey();
/*     */     }
/*     */     
/*     */     public boolean contains(Object object)
/*     */     {
/* 225 */       return this.map.containsKey(object);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 230 */       return true;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 235 */       return this.map.size();
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace()
/*     */     {
/* 241 */       return new SerializedForm(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<K> implements Serializable {
/*     */       final ImmutableMap<K, ?> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 249 */       SerializedForm(ImmutableMap<K, ?> map) { this.map = map; }
/*     */       
/*     */       Object readResolve()
/*     */       {
/* 253 */         return this.map.keySet();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   ImmutableCollection<V> createValues()
/*     */   {
/* 262 */     return new Values(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated=true)
/*     */   private static final class Values<K, V> extends ImmutableList<V> {
/*     */     @Weak
/*     */     final RegularImmutableMap<K, V> map;
/*     */     
/* 270 */     Values(RegularImmutableMap<K, V> map) { this.map = map; }
/*     */     
/*     */ 
/*     */     public V get(int index)
/*     */     {
/* 275 */       return (V)this.map.entries[index].getValue();
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 280 */       return this.map.size();
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 285 */       return true;
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace()
/*     */     {
/* 291 */       return new SerializedForm(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<V> implements Serializable {
/*     */       final ImmutableMap<?, V> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 299 */       SerializedForm(ImmutableMap<?, V> map) { this.map = map; }
/*     */       
/*     */       Object readResolve()
/*     */       {
/* 303 */         return this.map.values();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */