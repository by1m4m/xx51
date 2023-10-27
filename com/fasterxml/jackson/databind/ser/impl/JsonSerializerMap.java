/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.SerializerCache.TypeKey;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonSerializerMap
/*    */ {
/*    */   private final Bucket[] _buckets;
/*    */   private final int _size;
/*    */   
/*    */   public JsonSerializerMap(Map<SerializerCache.TypeKey, JsonSerializer<Object>> serializers)
/*    */   {
/* 23 */     int size = findSize(serializers.size());
/* 24 */     this._size = size;
/* 25 */     int hashMask = size - 1;
/* 26 */     Bucket[] buckets = new Bucket[size];
/* 27 */     for (Map.Entry<SerializerCache.TypeKey, JsonSerializer<Object>> entry : serializers.entrySet()) {
/* 28 */       SerializerCache.TypeKey key = (SerializerCache.TypeKey)entry.getKey();
/* 29 */       int index = key.hashCode() & hashMask;
/* 30 */       buckets[index] = new Bucket(buckets[index], key, (JsonSerializer)entry.getValue());
/*    */     }
/* 32 */     this._buckets = buckets;
/*    */   }
/*    */   
/*    */ 
/*    */   private static final int findSize(int size)
/*    */   {
/* 38 */     int needed = size <= 64 ? size + size : size + (size >> 2);
/* 39 */     int result = 8;
/* 40 */     while (result < needed) {
/* 41 */       result += result;
/*    */     }
/* 43 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int size()
/*    */   {
/* 52 */     return this._size;
/*    */   }
/*    */   
/*    */   public JsonSerializer<Object> find(SerializerCache.TypeKey key) {
/* 56 */     int index = key.hashCode() & this._buckets.length - 1;
/* 57 */     Bucket bucket = this._buckets[index];
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 62 */     if (bucket == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     if (key.equals(bucket.key)) {
/* 66 */       return bucket.value;
/*    */     }
/* 68 */     while ((bucket = bucket.next) != null) {
/* 69 */       if (key.equals(bucket.key)) {
/* 70 */         return bucket.value;
/*    */       }
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static final class Bucket
/*    */   {
/*    */     public final SerializerCache.TypeKey key;
/*    */     
/*    */ 
/*    */     public final JsonSerializer<Object> value;
/*    */     
/*    */     public final Bucket next;
/*    */     
/*    */ 
/*    */     public Bucket(Bucket next, SerializerCache.TypeKey key, JsonSerializer<Object> value)
/*    */     {
/* 90 */       this.next = next;
/* 91 */       this.key = key;
/* 92 */       this.value = value;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\JsonSerializerMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */