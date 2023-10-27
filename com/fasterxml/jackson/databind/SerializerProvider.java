/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerCache;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.impl.FailingSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;
/*      */ import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*      */ import com.fasterxml.jackson.databind.ser.std.NullSerializer;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*      */ import java.io.IOException;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class SerializerProvider
/*      */   extends DatabindContext
/*      */ {
/*      */   protected static final boolean CACHE_UNKNOWN_MAPPINGS = false;
/*   47 */   public static final JsonSerializer<Object> DEFAULT_NULL_KEY_SERIALIZER = new FailingSerializer("Null key for a Map not allowed in JSON (use a converting NullKeySerializer?)");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   53 */   protected static final JsonSerializer<Object> DEFAULT_UNKNOWN_SERIALIZER = new UnknownSerializer();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SerializationConfig _config;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> _serializationView;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SerializerFactory _serializerFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SerializerCache _serializerCache;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final RootNameLookup _rootNames;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient ContextAttributes _attributes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  120 */   protected JsonSerializer<Object> _unknownTypeSerializer = DEFAULT_UNKNOWN_SERIALIZER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _keySerializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */   protected JsonSerializer<Object> _nullValueSerializer = NullSerializer.instance;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  142 */   protected JsonSerializer<Object> _nullKeySerializer = DEFAULT_NULL_KEY_SERIALIZER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ReadOnlyClassToSerializerMap _knownSerializers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormat _dateFormat;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _stdNullValueSerializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerProvider()
/*      */   {
/*  183 */     this._config = null;
/*  184 */     this._serializerFactory = null;
/*  185 */     this._serializerCache = new SerializerCache();
/*      */     
/*  187 */     this._knownSerializers = null;
/*  188 */     this._rootNames = new RootNameLookup();
/*      */     
/*  190 */     this._serializationView = null;
/*  191 */     this._attributes = null;
/*      */     
/*      */ 
/*  194 */     this._stdNullValueSerializer = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f)
/*      */   {
/*  205 */     if (config == null) {
/*  206 */       throw new NullPointerException();
/*      */     }
/*  208 */     this._serializerFactory = f;
/*  209 */     this._config = config;
/*      */     
/*  211 */     this._serializerCache = src._serializerCache;
/*  212 */     this._unknownTypeSerializer = src._unknownTypeSerializer;
/*  213 */     this._keySerializer = src._keySerializer;
/*  214 */     this._nullValueSerializer = src._nullValueSerializer;
/*  215 */     this._nullKeySerializer = src._nullKeySerializer;
/*      */     
/*  217 */     this._stdNullValueSerializer = (this._nullValueSerializer == DEFAULT_NULL_KEY_SERIALIZER);
/*      */     
/*  219 */     this._rootNames = src._rootNames;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  224 */     this._knownSerializers = this._serializerCache.getReadOnlyLookupMap();
/*      */     
/*  226 */     this._serializationView = config.getActiveView();
/*  227 */     this._attributes = config.getAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializerProvider(SerializerProvider src)
/*      */   {
/*  238 */     this._config = null;
/*  239 */     this._serializationView = null;
/*  240 */     this._serializerFactory = null;
/*  241 */     this._knownSerializers = null;
/*      */     
/*      */ 
/*  244 */     this._serializerCache = new SerializerCache();
/*  245 */     this._rootNames = new RootNameLookup();
/*      */     
/*  247 */     this._unknownTypeSerializer = src._unknownTypeSerializer;
/*  248 */     this._keySerializer = src._keySerializer;
/*  249 */     this._nullValueSerializer = src._nullValueSerializer;
/*  250 */     this._nullKeySerializer = src._nullKeySerializer;
/*      */     
/*  252 */     this._stdNullValueSerializer = src._stdNullValueSerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultKeySerializer(JsonSerializer<Object> ks)
/*      */   {
/*  269 */     if (ks == null) {
/*  270 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*      */     }
/*  272 */     this._keySerializer = ks;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNullValueSerializer(JsonSerializer<Object> nvs)
/*      */   {
/*  286 */     if (nvs == null) {
/*  287 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*      */     }
/*  289 */     this._nullValueSerializer = nvs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNullKeySerializer(JsonSerializer<Object> nks)
/*      */   {
/*  303 */     if (nks == null) {
/*  304 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*      */     }
/*  306 */     this._nullKeySerializer = nks;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final SerializationConfig getConfig()
/*      */   {
/*  319 */     return this._config;
/*      */   }
/*      */   
/*      */   public final AnnotationIntrospector getAnnotationIntrospector() {
/*  323 */     return this._config.getAnnotationIntrospector();
/*      */   }
/*      */   
/*      */   public final TypeFactory getTypeFactory()
/*      */   {
/*  328 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */   public final Class<?> getActiveView() {
/*  332 */     return this._serializationView;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final Class<?> getSerializationView()
/*      */   {
/*  338 */     return this._serializationView;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(Object key)
/*      */   {
/*  348 */     return this._attributes.getAttribute(key);
/*      */   }
/*      */   
/*      */ 
/*      */   public SerializerProvider setAttribute(Object key, Object value)
/*      */   {
/*  354 */     this._attributes = this._attributes.withPerCallAttribute(key, value);
/*  355 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(SerializationFeature feature)
/*      */   {
/*  373 */     return this._config.isEnabled(feature);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean hasSerializationFeatures(int featureMask)
/*      */   {
/*  383 */     return this._config.hasSerializationFeatures(featureMask);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final FilterProvider getFilterProvider()
/*      */   {
/*  394 */     return this._config.getFilterProvider();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  404 */     return this._config.getLocale();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  414 */     return this._config.getTimeZone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract WritableObjectId findObjectId(Object paramObject, ObjectIdGenerator<?> paramObjectIdGenerator);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  462 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  463 */     if (ser == null)
/*      */     {
/*  465 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  466 */       if (ser == null)
/*      */       {
/*  468 */         ser = this._serializerCache.untypedValueSerializer(this._config.constructType(valueType));
/*  469 */         if (ser == null)
/*      */         {
/*  471 */           ser = _createAndCacheUntypedSerializer(valueType);
/*      */           
/*  473 */           if (ser == null) {
/*  474 */             ser = getUnknownTypeSerializer(valueType);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  479 */             return ser;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  485 */     return handleSecondaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findValueSerializer(JavaType valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  503 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  504 */     if (ser == null) {
/*  505 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  506 */       if (ser == null) {
/*  507 */         ser = _createAndCacheUntypedSerializer(valueType);
/*  508 */         if (ser == null) {
/*  509 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*      */           
/*      */ 
/*      */ 
/*  513 */           return ser;
/*      */         }
/*      */       }
/*      */     }
/*  517 */     return handleSecondaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findValueSerializer(Class<?> valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  530 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  531 */     if (ser == null) {
/*  532 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  533 */       if (ser == null) {
/*  534 */         ser = this._serializerCache.untypedValueSerializer(this._config.constructType(valueType));
/*  535 */         if (ser == null) {
/*  536 */           ser = _createAndCacheUntypedSerializer(valueType);
/*  537 */           if (ser == null) {
/*  538 */             ser = getUnknownTypeSerializer(valueType);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  546 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findValueSerializer(JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  560 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  561 */     if (ser == null) {
/*  562 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  563 */       if (ser == null) {
/*  564 */         ser = _createAndCacheUntypedSerializer(valueType);
/*  565 */         if (ser == null) {
/*  566 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  573 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findPrimaryPropertySerializer(JavaType valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  592 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  593 */     if (ser == null) {
/*  594 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  595 */       if (ser == null) {
/*  596 */         ser = _createAndCacheUntypedSerializer(valueType);
/*  597 */         if (ser == null) {
/*  598 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  603 */           return ser;
/*      */         }
/*      */       }
/*      */     }
/*  607 */     return handlePrimaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findPrimaryPropertySerializer(Class<?> valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  618 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  619 */     if (ser == null) {
/*  620 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  621 */       if (ser == null) {
/*  622 */         ser = this._serializerCache.untypedValueSerializer(this._config.constructType(valueType));
/*  623 */         if (ser == null) {
/*  624 */           ser = _createAndCacheUntypedSerializer(valueType);
/*  625 */           if (ser == null) {
/*  626 */             ser = getUnknownTypeSerializer(valueType);
/*      */             
/*      */ 
/*      */ 
/*  630 */             return ser;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  635 */     return handlePrimaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueType, boolean cache, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  658 */     JsonSerializer<Object> ser = this._knownSerializers.typedValueSerializer(valueType);
/*  659 */     if (ser != null) {
/*  660 */       return ser;
/*      */     }
/*      */     
/*  663 */     ser = this._serializerCache.typedValueSerializer(valueType);
/*  664 */     if (ser != null) {
/*  665 */       return ser;
/*      */     }
/*      */     
/*      */ 
/*  669 */     ser = findValueSerializer(valueType, property);
/*  670 */     TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, this._config.constructType(valueType));
/*      */     
/*  672 */     if (typeSer != null) {
/*  673 */       typeSer = typeSer.forProperty(property);
/*  674 */       ser = new TypeWrappedSerializer(typeSer, ser);
/*      */     }
/*  676 */     if (cache) {
/*  677 */       this._serializerCache.addTypedSerializer(valueType, ser);
/*      */     }
/*  679 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  703 */     JsonSerializer<Object> ser = this._knownSerializers.typedValueSerializer(valueType);
/*  704 */     if (ser != null) {
/*  705 */       return ser;
/*      */     }
/*      */     
/*  708 */     ser = this._serializerCache.typedValueSerializer(valueType);
/*  709 */     if (ser != null) {
/*  710 */       return ser;
/*      */     }
/*      */     
/*      */ 
/*  714 */     ser = findValueSerializer(valueType, property);
/*  715 */     TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, valueType);
/*  716 */     if (typeSer != null) {
/*  717 */       typeSer = typeSer.forProperty(property);
/*  718 */       ser = new TypeWrappedSerializer(typeSer, ser);
/*      */     }
/*  720 */     if (cache) {
/*  721 */       this._serializerCache.addTypedSerializer(valueType, ser);
/*      */     }
/*  723 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findKeySerializer(JavaType keyType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  740 */     JsonSerializer<Object> ser = this._serializerFactory.createKeySerializer(this._config, keyType, this._keySerializer);
/*      */     
/*  742 */     return _handleContextualResolvable(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> getDefaultNullKeySerializer()
/*      */   {
/*  755 */     return this._nullKeySerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> getDefaultNullValueSerializer()
/*      */   {
/*  762 */     return this._nullValueSerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findNullKeySerializer(JavaType serializationType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  786 */     return this._nullKeySerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findNullValueSerializer(BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  802 */     return this._nullValueSerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> getUnknownTypeSerializer(Class<?> unknownType)
/*      */   {
/*  818 */     return this._unknownTypeSerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUnknownTypeSerializer(JsonSerializer<?> ser)
/*      */   {
/*  829 */     return (ser == this._unknownTypeSerializer) || (ser == null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonSerializer<Object> serializerInstance(Annotated paramAnnotated, Object paramObject)
/*      */     throws JsonMappingException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<?> handlePrimaryContextualization(JsonSerializer<?> ser, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  873 */     if ((ser != null) && 
/*  874 */       ((ser instanceof ContextualSerializer))) {
/*  875 */       ser = ((ContextualSerializer)ser).createContextual(this, property);
/*      */     }
/*      */     
/*  878 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<?> handleSecondaryContextualization(JsonSerializer<?> ser, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  901 */     if ((ser != null) && 
/*  902 */       ((ser instanceof ContextualSerializer))) {
/*  903 */       ser = ((ContextualSerializer)ser).createContextual(this, property);
/*      */     }
/*      */     
/*  906 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeValue(Object value, JsonGenerator jgen)
/*      */     throws IOException
/*      */   {
/*  924 */     if (value == null) {
/*  925 */       if (this._stdNullValueSerializer) {
/*  926 */         jgen.writeNull();
/*      */       } else {
/*  928 */         this._nullValueSerializer.serialize(null, jgen, this);
/*      */       }
/*      */     } else {
/*  931 */       Class<?> cls = value.getClass();
/*  932 */       findTypedValueSerializer(cls, true, null).serialize(value, jgen, this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeField(String fieldName, Object value, JsonGenerator jgen)
/*      */     throws IOException
/*      */   {
/*  944 */     jgen.writeFieldName(fieldName);
/*  945 */     if (value == null)
/*      */     {
/*      */ 
/*      */ 
/*  949 */       if (this._stdNullValueSerializer) {
/*  950 */         jgen.writeNull();
/*      */       } else {
/*  952 */         this._nullValueSerializer.serialize(null, jgen, this);
/*      */       }
/*      */     } else {
/*  955 */       Class<?> cls = value.getClass();
/*  956 */       findTypedValueSerializer(cls, true, null).serialize(value, jgen, this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeDateValue(long timestamp, JsonGenerator jgen)
/*      */     throws IOException
/*      */   {
/*  971 */     if (isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
/*  972 */       jgen.writeNumber(timestamp);
/*      */     } else {
/*  974 */       jgen.writeString(_dateFormat().format(new Date(timestamp)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeDateValue(Date date, JsonGenerator jgen)
/*      */     throws IOException
/*      */   {
/*  989 */     if (isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
/*  990 */       jgen.writeNumber(date.getTime());
/*      */     } else {
/*  992 */       jgen.writeString(_dateFormat().format(date));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void defaultSerializeDateKey(long timestamp, JsonGenerator jgen)
/*      */     throws IOException
/*      */   {
/* 1004 */     if (isEnabled(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)) {
/* 1005 */       jgen.writeFieldName(String.valueOf(timestamp));
/*      */     } else {
/* 1007 */       jgen.writeFieldName(_dateFormat().format(new Date(timestamp)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void defaultSerializeDateKey(Date date, JsonGenerator jgen)
/*      */     throws IOException
/*      */   {
/* 1018 */     if (isEnabled(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)) {
/* 1019 */       jgen.writeFieldName(String.valueOf(date.getTime()));
/*      */     } else {
/* 1021 */       jgen.writeFieldName(_dateFormat().format(date));
/*      */     }
/*      */   }
/*      */   
/*      */   public final void defaultSerializeNull(JsonGenerator jgen) throws IOException
/*      */   {
/* 1027 */     if (this._stdNullValueSerializer) {
/* 1028 */       jgen.writeNull();
/*      */     } else {
/* 1030 */       this._nullValueSerializer.serialize(null, jgen, this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException mappingException(String message, Object... args)
/*      */   {
/* 1044 */     if ((args != null) && (args.length > 0)) {
/* 1045 */       message = String.format(message, args);
/*      */     }
/* 1047 */     return new JsonMappingException(message);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportIncompatibleRootType(Object value, JavaType rootType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1062 */     if (rootType.isPrimitive()) {
/* 1063 */       Class<?> wrapperType = ClassUtil.wrapperType(rootType.getRawClass());
/*      */       
/* 1065 */       if (wrapperType.isAssignableFrom(value.getClass())) {
/* 1066 */         return;
/*      */       }
/*      */     }
/* 1069 */     throw new JsonMappingException("Incompatible types: declared root type (" + rootType + ") vs " + value.getClass().getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _findExplicitUntypedSerializer(Class<?> runtimeType)
/*      */     throws JsonMappingException
/*      */   {
/* 1084 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(runtimeType);
/* 1085 */     if (ser == null)
/*      */     {
/* 1087 */       ser = this._serializerCache.untypedValueSerializer(runtimeType);
/* 1088 */       if (ser == null) {
/* 1089 */         ser = _createAndCacheUntypedSerializer(runtimeType);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1097 */     if (isUnknownTypeSerializer(ser)) {
/* 1098 */       return null;
/*      */     }
/* 1100 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _createAndCacheUntypedSerializer(Class<?> rawType)
/*      */     throws JsonMappingException
/*      */   {
/* 1117 */     JavaType type = this._config.constructType(rawType);
/*      */     JsonSerializer<Object> ser;
/*      */     try {
/* 1120 */       ser = _createUntypedSerializer(type);
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException iae)
/*      */     {
/* 1125 */       throw new JsonMappingException(iae.getMessage(), null, iae);
/*      */     }
/*      */     
/* 1128 */     if (ser != null) {
/* 1129 */       this._serializerCache.addAndResolveNonTypedSerializer(type, ser, this);
/*      */     }
/* 1131 */     return ser;
/*      */   }
/*      */   
/*      */   protected JsonSerializer<Object> _createAndCacheUntypedSerializer(JavaType type) throws JsonMappingException
/*      */   {
/*      */     JsonSerializer<Object> ser;
/*      */     try
/*      */     {
/* 1139 */       ser = _createUntypedSerializer(type);
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException iae)
/*      */     {
/* 1144 */       throw new JsonMappingException(iae.getMessage(), null, iae);
/*      */     }
/*      */     
/* 1147 */     if (ser != null) {
/* 1148 */       this._serializerCache.addAndResolveNonTypedSerializer(type, ser, this);
/*      */     }
/* 1150 */     return ser;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected JsonSerializer<Object> _createUntypedSerializer(JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 12	com/fasterxml/jackson/databind/SerializerProvider:_serializerCache	Lcom/fasterxml/jackson/databind/ser/SerializerCache;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 9	com/fasterxml/jackson/databind/SerializerProvider:_serializerFactory	Lcom/fasterxml/jackson/databind/ser/SerializerFactory;
/*      */     //   11: aload_0
/*      */     //   12: aload_1
/*      */     //   13: invokevirtual 100	com/fasterxml/jackson/databind/ser/SerializerFactory:createSerializer	(Lcom/fasterxml/jackson/databind/SerializerProvider;Lcom/fasterxml/jackson/databind/JavaType;)Lcom/fasterxml/jackson/databind/JsonSerializer;
/*      */     //   16: aload_2
/*      */     //   17: monitorexit
/*      */     //   18: areturn
/*      */     //   19: astore_3
/*      */     //   20: aload_2
/*      */     //   21: monitorexit
/*      */     //   22: aload_3
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1164	-> byte code offset #0
/*      */     //   Java source line #1166	-> byte code offset #7
/*      */     //   Java source line #1167	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	SerializerProvider
/*      */     //   0	24	1	type	JavaType
/*      */     //   5	16	2	Ljava/lang/Object;	Object
/*      */     //   19	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   protected JsonSerializer<Object> _handleContextualResolvable(JsonSerializer<?> ser, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/* 1179 */     if ((ser instanceof ResolvableSerializer)) {
/* 1180 */       ((ResolvableSerializer)ser).resolve(this);
/*      */     }
/* 1182 */     return handleSecondaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */   protected JsonSerializer<Object> _handleResolvable(JsonSerializer<?> ser)
/*      */     throws JsonMappingException
/*      */   {
/* 1189 */     if ((ser instanceof ResolvableSerializer)) {
/* 1190 */       ((ResolvableSerializer)ser).resolve(this);
/*      */     }
/* 1192 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DateFormat _dateFormat()
/*      */   {
/* 1203 */     if (this._dateFormat != null) {
/* 1204 */       return this._dateFormat;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1211 */     DateFormat df = this._config.getDateFormat();
/* 1212 */     this._dateFormat = (df = (DateFormat)df.clone());
/* 1213 */     return df;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\SerializerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */