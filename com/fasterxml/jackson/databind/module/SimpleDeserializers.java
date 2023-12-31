/*     */ package com.fasterxml.jackson.databind.module;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.type.ClassKey;
/*     */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class SimpleDeserializers implements Deserializers, java.io.Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3006673354353448880L;
/*  25 */   protected HashMap<ClassKey, JsonDeserializer<?>> _classMappings = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   protected boolean _hasEnumDeserializer = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleDeserializers() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleDeserializers(Map<Class<?>, JsonDeserializer<?>> desers)
/*     */   {
/*  46 */     addDeserializers(desers);
/*     */   }
/*     */   
/*     */   public <T> void addDeserializer(Class<T> forClass, JsonDeserializer<? extends T> deser)
/*     */   {
/*  51 */     ClassKey key = new ClassKey(forClass);
/*  52 */     if (this._classMappings == null) {
/*  53 */       this._classMappings = new HashMap();
/*     */     }
/*  55 */     this._classMappings.put(key, deser);
/*     */     
/*  57 */     if (forClass == Enum.class) {
/*  58 */       this._hasEnumDeserializer = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDeserializers(Map<Class<?>, JsonDeserializer<?>> desers)
/*     */   {
/*  68 */     for (Map.Entry<Class<?>, JsonDeserializer<?>> entry : desers.entrySet()) {
/*  69 */       Class<?> cls = (Class)entry.getKey();
/*     */       
/*  71 */       JsonDeserializer<Object> deser = (JsonDeserializer)entry.getValue();
/*  72 */       addDeserializer(cls, deser);
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
/*     */   public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */     throws JsonMappingException
/*     */   {
/*  88 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/*  96 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findCollectionDeserializer(CollectionType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */     throws JsonMappingException
/*     */   {
/* 106 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */     throws JsonMappingException
/*     */   {
/* 116 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 124 */     if (this._classMappings == null) {
/* 125 */       return null;
/*     */     }
/* 127 */     JsonDeserializer<?> deser = (JsonDeserializer)this._classMappings.get(new ClassKey(type));
/* 128 */     if ((deser == null) && 
/* 129 */       (this._hasEnumDeserializer) && (type.isEnum())) {
/* 130 */       deser = (JsonDeserializer)this._classMappings.get(new ClassKey(Enum.class));
/*     */     }
/*     */     
/* 133 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findMapDeserializer(MapType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */     throws JsonMappingException
/*     */   {
/* 144 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*     */     throws JsonMappingException
/*     */   {
/* 155 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(type.getRawClass()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType, DeserializationConfig config, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 163 */     return this._classMappings == null ? null : (JsonDeserializer)this._classMappings.get(new ClassKey(nodeType));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\module\SimpleDeserializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */