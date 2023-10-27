/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ 
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
/*      */ import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
/*      */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.IOException;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public abstract class BeanDeserializerBase extends StdDeserializer<Object> implements ContextualDeserializer, ResolvableDeserializer, java.io.Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   34 */   protected static final PropertyName TEMP_PROPERTY_NAME = new PropertyName("#temporary-name");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final transient com.fasterxml.jackson.databind.util.Annotations _classAnnotations;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _beanType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final com.fasterxml.jackson.annotation.JsonFormat.Shape _serializationShape;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ValueInstantiator _valueInstantiator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _delegateDeserializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PropertyBasedCreator _propertyBasedCreator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _nonStandardCreation;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _vanillaProcessing;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final BeanPropertyMap _beanProperties;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ValueInjector[] _injectables;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableAnyProperty _anySetter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final HashSet<String> _ignorableProps;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _ignoreAllUnknown;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _needViewProcesing;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Map<String, SettableBeanProperty> _backRefs;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient java.util.HashMap<com.fasterxml.jackson.databind.type.ClassKey, JsonDeserializer<Object>> _subDeserializers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected UnwrappedPropertyHandler _unwrappedPropertyHandler;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler _externalTypeIdHandler;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ObjectIdReader _objectIdReader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanDeserializerBase(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews)
/*      */   {
/*  205 */     super(beanDesc.getType());
/*      */     
/*  207 */     com.fasterxml.jackson.databind.introspect.AnnotatedClass ac = beanDesc.getClassInfo();
/*  208 */     this._classAnnotations = ac.getAnnotations();
/*  209 */     this._beanType = beanDesc.getType();
/*  210 */     this._valueInstantiator = builder.getValueInstantiator();
/*      */     
/*  212 */     this._beanProperties = properties;
/*  213 */     this._backRefs = backRefs;
/*  214 */     this._ignorableProps = ignorableProps;
/*  215 */     this._ignoreAllUnknown = ignoreAllUnknown;
/*      */     
/*  217 */     this._anySetter = builder.getAnySetter();
/*  218 */     List<ValueInjector> injectables = builder.getInjectables();
/*  219 */     this._injectables = ((injectables == null) || (injectables.isEmpty()) ? null : (ValueInjector[])injectables.toArray(new ValueInjector[injectables.size()]));
/*      */     
/*  221 */     this._objectIdReader = builder.getObjectIdReader();
/*  222 */     this._nonStandardCreation = ((this._unwrappedPropertyHandler != null) || (this._valueInstantiator.canCreateUsingDelegate()) || (this._valueInstantiator.canCreateFromObjectWith()) || (!this._valueInstantiator.canCreateUsingDefault()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  229 */     com.fasterxml.jackson.annotation.JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  230 */     this._serializationShape = (format == null ? null : format.getShape());
/*      */     
/*  232 */     this._needViewProcesing = hasViews;
/*  233 */     this._vanillaProcessing = ((!this._nonStandardCreation) && (this._injectables == null) && (!this._needViewProcesing) && (this._objectIdReader == null));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src)
/*      */   {
/*  242 */     this(src, src._ignoreAllUnknown);
/*      */   }
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, boolean ignoreAllUnknown)
/*      */   {
/*  247 */     super(src._beanType);
/*      */     
/*  249 */     this._classAnnotations = src._classAnnotations;
/*  250 */     this._beanType = src._beanType;
/*      */     
/*  252 */     this._valueInstantiator = src._valueInstantiator;
/*  253 */     this._delegateDeserializer = src._delegateDeserializer;
/*  254 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  256 */     this._beanProperties = src._beanProperties;
/*  257 */     this._backRefs = src._backRefs;
/*  258 */     this._ignorableProps = src._ignorableProps;
/*  259 */     this._ignoreAllUnknown = ignoreAllUnknown;
/*  260 */     this._anySetter = src._anySetter;
/*  261 */     this._injectables = src._injectables;
/*  262 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  264 */     this._nonStandardCreation = src._nonStandardCreation;
/*  265 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  266 */     this._needViewProcesing = src._needViewProcesing;
/*  267 */     this._serializationShape = src._serializationShape;
/*      */     
/*  269 */     this._vanillaProcessing = src._vanillaProcessing;
/*      */   }
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, NameTransformer unwrapper)
/*      */   {
/*  274 */     super(src._beanType);
/*      */     
/*  276 */     this._classAnnotations = src._classAnnotations;
/*  277 */     this._beanType = src._beanType;
/*      */     
/*  279 */     this._valueInstantiator = src._valueInstantiator;
/*  280 */     this._delegateDeserializer = src._delegateDeserializer;
/*  281 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  283 */     this._backRefs = src._backRefs;
/*  284 */     this._ignorableProps = src._ignorableProps;
/*  285 */     this._ignoreAllUnknown = ((unwrapper != null) || (src._ignoreAllUnknown));
/*  286 */     this._anySetter = src._anySetter;
/*  287 */     this._injectables = src._injectables;
/*  288 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  290 */     this._nonStandardCreation = src._nonStandardCreation;
/*  291 */     UnwrappedPropertyHandler uph = src._unwrappedPropertyHandler;
/*      */     
/*  293 */     if (unwrapper != null)
/*      */     {
/*  295 */       if (uph != null) {
/*  296 */         uph = uph.renameAll(unwrapper);
/*      */       }
/*      */       
/*  299 */       this._beanProperties = src._beanProperties.renameAll(unwrapper);
/*      */     } else {
/*  301 */       this._beanProperties = src._beanProperties;
/*      */     }
/*  303 */     this._unwrappedPropertyHandler = uph;
/*  304 */     this._needViewProcesing = src._needViewProcesing;
/*  305 */     this._serializationShape = src._serializationShape;
/*      */     
/*      */ 
/*  308 */     this._vanillaProcessing = false;
/*      */   }
/*      */   
/*      */   public BeanDeserializerBase(BeanDeserializerBase src, ObjectIdReader oir)
/*      */   {
/*  313 */     super(src._beanType);
/*      */     
/*  315 */     this._classAnnotations = src._classAnnotations;
/*  316 */     this._beanType = src._beanType;
/*      */     
/*  318 */     this._valueInstantiator = src._valueInstantiator;
/*  319 */     this._delegateDeserializer = src._delegateDeserializer;
/*  320 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  322 */     this._backRefs = src._backRefs;
/*  323 */     this._ignorableProps = src._ignorableProps;
/*  324 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  325 */     this._anySetter = src._anySetter;
/*  326 */     this._injectables = src._injectables;
/*      */     
/*  328 */     this._nonStandardCreation = src._nonStandardCreation;
/*  329 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  330 */     this._needViewProcesing = src._needViewProcesing;
/*  331 */     this._serializationShape = src._serializationShape;
/*      */     
/*      */ 
/*  334 */     this._objectIdReader = oir;
/*      */     
/*  336 */     if (oir == null) {
/*  337 */       this._beanProperties = src._beanProperties;
/*  338 */       this._vanillaProcessing = src._vanillaProcessing;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  344 */       com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty idProp = new com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty(oir, com.fasterxml.jackson.databind.PropertyMetadata.STD_REQUIRED);
/*  345 */       this._beanProperties = src._beanProperties.withProperty(idProp);
/*  346 */       this._vanillaProcessing = false;
/*      */     }
/*      */   }
/*      */   
/*      */   public BeanDeserializerBase(BeanDeserializerBase src, HashSet<String> ignorableProps)
/*      */   {
/*  352 */     super(src._beanType);
/*      */     
/*  354 */     this._classAnnotations = src._classAnnotations;
/*  355 */     this._beanType = src._beanType;
/*      */     
/*  357 */     this._valueInstantiator = src._valueInstantiator;
/*  358 */     this._delegateDeserializer = src._delegateDeserializer;
/*  359 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  361 */     this._backRefs = src._backRefs;
/*  362 */     this._ignorableProps = ignorableProps;
/*  363 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  364 */     this._anySetter = src._anySetter;
/*  365 */     this._injectables = src._injectables;
/*      */     
/*  367 */     this._nonStandardCreation = src._nonStandardCreation;
/*  368 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  369 */     this._needViewProcesing = src._needViewProcesing;
/*  370 */     this._serializationShape = src._serializationShape;
/*      */     
/*  372 */     this._vanillaProcessing = src._vanillaProcessing;
/*  373 */     this._objectIdReader = src._objectIdReader;
/*  374 */     this._beanProperties = src._beanProperties;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonDeserializer<Object> unwrappingDeserializer(NameTransformer paramNameTransformer);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract BeanDeserializerBase withObjectIdReader(ObjectIdReader paramObjectIdReader);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract BeanDeserializerBase withIgnorableProperties(HashSet<String> paramHashSet);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract BeanDeserializerBase asArrayDeserializer();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resolve(DeserializationContext ctxt)
/*      */     throws JsonMappingException
/*      */   {
/*  408 */     ExternalTypeHandler.Builder extTypes = null;
/*      */     
/*  410 */     if (this._valueInstantiator.canCreateFromObjectWith()) {
/*  411 */       SettableBeanProperty[] creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/*  412 */       this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps);
/*      */       
/*  414 */       for (SettableBeanProperty prop : this._propertyBasedCreator.properties()) {
/*  415 */         if (prop.hasValueTypeDeserializer()) {
/*  416 */           TypeDeserializer typeDeser = prop.getValueTypeDeserializer();
/*  417 */           if (typeDeser.getTypeInclusion() == com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/*  418 */             if (extTypes == null) {
/*  419 */               extTypes = new ExternalTypeHandler.Builder();
/*      */             }
/*  421 */             extTypes.addExternal(prop, typeDeser);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  427 */     UnwrappedPropertyHandler unwrapped = null;
/*      */     
/*  429 */     for (SettableBeanProperty origProp : this._beanProperties) {
/*  430 */       SettableBeanProperty prop = origProp;
/*      */       
/*  432 */       if (!prop.hasValueDeserializer())
/*      */       {
/*  434 */         JsonDeserializer<?> deser = findConvertingDeserializer(ctxt, prop);
/*  435 */         if (deser == null) {
/*  436 */           deser = findDeserializer(ctxt, prop.getType(), prop);
/*      */         }
/*  438 */         prop = prop.withValueDeserializer(deser);
/*      */       } else {
/*  440 */         JsonDeserializer<Object> deser = prop.getValueDeserializer();
/*      */         
/*      */ 
/*      */ 
/*  444 */         JsonDeserializer<?> cd = ctxt.handlePrimaryContextualization(deser, prop, prop.getType());
/*      */         
/*  446 */         if (cd != deser) {
/*  447 */           prop = prop.withValueDeserializer(cd);
/*      */         }
/*      */       }
/*      */       
/*  451 */       prop = _resolveManagedReferenceProperty(ctxt, prop);
/*      */       
/*      */ 
/*  454 */       if (!(prop instanceof com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty)) {
/*  455 */         prop = _resolvedObjectIdProperty(ctxt, prop);
/*      */       }
/*      */       
/*  458 */       SettableBeanProperty u = _resolveUnwrappedProperty(ctxt, prop);
/*  459 */       if (u != null) {
/*  460 */         prop = u;
/*  461 */         if (unwrapped == null) {
/*  462 */           unwrapped = new UnwrappedPropertyHandler();
/*      */         }
/*  464 */         unwrapped.addProperty(prop);
/*      */         
/*      */ 
/*      */ 
/*  468 */         this._beanProperties.remove(prop);
/*      */       }
/*      */       else
/*      */       {
/*  472 */         prop = _resolveInnerClassValuedProperty(ctxt, prop);
/*  473 */         if (prop != origProp) {
/*  474 */           this._beanProperties.replace(prop);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  480 */         if (prop.hasValueTypeDeserializer()) {
/*  481 */           TypeDeserializer typeDeser = prop.getValueTypeDeserializer();
/*  482 */           if (typeDeser.getTypeInclusion() == com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/*  483 */             if (extTypes == null) {
/*  484 */               extTypes = new ExternalTypeHandler.Builder();
/*      */             }
/*  486 */             extTypes.addExternal(prop, typeDeser);
/*      */             
/*  488 */             this._beanProperties.remove(prop);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  495 */     if ((this._anySetter != null) && (!this._anySetter.hasValueDeserializer())) {
/*  496 */       this._anySetter = this._anySetter.withValueDeserializer(findDeserializer(ctxt, this._anySetter.getType(), this._anySetter.getProperty()));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  501 */     if (this._valueInstantiator.canCreateUsingDelegate()) {
/*  502 */       JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/*  503 */       if (delegateType == null) {
/*  504 */         throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._beanType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
/*      */       }
/*      */       
/*      */ 
/*  508 */       com.fasterxml.jackson.databind.introspect.AnnotatedWithParams delegateCreator = this._valueInstantiator.getDelegateCreator();
/*      */       
/*  510 */       com.fasterxml.jackson.databind.BeanProperty.Std property = new com.fasterxml.jackson.databind.BeanProperty.Std(TEMP_PROPERTY_NAME, delegateType, null, this._classAnnotations, delegateCreator, com.fasterxml.jackson.databind.PropertyMetadata.STD_OPTIONAL);
/*      */       
/*      */ 
/*      */ 
/*  514 */       TypeDeserializer td = (TypeDeserializer)delegateType.getTypeHandler();
/*  515 */       if (td == null) {
/*  516 */         td = ctxt.getConfig().findTypeDeserializer(delegateType);
/*      */       }
/*  518 */       JsonDeserializer<Object> dd = findDeserializer(ctxt, delegateType, property);
/*  519 */       if (td != null) {
/*  520 */         td = td.forProperty(property);
/*  521 */         dd = new com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer(td, dd);
/*      */       }
/*  523 */       this._delegateDeserializer = dd;
/*      */     }
/*      */     
/*  526 */     if (extTypes != null) {
/*  527 */       this._externalTypeIdHandler = extTypes.build();
/*      */       
/*  529 */       this._nonStandardCreation = true;
/*      */     }
/*      */     
/*  532 */     this._unwrappedPropertyHandler = unwrapped;
/*  533 */     if (unwrapped != null) {
/*  534 */       this._nonStandardCreation = true;
/*      */     }
/*      */     
/*      */ 
/*  538 */     this._vanillaProcessing = ((this._vanillaProcessing) && (!this._nonStandardCreation));
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
/*      */   protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  552 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  553 */     if (intr != null) {
/*  554 */       Object convDef = intr.findDeserializationConverter(prop.getMember());
/*  555 */       if (convDef != null) {
/*  556 */         com.fasterxml.jackson.databind.util.Converter<Object, Object> conv = ctxt.converterInstance(prop.getMember(), convDef);
/*  557 */         JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*  558 */         JsonDeserializer<?> ser = ctxt.findContextualValueDeserializer(delegateType, prop);
/*  559 */         return new com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer(conv, delegateType, ser);
/*      */       }
/*      */     }
/*  562 */     return null;
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
/*      */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, com.fasterxml.jackson.databind.BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  576 */     ObjectIdReader oir = this._objectIdReader;
/*      */     
/*      */ 
/*  579 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  580 */     com.fasterxml.jackson.databind.introspect.AnnotatedMember accessor = (property == null) || (intr == null) ? null : property.getMember();
/*      */     
/*  582 */     if ((accessor != null) && (intr != null)) {
/*  583 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo(accessor);
/*  584 */       if (objectIdInfo != null)
/*      */       {
/*  586 */         objectIdInfo = intr.findObjectReferenceInfo(accessor, objectIdInfo);
/*      */         
/*  588 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  593 */         com.fasterxml.jackson.annotation.ObjectIdResolver resolver = ctxt.objectIdResolverInstance(accessor, objectIdInfo);
/*  594 */         com.fasterxml.jackson.annotation.ObjectIdGenerator<?> idGen; JavaType idType; SettableBeanProperty idProp; com.fasterxml.jackson.annotation.ObjectIdGenerator<?> idGen; if (implClass == com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class) {
/*  595 */           PropertyName propName = objectIdInfo.getPropertyName();
/*  596 */           SettableBeanProperty idProp = findProperty(propName);
/*  597 */           if (idProp == null) {
/*  598 */             throw new IllegalArgumentException("Invalid Object Id definition for " + handledType().getName() + ": can not find property with name '" + propName + "'");
/*      */           }
/*      */           
/*  601 */           JavaType idType = idProp.getType();
/*  602 */           idGen = new com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*      */         } else {
/*  604 */           JavaType type = ctxt.constructType(implClass);
/*  605 */           idType = ctxt.getTypeFactory().findTypeParameters(type, com.fasterxml.jackson.annotation.ObjectIdGenerator.class)[0];
/*  606 */           idProp = null;
/*  607 */           idGen = ctxt.objectIdGeneratorInstance(accessor, objectIdInfo);
/*      */         }
/*  609 */         JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/*  610 */         oir = ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), idGen, deser, idProp, resolver);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  615 */     BeanDeserializerBase contextual = this;
/*  616 */     if ((oir != null) && (oir != this._objectIdReader)) {
/*  617 */       contextual = contextual.withObjectIdReader(oir);
/*      */     }
/*      */     
/*  620 */     if (accessor != null) {
/*  621 */       String[] ignorals = intr.findPropertiesToIgnore(accessor);
/*  622 */       if ((ignorals != null) && (ignorals.length != 0)) {
/*  623 */         HashSet<String> newIgnored = com.fasterxml.jackson.databind.util.ArrayBuilders.setAndArray(contextual._ignorableProps, ignorals);
/*  624 */         contextual = contextual.withIgnorableProperties(newIgnored);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  629 */     com.fasterxml.jackson.annotation.JsonFormat.Shape shape = null;
/*  630 */     if (accessor != null) {
/*  631 */       com.fasterxml.jackson.annotation.JsonFormat.Value format = intr.findFormat(accessor);
/*      */       
/*  633 */       if (format != null) {
/*  634 */         shape = format.getShape();
/*      */       }
/*      */     }
/*  637 */     if (shape == null) {
/*  638 */       shape = this._serializationShape;
/*      */     }
/*  640 */     if (shape == com.fasterxml.jackson.annotation.JsonFormat.Shape.ARRAY) {
/*  641 */       contextual = contextual.asArrayDeserializer();
/*      */     }
/*  643 */     return contextual;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableBeanProperty _resolveManagedReferenceProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  653 */     String refName = prop.getManagedReferenceName();
/*  654 */     if (refName == null) {
/*  655 */       return prop;
/*      */     }
/*  657 */     JsonDeserializer<?> valueDeser = prop.getValueDeserializer();
/*  658 */     SettableBeanProperty backProp = valueDeser.findBackReference(refName);
/*  659 */     if (backProp == null) {
/*  660 */       throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': no back reference property found from type " + prop.getType());
/*      */     }
/*      */     
/*      */ 
/*  664 */     JavaType referredType = this._beanType;
/*  665 */     JavaType backRefType = backProp.getType();
/*  666 */     boolean isContainer = prop.getType().isContainerType();
/*  667 */     if (!backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
/*  668 */       throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': back reference type (" + backRefType.getRawClass().getName() + ") not compatible with managed type (" + referredType.getRawClass().getName() + ")");
/*      */     }
/*      */     
/*      */ 
/*  672 */     return new com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty(prop, refName, backProp, this._classAnnotations, isContainer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableBeanProperty _resolvedObjectIdProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  682 */     ObjectIdInfo objectIdInfo = prop.getObjectIdInfo();
/*  683 */     JsonDeserializer<Object> valueDeser = prop.getValueDeserializer();
/*  684 */     ObjectIdReader objectIdReader = valueDeser.getObjectIdReader();
/*  685 */     if ((objectIdInfo == null) && (objectIdReader == null)) {
/*  686 */       return prop;
/*      */     }
/*      */     
/*  689 */     return new com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty(prop, objectIdInfo);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableBeanProperty _resolveUnwrappedProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  699 */     com.fasterxml.jackson.databind.introspect.AnnotatedMember am = prop.getMember();
/*  700 */     if (am != null) {
/*  701 */       NameTransformer unwrapper = ctxt.getAnnotationIntrospector().findUnwrappingNameTransformer(am);
/*  702 */       if (unwrapper != null) {
/*  703 */         JsonDeserializer<Object> orig = prop.getValueDeserializer();
/*  704 */         JsonDeserializer<Object> unwrapping = orig.unwrappingDeserializer(unwrapper);
/*  705 */         if ((unwrapping != orig) && (unwrapping != null))
/*      */         {
/*  707 */           return prop.withValueDeserializer(unwrapping);
/*      */         }
/*      */       }
/*      */     }
/*  711 */     return null;
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
/*      */   protected SettableBeanProperty _resolveInnerClassValuedProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  724 */     JsonDeserializer<Object> deser = prop.getValueDeserializer();
/*      */     
/*  726 */     if ((deser instanceof BeanDeserializerBase)) {
/*  727 */       BeanDeserializerBase bd = (BeanDeserializerBase)deser;
/*  728 */       ValueInstantiator vi = bd.getValueInstantiator();
/*  729 */       if (!vi.canCreateUsingDefault()) {
/*  730 */         Class<?> valueClass = prop.getType().getRawClass();
/*  731 */         Class<?> enclosing = com.fasterxml.jackson.databind.util.ClassUtil.getOuterClass(valueClass);
/*      */         
/*  733 */         if ((enclosing != null) && (enclosing == this._beanType.getRawClass())) {
/*  734 */           for (java.lang.reflect.Constructor<?> ctor : valueClass.getConstructors()) {
/*  735 */             Class<?>[] paramTypes = ctor.getParameterTypes();
/*  736 */             if ((paramTypes.length == 1) && (paramTypes[0] == enclosing)) {
/*  737 */               if (ctxt.getConfig().canOverrideAccessModifiers()) {
/*  738 */                 com.fasterxml.jackson.databind.util.ClassUtil.checkAndFixAccess(ctor);
/*      */               }
/*  740 */               return new com.fasterxml.jackson.databind.deser.impl.InnerClassProperty(prop, ctor);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  746 */     return prop;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCachable()
/*      */   {
/*  756 */     return true;
/*      */   }
/*      */   
/*      */   public Class<?> handledType() {
/*  760 */     return this._beanType.getRawClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectIdReader getObjectIdReader()
/*      */   {
/*  770 */     return this._objectIdReader;
/*      */   }
/*      */   
/*      */   public boolean hasProperty(String propertyName) {
/*  774 */     return this._beanProperties.find(propertyName) != null;
/*      */   }
/*      */   
/*      */   public boolean hasViews() {
/*  778 */     return this._needViewProcesing;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getPropertyCount()
/*      */   {
/*  785 */     return this._beanProperties.size();
/*      */   }
/*      */   
/*      */   public java.util.Collection<Object> getKnownPropertyNames()
/*      */   {
/*  790 */     java.util.ArrayList<Object> names = new java.util.ArrayList();
/*  791 */     for (SettableBeanProperty prop : this._beanProperties) {
/*  792 */       names.add(prop.getName());
/*      */     }
/*  794 */     return names;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public final Class<?> getBeanClass()
/*      */   {
/*  801 */     return this._beanType.getRawClass();
/*      */   }
/*      */   
/*  804 */   public JavaType getValueType() { return this._beanType; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.util.Iterator<SettableBeanProperty> properties()
/*      */   {
/*  815 */     if (this._beanProperties == null) {
/*  816 */       throw new IllegalStateException("Can only call after BeanDeserializer has been resolved");
/*      */     }
/*  818 */     return this._beanProperties.iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.util.Iterator<SettableBeanProperty> creatorProperties()
/*      */   {
/*  830 */     if (this._propertyBasedCreator == null) {
/*  831 */       return java.util.Collections.emptyList().iterator();
/*      */     }
/*  833 */     return this._propertyBasedCreator.properties().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */   public SettableBeanProperty findProperty(PropertyName propertyName)
/*      */   {
/*  839 */     return findProperty(propertyName.getSimpleName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SettableBeanProperty findProperty(String propertyName)
/*      */   {
/*  851 */     SettableBeanProperty prop = this._beanProperties == null ? null : this._beanProperties.find(propertyName);
/*      */     
/*  853 */     if ((prop == null) && (this._propertyBasedCreator != null)) {
/*  854 */       prop = this._propertyBasedCreator.findCreatorProperty(propertyName);
/*      */     }
/*  856 */     return prop;
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
/*      */   public SettableBeanProperty findProperty(int propertyIndex)
/*      */   {
/*  871 */     SettableBeanProperty prop = this._beanProperties == null ? null : this._beanProperties.find(propertyIndex);
/*      */     
/*  873 */     if ((prop == null) && (this._propertyBasedCreator != null)) {
/*  874 */       prop = this._propertyBasedCreator.findCreatorProperty(propertyIndex);
/*      */     }
/*  876 */     return prop;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SettableBeanProperty findBackReference(String logicalName)
/*      */   {
/*  886 */     if (this._backRefs == null) {
/*  887 */       return null;
/*      */     }
/*  889 */     return (SettableBeanProperty)this._backRefs.get(logicalName);
/*      */   }
/*      */   
/*      */   public ValueInstantiator getValueInstantiator() {
/*  893 */     return this._valueInstantiator;
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
/*      */   public void replaceProperty(SettableBeanProperty original, SettableBeanProperty replacement)
/*      */   {
/*  917 */     this._beanProperties.replace(replacement);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract Object deserializeFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */     throws IOException
/*      */   {
/*  939 */     if (this._objectIdReader != null)
/*      */     {
/*  941 */       if (p.canReadObjectId()) {
/*  942 */         Object id = p.getObjectId();
/*  943 */         if (id != null) {
/*  944 */           Object ob = typeDeserializer.deserializeTypedFromObject(p, ctxt);
/*  945 */           return _handleTypedObjectId(p, ctxt, ob, id);
/*      */         }
/*      */       }
/*      */       
/*  949 */       JsonToken t = p.getCurrentToken();
/*  950 */       if (t != null)
/*      */       {
/*  952 */         if (t.isScalarValue()) {
/*  953 */           return deserializeFromObjectId(p, ctxt);
/*      */         }
/*      */         
/*  956 */         if (t == JsonToken.START_OBJECT) {
/*  957 */           t = p.nextToken();
/*      */         }
/*  959 */         if ((t == JsonToken.FIELD_NAME) && (this._objectIdReader.maySerializeAsObject()) && (this._objectIdReader.isValidReferencePropertyName(p.getCurrentName(), p)))
/*      */         {
/*  961 */           return deserializeFromObjectId(p, ctxt);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  966 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
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
/*      */   protected Object _handleTypedObjectId(JsonParser jp, DeserializationContext ctxt, Object pojo, Object rawId)
/*      */     throws IOException
/*      */   {
/*  983 */     JsonDeserializer<Object> idDeser = this._objectIdReader.getDeserializer();
/*      */     
/*      */     Object id;
/*      */     Object id;
/*  987 */     if (idDeser.handledType() == rawId.getClass())
/*      */     {
/*  989 */       id = rawId;
/*      */     } else {
/*  991 */       id = _convertObjectId(jp, ctxt, rawId, idDeser);
/*      */     }
/*      */     
/*  994 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*  995 */     roid.bindItem(pojo);
/*      */     
/*  997 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/*  998 */     if (idProp != null) {
/*  999 */       return idProp.setAndReturn(pojo, id);
/*      */     }
/* 1001 */     return pojo;
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
/*      */   protected Object _convertObjectId(JsonParser jp, DeserializationContext ctxt, Object rawId, JsonDeserializer<Object> idDeser)
/*      */     throws IOException
/*      */   {
/* 1017 */     TokenBuffer buf = new TokenBuffer(jp);
/* 1018 */     if ((rawId instanceof String)) {
/* 1019 */       buf.writeString((String)rawId);
/* 1020 */     } else if ((rawId instanceof Long)) {
/* 1021 */       buf.writeNumber(((Long)rawId).longValue());
/* 1022 */     } else if ((rawId instanceof Integer)) {
/* 1023 */       buf.writeNumber(((Integer)rawId).intValue());
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1028 */       buf.writeObject(rawId);
/*      */     }
/* 1030 */     JsonParser bufParser = buf.asParser();
/* 1031 */     bufParser.nextToken();
/* 1032 */     return idDeser.deserialize(bufParser, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object deserializeWithObjectId(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1045 */     return deserializeFromObject(jp, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object deserializeFromObjectId(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1054 */     Object id = this._objectIdReader.readObjectReference(jp, ctxt);
/* 1055 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*      */     
/* 1057 */     Object pojo = roid.resolve();
/* 1058 */     if (pojo == null) {
/* 1059 */       throw new UnresolvedForwardReference("Could not resolve Object Id [" + id + "] (for " + this._beanType + ").", jp.getCurrentLocation(), roid);
/*      */     }
/*      */     
/* 1062 */     return pojo;
/*      */   }
/*      */   
/*      */   protected Object deserializeFromObjectUsingNonDefault(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1068 */     if (this._delegateDeserializer != null) {
/* 1069 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*      */     }
/*      */     
/* 1072 */     if (this._propertyBasedCreator != null) {
/* 1073 */       return _deserializeUsingPropertyBased(jp, ctxt);
/*      */     }
/*      */     
/* 1076 */     if (this._beanType.isAbstract()) {
/* 1077 */       throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
/*      */     }
/*      */     
/* 1080 */     throw JsonMappingException.from(jp, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (missing default constructor or creator, or perhaps need to add/enable type information?)");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected abstract Object _deserializeUsingPropertyBased(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*      */     throws IOException, JsonProcessingException;
/*      */   
/*      */ 
/*      */   public Object deserializeFromNumber(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1092 */     if (this._objectIdReader != null) {
/* 1093 */       return deserializeFromObjectId(jp, ctxt);
/*      */     }
/*      */     
/* 1096 */     switch (jp.getNumberType()) {
/*      */     case INT: 
/* 1098 */       if ((this._delegateDeserializer != null) && 
/* 1099 */         (!this._valueInstantiator.canCreateFromInt())) {
/* 1100 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/* 1101 */         if (this._injectables != null) {
/* 1102 */           injectValues(ctxt, bean);
/*      */         }
/* 1104 */         return bean;
/*      */       }
/*      */       
/* 1107 */       return this._valueInstantiator.createFromInt(ctxt, jp.getIntValue());
/*      */     case LONG: 
/* 1109 */       if ((this._delegateDeserializer != null) && 
/* 1110 */         (!this._valueInstantiator.canCreateFromInt())) {
/* 1111 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/* 1112 */         if (this._injectables != null) {
/* 1113 */           injectValues(ctxt, bean);
/*      */         }
/* 1115 */         return bean;
/*      */       }
/*      */       
/* 1118 */       return this._valueInstantiator.createFromLong(ctxt, jp.getLongValue());
/*      */     }
/*      */     
/* 1121 */     if (this._delegateDeserializer != null) {
/* 1122 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/* 1123 */       if (this._injectables != null) {
/* 1124 */         injectValues(ctxt, bean);
/*      */       }
/* 1126 */       return bean;
/*      */     }
/* 1128 */     throw ctxt.instantiationException(handledType(), "no suitable creator method found to deserialize from JSON integer number");
/*      */   }
/*      */   
/*      */   public Object deserializeFromString(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1134 */     if (this._objectIdReader != null) {
/* 1135 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1141 */     if ((this._delegateDeserializer != null) && 
/* 1142 */       (!this._valueInstantiator.canCreateFromString())) {
/* 1143 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/* 1144 */       if (this._injectables != null) {
/* 1145 */         injectValues(ctxt, bean);
/*      */       }
/* 1147 */       return bean;
/*      */     }
/*      */     
/* 1150 */     return this._valueInstantiator.createFromString(ctxt, p.getText());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object deserializeFromDouble(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1160 */     switch (p.getNumberType()) {
/*      */     case FLOAT: 
/*      */     case DOUBLE: 
/* 1163 */       if ((this._delegateDeserializer != null) && 
/* 1164 */         (!this._valueInstantiator.canCreateFromDouble())) {
/* 1165 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/* 1166 */         if (this._injectables != null) {
/* 1167 */           injectValues(ctxt, bean);
/*      */         }
/* 1169 */         return bean;
/*      */       }
/*      */       
/* 1172 */       return this._valueInstantiator.createFromDouble(ctxt, p.getDoubleValue());
/*      */     }
/*      */     
/* 1175 */     if (this._delegateDeserializer != null) {
/* 1176 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*      */     }
/* 1178 */     throw ctxt.instantiationException(handledType(), "no suitable creator method found to deserialize from JSON floating-point number");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object deserializeFromBoolean(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1186 */     if ((this._delegateDeserializer != null) && 
/* 1187 */       (!this._valueInstantiator.canCreateFromBoolean())) {
/* 1188 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/* 1189 */       if (this._injectables != null) {
/* 1190 */         injectValues(ctxt, bean);
/*      */       }
/* 1192 */       return bean;
/*      */     }
/*      */     
/* 1195 */     boolean value = p.getCurrentToken() == JsonToken.VALUE_TRUE;
/* 1196 */     return this._valueInstantiator.createFromBoolean(ctxt, value);
/*      */   }
/*      */   
/*      */   public Object deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException
/*      */   {
/* 1201 */     if (this._delegateDeserializer != null) {
/*      */       try {
/* 1203 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/* 1204 */         if (this._injectables != null) {
/* 1205 */           injectValues(ctxt, bean);
/*      */         }
/* 1207 */         return bean;
/*      */       } catch (Exception e) {
/* 1209 */         wrapInstantiationProblem(e, ctxt);
/*      */       }
/* 1211 */     } else { if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 1212 */         JsonToken t = p.nextToken();
/* 1213 */         if ((t == JsonToken.END_ARRAY) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT))) {
/* 1214 */           return null;
/*      */         }
/* 1216 */         Object value = deserialize(p, ctxt);
/* 1217 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/* 1218 */           throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + this._valueClass.getName() + "' value but there was more than a single value in the array");
/*      */         }
/*      */         
/* 1221 */         return value; }
/* 1222 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/* 1223 */         JsonToken t = p.nextToken();
/* 1224 */         if (t == JsonToken.END_ARRAY) {
/* 1225 */           return null;
/*      */         }
/* 1227 */         throw ctxt.mappingException(handledType(), JsonToken.START_ARRAY);
/*      */       } }
/* 1229 */     throw ctxt.mappingException(handledType());
/*      */   }
/*      */   
/*      */ 
/*      */   public Object deserializeFromEmbedded(JsonParser jp, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1236 */     if (this._objectIdReader != null) {
/* 1237 */       return deserializeFromObjectId(jp, ctxt);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1242 */     return jp.getEmbeddedObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void injectValues(DeserializationContext ctxt, Object bean)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1254 */     for (ValueInjector injector : this._injectables) {
/* 1255 */       injector.inject(ctxt, bean);
/*      */     }
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
/*      */   protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1270 */     unknownTokens.writeEndObject();
/*      */     
/*      */ 
/* 1273 */     JsonParser bufferParser = unknownTokens.asParser();
/* 1274 */     while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
/* 1275 */       String propName = bufferParser.getCurrentName();
/*      */       
/* 1277 */       bufferParser.nextToken();
/* 1278 */       handleUnknownProperty(bufferParser, ctxt, bean, propName);
/*      */     }
/* 1280 */     return bean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleUnknownVanilla(JsonParser jp, DeserializationContext ctxt, Object bean, String propName)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1291 */     if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 1292 */       handleIgnoredProperty(jp, ctxt, bean, propName);
/* 1293 */     } else if (this._anySetter != null) {
/*      */       try
/*      */       {
/* 1296 */         this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
/*      */       } catch (Exception e) {
/* 1298 */         wrapAndThrow(e, bean, propName, ctxt);
/*      */       }
/*      */       
/*      */     } else {
/* 1302 */       handleUnknownProperty(jp, ctxt, bean, propName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1315 */     if (this._ignoreAllUnknown) {
/* 1316 */       jp.skipChildren();
/* 1317 */       return;
/*      */     }
/* 1319 */     if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 1320 */       handleIgnoredProperty(jp, ctxt, beanOrClass, propName);
/*      */     }
/*      */     
/*      */ 
/* 1324 */     super.handleUnknownProperty(jp, ctxt, beanOrClass, propName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleIgnoredProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1337 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)) {
/* 1338 */       throw com.fasterxml.jackson.databind.exc.IgnoredPropertyException.from(jp, beanOrClass, propName, getKnownPropertyNames());
/*      */     }
/* 1340 */     jp.skipChildren();
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
/*      */   protected Object handlePolymorphic(JsonParser jp, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1360 */     JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
/* 1361 */     if (subDeser != null) {
/* 1362 */       if (unknownTokens != null)
/*      */       {
/* 1364 */         unknownTokens.writeEndObject();
/* 1365 */         JsonParser p2 = unknownTokens.asParser();
/* 1366 */         p2.nextToken();
/* 1367 */         bean = subDeser.deserialize(p2, ctxt, bean);
/*      */       }
/*      */       
/* 1370 */       if (jp != null) {
/* 1371 */         bean = subDeser.deserialize(jp, ctxt, bean);
/*      */       }
/* 1373 */       return bean;
/*      */     }
/*      */     
/* 1376 */     if (unknownTokens != null) {
/* 1377 */       bean = handleUnknownProperties(ctxt, bean, unknownTokens);
/*      */     }
/*      */     
/* 1380 */     if (jp != null) {
/* 1381 */       bean = deserialize(jp, ctxt, bean);
/*      */     }
/* 1383 */     return bean;
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
/*      */   protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1397 */     synchronized (this) {
/* 1398 */       subDeser = this._subDeserializers == null ? null : (JsonDeserializer)this._subDeserializers.get(new com.fasterxml.jackson.databind.type.ClassKey(bean.getClass()));
/*      */     }
/* 1400 */     if (subDeser != null) {
/* 1401 */       return subDeser;
/*      */     }
/*      */     
/* 1404 */     JavaType type = ctxt.constructType(bean.getClass());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1411 */     JsonDeserializer<Object> subDeser = ctxt.findRootValueDeserializer(type);
/*      */     
/* 1413 */     if (subDeser != null) {
/* 1414 */       synchronized (this) {
/* 1415 */         if (this._subDeserializers == null) {
/* 1416 */           this._subDeserializers = new java.util.HashMap();
/*      */         }
/* 1418 */         this._subDeserializers.put(new com.fasterxml.jackson.databind.type.ClassKey(bean.getClass()), subDeser);
/*      */       }
/*      */     }
/* 1421 */     return subDeser;
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
/*      */   public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1446 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, fieldName);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void wrapAndThrow(Throwable t, Object bean, int index, DeserializationContext ctxt) throws IOException
/*      */   {
/* 1452 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Throwable throwOrReturnThrowable(Throwable t, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1462 */     while (((t instanceof java.lang.reflect.InvocationTargetException)) && (t.getCause() != null)) {
/* 1463 */       t = t.getCause();
/*      */     }
/*      */     
/* 1466 */     if ((t instanceof Error)) {
/* 1467 */       throw ((Error)t);
/*      */     }
/* 1469 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*      */     
/* 1471 */     if ((t instanceof IOException)) {
/* 1472 */       if ((!wrap) || (!(t instanceof JsonProcessingException))) {
/* 1473 */         throw ((IOException)t);
/*      */       }
/* 1475 */     } else if ((!wrap) && 
/* 1476 */       ((t instanceof RuntimeException))) {
/* 1477 */       throw ((RuntimeException)t);
/*      */     }
/*      */     
/* 1480 */     return t;
/*      */   }
/*      */   
/*      */   protected void wrapInstantiationProblem(Throwable t, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1486 */     while (((t instanceof java.lang.reflect.InvocationTargetException)) && (t.getCause() != null)) {
/* 1487 */       t = t.getCause();
/*      */     }
/*      */     
/* 1490 */     if ((t instanceof Error)) {
/* 1491 */       throw ((Error)t);
/*      */     }
/* 1493 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/* 1494 */     if ((t instanceof IOException))
/*      */     {
/* 1496 */       throw ((IOException)t); }
/* 1497 */     if ((!wrap) && 
/* 1498 */       ((t instanceof RuntimeException))) {
/* 1499 */       throw ((RuntimeException)t);
/*      */     }
/*      */     
/* 1502 */     throw ctxt.instantiationException(this._beanType.getRawClass(), t);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */