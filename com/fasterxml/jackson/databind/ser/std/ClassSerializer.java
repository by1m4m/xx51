/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassSerializer
/*    */   extends StdScalarSerializer<Class<?>>
/*    */ {
/*    */   public ClassSerializer()
/*    */   {
/* 22 */     super(Class.class, false);
/*    */   }
/*    */   
/*    */   public void serialize(Class<?> value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 28 */     jgen.writeString(value.getName());
/*    */   }
/*    */   
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 34 */     return createSchemaNode("string", true);
/*    */   }
/*    */   
/*    */ 
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 41 */     visitor.expectStringFormat(typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\ClassSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */