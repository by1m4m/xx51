/*      */ package com.fasterxml.jackson.databind.ser;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.JsonSerializer;
/*      */ import com.fasterxml.jackson.databind.SerializationConfig;
/*      */ import com.fasterxml.jackson.databind.SerializerProvider;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
/*      */ import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
/*      */ import com.fasterxml.jackson.databind.ext.OptionalHandlerFactory;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*      */ import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.IteratorSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.BooleanSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.DateSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
/*      */ import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
/*      */ import com.fasterxml.jackson.databind.type.ArrayType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionType;
/*      */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*      */ import com.fasterxml.jackson.databind.type.MapType;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ 
/*      */ public abstract class BasicSerializerFactory extends SerializerFactory implements java.io.Serializable
/*      */ {
/*   51 */   protected static final HashMap<String, JsonSerializer<?>> _concrete = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   59 */   protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy = new HashMap();
/*      */   
/*      */   protected final SerializerFactoryConfig _factoryConfig;
/*      */   
/*      */ 
/*      */   static
/*      */   {
/*   66 */     _concrete.put(String.class.getName(), new com.fasterxml.jackson.databind.ser.std.StringSerializer());
/*   67 */     ToStringSerializer sls = ToStringSerializer.instance;
/*   68 */     _concrete.put(StringBuffer.class.getName(), sls);
/*   69 */     _concrete.put(StringBuilder.class.getName(), sls);
/*   70 */     _concrete.put(Character.class.getName(), sls);
/*   71 */     _concrete.put(Character.TYPE.getName(), sls);
/*      */     
/*      */ 
/*   74 */     com.fasterxml.jackson.databind.ser.std.NumberSerializers.addAll(_concrete);
/*   75 */     _concrete.put(Boolean.TYPE.getName(), new BooleanSerializer(true));
/*   76 */     _concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
/*      */     
/*      */ 
/*   79 */     _concrete.put(BigInteger.class.getName(), new NumberSerializer(BigInteger.class));
/*   80 */     _concrete.put(BigDecimal.class.getName(), new NumberSerializer(BigDecimal.class));
/*      */     
/*      */ 
/*      */ 
/*   84 */     _concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
/*   85 */     DateSerializer dateSer = DateSerializer.instance;
/*   86 */     _concrete.put(java.util.Date.class.getName(), dateSer);
/*      */     
/*   88 */     _concrete.put(java.sql.Timestamp.class.getName(), dateSer);
/*      */     
/*      */ 
/*   91 */     _concreteLazy.put(java.sql.Date.class.getName(), com.fasterxml.jackson.databind.ser.std.SqlDateSerializer.class);
/*   92 */     _concreteLazy.put(java.sql.Time.class.getName(), com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer.class);
/*      */     
/*      */ 
/*   95 */     for (Map.Entry<Class<?>, Object> en : com.fasterxml.jackson.databind.ser.std.StdJdkSerializers.all()) {
/*   96 */       Object value = en.getValue();
/*   97 */       if ((value instanceof JsonSerializer)) {
/*   98 */         _concrete.put(((Class)en.getKey()).getName(), (JsonSerializer)value);
/*   99 */       } else if ((value instanceof Class))
/*      */       {
/*  101 */         Class<? extends JsonSerializer<?>> cls = (Class)value;
/*  102 */         _concreteLazy.put(((Class)en.getKey()).getName(), cls);
/*      */       } else {
/*  104 */         throw new IllegalStateException("Internal error: unrecognized value of type " + en.getClass().getName());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  110 */     _concreteLazy.put(com.fasterxml.jackson.databind.util.TokenBuffer.class.getName(), com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer.class);
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
/*      */ 
/*      */ 
/*      */   protected BasicSerializerFactory(SerializerFactoryConfig config)
/*      */   {
/*  137 */     this._factoryConfig = (config == null ? new SerializerFactoryConfig() : config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerFactoryConfig getFactoryConfig()
/*      */   {
/*  148 */     return this._factoryConfig;
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
/*      */   public final SerializerFactory withAdditionalSerializers(Serializers additional)
/*      */   {
/*  169 */     return withConfig(this._factoryConfig.withAdditionalSerializers(additional));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final SerializerFactory withAdditionalKeySerializers(Serializers additional)
/*      */   {
/*  178 */     return withConfig(this._factoryConfig.withAdditionalKeySerializers(additional));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final SerializerFactory withSerializerModifier(BeanSerializerModifier modifier)
/*      */   {
/*  187 */     return withConfig(this._factoryConfig.withSerializerModifier(modifier));
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
/*      */   public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType keyType, JsonSerializer<Object> defaultImpl)
/*      */   {
/*  209 */     BeanDescription beanDesc = config.introspectClassAnnotations(keyType.getRawClass());
/*  210 */     JsonSerializer<?> ser = null;
/*      */     
/*  212 */     if (this._factoryConfig.hasKeySerializers())
/*      */     {
/*  214 */       for (Serializers serializers : this._factoryConfig.keySerializers()) {
/*  215 */         ser = serializers.findSerializer(config, keyType, beanDesc);
/*  216 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  221 */     if (ser == null) {
/*  222 */       ser = defaultImpl;
/*  223 */       if (ser == null) {
/*  224 */         ser = StdKeySerializers.getStdKeySerializer(config, keyType.getRawClass(), false);
/*      */         
/*  226 */         if (ser == null) {
/*  227 */           beanDesc = config.introspect(keyType);
/*  228 */           AnnotatedMethod am = beanDesc.findJsonValueMethod();
/*  229 */           if (am != null) {
/*  230 */             Class<?> rawType = am.getRawReturnType();
/*  231 */             JsonSerializer<?> delegate = StdKeySerializers.getStdKeySerializer(config, rawType, true);
/*      */             
/*  233 */             Method m = am.getAnnotated();
/*  234 */             if (config.canOverrideAccessModifiers()) {
/*  235 */               ClassUtil.checkAndFixAccess(m);
/*      */             }
/*  237 */             ser = new JsonValueSerializer(m, delegate);
/*      */           } else {
/*  239 */             ser = StdKeySerializers.getDefault();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  246 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  247 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  248 */         ser = mod.modifyKeySerializer(config, keyType, beanDesc, ser);
/*      */       }
/*      */     }
/*  251 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeSerializer createTypeSerializer(SerializationConfig config, JavaType baseType)
/*      */   {
/*  263 */     BeanDescription bean = config.introspectClassAnnotations(baseType.getRawClass());
/*  264 */     com.fasterxml.jackson.databind.introspect.AnnotatedClass ac = bean.getClassInfo();
/*  265 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/*  266 */     TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
/*      */     
/*      */ 
/*      */ 
/*  270 */     java.util.Collection<com.fasterxml.jackson.databind.jsontype.NamedType> subtypes = null;
/*  271 */     if (b == null) {
/*  272 */       b = config.getDefaultTyper(baseType);
/*      */     } else {
/*  274 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
/*      */     }
/*  276 */     if (b == null) {
/*  277 */       return null;
/*      */     }
/*  279 */     return b.buildTypeSerializer(config, baseType, subtypes);
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
/*      */   protected final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BeanDescription beanDesc, boolean staticTyping)
/*      */   {
/*  304 */     Class<?> raw = type.getRawClass();
/*  305 */     String clsName = raw.getName();
/*  306 */     JsonSerializer<?> ser = (JsonSerializer)_concrete.get(clsName);
/*  307 */     if (ser == null) {
/*  308 */       Class<? extends JsonSerializer<?>> serClass = (Class)_concreteLazy.get(clsName);
/*  309 */       if (serClass != null) {
/*      */         try {
/*  311 */           return (JsonSerializer)serClass.newInstance();
/*      */         } catch (Exception e) {
/*  313 */           throw new IllegalStateException("Failed to instantiate standard serializer (of type " + serClass.getName() + "): " + e.getMessage(), e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  318 */     return ser;
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
/*      */   protected final JsonSerializer<?> findSerializerByAnnotations(SerializerProvider prov, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  341 */     Class<?> raw = type.getRawClass();
/*      */     
/*  343 */     if (com.fasterxml.jackson.databind.JsonSerializable.class.isAssignableFrom(raw)) {
/*  344 */       return com.fasterxml.jackson.databind.ser.std.SerializableSerializer.instance;
/*      */     }
/*      */     
/*  347 */     AnnotatedMethod valueMethod = beanDesc.findJsonValueMethod();
/*  348 */     if (valueMethod != null) {
/*  349 */       Method m = valueMethod.getAnnotated();
/*  350 */       if (prov.canOverrideAccessModifiers()) {
/*  351 */         ClassUtil.checkAndFixAccess(m);
/*      */       }
/*  353 */       JsonSerializer<Object> ser = findSerializerFromAnnotation(prov, valueMethod);
/*  354 */       return new JsonValueSerializer(m, ser);
/*      */     }
/*      */     
/*  357 */     return null;
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
/*      */   protected final JsonSerializer<?> findSerializerByPrimaryType(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  372 */     Class<?> raw = type.getRawClass();
/*      */     
/*      */ 
/*  375 */     JsonSerializer<?> ser = findOptionalStdSerializer(prov, type, beanDesc, staticTyping);
/*  376 */     if (ser != null) {
/*  377 */       return ser;
/*      */     }
/*      */     
/*  380 */     if (Calendar.class.isAssignableFrom(raw)) {
/*  381 */       return CalendarSerializer.instance;
/*      */     }
/*  383 */     if (java.util.Date.class.isAssignableFrom(raw)) {
/*  384 */       return DateSerializer.instance;
/*      */     }
/*  386 */     if (Map.Entry.class.isAssignableFrom(raw))
/*      */     {
/*  388 */       JavaType[] params = prov.getTypeFactory().findTypeParameters(type, Map.Entry.class);
/*  389 */       JavaType kt; JavaType kt; JavaType vt; if ((params == null) || (params.length != 2)) { JavaType vt;
/*  390 */         kt = vt = TypeFactory.unknownType();
/*      */       } else {
/*  392 */         kt = params[0];
/*  393 */         vt = params[1];
/*      */       }
/*  395 */       return buildMapEntrySerializer(prov.getConfig(), type, beanDesc, staticTyping, kt, vt);
/*      */     }
/*  397 */     if (java.nio.ByteBuffer.class.isAssignableFrom(raw)) {
/*  398 */       return new com.fasterxml.jackson.databind.ser.std.ByteBufferSerializer();
/*      */     }
/*  400 */     if (InetAddress.class.isAssignableFrom(raw)) {
/*  401 */       return new com.fasterxml.jackson.databind.ser.std.InetAddressSerializer();
/*      */     }
/*  403 */     if (InetSocketAddress.class.isAssignableFrom(raw)) {
/*  404 */       return new com.fasterxml.jackson.databind.ser.std.InetSocketAddressSerializer();
/*      */     }
/*  406 */     if (java.util.TimeZone.class.isAssignableFrom(raw)) {
/*  407 */       return new com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer();
/*      */     }
/*  409 */     if (java.nio.charset.Charset.class.isAssignableFrom(raw)) {
/*  410 */       return ToStringSerializer.instance;
/*      */     }
/*  412 */     if (Number.class.isAssignableFrom(raw))
/*      */     {
/*  414 */       JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  415 */       if (format != null) {
/*  416 */         switch (format.getShape()) {
/*      */         case STRING: 
/*  418 */           return ToStringSerializer.instance;
/*      */         case OBJECT: 
/*      */         case ARRAY: 
/*  421 */           return null;
/*      */         }
/*      */         
/*      */       }
/*  425 */       return NumberSerializer.instance;
/*      */     }
/*  427 */     if (Enum.class.isAssignableFrom(raw)) {
/*  428 */       return buildEnumSerializer(prov.getConfig(), type, beanDesc);
/*      */     }
/*  430 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> findOptionalStdSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  442 */     return OptionalHandlerFactory.instance.findSerializer(prov.getConfig(), type, beanDesc);
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
/*      */   protected final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  456 */     Class<?> type = javaType.getRawClass();
/*      */     
/*  458 */     if (Iterator.class.isAssignableFrom(type)) {
/*  459 */       JavaType[] params = config.getTypeFactory().findTypeParameters(javaType, Iterator.class);
/*  460 */       JavaType vt = (params == null) || (params.length != 1) ? TypeFactory.unknownType() : params[0];
/*      */       
/*  462 */       return buildIteratorSerializer(config, javaType, beanDesc, staticTyping, vt);
/*      */     }
/*  464 */     if (Iterable.class.isAssignableFrom(type)) {
/*  465 */       JavaType[] params = config.getTypeFactory().findTypeParameters(javaType, Iterable.class);
/*  466 */       JavaType vt = (params == null) || (params.length != 1) ? TypeFactory.unknownType() : params[0];
/*      */       
/*  468 */       return buildIterableSerializer(config, javaType, beanDesc, staticTyping, vt);
/*      */     }
/*  470 */     if (CharSequence.class.isAssignableFrom(type)) {
/*  471 */       return ToStringSerializer.instance;
/*      */     }
/*  473 */     return null;
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
/*      */   protected JsonSerializer<Object> findSerializerFromAnnotation(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/*  488 */     Object serDef = prov.getAnnotationIntrospector().findSerializer(a);
/*  489 */     if (serDef == null) {
/*  490 */       return null;
/*      */     }
/*  492 */     JsonSerializer<Object> ser = prov.serializerInstance(a, serDef);
/*      */     
/*  494 */     return findConvertingSerializer(prov, a, ser);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> findConvertingSerializer(SerializerProvider prov, Annotated a, JsonSerializer<?> ser)
/*      */     throws JsonMappingException
/*      */   {
/*  507 */     Converter<Object, Object> conv = findConverter(prov, a);
/*  508 */     if (conv == null) {
/*  509 */       return ser;
/*      */     }
/*  511 */     JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
/*  512 */     return new com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer(conv, delegateType, ser);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Converter<Object, Object> findConverter(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/*  519 */     Object convDef = prov.getAnnotationIntrospector().findSerializationConverter(a);
/*  520 */     if (convDef == null) {
/*  521 */       return null;
/*      */     }
/*  523 */     return prov.converterInstance(a, convDef);
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
/*      */   protected JsonSerializer<?> buildContainerSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  539 */     SerializationConfig config = prov.getConfig();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  545 */     if ((!staticTyping) && (type.useStaticType()) && (
/*  546 */       (!type.isContainerType()) || (type.getContentType().getRawClass() != Object.class))) {
/*  547 */       staticTyping = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  552 */     JavaType elementType = type.getContentType();
/*  553 */     TypeSerializer elementTypeSerializer = createTypeSerializer(config, elementType);
/*      */     
/*      */ 
/*      */ 
/*  557 */     if (elementTypeSerializer != null) {
/*  558 */       staticTyping = false;
/*      */     }
/*  560 */     JsonSerializer<Object> elementValueSerializer = _findContentSerializer(prov, beanDesc.getClassInfo());
/*      */     
/*      */ 
/*  563 */     if (type.isMapLikeType()) {
/*  564 */       MapLikeType mlt = (MapLikeType)type;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  570 */       JsonSerializer<Object> keySerializer = _findKeySerializer(prov, beanDesc.getClassInfo());
/*  571 */       if (mlt.isTrueMapType()) {
/*  572 */         return buildMapSerializer(config, (MapType)mlt, beanDesc, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */       
/*      */ 
/*  576 */       for (Serializers serializers : customSerializers()) {
/*  577 */         MapLikeType mlType = (MapLikeType)type;
/*  578 */         JsonSerializer<?> ser = serializers.findMapLikeSerializer(config, mlType, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */         
/*  580 */         if (ser != null)
/*      */         {
/*  582 */           if (this._factoryConfig.hasSerializerModifiers()) {
/*  583 */             for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  584 */               ser = mod.modifyMapLikeSerializer(config, mlType, beanDesc, ser);
/*      */             }
/*      */           }
/*  587 */           return ser;
/*      */         }
/*      */       }
/*  590 */       return null;
/*      */     }
/*  592 */     if (type.isCollectionLikeType()) {
/*  593 */       CollectionLikeType clt = (CollectionLikeType)type;
/*  594 */       if (clt.isTrueCollectionType()) {
/*  595 */         return buildCollectionSerializer(config, (CollectionType)clt, beanDesc, staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */       
/*  598 */       CollectionLikeType clType = (CollectionLikeType)type;
/*      */       
/*  600 */       for (Serializers serializers : customSerializers()) {
/*  601 */         JsonSerializer<?> ser = serializers.findCollectionLikeSerializer(config, clType, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */         
/*  603 */         if (ser != null)
/*      */         {
/*  605 */           if (this._factoryConfig.hasSerializerModifiers()) {
/*  606 */             for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  607 */               ser = mod.modifyCollectionLikeSerializer(config, clType, beanDesc, ser);
/*      */             }
/*      */           }
/*  610 */           return ser;
/*      */         }
/*      */       }
/*      */       
/*  614 */       return null;
/*      */     }
/*  616 */     if (type.isArrayType()) {
/*  617 */       return buildArraySerializer(config, (ArrayType)type, beanDesc, staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */     }
/*      */     
/*  620 */     return null;
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
/*      */   protected JsonSerializer<?> buildCollectionSerializer(SerializationConfig config, CollectionType type, BeanDescription beanDesc, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
/*      */     throws JsonMappingException
/*      */   {
/*  634 */     JsonSerializer<?> ser = null;
/*      */     
/*  636 */     for (Serializers serializers : customSerializers()) {
/*  637 */       ser = serializers.findCollectionSerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  639 */       if (ser != null) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  644 */     if (ser == null)
/*      */     {
/*      */ 
/*  647 */       JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  648 */       if ((format != null) && (format.getShape() == JsonFormat.Shape.OBJECT)) {
/*  649 */         return null;
/*      */       }
/*  651 */       Class<?> raw = type.getRawClass();
/*  652 */       if (java.util.EnumSet.class.isAssignableFrom(raw))
/*      */       {
/*  654 */         JavaType enumType = type.getContentType();
/*      */         
/*  656 */         if (!enumType.isEnumType()) {
/*  657 */           enumType = null;
/*      */         }
/*  659 */         ser = buildEnumSetSerializer(enumType);
/*      */       } else {
/*  661 */         Class<?> elementRaw = type.getContentType().getRawClass();
/*  662 */         if (isIndexedList(raw)) {
/*  663 */           if (elementRaw == String.class)
/*      */           {
/*  665 */             if ((elementValueSerializer == null) || (ClassUtil.isJacksonStdImpl(elementValueSerializer))) {
/*  666 */               ser = com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer.instance;
/*      */             }
/*      */           } else {
/*  669 */             ser = buildIndexedListSerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */           }
/*      */         }
/*  672 */         else if (elementRaw == String.class)
/*      */         {
/*  674 */           if ((elementValueSerializer == null) || (ClassUtil.isJacksonStdImpl(elementValueSerializer))) {
/*  675 */             ser = com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer.instance;
/*      */           }
/*      */         }
/*  678 */         if (ser == null) {
/*  679 */           ser = buildCollectionSerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  685 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  686 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  687 */         ser = mod.modifyCollectionSerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  690 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isIndexedList(Class<?> cls)
/*      */   {
/*  701 */     return java.util.RandomAccess.class.isAssignableFrom(cls);
/*      */   }
/*      */   
/*      */   public ContainerSerializer<?> buildIndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer)
/*      */   {
/*  706 */     return new com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer(elemType, staticTyping, vts, null, valueSerializer);
/*      */   }
/*      */   
/*      */   public ContainerSerializer<?> buildCollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
/*  710 */     return new com.fasterxml.jackson.databind.ser.std.CollectionSerializer(elemType, staticTyping, vts, null, valueSerializer);
/*      */   }
/*      */   
/*      */   public JsonSerializer<?> buildEnumSetSerializer(JavaType enumType) {
/*  714 */     return new com.fasterxml.jackson.databind.ser.std.EnumSetSerializer(enumType, null);
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
/*      */   protected JsonSerializer<?> buildMapSerializer(SerializationConfig config, MapType type, BeanDescription beanDesc, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
/*      */     throws JsonMappingException
/*      */   {
/*  733 */     JsonSerializer<?> ser = null;
/*  734 */     for (Serializers serializers : customSerializers()) {
/*  735 */       ser = serializers.findMapSerializer(config, type, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  737 */       if (ser != null) break;
/*      */     }
/*  739 */     if (ser == null)
/*      */     {
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
/*  756 */       Object filterId = findFilterId(config, beanDesc);
/*  757 */       MapSerializer mapSer = MapSerializer.construct(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()), type, staticTyping, elementTypeSerializer, keySerializer, elementValueSerializer, filterId);
/*      */       
/*      */ 
/*  760 */       Object suppressableValue = findSuppressableContentValue(config, type.getContentType(), beanDesc);
/*      */       
/*  762 */       if (suppressableValue != null) {
/*  763 */         mapSer = mapSer.withContentInclusion(suppressableValue);
/*      */       }
/*  765 */       ser = mapSer;
/*      */     }
/*      */     
/*  768 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  769 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  770 */         ser = mod.modifyMapSerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  773 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object findSuppressableContentValue(SerializationConfig config, JavaType contentType, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  783 */     JsonInclude.Include incl = beanDesc.findSerializationInclusionForContent(null);
/*      */     
/*  785 */     if (incl != null) {
/*  786 */       switch (incl)
/*      */       {
/*      */       case NON_DEFAULT: 
/*  789 */         incl = JsonInclude.Include.NON_EMPTY;
/*  790 */         break;
/*      */       }
/*      */       
/*      */       
/*      */ 
/*  795 */       return incl;
/*      */     }
/*  797 */     return null;
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
/*      */   protected JsonSerializer<?> buildArraySerializer(SerializationConfig config, ArrayType type, BeanDescription beanDesc, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
/*      */     throws JsonMappingException
/*      */   {
/*  816 */     JsonSerializer<?> ser = null;
/*      */     
/*  818 */     for (Serializers serializers : customSerializers()) {
/*  819 */       ser = serializers.findArraySerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  821 */       if (ser != null) {
/*      */         break;
/*      */       }
/*      */     }
/*  825 */     if (ser == null) {
/*  826 */       Class<?> raw = type.getRawClass();
/*      */       
/*  828 */       if ((elementValueSerializer == null) || (ClassUtil.isJacksonStdImpl(elementValueSerializer))) {
/*  829 */         if (String[].class == raw) {
/*  830 */           ser = com.fasterxml.jackson.databind.ser.impl.StringArraySerializer.instance;
/*      */         }
/*      */         else {
/*  833 */           ser = com.fasterxml.jackson.databind.ser.std.StdArraySerializers.findStandardImpl(raw);
/*      */         }
/*      */       }
/*  836 */       if (ser == null) {
/*  837 */         ser = new com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  842 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  843 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  844 */         ser = mod.modifyArraySerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  847 */     return ser;
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
/*      */   protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  864 */     return new IteratorSerializer(valueType, staticTyping, createTypeSerializer(config, valueType), null);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  871 */     JavaType[] params = config.getTypeFactory().findTypeParameters(type, Iterator.class);
/*  872 */     JavaType vt = (params == null) || (params.length != 1) ? TypeFactory.unknownType() : params[0];
/*      */     
/*  874 */     return buildIteratorSerializer(config, type, beanDesc, staticTyping, vt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  885 */     return new com.fasterxml.jackson.databind.ser.std.IterableSerializer(valueType, staticTyping, createTypeSerializer(config, valueType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  894 */     JavaType[] params = config.getTypeFactory().findTypeParameters(type, Iterable.class);
/*  895 */     JavaType vt = (params == null) || (params.length != 1) ? TypeFactory.unknownType() : params[0];
/*      */     
/*  897 */     return buildIterableSerializer(config, type, beanDesc, staticTyping, vt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> buildMapEntrySerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType keyType, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  908 */     return new MapEntrySerializer(valueType, keyType, valueType, staticTyping, createTypeSerializer(config, valueType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> buildEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  921 */     JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  922 */     if ((format != null) && (format.getShape() == JsonFormat.Shape.OBJECT))
/*      */     {
/*  924 */       ((BasicBeanDescription)beanDesc).removeProperty("declaringClass");
/*      */       
/*  926 */       return null;
/*      */     }
/*      */     
/*  929 */     Class<Enum<?>> enumClass = type.getRawClass();
/*  930 */     JsonSerializer<?> ser = com.fasterxml.jackson.databind.ser.std.EnumSerializer.construct(enumClass, config, beanDesc, format);
/*      */     
/*  932 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  933 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  934 */         ser = mod.modifyEnumSerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  937 */     return ser;
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
/*      */   protected <T extends JavaType> T modifyTypeByAnnotation(SerializationConfig config, Annotated a, T type)
/*      */   {
/*  954 */     Class<?> superclass = config.getAnnotationIntrospector().findSerializationType(a);
/*  955 */     if (superclass != null) {
/*      */       try {
/*  957 */         type = type.widenBy(superclass);
/*      */       } catch (IllegalArgumentException iae) {
/*  959 */         throw new IllegalArgumentException("Failed to widen type " + type + " with concrete-type annotation (value " + superclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage());
/*      */       }
/*      */     }
/*  962 */     return modifySecondaryTypesByAnnotation(config, a, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static <T extends JavaType> T modifySecondaryTypesByAnnotation(SerializationConfig config, Annotated a, T type)
/*      */   {
/*  969 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*      */     
/*  971 */     if (type.isContainerType()) {
/*  972 */       Class<?> keyClass = intr.findSerializationKeyType(a, type.getKeyType());
/*  973 */       if (keyClass != null)
/*      */       {
/*  975 */         if (!(type instanceof MapType)) {
/*  976 */           throw new IllegalArgumentException("Illegal key-type annotation: type " + type + " is not a Map type");
/*      */         }
/*      */         try {
/*  979 */           type = ((MapType)type).widenKey(keyClass);
/*      */         } catch (IllegalArgumentException iae) {
/*  981 */           throw new IllegalArgumentException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae.getMessage());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  986 */       Class<?> cc = intr.findSerializationContentType(a, type.getContentType());
/*  987 */       if (cc != null) {
/*      */         try {
/*  989 */           type = type.widenContentsBy(cc);
/*      */         } catch (IllegalArgumentException iae) {
/*  991 */           throw new IllegalArgumentException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*  995 */     return type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _findKeySerializer(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/* 1007 */     AnnotationIntrospector intr = prov.getAnnotationIntrospector();
/* 1008 */     Object serDef = intr.findKeySerializer(a);
/* 1009 */     if (serDef != null) {
/* 1010 */       return prov.serializerInstance(a, serDef);
/*      */     }
/* 1012 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _findContentSerializer(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/* 1024 */     AnnotationIntrospector intr = prov.getAnnotationIntrospector();
/* 1025 */     Object serDef = intr.findContentSerializer(a);
/* 1026 */     if (serDef != null) {
/* 1027 */       return prov.serializerInstance(a, serDef);
/*      */     }
/* 1029 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object findFilterId(SerializationConfig config, BeanDescription beanDesc)
/*      */   {
/* 1037 */     return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
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
/*      */   protected boolean usesStaticTyping(SerializationConfig config, BeanDescription beanDesc, TypeSerializer typeSer)
/*      */   {
/* 1054 */     if (typeSer != null) {
/* 1055 */       return false;
/*      */     }
/* 1057 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 1058 */     JsonSerialize.Typing t = intr.findSerializationTyping(beanDesc.getClassInfo());
/* 1059 */     if ((t != null) && (t != JsonSerialize.Typing.DEFAULT_TYPING)) {
/* 1060 */       return t == JsonSerialize.Typing.STATIC;
/*      */     }
/* 1062 */     return config.isEnabled(com.fasterxml.jackson.databind.MapperFeature.USE_STATIC_TYPING);
/*      */   }
/*      */   
/*      */   protected Class<?> _verifyAsClass(Object src, String methodName, Class<?> noneClass)
/*      */   {
/* 1067 */     if (src == null) {
/* 1068 */       return null;
/*      */     }
/* 1070 */     if (!(src instanceof Class)) {
/* 1071 */       throw new IllegalStateException("AnnotationIntrospector." + methodName + "() returned value of type " + src.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
/*      */     }
/* 1073 */     Class<?> cls = (Class)src;
/* 1074 */     if ((cls == noneClass) || (ClassUtil.isBogusClass(cls))) {
/* 1075 */       return null;
/*      */     }
/* 1077 */     return cls;
/*      */   }
/*      */   
/*      */   public abstract SerializerFactory withConfig(SerializerFactoryConfig paramSerializerFactoryConfig);
/*      */   
/*      */   public abstract JsonSerializer<Object> createSerializer(SerializerProvider paramSerializerProvider, JavaType paramJavaType)
/*      */     throws JsonMappingException;
/*      */   
/*      */   protected abstract Iterable<Serializers> customSerializers();
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\BasicSerializerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */