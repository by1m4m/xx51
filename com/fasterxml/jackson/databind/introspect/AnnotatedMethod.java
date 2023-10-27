/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public final class AnnotatedMethod
/*     */   extends AnnotatedWithParams
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient Method _method;
/*     */   protected Class<?>[] _paramClasses;
/*     */   protected Serialization _serialization;
/*     */   
/*     */   public AnnotatedMethod(AnnotatedClass ctxt, Method method, AnnotationMap classAnn, AnnotationMap[] paramAnnotations)
/*     */   {
/*  38 */     super(ctxt, classAnn, paramAnnotations);
/*  39 */     if (method == null) {
/*  40 */       throw new IllegalArgumentException("Can not construct AnnotatedMethod with null Method");
/*     */     }
/*  42 */     this._method = method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMethod(Serialization ser)
/*     */   {
/*  51 */     super(null, null, null);
/*  52 */     this._method = null;
/*  53 */     this._serialization = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMethod withMethod(Method m)
/*     */   {
/*  61 */     return new AnnotatedMethod(this._context, m, this._annotations, this._paramAnnotations);
/*     */   }
/*     */   
/*     */   public AnnotatedMethod withAnnotations(AnnotationMap ann)
/*     */   {
/*  66 */     return new AnnotatedMethod(this._context, this._method, ann, this._paramAnnotations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Method getAnnotated()
/*     */   {
/*  76 */     return this._method;
/*     */   }
/*     */   
/*  79 */   public int getModifiers() { return this._method.getModifiers(); }
/*     */   
/*     */   public String getName() {
/*  82 */     return this._method.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getGenericType()
/*     */   {
/*  91 */     return this._method.getGenericReturnType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/* 101 */     return this._method.getReturnType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getType(TypeBindings bindings)
/*     */   {
/* 110 */     return getType(bindings, this._method.getTypeParameters());
/*     */   }
/*     */   
/*     */   public final Object call() throws Exception
/*     */   {
/* 115 */     return this._method.invoke(null, new Object[0]);
/*     */   }
/*     */   
/*     */   public final Object call(Object[] args) throws Exception
/*     */   {
/* 120 */     return this._method.invoke(null, args);
/*     */   }
/*     */   
/*     */   public final Object call1(Object arg) throws Exception
/*     */   {
/* 125 */     return this._method.invoke(null, new Object[] { arg });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 135 */     return this._method.getDeclaringClass();
/*     */   }
/*     */   
/* 138 */   public Method getMember() { return this._method; }
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 144 */       this._method.invoke(pojo, new Object[] { value });
/*     */     } catch (IllegalAccessException e) {
/* 146 */       throw new IllegalArgumentException("Failed to setValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 149 */       throw new IllegalArgumentException("Failed to setValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getValue(Object pojo)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       return this._method.invoke(pojo, new Object[0]);
/*     */     } catch (IllegalAccessException e) {
/* 160 */       throw new IllegalArgumentException("Failed to getValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 163 */       throw new IllegalArgumentException("Failed to getValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getParameterCount()
/*     */   {
/* 176 */     return getRawParameterTypes().length;
/*     */   }
/*     */   
/*     */   public String getFullName() {
/* 180 */     return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?>[] getRawParameterTypes()
/*     */   {
/* 186 */     if (this._paramClasses == null) {
/* 187 */       this._paramClasses = this._method.getParameterTypes();
/*     */     }
/* 189 */     return this._paramClasses;
/*     */   }
/*     */   
/*     */   public Type[] getGenericParameterTypes() {
/* 193 */     return this._method.getGenericParameterTypes();
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getRawParameterType(int index)
/*     */   {
/* 199 */     Class<?>[] types = getRawParameterTypes();
/* 200 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */   
/*     */ 
/*     */   public Type getGenericParameterType(int index)
/*     */   {
/* 206 */     Type[] types = this._method.getGenericParameterTypes();
/* 207 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */   
/*     */   public Class<?> getRawReturnType() {
/* 211 */     return this._method.getReturnType();
/*     */   }
/*     */   
/*     */   public Type getGenericReturnType() {
/* 215 */     return this._method.getGenericReturnType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasReturnType()
/*     */   {
/* 226 */     Class<?> rt = getRawReturnType();
/* 227 */     return (rt != Void.TYPE) && (rt != Void.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 238 */     return "[method " + getFullName() + "]";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 243 */     return this._method.getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 248 */     if (o == this) return true;
/* 249 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 250 */     return ((AnnotatedMethod)o)._method == this._method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 260 */     return new AnnotatedMethod(new Serialization(this._method));
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 264 */     Class<?> clazz = this._serialization.clazz;
/*     */     try {
/* 266 */       Method m = clazz.getDeclaredMethod(this._serialization.name, this._serialization.args);
/*     */       
/*     */ 
/* 269 */       if (!m.isAccessible()) {
/* 270 */         ClassUtil.checkAndFixAccess(m);
/*     */       }
/* 272 */       return new AnnotatedMethod(null, m, null, null);
/*     */     } catch (Exception e) {
/* 274 */       throw new IllegalArgumentException("Could not find method '" + this._serialization.name + "' from Class '" + clazz.getName());
/*     */     }
/*     */   }
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
/*     */     protected Class<?>[] args;
/*     */     
/*     */ 
/*     */     public Serialization(Method setter)
/*     */     {
/* 293 */       this.clazz = setter.getDeclaringClass();
/* 294 */       this.name = setter.getName();
/* 295 */       this.args = setter.getParameterTypes();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */