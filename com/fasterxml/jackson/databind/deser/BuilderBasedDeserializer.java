/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
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
/*     */ public class BuilderBasedDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _buildMethod;
/*     */   
/*     */   public BuilderBasedDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews)
/*     */   {
/*  45 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
/*     */     
/*  47 */     this._buildMethod = builder.getBuildMethod();
/*     */     
/*  49 */     if (this._objectIdReader != null) {
/*  50 */       throw new IllegalArgumentException("Can not use Object Id with Builder-based deserialization (type " + beanDesc.getType() + ")");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src)
/*     */   {
/*  61 */     this(src, src._ignoreAllUnknown);
/*     */   }
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src, boolean ignoreAllUnknown)
/*     */   {
/*  66 */     super(src, ignoreAllUnknown);
/*  67 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src, NameTransformer unwrapper) {
/*  71 */     super(src, unwrapper);
/*  72 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, ObjectIdReader oir) {
/*  76 */     super(src, oir);
/*  77 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, HashSet<String> ignorableProps) {
/*  81 */     super(src, ignorableProps);
/*  82 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  92 */     return new BuilderBasedDeserializer(this, unwrapper);
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer withObjectIdReader(ObjectIdReader oir)
/*     */   {
/*  97 */     return new BuilderBasedDeserializer(this, oir);
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer withIgnorableProperties(HashSet<String> ignorableProps)
/*     */   {
/* 102 */     return new BuilderBasedDeserializer(this, ignorableProps);
/*     */   }
/*     */   
/*     */   protected BeanAsArrayBuilderDeserializer asArrayDeserializer()
/*     */   {
/* 107 */     SettableBeanProperty[] props = this._beanProperties.getPropertiesInInsertionOrder();
/* 108 */     return new BeanAsArrayBuilderDeserializer(this, props, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object finishBuild(DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 121 */       return this._buildMethod.getMember().invoke(builder, new Object[0]);
/*     */     } catch (Exception e) {
/* 123 */       wrapInstantiationProblem(e, ctxt); }
/* 124 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 135 */     JsonToken t = jp.getCurrentToken();
/*     */     
/*     */ 
/* 138 */     if (t == JsonToken.START_OBJECT) {
/* 139 */       t = jp.nextToken();
/* 140 */       if (this._vanillaProcessing) {
/* 141 */         return finishBuild(ctxt, vanillaDeserialize(jp, ctxt, t));
/*     */       }
/* 143 */       Object builder = deserializeFromObject(jp, ctxt);
/* 144 */       return finishBuild(ctxt, builder);
/*     */     }
/*     */     
/* 147 */     switch (t) {
/*     */     case VALUE_STRING: 
/* 149 */       return finishBuild(ctxt, deserializeFromString(jp, ctxt));
/*     */     case VALUE_NUMBER_INT: 
/* 151 */       return finishBuild(ctxt, deserializeFromNumber(jp, ctxt));
/*     */     case VALUE_NUMBER_FLOAT: 
/* 153 */       return finishBuild(ctxt, deserializeFromDouble(jp, ctxt));
/*     */     case VALUE_EMBEDDED_OBJECT: 
/* 155 */       return jp.getEmbeddedObject();
/*     */     case VALUE_TRUE: 
/*     */     case VALUE_FALSE: 
/* 158 */       return finishBuild(ctxt, deserializeFromBoolean(jp, ctxt));
/*     */     
/*     */     case START_ARRAY: 
/* 161 */       return finishBuild(ctxt, deserializeFromArray(jp, ctxt));
/*     */     case FIELD_NAME: 
/*     */     case END_OBJECT: 
/* 164 */       return finishBuild(ctxt, deserializeFromObject(jp, ctxt));
/*     */     }
/* 166 */     throw ctxt.mappingException(handledType());
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
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object builder)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 183 */     return finishBuild(ctxt, _deserialize(jp, ctxt, builder));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _deserialize(JsonParser jp, DeserializationContext ctxt, Object builder)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 196 */     if (this._injectables != null) {
/* 197 */       injectValues(ctxt, builder);
/*     */     }
/* 199 */     if (this._unwrappedPropertyHandler != null) {
/* 200 */       return deserializeWithUnwrapped(jp, ctxt, builder);
/*     */     }
/* 202 */     if (this._externalTypeIdHandler != null) {
/* 203 */       return deserializeWithExternalTypeId(jp, ctxt, builder);
/*     */     }
/* 205 */     if (this._needViewProcesing) {
/* 206 */       Class<?> view = ctxt.getActiveView();
/* 207 */       if (view != null) {
/* 208 */         return deserializeWithView(jp, ctxt, builder, view);
/*     */       }
/*     */     }
/* 211 */     JsonToken t = jp.getCurrentToken();
/*     */     
/* 213 */     if (t == JsonToken.START_OBJECT) {}
/* 214 */     for (t = jp.nextToken(); 
/*     */         
/* 216 */         t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 217 */       String propName = jp.getCurrentName();
/*     */       
/* 219 */       jp.nextToken();
/* 220 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*     */       
/* 222 */       if (prop != null) {
/*     */         try {
/* 224 */           builder = prop.deserializeSetAndReturn(jp, ctxt, builder);
/*     */         } catch (Exception e) {
/* 226 */           wrapAndThrow(e, builder, propName, ctxt);
/*     */         }
/*     */         
/*     */       } else
/* 230 */         handleUnknownVanilla(jp, ctxt, handledType(), propName);
/*     */     }
/* 232 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Object vanillaDeserialize(JsonParser jp, DeserializationContext ctxt, JsonToken t)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 243 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/* 244 */     for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/* 245 */       String propName = jp.getCurrentName();
/*     */       
/* 247 */       jp.nextToken();
/* 248 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 249 */       if (prop != null) {
/*     */         try {
/* 251 */           bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
/*     */         } catch (Exception e) {
/* 253 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */       } else {
/* 256 */         handleUnknownVanilla(jp, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 259 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 270 */     if (this._nonStandardCreation) {
/* 271 */       if (this._unwrappedPropertyHandler != null) {
/* 272 */         return deserializeWithUnwrapped(jp, ctxt);
/*     */       }
/* 274 */       if (this._externalTypeIdHandler != null) {
/* 275 */         return deserializeWithExternalTypeId(jp, ctxt);
/*     */       }
/* 277 */       return deserializeFromObjectUsingNonDefault(jp, ctxt);
/*     */     }
/* 279 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/* 280 */     if (this._injectables != null) {
/* 281 */       injectValues(ctxt, bean);
/*     */     }
/* 283 */     if (this._needViewProcesing) {
/* 284 */       Class<?> view = ctxt.getActiveView();
/* 285 */       if (view != null) {
/* 286 */         return deserializeWithView(jp, ctxt, bean, view);
/*     */       }
/*     */     }
/* 289 */     for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/* 290 */       String propName = jp.getCurrentName();
/*     */       
/* 292 */       jp.nextToken();
/* 293 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 294 */       if (prop != null) {
/*     */         try {
/* 296 */           bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
/*     */         } catch (Exception e) {
/* 298 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */         
/*     */       } else
/* 302 */         handleUnknownVanilla(jp, ctxt, bean, propName);
/*     */     }
/* 304 */     return bean;
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
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 321 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 322 */     PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt, this._objectIdReader);
/*     */     
/*     */ 
/* 325 */     TokenBuffer unknown = null;
/*     */     
/* 327 */     for (JsonToken t = jp.getCurrentToken(); 
/* 328 */         t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 329 */       String propName = jp.getCurrentName();
/* 330 */       jp.nextToken();
/*     */       
/* 332 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 333 */       if (creatorProp != null)
/*     */       {
/* 335 */         Object value = creatorProp.deserialize(jp, ctxt);
/* 336 */         if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/* 337 */           jp.nextToken();
/*     */           Object bean;
/*     */           try {
/* 340 */             bean = creator.build(ctxt, buffer);
/*     */           } catch (Exception e) {
/* 342 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 343 */             continue;
/*     */           }
/*     */           
/* 346 */           if (bean.getClass() != this._beanType.getRawClass()) {
/* 347 */             return handlePolymorphic(jp, ctxt, bean, unknown);
/*     */           }
/* 349 */           if (unknown != null) {
/* 350 */             bean = handleUnknownProperties(ctxt, bean, unknown);
/*     */           }
/*     */           
/* 353 */           return _deserialize(jp, ctxt, bean);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 358 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 362 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 363 */         if (prop != null) {
/* 364 */           buffer.bufferProperty(prop, prop.deserialize(jp, ctxt));
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 369 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 370 */           handleIgnoredProperty(jp, ctxt, handledType(), propName);
/*     */ 
/*     */ 
/*     */         }
/* 374 */         else if (this._anySetter != null) {
/* 375 */           buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
/*     */         }
/*     */         else
/*     */         {
/* 379 */           if (unknown == null) {
/* 380 */             unknown = new TokenBuffer(jp);
/*     */           }
/* 382 */           unknown.writeFieldName(propName);
/* 383 */           unknown.copyCurrentStructure(jp);
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try {
/* 389 */       bean = creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 391 */       wrapInstantiationProblem(e, ctxt);
/* 392 */       return null;
/*     */     }
/* 394 */     if (unknown != null)
/*     */     {
/* 396 */       if (bean.getClass() != this._beanType.getRawClass()) {
/* 397 */         return handlePolymorphic(null, ctxt, bean, unknown);
/*     */       }
/*     */       
/* 400 */       return handleUnknownProperties(ctxt, bean, unknown);
/*     */     }
/* 402 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object deserializeWithView(JsonParser jp, DeserializationContext ctxt, Object bean, Class<?> activeView)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 415 */     for (JsonToken t = jp.getCurrentToken(); 
/* 416 */         t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 417 */       String propName = jp.getCurrentName();
/*     */       
/* 419 */       jp.nextToken();
/* 420 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 421 */       if (prop != null) {
/* 422 */         if (!prop.visibleInView(activeView)) {
/* 423 */           jp.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 427 */             bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
/*     */           } catch (Exception e) {
/* 429 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       } else
/* 433 */         handleUnknownVanilla(jp, ctxt, bean, propName);
/*     */     }
/* 435 */     return bean;
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
/*     */   protected Object deserializeWithUnwrapped(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 452 */     if (this._delegateDeserializer != null) {
/* 453 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*     */     }
/* 455 */     if (this._propertyBasedCreator != null) {
/* 456 */       return deserializeUsingPropertyBasedWithUnwrapped(jp, ctxt);
/*     */     }
/* 458 */     TokenBuffer tokens = new TokenBuffer(jp);
/* 459 */     tokens.writeStartObject();
/* 460 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 462 */     if (this._injectables != null) {
/* 463 */       injectValues(ctxt, bean);
/*     */     }
/*     */     
/* 466 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 468 */     for (; 
/* 468 */         jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/* 469 */       String propName = jp.getCurrentName();
/* 470 */       jp.nextToken();
/* 471 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 472 */       if (prop != null) {
/* 473 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 474 */           jp.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 478 */             bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
/*     */           } catch (Exception e) {
/* 480 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 485 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 486 */         handleIgnoredProperty(jp, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 490 */         tokens.writeFieldName(propName);
/* 491 */         tokens.copyCurrentStructure(jp);
/*     */         
/* 493 */         if (this._anySetter != null) {
/*     */           try {
/* 495 */             this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
/*     */           } catch (Exception e) {
/* 497 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 502 */     tokens.writeEndObject();
/* 503 */     this._unwrappedPropertyHandler.processUnwrapped(jp, ctxt, bean, tokens);
/* 504 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object deserializeWithUnwrapped(JsonParser jp, DeserializationContext ctxt, Object bean)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 512 */     JsonToken t = jp.getCurrentToken();
/* 513 */     if (t == JsonToken.START_OBJECT) {
/* 514 */       t = jp.nextToken();
/*     */     }
/* 516 */     TokenBuffer tokens = new TokenBuffer(jp);
/* 517 */     tokens.writeStartObject();
/* 518 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 519 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 520 */       String propName = jp.getCurrentName();
/* 521 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 522 */       jp.nextToken();
/* 523 */       if (prop != null) {
/* 524 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 525 */           jp.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 529 */             bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
/*     */           } catch (Exception e) {
/* 531 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       }
/* 535 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 536 */         handleIgnoredProperty(jp, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 540 */         tokens.writeFieldName(propName);
/* 541 */         tokens.copyCurrentStructure(jp);
/*     */         
/* 543 */         if (this._anySetter != null)
/* 544 */           this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 547 */     tokens.writeEndObject();
/* 548 */     this._unwrappedPropertyHandler.processUnwrapped(jp, ctxt, bean, tokens);
/* 549 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 557 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 558 */     PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt, this._objectIdReader);
/*     */     
/* 560 */     TokenBuffer tokens = new TokenBuffer(jp);
/* 561 */     tokens.writeStartObject();
/*     */     
/* 563 */     for (JsonToken t = jp.getCurrentToken(); 
/* 564 */         t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 565 */       String propName = jp.getCurrentName();
/* 566 */       jp.nextToken();
/*     */       
/* 568 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 569 */       if (creatorProp != null)
/*     */       {
/* 571 */         Object value = creatorProp.deserialize(jp, ctxt);
/* 572 */         if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/* 573 */           t = jp.nextToken();
/*     */           Object bean;
/*     */           try {
/* 576 */             bean = creator.build(ctxt, buffer);
/*     */           } catch (Exception e) {
/* 578 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 579 */             continue;
/*     */           }
/*     */           
/* 582 */           while (t == JsonToken.FIELD_NAME) {
/* 583 */             jp.nextToken();
/* 584 */             tokens.copyCurrentStructure(jp);
/* 585 */             t = jp.nextToken();
/*     */           }
/* 587 */           tokens.writeEndObject();
/* 588 */           if (bean.getClass() != this._beanType.getRawClass())
/*     */           {
/*     */ 
/* 591 */             throw ctxt.mappingException("Can not create polymorphic instances with unwrapped values");
/*     */           }
/* 593 */           return this._unwrappedPropertyHandler.processUnwrapped(jp, ctxt, bean, tokens);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 598 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 602 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 603 */         if (prop != null) {
/* 604 */           buffer.bufferProperty(prop, prop.deserialize(jp, ctxt));
/*     */ 
/*     */         }
/* 607 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 608 */           handleIgnoredProperty(jp, ctxt, handledType(), propName);
/*     */         }
/*     */         else {
/* 611 */           tokens.writeFieldName(propName);
/* 612 */           tokens.copyCurrentStructure(jp);
/*     */           
/* 614 */           if (this._anySetter != null) {
/* 615 */             buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try
/*     */     {
/* 623 */       bean = creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 625 */       wrapInstantiationProblem(e, ctxt);
/* 626 */       return null;
/*     */     }
/* 628 */     return this._unwrappedPropertyHandler.processUnwrapped(jp, ctxt, bean, tokens);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object deserializeWithExternalTypeId(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 641 */     if (this._propertyBasedCreator != null) {
/* 642 */       return deserializeUsingPropertyBasedWithExternalTypeId(jp, ctxt);
/*     */     }
/* 644 */     return deserializeWithExternalTypeId(jp, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeWithExternalTypeId(JsonParser jp, DeserializationContext ctxt, Object bean)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 651 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 652 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/* 653 */     for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/* 654 */       String propName = jp.getCurrentName();
/* 655 */       jp.nextToken();
/* 656 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 657 */       if (prop != null) {
/* 658 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 659 */           jp.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 663 */             bean = prop.deserializeSetAndReturn(jp, ctxt, bean);
/*     */           } catch (Exception e) {
/* 665 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 670 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 671 */         handleIgnoredProperty(jp, ctxt, bean, propName);
/*     */ 
/*     */ 
/*     */       }
/* 675 */       else if (!ext.handlePropertyValue(jp, ctxt, propName, bean))
/*     */       {
/*     */ 
/*     */ 
/* 679 */         if (this._anySetter != null) {
/*     */           try {
/* 681 */             this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
/*     */           } catch (Exception e) {
/* 683 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 688 */           handleUnknownProperty(jp, ctxt, bean, propName);
/*     */         }
/*     */       }
/*     */     }
/* 692 */     return ext.complete(jp, ctxt, bean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 700 */     throw new IllegalStateException("Deserialization with Builder, External type id, @JsonCreator not yet implemented");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\BuilderBasedDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */