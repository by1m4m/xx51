/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class Types
/*     */ {
/*     */   public static ParameterizedType getSuperParameterizedType(Type type, Class<?> superClass)
/*     */   {
/*  57 */     if (((type instanceof Class)) || ((type instanceof ParameterizedType))) {
/*  58 */       while ((type != null) && (type != Object.class)) { Class<?> rawType;
/*     */         Class<?> rawType;
/*  60 */         if ((type instanceof Class))
/*     */         {
/*  62 */           rawType = (Class)type;
/*     */         }
/*     */         else {
/*  65 */           ParameterizedType parameterizedType = (ParameterizedType)type;
/*  66 */           rawType = getRawClass(parameterizedType);
/*     */           
/*  68 */           if (rawType == superClass)
/*     */           {
/*  70 */             return parameterizedType;
/*     */           }
/*  72 */           if (superClass.isInterface()) {
/*  73 */             Type[] arr$ = rawType.getGenericInterfaces();int len$ = arr$.length; for (int i$ = 0;; i$++) { if (i$ >= len$) break label138; Type interfaceType = arr$[i$];
/*     */               
/*  75 */               Class<?> interfaceClass = (interfaceType instanceof Class) ? (Class)interfaceType : getRawClass((ParameterizedType)interfaceType);
/*     */               
/*     */ 
/*  78 */               if (superClass.isAssignableFrom(interfaceClass)) {
/*  79 */                 type = interfaceType;
/*  80 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         label138:
/*  86 */         type = rawType.getGenericSuperclass();
/*     */       }
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAssignableToOrFrom(Class<?> classToCheck, Class<?> anotherClass)
/*     */   {
/*  99 */     return (classToCheck.isAssignableFrom(anotherClass)) || (anotherClass.isAssignableFrom(classToCheck));
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
/*     */   public static <T> T newInstance(Class<T> clazz)
/*     */   {
/*     */     try
/*     */     {
/* 116 */       return (T)clazz.newInstance();
/*     */     } catch (IllegalAccessException e) {
/* 118 */       throw handleExceptionForNewInstance(e, clazz);
/*     */     } catch (InstantiationException e) {
/* 120 */       throw handleExceptionForNewInstance(e, clazz);
/*     */     }
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException handleExceptionForNewInstance(Exception e, Class<?> clazz)
/*     */   {
/* 126 */     StringBuilder buf = new StringBuilder("unable to create new instance of class ").append(clazz.getName());
/*     */     
/* 128 */     ArrayList<String> reasons = new ArrayList();
/* 129 */     if (clazz.isArray()) {
/* 130 */       reasons.add("because it is an array");
/* 131 */     } else if (clazz.isPrimitive()) {
/* 132 */       reasons.add("because it is primitive");
/* 133 */     } else if (clazz == Void.class) {
/* 134 */       reasons.add("because it is void");
/*     */     } else {
/* 136 */       if (Modifier.isInterface(clazz.getModifiers())) {
/* 137 */         reasons.add("because it is an interface");
/* 138 */       } else if (Modifier.isAbstract(clazz.getModifiers())) {
/* 139 */         reasons.add("because it is abstract");
/*     */       }
/* 141 */       if ((clazz.getEnclosingClass() != null) && (!Modifier.isStatic(clazz.getModifiers()))) {
/* 142 */         reasons.add("because it is not static");
/*     */       }
/*     */       
/* 145 */       if (!Modifier.isPublic(clazz.getModifiers())) {
/* 146 */         reasons.add("possibly because it is not public");
/*     */       } else {
/*     */         try {
/* 149 */           clazz.getConstructor(new Class[0]);
/*     */         } catch (NoSuchMethodException e1) {
/* 151 */           reasons.add("because it has no accessible default constructor");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 156 */     boolean and = false;
/* 157 */     for (String reason : reasons) {
/* 158 */       if (and) {
/* 159 */         buf.append(" and");
/*     */       } else {
/* 161 */         and = true;
/*     */       }
/* 163 */       buf.append(" ").append(reason);
/*     */     }
/* 165 */     return new IllegalArgumentException(buf.toString(), e);
/*     */   }
/*     */   
/*     */   public static boolean isArray(Type type)
/*     */   {
/* 170 */     return ((type instanceof GenericArrayType)) || (((type instanceof Class)) && (((Class)type).isArray()));
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
/*     */   public static Type getArrayComponentType(Type array)
/*     */   {
/* 185 */     return (array instanceof GenericArrayType) ? ((GenericArrayType)array).getGenericComponentType() : ((Class)array).getComponentType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> getRawClass(ParameterizedType parameterType)
/*     */   {
/* 197 */     return (Class)parameterType.getRawType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type getBound(WildcardType wildcardType)
/*     */   {
/* 207 */     Type[] lowerBounds = wildcardType.getLowerBounds();
/* 208 */     if (lowerBounds.length != 0) {
/* 209 */       return lowerBounds[0];
/*     */     }
/* 211 */     return wildcardType.getUpperBounds()[0];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type resolveTypeVariable(List<Type> context, TypeVariable<?> typeVariable)
/*     */   {
/* 231 */     GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
/* 232 */     if ((genericDeclaration instanceof Class)) {
/* 233 */       Class<?> rawGenericDeclaration = (Class)genericDeclaration;
/*     */       
/* 235 */       int contextIndex = context.size();
/* 236 */       ParameterizedType parameterizedType = null;
/* 237 */       while (parameterizedType == null) { contextIndex--; if (contextIndex < 0) break;
/* 238 */         parameterizedType = getSuperParameterizedType((Type)context.get(contextIndex), rawGenericDeclaration);
/*     */       }
/*     */       
/* 241 */       if (parameterizedType != null)
/*     */       {
/* 243 */         TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
/* 244 */         for (int index = 0; 
/* 245 */             index < typeParameters.length; index++) {
/* 246 */           TypeVariable<?> typeParameter = typeParameters[index];
/* 247 */           if (typeParameter.equals(typeVariable)) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         
/* 252 */         Type result = parameterizedType.getActualTypeArguments()[index];
/* 253 */         if ((result instanceof TypeVariable))
/*     */         {
/* 255 */           Type resolve = resolveTypeVariable(context, (TypeVariable)result);
/* 256 */           if (resolve != null) {
/* 257 */             return resolve;
/*     */           }
/*     */         }
/*     */         
/* 261 */         return result;
/*     */       }
/*     */     }
/* 264 */     return null;
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
/*     */   public static Class<?> getRawArrayComponentType(List<Type> context, Type componentType)
/*     */   {
/* 277 */     if ((componentType instanceof TypeVariable)) {
/* 278 */       componentType = resolveTypeVariable(context, (TypeVariable)componentType);
/*     */     }
/* 280 */     if ((componentType instanceof GenericArrayType)) {
/* 281 */       Class<?> raw = getRawArrayComponentType(context, getArrayComponentType(componentType));
/* 282 */       return Array.newInstance(raw, 0).getClass();
/*     */     }
/* 284 */     if ((componentType instanceof Class)) {
/* 285 */       return (Class)componentType;
/*     */     }
/* 287 */     if ((componentType instanceof ParameterizedType)) {
/* 288 */       return getRawClass((ParameterizedType)componentType);
/*     */     }
/* 290 */     Preconditions.checkArgument(componentType == null, "wildcard type is not supported: %s", new Object[] { componentType });
/*     */     
/* 292 */     return Object.class;
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
/*     */   public static Type getIterableParameter(Type iterableType)
/*     */   {
/* 307 */     return getActualParameterAtPosition(iterableType, Iterable.class, 0);
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
/*     */   public static Type getMapValueParameter(Type mapType)
/*     */   {
/* 322 */     return getActualParameterAtPosition(mapType, Map.class, 1);
/*     */   }
/*     */   
/*     */   private static Type getActualParameterAtPosition(Type type, Class<?> superClass, int position) {
/* 326 */     ParameterizedType parameterizedType = getSuperParameterizedType(type, superClass);
/* 327 */     if (parameterizedType == null) {
/* 328 */       return null;
/*     */     }
/* 330 */     Type valueType = parameterizedType.getActualTypeArguments()[position];
/*     */     
/*     */ 
/* 333 */     if ((valueType instanceof TypeVariable)) {
/* 334 */       Type resolve = resolveTypeVariable(Arrays.asList(new Type[] { type }), (TypeVariable)valueType);
/* 335 */       if (resolve != null) {
/* 336 */         return resolve;
/*     */       }
/*     */     }
/* 339 */     return valueType;
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
/*     */   public static <T> Iterable<T> iterableOf(Object value)
/*     */   {
/* 355 */     if ((value instanceof Iterable)) {
/* 356 */       return (Iterable)value;
/*     */     }
/* 358 */     Class<?> valueClass = value.getClass();
/* 359 */     Preconditions.checkArgument(valueClass.isArray(), "not an array or Iterable: %s", new Object[] { valueClass });
/* 360 */     Class<?> subClass = valueClass.getComponentType();
/* 361 */     if (!subClass.isPrimitive()) {
/* 362 */       return Arrays.asList((Object[])value);
/*     */     }
/* 364 */     new Iterable()
/*     */     {
/*     */       public Iterator<T> iterator() {
/* 367 */         new Iterator()
/*     */         {
/* 369 */           final int length = Array.getLength(Types.1.this.val$value);
/* 370 */           int index = 0;
/*     */           
/*     */           public boolean hasNext() {
/* 373 */             return this.index < this.length;
/*     */           }
/*     */           
/*     */           public T next() {
/* 377 */             if (!hasNext()) {
/* 378 */               throw new NoSuchElementException();
/*     */             }
/* 380 */             return (T)Array.get(Types.1.this.val$value, this.index++);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 384 */             throw new UnsupportedOperationException();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object toArray(Collection<?> collection, Class<?> componentType)
/*     */   {
/* 400 */     if (componentType.isPrimitive()) {
/* 401 */       Object array = Array.newInstance(componentType, collection.size());
/* 402 */       int index = 0;
/* 403 */       for (Object value : collection) {
/* 404 */         Array.set(array, index++, value);
/*     */       }
/* 406 */       return array;
/*     */     }
/* 408 */     return collection.toArray((Object[])Array.newInstance(componentType, collection.size()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Types.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */