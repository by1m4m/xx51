/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotatedField
/*     */   extends AnnotatedMember
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient Field _field;
/*     */   protected Serialization _serialization;
/*     */   
/*     */   public AnnotatedField(AnnotatedClass contextClass, Field field, AnnotationMap annMap)
/*     */   {
/*  41 */     super(contextClass, annMap);
/*  42 */     this._field = field;
/*     */   }
/*     */   
/*     */   public AnnotatedField withAnnotations(AnnotationMap ann)
/*     */   {
/*  47 */     return new AnnotatedField(this._context, this._field, ann);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedField(Serialization ser)
/*     */   {
/*  55 */     super(null, null);
/*  56 */     this._field = null;
/*  57 */     this._serialization = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field getAnnotated()
/*     */   {
/*  67 */     return this._field;
/*     */   }
/*     */   
/*  70 */   public int getModifiers() { return this._field.getModifiers(); }
/*     */   
/*     */   public String getName() {
/*  73 */     return this._field.getName();
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls) {
/*  77 */     return this._annotations == null ? null : this._annotations.get(acls);
/*     */   }
/*     */   
/*     */   public Type getGenericType()
/*     */   {
/*  82 */     return this._field.getGenericType();
/*     */   }
/*     */   
/*     */   public Class<?> getRawType()
/*     */   {
/*  87 */     return this._field.getType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/*  97 */     return this._field.getDeclaringClass();
/*     */   }
/*     */   
/* 100 */   public Member getMember() { return this._field; }
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 106 */       this._field.set(pojo, value);
/*     */     } catch (IllegalAccessException e) {
/* 108 */       throw new IllegalArgumentException("Failed to setValue() for field " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getValue(Object pojo)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 117 */       return this._field.get(pojo);
/*     */     } catch (IllegalAccessException e) {
/* 119 */       throw new IllegalArgumentException("Failed to getValue() for field " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullName()
/*     */   {
/* 131 */     return getDeclaringClass().getName() + "#" + getName();
/*     */   }
/*     */   
/* 134 */   public int getAnnotationCount() { return this._annotations.size(); }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 138 */     return this._field.getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 143 */     if (o == this) return true;
/* 144 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 145 */     return ((AnnotatedField)o)._field == this._field;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 150 */     return "[field " + getFullName() + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 160 */     return new AnnotatedField(new Serialization(this._field));
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 164 */     Class<?> clazz = this._serialization.clazz;
/*     */     try {
/* 166 */       Field f = clazz.getDeclaredField(this._serialization.name);
/*     */       
/* 168 */       if (!f.isAccessible()) {
/* 169 */         ClassUtil.checkAndFixAccess(f);
/*     */       }
/* 171 */       return new AnnotatedField(null, f, null);
/*     */     } catch (Exception e) {
/* 173 */       throw new IllegalArgumentException("Could not find method '" + this._serialization.name + "' from Class '" + clazz.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class Serialization
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Class<?> clazz;
/*     */     
/*     */     protected String name;
/*     */     
/*     */ 
/*     */     public Serialization(Field f)
/*     */     {
/* 191 */       this.clazz = f.getDeclaringClass();
/* 192 */       this.name = f.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */