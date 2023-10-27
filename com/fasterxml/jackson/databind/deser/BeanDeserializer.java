/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public class BeanDeserializer
/*     */   extends BeanDeserializerBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public BeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews)
/*     */   {
/*  56 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanDeserializer(BeanDeserializerBase src)
/*     */   {
/*  65 */     super(src, src._ignoreAllUnknown);
/*     */   }
/*     */   
/*     */   protected BeanDeserializer(BeanDeserializerBase src, boolean ignoreAllUnknown) {
/*  69 */     super(src, ignoreAllUnknown);
/*     */   }
/*     */   
/*     */   protected BeanDeserializer(BeanDeserializerBase src, NameTransformer unwrapper) {
/*  73 */     super(src, unwrapper);
/*     */   }
/*     */   
/*     */   public BeanDeserializer(BeanDeserializerBase src, ObjectIdReader oir) {
/*  77 */     super(src, oir);
/*     */   }
/*     */   
/*     */   public BeanDeserializer(BeanDeserializerBase src, HashSet<String> ignorableProps) {
/*  81 */     super(src, ignorableProps);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  90 */     if (getClass() != BeanDeserializer.class) {
/*  91 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  97 */     return new BeanDeserializer(this, unwrapper);
/*     */   }
/*     */   
/*     */   public BeanDeserializer withObjectIdReader(ObjectIdReader oir)
/*     */   {
/* 102 */     return new BeanDeserializer(this, oir);
/*     */   }
/*     */   
/*     */   public BeanDeserializer withIgnorableProperties(HashSet<String> ignorableProps)
/*     */   {
/* 107 */     return new BeanDeserializer(this, ignorableProps);
/*     */   }
/*     */   
/*     */   protected BeanDeserializerBase asArrayDeserializer()
/*     */   {
/* 112 */     SettableBeanProperty[] props = this._beanProperties.getPropertiesInInsertionOrder();
/* 113 */     return new BeanAsArrayDeserializer(this, props);
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
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 132 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 134 */     if (t == JsonToken.START_OBJECT) {
/* 135 */       if (this._vanillaProcessing) {
/* 136 */         return vanillaDeserialize(p, ctxt, p.nextToken());
/*     */       }
/* 138 */       p.nextToken();
/* 139 */       if (this._objectIdReader != null) {
/* 140 */         return deserializeWithObjectId(p, ctxt);
/*     */       }
/* 142 */       return deserializeFromObject(p, ctxt);
/*     */     }
/* 144 */     return _deserializeOther(p, ctxt, t);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final Object _deserializeOther(JsonParser p, DeserializationContext ctxt, JsonToken t)
/*     */     throws IOException
/*     */   {
/* 151 */     switch (t) {
/*     */     case VALUE_STRING: 
/* 153 */       return deserializeFromString(p, ctxt);
/*     */     case VALUE_NUMBER_INT: 
/* 155 */       return deserializeFromNumber(p, ctxt);
/*     */     case VALUE_NUMBER_FLOAT: 
/* 157 */       return deserializeFromDouble(p, ctxt);
/*     */     case VALUE_EMBEDDED_OBJECT: 
/* 159 */       return deserializeFromEmbedded(p, ctxt);
/*     */     case VALUE_TRUE: 
/*     */     case VALUE_FALSE: 
/* 162 */       return deserializeFromBoolean(p, ctxt);
/*     */     
/*     */     case START_ARRAY: 
/* 165 */       return deserializeFromArray(p, ctxt);
/*     */     case FIELD_NAME: 
/*     */     case END_OBJECT: 
/* 168 */       if (this._vanillaProcessing) {
/* 169 */         return vanillaDeserialize(p, ctxt, t);
/*     */       }
/* 171 */       if (this._objectIdReader != null) {
/* 172 */         return deserializeWithObjectId(p, ctxt);
/*     */       }
/* 174 */       return deserializeFromObject(p, ctxt);
/*     */     }
/* 176 */     throw ctxt.mappingException(handledType());
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object _missingToken(JsonParser p, DeserializationContext ctxt)
/*     */     throws JsonProcessingException
/*     */   {
/* 183 */     throw ctxt.endOfInputException(handledType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 196 */     p.setCurrentValue(bean);
/* 197 */     if (this._injectables != null) {
/* 198 */       injectValues(ctxt, bean);
/*     */     }
/* 200 */     if (this._unwrappedPropertyHandler != null) {
/* 201 */       return deserializeWithUnwrapped(p, ctxt, bean);
/*     */     }
/* 203 */     if (this._externalTypeIdHandler != null) {
/* 204 */       return deserializeWithExternalTypeId(p, ctxt, bean);
/*     */     }
/* 206 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 208 */     if (t == JsonToken.START_OBJECT) {
/* 209 */       t = p.nextToken();
/*     */     }
/* 211 */     if (this._needViewProcesing) {
/* 212 */       Class<?> view = ctxt.getActiveView();
/* 213 */       if (view != null) {
/* 214 */         return deserializeWithView(p, ctxt, bean, view);
/*     */       }
/*     */     }
/* 217 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 218 */       String propName = p.getCurrentName();
/* 219 */       p.nextToken();
/* 220 */       if (!this._beanProperties.findDeserializeAndSet(p, ctxt, bean, propName)) {
/* 221 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 224 */     return bean;
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
/*     */   private final Object vanillaDeserialize(JsonParser p, DeserializationContext ctxt, JsonToken t)
/*     */     throws IOException
/*     */   {
/* 241 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 243 */     p.setCurrentValue(bean);
/* 245 */     for (; 
/* 245 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 246 */       String propName = p.getCurrentName();
/* 247 */       p.nextToken();
/* 248 */       if (!this._beanProperties.findDeserializeAndSet(p, ctxt, bean, propName)) {
/* 249 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       }
/*     */     }
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
/* 265 */     return bean;
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
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 281 */     if ((this._objectIdReader != null) && (this._objectIdReader.maySerializeAsObject()))
/*     */     {
/* 283 */       if ((p.getCurrentTokenId() == 5) && (this._objectIdReader.isValidReferencePropertyName(p.getCurrentName(), p)))
/*     */       {
/* 285 */         return deserializeFromObjectId(p, ctxt);
/*     */       }
/*     */     }
/* 288 */     if (this._nonStandardCreation) {
/* 289 */       if (this._unwrappedPropertyHandler != null) {
/* 290 */         return deserializeWithUnwrapped(p, ctxt);
/*     */       }
/* 292 */       if (this._externalTypeIdHandler != null) {
/* 293 */         return deserializeWithExternalTypeId(p, ctxt);
/*     */       }
/* 295 */       Object bean = deserializeFromObjectUsingNonDefault(p, ctxt);
/* 296 */       if (this._injectables != null) {
/* 297 */         injectValues(ctxt, bean);
/*     */       }
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
/* 311 */       return bean;
/*     */     }
/* 313 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 315 */     p.setCurrentValue(bean);
/* 316 */     if (p.canReadObjectId()) {
/* 317 */       Object id = p.getObjectId();
/* 318 */       if (id != null) {
/* 319 */         _handleTypedObjectId(p, ctxt, bean, id);
/*     */       }
/*     */     }
/* 322 */     if (this._injectables != null) {
/* 323 */       injectValues(ctxt, bean);
/*     */     }
/* 325 */     if (this._needViewProcesing) {
/* 326 */       Class<?> view = ctxt.getActiveView();
/* 327 */       if (view != null) {
/* 328 */         return deserializeWithView(p, ctxt, bean, view);
/*     */       }
/*     */     }
/* 331 */     for (JsonToken t = p.getCurrentToken(); 
/* 332 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 333 */       String propName = p.getCurrentName();
/* 334 */       p.nextToken();
/* 335 */       if (!this._beanProperties.findDeserializeAndSet(p, ctxt, bean, propName)) {
/* 336 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       }
/*     */     }
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
/* 351 */     return bean;
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
/*     */   protected Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 367 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 368 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/*     */ 
/* 371 */     TokenBuffer unknown = null;
/*     */     
/* 373 */     for (JsonToken t = p.getCurrentToken(); 
/* 374 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 375 */       String propName = p.getCurrentName();
/* 376 */       p.nextToken();
/*     */       
/* 378 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 379 */       if (creatorProp != null)
/*     */       {
/* 381 */         Object value = creatorProp.deserialize(p, ctxt);
/* 382 */         if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/* 383 */           p.nextToken();
/*     */           Object bean;
/*     */           try {
/* 386 */             bean = creator.build(ctxt, buffer);
/*     */           } catch (Exception e) {
/* 388 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 389 */             bean = null;
/*     */           }
/* 391 */           if (bean == null) {
/* 392 */             throw ctxt.instantiationException(this._beanType.getRawClass(), "JSON Creator returned null");
/*     */           }
/*     */           
/* 395 */           p.setCurrentValue(bean);
/*     */           
/*     */ 
/* 398 */           if (bean.getClass() != this._beanType.getRawClass()) {
/* 399 */             return handlePolymorphic(p, ctxt, bean, unknown);
/*     */           }
/* 401 */           if (unknown != null) {
/* 402 */             bean = handleUnknownProperties(ctxt, bean, unknown);
/*     */           }
/*     */           
/* 405 */           return deserialize(p, ctxt, bean);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 410 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 414 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 415 */         if (prop != null) {
/* 416 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 421 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 422 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */ 
/*     */ 
/*     */         }
/* 426 */         else if (this._anySetter != null) {
/* 427 */           buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*     */         }
/*     */         else
/*     */         {
/* 431 */           if (unknown == null) {
/* 432 */             unknown = new TokenBuffer(p);
/*     */           }
/* 434 */           unknown.writeFieldName(propName);
/* 435 */           unknown.copyCurrentStructure(p);
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try {
/* 441 */       bean = creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 443 */       wrapInstantiationProblem(e, ctxt);
/* 444 */       bean = null;
/*     */     }
/* 446 */     if (unknown != null)
/*     */     {
/* 448 */       if (bean.getClass() != this._beanType.getRawClass()) {
/* 449 */         return handlePolymorphic(null, ctxt, bean, unknown);
/*     */       }
/*     */       
/* 452 */       return handleUnknownProperties(ctxt, bean, unknown);
/*     */     }
/* 454 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object deserializeWithView(JsonParser p, DeserializationContext ctxt, Object bean, Class<?> activeView)
/*     */     throws IOException
/*     */   {
/* 467 */     for (JsonToken t = p.getCurrentToken(); 
/* 468 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 469 */       String propName = p.getCurrentName();
/*     */       
/* 471 */       p.nextToken();
/* 472 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 473 */       if (prop != null) {
/* 474 */         if (!prop.visibleInView(activeView)) {
/* 475 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 479 */             prop.deserializeAndSet(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 481 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       } else
/* 485 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */     }
/* 487 */     return bean;
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
/*     */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 504 */     if (this._delegateDeserializer != null) {
/* 505 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/* 507 */     if (this._propertyBasedCreator != null) {
/* 508 */       return deserializeUsingPropertyBasedWithUnwrapped(p, ctxt);
/*     */     }
/* 510 */     TokenBuffer tokens = new TokenBuffer(p);
/* 511 */     tokens.writeStartObject();
/* 512 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/*     */ 
/* 515 */     p.setCurrentValue(bean);
/*     */     
/* 517 */     if (this._injectables != null) {
/* 518 */       injectValues(ctxt, bean);
/*     */     }
/* 520 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 521 */     for (JsonToken t = p.getCurrentToken(); 
/* 522 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 523 */       String propName = p.getCurrentName();
/* 524 */       p.nextToken();
/* 525 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 526 */       if (prop != null) {
/* 527 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 528 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 532 */             prop.deserializeAndSet(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 534 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 539 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 540 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 544 */         tokens.writeFieldName(propName);
/* 545 */         tokens.copyCurrentStructure(p);
/*     */         
/* 547 */         if (this._anySetter != null) {
/*     */           try {
/* 549 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*     */           } catch (Exception e) {
/* 551 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 556 */     tokens.writeEndObject();
/* 557 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/* 558 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 565 */     JsonToken t = p.getCurrentToken();
/* 566 */     if (t == JsonToken.START_OBJECT) {
/* 567 */       t = p.nextToken();
/*     */     }
/* 569 */     TokenBuffer tokens = new TokenBuffer(p);
/* 570 */     tokens.writeStartObject();
/* 571 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 572 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 573 */       String propName = p.getCurrentName();
/* 574 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 575 */       p.nextToken();
/* 576 */       if (prop != null) {
/* 577 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 578 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 582 */             prop.deserializeAndSet(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 584 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       }
/* 588 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 589 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 593 */         tokens.writeFieldName(propName);
/* 594 */         tokens.copyCurrentStructure(p);
/*     */         
/* 596 */         if (this._anySetter != null)
/* 597 */           this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 600 */     tokens.writeEndObject();
/* 601 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/* 602 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 609 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 610 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 612 */     TokenBuffer tokens = new TokenBuffer(p);
/* 613 */     tokens.writeStartObject();
/*     */     
/* 615 */     for (JsonToken t = p.getCurrentToken(); 
/* 616 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 617 */       String propName = p.getCurrentName();
/* 618 */       p.nextToken();
/*     */       
/* 620 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 621 */       if (creatorProp != null)
/*     */       {
/* 623 */         Object value = creatorProp.deserialize(p, ctxt);
/* 624 */         if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/* 625 */           t = p.nextToken();
/*     */           Object bean;
/*     */           try {
/* 628 */             bean = creator.build(ctxt, buffer);
/*     */           } catch (Exception e) {
/* 630 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 631 */             continue;
/*     */           }
/*     */           
/* 634 */           p.setCurrentValue(bean);
/*     */           
/* 636 */           while (t == JsonToken.FIELD_NAME) {
/* 637 */             p.nextToken();
/* 638 */             tokens.copyCurrentStructure(p);
/* 639 */             t = p.nextToken();
/*     */           }
/* 641 */           tokens.writeEndObject();
/* 642 */           if (bean.getClass() != this._beanType.getRawClass())
/*     */           {
/*     */ 
/* 645 */             tokens.close();
/* 646 */             throw ctxt.mappingException("Can not create polymorphic instances with unwrapped values");
/*     */           }
/* 648 */           return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 653 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 657 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 658 */         if (prop != null) {
/* 659 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 665 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 666 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */         }
/*     */         else {
/* 669 */           tokens.writeFieldName(propName);
/* 670 */           tokens.copyCurrentStructure(p);
/*     */           
/* 672 */           if (this._anySetter != null) {
/* 673 */             buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try {
/* 680 */       bean = creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 682 */       wrapInstantiationProblem(e, ctxt);
/* 683 */       return null;
/*     */     }
/* 685 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 698 */     if (this._propertyBasedCreator != null) {
/* 699 */       return deserializeUsingPropertyBasedWithExternalTypeId(p, ctxt);
/*     */     }
/* 701 */     return deserializeWithExternalTypeId(p, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 708 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 709 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/*     */     
/* 711 */     for (JsonToken t = p.getCurrentToken(); t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 712 */       String propName = p.getCurrentName();
/* 713 */       t = p.nextToken();
/* 714 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 715 */       if (prop != null)
/*     */       {
/* 717 */         if (t.isScalarValue()) {
/* 718 */           ext.handleTypePropertyValue(p, ctxt, propName, bean);
/*     */         }
/* 720 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 721 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 725 */             prop.deserializeAndSet(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 727 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 732 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 733 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */ 
/*     */ 
/*     */       }
/* 737 */       else if (!ext.handlePropertyValue(p, ctxt, propName, bean))
/*     */       {
/*     */ 
/*     */ 
/* 741 */         if (this._anySetter != null) {
/*     */           try {
/* 743 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*     */           } catch (Exception e) {
/* 745 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */         else
/* 750 */           handleUnknownProperty(p, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 753 */     return ext.complete(p, ctxt, bean);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 760 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/* 761 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 762 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 764 */     TokenBuffer tokens = new TokenBuffer(p);
/* 765 */     tokens.writeStartObject();
/*     */     
/* 767 */     for (JsonToken t = p.getCurrentToken(); 
/* 768 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 769 */       String propName = p.getCurrentName();
/* 770 */       p.nextToken();
/*     */       
/* 772 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 773 */       if (creatorProp != null)
/*     */       {
/* 775 */         if (!ext.handlePropertyValue(p, ctxt, propName, buffer))
/*     */         {
/*     */ 
/*     */ 
/* 779 */           Object value = creatorProp.deserialize(p, ctxt);
/* 780 */           if (buffer.assignParameter(creatorProp.getCreatorIndex(), value)) {
/* 781 */             t = p.nextToken();
/*     */             Object bean;
/*     */             try {
/* 784 */               bean = creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 786 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 787 */               continue;
/*     */             }
/*     */             
/* 790 */             while (t == JsonToken.FIELD_NAME) {
/* 791 */               p.nextToken();
/* 792 */               tokens.copyCurrentStructure(p);
/* 793 */               t = p.nextToken();
/*     */             }
/* 795 */             if (bean.getClass() != this._beanType.getRawClass())
/*     */             {
/*     */ 
/* 798 */               throw ctxt.mappingException("Can not create polymorphic instances with unwrapped values");
/*     */             }
/* 800 */             return ext.complete(p, ctxt, bean);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 806 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 810 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 811 */         if (prop != null) {
/* 812 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */ 
/*     */ 
/*     */         }
/* 816 */         else if (!ext.handlePropertyValue(p, ctxt, propName, null))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 822 */           if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 823 */             handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */ 
/*     */ 
/*     */           }
/* 827 */           else if (this._anySetter != null) {
/* 828 */             buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     try {
/* 834 */       return ext.complete(p, ctxt, buffer, creator);
/*     */     } catch (Exception e) {
/* 836 */       wrapInstantiationProblem(e, ctxt); }
/* 837 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */