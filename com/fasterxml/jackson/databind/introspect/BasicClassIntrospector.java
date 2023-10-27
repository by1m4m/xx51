/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.type.SimpleType;
/*     */ import com.fasterxml.jackson.databind.util.LRUMap;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicClassIntrospector
/*     */   extends ClassIntrospector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected static final BasicBeanDescription STRING_DESC;
/*     */   protected static final BasicBeanDescription BOOLEAN_DESC;
/*     */   protected static final BasicBeanDescription INT_DESC;
/*     */   protected static final BasicBeanDescription LONG_DESC;
/*     */   
/*     */   static
/*     */   {
/*  32 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(String.class, null, null);
/*  33 */     STRING_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(String.class), ac);
/*     */     
/*     */ 
/*     */ 
/*  37 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(Boolean.TYPE, null, null);
/*  38 */     BOOLEAN_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Boolean.TYPE), ac);
/*     */     
/*     */ 
/*     */ 
/*  42 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(Integer.TYPE, null, null);
/*  43 */     INT_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Integer.TYPE), ac);
/*     */     
/*     */ 
/*     */ 
/*  47 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(Long.TYPE, null, null);
/*  48 */     LONG_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Long.TYPE), ac);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  58 */   public static final BasicClassIntrospector instance = new BasicClassIntrospector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final LRUMap<JavaType, BasicBeanDescription> _cachedFCA;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicClassIntrospector()
/*     */   {
/*  70 */     this._cachedFCA = new LRUMap(16, 64);
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
/*     */   public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/*  84 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/*  85 */     if (desc == null)
/*     */     {
/*     */ 
/*  88 */       desc = _findStdJdkCollectionDesc(cfg, type, r);
/*  89 */       if (desc == null) {
/*  90 */         desc = BasicBeanDescription.forSerialization(collectProperties(cfg, type, r, true, "set"));
/*     */       }
/*     */       
/*     */ 
/*  94 */       this._cachedFCA.putIfAbsent(type, desc);
/*     */     }
/*  96 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 104 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 105 */     if (desc == null)
/*     */     {
/*     */ 
/* 108 */       desc = _findStdJdkCollectionDesc(cfg, type, r);
/* 109 */       if (desc == null) {
/* 110 */         desc = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false, "set"));
/*     */       }
/*     */       
/*     */ 
/* 114 */       this._cachedFCA.putIfAbsent(type, desc);
/*     */     }
/* 116 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forDeserializationWithBuilder(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 125 */     BasicBeanDescription desc = BasicBeanDescription.forDeserialization(collectPropertiesWithBuilder(cfg, type, r, false));
/*     */     
/*     */ 
/* 128 */     this._cachedFCA.putIfAbsent(type, desc);
/* 129 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 136 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 137 */     if (desc == null)
/*     */     {
/*     */ 
/*     */ 
/* 141 */       desc = _findStdJdkCollectionDesc(cfg, type, r);
/* 142 */       if (desc == null) {
/* 143 */         desc = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false, "set"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 148 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 155 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 156 */     if (desc == null) {
/* 157 */       desc = (BasicBeanDescription)this._cachedFCA.get(type);
/* 158 */       if (desc == null) {
/* 159 */         boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
/* 160 */         AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), useAnnotations ? cfg.getAnnotationIntrospector() : null, r);
/*     */         
/* 162 */         desc = BasicBeanDescription.forOtherUse(cfg, type, ac);
/* 163 */         this._cachedFCA.put(type, desc);
/*     */       }
/*     */     }
/* 166 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 173 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 174 */     if (desc == null) {
/* 175 */       boolean useAnnotations = cfg.isAnnotationProcessingEnabled();
/* 176 */       AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
/* 177 */       AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(type.getRawClass(), useAnnotations ? ai : null, r);
/*     */       
/* 179 */       desc = BasicBeanDescription.forOtherUse(cfg, type, ac);
/*     */     }
/* 181 */     return desc;
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
/*     */   protected POJOPropertiesCollector collectProperties(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r, boolean forSerialization, String mutatorPrefix)
/*     */   {
/* 194 */     boolean useAnnotations = config.isAnnotationProcessingEnabled();
/* 195 */     AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), useAnnotations ? config.getAnnotationIntrospector() : null, r);
/*     */     
/* 197 */     return constructPropertyCollector(config, ac, type, forSerialization, mutatorPrefix).collect();
/*     */   }
/*     */   
/*     */ 
/*     */   protected POJOPropertiesCollector collectPropertiesWithBuilder(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r, boolean forSerialization)
/*     */   {
/* 203 */     boolean useAnnotations = config.isAnnotationProcessingEnabled();
/* 204 */     AnnotationIntrospector ai = useAnnotations ? config.getAnnotationIntrospector() : null;
/* 205 */     AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
/* 206 */     JsonPOJOBuilder.Value builderConfig = ai == null ? null : ai.findPOJOBuilderConfig(ac);
/* 207 */     String mutatorPrefix = builderConfig == null ? "with" : builderConfig.withPrefix;
/* 208 */     return constructPropertyCollector(config, ac, type, forSerialization, mutatorPrefix).collect();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected POJOPropertiesCollector constructPropertyCollector(MapperConfig<?> config, AnnotatedClass ac, JavaType type, boolean forSerialization, String mutatorPrefix)
/*     */   {
/* 218 */     return new POJOPropertiesCollector(config, forSerialization, type, ac, mutatorPrefix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BasicBeanDescription _findStdTypeDesc(JavaType type)
/*     */   {
/* 227 */     Class<?> cls = type.getRawClass();
/* 228 */     if (cls.isPrimitive()) {
/* 229 */       if (cls == Boolean.TYPE) {
/* 230 */         return BOOLEAN_DESC;
/*     */       }
/* 232 */       if (cls == Integer.TYPE) {
/* 233 */         return INT_DESC;
/*     */       }
/* 235 */       if (cls == Long.TYPE) {
/* 236 */         return LONG_DESC;
/*     */       }
/*     */     }
/* 239 */     else if (cls == String.class) {
/* 240 */       return STRING_DESC;
/*     */     }
/*     */     
/* 243 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _isStdJDKCollection(JavaType type)
/*     */   {
/* 253 */     if ((!type.isContainerType()) || (type.isArrayType())) {
/* 254 */       return false;
/*     */     }
/* 256 */     Class<?> raw = type.getRawClass();
/* 257 */     Package pkg = raw.getPackage();
/* 258 */     if (pkg != null) {
/* 259 */       String pkgName = pkg.getName();
/* 260 */       if ((pkgName.startsWith("java.lang")) || (pkgName.startsWith("java.util")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 265 */         if ((Collection.class.isAssignableFrom(raw)) || (Map.class.isAssignableFrom(raw)))
/*     */         {
/* 267 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 271 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected BasicBeanDescription _findStdJdkCollectionDesc(MapperConfig<?> cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 277 */     if (_isStdJDKCollection(type)) {
/* 278 */       AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), cfg.isAnnotationProcessingEnabled() ? cfg.getAnnotationIntrospector() : null, r);
/*     */       
/* 280 */       return BasicBeanDescription.forOtherUse(cfg, type, ac);
/*     */     }
/* 282 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\BasicClassIntrospector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */