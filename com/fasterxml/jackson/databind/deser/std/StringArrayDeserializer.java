/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class StringArrayDeserializer
/*     */   extends StdDeserializer<String[]>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  24 */   public static final StringArrayDeserializer instance = new StringArrayDeserializer();
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<String> _elementDeserializer;
/*     */   
/*     */ 
/*     */   public StringArrayDeserializer()
/*     */   {
/*  32 */     super(String[].class);
/*  33 */     this._elementDeserializer = null;
/*     */   }
/*     */   
/*     */   protected StringArrayDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  38 */     super(String[].class);
/*  39 */     this._elementDeserializer = deser;
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  46 */     if (!jp.isExpectedStartArrayToken()) {
/*  47 */       return handleNonArray(jp, ctxt);
/*     */     }
/*  49 */     if (this._elementDeserializer != null) {
/*  50 */       return _deserializeCustom(jp, ctxt);
/*     */     }
/*     */     
/*  53 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/*  54 */     Object[] chunk = buffer.resetAndStart();
/*     */     
/*  56 */     int ix = 0;
/*     */     try
/*     */     {
/*     */       for (;;) {
/*  60 */         String value = jp.nextTextValue();
/*  61 */         if (value == null) {
/*  62 */           JsonToken t = jp.getCurrentToken();
/*  63 */           if (t == JsonToken.END_ARRAY) {
/*     */             break;
/*     */           }
/*  66 */           if (t != JsonToken.VALUE_NULL) {
/*  67 */             value = _parseString(jp, ctxt);
/*     */           }
/*     */         }
/*  70 */         if (ix >= chunk.length) {
/*  71 */           chunk = buffer.appendCompletedChunk(chunk);
/*  72 */           ix = 0;
/*     */         }
/*  74 */         chunk[(ix++)] = value;
/*     */       }
/*     */     } catch (Exception e) {
/*  77 */       throw JsonMappingException.wrapWithPath(e, chunk, buffer.bufferedSize() + ix);
/*     */     }
/*  79 */     String[] result = (String[])buffer.completeAndClearBuffer(chunk, ix, String.class);
/*  80 */     ctxt.returnObjectBuffer(buffer);
/*  81 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  89 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/*  90 */     Object[] chunk = buffer.resetAndStart();
/*  91 */     JsonDeserializer<String> deser = this._elementDeserializer;
/*     */     
/*  93 */     int ix = 0;
/*     */     
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */         String value;
/*     */         
/*     */         String value;
/*     */         
/* 103 */         if (jp.nextTextValue() == null) {
/* 104 */           JsonToken t = jp.getCurrentToken();
/* 105 */           if (t == JsonToken.END_ARRAY) {
/*     */             break;
/*     */           }
/*     */           
/* 109 */           value = t == JsonToken.VALUE_NULL ? (String)deser.getNullValue() : (String)deser.deserialize(jp, ctxt);
/*     */         } else {
/* 111 */           value = (String)deser.deserialize(jp, ctxt);
/*     */         }
/* 113 */         if (ix >= chunk.length) {
/* 114 */           chunk = buffer.appendCompletedChunk(chunk);
/* 115 */           ix = 0;
/*     */         }
/* 117 */         chunk[(ix++)] = value;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 121 */       throw JsonMappingException.wrapWithPath(e, String.class, ix);
/*     */     }
/* 123 */     String[] result = (String[])buffer.completeAndClearBuffer(chunk, ix, String.class);
/* 124 */     ctxt.returnObjectBuffer(buffer);
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException
/*     */   {
/* 130 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */   
/*     */   private final String[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 136 */     if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
/*     */     {
/* 138 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 140 */         String str = jp.getText();
/* 141 */         if (str.length() == 0) {
/* 142 */           return null;
/*     */         }
/*     */       }
/* 145 */       throw ctxt.mappingException(this._valueClass);
/*     */     }
/* 147 */     return new String[] { jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : _parseString(jp, ctxt) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 157 */     JsonDeserializer<?> deser = this._elementDeserializer;
/*     */     
/* 159 */     deser = findConvertingContentDeserializer(ctxt, property, deser);
/* 160 */     JavaType type = ctxt.constructType(String.class);
/* 161 */     if (deser == null) {
/* 162 */       deser = ctxt.findContextualValueDeserializer(type, property);
/*     */     } else {
/* 164 */       deser = ctxt.handleSecondaryContextualization(deser, property, type);
/*     */     }
/*     */     
/* 167 */     if ((deser != null) && (isDefaultDeserializer(deser))) {
/* 168 */       deser = null;
/*     */     }
/* 170 */     if (this._elementDeserializer != deser) {
/* 171 */       return new StringArrayDeserializer(deser);
/*     */     }
/* 173 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StringArrayDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */