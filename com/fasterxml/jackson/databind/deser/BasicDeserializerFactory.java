/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ 
/*      */ import com.fasterxml.jackson.databind.AbstractTypeResolver;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.BeanProperty.Std;
/*      */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
/*      */ import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
/*      */ import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializers;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*      */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.type.ArrayType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionType;
/*      */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*      */ import com.fasterxml.jackson.databind.type.MapType;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ public abstract class BasicDeserializerFactory extends DeserializerFactory implements java.io.Serializable
/*      */ {
/*   41 */   private static final Class<?> CLASS_OBJECT = Object.class;
/*   42 */   private static final Class<?> CLASS_STRING = String.class;
/*   43 */   private static final Class<?> CLASS_CHAR_BUFFER = CharSequence.class;
/*   44 */   private static final Class<?> CLASS_ITERABLE = Iterable.class;
/*   45 */   private static final Class<?> CLASS_MAP_ENTRY = java.util.Map.Entry.class;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   51 */   protected static final PropertyName UNWRAPPED_CREATOR_PARAM_NAME = new PropertyName("@JsonUnwrapped");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   58 */   static final HashMap<String, Class<? extends Map>> _mapFallbacks = new HashMap();
/*      */   static final HashMap<String, Class<? extends Collection>> _collectionFallbacks;
/*      */   
/*   61 */   static { _mapFallbacks.put(Map.class.getName(), java.util.LinkedHashMap.class);
/*   62 */     _mapFallbacks.put(java.util.concurrent.ConcurrentMap.class.getName(), java.util.concurrent.ConcurrentHashMap.class);
/*   63 */     _mapFallbacks.put(java.util.SortedMap.class.getName(), java.util.TreeMap.class);
/*      */     
/*   65 */     _mapFallbacks.put(java.util.NavigableMap.class.getName(), java.util.TreeMap.class);
/*   66 */     _mapFallbacks.put(java.util.concurrent.ConcurrentNavigableMap.class.getName(), java.util.concurrent.ConcurrentSkipListMap.class);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   75 */     _collectionFallbacks = new HashMap();
/*      */     
/*      */ 
/*   78 */     _collectionFallbacks.put(Collection.class.getName(), java.util.ArrayList.class);
/*   79 */     _collectionFallbacks.put(java.util.List.class.getName(), java.util.ArrayList.class);
/*   80 */     _collectionFallbacks.put(java.util.Set.class.getName(), java.util.HashSet.class);
/*   81 */     _collectionFallbacks.put(java.util.SortedSet.class.getName(), java.util.TreeSet.class);
/*   82 */     _collectionFallbacks.put(java.util.Queue.class.getName(), java.util.LinkedList.class);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   89 */     _collectionFallbacks.put("java.util.Deque", java.util.LinkedList.class);
/*   90 */     _collectionFallbacks.put("java.util.NavigableSet", java.util.TreeSet.class);
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
/*      */   protected BasicDeserializerFactory(DeserializerFactoryConfig config)
/*      */   {
/*  112 */     this._factoryConfig = config;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DeserializerFactoryConfig getFactoryConfig()
/*      */   {
/*  123 */     return this._factoryConfig;
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
/*      */   public final DeserializerFactory withAdditionalDeserializers(Deserializers additional)
/*      */   {
/*  140 */     return withConfig(this._factoryConfig.withAdditionalDeserializers(additional));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final DeserializerFactory withAdditionalKeyDeserializers(KeyDeserializers additional)
/*      */   {
/*  149 */     return withConfig(this._factoryConfig.withAdditionalKeyDeserializers(additional));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier modifier)
/*      */   {
/*  158 */     return withConfig(this._factoryConfig.withDeserializerModifier(modifier));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final DeserializerFactory withAbstractTypeResolver(AbstractTypeResolver resolver)
/*      */   {
/*  167 */     return withConfig(this._factoryConfig.withAbstractTypeResolver(resolver));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final DeserializerFactory withValueInstantiators(ValueInstantiators instantiators)
/*      */   {
/*  176 */     return withConfig(this._factoryConfig.withValueInstantiators(instantiators));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType mapAbstractType(DeserializationConfig config, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*      */     for (;;)
/*      */     {
/*  191 */       JavaType next = _mapAbstractType2(config, type);
/*  192 */       if (next == null) {
/*  193 */         return type;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  199 */       Class<?> prevCls = type.getRawClass();
/*  200 */       Class<?> nextCls = next.getRawClass();
/*  201 */       if ((prevCls == nextCls) || (!prevCls.isAssignableFrom(nextCls))) {
/*  202 */         throw new IllegalArgumentException("Invalid abstract type resolution from " + type + " to " + next + ": latter is not a subtype of former");
/*      */       }
/*  204 */       type = next;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JavaType _mapAbstractType2(DeserializationConfig config, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  215 */     Class<?> currClass = type.getRawClass();
/*  216 */     if (this._factoryConfig.hasAbstractTypeResolvers()) {
/*  217 */       for (AbstractTypeResolver resolver : this._factoryConfig.abstractTypeResolvers()) {
/*  218 */         JavaType concrete = resolver.findTypeMapping(config, type);
/*  219 */         if ((concrete != null) && (concrete.getRawClass() != currClass)) {
/*  220 */           return concrete;
/*      */         }
/*      */       }
/*      */     }
/*  224 */     return null;
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
/*      */   public ValueInstantiator findValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  243 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*  245 */     ValueInstantiator instantiator = null;
/*      */     
/*  247 */     com.fasterxml.jackson.databind.introspect.AnnotatedClass ac = beanDesc.getClassInfo();
/*  248 */     Object instDef = ctxt.getAnnotationIntrospector().findValueInstantiator(ac);
/*  249 */     if (instDef != null) {
/*  250 */       instantiator = _valueInstantiatorInstance(config, ac, instDef);
/*      */     }
/*  252 */     if (instantiator == null)
/*      */     {
/*      */ 
/*      */ 
/*  256 */       instantiator = _findStdValueInstantiator(config, beanDesc);
/*  257 */       if (instantiator == null) {
/*  258 */         instantiator = _constructDefaultValueInstantiator(ctxt, beanDesc);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  263 */     if (this._factoryConfig.hasValueInstantiators()) {
/*  264 */       for (ValueInstantiators insts : this._factoryConfig.valueInstantiators()) {
/*  265 */         instantiator = insts.findValueInstantiator(config, beanDesc, instantiator);
/*      */         
/*  267 */         if (instantiator == null) {
/*  268 */           throw new JsonMappingException("Broken registered ValueInstantiators (of type " + insts.getClass().getName() + "): returned null ValueInstantiator");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  275 */     if (instantiator.getIncompleteParameter() != null) {
/*  276 */       AnnotatedParameter nonAnnotatedParam = instantiator.getIncompleteParameter();
/*  277 */       AnnotatedWithParams ctor = nonAnnotatedParam.getOwner();
/*  278 */       throw new IllegalArgumentException("Argument #" + nonAnnotatedParam.getIndex() + " of constructor " + ctor + " has no property name annotation; must have name when multiple-parameter constructor annotated as Creator");
/*      */     }
/*      */     
/*  281 */     return instantiator;
/*      */   }
/*      */   
/*      */ 
/*      */   private ValueInstantiator _findStdValueInstantiator(DeserializationConfig config, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  288 */     if (beanDesc.getBeanClass() == com.fasterxml.jackson.core.JsonLocation.class) {
/*  289 */       return new com.fasterxml.jackson.databind.deser.std.JsonLocationInstantiator();
/*      */     }
/*  291 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ValueInstantiator _constructDefaultValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  302 */     boolean fixAccess = ctxt.canOverrideAccessModifiers();
/*  303 */     CreatorCollector creators = new CreatorCollector(beanDesc, fixAccess);
/*  304 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*      */     
/*      */ 
/*  307 */     DeserializationConfig config = ctxt.getConfig();
/*  308 */     VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
/*  309 */     vchecker = intr.findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  316 */     Map<AnnotatedWithParams, BeanPropertyDefinition[]> creatorDefs = _findCreatorsFromProperties(ctxt, beanDesc);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  322 */     _addDeserializerFactoryMethods(ctxt, beanDesc, vchecker, intr, creators, creatorDefs);
/*      */     
/*  324 */     if (beanDesc.getType().isConcrete()) {
/*  325 */       _addDeserializerConstructors(ctxt, beanDesc, vchecker, intr, creators, creatorDefs);
/*      */     }
/*  327 */     return creators.constructValueInstantiator(config);
/*      */   }
/*      */   
/*      */   protected Map<AnnotatedWithParams, BeanPropertyDefinition[]> _findCreatorsFromProperties(DeserializationContext ctxt, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  333 */     Map<AnnotatedWithParams, BeanPropertyDefinition[]> result = java.util.Collections.emptyMap();
/*  334 */     for (BeanPropertyDefinition propDef : beanDesc.findProperties()) {
/*  335 */       java.util.Iterator<AnnotatedParameter> it = propDef.getConstructorParameters();
/*  336 */       while (it.hasNext()) {
/*  337 */         AnnotatedParameter param = (AnnotatedParameter)it.next();
/*  338 */         AnnotatedWithParams owner = param.getOwner();
/*  339 */         BeanPropertyDefinition[] defs = (BeanPropertyDefinition[])result.get(owner);
/*  340 */         int index = param.getIndex();
/*      */         
/*  342 */         if (defs == null) {
/*  343 */           if (result.isEmpty()) {
/*  344 */             result = new java.util.LinkedHashMap();
/*      */           }
/*  346 */           defs = new BeanPropertyDefinition[owner.getParameterCount()];
/*  347 */           result.put(owner, defs);
/*      */         }
/*  349 */         else if (defs[index] != null) {
/*  350 */           throw new IllegalStateException("Conflict: parameter #" + index + " of " + owner + " bound to more than one property; " + defs[index] + " vs " + propDef);
/*      */         }
/*      */         
/*      */ 
/*  354 */         defs[index] = propDef;
/*      */       }
/*      */     }
/*  357 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public ValueInstantiator _valueInstantiatorInstance(DeserializationConfig config, Annotated annotated, Object instDef)
/*      */     throws JsonMappingException
/*      */   {
/*  364 */     if (instDef == null) {
/*  365 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  370 */     if ((instDef instanceof ValueInstantiator)) {
/*  371 */       return (ValueInstantiator)instDef;
/*      */     }
/*  373 */     if (!(instDef instanceof Class)) {
/*  374 */       throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + instDef.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
/*      */     }
/*      */     
/*      */ 
/*  378 */     Class<?> instClass = (Class)instDef;
/*  379 */     if (ClassUtil.isBogusClass(instClass)) {
/*  380 */       return null;
/*      */     }
/*  382 */     if (!ValueInstantiator.class.isAssignableFrom(instClass)) {
/*  383 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + instClass.getName() + "; expected Class<ValueInstantiator>");
/*      */     }
/*      */     
/*  386 */     com.fasterxml.jackson.databind.cfg.HandlerInstantiator hi = config.getHandlerInstantiator();
/*  387 */     if (hi != null) {
/*  388 */       ValueInstantiator inst = hi.valueInstantiatorInstance(config, annotated, instClass);
/*  389 */       if (inst != null) {
/*  390 */         return inst;
/*      */       }
/*      */     }
/*  393 */     return (ValueInstantiator)ClassUtil.createInstance(instClass, config.canOverrideAccessModifiers());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected void _addDeserializerConstructors(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators)
/*      */     throws JsonMappingException
/*      */   {
/*  402 */     _addDeserializerConstructors(ctxt, beanDesc, vchecker, intr, creators, java.util.Collections.emptyMap());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addDeserializerConstructors(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, Map<AnnotatedWithParams, BeanPropertyDefinition[]> creatorParams)
/*      */     throws JsonMappingException
/*      */   {
/*  415 */     AnnotatedConstructor defaultCtor = beanDesc.findDefaultConstructor();
/*  416 */     if ((defaultCtor != null) && (
/*  417 */       (!creators.hasDefaultCreator()) || (intr.hasCreatorAnnotation(defaultCtor)))) {
/*  418 */       creators.setDefaultCreator(defaultCtor);
/*      */     }
/*      */     
/*  421 */     for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
/*  422 */       boolean isCreator = intr.hasCreatorAnnotation(ctor);
/*  423 */       BeanPropertyDefinition[] propDefs = (BeanPropertyDefinition[])creatorParams.get(ctor);
/*  424 */       int argCount = ctor.getParameterCount();
/*      */       
/*      */ 
/*  427 */       if (argCount == 1) {
/*  428 */         BeanPropertyDefinition argDef = propDefs == null ? null : propDefs[0];
/*  429 */         boolean useProps = _checkIfCreatorPropertyBased(intr, ctor, argDef);
/*      */         
/*  431 */         if (useProps) {
/*  432 */           CreatorProperty[] properties = new CreatorProperty[1];
/*  433 */           PropertyName name = argDef == null ? null : argDef.getFullName();
/*  434 */           AnnotatedParameter arg = ctor.getParameter(0);
/*  435 */           properties[0] = constructCreatorProperty(ctxt, beanDesc, name, 0, arg, intr.findInjectableValueId(arg));
/*      */           
/*  437 */           creators.addPropertyCreator(ctor, isCreator, properties);
/*      */         } else {
/*  439 */           _handleSingleArgumentConstructor(ctxt, beanDesc, vchecker, intr, creators, ctor, isCreator, vchecker.isCreatorVisible(ctor));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  444 */           if (argDef != null) {
/*  445 */             ((com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder)argDef).removeConstructors();
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/*  453 */         AnnotatedParameter nonAnnotatedParam = null;
/*  454 */         CreatorProperty[] properties = new CreatorProperty[argCount];
/*  455 */         int explicitNameCount = 0;
/*  456 */         int implicitNameCount = 0;
/*  457 */         int injectCount = 0;
/*  458 */         for (int i = 0; i < argCount; i++) {
/*  459 */           AnnotatedParameter param = ctor.getParameter(i);
/*  460 */           BeanPropertyDefinition propDef = propDefs == null ? null : propDefs[i];
/*  461 */           Object injectId = intr.findInjectableValueId(param);
/*  462 */           PropertyName name = propDef == null ? null : propDef.getFullName();
/*  463 */           if ((propDef != null) && (propDef.isExplicitlyNamed())) {
/*  464 */             explicitNameCount++;
/*  465 */             properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */ 
/*      */           }
/*  468 */           else if (injectId != null) {
/*  469 */             injectCount++;
/*  470 */             properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */           }
/*      */           else {
/*  473 */             com.fasterxml.jackson.databind.util.NameTransformer unwrapper = intr.findUnwrappingNameTransformer(param);
/*  474 */             if (unwrapper != null) {
/*  475 */               properties[i] = constructCreatorProperty(ctxt, beanDesc, UNWRAPPED_CREATOR_PARAM_NAME, i, param, null);
/*  476 */               explicitNameCount++;
/*      */ 
/*      */ 
/*      */             }
/*  480 */             else if ((isCreator) && 
/*  481 */               (name != null) && (!name.isEmpty())) {
/*  482 */               implicitNameCount++;
/*  483 */               properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */ 
/*      */ 
/*      */             }
/*  487 */             else if (nonAnnotatedParam == null) {
/*  488 */               nonAnnotatedParam = param;
/*      */             }
/*      */           }
/*      */         }
/*  492 */         int namedCount = explicitNameCount + implicitNameCount;
/*      */         
/*  494 */         if ((isCreator) || (explicitNameCount > 0) || (injectCount > 0))
/*      */         {
/*  496 */           if (namedCount + injectCount == argCount) {
/*  497 */             creators.addPropertyCreator(ctor, isCreator, properties);
/*  498 */           } else if ((explicitNameCount == 0) && (injectCount + 1 == argCount))
/*      */           {
/*  500 */             creators.addDelegatingCreator(ctor, isCreator, properties);
/*      */           }
/*      */           else {
/*  503 */             int ix = nonAnnotatedParam.getIndex();
/*  504 */             if ((ix == 0) && (ClassUtil.isNonStaticInnerClass(ctor.getDeclaringClass()))) {
/*  505 */               throw new IllegalArgumentException("Non-static inner classes like " + ctor.getDeclaringClass().getName() + " can not use @JsonCreator for constructors");
/*      */             }
/*      */             
/*  508 */             throw new IllegalArgumentException("Argument #" + ix + " of constructor " + ctor + " has no property name annotation; must have name when multiple-parameter constructor annotated as Creator");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean _checkIfCreatorPropertyBased(AnnotationIntrospector intr, AnnotatedWithParams creator, BeanPropertyDefinition propDef)
/*      */   {
/*  518 */     com.fasterxml.jackson.annotation.JsonCreator.Mode mode = intr.findCreatorBinding(creator);
/*      */     
/*  520 */     if (mode == com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES) {
/*  521 */       return true;
/*      */     }
/*  523 */     if (mode == com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING) {
/*  524 */       return false;
/*      */     }
/*      */     
/*  527 */     if (((propDef != null) && (propDef.isExplicitlyNamed())) || (intr.findInjectableValueId(creator.getParameter(0)) != null))
/*      */     {
/*  529 */       return true;
/*      */     }
/*  531 */     if (propDef != null)
/*      */     {
/*      */ 
/*  534 */       String implName = propDef.getName();
/*  535 */       if ((implName != null) && (!implName.isEmpty()) && 
/*  536 */         (propDef.couldSerialize())) {
/*  537 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  542 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _handleSingleArgumentConstructor(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, AnnotatedConstructor ctor, boolean isCreator, boolean isVisible)
/*      */     throws JsonMappingException
/*      */   {
/*  552 */     Class<?> type = ctor.getRawParameterType(0);
/*  553 */     if ((type == String.class) || (type == CharSequence.class)) {
/*  554 */       if ((isCreator) || (isVisible)) {
/*  555 */         creators.addStringCreator(ctor, isCreator);
/*      */       }
/*  557 */       return true;
/*      */     }
/*  559 */     if ((type == Integer.TYPE) || (type == Integer.class)) {
/*  560 */       if ((isCreator) || (isVisible)) {
/*  561 */         creators.addIntCreator(ctor, isCreator);
/*      */       }
/*  563 */       return true;
/*      */     }
/*  565 */     if ((type == Long.TYPE) || (type == Long.class)) {
/*  566 */       if ((isCreator) || (isVisible)) {
/*  567 */         creators.addLongCreator(ctor, isCreator);
/*      */       }
/*  569 */       return true;
/*      */     }
/*  571 */     if ((type == Double.TYPE) || (type == Double.class)) {
/*  572 */       if ((isCreator) || (isVisible)) {
/*  573 */         creators.addDoubleCreator(ctor, isCreator);
/*      */       }
/*  575 */       return true;
/*      */     }
/*  577 */     if ((type == Boolean.TYPE) || (type == Boolean.class)) {
/*  578 */       if ((isCreator) || (isVisible)) {
/*  579 */         creators.addBooleanCreator(ctor, isCreator);
/*      */       }
/*  581 */       return true;
/*      */     }
/*      */     
/*  584 */     if (isCreator) {
/*  585 */       creators.addDelegatingCreator(ctor, isCreator, null);
/*  586 */       return true;
/*      */     }
/*  588 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   protected void _addDeserializerFactoryMethods(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators)
/*      */     throws JsonMappingException
/*      */   {
/*  596 */     _addDeserializerFactoryMethods(ctxt, beanDesc, vchecker, intr, creators, java.util.Collections.emptyMap());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addDeserializerFactoryMethods(DeserializationContext ctxt, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, Map<AnnotatedWithParams, BeanPropertyDefinition[]> creatorParams)
/*      */     throws JsonMappingException
/*      */   {
/*  606 */     DeserializationConfig config = ctxt.getConfig();
/*  607 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/*  608 */       boolean isCreator = intr.hasCreatorAnnotation(factory);
/*  609 */       int argCount = factory.getParameterCount();
/*      */       
/*  611 */       if (argCount == 0) {
/*  612 */         if (isCreator) {
/*  613 */           creators.setDefaultCreator(factory);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  618 */         BeanPropertyDefinition[] propDefs = (BeanPropertyDefinition[])creatorParams.get(factory);
/*      */         
/*  620 */         if (argCount == 1) {
/*  621 */           BeanPropertyDefinition argDef = propDefs == null ? null : propDefs[0];
/*  622 */           boolean useProps = _checkIfCreatorPropertyBased(intr, factory, argDef);
/*  623 */           if (!useProps) {
/*  624 */             _handleSingleArgumentFactory(config, beanDesc, vchecker, intr, creators, factory, isCreator);
/*      */             
/*      */ 
/*  627 */             continue;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  632 */           if (!isCreator) {
/*      */             continue;
/*      */           }
/*      */         }
/*      */         
/*  637 */         AnnotatedParameter nonAnnotatedParam = null;
/*  638 */         CreatorProperty[] properties = new CreatorProperty[argCount];
/*  639 */         int implicitNameCount = 0;
/*  640 */         int explicitNameCount = 0;
/*  641 */         int injectCount = 0;
/*      */         
/*  643 */         for (int i = 0; i < argCount; i++) {
/*  644 */           AnnotatedParameter param = factory.getParameter(i);
/*  645 */           BeanPropertyDefinition propDef = propDefs == null ? null : propDefs[i];
/*  646 */           Object injectId = intr.findInjectableValueId(param);
/*  647 */           PropertyName name = propDef == null ? null : propDef.getFullName();
/*      */           
/*  649 */           if ((propDef != null) && (propDef.isExplicitlyNamed())) {
/*  650 */             explicitNameCount++;
/*  651 */             properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */ 
/*      */           }
/*  654 */           else if (injectId != null) {
/*  655 */             injectCount++;
/*  656 */             properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
/*      */           }
/*      */           else {
/*  659 */             com.fasterxml.jackson.databind.util.NameTransformer unwrapper = intr.findUnwrappingNameTransformer(param);
/*  660 */             if (unwrapper != null) {
/*  661 */               properties[i] = constructCreatorProperty(ctxt, beanDesc, UNWRAPPED_CREATOR_PARAM_NAME, i, param, null);
/*  662 */               implicitNameCount++;
/*      */ 
/*      */ 
/*      */             }
/*  666 */             else if ((isCreator) && 
/*  667 */               (name != null) && (!name.isEmpty())) {
/*  668 */               implicitNameCount++;
/*  669 */               properties[i] = constructCreatorProperty(ctxt, beanDesc, name, i, param, injectId);
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
/*      */             }
/*  685 */             else if (nonAnnotatedParam == null) {
/*  686 */               nonAnnotatedParam = param;
/*      */             }
/*      */           } }
/*  689 */         int namedCount = explicitNameCount + implicitNameCount;
/*      */         
/*      */ 
/*  692 */         if ((isCreator) || (explicitNameCount > 0) || (injectCount > 0))
/*      */         {
/*  694 */           if (namedCount + injectCount == argCount) {
/*  695 */             creators.addPropertyCreator(factory, isCreator, properties);
/*  696 */           } else if ((explicitNameCount == 0) && (injectCount + 1 == argCount))
/*      */           {
/*  698 */             creators.addDelegatingCreator(factory, isCreator, properties);
/*      */           } else {
/*  700 */             throw new IllegalArgumentException("Argument #" + nonAnnotatedParam.getIndex() + " of factory method " + factory + " has no property name annotation; must have name when multiple-parameter constructor annotated as Creator");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _handleSingleArgumentFactory(DeserializationConfig config, BeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorCollector creators, AnnotatedMethod factory, boolean isCreator)
/*      */     throws JsonMappingException
/*      */   {
/*  713 */     Class<?> type = factory.getRawParameterType(0);
/*      */     
/*  715 */     if ((type == String.class) || (type == CharSequence.class)) {
/*  716 */       if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  717 */         creators.addStringCreator(factory, isCreator);
/*      */       }
/*  719 */       return true;
/*      */     }
/*  721 */     if ((type == Integer.TYPE) || (type == Integer.class)) {
/*  722 */       if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  723 */         creators.addIntCreator(factory, isCreator);
/*      */       }
/*  725 */       return true;
/*      */     }
/*  727 */     if ((type == Long.TYPE) || (type == Long.class)) {
/*  728 */       if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  729 */         creators.addLongCreator(factory, isCreator);
/*      */       }
/*  731 */       return true;
/*      */     }
/*  733 */     if ((type == Double.TYPE) || (type == Double.class)) {
/*  734 */       if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  735 */         creators.addDoubleCreator(factory, isCreator);
/*      */       }
/*  737 */       return true;
/*      */     }
/*  739 */     if ((type == Boolean.TYPE) || (type == Boolean.class)) {
/*  740 */       if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  741 */         creators.addBooleanCreator(factory, isCreator);
/*      */       }
/*  743 */       return true;
/*      */     }
/*  745 */     if (isCreator) {
/*  746 */       creators.addDelegatingCreator(factory, isCreator, null);
/*  747 */       return true;
/*      */     }
/*  749 */     return false;
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
/*      */   protected CreatorProperty constructCreatorProperty(DeserializationContext ctxt, BeanDescription beanDesc, PropertyName name, int index, AnnotatedParameter param, Object injectableValueId)
/*      */     throws JsonMappingException
/*      */   {
/*  763 */     DeserializationConfig config = ctxt.getConfig();
/*  764 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*      */     com.fasterxml.jackson.databind.PropertyMetadata metadata;
/*      */     com.fasterxml.jackson.databind.PropertyMetadata metadata;
/*  767 */     if (intr == null) {
/*  768 */       metadata = com.fasterxml.jackson.databind.PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
/*      */     } else {
/*  770 */       Boolean b = intr.hasRequiredMarker(param);
/*  771 */       boolean req = (b != null) && (b.booleanValue());
/*  772 */       String desc = intr.findPropertyDescription(param);
/*  773 */       Integer idx = intr.findPropertyIndex(param);
/*  774 */       String def = intr.findPropertyDefaultValue(param);
/*  775 */       metadata = com.fasterxml.jackson.databind.PropertyMetadata.construct(req, desc, idx, def);
/*      */     }
/*      */     
/*      */ 
/*  779 */     JavaType t0 = config.getTypeFactory().constructType(param.getParameterType(), beanDesc.bindingsForBeanType());
/*  780 */     BeanProperty.Std property = new BeanProperty.Std(name, t0, intr.findWrapperName(param), beanDesc.getClassAnnotations(), param, metadata);
/*      */     
/*      */ 
/*  783 */     JavaType type = resolveType(ctxt, beanDesc, t0, param);
/*  784 */     if (type != t0) {
/*  785 */       property = property.withType(type);
/*      */     }
/*      */     
/*  788 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, param);
/*      */     
/*      */ 
/*  791 */     type = modifyTypeByAnnotation(ctxt, param, type);
/*      */     
/*      */ 
/*  794 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*      */     
/*  796 */     if (typeDeser == null) {
/*  797 */       typeDeser = findTypeDeserializer(config, type);
/*      */     }
/*      */     
/*      */ 
/*  801 */     CreatorProperty prop = new CreatorProperty(name, type, property.getWrapperName(), typeDeser, beanDesc.getClassAnnotations(), param, index, injectableValueId, metadata);
/*      */     
/*      */ 
/*  804 */     if (deser != null)
/*      */     {
/*  806 */       deser = ctxt.handlePrimaryContextualization(deser, prop, type);
/*  807 */       prop = prop.withValueDeserializer(deser);
/*      */     }
/*  809 */     return prop;
/*      */   }
/*      */   
/*      */   protected PropertyName _findParamName(AnnotatedParameter param, AnnotationIntrospector intr)
/*      */   {
/*  814 */     if ((param != null) && (intr != null)) {
/*  815 */       PropertyName name = intr.findNameForDeserialization(param);
/*  816 */       if (name != null) {
/*  817 */         return name;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  822 */       String str = intr.findImplicitPropertyName(param);
/*  823 */       if ((str != null) && (!str.isEmpty())) {
/*  824 */         return new PropertyName(str);
/*      */       }
/*      */     }
/*  827 */     return null;
/*      */   }
/*      */   
/*      */   protected PropertyName _findExplicitParamName(AnnotatedParameter param, AnnotationIntrospector intr)
/*      */   {
/*  832 */     if ((param != null) && (intr != null)) {
/*  833 */       return intr.findNameForDeserialization(param);
/*      */     }
/*  835 */     return null;
/*      */   }
/*      */   
/*      */   protected PropertyName _findImplicitParamName(AnnotatedParameter param, AnnotationIntrospector intr)
/*      */   {
/*  840 */     String str = intr.findImplicitPropertyName(param);
/*  841 */     if ((str != null) && (!str.isEmpty())) {
/*  842 */       return new PropertyName(str);
/*      */     }
/*  844 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean _hasExplicitParamName(AnnotatedParameter param, AnnotationIntrospector intr)
/*      */   {
/*  849 */     if ((param != null) && (intr != null)) {
/*  850 */       PropertyName n = intr.findNameForDeserialization(param);
/*  851 */       return (n != null) && (n.hasSimpleName());
/*      */     }
/*  853 */     return false;
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
/*      */   public JsonDeserializer<?> createArrayDeserializer(DeserializationContext ctxt, ArrayType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  867 */     DeserializationConfig config = ctxt.getConfig();
/*  868 */     JavaType elemType = type.getContentType();
/*      */     
/*      */ 
/*  871 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer)elemType.getValueHandler();
/*      */     
/*  873 */     TypeDeserializer elemTypeDeser = (TypeDeserializer)elemType.getTypeHandler();
/*      */     
/*  875 */     if (elemTypeDeser == null) {
/*  876 */       elemTypeDeser = findTypeDeserializer(config, elemType);
/*      */     }
/*      */     
/*  879 */     JsonDeserializer<?> deser = _findCustomArrayDeserializer(type, config, beanDesc, elemTypeDeser, contentDeser);
/*      */     
/*  881 */     if (deser == null) {
/*  882 */       if (contentDeser == null) {
/*  883 */         Class<?> raw = elemType.getRawClass();
/*  884 */         if (elemType.isPrimitive())
/*  885 */           return com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers.forType(raw);
/*  886 */         if (raw == String.class) {
/*  887 */           return com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer.instance;
/*      */         }
/*      */       }
/*  890 */       deser = new com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer(type, contentDeser, elemTypeDeser);
/*      */     }
/*      */     
/*  893 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  894 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  895 */         deser = mod.modifyArrayDeserializer(config, type, beanDesc, deser);
/*      */       }
/*      */     }
/*  898 */     return deser;
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
/*      */   public JsonDeserializer<?> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  912 */     JavaType contentType = type.getContentType();
/*      */     
/*  914 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer)contentType.getValueHandler();
/*  915 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*      */ 
/*  918 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/*  920 */     if (contentTypeDeser == null) {
/*  921 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/*      */     
/*      */ 
/*  925 */     JsonDeserializer<?> deser = _findCustomCollectionDeserializer(type, config, beanDesc, contentTypeDeser, contentDeser);
/*      */     
/*  927 */     if (deser == null) {
/*  928 */       Class<?> collectionClass = type.getRawClass();
/*  929 */       if (contentDeser == null)
/*      */       {
/*  931 */         if (java.util.EnumSet.class.isAssignableFrom(collectionClass)) {
/*  932 */           deser = new com.fasterxml.jackson.databind.deser.std.EnumSetDeserializer(contentType, null);
/*      */         }
/*      */       }
/*      */     }
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
/*  946 */     if (deser == null) {
/*  947 */       if ((type.isInterface()) || (type.isAbstract())) {
/*  948 */         CollectionType implType = _mapAbstractCollectionType(type, config);
/*  949 */         if (implType == null)
/*      */         {
/*  951 */           if (type.getTypeHandler() == null) {
/*  952 */             throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + type);
/*      */           }
/*  954 */           deser = AbstractDeserializer.constructForNonPOJO(beanDesc);
/*      */         } else {
/*  956 */           type = implType;
/*      */           
/*  958 */           beanDesc = config.introspectForCreation(type);
/*      */         }
/*      */       }
/*  961 */       if (deser == null) {
/*  962 */         ValueInstantiator inst = findValueInstantiator(ctxt, beanDesc);
/*  963 */         if (!inst.canCreateUsingDefault())
/*      */         {
/*  965 */           if (type.getRawClass() == java.util.concurrent.ArrayBlockingQueue.class) {
/*  966 */             return new com.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer(type, contentDeser, contentTypeDeser, inst, null);
/*      */           }
/*      */         }
/*      */         
/*  970 */         if (contentType.getRawClass() == String.class)
/*      */         {
/*  972 */           deser = new com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer(type, contentDeser, inst);
/*      */         } else {
/*  974 */           deser = new com.fasterxml.jackson.databind.deser.std.CollectionDeserializer(type, contentDeser, contentTypeDeser, inst);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  979 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  980 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  981 */         deser = mod.modifyCollectionDeserializer(config, type, beanDesc, deser);
/*      */       }
/*      */     }
/*  984 */     return deser;
/*      */   }
/*      */   
/*      */   protected CollectionType _mapAbstractCollectionType(JavaType type, DeserializationConfig config)
/*      */   {
/*  989 */     Class<?> collectionClass = type.getRawClass();
/*  990 */     collectionClass = (Class)_collectionFallbacks.get(collectionClass.getName());
/*  991 */     if (collectionClass == null) {
/*  992 */       return null;
/*      */     }
/*  994 */     return (CollectionType)config.constructSpecializedType(type, collectionClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationContext ctxt, CollectionLikeType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1003 */     JavaType contentType = type.getContentType();
/*      */     
/* 1005 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer)contentType.getValueHandler();
/* 1006 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*      */ 
/* 1009 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1011 */     if (contentTypeDeser == null) {
/* 1012 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/* 1014 */     JsonDeserializer<?> deser = _findCustomCollectionLikeDeserializer(type, config, beanDesc, contentTypeDeser, contentDeser);
/*      */     
/* 1016 */     if (deser != null)
/*      */     {
/* 1018 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1019 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1020 */           deser = mod.modifyCollectionLikeDeserializer(config, type, beanDesc, deser);
/*      */         }
/*      */       }
/*      */     }
/* 1024 */     return deser;
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
/*      */   public JsonDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1038 */     DeserializationConfig config = ctxt.getConfig();
/* 1039 */     JavaType keyType = type.getKeyType();
/* 1040 */     JavaType contentType = type.getContentType();
/*      */     
/*      */ 
/*      */ 
/* 1044 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer)contentType.getValueHandler();
/*      */     
/*      */ 
/* 1047 */     KeyDeserializer keyDes = (KeyDeserializer)keyType.getValueHandler();
/*      */     
/* 1049 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1051 */     if (contentTypeDeser == null) {
/* 1052 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/*      */     
/*      */ 
/* 1056 */     JsonDeserializer<?> deser = _findCustomMapDeserializer(type, config, beanDesc, keyDes, contentTypeDeser, contentDeser);
/*      */     
/*      */ 
/* 1059 */     if (deser == null)
/*      */     {
/* 1061 */       Class<?> mapClass = type.getRawClass();
/* 1062 */       if (java.util.EnumMap.class.isAssignableFrom(mapClass)) {
/* 1063 */         Class<?> kt = keyType.getRawClass();
/* 1064 */         if ((kt == null) || (!kt.isEnum())) {
/* 1065 */           throw new IllegalArgumentException("Can not construct EnumMap; generic (key) type not available");
/*      */         }
/* 1067 */         deser = new com.fasterxml.jackson.databind.deser.std.EnumMapDeserializer(type, null, contentDeser, contentTypeDeser);
/*      */       }
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
/* 1081 */       if (deser == null) {
/* 1082 */         if ((type.isInterface()) || (type.isAbstract()))
/*      */         {
/* 1084 */           Class<? extends Map> fallback = (Class)_mapFallbacks.get(mapClass.getName());
/* 1085 */           if (fallback != null) {
/* 1086 */             mapClass = fallback;
/* 1087 */             type = (MapType)config.constructSpecializedType(type, mapClass);
/*      */             
/* 1089 */             beanDesc = config.introspectForCreation(type);
/*      */           }
/*      */           else {
/* 1092 */             if (type.getTypeHandler() == null) {
/* 1093 */               throw new IllegalArgumentException("Can not find a deserializer for non-concrete Map type " + type);
/*      */             }
/* 1095 */             deser = AbstractDeserializer.constructForNonPOJO(beanDesc);
/*      */           }
/*      */         }
/* 1098 */         if (deser == null) {
/* 1099 */           ValueInstantiator inst = findValueInstantiator(ctxt, beanDesc);
/* 1100 */           com.fasterxml.jackson.databind.deser.std.MapDeserializer md = new com.fasterxml.jackson.databind.deser.std.MapDeserializer(type, inst, keyDes, contentDeser, contentTypeDeser);
/* 1101 */           md.setIgnorableProperties(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()));
/* 1102 */           deser = md;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1107 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1108 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1109 */         deser = mod.modifyMapDeserializer(config, type, beanDesc, deser);
/*      */       }
/*      */     }
/* 1112 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonDeserializer<?> createMapLikeDeserializer(DeserializationContext ctxt, MapLikeType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1121 */     JavaType keyType = type.getKeyType();
/* 1122 */     JavaType contentType = type.getContentType();
/* 1123 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*      */ 
/*      */ 
/* 1127 */     JsonDeserializer<Object> contentDeser = (JsonDeserializer)contentType.getValueHandler();
/*      */     
/*      */ 
/* 1130 */     KeyDeserializer keyDes = (KeyDeserializer)keyType.getValueHandler();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1137 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*      */     
/* 1139 */     if (contentTypeDeser == null) {
/* 1140 */       contentTypeDeser = findTypeDeserializer(config, contentType);
/*      */     }
/* 1142 */     JsonDeserializer<?> deser = _findCustomMapLikeDeserializer(type, config, beanDesc, keyDes, contentTypeDeser, contentDeser);
/*      */     
/* 1144 */     if (deser != null)
/*      */     {
/* 1146 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1147 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1148 */           deser = mod.modifyMapLikeDeserializer(config, type, beanDesc, deser);
/*      */         }
/*      */       }
/*      */     }
/* 1152 */     return deser;
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
/*      */   public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1169 */     DeserializationConfig config = ctxt.getConfig();
/* 1170 */     Class<?> enumClass = type.getRawClass();
/*      */     
/* 1172 */     JsonDeserializer<?> deser = _findCustomEnumDeserializer(enumClass, config, beanDesc);
/* 1173 */     if (deser == null)
/*      */     {
/* 1175 */       for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/* 1176 */         if (ctxt.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
/* 1177 */           int argCount = factory.getParameterCount();
/* 1178 */           if (argCount == 1) {
/* 1179 */             Class<?> returnType = factory.getRawReturnType();
/*      */             
/* 1181 */             if (returnType.isAssignableFrom(enumClass)) {
/* 1182 */               deser = com.fasterxml.jackson.databind.deser.std.EnumDeserializer.deserializerForCreator(config, enumClass, factory);
/* 1183 */               break;
/*      */             }
/*      */           }
/* 1186 */           throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1191 */       if (deser == null) {
/* 1192 */         deser = new com.fasterxml.jackson.databind.deser.std.EnumDeserializer(constructEnumResolver(enumClass, config, beanDesc.findJsonValueMethod()));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1197 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 1198 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1199 */         deser = mod.modifyEnumDeserializer(config, type, beanDesc, deser);
/*      */       }
/*      */     }
/* 1202 */     return deser;
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
/*      */   public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, JavaType nodeType, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1217 */     Class<? extends com.fasterxml.jackson.databind.JsonNode> nodeClass = nodeType.getRawClass();
/*      */     
/* 1219 */     JsonDeserializer<?> custom = _findCustomTreeNodeDeserializer(nodeClass, config, beanDesc);
/*      */     
/* 1221 */     if (custom != null) {
/* 1222 */       return custom;
/*      */     }
/* 1224 */     return com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer.getDeserializer(nodeClass);
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
/*      */   public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType)
/*      */     throws JsonMappingException
/*      */   {
/* 1238 */     BeanDescription bean = config.introspectClassAnnotations(baseType.getRawClass());
/* 1239 */     com.fasterxml.jackson.databind.introspect.AnnotatedClass ac = bean.getClassInfo();
/* 1240 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 1241 */     TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1246 */     Collection<com.fasterxml.jackson.databind.jsontype.NamedType> subtypes = null;
/* 1247 */     if (b == null) {
/* 1248 */       b = config.getDefaultTyper(baseType);
/* 1249 */       if (b == null) {
/* 1250 */         return null;
/*      */       }
/*      */     } else {
/* 1253 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
/*      */     }
/*      */     
/*      */ 
/* 1257 */     if ((b.getDefaultImpl() == null) && (baseType.isAbstract())) {
/* 1258 */       JavaType defaultType = mapAbstractType(config, baseType);
/* 1259 */       if ((defaultType != null) && (defaultType.getRawClass() != baseType.getRawClass())) {
/* 1260 */         b = b.defaultImpl(defaultType.getRawClass());
/*      */       }
/*      */     }
/* 1263 */     return b.buildTypeDeserializer(config, baseType, subtypes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<?> findOptionalStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1275 */     return com.fasterxml.jackson.databind.ext.OptionalHandlerFactory.instance.findDeserializer(type, ctxt.getConfig(), beanDesc);
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
/*      */   public KeyDeserializer createKeyDeserializer(DeserializationContext ctxt, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/* 1289 */     DeserializationConfig config = ctxt.getConfig();
/* 1290 */     KeyDeserializer deser = null;
/* 1291 */     BeanDescription beanDesc; if (this._factoryConfig.hasKeyDeserializers()) {
/* 1292 */       beanDesc = config.introspectClassAnnotations(type.getRawClass());
/* 1293 */       for (KeyDeserializers d : this._factoryConfig.keyDeserializers()) {
/* 1294 */         deser = d.findKeyDeserializer(type, config, beanDesc);
/* 1295 */         if (deser != null) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1301 */     if (deser == null) {
/* 1302 */       if (type.isEnumType()) {
/* 1303 */         return _createEnumKeyDeserializer(ctxt, type);
/*      */       }
/* 1305 */       deser = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
/*      */     }
/*      */     
/*      */ 
/* 1309 */     if ((deser != null) && 
/* 1310 */       (this._factoryConfig.hasDeserializerModifiers())) {
/* 1311 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 1312 */         deser = mod.modifyKeyDeserializer(config, type, deser);
/*      */       }
/*      */     }
/*      */     
/* 1316 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */   private KeyDeserializer _createEnumKeyDeserializer(DeserializationContext ctxt, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/* 1323 */     DeserializationConfig config = ctxt.getConfig();
/* 1324 */     BeanDescription beanDesc = config.introspect(type);
/* 1325 */     JsonDeserializer<?> des = findDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
/* 1326 */     if (des != null) {
/* 1327 */       return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, type, des);
/*      */     }
/* 1329 */     Class<?> enumClass = type.getRawClass();
/*      */     
/* 1331 */     JsonDeserializer<?> custom = _findCustomEnumDeserializer(enumClass, config, beanDesc);
/* 1332 */     if (custom != null) {
/* 1333 */       return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, type, custom);
/*      */     }
/*      */     
/* 1336 */     EnumResolver<?> enumRes = constructEnumResolver(enumClass, config, beanDesc.findJsonValueMethod());
/*      */     
/* 1338 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/* 1339 */       if (config.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
/* 1340 */         int argCount = factory.getParameterCount();
/* 1341 */         if (argCount == 1) {
/* 1342 */           Class<?> returnType = factory.getRawReturnType();
/*      */           
/* 1344 */           if (returnType.isAssignableFrom(enumClass))
/*      */           {
/* 1346 */             if (factory.getGenericParameterType(0) != String.class) {
/* 1347 */               throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
/*      */             }
/* 1349 */             if (config.canOverrideAccessModifiers()) {
/* 1350 */               ClassUtil.checkAndFixAccess(factory.getMember());
/*      */             }
/* 1352 */             return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes, factory);
/*      */           }
/*      */         }
/* 1355 */         throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1360 */     return StdKeyDeserializers.constructEnumKeyDeserializer(enumRes);
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
/*      */   protected final DeserializerFactoryConfig _factoryConfig;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated)
/*      */     throws JsonMappingException
/*      */   {
/* 1386 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 1387 */     TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, annotated, baseType);
/*      */     
/* 1389 */     if (b == null) {
/* 1390 */       return findTypeDeserializer(config, baseType);
/*      */     }
/*      */     
/* 1393 */     Collection<com.fasterxml.jackson.databind.jsontype.NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(annotated, config, ai, baseType);
/*      */     
/* 1395 */     return b.buildTypeDeserializer(config, baseType, subtypes);
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
/*      */   public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity)
/*      */     throws JsonMappingException
/*      */   {
/* 1413 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 1414 */     TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, propertyEntity, containerType);
/* 1415 */     JavaType contentType = containerType.getContentType();
/*      */     
/* 1417 */     if (b == null) {
/* 1418 */       return findTypeDeserializer(config, contentType);
/*      */     }
/*      */     
/* 1421 */     Collection<com.fasterxml.jackson.databind.jsontype.NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(propertyEntity, config, ai, contentType);
/*      */     
/* 1423 */     return b.buildTypeDeserializer(config, contentType, subtypes);
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
/*      */   public JsonDeserializer<?> findDefaultDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1437 */     Class<?> rawType = type.getRawClass();
/*      */     
/* 1439 */     if (rawType == CLASS_OBJECT) {
/* 1440 */       return new com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer();
/*      */     }
/* 1442 */     if ((rawType == CLASS_STRING) || (rawType == CLASS_CHAR_BUFFER)) {
/* 1443 */       return com.fasterxml.jackson.databind.deser.std.StringDeserializer.instance;
/*      */     }
/* 1445 */     if (rawType == CLASS_ITERABLE)
/*      */     {
/* 1447 */       TypeFactory tf = ctxt.getTypeFactory();
/* 1448 */       JavaType[] tps = tf.findTypeParameters(type, CLASS_ITERABLE);
/* 1449 */       JavaType elemType = (tps == null) || (tps.length != 1) ? TypeFactory.unknownType() : tps[0];
/* 1450 */       CollectionType ct = tf.constructCollectionType(Collection.class, elemType);
/*      */       
/* 1452 */       return createCollectionDeserializer(ctxt, ct, beanDesc);
/*      */     }
/* 1454 */     if (rawType == CLASS_MAP_ENTRY) {
/* 1455 */       DeserializationConfig config = ctxt.getConfig();
/* 1456 */       TypeFactory tf = ctxt.getTypeFactory();
/* 1457 */       JavaType[] tps = tf.findTypeParameters(type, CLASS_MAP_ENTRY);
/*      */       JavaType kt;
/* 1459 */       JavaType kt; JavaType vt; if ((tps == null) || (tps.length != 2)) { JavaType vt;
/* 1460 */         kt = vt = TypeFactory.unknownType();
/*      */       } else {
/* 1462 */         kt = tps[0];
/* 1463 */         vt = tps[1];
/*      */       }
/* 1465 */       TypeDeserializer vts = (TypeDeserializer)vt.getTypeHandler();
/* 1466 */       if (vts == null) {
/* 1467 */         vts = findTypeDeserializer(config, vt);
/*      */       }
/* 1469 */       JsonDeserializer<Object> valueDeser = (JsonDeserializer)vt.getValueHandler();
/* 1470 */       KeyDeserializer keyDes = (KeyDeserializer)kt.getValueHandler();
/* 1471 */       return new com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer(type, keyDes, valueDeser, vts);
/*      */     }
/* 1473 */     String clsName = rawType.getName();
/* 1474 */     if ((rawType.isPrimitive()) || (clsName.startsWith("java.")))
/*      */     {
/* 1476 */       JsonDeserializer<?> deser = com.fasterxml.jackson.databind.deser.std.NumberDeserializers.find(rawType, clsName);
/* 1477 */       if (deser == null) {
/* 1478 */         deser = com.fasterxml.jackson.databind.deser.std.DateDeserializers.find(rawType, clsName);
/*      */       }
/* 1480 */       if (deser != null) {
/* 1481 */         return deser;
/*      */       }
/*      */     }
/*      */     
/* 1485 */     if (rawType == com.fasterxml.jackson.databind.util.TokenBuffer.class) {
/* 1486 */       return new com.fasterxml.jackson.databind.deser.std.TokenBufferDeserializer();
/*      */     }
/* 1488 */     if (java.util.concurrent.atomic.AtomicReference.class.isAssignableFrom(rawType))
/*      */     {
/* 1490 */       TypeFactory tf = ctxt.getTypeFactory();
/* 1491 */       JavaType[] params = tf.findTypeParameters(type, java.util.concurrent.atomic.AtomicReference.class);
/*      */       JavaType referencedType;
/* 1493 */       JavaType referencedType; if ((params == null) || (params.length < 1)) {
/* 1494 */         referencedType = TypeFactory.unknownType();
/*      */       } else {
/* 1496 */         referencedType = params[0];
/*      */       }
/* 1498 */       TypeDeserializer vts = findTypeDeserializer(ctxt.getConfig(), referencedType);
/* 1499 */       BeanDescription refdDesc = ctxt.getConfig().introspectClassAnnotations(referencedType);
/* 1500 */       JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, refdDesc.getClassInfo());
/* 1501 */       return new com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer(referencedType, vts, deser);
/*      */     }
/* 1503 */     JsonDeserializer<?> deser = findOptionalStdDeserializer(ctxt, type, beanDesc);
/* 1504 */     if (deser != null) {
/* 1505 */       return deser;
/*      */     }
/* 1507 */     return com.fasterxml.jackson.databind.deser.std.JdkDeserializers.find(rawType, clsName);
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
/*      */   protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/* 1521 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1522 */       JsonDeserializer<?> deser = d.findArrayDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1524 */       if (deser != null) {
/* 1525 */         return deser;
/*      */       }
/*      */     }
/* 1528 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1536 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1537 */       JsonDeserializer<?> deser = d.findBeanDeserializer(type, config, beanDesc);
/* 1538 */       if (deser != null) {
/* 1539 */         return deser;
/*      */       }
/*      */     }
/* 1542 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/* 1550 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1551 */       JsonDeserializer<?> deser = d.findCollectionDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1553 */       if (deser != null) {
/* 1554 */         return deser;
/*      */       }
/*      */     }
/* 1557 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/* 1565 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1566 */       JsonDeserializer<?> deser = d.findCollectionLikeDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1568 */       if (deser != null) {
/* 1569 */         return deser;
/*      */       }
/*      */     }
/* 1572 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1579 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1580 */       JsonDeserializer<?> deser = d.findEnumDeserializer(type, config, beanDesc);
/* 1581 */       if (deser != null) {
/* 1582 */         return deser;
/*      */       }
/*      */     }
/* 1585 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/* 1594 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1595 */       JsonDeserializer<?> deser = d.findMapDeserializer(type, config, beanDesc, keyDeserializer, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1597 */       if (deser != null) {
/* 1598 */         return deser;
/*      */       }
/*      */     }
/* 1601 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType type, DeserializationConfig config, BeanDescription beanDesc, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/* 1610 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1611 */       JsonDeserializer<?> deser = d.findMapLikeDeserializer(type, config, beanDesc, keyDeserializer, elementTypeDeserializer, elementDeserializer);
/*      */       
/* 1613 */       if (deser != null) {
/* 1614 */         return deser;
/*      */       }
/*      */     }
/* 1617 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends com.fasterxml.jackson.databind.JsonNode> type, DeserializationConfig config, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/* 1624 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/* 1625 */       JsonDeserializer<?> deser = d.findTreeNodeDeserializer(type, config, beanDesc);
/* 1626 */       if (deser != null) {
/* 1627 */         return deser;
/*      */       }
/*      */     }
/* 1630 */     return null;
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
/*      */   protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann)
/*      */     throws JsonMappingException
/*      */   {
/* 1648 */     Object deserDef = ctxt.getAnnotationIntrospector().findDeserializer(ann);
/* 1649 */     if (deserDef == null) {
/* 1650 */       return null;
/*      */     }
/* 1652 */     return ctxt.deserializerInstance(ann, deserDef);
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
/*      */   protected <T extends JavaType> T modifyTypeByAnnotation(DeserializationContext ctxt, Annotated a, T type)
/*      */     throws JsonMappingException
/*      */   {
/* 1677 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 1678 */     Class<?> subclass = intr.findDeserializationType(a, type);
/* 1679 */     if (subclass != null) {
/*      */       try {
/* 1681 */         type = type.narrowBy(subclass);
/*      */       } catch (IllegalArgumentException iae) {
/* 1683 */         throw new JsonMappingException("Failed to narrow type " + type + " with concrete-type annotation (value " + subclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage(), null, iae);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1688 */     if (type.isContainerType()) {
/* 1689 */       Class<?> keyClass = intr.findDeserializationKeyType(a, type.getKeyType());
/* 1690 */       if (keyClass != null)
/*      */       {
/* 1692 */         if (!(type instanceof MapLikeType)) {
/* 1693 */           throw new JsonMappingException("Illegal key-type annotation: type " + type + " is not a Map(-like) type");
/*      */         }
/*      */         try {
/* 1696 */           type = ((MapLikeType)type).narrowKey(keyClass);
/*      */         } catch (IllegalArgumentException iae) {
/* 1698 */           throw new JsonMappingException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae.getMessage(), null, iae);
/*      */         }
/*      */       }
/* 1701 */       JavaType keyType = type.getKeyType();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1706 */       if ((keyType != null) && (keyType.getValueHandler() == null)) {
/* 1707 */         Object kdDef = intr.findKeyDeserializer(a);
/* 1708 */         KeyDeserializer kd = ctxt.keyDeserializerInstance(a, kdDef);
/* 1709 */         if (kd != null) {
/* 1710 */           type = ((MapLikeType)type).withKeyValueHandler(kd);
/* 1711 */           keyType = type.getKeyType();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1716 */       Class<?> cc = intr.findDeserializationContentType(a, type.getContentType());
/* 1717 */       if (cc != null) {
/*      */         try {
/* 1719 */           type = type.narrowContentsBy(cc);
/*      */         } catch (IllegalArgumentException iae) {
/* 1721 */           throw new JsonMappingException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae.getMessage(), null, iae);
/*      */         }
/*      */       }
/*      */       
/* 1725 */       JavaType contentType = type.getContentType();
/* 1726 */       if (contentType.getValueHandler() == null) {
/* 1727 */         Object cdDef = intr.findContentDeserializer(a);
/* 1728 */         JsonDeserializer<?> cd = ctxt.deserializerInstance(a, cdDef);
/* 1729 */         if (cd != null) {
/* 1730 */           type = type.withContentValueHandler(cd);
/*      */         }
/*      */       }
/*      */     }
/* 1734 */     return type;
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
/*      */   protected JavaType resolveType(DeserializationContext ctxt, BeanDescription beanDesc, JavaType type, AnnotatedMember member)
/*      */     throws JsonMappingException
/*      */   {
/* 1749 */     if (type.isContainerType()) {
/* 1750 */       AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 1751 */       JavaType keyType = type.getKeyType();
/* 1752 */       if (keyType != null) {
/* 1753 */         Object kdDef = intr.findKeyDeserializer(member);
/* 1754 */         KeyDeserializer kd = ctxt.keyDeserializerInstance(member, kdDef);
/* 1755 */         if (kd != null) {
/* 1756 */           type = ((MapLikeType)type).withKeyValueHandler(kd);
/* 1757 */           keyType = type.getKeyType();
/*      */         }
/*      */       }
/*      */       
/* 1761 */       Object cdDef = intr.findContentDeserializer(member);
/* 1762 */       JsonDeserializer<?> cd = ctxt.deserializerInstance(member, cdDef);
/* 1763 */       if (cd != null) {
/* 1764 */         type = type.withContentValueHandler(cd);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1771 */       if ((member instanceof AnnotatedMember)) {
/* 1772 */         TypeDeserializer contentTypeDeser = findPropertyContentTypeDeserializer(ctxt.getConfig(), type, member);
/*      */         
/* 1774 */         if (contentTypeDeser != null) {
/* 1775 */           type = type.withContentTypeHandler(contentTypeDeser);
/*      */         }
/*      */       }
/*      */     }
/*      */     TypeDeserializer valueTypeDeser;
/*      */     TypeDeserializer valueTypeDeser;
/* 1781 */     if ((member instanceof AnnotatedMember)) {
/* 1782 */       valueTypeDeser = findPropertyTypeDeserializer(ctxt.getConfig(), type, member);
/*      */     }
/*      */     else
/*      */     {
/* 1786 */       valueTypeDeser = findTypeDeserializer(ctxt.getConfig(), type);
/*      */     }
/* 1788 */     if (valueTypeDeser != null) {
/* 1789 */       type = type.withTypeHandler(valueTypeDeser);
/*      */     }
/* 1791 */     return type;
/*      */   }
/*      */   
/*      */ 
/*      */   protected EnumResolver<?> constructEnumResolver(Class<?> enumClass, DeserializationConfig config, AnnotatedMethod jsonValueMethod)
/*      */   {
/* 1797 */     if (jsonValueMethod != null) {
/* 1798 */       java.lang.reflect.Method accessor = jsonValueMethod.getAnnotated();
/* 1799 */       if (config.canOverrideAccessModifiers()) {
/* 1800 */         ClassUtil.checkAndFixAccess(accessor);
/*      */       }
/* 1802 */       return EnumResolver.constructUnsafeUsingMethod(enumClass, accessor);
/*      */     }
/*      */     
/* 1805 */     if (config.isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING)) {
/* 1806 */       return EnumResolver.constructUnsafeUsingToString(enumClass);
/*      */     }
/* 1808 */     return EnumResolver.constructUnsafe(enumClass, config.getAnnotationIntrospector());
/*      */   }
/*      */   
/*      */   protected AnnotatedMethod _findJsonValueFor(DeserializationConfig config, JavaType enumType)
/*      */   {
/* 1813 */     if (enumType == null) {
/* 1814 */       return null;
/*      */     }
/* 1816 */     BeanDescription beanDesc = config.introspect(enumType);
/* 1817 */     return beanDesc.findJsonValueMethod();
/*      */   }
/*      */   
/*      */   protected abstract DeserializerFactory withConfig(DeserializerFactoryConfig paramDeserializerFactoryConfig);
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\BasicDeserializerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */