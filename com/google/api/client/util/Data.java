/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class Data
/*     */ {
/*  47 */   public static final Boolean NULL_BOOLEAN = new Boolean(true);
/*     */   
/*     */ 
/*  50 */   public static final String NULL_STRING = new String();
/*     */   
/*     */ 
/*  53 */   public static final Character NULL_CHARACTER = new Character('\000');
/*     */   
/*     */ 
/*  56 */   public static final Byte NULL_BYTE = new Byte((byte)0);
/*     */   
/*     */ 
/*  59 */   public static final Short NULL_SHORT = new Short((short)0);
/*     */   
/*     */ 
/*  62 */   public static final Integer NULL_INTEGER = new Integer(0);
/*     */   
/*     */ 
/*  65 */   public static final Float NULL_FLOAT = new Float(0.0F);
/*     */   
/*     */ 
/*  68 */   public static final Long NULL_LONG = new Long(0L);
/*     */   
/*     */ 
/*  71 */   public static final Double NULL_DOUBLE = new Double(0.0D);
/*     */   
/*     */ 
/*  74 */   public static final BigInteger NULL_BIG_INTEGER = new BigInteger("0");
/*     */   
/*     */ 
/*  77 */   public static final BigDecimal NULL_BIG_DECIMAL = new BigDecimal("0");
/*     */   
/*     */ 
/*  80 */   public static final DateTime NULL_DATE_TIME = new DateTime(0L);
/*     */   
/*     */ 
/*  83 */   private static final ConcurrentHashMap<Class<?>, Object> NULL_CACHE = new ConcurrentHashMap();
/*     */   
/*     */   static
/*     */   {
/*  87 */     NULL_CACHE.put(Boolean.class, NULL_BOOLEAN);
/*  88 */     NULL_CACHE.put(String.class, NULL_STRING);
/*  89 */     NULL_CACHE.put(Character.class, NULL_CHARACTER);
/*  90 */     NULL_CACHE.put(Byte.class, NULL_BYTE);
/*  91 */     NULL_CACHE.put(Short.class, NULL_SHORT);
/*  92 */     NULL_CACHE.put(Integer.class, NULL_INTEGER);
/*  93 */     NULL_CACHE.put(Float.class, NULL_FLOAT);
/*  94 */     NULL_CACHE.put(Long.class, NULL_LONG);
/*  95 */     NULL_CACHE.put(Double.class, NULL_DOUBLE);
/*  96 */     NULL_CACHE.put(BigInteger.class, NULL_BIG_INTEGER);
/*  97 */     NULL_CACHE.put(BigDecimal.class, NULL_BIG_DECIMAL);
/*  98 */     NULL_CACHE.put(DateTime.class, NULL_DATE_TIME);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T nullOf(Class<?> objClass)
/*     */   {
/* 110 */     Object result = NULL_CACHE.get(objClass);
/* 111 */     if (result == null) {
/* 112 */       synchronized (NULL_CACHE) {
/* 113 */         result = NULL_CACHE.get(objClass);
/* 114 */         if (result == null) {
/* 115 */           if (objClass.isArray())
/*     */           {
/* 117 */             int dims = 0;
/* 118 */             Class<?> componentType = objClass;
/*     */             do {
/* 120 */               componentType = componentType.getComponentType();
/* 121 */               dims++;
/* 122 */             } while (componentType.isArray());
/* 123 */             result = Array.newInstance(componentType, new int[dims]);
/* 124 */           } else if (objClass.isEnum())
/*     */           {
/* 126 */             FieldInfo fieldInfo = ClassInfo.of(objClass).getFieldInfo(null);
/* 127 */             Preconditions.checkNotNull(fieldInfo, "enum missing constant with @NullValue annotation: %s", new Object[] { objClass });
/*     */             
/*     */ 
/* 130 */             Enum e = fieldInfo.enumValue();
/* 131 */             result = e;
/*     */           }
/*     */           else {
/* 134 */             result = Types.newInstance(objClass);
/*     */           }
/* 136 */           NULL_CACHE.put(objClass, result);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 141 */     T tResult = (T)result;
/* 142 */     return tResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isNull(Object object)
/*     */   {
/* 154 */     return (object != null) && (object == NULL_CACHE.get(object.getClass()));
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
/*     */   public static Map<String, Object> mapOf(Object data)
/*     */   {
/* 177 */     if ((data == null) || (isNull(data))) {
/* 178 */       return Collections.emptyMap();
/*     */     }
/* 180 */     if ((data instanceof Map))
/*     */     {
/* 182 */       Map<String, Object> result = (Map)data;
/* 183 */       return result;
/*     */     }
/* 185 */     Map<String, Object> result = new DataMap(data, false);
/* 186 */     return result;
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
/*     */   public static <T> T clone(T data)
/*     */   {
/* 207 */     if ((data == null) || (isPrimitive(data.getClass()))) {
/* 208 */       return data;
/*     */     }
/* 210 */     if ((data instanceof GenericData)) {
/* 211 */       return ((GenericData)data).clone();
/*     */     }
/*     */     
/* 214 */     Class<?> dataClass = data.getClass();
/* 215 */     T copy; T copy; if (dataClass.isArray()) {
/* 216 */       copy = Array.newInstance(dataClass.getComponentType(), Array.getLength(data)); } else { T copy;
/* 217 */       if ((data instanceof ArrayMap)) {
/* 218 */         copy = ((ArrayMap)data).clone();
/*     */       } else
/* 220 */         copy = Types.newInstance(dataClass);
/*     */     }
/* 222 */     deepCopy(data, copy);
/* 223 */     return copy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void deepCopy(Object src, Object dest)
/*     */   {
/* 256 */     Class<?> srcClass = src.getClass();
/* 257 */     Preconditions.checkArgument(srcClass == dest.getClass());
/* 258 */     int index; Map<String, Object> destMap; if (srcClass.isArray())
/*     */     {
/* 260 */       Preconditions.checkArgument(Array.getLength(src) == Array.getLength(dest));
/* 261 */       index = 0;
/* 262 */       for (Object value : Types.iterableOf(src))
/* 263 */         Array.set(dest, index++, clone(value));
/*     */     } else { Collection<Object> destCollection;
/* 265 */       if (Collection.class.isAssignableFrom(srcClass))
/*     */       {
/*     */ 
/* 268 */         Collection<Object> srcCollection = (Collection)src;
/* 269 */         if (ArrayList.class.isAssignableFrom(srcClass))
/*     */         {
/* 271 */           ArrayList<Object> destArrayList = (ArrayList)dest;
/* 272 */           destArrayList.ensureCapacity(srcCollection.size());
/*     */         }
/*     */         
/* 275 */         destCollection = (Collection)dest;
/* 276 */         for (Object srcValue : srcCollection) {
/* 277 */           destCollection.add(clone(srcValue));
/*     */         }
/*     */       }
/*     */       else {
/* 281 */         boolean isGenericData = GenericData.class.isAssignableFrom(srcClass);
/* 282 */         ClassInfo classInfo; if ((isGenericData) || (!Map.class.isAssignableFrom(srcClass))) {
/* 283 */           classInfo = isGenericData ? ((GenericData)src).classInfo : ClassInfo.of(srcClass);
/*     */           
/* 285 */           for (String fieldName : classInfo.names) {
/* 286 */             FieldInfo fieldInfo = classInfo.getFieldInfo(fieldName);
/*     */             
/* 288 */             if (!fieldInfo.isFinal())
/*     */             {
/* 290 */               if ((!isGenericData) || (!fieldInfo.isPrimitive())) {
/* 291 */                 Object srcValue = fieldInfo.getValue(src);
/* 292 */                 if (srcValue != null) {
/* 293 */                   fieldInfo.setValue(dest, clone(srcValue));
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 298 */         } else if (ArrayMap.class.isAssignableFrom(srcClass))
/*     */         {
/*     */ 
/* 301 */           ArrayMap<Object, Object> destMap = (ArrayMap)dest;
/*     */           
/* 303 */           ArrayMap<Object, Object> srcMap = (ArrayMap)src;
/* 304 */           int size = srcMap.size();
/* 305 */           for (int i = 0; i < size; i++) {
/* 306 */             Object srcValue = srcMap.getValue(i);
/* 307 */             destMap.set(i, clone(srcValue));
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 312 */           destMap = (Map)dest;
/*     */           
/* 314 */           Map<String, Object> srcMap = (Map)src;
/* 315 */           for (Map.Entry<String, Object> srcEntry : srcMap.entrySet()) {
/* 316 */             destMap.put(srcEntry.getKey(), clone(srcEntry.getValue()));
/*     */           }
/*     */         }
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
/*     */   public static boolean isPrimitive(Type type)
/*     */   {
/* 338 */     if ((type instanceof WildcardType)) {
/* 339 */       type = Types.getBound((WildcardType)type);
/*     */     }
/* 341 */     if (!(type instanceof Class)) {
/* 342 */       return false;
/*     */     }
/* 344 */     Class<?> typeClass = (Class)type;
/* 345 */     return (typeClass.isPrimitive()) || (typeClass == Character.class) || (typeClass == String.class) || (typeClass == Integer.class) || (typeClass == Long.class) || (typeClass == Short.class) || (typeClass == Byte.class) || (typeClass == Float.class) || (typeClass == Double.class) || (typeClass == BigInteger.class) || (typeClass == BigDecimal.class) || (typeClass == DateTime.class) || (typeClass == Boolean.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isValueOfPrimitiveType(Object fieldValue)
/*     */   {
/* 357 */     return (fieldValue == null) || (isPrimitive(fieldValue.getClass()));
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
/*     */   public static Object parsePrimitiveValue(Type type, String stringValue)
/*     */   {
/* 392 */     Class<?> primitiveClass = (type instanceof Class) ? (Class)type : null;
/* 393 */     if ((type == null) || (primitiveClass != null)) {
/* 394 */       if (primitiveClass == Void.class) {
/* 395 */         return null;
/*     */       }
/* 397 */       if ((stringValue == null) || (primitiveClass == null) || (primitiveClass.isAssignableFrom(String.class)))
/*     */       {
/* 399 */         return stringValue;
/*     */       }
/* 401 */       if ((primitiveClass == Character.class) || (primitiveClass == Character.TYPE)) {
/* 402 */         if (stringValue.length() != 1) {
/* 403 */           String str = String.valueOf(String.valueOf(primitiveClass));throw new IllegalArgumentException(37 + str.length() + "expected type Character/char but got " + str);
/*     */         }
/*     */         
/* 406 */         return Character.valueOf(stringValue.charAt(0));
/*     */       }
/* 408 */       if ((primitiveClass == Boolean.class) || (primitiveClass == Boolean.TYPE)) {
/* 409 */         return Boolean.valueOf(stringValue);
/*     */       }
/* 411 */       if ((primitiveClass == Byte.class) || (primitiveClass == Byte.TYPE)) {
/* 412 */         return Byte.valueOf(stringValue);
/*     */       }
/* 414 */       if ((primitiveClass == Short.class) || (primitiveClass == Short.TYPE)) {
/* 415 */         return Short.valueOf(stringValue);
/*     */       }
/* 417 */       if ((primitiveClass == Integer.class) || (primitiveClass == Integer.TYPE)) {
/* 418 */         return Integer.valueOf(stringValue);
/*     */       }
/* 420 */       if ((primitiveClass == Long.class) || (primitiveClass == Long.TYPE)) {
/* 421 */         return Long.valueOf(stringValue);
/*     */       }
/* 423 */       if ((primitiveClass == Float.class) || (primitiveClass == Float.TYPE)) {
/* 424 */         return Float.valueOf(stringValue);
/*     */       }
/* 426 */       if ((primitiveClass == Double.class) || (primitiveClass == Double.TYPE)) {
/* 427 */         return Double.valueOf(stringValue);
/*     */       }
/* 429 */       if (primitiveClass == DateTime.class) {
/* 430 */         return DateTime.parseRfc3339(stringValue);
/*     */       }
/* 432 */       if (primitiveClass == BigInteger.class) {
/* 433 */         return new BigInteger(stringValue);
/*     */       }
/* 435 */       if (primitiveClass == BigDecimal.class) {
/* 436 */         return new BigDecimal(stringValue);
/*     */       }
/* 438 */       if (primitiveClass.isEnum())
/*     */       {
/* 440 */         result = ClassInfo.of(primitiveClass).getFieldInfo(stringValue).enumValue();
/* 441 */         return result;
/*     */       }
/*     */     }
/* 444 */     Object result = String.valueOf(String.valueOf(type));throw new IllegalArgumentException(35 + ((String)result).length() + "expected primitive class, but got: " + (String)result);
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
/*     */   public static Collection<Object> newCollectionInstance(Type type)
/*     */   {
/* 465 */     if ((type instanceof WildcardType)) {
/* 466 */       type = Types.getBound((WildcardType)type);
/*     */     }
/* 468 */     if ((type instanceof ParameterizedType)) {
/* 469 */       type = ((ParameterizedType)type).getRawType();
/*     */     }
/* 471 */     Class<?> collectionClass = (type instanceof Class) ? (Class)type : null;
/* 472 */     if ((type == null) || ((type instanceof GenericArrayType)) || ((collectionClass != null) && ((collectionClass.isArray()) || (collectionClass.isAssignableFrom(ArrayList.class)))))
/*     */     {
/* 474 */       return new ArrayList();
/*     */     }
/* 476 */     if (collectionClass == null) {
/* 477 */       String str = String.valueOf(String.valueOf(type));throw new IllegalArgumentException(39 + str.length() + "unable to create new instance of type: " + str);
/*     */     }
/* 479 */     if (collectionClass.isAssignableFrom(HashSet.class)) {
/* 480 */       return new HashSet();
/*     */     }
/* 482 */     if (collectionClass.isAssignableFrom(TreeSet.class)) {
/* 483 */       return new TreeSet();
/*     */     }
/*     */     
/* 486 */     Object result = (Collection)Types.newInstance(collectionClass);
/* 487 */     return (Collection<Object>)result;
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
/*     */   public static Map<String, Object> newMapInstance(Class<?> mapClass)
/*     */   {
/* 506 */     if ((mapClass == null) || (mapClass.isAssignableFrom(ArrayMap.class))) {
/* 507 */       return ArrayMap.create();
/*     */     }
/* 509 */     if (mapClass.isAssignableFrom(TreeMap.class)) {
/* 510 */       return new TreeMap();
/*     */     }
/*     */     
/* 513 */     Map<String, Object> result = (Map)Types.newInstance(mapClass);
/* 514 */     return result;
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
/*     */   public static Type resolveWildcardTypeOrTypeVariable(List<Type> context, Type type)
/*     */   {
/* 530 */     if ((type instanceof WildcardType)) {
/* 531 */       type = Types.getBound((WildcardType)type);
/*     */     }
/*     */     
/* 534 */     while ((type instanceof TypeVariable))
/*     */     {
/* 536 */       Type resolved = Types.resolveTypeVariable(context, (TypeVariable)type);
/* 537 */       if (resolved != null) {
/* 538 */         type = resolved;
/*     */       }
/*     */       
/* 541 */       if ((type instanceof TypeVariable)) {
/* 542 */         type = ((TypeVariable)type).getBounds()[0];
/*     */       }
/*     */     }
/*     */     
/* 546 */     return type;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Data.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */