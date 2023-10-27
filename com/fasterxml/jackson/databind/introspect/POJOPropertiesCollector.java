/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.util.BeanUtil;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public class POJOPropertiesCollector
/*     */ {
/*     */   protected final MapperConfig<?> _config;
/*     */   protected final boolean _forSerialization;
/*     */   protected final boolean _stdBeanNaming;
/*     */   protected final JavaType _type;
/*     */   protected final AnnotatedClass _classDef;
/*     */   protected final VisibilityChecker<?> _visibilityChecker;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected final String _mutatorPrefix;
/*  69 */   protected final LinkedHashMap<String, POJOPropertyBuilder> _properties = new LinkedHashMap();
/*     */   
/*     */ 
/*  72 */   protected LinkedList<POJOPropertyBuilder> _creatorProperties = null;
/*     */   
/*  74 */   protected LinkedList<AnnotatedMember> _anyGetters = null;
/*     */   
/*  76 */   protected LinkedList<AnnotatedMethod> _anySetters = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected LinkedList<AnnotatedMethod> _jsonValueGetters = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashSet<String> _ignoredPropertyNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LinkedHashMap<Object, AnnotatedMember> _injectables;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected POJOPropertiesCollector(MapperConfig<?> config, boolean forSerialization, JavaType type, AnnotatedClass classDef, String mutatorPrefix)
/*     */   {
/* 106 */     this._config = config;
/* 107 */     this._stdBeanNaming = config.isEnabled(MapperFeature.USE_STD_BEAN_NAMING);
/* 108 */     this._forSerialization = forSerialization;
/* 109 */     this._type = type;
/* 110 */     this._classDef = classDef;
/* 111 */     this._mutatorPrefix = (mutatorPrefix == null ? "set" : mutatorPrefix);
/* 112 */     this._annotationIntrospector = (config.isAnnotationProcessingEnabled() ? this._config.getAnnotationIntrospector() : null);
/*     */     
/* 114 */     if (this._annotationIntrospector == null) {
/* 115 */       this._visibilityChecker = this._config.getDefaultVisibilityChecker();
/*     */     } else {
/* 117 */       this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(classDef, this._config.getDefaultVisibilityChecker());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapperConfig<?> getConfig()
/*     */   {
/* 129 */     return this._config;
/*     */   }
/*     */   
/*     */   public JavaType getType() {
/* 133 */     return this._type;
/*     */   }
/*     */   
/*     */   public AnnotatedClass getClassDef() {
/* 137 */     return this._classDef;
/*     */   }
/*     */   
/*     */   public AnnotationIntrospector getAnnotationIntrospector() {
/* 141 */     return this._annotationIntrospector;
/*     */   }
/*     */   
/*     */   public List<BeanPropertyDefinition> getProperties()
/*     */   {
/* 146 */     return new ArrayList(this._properties.values());
/*     */   }
/*     */   
/*     */   public Map<Object, AnnotatedMember> getInjectables() {
/* 150 */     return this._injectables;
/*     */   }
/*     */   
/*     */ 
/*     */   public AnnotatedMethod getJsonValueMethod()
/*     */   {
/* 156 */     if (this._jsonValueGetters != null) {
/* 157 */       if (this._jsonValueGetters.size() > 1) {
/* 158 */         reportProblem("Multiple value properties defined (" + this._jsonValueGetters.get(0) + " vs " + this._jsonValueGetters.get(1) + ")");
/*     */       }
/*     */       
/*     */ 
/* 162 */       return (AnnotatedMethod)this._jsonValueGetters.get(0);
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedMember getAnyGetter()
/*     */   {
/* 169 */     if (this._anyGetters != null) {
/* 170 */       if (this._anyGetters.size() > 1) {
/* 171 */         reportProblem("Multiple 'any-getters' defined (" + this._anyGetters.get(0) + " vs " + this._anyGetters.get(1) + ")");
/*     */       }
/*     */       
/* 174 */       return (AnnotatedMember)this._anyGetters.getFirst();
/*     */     }
/* 176 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod getAnySetterMethod()
/*     */   {
/* 181 */     if (this._anySetters != null) {
/* 182 */       if (this._anySetters.size() > 1) {
/* 183 */         reportProblem("Multiple 'any-setters' defined (" + this._anySetters.get(0) + " vs " + this._anySetters.get(1) + ")");
/*     */       }
/*     */       
/* 186 */       return (AnnotatedMethod)this._anySetters.getFirst();
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getIgnoredPropertyNames()
/*     */   {
/* 196 */     return this._ignoredPropertyNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectIdInfo getObjectIdInfo()
/*     */   {
/* 205 */     if (this._annotationIntrospector == null) {
/* 206 */       return null;
/*     */     }
/* 208 */     ObjectIdInfo info = this._annotationIntrospector.findObjectIdInfo(this._classDef);
/* 209 */     if (info != null) {
/* 210 */       info = this._annotationIntrospector.findObjectReferenceInfo(this._classDef, info);
/*     */     }
/* 212 */     return info;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> findPOJOBuilderClass()
/*     */   {
/* 220 */     return this._annotationIntrospector.findPOJOBuilder(this._classDef);
/*     */   }
/*     */   
/*     */   protected Map<String, POJOPropertyBuilder> getPropertyMap()
/*     */   {
/* 225 */     return this._properties;
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
/*     */   public POJOPropertiesCollector collect()
/*     */   {
/* 240 */     this._properties.clear();
/*     */     
/*     */ 
/* 243 */     _addFields();
/* 244 */     _addMethods();
/* 245 */     _addCreators();
/* 246 */     _addInjectables();
/*     */     
/*     */ 
/* 249 */     _removeUnwantedProperties();
/*     */     
/*     */ 
/* 252 */     _renameProperties();
/*     */     
/* 254 */     PropertyNamingStrategy naming = _findNamingStrategy();
/* 255 */     if (naming != null) {
/* 256 */       _renameUsing(naming);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 263 */     for (POJOPropertyBuilder property : this._properties.values()) {
/* 264 */       property.trimByVisibility();
/*     */     }
/*     */     
/*     */ 
/* 268 */     for (POJOPropertyBuilder property : this._properties.values()) {
/* 269 */       property.mergeAnnotations(this._forSerialization);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 275 */     if (this._config.isEnabled(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)) {
/* 276 */       _renameWithWrappers();
/*     */     }
/*     */     
/*     */ 
/* 280 */     _sortProperties();
/* 281 */     return this;
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
/*     */   protected void _sortProperties()
/*     */   {
/* 296 */     AnnotationIntrospector intr = this._annotationIntrospector;
/*     */     
/* 298 */     Boolean alpha = intr == null ? null : intr.findSerializationSortAlphabetically(this._classDef);
/*     */     boolean sort;
/* 300 */     boolean sort; if (alpha == null) {
/* 301 */       sort = this._config.shouldSortPropertiesAlphabetically();
/*     */     } else {
/* 303 */       sort = alpha.booleanValue();
/*     */     }
/* 305 */     String[] propertyOrder = intr == null ? null : intr.findSerializationPropertyOrder(this._classDef);
/*     */     
/*     */ 
/* 308 */     if ((!sort) && (this._creatorProperties == null) && (propertyOrder == null)) {
/* 309 */       return;
/*     */     }
/* 311 */     int size = this._properties.size();
/*     */     Map<String, POJOPropertyBuilder> all;
/*     */     Map<String, POJOPropertyBuilder> all;
/* 314 */     if (sort) {
/* 315 */       all = new TreeMap();
/*     */     } else {
/* 317 */       all = new LinkedHashMap(size + size);
/*     */     }
/*     */     
/* 320 */     for (POJOPropertyBuilder prop : this._properties.values()) {
/* 321 */       all.put(prop.getName(), prop);
/*     */     }
/* 323 */     Map<String, POJOPropertyBuilder> ordered = new LinkedHashMap(size + size);
/*     */     
/* 325 */     if (propertyOrder != null) {
/* 326 */       for (String name : propertyOrder) {
/* 327 */         POJOPropertyBuilder w = (POJOPropertyBuilder)all.get(name);
/* 328 */         if (w == null) {
/* 329 */           for (POJOPropertyBuilder prop : this._properties.values()) {
/* 330 */             if (name.equals(prop.getInternalName())) {
/* 331 */               w = prop;
/*     */               
/* 333 */               name = prop.getName();
/* 334 */               break;
/*     */             }
/*     */           }
/*     */         }
/* 338 */         if (w != null) {
/* 339 */           ordered.put(name, w);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 344 */     if (this._creatorProperties != null)
/*     */     {
/*     */       Collection<POJOPropertyBuilder> cr;
/*     */       
/*     */ 
/*     */       Collection<POJOPropertyBuilder> cr;
/*     */       
/* 351 */       if (sort) {
/* 352 */         TreeMap<String, POJOPropertyBuilder> sorted = new TreeMap();
/*     */         
/* 354 */         for (POJOPropertyBuilder prop : this._creatorProperties) {
/* 355 */           sorted.put(prop.getName(), prop);
/*     */         }
/* 357 */         cr = sorted.values();
/*     */       } else {
/* 359 */         cr = this._creatorProperties;
/*     */       }
/* 361 */       for (POJOPropertyBuilder prop : cr) {
/* 362 */         ordered.put(prop.getName(), prop);
/*     */       }
/*     */     }
/*     */     
/* 366 */     ordered.putAll(all);
/*     */     
/* 368 */     this._properties.clear();
/* 369 */     this._properties.putAll(ordered);
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
/*     */   protected void _addFields()
/*     */   {
/* 383 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 388 */     boolean pruneFinalFields = (!this._forSerialization) && (!this._config.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS));
/*     */     
/* 390 */     for (AnnotatedField f : this._classDef.fields()) {
/* 391 */       String implName = ai == null ? null : ai.findImplicitPropertyName(f);
/* 392 */       if (implName == null) {
/* 393 */         implName = f.getName();
/*     */       }
/*     */       
/*     */       PropertyName pn;
/*     */       PropertyName pn;
/* 398 */       if (ai == null) {
/* 399 */         pn = null; } else { PropertyName pn;
/* 400 */         if (this._forSerialization)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 406 */           pn = ai.findNameForSerialization(f);
/*     */         } else
/* 408 */           pn = ai.findNameForDeserialization(f);
/*     */       }
/* 410 */       boolean nameExplicit = pn != null;
/*     */       
/* 412 */       if ((nameExplicit) && (pn.isEmpty())) {
/* 413 */         pn = _propNameFromSimple(implName);
/* 414 */         nameExplicit = false;
/*     */       }
/*     */       
/* 417 */       boolean visible = pn != null;
/* 418 */       if (!visible) {
/* 419 */         visible = this._visibilityChecker.isFieldVisible(f);
/*     */       }
/*     */       
/* 422 */       boolean ignored = (ai != null) && (ai.hasIgnoreMarker(f));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 428 */       if ((!pruneFinalFields) || (pn != null) || (ignored) || (!Modifier.isFinal(f.getModifiers())))
/*     */       {
/*     */ 
/* 431 */         _property(implName).addField(f, pn, nameExplicit, visible, ignored);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _addCreators()
/*     */   {
/* 441 */     if (this._annotationIntrospector != null) {
/* 442 */       for (AnnotatedConstructor ctor : this._classDef.getConstructors()) {
/* 443 */         if (this._creatorProperties == null) {
/* 444 */           this._creatorProperties = new LinkedList();
/*     */         }
/* 446 */         int i = 0; for (int len = ctor.getParameterCount(); i < len; i++) {
/* 447 */           _addCreatorParam(ctor.getParameter(i));
/*     */         }
/*     */       }
/* 450 */       for (AnnotatedMethod factory : this._classDef.getStaticMethods()) {
/* 451 */         if (this._creatorProperties == null) {
/* 452 */           this._creatorProperties = new LinkedList();
/*     */         }
/* 454 */         int i = 0; for (int len = factory.getParameterCount(); i < len; i++) {
/* 455 */           _addCreatorParam(factory.getParameter(i));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _addCreatorParam(AnnotatedParameter param)
/*     */   {
/* 467 */     String impl = this._annotationIntrospector.findImplicitPropertyName(param);
/* 468 */     if (impl == null) {
/* 469 */       impl = "";
/*     */     }
/* 471 */     PropertyName pn = this._annotationIntrospector.findNameForDeserialization(param);
/* 472 */     boolean expl = (pn != null) && (!pn.isEmpty());
/* 473 */     if (!expl) {
/* 474 */       if (impl.isEmpty())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 479 */         return;
/*     */       }
/*     */       
/* 482 */       if (!this._annotationIntrospector.hasCreatorAnnotation(param.getOwner())) {
/* 483 */         return;
/*     */       }
/* 485 */       pn = new PropertyName(impl);
/*     */     }
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
/* 497 */     POJOPropertyBuilder prop = expl ? _property(pn) : _property(impl);
/*     */     
/*     */ 
/* 500 */     prop.addCtor(param, pn, expl, true, false);
/*     */     
/* 502 */     this._creatorProperties.add(prop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _addMethods()
/*     */   {
/* 510 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*     */     
/* 512 */     for (AnnotatedMethod m : this._classDef.memberMethods())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 518 */       int argCount = m.getParameterCount();
/* 519 */       if (argCount == 0) {
/* 520 */         _addGetterMethod(m, ai);
/* 521 */       } else if (argCount == 1) {
/* 522 */         _addSetterMethod(m, ai);
/* 523 */       } else if ((argCount == 2) && 
/* 524 */         (ai != null) && (ai.hasAnySetterAnnotation(m))) {
/* 525 */         if (this._anySetters == null) {
/* 526 */           this._anySetters = new LinkedList();
/*     */         }
/* 528 */         this._anySetters.add(m);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void _addGetterMethod(AnnotatedMethod m, AnnotationIntrospector ai)
/*     */   {
/* 537 */     if (!m.hasReturnType()) {
/* 538 */       return;
/*     */     }
/*     */     
/*     */ 
/* 542 */     if (ai != null) {
/* 543 */       if (ai.hasAnyGetterAnnotation(m)) {
/* 544 */         if (this._anyGetters == null) {
/* 545 */           this._anyGetters = new LinkedList();
/*     */         }
/* 547 */         this._anyGetters.add(m);
/* 548 */         return;
/*     */       }
/*     */       
/* 551 */       if (ai.hasAsValueAnnotation(m)) {
/* 552 */         if (this._jsonValueGetters == null) {
/* 553 */           this._jsonValueGetters = new LinkedList();
/*     */         }
/* 555 */         this._jsonValueGetters.add(m);
/* 556 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 562 */     PropertyName pn = ai == null ? null : ai.findNameForSerialization(m);
/* 563 */     boolean nameExplicit = pn != null;
/*     */     boolean visible;
/* 565 */     String implName; boolean visible; if (!nameExplicit) {
/* 566 */       String implName = ai == null ? null : ai.findImplicitPropertyName(m);
/* 567 */       if (implName == null)
/* 568 */         implName = BeanUtil.okNameForRegularGetter(m, m.getName(), this._stdBeanNaming);
/*     */       boolean visible;
/* 570 */       if (implName == null) {
/* 571 */         implName = BeanUtil.okNameForIsGetter(m, m.getName(), this._stdBeanNaming);
/* 572 */         if (implName == null) {
/* 573 */           return;
/*     */         }
/* 575 */         visible = this._visibilityChecker.isIsGetterVisible(m);
/*     */       } else {
/* 577 */         visible = this._visibilityChecker.isGetterVisible(m);
/*     */       }
/*     */     }
/*     */     else {
/* 581 */       implName = ai == null ? null : ai.findImplicitPropertyName(m);
/* 582 */       if (implName == null) {
/* 583 */         implName = BeanUtil.okNameForGetter(m, this._stdBeanNaming);
/*     */       }
/*     */       
/* 586 */       if (implName == null) {
/* 587 */         implName = m.getName();
/*     */       }
/* 589 */       if (pn.isEmpty())
/*     */       {
/* 591 */         pn = _propNameFromSimple(implName);
/* 592 */         nameExplicit = false;
/*     */       }
/* 594 */       visible = true;
/*     */     }
/* 596 */     boolean ignore = ai == null ? false : ai.hasIgnoreMarker(m);
/* 597 */     _property(implName).addGetter(m, pn, nameExplicit, visible, ignore);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void _addSetterMethod(AnnotatedMethod m, AnnotationIntrospector ai)
/*     */   {
/* 604 */     PropertyName pn = ai == null ? null : ai.findNameForDeserialization(m);
/* 605 */     boolean nameExplicit = pn != null;
/* 606 */     boolean visible; String implName; boolean visible; if (!nameExplicit) {
/* 607 */       String implName = ai == null ? null : ai.findImplicitPropertyName(m);
/* 608 */       if (implName == null) {
/* 609 */         implName = BeanUtil.okNameForMutator(m, this._mutatorPrefix, this._stdBeanNaming);
/*     */       }
/* 611 */       if (implName == null) {
/* 612 */         return;
/*     */       }
/* 614 */       visible = this._visibilityChecker.isSetterVisible(m);
/*     */     }
/*     */     else {
/* 617 */       implName = ai == null ? null : ai.findImplicitPropertyName(m);
/* 618 */       if (implName == null) {
/* 619 */         implName = BeanUtil.okNameForMutator(m, this._mutatorPrefix, this._stdBeanNaming);
/*     */       }
/*     */       
/* 622 */       if (implName == null) {
/* 623 */         implName = m.getName();
/*     */       }
/* 625 */       if (pn.isEmpty())
/*     */       {
/* 627 */         pn = _propNameFromSimple(implName);
/* 628 */         nameExplicit = false;
/*     */       }
/* 630 */       visible = true;
/*     */     }
/* 632 */     boolean ignore = ai == null ? false : ai.hasIgnoreMarker(m);
/* 633 */     _property(implName).addSetter(m, pn, nameExplicit, visible, ignore);
/*     */   }
/*     */   
/*     */   protected void _addInjectables()
/*     */   {
/* 638 */     AnnotationIntrospector ai = this._annotationIntrospector;
/* 639 */     if (ai == null) {
/* 640 */       return;
/*     */     }
/*     */     
/*     */ 
/* 644 */     for (AnnotatedField f : this._classDef.fields()) {
/* 645 */       _doAddInjectable(ai.findInjectableValueId(f), f);
/*     */     }
/*     */     
/* 648 */     for (AnnotatedMethod m : this._classDef.memberMethods())
/*     */     {
/*     */ 
/*     */ 
/* 652 */       if (m.getParameterCount() == 1)
/*     */       {
/*     */ 
/* 655 */         _doAddInjectable(ai.findInjectableValueId(m), m);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void _doAddInjectable(Object id, AnnotatedMember m) {
/* 661 */     if (id == null) {
/* 662 */       return;
/*     */     }
/* 664 */     if (this._injectables == null) {
/* 665 */       this._injectables = new LinkedHashMap();
/*     */     }
/* 667 */     AnnotatedMember prev = (AnnotatedMember)this._injectables.put(id, m);
/* 668 */     if (prev != null) {
/* 669 */       String type = id.getClass().getName();
/* 670 */       throw new IllegalArgumentException("Duplicate injectable value with id '" + String.valueOf(id) + "' (of type " + type + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   private PropertyName _propNameFromSimple(String simpleName)
/*     */   {
/* 676 */     return PropertyName.construct(simpleName, null);
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
/*     */   protected void _removeUnwantedProperties()
/*     */   {
/* 691 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = this._properties.entrySet().iterator();
/* 692 */     boolean forceNonVisibleRemoval = !this._config.isEnabled(MapperFeature.INFER_PROPERTY_MUTATORS);
/*     */     
/* 694 */     while (it.hasNext()) {
/* 695 */       Map.Entry<String, POJOPropertyBuilder> entry = (Map.Entry)it.next();
/* 696 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)entry.getValue();
/*     */       
/*     */ 
/* 699 */       if (!prop.anyVisible()) {
/* 700 */         it.remove();
/*     */       }
/*     */       else
/*     */       {
/* 704 */         if (prop.anyIgnorals())
/*     */         {
/* 706 */           if (!prop.isExplicitlyIncluded()) {
/* 707 */             it.remove();
/* 708 */             _addIgnored(prop.getName());
/* 709 */             continue;
/*     */           }
/*     */           
/* 712 */           prop.removeIgnored();
/* 713 */           if ((!this._forSerialization) && (!prop.couldDeserialize())) {
/* 714 */             _addIgnored(prop.getName());
/*     */           }
/*     */         }
/*     */         
/* 718 */         prop.removeNonVisible(forceNonVisibleRemoval);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void _addIgnored(String name) {
/* 724 */     if (!this._forSerialization) {
/* 725 */       if (this._ignoredPropertyNames == null) {
/* 726 */         this._ignoredPropertyNames = new HashSet();
/*     */       }
/* 728 */       this._ignoredPropertyNames.add(name);
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
/*     */   protected void _renameProperties()
/*     */   {
/* 741 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = this._properties.entrySet().iterator();
/* 742 */     LinkedList<POJOPropertyBuilder> renamed = null;
/* 743 */     while (it.hasNext()) {
/* 744 */       Map.Entry<String, POJOPropertyBuilder> entry = (Map.Entry)it.next();
/* 745 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)entry.getValue();
/*     */       
/* 747 */       Collection<PropertyName> l = prop.findExplicitNames();
/*     */       
/* 749 */       if (!l.isEmpty())
/*     */       {
/*     */ 
/* 752 */         it.remove();
/* 753 */         if (renamed == null) {
/* 754 */           renamed = new LinkedList();
/*     */         }
/*     */         
/* 757 */         if (l.size() == 1) {
/* 758 */           PropertyName n = (PropertyName)l.iterator().next();
/* 759 */           renamed.add(prop.withName(n));
/*     */         }
/*     */         else
/*     */         {
/* 763 */           renamed.addAll(prop.explode(l));
/*     */         }
/*     */       }
/*     */     }
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
/* 779 */     if (renamed != null) {
/* 780 */       for (POJOPropertyBuilder prop : renamed) {
/* 781 */         String name = prop.getName();
/* 782 */         POJOPropertyBuilder old = (POJOPropertyBuilder)this._properties.get(name);
/* 783 */         if (old == null) {
/* 784 */           this._properties.put(name, prop);
/*     */         } else {
/* 786 */           old.addAll(prop);
/*     */         }
/*     */         
/* 789 */         _updateCreatorProperty(prop, this._creatorProperties);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void _renameUsing(PropertyNamingStrategy naming)
/*     */   {
/* 796 */     POJOPropertyBuilder[] props = (POJOPropertyBuilder[])this._properties.values().toArray(new POJOPropertyBuilder[this._properties.size()]);
/* 797 */     this._properties.clear();
/* 798 */     for (POJOPropertyBuilder prop : props) {
/* 799 */       PropertyName fullName = prop.getFullName();
/* 800 */       String rename = null;
/*     */       
/*     */ 
/* 803 */       if (!prop.isExplicitlyNamed()) {
/* 804 */         if (this._forSerialization) {
/* 805 */           if (prop.hasGetter()) {
/* 806 */             rename = naming.nameForGetterMethod(this._config, prop.getGetter(), fullName.getSimpleName());
/* 807 */           } else if (prop.hasField()) {
/* 808 */             rename = naming.nameForField(this._config, prop.getField(), fullName.getSimpleName());
/*     */           }
/*     */         }
/* 811 */         else if (prop.hasSetter()) {
/* 812 */           rename = naming.nameForSetterMethod(this._config, prop.getSetter(), fullName.getSimpleName());
/* 813 */         } else if (prop.hasConstructorParameter()) {
/* 814 */           rename = naming.nameForConstructorParameter(this._config, prop.getConstructorParameter(), fullName.getSimpleName());
/* 815 */         } else if (prop.hasField()) {
/* 816 */           rename = naming.nameForField(this._config, prop.getField(), fullName.getSimpleName());
/* 817 */         } else if (prop.hasGetter())
/*     */         {
/*     */ 
/*     */ 
/* 821 */           rename = naming.nameForGetterMethod(this._config, prop.getGetter(), fullName.getSimpleName());
/*     */         }
/*     */       }
/*     */       String simpleName;
/*     */       String simpleName;
/* 826 */       if ((rename != null) && (!fullName.hasSimpleName(rename))) {
/* 827 */         prop = prop.withSimpleName(rename);
/* 828 */         simpleName = rename;
/*     */       } else {
/* 830 */         simpleName = fullName.getSimpleName();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 835 */       POJOPropertyBuilder old = (POJOPropertyBuilder)this._properties.get(simpleName);
/* 836 */       if (old == null) {
/* 837 */         this._properties.put(simpleName, prop);
/*     */       } else {
/* 839 */         old.addAll(prop);
/*     */       }
/*     */       
/* 842 */       _updateCreatorProperty(prop, this._creatorProperties);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _renameWithWrappers()
/*     */   {
/* 851 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = this._properties.entrySet().iterator();
/* 852 */     LinkedList<POJOPropertyBuilder> renamed = null;
/* 853 */     while (it.hasNext()) {
/* 854 */       Map.Entry<String, POJOPropertyBuilder> entry = (Map.Entry)it.next();
/* 855 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)entry.getValue();
/* 856 */       AnnotatedMember member = prop.getPrimaryMember();
/* 857 */       if (member != null)
/*     */       {
/*     */ 
/* 860 */         PropertyName wrapperName = this._annotationIntrospector.findWrapperName(member);
/*     */         
/*     */ 
/*     */ 
/* 864 */         if ((wrapperName != null) && (wrapperName.hasSimpleName()))
/*     */         {
/*     */ 
/* 867 */           if (!wrapperName.equals(prop.getFullName())) {
/* 868 */             if (renamed == null) {
/* 869 */               renamed = new LinkedList();
/*     */             }
/* 871 */             prop = prop.withName(wrapperName);
/* 872 */             renamed.add(prop);
/* 873 */             it.remove();
/*     */           } }
/*     */       }
/*     */     }
/* 877 */     if (renamed != null) {
/* 878 */       for (POJOPropertyBuilder prop : renamed) {
/* 879 */         String name = prop.getName();
/* 880 */         POJOPropertyBuilder old = (POJOPropertyBuilder)this._properties.get(name);
/* 881 */         if (old == null) {
/* 882 */           this._properties.put(name, prop);
/*     */         } else {
/* 884 */           old.addAll(prop);
/*     */         }
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
/*     */   protected void reportProblem(String msg)
/*     */   {
/* 898 */     throw new IllegalArgumentException("Problem with definition of " + this._classDef + ": " + msg);
/*     */   }
/*     */   
/*     */   protected POJOPropertyBuilder _property(PropertyName name) {
/* 902 */     return _property(name.getSimpleName());
/*     */   }
/*     */   
/*     */ 
/*     */   protected POJOPropertyBuilder _property(String implName)
/*     */   {
/* 908 */     POJOPropertyBuilder prop = (POJOPropertyBuilder)this._properties.get(implName);
/* 909 */     if (prop == null) {
/* 910 */       prop = new POJOPropertyBuilder(new PropertyName(implName), this._annotationIntrospector, this._forSerialization);
/*     */       
/* 912 */       this._properties.put(implName, prop);
/*     */     }
/* 914 */     return prop;
/*     */   }
/*     */   
/*     */   private PropertyNamingStrategy _findNamingStrategy()
/*     */   {
/* 919 */     Object namingDef = this._annotationIntrospector == null ? null : this._annotationIntrospector.findNamingStrategy(this._classDef);
/*     */     
/* 921 */     if (namingDef == null) {
/* 922 */       return this._config.getPropertyNamingStrategy();
/*     */     }
/* 924 */     if ((namingDef instanceof PropertyNamingStrategy)) {
/* 925 */       return (PropertyNamingStrategy)namingDef;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 930 */     if (!(namingDef instanceof Class)) {
/* 931 */       throw new IllegalStateException("AnnotationIntrospector returned PropertyNamingStrategy definition of type " + namingDef.getClass().getName() + "; expected type PropertyNamingStrategy or Class<PropertyNamingStrategy> instead");
/*     */     }
/*     */     
/* 934 */     Class<?> namingClass = (Class)namingDef;
/* 935 */     if (!PropertyNamingStrategy.class.isAssignableFrom(namingClass)) {
/* 936 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + namingClass.getName() + "; expected Class<PropertyNamingStrategy>");
/*     */     }
/*     */     
/* 939 */     HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 940 */     if (hi != null) {
/* 941 */       PropertyNamingStrategy pns = hi.namingStrategyInstance(this._config, this._classDef, namingClass);
/* 942 */       if (pns != null) {
/* 943 */         return pns;
/*     */       }
/*     */     }
/* 946 */     return (PropertyNamingStrategy)ClassUtil.createInstance(namingClass, this._config.canOverrideAccessModifiers());
/*     */   }
/*     */   
/*     */   protected void _updateCreatorProperty(POJOPropertyBuilder prop, List<POJOPropertyBuilder> creatorProperties)
/*     */   {
/* 951 */     if (creatorProperties != null) {
/* 952 */       int i = 0; for (int len = creatorProperties.size(); i < len; i++) {
/* 953 */         if (((POJOPropertyBuilder)creatorProperties.get(i)).getInternalName().equals(prop.getInternalName())) {
/* 954 */           creatorProperties.set(i, prop);
/* 955 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\POJOPropertiesCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */