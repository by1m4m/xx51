/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*      */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*      */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonEncoding;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonFactory.Feature;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.SegmentedStringWriter;
/*      */ import com.fasterxml.jackson.core.type.ResolvedType;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*      */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*      */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*      */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.node.NullNode;
/*      */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.Serializers;
/*      */ import com.fasterxml.jackson.databind.type.ClassKey;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeModifier;
/*      */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URL;
/*      */ import java.text.DateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.ServiceLoader;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ public class ObjectMapper extends ObjectCodec implements com.fasterxml.jackson.core.Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*      */   public static enum DefaultTyping
/*      */   {
/*   87 */     JAVA_LANG_OBJECT, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   97 */     OBJECT_AND_NON_CONCRETE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */     NON_CONCRETE_AND_ARRAYS, 
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
/*  117 */     NON_FINAL;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private DefaultTyping() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DefaultTypeResolverBuilder
/*      */     extends StdTypeResolverBuilder
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */     protected final ObjectMapper.DefaultTyping _appliesFor;
/*      */     
/*      */ 
/*      */ 
/*      */     public DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping t)
/*      */     {
/*  142 */       this._appliesFor = t;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public com.fasterxml.jackson.databind.jsontype.TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*      */     {
/*  149 */       return useForType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes) : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public com.fasterxml.jackson.databind.jsontype.TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*      */     {
/*  156 */       return useForType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes) : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean useForType(JavaType t)
/*      */     {
/*  169 */       switch (ObjectMapper.2.$SwitchMap$com$fasterxml$jackson$databind$ObjectMapper$DefaultTyping[this._appliesFor.ordinal()]) {
/*      */       case 1: 
/*  171 */         while (t.isArrayType()) {
/*  172 */           t = t.getContentType();
/*      */         }
/*      */       
/*      */       case 2: 
/*  176 */         return (t.getRawClass() == Object.class) || (!t.isConcrete()) || (TreeNode.class.isAssignableFrom(t.getRawClass()));
/*      */       
/*      */ 
/*      */       case 3: 
/*  180 */         while (t.isArrayType()) {
/*  181 */           t = t.getContentType();
/*      */         }
/*      */         
/*  184 */         return (!t.isFinal()) && (!TreeNode.class.isAssignableFrom(t.getRawClass()));
/*      */       }
/*      */       
/*  187 */       return t.getRawClass() == Object.class;
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
/*  199 */   private static final JavaType JSON_NODE_TYPE = com.fasterxml.jackson.databind.type.SimpleType.constructUnsafe(JsonNode.class);
/*      */   
/*      */ 
/*  202 */   protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector();
/*      */   
/*  204 */   protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER = com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std.defaultInstance();
/*      */   
/*  206 */   protected static final PrettyPrinter _defaultPrettyPrinter = new com.fasterxml.jackson.core.util.DefaultPrettyPrinter();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */   protected static final BaseSettings DEFAULT_BASE = new BaseSettings(null, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, TypeFactory.defaultInstance(), null, com.fasterxml.jackson.databind.util.StdDateFormat.instance, null, Locale.getDefault(), TimeZone.getTimeZone("GMT"), com.fasterxml.jackson.core.Base64Variants.getDefaultVariant());
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
/*      */   protected final JsonFactory _jsonFactory;
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
/*      */   protected TypeFactory _typeFactory;
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
/*      */   protected InjectableValues _injectableValues;
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
/*      */   protected SubtypeResolver _subtypeResolver;
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
/*      */   protected final RootNameLookup _rootNames;
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
/*      */   protected final HashMap<ClassKey, Class<?>> _mixInAnnotations;
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
/*      */   protected SerializationConfig _serializationConfig;
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
/*      */   protected DefaultSerializerProvider _serializerProvider;
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
/*      */   protected SerializerFactory _serializerFactory;
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
/*      */   protected DeserializationConfig _deserializationConfig;
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
/*      */   protected DefaultDeserializationContext _deserializationContext;
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
/*      */   protected Set<Object> _registeredModuleTypes;
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
/*  369 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers = new ConcurrentHashMap(64, 0.6F, 2);
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
/*      */   public ObjectMapper()
/*      */   {
/*  391 */     this(null, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper(JsonFactory jf)
/*      */   {
/*  400 */     this(jf, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectMapper(ObjectMapper src)
/*      */   {
/*  410 */     this._jsonFactory = src._jsonFactory.copy();
/*  411 */     this._jsonFactory.setCodec(this);
/*  412 */     this._subtypeResolver = src._subtypeResolver;
/*  413 */     this._rootNames = new RootNameLookup();
/*  414 */     this._typeFactory = src._typeFactory;
/*  415 */     this._injectableValues = src._injectableValues;
/*      */     
/*  417 */     HashMap<ClassKey, Class<?>> mixins = new HashMap(src._mixInAnnotations);
/*  418 */     this._mixInAnnotations = mixins;
/*  419 */     this._serializationConfig = new SerializationConfig(src._serializationConfig, mixins);
/*  420 */     this._deserializationConfig = new DeserializationConfig(src._deserializationConfig, mixins);
/*  421 */     this._serializerProvider = src._serializerProvider.copy();
/*  422 */     this._deserializationContext = src._deserializationContext.copy();
/*      */     
/*      */ 
/*  425 */     this._serializerFactory = src._serializerFactory;
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
/*      */   public ObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc)
/*      */   {
/*  449 */     if (jf == null) {
/*  450 */       this._jsonFactory = new MappingJsonFactory(this);
/*      */     } else {
/*  452 */       this._jsonFactory = jf;
/*  453 */       if (jf.getCodec() == null) {
/*  454 */         this._jsonFactory.setCodec(this);
/*      */       }
/*      */     }
/*  457 */     this._subtypeResolver = new com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver();
/*  458 */     this._rootNames = new RootNameLookup();
/*      */     
/*  460 */     this._typeFactory = TypeFactory.defaultInstance();
/*      */     
/*  462 */     HashMap<ClassKey, Class<?>> mixins = new HashMap();
/*  463 */     this._mixInAnnotations = mixins;
/*      */     
/*  465 */     BaseSettings base = DEFAULT_BASE.withClassIntrospector(defaultClassIntrospector());
/*  466 */     this._serializationConfig = new SerializationConfig(base, this._subtypeResolver, mixins);
/*      */     
/*  468 */     this._deserializationConfig = new DeserializationConfig(base, this._subtypeResolver, mixins);
/*      */     
/*      */ 
/*      */ 
/*  472 */     boolean needOrder = this._jsonFactory.requiresPropertyOrdering();
/*  473 */     if ((needOrder ^ this._serializationConfig.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY))) {
/*  474 */       configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, needOrder);
/*      */     }
/*      */     
/*  477 */     this._serializerProvider = (sp == null ? new com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl() : sp);
/*  478 */     this._deserializationContext = (dc == null ? new com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl(com.fasterxml.jackson.databind.deser.BeanDeserializerFactory.instance) : dc);
/*      */     
/*      */ 
/*      */ 
/*  482 */     this._serializerFactory = com.fasterxml.jackson.databind.ser.BeanSerializerFactory.instance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ClassIntrospector defaultClassIntrospector()
/*      */   {
/*  492 */     return new com.fasterxml.jackson.databind.introspect.BasicClassIntrospector();
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
/*      */   public ObjectMapper copy()
/*      */   {
/*  517 */     _checkInvalidCopy(ObjectMapper.class);
/*  518 */     return new ObjectMapper(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _checkInvalidCopy(Class<?> exp)
/*      */   {
/*  526 */     if (getClass() != exp) {
/*  527 */       throw new IllegalStateException("Failed copy(): " + getClass().getName() + " (version: " + version() + ") does not override copy(); it has to");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _newReader(DeserializationConfig config)
/*      */   {
/*  546 */     return new ObjectReader(this, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _newReader(DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues)
/*      */   {
/*  558 */     return new ObjectReader(this, config, valueType, valueToUpdate, schema, injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _newWriter(SerializationConfig config)
/*      */   {
/*  568 */     return new ObjectWriter(this, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _newWriter(SerializationConfig config, FormatSchema schema)
/*      */   {
/*  578 */     return new ObjectWriter(this, config, schema);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _newWriter(SerializationConfig config, JavaType rootType, PrettyPrinter pp)
/*      */   {
/*  589 */     return new ObjectWriter(this, config, rootType, pp);
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
/*      */   public Version version()
/*      */   {
/*  604 */     return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
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
/*      */   public ObjectMapper registerModule(Module module)
/*      */   {
/*  622 */     if (isEnabled(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS)) {
/*  623 */       Object typeId = module.getTypeId();
/*  624 */       if (typeId != null) {
/*  625 */         if (this._registeredModuleTypes == null) {
/*  626 */           this._registeredModuleTypes = new java.util.HashSet();
/*      */         }
/*      */         
/*  629 */         if (!this._registeredModuleTypes.add(typeId)) {
/*  630 */           return this;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  639 */     String name = module.getModuleName();
/*  640 */     if (name == null) {
/*  641 */       throw new IllegalArgumentException("Module without defined name");
/*      */     }
/*  643 */     Version version = module.version();
/*  644 */     if (version == null) {
/*  645 */       throw new IllegalArgumentException("Module without defined version");
/*      */     }
/*      */     
/*  648 */     final ObjectMapper mapper = this;
/*      */     
/*      */ 
/*  651 */     module.setupModule(new Module.SetupContext()
/*      */     {
/*      */ 
/*      */       public Version getMapperVersion()
/*      */       {
/*      */ 
/*  657 */         return ObjectMapper.this.version();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public <C extends ObjectCodec> C getOwner()
/*      */       {
/*  664 */         return mapper;
/*      */       }
/*      */       
/*      */       public TypeFactory getTypeFactory()
/*      */       {
/*  669 */         return ObjectMapper.this._typeFactory;
/*      */       }
/*      */       
/*      */       public boolean isEnabled(MapperFeature f)
/*      */       {
/*  674 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(DeserializationFeature f)
/*      */       {
/*  679 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(SerializationFeature f)
/*      */       {
/*  684 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(JsonFactory.Feature f)
/*      */       {
/*  689 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(JsonParser.Feature f)
/*      */       {
/*  694 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(JsonGenerator.Feature f)
/*      */       {
/*  699 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void addDeserializers(Deserializers d)
/*      */       {
/*  706 */         DeserializerFactory df = mapper._deserializationContext._factory.withAdditionalDeserializers(d);
/*  707 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void addKeyDeserializers(KeyDeserializers d)
/*      */       {
/*  712 */         DeserializerFactory df = mapper._deserializationContext._factory.withAdditionalKeyDeserializers(d);
/*  713 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void addBeanDeserializerModifier(BeanDeserializerModifier modifier)
/*      */       {
/*  718 */         DeserializerFactory df = mapper._deserializationContext._factory.withDeserializerModifier(modifier);
/*  719 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void addSerializers(Serializers s)
/*      */       {
/*  726 */         mapper._serializerFactory = mapper._serializerFactory.withAdditionalSerializers(s);
/*      */       }
/*      */       
/*      */       public void addKeySerializers(Serializers s)
/*      */       {
/*  731 */         mapper._serializerFactory = mapper._serializerFactory.withAdditionalKeySerializers(s);
/*      */       }
/*      */       
/*      */       public void addBeanSerializerModifier(BeanSerializerModifier modifier)
/*      */       {
/*  736 */         mapper._serializerFactory = mapper._serializerFactory.withSerializerModifier(modifier);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void addAbstractTypeResolver(AbstractTypeResolver resolver)
/*      */       {
/*  743 */         DeserializerFactory df = mapper._deserializationContext._factory.withAbstractTypeResolver(resolver);
/*  744 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void addTypeModifier(TypeModifier modifier)
/*      */       {
/*  749 */         TypeFactory f = mapper._typeFactory;
/*  750 */         f = f.withModifier(modifier);
/*  751 */         mapper.setTypeFactory(f);
/*      */       }
/*      */       
/*      */       public void addValueInstantiators(ValueInstantiators instantiators)
/*      */       {
/*  756 */         DeserializerFactory df = mapper._deserializationContext._factory.withValueInstantiators(instantiators);
/*  757 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void setClassIntrospector(ClassIntrospector ci)
/*      */       {
/*  762 */         mapper._deserializationConfig = mapper._deserializationConfig.with(ci);
/*  763 */         mapper._serializationConfig = mapper._serializationConfig.with(ci);
/*      */       }
/*      */       
/*      */       public void insertAnnotationIntrospector(AnnotationIntrospector ai)
/*      */       {
/*  768 */         mapper._deserializationConfig = mapper._deserializationConfig.withInsertedAnnotationIntrospector(ai);
/*  769 */         mapper._serializationConfig = mapper._serializationConfig.withInsertedAnnotationIntrospector(ai);
/*      */       }
/*      */       
/*      */       public void appendAnnotationIntrospector(AnnotationIntrospector ai)
/*      */       {
/*  774 */         mapper._deserializationConfig = mapper._deserializationConfig.withAppendedAnnotationIntrospector(ai);
/*  775 */         mapper._serializationConfig = mapper._serializationConfig.withAppendedAnnotationIntrospector(ai);
/*      */       }
/*      */       
/*      */       public void registerSubtypes(Class<?>... subtypes)
/*      */       {
/*  780 */         mapper.registerSubtypes(subtypes);
/*      */       }
/*      */       
/*      */       public void registerSubtypes(NamedType... subtypes)
/*      */       {
/*  785 */         mapper.registerSubtypes(subtypes);
/*      */       }
/*      */       
/*      */       public void setMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*      */       {
/*  790 */         mapper.addMixIn(target, mixinSource);
/*      */       }
/*      */       
/*      */       public void addDeserializationProblemHandler(DeserializationProblemHandler handler)
/*      */       {
/*  795 */         mapper.addHandler(handler);
/*      */       }
/*      */       
/*      */       public void setNamingStrategy(PropertyNamingStrategy naming)
/*      */       {
/*  800 */         mapper.setPropertyNamingStrategy(naming);
/*      */       }
/*  802 */     });
/*  803 */     return this;
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
/*      */   public ObjectMapper registerModules(Module... modules)
/*      */   {
/*  819 */     for (Module module : modules) {
/*  820 */       registerModule(module);
/*      */     }
/*  822 */     return this;
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
/*      */   public ObjectMapper registerModules(Iterable<Module> modules)
/*      */   {
/*  838 */     for (Module module : modules) {
/*  839 */       registerModule(module);
/*      */     }
/*  841 */     return this;
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
/*      */   public static List<Module> findModules()
/*      */   {
/*  854 */     return findModules(null);
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
/*      */   public static List<Module> findModules(ClassLoader classLoader)
/*      */   {
/*  868 */     ArrayList<Module> modules = new ArrayList();
/*  869 */     ServiceLoader<Module> loader = classLoader == null ? ServiceLoader.load(Module.class) : ServiceLoader.load(Module.class, classLoader);
/*      */     
/*  871 */     for (Module module : loader) {
/*  872 */       modules.add(module);
/*      */     }
/*  874 */     return modules;
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
/*      */   public ObjectMapper findAndRegisterModules()
/*      */   {
/*  890 */     return registerModules(findModules());
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
/*      */   public SerializationConfig getSerializationConfig()
/*      */   {
/*  908 */     return this._serializationConfig;
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
/*      */   public DeserializationConfig getDeserializationConfig()
/*      */   {
/*  921 */     return this._deserializationConfig;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DeserializationContext getDeserializationContext()
/*      */   {
/*  932 */     return this._deserializationContext;
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
/*      */   public ObjectMapper setSerializerFactory(SerializerFactory f)
/*      */   {
/*  946 */     this._serializerFactory = f;
/*  947 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerFactory getSerializerFactory()
/*      */   {
/*  958 */     return this._serializerFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setSerializerProvider(DefaultSerializerProvider p)
/*      */   {
/*  966 */     this._serializerProvider = p;
/*  967 */     return this;
/*      */   }
/*      */   
/*      */   public SerializerProvider getSerializerProvider() {
/*  971 */     return this._serializerProvider;
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
/*      */   public ObjectMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins)
/*      */   {
/*  996 */     this._mixInAnnotations.clear();
/*  997 */     if ((sourceMixins != null) && (sourceMixins.size() > 0)) {
/*  998 */       for (Map.Entry<Class<?>, Class<?>> en : sourceMixins.entrySet()) {
/*  999 */         this._mixInAnnotations.put(new ClassKey((Class)en.getKey()), en.getValue());
/*      */       }
/*      */     }
/* 1002 */     return this;
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
/*      */   public ObjectMapper addMixIn(Class<?> target, Class<?> mixinSource)
/*      */   {
/* 1019 */     this._mixInAnnotations.put(new ClassKey(target), mixinSource);
/* 1020 */     return this;
/*      */   }
/*      */   
/*      */   public Class<?> findMixInClassFor(Class<?> cls) {
/* 1024 */     return this._mixInAnnotations == null ? null : (Class)this._mixInAnnotations.get(new ClassKey(cls));
/*      */   }
/*      */   
/*      */   public int mixInCount() {
/* 1028 */     return this._mixInAnnotations == null ? 0 : this._mixInAnnotations.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins)
/*      */   {
/* 1037 */     setMixIns(sourceMixins);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public final void addMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*      */   {
/* 1045 */     addMixIn(target, mixinSource);
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
/*      */   public VisibilityChecker<?> getVisibilityChecker()
/*      */   {
/* 1060 */     return this._serializationConfig.getDefaultVisibilityChecker();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setVisibilityChecker(VisibilityChecker<?> vc)
/*      */   {
/* 1071 */     this._deserializationConfig = this._deserializationConfig.with(vc);
/* 1072 */     this._serializationConfig = this._serializationConfig.with(vc);
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
/*      */ 
/*      */ 
/*      */   public ObjectMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*      */   {
/* 1101 */     this._deserializationConfig = this._deserializationConfig.withVisibility(forMethod, visibility);
/* 1102 */     this._serializationConfig = this._serializationConfig.withVisibility(forMethod, visibility);
/* 1103 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SubtypeResolver getSubtypeResolver()
/*      */   {
/* 1110 */     return this._subtypeResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectMapper setSubtypeResolver(SubtypeResolver str)
/*      */   {
/* 1117 */     this._subtypeResolver = str;
/* 1118 */     this._deserializationConfig = this._deserializationConfig.with(str);
/* 1119 */     this._serializationConfig = this._serializationConfig.with(str);
/* 1120 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai)
/*      */   {
/* 1128 */     this._serializationConfig = this._serializationConfig.with(ai);
/* 1129 */     this._deserializationConfig = this._deserializationConfig.with(ai);
/* 1130 */     return this;
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
/*      */   public ObjectMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI, AnnotationIntrospector deserializerAI)
/*      */   {
/* 1148 */     this._serializationConfig = this._serializationConfig.with(serializerAI);
/* 1149 */     this._deserializationConfig = this._deserializationConfig.with(deserializerAI);
/* 1150 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s)
/*      */   {
/* 1157 */     this._serializationConfig = this._serializationConfig.with(s);
/* 1158 */     this._deserializationConfig = this._deserializationConfig.with(s);
/* 1159 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PropertyNamingStrategy getPropertyNamingStrategy()
/*      */   {
/* 1167 */     return this._serializationConfig.getPropertyNamingStrategy();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectMapper setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include incl)
/*      */   {
/* 1174 */     this._serializationConfig = this._serializationConfig.withSerializationInclusion(incl);
/* 1175 */     return this;
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
/*      */   public ObjectMapper enableDefaultTyping()
/*      */   {
/* 1191 */     return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping dti)
/*      */   {
/* 1201 */     return enableDefaultTyping(dti, JsonTypeInfo.As.WRAPPER_ARRAY);
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
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs)
/*      */   {
/* 1221 */     if (includeAs == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/* 1222 */       throw new IllegalArgumentException("Can not use includeAs of " + includeAs);
/*      */     }
/*      */     
/* 1225 */     TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(applicability);
/*      */     
/* 1227 */     typer = typer.init(com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS, null);
/* 1228 */     typer = typer.inclusion(includeAs);
/* 1229 */     return setDefaultTyping(typer);
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
/*      */   public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName)
/*      */   {
/* 1242 */     TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(applicability);
/*      */     
/* 1244 */     typer = typer.init(com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS, null);
/* 1245 */     typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
/* 1246 */     typer = typer.typeProperty(propertyName);
/* 1247 */     return setDefaultTyping(typer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disableDefaultTyping()
/*      */   {
/* 1257 */     return setDefaultTyping(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer)
/*      */   {
/* 1268 */     this._deserializationConfig = this._deserializationConfig.with(typer);
/* 1269 */     this._serializationConfig = this._serializationConfig.with(typer);
/* 1270 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerSubtypes(Class<?>... classes)
/*      */   {
/* 1281 */     getSubtypeResolver().registerSubtypes(classes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerSubtypes(NamedType... types)
/*      */   {
/* 1293 */     getSubtypeResolver().registerSubtypes(types);
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
/*      */   public TypeFactory getTypeFactory()
/*      */   {
/* 1306 */     return this._typeFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setTypeFactory(TypeFactory f)
/*      */   {
/* 1318 */     this._typeFactory = f;
/* 1319 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1320 */     this._serializationConfig = this._serializationConfig.with(f);
/* 1321 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructType(Type t)
/*      */   {
/* 1330 */     return this._typeFactory.constructType(t);
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
/*      */   public JsonNodeFactory getNodeFactory()
/*      */   {
/* 1350 */     return this._deserializationConfig.getNodeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setNodeFactory(JsonNodeFactory f)
/*      */   {
/* 1359 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1360 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper addHandler(DeserializationProblemHandler h)
/*      */   {
/* 1368 */     this._deserializationConfig = this._deserializationConfig.withHandler(h);
/* 1369 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper clearProblemHandlers()
/*      */   {
/* 1377 */     this._deserializationConfig = this._deserializationConfig.withNoProblemHandlers();
/* 1378 */     return this;
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
/*      */   public ObjectMapper setConfig(DeserializationConfig config)
/*      */   {
/* 1396 */     this._deserializationConfig = config;
/* 1397 */     return this;
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
/*      */   public void setFilters(FilterProvider filterProvider)
/*      */   {
/* 1418 */     this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
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
/*      */   public ObjectMapper setBase64Variant(Base64Variant v)
/*      */   {
/* 1432 */     this._serializationConfig = this._serializationConfig.with(v);
/* 1433 */     this._deserializationConfig = this._deserializationConfig.with(v);
/* 1434 */     return this;
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
/*      */   public ObjectMapper setConfig(SerializationConfig config)
/*      */   {
/* 1452 */     this._serializationConfig = config;
/* 1453 */     return this;
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
/*      */   public JsonFactory getFactory()
/*      */   {
/* 1471 */     return this._jsonFactory;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public JsonFactory getJsonFactory()
/*      */   {
/* 1478 */     return getFactory();
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
/*      */   public ObjectMapper setDateFormat(DateFormat dateFormat)
/*      */   {
/* 1492 */     this._deserializationConfig = this._deserializationConfig.with(dateFormat);
/* 1493 */     this._serializationConfig = this._serializationConfig.with(dateFormat);
/* 1494 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormat getDateFormat()
/*      */   {
/* 1502 */     return this._serializationConfig.getDateFormat();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object setHandlerInstantiator(HandlerInstantiator hi)
/*      */   {
/* 1514 */     this._deserializationConfig = this._deserializationConfig.with(hi);
/* 1515 */     this._serializationConfig = this._serializationConfig.with(hi);
/* 1516 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setInjectableValues(InjectableValues injectableValues)
/*      */   {
/* 1524 */     this._injectableValues = injectableValues;
/* 1525 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setLocale(Locale l)
/*      */   {
/* 1533 */     this._deserializationConfig = this._deserializationConfig.with(l);
/* 1534 */     this._serializationConfig = this._serializationConfig.with(l);
/* 1535 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setTimeZone(TimeZone tz)
/*      */   {
/* 1543 */     this._deserializationConfig = this._deserializationConfig.with(tz);
/* 1544 */     this._serializationConfig = this._serializationConfig.with(tz);
/* 1545 */     return this;
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
/*      */   public boolean isEnabled(MapperFeature f)
/*      */   {
/* 1559 */     return this._serializationConfig.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(MapperFeature f, boolean state)
/*      */   {
/* 1567 */     this._serializationConfig = (state ? this._serializationConfig.with(new MapperFeature[] { f }) : this._serializationConfig.without(new MapperFeature[] { f }));
/*      */     
/* 1569 */     this._deserializationConfig = (state ? this._deserializationConfig.with(new MapperFeature[] { f }) : this._deserializationConfig.without(new MapperFeature[] { f }));
/*      */     
/* 1571 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(MapperFeature... f)
/*      */   {
/* 1579 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1580 */     this._serializationConfig = this._serializationConfig.with(f);
/* 1581 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(MapperFeature... f)
/*      */   {
/* 1589 */     this._deserializationConfig = this._deserializationConfig.without(f);
/* 1590 */     this._serializationConfig = this._serializationConfig.without(f);
/* 1591 */     return this;
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
/*      */   public boolean isEnabled(SerializationFeature f)
/*      */   {
/* 1605 */     return this._serializationConfig.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(SerializationFeature f, boolean state)
/*      */   {
/* 1613 */     this._serializationConfig = (state ? this._serializationConfig.with(f) : this._serializationConfig.without(f));
/*      */     
/* 1615 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(SerializationFeature f)
/*      */   {
/* 1623 */     this._serializationConfig = this._serializationConfig.with(f);
/* 1624 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(SerializationFeature first, SerializationFeature... f)
/*      */   {
/* 1633 */     this._serializationConfig = this._serializationConfig.with(first, f);
/* 1634 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(SerializationFeature f)
/*      */   {
/* 1642 */     this._serializationConfig = this._serializationConfig.without(f);
/* 1643 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(SerializationFeature first, SerializationFeature... f)
/*      */   {
/* 1652 */     this._serializationConfig = this._serializationConfig.without(first, f);
/* 1653 */     return this;
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
/*      */   public boolean isEnabled(DeserializationFeature f)
/*      */   {
/* 1667 */     return this._deserializationConfig.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(DeserializationFeature f, boolean state)
/*      */   {
/* 1675 */     this._deserializationConfig = (state ? this._deserializationConfig.with(f) : this._deserializationConfig.without(f));
/*      */     
/* 1677 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(DeserializationFeature feature)
/*      */   {
/* 1685 */     this._deserializationConfig = this._deserializationConfig.with(feature);
/* 1686 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(DeserializationFeature first, DeserializationFeature... f)
/*      */   {
/* 1695 */     this._deserializationConfig = this._deserializationConfig.with(first, f);
/* 1696 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(DeserializationFeature feature)
/*      */   {
/* 1704 */     this._deserializationConfig = this._deserializationConfig.without(feature);
/* 1705 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(DeserializationFeature first, DeserializationFeature... f)
/*      */   {
/* 1714 */     this._deserializationConfig = this._deserializationConfig.without(first, f);
/* 1715 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonParser.Feature f)
/*      */   {
/* 1725 */     return this._deserializationConfig.isEnabled(f, this._jsonFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(JsonParser.Feature f, boolean state)
/*      */   {
/* 1736 */     this._jsonFactory.configure(f, state);
/* 1737 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(JsonParser.Feature... features)
/*      */   {
/* 1749 */     for (JsonParser.Feature f : features) {
/* 1750 */       this._jsonFactory.enable(f);
/*      */     }
/* 1752 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(JsonParser.Feature... features)
/*      */   {
/* 1764 */     for (JsonParser.Feature f : features) {
/* 1765 */       this._jsonFactory.disable(f);
/*      */     }
/* 1767 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/* 1777 */     return this._serializationConfig.isEnabled(f, this._jsonFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(JsonGenerator.Feature f, boolean state)
/*      */   {
/* 1788 */     this._jsonFactory.configure(f, state);
/* 1789 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(JsonGenerator.Feature... features)
/*      */   {
/* 1801 */     for (JsonGenerator.Feature f : features) {
/* 1802 */       this._jsonFactory.enable(f);
/*      */     }
/* 1804 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(JsonGenerator.Feature... features)
/*      */   {
/* 1816 */     for (JsonGenerator.Feature f : features) {
/* 1817 */       this._jsonFactory.disable(f);
/*      */     }
/* 1819 */     return this;
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
/*      */   public boolean isEnabled(JsonFactory.Feature f)
/*      */   {
/* 1835 */     return this._jsonFactory.isEnabled(f);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1870 */     return (T)_readValue(getDeserializationConfig(), jp, this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1894 */     return (T)_readValue(getDeserializationConfig(), jp, this._typeFactory.constructType(valueTypeRef));
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
/*      */   public final <T> T readValue(JsonParser jp, ResolvedType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1917 */     return (T)_readValue(getDeserializationConfig(), jp, (JavaType)valueType);
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
/*      */   public <T> T readValue(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 1936 */     return (T)_readValue(getDeserializationConfig(), jp, valueType);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends TreeNode> T readTree(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1970 */     DeserializationConfig cfg = getDeserializationConfig();
/* 1971 */     JsonToken t = jp.getCurrentToken();
/* 1972 */     if (t == null) {
/* 1973 */       t = jp.nextToken();
/* 1974 */       if (t == null) {
/* 1975 */         return null;
/*      */       }
/*      */     }
/* 1978 */     JsonNode n = (JsonNode)_readValue(cfg, jp, JSON_NODE_TYPE);
/* 1979 */     if (n == null) {
/* 1980 */       n = getNodeFactory().nullNode();
/*      */     }
/*      */     
/* 1983 */     T result = n;
/* 1984 */     return result;
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser jp, ResolvedType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2003 */     return readValues(jp, (JavaType)valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2012 */     DeserializationConfig config = getDeserializationConfig();
/* 2013 */     DeserializationContext ctxt = createDeserializationContext(jp, config);
/* 2014 */     JsonDeserializer<?> deser = _findRootDeserializer(ctxt, valueType);
/*      */     
/* 2016 */     return new MappingIterator(valueType, jp, ctxt, deser, false, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(JsonParser jp, Class<T> valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2027 */     return readValues(jp, this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2037 */     return readValues(jp, this._typeFactory.constructType(valueTypeRef));
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
/*      */   public JsonNode readTree(InputStream in)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2076 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(in), JSON_NODE_TYPE);
/* 2077 */     return n == null ? NullNode.instance : n;
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
/*      */ 
/*      */   public JsonNode readTree(Reader r)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2106 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(r), JSON_NODE_TYPE);
/* 2107 */     return n == null ? NullNode.instance : n;
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
/*      */ 
/*      */   public JsonNode readTree(String content)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2136 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(content), JSON_NODE_TYPE);
/* 2137 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(byte[] content)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2159 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(content), JSON_NODE_TYPE);
/* 2160 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(File file)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2186 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(file), JSON_NODE_TYPE);
/* 2187 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(URL source)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2213 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(source), JSON_NODE_TYPE);
/* 2214 */     return n == null ? NullNode.instance : n;
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
/*      */   public void writeValue(JsonGenerator jgen, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2232 */     SerializationConfig config = getSerializationConfig();
/*      */     
/* 2234 */     if (config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
/* 2235 */       jgen.useDefaultPrettyPrinter();
/*      */     }
/* 2237 */     if ((config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 2238 */       _writeCloseableValue(jgen, value, config);
/*      */     } else {
/* 2240 */       _serializerProvider(config).serializeValue(jgen, value);
/* 2241 */       if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2242 */         jgen.flush();
/*      */       }
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
/*      */   public void writeTree(JsonGenerator jgen, TreeNode rootNode)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2257 */     SerializationConfig config = getSerializationConfig();
/* 2258 */     _serializerProvider(config).serializeValue(jgen, rootNode);
/* 2259 */     if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2260 */       jgen.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeTree(JsonGenerator jgen, JsonNode rootNode)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2271 */     SerializationConfig config = getSerializationConfig();
/* 2272 */     _serializerProvider(config).serializeValue(jgen, rootNode);
/* 2273 */     if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2274 */       jgen.flush();
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
/*      */   public com.fasterxml.jackson.databind.node.ObjectNode createObjectNode()
/*      */   {
/* 2287 */     return this._deserializationConfig.getNodeFactory().objectNode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.databind.node.ArrayNode createArrayNode()
/*      */   {
/* 2299 */     return this._deserializationConfig.getNodeFactory().arrayNode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser treeAsTokens(TreeNode n)
/*      */   {
/* 2310 */     return new com.fasterxml.jackson.databind.node.TreeTraversingParser((JsonNode)n, this);
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
/*      */   public <T> T treeToValue(TreeNode n, Class<T> valueType)
/*      */     throws JsonProcessingException
/*      */   {
/*      */     try
/*      */     {
/* 2330 */       if ((valueType != Object.class) && (valueType.isAssignableFrom(n.getClass()))) {
/* 2331 */         return n;
/*      */       }
/* 2333 */       return (T)readValue(treeAsTokens(n), valueType);
/*      */     } catch (JsonProcessingException e) {
/* 2335 */       throw e;
/*      */     } catch (IOException e) {
/* 2337 */       throw new IllegalArgumentException(e.getMessage(), e);
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
/*      */   public <T extends JsonNode> T valueToTree(Object fromValue)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2363 */     if (fromValue == null) return null;
/* 2364 */     TokenBuffer buf = new TokenBuffer(this, false);
/*      */     JsonNode result;
/*      */     try {
/* 2367 */       writeValue(buf, fromValue);
/* 2368 */       JsonParser jp = buf.asParser();
/* 2369 */       result = (JsonNode)readTree(jp);
/* 2370 */       jp.close();
/*      */     } catch (IOException e) {
/* 2372 */       throw new IllegalArgumentException(e.getMessage(), e);
/*      */     }
/* 2374 */     return result;
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
/*      */   public boolean canSerialize(Class<?> type)
/*      */   {
/* 2399 */     return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause)
/*      */   {
/* 2410 */     return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, cause);
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
/*      */   public boolean canDeserialize(JavaType type)
/*      */   {
/* 2432 */     return createDeserializationContext(null, getDeserializationConfig()).hasValueDeserializerFor(type, null);
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
/*      */   public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause)
/*      */   {
/* 2445 */     return createDeserializationContext(null, getDeserializationConfig()).hasValueDeserializerFor(type, cause);
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
/*      */   public <T> T readValue(File src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2472 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(File src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2491 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
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
/*      */   public <T> T readValue(File src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2510 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public <T> T readValue(URL src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2531 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(URL src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2550 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(URL src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2557 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public <T> T readValue(String content, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2578 */     return (T)_readMapAndClose(this._jsonFactory.createParser(content), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(String content, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2597 */     return (T)_readMapAndClose(this._jsonFactory.createParser(content), this._typeFactory.constructType(valueTypeRef));
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
/*      */   public <T> T readValue(String content, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2616 */     return (T)_readMapAndClose(this._jsonFactory.createParser(content), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(Reader src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2625 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(Reader src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2632 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(Reader src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2639 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(InputStream src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2648 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(InputStream src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2655 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(InputStream src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2662 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2671 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2681 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(byte[] src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2688 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2696 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(byte[] src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2703 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2711 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), valueType);
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
/*      */   public void writeValue(File resultFile, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2728 */     _configAndWriteValue(this._jsonFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
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
/*      */   public void writeValue(OutputStream out, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2745 */     _configAndWriteValue(this._jsonFactory.createGenerator(out, JsonEncoding.UTF8), value);
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
/*      */   public void writeValue(Writer w, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2761 */     _configAndWriteValue(this._jsonFactory.createGenerator(w), value);
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
/*      */   public String writeValueAsString(Object value)
/*      */     throws JsonProcessingException
/*      */   {
/* 2777 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
/*      */     try {
/* 2779 */       _configAndWriteValue(this._jsonFactory.createGenerator(sw), value);
/*      */     } catch (JsonProcessingException e) {
/* 2781 */       throw e;
/*      */     } catch (IOException e) {
/* 2783 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/* 2785 */     return sw.getAndClear();
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
/*      */   public byte[] writeValueAsBytes(Object value)
/*      */     throws JsonProcessingException
/*      */   {
/* 2801 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
/*      */     try {
/* 2803 */       _configAndWriteValue(this._jsonFactory.createGenerator(bb, JsonEncoding.UTF8), value);
/*      */     } catch (JsonProcessingException e) {
/* 2805 */       throw e;
/*      */     } catch (IOException e) {
/* 2807 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/* 2809 */     byte[] result = bb.toByteArray();
/* 2810 */     bb.release();
/* 2811 */     return result;
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
/*      */   public <W extends ObjectWriter> W writer()
/*      */   {
/* 2827 */     return _newWriter(getSerializationConfig());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(SerializationFeature feature)
/*      */   {
/* 2837 */     return _newWriter(getSerializationConfig().with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(SerializationFeature first, SerializationFeature... other)
/*      */   {
/* 2848 */     return _newWriter(getSerializationConfig().with(first, other));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(DateFormat df)
/*      */   {
/* 2858 */     return _newWriter(getSerializationConfig().with(df));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writerWithView(Class<?> serializationView)
/*      */   {
/* 2867 */     return _newWriter(getSerializationConfig().withView(serializationView));
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
/*      */   public <W extends ObjectWriter> W writerFor(Class<?> rootType)
/*      */   {
/* 2883 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
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
/*      */   public <W extends ObjectWriter> W writerFor(TypeReference<?> rootType)
/*      */   {
/* 2901 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
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
/*      */   public <W extends ObjectWriter> W writerFor(JavaType rootType)
/*      */   {
/* 2919 */     return _newWriter(getSerializationConfig(), rootType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public <W extends ObjectWriter> W writerWithType(Class<?> rootType)
/*      */   {
/* 2928 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public <W extends ObjectWriter> W writerWithType(TypeReference<?> rootType)
/*      */   {
/* 2940 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public <W extends ObjectWriter> W writerWithType(JavaType rootType)
/*      */   {
/* 2952 */     return _newWriter(getSerializationConfig(), rootType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(PrettyPrinter pp)
/*      */   {
/* 2962 */     if (pp == null) {
/* 2963 */       pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */     }
/* 2965 */     return _newWriter(getSerializationConfig(), null, pp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writerWithDefaultPrettyPrinter()
/*      */   {
/* 2974 */     return _newWriter(getSerializationConfig(), null, _defaultPrettyPrinter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(FilterProvider filterProvider)
/*      */   {
/* 2984 */     return _newWriter(getSerializationConfig().withFilters(filterProvider));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(FormatSchema schema)
/*      */   {
/* 2996 */     _verifySchemaType(schema);
/* 2997 */     return _newWriter(getSerializationConfig(), schema);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(Base64Variant defaultBase64)
/*      */   {
/* 3008 */     return _newWriter(getSerializationConfig().with(defaultBase64));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(CharacterEscapes escapes)
/*      */   {
/* 3019 */     return _newWriter(getSerializationConfig()).with(escapes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <W extends ObjectWriter> W writer(ContextAttributes attrs)
/*      */   {
/* 3030 */     return _newWriter(getSerializationConfig().with(attrs));
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
/*      */   public <T extends ObjectReader> T reader()
/*      */   {
/* 3047 */     return _newReader(getDeserializationConfig()).with(this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(DeserializationFeature feature)
/*      */   {
/* 3059 */     return _newReader(getDeserializationConfig().with(feature));
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
/*      */   public <T extends ObjectReader> T reader(DeserializationFeature first, DeserializationFeature... other)
/*      */   {
/* 3072 */     return _newReader(getDeserializationConfig().with(first, other));
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
/*      */   public <T extends ObjectReader> T readerForUpdating(Object valueToUpdate)
/*      */   {
/* 3087 */     JavaType t = this._typeFactory.constructType(valueToUpdate.getClass());
/* 3088 */     return _newReader(getDeserializationConfig(), t, valueToUpdate, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(JavaType type)
/*      */   {
/* 3098 */     return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(Class<?> type)
/*      */   {
/* 3108 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(TypeReference<?> type)
/*      */   {
/* 3118 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(JsonNodeFactory f)
/*      */   {
/* 3128 */     return _newReader(getDeserializationConfig()).with(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(FormatSchema schema)
/*      */   {
/* 3140 */     _verifySchemaType(schema);
/* 3141 */     return _newReader(getDeserializationConfig(), null, null, schema, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(InjectableValues injectableValues)
/*      */   {
/* 3153 */     return _newReader(getDeserializationConfig(), null, null, null, injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T readerWithView(Class<?> view)
/*      */   {
/* 3163 */     return _newReader(getDeserializationConfig().withView(view));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(Base64Variant defaultBase64)
/*      */   {
/* 3174 */     return _newReader(getDeserializationConfig().with(defaultBase64));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends ObjectReader> T reader(ContextAttributes attrs)
/*      */   {
/* 3185 */     return _newReader(getDeserializationConfig().with(attrs));
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
/*      */   public <T> T convertValue(Object fromValue, Class<T> toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3211 */     if (fromValue == null) return null;
/* 3212 */     return (T)_convert(fromValue, this._typeFactory.constructType(toValueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, TypeReference<?> toValueTypeRef)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3219 */     return (T)convertValue(fromValue, this._typeFactory.constructType(toValueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, JavaType toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3227 */     if (fromValue == null) return null;
/* 3228 */     return (T)_convert(fromValue, toValueType);
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
/*      */   protected Object _convert(Object fromValue, JavaType toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3248 */     Class<?> targetType = toValueType.getRawClass();
/* 3249 */     if ((targetType != Object.class) && (!toValueType.hasGenericTypes()) && (targetType.isAssignableFrom(fromValue.getClass())))
/*      */     {
/*      */ 
/* 3252 */       return fromValue;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3258 */     TokenBuffer buf = new TokenBuffer(this, false);
/*      */     
/*      */     try
/*      */     {
/* 3262 */       SerializationConfig config = getSerializationConfig().without(SerializationFeature.WRAP_ROOT_VALUE);
/*      */       
/* 3264 */       _serializerProvider(config).serializeValue(buf, fromValue);
/*      */       
/*      */ 
/* 3267 */       JsonParser jp = buf.asParser();
/*      */       
/*      */ 
/* 3270 */       DeserializationConfig deserConfig = getDeserializationConfig();
/* 3271 */       JsonToken t = _initForReading(jp);
/* 3272 */       Object result; Object result; if (t == JsonToken.VALUE_NULL) {
/* 3273 */         DeserializationContext ctxt = createDeserializationContext(jp, deserConfig);
/* 3274 */         result = _findRootDeserializer(ctxt, toValueType).getNullValue(); } else { Object result;
/* 3275 */         if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 3276 */           result = null;
/*      */         } else {
/* 3278 */           DeserializationContext ctxt = createDeserializationContext(jp, deserConfig);
/* 3279 */           JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, toValueType);
/*      */           
/* 3281 */           result = deser.deserialize(jp, ctxt);
/*      */         } }
/* 3283 */       jp.close();
/* 3284 */       return result;
/*      */     } catch (IOException e) {
/* 3286 */       throw new IllegalArgumentException(e.getMessage(), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.databind.jsonschema.JsonSchema generateJsonSchema(Class<?> t)
/*      */     throws JsonMappingException
/*      */   {
/* 3306 */     return _serializerProvider(getSerializationConfig()).generateJsonSchema(t);
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
/*      */   public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/* 3323 */     acceptJsonFormatVisitor(this._typeFactory.constructType(type), visitor);
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
/*      */   public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/* 3341 */     if (type == null) {
/* 3342 */       throw new IllegalArgumentException("type must be provided");
/*      */     }
/* 3344 */     _serializerProvider(getSerializationConfig()).acceptJsonFormatVisitor(type, visitor);
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
/*      */   protected DefaultSerializerProvider _serializerProvider(SerializationConfig config)
/*      */   {
/* 3358 */     return this._serializerProvider.createInstance(config, this._serializerFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PrettyPrinter _defaultPrettyPrinter()
/*      */   {
/* 3367 */     return _defaultPrettyPrinter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _configAndWriteValue(JsonGenerator jgen, Object value)
/*      */     throws IOException
/*      */   {
/* 3377 */     SerializationConfig cfg = getSerializationConfig();
/* 3378 */     cfg.initialize(jgen);
/* 3379 */     if ((cfg.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 3380 */       _configAndWriteCloseable(jgen, value, cfg);
/* 3381 */       return;
/*      */     }
/* 3383 */     boolean closed = false;
/*      */     try {
/* 3385 */       _serializerProvider(cfg).serializeValue(jgen, value);
/* 3386 */       closed = true;
/* 3387 */       jgen.close(); return;
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 3392 */       if (!closed)
/*      */       {
/*      */ 
/*      */ 
/* 3396 */         jgen.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */         try {
/* 3398 */           jgen.close();
/*      */         }
/*      */         catch (IOException ioe) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void _configAndWriteValue(JsonGenerator jgen, Object value, Class<?> viewClass) throws IOException
/*      */   {
/* 3407 */     SerializationConfig cfg = getSerializationConfig().withView(viewClass);
/* 3408 */     cfg.initialize(jgen);
/*      */     
/*      */ 
/* 3411 */     if ((cfg.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 3412 */       _configAndWriteCloseable(jgen, value, cfg);
/* 3413 */       return;
/*      */     }
/* 3415 */     boolean closed = false;
/*      */     try {
/* 3417 */       _serializerProvider(cfg).serializeValue(jgen, value);
/* 3418 */       closed = true;
/* 3419 */       jgen.close(); return;
/*      */     } finally {
/* 3421 */       if (!closed)
/*      */       {
/*      */ 
/* 3424 */         jgen.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */         try {
/* 3426 */           jgen.close();
/*      */         }
/*      */         catch (IOException ioe) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _configAndWriteCloseable(JsonGenerator jgen, Object value, SerializationConfig cfg)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 3439 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 3441 */       _serializerProvider(cfg).serializeValue(jgen, value);
/* 3442 */       JsonGenerator tmpJgen = jgen;
/* 3443 */       jgen = null;
/* 3444 */       tmpJgen.close();
/* 3445 */       Closeable tmpToClose = toClose;
/* 3446 */       toClose = null;
/* 3447 */       tmpToClose.close(); return;
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 3452 */       if (jgen != null)
/*      */       {
/*      */ 
/* 3455 */         jgen.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */         try {
/* 3457 */           jgen.close();
/*      */         } catch (IOException ioe) {}
/*      */       }
/* 3460 */       if (toClose != null) {
/*      */         try {
/* 3462 */           toClose.close();
/*      */         }
/*      */         catch (IOException ioe) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeCloseableValue(JsonGenerator jgen, Object value, SerializationConfig cfg)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 3475 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 3477 */       _serializerProvider(cfg).serializeValue(jgen, value);
/* 3478 */       if (cfg.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 3479 */         jgen.flush();
/*      */       }
/* 3481 */       Closeable tmpToClose = toClose;
/* 3482 */       toClose = null;
/* 3483 */       tmpToClose.close(); return;
/*      */     } finally {
/* 3485 */       if (toClose != null) {
/*      */         try {
/* 3487 */           toClose.close();
/*      */         }
/*      */         catch (IOException ioe) {}
/*      */       }
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
/*      */ 
/*      */ 
/*      */   protected DefaultDeserializationContext createDeserializationContext(JsonParser jp, DeserializationConfig cfg)
/*      */   {
/* 3506 */     return this._deserializationContext.createInstance(cfg, jp, this._injectableValues);
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
/*      */   protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 3520 */     JsonToken t = _initForReading(jp);
/* 3521 */     Object result; Object result; if (t == JsonToken.VALUE_NULL)
/*      */     {
/* 3523 */       DeserializationContext ctxt = createDeserializationContext(jp, cfg);
/* 3524 */       result = _findRootDeserializer(ctxt, valueType).getNullValue(); } else { Object result;
/* 3525 */       if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 3526 */         result = null;
/*      */       } else {
/* 3528 */         DeserializationContext ctxt = createDeserializationContext(jp, cfg);
/* 3529 */         JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, valueType);
/*      */         Object result;
/* 3531 */         if (cfg.useRootWrapping()) {
/* 3532 */           result = _unwrapAndDeserialize(jp, ctxt, cfg, valueType, deser);
/*      */         } else {
/* 3534 */           result = deser.deserialize(jp, ctxt);
/*      */         }
/*      */       }
/*      */     }
/* 3538 */     jp.clearCurrentToken();
/* 3539 */     return result;
/*      */   }
/*      */   
/*      */   protected Object _readMapAndClose(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/*      */     try
/*      */     {
/* 3547 */       JsonToken t = _initForReading(jp);
/* 3548 */       Object result; DeserializationConfig cfg; Object result; if (t == JsonToken.VALUE_NULL)
/*      */       {
/* 3550 */         DeserializationContext ctxt = createDeserializationContext(jp, getDeserializationConfig());
/*      */         
/* 3552 */         result = _findRootDeserializer(ctxt, valueType).getNullValue(); } else { Object result;
/* 3553 */         if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 3554 */           result = null;
/*      */         } else {
/* 3556 */           cfg = getDeserializationConfig();
/* 3557 */           DeserializationContext ctxt = createDeserializationContext(jp, cfg);
/* 3558 */           JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, valueType);
/* 3559 */           Object result; if (cfg.useRootWrapping()) {
/* 3560 */             result = _unwrapAndDeserialize(jp, ctxt, cfg, valueType, deser);
/*      */           } else {
/* 3562 */             result = deser.deserialize(jp, ctxt);
/*      */           }
/* 3564 */           ctxt.checkUnresolvedObjectId();
/*      */         }
/*      */       }
/* 3567 */       jp.clearCurrentToken();
/* 3568 */       return (DeserializationConfig)result;
/*      */     } finally {
/*      */       try {
/* 3571 */         jp.close();
/*      */       }
/*      */       catch (IOException ioe) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _initForReading(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 3593 */     this._deserializationConfig.initialize(p);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3599 */     JsonToken t = p.getCurrentToken();
/* 3600 */     if (t == null)
/*      */     {
/* 3602 */       t = p.nextToken();
/* 3603 */       if (t == null)
/*      */       {
/*      */ 
/*      */ 
/* 3607 */         throw JsonMappingException.from(p, "No content to map due to end-of-input");
/*      */       }
/*      */     }
/* 3610 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, DeserializationConfig config, JavaType rootType, JsonDeserializer<Object> deser)
/*      */     throws IOException
/*      */   {
/* 3618 */     String expName = config.getRootName();
/* 3619 */     if (expName == null) {
/* 3620 */       PropertyName pname = this._rootNames.findRootName(rootType, config);
/* 3621 */       expName = pname.getSimpleName();
/*      */     }
/* 3623 */     if (p.getCurrentToken() != JsonToken.START_OBJECT) {
/* 3624 */       throw JsonMappingException.from(p, "Current token not START_OBJECT (needed to unwrap root name '" + expName + "'), but " + p.getCurrentToken());
/*      */     }
/*      */     
/* 3627 */     if (p.nextToken() != JsonToken.FIELD_NAME) {
/* 3628 */       throw JsonMappingException.from(p, "Current token not FIELD_NAME (to contain expected root name '" + expName + "'), but " + p.getCurrentToken());
/*      */     }
/*      */     
/* 3631 */     String actualName = p.getCurrentName();
/* 3632 */     if (!expName.equals(actualName)) {
/* 3633 */       throw JsonMappingException.from(p, "Root name '" + actualName + "' does not match expected ('" + expName + "') for type " + rootType);
/*      */     }
/*      */     
/*      */ 
/* 3637 */     p.nextToken();
/* 3638 */     Object result = deser.deserialize(p, ctxt);
/*      */     
/* 3640 */     if (p.nextToken() != JsonToken.END_OBJECT) {
/* 3641 */       throw JsonMappingException.from(p, "Current token not END_OBJECT (to match wrapper object with root name '" + expName + "'), but " + p.getCurrentToken());
/*      */     }
/*      */     
/* 3644 */     return result;
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
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/* 3661 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 3662 */     if (deser != null) {
/* 3663 */       return deser;
/*      */     }
/*      */     
/* 3666 */     deser = ctxt.findRootValueDeserializer(valueType);
/* 3667 */     if (deser == null) {
/* 3668 */       throw new JsonMappingException("Can not find a deserializer for type " + valueType);
/*      */     }
/* 3670 */     this._rootDeserializers.put(valueType, deser);
/* 3671 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _verifySchemaType(FormatSchema schema)
/*      */   {
/* 3679 */     if ((schema != null) && 
/* 3680 */       (!this._jsonFactory.canUseSchema(schema))) {
/* 3681 */       throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._jsonFactory.getFormatName());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ObjectMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */