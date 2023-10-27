/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonEncoding;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.SegmentedStringWriter;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.Instantiatable;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ public class ObjectWriter implements com.fasterxml.jackson.core.Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   39 */   protected static final PrettyPrinter NULL_PRETTY_PRINTER = new com.fasterxml.jackson.core.util.MinimalPrettyPrinter();
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
/*      */   protected final DefaultSerializerProvider _serializerProvider;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final com.fasterxml.jackson.databind.ser.SerializerFactory _serializerFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonFactory _generatorFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final GeneratorSettings _generatorSettings;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Prefetch _prefetch;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, JavaType rootType, PrettyPrinter pp)
/*      */   {
/*   97 */     this._config = config;
/*   98 */     this._serializerProvider = mapper._serializerProvider;
/*   99 */     this._serializerFactory = mapper._serializerFactory;
/*  100 */     this._generatorFactory = mapper._jsonFactory;
/*  101 */     this._generatorSettings = (pp == null ? GeneratorSettings.empty : new GeneratorSettings(pp, null, null, null));
/*      */     
/*      */ 
/*      */ 
/*  105 */     if ((rootType == null) || (rootType.hasRawClass(Object.class))) {
/*  106 */       this._prefetch = Prefetch.empty;
/*      */     } else {
/*  108 */       rootType = rootType.withStaticTyping();
/*  109 */       this._prefetch = _prefetchRootSerializer(config, rootType);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config)
/*      */   {
/*  118 */     this._config = config;
/*  119 */     this._serializerProvider = mapper._serializerProvider;
/*  120 */     this._serializerFactory = mapper._serializerFactory;
/*  121 */     this._generatorFactory = mapper._jsonFactory;
/*      */     
/*  123 */     this._prefetch = Prefetch.empty;
/*  124 */     this._generatorSettings = GeneratorSettings.empty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, FormatSchema s)
/*      */   {
/*  133 */     this._config = config;
/*      */     
/*  135 */     this._serializerProvider = mapper._serializerProvider;
/*  136 */     this._serializerFactory = mapper._serializerFactory;
/*  137 */     this._generatorFactory = mapper._jsonFactory;
/*      */     
/*  139 */     this._prefetch = Prefetch.empty;
/*  140 */     this._generatorSettings = (s == null ? GeneratorSettings.empty : new GeneratorSettings(null, s, null, null));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectWriter base, SerializationConfig config, GeneratorSettings genSettings, Prefetch prefetch)
/*      */   {
/*  150 */     this._config = config;
/*      */     
/*  152 */     this._serializerProvider = base._serializerProvider;
/*  153 */     this._serializerFactory = base._serializerFactory;
/*  154 */     this._generatorFactory = base._generatorFactory;
/*      */     
/*  156 */     this._generatorSettings = genSettings;
/*  157 */     this._prefetch = prefetch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectWriter base, SerializationConfig config)
/*      */   {
/*  165 */     this._config = config;
/*      */     
/*  167 */     this._serializerProvider = base._serializerProvider;
/*  168 */     this._serializerFactory = base._serializerFactory;
/*  169 */     this._generatorFactory = base._generatorFactory;
/*  170 */     this._generatorSettings = base._generatorSettings;
/*  171 */     this._prefetch = base._prefetch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectWriter base, JsonFactory f)
/*      */   {
/*  180 */     this._config = base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
/*      */     
/*      */ 
/*  183 */     this._serializerProvider = base._serializerProvider;
/*  184 */     this._serializerFactory = base._serializerFactory;
/*  185 */     this._generatorFactory = base._generatorFactory;
/*  186 */     this._generatorSettings = base._generatorSettings;
/*  187 */     this._prefetch = base._prefetch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.core.Version version()
/*      */   {
/*  196 */     return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
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
/*      */   protected ObjectWriter _new(ObjectWriter base, JsonFactory f)
/*      */   {
/*  213 */     return new ObjectWriter(base, f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _new(ObjectWriter base, SerializationConfig config)
/*      */   {
/*  222 */     return new ObjectWriter(base, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _new(GeneratorSettings genSettings, Prefetch prefetch)
/*      */   {
/*  233 */     return new ObjectWriter(this, this._config, genSettings, prefetch);
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
/*      */   protected SequenceWriter _newSequenceWriter(boolean wrapInArray, JsonGenerator gen, boolean managedInput)
/*      */     throws IOException
/*      */   {
/*  247 */     return new SequenceWriter(_serializerProvider(this._config), _configureGenerator(gen), managedInput, this._prefetch).init(wrapInArray);
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
/*      */   public ObjectWriter with(SerializationFeature feature)
/*      */   {
/*  263 */     SerializationConfig newConfig = this._config.with(feature);
/*  264 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(SerializationFeature first, SerializationFeature... other)
/*      */   {
/*  272 */     SerializationConfig newConfig = this._config.with(first, other);
/*  273 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withFeatures(SerializationFeature... features)
/*      */   {
/*  281 */     SerializationConfig newConfig = this._config.withFeatures(features);
/*  282 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(SerializationFeature feature)
/*      */   {
/*  290 */     SerializationConfig newConfig = this._config.without(feature);
/*  291 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(SerializationFeature first, SerializationFeature... other)
/*      */   {
/*  299 */     SerializationConfig newConfig = this._config.without(first, other);
/*  300 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutFeatures(SerializationFeature... features)
/*      */   {
/*  308 */     SerializationConfig newConfig = this._config.withoutFeatures(features);
/*  309 */     return newConfig == this._config ? this : _new(this, newConfig);
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
/*      */   public ObjectWriter with(JsonGenerator.Feature feature)
/*      */   {
/*  322 */     SerializationConfig newConfig = this._config.with(feature);
/*  323 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withFeatures(JsonGenerator.Feature... features)
/*      */   {
/*  330 */     SerializationConfig newConfig = this._config.withFeatures(features);
/*  331 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(JsonGenerator.Feature feature)
/*      */   {
/*  338 */     SerializationConfig newConfig = this._config.without(feature);
/*  339 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutFeatures(JsonGenerator.Feature... features)
/*      */   {
/*  346 */     SerializationConfig newConfig = this._config.withoutFeatures(features);
/*  347 */     return newConfig == this._config ? this : _new(this, newConfig);
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
/*      */   public ObjectWriter with(DateFormat df)
/*      */   {
/*  365 */     SerializationConfig newConfig = this._config.with(df);
/*  366 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withDefaultPrettyPrinter()
/*      */   {
/*  374 */     return with(new com.fasterxml.jackson.core.util.DefaultPrettyPrinter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(FilterProvider filterProvider)
/*      */   {
/*  382 */     return filterProvider == this._config.getFilterProvider() ? this : _new(this, this._config.withFilters(filterProvider));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(PrettyPrinter pp)
/*      */   {
/*  391 */     GeneratorSettings genSet = this._generatorSettings.with(pp);
/*  392 */     if (genSet == this._generatorSettings) {
/*  393 */       return this;
/*      */     }
/*  395 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withRootName(String rootName)
/*      */   {
/*  407 */     SerializationConfig newConfig = this._config.withRootName(rootName);
/*  408 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(FormatSchema schema)
/*      */   {
/*  419 */     GeneratorSettings genSet = this._generatorSettings.with(schema);
/*  420 */     if (genSet == this._generatorSettings) {
/*  421 */       return this;
/*      */     }
/*  423 */     _verifySchemaType(schema);
/*  424 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withSchema(FormatSchema schema)
/*      */   {
/*  432 */     return with(schema);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter forType(JavaType rootType)
/*      */   {
/*      */     Prefetch pf;
/*      */     
/*      */ 
/*      */ 
/*      */     Prefetch pf;
/*      */     
/*      */ 
/*      */ 
/*  448 */     if ((rootType == null) || (rootType.hasRawClass(Object.class))) {
/*  449 */       pf = Prefetch.empty;
/*      */     }
/*      */     else {
/*  452 */       rootType = rootType.withStaticTyping();
/*  453 */       pf = _prefetchRootSerializer(this._config, rootType);
/*      */     }
/*  455 */     return pf == this._prefetch ? this : _new(this._generatorSettings, pf);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter forType(Class<?> rootType)
/*      */   {
/*  466 */     if (rootType == Object.class) {
/*  467 */       return forType((JavaType)null);
/*      */     }
/*  469 */     return forType(this._config.constructType(rootType));
/*      */   }
/*      */   
/*      */   public ObjectWriter forType(TypeReference<?> rootType) {
/*  473 */     return forType(this._config.getTypeFactory().constructType(rootType.getType()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withType(JavaType rootType)
/*      */   {
/*  481 */     return forType(rootType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withType(Class<?> rootType)
/*      */   {
/*  489 */     return forType(rootType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withType(TypeReference<?> rootType)
/*      */   {
/*  497 */     return forType(rootType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withView(Class<?> view)
/*      */   {
/*  509 */     SerializationConfig newConfig = this._config.withView(view);
/*  510 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */   public ObjectWriter with(Locale l) {
/*  514 */     SerializationConfig newConfig = this._config.with(l);
/*  515 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */   public ObjectWriter with(TimeZone tz) {
/*  519 */     SerializationConfig newConfig = this._config.with(tz);
/*  520 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(com.fasterxml.jackson.core.Base64Variant b64variant)
/*      */   {
/*  530 */     SerializationConfig newConfig = this._config.with(b64variant);
/*  531 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(CharacterEscapes escapes)
/*      */   {
/*  538 */     GeneratorSettings genSet = this._generatorSettings.with(escapes);
/*  539 */     if (genSet == this._generatorSettings) {
/*  540 */       return this;
/*      */     }
/*  542 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(JsonFactory f)
/*      */   {
/*  549 */     return f == this._generatorFactory ? this : _new(this, f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(ContextAttributes attrs)
/*      */   {
/*  556 */     SerializationConfig newConfig = this._config.with(attrs);
/*  557 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withAttributes(Map<Object, Object> attrs)
/*      */   {
/*  564 */     SerializationConfig newConfig = (SerializationConfig)this._config.withAttributes(attrs);
/*  565 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withAttribute(Object key, Object value)
/*      */   {
/*  572 */     SerializationConfig newConfig = (SerializationConfig)this._config.withAttribute(key, value);
/*  573 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutAttribute(Object key)
/*      */   {
/*  580 */     SerializationConfig newConfig = (SerializationConfig)this._config.withoutAttribute(key);
/*  581 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withRootValueSeparator(String sep)
/*      */   {
/*  588 */     GeneratorSettings genSet = this._generatorSettings.withRootValueSeparator(sep);
/*  589 */     if (genSet == this._generatorSettings) {
/*  590 */       return this;
/*      */     }
/*  592 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withRootValueSeparator(SerializableString sep)
/*      */   {
/*  599 */     GeneratorSettings genSet = this._generatorSettings.withRootValueSeparator(sep);
/*  600 */     if (genSet == this._generatorSettings) {
/*  601 */       return this;
/*      */     }
/*  603 */     return _new(genSet, this._prefetch);
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
/*      */   public SequenceWriter writeValues(File out)
/*      */     throws IOException
/*      */   {
/*  626 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
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
/*      */   public SequenceWriter writeValues(JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/*  646 */     return _newSequenceWriter(false, _configureGenerator(gen), false);
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
/*      */   public SequenceWriter writeValues(Writer out)
/*      */     throws IOException
/*      */   {
/*  663 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out), true);
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
/*      */   public SequenceWriter writeValues(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  681 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
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
/*      */   public SequenceWriter writeValuesAsArray(File out)
/*      */     throws IOException
/*      */   {
/*  701 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
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
/*      */   public SequenceWriter writeValuesAsArray(JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/*  722 */     return _newSequenceWriter(true, gen, false);
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
/*      */   public SequenceWriter writeValuesAsArray(Writer out)
/*      */     throws IOException
/*      */   {
/*  741 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out), true);
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
/*      */   public SequenceWriter writeValuesAsArray(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  760 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(SerializationFeature f)
/*      */   {
/*  771 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(MapperFeature f) {
/*  775 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(JsonParser.Feature f) {
/*  779 */     return this._generatorFactory.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SerializationConfig getConfig()
/*      */   {
/*  786 */     return this._config;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonFactory getJsonFactory()
/*      */   {
/*  794 */     return this._generatorFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory getFactory()
/*      */   {
/*  801 */     return this._generatorFactory;
/*      */   }
/*      */   
/*      */   public TypeFactory getTypeFactory() {
/*  805 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasPrefetchedSerializer()
/*      */   {
/*  817 */     return this._prefetch.hasSerializer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ContextAttributes getAttributes()
/*      */   {
/*  824 */     return this._config.getAttributes();
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
/*      */   public void writeValue(JsonGenerator gen, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/*  840 */     _configureGenerator(gen);
/*  841 */     if ((this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable)))
/*      */     {
/*  843 */       _writeCloseableValue(gen, value, this._config);
/*      */     } else {
/*  845 */       if (this._prefetch.valueSerializer != null) {
/*  846 */         _serializerProvider(this._config).serializeValue(gen, value, this._prefetch.rootType, this._prefetch.valueSerializer);
/*      */       }
/*  848 */       else if (this._prefetch.typeSerializer != null) {
/*  849 */         _serializerProvider(this._config).serializePolymorphic(gen, value, this._prefetch.typeSerializer);
/*      */       } else {
/*  851 */         _serializerProvider(this._config).serializeValue(gen, value);
/*      */       }
/*  853 */       if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/*  854 */         gen.flush();
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
/*      */   public void writeValue(File resultFile, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/*  872 */     _configAndWriteValue(this._generatorFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
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
/*  889 */     _configAndWriteValue(this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), value);
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
/*  905 */     _configAndWriteValue(this._generatorFactory.createGenerator(w), value);
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
/*  921 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._generatorFactory._getBufferRecycler());
/*      */     try {
/*  923 */       _configAndWriteValue(this._generatorFactory.createGenerator(sw), value);
/*      */     } catch (JsonProcessingException e) {
/*  925 */       throw e;
/*      */     } catch (IOException e) {
/*  927 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/*  929 */     return sw.getAndClear();
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
/*  945 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._generatorFactory._getBufferRecycler());
/*      */     try {
/*  947 */       _configAndWriteValue(this._generatorFactory.createGenerator(bb, JsonEncoding.UTF8), value);
/*      */     } catch (JsonProcessingException e) {
/*  949 */       throw e;
/*      */     } catch (IOException e) {
/*  951 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/*  953 */     byte[] result = bb.toByteArray();
/*  954 */     bb.release();
/*  955 */     return result;
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
/*      */   public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/*  979 */     if (type == null) {
/*  980 */       throw new IllegalArgumentException("type must be provided");
/*      */     }
/*  982 */     _serializerProvider(this._config).acceptJsonFormatVisitor(type, visitor);
/*      */   }
/*      */   
/*      */   public boolean canSerialize(Class<?> type) {
/*  986 */     return _serializerProvider(this._config).hasSerializerFor(type, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canSerialize(Class<?> type, java.util.concurrent.atomic.AtomicReference<Throwable> cause)
/*      */   {
/*  996 */     return _serializerProvider(this._config).hasSerializerFor(type, cause);
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
/* 1010 */     return this._serializerProvider.createInstance(config, this._serializerFactory);
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
/*      */   protected void _verifySchemaType(FormatSchema schema)
/*      */   {
/* 1024 */     if ((schema != null) && 
/* 1025 */       (!this._generatorFactory.canUseSchema(schema))) {
/* 1026 */       throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._generatorFactory.getFormatName());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _configAndWriteValue(JsonGenerator gen, Object value)
/*      */     throws IOException
/*      */   {
/* 1038 */     _configureGenerator(gen);
/*      */     
/* 1040 */     if ((this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 1041 */       _writeCloseable(gen, value, this._config);
/* 1042 */       return;
/*      */     }
/* 1044 */     boolean closed = false;
/*      */     try {
/* 1046 */       if (this._prefetch.valueSerializer != null) {
/* 1047 */         _serializerProvider(this._config).serializeValue(gen, value, this._prefetch.rootType, this._prefetch.valueSerializer);
/*      */       }
/* 1049 */       else if (this._prefetch.typeSerializer != null) {
/* 1050 */         _serializerProvider(this._config).serializePolymorphic(gen, value, this._prefetch.typeSerializer);
/*      */       } else {
/* 1052 */         _serializerProvider(this._config).serializeValue(gen, value);
/*      */       }
/* 1054 */       closed = true;
/* 1055 */       gen.close(); return;
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1060 */       if (!closed)
/*      */       {
/*      */ 
/*      */ 
/* 1064 */         gen.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */         try {
/* 1066 */           gen.close();
/*      */         }
/*      */         catch (IOException ioe) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeCloseable(JsonGenerator gen, Object value, SerializationConfig cfg)
/*      */     throws IOException
/*      */   {
/* 1079 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 1081 */       if (this._prefetch.valueSerializer != null) {
/* 1082 */         _serializerProvider(cfg).serializeValue(gen, value, this._prefetch.rootType, this._prefetch.valueSerializer);
/*      */       }
/* 1084 */       else if (this._prefetch.typeSerializer != null) {
/* 1085 */         _serializerProvider(cfg).serializePolymorphic(gen, value, this._prefetch.typeSerializer);
/*      */       } else {
/* 1087 */         _serializerProvider(cfg).serializeValue(gen, value);
/*      */       }
/* 1089 */       JsonGenerator tmpGen = gen;
/* 1090 */       gen = null;
/* 1091 */       tmpGen.close();
/* 1092 */       Closeable tmpToClose = toClose;
/* 1093 */       toClose = null;
/* 1094 */       tmpToClose.close(); return;
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1099 */       if (gen != null)
/*      */       {
/*      */ 
/*      */ 
/* 1103 */         gen.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */         try {
/* 1105 */           gen.close();
/*      */         } catch (IOException ioe) {}
/*      */       }
/* 1108 */       if (toClose != null) {
/*      */         try {
/* 1110 */           toClose.close();
/*      */         }
/*      */         catch (IOException ioe) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeCloseableValue(JsonGenerator gen, Object value, SerializationConfig cfg)
/*      */     throws IOException
/*      */   {
/* 1123 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 1125 */       if (this._prefetch.valueSerializer != null) {
/* 1126 */         _serializerProvider(cfg).serializeValue(gen, value, this._prefetch.rootType, this._prefetch.valueSerializer);
/*      */       }
/* 1128 */       else if (this._prefetch.typeSerializer != null) {
/* 1129 */         _serializerProvider(cfg).serializePolymorphic(gen, value, this._prefetch.typeSerializer);
/*      */       } else {
/* 1131 */         _serializerProvider(cfg).serializeValue(gen, value);
/*      */       }
/* 1133 */       if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 1134 */         gen.flush();
/*      */       }
/* 1136 */       Closeable tmpToClose = toClose;
/* 1137 */       toClose = null;
/* 1138 */       tmpToClose.close(); return;
/*      */     } finally {
/* 1140 */       if (toClose != null) {
/*      */         try {
/* 1142 */           toClose.close();
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
/*      */   protected Prefetch _prefetchRootSerializer(SerializationConfig config, JavaType valueType)
/*      */   {
/* 1155 */     if ((valueType != null) && (this._config.isEnabled(SerializationFeature.EAGER_SERIALIZER_FETCH)))
/*      */     {
/*      */ 
/*      */       try
/*      */       {
/*      */ 
/*      */ 
/* 1162 */         JsonSerializer<Object> ser = _serializerProvider(config).findTypedValueSerializer(valueType, true, null);
/*      */         
/* 1164 */         if ((ser instanceof TypeWrappedSerializer)) {
/* 1165 */           return Prefetch.construct(valueType, ((TypeWrappedSerializer)ser).typeSerializer());
/*      */         }
/* 1167 */         return Prefetch.construct(valueType, ser);
/*      */       }
/*      */       catch (JsonProcessingException e) {}
/*      */     }
/*      */     
/*      */ 
/* 1173 */     return Prefetch.empty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected void _configureJsonGenerator(JsonGenerator gen)
/*      */   {
/* 1186 */     _configureGenerator(gen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonGenerator _configureGenerator(JsonGenerator gen)
/*      */   {
/* 1197 */     GeneratorSettings genSet = this._generatorSettings;
/* 1198 */     PrettyPrinter pp = genSet.prettyPrinter;
/* 1199 */     if (pp != null) {
/* 1200 */       if (pp == NULL_PRETTY_PRINTER) {
/* 1201 */         gen.setPrettyPrinter(null);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1206 */         if ((pp instanceof Instantiatable)) {
/* 1207 */           pp = (PrettyPrinter)((Instantiatable)pp).createInstance();
/*      */         }
/* 1209 */         gen.setPrettyPrinter(pp);
/*      */       }
/* 1211 */     } else if (this._config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
/* 1212 */       gen.useDefaultPrettyPrinter();
/*      */     }
/* 1214 */     CharacterEscapes esc = genSet.characterEscapes;
/* 1215 */     if (esc != null) {
/* 1216 */       gen.setCharacterEscapes(esc);
/*      */     }
/* 1218 */     FormatSchema sch = genSet.schema;
/* 1219 */     if (sch != null) {
/* 1220 */       gen.setSchema(sch);
/*      */     }
/* 1222 */     SerializableString sep = genSet.rootValueSeparator;
/* 1223 */     if (sep != null) {
/* 1224 */       gen.setRootValueSeparator(sep);
/*      */     }
/* 1226 */     this._config.initialize(gen);
/* 1227 */     return gen;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class GeneratorSettings
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1248 */     public static final GeneratorSettings empty = new GeneratorSettings(null, null, null, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final PrettyPrinter prettyPrinter;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final FormatSchema schema;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final CharacterEscapes characterEscapes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public final SerializableString rootValueSeparator;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public GeneratorSettings(PrettyPrinter pp, FormatSchema sch, CharacterEscapes esc, SerializableString rootSep)
/*      */     {
/* 1279 */       this.prettyPrinter = pp;
/* 1280 */       this.schema = sch;
/* 1281 */       this.characterEscapes = esc;
/* 1282 */       this.rootValueSeparator = rootSep;
/*      */     }
/*      */     
/*      */     public GeneratorSettings with(PrettyPrinter pp)
/*      */     {
/* 1287 */       if (pp == null) {
/* 1288 */         pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */       }
/* 1290 */       return pp == this.prettyPrinter ? this : new GeneratorSettings(pp, this.schema, this.characterEscapes, this.rootValueSeparator);
/*      */     }
/*      */     
/*      */     public GeneratorSettings with(FormatSchema sch)
/*      */     {
/* 1295 */       return this.schema == sch ? this : new GeneratorSettings(this.prettyPrinter, sch, this.characterEscapes, this.rootValueSeparator);
/*      */     }
/*      */     
/*      */     public GeneratorSettings with(CharacterEscapes esc)
/*      */     {
/* 1300 */       return this.characterEscapes == esc ? this : new GeneratorSettings(this.prettyPrinter, this.schema, esc, this.rootValueSeparator);
/*      */     }
/*      */     
/*      */     public GeneratorSettings withRootValueSeparator(String sep)
/*      */     {
/* 1305 */       if (sep == null) {
/* 1306 */         if (this.rootValueSeparator == null) {
/* 1307 */           return this;
/*      */         }
/* 1309 */       } else if (sep.equals(this.rootValueSeparator)) {
/* 1310 */         return this;
/*      */       }
/* 1312 */       return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep == null ? null : new com.fasterxml.jackson.core.io.SerializedString(sep));
/*      */     }
/*      */     
/*      */     public GeneratorSettings withRootValueSeparator(SerializableString sep)
/*      */     {
/* 1317 */       if (sep == null) {
/* 1318 */         if (this.rootValueSeparator == null) {
/* 1319 */           return this;
/*      */         }
/*      */       }
/* 1322 */       else if ((this.rootValueSeparator != null) && (sep.getValue().equals(this.rootValueSeparator.getValue())))
/*      */       {
/* 1324 */         return this;
/*      */       }
/*      */       
/* 1327 */       return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class Prefetch
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1343 */     public static final Prefetch empty = new Prefetch(null, null, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final JavaType rootType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public final JsonSerializer<Object> valueSerializer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public final TypeSerializer typeSerializer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Prefetch(JavaType type, JsonSerializer<Object> ser, TypeSerializer typeSer)
/*      */     {
/* 1367 */       this.rootType = type;
/* 1368 */       this.valueSerializer = ser;
/* 1369 */       this.typeSerializer = typeSer;
/*      */     }
/*      */     
/*      */     public static Prefetch construct(JavaType type, JsonSerializer<Object> ser) {
/* 1373 */       if ((type == null) && (ser == null)) {
/* 1374 */         return empty;
/*      */       }
/* 1376 */       return new Prefetch(type, ser, null);
/*      */     }
/*      */     
/*      */     public static Prefetch construct(JavaType type, TypeSerializer typeSer) {
/* 1380 */       if ((type == null) && (typeSer == null)) {
/* 1381 */         return empty;
/*      */       }
/* 1383 */       return new Prefetch(type, null, typeSer);
/*      */     }
/*      */     
/*      */     public boolean hasSerializer() {
/* 1387 */       return (this.valueSerializer != null) || (this.typeSerializer != null);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ObjectWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */