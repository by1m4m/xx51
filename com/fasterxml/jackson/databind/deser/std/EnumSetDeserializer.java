/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
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
/*     */ public class EnumSetDeserializer
/*     */   extends StdDeserializer<EnumSet<?>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _enumType;
/*     */   protected final Class<Enum> _enumClass;
/*     */   protected JsonDeserializer<Enum<?>> _enumDeserializer;
/*     */   
/*     */   public EnumSetDeserializer(JavaType enumType, JsonDeserializer<?> deser)
/*     */   {
/*  39 */     super(EnumSet.class);
/*  40 */     this._enumType = enumType;
/*  41 */     this._enumClass = enumType.getRawClass();
/*  42 */     this._enumDeserializer = deser;
/*     */   }
/*     */   
/*     */   public EnumSetDeserializer withDeserializer(JsonDeserializer<?> deser) {
/*  46 */     if (this._enumDeserializer == deser) {
/*  47 */       return this;
/*     */     }
/*  49 */     return new EnumSetDeserializer(this._enumType, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/*  59 */     if (this._enumType.getValueHandler() != null) {
/*  60 */       return false;
/*     */     }
/*  62 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  69 */     JsonDeserializer<?> deser = this._enumDeserializer;
/*  70 */     if (deser == null) {
/*  71 */       deser = ctxt.findContextualValueDeserializer(this._enumType, property);
/*     */     } else {
/*  73 */       deser = ctxt.handleSecondaryContextualization(deser, property, this._enumType);
/*     */     }
/*  75 */     return withDeserializer(deser);
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
/*     */   public EnumSet<?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  90 */     if (!jp.isExpectedStartArrayToken()) {
/*  91 */       throw ctxt.mappingException(EnumSet.class);
/*     */     }
/*  93 */     EnumSet result = constructSet();
/*     */     try
/*     */     {
/*     */       JsonToken t;
/*  97 */       while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */         if (t == JsonToken.VALUE_NULL) {
/* 104 */           throw ctxt.mappingException(this._enumClass);
/*     */         }
/* 106 */         Enum<?> value = (Enum)this._enumDeserializer.deserialize(jp, ctxt);
/*     */         
/*     */ 
/*     */ 
/* 110 */         if (value != null) {
/* 111 */           result.add(value);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 115 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     }
/* 117 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 125 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private EnumSet constructSet()
/*     */   {
/* 132 */     return EnumSet.noneOf(this._enumClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\EnumSetDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */