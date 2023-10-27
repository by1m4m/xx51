/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonBackReference;
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonIdentityInfo;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonManagedReference;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/*     */ import com.fasterxml.jackson.annotation.JsonRootName;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.annotation.JsonUnwrapped;
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonAppend;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import java.util.List;
/*     */ 
/*     */ public class JacksonAnnotationIntrospector extends com.fasterxml.jackson.databind.AnnotationIntrospector implements java.io.Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public com.fasterxml.jackson.core.Version version()
/*     */   {
/*  36 */     return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
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
/*     */   public boolean isAnnotationBundle(java.lang.annotation.Annotation ann)
/*     */   {
/*  51 */     return ann.annotationType().getAnnotation(com.fasterxml.jackson.annotation.JacksonAnnotationsInside.class) != null;
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
/*     */   public PropertyName findRootName(AnnotatedClass ac)
/*     */   {
/*  72 */     JsonRootName ann = (JsonRootName)_findAnnotation(ac, JsonRootName.class);
/*  73 */     if (ann == null) {
/*  74 */       return null;
/*     */     }
/*  76 */     String ns = ann.namespace();
/*  77 */     if ((ns != null) && (ns.length() == 0)) {
/*  78 */       ns = null;
/*     */     }
/*  80 */     return PropertyName.construct(ann.value(), ns);
/*     */   }
/*     */   
/*     */   public String[] findPropertiesToIgnore(Annotated ac)
/*     */   {
/*  85 */     JsonIgnoreProperties ignore = (JsonIgnoreProperties)_findAnnotation(ac, JsonIgnoreProperties.class);
/*  86 */     return ignore == null ? null : ignore.value();
/*     */   }
/*     */   
/*     */   public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*     */   {
/*  91 */     JsonIgnoreProperties ignore = (JsonIgnoreProperties)_findAnnotation(ac, JsonIgnoreProperties.class);
/*  92 */     return ignore == null ? null : Boolean.valueOf(ignore.ignoreUnknown());
/*     */   }
/*     */   
/*     */   public Boolean isIgnorableType(AnnotatedClass ac)
/*     */   {
/*  97 */     com.fasterxml.jackson.annotation.JsonIgnoreType ignore = (com.fasterxml.jackson.annotation.JsonIgnoreType)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonIgnoreType.class);
/*  98 */     return ignore == null ? null : Boolean.valueOf(ignore.value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Object findFilterId(AnnotatedClass ac)
/*     */   {
/* 107 */     return _findFilterId(ac);
/*     */   }
/*     */   
/*     */   public Object findFilterId(Annotated a)
/*     */   {
/* 112 */     return _findFilterId(a);
/*     */   }
/*     */   
/*     */   protected final Object _findFilterId(Annotated a)
/*     */   {
/* 117 */     com.fasterxml.jackson.annotation.JsonFilter ann = (com.fasterxml.jackson.annotation.JsonFilter)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonFilter.class);
/* 118 */     if (ann != null) {
/* 119 */       String id = ann.value();
/*     */       
/* 121 */       if (id.length() > 0) {
/* 122 */         return id;
/*     */       }
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findNamingStrategy(AnnotatedClass ac)
/*     */   {
/* 131 */     com.fasterxml.jackson.databind.annotation.JsonNaming ann = (com.fasterxml.jackson.databind.annotation.JsonNaming)_findAnnotation(ac, com.fasterxml.jackson.databind.annotation.JsonNaming.class);
/* 132 */     return ann == null ? null : ann.value();
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
/*     */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*     */   {
/* 145 */     com.fasterxml.jackson.annotation.JsonAutoDetect ann = (com.fasterxml.jackson.annotation.JsonAutoDetect)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonAutoDetect.class);
/* 146 */     return ann == null ? checker : checker.with(ann);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member)
/*     */   {
/* 158 */     JsonManagedReference ref1 = (JsonManagedReference)_findAnnotation(member, JsonManagedReference.class);
/* 159 */     if (ref1 != null) {
/* 160 */       return com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.managed(ref1.value());
/*     */     }
/* 162 */     JsonBackReference ref2 = (JsonBackReference)_findAnnotation(member, JsonBackReference.class);
/* 163 */     if (ref2 != null) {
/* 164 */       return com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.back(ref2.value());
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public com.fasterxml.jackson.databind.util.NameTransformer findUnwrappingNameTransformer(AnnotatedMember member)
/*     */   {
/* 172 */     JsonUnwrapped ann = (JsonUnwrapped)_findAnnotation(member, JsonUnwrapped.class);
/*     */     
/*     */ 
/* 175 */     if ((ann == null) || (!ann.enabled())) {
/* 176 */       return null;
/*     */     }
/* 178 */     String prefix = ann.prefix();
/* 179 */     String suffix = ann.suffix();
/* 180 */     return com.fasterxml.jackson.databind.util.NameTransformer.simpleTransformer(prefix, suffix);
/*     */   }
/*     */   
/*     */   public boolean hasIgnoreMarker(AnnotatedMember m)
/*     */   {
/* 185 */     return _isIgnorable(m);
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean hasRequiredMarker(AnnotatedMember m)
/*     */   {
/* 191 */     JsonProperty ann = (JsonProperty)_findAnnotation(m, JsonProperty.class);
/* 192 */     if (ann != null) {
/* 193 */       return Boolean.valueOf(ann.required());
/*     */     }
/* 195 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findInjectableValueId(AnnotatedMember m)
/*     */   {
/* 201 */     com.fasterxml.jackson.annotation.JacksonInject ann = (com.fasterxml.jackson.annotation.JacksonInject)_findAnnotation(m, com.fasterxml.jackson.annotation.JacksonInject.class);
/* 202 */     if (ann == null) {
/* 203 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 208 */     String id = ann.value();
/* 209 */     if (id.length() == 0)
/*     */     {
/* 211 */       if (!(m instanceof AnnotatedMethod)) {
/* 212 */         return m.getRawType().getName();
/*     */       }
/* 214 */       AnnotatedMethod am = (AnnotatedMethod)m;
/* 215 */       if (am.getParameterCount() == 0) {
/* 216 */         return m.getRawType().getName();
/*     */       }
/* 218 */       return am.getRawParameterType(0).getName();
/*     */     }
/* 220 */     return id;
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
/*     */   public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType)
/*     */   {
/* 233 */     return _findTypeResolver(config, ac, baseType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*     */   {
/* 243 */     if (baseType.isContainerType()) { return null;
/*     */     }
/* 245 */     return _findTypeResolver(config, am, baseType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType)
/*     */   {
/* 255 */     if (!containerType.isContainerType()) {
/* 256 */       throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
/*     */     }
/* 258 */     return _findTypeResolver(config, am, containerType);
/*     */   }
/*     */   
/*     */ 
/*     */   public List<com.fasterxml.jackson.databind.jsontype.NamedType> findSubtypes(Annotated a)
/*     */   {
/* 264 */     com.fasterxml.jackson.annotation.JsonSubTypes t = (com.fasterxml.jackson.annotation.JsonSubTypes)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonSubTypes.class);
/* 265 */     if (t == null) return null;
/* 266 */     com.fasterxml.jackson.annotation.JsonSubTypes.Type[] types = t.value();
/* 267 */     java.util.ArrayList<com.fasterxml.jackson.databind.jsontype.NamedType> result = new java.util.ArrayList(types.length);
/* 268 */     for (com.fasterxml.jackson.annotation.JsonSubTypes.Type type : types) {
/* 269 */       result.add(new com.fasterxml.jackson.databind.jsontype.NamedType(type.value(), type.name()));
/*     */     }
/* 271 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String findTypeName(AnnotatedClass ac)
/*     */   {
/* 277 */     com.fasterxml.jackson.annotation.JsonTypeName tn = (com.fasterxml.jackson.annotation.JsonTypeName)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonTypeName.class);
/* 278 */     return tn == null ? null : tn.value();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object findSerializer(Annotated a)
/*     */   {
/* 290 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 291 */     if (ann != null) {
/* 292 */       Class<? extends JsonSerializer<?>> serClass = ann.using();
/* 293 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/* 294 */         return serClass;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 302 */     com.fasterxml.jackson.annotation.JsonRawValue annRaw = (com.fasterxml.jackson.annotation.JsonRawValue)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonRawValue.class);
/* 303 */     if ((annRaw != null) && (annRaw.value()))
/*     */     {
/* 305 */       Class<?> cls = a.getRawType();
/* 306 */       return new com.fasterxml.jackson.databind.ser.std.RawSerializer(cls);
/*     */     }
/* 308 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated a)
/*     */   {
/* 314 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 315 */     if (ann != null) {
/* 316 */       Class<? extends JsonSerializer<?>> serClass = ann.keyUsing();
/* 317 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/* 318 */         return serClass;
/*     */       }
/*     */     }
/* 321 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated a)
/*     */   {
/* 327 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 328 */     if (ann != null) {
/* 329 */       Class<? extends JsonSerializer<?>> serClass = ann.contentUsing();
/* 330 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/* 331 */         return serClass;
/*     */       }
/*     */     }
/* 334 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findNullSerializer(Annotated a)
/*     */   {
/* 340 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 341 */     if (ann != null) {
/* 342 */       Class<? extends JsonSerializer<?>> serClass = ann.nullsUsing();
/* 343 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/* 344 */         return serClass;
/*     */       }
/*     */     }
/* 347 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonInclude.Include findSerializationInclusion(Annotated a, JsonInclude.Include defValue)
/*     */   {
/* 353 */     JsonInclude inc = (JsonInclude)_findAnnotation(a, JsonInclude.class);
/* 354 */     if (inc != null) {
/* 355 */       return inc.value();
/*     */     }
/* 357 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 358 */     if (ann != null)
/*     */     {
/* 360 */       com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion i2 = ann.include();
/* 361 */       switch (i2) {
/*     */       case ALWAYS: 
/* 363 */         return JsonInclude.Include.ALWAYS;
/*     */       case NON_NULL: 
/* 365 */         return JsonInclude.Include.NON_NULL;
/*     */       case NON_DEFAULT: 
/* 367 */         return JsonInclude.Include.NON_DEFAULT;
/*     */       case NON_EMPTY: 
/* 369 */         return JsonInclude.Include.NON_EMPTY;
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 374 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonInclude.Include findSerializationInclusionForContent(Annotated a, JsonInclude.Include defValue)
/*     */   {
/* 380 */     JsonInclude inc = (JsonInclude)_findAnnotation(a, JsonInclude.class);
/* 381 */     return inc == null ? defValue : inc.content();
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> findSerializationType(Annotated am)
/*     */   {
/* 387 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(am, JsonSerialize.class);
/* 388 */     return ann == null ? null : _classIfExplicit(ann.as());
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> findSerializationKeyType(Annotated am, JavaType baseType)
/*     */   {
/* 394 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(am, JsonSerialize.class);
/* 395 */     return ann == null ? null : _classIfExplicit(ann.keyAs());
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> findSerializationContentType(Annotated am, JavaType baseType)
/*     */   {
/* 401 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(am, JsonSerialize.class);
/* 402 */     return ann == null ? null : _classIfExplicit(ann.contentAs());
/*     */   }
/*     */   
/*     */ 
/*     */   public com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing findSerializationTyping(Annotated a)
/*     */   {
/* 408 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 409 */     return ann == null ? null : ann.typing();
/*     */   }
/*     */   
/*     */   public Object findSerializationConverter(Annotated a)
/*     */   {
/* 414 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 415 */     return ann == null ? null : _classIfExplicit(ann.converter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*     */   }
/*     */   
/*     */   public Object findSerializationContentConverter(AnnotatedMember a)
/*     */   {
/* 420 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/* 421 */     return ann == null ? null : _classIfExplicit(ann.contentConverter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?>[] findViews(Annotated a)
/*     */   {
/* 427 */     JsonView ann = (JsonView)_findAnnotation(a, JsonView.class);
/* 428 */     return ann == null ? null : ann.value();
/*     */   }
/*     */   
/*     */   public Boolean isTypeId(AnnotatedMember member)
/*     */   {
/* 433 */     return Boolean.valueOf(_hasAnnotation(member, com.fasterxml.jackson.annotation.JsonTypeId.class));
/*     */   }
/*     */   
/*     */   public ObjectIdInfo findObjectIdInfo(Annotated ann)
/*     */   {
/* 438 */     JsonIdentityInfo info = (JsonIdentityInfo)_findAnnotation(ann, JsonIdentityInfo.class);
/* 439 */     if ((info == null) || (info.generator() == com.fasterxml.jackson.annotation.ObjectIdGenerators.None.class)) {
/* 440 */       return null;
/*     */     }
/*     */     
/* 443 */     PropertyName name = new PropertyName(info.property());
/* 444 */     return new ObjectIdInfo(name, info.scope(), info.generator(), info.resolver());
/*     */   }
/*     */   
/*     */   public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo)
/*     */   {
/* 449 */     com.fasterxml.jackson.annotation.JsonIdentityReference ref = (com.fasterxml.jackson.annotation.JsonIdentityReference)_findAnnotation(ann, com.fasterxml.jackson.annotation.JsonIdentityReference.class);
/* 450 */     if (ref != null) {
/* 451 */       objectIdInfo = objectIdInfo.withAlwaysAsId(ref.alwaysAsId());
/*     */     }
/* 453 */     return objectIdInfo;
/*     */   }
/*     */   
/*     */   public com.fasterxml.jackson.annotation.JsonFormat.Value findFormat(Annotated ann)
/*     */   {
/* 458 */     com.fasterxml.jackson.annotation.JsonFormat f = (com.fasterxml.jackson.annotation.JsonFormat)_findAnnotation(ann, com.fasterxml.jackson.annotation.JsonFormat.class);
/* 459 */     return f == null ? null : new com.fasterxml.jackson.annotation.JsonFormat.Value(f);
/*     */   }
/*     */   
/*     */   public String findPropertyDefaultValue(Annotated ann)
/*     */   {
/* 464 */     JsonProperty prop = (JsonProperty)_findAnnotation(ann, JsonProperty.class);
/* 465 */     if (prop == null) {
/* 466 */       return null;
/*     */     }
/* 468 */     String str = prop.defaultValue();
/*     */     
/* 470 */     return str.isEmpty() ? null : str;
/*     */   }
/*     */   
/*     */   public String findPropertyDescription(Annotated ann)
/*     */   {
/* 475 */     com.fasterxml.jackson.annotation.JsonPropertyDescription desc = (com.fasterxml.jackson.annotation.JsonPropertyDescription)_findAnnotation(ann, com.fasterxml.jackson.annotation.JsonPropertyDescription.class);
/* 476 */     return desc == null ? null : desc.value();
/*     */   }
/*     */   
/*     */   public Integer findPropertyIndex(Annotated ann)
/*     */   {
/* 481 */     JsonProperty prop = (JsonProperty)_findAnnotation(ann, JsonProperty.class);
/* 482 */     if (prop != null) {
/* 483 */       int ix = prop.index();
/* 484 */       if (ix != -1) {
/* 485 */         return Integer.valueOf(ix);
/*     */       }
/*     */     }
/* 488 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String findImplicitPropertyName(AnnotatedMember param)
/*     */   {
/* 495 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*     */   {
/* 506 */     JsonPropertyOrder order = (JsonPropertyOrder)_findAnnotation(ac, JsonPropertyOrder.class);
/* 507 */     return order == null ? null : order.value();
/*     */   }
/*     */   
/*     */   public Boolean findSerializationSortAlphabetically(Annotated ann)
/*     */   {
/* 512 */     return _findSortAlpha(ann);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Boolean findSerializationSortAlphabetically(AnnotatedClass ac)
/*     */   {
/* 518 */     return _findSortAlpha(ac);
/*     */   }
/*     */   
/*     */   private final Boolean _findSortAlpha(Annotated ann) {
/* 522 */     JsonPropertyOrder order = (JsonPropertyOrder)_findAnnotation(ann, JsonPropertyOrder.class);
/* 523 */     return order == null ? null : Boolean.valueOf(order.alphabetic());
/*     */   }
/*     */   
/*     */ 
/*     */   public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties)
/*     */   {
/* 529 */     JsonAppend ann = (JsonAppend)_findAnnotation(ac, JsonAppend.class);
/* 530 */     if (ann == null) {
/* 531 */       return;
/*     */     }
/* 533 */     boolean prepend = ann.prepend();
/* 534 */     JavaType propType = null;
/*     */     
/*     */ 
/* 537 */     JsonAppend.Attr[] attrs = ann.attrs();
/* 538 */     int i = 0; for (int len = attrs.length; i < len; i++) {
/* 539 */       if (propType == null) {
/* 540 */         propType = config.constructType(Object.class);
/*     */       }
/* 542 */       BeanPropertyWriter bpw = _constructVirtualProperty(attrs[i], config, ac, propType);
/*     */       
/* 544 */       if (prepend) {
/* 545 */         properties.add(i, bpw);
/*     */       } else {
/* 547 */         properties.add(bpw);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 552 */     JsonAppend.Prop[] props = ann.props();
/* 553 */     int i = 0; for (int len = props.length; i < len; i++) {
/* 554 */       BeanPropertyWriter bpw = _constructVirtualProperty(props[i], config, ac);
/*     */       
/* 556 */       if (prepend) {
/* 557 */         properties.add(i, bpw);
/*     */       } else {
/* 559 */         properties.add(bpw);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanPropertyWriter _constructVirtualProperty(JsonAppend.Attr attr, MapperConfig<?> config, AnnotatedClass ac, JavaType type)
/*     */   {
/* 567 */     PropertyMetadata metadata = attr.required() ? PropertyMetadata.STD_REQUIRED : PropertyMetadata.STD_OPTIONAL;
/*     */     
/*     */ 
/* 570 */     String attrName = attr.value();
/*     */     
/*     */ 
/* 573 */     PropertyName propName = _propertyName(attr.propName(), attr.propNamespace());
/* 574 */     if (!propName.hasSimpleName()) {
/* 575 */       propName = new PropertyName(attrName);
/*     */     }
/*     */     
/* 578 */     AnnotatedMember member = new VirtualAnnotatedMember(ac, ac.getRawType(), attrName, type.getRawClass());
/*     */     
/*     */ 
/* 581 */     com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition propDef = com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition.construct(config, member, propName, metadata, attr.include());
/*     */     
/*     */ 
/* 584 */     return com.fasterxml.jackson.databind.ser.impl.AttributePropertyWriter.construct(attrName, propDef, ac.getAnnotations(), type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter _constructVirtualProperty(JsonAppend.Prop prop, MapperConfig<?> config, AnnotatedClass ac)
/*     */   {
/* 591 */     PropertyMetadata metadata = prop.required() ? PropertyMetadata.STD_REQUIRED : PropertyMetadata.STD_OPTIONAL;
/*     */     
/* 593 */     PropertyName propName = _propertyName(prop.name(), prop.namespace());
/* 594 */     JavaType type = config.constructType(prop.type());
/*     */     
/* 596 */     AnnotatedMember member = new VirtualAnnotatedMember(ac, ac.getRawType(), propName.getSimpleName(), type.getRawClass());
/*     */     
/*     */ 
/* 599 */     com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition propDef = com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition.construct(config, member, propName, metadata, prop.include());
/*     */     
/*     */ 
/* 602 */     Class<?> implClass = prop.value();
/*     */     
/* 604 */     com.fasterxml.jackson.databind.cfg.HandlerInstantiator hi = config.getHandlerInstantiator();
/* 605 */     com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter bpw = hi == null ? null : hi.virtualPropertyWriterInstance(config, implClass);
/*     */     
/* 607 */     if (bpw == null) {
/* 608 */       bpw = (com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter)com.fasterxml.jackson.databind.util.ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 613 */     return bpw.withConfig(config, ac, propDef, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName findNameForSerialization(Annotated a)
/*     */   {
/* 625 */     String name = null;
/*     */     
/* 627 */     com.fasterxml.jackson.annotation.JsonGetter jg = (com.fasterxml.jackson.annotation.JsonGetter)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonGetter.class);
/* 628 */     if (jg != null) {
/* 629 */       name = jg.value();
/*     */     } else {
/* 631 */       JsonProperty pann = (JsonProperty)_findAnnotation(a, JsonProperty.class);
/* 632 */       if (pann != null) {
/* 633 */         name = pann.value();
/* 634 */       } else if ((_hasAnnotation(a, JsonSerialize.class)) || (_hasAnnotation(a, JsonView.class))) {
/* 635 */         name = "";
/*     */       } else {
/* 637 */         return null;
/*     */       }
/*     */     }
/* 640 */     if (name.length() == 0) {
/* 641 */       return PropertyName.USE_DEFAULT;
/*     */     }
/* 643 */     return new PropertyName(name);
/*     */   }
/*     */   
/*     */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*     */   {
/* 648 */     com.fasterxml.jackson.annotation.JsonValue ann = (com.fasterxml.jackson.annotation.JsonValue)_findAnnotation(am, com.fasterxml.jackson.annotation.JsonValue.class);
/*     */     
/* 650 */     return (ann != null) && (ann.value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<? extends com.fasterxml.jackson.databind.JsonDeserializer<?>> findDeserializer(Annotated a)
/*     */   {
/* 662 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/* 663 */     if (ann != null) {
/* 664 */       Class<? extends com.fasterxml.jackson.databind.JsonDeserializer<?>> deserClass = ann.using();
/* 665 */       if (deserClass != com.fasterxml.jackson.databind.JsonDeserializer.None.class) {
/* 666 */         return deserClass;
/*     */       }
/*     */     }
/* 669 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<? extends com.fasterxml.jackson.databind.KeyDeserializer> findKeyDeserializer(Annotated a)
/*     */   {
/* 675 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/* 676 */     if (ann != null) {
/* 677 */       Class<? extends com.fasterxml.jackson.databind.KeyDeserializer> deserClass = ann.keyUsing();
/* 678 */       if (deserClass != com.fasterxml.jackson.databind.KeyDeserializer.None.class) {
/* 679 */         return deserClass;
/*     */       }
/*     */     }
/* 682 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<? extends com.fasterxml.jackson.databind.JsonDeserializer<?>> findContentDeserializer(Annotated a)
/*     */   {
/* 688 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/* 689 */     if (ann != null) {
/* 690 */       Class<? extends com.fasterxml.jackson.databind.JsonDeserializer<?>> deserClass = ann.contentUsing();
/* 691 */       if (deserClass != com.fasterxml.jackson.databind.JsonDeserializer.None.class) {
/* 692 */         return deserClass;
/*     */       }
/*     */     }
/* 695 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> findDeserializationType(Annotated am, JavaType baseType)
/*     */   {
/* 700 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(am, JsonDeserialize.class);
/* 701 */     return ann == null ? null : _classIfExplicit(ann.as());
/*     */   }
/*     */   
/*     */   public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType)
/*     */   {
/* 706 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(am, JsonDeserialize.class);
/* 707 */     return ann == null ? null : _classIfExplicit(ann.keyAs());
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType)
/*     */   {
/* 713 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(am, JsonDeserialize.class);
/* 714 */     return ann == null ? null : _classIfExplicit(ann.contentAs());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findDeserializationConverter(Annotated a)
/*     */   {
/* 720 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/* 721 */     return ann == null ? null : _classIfExplicit(ann.converter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findDeserializationContentConverter(AnnotatedMember a)
/*     */   {
/* 727 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/* 728 */     return ann == null ? null : _classIfExplicit(ann.contentConverter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object findValueInstantiator(AnnotatedClass ac)
/*     */   {
/* 740 */     com.fasterxml.jackson.databind.annotation.JsonValueInstantiator ann = (com.fasterxml.jackson.databind.annotation.JsonValueInstantiator)_findAnnotation(ac, com.fasterxml.jackson.databind.annotation.JsonValueInstantiator.class);
/*     */     
/* 742 */     return ann == null ? null : ann.value();
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> findPOJOBuilder(AnnotatedClass ac)
/*     */   {
/* 748 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(ac, JsonDeserialize.class);
/* 749 */     return ann == null ? null : _classIfExplicit(ann.builder());
/*     */   }
/*     */   
/*     */ 
/*     */   public com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac)
/*     */   {
/* 755 */     com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder ann = (com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder)_findAnnotation(ac, com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.class);
/* 756 */     return ann == null ? null : new com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value(ann);
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
/*     */   public PropertyName findNameForDeserialization(Annotated a)
/*     */   {
/* 772 */     com.fasterxml.jackson.annotation.JsonSetter js = (com.fasterxml.jackson.annotation.JsonSetter)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonSetter.class);
/* 773 */     String name; if (js != null) {
/* 774 */       name = js.value();
/*     */     } else {
/* 776 */       JsonProperty pann = (JsonProperty)_findAnnotation(a, JsonProperty.class);
/* 777 */       String name; if (pann != null) {
/* 778 */         name = pann.value();
/*     */       }
/*     */       else
/*     */       {
/*     */         String name;
/* 783 */         if ((_hasAnnotation(a, JsonDeserialize.class)) || (_hasAnnotation(a, JsonView.class)) || (_hasAnnotation(a, JsonUnwrapped.class)) || (_hasAnnotation(a, JsonBackReference.class)) || (_hasAnnotation(a, JsonManagedReference.class)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 788 */           name = "";
/*     */         } else
/* 790 */           return null;
/*     */       } }
/*     */     String name;
/* 793 */     if (name.length() == 0) {
/* 794 */       return PropertyName.USE_DEFAULT;
/*     */     }
/* 796 */     return new PropertyName(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 806 */     return _hasAnnotation(am, com.fasterxml.jackson.annotation.JsonAnySetter.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 815 */     return _hasAnnotation(am, com.fasterxml.jackson.annotation.JsonAnyGetter.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasCreatorAnnotation(Annotated a)
/*     */   {
/* 825 */     JsonCreator ann = (JsonCreator)_findAnnotation(a, JsonCreator.class);
/* 826 */     return (ann != null) && (ann.mode() != com.fasterxml.jackson.annotation.JsonCreator.Mode.DISABLED);
/*     */   }
/*     */   
/*     */   public com.fasterxml.jackson.annotation.JsonCreator.Mode findCreatorBinding(Annotated a)
/*     */   {
/* 831 */     JsonCreator ann = (JsonCreator)_findAnnotation(a, JsonCreator.class);
/* 832 */     return ann == null ? null : ann.mode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _isIgnorable(Annotated a)
/*     */   {
/* 843 */     com.fasterxml.jackson.annotation.JsonIgnore ann = (com.fasterxml.jackson.annotation.JsonIgnore)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonIgnore.class);
/* 844 */     return (ann != null) && (ann.value());
/*     */   }
/*     */   
/*     */   protected Class<?> _classIfExplicit(Class<?> cls) {
/* 848 */     if ((cls == null) || (com.fasterxml.jackson.databind.util.ClassUtil.isBogusClass(cls))) {
/* 849 */       return null;
/*     */     }
/* 851 */     return cls;
/*     */   }
/*     */   
/*     */   protected Class<?> _classIfExplicit(Class<?> cls, Class<?> implicit) {
/* 855 */     cls = _classIfExplicit(cls);
/* 856 */     return (cls == null) || (cls == implicit) ? null : cls;
/*     */   }
/*     */   
/*     */   protected PropertyName _propertyName(String localName, String namespace) {
/* 860 */     if (localName.isEmpty()) {
/* 861 */       return PropertyName.USE_DEFAULT;
/*     */     }
/* 863 */     if ((namespace == null) || (namespace.isEmpty())) {
/* 864 */       return new PropertyName(localName);
/*     */     }
/* 866 */     return new PropertyName(localName, namespace);
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
/*     */   protected TypeResolverBuilder<?> _findTypeResolver(MapperConfig<?> config, Annotated ann, JavaType baseType)
/*     */   {
/* 879 */     JsonTypeInfo info = (JsonTypeInfo)_findAnnotation(ann, JsonTypeInfo.class);
/* 880 */     com.fasterxml.jackson.databind.annotation.JsonTypeResolver resAnn = (com.fasterxml.jackson.databind.annotation.JsonTypeResolver)_findAnnotation(ann, com.fasterxml.jackson.databind.annotation.JsonTypeResolver.class);
/*     */     TypeResolverBuilder<?> b;
/* 882 */     if (resAnn != null) {
/* 883 */       if (info == null) {
/* 884 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 890 */       b = config.typeResolverBuilderInstance(ann, resAnn.value());
/*     */     } else {
/* 892 */       if (info == null) {
/* 893 */         return null;
/*     */       }
/*     */       
/* 896 */       if (info.use() == com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE) {
/* 897 */         return _constructNoTypeResolverBuilder();
/*     */       }
/* 899 */       b = _constructStdTypeResolverBuilder();
/*     */     }
/*     */     
/* 902 */     com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver idResInfo = (com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver)_findAnnotation(ann, com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver.class);
/* 903 */     com.fasterxml.jackson.databind.jsontype.TypeIdResolver idRes = idResInfo == null ? null : config.typeIdResolverInstance(ann, idResInfo.value());
/*     */     
/* 905 */     if (idRes != null) {
/* 906 */       idRes.init(baseType);
/*     */     }
/* 908 */     TypeResolverBuilder<?> b = b.init(info.use(), idRes);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 913 */     com.fasterxml.jackson.annotation.JsonTypeInfo.As inclusion = info.include();
/* 914 */     if ((inclusion == com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY) && ((ann instanceof AnnotatedClass))) {
/* 915 */       inclusion = com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
/*     */     }
/* 917 */     b = b.inclusion(inclusion);
/* 918 */     b = b.typeProperty(info.property());
/* 919 */     Class<?> defaultImpl = info.defaultImpl();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 925 */     if ((defaultImpl != com.fasterxml.jackson.annotation.JsonTypeInfo.None.class) && (!defaultImpl.isAnnotation())) {
/* 926 */       b = b.defaultImpl(defaultImpl);
/*     */     }
/* 928 */     b = b.typeIdVisibility(info.visible());
/* 929 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder _constructStdTypeResolverBuilder()
/*     */   {
/* 937 */     return new com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder _constructNoTypeResolverBuilder()
/*     */   {
/* 945 */     return com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder.noTypeInfoBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\JacksonAnnotationIntrospector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */