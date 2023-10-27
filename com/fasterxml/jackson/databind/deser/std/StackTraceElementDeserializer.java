/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class StackTraceElementDeserializer
/*    */   extends StdScalarDeserializer<StackTraceElement>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public StackTraceElementDeserializer()
/*    */   {
/* 17 */     super(StackTraceElement.class);
/*    */   }
/*    */   
/*    */   public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*    */   {
/* 22 */     JsonToken t = jp.getCurrentToken();
/*    */     
/* 24 */     if (t == JsonToken.START_OBJECT) {
/* 25 */       String className = "";String methodName = "";String fileName = "";
/* 26 */       int lineNumber = -1;
/*    */       
/* 28 */       while ((t = jp.nextValue()) != JsonToken.END_OBJECT) {
/* 29 */         String propName = jp.getCurrentName();
/* 30 */         if ("className".equals(propName)) {
/* 31 */           className = jp.getText();
/* 32 */         } else if ("fileName".equals(propName)) {
/* 33 */           fileName = jp.getText();
/* 34 */         } else if ("lineNumber".equals(propName)) {
/* 35 */           if (t.isNumeric()) {
/* 36 */             lineNumber = jp.getIntValue();
/*    */           } else {
/* 38 */             throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'lineNumber'");
/*    */           }
/* 40 */         } else if ("methodName".equals(propName)) {
/* 41 */           methodName = jp.getText();
/* 42 */         } else if (!"nativeMethod".equals(propName))
/*    */         {
/*    */ 
/* 45 */           handleUnknownProperty(jp, ctxt, this._valueClass, propName);
/*    */         }
/*    */       }
/* 48 */       return new StackTraceElement(className, methodName, fileName, lineNumber); }
/* 49 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 50 */       jp.nextToken();
/* 51 */       StackTraceElement value = deserialize(jp, ctxt);
/* 52 */       if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 53 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'java.lang.StackTraceElement' value but there was more than a single value in the array");
/*    */       }
/*    */       
/*    */ 
/* 57 */       return value;
/*    */     }
/*    */     
/* 60 */     throw ctxt.mappingException(this._valueClass, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StackTraceElementDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */