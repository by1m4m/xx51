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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsPropertyTypeDeserializer
/*     */   extends AsArrayTypeDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JsonTypeInfo.As _inclusion;
/*     */   
/*     */   public AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl)
/*     */   {
/*  32 */     this(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, JsonTypeInfo.As.PROPERTY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AsPropertyTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl, JsonTypeInfo.As inclusion)
/*     */   {
/*  39 */     super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
/*  40 */     this._inclusion = inclusion;
/*     */   }
/*     */   
/*     */   public AsPropertyTypeDeserializer(AsPropertyTypeDeserializer src, BeanProperty property) {
/*  44 */     super(src, property);
/*  45 */     this._inclusion = src._inclusion;
/*     */   }
/*     */   
/*     */   public TypeDeserializer forProperty(BeanProperty prop)
/*     */   {
/*  50 */     return prop == this._property ? this : new AsPropertyTypeDeserializer(this, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  54 */     return this._inclusion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  65 */     if (jp.canReadTypeId()) {
/*  66 */       Object typeId = jp.getTypeId();
/*  67 */       if (typeId != null) {
/*  68 */         return _deserializeWithNativeTypeId(jp, ctxt, typeId);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  73 */     JsonToken t = jp.getCurrentToken();
/*  74 */     if (t == JsonToken.START_OBJECT) {
/*  75 */       t = jp.nextToken();
/*  76 */     } else { if (t == JsonToken.START_ARRAY)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */         return _deserializeTypedUsingDefaultImpl(jp, ctxt, null); }
/*  85 */       if (t != JsonToken.FIELD_NAME) {
/*  86 */         return _deserializeTypedUsingDefaultImpl(jp, ctxt, null);
/*     */       }
/*     */     }
/*  89 */     TokenBuffer tb = null;
/*  91 */     for (; 
/*  91 */         t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/*  92 */       String name = jp.getCurrentName();
/*  93 */       jp.nextToken();
/*  94 */       if (this._typePropertyName.equals(name)) {
/*  95 */         return _deserializeTypedForId(jp, ctxt, tb);
/*     */       }
/*  97 */       if (tb == null) {
/*  98 */         tb = new TokenBuffer(null, false);
/*     */       }
/* 100 */       tb.writeFieldName(name);
/* 101 */       tb.copyCurrentStructure(jp);
/*     */     }
/* 103 */     return _deserializeTypedUsingDefaultImpl(jp, ctxt, tb);
/*     */   }
/*     */   
/*     */   protected final Object _deserializeTypedForId(JsonParser jp, DeserializationContext ctxt, TokenBuffer tb)
/*     */     throws IOException
/*     */   {
/* 109 */     String typeId = jp.getText();
/* 110 */     JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
/* 111 */     if (this._typeIdVisible) {
/* 112 */       if (tb == null) {
/* 113 */         tb = new TokenBuffer(null, false);
/*     */       }
/* 115 */       tb.writeFieldName(jp.getCurrentName());
/* 116 */       tb.writeString(typeId);
/*     */     }
/* 118 */     if (tb != null) {
/* 119 */       jp = JsonParserSequence.createFlattened(tb.asParser(jp), jp);
/*     */     }
/*     */     
/* 122 */     jp.nextToken();
/*     */     
/* 124 */     return deser.deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object _deserializeTypedUsingDefaultImpl(JsonParser jp, DeserializationContext ctxt, TokenBuffer tb)
/*     */     throws IOException
/*     */   {
/* 132 */     JsonDeserializer<Object> deser = _findDefaultImplDeserializer(ctxt);
/* 133 */     if (deser != null) {
/* 134 */       if (tb != null) {
/* 135 */         tb.writeEndObject();
/* 136 */         jp = tb.asParser(jp);
/*     */         
/* 138 */         jp.nextToken();
/*     */       }
/* 140 */       return deser.deserialize(jp, ctxt);
/*     */     }
/*     */     
/* 143 */     Object result = TypeDeserializer.deserializeIfNatural(jp, ctxt, this._baseType);
/* 144 */     if (result != null) {
/* 145 */       return result;
/*     */     }
/*     */     
/* 148 */     if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
/* 149 */       return super.deserializeTypedFromAny(jp, ctxt);
/*     */     }
/* 151 */     throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "missing property '" + this._typePropertyName + "' that is to contain type id  (for class " + baseTypeName() + ")");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 164 */     if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
/* 165 */       return super.deserializeTypedFromArray(jp, ctxt);
/*     */     }
/* 167 */     return deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsPropertyTypeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */