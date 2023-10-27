/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyBuilder
/*     */ {
/*     */   protected final SerializationConfig _config;
/*     */   protected final BeanDescription _beanDesc;
/*     */   protected final JsonInclude.Include _defaultInclusion;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected Object _defaultBean;
/*     */   
/*     */   public PropertyBuilder(SerializationConfig config, BeanDescription beanDesc)
/*     */   {
/*  40 */     this._config = config;
/*  41 */     this._beanDesc = beanDesc;
/*  42 */     this._defaultInclusion = beanDesc.findSerializationInclusion(config.getSerializationInclusion());
/*  43 */     this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Annotations getClassAnnotations()
/*     */   {
/*  53 */     return this._beanDesc.getClassAnnotations();
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
/*     */   protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping)
/*     */     throws JsonMappingException
/*     */   {
/*  68 */     JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
/*     */     
/*     */ 
/*  71 */     if (contentTypeSer != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  76 */       if (serializationType == null)
/*     */       {
/*  78 */         serializationType = declaredType;
/*     */       }
/*  80 */       JavaType ct = serializationType.getContentType();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  85 */       if (ct == null) {
/*  86 */         throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + propDef.getName() + "' (of type " + this._beanDesc.getType() + "); serialization type " + serializationType + " has no content");
/*     */       }
/*     */       
/*  89 */       serializationType = serializationType.withContentTypeHandler(contentTypeSer);
/*  90 */       ct = serializationType.getContentType();
/*     */     }
/*     */     
/*  93 */     Object valueToSuppress = null;
/*  94 */     boolean suppressNulls = false;
/*     */     
/*  96 */     JsonInclude.Include inclusion = propDef.findInclusion();
/*  97 */     if (inclusion == null) {
/*  98 */       inclusion = this._defaultInclusion;
/*     */     }
/* 100 */     if (inclusion != null) {
/* 101 */       switch (inclusion) {
/*     */       case NON_DEFAULT: 
/* 103 */         valueToSuppress = getDefaultValue(propDef.getName(), am);
/* 104 */         if (valueToSuppress == null) {
/* 105 */           suppressNulls = true;
/*     */ 
/*     */         }
/* 108 */         else if (valueToSuppress.getClass().isArray()) {
/* 109 */           valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case NON_EMPTY: 
/* 115 */         suppressNulls = true;
/*     */         
/* 117 */         valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
/* 118 */         break;
/*     */       case NON_NULL: 
/* 120 */         suppressNulls = true;
/*     */       
/*     */ 
/*     */       case ALWAYS: 
/* 124 */         if ((declaredType.isContainerType()) && (!this._config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)))
/*     */         {
/* 126 */           valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 131 */     BeanPropertyWriter bpw = new BeanPropertyWriter(propDef, am, this._beanDesc.getClassAnnotations(), declaredType, ser, typeSer, serializationType, suppressNulls, valueToSuppress);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 136 */     Object serDef = this._annotationIntrospector.findNullSerializer(am);
/* 137 */     if (serDef != null) {
/* 138 */       bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
/*     */     }
/*     */     
/* 141 */     NameTransformer unwrapper = this._annotationIntrospector.findUnwrappingNameTransformer(am);
/* 142 */     if (unwrapper != null) {
/* 143 */       bpw = bpw.unwrappingWriter(unwrapper);
/*     */     }
/* 145 */     return bpw;
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
/*     */   protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType)
/*     */   {
/* 163 */     Class<?> serClass = this._annotationIntrospector.findSerializationType(a);
/* 164 */     if (serClass != null)
/*     */     {
/* 166 */       Class<?> rawDeclared = declaredType.getRawClass();
/* 167 */       if (serClass.isAssignableFrom(rawDeclared)) {
/* 168 */         declaredType = declaredType.widenBy(serClass);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 176 */         if (!rawDeclared.isAssignableFrom(serClass)) {
/* 177 */           throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + a.getName() + "': class " + serClass.getName() + " not a super-type of (declared) class " + rawDeclared.getName());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 183 */         declaredType = this._config.constructSpecializedType(declaredType, serClass);
/*     */       }
/* 185 */       useStaticTyping = true;
/*     */     }
/*     */     
/*     */ 
/* 189 */     JavaType secondary = BasicSerializerFactory.modifySecondaryTypesByAnnotation(this._config, a, declaredType);
/* 190 */     if (secondary != declaredType) {
/* 191 */       useStaticTyping = true;
/* 192 */       declaredType = secondary;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 198 */     JsonSerialize.Typing typing = this._annotationIntrospector.findSerializationTyping(a);
/* 199 */     if ((typing != null) && (typing != JsonSerialize.Typing.DEFAULT_TYPING)) {
/* 200 */       useStaticTyping = typing == JsonSerialize.Typing.STATIC;
/*     */     }
/* 202 */     return useStaticTyping ? declaredType : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getDefaultBean()
/*     */   {
/* 213 */     if (this._defaultBean == null)
/*     */     {
/*     */ 
/*     */ 
/* 217 */       this._defaultBean = this._beanDesc.instantiateBean(this._config.canOverrideAccessModifiers());
/* 218 */       if (this._defaultBean == null) {
/* 219 */         Class<?> cls = this._beanDesc.getClassInfo().getAnnotated();
/* 220 */         throw new IllegalArgumentException("Class " + cls.getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
/*     */       }
/*     */     }
/* 223 */     return this._defaultBean;
/*     */   }
/*     */   
/*     */   protected Object getDefaultValue(String name, AnnotatedMember member)
/*     */   {
/* 228 */     Object defaultBean = getDefaultBean();
/*     */     try {
/* 230 */       return member.getValue(defaultBean);
/*     */     } catch (Exception e) {
/* 232 */       return _throwWrapped(e, name, defaultBean);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _throwWrapped(Exception e, String propName, Object defaultBean)
/*     */   {
/* 244 */     Throwable t = e;
/* 245 */     while (t.getCause() != null) {
/* 246 */       t = t.getCause();
/*     */     }
/* 248 */     if ((t instanceof Error)) throw ((Error)t);
/* 249 */     if ((t instanceof RuntimeException)) throw ((RuntimeException)t);
/* 250 */     throw new IllegalArgumentException("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\PropertyBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */