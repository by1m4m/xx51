/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Collections;
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
/*     */ public abstract class AnnotatedMember
/*     */   extends Annotated
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient AnnotatedClass _context;
/*     */   protected final transient AnnotationMap _annotations;
/*     */   
/*     */   protected AnnotatedMember(AnnotatedClass ctxt, AnnotationMap annotations)
/*     */   {
/*  44 */     this._context = ctxt;
/*  45 */     this._annotations = annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMember(AnnotatedMember base)
/*     */   {
/*  54 */     this._context = base._context;
/*  55 */     this._annotations = base._annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Class<?> getDeclaringClass();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Member getMember();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedClass getContextClass()
/*     */   {
/*  83 */     return this._context;
/*     */   }
/*     */   
/*     */   public Iterable<Annotation> annotations()
/*     */   {
/*  88 */     if (this._annotations == null) {
/*  89 */       return Collections.emptyList();
/*     */     }
/*  91 */     return this._annotations.annotations();
/*     */   }
/*     */   
/*     */   protected AnnotationMap getAllAnnotations()
/*     */   {
/*  96 */     return this._annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean addOrOverride(Annotation a)
/*     */   {
/* 105 */     return this._annotations.add(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean addIfNotPresent(Annotation a)
/*     */   {
/* 114 */     return this._annotations.addIfNotPresent(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void fixAccess()
/*     */   {
/* 123 */     ClassUtil.checkAndFixAccess(getMember());
/*     */   }
/*     */   
/*     */   public abstract void setValue(Object paramObject1, Object paramObject2)
/*     */     throws UnsupportedOperationException, IllegalArgumentException;
/*     */   
/*     */   public abstract Object getValue(Object paramObject)
/*     */     throws UnsupportedOperationException, IllegalArgumentException;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */