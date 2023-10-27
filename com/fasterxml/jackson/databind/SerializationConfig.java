/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.type.ClassKey;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
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
/*     */ public final class SerializationConfig
/*     */   extends MapperConfigBase<SerializationFeature, SerializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int _serFeatures;
/*  54 */   protected JsonInclude.Include _serializationInclusion = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final FilterProvider _filterProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _generatorFeatures;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _generatorFeaturesToChange;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig(BaseSettings base, SubtypeResolver str, Map<ClassKey, Class<?>> mixins)
/*     */   {
/*  84 */     super(base, str, mixins);
/*  85 */     this._serFeatures = collectFeatureDefaults(SerializationFeature.class);
/*  86 */     this._filterProvider = null;
/*  87 */     this._generatorFeatures = 0;
/*  88 */     this._generatorFeaturesToChange = 0;
/*     */   }
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, SubtypeResolver str)
/*     */   {
/*  93 */     super(src, str);
/*  94 */     this._serFeatures = src._serFeatures;
/*  95 */     this._serializationInclusion = src._serializationInclusion;
/*  96 */     this._filterProvider = src._filterProvider;
/*  97 */     this._generatorFeatures = src._generatorFeatures;
/*  98 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private SerializationConfig(SerializationConfig src, int mapperFeatures, int serFeatures, int generatorFeatures, int generatorFeatureMask)
/*     */   {
/* 105 */     super(src, mapperFeatures);
/* 106 */     this._serFeatures = serFeatures;
/* 107 */     this._serializationInclusion = src._serializationInclusion;
/* 108 */     this._filterProvider = src._filterProvider;
/* 109 */     this._generatorFeatures = generatorFeatures;
/* 110 */     this._generatorFeaturesToChange = generatorFeatureMask;
/*     */   }
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, BaseSettings base)
/*     */   {
/* 115 */     super(src, base);
/* 116 */     this._serFeatures = src._serFeatures;
/* 117 */     this._serializationInclusion = src._serializationInclusion;
/* 118 */     this._filterProvider = src._filterProvider;
/* 119 */     this._generatorFeatures = src._generatorFeatures;
/* 120 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, FilterProvider filters)
/*     */   {
/* 125 */     super(src);
/* 126 */     this._serFeatures = src._serFeatures;
/* 127 */     this._serializationInclusion = src._serializationInclusion;
/* 128 */     this._filterProvider = filters;
/* 129 */     this._generatorFeatures = src._generatorFeatures;
/* 130 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, Class<?> view)
/*     */   {
/* 135 */     super(src, view);
/* 136 */     this._serFeatures = src._serFeatures;
/* 137 */     this._serializationInclusion = src._serializationInclusion;
/* 138 */     this._filterProvider = src._filterProvider;
/* 139 */     this._generatorFeatures = src._generatorFeatures;
/* 140 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, JsonInclude.Include incl)
/*     */   {
/* 145 */     super(src);
/* 146 */     this._serFeatures = src._serFeatures;
/* 147 */     this._serializationInclusion = incl;
/* 148 */     this._filterProvider = src._filterProvider;
/* 149 */     this._generatorFeatures = src._generatorFeatures;
/* 150 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, String rootName)
/*     */   {
/* 155 */     super(src, rootName);
/* 156 */     this._serFeatures = src._serFeatures;
/* 157 */     this._serializationInclusion = src._serializationInclusion;
/* 158 */     this._filterProvider = src._filterProvider;
/* 159 */     this._generatorFeatures = src._generatorFeatures;
/* 160 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SerializationConfig(SerializationConfig src, Map<ClassKey, Class<?>> mixins)
/*     */   {
/* 168 */     super(src, mixins);
/* 169 */     this._serFeatures = src._serFeatures;
/* 170 */     this._serializationInclusion = src._serializationInclusion;
/* 171 */     this._filterProvider = src._filterProvider;
/* 172 */     this._generatorFeatures = src._generatorFeatures;
/* 173 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SerializationConfig(SerializationConfig src, ContextAttributes attrs)
/*     */   {
/* 181 */     super(src, attrs);
/* 182 */     this._serFeatures = src._serFeatures;
/* 183 */     this._serializationInclusion = src._serializationInclusion;
/* 184 */     this._filterProvider = src._filterProvider;
/* 185 */     this._generatorFeatures = src._generatorFeatures;
/* 186 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
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
/*     */   public SerializationConfig with(MapperFeature... features)
/*     */   {
/* 202 */     int newMapperFlags = this._mapperFeatures;
/* 203 */     for (MapperFeature f : features) {
/* 204 */       newMapperFlags |= f.getMask();
/*     */     }
/* 206 */     return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig without(MapperFeature... features)
/*     */   {
/* 218 */     int newMapperFlags = this._mapperFeatures;
/* 219 */     for (MapperFeature f : features) {
/* 220 */       newMapperFlags &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 222 */     return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */   public SerializationConfig with(MapperFeature feature, boolean state)
/*     */   {
/*     */     int newMapperFlags;
/*     */     
/*     */     int newMapperFlags;
/* 231 */     if (state) {
/* 232 */       newMapperFlags = this._mapperFeatures | feature.getMask();
/*     */     } else {
/* 234 */       newMapperFlags = this._mapperFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 236 */     return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SerializationConfig with(AnnotationIntrospector ai)
/*     */   {
/* 243 */     return _withBase(this._base.withAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public SerializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 248 */     return _withBase(this._base.withAppendedAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public SerializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 253 */     return _withBase(this._base.withInsertedAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(ClassIntrospector ci)
/*     */   {
/* 258 */     return _withBase(this._base.withClassIntrospector(ci));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig with(DateFormat df)
/*     */   {
/* 268 */     SerializationConfig cfg = new SerializationConfig(this, this._base.withDateFormat(df));
/*     */     
/* 270 */     if (df == null) {
/* 271 */       cfg = cfg.with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */     } else {
/* 273 */       cfg = cfg.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */     }
/* 275 */     return cfg;
/*     */   }
/*     */   
/*     */   public SerializationConfig with(HandlerInstantiator hi)
/*     */   {
/* 280 */     return _withBase(this._base.withHandlerInstantiator(hi));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(PropertyNamingStrategy pns)
/*     */   {
/* 285 */     return _withBase(this._base.withPropertyNamingStrategy(pns));
/*     */   }
/*     */   
/*     */   public SerializationConfig withRootName(String rootName)
/*     */   {
/* 290 */     if (rootName == null) {
/* 291 */       if (this._rootName == null) {
/* 292 */         return this;
/*     */       }
/* 294 */     } else if (rootName.equals(this._rootName)) {
/* 295 */       return this;
/*     */     }
/* 297 */     return new SerializationConfig(this, rootName);
/*     */   }
/*     */   
/*     */   public SerializationConfig with(SubtypeResolver str)
/*     */   {
/* 302 */     return str == this._subtypeResolver ? this : new SerializationConfig(this, str);
/*     */   }
/*     */   
/*     */   public SerializationConfig with(TypeFactory tf)
/*     */   {
/* 307 */     return _withBase(this._base.withTypeFactory(tf));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(TypeResolverBuilder<?> trb)
/*     */   {
/* 312 */     return _withBase(this._base.withTypeResolverBuilder(trb));
/*     */   }
/*     */   
/*     */   public SerializationConfig withView(Class<?> view)
/*     */   {
/* 317 */     return this._view == view ? this : new SerializationConfig(this, view);
/*     */   }
/*     */   
/*     */   public SerializationConfig with(VisibilityChecker<?> vc)
/*     */   {
/* 322 */     return _withBase(this._base.withVisibilityChecker(vc));
/*     */   }
/*     */   
/*     */   public SerializationConfig withVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*     */   {
/* 327 */     return _withBase(this._base.withVisibility(forMethod, visibility));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(Locale l)
/*     */   {
/* 332 */     return _withBase(this._base.with(l));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(TimeZone tz)
/*     */   {
/* 337 */     return _withBase(this._base.with(tz));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(Base64Variant base64)
/*     */   {
/* 342 */     return _withBase(this._base.with(base64));
/*     */   }
/*     */   
/*     */   public SerializationConfig with(ContextAttributes attrs)
/*     */   {
/* 347 */     return attrs == this._attributes ? this : new SerializationConfig(this, attrs);
/*     */   }
/*     */   
/*     */   private final SerializationConfig _withBase(BaseSettings newBase) {
/* 351 */     return this._base == newBase ? this : new SerializationConfig(this, newBase);
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
/*     */   public SerializationConfig with(SerializationFeature feature)
/*     */   {
/* 366 */     int newSerFeatures = this._serFeatures | feature.getMask();
/* 367 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig with(SerializationFeature first, SerializationFeature... features)
/*     */   {
/* 378 */     int newSerFeatures = this._serFeatures | first.getMask();
/* 379 */     for (SerializationFeature f : features) {
/* 380 */       newSerFeatures |= f.getMask();
/*     */     }
/* 382 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig withFeatures(SerializationFeature... features)
/*     */   {
/* 393 */     int newSerFeatures = this._serFeatures;
/* 394 */     for (SerializationFeature f : features) {
/* 395 */       newSerFeatures |= f.getMask();
/*     */     }
/* 397 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig without(SerializationFeature feature)
/*     */   {
/* 408 */     int newSerFeatures = this._serFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 409 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig without(SerializationFeature first, SerializationFeature... features)
/*     */   {
/* 420 */     int newSerFeatures = this._serFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 421 */     for (SerializationFeature f : features) {
/* 422 */       newSerFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 424 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig withoutFeatures(SerializationFeature... features)
/*     */   {
/* 435 */     int newSerFeatures = this._serFeatures;
/* 436 */     for (SerializationFeature f : features) {
/* 437 */       newSerFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 439 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange);
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
/*     */   public SerializationConfig with(JsonGenerator.Feature feature)
/*     */   {
/* 457 */     int newSet = this._generatorFeatures | feature.getMask();
/* 458 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/* 459 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
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
/*     */   public SerializationConfig withFeatures(JsonGenerator.Feature... features)
/*     */   {
/* 472 */     int newSet = this._generatorFeatures;
/* 473 */     int newMask = this._generatorFeaturesToChange;
/* 474 */     for (JsonGenerator.Feature f : features) {
/* 475 */       int mask = f.getMask();
/* 476 */       newSet |= mask;
/* 477 */       newMask |= mask;
/*     */     }
/* 479 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
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
/*     */   public SerializationConfig without(JsonGenerator.Feature feature)
/*     */   {
/* 492 */     int newSet = this._generatorFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 493 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/* 494 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
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
/*     */   public SerializationConfig withoutFeatures(JsonGenerator.Feature... features)
/*     */   {
/* 507 */     int newSet = this._generatorFeatures;
/* 508 */     int newMask = this._generatorFeaturesToChange;
/* 509 */     for (JsonGenerator.Feature f : features) {
/* 510 */       int mask = f.getMask();
/* 511 */       newSet &= (mask ^ 0xFFFFFFFF);
/* 512 */       newMask |= mask;
/*     */     }
/* 514 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializationConfig withFilters(FilterProvider filterProvider)
/*     */   {
/* 526 */     return filterProvider == this._filterProvider ? this : new SerializationConfig(this, filterProvider);
/*     */   }
/*     */   
/*     */   public SerializationConfig withSerializationInclusion(JsonInclude.Include incl) {
/* 530 */     return this._serializationInclusion == incl ? this : new SerializationConfig(this, incl);
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
/*     */   public void initialize(JsonGenerator g)
/*     */   {
/* 548 */     if (SerializationFeature.INDENT_OUTPUT.enabledIn(this._serFeatures)) {
/* 549 */       g.useDefaultPrettyPrinter();
/*     */     }
/*     */     
/* 552 */     boolean useBigDec = SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._serFeatures);
/* 553 */     if ((this._generatorFeaturesToChange != 0) || (useBigDec)) {
/* 554 */       int orig = g.getFeatureMask();
/* 555 */       int newFlags = orig & (this._generatorFeaturesToChange ^ 0xFFFFFFFF) | this._generatorFeatures;
/*     */       
/* 557 */       if (useBigDec) {
/* 558 */         newFlags |= JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN.getMask();
/*     */       }
/* 560 */       if (orig != newFlags) {
/* 561 */         g.setFeatureMask(newFlags);
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
/*     */   public boolean useRootWrapping()
/*     */   {
/* 575 */     if (this._rootName != null) {
/* 576 */       return this._rootName.length() > 0;
/*     */     }
/* 578 */     return isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 584 */     if (isEnabled(MapperFeature.USE_ANNOTATIONS)) {
/* 585 */       return super.getAnnotationIntrospector();
/*     */     }
/* 587 */     return AnnotationIntrospector.nopInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectClassAnnotations(JavaType type)
/*     */   {
/* 596 */     return getClassIntrospector().forClassAnnotations(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectDirectClassAnnotations(JavaType type)
/*     */   {
/* 606 */     return getClassIntrospector().forDirectClassAnnotations(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public VisibilityChecker<?> getDefaultVisibilityChecker()
/*     */   {
/* 612 */     VisibilityChecker<?> vchecker = super.getDefaultVisibilityChecker();
/* 613 */     if (!isEnabled(MapperFeature.AUTO_DETECT_GETTERS)) {
/* 614 */       vchecker = vchecker.withGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/*     */     
/* 617 */     if (!isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS)) {
/* 618 */       vchecker = vchecker.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 620 */     if (!isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
/* 621 */       vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 623 */     return vchecker;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isEnabled(SerializationFeature f)
/*     */   {
/* 633 */     return (this._serFeatures & f.getMask()) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isEnabled(JsonGenerator.Feature f, JsonFactory factory)
/*     */   {
/* 644 */     int mask = f.getMask();
/* 645 */     if ((this._generatorFeaturesToChange & mask) != 0) {
/* 646 */       return (this._generatorFeatures & f.getMask()) != 0;
/*     */     }
/* 648 */     return factory.isEnabled(f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasSerializationFeatures(int featureMask)
/*     */   {
/* 658 */     return (this._serFeatures & featureMask) == featureMask;
/*     */   }
/*     */   
/*     */   public final int getSerializationFeatures() {
/* 662 */     return this._serFeatures;
/*     */   }
/*     */   
/*     */   public JsonInclude.Include getSerializationInclusion()
/*     */   {
/* 667 */     if (this._serializationInclusion != null) {
/* 668 */       return this._serializationInclusion;
/*     */     }
/* 670 */     return JsonInclude.Include.ALWAYS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterProvider getFilterProvider()
/*     */   {
/* 680 */     return this._filterProvider;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type)
/*     */   {
/* 695 */     return getClassIntrospector().forSerialization(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 706 */     return "[SerializationConfig: flags=0x" + Integer.toHexString(this._serFeatures) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\SerializationConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */