/*     */ package io.netty.util.collection;
/*     */ 
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public class ShortObjectHashMap<V>
/*     */   implements ShortObjectMap<V>
/*     */ {
/*     */   public static final int DEFAULT_CAPACITY = 8;
/*     */   public static final float DEFAULT_LOAD_FACTOR = 0.5F;
/*  49 */   private static final Object NULL_VALUE = new Object();
/*     */   
/*     */   private int maxSize;
/*     */   
/*     */   private final float loadFactor;
/*     */   
/*     */   private short[] keys;
/*     */   
/*     */   private V[] values;
/*     */   
/*     */   private int size;
/*     */   
/*     */   private int mask;
/*  62 */   private final Set<Short> keySet = new KeySet(null);
/*  63 */   private final Set<Map.Entry<Short, V>> entrySet = new EntrySet(null);
/*  64 */   private final Iterable<ShortObjectMap.PrimitiveEntry<V>> entries = new Iterable()
/*     */   {
/*     */     public Iterator<ShortObjectMap.PrimitiveEntry<V>> iterator() {
/*  67 */       return new ShortObjectHashMap.PrimitiveIterator(ShortObjectHashMap.this, null);
/*     */     }
/*     */   };
/*     */   
/*     */   public ShortObjectHashMap() {
/*  72 */     this(8, 0.5F);
/*     */   }
/*     */   
/*     */   public ShortObjectHashMap(int initialCapacity) {
/*  76 */     this(initialCapacity, 0.5F);
/*     */   }
/*     */   
/*     */   public ShortObjectHashMap(int initialCapacity, float loadFactor) {
/*  80 */     if ((loadFactor <= 0.0F) || (loadFactor > 1.0F))
/*     */     {
/*     */ 
/*  83 */       throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
/*     */     }
/*     */     
/*  86 */     this.loadFactor = loadFactor;
/*     */     
/*     */ 
/*  89 */     int capacity = MathUtil.safeFindNextPositivePowerOfTwo(initialCapacity);
/*  90 */     this.mask = (capacity - 1);
/*     */     
/*     */ 
/*  93 */     this.keys = new short[capacity];
/*     */     
/*  95 */     V[] temp = (Object[])new Object[capacity];
/*  96 */     this.values = temp;
/*     */     
/*     */ 
/*  99 */     this.maxSize = calcMaxSize(capacity);
/*     */   }
/*     */   
/*     */   private static <T> T toExternal(T value) {
/* 103 */     assert (value != null) : "null is not a legitimate internal value. Concurrent Modification?";
/* 104 */     return value == NULL_VALUE ? null : value;
/*     */   }
/*     */   
/*     */   private static <T> T toInternal(T value)
/*     */   {
/* 109 */     return (T)(value == null ? NULL_VALUE : value);
/*     */   }
/*     */   
/*     */   public V get(short key)
/*     */   {
/* 114 */     int index = indexOf(key);
/* 115 */     return index == -1 ? null : toExternal(this.values[index]);
/*     */   }
/*     */   
/*     */   public V put(short key, V value)
/*     */   {
/* 120 */     int startIndex = hashIndex(key);
/* 121 */     int index = startIndex;
/*     */     do
/*     */     {
/* 124 */       if (this.values[index] == null)
/*     */       {
/* 126 */         this.keys[index] = key;
/* 127 */         this.values[index] = toInternal(value);
/* 128 */         growSize();
/* 129 */         return null;
/*     */       }
/* 131 */       if (this.keys[index] == key)
/*     */       {
/* 133 */         V previousValue = this.values[index];
/* 134 */         this.values[index] = toInternal(value);
/* 135 */         return (V)toExternal(previousValue);
/*     */       }
/*     */       
/*     */     }
/* 139 */     while ((index = probeNext(index)) != startIndex);
/*     */     
/* 141 */     throw new IllegalStateException("Unable to insert");
/*     */   }
/*     */   
/*     */ 
/*     */   public void putAll(Map<? extends Short, ? extends V> sourceMap)
/*     */   {
/*     */     ShortObjectHashMap<V> source;
/* 148 */     if ((sourceMap instanceof ShortObjectHashMap))
/*     */     {
/*     */ 
/* 151 */       source = (ShortObjectHashMap)sourceMap;
/* 152 */       for (int i = 0; i < source.values.length; i++) {
/* 153 */         V sourceValue = source.values[i];
/* 154 */         if (sourceValue != null) {
/* 155 */           put(source.keys[i], sourceValue);
/*     */         }
/*     */       }
/* 158 */       return;
/*     */     }
/*     */     
/*     */ 
/* 162 */     for (Map.Entry<? extends Short, ? extends V> entry : sourceMap.entrySet()) {
/* 163 */       put((Short)entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public V remove(short key)
/*     */   {
/* 169 */     int index = indexOf(key);
/* 170 */     if (index == -1) {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     V prev = this.values[index];
/* 175 */     removeAt(index);
/* 176 */     return (V)toExternal(prev);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 181 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 186 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 191 */     Arrays.fill(this.keys, (short)0);
/* 192 */     Arrays.fill(this.values, null);
/* 193 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public boolean containsKey(short key)
/*     */   {
/* 198 */     return indexOf(key) >= 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsValue(Object value)
/*     */   {
/* 204 */     V v1 = toInternal(value);
/* 205 */     for (V v2 : this.values)
/*     */     {
/* 207 */       if ((v2 != null) && (v2.equals(v1))) {
/* 208 */         return true;
/*     */       }
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */   
/*     */   public Iterable<ShortObjectMap.PrimitiveEntry<V>> entries()
/*     */   {
/* 216 */     return this.entries;
/*     */   }
/*     */   
/*     */   public Collection<V> values()
/*     */   {
/* 221 */     new AbstractCollection()
/*     */     {
/*     */       public Iterator<V> iterator() {
/* 224 */         new Iterator() {
/* 225 */           final ShortObjectHashMap<V>.PrimitiveIterator iter = new ShortObjectHashMap.PrimitiveIterator(ShortObjectHashMap.this, null);
/*     */           
/*     */           public boolean hasNext()
/*     */           {
/* 229 */             return this.iter.hasNext();
/*     */           }
/*     */           
/*     */           public V next()
/*     */           {
/* 234 */             return (V)this.iter.next().value();
/*     */           }
/*     */           
/*     */           public void remove()
/*     */           {
/* 239 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/* 246 */         return ShortObjectHashMap.this.size;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 256 */     int hash = this.size;
/* 257 */     for (short key : this.keys)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */       hash ^= hashCode(key);
/*     */     }
/* 267 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 272 */     if (this == obj) {
/* 273 */       return true;
/*     */     }
/* 275 */     if (!(obj instanceof ShortObjectMap)) {
/* 276 */       return false;
/*     */     }
/*     */     
/* 279 */     ShortObjectMap other = (ShortObjectMap)obj;
/* 280 */     if (this.size != other.size()) {
/* 281 */       return false;
/*     */     }
/* 283 */     for (int i = 0; i < this.values.length; i++) {
/* 284 */       V value = this.values[i];
/* 285 */       if (value != null) {
/* 286 */         short key = this.keys[i];
/* 287 */         Object otherValue = other.get(key);
/* 288 */         if (value == NULL_VALUE) {
/* 289 */           if (otherValue != null) {
/* 290 */             return false;
/*     */           }
/* 292 */         } else if (!value.equals(otherValue)) {
/* 293 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 297 */     return true;
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 302 */     return containsKey(objectToKey(key));
/*     */   }
/*     */   
/*     */   public V get(Object key)
/*     */   {
/* 307 */     return (V)get(objectToKey(key));
/*     */   }
/*     */   
/*     */   public V put(Short key, V value)
/*     */   {
/* 312 */     return (V)put(objectToKey(key), value);
/*     */   }
/*     */   
/*     */   public V remove(Object key)
/*     */   {
/* 317 */     return (V)remove(objectToKey(key));
/*     */   }
/*     */   
/*     */   public Set<Short> keySet()
/*     */   {
/* 322 */     return this.keySet;
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<Short, V>> entrySet()
/*     */   {
/* 327 */     return this.entrySet;
/*     */   }
/*     */   
/*     */   private short objectToKey(Object key) {
/* 331 */     return ((Short)key).shortValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int indexOf(short key)
/*     */   {
/* 341 */     int startIndex = hashIndex(key);
/* 342 */     int index = startIndex;
/*     */     do
/*     */     {
/* 345 */       if (this.values[index] == null)
/*     */       {
/* 347 */         return -1;
/*     */       }
/* 349 */       if (key == this.keys[index]) {
/* 350 */         return index;
/*     */       }
/*     */       
/*     */     }
/* 354 */     while ((index = probeNext(index)) != startIndex);
/* 355 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int hashIndex(short key)
/*     */   {
/* 365 */     return hashCode(key) & this.mask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int hashCode(short key)
/*     */   {
/* 372 */     return key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int probeNext(int index)
/*     */   {
/* 380 */     return index + 1 & this.mask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void growSize()
/*     */   {
/* 387 */     this.size += 1;
/*     */     
/* 389 */     if (this.size > this.maxSize) {
/* 390 */       if (this.keys.length == Integer.MAX_VALUE) {
/* 391 */         throw new IllegalStateException("Max capacity reached at size=" + this.size);
/*     */       }
/*     */       
/*     */ 
/* 395 */       rehash(this.keys.length << 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean removeAt(int index)
/*     */   {
/* 407 */     this.size -= 1;
/*     */     
/*     */ 
/* 410 */     this.keys[index] = 0;
/* 411 */     this.values[index] = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 418 */     int nextFree = index;
/* 419 */     int i = probeNext(index);
/* 420 */     for (V value = this.values[i]; value != null; value = this.values[(i = probeNext(i))]) {
/* 421 */       short key = this.keys[i];
/* 422 */       int bucket = hashIndex(key);
/* 423 */       if (((i < bucket) && ((bucket <= nextFree) || (nextFree <= i))) || ((bucket <= nextFree) && (nextFree <= i)))
/*     */       {
/*     */ 
/* 426 */         this.keys[nextFree] = key;
/* 427 */         this.values[nextFree] = value;
/*     */         
/* 429 */         this.keys[i] = 0;
/* 430 */         this.values[i] = null;
/* 431 */         nextFree = i;
/*     */       }
/*     */     }
/* 434 */     return nextFree != index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int calcMaxSize(int capacity)
/*     */   {
/* 442 */     int upperBound = capacity - 1;
/* 443 */     return Math.min(upperBound, (int)(capacity * this.loadFactor));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void rehash(int newCapacity)
/*     */   {
/* 452 */     short[] oldKeys = this.keys;
/* 453 */     V[] oldVals = this.values;
/*     */     
/* 455 */     this.keys = new short[newCapacity];
/*     */     
/* 457 */     V[] temp = (Object[])new Object[newCapacity];
/* 458 */     this.values = temp;
/*     */     
/* 460 */     this.maxSize = calcMaxSize(newCapacity);
/* 461 */     this.mask = (newCapacity - 1);
/*     */     
/*     */ 
/* 464 */     for (int i = 0; i < oldVals.length; i++) {
/* 465 */       V oldVal = oldVals[i];
/* 466 */       if (oldVal != null)
/*     */       {
/*     */ 
/* 469 */         short oldKey = oldKeys[i];
/* 470 */         int index = hashIndex(oldKey);
/*     */         for (;;)
/*     */         {
/* 473 */           if (this.values[index] == null) {
/* 474 */             this.keys[index] = oldKey;
/* 475 */             this.values[index] = oldVal;
/* 476 */             break;
/*     */           }
/*     */           
/*     */ 
/* 480 */           index = probeNext(index);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 488 */     if (isEmpty()) {
/* 489 */       return "{}";
/*     */     }
/* 491 */     StringBuilder sb = new StringBuilder(4 * this.size);
/* 492 */     sb.append('{');
/* 493 */     boolean first = true;
/* 494 */     for (int i = 0; i < this.values.length; i++) {
/* 495 */       V value = this.values[i];
/* 496 */       if (value != null) {
/* 497 */         if (!first) {
/* 498 */           sb.append(", ");
/*     */         }
/* 500 */         sb.append(keyToString(this.keys[i])).append('=').append(value == this ? "(this Map)" : 
/* 501 */           toExternal(value));
/* 502 */         first = false;
/*     */       }
/*     */     }
/* 505 */     return '}';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String keyToString(short key)
/*     */   {
/* 513 */     return Short.toString(key);
/*     */   }
/*     */   
/*     */   private final class EntrySet extends AbstractSet<Map.Entry<Short, V>>
/*     */   {
/*     */     private EntrySet() {}
/*     */     
/*     */     public Iterator<Map.Entry<Short, V>> iterator()
/*     */     {
/* 522 */       return new ShortObjectHashMap.MapIterator(ShortObjectHashMap.this, null);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 527 */       return ShortObjectHashMap.this.size();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class KeySet extends AbstractSet<Short>
/*     */   {
/*     */     private KeySet() {}
/*     */     
/*     */     public int size()
/*     */     {
/* 537 */       return ShortObjectHashMap.this.size();
/*     */     }
/*     */     
/*     */     public boolean contains(Object o)
/*     */     {
/* 542 */       return ShortObjectHashMap.this.containsKey(o);
/*     */     }
/*     */     
/*     */     public boolean remove(Object o)
/*     */     {
/* 547 */       return ShortObjectHashMap.this.remove(o) != null;
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> retainedKeys)
/*     */     {
/* 552 */       boolean changed = false;
/* 553 */       for (Iterator<ShortObjectMap.PrimitiveEntry<V>> iter = ShortObjectHashMap.this.entries().iterator(); iter.hasNext();) {
/* 554 */         ShortObjectMap.PrimitiveEntry<V> entry = (ShortObjectMap.PrimitiveEntry)iter.next();
/* 555 */         if (!retainedKeys.contains(Short.valueOf(entry.key()))) {
/* 556 */           changed = true;
/* 557 */           iter.remove();
/*     */         }
/*     */       }
/* 560 */       return changed;
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 565 */       ShortObjectHashMap.this.clear();
/*     */     }
/*     */     
/*     */     public Iterator<Short> iterator()
/*     */     {
/* 570 */       new Iterator() {
/* 571 */         private final Iterator<Map.Entry<Short, V>> iter = ShortObjectHashMap.this.entrySet.iterator();
/*     */         
/*     */         public boolean hasNext()
/*     */         {
/* 575 */           return this.iter.hasNext();
/*     */         }
/*     */         
/*     */         public Short next()
/*     */         {
/* 580 */           return (Short)((Map.Entry)this.iter.next()).getKey();
/*     */         }
/*     */         
/*     */         public void remove()
/*     */         {
/* 585 */           this.iter.remove();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PrimitiveIterator implements Iterator<ShortObjectMap.PrimitiveEntry<V>>, ShortObjectMap.PrimitiveEntry<V>
/*     */   {
/*     */     private PrimitiveIterator() {}
/*     */     
/* 595 */     private int prevIndex = -1;
/* 596 */     private int nextIndex = -1;
/* 597 */     private int entryIndex = -1;
/*     */     
/*     */     private void scanNext() {
/* 600 */       while ((++this.nextIndex != ShortObjectHashMap.this.values.length) && (ShortObjectHashMap.this.values[this.nextIndex] == null)) {}
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 606 */       if (this.nextIndex == -1) {
/* 607 */         scanNext();
/*     */       }
/* 609 */       return this.nextIndex != ShortObjectHashMap.this.values.length;
/*     */     }
/*     */     
/*     */     public ShortObjectMap.PrimitiveEntry<V> next()
/*     */     {
/* 614 */       if (!hasNext()) {
/* 615 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 618 */       this.prevIndex = this.nextIndex;
/* 619 */       scanNext();
/*     */       
/*     */ 
/* 622 */       this.entryIndex = this.prevIndex;
/* 623 */       return this;
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 628 */       if (this.prevIndex == -1) {
/* 629 */         throw new IllegalStateException("next must be called before each remove.");
/*     */       }
/* 631 */       if (ShortObjectHashMap.this.removeAt(this.prevIndex))
/*     */       {
/*     */ 
/*     */ 
/* 635 */         this.nextIndex = this.prevIndex;
/*     */       }
/* 637 */       this.prevIndex = -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public short key()
/*     */     {
/* 645 */       return ShortObjectHashMap.this.keys[this.entryIndex];
/*     */     }
/*     */     
/*     */     public V value()
/*     */     {
/* 650 */       return (V)ShortObjectHashMap.toExternal(ShortObjectHashMap.this.values[this.entryIndex]);
/*     */     }
/*     */     
/*     */     public void setValue(V value)
/*     */     {
/* 655 */       ShortObjectHashMap.this.values[this.entryIndex] = ShortObjectHashMap.toInternal(value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class MapIterator
/*     */     implements Iterator<Map.Entry<Short, V>>
/*     */   {
/* 663 */     private final ShortObjectHashMap<V>.PrimitiveIterator iter = new ShortObjectHashMap.PrimitiveIterator(ShortObjectHashMap.this, null);
/*     */     
/*     */     private MapIterator() {}
/*     */     
/* 667 */     public boolean hasNext() { return this.iter.hasNext(); }
/*     */     
/*     */ 
/*     */     public Map.Entry<Short, V> next()
/*     */     {
/* 672 */       if (!hasNext()) {
/* 673 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 676 */       this.iter.next();
/*     */       
/* 678 */       return new ShortObjectHashMap.MapEntry(ShortObjectHashMap.this, ShortObjectHashMap.PrimitiveIterator.access$1100(this.iter));
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 683 */       this.iter.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   final class MapEntry
/*     */     implements Map.Entry<Short, V>
/*     */   {
/*     */     private final int entryIndex;
/*     */     
/*     */     MapEntry(int entryIndex)
/*     */     {
/* 694 */       this.entryIndex = entryIndex;
/*     */     }
/*     */     
/*     */     public Short getKey()
/*     */     {
/* 699 */       verifyExists();
/* 700 */       return Short.valueOf(ShortObjectHashMap.this.keys[this.entryIndex]);
/*     */     }
/*     */     
/*     */     public V getValue()
/*     */     {
/* 705 */       verifyExists();
/* 706 */       return (V)ShortObjectHashMap.toExternal(ShortObjectHashMap.this.values[this.entryIndex]);
/*     */     }
/*     */     
/*     */     public V setValue(V value)
/*     */     {
/* 711 */       verifyExists();
/* 712 */       V prevValue = ShortObjectHashMap.toExternal(ShortObjectHashMap.this.values[this.entryIndex]);
/* 713 */       ShortObjectHashMap.this.values[this.entryIndex] = ShortObjectHashMap.toInternal(value);
/* 714 */       return prevValue;
/*     */     }
/*     */     
/*     */     private void verifyExists() {
/* 718 */       if (ShortObjectHashMap.this.values[this.entryIndex] == null) {
/* 719 */         throw new IllegalStateException("The map entry has been removed");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\collection\ShortObjectHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */