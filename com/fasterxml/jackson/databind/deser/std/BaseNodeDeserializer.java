/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class BaseNodeDeserializer<T extends JsonNode>
/*     */   extends StdDeserializer<T>
/*     */ {
/*     */   public BaseNodeDeserializer(Class<T> vc)
/*     */   {
/* 133 */     super(vc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 144 */     return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 152 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _reportProblem(JsonParser jp, String msg)
/*     */     throws JsonMappingException
/*     */   {
/* 161 */     throw new JsonMappingException(msg, jp.getTokenLocation());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected void _handleDuplicateField(String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue)
/*     */     throws JsonProcessingException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _handleDuplicateField(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue)
/*     */     throws JsonProcessingException
/*     */   {
/* 197 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)) {
/* 198 */       _reportProblem(jp, "Duplicate field '" + fieldName + "' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled");
/*     */     }
/*     */     
/* 201 */     _handleDuplicateField(fieldName, objectNode, oldValue, newValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ObjectNode deserializeObject(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 213 */     ObjectNode node = nodeFactory.objectNode();
/* 214 */     JsonToken t = jp.getCurrentToken();
/* 215 */     if (t == JsonToken.START_OBJECT) {}
/* 216 */     for (t = jp.nextToken(); 
/*     */         
/* 218 */         t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 219 */       String fieldName = jp.getCurrentName();
/*     */       
/* 221 */       t = jp.nextToken();
/* 222 */       JsonNode value; switch (t.id()) {
/*     */       case 1: 
/* 224 */         value = deserializeObject(jp, ctxt, nodeFactory);
/* 225 */         break;
/*     */       case 3: 
/* 227 */         value = deserializeArray(jp, ctxt, nodeFactory);
/* 228 */         break;
/*     */       case 6: 
/* 230 */         value = nodeFactory.textNode(jp.getText());
/* 231 */         break;
/*     */       case 7: 
/* 233 */         value = _fromInt(jp, ctxt, nodeFactory);
/* 234 */         break;
/*     */       case 9: 
/* 236 */         value = nodeFactory.booleanNode(true);
/* 237 */         break;
/*     */       case 10: 
/* 239 */         value = nodeFactory.booleanNode(false);
/* 240 */         break;
/*     */       case 11: 
/* 242 */         value = nodeFactory.nullNode();
/* 243 */         break;
/*     */       case 2: case 4: case 5: case 8: default: 
/* 245 */         value = deserializeAny(jp, ctxt, nodeFactory);
/*     */       }
/* 247 */       JsonNode old = node.replace(fieldName, value);
/* 248 */       if (old != null) {
/* 249 */         _handleDuplicateField(jp, ctxt, nodeFactory, fieldName, node, old, value);
/*     */       }
/*     */     }
/*     */     
/* 253 */     return node;
/*     */   }
/*     */   
/*     */   protected final ArrayNode deserializeArray(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 259 */     ArrayNode node = nodeFactory.arrayNode();
/*     */     for (;;) {
/* 261 */       JsonToken t = jp.nextToken();
/* 262 */       if (t == null) {
/* 263 */         throw ctxt.mappingException("Unexpected end-of-input when binding data into ArrayNode");
/*     */       }
/* 265 */       switch (t.id()) {
/*     */       case 1: 
/* 267 */         node.add(deserializeObject(jp, ctxt, nodeFactory));
/* 268 */         break;
/*     */       case 3: 
/* 270 */         node.add(deserializeArray(jp, ctxt, nodeFactory));
/* 271 */         break;
/*     */       case 4: 
/* 273 */         return node;
/*     */       case 6: 
/* 275 */         node.add(nodeFactory.textNode(jp.getText()));
/* 276 */         break;
/*     */       case 7: 
/* 278 */         node.add(_fromInt(jp, ctxt, nodeFactory));
/* 279 */         break;
/*     */       case 9: 
/* 281 */         node.add(nodeFactory.booleanNode(true));
/* 282 */         break;
/*     */       case 10: 
/* 284 */         node.add(nodeFactory.booleanNode(false));
/* 285 */         break;
/*     */       case 11: 
/* 287 */         node.add(nodeFactory.nullNode());
/* 288 */         break;
/*     */       case 2: case 5: case 8: default: 
/* 290 */         node.add(deserializeAny(jp, ctxt, nodeFactory));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   protected final JsonNode deserializeAny(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 299 */     switch (jp.getCurrentTokenId()) {
/*     */     case 1: 
/*     */     case 2: 
/* 302 */       return deserializeObject(jp, ctxt, nodeFactory);
/*     */     case 3: 
/* 304 */       return deserializeArray(jp, ctxt, nodeFactory);
/*     */     case 5: 
/* 306 */       return deserializeObject(jp, ctxt, nodeFactory);
/*     */     case 12: 
/* 308 */       return _fromEmbedded(jp, ctxt, nodeFactory);
/*     */     case 6: 
/* 310 */       return nodeFactory.textNode(jp.getText());
/*     */     case 7: 
/* 312 */       return _fromInt(jp, ctxt, nodeFactory);
/*     */     case 8: 
/* 314 */       return _fromFloat(jp, ctxt, nodeFactory);
/*     */     case 9: 
/* 316 */       return nodeFactory.booleanNode(true);
/*     */     case 10: 
/* 318 */       return nodeFactory.booleanNode(false);
/*     */     case 11: 
/* 320 */       return nodeFactory.nullNode();
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 328 */     throw ctxt.mappingException(handledType());
/*     */   }
/*     */   
/*     */ 
/*     */   protected final JsonNode _fromInt(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 335 */     JsonParser.NumberType nt = jp.getNumberType();
/* 336 */     if ((nt == JsonParser.NumberType.BIG_INTEGER) || (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)))
/*     */     {
/* 338 */       return nodeFactory.numberNode(jp.getBigIntegerValue());
/*     */     }
/* 340 */     if (nt == JsonParser.NumberType.INT) {
/* 341 */       return nodeFactory.numberNode(jp.getIntValue());
/*     */     }
/* 343 */     return nodeFactory.numberNode(jp.getLongValue());
/*     */   }
/*     */   
/*     */   protected final JsonNode _fromFloat(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 349 */     JsonParser.NumberType nt = jp.getNumberType();
/* 350 */     if ((nt == JsonParser.NumberType.BIG_DECIMAL) || (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)))
/*     */     {
/* 352 */       return nodeFactory.numberNode(jp.getDecimalValue());
/*     */     }
/* 354 */     return nodeFactory.numberNode(jp.getDoubleValue());
/*     */   }
/*     */   
/*     */ 
/*     */   protected final JsonNode _fromEmbedded(JsonParser jp, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 361 */     Object ob = jp.getEmbeddedObject();
/* 362 */     if (ob == null) {
/* 363 */       return nodeFactory.nullNode();
/*     */     }
/* 365 */     Class<?> type = ob.getClass();
/* 366 */     if (type == byte[].class) {
/* 367 */       return nodeFactory.binaryNode((byte[])ob);
/*     */     }
/* 369 */     if (JsonNode.class.isAssignableFrom(type))
/*     */     {
/* 371 */       return (JsonNode)ob;
/*     */     }
/*     */     
/* 374 */     return nodeFactory.pojoNode(ob);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\BaseNodeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */