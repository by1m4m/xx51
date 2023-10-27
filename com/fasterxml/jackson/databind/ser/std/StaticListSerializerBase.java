/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ public abstract class StaticListSerializerBase<T extends Collection<?>>
/*    */   extends StdSerializer<T>
/*    */ {
/*    */   protected StaticListSerializerBase(Class<?> cls)
/*    */   {
/* 19 */     super(cls, false);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public boolean isEmpty(T value)
/*    */   {
/* 25 */     return isEmpty(null, value);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider provider, T value)
/*    */   {
/* 30 */     return (value == null) || (value.size() == 0);
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 35 */     return createSchemaNode("array", true).set("items", contentSchema());
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
/*    */   {
/* 40 */     acceptContentVisitor(visitor.expectArrayFormat(typeHint));
/*    */   }
/*    */   
/*    */   protected abstract JsonNode contentSchema();
/*    */   
/*    */   protected abstract void acceptContentVisitor(JsonArrayFormatVisitor paramJsonArrayFormatVisitor)
/*    */     throws JsonMappingException;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StaticListSerializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */