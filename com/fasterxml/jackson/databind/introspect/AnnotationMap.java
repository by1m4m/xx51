/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotationMap
/*     */   implements Annotations
/*     */ {
/*     */   protected HashMap<Class<? extends Annotation>, Annotation> _annotations;
/*     */   
/*     */   public AnnotationMap() {}
/*     */   
/*     */   private AnnotationMap(HashMap<Class<? extends Annotation>, Annotation> a)
/*     */   {
/*  21 */     this._annotations = a;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A get(Class<A> cls)
/*     */   {
/*  28 */     if (this._annotations == null) {
/*  29 */       return null;
/*     */     }
/*  31 */     return (Annotation)this._annotations.get(cls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Iterable<Annotation> annotations()
/*     */   {
/*  38 */     if ((this._annotations == null) || (this._annotations.size() == 0)) {
/*  39 */       return Collections.emptyList();
/*     */     }
/*  41 */     return this._annotations.values();
/*     */   }
/*     */   
/*     */   public static AnnotationMap merge(AnnotationMap primary, AnnotationMap secondary)
/*     */   {
/*  46 */     if ((primary == null) || (primary._annotations == null) || (primary._annotations.isEmpty())) {
/*  47 */       return secondary;
/*     */     }
/*  49 */     if ((secondary == null) || (secondary._annotations == null) || (secondary._annotations.isEmpty())) {
/*  50 */       return primary;
/*     */     }
/*  52 */     HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap();
/*     */     
/*     */ 
/*  55 */     for (Annotation ann : secondary._annotations.values()) {
/*  56 */       annotations.put(ann.annotationType(), ann);
/*     */     }
/*     */     
/*  59 */     for (Annotation ann : primary._annotations.values()) {
/*  60 */       annotations.put(ann.annotationType(), ann);
/*     */     }
/*  62 */     return new AnnotationMap(annotations);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  67 */     return this._annotations == null ? 0 : this._annotations.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean addIfNotPresent(Annotation ann)
/*     */   {
/*  76 */     if ((this._annotations == null) || (!this._annotations.containsKey(ann.annotationType()))) {
/*  77 */       _add(ann);
/*  78 */       return true;
/*     */     }
/*  80 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean add(Annotation ann)
/*     */   {
/*  87 */     return _add(ann);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  92 */     if (this._annotations == null) {
/*  93 */       return "[null]";
/*     */     }
/*  95 */     return this._annotations.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _add(Annotation ann)
/*     */   {
/* 105 */     if (this._annotations == null) {
/* 106 */       this._annotations = new HashMap();
/*     */     }
/* 108 */     Annotation previous = (Annotation)this._annotations.put(ann.annotationType(), ann);
/* 109 */     return (previous != null) && (previous.equals(ann));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotationMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */