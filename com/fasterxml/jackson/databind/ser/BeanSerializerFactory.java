/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty.Std;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanSerializerFactory
/*     */   extends BasicSerializerFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  62 */   public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerFactory(SerializerFactoryConfig config)
/*     */   {
/*  75 */     super(config);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializerFactory withConfig(SerializerFactoryConfig config)
/*     */   {
/*  87 */     if (this._factoryConfig == config) {
/*  88 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     if (getClass() != BeanSerializerFactory.class) {
/*  97 */       throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
/*     */     }
/*     */     
/*     */ 
/* 101 */     return new BeanSerializerFactory(config);
/*     */   }
/*     */   
/*     */   protected Iterable<Serializers> customSerializers()
/*     */   {
/* 106 */     return this._factoryConfig.serializers();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType origType)
/*     */     throws JsonMappingException
/*     */   {
/* 132 */     SerializationConfig config = prov.getConfig();
/* 133 */     BeanDescription beanDesc = config.introspect(origType);
/* 134 */     JsonSerializer<?> ser = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
/* 135 */     if (ser != null) {
/* 136 */       return ser;
/*     */     }
/*     */     
/*     */ 
/* 140 */     JavaType type = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), origType);
/* 141 */     boolean staticTyping; boolean staticTyping; if (type == origType) {
/* 142 */       staticTyping = false;
/*     */     } else {
/* 144 */       staticTyping = true;
/* 145 */       if (!type.hasRawClass(origType.getRawClass())) {
/* 146 */         beanDesc = config.introspect(type);
/*     */       }
/*     */     }
/*     */     
/* 150 */     Converter<Object, Object> conv = beanDesc.findSerializationConverter();
/* 151 */     if (conv == null) {
/* 152 */       return _createSerializer2(prov, type, beanDesc, staticTyping);
/*     */     }
/* 154 */     JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
/*     */     
/*     */ 
/* 157 */     if (!delegateType.hasRawClass(type.getRawClass())) {
/* 158 */       beanDesc = config.introspect(delegateType);
/*     */       
/* 160 */       ser = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
/*     */     }
/*     */     
/* 163 */     if ((ser == null) && (!delegateType.isJavaLangObject())) {
/* 164 */       ser = _createSerializer2(prov, delegateType, beanDesc, true);
/*     */     }
/* 166 */     return new StdDelegatingSerializer(conv, delegateType, ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<?> _createSerializer2(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*     */     throws JsonMappingException
/*     */   {
/* 174 */     JsonSerializer<?> ser = findSerializerByAnnotations(prov, type, beanDesc);
/* 175 */     if (ser != null) {
/* 176 */       return ser;
/*     */     }
/* 178 */     SerializationConfig config = prov.getConfig();
/*     */     
/*     */ 
/*     */ 
/* 182 */     if (type.isContainerType()) {
/* 183 */       if (!staticTyping) {
/* 184 */         staticTyping = usesStaticTyping(config, beanDesc, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */       ser = buildContainerSerializer(prov, type, beanDesc, staticTyping);
/*     */       
/* 199 */       if (ser != null) {
/* 200 */         return ser;
/*     */       }
/*     */     }
/*     */     else {
/* 204 */       for (Serializers serializers : customSerializers()) {
/* 205 */         ser = serializers.findSerializer(config, type, beanDesc);
/* 206 */         if (ser != null) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 215 */     if (ser == null) {
/* 216 */       ser = findSerializerByLookup(type, config, beanDesc, staticTyping);
/* 217 */       if (ser == null) {
/* 218 */         ser = findSerializerByPrimaryType(prov, type, beanDesc, staticTyping);
/* 219 */         if (ser == null)
/*     */         {
/*     */ 
/*     */ 
/* 223 */           ser = findBeanSerializer(prov, type, beanDesc);
/*     */           
/* 225 */           if (ser == null) {
/* 226 */             ser = findSerializerByAddonType(config, type, beanDesc, staticTyping);
/*     */             
/*     */ 
/*     */ 
/* 230 */             if (ser == null) {
/* 231 */               ser = prov.getUnknownTypeSerializer(beanDesc.getBeanClass());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 237 */     if (ser != null)
/*     */     {
/* 239 */       if (this._factoryConfig.hasSerializerModifiers()) {
/* 240 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 241 */           ser = mod.modifySerializer(config, beanDesc, ser);
/*     */         }
/*     */       }
/*     */     }
/* 245 */     return ser;
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
/*     */   public JsonSerializer<Object> findBeanSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 263 */     if (!isPotentialBeanType(type.getRawClass()))
/*     */     {
/*     */ 
/* 266 */       if (!type.isEnumType()) {
/* 267 */         return null;
/*     */       }
/*     */     }
/* 270 */     return constructBeanSerializer(prov, beanDesc);
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
/*     */   public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor)
/*     */     throws JsonMappingException
/*     */   {
/* 287 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 288 */     TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, accessor, baseType);
/*     */     
/* 290 */     if (b == null) {
/* 291 */       return createTypeSerializer(config, baseType);
/*     */     }
/* 293 */     Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai, baseType);
/*     */     
/* 295 */     return b.buildTypeSerializer(config, baseType, subtypes);
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
/*     */   public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor)
/*     */     throws JsonMappingException
/*     */   {
/* 312 */     JavaType contentType = containerType.getContentType();
/* 313 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 314 */     TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, accessor, containerType);
/*     */     
/* 316 */     if (b == null) {
/* 317 */       return createTypeSerializer(config, contentType);
/*     */     }
/* 319 */     Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai, contentType);
/*     */     
/* 321 */     return b.buildTypeSerializer(config, contentType, subtypes);
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
/*     */   protected JsonSerializer<Object> constructBeanSerializer(SerializerProvider prov, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 342 */     if (beanDesc.getBeanClass() == Object.class) {
/* 343 */       return prov.getUnknownTypeSerializer(Object.class);
/*     */     }
/*     */     
/* 346 */     SerializationConfig config = prov.getConfig();
/* 347 */     BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
/* 348 */     builder.setConfig(config);
/*     */     
/*     */ 
/* 351 */     List<BeanPropertyWriter> props = findBeanProperties(prov, beanDesc, builder);
/* 352 */     if (props == null) {
/* 353 */       props = new ArrayList();
/*     */     }
/*     */     
/* 356 */     prov.getAnnotationIntrospector().findAndAddVirtualProperties(config, beanDesc.getClassInfo(), props);
/*     */     
/*     */ 
/* 359 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 360 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 361 */         props = mod.changeProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 366 */     props = filterBeanProperties(config, beanDesc, props);
/*     */     
/*     */ 
/* 369 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 370 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 371 */         props = mod.orderProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 379 */     builder.setObjectIdWriter(constructObjectIdHandler(prov, beanDesc, props));
/*     */     
/* 381 */     builder.setProperties(props);
/* 382 */     builder.setFilterId(findFilterId(config, beanDesc));
/*     */     
/* 384 */     AnnotatedMember anyGetter = beanDesc.findAnyGetter();
/* 385 */     if (anyGetter != null) {
/* 386 */       if (config.canOverrideAccessModifiers()) {
/* 387 */         anyGetter.fixAccess();
/*     */       }
/* 389 */       JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
/*     */       
/* 391 */       boolean staticTyping = config.isEnabled(MapperFeature.USE_STATIC_TYPING);
/* 392 */       JavaType valueType = type.getContentType();
/* 393 */       TypeSerializer typeSer = createTypeSerializer(config, valueType);
/*     */       
/*     */ 
/* 396 */       JsonSerializer<?> anySer = findSerializerFromAnnotation(prov, anyGetter);
/* 397 */       if (anySer == null)
/*     */       {
/* 399 */         anySer = MapSerializer.construct(null, type, staticTyping, typeSer, null, null, null);
/*     */       }
/*     */       
/*     */ 
/* 403 */       PropertyName name = new PropertyName(anyGetter.getName());
/* 404 */       BeanProperty.Std anyProp = new BeanProperty.Std(name, valueType, null, beanDesc.getClassAnnotations(), anyGetter, PropertyMetadata.STD_OPTIONAL);
/*     */       
/* 406 */       builder.setAnyGetter(new AnyGetterWriter(anyProp, anyGetter, anySer));
/*     */     }
/*     */     
/* 409 */     processViews(config, builder);
/*     */     
/*     */ 
/* 412 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 413 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 414 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */     
/* 418 */     JsonSerializer<Object> ser = builder.build();
/*     */     
/* 420 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/* 424 */       if (beanDesc.hasKnownClassAnnotations()) {
/* 425 */         return builder.createDummy();
/*     */       }
/*     */     }
/* 428 */     return ser;
/*     */   }
/*     */   
/*     */ 
/*     */   protected ObjectIdWriter constructObjectIdHandler(SerializerProvider prov, BeanDescription beanDesc, List<BeanPropertyWriter> props)
/*     */     throws JsonMappingException
/*     */   {
/* 435 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/* 436 */     if (objectIdInfo == null) {
/* 437 */       return null;
/*     */     }
/*     */     
/* 440 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */     
/*     */ 
/* 443 */     if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 444 */       String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 445 */       BeanPropertyWriter idProp = null;
/*     */       
/* 447 */       int i = 0; for (int len = props.size();; i++) {
/* 448 */         if (i == len) {
/* 449 */           throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": can not find property with name '" + propName + "'");
/*     */         }
/*     */         
/* 452 */         BeanPropertyWriter prop = (BeanPropertyWriter)props.get(i);
/* 453 */         if (propName.equals(prop.getName())) {
/* 454 */           idProp = prop;
/*     */           
/*     */ 
/*     */ 
/* 458 */           if (i <= 0) break;
/* 459 */           props.remove(i);
/* 460 */           props.add(0, idProp); break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 465 */       JavaType idType = idProp.getType();
/* 466 */       ObjectIdGenerator<?> gen = new PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/*     */       
/* 468 */       return ObjectIdWriter.construct(idType, (PropertyName)null, gen, objectIdInfo.getAlwaysAsId());
/*     */     }
/*     */     
/*     */ 
/* 472 */     JavaType type = prov.constructType(implClass);
/*     */     
/* 474 */     JavaType idType = prov.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/* 475 */     ObjectIdGenerator<?> gen = prov.objectIdGeneratorInstance(beanDesc.getClassInfo(), objectIdInfo);
/* 476 */     return ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo.getAlwaysAsId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews)
/*     */   {
/* 488 */     return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
/*     */   }
/*     */   
/*     */ 
/*     */   protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc)
/*     */   {
/* 494 */     return new PropertyBuilder(config, beanDesc);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDesc) {
/* 498 */     return new BeanSerializerBuilder(beanDesc);
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
/* 517 */     return (ClassUtil.canBeABeanType(type) == null) && (!ClassUtil.isProxyType(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 528 */     List<BeanPropertyDefinition> properties = beanDesc.findProperties();
/* 529 */     SerializationConfig config = prov.getConfig();
/*     */     
/*     */ 
/* 532 */     removeIgnorableTypes(config, beanDesc, properties);
/*     */     
/*     */ 
/* 535 */     if (config.isEnabled(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)) {
/* 536 */       removeSetterlessGetters(config, beanDesc, properties);
/*     */     }
/*     */     
/*     */ 
/* 540 */     if (properties.isEmpty()) {
/* 541 */       return null;
/*     */     }
/*     */     
/* 544 */     boolean staticTyping = usesStaticTyping(config, beanDesc, null);
/* 545 */     PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
/*     */     
/* 547 */     ArrayList<BeanPropertyWriter> result = new ArrayList(properties.size());
/* 548 */     TypeBindings typeBind = beanDesc.bindingsForBeanType();
/* 549 */     for (BeanPropertyDefinition property : properties) {
/* 550 */       AnnotatedMember accessor = property.getAccessor();
/*     */       
/* 552 */       if (property.isTypeId()) {
/* 553 */         if (accessor != null) {
/* 554 */           if (config.canOverrideAccessModifiers()) {
/* 555 */             accessor.fixAccess();
/*     */           }
/* 557 */           builder.setTypeId(accessor);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 562 */         AnnotationIntrospector.ReferenceProperty refType = property.findReferenceType();
/* 563 */         if ((refType == null) || (!refType.isBackReference()))
/*     */         {
/*     */ 
/* 566 */           if ((accessor instanceof AnnotatedMethod)) {
/* 567 */             result.add(_constructWriter(prov, property, typeBind, pb, staticTyping, (AnnotatedMethod)accessor));
/*     */           } else
/* 569 */             result.add(_constructWriter(prov, property, typeBind, pb, staticTyping, (AnnotatedField)accessor)); }
/*     */       }
/*     */     }
/* 572 */     return result;
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
/*     */   protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> props)
/*     */   {
/* 588 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 589 */     AnnotatedClass ac = beanDesc.getClassInfo();
/* 590 */     String[] ignored = intr.findPropertiesToIgnore(ac);
/* 591 */     if ((ignored != null) && (ignored.length > 0)) {
/* 592 */       HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(ignored);
/* 593 */       Iterator<BeanPropertyWriter> it = props.iterator();
/* 594 */       while (it.hasNext()) {
/* 595 */         if (ignoredSet.contains(((BeanPropertyWriter)it.next()).getName())) {
/* 596 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/* 600 */     return props;
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
/*     */   protected void processViews(SerializationConfig config, BeanSerializerBuilder builder)
/*     */   {
/* 615 */     List<BeanPropertyWriter> props = builder.getProperties();
/* 616 */     boolean includeByDefault = config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/* 617 */     int propCount = props.size();
/* 618 */     int viewsFound = 0;
/* 619 */     BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
/*     */     
/* 621 */     for (int i = 0; i < propCount; i++) {
/* 622 */       BeanPropertyWriter bpw = (BeanPropertyWriter)props.get(i);
/* 623 */       Class<?>[] views = bpw.getViews();
/* 624 */       if (views == null) {
/* 625 */         if (includeByDefault) {
/* 626 */           filtered[i] = bpw;
/*     */         }
/*     */       } else {
/* 629 */         viewsFound++;
/* 630 */         filtered[i] = constructFilteredBeanWriter(bpw, views);
/*     */       }
/*     */     }
/*     */     
/* 634 */     if ((includeByDefault) && (viewsFound == 0)) {
/* 635 */       return;
/*     */     }
/* 637 */     builder.setFilteredProperties(filtered);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeIgnorableTypes(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties)
/*     */   {
/* 648 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 649 */     HashMap<Class<?>, Boolean> ignores = new HashMap();
/* 650 */     Iterator<BeanPropertyDefinition> it = properties.iterator();
/* 651 */     while (it.hasNext()) {
/* 652 */       BeanPropertyDefinition property = (BeanPropertyDefinition)it.next();
/* 653 */       AnnotatedMember accessor = property.getAccessor();
/* 654 */       if (accessor == null) {
/* 655 */         it.remove();
/*     */       }
/*     */       else {
/* 658 */         Class<?> type = accessor.getRawType();
/* 659 */         Boolean result = (Boolean)ignores.get(type);
/* 660 */         if (result == null) {
/* 661 */           BeanDescription desc = config.introspectClassAnnotations(type);
/* 662 */           AnnotatedClass ac = desc.getClassInfo();
/* 663 */           result = intr.isIgnorableType(ac);
/*     */           
/* 665 */           if (result == null) {
/* 666 */             result = Boolean.FALSE;
/*     */           }
/* 668 */           ignores.put(type, result);
/*     */         }
/*     */         
/* 671 */         if (result.booleanValue()) {
/* 672 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeSetterlessGetters(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties)
/*     */   {
/* 683 */     Iterator<BeanPropertyDefinition> it = properties.iterator();
/* 684 */     while (it.hasNext()) {
/* 685 */       BeanPropertyDefinition property = (BeanPropertyDefinition)it.next();
/*     */       
/*     */ 
/* 688 */       if ((!property.couldDeserialize()) && (!property.isExplicitlyIncluded())) {
/* 689 */         it.remove();
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
/*     */   protected BeanPropertyWriter _constructWriter(SerializerProvider prov, BeanPropertyDefinition propDef, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, AnnotatedMember accessor)
/*     */     throws JsonMappingException
/*     */   {
/* 709 */     PropertyName name = propDef.getFullName();
/* 710 */     if (prov.canOverrideAccessModifiers()) {
/* 711 */       accessor.fixAccess();
/*     */     }
/* 713 */     JavaType type = accessor.getType(typeContext);
/* 714 */     BeanProperty.Std property = new BeanProperty.Std(name, type, propDef.getWrapperName(), pb.getClassAnnotations(), accessor, propDef.getMetadata());
/*     */     
/*     */ 
/*     */ 
/* 718 */     JsonSerializer<?> annotatedSerializer = findSerializerFromAnnotation(prov, accessor);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 723 */     if ((annotatedSerializer instanceof ResolvableSerializer)) {
/* 724 */       ((ResolvableSerializer)annotatedSerializer).resolve(prov);
/*     */     }
/*     */     
/* 727 */     annotatedSerializer = prov.handlePrimaryContextualization(annotatedSerializer, property);
/*     */     
/* 729 */     TypeSerializer contentTypeSer = null;
/*     */     
/* 731 */     if ((ClassUtil.isCollectionMapOrArray(type.getRawClass())) || (type.isCollectionLikeType()) || (type.isMapLikeType())) {
/* 732 */       contentTypeSer = findPropertyContentTypeSerializer(type, prov.getConfig(), accessor);
/*     */     }
/*     */     
/* 735 */     TypeSerializer typeSer = findPropertyTypeSerializer(type, prov.getConfig(), accessor);
/* 736 */     BeanPropertyWriter pbw = pb.buildWriter(prov, propDef, type, annotatedSerializer, typeSer, contentTypeSer, accessor, staticTyping);
/*     */     
/* 738 */     return pbw;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\BeanSerializerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */