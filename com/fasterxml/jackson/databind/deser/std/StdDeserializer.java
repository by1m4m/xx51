/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StdDeserializer<T>
/*     */   extends JsonDeserializer<T>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<?> _valueClass;
/*     */   
/*     */   protected StdDeserializer(Class<?> vc)
/*     */   {
/*  36 */     this._valueClass = vc;
/*     */   }
/*     */   
/*     */   protected StdDeserializer(JavaType valueType) {
/*  40 */     this._valueClass = (valueType == null ? null : valueType.getRawClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDeserializer(StdDeserializer<?> src)
/*     */   {
/*  50 */     this._valueClass = src._valueClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> handledType()
/*     */   {
/*  60 */     return this._valueClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final Class<?> getValueClass()
/*     */   {
/*  72 */     return this._valueClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType getValueType()
/*     */   {
/*  79 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer)
/*     */   {
/*  88 */     return ClassUtil.isJacksonStdImpl(deserializer);
/*     */   }
/*     */   
/*     */   protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
/*  92 */     return ClassUtil.isJacksonStdImpl(keyDeser);
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
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 108 */     return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _parseBooleanPrimitive(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 121 */     JsonToken t = jp.getCurrentToken();
/* 122 */     if (t == JsonToken.VALUE_TRUE) return true;
/* 123 */     if (t == JsonToken.VALUE_FALSE) return false;
/* 124 */     if (t == JsonToken.VALUE_NULL) { return false;
/*     */     }
/*     */     
/* 127 */     if (t == JsonToken.VALUE_NUMBER_INT)
/*     */     {
/* 129 */       if (jp.getNumberType() == JsonParser.NumberType.INT) {
/* 130 */         return jp.getIntValue() != 0;
/*     */       }
/* 132 */       return _parseBooleanFromNumber(jp, ctxt);
/*     */     }
/*     */     
/* 135 */     if (t == JsonToken.VALUE_STRING) {
/* 136 */       String text = jp.getText().trim();
/*     */       
/* 138 */       if (("true".equals(text)) || ("True".equals(text))) {
/* 139 */         return true;
/*     */       }
/* 141 */       if (("false".equals(text)) || ("False".equals(text)) || (text.length() == 0)) {
/* 142 */         return false;
/*     */       }
/* 144 */       if (_hasTextualNull(text)) {
/* 145 */         return false;
/*     */       }
/* 147 */       throw ctxt.weirdStringException(text, this._valueClass, "only \"true\" or \"false\" recognized");
/*     */     }
/*     */     
/* 150 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 151 */       jp.nextToken();
/* 152 */       boolean parsed = _parseBooleanPrimitive(jp, ctxt);
/* 153 */       t = jp.nextToken();
/* 154 */       if (t != JsonToken.END_ARRAY) {
/* 155 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'boolean' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 158 */       return parsed;
/*     */     }
/*     */     
/* 161 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final Boolean _parseBoolean(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 167 */     JsonToken t = jp.getCurrentToken();
/* 168 */     if (t == JsonToken.VALUE_TRUE) {
/* 169 */       return Boolean.TRUE;
/*     */     }
/* 171 */     if (t == JsonToken.VALUE_FALSE) {
/* 172 */       return Boolean.FALSE;
/*     */     }
/*     */     
/* 175 */     if (t == JsonToken.VALUE_NUMBER_INT)
/*     */     {
/* 177 */       if (jp.getNumberType() == JsonParser.NumberType.INT) {
/* 178 */         return jp.getIntValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
/*     */       }
/* 180 */       return Boolean.valueOf(_parseBooleanFromNumber(jp, ctxt));
/*     */     }
/* 182 */     if (t == JsonToken.VALUE_NULL) {
/* 183 */       return (Boolean)getNullValue();
/*     */     }
/*     */     
/* 186 */     if (t == JsonToken.VALUE_STRING) {
/* 187 */       String text = jp.getText().trim();
/*     */       
/* 189 */       if (("true".equals(text)) || ("True".equals(text))) {
/* 190 */         return Boolean.TRUE;
/*     */       }
/* 192 */       if (("false".equals(text)) || ("False".equals(text))) {
/* 193 */         return Boolean.FALSE;
/*     */       }
/* 195 */       if (text.length() == 0) {
/* 196 */         return (Boolean)getEmptyValue();
/*     */       }
/* 198 */       if (_hasTextualNull(text)) {
/* 199 */         return (Boolean)getNullValue();
/*     */       }
/* 201 */       throw ctxt.weirdStringException(text, this._valueClass, "only \"true\" or \"false\" recognized");
/*     */     }
/*     */     
/* 204 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 205 */       jp.nextToken();
/* 206 */       Boolean parsed = _parseBoolean(jp, ctxt);
/* 207 */       t = jp.nextToken();
/* 208 */       if (t != JsonToken.END_ARRAY) {
/* 209 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Boolean' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 212 */       return parsed;
/*     */     }
/*     */     
/* 215 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final boolean _parseBooleanFromNumber(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 221 */     if (jp.getNumberType() == JsonParser.NumberType.LONG) {
/* 222 */       return (jp.getLongValue() == 0L ? Boolean.FALSE : Boolean.TRUE).booleanValue();
/*     */     }
/*     */     
/* 225 */     String str = jp.getText();
/* 226 */     if (("0.0".equals(str)) || ("0".equals(str))) {
/* 227 */       return Boolean.FALSE.booleanValue();
/*     */     }
/* 229 */     return Boolean.TRUE.booleanValue();
/*     */   }
/*     */   
/*     */   protected Byte _parseByte(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 235 */     JsonToken t = jp.getCurrentToken();
/* 236 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 237 */       return Byte.valueOf(jp.getByteValue());
/*     */     }
/* 239 */     if (t == JsonToken.VALUE_STRING) {
/* 240 */       String text = jp.getText().trim();
/* 241 */       if (_hasTextualNull(text)) {
/* 242 */         return (Byte)getNullValue();
/*     */       }
/*     */       int value;
/*     */       try {
/* 246 */         int len = text.length();
/* 247 */         if (len == 0) {
/* 248 */           return (Byte)getEmptyValue();
/*     */         }
/* 250 */         value = NumberInput.parseInt(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 252 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid Byte value");
/*     */       }
/*     */       
/*     */ 
/* 256 */       if ((value < -128) || (value > 255)) {
/* 257 */         throw ctxt.weirdStringException(text, this._valueClass, "overflow, value can not be represented as 8-bit value");
/*     */       }
/* 259 */       return Byte.valueOf((byte)value);
/*     */     }
/* 261 */     if (t == JsonToken.VALUE_NULL) {
/* 262 */       return (Byte)getNullValue();
/*     */     }
/*     */     
/* 265 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 266 */       jp.nextToken();
/* 267 */       Byte parsed = _parseByte(jp, ctxt);
/* 268 */       t = jp.nextToken();
/* 269 */       if (t != JsonToken.END_ARRAY) {
/* 270 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Byte' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 273 */       return parsed;
/*     */     }
/* 275 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected Short _parseShort(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 281 */     JsonToken t = jp.getCurrentToken();
/* 282 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 283 */       return Short.valueOf(jp.getShortValue());
/*     */     }
/* 285 */     if (t == JsonToken.VALUE_STRING) {
/* 286 */       String text = jp.getText().trim();
/*     */       int value;
/*     */       try {
/* 289 */         int len = text.length();
/* 290 */         if (len == 0) {
/* 291 */           return (Short)getEmptyValue();
/*     */         }
/* 293 */         if (_hasTextualNull(text)) {
/* 294 */           return (Short)getNullValue();
/*     */         }
/* 296 */         value = NumberInput.parseInt(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 298 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid Short value");
/*     */       }
/*     */       
/* 301 */       if ((value < 32768) || (value > 32767)) {
/* 302 */         throw ctxt.weirdStringException(text, this._valueClass, "overflow, value can not be represented as 16-bit value");
/*     */       }
/* 304 */       return Short.valueOf((short)value);
/*     */     }
/* 306 */     if (t == JsonToken.VALUE_NULL) {
/* 307 */       return (Short)getNullValue();
/*     */     }
/*     */     
/* 310 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 311 */       jp.nextToken();
/* 312 */       Short parsed = _parseShort(jp, ctxt);
/* 313 */       t = jp.nextToken();
/* 314 */       if (t != JsonToken.END_ARRAY) {
/* 315 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Short' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 318 */       return parsed;
/*     */     }
/* 320 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final short _parseShortPrimitive(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 326 */     int value = _parseIntPrimitive(jp, ctxt);
/*     */     
/* 328 */     if ((value < 32768) || (value > 32767)) {
/* 329 */       throw ctxt.weirdStringException(String.valueOf(value), this._valueClass, "overflow, value can not be represented as 16-bit value");
/*     */     }
/*     */     
/* 332 */     return (short)value;
/*     */   }
/*     */   
/*     */   protected final int _parseIntPrimitive(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 338 */     JsonToken t = jp.getCurrentToken();
/*     */     
/*     */ 
/* 341 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 342 */       return jp.getIntValue();
/*     */     }
/* 344 */     if (t == JsonToken.VALUE_STRING) {
/* 345 */       String text = jp.getText().trim();
/* 346 */       if (_hasTextualNull(text)) {
/* 347 */         return 0;
/*     */       }
/*     */       try {
/* 350 */         int len = text.length();
/* 351 */         if (len > 9) {
/* 352 */           long l = Long.parseLong(text);
/* 353 */           if ((l < -2147483648L) || (l > 2147483647L)) {
/* 354 */             throw ctxt.weirdStringException(text, this._valueClass, "Overflow: numeric value (" + text + ") out of range of int (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
/*     */           }
/*     */           
/* 357 */           return (int)l;
/*     */         }
/* 359 */         if (len == 0) {
/* 360 */           return 0;
/*     */         }
/* 362 */         return NumberInput.parseInt(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 364 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid int value");
/*     */       }
/*     */     }
/* 367 */     if (t == JsonToken.VALUE_NULL) {
/* 368 */       return 0;
/*     */     }
/*     */     
/* 371 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 372 */       jp.nextToken();
/* 373 */       int parsed = _parseIntPrimitive(jp, ctxt);
/* 374 */       t = jp.nextToken();
/* 375 */       if (t != JsonToken.END_ARRAY) {
/* 376 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'int' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 379 */       return parsed;
/*     */     }
/*     */     
/* 382 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final Integer _parseInteger(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 388 */     JsonToken t = jp.getCurrentToken();
/* 389 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 390 */       return Integer.valueOf(jp.getIntValue());
/*     */     }
/* 392 */     if (t == JsonToken.VALUE_STRING) {
/* 393 */       String text = jp.getText().trim();
/*     */       try {
/* 395 */         int len = text.length();
/* 396 */         if (_hasTextualNull(text)) {
/* 397 */           return (Integer)getNullValue();
/*     */         }
/* 399 */         if (len > 9) {
/* 400 */           long l = Long.parseLong(text);
/* 401 */           if ((l < -2147483648L) || (l > 2147483647L)) {
/* 402 */             throw ctxt.weirdStringException(text, this._valueClass, "Overflow: numeric value (" + text + ") out of range of Integer (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
/*     */           }
/*     */           
/* 405 */           return Integer.valueOf((int)l);
/*     */         }
/* 407 */         if (len == 0) {
/* 408 */           return (Integer)getEmptyValue();
/*     */         }
/* 410 */         return Integer.valueOf(NumberInput.parseInt(text));
/*     */       } catch (IllegalArgumentException iae) {
/* 412 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid Integer value");
/*     */       }
/*     */     }
/* 415 */     if (t == JsonToken.VALUE_NULL) {
/* 416 */       return (Integer)getNullValue();
/*     */     }
/*     */     
/* 419 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 420 */       jp.nextToken();
/* 421 */       Integer parsed = _parseInteger(jp, ctxt);
/* 422 */       t = jp.nextToken();
/* 423 */       if (t != JsonToken.END_ARRAY) {
/* 424 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Integer' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 427 */       return parsed;
/*     */     }
/*     */     
/* 430 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final Long _parseLong(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/* 435 */     JsonToken t = jp.getCurrentToken();
/*     */     
/*     */ 
/* 438 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 439 */       return Long.valueOf(jp.getLongValue());
/*     */     }
/*     */     
/* 442 */     if (t == JsonToken.VALUE_STRING)
/*     */     {
/* 444 */       String text = jp.getText().trim();
/* 445 */       if (text.length() == 0) {
/* 446 */         return (Long)getEmptyValue();
/*     */       }
/* 448 */       if (_hasTextualNull(text)) {
/* 449 */         return (Long)getNullValue();
/*     */       }
/*     */       try {
/* 452 */         return Long.valueOf(NumberInput.parseLong(text));
/*     */       } catch (IllegalArgumentException iae) {
/* 454 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid Long value");
/*     */       } }
/* 456 */     if (t == JsonToken.VALUE_NULL) {
/* 457 */       return (Long)getNullValue();
/*     */     }
/*     */     
/* 460 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 461 */       jp.nextToken();
/* 462 */       Long parsed = _parseLong(jp, ctxt);
/* 463 */       t = jp.nextToken();
/* 464 */       if (t != JsonToken.END_ARRAY) {
/* 465 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Long' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 468 */       return parsed;
/*     */     }
/*     */     
/* 471 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final long _parseLongPrimitive(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 477 */     JsonToken t = jp.getCurrentToken();
/* 478 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 479 */       return jp.getLongValue();
/*     */     }
/* 481 */     if (t == JsonToken.VALUE_STRING) {
/* 482 */       String text = jp.getText().trim();
/* 483 */       if ((text.length() == 0) || (_hasTextualNull(text))) {
/* 484 */         return 0L;
/*     */       }
/*     */       try {
/* 487 */         return NumberInput.parseLong(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 489 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid long value");
/*     */       } }
/* 491 */     if (t == JsonToken.VALUE_NULL) {
/* 492 */       return 0L;
/*     */     }
/*     */     
/* 495 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 496 */       jp.nextToken();
/* 497 */       long parsed = _parseLongPrimitive(jp, ctxt);
/* 498 */       t = jp.nextToken();
/* 499 */       if (t != JsonToken.END_ARRAY) {
/* 500 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'long' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 503 */       return parsed;
/*     */     }
/* 505 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final Float _parseFloat(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 512 */     JsonToken t = jp.getCurrentToken();
/*     */     
/* 514 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 515 */       return Float.valueOf(jp.getFloatValue());
/*     */     }
/*     */     
/* 518 */     if (t == JsonToken.VALUE_STRING) {
/* 519 */       String text = jp.getText().trim();
/* 520 */       if (text.length() == 0) {
/* 521 */         return (Float)getEmptyValue();
/*     */       }
/* 523 */       if (_hasTextualNull(text)) {
/* 524 */         return (Float)getNullValue();
/*     */       }
/* 526 */       switch (text.charAt(0)) {
/*     */       case 'I': 
/* 528 */         if (_isPosInf(text)) {
/* 529 */           return Float.valueOf(Float.POSITIVE_INFINITY);
/*     */         }
/*     */         break;
/*     */       case 'N': 
/* 533 */         if (_isNaN(text)) {
/* 534 */           return Float.valueOf(NaN.0F);
/*     */         }
/*     */         break;
/*     */       case '-': 
/* 538 */         if (_isNegInf(text)) {
/* 539 */           return Float.valueOf(Float.NEGATIVE_INFINITY);
/*     */         }
/*     */         break;
/*     */       }
/*     */       try {
/* 544 */         return Float.valueOf(Float.parseFloat(text));
/*     */       } catch (IllegalArgumentException iae) {
/* 546 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid Float value");
/*     */       } }
/* 548 */     if (t == JsonToken.VALUE_NULL) {
/* 549 */       return (Float)getNullValue();
/*     */     }
/*     */     
/* 552 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 553 */       jp.nextToken();
/* 554 */       Float parsed = _parseFloat(jp, ctxt);
/* 555 */       t = jp.nextToken();
/* 556 */       if (t != JsonToken.END_ARRAY) {
/* 557 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Byte' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 560 */       return parsed;
/*     */     }
/*     */     
/* 563 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final float _parseFloatPrimitive(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 569 */     JsonToken t = jp.getCurrentToken();
/*     */     
/* 571 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 572 */       return jp.getFloatValue();
/*     */     }
/* 574 */     if (t == JsonToken.VALUE_STRING) {
/* 575 */       String text = jp.getText().trim();
/* 576 */       if ((text.length() == 0) || (_hasTextualNull(text))) {
/* 577 */         return 0.0F;
/*     */       }
/* 579 */       switch (text.charAt(0)) {
/*     */       case 'I': 
/* 581 */         if (_isPosInf(text)) {
/* 582 */           return Float.POSITIVE_INFINITY;
/*     */         }
/*     */         break;
/*     */       case 'N': 
/* 586 */         if (_isNaN(text)) return NaN.0F;
/*     */         break;
/*     */       case '-': 
/* 589 */         if (_isNegInf(text)) {
/* 590 */           return Float.NEGATIVE_INFINITY;
/*     */         }
/*     */         break;
/*     */       }
/*     */       try {
/* 595 */         return Float.parseFloat(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 597 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid float value");
/*     */       } }
/* 599 */     if (t == JsonToken.VALUE_NULL) {
/* 600 */       return 0.0F;
/*     */     }
/*     */     
/* 603 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 604 */       jp.nextToken();
/* 605 */       float parsed = _parseFloatPrimitive(jp, ctxt);
/* 606 */       t = jp.nextToken();
/* 607 */       if (t != JsonToken.END_ARRAY) {
/* 608 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'float' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 611 */       return parsed;
/*     */     }
/*     */     
/* 614 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected final Double _parseDouble(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 620 */     JsonToken t = jp.getCurrentToken();
/*     */     
/* 622 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 623 */       return Double.valueOf(jp.getDoubleValue());
/*     */     }
/* 625 */     if (t == JsonToken.VALUE_STRING) {
/* 626 */       String text = jp.getText().trim();
/* 627 */       if (text.length() == 0) {
/* 628 */         return (Double)getEmptyValue();
/*     */       }
/* 630 */       if (_hasTextualNull(text)) {
/* 631 */         return (Double)getNullValue();
/*     */       }
/* 633 */       switch (text.charAt(0)) {
/*     */       case 'I': 
/* 635 */         if (_isPosInf(text)) {
/* 636 */           return Double.valueOf(Double.POSITIVE_INFINITY);
/*     */         }
/*     */         break;
/*     */       case 'N': 
/* 640 */         if (_isNaN(text)) {
/* 641 */           return Double.valueOf(NaN.0D);
/*     */         }
/*     */         break;
/*     */       case '-': 
/* 645 */         if (_isNegInf(text)) {
/* 646 */           return Double.valueOf(Double.NEGATIVE_INFINITY);
/*     */         }
/*     */         break;
/*     */       }
/*     */       try {
/* 651 */         return Double.valueOf(parseDouble(text));
/*     */       } catch (IllegalArgumentException iae) {
/* 653 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid Double value");
/*     */       } }
/* 655 */     if (t == JsonToken.VALUE_NULL) {
/* 656 */       return (Double)getNullValue();
/*     */     }
/*     */     
/* 659 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 660 */       jp.nextToken();
/* 661 */       Double parsed = _parseDouble(jp, ctxt);
/* 662 */       t = jp.nextToken();
/* 663 */       if (t != JsonToken.END_ARRAY) {
/* 664 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Double' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 667 */       return parsed;
/*     */     }
/*     */     
/* 670 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final double _parseDoublePrimitive(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 677 */     JsonToken t = jp.getCurrentToken();
/*     */     
/* 679 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 680 */       return jp.getDoubleValue();
/*     */     }
/*     */     
/* 683 */     if (t == JsonToken.VALUE_STRING) {
/* 684 */       String text = jp.getText().trim();
/* 685 */       if ((text.length() == 0) || (_hasTextualNull(text))) {
/* 686 */         return 0.0D;
/*     */       }
/* 688 */       switch (text.charAt(0)) {
/*     */       case 'I': 
/* 690 */         if (_isPosInf(text)) {
/* 691 */           return Double.POSITIVE_INFINITY;
/*     */         }
/*     */         break;
/*     */       case 'N': 
/* 695 */         if (_isNaN(text)) {
/* 696 */           return NaN.0D;
/*     */         }
/*     */         break;
/*     */       case '-': 
/* 700 */         if (_isNegInf(text)) {
/* 701 */           return Double.NEGATIVE_INFINITY;
/*     */         }
/*     */         break;
/*     */       }
/*     */       try {
/* 706 */         return parseDouble(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 708 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid double value");
/*     */       } }
/* 710 */     if (t == JsonToken.VALUE_NULL) {
/* 711 */       return 0.0D;
/*     */     }
/*     */     
/* 714 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 715 */       jp.nextToken();
/* 716 */       double parsed = _parseDoublePrimitive(jp, ctxt);
/* 717 */       t = jp.nextToken();
/* 718 */       if (t != JsonToken.END_ARRAY) {
/* 719 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'Byte' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 722 */       return parsed;
/*     */     }
/*     */     
/* 725 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */   protected Date _parseDate(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 731 */     JsonToken t = jp.getCurrentToken();
/* 732 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/* 733 */       return new Date(jp.getLongValue());
/*     */     }
/* 735 */     if (t == JsonToken.VALUE_NULL) {
/* 736 */       return (Date)getNullValue();
/*     */     }
/* 738 */     if (t == JsonToken.VALUE_STRING) {
/* 739 */       String value = null;
/*     */       try
/*     */       {
/* 742 */         value = jp.getText().trim();
/* 743 */         if (value.length() == 0) {
/* 744 */           return (Date)getEmptyValue();
/*     */         }
/* 746 */         if (_hasTextualNull(value)) {
/* 747 */           return (Date)getNullValue();
/*     */         }
/* 749 */         return ctxt.parseDate(value);
/*     */       } catch (IllegalArgumentException iae) {
/* 751 */         throw ctxt.weirdStringException(value, this._valueClass, "not a valid representation (error: " + iae.getMessage() + ")");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 756 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 757 */       jp.nextToken();
/* 758 */       Date parsed = _parseDate(jp, ctxt);
/* 759 */       t = jp.nextToken();
/* 760 */       if (t != JsonToken.END_ARRAY) {
/* 761 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'java.util.Date' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 764 */       return parsed;
/*     */     }
/* 766 */     throw ctxt.mappingException(this._valueClass, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final double parseDouble(String numStr)
/*     */     throws NumberFormatException
/*     */   {
/* 777 */     if ("2.2250738585072012e-308".equals(numStr)) {
/* 778 */       return Double.MIN_VALUE;
/*     */     }
/* 780 */     return Double.parseDouble(numStr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _parseString(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 791 */     JsonToken t = jp.getCurrentToken();
/* 792 */     if (t == JsonToken.VALUE_STRING) {
/* 793 */       return jp.getText();
/*     */     }
/*     */     
/*     */ 
/* 797 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 798 */       jp.nextToken();
/* 799 */       String parsed = _parseString(jp, ctxt);
/* 800 */       if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 801 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'String' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 804 */       return parsed;
/*     */     }
/* 806 */     String value = jp.getValueAsString();
/* 807 */     if (value != null) {
/* 808 */       return value;
/*     */     }
/* 810 */     throw ctxt.mappingException(String.class, jp.getCurrentToken());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected T _deserializeFromEmpty(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 822 */     JsonToken t = jp.getCurrentToken();
/* 823 */     if (t == JsonToken.START_ARRAY) {
/* 824 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/* 825 */         t = jp.nextToken();
/* 826 */         if (t == JsonToken.END_ARRAY) {
/* 827 */           return null;
/*     */         }
/* 829 */         throw ctxt.mappingException(handledType(), JsonToken.START_ARRAY);
/*     */       }
/* 831 */     } else if ((t == JsonToken.VALUE_STRING) && 
/* 832 */       (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))) {
/* 833 */       String str = jp.getText().trim();
/* 834 */       if (str.isEmpty()) {
/* 835 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 839 */     throw ctxt.mappingException(handledType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _hasTextualNull(String value)
/*     */   {
/* 850 */     return "null".equals(value);
/*     */   }
/*     */   
/*     */   protected final boolean _isNegInf(String text) {
/* 854 */     return ("-Infinity".equals(text)) || ("-INF".equals(text));
/*     */   }
/*     */   
/*     */   protected final boolean _isPosInf(String text) {
/* 858 */     return ("Infinity".equals(text)) || ("INF".equals(text));
/*     */   }
/*     */   
/* 861 */   protected final boolean _isNaN(String text) { return "NaN".equals(text); }
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
/*     */   protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 882 */     return ctxt.findContextualValueDeserializer(type, property);
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
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> existingDeserializer)
/*     */     throws JsonMappingException
/*     */   {
/* 905 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 906 */     if ((intr != null) && (prop != null)) {
/* 907 */       AnnotatedMember member = prop.getMember();
/* 908 */       if (member != null) {
/* 909 */         Object convDef = intr.findDeserializationContentConverter(member);
/* 910 */         if (convDef != null) {
/* 911 */           Converter<Object, Object> conv = ctxt.converterInstance(prop.getMember(), convDef);
/* 912 */           JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 913 */           if (existingDeserializer == null) {
/* 914 */             existingDeserializer = ctxt.findContextualValueDeserializer(delegateType, prop);
/*     */           }
/* 916 */           return new StdDelegatingDeserializer(conv, delegateType, existingDeserializer);
/*     */         }
/*     */       }
/*     */     }
/* 920 */     return existingDeserializer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object instanceOrClass, String propName)
/*     */     throws IOException
/*     */   {
/* 950 */     if (instanceOrClass == null) {
/* 951 */       instanceOrClass = handledType();
/*     */     }
/*     */     
/* 954 */     if (ctxt.handleUnknownProperty(jp, this, instanceOrClass, propName)) {
/* 955 */       return;
/*     */     }
/*     */     
/* 958 */     ctxt.reportUnknownProperty(instanceOrClass, propName, this);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 963 */     jp.skipChildren();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StdDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */