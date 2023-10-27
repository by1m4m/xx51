/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ArrayValueMap
/*     */ {
/*     */   static class ArrayValue
/*     */   {
/*     */     final Class<?> componentType;
/*  55 */     final ArrayList<Object> values = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */     ArrayValue(Class<?> componentType)
/*     */     {
/*  61 */       this.componentType = componentType;
/*     */     }
/*     */     
/*     */     Object toArray()
/*     */     {
/*  66 */       return Types.toArray(this.values, this.componentType);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void addValue(Class<?> componentType, Object value)
/*     */     {
/*  74 */       Preconditions.checkArgument(componentType == this.componentType);
/*  75 */       this.values.add(value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  80 */   private final Map<String, ArrayValue> keyMap = ArrayMap.create();
/*     */   
/*     */ 
/*  83 */   private final Map<Field, ArrayValue> fieldMap = ArrayMap.create();
/*     */   
/*     */ 
/*     */ 
/*     */   private final Object destination;
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayValueMap(Object destination)
/*     */   {
/*  93 */     this.destination = destination;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValues()
/*     */   {
/* 101 */     for (Map.Entry<String, ArrayValue> entry : this.keyMap.entrySet())
/*     */     {
/* 103 */       Map<String, Object> destinationMap = (Map)this.destination;
/* 104 */       destinationMap.put(entry.getKey(), ((ArrayValue)entry.getValue()).toArray());
/*     */     }
/* 106 */     for (Map.Entry<Field, ArrayValue> entry : this.fieldMap.entrySet()) {
/* 107 */       FieldInfo.setFieldValue((Field)entry.getKey(), this.destination, ((ArrayValue)entry.getValue()).toArray());
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
/*     */   public void put(Field field, Class<?> arrayComponentType, Object value)
/*     */   {
/* 120 */     ArrayValue arrayValue = (ArrayValue)this.fieldMap.get(field);
/* 121 */     if (arrayValue == null) {
/* 122 */       arrayValue = new ArrayValue(arrayComponentType);
/* 123 */       this.fieldMap.put(field, arrayValue);
/*     */     }
/* 125 */     arrayValue.addValue(arrayComponentType, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String keyName, Class<?> arrayComponentType, Object value)
/*     */   {
/* 137 */     ArrayValue arrayValue = (ArrayValue)this.keyMap.get(keyName);
/* 138 */     if (arrayValue == null) {
/* 139 */       arrayValue = new ArrayValue(arrayComponentType);
/* 140 */       this.keyMap.put(keyName, arrayValue);
/*     */     }
/* 142 */     arrayValue.addValue(arrayComponentType, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ArrayValueMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */