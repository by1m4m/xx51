/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ public class UnknownSerializer extends StdSerializer<Object>
/*    */ {
/*    */   public UnknownSerializer()
/*    */   {
/* 17 */     super(Object.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws java.io.IOException, JsonMappingException
/*    */   {
/* 25 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
/* 26 */       failForEmpty(value);
/*    */     }
/*    */     
/* 29 */     jgen.writeStartObject();
/* 30 */     jgen.writeEndObject();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws java.io.IOException, com.fasterxml.jackson.core.JsonGenerationException
/*    */   {
/* 38 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) {
/* 39 */       failForEmpty(value);
/*    */     }
/* 41 */     typeSer.writeTypePrefixForObject(value, jgen);
/* 42 */     typeSer.writeTypeSuffixForObject(value, jgen);
/*    */   }
/*    */   
/*    */   public com.fasterxml.jackson.databind.JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException
/*    */   {
/* 47 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 54 */     visitor.expectAnyFormat(typeHint);
/*    */   }
/*    */   
/*    */   protected void failForEmpty(Object value) throws JsonMappingException
/*    */   {
/* 59 */     throw new JsonMappingException("No serializer found for class " + value.getClass().getName() + " and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) )");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\UnknownSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */