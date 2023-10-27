/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Locale;
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
/*     */ public abstract class MapperConfig<T extends MapperConfig<T>>
/*     */   implements ClassIntrospector.MixInResolver, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int _mapperFeatures;
/*     */   protected final BaseSettings _base;
/*     */   
/*     */   protected MapperConfig(BaseSettings base, int mapperFeatures)
/*     */   {
/*  59 */     this._base = base;
/*  60 */     this._mapperFeatures = mapperFeatures;
/*     */   }
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src)
/*     */   {
/*  65 */     this._base = src._base;
/*  66 */     this._mapperFeatures = src._mapperFeatures;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <F extends Enum<F>,  extends ConfigFeature> int collectFeatureDefaults(Class<F> enumClass)
/*     */   {
/*  75 */     int flags = 0;
/*  76 */     for (F value : (Enum[])enumClass.getEnumConstants()) {
/*  77 */       if (((ConfigFeature)value).enabledByDefault()) {
/*  78 */         flags |= ((ConfigFeature)value).getMask();
/*     */       }
/*     */     }
/*  81 */     return flags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(MapperFeature... paramVarArgs);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T without(MapperFeature... paramVarArgs);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(MapperFeature paramMapperFeature, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isEnabled(MapperFeature f)
/*     */   {
/* 118 */     return (this._mapperFeatures & f.getMask()) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasMapperFeatures(int featureMask)
/*     */   {
/* 128 */     return (this._mapperFeatures & featureMask) == featureMask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isAnnotationProcessingEnabled()
/*     */   {
/* 138 */     return isEnabled(MapperFeature.USE_ANNOTATIONS);
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
/*     */   public final boolean canOverrideAccessModifiers()
/*     */   {
/* 153 */     return isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean shouldSortPropertiesAlphabetically()
/*     */   {
/* 161 */     return isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
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
/*     */   public abstract boolean useRootWrapping();
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
/*     */   public SerializableString compileString(String src)
/*     */   {
/* 193 */     return new SerializedString(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassIntrospector getClassIntrospector()
/*     */   {
/* 203 */     return this._base.getClassIntrospector();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 213 */     return this._base.getAnnotationIntrospector();
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
/*     */   public VisibilityChecker<?> getDefaultVisibilityChecker()
/*     */   {
/* 226 */     return this._base.getVisibilityChecker();
/*     */   }
/*     */   
/*     */   public final PropertyNamingStrategy getPropertyNamingStrategy() {
/* 230 */     return this._base.getPropertyNamingStrategy();
/*     */   }
/*     */   
/*     */   public final HandlerInstantiator getHandlerInstantiator() {
/* 234 */     return this._base.getHandlerInstantiator();
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
/*     */   public final TypeResolverBuilder<?> getDefaultTyper(JavaType baseType)
/*     */   {
/* 250 */     return this._base.getTypeResolverBuilder();
/*     */   }
/*     */   
/*     */   public abstract SubtypeResolver getSubtypeResolver();
/*     */   
/*     */   public final TypeFactory getTypeFactory() {
/* 256 */     return this._base.getTypeFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JavaType constructType(Class<?> cls)
/*     */   {
/* 268 */     return getTypeFactory().constructType(cls, (TypeBindings)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JavaType constructType(TypeReference<?> valueTypeRef)
/*     */   {
/* 280 */     return getTypeFactory().constructType(valueTypeRef.getType(), (TypeBindings)null);
/*     */   }
/*     */   
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
/* 284 */     return getTypeFactory().constructSpecializedType(baseType, subclass);
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
/*     */   public BeanDescription introspectClassAnnotations(Class<?> cls)
/*     */   {
/* 298 */     return introspectClassAnnotations(constructType(cls));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanDescription introspectClassAnnotations(JavaType paramJavaType);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectDirectClassAnnotations(Class<?> cls)
/*     */   {
/* 313 */     return introspectDirectClassAnnotations(constructType(cls));
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
/*     */   public abstract BeanDescription introspectDirectClassAnnotations(JavaType paramJavaType);
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
/*     */   public final DateFormat getDateFormat()
/*     */   {
/* 344 */     return this._base.getDateFormat();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Locale getLocale()
/*     */   {
/* 351 */     return this._base.getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final TimeZone getTimeZone()
/*     */   {
/* 358 */     return this._base.getTimeZone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Class<?> getActiveView();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Base64Variant getBase64Variant()
/*     */   {
/* 373 */     return this._base.getBase64Variant();
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
/*     */   public abstract ContextAttributes getAttributes();
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
/*     */   public TypeResolverBuilder<?> typeResolverBuilderInstance(Annotated annotated, Class<? extends TypeResolverBuilder<?>> builderClass)
/*     */   {
/* 398 */     HandlerInstantiator hi = getHandlerInstantiator();
/* 399 */     if (hi != null) {
/* 400 */       TypeResolverBuilder<?> builder = hi.typeResolverBuilderInstance(this, annotated, builderClass);
/* 401 */       if (builder != null) {
/* 402 */         return builder;
/*     */       }
/*     */     }
/* 405 */     return (TypeResolverBuilder)ClassUtil.createInstance(builderClass, canOverrideAccessModifiers());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeIdResolver typeIdResolverInstance(Annotated annotated, Class<? extends TypeIdResolver> resolverClass)
/*     */   {
/* 415 */     HandlerInstantiator hi = getHandlerInstantiator();
/* 416 */     if (hi != null) {
/* 417 */       TypeIdResolver builder = hi.typeIdResolverInstance(this, annotated, resolverClass);
/* 418 */       if (builder != null) {
/* 419 */         return builder;
/*     */       }
/*     */     }
/* 422 */     return (TypeIdResolver)ClassUtil.createInstance(resolverClass, canOverrideAccessModifiers());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\cfg\MapperConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */