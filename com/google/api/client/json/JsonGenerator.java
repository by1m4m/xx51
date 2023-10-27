/*     */ package com.google.api.client.json;
/*     */ 
/*     */ import com.google.api.client.util.ClassInfo;
/*     */ import com.google.api.client.util.Data;
/*     */ import com.google.api.client.util.DateTime;
/*     */ import com.google.api.client.util.FieldInfo;
/*     */ import com.google.api.client.util.GenericData;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Types;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public abstract class JsonGenerator
/*     */ {
/*     */   public abstract JsonFactory getFactory();
/*     */   
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeStartArray()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeEndArray()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeStartObject()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeEndObject()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeFieldName(String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNull()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeString(String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeBoolean(boolean paramBoolean)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(long paramLong)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(BigInteger paramBigInteger)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(float paramFloat)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(double paramDouble)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(BigDecimal paramBigDecimal)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeNumber(String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public final void serialize(Object value)
/*     */     throws IOException
/*     */   {
/* 106 */     serialize(false, value);
/*     */   }
/*     */   
/*     */   private void serialize(boolean isJsonString, Object value) throws IOException {
/* 110 */     if (value == null) {
/* 111 */       return;
/*     */     }
/* 113 */     Class<?> valueClass = value.getClass();
/* 114 */     if (Data.isNull(value)) {
/* 115 */       writeNull();
/* 116 */     } else if ((value instanceof String)) {
/* 117 */       writeString((String)value);
/* 118 */     } else if ((value instanceof Number)) {
/* 119 */       if (isJsonString) {
/* 120 */         writeString(value.toString());
/* 121 */       } else if ((value instanceof BigDecimal)) {
/* 122 */         writeNumber((BigDecimal)value);
/* 123 */       } else if ((value instanceof BigInteger)) {
/* 124 */         writeNumber((BigInteger)value);
/* 125 */       } else if ((value instanceof Long)) {
/* 126 */         writeNumber(((Long)value).longValue());
/* 127 */       } else if ((value instanceof Float)) {
/* 128 */         float floatValue = ((Number)value).floatValue();
/* 129 */         Preconditions.checkArgument((!Float.isInfinite(floatValue)) && (!Float.isNaN(floatValue)));
/* 130 */         writeNumber(floatValue);
/* 131 */       } else if (((value instanceof Integer)) || ((value instanceof Short)) || ((value instanceof Byte))) {
/* 132 */         writeNumber(((Number)value).intValue());
/*     */       } else {
/* 134 */         double doubleValue = ((Number)value).doubleValue();
/* 135 */         Preconditions.checkArgument((!Double.isInfinite(doubleValue)) && (!Double.isNaN(doubleValue)));
/* 136 */         writeNumber(doubleValue);
/*     */       }
/* 138 */     } else if ((value instanceof Boolean)) {
/* 139 */       writeBoolean(((Boolean)value).booleanValue());
/* 140 */     } else if ((value instanceof DateTime)) {
/* 141 */       writeString(((DateTime)value).toStringRfc3339());
/* 142 */     } else if (((value instanceof Iterable)) || (valueClass.isArray())) {
/* 143 */       writeStartArray();
/* 144 */       for (Object o : Types.iterableOf(value)) {
/* 145 */         serialize(isJsonString, o);
/*     */       }
/* 147 */       writeEndArray();
/* 148 */     } else if (valueClass.isEnum()) {
/* 149 */       String name = FieldInfo.of((Enum)value).getName();
/* 150 */       if (name == null) {
/* 151 */         writeNull();
/*     */       } else {
/* 153 */         writeString(name);
/*     */       }
/*     */     } else {
/* 156 */       writeStartObject();
/*     */       
/* 158 */       boolean isMapNotGenericData = ((value instanceof Map)) && (!(value instanceof GenericData));
/* 159 */       ClassInfo classInfo = isMapNotGenericData ? null : ClassInfo.of(valueClass);
/* 160 */       for (Map.Entry<String, Object> entry : Data.mapOf(value).entrySet()) {
/* 161 */         Object fieldValue = entry.getValue();
/* 162 */         if (fieldValue != null) {
/* 163 */           String fieldName = (String)entry.getKey();
/*     */           boolean isJsonStringForField;
/* 165 */           boolean isJsonStringForField; if (isMapNotGenericData) {
/* 166 */             isJsonStringForField = isJsonString;
/*     */           } else {
/* 168 */             Field field = classInfo.getField(fieldName);
/* 169 */             isJsonStringForField = (field != null) && (field.getAnnotation(JsonString.class) != null);
/*     */           }
/* 171 */           writeFieldName(fieldName);
/* 172 */           serialize(isJsonStringForField, fieldValue);
/*     */         }
/*     */       }
/* 175 */       writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enablePrettyPrint()
/*     */     throws IOException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\JsonGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */