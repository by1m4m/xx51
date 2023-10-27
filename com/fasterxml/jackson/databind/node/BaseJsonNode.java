/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.core.ObjectCodec;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.JsonSerializable;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseJsonNode
/*    */   extends JsonNode
/*    */   implements JsonSerializable
/*    */ {
/*    */   public final JsonNode findPath(String fieldName)
/*    */   {
/* 33 */     JsonNode value = findValue(fieldName);
/* 34 */     if (value == null) {
/* 35 */       return MissingNode.getInstance();
/*    */     }
/* 37 */     return value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonParser traverse()
/*    */   {
/* 48 */     return new TreeTraversingParser(this);
/*    */   }
/*    */   
/*    */   public JsonParser traverse(ObjectCodec codec)
/*    */   {
/* 53 */     return new TreeTraversingParser(this, codec);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract JsonToken asToken();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonParser.NumberType numberType()
/*    */   {
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   public abstract void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*    */     throws IOException, JsonProcessingException;
/*    */   
/*    */   public abstract void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
/*    */     throws IOException, JsonProcessingException;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\BaseJsonNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */