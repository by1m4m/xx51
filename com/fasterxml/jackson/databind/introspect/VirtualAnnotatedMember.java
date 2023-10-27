/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
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
/*     */ public class VirtualAnnotatedMember
/*     */   extends AnnotatedMember
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<?> _declaringClass;
/*     */   protected final Class<?> _rawType;
/*     */   protected final String _name;
/*     */   
/*     */   public VirtualAnnotatedMember(AnnotatedClass contextClass, Class<?> declaringClass, String name, Class<?> rawType)
/*     */   {
/*  32 */     super(contextClass, null);
/*  33 */     this._declaringClass = declaringClass;
/*  34 */     this._rawType = rawType;
/*  35 */     this._name = name;
/*     */   }
/*     */   
/*     */   public Annotated withAnnotations(AnnotationMap fallback)
/*     */   {
/*  40 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field getAnnotated()
/*     */   {
/*  50 */     return null;
/*     */   }
/*     */   
/*  53 */   public int getModifiers() { return 0; }
/*     */   
/*     */   public String getName() {
/*  56 */     return this._name;
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls) {
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public Type getGenericType()
/*     */   {
/*  65 */     return this._rawType;
/*     */   }
/*     */   
/*     */   public Class<?> getRawType()
/*     */   {
/*  70 */     return this._rawType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/*  80 */     return this._declaringClass;
/*     */   }
/*     */   
/*  83 */   public Member getMember() { return null; }
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException
/*     */   {
/*  87 */     throw new IllegalArgumentException("Can not set virtual property '" + this._name + "'");
/*     */   }
/*     */   
/*     */   public Object getValue(Object pojo) throws IllegalArgumentException
/*     */   {
/*  92 */     throw new IllegalArgumentException("Can not get virtual property '" + this._name + "'");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullName()
/*     */   {
/* 102 */     return getDeclaringClass().getName() + "#" + getName();
/*     */   }
/*     */   
/* 105 */   public int getAnnotationCount() { return 0; }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 109 */     return this._name.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 114 */     if (o == this) return true;
/* 115 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 116 */     VirtualAnnotatedMember other = (VirtualAnnotatedMember)o;
/* 117 */     return (other._declaringClass == this._declaringClass) && (other._name.equals(this._name));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     return "[field " + getFullName() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\VirtualAnnotatedMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */