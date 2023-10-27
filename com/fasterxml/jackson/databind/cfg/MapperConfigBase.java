/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
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
/*     */ public abstract class MapperConfigBase<CFG extends ConfigFeature, T extends MapperConfigBase<CFG, T>>
/*     */   extends MapperConfig<T>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6062961959359172474L;
/*  28 */   private static final int DEFAULT_MAPPER_FEATURES = collectFeatureDefaults(MapperFeature.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Map<ClassKey, Class<?>> _mixInAnnotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SubtypeResolver _subtypeResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _rootName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?> _view;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ContextAttributes _attributes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(BaseSettings base, SubtypeResolver str, Map<ClassKey, Class<?>> mixins)
/*     */   {
/*  84 */     super(base, DEFAULT_MAPPER_FEATURES);
/*  85 */     this._mixInAnnotations = mixins;
/*  86 */     this._subtypeResolver = str;
/*  87 */     this._rootName = null;
/*  88 */     this._view = null;
/*     */     
/*  90 */     this._attributes = ContextAttributes.getEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src)
/*     */   {
/*  99 */     super(src);
/* 100 */     this._mixInAnnotations = src._mixInAnnotations;
/* 101 */     this._subtypeResolver = src._subtypeResolver;
/* 102 */     this._rootName = src._rootName;
/* 103 */     this._view = src._view;
/* 104 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, BaseSettings base)
/*     */   {
/* 109 */     super(base, src._mapperFeatures);
/* 110 */     this._mixInAnnotations = src._mixInAnnotations;
/* 111 */     this._subtypeResolver = src._subtypeResolver;
/* 112 */     this._rootName = src._rootName;
/* 113 */     this._view = src._view;
/* 114 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, int mapperFeatures)
/*     */   {
/* 119 */     super(src._base, mapperFeatures);
/* 120 */     this._mixInAnnotations = src._mixInAnnotations;
/* 121 */     this._subtypeResolver = src._subtypeResolver;
/* 122 */     this._rootName = src._rootName;
/* 123 */     this._view = src._view;
/* 124 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SubtypeResolver str) {
/* 128 */     super(src);
/* 129 */     this._mixInAnnotations = src._mixInAnnotations;
/* 130 */     this._subtypeResolver = str;
/* 131 */     this._rootName = src._rootName;
/* 132 */     this._view = src._view;
/* 133 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, String rootName) {
/* 137 */     super(src);
/* 138 */     this._mixInAnnotations = src._mixInAnnotations;
/* 139 */     this._subtypeResolver = src._subtypeResolver;
/* 140 */     this._rootName = rootName;
/* 141 */     this._view = src._view;
/* 142 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, Class<?> view)
/*     */   {
/* 147 */     super(src);
/* 148 */     this._mixInAnnotations = src._mixInAnnotations;
/* 149 */     this._subtypeResolver = src._subtypeResolver;
/* 150 */     this._rootName = src._rootName;
/* 151 */     this._view = view;
/* 152 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, Map<ClassKey, Class<?>> mixins)
/*     */   {
/* 160 */     super(src);
/* 161 */     this._mixInAnnotations = mixins;
/* 162 */     this._subtypeResolver = src._subtypeResolver;
/* 163 */     this._rootName = src._rootName;
/* 164 */     this._view = src._view;
/* 165 */     this._attributes = src._attributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, ContextAttributes attr)
/*     */   {
/* 173 */     super(src);
/* 174 */     this._mixInAnnotations = src._mixInAnnotations;
/* 175 */     this._subtypeResolver = src._subtypeResolver;
/* 176 */     this._rootName = src._rootName;
/* 177 */     this._view = src._view;
/* 178 */     this._attributes = attr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withAppendedAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withInsertedAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(ClassIntrospector paramClassIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(DateFormat paramDateFormat);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(HandlerInstantiator paramHandlerInstantiator);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(PropertyNamingStrategy paramPropertyNamingStrategy);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withRootName(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(SubtypeResolver paramSubtypeResolver);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(TypeFactory paramTypeFactory);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(TypeResolverBuilder<?> paramTypeResolverBuilder);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withView(Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(VisibilityChecker<?> paramVisibilityChecker);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withVisibility(PropertyAccessor paramPropertyAccessor, JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(Locale paramLocale);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(TimeZone paramTimeZone);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(Base64Variant paramBase64Variant);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(ContextAttributes paramContextAttributes);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withAttributes(Map<Object, Object> attributes)
/*     */   {
/* 339 */     return with(getAttributes().withSharedAttributes(attributes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withAttribute(Object key, Object value)
/*     */   {
/* 349 */     return with(getAttributes().withSharedAttribute(key, value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withoutAttribute(Object key)
/*     */   {
/* 359 */     return with(getAttributes().withoutSharedAttribute(key));
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
/*     */   public final SubtypeResolver getSubtypeResolver()
/*     */   {
/* 375 */     return this._subtypeResolver;
/*     */   }
/*     */   
/*     */   public final String getRootName() {
/* 379 */     return this._rootName;
/*     */   }
/*     */   
/*     */   public final Class<?> getActiveView()
/*     */   {
/* 384 */     return this._view;
/*     */   }
/*     */   
/*     */   public final ContextAttributes getAttributes()
/*     */   {
/* 389 */     return this._attributes;
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
/*     */   public final Class<?> findMixInClassFor(Class<?> cls)
/*     */   {
/* 404 */     return this._mixInAnnotations == null ? null : (Class)this._mixInAnnotations.get(new ClassKey(cls));
/*     */   }
/*     */   
/*     */   public final int mixInCount() {
/* 408 */     return this._mixInAnnotations == null ? 0 : this._mixInAnnotations.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\cfg\MapperConfigBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */