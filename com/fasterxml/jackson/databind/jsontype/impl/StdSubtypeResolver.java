/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StdSubtypeResolver
/*     */   extends SubtypeResolver
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected LinkedHashSet<NamedType> _registeredSubtypes;
/*     */   
/*     */   public void registerSubtypes(NamedType... types)
/*     */   {
/*  33 */     if (this._registeredSubtypes == null) {
/*  34 */       this._registeredSubtypes = new LinkedHashSet();
/*     */     }
/*  36 */     for (NamedType type : types) {
/*  37 */       this._registeredSubtypes.add(type);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerSubtypes(Class<?>... classes)
/*     */   {
/*  43 */     NamedType[] types = new NamedType[classes.length];
/*  44 */     int i = 0; for (int len = classes.length; i < len; i++) {
/*  45 */       types[i] = new NamedType(classes[i]);
/*     */     }
/*  47 */     registerSubtypes(types);
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
/*     */   public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai, JavaType baseType)
/*     */   {
/*  62 */     Class<?> rawBase = baseType == null ? property.getRawType() : baseType.getRawClass();
/*     */     
/*  64 */     HashMap<NamedType, NamedType> collected = new HashMap();
/*     */     
/*  66 */     if (this._registeredSubtypes != null) {
/*  67 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/*  69 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/*  70 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config);
/*  71 */           _collectAndResolve(curr, subtype, config, ai, collected);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  77 */     Collection<NamedType> st = ai.findSubtypes(property);
/*  78 */     if (st != null) {
/*  79 */       for (NamedType nt : st) {
/*  80 */         AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(nt.getType(), ai, config);
/*  81 */         _collectAndResolve(ac, nt, config, ai, collected);
/*     */       }
/*     */     }
/*     */     
/*  85 */     NamedType rootType = new NamedType(rawBase, null);
/*  86 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(rawBase, ai, config);
/*     */     
/*     */ 
/*  89 */     _collectAndResolve(ac, rootType, config, ai, collected);
/*  90 */     return new ArrayList(collected.values());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass type, MapperConfig<?> config, AnnotationIntrospector ai)
/*     */   {
/*  97 */     HashMap<NamedType, NamedType> subtypes = new HashMap();
/*     */     Class<?> rawBase;
/*  99 */     if (this._registeredSubtypes != null) {
/* 100 */       rawBase = type.getRawType();
/* 101 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/* 103 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/* 104 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config);
/* 105 */           _collectAndResolve(curr, subtype, config, ai, subtypes);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 110 */     NamedType rootType = new NamedType(type.getRawType(), null);
/* 111 */     _collectAndResolve(type, rootType, config, ai, subtypes);
/* 112 */     return new ArrayList(subtypes.values());
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
/*     */   protected void _collectAndResolve(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, AnnotationIntrospector ai, HashMap<NamedType, NamedType> collectedSubtypes)
/*     */   {
/* 128 */     if (!namedType.hasName()) {
/* 129 */       String name = ai.findTypeName(annotatedType);
/* 130 */       if (name != null) {
/* 131 */         namedType = new NamedType(namedType.getType(), name);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 136 */     if (collectedSubtypes.containsKey(namedType))
/*     */     {
/* 138 */       if (namedType.hasName()) {
/* 139 */         NamedType prev = (NamedType)collectedSubtypes.get(namedType);
/* 140 */         if (!prev.hasName()) {
/* 141 */           collectedSubtypes.put(namedType, namedType);
/*     */         }
/*     */       }
/* 144 */       return;
/*     */     }
/*     */     
/* 147 */     collectedSubtypes.put(namedType, namedType);
/* 148 */     Collection<NamedType> st = ai.findSubtypes(annotatedType);
/* 149 */     if ((st != null) && (!st.isEmpty())) {
/* 150 */       for (NamedType subtype : st) {
/* 151 */         AnnotatedClass subtypeClass = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config);
/*     */         
/* 153 */         if (!subtype.hasName()) {
/* 154 */           subtype = new NamedType(subtype.getType(), ai.findTypeName(subtypeClass));
/*     */         }
/* 156 */         _collectAndResolve(subtypeClass, subtype, config, ai, collectedSubtypes);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\StdSubtypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */