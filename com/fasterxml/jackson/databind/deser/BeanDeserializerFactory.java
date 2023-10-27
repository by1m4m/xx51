/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.AbstractTypeResolver;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty.Std;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
/*     */ import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BeanDeserializerFactory extends BasicDeserializerFactory implements java.io.Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private static final Class<?>[] INIT_CAUSE_PARAMS = { Throwable.class };
/*     */   
/*  43 */   private static final Class<?>[] NO_VIEWS = new Class[0];
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
/*  55 */   public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
/*     */   
/*     */   public BeanDeserializerFactory(DeserializerFactoryConfig config)
/*     */   {
/*  59 */     super(config);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializerFactory withConfig(DeserializerFactoryConfig config)
/*     */   {
/*  70 */     if (this._factoryConfig == config) {
/*  71 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */     if (getClass() != BeanDeserializerFactory.class) {
/*  80 */       throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
/*     */     }
/*     */     
/*     */ 
/*  84 */     return new BeanDeserializerFactory(config);
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
/*     */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 103 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 105 */     JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, beanDesc);
/* 106 */     if (custom != null) {
/* 107 */       return custom;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 113 */     if (type.isThrowable()) {
/* 114 */       return buildThrowableDeserializer(ctxt, type, beanDesc);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 119 */     if (type.isAbstract())
/*     */     {
/* 121 */       JavaType concreteType = materializeAbstractType(ctxt, type, beanDesc);
/* 122 */       if (concreteType != null)
/*     */       {
/*     */ 
/*     */ 
/* 126 */         beanDesc = config.introspect(concreteType);
/* 127 */         return buildBeanDeserializer(ctxt, concreteType, beanDesc);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 133 */     JsonDeserializer<Object> deser = findStdDeserializer(ctxt, type, beanDesc);
/* 134 */     if (deser != null) {
/* 135 */       return deser;
/*     */     }
/*     */     
/*     */ 
/* 139 */     if (!isPotentialBeanType(type.getRawClass())) {
/* 140 */       return null;
/*     */     }
/*     */     
/* 143 */     return buildBeanDeserializer(ctxt, type, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription beanDesc, Class<?> builderClass)
/*     */     throws JsonMappingException
/*     */   {
/* 153 */     JavaType builderType = ctxt.constructType(builderClass);
/* 154 */     BeanDescription builderDesc = ctxt.getConfig().introspectForBuilder(builderType);
/* 155 */     return buildBuilderBasedDeserializer(ctxt, valueType, builderDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 168 */     JsonDeserializer<?> deser = findDefaultDeserializer(ctxt, type, beanDesc);
/*     */     
/* 170 */     if ((deser != null) && 
/* 171 */       (this._factoryConfig.hasDeserializerModifiers())) {
/* 172 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 173 */         deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
/*     */       }
/*     */     }
/*     */     
/* 177 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */   protected JavaType materializeAbstractType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 184 */     JavaType abstractType = beanDesc.getType();
/*     */     
/*     */ 
/* 187 */     for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
/* 188 */       JavaType concrete = r.resolveAbstractType(ctxt.getConfig(), abstractType);
/* 189 */       if (concrete != null) {
/* 190 */         return concrete;
/*     */       }
/*     */     }
/* 193 */     return null;
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
/*     */   public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 216 */     ValueInstantiator valueInstantiator = findValueInstantiator(ctxt, beanDesc);
/* 217 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/* 218 */     builder.setValueInstantiator(valueInstantiator);
/*     */     
/* 220 */     addBeanProps(ctxt, beanDesc, builder);
/* 221 */     addObjectIdReader(ctxt, beanDesc, builder);
/*     */     
/*     */ 
/* 224 */     addReferenceProperties(ctxt, beanDesc, builder);
/* 225 */     addInjectables(ctxt, beanDesc, builder);
/*     */     
/* 227 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 229 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 230 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 231 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */     
/*     */     JsonDeserializer<?> deserializer;
/*     */     
/*     */     JsonDeserializer<?> deserializer;
/*     */     
/* 239 */     if ((type.isAbstract()) && (!valueInstantiator.canInstantiate())) {
/* 240 */       deserializer = builder.buildAbstract();
/*     */     } else {
/* 242 */       deserializer = builder.build();
/*     */     }
/*     */     
/*     */ 
/* 246 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 247 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 248 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*     */       }
/*     */     }
/* 251 */     return deserializer;
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
/*     */   protected JsonDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription builderDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 267 */     ValueInstantiator valueInstantiator = findValueInstantiator(ctxt, builderDesc);
/* 268 */     DeserializationConfig config = ctxt.getConfig();
/* 269 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, builderDesc);
/* 270 */     builder.setValueInstantiator(valueInstantiator);
/*     */     
/* 272 */     addBeanProps(ctxt, builderDesc, builder);
/* 273 */     addObjectIdReader(ctxt, builderDesc, builder);
/*     */     
/*     */ 
/* 276 */     addReferenceProperties(ctxt, builderDesc, builder);
/* 277 */     addInjectables(ctxt, builderDesc, builder);
/*     */     
/* 279 */     JsonPOJOBuilder.Value builderConfig = builderDesc.findPOJOBuilderConfig();
/* 280 */     String buildMethodName = builderConfig == null ? "build" : builderConfig.buildMethodName;
/*     */     
/*     */ 
/*     */ 
/* 284 */     AnnotatedMethod buildMethod = builderDesc.findMethod(buildMethodName, null);
/* 285 */     if ((buildMethod != null) && 
/* 286 */       (config.canOverrideAccessModifiers())) {
/* 287 */       ClassUtil.checkAndFixAccess(buildMethod.getMember());
/*     */     }
/*     */     
/* 290 */     builder.setPOJOBuilder(buildMethod, builderConfig);
/*     */     
/* 292 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 293 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 294 */         builder = mod.updateBuilder(config, builderDesc, builder);
/*     */       }
/*     */     }
/* 297 */     JsonDeserializer<?> deserializer = builder.buildBuilderBased(valueType, buildMethodName);
/*     */     
/*     */ 
/*     */ 
/* 301 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 302 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 303 */         deserializer = mod.modifyDeserializer(config, builderDesc, deserializer);
/*     */       }
/*     */     }
/* 306 */     return deserializer;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addObjectIdReader(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 313 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/* 314 */     if (objectIdInfo == null) {
/* 315 */       return;
/*     */     }
/* 317 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 322 */     com.fasterxml.jackson.annotation.ObjectIdResolver resolver = ctxt.objectIdResolverInstance(beanDesc.getClassInfo(), objectIdInfo);
/*     */     ObjectIdGenerator<?> gen;
/*     */     JavaType idType;
/* 325 */     SettableBeanProperty idProp; ObjectIdGenerator<?> gen; if (implClass == com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class) {
/* 326 */       PropertyName propName = objectIdInfo.getPropertyName();
/* 327 */       SettableBeanProperty idProp = builder.findProperty(propName);
/* 328 */       if (idProp == null) {
/* 329 */         throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": can not find property with name '" + propName + "'");
/*     */       }
/*     */       
/* 332 */       JavaType idType = idProp.getType();
/* 333 */       gen = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*     */     } else {
/* 335 */       JavaType type = ctxt.constructType(implClass);
/* 336 */       idType = ctxt.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/* 337 */       idProp = null;
/* 338 */       gen = ctxt.objectIdGeneratorInstance(beanDesc.getClassInfo(), objectIdInfo);
/*     */     }
/*     */     
/* 341 */     JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/* 342 */     builder.setObjectIdReader(com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), gen, deser, idProp, resolver));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 351 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 353 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/* 354 */     builder.setValueInstantiator(findValueInstantiator(ctxt, beanDesc));
/*     */     
/* 356 */     addBeanProps(ctxt, beanDesc, builder);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 363 */     AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
/* 364 */     if (am != null) {
/* 365 */       SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct(ctxt.getConfig(), am, new PropertyName("cause"));
/*     */       
/* 367 */       SettableBeanProperty prop = constructSettableProperty(ctxt, beanDesc, propDef, am.getGenericParameterType(0));
/*     */       
/* 369 */       if (prop != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 374 */         builder.addOrReplaceProperty(prop, true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 379 */     builder.addIgnorable("localizedMessage");
/*     */     
/* 381 */     builder.addIgnorable("suppressed");
/*     */     
/*     */ 
/*     */ 
/* 385 */     builder.addIgnorable("message");
/*     */     
/*     */ 
/* 388 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 389 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 390 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/* 393 */     JsonDeserializer<?> deserializer = builder.build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 398 */     if ((deserializer instanceof BeanDeserializer)) {
/* 399 */       deserializer = new com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer((BeanDeserializer)deserializer);
/*     */     }
/*     */     
/*     */ 
/* 403 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 404 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 405 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*     */       }
/*     */     }
/* 408 */     return deserializer;
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
/*     */   protected BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt, BeanDescription beanDesc)
/*     */   {
/* 425 */     return new BeanDeserializerBuilder(beanDesc, ctxt.getConfig());
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
/*     */   protected void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 439 */     SettableBeanProperty[] creatorProps = builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig());
/*     */     
/* 441 */     boolean isConcrete = !beanDesc.getType().isAbstract();
/*     */     
/*     */ 
/* 444 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 445 */     boolean ignoreAny = false;
/*     */     
/* 447 */     Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
/* 448 */     if (B != null) {
/* 449 */       ignoreAny = B.booleanValue();
/* 450 */       builder.setIgnoreUnknownProperties(ignoreAny);
/*     */     }
/*     */     
/*     */ 
/* 454 */     Set<String> ignored = com.fasterxml.jackson.databind.util.ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo()));
/* 455 */     for (String propName : ignored) {
/* 456 */       builder.addIgnorable(propName);
/*     */     }
/*     */     
/* 459 */     AnnotatedMethod anySetter = beanDesc.findAnySetter();
/* 460 */     if (anySetter != null) {
/* 461 */       builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetter));
/*     */     }
/*     */     
/*     */ 
/* 465 */     if (anySetter == null) {
/* 466 */       Collection<String> ignored2 = beanDesc.getIgnoredPropertyNames();
/* 467 */       if (ignored2 != null) {
/* 468 */         for (String propName : ignored2)
/*     */         {
/*     */ 
/* 471 */           builder.addIgnorable(propName);
/*     */         }
/*     */       }
/*     */     }
/* 475 */     boolean useGettersAsSetters = (ctxt.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS)) && (ctxt.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
/*     */     
/*     */ 
/*     */ 
/* 479 */     List<BeanPropertyDefinition> propDefs = filterBeanProps(ctxt, beanDesc, builder, beanDesc.findProperties(), ignored);
/*     */     
/*     */ 
/*     */ 
/* 483 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 484 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 485 */         propDefs = mod.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 490 */     for (BeanPropertyDefinition propDef : propDefs) {
/* 491 */       SettableBeanProperty prop = null;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 496 */       if (propDef.hasSetter()) {
/* 497 */         Type propertyType = propDef.getSetter().getGenericParameterType(0);
/* 498 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/* 499 */       } else if (propDef.hasField()) {
/* 500 */         Type propertyType = propDef.getField().getGenericType();
/* 501 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/* 502 */       } else if ((useGettersAsSetters) && (propDef.hasGetter()))
/*     */       {
/*     */ 
/*     */ 
/* 506 */         AnnotatedMethod getter = propDef.getGetter();
/*     */         
/* 508 */         Class<?> rawPropertyType = getter.getRawType();
/* 509 */         if ((Collection.class.isAssignableFrom(rawPropertyType)) || (Map.class.isAssignableFrom(rawPropertyType)))
/*     */         {
/* 511 */           prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 516 */       if ((isConcrete) && (propDef.hasConstructorParameter()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 522 */         String name = propDef.getName();
/* 523 */         CreatorProperty cprop = null;
/* 524 */         if (creatorProps != null) {
/* 525 */           for (SettableBeanProperty cp : creatorProps) {
/* 526 */             if (name.equals(cp.getName())) {
/* 527 */               cprop = (CreatorProperty)cp;
/* 528 */               break;
/*     */             }
/*     */           }
/*     */         }
/* 532 */         if (cprop == null) {
/* 533 */           throw ctxt.mappingException("Could not find creator property with name '" + name + "' (in class " + beanDesc.getBeanClass().getName() + ")");
/*     */         }
/*     */         
/* 536 */         if (prop != null) {
/* 537 */           cprop = cprop.withFallbackSetter(prop);
/*     */         }
/* 539 */         prop = cprop;
/* 540 */         builder.addCreatorProperty(cprop);
/*     */ 
/*     */ 
/*     */       }
/* 544 */       else if (prop != null) {
/* 545 */         Class<?>[] views = propDef.findViews();
/* 546 */         if (views == null)
/*     */         {
/* 548 */           if (!ctxt.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
/* 549 */             views = NO_VIEWS;
/*     */           }
/*     */         }
/*     */         
/* 553 */         prop.setViews(views);
/* 554 */         builder.addProperty(prop);
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
/*     */   protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder, List<BeanPropertyDefinition> propDefsIn, Set<String> ignored)
/*     */     throws JsonMappingException
/*     */   {
/* 571 */     ArrayList<BeanPropertyDefinition> result = new ArrayList(Math.max(4, propDefsIn.size()));
/*     */     
/* 573 */     HashMap<Class<?>, Boolean> ignoredTypes = new HashMap();
/*     */     
/* 575 */     for (BeanPropertyDefinition property : propDefsIn) {
/* 576 */       String name = property.getName();
/* 577 */       if (!ignored.contains(name))
/*     */       {
/*     */ 
/* 580 */         if (!property.hasConstructorParameter()) {
/* 581 */           Class<?> rawPropertyType = null;
/* 582 */           if (property.hasSetter()) {
/* 583 */             rawPropertyType = property.getSetter().getRawParameterType(0);
/* 584 */           } else if (property.hasField()) {
/* 585 */             rawPropertyType = property.getField().getRawType();
/*     */           }
/*     */           
/*     */ 
/* 589 */           if ((rawPropertyType != null) && (isIgnorableType(ctxt.getConfig(), beanDesc, rawPropertyType, ignoredTypes)))
/*     */           {
/*     */ 
/* 592 */             builder.addIgnorable(name);
/* 593 */             continue;
/*     */           }
/*     */         }
/* 596 */         result.add(property);
/*     */       } }
/* 598 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 610 */     Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
/* 611 */     if (refs != null) {
/* 612 */       for (Map.Entry<String, AnnotatedMember> en : refs.entrySet()) {
/* 613 */         String name = (String)en.getKey();
/* 614 */         AnnotatedMember m = (AnnotatedMember)en.getValue();
/*     */         Type genericType;
/* 616 */         Type genericType; if ((m instanceof AnnotatedMethod)) {
/* 617 */           genericType = ((AnnotatedMethod)m).getGenericParameterType(0);
/*     */         } else {
/* 619 */           genericType = m.getRawType();
/*     */         }
/* 621 */         SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct(ctxt.getConfig(), m);
/*     */         
/* 623 */         builder.addBackReferenceProperty(name, constructSettableProperty(ctxt, beanDesc, propDef, genericType));
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
/*     */   protected void addInjectables(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 637 */     Map<Object, AnnotatedMember> raw = beanDesc.findInjectables();
/* 638 */     boolean fixAccess; if (raw != null) {
/* 639 */       fixAccess = ctxt.canOverrideAccessModifiers();
/* 640 */       for (Map.Entry<Object, AnnotatedMember> entry : raw.entrySet()) {
/* 641 */         AnnotatedMember m = (AnnotatedMember)entry.getValue();
/* 642 */         if (fixAccess) {
/* 643 */           m.fixAccess();
/*     */         }
/* 645 */         builder.addInjectable(new PropertyName(m.getName()), beanDesc.resolveType(m.getGenericType()), beanDesc.getClassAnnotations(), m, entry.getKey());
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
/*     */   protected SettableAnyProperty constructAnySetter(DeserializationContext ctxt, BeanDescription beanDesc, AnnotatedMethod setter)
/*     */     throws JsonMappingException
/*     */   {
/* 661 */     if (ctxt.canOverrideAccessModifiers()) {
/* 662 */       setter.fixAccess();
/*     */     }
/*     */     
/* 665 */     JavaType type = beanDesc.bindingsForBeanType().resolveType(setter.getGenericParameterType(1));
/* 666 */     BeanProperty.Std property = new BeanProperty.Std(new PropertyName(setter.getName()), type, null, beanDesc.getClassAnnotations(), setter, com.fasterxml.jackson.databind.PropertyMetadata.STD_OPTIONAL);
/*     */     
/*     */ 
/* 669 */     type = resolveType(ctxt, beanDesc, type, setter);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 676 */     JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, setter);
/*     */     
/*     */ 
/*     */ 
/* 680 */     type = modifyTypeByAnnotation(ctxt, setter, type);
/* 681 */     if (deser == null) {
/* 682 */       deser = (JsonDeserializer)type.getValueHandler();
/*     */     }
/* 684 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/* 685 */     return new SettableAnyProperty(property, setter, type, deser, typeDeser);
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
/*     */   protected SettableBeanProperty constructSettableProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef, Type jdkType)
/*     */     throws JsonMappingException
/*     */   {
/* 702 */     AnnotatedMember mutator = propDef.getNonConstructorMutator();
/* 703 */     if (ctxt.canOverrideAccessModifiers()) {
/* 704 */       mutator.fixAccess();
/*     */     }
/*     */     
/* 707 */     JavaType t0 = beanDesc.resolveType(jdkType);
/*     */     
/* 709 */     BeanProperty.Std property = new BeanProperty.Std(propDef.getFullName(), t0, propDef.getWrapperName(), beanDesc.getClassAnnotations(), mutator, propDef.getMetadata());
/*     */     
/*     */ 
/* 712 */     JavaType type = resolveType(ctxt, beanDesc, t0, mutator);
/*     */     
/* 714 */     if (type != t0) {
/* 715 */       property = property.withType(type);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 721 */     JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(ctxt, mutator);
/* 722 */     type = modifyTypeByAnnotation(ctxt, mutator, type);
/* 723 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*     */     SettableBeanProperty prop;
/* 725 */     SettableBeanProperty prop; if ((mutator instanceof AnnotatedMethod)) {
/* 726 */       prop = new MethodProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedMethod)mutator);
/*     */     }
/*     */     else {
/* 729 */       prop = new FieldProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedField)mutator);
/*     */     }
/*     */     
/* 732 */     if (propDeser != null) {
/* 733 */       prop = prop.withValueDeserializer(propDeser);
/*     */     }
/*     */     
/* 736 */     AnnotationIntrospector.ReferenceProperty ref = propDef.findReferenceType();
/* 737 */     if ((ref != null) && (ref.isManagedReference())) {
/* 738 */       prop.setManagedReferenceName(ref.getName());
/*     */     }
/* 740 */     ObjectIdInfo objectIdInfo = propDef.findObjectIdInfo();
/* 741 */     if (objectIdInfo != null) {
/* 742 */       prop.setObjectIdInfo(objectIdInfo);
/*     */     }
/* 744 */     return prop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty constructSetterlessProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef)
/*     */     throws JsonMappingException
/*     */   {
/* 755 */     AnnotatedMethod getter = propDef.getGetter();
/*     */     
/* 757 */     if (ctxt.canOverrideAccessModifiers()) {
/* 758 */       getter.fixAccess();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 764 */     JavaType type = getter.getType(beanDesc.bindingsForBeanType());
/*     */     
/*     */ 
/*     */ 
/* 768 */     JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(ctxt, getter);
/* 769 */     type = modifyTypeByAnnotation(ctxt, getter, type);
/*     */     
/* 771 */     type = resolveType(ctxt, beanDesc, type, getter);
/* 772 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/* 773 */     SettableBeanProperty prop = new com.fasterxml.jackson.databind.deser.impl.SetterlessProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), getter);
/*     */     
/* 775 */     if (propDeser != null) {
/* 776 */       prop = prop.withValueDeserializer(propDeser);
/*     */     }
/* 778 */     return prop;
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
/*     */   protected boolean isPotentialBeanType(Class<?> type)
/*     */   {
/* 797 */     String typeStr = ClassUtil.canBeABeanType(type);
/* 798 */     if (typeStr != null) {
/* 799 */       throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*     */     }
/* 801 */     if (ClassUtil.isProxyType(type)) {
/* 802 */       throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 807 */     typeStr = ClassUtil.isLocalType(type, true);
/* 808 */     if (typeStr != null) {
/* 809 */       throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*     */     }
/* 811 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isIgnorableType(DeserializationConfig config, BeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes)
/*     */   {
/* 821 */     Boolean status = (Boolean)ignoredTypes.get(type);
/* 822 */     if (status != null) {
/* 823 */       return status.booleanValue();
/*     */     }
/* 825 */     BeanDescription desc = config.introspectClassAnnotations(type);
/* 826 */     status = config.getAnnotationIntrospector().isIgnorableType(desc.getClassInfo());
/*     */     
/* 828 */     return status == null ? false : status.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */