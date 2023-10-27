/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.type.ResolvedType;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import com.fasterxml.jackson.databind.deser.DataFormatReaders;
/*      */ import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.node.TreeTraversingParser;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URL;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ 
/*      */ public class ObjectReader extends ObjectCodec implements com.fasterxml.jackson.core.Versioned, java.io.Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   43 */   private static final JavaType JSON_NODE_TYPE = com.fasterxml.jackson.databind.type.SimpleType.constructUnsafe(JsonNode.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DeserializationConfig _config;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DefaultDeserializationContext _context;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonFactory _parserFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _unwrapRoot;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _valueType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonDeserializer<Object> _rootDeserializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Object _valueToUpdate;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final FormatSchema _schema;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final InjectableValues _injectableValues;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DataFormatReaders _dataFormatReaders;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
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
/*      */   protected ObjectReader(ObjectMapper mapper, DeserializationConfig config)
/*      */   {
/*  162 */     this(mapper, config, null, null, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues)
/*      */   {
/*  173 */     this._config = config;
/*  174 */     this._context = mapper._deserializationContext;
/*  175 */     this._rootDeserializers = mapper._rootDeserializers;
/*  176 */     this._parserFactory = mapper._jsonFactory;
/*  177 */     this._rootNames = mapper._rootNames;
/*  178 */     this._valueType = valueType;
/*  179 */     this._valueToUpdate = valueToUpdate;
/*  180 */     if ((valueToUpdate != null) && (valueType.isArrayType())) {
/*  181 */       throw new IllegalArgumentException("Can not update an array value");
/*      */     }
/*  183 */     this._schema = schema;
/*  184 */     this._injectableValues = injectableValues;
/*  185 */     this._unwrapRoot = config.useRootWrapping();
/*      */     
/*  187 */     this._rootDeserializer = _prefetchRootDeserializer(config, valueType);
/*  188 */     this._dataFormatReaders = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders)
/*      */   {
/*  199 */     this._config = config;
/*  200 */     this._context = base._context;
/*      */     
/*  202 */     this._rootDeserializers = base._rootDeserializers;
/*  203 */     this._parserFactory = base._parserFactory;
/*  204 */     this._rootNames = base._rootNames;
/*      */     
/*  206 */     this._valueType = valueType;
/*  207 */     this._rootDeserializer = rootDeser;
/*  208 */     this._valueToUpdate = valueToUpdate;
/*  209 */     if ((valueToUpdate != null) && (valueType.isArrayType())) {
/*  210 */       throw new IllegalArgumentException("Can not update an array value");
/*      */     }
/*  212 */     this._schema = schema;
/*  213 */     this._injectableValues = injectableValues;
/*  214 */     this._unwrapRoot = config.useRootWrapping();
/*  215 */     this._dataFormatReaders = dataFormatReaders;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectReader base, DeserializationConfig config)
/*      */   {
/*  223 */     this._config = config;
/*  224 */     this._context = base._context;
/*      */     
/*  226 */     this._rootDeserializers = base._rootDeserializers;
/*  227 */     this._parserFactory = base._parserFactory;
/*  228 */     this._rootNames = base._rootNames;
/*      */     
/*  230 */     this._valueType = base._valueType;
/*  231 */     this._rootDeserializer = base._rootDeserializer;
/*  232 */     this._valueToUpdate = base._valueToUpdate;
/*  233 */     this._schema = base._schema;
/*  234 */     this._injectableValues = base._injectableValues;
/*  235 */     this._unwrapRoot = config.useRootWrapping();
/*  236 */     this._dataFormatReaders = base._dataFormatReaders;
/*      */   }
/*      */   
/*      */ 
/*      */   protected ObjectReader(ObjectReader base, JsonFactory f)
/*      */   {
/*  242 */     this._config = base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
/*      */     
/*  244 */     this._context = base._context;
/*      */     
/*  246 */     this._rootDeserializers = base._rootDeserializers;
/*  247 */     this._parserFactory = f;
/*  248 */     this._rootNames = base._rootNames;
/*      */     
/*  250 */     this._valueType = base._valueType;
/*  251 */     this._rootDeserializer = base._rootDeserializer;
/*  252 */     this._valueToUpdate = base._valueToUpdate;
/*  253 */     this._schema = base._schema;
/*  254 */     this._injectableValues = base._injectableValues;
/*  255 */     this._unwrapRoot = base._unwrapRoot;
/*  256 */     this._dataFormatReaders = base._dataFormatReaders;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Version version()
/*      */   {
/*  265 */     return PackageVersion.VERSION;
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
/*      */   protected ObjectReader _new(ObjectReader base, JsonFactory f)
/*      */   {
/*  282 */     return new ObjectReader(base, f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _new(ObjectReader base, DeserializationConfig config)
/*      */   {
/*  291 */     return new ObjectReader(base, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _new(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders)
/*      */   {
/*  303 */     return new ObjectReader(base, config, valueType, rootDeser, valueToUpdate, schema, injectableValues, dataFormatReaders);
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
/*      */   protected <T> MappingIterator<T> _newIterator(JavaType valueType, JsonParser parser, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean parserManaged, Object valueToUpdate)
/*      */   {
/*  317 */     return new MappingIterator(valueType, parser, ctxt, deser, parserManaged, valueToUpdate);
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
/*      */   protected JsonToken _initForReading(JsonParser p)
/*      */     throws IOException
/*      */   {
/*  334 */     if (this._schema != null) {
/*  335 */       p.setSchema(this._schema);
/*      */     }
/*  337 */     this._config.initialize(p);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  343 */     JsonToken t = p.getCurrentToken();
/*  344 */     if (t == null) {
/*  345 */       t = p.nextToken();
/*  346 */       if (t == null)
/*      */       {
/*  348 */         throw JsonMappingException.from(p, "No content to map due to end-of-input");
/*      */       }
/*      */     }
/*  351 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _initForMultiRead(JsonParser p)
/*      */     throws IOException
/*      */   {
/*  364 */     if (this._schema != null) {
/*  365 */       p.setSchema(this._schema);
/*      */     }
/*  367 */     this._config.initialize(p);
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
/*      */   public ObjectReader with(DeserializationFeature feature)
/*      */   {
/*  381 */     return _with(this._config.with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(DeserializationFeature first, DeserializationFeature... other)
/*      */   {
/*  391 */     return _with(this._config.with(first, other));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withFeatures(DeserializationFeature... features)
/*      */   {
/*  399 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(DeserializationFeature feature)
/*      */   {
/*  407 */     return _with(this._config.without(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(DeserializationFeature first, DeserializationFeature... other)
/*      */   {
/*  416 */     return _with(this._config.without(first, other));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutFeatures(DeserializationFeature... features)
/*      */   {
/*  424 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader with(JsonParser.Feature feature)
/*      */   {
/*  438 */     return _with(this._config.with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withFeatures(JsonParser.Feature... features)
/*      */   {
/*  446 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(JsonParser.Feature feature)
/*      */   {
/*  454 */     return _with(this._config.without(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutFeatures(JsonParser.Feature... features)
/*      */   {
/*  462 */     return _with(this._config.withoutFeatures(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(DeserializationConfig config)
/*      */   {
/*  472 */     return _with(config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(InjectableValues injectableValues)
/*      */   {
/*  484 */     if (this._injectableValues == injectableValues) {
/*  485 */       return this;
/*      */     }
/*  487 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader with(JsonNodeFactory f)
/*      */   {
/*  501 */     return _with(this._config.with(f));
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
/*      */   public ObjectReader with(JsonFactory f)
/*      */   {
/*  516 */     if (f == this._parserFactory) {
/*  517 */       return this;
/*      */     }
/*  519 */     ObjectReader r = _new(this, f);
/*      */     
/*  521 */     if (f.getCodec() == null) {
/*  522 */       f.setCodec(r);
/*      */     }
/*  524 */     return r;
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
/*      */   public ObjectReader withRootName(String rootName)
/*      */   {
/*  537 */     return _with(this._config.withRootName(rootName));
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
/*      */   public ObjectReader with(FormatSchema schema)
/*      */   {
/*  550 */     if (this._schema == schema) {
/*  551 */       return this;
/*      */     }
/*  553 */     _verifySchemaType(schema);
/*  554 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, schema, this._injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader forType(JavaType valueType)
/*      */   {
/*  569 */     if ((valueType != null) && (valueType.equals(this._valueType))) {
/*  570 */       return this;
/*      */     }
/*  572 */     JsonDeserializer<Object> rootDeser = _prefetchRootDeserializer(this._config, valueType);
/*      */     
/*  574 */     DataFormatReaders det = this._dataFormatReaders;
/*  575 */     if (det != null) {
/*  576 */       det = det.withType(valueType);
/*      */     }
/*  578 */     return _new(this, this._config, valueType, rootDeser, this._valueToUpdate, this._schema, this._injectableValues, det);
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
/*      */   public ObjectReader forType(Class<?> valueType)
/*      */   {
/*  592 */     return forType(this._config.constructType(valueType));
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
/*      */   public ObjectReader forType(TypeReference<?> valueTypeRef)
/*      */   {
/*  605 */     return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(JavaType valueType)
/*      */   {
/*  613 */     return forType(valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(Class<?> valueType)
/*      */   {
/*  621 */     return forType(this._config.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(Type valueType)
/*      */   {
/*  629 */     return forType(this._config.getTypeFactory().constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(TypeReference<?> valueTypeRef)
/*      */   {
/*  637 */     return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
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
/*      */   public ObjectReader withValueToUpdate(Object value)
/*      */   {
/*  650 */     if (value == this._valueToUpdate) return this;
/*  651 */     if (value == null) {
/*  652 */       throw new IllegalArgumentException("cat not update null value");
/*      */     }
/*      */     
/*      */ 
/*      */     JavaType t;
/*      */     
/*      */     JavaType t;
/*      */     
/*  660 */     if (this._valueType == null) {
/*  661 */       t = this._config.constructType(value.getClass());
/*      */     } else {
/*  663 */       t = this._valueType;
/*      */     }
/*  665 */     return _new(this, this._config, t, this._rootDeserializer, value, this._schema, this._injectableValues, this._dataFormatReaders);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withView(Class<?> activeView)
/*      */   {
/*  677 */     return _with(this._config.withView(activeView));
/*      */   }
/*      */   
/*      */   public ObjectReader with(Locale l) {
/*  681 */     return _with(this._config.with(l));
/*      */   }
/*      */   
/*      */   public ObjectReader with(TimeZone tz) {
/*  685 */     return _with(this._config.with(tz));
/*      */   }
/*      */   
/*      */   public ObjectReader withHandler(DeserializationProblemHandler h) {
/*  689 */     return _with(this._config.withHandler(h));
/*      */   }
/*      */   
/*      */   public ObjectReader with(Base64Variant defaultBase64) {
/*  693 */     return _with(this._config.with(defaultBase64));
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
/*      */   public ObjectReader withFormatDetection(ObjectReader... readers)
/*      */   {
/*  719 */     return withFormatDetection(new DataFormatReaders(readers));
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
/*      */   public ObjectReader withFormatDetection(DataFormatReaders readers)
/*      */   {
/*  738 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, this._injectableValues, readers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(ContextAttributes attrs)
/*      */   {
/*  746 */     return _with(this._config.with(attrs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withAttributes(Map<Object, Object> attrs)
/*      */   {
/*  753 */     return _with((DeserializationConfig)this._config.withAttributes(attrs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withAttribute(Object key, Object value)
/*      */   {
/*  760 */     return _with((DeserializationConfig)this._config.withAttribute(key, value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutAttribute(Object key)
/*      */   {
/*  767 */     return _with((DeserializationConfig)this._config.withoutAttribute(key));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _with(DeserializationConfig newConfig)
/*      */   {
/*  777 */     if (newConfig == this._config) {
/*  778 */       return this;
/*      */     }
/*  780 */     ObjectReader r = _new(this, newConfig);
/*  781 */     if (this._dataFormatReaders != null) {
/*  782 */       r = r.withFormatDetection(this._dataFormatReaders.with(newConfig));
/*      */     }
/*  784 */     return r;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(DeserializationFeature f)
/*      */   {
/*  794 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(MapperFeature f) {
/*  798 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(JsonParser.Feature f) {
/*  802 */     return this._parserFactory.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DeserializationConfig getConfig()
/*      */   {
/*  809 */     return this._config;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory getFactory()
/*      */   {
/*  817 */     return this._parserFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonFactory getJsonFactory()
/*      */   {
/*  826 */     return this._parserFactory;
/*      */   }
/*      */   
/*      */   public TypeFactory getTypeFactory() {
/*  830 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ContextAttributes getAttributes()
/*      */   {
/*  837 */     return this._config.getAttributes();
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
/*      */   public <T> T readValue(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  860 */     return (T)_bind(jp, this._valueToUpdate);
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
/*      */   public <T> T readValue(JsonParser jp, Class<T> valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  878 */     return (T)withType(valueType).readValue(jp);
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
/*      */   public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  896 */     return (T)withType(valueTypeRef).readValue(jp);
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
/*      */   public <T> T readValue(JsonParser jp, ResolvedType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  912 */     return (T)withType((JavaType)valueType).readValue(jp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  923 */     return (T)withType(valueType).readValue(jp);
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
/*      */   public <T> Iterator<T> readValues(JsonParser jp, Class<T> valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  938 */     return withType(valueType).readValues(jp);
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
/*      */   public <T> Iterator<T> readValues(JsonParser jp, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  953 */     return withType(valueTypeRef).readValues(jp);
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
/*      */   public <T> Iterator<T> readValues(JsonParser jp, ResolvedType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  968 */     return readValues(jp, (JavaType)valueType);
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
/*      */   public <T> Iterator<T> readValues(JsonParser jp, JavaType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  982 */     return withType(valueType).readValues(jp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonNode createArrayNode()
/*      */   {
/*  993 */     return this._config.getNodeFactory().arrayNode();
/*      */   }
/*      */   
/*      */   public JsonNode createObjectNode()
/*      */   {
/*  998 */     return this._config.getNodeFactory().objectNode();
/*      */   }
/*      */   
/*      */   public JsonParser treeAsTokens(TreeNode n)
/*      */   {
/* 1003 */     return new TreeTraversingParser((JsonNode)n, this);
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
/*      */   public <T extends TreeNode> T readTree(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1022 */     return _bindAsTree(jp);
/*      */   }
/*      */   
/*      */   public void writeTree(JsonGenerator jgen, TreeNode rootNode)
/*      */   {
/* 1027 */     throw new UnsupportedOperationException();
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
/*      */   public <T> T readValue(InputStream src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1046 */     if (this._dataFormatReaders != null) {
/* 1047 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(src), false);
/*      */     }
/* 1049 */     return (T)_bindAndClose(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(Reader src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1062 */     if (this._dataFormatReaders != null) {
/* 1063 */       _reportUndetectableSource(src);
/*      */     }
/* 1065 */     return (T)_bindAndClose(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(String src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1078 */     if (this._dataFormatReaders != null) {
/* 1079 */       _reportUndetectableSource(src);
/*      */     }
/* 1081 */     return (T)_bindAndClose(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1094 */     if (this._dataFormatReaders != null) {
/* 1095 */       return (T)_detectBindAndClose(src, 0, src.length);
/*      */     }
/* 1097 */     return (T)_bindAndClose(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int length)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1110 */     if (this._dataFormatReaders != null) {
/* 1111 */       return (T)_detectBindAndClose(src, offset, length);
/*      */     }
/* 1113 */     return (T)_bindAndClose(this._parserFactory.createParser(src, offset, length), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(File src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1120 */     if (this._dataFormatReaders != null) {
/* 1121 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/* 1123 */     return (T)_bindAndClose(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(URL src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1136 */     if (this._dataFormatReaders != null) {
/* 1137 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/* 1139 */     return (T)_bindAndClose(this._parserFactory.createParser(src), this._valueToUpdate);
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
/*      */   public <T> T readValue(JsonNode src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1153 */     if (this._dataFormatReaders != null) {
/* 1154 */       _reportUndetectableSource(src);
/*      */     }
/* 1156 */     return (T)_bindAndClose(treeAsTokens(src), this._valueToUpdate);
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
/*      */   public JsonNode readTree(InputStream in)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1171 */     if (this._dataFormatReaders != null) {
/* 1172 */       return _detectBindAndCloseAsTree(in);
/*      */     }
/* 1174 */     return _bindAndCloseAsTree(this._parserFactory.createParser(in));
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
/*      */   public JsonNode readTree(Reader r)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1189 */     if (this._dataFormatReaders != null) {
/* 1190 */       _reportUndetectableSource(r);
/*      */     }
/* 1192 */     return _bindAndCloseAsTree(this._parserFactory.createParser(r));
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
/*      */   public JsonNode readTree(String json)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1207 */     if (this._dataFormatReaders != null) {
/* 1208 */       _reportUndetectableSource(json);
/*      */     }
/* 1210 */     return _bindAndCloseAsTree(this._parserFactory.createParser(json));
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser jp)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1233 */     DeserializationContext ctxt = createDeserializationContext(jp, this._config);
/*      */     
/* 1235 */     return _newIterator(this._valueType, jp, ctxt, _findRootDeserializer(ctxt, this._valueType), false, this._valueToUpdate);
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
/*      */   public <T> MappingIterator<T> readValues(InputStream src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1263 */     if (this._dataFormatReaders != null) {
/* 1264 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src), false);
/*      */     }
/* 1266 */     return _bindAndReadValues(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(Reader src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1276 */     if (this._dataFormatReaders != null) {
/* 1277 */       _reportUndetectableSource(src);
/*      */     }
/* 1279 */     JsonParser p = this._parserFactory.createParser(src);
/* 1280 */     _initForMultiRead(p);
/* 1281 */     p.nextToken();
/* 1282 */     DeserializationContext ctxt = createDeserializationContext(p, this._config);
/* 1283 */     return _newIterator(this._valueType, p, ctxt, _findRootDeserializer(ctxt, this._valueType), true, this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(String json)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1296 */     if (this._dataFormatReaders != null) {
/* 1297 */       _reportUndetectableSource(json);
/*      */     }
/* 1299 */     JsonParser p = this._parserFactory.createParser(json);
/* 1300 */     _initForMultiRead(p);
/* 1301 */     p.nextToken();
/* 1302 */     DeserializationContext ctxt = createDeserializationContext(p, this._config);
/* 1303 */     return _newIterator(this._valueType, p, ctxt, _findRootDeserializer(ctxt, this._valueType), true, this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(byte[] src, int offset, int length)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1313 */     if (this._dataFormatReaders != null) {
/* 1314 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src, offset, length), false);
/*      */     }
/* 1316 */     return _bindAndReadValues(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final <T> MappingIterator<T> readValues(byte[] src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1324 */     return readValues(src, 0, src.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(File src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1333 */     if (this._dataFormatReaders != null) {
/* 1334 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(_inputStream(src)), false);
/*      */     }
/*      */     
/* 1337 */     return _bindAndReadValues(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(URL src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1348 */     if (this._dataFormatReaders != null) {
/* 1349 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/*      */     
/* 1352 */     return _bindAndReadValues(this._parserFactory.createParser(src), this._valueToUpdate);
/*      */   }
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
/* 1365 */       return (T)readValue(treeAsTokens(n), valueType);
/*      */     } catch (JsonProcessingException e) {
/* 1367 */       throw e;
/*      */     } catch (IOException e) {
/* 1369 */       throw new IllegalArgumentException(e.getMessage(), e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeValue(JsonGenerator jgen, Object value) throws IOException, JsonProcessingException
/*      */   {
/* 1375 */     throw new UnsupportedOperationException("Not implemented for ObjectReader");
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
/*      */   protected Object _bind(JsonParser jp, Object valueToUpdate)
/*      */     throws IOException
/*      */   {
/* 1393 */     JsonToken t = _initForReading(jp);
/* 1394 */     Object result; Object result; if (t == JsonToken.VALUE_NULL) { Object result;
/* 1395 */       if (valueToUpdate == null) {
/* 1396 */         DeserializationContext ctxt = createDeserializationContext(jp, this._config);
/* 1397 */         result = _findRootDeserializer(ctxt, this._valueType).getNullValue();
/*      */       } else {
/* 1399 */         result = valueToUpdate;
/*      */       } } else { Object result;
/* 1401 */       if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 1402 */         result = valueToUpdate;
/*      */       } else {
/* 1404 */         DeserializationContext ctxt = createDeserializationContext(jp, this._config);
/* 1405 */         JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, this._valueType);
/* 1406 */         Object result; if (this._unwrapRoot) {
/* 1407 */           result = _unwrapAndDeserialize(jp, ctxt, this._valueType, deser);
/*      */         } else { Object result;
/* 1409 */           if (valueToUpdate == null) {
/* 1410 */             result = deser.deserialize(jp, ctxt);
/*      */           } else {
/* 1412 */             deser.deserialize(jp, ctxt, valueToUpdate);
/* 1413 */             result = valueToUpdate;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1418 */     jp.clearCurrentToken();
/* 1419 */     return result;
/*      */   }
/*      */   
/*      */   protected Object _bindAndClose(JsonParser jp, Object valueToUpdate) throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1426 */       JsonToken t = _initForReading(jp);
/* 1427 */       Object result; DeserializationContext ctxt; Object result; if (t == JsonToken.VALUE_NULL) { Object result;
/* 1428 */         if (valueToUpdate == null) {
/* 1429 */           DeserializationContext ctxt = createDeserializationContext(jp, this._config);
/* 1430 */           result = _findRootDeserializer(ctxt, this._valueType).getNullValue();
/*      */         } else {
/* 1432 */           result = valueToUpdate;
/*      */         } } else { Object result;
/* 1434 */         if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 1435 */           result = valueToUpdate;
/*      */         } else {
/* 1437 */           ctxt = createDeserializationContext(jp, this._config);
/* 1438 */           JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, this._valueType);
/* 1439 */           Object result; if (this._unwrapRoot) {
/* 1440 */             result = _unwrapAndDeserialize(jp, ctxt, this._valueType, deser);
/*      */           } else { Object result;
/* 1442 */             if (valueToUpdate == null) {
/* 1443 */               result = deser.deserialize(jp, ctxt);
/*      */             } else {
/* 1445 */               deser.deserialize(jp, ctxt, valueToUpdate);
/* 1446 */               result = valueToUpdate;
/*      */             }
/*      */           }
/*      */         } }
/* 1450 */       return (DeserializationContext)result;
/*      */     } finally {
/*      */       try {
/* 1453 */         jp.close();
/*      */       } catch (IOException ioe) {}
/*      */     }
/*      */   }
/*      */   
/*      */   protected JsonNode _bindAndCloseAsTree(JsonParser jp) throws IOException {
/*      */     try {
/* 1460 */       return _bindAsTree(jp);
/*      */     } finally {
/*      */       try {
/* 1463 */         jp.close();
/*      */       }
/*      */       catch (IOException ioe) {}
/*      */     }
/*      */   }
/*      */   
/*      */   protected JsonNode _bindAsTree(JsonParser jp) throws IOException
/*      */   {
/* 1471 */     JsonToken t = _initForReading(jp);
/* 1472 */     JsonNode result; JsonNode result; if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 1473 */       result = com.fasterxml.jackson.databind.node.NullNode.instance;
/*      */     } else {
/* 1475 */       DeserializationContext ctxt = createDeserializationContext(jp, this._config);
/* 1476 */       JsonDeserializer<Object> deser = _findTreeDeserializer(ctxt);
/* 1477 */       JsonNode result; if (this._unwrapRoot) {
/* 1478 */         result = (JsonNode)_unwrapAndDeserialize(jp, ctxt, JSON_NODE_TYPE, deser);
/*      */       } else {
/* 1480 */         result = (JsonNode)deser.deserialize(jp, ctxt);
/*      */       }
/*      */     }
/*      */     
/* 1484 */     jp.clearCurrentToken();
/* 1485 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected <T> MappingIterator<T> _bindAndReadValues(JsonParser p, Object valueToUpdate)
/*      */     throws IOException
/*      */   {
/* 1493 */     _initForMultiRead(p);
/* 1494 */     p.nextToken();
/* 1495 */     DeserializationContext ctxt = createDeserializationContext(p, this._config);
/* 1496 */     return _newIterator(this._valueType, p, ctxt, _findRootDeserializer(ctxt, this._valueType), true, this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Object _unwrapAndDeserialize(JsonParser jp, DeserializationContext ctxt, JavaType rootType, JsonDeserializer<Object> deser)
/*      */     throws IOException
/*      */   {
/* 1503 */     String expName = this._config.getRootName();
/* 1504 */     if (expName == null) {
/* 1505 */       PropertyName pname = this._rootNames.findRootName(rootType, this._config);
/* 1506 */       expName = pname.getSimpleName();
/*      */     }
/* 1508 */     if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
/* 1509 */       throw JsonMappingException.from(jp, "Current token not START_OBJECT (needed to unwrap root name '" + expName + "'), but " + jp.getCurrentToken());
/*      */     }
/*      */     
/* 1512 */     if (jp.nextToken() != JsonToken.FIELD_NAME) {
/* 1513 */       throw JsonMappingException.from(jp, "Current token not FIELD_NAME (to contain expected root name '" + expName + "'), but " + jp.getCurrentToken());
/*      */     }
/*      */     
/* 1516 */     String actualName = jp.getCurrentName();
/* 1517 */     if (!expName.equals(actualName)) {
/* 1518 */       throw JsonMappingException.from(jp, "Root name '" + actualName + "' does not match expected ('" + expName + "') for type " + rootType);
/*      */     }
/*      */     
/*      */ 
/* 1522 */     jp.nextToken();
/*      */     Object result;
/* 1524 */     Object result; if (this._valueToUpdate == null) {
/* 1525 */       result = deser.deserialize(jp, ctxt);
/*      */     } else {
/* 1527 */       deser.deserialize(jp, ctxt, this._valueToUpdate);
/* 1528 */       result = this._valueToUpdate;
/*      */     }
/*      */     
/* 1531 */     if (jp.nextToken() != JsonToken.END_OBJECT) {
/* 1532 */       throw JsonMappingException.from(jp, "Current token not END_OBJECT (to match wrapper object with root name '" + expName + "'), but " + jp.getCurrentToken());
/*      */     }
/*      */     
/* 1535 */     return result;
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
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/* 1551 */     if (this._rootDeserializer != null) {
/* 1552 */       return this._rootDeserializer;
/*      */     }
/*      */     
/*      */ 
/* 1556 */     if (valueType == null) {
/* 1557 */       throw new JsonMappingException("No value type configured for ObjectReader");
/*      */     }
/*      */     
/*      */ 
/* 1561 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 1562 */     if (deser != null) {
/* 1563 */       return deser;
/*      */     }
/*      */     
/* 1566 */     deser = ctxt.findRootValueDeserializer(valueType);
/* 1567 */     if (deser == null) {
/* 1568 */       throw new JsonMappingException("Can not find a deserializer for type " + valueType);
/*      */     }
/* 1570 */     this._rootDeserializers.put(valueType, deser);
/* 1571 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _findTreeDeserializer(DeserializationContext ctxt)
/*      */     throws JsonMappingException
/*      */   {
/* 1580 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(JSON_NODE_TYPE);
/* 1581 */     if (deser == null)
/*      */     {
/* 1583 */       deser = ctxt.findRootValueDeserializer(JSON_NODE_TYPE);
/* 1584 */       if (deser == null) {
/* 1585 */         throw new JsonMappingException("Can not find a deserializer for type " + JSON_NODE_TYPE);
/*      */       }
/* 1587 */       this._rootDeserializers.put(JSON_NODE_TYPE, deser);
/*      */     }
/* 1589 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _prefetchRootDeserializer(DeserializationConfig config, JavaType valueType)
/*      */   {
/* 1599 */     if ((valueType == null) || (!this._config.isEnabled(DeserializationFeature.EAGER_DESERIALIZER_FETCH))) {
/* 1600 */       return null;
/*      */     }
/*      */     
/* 1603 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 1604 */     if (deser == null) {
/*      */       try
/*      */       {
/* 1607 */         DeserializationContext ctxt = createDeserializationContext(null, this._config);
/* 1608 */         deser = ctxt.findRootValueDeserializer(valueType);
/* 1609 */         if (deser != null) {
/* 1610 */           this._rootDeserializers.put(valueType, deser);
/*      */         }
/* 1612 */         return deser;
/*      */       }
/*      */       catch (JsonProcessingException e) {}
/*      */     }
/*      */     
/*      */ 
/* 1618 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _detectBindAndClose(byte[] src, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1630 */     DataFormatReaders.Match match = this._dataFormatReaders.findFormat(src, offset, length);
/* 1631 */     if (!match.hasMatch()) {
/* 1632 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1634 */     JsonParser jp = match.createParserWithMatch();
/* 1635 */     return match.getReader()._bindAndClose(jp, this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Object _detectBindAndClose(DataFormatReaders.Match match, boolean forceClosing)
/*      */     throws IOException
/*      */   {
/* 1642 */     if (!match.hasMatch()) {
/* 1643 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1645 */     JsonParser p = match.createParserWithMatch();
/*      */     
/*      */ 
/* 1648 */     if (forceClosing) {
/* 1649 */       p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/*      */     }
/*      */     
/* 1652 */     return match.getReader()._bindAndClose(p, this._valueToUpdate);
/*      */   }
/*      */   
/*      */ 
/*      */   protected <T> MappingIterator<T> _detectBindAndReadValues(DataFormatReaders.Match match, boolean forceClosing)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1659 */     if (!match.hasMatch()) {
/* 1660 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1662 */     JsonParser p = match.createParserWithMatch();
/*      */     
/*      */ 
/* 1665 */     if (forceClosing) {
/* 1666 */       p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/*      */     }
/*      */     
/* 1669 */     return match.getReader()._bindAndReadValues(p, this._valueToUpdate);
/*      */   }
/*      */   
/*      */   protected JsonNode _detectBindAndCloseAsTree(InputStream in)
/*      */     throws IOException
/*      */   {
/* 1675 */     DataFormatReaders.Match match = this._dataFormatReaders.findFormat(in);
/* 1676 */     if (!match.hasMatch()) {
/* 1677 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1679 */     JsonParser p = match.createParserWithMatch();
/* 1680 */     p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/* 1681 */     return match.getReader()._bindAndCloseAsTree(p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportUnkownFormat(DataFormatReaders detector, DataFormatReaders.Match match)
/*      */     throws JsonProcessingException
/*      */   {
/* 1690 */     throw new JsonParseException("Can not detect format from input, does not look like any of detectable formats " + detector.toString(), JsonLocation.NA);
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
/*      */   protected void _verifySchemaType(FormatSchema schema)
/*      */   {
/* 1706 */     if ((schema != null) && 
/* 1707 */       (!this._parserFactory.canUseSchema(schema))) {
/* 1708 */       throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._parserFactory.getFormatName());
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
/*      */   protected DefaultDeserializationContext createDeserializationContext(JsonParser jp, DeserializationConfig cfg)
/*      */   {
/* 1722 */     return this._context.createInstance(cfg, jp, this._injectableValues);
/*      */   }
/*      */   
/*      */   protected void _reportUndetectableSource(Object src) throws JsonProcessingException
/*      */   {
/* 1727 */     throw new JsonParseException("Can not use source of type " + src.getClass().getName() + " with format auto-detection: must be byte- not char-based", JsonLocation.NA);
/*      */   }
/*      */   
/*      */   protected InputStream _inputStream(URL src)
/*      */     throws IOException
/*      */   {
/* 1733 */     return src.openStream();
/*      */   }
/*      */   
/*      */   protected InputStream _inputStream(File f) throws IOException {
/* 1737 */     return new java.io.FileInputStream(f);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ObjectReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */