/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
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
/*     */ public class ArrayBlockingQueueDeserializer
/*     */   extends CollectionDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public ArrayBlockingQueueDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeser)
/*     */   {
/*  37 */     super(collectionType, valueDeser, valueTypeDeser, valueInstantiator, delegateDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayBlockingQueueDeserializer(ArrayBlockingQueueDeserializer src)
/*     */   {
/*  45 */     super(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayBlockingQueueDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd)
/*     */   {
/*  56 */     if ((dd == this._delegateDeserializer) && (vd == this._valueDeserializer) && (vtd == this._valueTypeDeserializer)) {
/*  57 */       return this;
/*     */     }
/*  59 */     return new ArrayBlockingQueueDeserializer(this._collectionType, vd, vtd, this._valueInstantiator, dd);
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
/*     */   public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  75 */     if (this._delegateDeserializer != null) {
/*  76 */       return (Collection)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*     */     }
/*     */     
/*  79 */     if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
/*  80 */       String str = jp.getText();
/*  81 */       if (str.length() == 0) {
/*  82 */         return (Collection)this._valueInstantiator.createFromString(ctxt, str);
/*     */       }
/*     */     }
/*  85 */     return deserialize(jp, ctxt, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<Object> result0)
/*     */     throws IOException
/*     */   {
/*  92 */     if (!jp.isExpectedStartArrayToken()) {
/*  93 */       return handleNonArray(jp, ctxt, new ArrayBlockingQueue(1));
/*     */     }
/*  95 */     ArrayList<Object> tmp = new ArrayList();
/*     */     
/*  97 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/*     */     
/*  99 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     try {
/*     */       JsonToken t;
/* 102 */       while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
/*     */         Object value;
/*     */         Object value;
/* 105 */         if (t == JsonToken.VALUE_NULL) {
/* 106 */           value = valueDes.getNullValue(); } else { Object value;
/* 107 */           if (typeDeser == null) {
/* 108 */             value = valueDes.deserialize(jp, ctxt);
/*     */           } else
/* 110 */             value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */         }
/* 112 */         tmp.add(value);
/*     */       }
/*     */     } catch (Exception e) {
/* 115 */       throw JsonMappingException.wrapWithPath(e, tmp, tmp.size());
/*     */     }
/* 117 */     if (result0 != null) {
/* 118 */       result0.addAll(tmp);
/* 119 */       return result0;
/*     */     }
/* 121 */     return new ArrayBlockingQueue(tmp.size(), false, tmp);
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 127 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\ArrayBlockingQueueDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */