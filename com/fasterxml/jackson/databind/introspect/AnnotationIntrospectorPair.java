/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator.Mode;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer.None;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class AnnotationIntrospectorPair
/*     */   extends AnnotationIntrospector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotationIntrospector _primary;
/*     */   protected final AnnotationIntrospector _secondary;
/*     */   
/*     */   public AnnotationIntrospectorPair(AnnotationIntrospector p, AnnotationIntrospector s)
/*     */   {
/*  48 */     this._primary = p;
/*  49 */     this._secondary = s;
/*     */   }
/*     */   
/*     */   public Version version()
/*     */   {
/*  54 */     return this._primary.version();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AnnotationIntrospector create(AnnotationIntrospector primary, AnnotationIntrospector secondary)
/*     */   {
/*  65 */     if (primary == null) {
/*  66 */       return secondary;
/*     */     }
/*  68 */     if (secondary == null) {
/*  69 */       return primary;
/*     */     }
/*  71 */     return new AnnotationIntrospectorPair(primary, secondary);
/*     */   }
/*     */   
/*     */   public Collection<AnnotationIntrospector> allIntrospectors()
/*     */   {
/*  76 */     return allIntrospectors(new ArrayList());
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result)
/*     */   {
/*  82 */     this._primary.allIntrospectors(result);
/*  83 */     this._secondary.allIntrospectors(result);
/*  84 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAnnotationBundle(Annotation ann)
/*     */   {
/*  91 */     return (this._primary.isAnnotationBundle(ann)) || (this._secondary.isAnnotationBundle(ann));
/*     */   }
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
/* 103 */     PropertyName name1 = this._primary.findRootName(ac);
/* 104 */     if (name1 == null) {
/* 105 */       return this._secondary.findRootName(ac);
/*     */     }
/* 107 */     if (name1.hasSimpleName()) {
/* 108 */       return name1;
/*     */     }
/*     */     
/* 111 */     PropertyName name2 = this._secondary.findRootName(ac);
/* 112 */     return name2 == null ? name1 : name2;
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] findPropertiesToIgnore(Annotated ac)
/*     */   {
/* 118 */     String[] result = this._primary.findPropertiesToIgnore(ac);
/* 119 */     if (result == null) {
/* 120 */       result = this._secondary.findPropertiesToIgnore(ac);
/*     */     }
/* 122 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*     */   {
/* 128 */     Boolean result = this._primary.findIgnoreUnknownProperties(ac);
/* 129 */     if (result == null) {
/* 130 */       result = this._secondary.findIgnoreUnknownProperties(ac);
/*     */     }
/* 132 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean isIgnorableType(AnnotatedClass ac)
/*     */   {
/* 138 */     Boolean result = this._primary.isIgnorableType(ac);
/* 139 */     if (result == null) {
/* 140 */       result = this._secondary.isIgnorableType(ac);
/*     */     }
/* 142 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public Object findFilterId(AnnotatedClass ac)
/*     */   {
/* 149 */     Object id = this._primary.findFilterId(ac);
/* 150 */     if (id == null) {
/* 151 */       id = this._secondary.findFilterId(ac);
/*     */     }
/* 153 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findFilterId(Annotated ann)
/*     */   {
/* 159 */     Object id = this._primary.findFilterId(ann);
/* 160 */     if (id == null) {
/* 161 */       id = this._secondary.findFilterId(ann);
/*     */     }
/* 163 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findNamingStrategy(AnnotatedClass ac)
/*     */   {
/* 169 */     Object str = this._primary.findNamingStrategy(ac);
/* 170 */     if (str == null) {
/* 171 */       str = this._secondary.findNamingStrategy(ac);
/*     */     }
/* 173 */     return str;
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
/*     */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*     */   {
/* 189 */     checker = this._secondary.findAutoDetectVisibility(ac, checker);
/* 190 */     return this._primary.findAutoDetectVisibility(ac, checker);
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
/* 203 */     TypeResolverBuilder<?> b = this._primary.findTypeResolver(config, ac, baseType);
/* 204 */     if (b == null) {
/* 205 */       b = this._secondary.findTypeResolver(config, ac, baseType);
/*     */     }
/* 207 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*     */   {
/* 214 */     TypeResolverBuilder<?> b = this._primary.findPropertyTypeResolver(config, am, baseType);
/* 215 */     if (b == null) {
/* 216 */       b = this._secondary.findPropertyTypeResolver(config, am, baseType);
/*     */     }
/* 218 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*     */   {
/* 225 */     TypeResolverBuilder<?> b = this._primary.findPropertyContentTypeResolver(config, am, baseType);
/* 226 */     if (b == null) {
/* 227 */       b = this._secondary.findPropertyContentTypeResolver(config, am, baseType);
/*     */     }
/* 229 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<NamedType> findSubtypes(Annotated a)
/*     */   {
/* 235 */     List<NamedType> types1 = this._primary.findSubtypes(a);
/* 236 */     List<NamedType> types2 = this._secondary.findSubtypes(a);
/* 237 */     if ((types1 == null) || (types1.isEmpty())) return types2;
/* 238 */     if ((types2 == null) || (types2.isEmpty())) return types1;
/* 239 */     ArrayList<NamedType> result = new ArrayList(types1.size() + types2.size());
/* 240 */     result.addAll(types1);
/* 241 */     result.addAll(types2);
/* 242 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String findTypeName(AnnotatedClass ac)
/*     */   {
/* 248 */     String name = this._primary.findTypeName(ac);
/* 249 */     if ((name == null) || (name.length() == 0)) {
/* 250 */       name = this._secondary.findTypeName(ac);
/*     */     }
/* 252 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member)
/*     */   {
/* 259 */     AnnotationIntrospector.ReferenceProperty r = this._primary.findReferenceType(member);
/* 260 */     return r == null ? this._secondary.findReferenceType(member) : r;
/*     */   }
/*     */   
/*     */   public NameTransformer findUnwrappingNameTransformer(AnnotatedMember member)
/*     */   {
/* 265 */     NameTransformer r = this._primary.findUnwrappingNameTransformer(member);
/* 266 */     return r == null ? this._secondary.findUnwrappingNameTransformer(member) : r;
/*     */   }
/*     */   
/*     */   public Object findInjectableValueId(AnnotatedMember m)
/*     */   {
/* 271 */     Object r = this._primary.findInjectableValueId(m);
/* 272 */     return r == null ? this._secondary.findInjectableValueId(m) : r;
/*     */   }
/*     */   
/*     */   public boolean hasIgnoreMarker(AnnotatedMember m)
/*     */   {
/* 277 */     return (this._primary.hasIgnoreMarker(m)) || (this._secondary.hasIgnoreMarker(m));
/*     */   }
/*     */   
/*     */   public Boolean hasRequiredMarker(AnnotatedMember m)
/*     */   {
/* 282 */     Boolean r = this._primary.hasRequiredMarker(m);
/* 283 */     return r == null ? this._secondary.hasRequiredMarker(m) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object findSerializer(Annotated am)
/*     */   {
/* 290 */     Object r = this._primary.findSerializer(am);
/* 291 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findSerializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findKeySerializer(Annotated a)
/*     */   {
/* 297 */     Object r = this._primary.findKeySerializer(a);
/* 298 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findKeySerializer(a);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findContentSerializer(Annotated a)
/*     */   {
/* 304 */     Object r = this._primary.findContentSerializer(a);
/* 305 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findContentSerializer(a);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findNullSerializer(Annotated a)
/*     */   {
/* 311 */     Object r = this._primary.findNullSerializer(a);
/* 312 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findNullSerializer(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonInclude.Include findSerializationInclusion(Annotated a, JsonInclude.Include defValue)
/*     */   {
/* 321 */     defValue = this._secondary.findSerializationInclusion(a, defValue);
/* 322 */     defValue = this._primary.findSerializationInclusion(a, defValue);
/* 323 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonInclude.Include findSerializationInclusionForContent(Annotated a, JsonInclude.Include defValue)
/*     */   {
/* 330 */     defValue = this._secondary.findSerializationInclusion(a, defValue);
/* 331 */     defValue = this._primary.findSerializationInclusion(a, defValue);
/* 332 */     return defValue;
/*     */   }
/*     */   
/*     */   public Class<?> findSerializationType(Annotated a)
/*     */   {
/* 337 */     Class<?> r = this._primary.findSerializationType(a);
/* 338 */     return r == null ? this._secondary.findSerializationType(a) : r;
/*     */   }
/*     */   
/*     */   public Class<?> findSerializationKeyType(Annotated am, JavaType baseType)
/*     */   {
/* 343 */     Class<?> r = this._primary.findSerializationKeyType(am, baseType);
/* 344 */     return r == null ? this._secondary.findSerializationKeyType(am, baseType) : r;
/*     */   }
/*     */   
/*     */   public Class<?> findSerializationContentType(Annotated am, JavaType baseType)
/*     */   {
/* 349 */     Class<?> r = this._primary.findSerializationContentType(am, baseType);
/* 350 */     return r == null ? this._secondary.findSerializationContentType(am, baseType) : r;
/*     */   }
/*     */   
/*     */   public JsonSerialize.Typing findSerializationTyping(Annotated a)
/*     */   {
/* 355 */     JsonSerialize.Typing r = this._primary.findSerializationTyping(a);
/* 356 */     return r == null ? this._secondary.findSerializationTyping(a) : r;
/*     */   }
/*     */   
/*     */   public Object findSerializationConverter(Annotated a)
/*     */   {
/* 361 */     Object r = this._primary.findSerializationConverter(a);
/* 362 */     return r == null ? this._secondary.findSerializationConverter(a) : r;
/*     */   }
/*     */   
/*     */   public Object findSerializationContentConverter(AnnotatedMember a)
/*     */   {
/* 367 */     Object r = this._primary.findSerializationContentConverter(a);
/* 368 */     return r == null ? this._secondary.findSerializationContentConverter(a) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?>[] findViews(Annotated a)
/*     */   {
/* 377 */     Class<?>[] result = this._primary.findViews(a);
/* 378 */     if (result == null) {
/* 379 */       result = this._secondary.findViews(a);
/*     */     }
/* 381 */     return result;
/*     */   }
/*     */   
/*     */   public Boolean isTypeId(AnnotatedMember member)
/*     */   {
/* 386 */     Boolean b = this._primary.isTypeId(member);
/* 387 */     return b == null ? this._secondary.isTypeId(member) : b;
/*     */   }
/*     */   
/*     */   public ObjectIdInfo findObjectIdInfo(Annotated ann)
/*     */   {
/* 392 */     ObjectIdInfo r = this._primary.findObjectIdInfo(ann);
/* 393 */     return r == null ? this._secondary.findObjectIdInfo(ann) : r;
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo)
/*     */   {
/* 399 */     objectIdInfo = this._secondary.findObjectReferenceInfo(ann, objectIdInfo);
/* 400 */     objectIdInfo = this._primary.findObjectReferenceInfo(ann, objectIdInfo);
/* 401 */     return objectIdInfo;
/*     */   }
/*     */   
/*     */   public JsonFormat.Value findFormat(Annotated ann)
/*     */   {
/* 406 */     JsonFormat.Value r = this._primary.findFormat(ann);
/* 407 */     return r == null ? this._secondary.findFormat(ann) : r;
/*     */   }
/*     */   
/*     */   public PropertyName findWrapperName(Annotated ann)
/*     */   {
/* 412 */     PropertyName name = this._primary.findWrapperName(ann);
/* 413 */     if (name == null) {
/* 414 */       name = this._secondary.findWrapperName(ann);
/* 415 */     } else if (name == PropertyName.USE_DEFAULT)
/*     */     {
/* 417 */       PropertyName name2 = this._secondary.findWrapperName(ann);
/* 418 */       if (name2 != null) {
/* 419 */         name = name2;
/*     */       }
/*     */     }
/* 422 */     return name;
/*     */   }
/*     */   
/*     */   public String findPropertyDefaultValue(Annotated ann)
/*     */   {
/* 427 */     String str = this._primary.findPropertyDefaultValue(ann);
/* 428 */     return (str == null) || (str.isEmpty()) ? this._secondary.findPropertyDefaultValue(ann) : str;
/*     */   }
/*     */   
/*     */   public String findPropertyDescription(Annotated ann)
/*     */   {
/* 433 */     String r = this._primary.findPropertyDescription(ann);
/* 434 */     return r == null ? this._secondary.findPropertyDescription(ann) : r;
/*     */   }
/*     */   
/*     */   public Integer findPropertyIndex(Annotated ann)
/*     */   {
/* 439 */     Integer r = this._primary.findPropertyIndex(ann);
/* 440 */     return r == null ? this._secondary.findPropertyIndex(ann) : r;
/*     */   }
/*     */   
/*     */   public String findImplicitPropertyName(AnnotatedMember param)
/*     */   {
/* 445 */     String r = this._primary.findImplicitPropertyName(param);
/* 446 */     return r == null ? this._secondary.findImplicitPropertyName(param) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*     */   {
/* 453 */     String[] r = this._primary.findSerializationPropertyOrder(ac);
/* 454 */     return r == null ? this._secondary.findSerializationPropertyOrder(ac) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Boolean findSerializationSortAlphabetically(AnnotatedClass ac)
/*     */   {
/* 465 */     Boolean r = this._primary.findSerializationSortAlphabetically(ac);
/* 466 */     return r == null ? this._secondary.findSerializationSortAlphabetically(ac) : r;
/*     */   }
/*     */   
/*     */   public Boolean findSerializationSortAlphabetically(Annotated ann)
/*     */   {
/* 471 */     Boolean r = this._primary.findSerializationSortAlphabetically(ann);
/* 472 */     return r == null ? this._secondary.findSerializationSortAlphabetically(ann) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties)
/*     */   {
/* 479 */     this._primary.findAndAddVirtualProperties(config, ac, properties);
/* 480 */     this._secondary.findAndAddVirtualProperties(config, ac, properties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PropertyName findNameForSerialization(Annotated a)
/*     */   {
/* 487 */     PropertyName n = this._primary.findNameForSerialization(a);
/*     */     
/* 489 */     if (n == null) {
/* 490 */       n = this._secondary.findNameForSerialization(a);
/* 491 */     } else if (n == PropertyName.USE_DEFAULT) {
/* 492 */       PropertyName n2 = this._secondary.findNameForSerialization(a);
/* 493 */       if (n2 != null) {
/* 494 */         n = n2;
/*     */       }
/*     */     }
/* 497 */     return n;
/*     */   }
/*     */   
/*     */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*     */   {
/* 502 */     return (this._primary.hasAsValueAnnotation(am)) || (this._secondary.hasAsValueAnnotation(am));
/*     */   }
/*     */   
/*     */   public String findEnumValue(Enum<?> value)
/*     */   {
/* 507 */     String r = this._primary.findEnumValue(value);
/* 508 */     return r == null ? this._secondary.findEnumValue(value) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object findDeserializer(Annotated am)
/*     */   {
/* 515 */     Object r = this._primary.findDeserializer(am);
/* 516 */     return _isExplicitClassOrOb(r, JsonDeserializer.None.class) ? r : this._secondary.findDeserializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findKeyDeserializer(Annotated am)
/*     */   {
/* 522 */     Object r = this._primary.findKeyDeserializer(am);
/* 523 */     return _isExplicitClassOrOb(r, KeyDeserializer.None.class) ? r : this._secondary.findKeyDeserializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findContentDeserializer(Annotated am)
/*     */   {
/* 529 */     Object r = this._primary.findContentDeserializer(am);
/* 530 */     return _isExplicitClassOrOb(r, JsonDeserializer.None.class) ? r : this._secondary.findContentDeserializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> findDeserializationType(Annotated am, JavaType baseType)
/*     */   {
/* 536 */     Class<?> r = this._primary.findDeserializationType(am, baseType);
/* 537 */     return r != null ? r : this._secondary.findDeserializationType(am, baseType);
/*     */   }
/*     */   
/*     */   public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType)
/*     */   {
/* 542 */     Class<?> result = this._primary.findDeserializationKeyType(am, baseKeyType);
/* 543 */     return result == null ? this._secondary.findDeserializationKeyType(am, baseKeyType) : result;
/*     */   }
/*     */   
/*     */   public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType)
/*     */   {
/* 548 */     Class<?> result = this._primary.findDeserializationContentType(am, baseContentType);
/* 549 */     return result == null ? this._secondary.findDeserializationContentType(am, baseContentType) : result;
/*     */   }
/*     */   
/*     */   public Object findDeserializationConverter(Annotated a)
/*     */   {
/* 554 */     Object ob = this._primary.findDeserializationConverter(a);
/* 555 */     return ob == null ? this._secondary.findDeserializationConverter(a) : ob;
/*     */   }
/*     */   
/*     */   public Object findDeserializationContentConverter(AnnotatedMember a)
/*     */   {
/* 560 */     Object ob = this._primary.findDeserializationContentConverter(a);
/* 561 */     return ob == null ? this._secondary.findDeserializationContentConverter(a) : ob;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object findValueInstantiator(AnnotatedClass ac)
/*     */   {
/* 568 */     Object result = this._primary.findValueInstantiator(ac);
/* 569 */     return result == null ? this._secondary.findValueInstantiator(ac) : result;
/*     */   }
/*     */   
/*     */   public Class<?> findPOJOBuilder(AnnotatedClass ac)
/*     */   {
/* 574 */     Class<?> result = this._primary.findPOJOBuilder(ac);
/* 575 */     return result == null ? this._secondary.findPOJOBuilder(ac) : result;
/*     */   }
/*     */   
/*     */   public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac)
/*     */   {
/* 580 */     JsonPOJOBuilder.Value result = this._primary.findPOJOBuilderConfig(ac);
/* 581 */     return result == null ? this._secondary.findPOJOBuilderConfig(ac) : result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName findNameForDeserialization(Annotated a)
/*     */   {
/* 590 */     PropertyName n = this._primary.findNameForDeserialization(a);
/* 591 */     if (n == null) {
/* 592 */       n = this._secondary.findNameForDeserialization(a);
/* 593 */     } else if (n == PropertyName.USE_DEFAULT) {
/* 594 */       PropertyName n2 = this._secondary.findNameForDeserialization(a);
/* 595 */       if (n2 != null) {
/* 596 */         n = n2;
/*     */       }
/*     */     }
/* 599 */     return n;
/*     */   }
/*     */   
/*     */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 604 */     return (this._primary.hasAnySetterAnnotation(am)) || (this._secondary.hasAnySetterAnnotation(am));
/*     */   }
/*     */   
/*     */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 609 */     return (this._primary.hasAnyGetterAnnotation(am)) || (this._secondary.hasAnyGetterAnnotation(am));
/*     */   }
/*     */   
/*     */   public boolean hasCreatorAnnotation(Annotated a)
/*     */   {
/* 614 */     return (this._primary.hasCreatorAnnotation(a)) || (this._secondary.hasCreatorAnnotation(a));
/*     */   }
/*     */   
/*     */   public JsonCreator.Mode findCreatorBinding(Annotated a)
/*     */   {
/* 619 */     JsonCreator.Mode mode = this._primary.findCreatorBinding(a);
/* 620 */     if (mode != null) {
/* 621 */       return mode;
/*     */     }
/* 623 */     return this._secondary.findCreatorBinding(a);
/*     */   }
/*     */   
/*     */   protected boolean _isExplicitClassOrOb(Object maybeCls, Class<?> implicit) {
/* 627 */     if (maybeCls == null) {
/* 628 */       return false;
/*     */     }
/* 630 */     if (!(maybeCls instanceof Class)) {
/* 631 */       return true;
/*     */     }
/* 633 */     Class<?> cls = (Class)maybeCls;
/* 634 */     return (cls != implicit) && (!ClassUtil.isBogusClass(cls));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotationIntrospectorPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */