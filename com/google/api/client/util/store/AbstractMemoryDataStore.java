/*     */ package com.google.api.client.util.store;
/*     */ 
/*     */ import com.google.api.client.util.IOUtils;
/*     */ import com.google.api.client.util.Lists;
/*     */ import com.google.api.client.util.Maps;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ class AbstractMemoryDataStore<V extends Serializable>
/*     */   extends AbstractDataStore<V>
/*     */ {
/*  43 */   private final Lock lock = new ReentrantLock();
/*     */   
/*     */ 
/*  46 */   HashMap<String, byte[]> keyValueMap = Maps.newHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractMemoryDataStore(DataStoreFactory dataStoreFactory, String id)
/*     */   {
/*  53 */     super(dataStoreFactory, id);
/*     */   }
/*     */   
/*     */   public final Set<String> keySet() throws IOException {
/*  57 */     this.lock.lock();
/*     */     try {
/*  59 */       return Collections.unmodifiableSet(this.keyValueMap.keySet());
/*     */     } finally {
/*  61 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public final Collection<V> values() throws IOException {
/*  66 */     this.lock.lock();
/*     */     try {
/*  68 */       List<V> result = Lists.newArrayList();
/*  69 */       for (byte[] bytes : this.keyValueMap.values()) {
/*  70 */         result.add(IOUtils.deserialize(bytes));
/*     */       }
/*  72 */       return Collections.unmodifiableList(result);
/*     */     } finally {
/*  74 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public final V get(String key) throws IOException {
/*  79 */     if (key == null) {
/*  80 */       return null;
/*     */     }
/*  82 */     this.lock.lock();
/*     */     try {
/*  84 */       return IOUtils.deserialize((byte[])this.keyValueMap.get(key));
/*     */     } finally {
/*  86 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public final DataStore<V> set(String key, V value) throws IOException {
/*  91 */     Preconditions.checkNotNull(key);
/*  92 */     Preconditions.checkNotNull(value);
/*  93 */     this.lock.lock();
/*     */     try {
/*  95 */       this.keyValueMap.put(key, IOUtils.serialize(value));
/*  96 */       save();
/*     */     } finally {
/*  98 */       this.lock.unlock();
/*     */     }
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public DataStore<V> delete(String key) throws IOException {
/* 104 */     if (key == null) {
/* 105 */       return this;
/*     */     }
/* 107 */     this.lock.lock();
/*     */     try {
/* 109 */       this.keyValueMap.remove(key);
/* 110 */       save();
/*     */     } finally {
/* 112 */       this.lock.unlock();
/*     */     }
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public final DataStore<V> clear() throws IOException {
/* 118 */     this.lock.lock();
/*     */     try {
/* 120 */       this.keyValueMap.clear();
/* 121 */       save();
/*     */     } finally {
/* 123 */       this.lock.unlock();
/*     */     }
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public boolean containsKey(String key) throws IOException
/*     */   {
/* 130 */     if (key == null) {
/* 131 */       return false;
/*     */     }
/* 133 */     this.lock.lock();
/*     */     try {
/* 135 */       return this.keyValueMap.containsKey(key);
/*     */     } finally {
/* 137 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean containsValue(V value) throws IOException
/*     */   {
/* 143 */     if (value == null) {
/* 144 */       return false;
/*     */     }
/* 146 */     this.lock.lock();
/*     */     try {
/* 148 */       byte[] serialized = IOUtils.serialize(value);
/* 149 */       for (byte[] bytes : this.keyValueMap.values()) {
/* 150 */         if (Arrays.equals(serialized, bytes)) {
/* 151 */           return true;
/*     */         }
/*     */       }
/* 154 */       return 0;
/*     */     } finally {
/* 156 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEmpty() throws IOException
/*     */   {
/* 162 */     this.lock.lock();
/*     */     try {
/* 164 */       return this.keyValueMap.isEmpty();
/*     */     } finally {
/* 166 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public int size() throws IOException
/*     */   {
/* 172 */     this.lock.lock();
/*     */     try {
/* 174 */       return this.keyValueMap.size();
/*     */     } finally {
/* 176 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void save()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 190 */     return DataStoreUtils.toString(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\AbstractMemoryDataStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */