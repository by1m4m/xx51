/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerCache;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*      */ import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*      */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DeserializationContext
/*      */   extends DatabindContext
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -4290063686213707727L;
/*      */   private static final int MAX_ERROR_STR_LEN = 500;
/*      */   protected final DeserializerCache _cache;
/*      */   protected final DeserializerFactory _factory;
/*      */   protected final DeserializationConfig _config;
/*      */   protected final int _featureFlags;
/*      */   protected final Class<?> _view;
/*      */   protected transient JsonParser _parser;
/*      */   protected final InjectableValues _injectableValues;
/*      */   protected transient ArrayBuilders _arrayBuilders;
/*      */   protected transient ObjectBuffer _objectBuffer;
/*      */   protected transient DateFormat _dateFormat;
/*      */   protected transient ContextAttributes _attributes;
/*      */   protected LinkedNode<JavaType> _currentType;
/*      */   
/*      */   protected DeserializationContext(DeserializerFactory df)
/*      */   {
/*  148 */     this(df, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected DeserializationContext(DeserializerFactory df, DeserializerCache cache)
/*      */   {
/*  154 */     if (df == null) {
/*  155 */       throw new IllegalArgumentException("Can not pass null DeserializerFactory");
/*      */     }
/*  157 */     this._factory = df;
/*  158 */     this._cache = (cache == null ? new DeserializerCache() : cache);
/*      */     
/*  160 */     this._featureFlags = 0;
/*  161 */     this._config = null;
/*  162 */     this._injectableValues = null;
/*  163 */     this._view = null;
/*  164 */     this._attributes = null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected DeserializationContext(DeserializationContext src, DeserializerFactory factory)
/*      */   {
/*  170 */     this._cache = src._cache;
/*  171 */     this._factory = factory;
/*      */     
/*  173 */     this._config = src._config;
/*  174 */     this._featureFlags = src._featureFlags;
/*  175 */     this._view = src._view;
/*  176 */     this._parser = src._parser;
/*  177 */     this._injectableValues = src._injectableValues;
/*  178 */     this._attributes = src._attributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DeserializationContext(DeserializationContext src, DeserializationConfig config, JsonParser p, InjectableValues injectableValues)
/*      */   {
/*  188 */     this._cache = src._cache;
/*  189 */     this._factory = src._factory;
/*      */     
/*  191 */     this._config = config;
/*  192 */     this._featureFlags = config.getDeserializationFeatures();
/*  193 */     this._view = config.getActiveView();
/*  194 */     this._parser = p;
/*  195 */     this._injectableValues = injectableValues;
/*  196 */     this._attributes = config.getAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected DeserializationContext(DeserializationContext src)
/*      */   {
/*  203 */     this._cache = new DeserializerCache();
/*  204 */     this._factory = src._factory;
/*      */     
/*  206 */     this._config = src._config;
/*  207 */     this._featureFlags = src._featureFlags;
/*  208 */     this._view = src._view;
/*  209 */     this._injectableValues = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DeserializationConfig getConfig()
/*      */   {
/*  219 */     return this._config;
/*      */   }
/*      */   
/*  222 */   public final Class<?> getActiveView() { return this._view; }
/*      */   
/*      */   public final AnnotationIntrospector getAnnotationIntrospector()
/*      */   {
/*  226 */     return this._config.getAnnotationIntrospector();
/*      */   }
/*      */   
/*      */   public final TypeFactory getTypeFactory()
/*      */   {
/*  231 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(Object key)
/*      */   {
/*  242 */     return this._attributes.getAttribute(key);
/*      */   }
/*      */   
/*      */ 
/*      */   public DeserializationContext setAttribute(Object key, Object value)
/*      */   {
/*  248 */     this._attributes = this._attributes.withPerCallAttribute(key, value);
/*  249 */     return this;
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
/*      */   public JavaType getContextualType()
/*      */   {
/*  266 */     return this._currentType == null ? null : (JavaType)this._currentType.value();
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
/*      */   public DeserializerFactory getFactory()
/*      */   {
/*  279 */     return this._factory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(DeserializationFeature feat)
/*      */   {
/*  290 */     return (this._featureFlags & feat.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean hasDeserializationFeatures(int featureMask)
/*      */   {
/*  300 */     return this._config.hasDeserializationFeatures(featureMask);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonParser getParser()
/*      */   {
/*  311 */     return this._parser;
/*      */   }
/*      */   
/*      */   public final Object findInjectableValue(Object valueId, BeanProperty forProperty, Object beanInstance)
/*      */   {
/*  316 */     if (this._injectableValues == null) {
/*  317 */       throw new IllegalStateException("No 'injectableValues' configured, can not inject value with id [" + valueId + "]");
/*      */     }
/*  319 */     return this._injectableValues.findInjectableValue(valueId, this, forProperty, beanInstance);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Base64Variant getBase64Variant()
/*      */   {
/*  331 */     return this._config.getBase64Variant();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonNodeFactory getNodeFactory()
/*      */   {
/*  341 */     return this._config.getNodeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  351 */     return this._config.getLocale();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  361 */     return this._config.getTimeZone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public boolean hasValueDeserializerFor(JavaType type)
/*      */   {
/*  372 */     return hasValueDeserializerFor(type, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasValueDeserializerFor(JavaType type, AtomicReference<Throwable> cause)
/*      */   {
/*      */     try
/*      */     {
/*  384 */       return this._cache.hasValueDeserializerFor(this, this._factory, type);
/*      */     } catch (JsonMappingException e) {
/*  386 */       if (cause != null) {
/*  387 */         cause.set(e);
/*      */       }
/*      */     } catch (RuntimeException e) {
/*  390 */       if (cause == null) {
/*  391 */         throw e;
/*      */       }
/*  393 */       cause.set(e);
/*      */     }
/*  395 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  406 */     JsonDeserializer<Object> deser = this._cache.findValueDeserializer(this, this._factory, type);
/*  407 */     if (deser != null) {
/*  408 */       deser = handleSecondaryContextualization(deser, prop, type);
/*      */     }
/*  410 */     return deser;
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
/*      */   public final JsonDeserializer<Object> findNonContextualValueDeserializer(JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  429 */     return this._cache.findValueDeserializer(this, this._factory, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonDeserializer<Object> findRootValueDeserializer(JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  439 */     JsonDeserializer<Object> deser = this._cache.findValueDeserializer(this, this._factory, type);
/*      */     
/*  441 */     if (deser == null) {
/*  442 */       return null;
/*      */     }
/*  444 */     deser = handleSecondaryContextualization(deser, null, type);
/*  445 */     TypeDeserializer typeDeser = this._factory.findTypeDeserializer(this._config, type);
/*  446 */     if (typeDeser != null)
/*      */     {
/*  448 */       typeDeser = typeDeser.forProperty(null);
/*  449 */       return new TypeWrappedDeserializer(typeDeser, deser);
/*      */     }
/*  451 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final KeyDeserializer findKeyDeserializer(JavaType keyType, BeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  462 */     KeyDeserializer kd = this._cache.findKeyDeserializer(this, this._factory, keyType);
/*      */     
/*      */ 
/*  465 */     if ((kd instanceof ContextualKeyDeserializer)) {
/*  466 */       kd = ((ContextualKeyDeserializer)kd).createContextual(this, prop);
/*      */     }
/*  468 */     return kd;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract ReadableObjectId findObjectId(Object paramObject, ObjectIdGenerator<?> paramObjectIdGenerator, ObjectIdResolver paramObjectIdResolver);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public abstract ReadableObjectId findObjectId(Object paramObject, ObjectIdGenerator<?> paramObjectIdGenerator);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void checkUnresolvedObjectId()
/*      */     throws UnresolvedForwardReference;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JavaType constructType(Class<?> cls)
/*      */   {
/*  508 */     return this._config.constructType(cls);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> findClass(String className)
/*      */     throws ClassNotFoundException
/*      */   {
/*  520 */     return ClassUtil.findClass(className);
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
/*      */   public final ObjectBuffer leaseObjectBuffer()
/*      */   {
/*  537 */     ObjectBuffer buf = this._objectBuffer;
/*  538 */     if (buf == null) {
/*  539 */       buf = new ObjectBuffer();
/*      */     } else {
/*  541 */       this._objectBuffer = null;
/*      */     }
/*  543 */     return buf;
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
/*      */   public final void returnObjectBuffer(ObjectBuffer buf)
/*      */   {
/*  557 */     if ((this._objectBuffer == null) || (buf.initialCapacity() >= this._objectBuffer.initialCapacity()))
/*      */     {
/*  559 */       this._objectBuffer = buf;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ArrayBuilders getArrayBuilders()
/*      */   {
/*  569 */     if (this._arrayBuilders == null) {
/*  570 */       this._arrayBuilders = new ArrayBuilders();
/*      */     }
/*  572 */     return this._arrayBuilders;
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
/*      */   public abstract JsonDeserializer<Object> deserializerInstance(Annotated paramAnnotated, Object paramObject)
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
/*      */   public abstract KeyDeserializer keyDeserializerInstance(Annotated paramAnnotated, Object paramObject)
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
/*      */   public JsonDeserializer<?> handlePrimaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  611 */     if ((deser instanceof ContextualDeserializer)) {
/*  612 */       this._currentType = new LinkedNode(type, this._currentType);
/*      */       try {
/*  614 */         deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */       } finally {
/*  616 */         this._currentType = this._currentType.next();
/*      */       }
/*      */     }
/*  619 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public JsonDeserializer<?> handlePrimaryContextualization(JsonDeserializer<?> deser, BeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  627 */     return handlePrimaryContextualization(deser, prop, TypeFactory.unknownType());
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
/*      */   public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  650 */     if ((deser instanceof ContextualDeserializer)) {
/*  651 */       this._currentType = new LinkedNode(type, this._currentType);
/*      */       try {
/*  653 */         deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */       } finally {
/*  655 */         this._currentType = this._currentType.next();
/*      */       }
/*      */     }
/*  658 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  666 */     if ((deser instanceof ContextualDeserializer)) {
/*  667 */       deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */     }
/*  669 */     return deser;
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
/*      */   public Date parseDate(String dateStr)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/*  691 */       DateFormat df = getDateFormat();
/*  692 */       return df.parse(dateStr);
/*      */     } catch (ParseException e) {
/*  694 */       throw new IllegalArgumentException("Failed to parse Date value '" + dateStr + "': " + e.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar constructCalendar(Date d)
/*      */   {
/*  704 */     Calendar c = Calendar.getInstance(getTimeZone());
/*  705 */     c.setTime(d);
/*  706 */     return c;
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
/*      */   public <T> T readValue(JsonParser p, Class<T> type)
/*      */     throws IOException
/*      */   {
/*  727 */     return (T)readValue(p, getTypeFactory().constructType(type));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(JsonParser p, JavaType type)
/*      */     throws IOException
/*      */   {
/*  735 */     JsonDeserializer<Object> deser = findRootValueDeserializer(type);
/*  736 */     if (deser == null) {}
/*      */     
/*  738 */     return (T)deser.deserialize(p, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readPropertyValue(JsonParser p, BeanProperty prop, Class<T> type)
/*      */     throws IOException
/*      */   {
/*  750 */     return (T)readPropertyValue(p, prop, getTypeFactory().constructType(type));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readPropertyValue(JsonParser p, BeanProperty prop, JavaType type)
/*      */     throws IOException
/*      */   {
/*  758 */     JsonDeserializer<Object> deser = findContextualValueDeserializer(type, prop);
/*  759 */     if (deser == null) {}
/*      */     
/*      */ 
/*  762 */     return (T)deser.deserialize(p, this);
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
/*      */   public boolean handleUnknownProperty(JsonParser p, JsonDeserializer<?> deser, Object instanceOrClass, String propName)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/*  786 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  787 */     if (h != null) {
/*  788 */       while (h != null)
/*      */       {
/*  790 */         if (((DeserializationProblemHandler)h.value()).handleUnknownProperty(this, p, deser, instanceOrClass, propName)) {
/*  791 */           return true;
/*      */         }
/*  793 */         h = h.next();
/*      */       }
/*      */     }
/*  796 */     return false;
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
/*      */   public void reportUnknownProperty(Object instanceOrClass, String fieldName, JsonDeserializer<?> deser)
/*      */     throws JsonMappingException
/*      */   {
/*  812 */     if (!isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/*  813 */       return;
/*      */     }
/*      */     
/*  816 */     Collection<Object> propIds = deser == null ? null : deser.getKnownPropertyNames();
/*  817 */     throw UnrecognizedPropertyException.from(this._parser, instanceOrClass, fieldName, propIds);
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
/*      */   public JsonMappingException mappingException(Class<?> targetClass)
/*      */   {
/*  831 */     return mappingException(targetClass, this._parser.getCurrentToken());
/*      */   }
/*      */   
/*      */   public JsonMappingException mappingException(Class<?> targetClass, JsonToken token) {
/*  835 */     return JsonMappingException.from(this._parser, "Can not deserialize instance of " + _calcName(targetClass) + " out of " + token + " token");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException mappingException(String message)
/*      */   {
/*  843 */     return JsonMappingException.from(getParser(), message);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException instantiationException(Class<?> instClass, Throwable t)
/*      */   {
/*  852 */     return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + ", problem: " + t.getMessage(), t);
/*      */   }
/*      */   
/*      */   public JsonMappingException instantiationException(Class<?> instClass, String msg)
/*      */   {
/*  857 */     return JsonMappingException.from(this._parser, "Can not construct instance of " + instClass.getName() + ", problem: " + msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonMappingException weirdStringException(Class<?> instClass, String msg)
/*      */   {
/*  869 */     return weirdStringException(null, instClass, msg);
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
/*      */   public JsonMappingException weirdStringException(String value, Class<?> instClass, String msg)
/*      */   {
/*  883 */     return InvalidFormatException.from(this._parser, "Can not construct instance of " + instClass.getName() + " from String value '" + _valueDesc() + "': " + msg, value, instClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonMappingException weirdNumberException(Class<?> instClass, String msg)
/*      */   {
/*  894 */     return weirdStringException(null, instClass, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException weirdNumberException(Number value, Class<?> instClass, String msg)
/*      */   {
/*  902 */     return InvalidFormatException.from(this._parser, "Can not construct instance of " + instClass.getName() + " from number value (" + _valueDesc() + "): " + msg, null, instClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException weirdKeyException(Class<?> keyClass, String keyValue, String msg)
/*      */   {
/*  913 */     return InvalidFormatException.from(this._parser, "Can not construct Map key of type " + keyClass.getName() + " from String \"" + _desc(keyValue) + "\": " + msg, keyValue, keyClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException wrongTokenException(JsonParser p, JsonToken expToken, String msg0)
/*      */   {
/*  923 */     String msg = "Unexpected token (" + p.getCurrentToken() + "), expected " + expToken;
/*  924 */     if (msg0 != null) {
/*  925 */       msg = msg + ": " + msg0;
/*      */     }
/*  927 */     return JsonMappingException.from(p, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonMappingException unknownTypeException(JavaType type, String id)
/*      */   {
/*  936 */     return JsonMappingException.from(this._parser, "Could not resolve type id '" + id + "' into a subtype of " + type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonMappingException unknownTypeException(JavaType type, String id, String extraDesc)
/*      */   {
/*  944 */     String msg = "Could not resolve type id '" + id + "' into a subtype of " + type;
/*  945 */     if (extraDesc != null) {
/*  946 */       msg = msg + ": " + extraDesc;
/*      */     }
/*  948 */     return JsonMappingException.from(this._parser, msg);
/*      */   }
/*      */   
/*      */   public JsonMappingException endOfInputException(Class<?> instClass) {
/*  952 */     return JsonMappingException.from(this._parser, "Unexpected end-of-input when trying to deserialize a " + instClass.getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormat getDateFormat()
/*      */   {
/*  964 */     if (this._dateFormat != null) {
/*  965 */       return this._dateFormat;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  972 */     DateFormat df = this._config.getDateFormat();
/*  973 */     this._dateFormat = (df = (DateFormat)df.clone());
/*  974 */     return df;
/*      */   }
/*      */   
/*      */   protected String determineClassName(Object instance) {
/*  978 */     return ClassUtil.getClassDescription(instance);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _calcName(Class<?> cls)
/*      */   {
/*  988 */     if (cls.isArray()) {
/*  989 */       return _calcName(cls.getComponentType()) + "[]";
/*      */     }
/*  991 */     return cls.getName();
/*      */   }
/*      */   
/*      */   protected String _valueDesc() {
/*      */     try {
/*  996 */       return _desc(this._parser.getText());
/*      */     } catch (Exception e) {}
/*  998 */     return "[N/A]";
/*      */   }
/*      */   
/*      */ 
/*      */   protected String _desc(String desc)
/*      */   {
/* 1004 */     if (desc.length() > 500) {
/* 1005 */       desc = desc.substring(0, 500) + "]...[" + desc.substring(desc.length() - 500);
/*      */     }
/* 1007 */     return desc;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\DeserializationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */