/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.Converter.None;
/*     */ import java.lang.reflect.Type;
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
/*     */ public abstract class DatabindContext
/*     */ {
/*     */   public abstract MapperConfig<?> getConfig();
/*     */   
/*     */   public abstract AnnotationIntrospector getAnnotationIntrospector();
/*     */   
/*     */   public final boolean isEnabled(MapperFeature feature)
/*     */   {
/*  61 */     return getConfig().isEnabled(feature);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean canOverrideAccessModifiers()
/*     */   {
/*  71 */     return getConfig().canOverrideAccessModifiers();
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
/*     */   public abstract Class<?> getActiveView();
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
/*     */   public abstract Object getAttribute(Object paramObject);
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
/*     */   public abstract DatabindContext setAttribute(Object paramObject1, Object paramObject2);
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
/*     */   public JavaType constructType(Type type)
/*     */   {
/* 124 */     return getTypeFactory().constructType(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass)
/*     */   {
/* 133 */     if (baseType.getRawClass() == subclass) {
/* 134 */       return baseType;
/*     */     }
/* 136 */     return getConfig().constructSpecializedType(baseType, subclass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract TypeFactory getTypeFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
/*     */     throws JsonMappingException
/*     */   {
/* 151 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/* 152 */     MapperConfig<?> config = getConfig();
/* 153 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 154 */     ObjectIdGenerator<?> gen = hi == null ? null : hi.objectIdGeneratorInstance(config, annotated, implClass);
/* 155 */     if (gen == null) {
/* 156 */       gen = (ObjectIdGenerator)ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 159 */     return gen.forScope(objectIdInfo.getScope());
/*     */   }
/*     */   
/*     */   public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
/*     */   {
/* 164 */     Class<? extends ObjectIdResolver> implClass = objectIdInfo.getResolverType();
/* 165 */     MapperConfig<?> config = getConfig();
/* 166 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 167 */     ObjectIdResolver resolver = hi == null ? null : hi.resolverIdGeneratorInstance(config, annotated, implClass);
/* 168 */     if (resolver == null) {
/* 169 */       resolver = (ObjectIdResolver)ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 172 */     return resolver;
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
/*     */   public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef)
/*     */     throws JsonMappingException
/*     */   {
/* 186 */     if (converterDef == null) {
/* 187 */       return null;
/*     */     }
/* 189 */     if ((converterDef instanceof Converter)) {
/* 190 */       return (Converter)converterDef;
/*     */     }
/* 192 */     if (!(converterDef instanceof Class)) {
/* 193 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef.getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/*     */     
/* 196 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 198 */     if ((converterClass == Converter.None.class) || (ClassUtil.isBogusClass(converterClass))) {
/* 199 */       return null;
/*     */     }
/* 201 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 202 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass.getName() + "; expected Class<Converter>");
/*     */     }
/*     */     
/* 205 */     MapperConfig<?> config = getConfig();
/* 206 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 207 */     Converter<?, ?> conv = hi == null ? null : hi.converterInstance(config, annotated, converterClass);
/* 208 */     if (conv == null) {
/* 209 */       conv = (Converter)ClassUtil.createInstance(converterClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 212 */     return conv;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\DatabindContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */