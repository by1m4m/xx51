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
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class UntypedObjectDeserializer
/*     */   extends StdDeserializer<Object>
/*     */   implements ResolvableDeserializer, ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   protected static final Object[] NO_OBJECTS = new Object[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  44 */   public static final UntypedObjectDeserializer instance = new UntypedObjectDeserializer();
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<Object> _mapDeserializer;
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<Object> _listDeserializer;
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<Object> _stringDeserializer;
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<Object> _numberDeserializer;
/*     */   
/*     */ 
/*     */   public UntypedObjectDeserializer()
/*     */   {
/*  61 */     super(Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UntypedObjectDeserializer(UntypedObjectDeserializer base, JsonDeserializer<?> mapDeser, JsonDeserializer<?> listDeser, JsonDeserializer<?> stringDeser, JsonDeserializer<?> numberDeser)
/*     */   {
/*  69 */     super(Object.class);
/*  70 */     this._mapDeserializer = mapDeser;
/*  71 */     this._listDeserializer = listDeser;
/*  72 */     this._stringDeserializer = stringDeser;
/*  73 */     this._numberDeserializer = numberDeser;
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
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/*  91 */     JavaType obType = ctxt.constructType(Object.class);
/*  92 */     JavaType stringType = ctxt.constructType(String.class);
/*  93 */     TypeFactory tf = ctxt.getTypeFactory();
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
/* 105 */     this._mapDeserializer = _findCustomDeser(ctxt, tf.constructMapType(Map.class, stringType, obType));
/* 106 */     this._listDeserializer = _findCustomDeser(ctxt, tf.constructCollectionType(List.class, obType));
/* 107 */     this._stringDeserializer = _findCustomDeser(ctxt, stringType);
/* 108 */     this._numberDeserializer = _findCustomDeser(ctxt, tf.constructType(Number.class));
/*     */     
/*     */ 
/*     */ 
/* 112 */     JavaType unknown = TypeFactory.unknownType();
/* 113 */     this._mapDeserializer = ctxt.handleSecondaryContextualization(this._mapDeserializer, null, unknown);
/* 114 */     this._listDeserializer = ctxt.handleSecondaryContextualization(this._listDeserializer, null, unknown);
/* 115 */     this._stringDeserializer = ctxt.handleSecondaryContextualization(this._stringDeserializer, null, unknown);
/* 116 */     this._numberDeserializer = ctxt.handleSecondaryContextualization(this._numberDeserializer, null, unknown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _findCustomDeser(DeserializationContext ctxt, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 125 */     JsonDeserializer<?> deser = ctxt.findNonContextualValueDeserializer(type);
/* 126 */     if (ClassUtil.isJacksonStdImpl(deser)) {
/* 127 */       return null;
/*     */     }
/* 129 */     return deser;
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
/* 142 */     if ((this._stringDeserializer == null) && (this._numberDeserializer == null) && (this._mapDeserializer == null) && (this._listDeserializer == null) && (getClass() == UntypedObjectDeserializer.class))
/*     */     {
/*     */ 
/* 145 */       return Vanilla.std;
/*     */     }
/* 147 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<?> _withResolved(JsonDeserializer<?> mapDeser, JsonDeserializer<?> listDeser, JsonDeserializer<?> stringDeser, JsonDeserializer<?> numberDeser)
/*     */   {
/* 153 */     return new UntypedObjectDeserializer(this, mapDeser, listDeser, stringDeser, numberDeser);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 173 */     return true;
/*     */   }
/*     */   
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 179 */     switch (jp.getCurrentToken()) {
/*     */     case FIELD_NAME: 
/*     */     case START_OBJECT: 
/* 182 */       if (this._mapDeserializer != null) {
/* 183 */         return this._mapDeserializer.deserialize(jp, ctxt);
/*     */       }
/* 185 */       return mapObject(jp, ctxt);
/*     */     case START_ARRAY: 
/* 187 */       if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
/* 188 */         return mapArrayToArray(jp, ctxt);
/*     */       }
/* 190 */       if (this._listDeserializer != null) {
/* 191 */         return this._listDeserializer.deserialize(jp, ctxt);
/*     */       }
/* 193 */       return mapArray(jp, ctxt);
/*     */     case VALUE_EMBEDDED_OBJECT: 
/* 195 */       return jp.getEmbeddedObject();
/*     */     case VALUE_STRING: 
/* 197 */       if (this._stringDeserializer != null) {
/* 198 */         return this._stringDeserializer.deserialize(jp, ctxt);
/*     */       }
/* 200 */       return jp.getText();
/*     */     
/*     */     case VALUE_NUMBER_INT: 
/* 203 */       if (this._numberDeserializer != null) {
/* 204 */         return this._numberDeserializer.deserialize(jp, ctxt);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 209 */       if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 210 */         return jp.getBigIntegerValue();
/*     */       }
/* 212 */       return jp.getNumberValue();
/*     */     
/*     */     case VALUE_NUMBER_FLOAT: 
/* 215 */       if (this._numberDeserializer != null) {
/* 216 */         return this._numberDeserializer.deserialize(jp, ctxt);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 221 */       if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 222 */         return jp.getDecimalValue();
/*     */       }
/* 224 */       return Double.valueOf(jp.getDoubleValue());
/*     */     
/*     */     case VALUE_TRUE: 
/* 227 */       return Boolean.TRUE;
/*     */     case VALUE_FALSE: 
/* 229 */       return Boolean.FALSE;
/*     */     
/*     */     case VALUE_NULL: 
/* 232 */       return null;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 237 */     throw ctxt.mappingException(Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 244 */     JsonToken t = jp.getCurrentToken();
/* 245 */     switch (t)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */     case FIELD_NAME: 
/*     */     case START_OBJECT: 
/*     */     case START_ARRAY: 
/* 253 */       return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     case VALUE_STRING: 
/* 259 */       if (this._stringDeserializer != null) {
/* 260 */         return this._stringDeserializer.deserialize(jp, ctxt);
/*     */       }
/* 262 */       return jp.getText();
/*     */     
/*     */     case VALUE_NUMBER_INT: 
/* 265 */       if (this._numberDeserializer != null) {
/* 266 */         return this._numberDeserializer.deserialize(jp, ctxt);
/*     */       }
/*     */       
/* 269 */       if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 270 */         return jp.getBigIntegerValue();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 276 */       return jp.getNumberValue();
/*     */     
/*     */     case VALUE_NUMBER_FLOAT: 
/* 279 */       if (this._numberDeserializer != null) {
/* 280 */         return this._numberDeserializer.deserialize(jp, ctxt);
/*     */       }
/*     */       
/* 283 */       if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 284 */         return jp.getDecimalValue();
/*     */       }
/* 286 */       return Double.valueOf(jp.getDoubleValue());
/*     */     
/*     */     case VALUE_TRUE: 
/* 289 */       return Boolean.TRUE;
/*     */     case VALUE_FALSE: 
/* 291 */       return Boolean.FALSE;
/*     */     case VALUE_EMBEDDED_OBJECT: 
/* 293 */       return jp.getEmbeddedObject();
/*     */     
/*     */     case VALUE_NULL: 
/* 296 */       return null; }
/*     */     
/* 298 */     throw ctxt.mappingException(Object.class);
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
/*     */   protected Object mapArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 314 */     if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 315 */       return new ArrayList(2);
/*     */     }
/* 317 */     Object value = deserialize(jp, ctxt);
/* 318 */     if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 319 */       ArrayList<Object> l = new ArrayList(2);
/* 320 */       l.add(value);
/* 321 */       return l;
/*     */     }
/* 323 */     Object value2 = deserialize(jp, ctxt);
/* 324 */     if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 325 */       ArrayList<Object> l = new ArrayList(2);
/* 326 */       l.add(value);
/* 327 */       l.add(value2);
/* 328 */       return l;
/*     */     }
/* 330 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 331 */     Object[] values = buffer.resetAndStart();
/* 332 */     int ptr = 0;
/* 333 */     values[(ptr++)] = value;
/* 334 */     values[(ptr++)] = value2;
/* 335 */     int totalSize = ptr;
/*     */     do {
/* 337 */       value = deserialize(jp, ctxt);
/* 338 */       totalSize++;
/* 339 */       if (ptr >= values.length) {
/* 340 */         values = buffer.appendCompletedChunk(values);
/* 341 */         ptr = 0;
/*     */       }
/* 343 */       values[(ptr++)] = value;
/* 344 */     } while (jp.nextToken() != JsonToken.END_ARRAY);
/*     */     
/* 346 */     ArrayList<Object> result = new ArrayList(totalSize);
/* 347 */     buffer.completeAndClearBuffer(values, ptr, result);
/* 348 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object mapObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 356 */     JsonToken t = jp.getCurrentToken();
/* 357 */     if (t == JsonToken.START_OBJECT) {
/* 358 */       t = jp.nextToken();
/*     */     }
/*     */     
/* 361 */     if (t == JsonToken.END_OBJECT)
/*     */     {
/* 363 */       return new LinkedHashMap(2);
/*     */     }
/* 365 */     String field1 = jp.getCurrentName();
/* 366 */     jp.nextToken();
/* 367 */     Object value1 = deserialize(jp, ctxt);
/* 368 */     if (jp.nextToken() == JsonToken.END_OBJECT) {
/* 369 */       LinkedHashMap<String, Object> result = new LinkedHashMap(2);
/* 370 */       result.put(field1, value1);
/* 371 */       return result;
/*     */     }
/* 373 */     String field2 = jp.getCurrentName();
/* 374 */     jp.nextToken();
/* 375 */     Object value2 = deserialize(jp, ctxt);
/* 376 */     if (jp.nextToken() == JsonToken.END_OBJECT) {
/* 377 */       LinkedHashMap<String, Object> result = new LinkedHashMap(4);
/* 378 */       result.put(field1, value1);
/* 379 */       result.put(field2, value2);
/* 380 */       return result;
/*     */     }
/*     */     
/* 383 */     LinkedHashMap<String, Object> result = new LinkedHashMap();
/* 384 */     result.put(field1, value1);
/* 385 */     result.put(field2, value2);
/*     */     do {
/* 387 */       String fieldName = jp.getCurrentName();
/* 388 */       jp.nextToken();
/* 389 */       result.put(fieldName, deserialize(jp, ctxt));
/* 390 */     } while (jp.nextToken() != JsonToken.END_OBJECT);
/* 391 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object[] mapArrayToArray(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 400 */     if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 401 */       return NO_OBJECTS;
/*     */     }
/* 403 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 404 */     Object[] values = buffer.resetAndStart();
/* 405 */     int ptr = 0;
/*     */     do {
/* 407 */       Object value = deserialize(jp, ctxt);
/* 408 */       if (ptr >= values.length) {
/* 409 */         values = buffer.appendCompletedChunk(values);
/* 410 */         ptr = 0;
/*     */       }
/* 412 */       values[(ptr++)] = value;
/* 413 */     } while (jp.nextToken() != JsonToken.END_ARRAY);
/* 414 */     return buffer.completeAndClearBuffer(values, ptr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class Vanilla
/*     */     extends StdDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 430 */     public static final Vanilla std = new Vanilla();
/*     */     
/* 432 */     public Vanilla() { super(); }
/*     */     
/*     */     public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 437 */       switch (jp.getCurrentTokenId())
/*     */       {
/*     */       case 1: 
/* 440 */         JsonToken t = jp.nextToken();
/* 441 */         if (t == JsonToken.END_OBJECT) {
/* 442 */           return new LinkedHashMap(2);
/*     */         }
/*     */       
/*     */       case 5: 
/* 446 */         return mapObject(jp, ctxt);
/*     */       
/*     */       case 3: 
/* 449 */         JsonToken t = jp.nextToken();
/* 450 */         if (t == JsonToken.END_ARRAY) {
/* 451 */           if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
/* 452 */             return UntypedObjectDeserializer.NO_OBJECTS;
/*     */           }
/* 454 */           return new ArrayList(2);
/*     */         }
/*     */         
/* 457 */         if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
/* 458 */           return mapArrayToArray(jp, ctxt);
/*     */         }
/* 460 */         return mapArray(jp, ctxt);
/*     */       case 12: 
/* 462 */         return jp.getEmbeddedObject();
/*     */       case 6: 
/* 464 */         return jp.getText();
/*     */       
/*     */       case 7: 
/* 467 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 468 */           return jp.getBigIntegerValue();
/*     */         }
/* 470 */         return jp.getNumberValue();
/*     */       
/*     */       case 8: 
/* 473 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 474 */           return jp.getDecimalValue();
/*     */         }
/* 476 */         return Double.valueOf(jp.getDoubleValue());
/*     */       
/*     */       case 9: 
/* 479 */         return Boolean.TRUE;
/*     */       case 10: 
/* 481 */         return Boolean.FALSE;
/*     */       
/*     */       case 11: 
/* 484 */         return null;
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 489 */       throw ctxt.mappingException(Object.class);
/*     */     }
/*     */     
/*     */ 
/*     */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException
/*     */     {
/* 496 */       switch (jp.getCurrentTokenId()) {
/*     */       case 1: 
/*     */       case 3: 
/*     */       case 5: 
/* 500 */         return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */       
/*     */       case 6: 
/* 503 */         return jp.getText();
/*     */       
/*     */       case 7: 
/* 506 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 507 */           return jp.getBigIntegerValue();
/*     */         }
/* 509 */         return jp.getNumberValue();
/*     */       
/*     */       case 8: 
/* 512 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 513 */           return jp.getDecimalValue();
/*     */         }
/* 515 */         return Double.valueOf(jp.getDoubleValue());
/*     */       
/*     */       case 9: 
/* 518 */         return Boolean.TRUE;
/*     */       case 10: 
/* 520 */         return Boolean.FALSE;
/*     */       case 12: 
/* 522 */         return jp.getEmbeddedObject();
/*     */       
/*     */       case 11: 
/* 525 */         return null;
/*     */       }
/* 527 */       throw ctxt.mappingException(Object.class);
/*     */     }
/*     */     
/*     */     protected Object mapArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 533 */       Object value = deserialize(jp, ctxt);
/* 534 */       if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 535 */         ArrayList<Object> l = new ArrayList(2);
/* 536 */         l.add(value);
/* 537 */         return l;
/*     */       }
/* 539 */       Object value2 = deserialize(jp, ctxt);
/* 540 */       if (jp.nextToken() == JsonToken.END_ARRAY) {
/* 541 */         ArrayList<Object> l = new ArrayList(2);
/* 542 */         l.add(value);
/* 543 */         l.add(value2);
/* 544 */         return l;
/*     */       }
/* 546 */       ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 547 */       Object[] values = buffer.resetAndStart();
/* 548 */       int ptr = 0;
/* 549 */       values[(ptr++)] = value;
/* 550 */       values[(ptr++)] = value2;
/* 551 */       int totalSize = ptr;
/*     */       do {
/* 553 */         value = deserialize(jp, ctxt);
/* 554 */         totalSize++;
/* 555 */         if (ptr >= values.length) {
/* 556 */           values = buffer.appendCompletedChunk(values);
/* 557 */           ptr = 0;
/*     */         }
/* 559 */         values[(ptr++)] = value;
/* 560 */       } while (jp.nextToken() != JsonToken.END_ARRAY);
/*     */       
/* 562 */       ArrayList<Object> result = new ArrayList(totalSize);
/* 563 */       buffer.completeAndClearBuffer(values, ptr, result);
/* 564 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object mapObject(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 573 */       String field1 = jp.getText();
/* 574 */       jp.nextToken();
/* 575 */       Object value1 = deserialize(jp, ctxt);
/* 576 */       if (jp.nextToken() == JsonToken.END_OBJECT) {
/* 577 */         LinkedHashMap<String, Object> result = new LinkedHashMap(2);
/* 578 */         result.put(field1, value1);
/* 579 */         return result;
/*     */       }
/* 581 */       String field2 = jp.getText();
/* 582 */       jp.nextToken();
/* 583 */       Object value2 = deserialize(jp, ctxt);
/* 584 */       if (jp.nextToken() == JsonToken.END_OBJECT) {
/* 585 */         LinkedHashMap<String, Object> result = new LinkedHashMap(4);
/* 586 */         result.put(field1, value1);
/* 587 */         result.put(field2, value2);
/* 588 */         return result;
/*     */       }
/*     */       
/* 591 */       LinkedHashMap<String, Object> result = new LinkedHashMap();
/* 592 */       result.put(field1, value1);
/* 593 */       result.put(field2, value2);
/*     */       do {
/* 595 */         String fieldName = jp.getText();
/* 596 */         jp.nextToken();
/* 597 */         result.put(fieldName, deserialize(jp, ctxt));
/* 598 */       } while (jp.nextToken() != JsonToken.END_OBJECT);
/* 599 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     protected Object[] mapArrayToArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 606 */       ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 607 */       Object[] values = buffer.resetAndStart();
/* 608 */       int ptr = 0;
/*     */       do {
/* 610 */         Object value = deserialize(jp, ctxt);
/* 611 */         if (ptr >= values.length) {
/* 612 */           values = buffer.appendCompletedChunk(values);
/* 613 */           ptr = 0;
/*     */         }
/* 615 */         values[(ptr++)] = value;
/* 616 */       } while (jp.nextToken() != JsonToken.END_ARRAY);
/* 617 */       return buffer.completeAndClearBuffer(values, ptr);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\UntypedObjectDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */