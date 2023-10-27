/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class BeanPropertyWriter
/*     */   extends PropertyWriter
/*     */   implements BeanProperty
/*     */ {
/*  41 */   public static final Object MARKER_FOR_EMPTY = JsonInclude.Include.NON_EMPTY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotatedMember _member;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Annotations _contextAnnotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _declaredType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Method _accessorMethod;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Field _field;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashMap<Object, Object> _internalSettings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SerializedString _name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _wrapperName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _cfgSerializationType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _serializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _nullSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _suppressNulls;
/*     */   
/*     */ 
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
/*     */ 
/*     */   protected final Class<?>[] _includeInViews;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeSerializer _typeSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _nonTrivialBaseType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyMetadata _metadata;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, boolean suppressNulls, Object suppressableValue)
/*     */   {
/* 195 */     this._member = member;
/* 196 */     this._contextAnnotations = contextAnnotations;
/*     */     
/* 198 */     this._name = new SerializedString(propDef.getName());
/* 199 */     this._wrapperName = propDef.getWrapperName();
/* 200 */     this._metadata = propDef.getMetadata();
/* 201 */     this._includeInViews = propDef.findViews();
/*     */     
/* 203 */     this._declaredType = declaredType;
/* 204 */     this._serializer = ser;
/* 205 */     this._dynamicSerializers = (ser == null ? PropertySerializerMap.emptyForProperties() : null);
/* 206 */     this._typeSerializer = typeSer;
/* 207 */     this._cfgSerializationType = serType;
/*     */     
/* 209 */     if ((member instanceof AnnotatedField)) {
/* 210 */       this._accessorMethod = null;
/* 211 */       this._field = ((Field)member.getMember());
/* 212 */     } else if ((member instanceof AnnotatedMethod)) {
/* 213 */       this._accessorMethod = ((Method)member.getMember());
/* 214 */       this._field = null;
/*     */     }
/*     */     else {
/* 217 */       this._accessorMethod = null;
/* 218 */       this._field = null;
/*     */     }
/* 220 */     this._suppressNulls = suppressNulls;
/* 221 */     this._suppressableValue = suppressableValue;
/*     */     
/*     */ 
/* 224 */     this._nullSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter()
/*     */   {
/* 235 */     this._member = null;
/* 236 */     this._contextAnnotations = null;
/*     */     
/* 238 */     this._name = null;
/* 239 */     this._wrapperName = null;
/* 240 */     this._metadata = null;
/* 241 */     this._includeInViews = null;
/*     */     
/* 243 */     this._declaredType = null;
/* 244 */     this._serializer = null;
/* 245 */     this._dynamicSerializers = null;
/* 246 */     this._typeSerializer = null;
/* 247 */     this._cfgSerializationType = null;
/*     */     
/* 249 */     this._accessorMethod = null;
/* 250 */     this._field = null;
/* 251 */     this._suppressNulls = false;
/* 252 */     this._suppressableValue = null;
/*     */     
/* 254 */     this._nullSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base)
/*     */   {
/* 261 */     this(base, base._name);
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
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, PropertyName name)
/*     */   {
/* 274 */     this._name = new SerializedString(name.getSimpleName());
/* 275 */     this._wrapperName = base._wrapperName;
/*     */     
/* 277 */     this._member = base._member;
/* 278 */     this._contextAnnotations = base._contextAnnotations;
/* 279 */     this._declaredType = base._declaredType;
/* 280 */     this._accessorMethod = base._accessorMethod;
/* 281 */     this._field = base._field;
/* 282 */     this._serializer = base._serializer;
/* 283 */     this._nullSerializer = base._nullSerializer;
/*     */     
/* 285 */     if (base._internalSettings != null) {
/* 286 */       this._internalSettings = new HashMap(base._internalSettings);
/*     */     }
/* 288 */     this._cfgSerializationType = base._cfgSerializationType;
/* 289 */     this._dynamicSerializers = base._dynamicSerializers;
/* 290 */     this._suppressNulls = base._suppressNulls;
/* 291 */     this._suppressableValue = base._suppressableValue;
/* 292 */     this._includeInViews = base._includeInViews;
/* 293 */     this._typeSerializer = base._typeSerializer;
/* 294 */     this._nonTrivialBaseType = base._nonTrivialBaseType;
/* 295 */     this._metadata = base._metadata;
/*     */   }
/*     */   
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, SerializedString name) {
/* 299 */     this._name = name;
/* 300 */     this._wrapperName = base._wrapperName;
/*     */     
/* 302 */     this._member = base._member;
/* 303 */     this._contextAnnotations = base._contextAnnotations;
/* 304 */     this._declaredType = base._declaredType;
/* 305 */     this._accessorMethod = base._accessorMethod;
/* 306 */     this._field = base._field;
/* 307 */     this._serializer = base._serializer;
/* 308 */     this._nullSerializer = base._nullSerializer;
/*     */     
/* 310 */     if (base._internalSettings != null) {
/* 311 */       this._internalSettings = new HashMap(base._internalSettings);
/*     */     }
/* 313 */     this._cfgSerializationType = base._cfgSerializationType;
/* 314 */     this._dynamicSerializers = base._dynamicSerializers;
/* 315 */     this._suppressNulls = base._suppressNulls;
/* 316 */     this._suppressableValue = base._suppressableValue;
/* 317 */     this._includeInViews = base._includeInViews;
/* 318 */     this._typeSerializer = base._typeSerializer;
/* 319 */     this._nonTrivialBaseType = base._nonTrivialBaseType;
/* 320 */     this._metadata = base._metadata;
/*     */   }
/*     */   
/*     */   public BeanPropertyWriter rename(NameTransformer transformer) {
/* 324 */     String newName = transformer.transform(this._name.getValue());
/* 325 */     if (newName.equals(this._name.toString())) {
/* 326 */       return this;
/*     */     }
/* 328 */     return new BeanPropertyWriter(this, new PropertyName(newName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void assignSerializer(JsonSerializer<Object> ser)
/*     */   {
/* 338 */     if ((this._serializer != null) && (this._serializer != ser)) {
/* 339 */       throw new IllegalStateException("Can not override serializer");
/*     */     }
/* 341 */     this._serializer = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void assignNullSerializer(JsonSerializer<Object> nullSer)
/*     */   {
/* 351 */     if ((this._nullSerializer != null) && (this._nullSerializer != nullSer)) {
/* 352 */       throw new IllegalStateException("Can not override null serializer");
/*     */     }
/* 354 */     this._nullSerializer = nullSer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper)
/*     */   {
/* 362 */     return new UnwrappingBeanPropertyWriter(this, unwrapper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNonTrivialBaseType(JavaType t)
/*     */   {
/* 371 */     this._nonTrivialBaseType = t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 381 */     return this._name.getValue();
/*     */   }
/*     */   
/*     */   public PropertyName getFullName() {
/* 385 */     return new PropertyName(this._name.getValue());
/*     */   }
/*     */   
/* 388 */   public JavaType getType() { return this._declaredType; }
/* 389 */   public PropertyName getWrapperName() { return this._wrapperName; }
/* 390 */   public boolean isRequired() { return this._metadata.isRequired(); }
/* 391 */   public PropertyMetadata getMetadata() { return this._metadata; }
/*     */   
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/* 396 */     return this._member == null ? null : this._member.getAnnotation(acls);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */   {
/* 402 */     return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
/*     */   }
/*     */   
/* 405 */   public AnnotatedMember getMember() { return this._member; }
/*     */   
/*     */   protected void _depositSchemaProperty(ObjectNode propertiesNode, JsonNode schemaNode)
/*     */   {
/* 409 */     propertiesNode.set(getName(), schemaNode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVirtual()
/*     */   {
/* 420 */     return false;
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
/*     */   public Object getInternalSetting(Object key)
/*     */   {
/* 435 */     return this._internalSettings == null ? null : this._internalSettings.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object setInternalSetting(Object key, Object value)
/*     */   {
/* 444 */     if (this._internalSettings == null) {
/* 445 */       this._internalSettings = new HashMap();
/*     */     }
/* 447 */     return this._internalSettings.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object removeInternalSetting(Object key)
/*     */   {
/* 456 */     Object removed = null;
/* 457 */     if (this._internalSettings != null) {
/* 458 */       removed = this._internalSettings.remove(key);
/*     */       
/* 460 */       if (this._internalSettings.size() == 0) {
/* 461 */         this._internalSettings = null;
/*     */       }
/*     */     }
/* 464 */     return removed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 473 */   public SerializableString getSerializedName() { return this._name; }
/*     */   
/* 475 */   public boolean hasSerializer() { return this._serializer != null; }
/* 476 */   public boolean hasNullSerializer() { return this._nullSerializer != null; }
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
/* 487 */   public boolean isUnwrapping() { return false; }
/*     */   
/* 489 */   public boolean willSuppressNulls() { return this._suppressNulls; }
/*     */   
/*     */ 
/* 492 */   public JsonSerializer<Object> getSerializer() { return this._serializer; }
/*     */   
/* 494 */   public JavaType getSerializationType() { return this._cfgSerializationType; }
/*     */   
/*     */   public Class<?> getRawSerializationType() {
/* 497 */     return this._cfgSerializationType == null ? null : this._cfgSerializationType.getRawClass();
/*     */   }
/*     */   
/*     */   public Class<?> getPropertyType() {
/* 501 */     return this._accessorMethod != null ? this._accessorMethod.getReturnType() : this._field.getType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getGenericPropertyType()
/*     */   {
/* 510 */     if (this._accessorMethod != null) {
/* 511 */       return this._accessorMethod.getGenericReturnType();
/*     */     }
/* 513 */     if (this._field != null) {
/* 514 */       return this._field.getGenericType();
/*     */     }
/* 516 */     return null;
/*     */   }
/*     */   
/* 519 */   public Class<?>[] getViews() { return this._includeInViews; }
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
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 536 */     Object value = this._accessorMethod == null ? this._field.get(bean) : this._accessorMethod.invoke(bean, new Object[0]);
/*     */     
/*     */ 
/* 539 */     if (value == null) {
/* 540 */       if (this._nullSerializer != null) {
/* 541 */         gen.writeFieldName(this._name);
/* 542 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       }
/* 544 */       return;
/*     */     }
/*     */     
/* 547 */     JsonSerializer<Object> ser = this._serializer;
/* 548 */     if (ser == null) {
/* 549 */       Class<?> cls = value.getClass();
/* 550 */       PropertySerializerMap m = this._dynamicSerializers;
/* 551 */       ser = m.serializerFor(cls);
/* 552 */       if (ser == null) {
/* 553 */         ser = _findAndAddDynamic(m, cls, prov);
/*     */       }
/*     */     }
/*     */     
/* 557 */     if (this._suppressableValue != null) {
/* 558 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 559 */         if (!ser.isEmpty(prov, value)) {}
/*     */ 
/*     */       }
/* 562 */       else if (this._suppressableValue.equals(value)) {
/* 563 */         return;
/*     */       }
/*     */     }
/*     */     
/* 567 */     if (value == bean)
/*     */     {
/* 569 */       if (_handleSelfReference(bean, gen, prov, ser)) {
/* 570 */         return;
/*     */       }
/*     */     }
/* 573 */     gen.writeFieldName(this._name);
/* 574 */     if (this._typeSerializer == null) {
/* 575 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 577 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
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
/*     */   public void serializeAsOmittedField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 591 */     if (!gen.canOmitFields()) {
/* 592 */       gen.writeOmittedField(this._name.getValue());
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
/*     */   public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 608 */     Object value = this._accessorMethod == null ? this._field.get(bean) : this._accessorMethod.invoke(bean, new Object[0]);
/* 609 */     if (value == null) {
/* 610 */       if (this._nullSerializer != null) {
/* 611 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       } else {
/* 613 */         gen.writeNull();
/*     */       }
/* 615 */       return;
/*     */     }
/*     */     
/* 618 */     JsonSerializer<Object> ser = this._serializer;
/* 619 */     if (ser == null) {
/* 620 */       Class<?> cls = value.getClass();
/* 621 */       PropertySerializerMap map = this._dynamicSerializers;
/* 622 */       ser = map.serializerFor(cls);
/* 623 */       if (ser == null) {
/* 624 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/*     */     
/* 628 */     if (this._suppressableValue != null) {
/* 629 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 630 */         if (ser.isEmpty(prov, value)) {
/* 631 */           serializeAsPlaceholder(bean, gen, prov);
/*     */         }
/*     */       }
/* 634 */       else if (this._suppressableValue.equals(value)) {
/* 635 */         serializeAsPlaceholder(bean, gen, prov);
/* 636 */         return;
/*     */       }
/*     */     }
/*     */     
/* 640 */     if ((value == bean) && 
/* 641 */       (_handleSelfReference(bean, gen, prov, ser))) {
/* 642 */       return;
/*     */     }
/*     */     
/* 645 */     if (this._typeSerializer == null) {
/* 646 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 648 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
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
/*     */   public void serializeAsPlaceholder(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 664 */     if (this._nullSerializer != null) {
/* 665 */       this._nullSerializer.serialize(null, gen, prov);
/*     */     } else {
/* 667 */       gen.writeNull();
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
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor v)
/*     */     throws JsonMappingException
/*     */   {
/* 682 */     if (v != null) {
/* 683 */       if (isRequired()) {
/* 684 */         v.property(this);
/*     */       } else {
/* 686 */         v.optionalProperty(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 706 */     JavaType propType = getSerializationType();
/*     */     
/* 708 */     Type hint = propType == null ? getGenericPropertyType() : propType.getRawClass();
/*     */     
/*     */ 
/* 711 */     JsonSerializer<Object> ser = getSerializer();
/* 712 */     if (ser == null) {
/* 713 */       ser = provider.findValueSerializer(getType(), this);
/*     */     }
/* 715 */     boolean isOptional = !isRequired();
/* 716 */     JsonNode schemaNode; JsonNode schemaNode; if ((ser instanceof SchemaAware)) {
/* 717 */       schemaNode = ((SchemaAware)ser).getSchema(provider, hint, isOptional);
/*     */     } else {
/* 719 */       schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */     }
/* 721 */     _depositSchemaProperty(propertiesNode, schemaNode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     
/*     */ 
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     
/* 734 */     if (this._nonTrivialBaseType != null) {
/* 735 */       JavaType t = provider.constructSpecializedType(this._nonTrivialBaseType, type);
/* 736 */       result = map.findAndAddPrimarySerializer(t, provider, this);
/*     */     } else {
/* 738 */       result = map.findAndAddPrimarySerializer(type, provider, this);
/*     */     }
/*     */     
/* 741 */     if (map != result.map) {
/* 742 */       this._dynamicSerializers = result.map;
/*     */     }
/* 744 */     return result.serializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object get(Object bean)
/*     */     throws Exception
/*     */   {
/* 756 */     return this._accessorMethod == null ? this._field.get(bean) : this._accessorMethod.invoke(bean, new Object[0]);
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
/*     */   protected boolean _handleSelfReference(Object bean, JsonGenerator gen, SerializerProvider prov, JsonSerializer<?> ser)
/*     */     throws JsonMappingException
/*     */   {
/* 774 */     if ((prov.isEnabled(SerializationFeature.FAIL_ON_SELF_REFERENCES)) && (!ser.usesObjectId()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 780 */       if ((ser instanceof BeanSerializerBase)) {
/* 781 */         throw new JsonMappingException("Direct self-reference leading to cycle");
/*     */       }
/*     */     }
/* 784 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 789 */     StringBuilder sb = new StringBuilder(40);
/* 790 */     sb.append("property '").append(getName()).append("' (");
/* 791 */     if (this._accessorMethod != null) {
/* 792 */       sb.append("via method ").append(this._accessorMethod.getDeclaringClass().getName()).append("#").append(this._accessorMethod.getName());
/* 793 */     } else if (this._field != null) {
/* 794 */       sb.append("field \"").append(this._field.getDeclaringClass().getName()).append("#").append(this._field.getName());
/*     */     } else {
/* 796 */       sb.append("virtual");
/*     */     }
/* 798 */     if (this._serializer == null) {
/* 799 */       sb.append(", no static serializer");
/*     */     } else {
/* 801 */       sb.append(", static serializer of type " + this._serializer.getClass().getName());
/*     */     }
/* 803 */     sb.append(')');
/* 804 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\BeanPropertyWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */