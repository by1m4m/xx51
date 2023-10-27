/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.eclipse.jetty.util.annotation.Name;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public class TypeUtil
/*     */ {
/*  55 */   private static final Logger LOG = Log.getLogger(TypeUtil.class);
/*  56 */   public static final Class<?>[] NO_ARGS = new Class[0];
/*     */   
/*     */   public static final int CR = 13;
/*     */   
/*     */   public static final int LF = 10;
/*  61 */   private static final HashMap<String, Class<?>> name2Class = new HashMap();
/*     */   private static final HashMap<Class<?>, String> class2Name;
/*     */   
/*  64 */   static { name2Class.put("boolean", Boolean.TYPE);
/*  65 */     name2Class.put("byte", Byte.TYPE);
/*  66 */     name2Class.put("char", Character.TYPE);
/*  67 */     name2Class.put("double", Double.TYPE);
/*  68 */     name2Class.put("float", Float.TYPE);
/*  69 */     name2Class.put("int", Integer.TYPE);
/*  70 */     name2Class.put("long", Long.TYPE);
/*  71 */     name2Class.put("short", Short.TYPE);
/*  72 */     name2Class.put("void", Void.TYPE);
/*     */     
/*  74 */     name2Class.put("java.lang.Boolean.TYPE", Boolean.TYPE);
/*  75 */     name2Class.put("java.lang.Byte.TYPE", Byte.TYPE);
/*  76 */     name2Class.put("java.lang.Character.TYPE", Character.TYPE);
/*  77 */     name2Class.put("java.lang.Double.TYPE", Double.TYPE);
/*  78 */     name2Class.put("java.lang.Float.TYPE", Float.TYPE);
/*  79 */     name2Class.put("java.lang.Integer.TYPE", Integer.TYPE);
/*  80 */     name2Class.put("java.lang.Long.TYPE", Long.TYPE);
/*  81 */     name2Class.put("java.lang.Short.TYPE", Short.TYPE);
/*  82 */     name2Class.put("java.lang.Void.TYPE", Void.TYPE);
/*     */     
/*  84 */     name2Class.put("java.lang.Boolean", Boolean.class);
/*  85 */     name2Class.put("java.lang.Byte", Byte.class);
/*  86 */     name2Class.put("java.lang.Character", Character.class);
/*  87 */     name2Class.put("java.lang.Double", Double.class);
/*  88 */     name2Class.put("java.lang.Float", Float.class);
/*  89 */     name2Class.put("java.lang.Integer", Integer.class);
/*  90 */     name2Class.put("java.lang.Long", Long.class);
/*  91 */     name2Class.put("java.lang.Short", Short.class);
/*     */     
/*  93 */     name2Class.put("Boolean", Boolean.class);
/*  94 */     name2Class.put("Byte", Byte.class);
/*  95 */     name2Class.put("Character", Character.class);
/*  96 */     name2Class.put("Double", Double.class);
/*  97 */     name2Class.put("Float", Float.class);
/*  98 */     name2Class.put("Integer", Integer.class);
/*  99 */     name2Class.put("Long", Long.class);
/* 100 */     name2Class.put("Short", Short.class);
/*     */     
/* 102 */     name2Class.put(null, Void.TYPE);
/* 103 */     name2Class.put("string", String.class);
/* 104 */     name2Class.put("String", String.class);
/* 105 */     name2Class.put("java.lang.String", String.class);
/*     */     
/*     */ 
/*     */ 
/* 109 */     class2Name = new HashMap();
/*     */     
/*     */ 
/* 112 */     class2Name.put(Boolean.TYPE, "boolean");
/* 113 */     class2Name.put(Byte.TYPE, "byte");
/* 114 */     class2Name.put(Character.TYPE, "char");
/* 115 */     class2Name.put(Double.TYPE, "double");
/* 116 */     class2Name.put(Float.TYPE, "float");
/* 117 */     class2Name.put(Integer.TYPE, "int");
/* 118 */     class2Name.put(Long.TYPE, "long");
/* 119 */     class2Name.put(Short.TYPE, "short");
/* 120 */     class2Name.put(Void.TYPE, "void");
/*     */     
/* 122 */     class2Name.put(Boolean.class, "java.lang.Boolean");
/* 123 */     class2Name.put(Byte.class, "java.lang.Byte");
/* 124 */     class2Name.put(Character.class, "java.lang.Character");
/* 125 */     class2Name.put(Double.class, "java.lang.Double");
/* 126 */     class2Name.put(Float.class, "java.lang.Float");
/* 127 */     class2Name.put(Integer.class, "java.lang.Integer");
/* 128 */     class2Name.put(Long.class, "java.lang.Long");
/* 129 */     class2Name.put(Short.class, "java.lang.Short");
/*     */     
/* 131 */     class2Name.put(null, "void");
/* 132 */     class2Name.put(String.class, "java.lang.String");
/*     */     
/*     */ 
/*     */ 
/* 136 */     class2Value = new HashMap();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 141 */       Class<?>[] s = { String.class };
/*     */       
/* 143 */       class2Value.put(Boolean.TYPE, Boolean.class
/* 144 */         .getMethod("valueOf", s));
/* 145 */       class2Value.put(Byte.TYPE, Byte.class
/* 146 */         .getMethod("valueOf", s));
/* 147 */       class2Value.put(Double.TYPE, Double.class
/* 148 */         .getMethod("valueOf", s));
/* 149 */       class2Value.put(Float.TYPE, Float.class
/* 150 */         .getMethod("valueOf", s));
/* 151 */       class2Value.put(Integer.TYPE, Integer.class
/* 152 */         .getMethod("valueOf", s));
/* 153 */       class2Value.put(Long.TYPE, Long.class
/* 154 */         .getMethod("valueOf", s));
/* 155 */       class2Value.put(Short.TYPE, Short.class
/* 156 */         .getMethod("valueOf", s));
/*     */       
/* 158 */       class2Value.put(Boolean.class, Boolean.class
/* 159 */         .getMethod("valueOf", s));
/* 160 */       class2Value.put(Byte.class, Byte.class
/* 161 */         .getMethod("valueOf", s));
/* 162 */       class2Value.put(Double.class, Double.class
/* 163 */         .getMethod("valueOf", s));
/* 164 */       class2Value.put(Float.class, Float.class
/* 165 */         .getMethod("valueOf", s));
/* 166 */       class2Value.put(Integer.class, Integer.class
/* 167 */         .getMethod("valueOf", s));
/* 168 */       class2Value.put(Long.class, Long.class
/* 169 */         .getMethod("valueOf", s));
/* 170 */       class2Value.put(Short.class, Short.class
/* 171 */         .getMethod("valueOf", s));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 175 */       throw new Error(e);
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
/*     */   public static <T> List<T> asList(T[] a)
/*     */   {
/* 189 */     if (a == null)
/* 190 */       return Collections.emptyList();
/* 191 */     return Arrays.asList(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> fromName(String name)
/*     */   {
/* 201 */     return (Class)name2Class.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toName(Class<?> type)
/*     */   {
/* 211 */     return (String)class2Name.get(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object valueOf(Class<?> type, String value)
/*     */   {
/*     */     try
/*     */     {
/* 224 */       if (type.equals(String.class)) {
/* 225 */         return value;
/*     */       }
/* 227 */       Method m = (Method)class2Value.get(type);
/* 228 */       if (m != null) {
/* 229 */         return m.invoke(null, new Object[] { value });
/*     */       }
/* 231 */       if ((type.equals(Character.TYPE)) || 
/* 232 */         (type.equals(Character.class))) {
/* 233 */         return Character.valueOf(value.charAt(0));
/*     */       }
/* 235 */       Constructor<?> c = type.getConstructor(new Class[] { String.class });
/* 236 */       return c.newInstance(new Object[] { value });
/*     */     }
/*     */     catch (NoSuchMethodException|IllegalAccessException|InstantiationException x)
/*     */     {
/* 240 */       LOG.ignore(x);
/*     */     }
/*     */     catch (InvocationTargetException x)
/*     */     {
/* 244 */       if ((x.getTargetException() instanceof Error))
/* 245 */         throw ((Error)x.getTargetException());
/* 246 */       LOG.ignore(x);
/*     */     }
/* 248 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object valueOf(String type, String value)
/*     */   {
/* 259 */     return valueOf(fromName(type), value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final HashMap<Class<?>, Method> class2Value;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int parseInt(String s, int offset, int length, int base)
/*     */     throws NumberFormatException
/*     */   {
/* 275 */     int value = 0;
/*     */     
/* 277 */     if (length < 0) {
/* 278 */       length = s.length() - offset;
/*     */     }
/* 280 */     for (int i = 0; i < length; i++)
/*     */     {
/* 282 */       char c = s.charAt(offset + i);
/*     */       
/* 284 */       int digit = convertHexDigit(c);
/* 285 */       if ((digit < 0) || (digit >= base))
/* 286 */         throw new NumberFormatException(s.substring(offset, offset + length));
/* 287 */       value = value * base + digit;
/*     */     }
/* 289 */     return value;
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
/*     */   public static int parseInt(byte[] b, int offset, int length, int base)
/*     */     throws NumberFormatException
/*     */   {
/* 305 */     int value = 0;
/*     */     
/* 307 */     if (length < 0) {
/* 308 */       length = b.length - offset;
/*     */     }
/* 310 */     for (int i = 0; i < length; i++)
/*     */     {
/* 312 */       char c = (char)(0xFF & b[(offset + i)]);
/*     */       
/* 314 */       int digit = c - '0';
/* 315 */       if ((digit < 0) || (digit >= base) || (digit >= 10))
/*     */       {
/* 317 */         digit = '\n' + c - 65;
/* 318 */         if ((digit < 10) || (digit >= base))
/* 319 */           digit = '\n' + c - 97;
/*     */       }
/* 321 */       if ((digit < 0) || (digit >= base))
/* 322 */         throw new NumberFormatException(new String(b, offset, length));
/* 323 */       value = value * base + digit;
/*     */     }
/* 325 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte[] parseBytes(String s, int base)
/*     */   {
/* 331 */     byte[] bytes = new byte[s.length() / 2];
/* 332 */     for (int i = 0; i < s.length(); i += 2)
/* 333 */       bytes[(i / 2)] = ((byte)parseInt(s, i, 2, base));
/* 334 */     return bytes;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String toString(byte[] bytes, int base)
/*     */   {
/* 340 */     StringBuilder buf = new StringBuilder();
/* 341 */     for (byte b : bytes)
/*     */     {
/* 343 */       int bi = 0xFF & b;
/* 344 */       int c = 48 + bi / base % base;
/* 345 */       if (c > 57)
/* 346 */         c = 97 + (c - 48 - 10);
/* 347 */       buf.append((char)c);
/* 348 */       c = 48 + bi % base;
/* 349 */       if (c > 57)
/* 350 */         c = 97 + (c - 48 - 10);
/* 351 */       buf.append((char)c);
/*     */     }
/* 353 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte convertHexDigit(byte c)
/*     */   {
/* 363 */     byte b = (byte)((c & 0x1F) + (c >> 6) * 25 - 16);
/* 364 */     if ((b < 0) || (b > 15))
/* 365 */       throw new NumberFormatException("!hex " + c);
/* 366 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int convertHexDigit(char c)
/*     */   {
/* 376 */     int d = (c & 0x1F) + (c >> '\006') * 25 - 16;
/* 377 */     if ((d < 0) || (d > 15))
/* 378 */       throw new NumberFormatException("!hex " + c);
/* 379 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int convertHexDigit(int c)
/*     */   {
/* 389 */     int d = (c & 0x1F) + (c >> 6) * 25 - 16;
/* 390 */     if ((d < 0) || (d > 15))
/* 391 */       throw new NumberFormatException("!hex " + c);
/* 392 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void toHex(byte b, Appendable buf)
/*     */   {
/*     */     try
/*     */     {
/* 400 */       int d = 0xF & (0xF0 & b) >> 4;
/* 401 */       buf.append((char)((d > 9 ? 55 : 48) + d));
/* 402 */       d = 0xF & b;
/* 403 */       buf.append((char)((d > 9 ? 55 : 48) + d));
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 407 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void toHex(int value, Appendable buf)
/*     */     throws IOException
/*     */   {
/* 414 */     int d = 0xF & (0xF0000000 & value) >> 28;
/* 415 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 416 */     d = 0xF & (0xF000000 & value) >> 24;
/* 417 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 418 */     d = 0xF & (0xF00000 & value) >> 20;
/* 419 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 420 */     d = 0xF & (0xF0000 & value) >> 16;
/* 421 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 422 */     d = 0xF & (0xF000 & value) >> 12;
/* 423 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 424 */     d = 0xF & (0xF00 & value) >> 8;
/* 425 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 426 */     d = 0xF & (0xF0 & value) >> 4;
/* 427 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/* 428 */     d = 0xF & value;
/* 429 */     buf.append((char)((d > 9 ? 55 : 48) + d));
/*     */     
/* 431 */     Integer.toString(0, 36);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void toHex(long value, Appendable buf)
/*     */     throws IOException
/*     */   {
/* 438 */     toHex((int)(value >> 32), buf);
/* 439 */     toHex((int)value, buf);
/*     */   }
/*     */   
/*     */ 
/*     */   public static String toHexString(byte b)
/*     */   {
/* 445 */     return toHexString(new byte[] { b }, 0, 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public static String toHexString(byte[] b)
/*     */   {
/* 451 */     return toHexString(b, 0, b.length);
/*     */   }
/*     */   
/*     */ 
/*     */   public static String toHexString(byte[] b, int offset, int length)
/*     */   {
/* 457 */     StringBuilder buf = new StringBuilder();
/* 458 */     for (int i = offset; i < offset + length; i++)
/*     */     {
/* 460 */       int bi = 0xFF & b[i];
/* 461 */       int c = 48 + bi / 16 % 16;
/* 462 */       if (c > 57)
/* 463 */         c = 65 + (c - 48 - 10);
/* 464 */       buf.append((char)c);
/* 465 */       c = 48 + bi % 16;
/* 466 */       if (c > 57)
/* 467 */         c = 97 + (c - 48 - 10);
/* 468 */       buf.append((char)c);
/*     */     }
/* 470 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte[] fromHexString(String s)
/*     */   {
/* 476 */     if (s.length() % 2 != 0)
/* 477 */       throw new IllegalArgumentException(s);
/* 478 */     byte[] array = new byte[s.length() / 2];
/* 479 */     for (int i = 0; i < array.length; i++)
/*     */     {
/* 481 */       int b = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
/* 482 */       array[i] = ((byte)(0xFF & b));
/*     */     }
/* 484 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void dump(Class<?> c)
/*     */   {
/* 490 */     System.err.println("Dump: " + c);
/* 491 */     dump(c.getClassLoader());
/*     */   }
/*     */   
/*     */   public static void dump(ClassLoader cl)
/*     */   {
/* 496 */     System.err.println("Dump Loaders:");
/* 497 */     while (cl != null)
/*     */     {
/* 499 */       System.err.println("  loader " + cl);
/* 500 */       cl = cl.getParent();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static Object call(Class<?> oClass, String methodName, Object obj, Object[] arg)
/*     */     throws InvocationTargetException, NoSuchMethodException
/*     */   {
/* 508 */     Objects.requireNonNull(oClass, "Class cannot be null");
/* 509 */     Objects.requireNonNull(methodName, "Method name cannot be null");
/* 510 */     if (StringUtil.isBlank(methodName))
/*     */     {
/* 512 */       throw new IllegalArgumentException("Method name cannot be blank");
/*     */     }
/*     */     
/*     */ 
/* 516 */     Method[] arrayOfMethod1 = oClass.getMethods();int i = arrayOfMethod1.length; for (Method localMethod1 = 0; localMethod1 < i; localMethod1++) { method = arrayOfMethod1[localMethod1];
/*     */       
/* 518 */       if (method.getName().equals(methodName))
/*     */       {
/* 520 */         if (method.getParameterCount() == arg.length)
/*     */         {
/* 522 */           if (Modifier.isStatic(method.getModifiers()) == (obj == null))
/*     */           {
/* 524 */             if ((obj != null) || (method.getDeclaringClass() == oClass))
/*     */             {
/*     */               try
/*     */               {
/*     */ 
/* 529 */                 return method.invoke(obj, arg);
/*     */               }
/*     */               catch (IllegalAccessException|IllegalArgumentException e)
/*     */               {
/* 533 */                 LOG.ignore(e);
/*     */               } } }
/*     */         }
/*     */       }
/*     */     }
/* 538 */     Object[] args_with_opts = null;
/*     */     
/* 540 */     Method[] arrayOfMethod2 = oClass.getMethods();localMethod1 = arrayOfMethod2.length; for (Method method = 0; method < localMethod1; method++) { Method method = arrayOfMethod2[method];
/*     */       
/* 542 */       if (method.getName().equals(methodName))
/*     */       {
/* 544 */         if (method.getParameterCount() == arg.length + 1)
/*     */         {
/* 546 */           if (method.getParameterTypes()[arg.length].isArray())
/*     */           {
/* 548 */             if (Modifier.isStatic(method.getModifiers()) == (obj == null))
/*     */             {
/* 550 */               if ((obj != null) || (method.getDeclaringClass() == oClass))
/*     */               {
/*     */ 
/* 553 */                 if (args_with_opts == null) {
/* 554 */                   args_with_opts = ArrayUtil.addToArray(arg, new Object[0], Object.class);
/*     */                 }
/*     */                 try {
/* 557 */                   return method.invoke(obj, args_with_opts);
/*     */                 }
/*     */                 catch (IllegalAccessException|IllegalArgumentException e)
/*     */                 {
/* 561 */                   LOG.ignore(e);
/*     */                 }
/*     */               } } } }
/*     */       }
/*     */     }
/* 566 */     throw new NoSuchMethodException(methodName);
/*     */   }
/*     */   
/*     */   public static Object construct(Class<?> klass, Object[] arguments) throws InvocationTargetException, NoSuchMethodException
/*     */   {
/* 571 */     Objects.requireNonNull(klass, "Class cannot be null");
/*     */     
/* 573 */     for (Constructor<?> constructor : klass.getConstructors())
/*     */     {
/* 575 */       if (arguments == null ? 
/*     */       
/*     */ 
/* 578 */         constructor.getParameterCount() == 0 : 
/*     */         
/*     */ 
/* 581 */         constructor.getParameterCount() == arguments.length)
/*     */       {
/*     */         try
/*     */         {
/*     */ 
/* 586 */           return constructor.newInstance(arguments);
/*     */         }
/*     */         catch (InstantiationException|IllegalAccessException|IllegalArgumentException e)
/*     */         {
/* 590 */           LOG.ignore(e);
/*     */         } }
/*     */     }
/* 593 */     throw new NoSuchMethodException("<init>");
/*     */   }
/*     */   
/*     */   public static Object construct(Class<?> klass, Object[] arguments, Map<String, Object> namedArgMap) throws InvocationTargetException, NoSuchMethodException
/*     */   {
/* 598 */     Objects.requireNonNull(klass, "Class cannot be null");
/* 599 */     Objects.requireNonNull(namedArgMap, "Named Argument Map cannot be null");
/*     */     
/* 601 */     for (Constructor<?> constructor : klass.getConstructors())
/*     */     {
/* 603 */       if (arguments == null ? 
/*     */       
/*     */ 
/* 606 */         constructor.getParameterCount() == 0 : 
/*     */         
/*     */ 
/* 609 */         constructor.getParameterCount() == arguments.length)
/*     */       {
/*     */         try
/*     */         {
/*     */ 
/* 614 */           Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
/*     */           
/* 616 */           if ((arguments == null) || (arguments.length == 0))
/*     */           {
/* 618 */             if (LOG.isDebugEnabled())
/* 619 */               LOG.debug("Constructor has no arguments", new Object[0]);
/* 620 */             return constructor.newInstance(arguments);
/*     */           }
/* 622 */           if ((parameterAnnotations == null) || (parameterAnnotations.length == 0))
/*     */           {
/* 624 */             if (LOG.isDebugEnabled())
/* 625 */               LOG.debug("Constructor has no parameter annotations", new Object[0]);
/* 626 */             return constructor.newInstance(arguments);
/*     */           }
/*     */           
/*     */ 
/* 630 */           Object[] swizzled = new Object[arguments.length];
/*     */           
/* 632 */           int count = 0;
/* 633 */           for (Annotation[] annotations : parameterAnnotations)
/*     */           {
/* 635 */             for (Annotation annotation : annotations)
/*     */             {
/* 637 */               if ((annotation instanceof Name))
/*     */               {
/* 639 */                 Name param = (Name)annotation;
/*     */                 
/* 641 */                 if (namedArgMap.containsKey(param.value()))
/*     */                 {
/* 643 */                   if (LOG.isDebugEnabled())
/* 644 */                     LOG.debug("placing named {} in position {}", new Object[] { param.value(), Integer.valueOf(count) });
/* 645 */                   swizzled[count] = namedArgMap.get(param.value());
/*     */                 }
/*     */                 else
/*     */                 {
/* 649 */                   if (LOG.isDebugEnabled())
/* 650 */                     LOG.debug("placing {} in position {}", new Object[] { arguments[count], Integer.valueOf(count) });
/* 651 */                   swizzled[count] = arguments[count];
/*     */                 }
/* 653 */                 count++;
/*     */ 
/*     */ 
/*     */               }
/* 657 */               else if (LOG.isDebugEnabled()) {
/* 658 */                 LOG.debug("passing on annotation {}", new Object[] { annotation });
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 663 */           return constructor.newInstance(swizzled);
/*     */ 
/*     */         }
/*     */         catch (InstantiationException|IllegalAccessException|IllegalArgumentException e)
/*     */         {
/*     */ 
/* 669 */           LOG.ignore(e);
/*     */         } }
/*     */     }
/* 672 */     throw new NoSuchMethodException("<init>");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isTrue(Object o)
/*     */   {
/* 682 */     if (o == null)
/* 683 */       return false;
/* 684 */     if ((o instanceof Boolean))
/* 685 */       return ((Boolean)o).booleanValue();
/* 686 */     return Boolean.parseBoolean(o.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFalse(Object o)
/*     */   {
/* 696 */     if (o == null)
/* 697 */       return false;
/* 698 */     if ((o instanceof Boolean))
/* 699 */       return !((Boolean)o).booleanValue();
/* 700 */     return "false".equalsIgnoreCase(o.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   public static URI getLocationOfClass(Class<?> clazz)
/*     */   {
/*     */     try
/*     */     {
/* 708 */       ProtectionDomain domain = clazz.getProtectionDomain();
/* 709 */       if (domain != null)
/*     */       {
/* 711 */         CodeSource source = domain.getCodeSource();
/* 712 */         if (source != null)
/*     */         {
/* 714 */           URL location = source.getLocation();
/*     */           
/* 716 */           if (location != null) {
/* 717 */             return location.toURI();
/*     */           }
/*     */         }
/*     */       }
/* 721 */       String resourceName = clazz.getName().replace('.', '/') + ".class";
/* 722 */       ClassLoader loader = clazz.getClassLoader();
/* 723 */       URL url = (loader == null ? ClassLoader.getSystemClassLoader() : loader).getResource(resourceName);
/* 724 */       if (url != null)
/*     */       {
/* 726 */         return URIUtil.getJarSource(url.toURI());
/*     */       }
/*     */     }
/*     */     catch (URISyntaxException e)
/*     */     {
/* 731 */       LOG.debug(e);
/*     */     }
/* 733 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\TypeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */