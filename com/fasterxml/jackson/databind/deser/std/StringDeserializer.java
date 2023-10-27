/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Base64Variant;
/*    */ import com.fasterxml.jackson.core.Base64Variants;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public final class StringDeserializer
/*    */   extends StdScalarDeserializer<String>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 20 */   public static final StringDeserializer instance = new StringDeserializer();
/*    */   
/* 22 */   public StringDeserializer() { super(String.class); }
/*    */   
/*    */   public String deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException
/*    */   {
/* 27 */     JsonToken curr = jp.getCurrentToken();
/* 28 */     if (curr == JsonToken.VALUE_STRING) {
/* 29 */       return jp.getText();
/*    */     }
/*    */     
/*    */ 
/* 33 */     if ((curr == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 34 */       jp.nextToken();
/* 35 */       String parsed = _parseString(jp, ctxt);
/* 36 */       if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 37 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'String' value but there was more than a single value in the array");
/*    */       }
/*    */       
/* 40 */       return parsed;
/*    */     }
/*    */     
/* 43 */     if (curr == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 44 */       Object ob = jp.getEmbeddedObject();
/* 45 */       if (ob == null) {
/* 46 */         return null;
/*    */       }
/* 48 */       if ((ob instanceof byte[])) {
/* 49 */         return Base64Variants.getDefaultVariant().encode((byte[])ob, false);
/*    */       }
/*    */       
/* 52 */       return ob.toString();
/*    */     }
/*    */     
/* 55 */     String text = jp.getValueAsString();
/* 56 */     if (text != null) {
/* 57 */       return text;
/*    */     }
/* 59 */     throw ctxt.mappingException(this._valueClass, curr);
/*    */   }
/*    */   
/*    */ 
/*    */   public String deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException
/*    */   {
/* 66 */     return deserialize(jp, ctxt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StringDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */