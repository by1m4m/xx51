/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableList.Builder;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.AnnotatedType;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
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
/*     */ @Beta
/*     */ public abstract class Invokable<T, R>
/*     */   extends Element
/*     */   implements GenericDeclaration
/*     */ {
/*     */   <M extends AccessibleObject,  extends Member> Invokable(M member)
/*     */   {
/*  62 */     super(member);
/*     */   }
/*     */   
/*     */   public static Invokable<?, Object> from(Method method)
/*     */   {
/*  67 */     return new MethodInvokable(method);
/*     */   }
/*     */   
/*     */   public static <T> Invokable<T, T> from(Constructor<T> constructor)
/*     */   {
/*  72 */     return new ConstructorInvokable(constructor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isOverridable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isVarArgs();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final R invoke(T receiver, Object... args)
/*     */     throws InvocationTargetException, IllegalAccessException
/*     */   {
/* 102 */     return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final TypeToken<? extends R> getReturnType()
/*     */   {
/* 109 */     return TypeToken.of(getGenericReturnType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ImmutableList<Parameter> getParameters()
/*     */   {
/* 118 */     Type[] parameterTypes = getGenericParameterTypes();
/* 119 */     Annotation[][] annotations = getParameterAnnotations();
/* 120 */     AnnotatedType[] annotatedTypes = getAnnotatedParameterTypes();
/* 121 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 122 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 123 */       builder.add(new Parameter(this, i, 
/*     */       
/* 125 */         TypeToken.of(parameterTypes[i]), annotations[i], annotatedTypes[i]));
/*     */     }
/* 127 */     return builder.build();
/*     */   }
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes()
/*     */   {
/* 132 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 133 */     for (Type type : getGenericExceptionTypes())
/*     */     {
/*     */ 
/*     */ 
/* 137 */       TypeToken<? extends Throwable> exceptionType = TypeToken.of(type);
/* 138 */       builder.add(exceptionType);
/*     */     }
/* 140 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType)
/*     */   {
/* 152 */     return returning(TypeToken.of(returnType));
/*     */   }
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType)
/*     */   {
/* 157 */     if (!returnType.isSupertypeOf(getReturnType()))
/*     */     {
/* 159 */       throw new IllegalArgumentException("Invokable is known to return " + getReturnType() + ", not " + returnType);
/*     */     }
/*     */     
/* 162 */     Invokable<T, R1> specialized = this;
/* 163 */     return specialized;
/*     */   }
/*     */   
/*     */ 
/*     */   public final Class<? super T> getDeclaringClass()
/*     */   {
/* 169 */     return super.getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeToken<T> getOwnerType()
/*     */   {
/* 177 */     return TypeToken.of(getDeclaringClass());
/*     */   }
/*     */   
/*     */   abstract Object invokeInternal(Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract AnnotatedType[] getAnnotatedParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   public abstract AnnotatedType getAnnotatedReturnType();
/*     */   
/*     */   static class MethodInvokable<T>
/*     */     extends Invokable<T, Object>
/*     */   {
/*     */     final Method method;
/*     */     
/*     */     MethodInvokable(Method method)
/*     */     {
/* 201 */       super();
/* 202 */       this.method = method;
/*     */     }
/*     */     
/*     */     final Object invokeInternal(Object receiver, Object[] args)
/*     */       throws InvocationTargetException, IllegalAccessException
/*     */     {
/* 208 */       return this.method.invoke(receiver, args);
/*     */     }
/*     */     
/*     */     Type getGenericReturnType()
/*     */     {
/* 213 */       return this.method.getGenericReturnType();
/*     */     }
/*     */     
/*     */     Type[] getGenericParameterTypes()
/*     */     {
/* 218 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */     
/*     */     AnnotatedType[] getAnnotatedParameterTypes()
/*     */     {
/* 223 */       return this.method.getAnnotatedParameterTypes();
/*     */     }
/*     */     
/*     */     public AnnotatedType getAnnotatedReturnType()
/*     */     {
/* 228 */       return this.method.getAnnotatedReturnType();
/*     */     }
/*     */     
/*     */     Type[] getGenericExceptionTypes()
/*     */     {
/* 233 */       return this.method.getGenericExceptionTypes();
/*     */     }
/*     */     
/*     */     final Annotation[][] getParameterAnnotations()
/*     */     {
/* 238 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters()
/*     */     {
/* 243 */       return this.method.getTypeParameters();
/*     */     }
/*     */     
/*     */     public final boolean isOverridable()
/*     */     {
/* 248 */       return (!isFinal()) && 
/* 249 */         (!isPrivate()) && 
/* 250 */         (!isStatic()) && 
/* 251 */         (!Modifier.isFinal(getDeclaringClass().getModifiers()));
/*     */     }
/*     */     
/*     */     public final boolean isVarArgs()
/*     */     {
/* 256 */       return this.method.isVarArgs();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstructorInvokable<T> extends Invokable<T, T>
/*     */   {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 265 */       super();
/* 266 */       this.constructor = constructor;
/*     */     }
/*     */     
/*     */     final Object invokeInternal(Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException
/*     */     {
/*     */       try
/*     */       {
/* 273 */         return this.constructor.newInstance(args);
/*     */       } catch (InstantiationException e) {
/* 275 */         throw new RuntimeException(this.constructor + " failed.", e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Type getGenericReturnType()
/*     */     {
/* 285 */       Class<?> declaringClass = getDeclaringClass();
/* 286 */       TypeVariable<?>[] typeParams = declaringClass.getTypeParameters();
/* 287 */       if (typeParams.length > 0) {
/* 288 */         return Types.newParameterizedType(declaringClass, typeParams);
/*     */       }
/* 290 */       return declaringClass;
/*     */     }
/*     */     
/*     */ 
/*     */     Type[] getGenericParameterTypes()
/*     */     {
/* 296 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 297 */       if ((types.length > 0) && (mayNeedHiddenThis())) {
/* 298 */         Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
/* 299 */         if ((types.length == rawParamTypes.length) && 
/* 300 */           (rawParamTypes[0] == getDeclaringClass().getEnclosingClass()))
/*     */         {
/* 302 */           return (Type[])Arrays.copyOfRange(types, 1, types.length);
/*     */         }
/*     */       }
/* 305 */       return types;
/*     */     }
/*     */     
/*     */     AnnotatedType[] getAnnotatedParameterTypes()
/*     */     {
/* 310 */       return this.constructor.getAnnotatedParameterTypes();
/*     */     }
/*     */     
/*     */     public AnnotatedType getAnnotatedReturnType()
/*     */     {
/* 315 */       return this.constructor.getAnnotatedReturnType();
/*     */     }
/*     */     
/*     */     Type[] getGenericExceptionTypes()
/*     */     {
/* 320 */       return this.constructor.getGenericExceptionTypes();
/*     */     }
/*     */     
/*     */     final Annotation[][] getParameterAnnotations()
/*     */     {
/* 325 */       return this.constructor.getParameterAnnotations();
/*     */     }
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
/*     */     public final TypeVariable<?>[] getTypeParameters()
/*     */     {
/* 339 */       TypeVariable<?>[] declaredByClass = getDeclaringClass().getTypeParameters();
/* 340 */       TypeVariable<?>[] declaredByConstructor = this.constructor.getTypeParameters();
/* 341 */       TypeVariable<?>[] result = new TypeVariable[declaredByClass.length + declaredByConstructor.length];
/*     */       
/* 343 */       System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
/* 344 */       System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
/*     */       
/* 346 */       return result;
/*     */     }
/*     */     
/*     */     public final boolean isOverridable()
/*     */     {
/* 351 */       return false;
/*     */     }
/*     */     
/*     */     public final boolean isVarArgs()
/*     */     {
/* 356 */       return this.constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 360 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 361 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 363 */         return true;
/*     */       }
/* 365 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 366 */       if (enclosingMethod != null)
/*     */       {
/* 368 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 376 */       return (declaringClass.getEnclosingClass() != null) && 
/* 377 */         (!Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\reflect\Invokable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */