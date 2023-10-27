/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldInfo
/*     */ {
/*  36 */   private static final Map<Field, FieldInfo> CACHE = new WeakHashMap();
/*     */   
/*     */   private final boolean isPrimitive;
/*     */   
/*     */   private final Field field;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public static FieldInfo of(Enum<?> enumValue)
/*     */   {
/*     */     try
/*     */     {
/*  48 */       FieldInfo result = of(enumValue.getClass().getField(enumValue.name()));
/*  49 */       Preconditions.checkArgument(result != null, "enum constant missing @Value or @NullValue annotation: %s", new Object[] { enumValue });
/*     */       
/*  51 */       return result;
/*     */     }
/*     */     catch (NoSuchFieldException e) {
/*  54 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static FieldInfo of(Field field)
/*     */   {
/*  66 */     if (field == null) {
/*  67 */       return null;
/*     */     }
/*  69 */     synchronized (CACHE) {
/*  70 */       FieldInfo fieldInfo = (FieldInfo)CACHE.get(field);
/*  71 */       boolean isEnumContant = field.isEnumConstant();
/*  72 */       if ((fieldInfo == null) && ((isEnumContant) || (!Modifier.isStatic(field.getModifiers())))) { String fieldName;
/*     */         String fieldName;
/*  74 */         if (isEnumContant)
/*     */         {
/*  76 */           Value value = (Value)field.getAnnotation(Value.class);
/*  77 */           String fieldName; if (value != null) {
/*  78 */             fieldName = value.value();
/*     */           }
/*     */           else {
/*  81 */             NullValue nullValue = (NullValue)field.getAnnotation(NullValue.class);
/*  82 */             String fieldName; if (nullValue != null) {
/*  83 */               fieldName = null;
/*     */             }
/*     */             else {
/*  86 */               return null;
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/*  91 */           Key key = (Key)field.getAnnotation(Key.class);
/*  92 */           if (key == null)
/*     */           {
/*  94 */             return null;
/*     */           }
/*  96 */           fieldName = key.value();
/*  97 */           field.setAccessible(true);
/*     */         }
/*  99 */         if ("##default".equals(fieldName)) {
/* 100 */           fieldName = field.getName();
/*     */         }
/* 102 */         fieldInfo = new FieldInfo(field, fieldName);
/* 103 */         CACHE.put(field, fieldInfo);
/*     */       }
/* 105 */       return fieldInfo;
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
/*     */ 
/*     */ 
/*     */   FieldInfo(Field field, String name)
/*     */   {
/* 127 */     this.field = field;
/* 128 */     this.name = (name == null ? null : name.intern());
/* 129 */     this.isPrimitive = Data.isPrimitive(getType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field getField()
/*     */   {
/* 138 */     return this.field;
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
/*     */   public String getName()
/*     */   {
/* 153 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getType()
/*     */   {
/* 162 */     return this.field.getType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getGenericType()
/*     */   {
/* 172 */     return this.field.getGenericType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFinal()
/*     */   {
/* 181 */     return Modifier.isFinal(this.field.getModifiers());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 190 */     return this.isPrimitive;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getValue(Object obj)
/*     */   {
/* 197 */     return getFieldValue(this.field, obj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(Object obj, Object value)
/*     */   {
/* 206 */     setFieldValue(this.field, obj, value);
/*     */   }
/*     */   
/*     */   public ClassInfo getClassInfo()
/*     */   {
/* 211 */     return ClassInfo.of(this.field.getDeclaringClass());
/*     */   }
/*     */   
/*     */   public <T extends Enum<T>> T enumValue()
/*     */   {
/* 216 */     return Enum.valueOf(this.field.getDeclaringClass(), this.field.getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public static Object getFieldValue(Field field, Object obj)
/*     */   {
/*     */     try
/*     */     {
/* 224 */       return field.get(obj);
/*     */     } catch (IllegalAccessException e) {
/* 226 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setFieldValue(Field field, Object obj, Object value)
/*     */   {
/* 236 */     if (Modifier.isFinal(field.getModifiers())) {
/* 237 */       Object finalValue = getFieldValue(field, obj);
/* 238 */       if (value == null ? finalValue != null : !value.equals(finalValue)) {
/* 239 */         String str1 = String.valueOf(String.valueOf(finalValue));String str2 = String.valueOf(String.valueOf(value));String str3 = String.valueOf(String.valueOf(field.getName()));String str4 = String.valueOf(String.valueOf(obj.getClass().getName()));throw new IllegalArgumentException(48 + str1.length() + str2.length() + str3.length() + str4.length() + "expected final value <" + str1 + "> but was <" + str2 + "> on " + str3 + " field in " + str4);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       try {
/* 245 */         field.set(obj, value);
/*     */       } catch (SecurityException e) {
/* 247 */         throw new IllegalArgumentException(e);
/*     */       } catch (IllegalAccessException e) {
/* 249 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\FieldInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */