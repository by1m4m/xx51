/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.annotation.NoClass;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassUtil
/*     */ {
/*     */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore)
/*     */   {
/*  29 */     return findSuperTypes(cls, endBefore, new ArrayList(8));
/*     */   }
/*     */   
/*     */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
/*  33 */     _addSuperTypes(cls, endBefore, result, false);
/*  34 */     return result;
/*     */   }
/*     */   
/*     */   private static void _addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
/*  38 */     if ((cls == endBefore) || (cls == null) || (cls == Object.class)) return;
/*  39 */     if (addClassItself) {
/*  40 */       if (result.contains(cls)) {
/*  41 */         return;
/*     */       }
/*  43 */       result.add(cls);
/*     */     }
/*  45 */     for (Class<?> intCls : cls.getInterfaces()) {
/*  46 */       _addSuperTypes(intCls, endBefore, result, true);
/*     */     }
/*  48 */     _addSuperTypes(cls.getSuperclass(), endBefore, result, true);
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
/*     */   public static String canBeABeanType(Class<?> type)
/*     */   {
/*  64 */     if (type.isAnnotation()) {
/*  65 */       return "annotation";
/*     */     }
/*  67 */     if (type.isArray()) {
/*  68 */       return "array";
/*     */     }
/*  70 */     if (type.isEnum()) {
/*  71 */       return "enum";
/*     */     }
/*  73 */     if (type.isPrimitive()) {
/*  74 */       return "primitive";
/*     */     }
/*     */     
/*     */ 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String isLocalType(Class<?> type, boolean allowNonStatic)
/*     */   {
/*     */     try
/*     */     {
/*  89 */       if (type.getEnclosingMethod() != null) {
/*  90 */         return "local/anonymous";
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */       if ((!allowNonStatic) && 
/*  98 */         (type.getEnclosingClass() != null) && 
/*  99 */         (!Modifier.isStatic(type.getModifiers()))) {
/* 100 */         return "non-static member class";
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {}catch (NullPointerException e) {}
/*     */     
/*     */ 
/*     */ 
/* 107 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> getOuterClass(Class<?> type)
/*     */   {
/*     */     try
/*     */     {
/* 118 */       if (type.getEnclosingMethod() != null) {
/* 119 */         return null;
/*     */       }
/* 121 */       if (!Modifier.isStatic(type.getModifiers())) {
/* 122 */         return type.getEnclosingClass();
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {}catch (NullPointerException e) {}
/* 126 */     return null;
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
/*     */   public static boolean isProxyType(Class<?> type)
/*     */   {
/* 144 */     String name = type.getName();
/*     */     
/* 146 */     if ((name.startsWith("net.sf.cglib.proxy.")) || (name.startsWith("org.hibernate.proxy.")))
/*     */     {
/* 148 */       return true;
/*     */     }
/*     */     
/* 151 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isConcrete(Class<?> type)
/*     */   {
/* 160 */     int mod = type.getModifiers();
/* 161 */     return (mod & 0x600) == 0;
/*     */   }
/*     */   
/*     */   public static boolean isConcrete(Member member)
/*     */   {
/* 166 */     int mod = member.getModifiers();
/* 167 */     return (mod & 0x600) == 0;
/*     */   }
/*     */   
/*     */   public static boolean isCollectionMapOrArray(Class<?> type)
/*     */   {
/* 172 */     if (type.isArray()) return true;
/* 173 */     if (Collection.class.isAssignableFrom(type)) return true;
/* 174 */     if (Map.class.isAssignableFrom(type)) return true;
/* 175 */     return false;
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
/*     */   public static String getClassDescription(Object classOrInstance)
/*     */   {
/* 191 */     if (classOrInstance == null) {
/* 192 */       return "unknown";
/*     */     }
/* 194 */     Class<?> cls = (classOrInstance instanceof Class) ? (Class)classOrInstance : classOrInstance.getClass();
/*     */     
/* 196 */     return cls.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> findClass(String className)
/*     */     throws ClassNotFoundException
/*     */   {
/* 208 */     if (className.indexOf('.') < 0) {
/* 209 */       if ("int".equals(className)) return Integer.TYPE;
/* 210 */       if ("long".equals(className)) return Long.TYPE;
/* 211 */       if ("float".equals(className)) return Float.TYPE;
/* 212 */       if ("double".equals(className)) return Double.TYPE;
/* 213 */       if ("boolean".equals(className)) return Boolean.TYPE;
/* 214 */       if ("byte".equals(className)) return Byte.TYPE;
/* 215 */       if ("char".equals(className)) return Character.TYPE;
/* 216 */       if ("short".equals(className)) return Short.TYPE;
/* 217 */       if ("void".equals(className)) { return Void.TYPE;
/*     */       }
/*     */     }
/* 220 */     Throwable prob = null;
/* 221 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/*     */     
/* 223 */     if (loader != null) {
/*     */       try {
/* 225 */         return Class.forName(className, true, loader);
/*     */       } catch (Exception e) {
/* 227 */         prob = getRootCause(e);
/*     */       }
/*     */     }
/*     */     try {
/* 231 */       return Class.forName(className);
/*     */     } catch (Exception e) {
/* 233 */       if (prob == null) {
/* 234 */         prob = getRootCause(e);
/*     */       }
/*     */       
/* 237 */       if ((prob instanceof RuntimeException)) {
/* 238 */         throw ((RuntimeException)prob);
/*     */       }
/* 240 */       throw new ClassNotFoundException(prob.getMessage(), prob);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasGetterSignature(Method m)
/*     */   {
/* 252 */     if (Modifier.isStatic(m.getModifiers())) {
/* 253 */       return false;
/*     */     }
/*     */     
/* 256 */     Class<?>[] pts = m.getParameterTypes();
/* 257 */     if ((pts != null) && (pts.length != 0)) {
/* 258 */       return false;
/*     */     }
/*     */     
/* 261 */     if (Void.TYPE == m.getReturnType()) {
/* 262 */       return false;
/*     */     }
/*     */     
/* 265 */     return true;
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
/*     */   public static Throwable getRootCause(Throwable t)
/*     */   {
/* 280 */     while (t.getCause() != null) {
/* 281 */       t = t.getCause();
/*     */     }
/* 283 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void throwRootCause(Throwable t)
/*     */     throws Exception
/*     */   {
/* 294 */     t = getRootCause(t);
/* 295 */     if ((t instanceof Exception)) {
/* 296 */       throw ((Exception)t);
/*     */     }
/* 298 */     throw ((Error)t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void throwAsIAE(Throwable t)
/*     */   {
/* 307 */     throwAsIAE(t, t.getMessage());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void throwAsIAE(Throwable t, String msg)
/*     */   {
/* 317 */     if ((t instanceof RuntimeException)) {
/* 318 */       throw ((RuntimeException)t);
/*     */     }
/* 320 */     if ((t instanceof Error)) {
/* 321 */       throw ((Error)t);
/*     */     }
/* 323 */     throw new IllegalArgumentException(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void unwrapAndThrowAsIAE(Throwable t)
/*     */   {
/* 333 */     throwAsIAE(getRootCause(t));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void unwrapAndThrowAsIAE(Throwable t, String msg)
/*     */   {
/* 343 */     throwAsIAE(getRootCause(t), msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T createInstance(Class<T> cls, boolean canFixAccess)
/*     */     throws IllegalArgumentException
/*     */   {
/* 368 */     Constructor<T> ctor = findConstructor(cls, canFixAccess);
/* 369 */     if (ctor == null) {
/* 370 */       throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
/*     */     }
/*     */     try {
/* 373 */       return (T)ctor.newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 375 */       unwrapAndThrowAsIAE(e, "Failed to instantiate class " + cls.getName() + ", problem: " + e.getMessage()); }
/* 376 */     return null;
/*     */   }
/*     */   
/*     */   public static <T> Constructor<T> findConstructor(Class<T> cls, boolean canFixAccess)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 384 */       Constructor<T> ctor = cls.getDeclaredConstructor(new Class[0]);
/* 385 */       if (canFixAccess) {
/* 386 */         checkAndFixAccess(ctor);
/*     */ 
/*     */       }
/* 389 */       else if (!Modifier.isPublic(ctor.getModifiers())) {
/* 390 */         throw new IllegalArgumentException("Default constructor for " + cls.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
/*     */       }
/*     */       
/* 393 */       return ctor;
/*     */     }
/*     */     catch (NoSuchMethodException e) {}catch (Exception e)
/*     */     {
/* 397 */       unwrapAndThrowAsIAE(e, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e.getMessage());
/*     */     }
/* 399 */     return null;
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
/*     */   public static Object defaultValue(Class<?> cls)
/*     */   {
/* 414 */     if (cls == Integer.TYPE) {
/* 415 */       return Integer.valueOf(0);
/*     */     }
/* 417 */     if (cls == Long.TYPE) {
/* 418 */       return Long.valueOf(0L);
/*     */     }
/* 420 */     if (cls == Boolean.TYPE) {
/* 421 */       return Boolean.FALSE;
/*     */     }
/* 423 */     if (cls == Double.TYPE) {
/* 424 */       return Double.valueOf(0.0D);
/*     */     }
/* 426 */     if (cls == Float.TYPE) {
/* 427 */       return Float.valueOf(0.0F);
/*     */     }
/* 429 */     if (cls == Byte.TYPE) {
/* 430 */       return Byte.valueOf((byte)0);
/*     */     }
/* 432 */     if (cls == Short.TYPE) {
/* 433 */       return Short.valueOf((short)0);
/*     */     }
/* 435 */     if (cls == Character.TYPE) {
/* 436 */       return Character.valueOf('\000');
/*     */     }
/* 438 */     throw new IllegalArgumentException("Class " + cls.getName() + " is not a primitive type");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> wrapperType(Class<?> primitiveType)
/*     */   {
/* 447 */     if (primitiveType == Integer.TYPE) {
/* 448 */       return Integer.class;
/*     */     }
/* 450 */     if (primitiveType == Long.TYPE) {
/* 451 */       return Long.class;
/*     */     }
/* 453 */     if (primitiveType == Boolean.TYPE) {
/* 454 */       return Boolean.class;
/*     */     }
/* 456 */     if (primitiveType == Double.TYPE) {
/* 457 */       return Double.class;
/*     */     }
/* 459 */     if (primitiveType == Float.TYPE) {
/* 460 */       return Float.class;
/*     */     }
/* 462 */     if (primitiveType == Byte.TYPE) {
/* 463 */       return Byte.class;
/*     */     }
/* 465 */     if (primitiveType == Short.TYPE) {
/* 466 */       return Short.class;
/*     */     }
/* 468 */     if (primitiveType == Character.TYPE) {
/* 469 */       return Character.class;
/*     */     }
/* 471 */     throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
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
/*     */   public static void checkAndFixAccess(Member member)
/*     */   {
/* 489 */     AccessibleObject ao = (AccessibleObject)member;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 497 */       ao.setAccessible(true);
/*     */ 
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */ 
/* 503 */       if (!ao.isAccessible()) {
/* 504 */         Class<?> declClass = member.getDeclaringClass();
/* 505 */         throw new IllegalArgumentException("Can not access " + member + " (from class " + declClass.getName() + "; failed to set access: " + se.getMessage());
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<? extends Enum<?>> findEnumType(EnumSet<?> s)
/*     */   {
/* 526 */     if (!s.isEmpty()) {
/* 527 */       return findEnumType((Enum)s.iterator().next());
/*     */     }
/*     */     
/* 530 */     return EnumTypeLocator.instance.enumTypeFor(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<? extends Enum<?>> findEnumType(EnumMap<?, ?> m)
/*     */   {
/* 541 */     if (!m.isEmpty()) {
/* 542 */       return findEnumType((Enum)m.keySet().iterator().next());
/*     */     }
/*     */     
/* 545 */     return EnumTypeLocator.instance.enumTypeFor(m);
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
/*     */   public static Class<? extends Enum<?>> findEnumType(Enum<?> en)
/*     */   {
/* 558 */     Class<?> ec = en.getClass();
/* 559 */     if (ec.getSuperclass() != Enum.class) {
/* 560 */       ec = ec.getSuperclass();
/*     */     }
/* 562 */     return ec;
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
/*     */   public static Class<? extends Enum<?>> findEnumType(Class<?> cls)
/*     */   {
/* 575 */     if (cls.getSuperclass() != Enum.class) {
/* 576 */       cls = cls.getSuperclass();
/*     */     }
/* 578 */     return cls;
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
/*     */   public static boolean isJacksonStdImpl(Object impl)
/*     */   {
/* 595 */     return (impl != null) && (isJacksonStdImpl(impl.getClass()));
/*     */   }
/*     */   
/*     */   public static boolean isJacksonStdImpl(Class<?> implClass) {
/* 599 */     return implClass.getAnnotation(JacksonStdImpl.class) != null;
/*     */   }
/*     */   
/*     */   public static boolean isBogusClass(Class<?> cls) {
/* 603 */     return (cls == Void.class) || (cls == Void.TYPE) || (cls == NoClass.class);
/*     */   }
/*     */   
/*     */   public static boolean isNonStaticInnerClass(Class<?> cls)
/*     */   {
/* 608 */     return (cls.getEnclosingClass() != null) && (!Modifier.isStatic(cls.getModifiers()));
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
/*     */   private static class EnumTypeLocator
/*     */   {
/* 624 */     static final EnumTypeLocator instance = new EnumTypeLocator();
/*     */     
/*     */     private final Field enumSetTypeField;
/*     */     
/*     */     private final Field enumMapTypeField;
/*     */     
/*     */ 
/*     */     private EnumTypeLocator()
/*     */     {
/* 633 */       this.enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);
/* 634 */       this.enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
/*     */     }
/*     */     
/*     */ 
/*     */     public Class<? extends Enum<?>> enumTypeFor(EnumSet<?> set)
/*     */     {
/* 640 */       if (this.enumSetTypeField != null) {
/* 641 */         return (Class)get(set, this.enumSetTypeField);
/*     */       }
/* 643 */       throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
/*     */     }
/*     */     
/*     */ 
/*     */     public Class<? extends Enum<?>> enumTypeFor(EnumMap<?, ?> set)
/*     */     {
/* 649 */       if (this.enumMapTypeField != null) {
/* 650 */         return (Class)get(set, this.enumMapTypeField);
/*     */       }
/* 652 */       throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
/*     */     }
/*     */     
/*     */     private Object get(Object bean, Field field)
/*     */     {
/*     */       try {
/* 658 */         return field.get(bean);
/*     */       } catch (Exception e) {
/* 660 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     private static Field locateField(Class<?> fromClass, String expectedName, Class<?> type)
/*     */     {
/* 666 */       Field found = null;
/*     */       
/* 668 */       Field[] fields = fromClass.getDeclaredFields();
/* 669 */       for (Field f : fields) {
/* 670 */         if ((expectedName.equals(f.getName())) && (f.getType() == type)) {
/* 671 */           found = f;
/* 672 */           break;
/*     */         }
/*     */       }
/*     */       
/* 676 */       if (found == null) {
/* 677 */         for (Field f : fields) {
/* 678 */           if (f.getType() == type)
/*     */           {
/* 680 */             if (found != null) return null;
/* 681 */             found = f;
/*     */           }
/*     */         }
/*     */       }
/* 685 */       if (found != null) {
/*     */         try {
/* 687 */           found.setAccessible(true);
/*     */         } catch (Throwable t) {}
/*     */       }
/* 690 */       return found;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\ClassUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */