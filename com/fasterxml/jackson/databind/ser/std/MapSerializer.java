/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapSerializer extends ContainerSerializer<Map<?, ?>> implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = -3465193297149553004L;
/*  36 */   protected static final JavaType UNSPECIFIED_TYPE = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanProperty _property;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HashSet<String> _ignoredEntries;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _valueTypeIsStatic;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _keyType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _valueType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _keySerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertySerializerMap _dynamicValueSerializers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _filterId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _sortKeys;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _suppressableValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer)
/*     */   {
/* 124 */     super(Map.class, false);
/* 125 */     this._ignoredEntries = ignoredEntries;
/* 126 */     this._keyType = keyType;
/* 127 */     this._valueType = valueType;
/* 128 */     this._valueTypeIsStatic = valueTypeIsStatic;
/* 129 */     this._valueTypeSerializer = vts;
/* 130 */     this._keySerializer = keySerializer;
/* 131 */     this._valueSerializer = valueSerializer;
/* 132 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/* 133 */     this._property = null;
/* 134 */     this._filterId = null;
/* 135 */     this._sortKeys = false;
/* 136 */     this._suppressableValue = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void _ensureOverride()
/*     */   {
/* 143 */     if (getClass() != MapSerializer.class) {
/* 144 */       throw new IllegalStateException("Missing override in class " + getClass().getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, HashSet<String> ignored)
/*     */   {
/* 153 */     super(Map.class, false);
/* 154 */     this._ignoredEntries = ignored;
/* 155 */     this._keyType = src._keyType;
/* 156 */     this._valueType = src._valueType;
/* 157 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/* 158 */     this._valueTypeSerializer = src._valueTypeSerializer;
/* 159 */     this._keySerializer = keySerializer;
/* 160 */     this._valueSerializer = valueSerializer;
/* 161 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/* 162 */     this._property = property;
/* 163 */     this._filterId = src._filterId;
/* 164 */     this._sortKeys = src._sortKeys;
/* 165 */     this._suppressableValue = src._suppressableValue;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected MapSerializer(MapSerializer src, TypeSerializer vts) {
/* 170 */     this(src, vts, src._suppressableValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue)
/*     */   {
/* 179 */     super(Map.class, false);
/* 180 */     this._ignoredEntries = src._ignoredEntries;
/* 181 */     this._keyType = src._keyType;
/* 182 */     this._valueType = src._valueType;
/* 183 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/* 184 */     this._valueTypeSerializer = vts;
/* 185 */     this._keySerializer = src._keySerializer;
/* 186 */     this._valueSerializer = src._valueSerializer;
/* 187 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/* 188 */     this._property = src._property;
/* 189 */     this._filterId = src._filterId;
/* 190 */     this._sortKeys = src._sortKeys;
/* 191 */     this._suppressableValue = suppressableValue;
/*     */   }
/*     */   
/*     */   protected MapSerializer(MapSerializer src, Object filterId, boolean sortKeys)
/*     */   {
/* 196 */     super(Map.class, false);
/* 197 */     this._ignoredEntries = src._ignoredEntries;
/* 198 */     this._keyType = src._keyType;
/* 199 */     this._valueType = src._valueType;
/* 200 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/* 201 */     this._valueTypeSerializer = src._valueTypeSerializer;
/* 202 */     this._keySerializer = src._keySerializer;
/* 203 */     this._valueSerializer = src._valueSerializer;
/* 204 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/* 205 */     this._property = src._property;
/* 206 */     this._filterId = filterId;
/* 207 */     this._sortKeys = sortKeys;
/* 208 */     this._suppressableValue = src._suppressableValue;
/*     */   }
/*     */   
/*     */   public MapSerializer _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/* 213 */     if (this._valueTypeSerializer == vts) {
/* 214 */       return this;
/*     */     }
/* 216 */     _ensureOverride();
/* 217 */     return new MapSerializer(this, vts, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, HashSet<String> ignored, boolean sortKeys)
/*     */   {
/* 227 */     _ensureOverride();
/* 228 */     MapSerializer ser = new MapSerializer(this, property, keySerializer, valueSerializer, ignored);
/* 229 */     if (sortKeys != ser._sortKeys) {
/* 230 */       ser = new MapSerializer(ser, this._filterId, sortKeys);
/*     */     }
/* 232 */     return ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MapSerializer withFilterId(Object filterId)
/*     */   {
/* 239 */     if (this._filterId == filterId) {
/* 240 */       return this;
/*     */     }
/* 242 */     _ensureOverride();
/* 243 */     return new MapSerializer(this, filterId, this._sortKeys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapSerializer withContentInclusion(Object suppressableValue)
/*     */   {
/* 253 */     if (suppressableValue == this._suppressableValue) {
/* 254 */       return this;
/*     */     }
/* 256 */     _ensureOverride();
/* 257 */     return new MapSerializer(this, this._valueTypeSerializer, suppressableValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer, Object filterId)
/*     */   {
/* 268 */     HashSet<String> ignoredEntries = (ignoredList == null) || (ignoredList.length == 0) ? null : ArrayBuilders.arrayToSet(ignoredList);
/*     */     
/*     */     JavaType keyType;
/*     */     JavaType keyType;
/*     */     JavaType valueType;
/* 273 */     if (mapType == null) { JavaType valueType;
/* 274 */       keyType = valueType = UNSPECIFIED_TYPE;
/*     */     } else {
/* 276 */       keyType = mapType.getKeyType();
/* 277 */       valueType = mapType.getContentType();
/*     */     }
/*     */     
/* 280 */     if (!staticValueType) {
/* 281 */       staticValueType = (valueType != null) && (valueType.isFinal());
/*     */ 
/*     */     }
/* 284 */     else if (valueType.getRawClass() == Object.class) {
/* 285 */       staticValueType = false;
/*     */     }
/*     */     
/* 288 */     MapSerializer ser = new MapSerializer(ignoredEntries, keyType, valueType, staticValueType, vts, keySerializer, valueSerializer);
/*     */     
/* 290 */     if (filterId != null) {
/* 291 */       ser = ser.withFilterId(filterId);
/*     */     }
/* 293 */     return ser;
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 311 */     JsonSerializer<?> ser = null;
/* 312 */     JsonSerializer<?> keySer = null;
/* 313 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 314 */     AnnotatedMember propertyAcc = property == null ? null : property.getMember();
/* 315 */     Object suppressableValue = this._suppressableValue;
/*     */     
/*     */ 
/* 318 */     if ((propertyAcc != null) && (intr != null)) {
/* 319 */       Object serDef = intr.findKeySerializer(propertyAcc);
/* 320 */       if (serDef != null) {
/* 321 */         keySer = provider.serializerInstance(propertyAcc, serDef);
/*     */       }
/* 323 */       serDef = intr.findContentSerializer(propertyAcc);
/* 324 */       if (serDef != null) {
/* 325 */         ser = provider.serializerInstance(propertyAcc, serDef);
/*     */       }
/* 327 */       JsonInclude.Include incl = intr.findSerializationInclusionForContent(propertyAcc, null);
/* 328 */       if (incl != null) {
/* 329 */         suppressableValue = incl;
/*     */       }
/*     */     }
/* 332 */     if (ser == null) {
/* 333 */       ser = this._valueSerializer;
/*     */     }
/*     */     
/* 336 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 337 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/* 341 */       if (((this._valueTypeIsStatic) && (this._valueType.getRawClass() != Object.class)) || (hasContentTypeAnnotation(provider, property)))
/*     */       {
/* 343 */         ser = provider.findValueSerializer(this._valueType, property);
/*     */       }
/*     */     } else {
/* 346 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/* 348 */     if (keySer == null) {
/* 349 */       keySer = this._keySerializer;
/*     */     }
/* 351 */     if (keySer == null) {
/* 352 */       keySer = provider.findKeySerializer(this._keyType, property);
/*     */     } else {
/* 354 */       keySer = provider.handleSecondaryContextualization(keySer, property);
/*     */     }
/* 356 */     HashSet<String> ignored = this._ignoredEntries;
/* 357 */     boolean sortKeys = false;
/* 358 */     if ((intr != null) && (propertyAcc != null)) {
/* 359 */       String[] moreToIgnore = intr.findPropertiesToIgnore(propertyAcc);
/* 360 */       if (moreToIgnore != null) {
/* 361 */         ignored = ignored == null ? new HashSet() : new HashSet(ignored);
/* 362 */         for (String str : moreToIgnore) {
/* 363 */           ignored.add(str);
/*     */         }
/*     */       }
/* 366 */       Boolean b = intr.findSerializationSortAlphabetically(propertyAcc);
/* 367 */       sortKeys = (b != null) && (b.booleanValue());
/*     */     }
/* 369 */     MapSerializer mser = withResolved(property, keySer, ser, ignored, sortKeys);
/* 370 */     if (suppressableValue != this._suppressableValue) {
/* 371 */       mser = mser.withContentInclusion(suppressableValue);
/*     */     }
/*     */     
/*     */ 
/* 375 */     if (property != null) {
/* 376 */       AnnotatedMember m = property.getMember();
/* 377 */       if (m != null) {
/* 378 */         Object filterId = intr.findFilterId(m);
/* 379 */         if (filterId != null) {
/* 380 */           mser = mser.withFilterId(filterId);
/*     */         }
/*     */       }
/*     */     }
/* 384 */     return mser;
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
/* 395 */     return this._valueType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 400 */     return this._valueSerializer;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Map<?, ?> value)
/*     */   {
/* 405 */     return (value == null) || (value.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Map<?, ?> value)
/*     */   {
/* 410 */     return value.size() == 1;
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
/*     */   public JsonSerializer<?> getKeySerializer()
/*     */   {
/* 430 */     return this._keySerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 443 */     gen.writeStartObject();
/*     */     
/* 445 */     gen.setCurrentValue(value);
/* 446 */     if (!value.isEmpty()) {
/* 447 */       Object suppressableValue = this._suppressableValue;
/* 448 */       if (suppressableValue == null) {
/* 449 */         if (!provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES)) {
/* 450 */           suppressableValue = JsonInclude.Include.NON_NULL;
/*     */         }
/* 452 */       } else if (suppressableValue == JsonInclude.Include.ALWAYS) {
/* 453 */         suppressableValue = null;
/*     */       }
/* 455 */       if ((this._sortKeys) || (provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS))) {
/* 456 */         value = _orderEntries(value);
/*     */       }
/* 458 */       if (this._filterId != null) {
/* 459 */         serializeFilteredFields(value, gen, provider, findPropertyFilter(provider, this._filterId, value), suppressableValue);
/*     */       }
/* 461 */       else if (suppressableValue != null) {
/* 462 */         serializeOptionalFields(value, gen, provider, suppressableValue);
/* 463 */       } else if (this._valueSerializer != null) {
/* 464 */         serializeFieldsUsing(value, gen, provider, this._valueSerializer);
/*     */       } else {
/* 466 */         serializeFields(value, gen, provider);
/*     */       }
/*     */     }
/* 469 */     gen.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 477 */     typeSer.writeTypePrefixForObject(value, gen);
/*     */     
/* 479 */     gen.setCurrentValue(value);
/* 480 */     if (!value.isEmpty()) {
/* 481 */       Object suppressableValue = this._suppressableValue;
/* 482 */       if (suppressableValue == null) {
/* 483 */         if (!provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES)) {
/* 484 */           suppressableValue = JsonInclude.Include.NON_NULL;
/*     */         }
/* 486 */       } else if (suppressableValue == JsonInclude.Include.ALWAYS) {
/* 487 */         suppressableValue = null;
/*     */       }
/* 489 */       if ((this._sortKeys) || (provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS))) {
/* 490 */         value = _orderEntries(value);
/*     */       }
/* 492 */       if (this._filterId != null) {
/* 493 */         serializeFilteredFields(value, gen, provider, findPropertyFilter(provider, this._filterId, value), suppressableValue);
/*     */       }
/* 495 */       else if (suppressableValue != null) {
/* 496 */         serializeOptionalFields(value, gen, provider, suppressableValue);
/* 497 */       } else if (this._valueSerializer != null) {
/* 498 */         serializeFieldsUsing(value, gen, provider, this._valueSerializer);
/*     */       } else {
/* 500 */         serializeFields(value, gen, provider);
/*     */       }
/*     */     }
/* 503 */     typeSer.writeTypeSuffixForObject(value, gen);
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
/*     */   public void serializeFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 520 */     if (this._valueTypeSerializer != null) {
/* 521 */       serializeTypedFields(value, gen, provider, null);
/* 522 */       return;
/*     */     }
/* 524 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/* 525 */     HashSet<String> ignored = this._ignoredEntries;
/*     */     
/* 527 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*     */     
/* 529 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/* 530 */       Object valueElem = entry.getValue();
/*     */       
/* 532 */       Object keyElem = entry.getKey();
/* 533 */       if (keyElem == null) {
/* 534 */         provider.findNullKeySerializer(this._keyType, this._property).serialize(null, gen, provider);
/*     */       }
/*     */       else {
/* 537 */         if ((ignored != null) && (ignored.contains(keyElem))) continue;
/* 538 */         keySerializer.serialize(keyElem, gen, provider);
/*     */       }
/*     */       
/*     */ 
/* 542 */       if (valueElem == null) {
/* 543 */         provider.defaultSerializeNull(gen);
/*     */       } else {
/* 545 */         Class<?> cc = valueElem.getClass();
/* 546 */         JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 547 */         if (serializer == null) {
/* 548 */           if (this._valueType.hasGenericTypes()) {
/* 549 */             serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*     */           }
/*     */           else {
/* 552 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 554 */           serializers = this._dynamicValueSerializers;
/*     */         }
/*     */         try {
/* 557 */           serializer.serialize(valueElem, gen, provider);
/*     */         }
/*     */         catch (Exception e) {
/* 560 */           String keyDesc = "" + keyElem;
/* 561 */           wrapAndThrow(provider, e, value, keyDesc);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeOptionalFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
/*     */     throws IOException
/*     */   {
/* 572 */     if (this._valueTypeSerializer != null) {
/* 573 */       serializeTypedFields(value, gen, provider, suppressableValue);
/* 574 */       return;
/*     */     }
/* 576 */     HashSet<String> ignored = this._ignoredEntries;
/* 577 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*     */     
/* 579 */     for (Map.Entry<?, ?> entry : value.entrySet())
/*     */     {
/* 581 */       Object keyElem = entry.getKey();
/*     */       JsonSerializer<Object> keySerializer;
/* 583 */       JsonSerializer<Object> keySerializer; if (keyElem == null) {
/* 584 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*     */       } else {
/* 586 */         if ((ignored != null) && (ignored.contains(keyElem))) continue;
/* 587 */         keySerializer = this._keySerializer;
/*     */       }
/*     */       
/*     */ 
/* 591 */       Object valueElem = entry.getValue();
/*     */       JsonSerializer<Object> valueSer;
/* 593 */       if (valueElem == null) {
/* 594 */         if (suppressableValue != null) {
/*     */           continue;
/*     */         }
/* 597 */         JsonSerializer<Object> valueSer = provider.getDefaultNullValueSerializer();
/*     */       } else {
/* 599 */         valueSer = this._valueSerializer;
/* 600 */         if (valueSer == null) {
/* 601 */           Class<?> cc = valueElem.getClass();
/* 602 */           valueSer = serializers.serializerFor(cc);
/* 603 */           if (valueSer == null) {
/* 604 */             if (this._valueType.hasGenericTypes()) {
/* 605 */               valueSer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*     */             }
/*     */             else {
/* 608 */               valueSer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 610 */             serializers = this._dynamicValueSerializers;
/*     */           }
/*     */         }
/*     */         
/* 614 */         if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, valueElem))) {
/*     */           continue;
/*     */         }
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 621 */         keySerializer.serialize(keyElem, gen, provider);
/* 622 */         valueSer.serialize(valueElem, gen, provider);
/*     */       } catch (Exception e) {
/* 624 */         String keyDesc = "" + keyElem;
/* 625 */         wrapAndThrow(provider, e, value, keyDesc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void serializeFieldsUsing(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 639 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/* 640 */     HashSet<String> ignored = this._ignoredEntries;
/* 641 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     
/* 643 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/* 644 */       Object keyElem = entry.getKey();
/* 645 */       if ((ignored == null) || (!ignored.contains(keyElem)))
/*     */       {
/* 647 */         if (keyElem == null) {
/* 648 */           provider.findNullKeySerializer(this._keyType, this._property).serialize(null, gen, provider);
/*     */         } else {
/* 650 */           keySerializer.serialize(keyElem, gen, provider);
/*     */         }
/* 652 */         Object valueElem = entry.getValue();
/* 653 */         if (valueElem == null) {
/* 654 */           provider.defaultSerializeNull(gen);
/*     */         } else {
/*     */           try {
/* 657 */             if (typeSer == null) {
/* 658 */               ser.serialize(valueElem, gen, provider);
/*     */             } else {
/* 660 */               ser.serializeWithType(valueElem, gen, provider, typeSer);
/*     */             }
/*     */           } catch (Exception e) {
/* 663 */             String keyDesc = "" + keyElem;
/* 664 */             wrapAndThrow(provider, e, value, keyDesc);
/*     */           }
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
/*     */ 
/*     */ 
/*     */   public void serializeFilteredFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter, Object suppressableValue)
/*     */     throws IOException
/*     */   {
/* 681 */     HashSet<String> ignored = this._ignoredEntries;
/*     */     
/* 683 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/* 684 */     MapProperty prop = new MapProperty(this._valueTypeSerializer, this._property);
/*     */     
/* 686 */     for (Map.Entry<?, ?> entry : value.entrySet())
/*     */     {
/* 688 */       Object keyElem = entry.getKey();
/* 689 */       if ((ignored == null) || (!ignored.contains(keyElem))) {
/*     */         JsonSerializer<Object> keySerializer;
/*     */         JsonSerializer<Object> keySerializer;
/* 692 */         if (keyElem == null) {
/* 693 */           keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*     */         } else {
/* 695 */           keySerializer = this._keySerializer;
/*     */         }
/*     */         
/* 698 */         Object valueElem = entry.getValue();
/*     */         
/*     */         JsonSerializer<Object> valueSer;
/*     */         
/* 702 */         if (valueElem == null) {
/* 703 */           if (suppressableValue != null) {
/*     */             continue;
/*     */           }
/* 706 */           JsonSerializer<Object> valueSer = provider.getDefaultNullValueSerializer();
/*     */         } else {
/* 708 */           valueSer = this._valueSerializer;
/* 709 */           if (valueSer == null) {
/* 710 */             Class<?> cc = valueElem.getClass();
/* 711 */             valueSer = serializers.serializerFor(cc);
/* 712 */             if (valueSer == null) {
/* 713 */               if (this._valueType.hasGenericTypes()) {
/* 714 */                 valueSer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*     */               }
/*     */               else {
/* 717 */                 valueSer = _findAndAddDynamic(serializers, cc, provider);
/*     */               }
/* 719 */               serializers = this._dynamicValueSerializers;
/*     */             }
/*     */           }
/*     */           
/* 723 */           if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, valueElem))) {
/*     */             continue;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 729 */         prop.reset(keyElem, keySerializer, valueSer);
/*     */         try {
/* 731 */           filter.serializeAsField(valueElem, gen, provider, prop);
/*     */         } catch (Exception e) {
/* 733 */           String keyDesc = "" + keyElem;
/* 734 */           wrapAndThrow(provider, e, value, keyDesc);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void serializeFilteredFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter) throws IOException {
/* 742 */     serializeFilteredFields(value, gen, provider, filter, provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES) ? null : JsonInclude.Include.NON_NULL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void serializeTypedFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
/*     */     throws IOException
/*     */   {
/* 753 */     HashSet<String> ignored = this._ignoredEntries;
/* 754 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*     */     
/* 756 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/* 757 */       Object keyElem = entry.getKey();
/*     */       JsonSerializer<Object> keySerializer;
/* 759 */       JsonSerializer<Object> keySerializer; if (keyElem == null) {
/* 760 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*     */       }
/*     */       else {
/* 763 */         if ((ignored != null) && (ignored.contains(keyElem))) continue;
/* 764 */         keySerializer = this._keySerializer;
/*     */       }
/* 766 */       Object valueElem = entry.getValue();
/*     */       
/*     */       JsonSerializer<Object> valueSer;
/*     */       
/* 770 */       if (valueElem == null) {
/* 771 */         if (suppressableValue != null) {
/*     */           continue;
/*     */         }
/* 774 */         JsonSerializer<Object> valueSer = provider.getDefaultNullValueSerializer();
/*     */       } else {
/* 776 */         valueSer = this._valueSerializer;
/* 777 */         Class<?> cc = valueElem.getClass();
/* 778 */         valueSer = serializers.serializerFor(cc);
/* 779 */         if (valueSer == null) {
/* 780 */           if (this._valueType.hasGenericTypes()) {
/* 781 */             valueSer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*     */           }
/*     */           else {
/* 784 */             valueSer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 786 */           serializers = this._dynamicValueSerializers;
/*     */         }
/*     */         
/* 789 */         if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, valueElem))) {
/*     */           continue;
/*     */         }
/*     */       }
/*     */       
/* 794 */       keySerializer.serialize(keyElem, gen, provider);
/*     */       try {
/* 796 */         valueSer.serializeWithType(valueElem, gen, provider, this._valueTypeSerializer);
/*     */       } catch (Exception e) {
/* 798 */         String keyDesc = "" + keyElem;
/* 799 */         wrapAndThrow(provider, e, value, keyDesc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void serializeTypedFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException
/*     */   {
/* 807 */     serializeTypedFields(value, gen, provider, provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES) ? null : JsonInclude.Include.NON_NULL);
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
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 820 */     com.fasterxml.jackson.databind.node.ObjectNode o = createSchemaNode("object", true);
/*     */     
/*     */ 
/* 823 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 830 */     JsonMapFormatVisitor v2 = visitor == null ? null : visitor.expectMapFormat(typeHint);
/* 831 */     if (v2 != null) {
/* 832 */       v2.keyFormat(this._keySerializer, this._keyType);
/* 833 */       JsonSerializer<?> valueSer = this._valueSerializer;
/* 834 */       if (valueSer == null) {
/* 835 */         valueSer = _findAndAddDynamic(this._dynamicValueSerializers, this._valueType, visitor.getProvider());
/*     */       }
/*     */       
/* 838 */       v2.valueFormat(valueSer, this._valueType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 851 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 853 */     if (map != result.map) {
/* 854 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 856 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 862 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 863 */     if (map != result.map) {
/* 864 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 866 */     return result.serializer;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Map<?, ?> _orderEntries(Map<?, ?> input)
/*     */   {
/* 872 */     if ((input instanceof java.util.SortedMap)) {
/* 873 */       return input;
/*     */     }
/* 875 */     return new TreeMap(input);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\MapSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */