/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class ArrayMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements Cloneable
/*     */ {
/*     */   int size;
/*     */   private Object[] data;
/*     */   
/*     */   public static <K, V> ArrayMap<K, V> create()
/*     */   {
/*  57 */     return new ArrayMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K, V> ArrayMap<K, V> create(int initialCapacity)
/*     */   {
/*  65 */     ArrayMap<K, V> result = create();
/*  66 */     result.ensureCapacity(initialCapacity);
/*  67 */     return result;
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
/*     */   public static <K, V> ArrayMap<K, V> of(Object... keyValuePairs)
/*     */   {
/*  81 */     ArrayMap<K, V> result = create(1);
/*  82 */     int length = keyValuePairs.length;
/*  83 */     if (1 == length % 2) {
/*  84 */       String str = String.valueOf(String.valueOf(keyValuePairs[(length - 1)]));throw new IllegalArgumentException(28 + str.length() + "missing value for last key: " + str);
/*     */     }
/*     */     
/*  87 */     result.size = (keyValuePairs.length / 2);
/*  88 */     Object[] data = result.data = new Object[length];
/*  89 */     System.arraycopy(keyValuePairs, 0, data, 0, length);
/*  90 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public final int size()
/*     */   {
/*  96 */     return this.size;
/*     */   }
/*     */   
/*     */   public final K getKey(int index)
/*     */   {
/* 101 */     if ((index < 0) || (index >= this.size)) {
/* 102 */       return null;
/*     */     }
/*     */     
/* 105 */     K result = this.data[(index << 1)];
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   public final V getValue(int index)
/*     */   {
/* 111 */     if ((index < 0) || (index >= this.size)) {
/* 112 */       return null;
/*     */     }
/* 114 */     return (V)valueAtDataIndex(1 + (index << 1));
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
/*     */   public final V set(int index, K key, V value)
/*     */   {
/* 128 */     if (index < 0) {
/* 129 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 131 */     int minSize = index + 1;
/* 132 */     ensureCapacity(minSize);
/* 133 */     int dataIndex = index << 1;
/* 134 */     V result = valueAtDataIndex(dataIndex + 1);
/* 135 */     setData(dataIndex, key, value);
/* 136 */     if (minSize > this.size) {
/* 137 */       this.size = minSize;
/*     */     }
/* 139 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V set(int index, V value)
/*     */   {
/* 149 */     int size = this.size;
/* 150 */     if ((index < 0) || (index >= size)) {
/* 151 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 153 */     int valueDataIndex = 1 + (index << 1);
/* 154 */     V result = valueAtDataIndex(valueDataIndex);
/* 155 */     this.data[valueDataIndex] = value;
/* 156 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void add(K key, V value)
/*     */   {
/* 166 */     set(this.size, key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V remove(int index)
/*     */   {
/* 175 */     return (V)removeFromDataIndexOfKey(index << 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean containsKey(Object key)
/*     */   {
/* 181 */     return -2 != getDataIndexOfKey(key);
/*     */   }
/*     */   
/*     */   public final int getIndexOfKey(K key)
/*     */   {
/* 186 */     return getDataIndexOfKey(key) >> 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V get(Object key)
/*     */   {
/* 195 */     return (V)valueAtDataIndex(getDataIndexOfKey(key) + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V put(K key, V value)
/*     */   {
/* 205 */     int index = getIndexOfKey(key);
/* 206 */     if (index == -1) {
/* 207 */       index = this.size;
/*     */     }
/* 209 */     return (V)set(index, key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V remove(Object key)
/*     */   {
/* 219 */     return (V)removeFromDataIndexOfKey(getDataIndexOfKey(key));
/*     */   }
/*     */   
/*     */   public final void trim()
/*     */   {
/* 224 */     setDataCapacity(this.size << 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void ensureCapacity(int minCapacity)
/*     */   {
/* 231 */     if (minCapacity < 0) {
/* 232 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 234 */     Object[] data = this.data;
/* 235 */     int minDataCapacity = minCapacity << 1;
/* 236 */     int oldDataCapacity = data == null ? 0 : data.length;
/* 237 */     if (minDataCapacity > oldDataCapacity) {
/* 238 */       int newDataCapacity = oldDataCapacity / 2 * 3 + 1;
/* 239 */       if (newDataCapacity % 2 != 0) {
/* 240 */         newDataCapacity++;
/*     */       }
/* 242 */       if (newDataCapacity < minDataCapacity) {
/* 243 */         newDataCapacity = minDataCapacity;
/*     */       }
/* 245 */       setDataCapacity(newDataCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setDataCapacity(int newDataCapacity) {
/* 250 */     if (newDataCapacity == 0) {
/* 251 */       this.data = null;
/* 252 */       return;
/*     */     }
/* 254 */     int size = this.size;
/* 255 */     Object[] oldData = this.data;
/* 256 */     if ((size == 0) || (newDataCapacity != oldData.length)) {
/* 257 */       Object[] newData = this.data = new Object[newDataCapacity];
/* 258 */       if (size != 0) {
/* 259 */         System.arraycopy(oldData, 0, newData, 0, size << 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setData(int dataIndexOfKey, K key, V value) {
/* 265 */     Object[] data = this.data;
/* 266 */     data[dataIndexOfKey] = key;
/* 267 */     data[(dataIndexOfKey + 1)] = value;
/*     */   }
/*     */   
/*     */   private V valueAtDataIndex(int dataIndex) {
/* 271 */     if (dataIndex < 0) {
/* 272 */       return null;
/*     */     }
/*     */     
/* 275 */     V result = this.data[dataIndex];
/* 276 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int getDataIndexOfKey(Object key)
/*     */   {
/* 283 */     int dataSize = this.size << 1;
/* 284 */     Object[] data = this.data;
/* 285 */     for (int i = 0; i < dataSize; i += 2) {
/* 286 */       Object k = data[i];
/* 287 */       if (key == null ? k == null : key.equals(k)) {
/* 288 */         return i;
/*     */       }
/*     */     }
/* 291 */     return -2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private V removeFromDataIndexOfKey(int dataIndexOfKey)
/*     */   {
/* 299 */     int dataSize = this.size << 1;
/* 300 */     if ((dataIndexOfKey < 0) || (dataIndexOfKey >= dataSize)) {
/* 301 */       return null;
/*     */     }
/* 303 */     V result = valueAtDataIndex(dataIndexOfKey + 1);
/* 304 */     Object[] data = this.data;
/* 305 */     int moved = dataSize - dataIndexOfKey - 2;
/* 306 */     if (moved != 0) {
/* 307 */       System.arraycopy(data, dataIndexOfKey + 2, data, dataIndexOfKey, moved);
/*     */     }
/* 309 */     this.size -= 1;
/* 310 */     setData(dataSize - 2, null, null);
/* 311 */     return result;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 316 */     this.size = 0;
/* 317 */     this.data = null;
/*     */   }
/*     */   
/*     */   public final boolean containsValue(Object value)
/*     */   {
/* 322 */     int dataSize = this.size << 1;
/* 323 */     Object[] data = this.data;
/* 324 */     for (int i = 1; i < dataSize; i += 2) {
/* 325 */       Object v = data[i];
/* 326 */       if (value == null ? v == null : value.equals(v)) {
/* 327 */         return true;
/*     */       }
/*     */     }
/* 330 */     return false;
/*     */   }
/*     */   
/*     */   public final Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 335 */     return new EntrySet();
/*     */   }
/*     */   
/*     */   public ArrayMap<K, V> clone()
/*     */   {
/*     */     try
/*     */     {
/* 342 */       ArrayMap<K, V> result = (ArrayMap)super.clone();
/* 343 */       Object[] data = this.data;
/* 344 */       if (data != null) {
/* 345 */         int length = data.length;
/* 346 */         Object[] resultData = result.data = new Object[length];
/* 347 */         System.arraycopy(data, 0, resultData, 0, length);
/*     */       }
/* 349 */       return result;
/*     */     }
/*     */     catch (CloneNotSupportedException e) {}
/* 352 */     return null;
/*     */   }
/*     */   
/*     */   final class EntrySet extends AbstractSet<Map.Entry<K, V>>
/*     */   {
/*     */     EntrySet() {}
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 360 */       return new ArrayMap.EntryIterator(ArrayMap.this);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 365 */       return ArrayMap.this.size;
/*     */     }
/*     */   }
/*     */   
/*     */   final class EntryIterator implements Iterator<Map.Entry<K, V>> {
/*     */     private boolean removed;
/*     */     private int nextIndex;
/*     */     
/*     */     EntryIterator() {}
/*     */     
/* 375 */     public boolean hasNext() { return this.nextIndex < ArrayMap.this.size; }
/*     */     
/*     */     public Map.Entry<K, V> next()
/*     */     {
/* 379 */       int index = this.nextIndex;
/* 380 */       if (index == ArrayMap.this.size) {
/* 381 */         throw new NoSuchElementException();
/*     */       }
/* 383 */       this.nextIndex += 1;
/* 384 */       return new ArrayMap.Entry(ArrayMap.this, index);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 388 */       int index = this.nextIndex - 1;
/* 389 */       if ((this.removed) || (index < 0)) {
/* 390 */         throw new IllegalArgumentException();
/*     */       }
/* 392 */       ArrayMap.this.remove(index);
/* 393 */       this.removed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   final class Entry implements Map.Entry<K, V>
/*     */   {
/*     */     private int index;
/*     */     
/*     */     Entry(int index) {
/* 402 */       this.index = index;
/*     */     }
/*     */     
/*     */     public K getKey() {
/* 406 */       return (K)ArrayMap.this.getKey(this.index);
/*     */     }
/*     */     
/*     */     public V getValue() {
/* 410 */       return (V)ArrayMap.this.getValue(this.index);
/*     */     }
/*     */     
/*     */     public V setValue(V value) {
/* 414 */       return (V)ArrayMap.this.set(this.index, value);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 419 */       return getKey().hashCode() ^ getValue().hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 424 */       if (this == obj) {
/* 425 */         return true;
/*     */       }
/* 427 */       if (!(obj instanceof Map.Entry)) {
/* 428 */         return false;
/*     */       }
/* 430 */       Map.Entry<?, ?> other = (Map.Entry)obj;
/* 431 */       return (Objects.equal(getKey(), other.getKey())) && (Objects.equal(getValue(), other.getValue()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ArrayMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */