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
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class ObjectArrayDeserializer
/*     */   extends ContainerDeserializerBase<Object[]>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ArrayType _arrayType;
/*     */   protected final boolean _untyped;
/*     */   protected final Class<?> _elementClass;
/*     */   protected JsonDeserializer<Object> _elementDeserializer;
/*     */   protected final TypeDeserializer _elementTypeDeserializer;
/*     */   
/*     */   public ObjectArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser)
/*     */   {
/*  63 */     super(arrayType);
/*  64 */     this._arrayType = arrayType;
/*  65 */     this._elementClass = arrayType.getContentType().getRawClass();
/*  66 */     this._untyped = (this._elementClass == Object.class);
/*  67 */     this._elementDeserializer = elemDeser;
/*  68 */     this._elementTypeDeserializer = elemTypeDeser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectArrayDeserializer withDeserializer(TypeDeserializer elemTypeDeser, JsonDeserializer<?> elemDeser)
/*     */   {
/*  78 */     if ((elemDeser == this._elementDeserializer) && (elemTypeDeser == this._elementTypeDeserializer)) {
/*  79 */       return this;
/*     */     }
/*  81 */     return new ObjectArrayDeserializer(this._arrayType, elemDeser, elemTypeDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  89 */     JsonDeserializer<?> deser = this._elementDeserializer;
/*     */     
/*  91 */     deser = findConvertingContentDeserializer(ctxt, property, deser);
/*  92 */     JavaType vt = this._arrayType.getContentType();
/*  93 */     if (deser == null) {
/*  94 */       deser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/*  96 */       deser = ctxt.handleSecondaryContextualization(deser, property, vt);
/*     */     }
/*  98 */     TypeDeserializer elemTypeDeser = this._elementTypeDeserializer;
/*  99 */     if (elemTypeDeser != null) {
/* 100 */       elemTypeDeser = elemTypeDeser.forProperty(property);
/*     */     }
/* 102 */     return withDeserializer(elemTypeDeser, deser);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 108 */     return (this._elementDeserializer == null) && (this._elementTypeDeserializer == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 119 */     return this._arrayType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 124 */     return this._elementDeserializer;
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
/*     */   public Object[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 138 */     if (!jp.isExpectedStartArrayToken()) {
/* 139 */       return handleNonArray(jp, ctxt);
/*     */     }
/*     */     
/* 142 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 143 */     Object[] chunk = buffer.resetAndStart();
/* 144 */     int ix = 0;
/*     */     
/* 146 */     TypeDeserializer typeDeser = this._elementTypeDeserializer;
/*     */     try {
/*     */       JsonToken t;
/* 149 */       while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */       {
/*     */         Object value;
/*     */         Object value;
/* 153 */         if (t == JsonToken.VALUE_NULL) {
/* 154 */           value = this._elementDeserializer.getNullValue(); } else { Object value;
/* 155 */           if (typeDeser == null) {
/* 156 */             value = this._elementDeserializer.deserialize(jp, ctxt);
/*     */           } else
/* 158 */             value = this._elementDeserializer.deserializeWithType(jp, ctxt, typeDeser);
/*     */         }
/* 160 */         if (ix >= chunk.length) {
/* 161 */           chunk = buffer.appendCompletedChunk(chunk);
/* 162 */           ix = 0;
/*     */         }
/* 164 */         chunk[(ix++)] = value;
/*     */       }
/*     */     } catch (Exception e) {
/* 167 */       throw JsonMappingException.wrapWithPath(e, chunk, buffer.bufferedSize() + ix);
/*     */     }
/*     */     
/*     */     Object[] result;
/*     */     Object[] result;
/* 172 */     if (this._untyped) {
/* 173 */       result = buffer.completeAndClearBuffer(chunk, ix);
/*     */     } else {
/* 175 */       result = buffer.completeAndClearBuffer(chunk, ix, this._elementClass);
/*     */     }
/* 177 */     ctxt.returnObjectBuffer(buffer);
/* 178 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 189 */     return (Object[])typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Byte[] deserializeFromBase64(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 202 */     byte[] b = jp.getBinaryValue(ctxt.getBase64Variant());
/*     */     
/* 204 */     Byte[] result = new Byte[b.length];
/* 205 */     int i = 0; for (int len = b.length; i < len; i++) {
/* 206 */       result[i] = Byte.valueOf(b[i]);
/*     */     }
/* 208 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private final Object[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 215 */     if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */     {
/* 217 */       String str = jp.getText();
/* 218 */       if (str.length() == 0) {
/* 219 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 224 */     if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
/*     */     {
/*     */ 
/*     */ 
/* 228 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (this._elementClass == Byte.class))
/*     */       {
/* 230 */         return deserializeFromBase64(jp, ctxt);
/*     */       }
/* 232 */       throw ctxt.mappingException(this._arrayType.getRawClass());
/*     */     }
/* 234 */     JsonToken t = jp.getCurrentToken();
/*     */     Object value;
/*     */     Object value;
/* 237 */     if (t == JsonToken.VALUE_NULL) {
/* 238 */       value = this._elementDeserializer.getNullValue(); } else { Object value;
/* 239 */       if (this._elementTypeDeserializer == null) {
/* 240 */         value = this._elementDeserializer.deserialize(jp, ctxt);
/*     */       } else {
/* 242 */         value = this._elementDeserializer.deserializeWithType(jp, ctxt, this._elementTypeDeserializer);
/*     */       }
/*     */     }
/*     */     Object[] result;
/*     */     Object[] result;
/* 247 */     if (this._untyped) {
/* 248 */       result = new Object[1];
/*     */     } else {
/* 250 */       result = (Object[])Array.newInstance(this._elementClass, 1);
/*     */     }
/* 252 */     result[0] = value;
/* 253 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\ObjectArrayDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */