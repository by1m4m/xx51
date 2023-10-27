/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public abstract class ContainerNode<T extends ContainerNode<T>>
/*     */   extends BaseJsonNode
/*     */   implements JsonNodeCreator
/*     */ {
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */   
/*     */   protected ContainerNode(JsonNodeFactory nc)
/*     */   {
/*  27 */     this._nodeFactory = nc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonToken asToken();
/*     */   
/*     */ 
/*     */   public String asText()
/*     */   {
/*  37 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int size();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonNode get(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonNode get(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ArrayNode arrayNode()
/*     */   {
/*  66 */     return this._nodeFactory.arrayNode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final ObjectNode objectNode()
/*     */   {
/*  73 */     return this._nodeFactory.objectNode();
/*     */   }
/*     */   
/*  76 */   public final NullNode nullNode() { return this._nodeFactory.nullNode(); }
/*     */   
/*     */   public final BooleanNode booleanNode(boolean v) {
/*  79 */     return this._nodeFactory.booleanNode(v);
/*     */   }
/*     */   
/*  82 */   public final NumericNode numberNode(byte v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*  84 */   public final NumericNode numberNode(short v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*  86 */   public final NumericNode numberNode(int v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */   public final NumericNode numberNode(long v) {
/*  89 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   
/*     */   public final NumericNode numberNode(BigInteger v)
/*     */   {
/*  94 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   
/*  97 */   public final NumericNode numberNode(float v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*  99 */   public final NumericNode numberNode(double v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 101 */   public final NumericNode numberNode(BigDecimal v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */ 
/*     */ 
/* 105 */   public final ValueNode numberNode(Byte v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 107 */   public final ValueNode numberNode(Short v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 109 */   public final ValueNode numberNode(Integer v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 111 */   public final ValueNode numberNode(Long v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */ 
/* 114 */   public final ValueNode numberNode(Float v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 116 */   public final ValueNode numberNode(Double v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */   public final TextNode textNode(String text) {
/* 119 */     return this._nodeFactory.textNode(text);
/*     */   }
/*     */   
/* 122 */   public final BinaryNode binaryNode(byte[] data) { return this._nodeFactory.binaryNode(data); }
/*     */   
/* 124 */   public final BinaryNode binaryNode(byte[] data, int offset, int length) { return this._nodeFactory.binaryNode(data, offset, length); }
/*     */   
/*     */   public final ValueNode pojoNode(Object pojo) {
/* 127 */     return this._nodeFactory.pojoNode(pojo);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public final POJONode POJONode(Object pojo)
/*     */   {
/* 133 */     return (POJONode)this._nodeFactory.pojoNode(pojo);
/*     */   }
/*     */   
/*     */   public abstract T removeAll();
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\ContainerNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */