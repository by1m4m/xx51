/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumResolver<T extends Enum<T>>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<T> _enumClass;
/*     */   protected final T[] _enums;
/*     */   protected final HashMap<String, T> _enumsById;
/*     */   
/*     */   protected EnumResolver(Class<T> enumClass, T[] enums, HashMap<String, T> map)
/*     */   {
/*  24 */     this._enumClass = enumClass;
/*  25 */     this._enums = enums;
/*  26 */     this._enumsById = map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <ET extends Enum<ET>> EnumResolver<ET> constructFor(Class<ET> enumCls, AnnotationIntrospector ai)
/*     */   {
/*  35 */     ET[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  36 */     if (enumValues == null) {
/*  37 */       throw new IllegalArgumentException("No enum constants for class " + enumCls.getName());
/*     */     }
/*  39 */     HashMap<String, ET> map = new HashMap();
/*  40 */     for (ET e : enumValues) {
/*  41 */       map.put(ai.findEnumValue(e), e);
/*     */     }
/*  43 */     return new EnumResolver(enumCls, enumValues, map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <ET extends Enum<ET>> EnumResolver<ET> constructUsingToString(Class<ET> enumCls)
/*     */   {
/*  52 */     ET[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  53 */     HashMap<String, ET> map = new HashMap();
/*     */     
/*  55 */     int i = enumValues.length; for (;;) { i--; if (i < 0) break;
/*  56 */       ET e = enumValues[i];
/*  57 */       map.put(e.toString(), e);
/*     */     }
/*  59 */     return new EnumResolver(enumCls, enumValues, map);
/*     */   }
/*     */   
/*     */ 
/*     */   public static <ET extends Enum<ET>> EnumResolver<ET> constructUsingMethod(Class<ET> enumCls, Method accessor)
/*     */   {
/*  65 */     ET[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  66 */     HashMap<String, ET> map = new HashMap();
/*     */     
/*  68 */     int i = enumValues.length; for (;;) { i--; if (i < 0) break;
/*  69 */       ET en = enumValues[i];
/*     */       try {
/*  71 */         Object o = accessor.invoke(en, new Object[0]);
/*  72 */         if (o != null) {
/*  73 */           map.put(o.toString(), en);
/*     */         }
/*     */       } catch (Exception e) {
/*  76 */         throw new IllegalArgumentException("Failed to access @JsonValue of Enum value " + en + ": " + e.getMessage());
/*     */       }
/*     */     }
/*  79 */     return new EnumResolver(enumCls, enumValues, map);
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
/*     */   public static EnumResolver<?> constructUnsafe(Class<?> rawEnumCls, AnnotationIntrospector ai)
/*     */   {
/*  92 */     Class<Enum> enumCls = rawEnumCls;
/*  93 */     return constructFor(enumCls, ai);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumResolver<?> constructUnsafeUsingToString(Class<?> rawEnumCls)
/*     */   {
/* 104 */     Class<Enum> enumCls = rawEnumCls;
/* 105 */     return constructUsingToString(enumCls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumResolver<?> constructUnsafeUsingMethod(Class<?> rawEnumCls, Method accessor)
/*     */   {
/* 116 */     Class<Enum> enumCls = rawEnumCls;
/* 117 */     return constructUsingMethod(enumCls, accessor);
/*     */   }
/*     */   
/* 120 */   public T findEnum(String key) { return (Enum)this._enumsById.get(key); }
/*     */   
/*     */   public T getEnum(int index) {
/* 123 */     if ((index < 0) || (index >= this._enums.length)) {
/* 124 */       return null;
/*     */     }
/* 126 */     return this._enums[index];
/*     */   }
/*     */   
/*     */   public List<T> getEnums() {
/* 130 */     ArrayList<T> enums = new ArrayList(this._enums.length);
/* 131 */     for (T e : this._enums) {
/* 132 */       enums.add(e);
/*     */     }
/* 134 */     return enums;
/*     */   }
/*     */   
/* 137 */   public Class<T> getEnumClass() { return this._enumClass; }
/*     */   
/* 139 */   public int lastValidIndex() { return this._enums.length - 1; }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\EnumResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */