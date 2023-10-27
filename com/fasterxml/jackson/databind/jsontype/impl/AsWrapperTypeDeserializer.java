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
/*     */ public class AsWrapperTypeDeserializer
/*     */   extends TypeDeserializerBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5345570420394408290L;
/*     */   
/*     */   public AsWrapperTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl)
/*     */   {
/*  29 */     super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
/*     */   }
/*     */   
/*     */   protected AsWrapperTypeDeserializer(AsWrapperTypeDeserializer src, BeanProperty property) {
/*  33 */     super(src, property);
/*     */   }
/*     */   
/*     */   public TypeDeserializer forProperty(BeanProperty prop)
/*     */   {
/*  38 */     return prop == this._property ? this : new AsWrapperTypeDeserializer(this, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  42 */     return JsonTypeInfo.As.WRAPPER_OBJECT;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  49 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */   public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/*  54 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */   public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/*  59 */     return _deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/*  64 */     return _deserialize(jp, ctxt);
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
/*  82 */     if (jp.canReadTypeId()) {
/*  83 */       Object typeId = jp.getTypeId();
/*  84 */       if (typeId != null) {
/*  85 */         return _deserializeWithNativeTypeId(jp, ctxt, typeId);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  90 */     if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
/*  91 */       throw ctxt.wrongTokenException(jp, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class " + baseTypeName());
/*     */     }
/*     */     
/*     */ 
/*  95 */     if (jp.nextToken() != JsonToken.FIELD_NAME) {
/*  96 */       throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
/*     */     }
/*     */     
/*  99 */     String typeId = jp.getText();
/* 100 */     JsonDeserializer<Object> deser = _findDeserializer(ctxt, typeId);
/* 101 */     jp.nextToken();
/*     */     
/*     */ 
/* 104 */     if ((this._typeIdVisible) && (jp.getCurrentToken() == JsonToken.START_OBJECT))
/*     */     {
/* 106 */       TokenBuffer tb = new TokenBuffer(null, false);
/* 107 */       tb.writeStartObject();
/* 108 */       tb.writeFieldName(this._typePropertyName);
/* 109 */       tb.writeString(typeId);
/* 110 */       jp = JsonParserSequence.createFlattened(tb.asParser(jp), jp);
/* 111 */       jp.nextToken();
/*     */     }
/*     */     
/* 114 */     Object value = deser.deserialize(jp, ctxt);
/*     */     
/* 116 */     if (jp.nextToken() != JsonToken.END_OBJECT) {
/* 117 */       throw ctxt.wrongTokenException(jp, JsonToken.END_OBJECT, "expected closing END_OBJECT after type information and deserialized value");
/*     */     }
/*     */     
/* 120 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsWrapperTypeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */