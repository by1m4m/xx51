/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DecimalNode
/*     */   extends NumericNode
/*     */ {
/*  17 */   public static final DecimalNode ZERO = new DecimalNode(BigDecimal.ZERO);
/*     */   
/*  19 */   private static final BigDecimal MIN_INTEGER = BigDecimal.valueOf(-2147483648L);
/*  20 */   private static final BigDecimal MAX_INTEGER = BigDecimal.valueOf(2147483647L);
/*  21 */   private static final BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);
/*  22 */   private static final BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BigDecimal _value;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public DecimalNode(BigDecimal v) { this._value = v; }
/*     */   
/*  34 */   public static DecimalNode valueOf(BigDecimal d) { return new DecimalNode(d); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  42 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*     */   }
/*     */   
/*  45 */   public JsonParser.NumberType numberType() { return JsonParser.NumberType.BIG_DECIMAL; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFloatingPointNumber()
/*     */   {
/*  54 */     return true;
/*     */   }
/*     */   
/*  57 */   public boolean isBigDecimal() { return true; }
/*     */   
/*     */   public boolean canConvertToInt() {
/*  60 */     return (this._value.compareTo(MIN_INTEGER) >= 0) && (this._value.compareTo(MAX_INTEGER) <= 0);
/*     */   }
/*     */   
/*  63 */   public boolean canConvertToLong() { return (this._value.compareTo(MIN_LONG) >= 0) && (this._value.compareTo(MAX_LONG) <= 0); }
/*     */   
/*     */   public Number numberValue()
/*     */   {
/*  67 */     return this._value;
/*     */   }
/*     */   
/*  70 */   public short shortValue() { return this._value.shortValue(); }
/*     */   
/*     */   public int intValue() {
/*  73 */     return this._value.intValue();
/*     */   }
/*     */   
/*  76 */   public long longValue() { return this._value.longValue(); }
/*     */   
/*     */   public BigInteger bigIntegerValue()
/*     */   {
/*  80 */     return this._value.toBigInteger();
/*     */   }
/*     */   
/*  83 */   public float floatValue() { return this._value.floatValue(); }
/*     */   
/*     */   public double doubleValue() {
/*  86 */     return this._value.doubleValue();
/*     */   }
/*     */   
/*  89 */   public BigDecimal decimalValue() { return this._value; }
/*     */   
/*     */   public String asText()
/*     */   {
/*  93 */     return this._value.toString();
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
/*     */   public final void serialize(JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 110 */     jgen.writeNumber(this._value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 116 */     if (o == this) return true;
/* 117 */     if (o == null) return false;
/* 118 */     if ((o instanceof DecimalNode)) {
/* 119 */       return ((DecimalNode)o)._value.compareTo(this._value) == 0;
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 125 */     return Double.valueOf(doubleValue()).hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\DecimalNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */