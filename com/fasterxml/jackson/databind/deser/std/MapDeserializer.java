/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ @JacksonStdImpl
/*     */ public class MapDeserializer
/*     */   extends ContainerDeserializerBase<Map<Object, Object>>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _mapType;
/*     */   protected final KeyDeserializer _keyDeserializer;
/*     */   protected boolean _standardStringKey;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final boolean _hasDefaultCreator;
/*     */   protected JsonDeserializer<Object> _delegateDeserializer;
/*     */   protected PropertyBasedCreator _propertyBasedCreator;
/*     */   protected HashSet<String> _ignorableProperties;
/*     */   
/*     */   public MapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  98 */     super(mapType);
/*  99 */     this._mapType = mapType;
/* 100 */     this._keyDeserializer = keyDeser;
/* 101 */     this._valueDeserializer = valueDeser;
/* 102 */     this._valueTypeDeserializer = valueTypeDeser;
/* 103 */     this._valueInstantiator = valueInstantiator;
/* 104 */     this._hasDefaultCreator = valueInstantiator.canCreateUsingDefault();
/* 105 */     this._delegateDeserializer = null;
/* 106 */     this._propertyBasedCreator = null;
/* 107 */     this._standardStringKey = _isStdKeyDeser(mapType, keyDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapDeserializer(MapDeserializer src)
/*     */   {
/* 116 */     super(src._mapType);
/* 117 */     this._mapType = src._mapType;
/* 118 */     this._keyDeserializer = src._keyDeserializer;
/* 119 */     this._valueDeserializer = src._valueDeserializer;
/* 120 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 121 */     this._valueInstantiator = src._valueInstantiator;
/* 122 */     this._propertyBasedCreator = src._propertyBasedCreator;
/* 123 */     this._delegateDeserializer = src._delegateDeserializer;
/* 124 */     this._hasDefaultCreator = src._hasDefaultCreator;
/*     */     
/* 126 */     this._ignorableProperties = src._ignorableProperties;
/*     */     
/* 128 */     this._standardStringKey = src._standardStringKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapDeserializer(MapDeserializer src, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, HashSet<String> ignorable)
/*     */   {
/* 136 */     super(src._mapType);
/* 137 */     this._mapType = src._mapType;
/* 138 */     this._keyDeserializer = keyDeser;
/* 139 */     this._valueDeserializer = valueDeser;
/* 140 */     this._valueTypeDeserializer = valueTypeDeser;
/* 141 */     this._valueInstantiator = src._valueInstantiator;
/* 142 */     this._propertyBasedCreator = src._propertyBasedCreator;
/* 143 */     this._delegateDeserializer = src._delegateDeserializer;
/* 144 */     this._hasDefaultCreator = src._hasDefaultCreator;
/* 145 */     this._ignorableProperties = ignorable;
/*     */     
/* 147 */     this._standardStringKey = _isStdKeyDeser(this._mapType, keyDeser);
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
/*     */   protected MapDeserializer withResolved(KeyDeserializer keyDeser, TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser, HashSet<String> ignorable)
/*     */   {
/* 160 */     if ((this._keyDeserializer == keyDeser) && (this._valueDeserializer == valueDeser) && (this._valueTypeDeserializer == valueTypeDeser) && (this._ignorableProperties == ignorable))
/*     */     {
/* 162 */       return this;
/*     */     }
/* 164 */     return new MapDeserializer(this, keyDeser, valueDeser, valueTypeDeser, ignorable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _isStdKeyDeser(JavaType mapType, KeyDeserializer keyDeser)
/*     */   {
/* 174 */     if (keyDeser == null) {
/* 175 */       return true;
/*     */     }
/* 177 */     JavaType keyType = mapType.getKeyType();
/* 178 */     if (keyType == null) {
/* 179 */       return true;
/*     */     }
/* 181 */     Class<?> rawKeyType = keyType.getRawClass();
/* 182 */     return ((rawKeyType == String.class) || (rawKeyType == Object.class)) && (isDefaultKeyDeserializer(keyDeser));
/*     */   }
/*     */   
/*     */   public void setIgnorableProperties(String[] ignorable)
/*     */   {
/* 187 */     this._ignorableProperties = ((ignorable == null) || (ignorable.length == 0) ? null : ArrayBuilders.arrayToSet(ignorable));
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
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/* 201 */     if (this._valueInstantiator.canCreateUsingDelegate()) {
/* 202 */       JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 203 */       if (delegateType == null) {
/* 204 */         throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._mapType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */       this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/*     */     }
/* 214 */     if (this._valueInstantiator.canCreateFromObjectWith()) {
/* 215 */       SettableBeanProperty[] creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/* 216 */       this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps);
/*     */     }
/* 218 */     this._standardStringKey = _isStdKeyDeser(this._mapType, this._keyDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 229 */     KeyDeserializer kd = this._keyDeserializer;
/* 230 */     if (kd == null) {
/* 231 */       kd = ctxt.findKeyDeserializer(this._mapType.getKeyType(), property);
/*     */     }
/* 233 */     else if ((kd instanceof ContextualKeyDeserializer)) {
/* 234 */       kd = ((ContextualKeyDeserializer)kd).createContextual(ctxt, property);
/*     */     }
/*     */     
/* 237 */     JsonDeserializer<?> vd = this._valueDeserializer;
/*     */     
/* 239 */     if (property != null) {
/* 240 */       vd = findConvertingContentDeserializer(ctxt, property, vd);
/*     */     }
/* 242 */     JavaType vt = this._mapType.getContentType();
/* 243 */     if (vd == null) {
/* 244 */       vd = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 246 */       vd = ctxt.handleSecondaryContextualization(vd, property, vt);
/*     */     }
/* 248 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/* 249 */     if (vtd != null) {
/* 250 */       vtd = vtd.forProperty(property);
/*     */     }
/* 252 */     HashSet<String> ignored = this._ignorableProperties;
/* 253 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 254 */     if ((intr != null) && (property != null)) {
/* 255 */       AnnotatedMember member = property.getMember();
/* 256 */       if (member != null) {
/* 257 */         String[] moreToIgnore = intr.findPropertiesToIgnore(member);
/* 258 */         if (moreToIgnore != null) {
/* 259 */           ignored = ignored == null ? new HashSet() : new HashSet(ignored);
/* 260 */           for (String str : moreToIgnore) {
/* 261 */             ignored.add(str);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 266 */     return withResolved(kd, vtd, vd, ignored);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 277 */     return this._mapType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 282 */     return this._valueDeserializer;
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
/*     */   public boolean isCachable()
/*     */   {
/* 309 */     return (this._valueDeserializer == null) && (this._keyDeserializer == null) && (this._valueTypeDeserializer == null) && (this._ignorableProperties == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 320 */     if (this._propertyBasedCreator != null) {
/* 321 */       return _deserializeUsingCreator(jp, ctxt);
/*     */     }
/* 323 */     if (this._delegateDeserializer != null) {
/* 324 */       return (Map)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*     */     }
/*     */     
/* 327 */     if (!this._hasDefaultCreator) {
/* 328 */       throw ctxt.instantiationException(getMapClass(), "No default constructor found");
/*     */     }
/*     */     
/* 331 */     JsonToken t = jp.getCurrentToken();
/* 332 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME) && (t != JsonToken.END_OBJECT))
/*     */     {
/* 334 */       if (t == JsonToken.VALUE_STRING) {
/* 335 */         return (Map)this._valueInstantiator.createFromString(ctxt, jp.getText());
/*     */       }
/*     */       
/* 338 */       return (Map)_deserializeFromEmpty(jp, ctxt);
/*     */     }
/* 340 */     Map<Object, Object> result = (Map)this._valueInstantiator.createUsingDefault(ctxt);
/* 341 */     if (this._standardStringKey) {
/* 342 */       _readAndBindStringMap(jp, ctxt, result);
/* 343 */       return result;
/*     */     }
/* 345 */     _readAndBind(jp, ctxt, result);
/* 346 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException
/*     */   {
/* 355 */     p.setCurrentValue(result);
/*     */     
/*     */ 
/* 358 */     JsonToken t = p.getCurrentToken();
/* 359 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME)) {
/* 360 */       throw ctxt.mappingException(getMapClass());
/*     */     }
/* 362 */     if (this._standardStringKey) {
/* 363 */       _readAndBindStringMap(p, ctxt, result);
/* 364 */       return result;
/*     */     }
/* 366 */     _readAndBind(p, ctxt, result);
/* 367 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 376 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 386 */   public final Class<?> getMapClass() { return this._mapType.getRawClass(); }
/*     */   
/* 388 */   public JavaType getValueType() { return this._mapType; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _readAndBind(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 400 */     JsonToken t = jp.getCurrentToken();
/* 401 */     if (t == JsonToken.START_OBJECT) {
/* 402 */       t = jp.nextToken();
/*     */     }
/* 404 */     KeyDeserializer keyDes = this._keyDeserializer;
/* 405 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 406 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 408 */     MapReferringAccumulator referringAccumulator = null;
/* 409 */     boolean useObjectId = valueDes.getObjectIdReader() != null;
/* 410 */     if (useObjectId) {
/* 411 */       referringAccumulator = new MapReferringAccumulator(this._mapType.getContentType().getRawClass(), result);
/*     */     }
/* 413 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken())
/*     */     {
/* 415 */       String fieldName = jp.getCurrentName();
/* 416 */       Object key = keyDes.deserializeKey(fieldName, ctxt);
/*     */       
/* 418 */       t = jp.nextToken();
/* 419 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(fieldName))) {
/* 420 */         jp.skipChildren();
/*     */       } else {
/*     */         try
/*     */         {
/*     */           Object value;
/*     */           Object value;
/* 426 */           if (t == JsonToken.VALUE_NULL) {
/* 427 */             value = valueDes.getNullValue(); } else { Object value;
/* 428 */             if (typeDeser == null) {
/* 429 */               value = valueDes.deserialize(jp, ctxt);
/*     */             } else {
/* 431 */               value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 437 */           if (useObjectId) {
/* 438 */             referringAccumulator.put(key, value);
/*     */           } else {
/* 440 */             result.put(key, value);
/*     */           }
/*     */         } catch (UnresolvedForwardReference reference) {
/* 443 */           handleUnresolvedReference(jp, referringAccumulator, key, reference);
/*     */         } catch (Exception e) {
/* 445 */           wrapAndThrow(e, result, fieldName);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _readAndBindStringMap(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 459 */     JsonToken t = jp.getCurrentToken();
/* 460 */     if (t == JsonToken.START_OBJECT) {
/* 461 */       t = jp.nextToken();
/*     */     }
/* 463 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 464 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 465 */     MapReferringAccumulator referringAccumulator = null;
/* 466 */     boolean useObjectId = valueDes.getObjectIdReader() != null;
/* 467 */     if (useObjectId) {
/* 468 */       referringAccumulator = new MapReferringAccumulator(this._mapType.getContentType().getRawClass(), result);
/*     */     }
/* 470 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken())
/*     */     {
/* 472 */       String fieldName = jp.getCurrentName();
/*     */       
/* 474 */       t = jp.nextToken();
/* 475 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(fieldName))) {
/* 476 */         jp.skipChildren();
/*     */       } else {
/*     */         try
/*     */         {
/*     */           Object value;
/*     */           Object value;
/* 482 */           if (t == JsonToken.VALUE_NULL) {
/* 483 */             value = valueDes.getNullValue(); } else { Object value;
/* 484 */             if (typeDeser == null) {
/* 485 */               value = valueDes.deserialize(jp, ctxt);
/*     */             } else
/* 487 */               value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */           }
/* 489 */           if (useObjectId) {
/* 490 */             referringAccumulator.put(fieldName, value);
/*     */           } else {
/* 492 */             result.put(fieldName, value);
/*     */           }
/*     */         } catch (UnresolvedForwardReference reference) {
/* 495 */           handleUnresolvedReference(jp, referringAccumulator, fieldName, reference);
/*     */         } catch (Exception e) {
/* 497 */           wrapAndThrow(e, result, fieldName);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<Object, Object> _deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 506 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*     */     
/* 508 */     PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt, null);
/*     */     
/* 510 */     JsonToken t = jp.getCurrentToken();
/* 511 */     if (t == JsonToken.START_OBJECT) {
/* 512 */       t = jp.nextToken();
/*     */     }
/* 514 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 515 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 516 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 517 */       String propName = jp.getCurrentName();
/* 518 */       t = jp.nextToken();
/* 519 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(propName))) {
/* 520 */         jp.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 524 */         SettableBeanProperty prop = creator.findCreatorProperty(propName);
/* 525 */         if (prop != null)
/*     */         {
/* 527 */           Object value = prop.deserialize(jp, ctxt);
/* 528 */           if (buffer.assignParameter(prop.getCreatorIndex(), value)) {
/* 529 */             jp.nextToken();
/*     */             Map<Object, Object> result;
/*     */             try {
/* 532 */               result = (Map)creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 534 */               wrapAndThrow(e, this._mapType.getRawClass(), propName);
/* 535 */               return null;
/*     */             }
/* 537 */             _readAndBind(jp, ctxt, result);
/* 538 */             return result;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 543 */           String fieldName = jp.getCurrentName();
/* 544 */           Object key = this._keyDeserializer.deserializeKey(fieldName, ctxt);
/*     */           Object value;
/*     */           try {
/*     */             Object value;
/* 548 */             if (t == JsonToken.VALUE_NULL) {
/* 549 */               value = valueDes.getNullValue(); } else { Object value;
/* 550 */               if (typeDeser == null) {
/* 551 */                 value = valueDes.deserialize(jp, ctxt);
/*     */               } else
/* 553 */                 value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */             }
/*     */           } catch (Exception e) {
/* 556 */             wrapAndThrow(e, this._mapType.getRawClass(), propName);
/* 557 */             return null;
/*     */           }
/* 559 */           buffer.bufferMapProperty(key, value);
/*     */         }
/*     */       }
/*     */     }
/*     */     try {
/* 564 */       return (Map)creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 566 */       wrapAndThrow(e, this._mapType.getRawClass(), null); }
/* 567 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void wrapAndThrow(Throwable t, Object ref) throws IOException
/*     */   {
/* 573 */     wrapAndThrow(t, ref, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private void handleUnresolvedReference(JsonParser jp, MapReferringAccumulator accumulator, Object key, UnresolvedForwardReference reference)
/*     */     throws JsonMappingException
/*     */   {
/* 580 */     if (accumulator == null) {
/* 581 */       throw JsonMappingException.from(jp, "Unresolved forward reference but no identity info.", reference);
/*     */     }
/* 583 */     ReadableObjectId.Referring referring = accumulator.handleUnresolvedReference(reference, key);
/* 584 */     reference.getRoid().appendReferring(referring);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class MapReferringAccumulator
/*     */   {
/*     */     private final Class<?> _valueType;
/*     */     
/*     */     private Map<Object, Object> _result;
/* 593 */     private List<MapDeserializer.MapReferring> _accumulator = new ArrayList();
/*     */     
/*     */     public MapReferringAccumulator(Class<?> valueType, Map<Object, Object> result) {
/* 596 */       this._valueType = valueType;
/* 597 */       this._result = result;
/*     */     }
/*     */     
/*     */     public void put(Object key, Object value)
/*     */     {
/* 602 */       if (this._accumulator.isEmpty()) {
/* 603 */         this._result.put(key, value);
/*     */       } else {
/* 605 */         MapDeserializer.MapReferring ref = (MapDeserializer.MapReferring)this._accumulator.get(this._accumulator.size() - 1);
/* 606 */         ref.next.put(key, value);
/*     */       }
/*     */     }
/*     */     
/*     */     public ReadableObjectId.Referring handleUnresolvedReference(UnresolvedForwardReference reference, Object key)
/*     */     {
/* 612 */       MapDeserializer.MapReferring id = new MapDeserializer.MapReferring(this, reference, this._valueType, key);
/* 613 */       this._accumulator.add(id);
/* 614 */       return id;
/*     */     }
/*     */     
/*     */     public void resolveForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 619 */       Iterator<MapDeserializer.MapReferring> iterator = this._accumulator.iterator();
/*     */       
/*     */ 
/*     */ 
/* 623 */       Map<Object, Object> previous = this._result;
/* 624 */       while (iterator.hasNext()) {
/* 625 */         MapDeserializer.MapReferring ref = (MapDeserializer.MapReferring)iterator.next();
/* 626 */         if (ref.hasId(id)) {
/* 627 */           iterator.remove();
/* 628 */           previous.put(ref.key, value);
/* 629 */           previous.putAll(ref.next);
/* 630 */           return;
/*     */         }
/* 632 */         previous = ref.next;
/*     */       }
/*     */       
/* 635 */       throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class MapReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final MapDeserializer.MapReferringAccumulator _parent;
/*     */     
/*     */ 
/* 648 */     public final Map<Object, Object> next = new LinkedHashMap();
/*     */     
/*     */     public final Object key;
/*     */     
/*     */     MapReferring(MapDeserializer.MapReferringAccumulator parent, UnresolvedForwardReference ref, Class<?> valueType, Object key)
/*     */     {
/* 654 */       super(valueType);
/* 655 */       this._parent = parent;
/* 656 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 661 */       this._parent.resolveForwardReference(id, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\MapDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */