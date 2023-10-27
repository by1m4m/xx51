/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.type.ClassKey;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
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
/*     */ public final class DeserializationConfig
/*     */   extends MapperConfigBase<DeserializationFeature, DeserializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int _deserFeatures;
/*     */   protected final LinkedNode<DeserializationProblemHandler> _problemHandlers;
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */   protected final int _parserFeatures;
/*     */   protected final int _parserFeaturesToChange;
/*     */   
/*     */   public DeserializationConfig(BaseSettings base, SubtypeResolver str, Map<ClassKey, Class<?>> mixins)
/*     */   {
/*  85 */     super(base, str, mixins);
/*  86 */     this._deserFeatures = collectFeatureDefaults(DeserializationFeature.class);
/*  87 */     this._nodeFactory = JsonNodeFactory.instance;
/*  88 */     this._problemHandlers = null;
/*  89 */     this._parserFeatures = 0;
/*  90 */     this._parserFeaturesToChange = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private DeserializationConfig(DeserializationConfig src, int mapperFeatures, int deserFeatures, int parserFeatures, int parserFeatureMask)
/*     */   {
/*  97 */     super(src, mapperFeatures);
/*  98 */     this._deserFeatures = deserFeatures;
/*  99 */     this._nodeFactory = src._nodeFactory;
/* 100 */     this._problemHandlers = src._problemHandlers;
/* 101 */     this._parserFeatures = parserFeatures;
/* 102 */     this._parserFeaturesToChange = parserFeatureMask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DeserializationConfig(DeserializationConfig src, SubtypeResolver str)
/*     */   {
/* 111 */     super(src, str);
/* 112 */     this._deserFeatures = src._deserFeatures;
/* 113 */     this._nodeFactory = src._nodeFactory;
/* 114 */     this._problemHandlers = src._problemHandlers;
/* 115 */     this._parserFeatures = src._parserFeatures;
/* 116 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, BaseSettings base)
/*     */   {
/* 121 */     super(src, base);
/* 122 */     this._deserFeatures = src._deserFeatures;
/* 123 */     this._nodeFactory = src._nodeFactory;
/* 124 */     this._problemHandlers = src._problemHandlers;
/* 125 */     this._parserFeatures = src._parserFeatures;
/* 126 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, JsonNodeFactory f)
/*     */   {
/* 131 */     super(src);
/* 132 */     this._deserFeatures = src._deserFeatures;
/* 133 */     this._problemHandlers = src._problemHandlers;
/* 134 */     this._nodeFactory = f;
/* 135 */     this._parserFeatures = src._parserFeatures;
/* 136 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */   private DeserializationConfig(DeserializationConfig src, LinkedNode<DeserializationProblemHandler> problemHandlers)
/*     */   {
/* 142 */     super(src);
/* 143 */     this._deserFeatures = src._deserFeatures;
/* 144 */     this._problemHandlers = problemHandlers;
/* 145 */     this._nodeFactory = src._nodeFactory;
/* 146 */     this._parserFeatures = src._parserFeatures;
/* 147 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, String rootName)
/*     */   {
/* 152 */     super(src, rootName);
/* 153 */     this._deserFeatures = src._deserFeatures;
/* 154 */     this._problemHandlers = src._problemHandlers;
/* 155 */     this._nodeFactory = src._nodeFactory;
/* 156 */     this._parserFeatures = src._parserFeatures;
/* 157 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, Class<?> view)
/*     */   {
/* 162 */     super(src, view);
/* 163 */     this._deserFeatures = src._deserFeatures;
/* 164 */     this._problemHandlers = src._problemHandlers;
/* 165 */     this._nodeFactory = src._nodeFactory;
/* 166 */     this._parserFeatures = src._parserFeatures;
/* 167 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DeserializationConfig(DeserializationConfig src, Map<ClassKey, Class<?>> mixins)
/*     */   {
/* 175 */     super(src, mixins);
/* 176 */     this._deserFeatures = src._deserFeatures;
/* 177 */     this._problemHandlers = src._problemHandlers;
/* 178 */     this._nodeFactory = src._nodeFactory;
/* 179 */     this._parserFeatures = src._parserFeatures;
/* 180 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DeserializationConfig(DeserializationConfig src, ContextAttributes attrs)
/*     */   {
/* 188 */     super(src, attrs);
/* 189 */     this._deserFeatures = src._deserFeatures;
/* 190 */     this._problemHandlers = src._problemHandlers;
/* 191 */     this._nodeFactory = src._nodeFactory;
/* 192 */     this._parserFeatures = src._parserFeatures;
/* 193 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/*     */   }
/*     */   
/*     */   protected BaseSettings getBaseSettings() {
/* 197 */     return this._base;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig with(MapperFeature... features)
/*     */   {
/* 208 */     int newMapperFlags = this._mapperFeatures;
/* 209 */     for (MapperFeature f : features) {
/* 210 */       newMapperFlags |= f.getMask();
/*     */     }
/* 212 */     return newMapperFlags == this._mapperFeatures ? this : new DeserializationConfig(this, newMapperFlags, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig without(MapperFeature... features)
/*     */   {
/* 221 */     int newMapperFlags = this._mapperFeatures;
/* 222 */     for (MapperFeature f : features) {
/* 223 */       newMapperFlags &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 225 */     return newMapperFlags == this._mapperFeatures ? this : new DeserializationConfig(this, newMapperFlags, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */   public DeserializationConfig with(MapperFeature feature, boolean state)
/*     */   {
/*     */     int newMapperFlags;
/*     */     
/*     */     int newMapperFlags;
/* 234 */     if (state) {
/* 235 */       newMapperFlags = this._mapperFeatures | feature.getMask();
/*     */     } else {
/* 237 */       newMapperFlags = this._mapperFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 239 */     return newMapperFlags == this._mapperFeatures ? this : new DeserializationConfig(this, newMapperFlags, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DeserializationConfig with(ClassIntrospector ci)
/*     */   {
/* 246 */     return _withBase(this._base.withClassIntrospector(ci));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(AnnotationIntrospector ai)
/*     */   {
/* 251 */     return _withBase(this._base.withAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(VisibilityChecker<?> vc)
/*     */   {
/* 256 */     return _withBase(this._base.withVisibilityChecker(vc));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*     */   {
/* 261 */     return _withBase(this._base.withVisibility(forMethod, visibility));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(TypeResolverBuilder<?> trb)
/*     */   {
/* 266 */     return _withBase(this._base.withTypeResolverBuilder(trb));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(SubtypeResolver str)
/*     */   {
/* 271 */     return this._subtypeResolver == str ? this : new DeserializationConfig(this, str);
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(PropertyNamingStrategy pns)
/*     */   {
/* 276 */     return _withBase(this._base.withPropertyNamingStrategy(pns));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withRootName(String rootName)
/*     */   {
/* 281 */     if (rootName == null) {
/* 282 */       if (this._rootName == null) {
/* 283 */         return this;
/*     */       }
/* 285 */     } else if (rootName.equals(this._rootName)) {
/* 286 */       return this;
/*     */     }
/* 288 */     return new DeserializationConfig(this, rootName);
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(TypeFactory tf)
/*     */   {
/* 293 */     return _withBase(this._base.withTypeFactory(tf));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(DateFormat df)
/*     */   {
/* 298 */     return _withBase(this._base.withDateFormat(df));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(HandlerInstantiator hi)
/*     */   {
/* 303 */     return _withBase(this._base.withHandlerInstantiator(hi));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 308 */     return _withBase(this._base.withInsertedAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 313 */     return _withBase(this._base.withAppendedAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withView(Class<?> view)
/*     */   {
/* 318 */     return this._view == view ? this : new DeserializationConfig(this, view);
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(Locale l)
/*     */   {
/* 323 */     return _withBase(this._base.with(l));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(TimeZone tz)
/*     */   {
/* 328 */     return _withBase(this._base.with(tz));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(Base64Variant base64)
/*     */   {
/* 333 */     return _withBase(this._base.with(base64));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(ContextAttributes attrs)
/*     */   {
/* 338 */     return attrs == this._attributes ? this : new DeserializationConfig(this, attrs);
/*     */   }
/*     */   
/*     */   private final DeserializationConfig _withBase(BaseSettings newBase) {
/* 342 */     return this._base == newBase ? this : new DeserializationConfig(this, newBase);
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
/*     */   public DeserializationConfig with(DeserializationFeature feature)
/*     */   {
/* 357 */     int newDeserFeatures = this._deserFeatures | feature.getMask();
/* 358 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig with(DeserializationFeature first, DeserializationFeature... features)
/*     */   {
/* 370 */     int newDeserFeatures = this._deserFeatures | first.getMask();
/* 371 */     for (DeserializationFeature f : features) {
/* 372 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 374 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withFeatures(DeserializationFeature... features)
/*     */   {
/* 385 */     int newDeserFeatures = this._deserFeatures;
/* 386 */     for (DeserializationFeature f : features) {
/* 387 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 389 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig without(DeserializationFeature feature)
/*     */   {
/* 400 */     int newDeserFeatures = this._deserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 401 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig without(DeserializationFeature first, DeserializationFeature... features)
/*     */   {
/* 413 */     int newDeserFeatures = this._deserFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 414 */     for (DeserializationFeature f : features) {
/* 415 */       newDeserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 417 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withoutFeatures(DeserializationFeature... features)
/*     */   {
/* 428 */     int newDeserFeatures = this._deserFeatures;
/* 429 */     for (DeserializationFeature f : features) {
/* 430 */       newDeserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 432 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange);
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
/*     */   public DeserializationConfig with(JsonParser.Feature feature)
/*     */   {
/* 451 */     int newSet = this._parserFeatures | feature.getMask();
/* 452 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 453 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask);
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
/*     */   public DeserializationConfig withFeatures(JsonParser.Feature... features)
/*     */   {
/* 466 */     int newSet = this._parserFeatures;
/* 467 */     int newMask = this._parserFeaturesToChange;
/* 468 */     for (JsonParser.Feature f : features) {
/* 469 */       int mask = f.getMask();
/* 470 */       newSet |= mask;
/* 471 */       newMask |= mask;
/*     */     }
/* 473 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask);
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
/*     */   public DeserializationConfig without(JsonParser.Feature feature)
/*     */   {
/* 486 */     int newSet = this._parserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 487 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 488 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask);
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
/*     */   public DeserializationConfig withoutFeatures(JsonParser.Feature... features)
/*     */   {
/* 501 */     int newSet = this._parserFeatures;
/* 502 */     int newMask = this._parserFeaturesToChange;
/* 503 */     for (JsonParser.Feature f : features) {
/* 504 */       int mask = f.getMask();
/* 505 */       newSet &= (mask ^ 0xFFFFFFFF);
/* 506 */       newMask |= mask;
/*     */     }
/* 508 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask);
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
/*     */   public DeserializationConfig with(JsonNodeFactory f)
/*     */   {
/* 524 */     if (this._nodeFactory == f) {
/* 525 */       return this;
/*     */     }
/* 527 */     return new DeserializationConfig(this, f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withHandler(DeserializationProblemHandler h)
/*     */   {
/* 537 */     if (LinkedNode.contains(this._problemHandlers, h)) {
/* 538 */       return this;
/*     */     }
/* 540 */     return new DeserializationConfig(this, new LinkedNode(h, this._problemHandlers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withNoProblemHandlers()
/*     */   {
/* 549 */     if (this._problemHandlers == null) {
/* 550 */       return this;
/*     */     }
/* 552 */     return new DeserializationConfig(this, (LinkedNode)null);
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
/*     */   public void initialize(JsonParser p)
/*     */   {
/* 570 */     if (this._parserFeaturesToChange != 0) {
/* 571 */       int orig = p.getFeatureMask();
/* 572 */       int newFlags = orig & (this._parserFeaturesToChange ^ 0xFFFFFFFF) | this._parserFeatures;
/* 573 */       if (orig != newFlags) {
/* 574 */         p.setFeatureMask(newFlags);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 595 */     if (isEnabled(MapperFeature.USE_ANNOTATIONS)) {
/* 596 */       return super.getAnnotationIntrospector();
/*     */     }
/* 598 */     return NopAnnotationIntrospector.instance;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean useRootWrapping()
/*     */   {
/* 604 */     if (this._rootName != null) {
/* 605 */       return this._rootName.length() > 0;
/*     */     }
/* 607 */     return isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectClassAnnotations(JavaType type)
/*     */   {
/* 616 */     return getClassIntrospector().forClassAnnotations(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectDirectClassAnnotations(JavaType type)
/*     */   {
/* 626 */     return getClassIntrospector().forDirectClassAnnotations(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public VisibilityChecker<?> getDefaultVisibilityChecker()
/*     */   {
/* 632 */     VisibilityChecker<?> vchecker = super.getDefaultVisibilityChecker();
/* 633 */     if (!isEnabled(MapperFeature.AUTO_DETECT_SETTERS)) {
/* 634 */       vchecker = vchecker.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 636 */     if (!isEnabled(MapperFeature.AUTO_DETECT_CREATORS)) {
/* 637 */       vchecker = vchecker.withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 639 */     if (!isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
/* 640 */       vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 642 */     return vchecker;
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(DeserializationFeature f) {
/* 646 */     return (this._deserFeatures & f.getMask()) != 0;
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(JsonParser.Feature f, JsonFactory factory) {
/* 650 */     int mask = f.getMask();
/* 651 */     if ((this._parserFeaturesToChange & mask) != 0) {
/* 652 */       return (this._parserFeatures & f.getMask()) != 0;
/*     */     }
/* 654 */     return factory.isEnabled(f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasDeserializationFeatures(int featureMask)
/*     */   {
/* 664 */     return (this._deserFeatures & featureMask) == featureMask;
/*     */   }
/*     */   
/*     */   public final int getDeserializationFeatures() {
/* 668 */     return this._deserFeatures;
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
/*     */   public LinkedNode<DeserializationProblemHandler> getProblemHandlers()
/*     */   {
/* 682 */     return this._problemHandlers;
/*     */   }
/*     */   
/*     */   public final JsonNodeFactory getNodeFactory() {
/* 686 */     return this._nodeFactory;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type)
/*     */   {
/* 703 */     return getClassIntrospector().forDeserialization(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends BeanDescription> T introspectForCreation(JavaType type)
/*     */   {
/* 712 */     return getClassIntrospector().forCreation(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends BeanDescription> T introspectForBuilder(JavaType type)
/*     */   {
/* 720 */     return getClassIntrospector().forDeserializationWithBuilder(this, type, this);
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
/*     */   public TypeDeserializer findTypeDeserializer(JavaType baseType)
/*     */     throws JsonMappingException
/*     */   {
/* 739 */     BeanDescription bean = introspectClassAnnotations(baseType.getRawClass());
/* 740 */     AnnotatedClass ac = bean.getClassInfo();
/* 741 */     TypeResolverBuilder<?> b = getAnnotationIntrospector().findTypeResolver(this, ac, baseType);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 746 */     Collection<NamedType> subtypes = null;
/* 747 */     if (b == null) {
/* 748 */       b = getDefaultTyper(baseType);
/* 749 */       if (b == null) {
/* 750 */         return null;
/*     */       }
/*     */     } else {
/* 753 */       subtypes = getSubtypeResolver().collectAndResolveSubtypes(ac, this, getAnnotationIntrospector());
/*     */     }
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
/* 768 */     return b.buildTypeDeserializer(this, baseType, subtypes);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\DeserializationConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */