/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException.Reference;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.AnyGetterWriter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public abstract class BeanSerializerBase extends StdSerializer<Object> implements com.fasterxml.jackson.databind.ser.ContextualSerializer, ResolvableSerializer, com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable, com.fasterxml.jackson.databind.jsonschema.SchemaAware
/*     */ {
/*  42 */   protected static final PropertyName NAME_FOR_OBJECT_REF = new PropertyName("#object-ref");
/*     */   
/*  44 */   protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanPropertyWriter[] _props;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanPropertyWriter[] _filteredProps;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnyGetterWriter _anyGetterWriter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _propertyFilterId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotatedMember _typeId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ObjectIdWriter _objectIdWriter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonFormat.Shape _serializationShape;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/* 108 */     super(type);
/* 109 */     this._props = properties;
/* 110 */     this._filteredProps = filteredProperties;
/* 111 */     if (builder == null) {
/* 112 */       this._typeId = null;
/* 113 */       this._anyGetterWriter = null;
/* 114 */       this._propertyFilterId = null;
/* 115 */       this._objectIdWriter = null;
/* 116 */       this._serializationShape = null;
/*     */     } else {
/* 118 */       this._typeId = builder.getTypeId();
/* 119 */       this._anyGetterWriter = builder.getAnyGetter();
/* 120 */       this._propertyFilterId = builder.getFilterId();
/* 121 */       this._objectIdWriter = builder.getObjectIdWriter();
/* 122 */       JsonFormat.Value format = builder.getBeanDescription().findExpectedFormat(null);
/* 123 */       this._serializationShape = (format == null ? null : format.getShape());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanSerializerBase(BeanSerializerBase src, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/* 130 */     super(src._handledType);
/* 131 */     this._props = properties;
/* 132 */     this._filteredProps = filteredProperties;
/*     */     
/* 134 */     this._typeId = src._typeId;
/* 135 */     this._anyGetterWriter = src._anyGetterWriter;
/* 136 */     this._objectIdWriter = src._objectIdWriter;
/* 137 */     this._propertyFilterId = src._propertyFilterId;
/* 138 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter)
/*     */   {
/* 144 */     this(src, objectIdWriter, src._propertyFilterId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId)
/*     */   {
/* 153 */     super(src._handledType);
/* 154 */     this._props = src._props;
/* 155 */     this._filteredProps = src._filteredProps;
/*     */     
/* 157 */     this._typeId = src._typeId;
/* 158 */     this._anyGetterWriter = src._anyGetterWriter;
/* 159 */     this._objectIdWriter = objectIdWriter;
/* 160 */     this._propertyFilterId = filterId;
/* 161 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, String[] toIgnore)
/*     */   {
/* 166 */     super(src._handledType);
/*     */     
/*     */ 
/* 169 */     HashSet<String> ignoredSet = com.fasterxml.jackson.databind.util.ArrayBuilders.arrayToSet(toIgnore);
/* 170 */     BeanPropertyWriter[] propsIn = src._props;
/* 171 */     BeanPropertyWriter[] fpropsIn = src._filteredProps;
/* 172 */     int len = propsIn.length;
/*     */     
/* 174 */     ArrayList<BeanPropertyWriter> propsOut = new ArrayList(len);
/* 175 */     ArrayList<BeanPropertyWriter> fpropsOut = fpropsIn == null ? null : new ArrayList(len);
/*     */     
/* 177 */     for (int i = 0; i < len; i++) {
/* 178 */       BeanPropertyWriter bpw = propsIn[i];
/*     */       
/* 180 */       if (!ignoredSet.contains(bpw.getName()))
/*     */       {
/*     */ 
/* 183 */         propsOut.add(bpw);
/* 184 */         if (fpropsIn != null)
/* 185 */           fpropsOut.add(fpropsIn[i]);
/*     */       }
/*     */     }
/* 188 */     this._props = ((BeanPropertyWriter[])propsOut.toArray(new BeanPropertyWriter[propsOut.size()]));
/* 189 */     this._filteredProps = (fpropsOut == null ? null : (BeanPropertyWriter[])fpropsOut.toArray(new BeanPropertyWriter[fpropsOut.size()]));
/*     */     
/* 191 */     this._typeId = src._typeId;
/* 192 */     this._anyGetterWriter = src._anyGetterWriter;
/* 193 */     this._objectIdWriter = src._objectIdWriter;
/* 194 */     this._propertyFilterId = src._propertyFilterId;
/* 195 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanSerializerBase withObjectIdWriter(ObjectIdWriter paramObjectIdWriter);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract BeanSerializerBase withIgnorals(String[] paramArrayOfString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract BeanSerializerBase asArraySerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract BeanSerializerBase withFilterId(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src)
/*     */   {
/* 236 */     this(src, src._props, src._filteredProps);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src, NameTransformer unwrapper)
/*     */   {
/* 244 */     this(src, rename(src._props, unwrapper), rename(src._filteredProps, unwrapper));
/*     */   }
/*     */   
/*     */ 
/*     */   private static final BeanPropertyWriter[] rename(BeanPropertyWriter[] props, NameTransformer transformer)
/*     */   {
/* 250 */     if ((props == null) || (props.length == 0) || (transformer == null) || (transformer == NameTransformer.NOP)) {
/* 251 */       return props;
/*     */     }
/* 253 */     int len = props.length;
/* 254 */     BeanPropertyWriter[] result = new BeanPropertyWriter[len];
/* 255 */     for (int i = 0; i < len; i++) {
/* 256 */       BeanPropertyWriter bpw = props[i];
/* 257 */       if (bpw != null) {
/* 258 */         result[i] = bpw.rename(transformer);
/*     */       }
/*     */     }
/* 261 */     return result;
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
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 278 */     int filteredCount = this._filteredProps == null ? 0 : this._filteredProps.length;
/* 279 */     int i = 0; for (int len = this._props.length; i < len; i++) {
/* 280 */       BeanPropertyWriter prop = this._props[i];
/*     */       
/* 282 */       if ((!prop.willSuppressNulls()) && (!prop.hasNullSerializer())) {
/* 283 */         JsonSerializer<Object> nullSer = provider.findNullValueSerializer(prop);
/* 284 */         if (nullSer != null) {
/* 285 */           prop.assignNullSerializer(nullSer);
/*     */           
/* 287 */           if (i < filteredCount) {
/* 288 */             BeanPropertyWriter w2 = this._filteredProps[i];
/* 289 */             if (w2 != null) {
/* 290 */               w2.assignNullSerializer(nullSer);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 296 */       if (!prop.hasSerializer())
/*     */       {
/*     */ 
/*     */ 
/* 300 */         JsonSerializer<Object> ser = findConvertingSerializer(provider, prop);
/* 301 */         if (ser == null)
/*     */         {
/* 303 */           JavaType type = prop.getSerializationType();
/*     */           
/*     */ 
/*     */ 
/* 307 */           if (type == null) {
/* 308 */             type = provider.constructType(prop.getGenericPropertyType());
/* 309 */             if (!type.isFinal()) {
/* 310 */               if ((!type.isContainerType()) && (type.containedTypeCount() <= 0)) continue;
/* 311 */               prop.setNonTrivialBaseType(type); continue;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 316 */           ser = provider.findValueSerializer(type, prop);
/*     */           
/*     */ 
/*     */ 
/* 320 */           if (type.isContainerType()) {
/* 321 */             TypeSerializer typeSer = (TypeSerializer)type.getContentType().getTypeHandler();
/* 322 */             if (typeSer != null)
/*     */             {
/* 324 */               if ((ser instanceof ContainerSerializer))
/*     */               {
/*     */ 
/* 327 */                 JsonSerializer<Object> ser2 = ((ContainerSerializer)ser).withValueTypeSerializer(typeSer);
/* 328 */                 ser = ser2;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 333 */         prop.assignSerializer(ser);
/*     */         
/* 335 */         if (i < filteredCount) {
/* 336 */           BeanPropertyWriter w2 = this._filteredProps[i];
/* 337 */           if (w2 != null) {
/* 338 */             w2.assignSerializer(ser);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 344 */     if (this._anyGetterWriter != null)
/*     */     {
/* 346 */       this._anyGetterWriter.resolve(provider);
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
/*     */   protected JsonSerializer<Object> findConvertingSerializer(SerializerProvider provider, BeanPropertyWriter prop)
/*     */     throws JsonMappingException
/*     */   {
/* 361 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 362 */     if (intr != null) {
/* 363 */       AnnotatedMember m = prop.getMember();
/* 364 */       if (m != null) {
/* 365 */         Object convDef = intr.findSerializationConverter(m);
/* 366 */         if (convDef != null) {
/* 367 */           Converter<Object, Object> conv = provider.converterInstance(prop.getMember(), convDef);
/* 368 */           JavaType delegateType = conv.getOutputType(provider.getTypeFactory());
/*     */           
/* 370 */           JsonSerializer<?> ser = delegateType.isJavaLangObject() ? null : provider.findValueSerializer(delegateType, prop);
/*     */           
/* 372 */           return new StdDelegatingSerializer(conv, delegateType, ser);
/*     */         }
/*     */       }
/*     */     }
/* 376 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 385 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 386 */     AnnotatedMember accessor = (property == null) || (intr == null) ? null : property.getMember();
/*     */     
/* 388 */     SerializationConfig config = provider.getConfig();
/*     */     
/*     */ 
/*     */ 
/* 392 */     JsonFormat.Shape shape = null;
/* 393 */     if (accessor != null) {
/* 394 */       JsonFormat.Value format = intr.findFormat(accessor);
/*     */       
/* 396 */       if (format != null) {
/* 397 */         shape = format.getShape();
/*     */         
/* 399 */         if ((shape != this._serializationShape) && 
/* 400 */           (this._handledType.isEnum())) {
/* 401 */           switch (shape)
/*     */           {
/*     */ 
/*     */           case STRING: 
/*     */           case NUMBER: 
/*     */           case NUMBER_INT: 
/* 407 */             BeanDescription desc = config.introspectClassAnnotations(this._handledType);
/* 408 */             JsonSerializer<?> ser = EnumSerializer.construct(this._handledType, provider.getConfig(), desc, format);
/*     */             
/* 410 */             return provider.handlePrimaryContextualization(ser, property);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 417 */     ObjectIdWriter oiw = this._objectIdWriter;
/* 418 */     String[] ignorals = null;
/* 419 */     Object newFilterId = null;
/*     */     
/*     */ 
/* 422 */     if (accessor != null) {
/* 423 */       ignorals = intr.findPropertiesToIgnore(accessor);
/* 424 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo(accessor);
/* 425 */       if (objectIdInfo == null)
/*     */       {
/* 427 */         if (oiw != null) {
/* 428 */           objectIdInfo = intr.findObjectReferenceInfo(accessor, new ObjectIdInfo(NAME_FOR_OBJECT_REF, null, null, null));
/*     */           
/* 430 */           oiw = this._objectIdWriter.withAlwaysAsId(objectIdInfo.getAlwaysAsId());
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 439 */         objectIdInfo = intr.findObjectReferenceInfo(accessor, objectIdInfo);
/*     */         
/* 441 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/* 442 */         JavaType type = provider.constructType(implClass);
/* 443 */         JavaType idType = provider.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/*     */         
/* 445 */         if (implClass == com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class) {
/* 446 */           String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 447 */           BeanPropertyWriter idProp = null;
/*     */           
/* 449 */           int i = 0; for (int len = this._props.length;; i++) {
/* 450 */             if (i == len) {
/* 451 */               throw new IllegalArgumentException("Invalid Object Id definition for " + this._handledType.getName() + ": can not find property with name '" + propName + "'");
/*     */             }
/*     */             
/* 454 */             BeanPropertyWriter prop = this._props[i];
/* 455 */             if (propName.equals(prop.getName())) {
/* 456 */               idProp = prop;
/*     */               
/*     */ 
/*     */ 
/* 460 */               if (i <= 0) break;
/* 461 */               System.arraycopy(this._props, 0, this._props, 1, i);
/* 462 */               this._props[0] = idProp;
/* 463 */               if (this._filteredProps == null) break;
/* 464 */               BeanPropertyWriter fp = this._filteredProps[i];
/* 465 */               System.arraycopy(this._filteredProps, 0, this._filteredProps, 1, i);
/* 466 */               this._filteredProps[0] = fp;
/* 467 */               break;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 472 */           idType = idProp.getType();
/* 473 */           ObjectIdGenerator<?> gen = new com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/* 474 */           oiw = ObjectIdWriter.construct(idType, (PropertyName)null, gen, objectIdInfo.getAlwaysAsId());
/*     */         } else {
/* 476 */           ObjectIdGenerator<?> gen = provider.objectIdGeneratorInstance(accessor, objectIdInfo);
/* 477 */           oiw = ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo.getAlwaysAsId());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 483 */       Object filterId = intr.findFilterId(accessor);
/* 484 */       if (filterId != null)
/*     */       {
/* 486 */         if ((this._propertyFilterId == null) || (!filterId.equals(this._propertyFilterId))) {
/* 487 */           newFilterId = filterId;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 492 */     BeanSerializerBase contextual = this;
/* 493 */     if (oiw != null) {
/* 494 */       JsonSerializer<?> ser = provider.findValueSerializer(oiw.idType, property);
/* 495 */       oiw = oiw.withSerializer(ser);
/* 496 */       if (oiw != this._objectIdWriter) {
/* 497 */         contextual = contextual.withObjectIdWriter(oiw);
/*     */       }
/*     */     }
/*     */     
/* 501 */     if ((ignorals != null) && (ignorals.length != 0)) {
/* 502 */       contextual = contextual.withIgnorals(ignorals);
/*     */     }
/* 504 */     if (newFilterId != null) {
/* 505 */       contextual = contextual.withFilterId(newFilterId);
/*     */     }
/* 507 */     if (shape == null) {
/* 508 */       shape = this._serializationShape;
/*     */     }
/* 510 */     if (shape == JsonFormat.Shape.ARRAY) {
/* 511 */       return contextual.asArraySerializer();
/*     */     }
/* 513 */     return contextual;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean usesObjectId()
/*     */   {
/* 524 */     return this._objectIdWriter != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void serialize(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 538 */     if (this._objectIdWriter != null) {
/* 539 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/* 540 */       return;
/*     */     }
/*     */     
/* 543 */     String typeStr = this._typeId == null ? null : _customTypeId(bean);
/* 544 */     if (typeStr == null) {
/* 545 */       typeSer.writeTypePrefixForObject(bean, gen);
/*     */     } else {
/* 547 */       typeSer.writeCustomTypePrefixForObject(bean, gen, typeStr);
/*     */     }
/* 549 */     if (this._propertyFilterId != null) {
/* 550 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 552 */       serializeFields(bean, gen, provider);
/*     */     }
/* 554 */     if (typeStr == null) {
/* 555 */       typeSer.writeTypeSuffixForObject(bean, gen);
/*     */     } else {
/* 557 */       typeSer.writeCustomTypeSuffixForObject(bean, gen, typeStr);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator jgen, SerializerProvider provider, boolean startEndObject)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 566 */     ObjectIdWriter w = this._objectIdWriter;
/* 567 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 569 */     if (objectId.writeAsId(jgen, provider, w)) {
/* 570 */       return;
/*     */     }
/*     */     
/* 573 */     Object id = objectId.generateId(bean);
/* 574 */     if (w.alwaysAsId) {
/* 575 */       w.serializer.serialize(id, jgen, provider);
/* 576 */       return;
/*     */     }
/* 578 */     if (startEndObject) {
/* 579 */       jgen.writeStartObject();
/*     */     }
/* 581 */     objectId.writeAsField(jgen, provider, w);
/* 582 */     if (this._propertyFilterId != null) {
/* 583 */       serializeFieldsFiltered(bean, jgen, provider);
/*     */     } else {
/* 585 */       serializeFields(bean, jgen, provider);
/*     */     }
/* 587 */     if (startEndObject) {
/* 588 */       jgen.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 597 */     ObjectIdWriter w = this._objectIdWriter;
/* 598 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 600 */     if (objectId.writeAsId(jgen, provider, w)) {
/* 601 */       return;
/*     */     }
/*     */     
/* 604 */     Object id = objectId.generateId(bean);
/* 605 */     if (w.alwaysAsId) {
/* 606 */       w.serializer.serialize(id, jgen, provider);
/* 607 */       return;
/*     */     }
/*     */     
/* 610 */     _serializeObjectId(bean, jgen, provider, typeSer, objectId);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void _serializeObjectId(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer, WritableObjectId objectId)
/*     */     throws IOException, com.fasterxml.jackson.core.JsonProcessingException, JsonGenerationException
/*     */   {
/* 617 */     ObjectIdWriter w = this._objectIdWriter;
/* 618 */     String typeStr = this._typeId == null ? null : _customTypeId(bean);
/* 619 */     if (typeStr == null) {
/* 620 */       typeSer.writeTypePrefixForObject(bean, jgen);
/*     */     } else {
/* 622 */       typeSer.writeCustomTypePrefixForObject(bean, jgen, typeStr);
/*     */     }
/* 624 */     objectId.writeAsField(jgen, provider, w);
/* 625 */     if (this._propertyFilterId != null) {
/* 626 */       serializeFieldsFiltered(bean, jgen, provider);
/*     */     } else {
/* 628 */       serializeFields(bean, jgen, provider);
/*     */     }
/* 630 */     if (typeStr == null) {
/* 631 */       typeSer.writeTypeSuffixForObject(bean, jgen);
/*     */     } else {
/* 633 */       typeSer.writeCustomTypeSuffixForObject(bean, jgen, typeStr);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final String _customTypeId(Object bean)
/*     */   {
/* 639 */     Object typeId = this._typeId.getValue(bean);
/* 640 */     if (typeId == null) {
/* 641 */       return "";
/*     */     }
/* 643 */     return (typeId instanceof String) ? (String)typeId : typeId.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void serializeFields(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */ 
/*     */     BeanPropertyWriter[] props;
/*     */     
/* 656 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 657 */       props = this._filteredProps;
/*     */     } else {
/* 659 */       props = this._props;
/*     */     }
/* 661 */     int i = 0;
/*     */     try {
/* 663 */       for (int len = props.length; i < len; i++) {
/* 664 */         BeanPropertyWriter prop = props[i];
/* 665 */         if (prop != null) {
/* 666 */           prop.serializeAsField(bean, jgen, provider);
/*     */         }
/*     */       }
/* 669 */       if (this._anyGetterWriter != null) {
/* 670 */         this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
/*     */       }
/*     */     } catch (Exception e) {
/* 673 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 674 */       wrapAndThrow(provider, e, bean, name);
/*     */ 
/*     */     }
/*     */     catch (StackOverflowError e)
/*     */     {
/*     */ 
/* 680 */       JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e);
/* 681 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 682 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 683 */       throw mapE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void serializeFieldsFiltered(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */ 
/*     */ 
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */ 
/* 700 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 701 */       props = this._filteredProps;
/*     */     } else {
/* 703 */       props = this._props;
/*     */     }
/* 705 */     PropertyFilter filter = findPropertyFilter(provider, this._propertyFilterId, bean);
/*     */     
/* 707 */     if (filter == null) {
/* 708 */       serializeFields(bean, jgen, provider);
/* 709 */       return;
/*     */     }
/* 711 */     int i = 0;
/*     */     try {
/* 713 */       for (int len = props.length; i < len; i++) {
/* 714 */         BeanPropertyWriter prop = props[i];
/* 715 */         if (prop != null) {
/* 716 */           filter.serializeAsField(bean, jgen, provider, prop);
/*     */         }
/*     */       }
/* 719 */       if (this._anyGetterWriter != null) {
/* 720 */         this._anyGetterWriter.getAndFilter(bean, jgen, provider, filter);
/*     */       }
/*     */     } catch (Exception e) {
/* 723 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 724 */       wrapAndThrow(provider, e, bean, name);
/*     */     } catch (StackOverflowError e) {
/* 726 */       JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e);
/* 727 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 728 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 729 */       throw mapE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 738 */     ObjectNode o = createSchemaNode("object", true);
/*     */     
/*     */ 
/* 741 */     JsonSerializableSchema ann = (JsonSerializableSchema)this._handledType.getAnnotation(JsonSerializableSchema.class);
/* 742 */     if (ann != null) {
/* 743 */       String id = ann.id();
/* 744 */       if ((id != null) && (id.length() > 0)) {
/* 745 */         o.put("id", id);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 751 */     ObjectNode propertiesNode = o.objectNode();
/*     */     PropertyFilter filter;
/* 753 */     PropertyFilter filter; if (this._propertyFilterId != null) {
/* 754 */       filter = findPropertyFilter(provider, this._propertyFilterId, null);
/*     */     } else {
/* 756 */       filter = null;
/*     */     }
/*     */     
/* 759 */     for (int i = 0; i < this._props.length; i++) {
/* 760 */       BeanPropertyWriter prop = this._props[i];
/* 761 */       if (filter == null) {
/* 762 */         prop.depositSchemaProperty(propertiesNode, provider);
/*     */       } else {
/* 764 */         filter.depositSchemaProperty(prop, propertiesNode, provider);
/*     */       }
/*     */     }
/*     */     
/* 768 */     o.put("properties", propertiesNode);
/* 769 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 777 */     if (visitor == null) {
/* 778 */       return;
/*     */     }
/* 780 */     com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
/* 781 */     if (objectVisitor == null) {
/* 782 */       return;
/*     */     }
/* 784 */     if (this._propertyFilterId != null) {
/* 785 */       PropertyFilter filter = findPropertyFilter(visitor.getProvider(), this._propertyFilterId, null);
/*     */       
/* 787 */       for (int i = 0; i < this._props.length; i++) {
/* 788 */         filter.depositSchemaProperty(this._props[i], objectVisitor, visitor.getProvider());
/*     */       }
/*     */     } else {
/* 791 */       for (int i = 0; i < this._props.length; i++) {
/* 792 */         this._props[i].depositSchemaProperty(objectVisitor);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\BeanSerializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */