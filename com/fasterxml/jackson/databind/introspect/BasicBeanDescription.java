/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.Converter.None;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicBeanDescription
/*     */   extends BeanDescription
/*     */ {
/*     */   protected final MapperConfig<?> _config;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected final AnnotatedClass _classInfo;
/*     */   protected TypeBindings _bindings;
/*     */   protected final List<BeanPropertyDefinition> _properties;
/*     */   protected ObjectIdInfo _objectIdInfo;
/*     */   protected AnnotatedMethod _anySetterMethod;
/*     */   protected Map<Object, AnnotatedMember> _injectables;
/*     */   protected Set<String> _ignoredPropertyNames;
/*     */   protected AnnotatedMethod _jsonValueMethod;
/*     */   protected AnnotatedMember _anyGetter;
/*     */   
/*     */   protected BasicBeanDescription(MapperConfig<?> config, JavaType type, AnnotatedClass classDef, List<BeanPropertyDefinition> props)
/*     */   {
/*  90 */     super(type);
/*  91 */     this._config = config;
/*  92 */     this._annotationIntrospector = (config == null ? null : config.getAnnotationIntrospector());
/*  93 */     this._classInfo = classDef;
/*  94 */     this._properties = props;
/*     */   }
/*     */   
/*     */   protected BasicBeanDescription(POJOPropertiesCollector coll)
/*     */   {
/*  99 */     this(coll.getConfig(), coll.getType(), coll.getClassDef(), coll.getProperties());
/* 100 */     this._objectIdInfo = coll.getObjectIdInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicBeanDescription forDeserialization(POJOPropertiesCollector coll)
/*     */   {
/* 109 */     BasicBeanDescription desc = new BasicBeanDescription(coll);
/* 110 */     desc._anySetterMethod = coll.getAnySetterMethod();
/* 111 */     desc._ignoredPropertyNames = coll.getIgnoredPropertyNames();
/* 112 */     desc._injectables = coll.getInjectables();
/* 113 */     desc._jsonValueMethod = coll.getJsonValueMethod();
/* 114 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicBeanDescription forSerialization(POJOPropertiesCollector coll)
/*     */   {
/* 123 */     BasicBeanDescription desc = new BasicBeanDescription(coll);
/* 124 */     desc._jsonValueMethod = coll.getJsonValueMethod();
/* 125 */     desc._anyGetter = coll.getAnyGetter();
/* 126 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicBeanDescription forOtherUse(MapperConfig<?> config, JavaType type, AnnotatedClass ac)
/*     */   {
/* 137 */     return new BasicBeanDescription(config, type, ac, Collections.emptyList());
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
/*     */   public boolean removeProperty(String propName)
/*     */   {
/* 156 */     Iterator<BeanPropertyDefinition> it = this._properties.iterator();
/* 157 */     while (it.hasNext()) {
/* 158 */       BeanPropertyDefinition prop = (BeanPropertyDefinition)it.next();
/* 159 */       if (prop.getName().equals(propName)) {
/* 160 */         it.remove();
/* 161 */         return true;
/*     */       }
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedClass getClassInfo()
/*     */   {
/* 174 */     return this._classInfo;
/*     */   }
/*     */   
/* 177 */   public ObjectIdInfo getObjectIdInfo() { return this._objectIdInfo; }
/*     */   
/*     */   public List<BeanPropertyDefinition> findProperties()
/*     */   {
/* 181 */     return this._properties;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod findJsonValueMethod()
/*     */   {
/* 186 */     return this._jsonValueMethod;
/*     */   }
/*     */   
/*     */   public Set<String> getIgnoredPropertyNames()
/*     */   {
/* 191 */     if (this._ignoredPropertyNames == null) {
/* 192 */       return Collections.emptySet();
/*     */     }
/* 194 */     return this._ignoredPropertyNames;
/*     */   }
/*     */   
/*     */   public boolean hasKnownClassAnnotations()
/*     */   {
/* 199 */     return this._classInfo.hasAnnotations();
/*     */   }
/*     */   
/*     */   public Annotations getClassAnnotations()
/*     */   {
/* 204 */     return this._classInfo.getAnnotations();
/*     */   }
/*     */   
/*     */ 
/*     */   public TypeBindings bindingsForBeanType()
/*     */   {
/* 210 */     if (this._bindings == null) {
/* 211 */       this._bindings = new TypeBindings(this._config.getTypeFactory(), this._type);
/*     */     }
/* 213 */     return this._bindings;
/*     */   }
/*     */   
/*     */   public JavaType resolveType(Type jdkType)
/*     */   {
/* 218 */     if (jdkType == null) {
/* 219 */       return null;
/*     */     }
/* 221 */     return bindingsForBeanType().resolveType(jdkType);
/*     */   }
/*     */   
/*     */   public AnnotatedConstructor findDefaultConstructor()
/*     */   {
/* 226 */     return this._classInfo.getDefaultConstructor();
/*     */   }
/*     */   
/*     */   public AnnotatedMethod findAnySetter()
/*     */     throws IllegalArgumentException
/*     */   {
/* 232 */     if (this._anySetterMethod != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */       Class<?> type = this._anySetterMethod.getRawParameterType(0);
/* 242 */       if ((type != String.class) && (type != Object.class)) {
/* 243 */         throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + this._anySetterMethod.getName() + "(): first argument not of type String or Object, but " + type.getName());
/*     */       }
/*     */     }
/* 246 */     return this._anySetterMethod;
/*     */   }
/*     */   
/*     */   public Map<Object, AnnotatedMember> findInjectables()
/*     */   {
/* 251 */     return this._injectables;
/*     */   }
/*     */   
/*     */   public List<AnnotatedConstructor> getConstructors()
/*     */   {
/* 256 */     return this._classInfo.getConstructors();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object instantiateBean(boolean fixAccess)
/*     */   {
/* 262 */     AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
/* 263 */     if (ac == null) {
/* 264 */       return null;
/*     */     }
/* 266 */     if (fixAccess) {
/* 267 */       ac.fixAccess();
/*     */     }
/*     */     try {
/* 270 */       return ac.getAnnotated().newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 272 */       Throwable t = e;
/* 273 */       while (t.getCause() != null) {
/* 274 */         t = t.getCause();
/*     */       }
/* 276 */       if ((t instanceof Error)) throw ((Error)t);
/* 277 */       if ((t instanceof RuntimeException)) throw ((RuntimeException)t);
/* 278 */       throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes)
/*     */   {
/* 290 */     return this._classInfo.findMethod(name, paramTypes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonFormat.Value findExpectedFormat(JsonFormat.Value defValue)
/*     */   {
/* 302 */     if (this._annotationIntrospector != null) {
/* 303 */       JsonFormat.Value v = this._annotationIntrospector.findFormat(this._classInfo);
/* 304 */       if (v != null) {
/* 305 */         return v;
/*     */       }
/*     */     }
/* 308 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Converter<Object, Object> findSerializationConverter()
/*     */   {
/* 320 */     if (this._annotationIntrospector == null) {
/* 321 */       return null;
/*     */     }
/* 323 */     return _createConverter(this._annotationIntrospector.findSerializationConverter(this._classInfo));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonInclude.Include findSerializationInclusion(JsonInclude.Include defValue)
/*     */   {
/* 334 */     if (this._annotationIntrospector == null) {
/* 335 */       return defValue;
/*     */     }
/* 337 */     return this._annotationIntrospector.findSerializationInclusion(this._classInfo, defValue);
/*     */   }
/*     */   
/*     */   public JsonInclude.Include findSerializationInclusionForContent(JsonInclude.Include defValue)
/*     */   {
/* 342 */     if (this._annotationIntrospector == null) {
/* 343 */       return defValue;
/*     */     }
/* 345 */     return this._annotationIntrospector.findSerializationInclusionForContent(this._classInfo, defValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMember findAnyGetter()
/*     */     throws IllegalArgumentException
/*     */   {
/* 357 */     if (this._anyGetter != null)
/*     */     {
/*     */ 
/*     */ 
/* 361 */       Class<?> type = this._anyGetter.getRawType();
/* 362 */       if (!Map.class.isAssignableFrom(type)) {
/* 363 */         throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + this._anyGetter.getName() + "(): return type is not instance of java.util.Map");
/*     */       }
/*     */     }
/* 366 */     return this._anyGetter;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, AnnotatedMember> findBackReferenceProperties()
/*     */   {
/* 372 */     HashMap<String, AnnotatedMember> result = null;
/*     */     
/*     */ 
/* 375 */     for (BeanPropertyDefinition property : this._properties)
/*     */     {
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
/* 387 */       AnnotatedMember am = property.getMutator();
/* 388 */       if (am != null)
/*     */       {
/*     */ 
/* 391 */         AnnotationIntrospector.ReferenceProperty refDef = this._annotationIntrospector.findReferenceType(am);
/* 392 */         if ((refDef != null) && (refDef.isBackReference())) {
/* 393 */           if (result == null) {
/* 394 */             result = new HashMap();
/*     */           }
/* 396 */           String refName = refDef.getName();
/* 397 */           if (result.put(refName, am) != null)
/* 398 */             throw new IllegalArgumentException("Multiple back-reference properties with name '" + refName + "'");
/*     */         }
/*     */       }
/*     */     }
/* 402 */     return result;
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
/*     */   public List<AnnotatedMethod> getFactoryMethods()
/*     */   {
/* 415 */     List<AnnotatedMethod> candidates = this._classInfo.getStaticMethods();
/* 416 */     if (candidates.isEmpty()) {
/* 417 */       return candidates;
/*     */     }
/* 419 */     ArrayList<AnnotatedMethod> result = new ArrayList();
/* 420 */     for (AnnotatedMethod am : candidates) {
/* 421 */       if (isFactoryMethod(am)) {
/* 422 */         result.add(am);
/*     */       }
/*     */     }
/* 425 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Constructor<?> findSingleArgConstructor(Class<?>... argTypes)
/*     */   {
/* 431 */     for (AnnotatedConstructor ac : this._classInfo.getConstructors())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 436 */       if (ac.getParameterCount() == 1) {
/* 437 */         Class<?> actArg = ac.getRawParameterType(0);
/* 438 */         for (Class<?> expArg : argTypes) {
/* 439 */           if (expArg == actArg) {
/* 440 */             return ac.getAnnotated();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 445 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Method findFactoryMethod(Class<?>... expArgTypes)
/*     */   {
/* 452 */     for (AnnotatedMethod am : this._classInfo.getStaticMethods()) {
/* 453 */       if (isFactoryMethod(am))
/*     */       {
/* 455 */         Class<?> actualArgType = am.getRawParameterType(0);
/* 456 */         for (Class<?> expArgType : expArgTypes)
/*     */         {
/* 458 */           if (actualArgType.isAssignableFrom(expArgType)) {
/* 459 */             return am.getAnnotated();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 464 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isFactoryMethod(AnnotatedMethod am)
/*     */   {
/* 473 */     Class<?> rt = am.getRawReturnType();
/* 474 */     if (!getBeanClass().isAssignableFrom(rt)) {
/* 475 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 482 */     if (this._annotationIntrospector.hasCreatorAnnotation(am)) {
/* 483 */       return true;
/*     */     }
/* 485 */     String name = am.getName();
/* 486 */     if ("valueOf".equals(name)) {
/* 487 */       return true;
/*     */     }
/*     */     
/* 490 */     if (("fromString".equals(name)) && 
/* 491 */       (1 == am.getParameterCount())) {
/* 492 */       Class<?> cls = am.getRawParameterType(0);
/* 493 */       if ((cls == String.class) || (CharSequence.class.isAssignableFrom(cls))) {
/* 494 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 498 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public List<String> findCreatorPropertyNames()
/*     */   {
/* 507 */     List<PropertyName> params = findCreatorParameterNames();
/* 508 */     if (params.isEmpty()) {
/* 509 */       return Collections.emptyList();
/*     */     }
/* 511 */     List<String> result = new ArrayList(params.size());
/* 512 */     for (PropertyName name : params) {
/* 513 */       result.add(name.getSimpleName());
/*     */     }
/* 515 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public List<PropertyName> findCreatorParameterNames()
/*     */   {
/* 524 */     for (int i = 0; i < 2; i++) {
/* 525 */       List<? extends AnnotatedWithParams> l = i == 0 ? getConstructors() : getFactoryMethods();
/*     */       
/* 527 */       for (AnnotatedWithParams creator : l) {
/* 528 */         int argCount = creator.getParameterCount();
/* 529 */         if (argCount >= 1) {
/* 530 */           PropertyName name = _findCreatorPropertyName(creator.getParameter(0));
/* 531 */           if ((name != null) && (!name.isEmpty()))
/*     */           {
/*     */ 
/* 534 */             List<PropertyName> names = new ArrayList();
/* 535 */             names.add(name);
/* 536 */             for (int p = 1; p < argCount; p++) {
/* 537 */               name = _findCreatorPropertyName(creator.getParameter(p));
/* 538 */               names.add(name);
/*     */             }
/* 540 */             return names;
/*     */           }
/*     */         } } }
/* 543 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   protected PropertyName _findCreatorPropertyName(AnnotatedParameter param)
/*     */   {
/* 548 */     PropertyName name = this._annotationIntrospector.findNameForDeserialization(param);
/* 549 */     if ((name == null) || (name.isEmpty())) {
/* 550 */       String str = this._annotationIntrospector.findImplicitPropertyName(param);
/* 551 */       if ((str != null) && (!str.isEmpty())) {
/* 552 */         name = new PropertyName(str);
/*     */       }
/*     */     }
/* 555 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> findPOJOBuilder()
/*     */   {
/* 567 */     return this._annotationIntrospector == null ? null : this._annotationIntrospector.findPOJOBuilder(this._classInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonPOJOBuilder.Value findPOJOBuilderConfig()
/*     */   {
/* 574 */     return this._annotationIntrospector == null ? null : this._annotationIntrospector.findPOJOBuilderConfig(this._classInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Converter<Object, Object> findDeserializationConverter()
/*     */   {
/* 581 */     if (this._annotationIntrospector == null) {
/* 582 */       return null;
/*     */     }
/* 584 */     return _createConverter(this._annotationIntrospector.findDeserializationConverter(this._classInfo));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedHashMap<String, AnnotatedField> _findPropertyFields(Collection<String> ignoredProperties, boolean forSerialization)
/*     */   {
/* 606 */     LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap();
/* 607 */     for (BeanPropertyDefinition property : this._properties) {
/* 608 */       AnnotatedField f = property.getField();
/* 609 */       if (f != null) {
/* 610 */         String name = property.getName();
/* 611 */         if ((ignoredProperties == null) || 
/* 612 */           (!ignoredProperties.contains(name)))
/*     */         {
/*     */ 
/*     */ 
/* 616 */           results.put(name, f); }
/*     */       }
/*     */     }
/* 619 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Converter<Object, Object> _createConverter(Object converterDef)
/*     */   {
/* 631 */     if (converterDef == null) {
/* 632 */       return null;
/*     */     }
/* 634 */     if ((converterDef instanceof Converter)) {
/* 635 */       return (Converter)converterDef;
/*     */     }
/* 637 */     if (!(converterDef instanceof Class)) {
/* 638 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef.getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/*     */     
/* 641 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 643 */     if ((converterClass == Converter.None.class) || (ClassUtil.isBogusClass(converterClass))) {
/* 644 */       return null;
/*     */     }
/* 646 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 647 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass.getName() + "; expected Class<Converter>");
/*     */     }
/*     */     
/* 650 */     HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 651 */     Converter<?, ?> conv = hi == null ? null : hi.converterInstance(this._config, this._classInfo, converterClass);
/* 652 */     if (conv == null) {
/* 653 */       conv = (Converter)ClassUtil.createInstance(converterClass, this._config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 656 */     return conv;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\BasicBeanDescription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */