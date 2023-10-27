/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumMap;
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
/*     */ public class EnumMapDeserializer
/*     */   extends ContainerDeserializerBase<EnumMap<?, ?>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _mapType;
/*     */   protected final Class<?> _enumClass;
/*     */   protected KeyDeserializer _keyDeserializer;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */   public EnumMapDeserializer(JavaType mapType, KeyDeserializer keyDeserializer, JsonDeserializer<?> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  46 */     super(mapType);
/*  47 */     this._mapType = mapType;
/*  48 */     this._enumClass = mapType.getKeyType().getRawClass();
/*  49 */     this._keyDeserializer = keyDeserializer;
/*  50 */     this._valueDeserializer = valueDeser;
/*  51 */     this._valueTypeDeserializer = valueTypeDeser;
/*     */   }
/*     */   
/*     */   public EnumMapDeserializer withResolved(KeyDeserializer keyDeserializer, JsonDeserializer<?> valueDeserializer, TypeDeserializer valueTypeDeser)
/*     */   {
/*  56 */     if ((keyDeserializer == this._keyDeserializer) && (valueDeserializer == this._valueDeserializer) && (valueTypeDeser == this._valueTypeDeserializer)) {
/*  57 */       return this;
/*     */     }
/*  59 */     return new EnumMapDeserializer(this._mapType, keyDeserializer, valueDeserializer, this._valueTypeDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  72 */     KeyDeserializer kd = this._keyDeserializer;
/*  73 */     if (kd == null) {
/*  74 */       kd = ctxt.findKeyDeserializer(this._mapType.getKeyType(), property);
/*     */     }
/*  76 */     JsonDeserializer<?> vd = this._valueDeserializer;
/*  77 */     JavaType vt = this._mapType.getContentType();
/*  78 */     if (vd == null) {
/*  79 */       vd = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/*  81 */       vd = ctxt.handleSecondaryContextualization(vd, property, vt);
/*     */     }
/*  83 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/*  84 */     if (vtd != null) {
/*  85 */       vtd = vtd.forProperty(property);
/*     */     }
/*  87 */     return withResolved(kd, vd, vtd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/*  97 */     return (this._valueDeserializer == null) && (this._keyDeserializer == null) && (this._valueTypeDeserializer == null);
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
/*     */   public JavaType getContentType()
/*     */   {
/* 110 */     return this._mapType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 115 */     return this._valueDeserializer;
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
/*     */   public EnumMap<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 129 */     if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
/* 130 */       return (EnumMap)_deserializeFromEmpty(jp, ctxt);
/*     */     }
/* 132 */     EnumMap result = constructMap();
/* 133 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 134 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 136 */     while (jp.nextToken() == JsonToken.FIELD_NAME) {
/* 137 */       String keyName = jp.getCurrentName();
/*     */       
/* 139 */       Enum<?> key = (Enum)this._keyDeserializer.deserializeKey(keyName, ctxt);
/* 140 */       if (key == null) {
/* 141 */         if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 142 */           throw ctxt.weirdStringException(keyName, this._enumClass, "value not one of declared Enum instance names for " + this._mapType.getKeyType());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 148 */         jp.nextToken();
/* 149 */         jp.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 153 */         JsonToken t = jp.nextToken();
/*     */         
/*     */         Object value;
/*     */         
/*     */         try
/*     */         {
/*     */           Object value;
/* 160 */           if (t == JsonToken.VALUE_NULL) {
/* 161 */             value = valueDes.getNullValue(); } else { Object value;
/* 162 */             if (typeDeser == null) {
/* 163 */               value = valueDes.deserialize(jp, ctxt);
/*     */             } else
/* 165 */               value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */           }
/*     */         } catch (Exception e) {
/* 168 */           wrapAndThrow(e, result, keyName);
/* 169 */           return null;
/*     */         }
/* 171 */         result.put(key, value);
/*     */       } }
/* 173 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 181 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */   
/*     */   protected EnumMap<?, ?> constructMap() {
/* 185 */     return new EnumMap(this._enumClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\EnumMapDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */