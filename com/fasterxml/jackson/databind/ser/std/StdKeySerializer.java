/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StdKeySerializer
/*    */   extends StdSerializer<Object>
/*    */ {
/*    */   public StdKeySerializer()
/*    */   {
/* 22 */     super(Object.class);
/*    */   }
/*    */   
/*    */   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*    */   {
/* 27 */     Class<?> cls = value.getClass();
/*    */     String str;
/* 29 */     String str; if (cls == String.class) {
/* 30 */       str = (String)value;
/* 31 */     } else { if (Date.class.isAssignableFrom(cls)) {
/* 32 */         provider.defaultSerializeDateKey((Date)value, jgen); return; }
/*    */       String str;
/* 34 */       if (cls == Class.class) {
/* 35 */         str = ((Class)value).getName();
/*    */       } else
/* 37 */         str = value.toString();
/*    */     }
/* 39 */     jgen.writeFieldName(str);
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException
/*    */   {
/* 44 */     return createSchemaNode("string");
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
/*    */   {
/* 49 */     visitor.expectStringFormat(typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StdKeySerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */