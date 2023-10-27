/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class JsonNodeFactory
/*     */   implements Serializable, JsonNodeCreator
/*     */ {
/*     */   private static final long serialVersionUID = -3271940633258788634L;
/*     */   private final boolean _cfgBigDecimalExact;
/*  22 */   private static final JsonNodeFactory decimalsNormalized = new JsonNodeFactory(false);
/*     */   
/*  24 */   private static final JsonNodeFactory decimalsAsIs = new JsonNodeFactory(true);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final JsonNodeFactory instance = decimalsNormalized;
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
/*     */   public JsonNodeFactory(boolean bigDecimalExact)
/*     */   {
/*  61 */     this._cfgBigDecimalExact = bigDecimalExact;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonNodeFactory()
/*     */   {
/*  72 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonNodeFactory withExactBigDecimals(boolean bigDecimalExact)
/*     */   {
/*  84 */     return bigDecimalExact ? decimalsAsIs : decimalsNormalized;
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
/*     */   public BooleanNode booleanNode(boolean v)
/*     */   {
/*  99 */     return v ? BooleanNode.getTrue() : BooleanNode.getFalse();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NullNode nullNode()
/*     */   {
/* 107 */     return NullNode.getInstance();
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
/*     */   public NumericNode numberNode(byte v)
/*     */   {
/* 120 */     return IntNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Byte value)
/*     */   {
/* 130 */     return value == null ? nullNode() : IntNode.valueOf(value.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(short v)
/*     */   {
/* 138 */     return ShortNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Short value)
/*     */   {
/* 148 */     return value == null ? nullNode() : ShortNode.valueOf(value.shortValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(int v)
/*     */   {
/* 156 */     return IntNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Integer value)
/*     */   {
/* 166 */     return value == null ? nullNode() : IntNode.valueOf(value.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(long v)
/*     */   {
/* 175 */     if (_inIntRange(v)) {
/* 176 */       return IntNode.valueOf((int)v);
/*     */     }
/* 178 */     return LongNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Long value)
/*     */   {
/* 188 */     if (value == null) {
/* 189 */       return nullNode();
/*     */     }
/* 191 */     long l = value.longValue();
/* 192 */     return _inIntRange(l) ? IntNode.valueOf((int)l) : LongNode.valueOf(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(BigInteger v)
/*     */   {
/* 201 */     return BigIntegerNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(float v)
/*     */   {
/* 208 */     return FloatNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Float value)
/*     */   {
/* 218 */     return value == null ? nullNode() : FloatNode.valueOf(value.floatValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(double v)
/*     */   {
/* 226 */     return DoubleNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Double value)
/*     */   {
/* 236 */     return value == null ? nullNode() : DoubleNode.valueOf(value.doubleValue());
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
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(BigDecimal v)
/*     */   {
/* 256 */     if (this._cfgBigDecimalExact) {
/* 257 */       return DecimalNode.valueOf(v);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */     return v.compareTo(BigDecimal.ZERO) == 0 ? DecimalNode.ZERO : DecimalNode.valueOf(v.stripTrailingZeros());
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
/*     */   public TextNode textNode(String text)
/*     */   {
/* 283 */     return TextNode.valueOf(text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryNode binaryNode(byte[] data)
/*     */   {
/* 291 */     return BinaryNode.valueOf(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryNode binaryNode(byte[] data, int offset, int length)
/*     */   {
/* 300 */     return BinaryNode.valueOf(data, offset, length);
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
/*     */   public ArrayNode arrayNode()
/*     */   {
/* 313 */     return new ArrayNode(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectNode objectNode()
/*     */   {
/* 319 */     return new ObjectNode(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode pojoNode(Object pojo)
/*     */   {
/* 328 */     return new POJONode(pojo);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public POJONode POJONode(Object pojo)
/*     */   {
/* 334 */     return new POJONode(pojo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _inIntRange(long l)
/*     */   {
/* 344 */     int i = (int)l;
/* 345 */     long l2 = i;
/* 346 */     return l2 == l;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\JsonNodeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */