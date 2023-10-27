/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.SerializerCache.TypeKey;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ReadOnlyClassToSerializerMap
/*    */ {
/*    */   protected final JsonSerializerMap _map;
/* 27 */   protected SerializerCache.TypeKey _cacheKey = null;
/*    */   
/*    */   private ReadOnlyClassToSerializerMap(JsonSerializerMap map) {
/* 30 */     this._map = map;
/*    */   }
/*    */   
/*    */   public ReadOnlyClassToSerializerMap instance() {
/* 34 */     return new ReadOnlyClassToSerializerMap(this._map);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ReadOnlyClassToSerializerMap from(HashMap<SerializerCache.TypeKey, JsonSerializer<Object>> src)
/*    */   {
/* 43 */     return new ReadOnlyClassToSerializerMap(new JsonSerializerMap(src));
/*    */   }
/*    */   
/*    */   public JsonSerializer<Object> typedValueSerializer(JavaType type) {
/* 47 */     if (this._cacheKey == null) {
/* 48 */       this._cacheKey = new SerializerCache.TypeKey(type, true);
/*    */     } else {
/* 50 */       this._cacheKey.resetTyped(type);
/*    */     }
/* 52 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */   
/*    */   public JsonSerializer<Object> typedValueSerializer(Class<?> cls) {
/* 56 */     if (this._cacheKey == null) {
/* 57 */       this._cacheKey = new SerializerCache.TypeKey(cls, true);
/*    */     } else {
/* 59 */       this._cacheKey.resetTyped(cls);
/*    */     }
/* 61 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */   
/*    */   public JsonSerializer<Object> untypedValueSerializer(JavaType type) {
/* 65 */     if (this._cacheKey == null) {
/* 66 */       this._cacheKey = new SerializerCache.TypeKey(type, false);
/*    */     } else {
/* 68 */       this._cacheKey.resetUntyped(type);
/*    */     }
/* 70 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */   
/*    */   public JsonSerializer<Object> untypedValueSerializer(Class<?> cls) {
/* 74 */     if (this._cacheKey == null) {
/* 75 */       this._cacheKey = new SerializerCache.TypeKey(cls, false);
/*    */     } else {
/* 77 */       this._cacheKey.resetUntyped(cls);
/*    */     }
/* 79 */     return this._map.find(this._cacheKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\ReadOnlyClassToSerializerMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */