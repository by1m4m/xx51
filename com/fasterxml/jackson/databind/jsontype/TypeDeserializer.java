/*     */ package com.fasterxml.jackson.databind.jsontype;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeDeserializer
/*     */ {
/*     */   public abstract TypeDeserializer forProperty(BeanProperty paramBeanProperty);
/*     */   
/*     */   public abstract JsonTypeInfo.As getTypeInclusion();
/*     */   
/*     */   public abstract String getPropertyName();
/*     */   
/*     */   public abstract TypeIdResolver getTypeIdResolver();
/*     */   
/*     */   public abstract Class<?> getDefaultImpl();
/*     */   
/*     */   public abstract Object deserializeTypedFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*     */     throws IOException;
/*     */   
/*     */   public abstract Object deserializeTypedFromArray(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*     */     throws IOException;
/*     */   
/*     */   public abstract Object deserializeTypedFromScalar(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*     */     throws IOException;
/*     */   
/*     */   public abstract Object deserializeTypedFromAny(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*     */     throws IOException;
/*     */   
/*     */   public static Object deserializeIfNatural(JsonParser jp, DeserializationContext ctxt, JavaType baseType)
/*     */     throws IOException
/*     */   {
/* 139 */     return deserializeIfNatural(jp, ctxt, baseType.getRawClass());
/*     */   }
/*     */   
/*     */   public static Object deserializeIfNatural(JsonParser jp, DeserializationContext ctxt, Class<?> base)
/*     */     throws IOException
/*     */   {
/* 145 */     JsonToken t = jp.getCurrentToken();
/* 146 */     if (t == null) {
/* 147 */       return null;
/*     */     }
/* 149 */     switch (t) {
/*     */     case VALUE_STRING: 
/* 151 */       if (base.isAssignableFrom(String.class)) {
/* 152 */         return jp.getText();
/*     */       }
/*     */       break;
/*     */     case VALUE_NUMBER_INT: 
/* 156 */       if (base.isAssignableFrom(Integer.class)) {
/* 157 */         return Integer.valueOf(jp.getIntValue());
/*     */       }
/*     */       
/*     */       break;
/*     */     case VALUE_NUMBER_FLOAT: 
/* 162 */       if (base.isAssignableFrom(Double.class)) {
/* 163 */         return Double.valueOf(jp.getDoubleValue());
/*     */       }
/*     */       break;
/*     */     case VALUE_TRUE: 
/* 167 */       if (base.isAssignableFrom(Boolean.class)) {
/* 168 */         return Boolean.TRUE;
/*     */       }
/*     */       break;
/*     */     case VALUE_FALSE: 
/* 172 */       if (base.isAssignableFrom(Boolean.class)) {
/* 173 */         return Boolean.FALSE;
/*     */       }
/*     */       break;
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\TypeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */