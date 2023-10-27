/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
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
/*     */ public abstract class AnnotatedWithParams
/*     */   extends AnnotatedMember
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotationMap[] _paramAnnotations;
/*     */   
/*     */   protected AnnotatedWithParams(AnnotatedClass ctxt, AnnotationMap annotations, AnnotationMap[] paramAnnotations)
/*     */   {
/*  35 */     super(ctxt, annotations);
/*  36 */     this._paramAnnotations = paramAnnotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void addOrOverrideParam(int paramIndex, Annotation a)
/*     */   {
/*  47 */     AnnotationMap old = this._paramAnnotations[paramIndex];
/*  48 */     if (old == null) {
/*  49 */       old = new AnnotationMap();
/*  50 */       this._paramAnnotations[paramIndex] = old;
/*     */     }
/*  52 */     old.add(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedParameter replaceParameterAnnotations(int index, AnnotationMap ann)
/*     */   {
/*  61 */     this._paramAnnotations[index] = ann;
/*  62 */     return getParameter(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType getType(TypeBindings bindings, TypeVariable<?>[] typeParams)
/*     */   {
/*  74 */     if ((typeParams != null) && (typeParams.length > 0)) {
/*  75 */       bindings = bindings.childInstance();
/*  76 */       for (TypeVariable<?> var : typeParams) {
/*  77 */         String name = var.getName();
/*     */         
/*  79 */         bindings._addPlaceholder(name);
/*     */         
/*  81 */         Type lowerBound = var.getBounds()[0];
/*  82 */         JavaType type = lowerBound == null ? TypeFactory.unknownType() : bindings.resolveType(lowerBound);
/*     */         
/*  84 */         bindings.addBinding(var.getName(), type);
/*     */       }
/*     */     }
/*  87 */     return bindings.resolveType(getGenericType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  98 */     return this._annotations.get(acls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final AnnotationMap getParameterAnnotations(int index)
/*     */   {
/* 109 */     if ((this._paramAnnotations != null) && 
/* 110 */       (index >= 0) && (index < this._paramAnnotations.length)) {
/* 111 */       return this._paramAnnotations[index];
/*     */     }
/*     */     
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   public final AnnotatedParameter getParameter(int index) {
/* 118 */     return new AnnotatedParameter(this, getGenericParameterType(index), getParameterAnnotations(index), index);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int getParameterCount();
/*     */   
/*     */ 
/*     */   public abstract Class<?> getRawParameterType(int paramInt);
/*     */   
/*     */ 
/*     */   public abstract Type getGenericParameterType(int paramInt);
/*     */   
/*     */ 
/*     */   public final JavaType resolveParameterType(int index, TypeBindings bindings)
/*     */   {
/* 133 */     return bindings.resolveType(getGenericParameterType(index));
/*     */   }
/*     */   
/* 136 */   public final int getAnnotationCount() { return this._annotations.size(); }
/*     */   
/*     */   public abstract Object call()
/*     */     throws Exception;
/*     */   
/*     */   public abstract Object call(Object[] paramArrayOfObject)
/*     */     throws Exception;
/*     */   
/*     */   public abstract Object call1(Object paramObject)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedWithParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */