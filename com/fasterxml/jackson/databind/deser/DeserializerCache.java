/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.NoClassDefFoundDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class DeserializerCache
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  43 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers = new ConcurrentHashMap(64, 0.75F, 4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers = new HashMap(8);
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
/*     */   Object writeReplace()
/*     */   {
/*  70 */     this._incompleteDeserializers.clear();
/*     */     
/*  72 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int cachedDeserializersCount()
/*     */   {
/*  94 */     return this._cachedDeserializers.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushCachedDeserializers()
/*     */   {
/* 105 */     this._cachedDeserializers.clear();
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
/*     */   public JsonDeserializer<Object> findValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType propertyType)
/*     */     throws JsonMappingException
/*     */   {
/* 140 */     JsonDeserializer<Object> deser = _findCachedDeserializer(propertyType);
/* 141 */     if (deser == null)
/*     */     {
/* 143 */       deser = _createAndCacheValueDeserializer(ctxt, factory, propertyType);
/* 144 */       if (deser == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 149 */         deser = _handleUnknownValueDeserializer(propertyType);
/*     */       }
/*     */     }
/* 152 */     return deser;
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
/*     */   public KeyDeserializer findKeyDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 167 */     KeyDeserializer kd = factory.createKeyDeserializer(ctxt, type);
/* 168 */     if (kd == null) {
/* 169 */       return _handleUnknownKeyDeserializer(type);
/*     */     }
/*     */     
/* 172 */     if ((kd instanceof ResolvableDeserializer)) {
/* 173 */       ((ResolvableDeserializer)kd).resolve(ctxt);
/*     */     }
/* 175 */     return kd;
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
/*     */   public boolean hasValueDeserializerFor(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 190 */     JsonDeserializer<Object> deser = _findCachedDeserializer(type);
/* 191 */     if (deser == null) {
/* 192 */       deser = _createAndCacheValueDeserializer(ctxt, factory, type);
/*     */     }
/* 194 */     return deser != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type)
/*     */   {
/* 205 */     if (type == null) {
/* 206 */       throw new IllegalArgumentException("Null JavaType passed");
/*     */     }
/* 208 */     if (_hasCustomValueHandler(type)) {
/* 209 */       return null;
/*     */     }
/* 211 */     return (JsonDeserializer)this._cachedDeserializers.get(type);
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
/*     */   protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 229 */     synchronized (this._incompleteDeserializers)
/*     */     {
/* 231 */       JsonDeserializer<Object> deser = _findCachedDeserializer(type);
/* 232 */       if (deser != null) {
/* 233 */         return deser;
/*     */       }
/* 235 */       int count = this._incompleteDeserializers.size();
/*     */       
/* 237 */       if (count > 0) {
/* 238 */         deser = (JsonDeserializer)this._incompleteDeserializers.get(type);
/* 239 */         if (deser != null) {
/* 240 */           return deser;
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 245 */         JsonDeserializer localJsonDeserializer = _createAndCache2(ctxt, factory, type);
/*     */         
/*     */ 
/* 248 */         if ((count == 0) && (this._incompleteDeserializers.size() > 0))
/* 249 */           this._incompleteDeserializers.clear(); return localJsonDeserializer;
/*     */       }
/*     */       finally
/*     */       {
/* 248 */         if ((count == 0) && (this._incompleteDeserializers.size() > 0)) {
/* 249 */           this._incompleteDeserializers.clear();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _createAndCache2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonDeserializer<Object> deser;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 265 */       deser = _createDeserializer(ctxt, factory, type);
/*     */ 
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/* 270 */       throw new JsonMappingException(iae.getMessage(), null, iae);
/*     */     }
/* 272 */     if (deser == null) {
/* 273 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 279 */     boolean isResolvable = deser instanceof ResolvableDeserializer;
/*     */     
/* 281 */     boolean addToCache = (!_hasCustomValueHandler(type)) && (deser.isCachable());
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
/* 295 */     if (isResolvable) {
/* 296 */       this._incompleteDeserializers.put(type, deser);
/* 297 */       ((ResolvableDeserializer)deser).resolve(ctxt);
/* 298 */       this._incompleteDeserializers.remove(type);
/*     */     }
/* 300 */     if (addToCache) {
/* 301 */       this._cachedDeserializers.put(type, deser);
/*     */     }
/* 303 */     return deser;
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
/*     */ 
/*     */   protected JsonDeserializer<Object> _createDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 322 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/*     */ 
/* 325 */     if ((type.isAbstract()) || (type.isMapLikeType()) || (type.isCollectionLikeType())) {
/* 326 */       type = factory.mapAbstractType(config, type);
/*     */     }
/*     */     BeanDescription beanDesc;
/*     */     try {
/* 330 */       beanDesc = config.introspect(type);
/*     */     } catch (NoClassDefFoundError error) {
/* 332 */       return new NoClassDefFoundDeserializer(error);
/*     */     }
/*     */     
/* 335 */     JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
/*     */     
/* 337 */     if (deser != null) {
/* 338 */       return deser;
/*     */     }
/*     */     
/*     */ 
/* 342 */     JavaType newType = modifyTypeByAnnotation(ctxt, beanDesc.getClassInfo(), type);
/* 343 */     if (newType != type) {
/* 344 */       type = newType;
/* 345 */       beanDesc = config.introspect(newType);
/*     */     }
/*     */     
/*     */ 
/* 349 */     Class<?> builder = beanDesc.findPOJOBuilder();
/* 350 */     if (builder != null) {
/* 351 */       return factory.createBuilderBasedDeserializer(ctxt, type, beanDesc, builder);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 356 */     Converter<Object, Object> conv = beanDesc.findDeserializationConverter();
/* 357 */     if (conv == null) {
/* 358 */       return _createDeserializer2(ctxt, factory, type, beanDesc);
/*     */     }
/*     */     
/* 361 */     JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*     */     
/* 363 */     if (!delegateType.hasRawClass(type.getRawClass())) {
/* 364 */       beanDesc = config.introspect(delegateType);
/*     */     }
/* 366 */     return new StdDelegatingDeserializer(conv, delegateType, _createDeserializer2(ctxt, factory, delegateType, beanDesc));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<?> _createDeserializer2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 374 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 376 */     if (type.isEnumType()) {
/* 377 */       return factory.createEnumDeserializer(ctxt, type, beanDesc);
/*     */     }
/* 379 */     if (type.isContainerType()) {
/* 380 */       if (type.isArrayType()) {
/* 381 */         return factory.createArrayDeserializer(ctxt, (ArrayType)type, beanDesc);
/*     */       }
/* 383 */       if (type.isMapLikeType()) {
/* 384 */         MapLikeType mlt = (MapLikeType)type;
/* 385 */         if (mlt.isTrueMapType()) {
/* 386 */           return factory.createMapDeserializer(ctxt, (MapType)mlt, beanDesc);
/*     */         }
/* 388 */         return factory.createMapLikeDeserializer(ctxt, mlt, beanDesc);
/*     */       }
/* 390 */       if (type.isCollectionLikeType())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 396 */         JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/* 397 */         if ((format == null) || (format.getShape() != JsonFormat.Shape.OBJECT)) {
/* 398 */           CollectionLikeType clt = (CollectionLikeType)type;
/* 399 */           if (clt.isTrueCollectionType()) {
/* 400 */             return factory.createCollectionDeserializer(ctxt, (CollectionType)clt, beanDesc);
/*     */           }
/* 402 */           return factory.createCollectionLikeDeserializer(ctxt, clt, beanDesc);
/*     */         }
/*     */       }
/*     */     }
/* 406 */     if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
/* 407 */       return factory.createTreeDeserializer(config, type, beanDesc);
/*     */     }
/* 409 */     return factory.createBeanDeserializer(ctxt, type, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann)
/*     */     throws JsonMappingException
/*     */   {
/* 421 */     Object deserDef = ctxt.getAnnotationIntrospector().findDeserializer(ann);
/* 422 */     if (deserDef == null) {
/* 423 */       return null;
/*     */     }
/* 425 */     JsonDeserializer<Object> deser = ctxt.deserializerInstance(ann, deserDef);
/*     */     
/* 427 */     return findConvertingDeserializer(ctxt, ann, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, Annotated a, JsonDeserializer<Object> deser)
/*     */     throws JsonMappingException
/*     */   {
/* 440 */     Converter<Object, Object> conv = findConverter(ctxt, a);
/* 441 */     if (conv == null) {
/* 442 */       return deser;
/*     */     }
/* 444 */     JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 445 */     return new StdDelegatingDeserializer(conv, delegateType, deser);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Converter<Object, Object> findConverter(DeserializationContext ctxt, Annotated a)
/*     */     throws JsonMappingException
/*     */   {
/* 452 */     Object convDef = ctxt.getAnnotationIntrospector().findDeserializationConverter(a);
/* 453 */     if (convDef == null) {
/* 454 */       return null;
/*     */     }
/* 456 */     return ctxt.converterInstance(a, convDef);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JavaType modifyTypeByAnnotation(DeserializationContext ctxt, Annotated a, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 479 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 480 */     Class<?> subclass = intr.findDeserializationType(a, type);
/* 481 */     if (subclass != null) {
/*     */       try {
/* 483 */         type = type.narrowBy(subclass);
/*     */       } catch (IllegalArgumentException iae) {
/* 485 */         throw new JsonMappingException("Failed to narrow type " + type + " with concrete-type annotation (value " + subclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage(), null, iae);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 490 */     if (type.isContainerType()) {
/* 491 */       Class<?> keyClass = intr.findDeserializationKeyType(a, type.getKeyType());
/* 492 */       if (keyClass != null)
/*     */       {
/* 494 */         if (!(type instanceof MapLikeType)) {
/* 495 */           throw new JsonMappingException("Illegal key-type annotation: type " + type + " is not a Map(-like) type");
/*     */         }
/*     */         try {
/* 498 */           type = ((MapLikeType)type).narrowKey(keyClass);
/*     */         } catch (IllegalArgumentException iae) {
/* 500 */           throw new JsonMappingException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae.getMessage(), null, iae);
/*     */         }
/*     */       }
/* 503 */       JavaType keyType = type.getKeyType();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 508 */       if ((keyType != null) && (keyType.getValueHandler() == null)) {
/* 509 */         Object kdDef = intr.findKeyDeserializer(a);
/* 510 */         if (kdDef != null) {
/* 511 */           KeyDeserializer kd = ctxt.keyDeserializerInstance(a, kdDef);
/* 512 */           if (kd != null) {
/* 513 */             type = ((MapLikeType)type).withKeyValueHandler(kd);
/* 514 */             keyType = type.getKeyType();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 520 */       Class<?> cc = intr.findDeserializationContentType(a, type.getContentType());
/* 521 */       if (cc != null) {
/*     */         try {
/* 523 */           type = type.narrowContentsBy(cc);
/*     */         } catch (IllegalArgumentException iae) {
/* 525 */           throw new JsonMappingException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae.getMessage(), null, iae);
/*     */         }
/*     */       }
/*     */       
/* 529 */       JavaType contentType = type.getContentType();
/* 530 */       if (contentType.getValueHandler() == null) {
/* 531 */         Object cdDef = intr.findContentDeserializer(a);
/* 532 */         if (cdDef != null) {
/* 533 */           JsonDeserializer<?> cd = null;
/* 534 */           if ((cdDef instanceof JsonDeserializer)) {
/* 535 */             cdDef = (JsonDeserializer)cdDef;
/*     */           } else {
/* 537 */             Class<?> cdClass = _verifyAsClass(cdDef, "findContentDeserializer", JsonDeserializer.None.class);
/* 538 */             if (cdClass != null) {
/* 539 */               cd = ctxt.deserializerInstance(a, cdClass);
/*     */             }
/*     */           }
/* 542 */           if (cd != null) {
/* 543 */             type = type.withContentValueHandler(cd);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 548 */     return type;
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
/*     */   private boolean _hasCustomValueHandler(JavaType t)
/*     */   {
/* 564 */     if (t.isContainerType()) {
/* 565 */       JavaType ct = t.getContentType();
/* 566 */       return (ct != null) && (ct.getValueHandler() != null);
/*     */     }
/* 568 */     return false;
/*     */   }
/*     */   
/*     */   private Class<?> _verifyAsClass(Object src, String methodName, Class<?> noneClass)
/*     */   {
/* 573 */     if (src == null) {
/* 574 */       return null;
/*     */     }
/* 576 */     if (!(src instanceof Class)) {
/* 577 */       throw new IllegalStateException("AnnotationIntrospector." + methodName + "() returned value of type " + src.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
/*     */     }
/* 579 */     Class<?> cls = (Class)src;
/* 580 */     if ((cls == noneClass) || (ClassUtil.isBogusClass(cls))) {
/* 581 */       return null;
/*     */     }
/* 583 */     return cls;
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
/*     */   protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 598 */     Class<?> rawClass = type.getRawClass();
/* 599 */     if (!ClassUtil.isConcrete(rawClass)) {
/* 600 */       throw new JsonMappingException("Can not find a Value deserializer for abstract type " + type);
/*     */     }
/* 602 */     throw new JsonMappingException("Can not find a Value deserializer for type " + type);
/*     */   }
/*     */   
/*     */   protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 608 */     throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + type);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\DeserializerCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */