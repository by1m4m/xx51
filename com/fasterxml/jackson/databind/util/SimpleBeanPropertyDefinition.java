/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ public class SimpleBeanPropertyDefinition
/*     */   extends BeanPropertyDefinition
/*     */ {
/*     */   protected final AnnotationIntrospector _introspector;
/*     */   protected final AnnotatedMember _member;
/*     */   protected final PropertyMetadata _metadata;
/*     */   protected final PropertyName _fullName;
/*     */   protected final JsonInclude.Include _inclusion;
/*     */   @Deprecated
/*     */   protected final String _name;
/*     */   
/*     */   @Deprecated
/*     */   public SimpleBeanPropertyDefinition(AnnotatedMember member)
/*     */   {
/*  65 */     this(member, member.getName(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public SimpleBeanPropertyDefinition(AnnotatedMember member, String name)
/*     */   {
/*  73 */     this(member, new PropertyName(name), null, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SimpleBeanPropertyDefinition(AnnotatedMember member, PropertyName fullName, AnnotationIntrospector intr, PropertyMetadata metadata, JsonInclude.Include inclusion)
/*     */   {
/*  80 */     this._introspector = intr;
/*  81 */     this._member = member;
/*  82 */     this._fullName = fullName;
/*  83 */     this._name = fullName.getSimpleName();
/*  84 */     this._metadata = (metadata == null ? PropertyMetadata.STD_OPTIONAL : metadata);
/*  85 */     this._inclusion = inclusion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected SimpleBeanPropertyDefinition(AnnotatedMember member, String name, AnnotationIntrospector intr)
/*     */   {
/*  94 */     this(member, new PropertyName(name), intr, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member)
/*     */   {
/* 102 */     return new SimpleBeanPropertyDefinition(member, new PropertyName(member.getName()), config == null ? null : config.getAnnotationIntrospector(), null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, String name)
/*     */   {
/* 113 */     return new SimpleBeanPropertyDefinition(member, new PropertyName(name), config == null ? null : config.getAnnotationIntrospector(), null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name)
/*     */   {
/* 123 */     return construct(config, member, name, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name, PropertyMetadata metadata, JsonInclude.Include inclusion)
/*     */   {
/* 132 */     return new SimpleBeanPropertyDefinition(member, name, config == null ? null : config.getAnnotationIntrospector(), metadata, inclusion);
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
/*     */   @Deprecated
/*     */   public BeanPropertyDefinition withName(String newName)
/*     */   {
/* 146 */     return withSimpleName(newName);
/*     */   }
/*     */   
/*     */   public BeanPropertyDefinition withSimpleName(String newName)
/*     */   {
/* 151 */     if ((this._fullName.hasSimpleName(newName)) && (!this._fullName.hasNamespace())) {
/* 152 */       return this;
/*     */     }
/* 154 */     return new SimpleBeanPropertyDefinition(this._member, new PropertyName(newName), this._introspector, this._metadata, this._inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanPropertyDefinition withName(PropertyName newName)
/*     */   {
/* 160 */     if (this._fullName.equals(newName)) {
/* 161 */       return this;
/*     */     }
/* 163 */     return new SimpleBeanPropertyDefinition(this._member, newName, this._introspector, this._metadata, this._inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyDefinition withMetadata(PropertyMetadata metadata)
/*     */   {
/* 171 */     if (metadata.equals(this._metadata)) {
/* 172 */       return this;
/*     */     }
/* 174 */     return new SimpleBeanPropertyDefinition(this._member, this._fullName, this._introspector, metadata, this._inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyDefinition withInclusion(JsonInclude.Include inclusion)
/*     */   {
/* 182 */     if (this._inclusion == inclusion) {
/* 183 */       return this;
/*     */     }
/* 185 */     return new SimpleBeanPropertyDefinition(this._member, this._fullName, this._introspector, this._metadata, inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 196 */     return this._fullName.getSimpleName();
/*     */   }
/*     */   
/* 199 */   public PropertyName getFullName() { return this._fullName; }
/*     */   
/*     */   public String getInternalName() {
/* 202 */     return getName();
/*     */   }
/*     */   
/*     */   public PropertyName getWrapperName() {
/* 206 */     return (this._introspector == null) && (this._member != null) ? null : this._introspector.findWrapperName(this._member);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 212 */   public boolean isExplicitlyIncluded() { return false; }
/* 213 */   public boolean isExplicitlyNamed() { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyMetadata getMetadata()
/*     */   {
/* 221 */     return this._metadata;
/*     */   }
/*     */   
/*     */   public JsonInclude.Include findInclusion()
/*     */   {
/* 226 */     return this._inclusion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasGetter()
/*     */   {
/* 236 */     return getGetter() != null;
/*     */   }
/*     */   
/* 239 */   public boolean hasSetter() { return getSetter() != null; }
/*     */   
/*     */   public boolean hasField() {
/* 242 */     return this._member instanceof AnnotatedField;
/*     */   }
/*     */   
/* 245 */   public boolean hasConstructorParameter() { return this._member instanceof AnnotatedParameter; }
/*     */   
/*     */   public AnnotatedMethod getGetter()
/*     */   {
/* 249 */     if (((this._member instanceof AnnotatedMethod)) && (((AnnotatedMethod)this._member).getParameterCount() == 0))
/*     */     {
/* 251 */       return (AnnotatedMethod)this._member;
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod getSetter()
/*     */   {
/* 258 */     if (((this._member instanceof AnnotatedMethod)) && (((AnnotatedMethod)this._member).getParameterCount() == 1))
/*     */     {
/* 260 */       return (AnnotatedMethod)this._member;
/*     */     }
/* 262 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedField getField()
/*     */   {
/* 267 */     return (this._member instanceof AnnotatedField) ? (AnnotatedField)this._member : null;
/*     */   }
/*     */   
/*     */   public AnnotatedParameter getConstructorParameter()
/*     */   {
/* 272 */     return (this._member instanceof AnnotatedParameter) ? (AnnotatedParameter)this._member : null;
/*     */   }
/*     */   
/*     */   public Iterator<AnnotatedParameter> getConstructorParameters()
/*     */   {
/* 277 */     AnnotatedParameter param = getConstructorParameter();
/* 278 */     if (param == null) {
/* 279 */       return EmptyIterator.instance();
/*     */     }
/* 281 */     return Collections.singleton(param).iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMember getAccessor()
/*     */   {
/* 291 */     AnnotatedMember acc = getGetter();
/* 292 */     if (acc == null) {
/* 293 */       acc = getField();
/*     */     }
/* 295 */     return acc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMember getMutator()
/*     */   {
/* 305 */     AnnotatedMember acc = getConstructorParameter();
/* 306 */     if (acc == null) {
/* 307 */       acc = getSetter();
/* 308 */       if (acc == null) {
/* 309 */         acc = getField();
/*     */       }
/*     */     }
/* 312 */     return acc;
/*     */   }
/*     */   
/*     */   public AnnotatedMember getNonConstructorMutator()
/*     */   {
/* 317 */     AnnotatedMember acc = getSetter();
/* 318 */     if (acc == null) {
/* 319 */       acc = getField();
/*     */     }
/* 321 */     return acc;
/*     */   }
/*     */   
/*     */   public AnnotatedMember getPrimaryMember() {
/* 325 */     return this._member;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\SimpleBeanPropertyDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */