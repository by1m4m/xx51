/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class BeanPropertyMap
/*     */   implements Iterable<SettableBeanProperty>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final Bucket[] _buckets;
/*     */   private final int _hashMask;
/*     */   private final int _size;
/*     */   private final boolean _caseInsensitive;
/*  51 */   private int _nextBucketIndex = 0;
/*     */   
/*     */   public BeanPropertyMap(Collection<SettableBeanProperty> properties, boolean caseInsensitivePropertyComparison)
/*     */   {
/*  55 */     this._caseInsensitive = caseInsensitivePropertyComparison;
/*  56 */     this._size = properties.size();
/*  57 */     int bucketCount = findSize(this._size);
/*  58 */     this._hashMask = (bucketCount - 1);
/*  59 */     Bucket[] buckets = new Bucket[bucketCount];
/*  60 */     for (SettableBeanProperty property : properties) {
/*  61 */       String key = getPropertyName(property);
/*  62 */       int index = key.hashCode() & this._hashMask;
/*  63 */       buckets[index] = new Bucket(buckets[index], key, property, this._nextBucketIndex++);
/*     */     }
/*  65 */     this._buckets = buckets;
/*     */   }
/*     */   
/*     */   private BeanPropertyMap(Bucket[] buckets, int size, int index, boolean caseInsensitivePropertyComparison)
/*     */   {
/*  70 */     this._buckets = buckets;
/*  71 */     this._size = size;
/*  72 */     this._hashMask = (buckets.length - 1);
/*  73 */     this._nextBucketIndex = index;
/*  74 */     this._caseInsensitive = caseInsensitivePropertyComparison;
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
/*     */ 
/*     */   public BeanPropertyMap withProperty(SettableBeanProperty newProperty)
/*     */   {
/*  89 */     int bcount = this._buckets.length;
/*  90 */     Bucket[] newBuckets = new Bucket[bcount];
/*  91 */     System.arraycopy(this._buckets, 0, newBuckets, 0, bcount);
/*  92 */     String propName = getPropertyName(newProperty);
/*     */     
/*  94 */     SettableBeanProperty oldProp = find(propName);
/*  95 */     if (oldProp == null)
/*     */     {
/*     */ 
/*     */ 
/*  99 */       int index = propName.hashCode() & this._hashMask;
/* 100 */       newBuckets[index] = new Bucket(newBuckets[index], propName, newProperty, this._nextBucketIndex++);
/*     */       
/* 102 */       return new BeanPropertyMap(newBuckets, this._size + 1, this._nextBucketIndex, this._caseInsensitive);
/*     */     }
/*     */     
/* 105 */     BeanPropertyMap newMap = new BeanPropertyMap(newBuckets, bcount, this._nextBucketIndex, this._caseInsensitive);
/* 106 */     newMap.replace(newProperty);
/* 107 */     return newMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyMap renameAll(NameTransformer transformer)
/*     */   {
/* 116 */     if ((transformer == null) || (transformer == NameTransformer.NOP)) {
/* 117 */       return this;
/*     */     }
/* 119 */     Iterator<SettableBeanProperty> it = iterator();
/* 120 */     ArrayList<SettableBeanProperty> newProps = new ArrayList();
/* 121 */     while (it.hasNext()) {
/* 122 */       SettableBeanProperty prop = (SettableBeanProperty)it.next();
/* 123 */       String newName = transformer.transform(prop.getName());
/* 124 */       prop = prop.withSimpleName(newName);
/* 125 */       JsonDeserializer<?> deser = prop.getValueDeserializer();
/* 126 */       if (deser != null)
/*     */       {
/* 128 */         JsonDeserializer<Object> newDeser = deser.unwrappingDeserializer(transformer);
/*     */         
/* 130 */         if (newDeser != deser) {
/* 131 */           prop = prop.withValueDeserializer(newDeser);
/*     */         }
/*     */       }
/* 134 */       newProps.add(prop);
/*     */     }
/*     */     
/* 137 */     return new BeanPropertyMap(newProps, this._caseInsensitive);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanPropertyMap assignIndexes()
/*     */   {
/* 143 */     int index = 0;
/* 144 */     for (Bucket bucket : this._buckets) {
/* 145 */       while (bucket != null) {
/* 146 */         bucket.value.assignIndex(index++);
/* 147 */         bucket = bucket.next;
/*     */       }
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final int findSize(int size)
/*     */   {
/* 156 */     int needed = size <= 32 ? size + size : size + (size >> 2);
/* 157 */     int result = 2;
/* 158 */     while (result < needed) {
/* 159 */       result += result;
/*     */     }
/* 161 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private String getPropertyName(SettableBeanProperty prop)
/*     */   {
/* 167 */     return this._caseInsensitive ? prop.getName().toLowerCase() : prop.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 179 */     StringBuilder sb = new StringBuilder();
/* 180 */     sb.append("Properties=[");
/* 181 */     int count = 0;
/* 182 */     for (SettableBeanProperty prop : getPropertiesInInsertionOrder())
/* 183 */       if (prop != null)
/*     */       {
/*     */ 
/* 186 */         if (count++ > 0) {
/* 187 */           sb.append(", ");
/*     */         }
/* 189 */         sb.append(prop.getName());
/* 190 */         sb.append('(');
/* 191 */         sb.append(prop.getType());
/* 192 */         sb.append(')');
/*     */       }
/* 194 */     sb.append(']');
/* 195 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<SettableBeanProperty> iterator()
/*     */   {
/* 203 */     return new IteratorImpl(this._buckets);
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
/*     */   public SettableBeanProperty[] getPropertiesInInsertionOrder()
/*     */   {
/* 216 */     int len = this._nextBucketIndex;
/* 217 */     SettableBeanProperty[] result = new SettableBeanProperty[len];
/* 218 */     for (Bucket root : this._buckets) {
/* 219 */       for (Bucket bucket = root; bucket != null; bucket = bucket.next) {
/* 220 */         result[bucket.index] = bucket.value;
/*     */       }
/*     */     }
/* 223 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 232 */     return this._size;
/*     */   }
/*     */   
/*     */   public SettableBeanProperty find(String key) {
/* 236 */     if (key == null) {
/* 237 */       throw new IllegalArgumentException("Can not pass null property name");
/*     */     }
/* 239 */     if (this._caseInsensitive) {
/* 240 */       key = key.toLowerCase();
/*     */     }
/* 242 */     int index = key.hashCode() & this._hashMask;
/* 243 */     Bucket bucket = this._buckets[index];
/*     */     
/* 245 */     if (bucket == null) {
/* 246 */       return null;
/*     */     }
/*     */     
/* 249 */     if (bucket.key == key) {
/* 250 */       return bucket.value;
/*     */     }
/* 252 */     while ((bucket = bucket.next) != null) {
/* 253 */       if (bucket.key == key) {
/* 254 */         return bucket.value;
/*     */       }
/*     */     }
/*     */     
/* 258 */     return _findWithEquals(key, index);
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
/*     */   public boolean findDeserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, String key)
/*     */     throws IOException
/*     */   {
/* 273 */     if (this._caseInsensitive) {
/* 274 */       key = key.toLowerCase();
/*     */     }
/* 276 */     int index = key.hashCode() & this._hashMask;
/* 277 */     Bucket bucket = this._buckets[index];
/*     */     
/* 279 */     if (bucket == null) {
/* 280 */       return false;
/*     */     }
/*     */     
/* 283 */     if (bucket.key == key) {
/*     */       try {
/* 285 */         bucket.value.deserializeAndSet(p, ctxt, bean);
/*     */       } catch (Exception e) {
/* 287 */         wrapAndThrow(e, bean, key, ctxt);
/*     */       }
/* 289 */       return true;
/*     */     }
/* 291 */     return _findDeserializeAndSet2(p, ctxt, bean, key, index);
/*     */   }
/*     */   
/*     */   private final boolean _findDeserializeAndSet2(JsonParser p, DeserializationContext ctxt, Object bean, String key, int index)
/*     */     throws IOException
/*     */   {
/* 297 */     SettableBeanProperty prop = null;
/* 298 */     Bucket bucket = this._buckets[index];
/*     */     do {
/* 300 */       if ((bucket = bucket.next) == null) {
/* 301 */         prop = _findWithEquals(key, index);
/* 302 */         if (prop != null) break;
/* 303 */         return false;
/*     */       }
/*     */       
/*     */     }
/* 307 */     while (bucket.key != key);
/* 308 */     prop = bucket.value;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 313 */       prop.deserializeAndSet(p, ctxt, bean);
/*     */     } catch (Exception e) {
/* 315 */       wrapAndThrow(e, bean, key, ctxt);
/*     */     }
/* 317 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettableBeanProperty find(int propertyIndex)
/*     */   {
/* 325 */     int i = 0; for (int end = this._buckets.length; i < end; i++) {
/* 326 */       for (Bucket bucket = this._buckets[i]; bucket != null; bucket = bucket.next) {
/* 327 */         if (bucket.index == propertyIndex) {
/* 328 */           return bucket.value;
/*     */         }
/*     */       }
/*     */     }
/* 332 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void replace(SettableBeanProperty property)
/*     */   {
/* 342 */     String name = getPropertyName(property);
/* 343 */     int index = name.hashCode() & this._buckets.length - 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 348 */     Bucket tail = null;
/* 349 */     int foundIndex = -1;
/*     */     
/* 351 */     for (Bucket bucket = this._buckets[index]; bucket != null; bucket = bucket.next)
/*     */     {
/* 353 */       if ((foundIndex < 0) && (bucket.key.equals(name))) {
/* 354 */         foundIndex = bucket.index;
/*     */       } else {
/* 356 */         tail = new Bucket(tail, bucket.key, bucket.value, bucket.index);
/*     */       }
/*     */     }
/*     */     
/* 360 */     if (foundIndex < 0) {
/* 361 */       throw new NoSuchElementException("No entry '" + property + "' found, can't replace");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 366 */     this._buckets[index] = new Bucket(tail, name, property, foundIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(SettableBeanProperty property)
/*     */   {
/* 376 */     String name = getPropertyName(property);
/* 377 */     int index = name.hashCode() & this._buckets.length - 1;
/* 378 */     Bucket tail = null;
/* 379 */     boolean found = false;
/*     */     
/* 381 */     for (Bucket bucket = this._buckets[index]; bucket != null; bucket = bucket.next)
/*     */     {
/* 383 */       if ((!found) && (bucket.key.equals(name))) {
/* 384 */         found = true;
/*     */       } else {
/* 386 */         tail = new Bucket(tail, bucket.key, bucket.value, bucket.index);
/*     */       }
/*     */     }
/* 389 */     if (!found) {
/* 390 */       throw new NoSuchElementException("No entry '" + property + "' found, can't remove");
/*     */     }
/* 392 */     this._buckets[index] = tail;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SettableBeanProperty _findWithEquals(String key, int index)
/*     */   {
/* 403 */     Bucket bucket = this._buckets[index];
/* 404 */     while (bucket != null) {
/* 405 */       if (key.equals(bucket.key)) {
/* 406 */         return bucket.value;
/*     */       }
/* 408 */       bucket = bucket.next;
/*     */     }
/* 410 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 420 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 421 */       t = t.getCause();
/*     */     }
/*     */     
/* 424 */     if ((t instanceof Error)) {
/* 425 */       throw ((Error)t);
/*     */     }
/*     */     
/* 428 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*     */     
/* 430 */     if ((t instanceof IOException)) {
/* 431 */       if ((!wrap) || (!(t instanceof JsonProcessingException))) {
/* 432 */         throw ((IOException)t);
/*     */       }
/* 434 */     } else if ((!wrap) && 
/* 435 */       ((t instanceof RuntimeException))) {
/* 436 */       throw ((RuntimeException)t);
/*     */     }
/*     */     
/* 439 */     throw JsonMappingException.wrapWithPath(t, bean, fieldName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class Bucket
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */     public final Bucket next;
/*     */     
/*     */ 
/*     */     public final String key;
/*     */     
/*     */ 
/*     */     public final SettableBeanProperty value;
/*     */     
/*     */ 
/*     */     public final int index;
/*     */     
/*     */ 
/*     */     public Bucket(Bucket next, String key, SettableBeanProperty value, int index)
/*     */     {
/* 464 */       this.next = next;
/* 465 */       this.key = key;
/* 466 */       this.value = value;
/* 467 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class IteratorImpl
/*     */     implements Iterator<SettableBeanProperty>
/*     */   {
/*     */     private final BeanPropertyMap.Bucket[] _buckets;
/*     */     
/*     */ 
/*     */ 
/*     */     private BeanPropertyMap.Bucket _currentBucket;
/*     */     
/*     */ 
/*     */     private int _nextBucketIndex;
/*     */     
/*     */ 
/*     */ 
/*     */     public IteratorImpl(BeanPropertyMap.Bucket[] buckets)
/*     */     {
/* 490 */       this._buckets = buckets;
/*     */       
/* 492 */       int i = 0;
/* 493 */       for (int len = this._buckets.length; i < len;) {
/* 494 */         BeanPropertyMap.Bucket b = this._buckets[(i++)];
/* 495 */         if (b != null) {
/* 496 */           this._currentBucket = b;
/* 497 */           break;
/*     */         }
/*     */       }
/* 500 */       this._nextBucketIndex = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 505 */       return this._currentBucket != null;
/*     */     }
/*     */     
/*     */ 
/*     */     public SettableBeanProperty next()
/*     */     {
/* 511 */       BeanPropertyMap.Bucket curr = this._currentBucket;
/* 512 */       if (curr == null) {
/* 513 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 516 */       BeanPropertyMap.Bucket b = curr.next;
/* 517 */       while ((b == null) && (this._nextBucketIndex < this._buckets.length)) {
/* 518 */         b = this._buckets[(this._nextBucketIndex++)];
/*     */       }
/* 520 */       this._currentBucket = b;
/* 521 */       return curr.value;
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 526 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\BeanPropertyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */