/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
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
/*     */ public final class AnnotatedConstructor
/*     */   extends AnnotatedWithParams
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Constructor<?> _constructor;
/*     */   protected Serialization _serialization;
/*     */   
/*     */   public AnnotatedConstructor(AnnotatedClass ctxt, Constructor<?> constructor, AnnotationMap classAnn, AnnotationMap[] paramAnn)
/*     */   {
/*  34 */     super(ctxt, classAnn, paramAnn);
/*  35 */     if (constructor == null) {
/*  36 */       throw new IllegalArgumentException("Null constructor not allowed");
/*     */     }
/*  38 */     this._constructor = constructor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedConstructor(Serialization ser)
/*     */   {
/*  47 */     super(null, null, null);
/*  48 */     this._constructor = null;
/*  49 */     this._serialization = ser;
/*     */   }
/*     */   
/*     */   public AnnotatedConstructor withAnnotations(AnnotationMap ann)
/*     */   {
/*  54 */     return new AnnotatedConstructor(this._context, this._constructor, ann, this._paramAnnotations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Constructor<?> getAnnotated()
/*     */   {
/*  64 */     return this._constructor;
/*     */   }
/*     */   
/*  67 */   public int getModifiers() { return this._constructor.getModifiers(); }
/*     */   
/*     */   public String getName() {
/*  70 */     return this._constructor.getName();
/*     */   }
/*     */   
/*     */   public Type getGenericType() {
/*  74 */     return getRawType();
/*     */   }
/*     */   
/*     */   public Class<?> getRawType()
/*     */   {
/*  79 */     return this._constructor.getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getType(TypeBindings bindings)
/*     */   {
/*  90 */     return getType(bindings, this._constructor.getTypeParameters());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getParameterCount()
/*     */   {
/* 101 */     return this._constructor.getParameterTypes().length;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getRawParameterType(int index)
/*     */   {
/* 107 */     Class<?>[] types = this._constructor.getParameterTypes();
/* 108 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */   
/*     */ 
/*     */   public Type getGenericParameterType(int index)
/*     */   {
/* 114 */     Type[] types = this._constructor.getGenericParameterTypes();
/* 115 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */   
/*     */   public final Object call() throws Exception
/*     */   {
/* 120 */     return this._constructor.newInstance(new Object[0]);
/*     */   }
/*     */   
/*     */   public final Object call(Object[] args) throws Exception
/*     */   {
/* 125 */     return this._constructor.newInstance(args);
/*     */   }
/*     */   
/*     */   public final Object call1(Object arg) throws Exception
/*     */   {
/* 130 */     return this._constructor.newInstance(new Object[] { arg });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 140 */     return this._constructor.getDeclaringClass();
/*     */   }
/*     */   
/* 143 */   public Member getMember() { return this._constructor; }
/*     */   
/*     */ 
/*     */   public void setValue(Object pojo, Object value)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 149 */     throw new UnsupportedOperationException("Cannot call setValue() on constructor of " + getDeclaringClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getValue(Object pojo)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 157 */     throw new UnsupportedOperationException("Cannot call getValue() on constructor of " + getDeclaringClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 169 */     return "[constructor for " + getName() + ", annotations: " + this._annotations + "]";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 174 */     return this._constructor.getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 179 */     if (o == this) return true;
/* 180 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 181 */     return ((AnnotatedConstructor)o)._constructor == this._constructor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 191 */     return new AnnotatedConstructor(new Serialization(this._constructor));
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 195 */     Class<?> clazz = this._serialization.clazz;
/*     */     try {
/* 197 */       Constructor<?> ctor = clazz.getDeclaredConstructor(this._serialization.args);
/*     */       
/* 199 */       if (!ctor.isAccessible()) {
/* 200 */         ClassUtil.checkAndFixAccess(ctor);
/*     */       }
/* 202 */       return new AnnotatedConstructor(null, ctor, null, null);
/*     */     } catch (Exception e) {
/* 204 */       throw new IllegalArgumentException("Could not find constructor with " + this._serialization.args.length + " args from Class '" + clazz.getName());
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
/*     */     protected Class<?>[] args;
/*     */     
/*     */ 
/*     */     public Serialization(Constructor<?> ctor)
/*     */     {
/* 222 */       this.clazz = ctor.getDeclaringClass();
/* 223 */       this.args = ctor.getParameterTypes();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */