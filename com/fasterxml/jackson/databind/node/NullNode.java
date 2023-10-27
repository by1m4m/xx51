/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NullNode
/*    */   extends ValueNode
/*    */ {
/* 18 */   public static final NullNode instance = new NullNode();
/*    */   
/*    */   public static NullNode getInstance()
/*    */   {
/* 22 */     return instance;
/*    */   }
/*    */   
/*    */   public JsonNodeType getNodeType()
/*    */   {
/* 27 */     return JsonNodeType.NULL;
/*    */   }
/*    */   
/* 30 */   public JsonToken asToken() { return JsonToken.VALUE_NULL; }
/*    */   
/* 32 */   public String asText(String defaultValue) { return defaultValue; }
/* 33 */   public String asText() { return "null"; }
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
/*    */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 48 */     provider.defaultSerializeNull(jg);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 54 */     return o == this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\NullNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */