/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.util.JsonParserSequence;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsArrayTypeDeserializer
/*     */   extends TypeDeserializerBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public AsArrayTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl)
/*     */   {
/*  29 */     super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
/*     */   }
/*     */   
/*     */   public AsArrayTypeDeserializer(AsArrayTypeDeserializer src, BeanProperty property) {
/*  33 */     super(src, property);
/*     */   }
/*     */   
/*     */ 
/*     */   public TypeDeserializer forProperty(BeanProperty prop)
/*     */   {
/*  39 */     return prop == this._property ? this : new AsArrayTypeDeserializer(this, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  43 */     return JsonTypeInfo.As.WRAPPER_ARRAY;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  50 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  58 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */   public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/*  63 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/*  68 */     return _deserialize(jp, ctxt);
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
/*     */   private final Object _deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  86 */     if (jp.canReadTypeId()) {
/*  87 */       Object typeId = jp.getTypeId();
/*  88 */       if (typeId != null) {
/*  89 */         return _deserializeWithNativeTypeId(jp, ctxt, typeId);
/*     */       }
/*     */     }
/*  92 */     boolean hadStartArray = jp.isExpectedStartArrayToken();
/*  93 */     String typeId = _locateTypeId(jp, ctxt);
/*  94 */     JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
/*     */     
/*  96 */     if ((this._typeIdVisible) && (!_usesExternalId()) && (jp.getCurrentToken() == JsonToken.START_OBJECT))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */       TokenBuffer tb = new TokenBuffer(null, false);
/* 104 */       tb.writeStartObject();
/* 105 */       tb.writeFieldName(this._typePropertyName);
/* 106 */       tb.writeString(typeId);
/* 107 */       jp = JsonParserSequence.createFlattened(tb.asParser(jp), jp);
/* 108 */       jp.nextToken();
/*     */     }
/* 110 */     Object value = deser.deserialize(jp, ctxt);
/*     */     
/* 112 */     if ((hadStartArray) && (jp.nextToken() != JsonToken.END_ARRAY)) {
/* 113 */       throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "expected closing END_ARRAY after type information and deserialized value");
/*     */     }
/*     */     
/* 116 */     return value;
/*     */   }
/*     */   
/*     */   protected final String _locateTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/* 121 */     if (!jp.isExpectedStartArrayToken())
/*     */     {
/*     */ 
/* 124 */       if (this._defaultImpl != null) {
/* 125 */         return this._idResolver.idFromBaseType();
/*     */       }
/* 127 */       throw ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "need JSON Array to contain As.WRAPPER_ARRAY type information for class " + baseTypeName());
/*     */     }
/*     */     
/* 130 */     JsonToken t = jp.nextToken();
/* 131 */     if (t == JsonToken.VALUE_STRING) {
/* 132 */       String result = jp.getText();
/* 133 */       jp.nextToken();
/* 134 */       return result;
/*     */     }
/* 136 */     if (this._defaultImpl != null) {
/* 137 */       return this._idResolver.idFromBaseType();
/*     */     }
/* 139 */     throw ctxt.wrongTokenException(jp, JsonToken.VALUE_STRING, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean _usesExternalId()
/*     */   {
/* 146 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsArrayTypeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */