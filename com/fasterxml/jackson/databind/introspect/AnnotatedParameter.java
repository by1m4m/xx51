/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotatedParameter
/*     */   extends AnnotatedMember
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedWithParams _owner;
/*     */   protected final Type _type;
/*     */   protected final int _index;
/*     */   
/*     */   public AnnotatedParameter(AnnotatedWithParams owner, Type type, AnnotationMap annotations, int index)
/*     */   {
/*  47 */     super(owner == null ? null : owner.getContextClass(), annotations);
/*  48 */     this._owner = owner;
/*  49 */     this._type = type;
/*  50 */     this._index = index;
/*     */   }
/*     */   
/*     */   public AnnotatedParameter withAnnotations(AnnotationMap ann)
/*     */   {
/*  55 */     if (ann == this._annotations) {
/*  56 */       return this;
/*     */     }
/*  58 */     return this._owner.replaceParameterAnnotations(this._index, ann);
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
/*     */   public AnnotatedElement getAnnotated()
/*     */   {
/*  72 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/*  79 */     return this._owner.getModifiers();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  86 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  95 */     return this._annotations == null ? null : this._annotations.get(acls);
/*     */   }
/*     */   
/*     */   public Type getGenericType()
/*     */   {
/* 100 */     return this._type;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/* 106 */     if ((this._type instanceof Class)) {
/* 107 */       return (Class)this._type;
/*     */     }
/*     */     
/* 110 */     JavaType t = TypeFactory.defaultInstance().constructType(this._type);
/* 111 */     return t.getRawClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 122 */     return this._owner.getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Member getMember()
/*     */   {
/* 130 */     return this._owner.getMember();
/*     */   }
/*     */   
/*     */   public void setValue(Object pojo, Object value)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 136 */     throw new UnsupportedOperationException("Cannot call setValue() on constructor parameter of " + getDeclaringClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getValue(Object pojo)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 143 */     throw new UnsupportedOperationException("Cannot call getValue() on constructor parameter of " + getDeclaringClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getParameterType()
/*     */   {
/* 153 */     return this._type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedWithParams getOwner()
/*     */   {
/* 161 */     return this._owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 168 */     return this._index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 178 */     return this._owner.hashCode() + this._index;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 183 */     if (o == this) return true;
/* 184 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 185 */     AnnotatedParameter other = (AnnotatedParameter)o;
/* 186 */     return (other._owner.equals(this._owner)) && (other._index == this._index);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 191 */     return "[parameter #" + getIndex() + ", annotations: " + this._annotations + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */