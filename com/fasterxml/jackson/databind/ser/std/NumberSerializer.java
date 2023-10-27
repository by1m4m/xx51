/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class NumberSerializer
/*    */   extends StdScalarSerializer<Number>
/*    */ {
/* 29 */   public static final NumberSerializer instance = new NumberSerializer(Number.class);
/*    */   protected final boolean _isInt;
/*    */   
/*    */   @Deprecated
/*    */   public NumberSerializer()
/*    */   {
/* 35 */     super(Number.class);
/* 36 */     this._isInt = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public NumberSerializer(Class<? extends Number> rawType)
/*    */   {
/* 43 */     super(rawType, false);
/*    */     
/* 45 */     this._isInt = (rawType == BigInteger.class);
/*    */   }
/*    */   
/*    */ 
/*    */   public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 52 */     if ((value instanceof BigDecimal)) {
/* 53 */       jgen.writeNumber((BigDecimal)value);
/* 54 */     } else if ((value instanceof BigInteger)) {
/* 55 */       jgen.writeNumber((BigInteger)value);
/*    */ 
/*    */ 
/*    */ 
/*    */     }
/* 60 */     else if ((value instanceof Integer)) {
/* 61 */       jgen.writeNumber(value.intValue());
/* 62 */     } else if ((value instanceof Long)) {
/* 63 */       jgen.writeNumber(value.longValue());
/* 64 */     } else if ((value instanceof Double)) {
/* 65 */       jgen.writeNumber(value.doubleValue());
/* 66 */     } else if ((value instanceof Float)) {
/* 67 */       jgen.writeNumber(value.floatValue());
/* 68 */     } else if (((value instanceof Byte)) || ((value instanceof Short))) {
/* 69 */       jgen.writeNumber(value.intValue());
/*    */     }
/*    */     else {
/* 72 */       jgen.writeNumber(value.toString());
/*    */     }
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 78 */     return createSchemaNode(this._isInt ? "integer" : "number", true);
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 84 */     if (this._isInt) {
/* 85 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 86 */       if (v2 != null) {
/* 87 */         v2.numberType(JsonParser.NumberType.BIG_INTEGER);
/*    */       }
/*    */     } else {
/* 90 */       JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(typeHint);
/* 91 */       if (v2 != null) {
/* 92 */         Class<?> h = handledType();
/* 93 */         if (h == BigDecimal.class) {
/* 94 */           v2.numberType(JsonParser.NumberType.BIG_DECIMAL);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\NumberSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */