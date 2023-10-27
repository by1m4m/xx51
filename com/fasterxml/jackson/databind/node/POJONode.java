/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class POJONode
/*     */   extends ValueNode
/*     */ {
/*     */   protected final Object _value;
/*     */   
/*     */   public POJONode(Object v)
/*     */   {
/*  19 */     this._value = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNodeType getNodeType()
/*     */   {
/*  30 */     return JsonNodeType.POJO;
/*     */   }
/*     */   
/*  33 */   public JsonToken asToken() { return JsonToken.VALUE_EMBEDDED_OBJECT; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] binaryValue()
/*     */     throws IOException
/*     */   {
/*  43 */     if ((this._value instanceof byte[])) {
/*  44 */       return (byte[])this._value;
/*     */     }
/*  46 */     return super.binaryValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String asText()
/*     */   {
/*  56 */     return this._value == null ? "null" : this._value.toString();
/*     */   }
/*     */   
/*  59 */   public String asText(String defaultValue) { return this._value == null ? defaultValue : this._value.toString(); }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean asBoolean(boolean defaultValue)
/*     */   {
/*  65 */     if ((this._value != null) && ((this._value instanceof Boolean))) {
/*  66 */       return ((Boolean)this._value).booleanValue();
/*     */     }
/*  68 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public int asInt(int defaultValue)
/*     */   {
/*  74 */     if ((this._value instanceof Number)) {
/*  75 */       return ((Number)this._value).intValue();
/*     */     }
/*  77 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public long asLong(long defaultValue)
/*     */   {
/*  83 */     if ((this._value instanceof Number)) {
/*  84 */       return ((Number)this._value).longValue();
/*     */     }
/*  86 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public double asDouble(double defaultValue)
/*     */   {
/*  92 */     if ((this._value instanceof Number)) {
/*  93 */       return ((Number)this._value).doubleValue();
/*     */     }
/*  95 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 108 */     if (this._value == null) {
/* 109 */       provider.defaultSerializeNull(jg);
/*     */     } else {
/* 111 */       jg.writeObject(this._value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getPojo()
/*     */   {
/* 124 */     return this._value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 135 */     if (o == this) return true;
/* 136 */     if (o == null) return false;
/* 137 */     if ((o instanceof POJONode)) {
/* 138 */       return _pojoEquals((POJONode)o);
/*     */     }
/* 140 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _pojoEquals(POJONode other)
/*     */   {
/* 148 */     if (this._value == null) {
/* 149 */       return other._value == null;
/*     */     }
/* 151 */     return this._value.equals(other._value);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 155 */     return this._value.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 160 */     return String.valueOf(this._value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\POJONode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */