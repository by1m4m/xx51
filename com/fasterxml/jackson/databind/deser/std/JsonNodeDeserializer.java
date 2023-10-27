/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.NullNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonNodeDeserializer
/*     */   extends BaseNodeDeserializer<JsonNode>
/*     */ {
/*  22 */   private static final JsonNodeDeserializer instance = new JsonNodeDeserializer();
/*     */   
/*  24 */   protected JsonNodeDeserializer() { super(JsonNode.class); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> nodeClass)
/*     */   {
/*  31 */     if (nodeClass == ObjectNode.class) {
/*  32 */       return ObjectDeserializer.getInstance();
/*     */     }
/*  34 */     if (nodeClass == ArrayNode.class) {
/*  35 */       return ArrayDeserializer.getInstance();
/*     */     }
/*     */     
/*  38 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getNullValue()
/*     */   {
/*  49 */     return NullNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  60 */     switch (jp.getCurrentTokenId()) {
/*     */     case 1: 
/*  62 */       return deserializeObject(jp, ctxt, ctxt.getNodeFactory());
/*     */     case 3: 
/*  64 */       return deserializeArray(jp, ctxt, ctxt.getNodeFactory());
/*     */     }
/*  66 */     return deserializeAny(jp, ctxt, ctxt.getNodeFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class ObjectDeserializer
/*     */     extends BaseNodeDeserializer<ObjectNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  81 */     protected static final ObjectDeserializer _instance = new ObjectDeserializer();
/*     */     
/*  83 */     protected ObjectDeserializer() { super(); }
/*     */     
/*  85 */     public static ObjectDeserializer getInstance() { return _instance; }
/*     */     
/*     */     public ObjectNode deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/*  90 */       if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
/*  91 */         jp.nextToken();
/*  92 */         return deserializeObject(jp, ctxt, ctxt.getNodeFactory());
/*     */       }
/*  94 */       if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
/*  95 */         return deserializeObject(jp, ctxt, ctxt.getNodeFactory());
/*     */       }
/*  97 */       throw ctxt.mappingException(ObjectNode.class);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class ArrayDeserializer
/*     */     extends BaseNodeDeserializer<ArrayNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 106 */     protected static final ArrayDeserializer _instance = new ArrayDeserializer();
/*     */     
/* 108 */     protected ArrayDeserializer() { super(); }
/*     */     
/* 110 */     public static ArrayDeserializer getInstance() { return _instance; }
/*     */     
/*     */ 
/*     */     public ArrayNode deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 116 */       if (jp.isExpectedStartArrayToken()) {
/* 117 */         return deserializeArray(jp, ctxt, ctxt.getNodeFactory());
/*     */       }
/* 119 */       throw ctxt.mappingException(ArrayNode.class);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\JsonNodeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */