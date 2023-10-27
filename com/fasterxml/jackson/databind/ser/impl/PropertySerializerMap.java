/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.util.Arrays;
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
/*     */ public abstract class PropertySerializerMap
/*     */ {
/*     */   protected final boolean _resetWhenFull;
/*     */   
/*     */   protected PropertySerializerMap(boolean resetWhenFull)
/*     */   {
/*  36 */     this._resetWhenFull = resetWhenFull;
/*     */   }
/*     */   
/*     */   protected PropertySerializerMap(PropertySerializerMap base) {
/*  40 */     this._resetWhenFull = base._resetWhenFull;
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
/*     */   public abstract JsonSerializer<Object> serializerFor(Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddPrimarySerializer(Class<?> type, SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  64 */     JsonSerializer<Object> serializer = provider.findPrimaryPropertySerializer(type, property);
/*  65 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */   
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddPrimarySerializer(JavaType type, SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  72 */     JsonSerializer<Object> serializer = provider.findPrimaryPropertySerializer(type, property);
/*  73 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
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
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddSecondarySerializer(Class<?> type, SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  90 */     JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
/*  91 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */   
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddSecondarySerializer(JavaType type, SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  98 */     JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
/*  99 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
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
/*     */ 
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddRootValueSerializer(Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 117 */     JsonSerializer<Object> serializer = provider.findTypedValueSerializer(type, false, null);
/* 118 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SerializerAndMapResult findAndAddRootValueSerializer(JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 128 */     JsonSerializer<Object> serializer = provider.findTypedValueSerializer(type, false, null);
/* 129 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final SerializerAndMapResult addSerializer(Class<?> type, JsonSerializer<Object> serializer)
/*     */   {
/* 139 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final SerializerAndMapResult addSerializer(JavaType type, JsonSerializer<Object> serializer)
/*     */   {
/* 146 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer);
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public static PropertySerializerMap emptyMap()
/*     */   {
/* 156 */     return emptyForProperties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static PropertySerializerMap emptyForProperties()
/*     */   {
/* 163 */     return Empty.FOR_PROPERTIES;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static PropertySerializerMap emptyForRootValues()
/*     */   {
/* 170 */     return Empty.FOR_ROOT_VALUES;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class SerializerAndMapResult
/*     */   {
/*     */     public final JsonSerializer<Object> serializer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final PropertySerializerMap map;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public SerializerAndMapResult(JsonSerializer<Object> serializer, PropertySerializerMap map)
/*     */     {
/* 191 */       this.serializer = serializer;
/* 192 */       this.map = map;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class TypeAndSerializer
/*     */   {
/*     */     public final Class<?> type;
/*     */     
/*     */     public final JsonSerializer<Object> serializer;
/*     */     
/*     */     public TypeAndSerializer(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 205 */       this.type = type;
/* 206 */       this.serializer = serializer;
/*     */     }
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
/*     */   private static final class Empty
/*     */     extends PropertySerializerMap
/*     */   {
/* 223 */     public static final Empty FOR_PROPERTIES = new Empty(false);
/*     */     
/*     */ 
/* 226 */     public static final Empty FOR_ROOT_VALUES = new Empty(true);
/*     */     
/*     */     protected Empty(boolean resetWhenFull) {
/* 229 */       super();
/*     */     }
/*     */     
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 234 */       return null;
/*     */     }
/*     */     
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 239 */       return new PropertySerializerMap.Single(this, type, serializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class Single
/*     */     extends PropertySerializerMap
/*     */   {
/*     */     private final Class<?> _type;
/*     */     
/*     */     private final JsonSerializer<Object> _serializer;
/*     */     
/*     */ 
/*     */     public Single(PropertySerializerMap base, Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 255 */       super();
/* 256 */       this._type = type;
/* 257 */       this._serializer = serializer;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 263 */       if (type == this._type) {
/* 264 */         return this._serializer;
/*     */       }
/* 266 */       return null;
/*     */     }
/*     */     
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 271 */       return new PropertySerializerMap.Double(this, this._type, this._serializer, type, serializer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Double extends PropertySerializerMap
/*     */   {
/*     */     private final Class<?> _type1;
/*     */     private final Class<?> _type2;
/*     */     private final JsonSerializer<Object> _serializer1;
/*     */     private final JsonSerializer<Object> _serializer2;
/*     */     
/*     */     public Double(PropertySerializerMap base, Class<?> type1, JsonSerializer<Object> serializer1, Class<?> type2, JsonSerializer<Object> serializer2)
/*     */     {
/* 284 */       super();
/* 285 */       this._type1 = type1;
/* 286 */       this._serializer1 = serializer1;
/* 287 */       this._type2 = type2;
/* 288 */       this._serializer2 = serializer2;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 294 */       if (type == this._type1) {
/* 295 */         return this._serializer1;
/*     */       }
/* 297 */       if (type == this._type2) {
/* 298 */         return this._serializer2;
/*     */       }
/* 300 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 306 */       PropertySerializerMap.TypeAndSerializer[] ts = new PropertySerializerMap.TypeAndSerializer[3];
/* 307 */       ts[0] = new PropertySerializerMap.TypeAndSerializer(this._type1, this._serializer1);
/* 308 */       ts[1] = new PropertySerializerMap.TypeAndSerializer(this._type2, this._serializer2);
/* 309 */       ts[2] = new PropertySerializerMap.TypeAndSerializer(type, serializer);
/* 310 */       return new PropertySerializerMap.Multi(this, ts);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class Multi
/*     */     extends PropertySerializerMap
/*     */   {
/*     */     private static final int MAX_ENTRIES = 8;
/*     */     
/*     */ 
/*     */     private final PropertySerializerMap.TypeAndSerializer[] _entries;
/*     */     
/*     */ 
/*     */ 
/*     */     public Multi(PropertySerializerMap base, PropertySerializerMap.TypeAndSerializer[] entries)
/*     */     {
/* 329 */       super();
/* 330 */       this._entries = entries;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type)
/*     */     {
/* 336 */       int i = 0; for (int len = this._entries.length; i < len; i++) {
/* 337 */         PropertySerializerMap.TypeAndSerializer entry = this._entries[i];
/* 338 */         if (entry.type == type) {
/* 339 */           return entry.serializer;
/*     */         }
/*     */       }
/* 342 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer)
/*     */     {
/* 348 */       int len = this._entries.length;
/*     */       
/*     */ 
/* 351 */       if (len == 8) {
/* 352 */         if (this._resetWhenFull) {
/* 353 */           return new PropertySerializerMap.Single(this, type, serializer);
/*     */         }
/* 355 */         return this;
/*     */       }
/* 357 */       PropertySerializerMap.TypeAndSerializer[] entries = (PropertySerializerMap.TypeAndSerializer[])Arrays.copyOf(this._entries, len + 1);
/* 358 */       entries[len] = new PropertySerializerMap.TypeAndSerializer(type, serializer);
/* 359 */       return new Multi(this, entries);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\PropertySerializerMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */