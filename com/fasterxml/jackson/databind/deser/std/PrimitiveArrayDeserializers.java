/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.ByteBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.DoubleBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.ShortBuilder;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class PrimitiveArrayDeserializers<T> extends StdDeserializer<T>
/*     */ {
/*     */   protected PrimitiveArrayDeserializers(Class<T> cls)
/*     */   {
/*  19 */     super(cls);
/*     */   }
/*     */   
/*     */ 
/*     */   public static com.fasterxml.jackson.databind.JsonDeserializer<?> forType(Class<?> rawType)
/*     */   {
/*  25 */     if (rawType == Integer.TYPE) {
/*  26 */       return IntDeser.instance;
/*     */     }
/*  28 */     if (rawType == Long.TYPE) {
/*  29 */       return LongDeser.instance;
/*     */     }
/*     */     
/*  32 */     if (rawType == Byte.TYPE) {
/*  33 */       return new ByteDeser();
/*     */     }
/*  35 */     if (rawType == Short.TYPE) {
/*  36 */       return new ShortDeser();
/*     */     }
/*  38 */     if (rawType == Float.TYPE) {
/*  39 */       return new FloatDeser();
/*     */     }
/*  41 */     if (rawType == Double.TYPE) {
/*  42 */       return new DoubleDeser();
/*     */     }
/*  44 */     if (rawType == Boolean.TYPE) {
/*  45 */       return new BooleanDeser();
/*     */     }
/*  47 */     if (rawType == Character.TYPE) {
/*  48 */       return new CharDeser();
/*     */     }
/*  50 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, com.fasterxml.jackson.databind.jsontype.TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/*  60 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class CharDeser
/*     */     extends PrimitiveArrayDeserializers<char[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     public CharDeser()
/*     */     {
/*  75 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public char[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/*  84 */       JsonToken t = jp.getCurrentToken();
/*  85 */       if (t == JsonToken.VALUE_STRING)
/*     */       {
/*  87 */         char[] buffer = jp.getTextCharacters();
/*  88 */         int offset = jp.getTextOffset();
/*  89 */         int len = jp.getTextLength();
/*     */         
/*  91 */         char[] result = new char[len];
/*  92 */         System.arraycopy(buffer, offset, result, 0, len);
/*  93 */         return result;
/*     */       }
/*  95 */       if (jp.isExpectedStartArrayToken())
/*     */       {
/*  97 */         StringBuilder sb = new StringBuilder(64);
/*  98 */         while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
/*  99 */           if (t != JsonToken.VALUE_STRING) {
/* 100 */             throw ctxt.mappingException(Character.TYPE);
/*     */           }
/* 102 */           String str = jp.getText();
/* 103 */           if (str.length() != 1) {
/* 104 */             throw JsonMappingException.from(jp, "Can not convert a JSON String of length " + str.length() + " into a char element of char array");
/*     */           }
/* 106 */           sb.append(str.charAt(0));
/*     */         }
/* 108 */         return sb.toString().toCharArray();
/*     */       }
/*     */       
/* 111 */       if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 112 */         Object ob = jp.getEmbeddedObject();
/* 113 */         if (ob == null) return null;
/* 114 */         if ((ob instanceof char[])) {
/* 115 */           return (char[])ob;
/*     */         }
/* 117 */         if ((ob instanceof String)) {
/* 118 */           return ((String)ob).toCharArray();
/*     */         }
/*     */         
/* 121 */         if ((ob instanceof byte[])) {
/* 122 */           return com.fasterxml.jackson.core.Base64Variants.getDefaultVariant().encode((byte[])ob, false).toCharArray();
/*     */         }
/*     */       }
/*     */       
/* 126 */       throw ctxt.mappingException(this._valueClass);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class BooleanDeser
/*     */     extends PrimitiveArrayDeserializers<boolean[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     public BooleanDeser()
/*     */     {
/* 142 */       super();
/*     */     }
/*     */     
/*     */     public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonProcessingException
/*     */     {
/* 148 */       if (!jp.isExpectedStartArrayToken()) {
/* 149 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 151 */       com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
/* 152 */       boolean[] chunk = (boolean[])builder.resetAndStart();
/* 153 */       int ix = 0;
/*     */       try
/*     */       {
/* 156 */         while (jp.nextToken() != JsonToken.END_ARRAY)
/*     */         {
/* 158 */           boolean value = _parseBooleanPrimitive(jp, ctxt);
/* 159 */           if (ix >= chunk.length) {
/* 160 */             chunk = (boolean[])builder.appendCompletedChunk(chunk, ix);
/* 161 */             ix = 0;
/*     */           }
/* 163 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 166 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 168 */       return (boolean[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     private final boolean[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 174 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 176 */         if (jp.getText().length() == 0) {
/* 177 */           return null;
/*     */         }
/*     */       }
/* 180 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 181 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 183 */       return new boolean[] { _parseBooleanPrimitive(jp, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class ByteDeser
/*     */     extends PrimitiveArrayDeserializers<byte[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */     public ByteDeser()
/*     */     {
/* 197 */       super();
/*     */     }
/*     */     
/*     */     public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 202 */       JsonToken t = jp.getCurrentToken();
/*     */       
/*     */ 
/* 205 */       if (t == JsonToken.VALUE_STRING) {
/* 206 */         return jp.getBinaryValue(ctxt.getBase64Variant());
/*     */       }
/*     */       
/* 209 */       if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 210 */         Object ob = jp.getEmbeddedObject();
/* 211 */         if (ob == null) return null;
/* 212 */         if ((ob instanceof byte[])) {
/* 213 */           return (byte[])ob;
/*     */         }
/*     */       }
/* 216 */       if (!jp.isExpectedStartArrayToken()) {
/* 217 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 219 */       ArrayBuilders.ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
/* 220 */       byte[] chunk = (byte[])builder.resetAndStart();
/* 221 */       int ix = 0;
/*     */       try
/*     */       {
/* 224 */         while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
/*     */           byte value;
/*     */           byte value;
/* 227 */           if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*     */           {
/* 229 */             value = jp.getByteValue();
/*     */           }
/*     */           else {
/* 232 */             if (t != JsonToken.VALUE_NULL) {
/* 233 */               throw ctxt.mappingException(this._valueClass.getComponentType());
/*     */             }
/* 235 */             value = 0;
/*     */           }
/* 237 */           if (ix >= chunk.length) {
/* 238 */             chunk = (byte[])builder.appendCompletedChunk(chunk, ix);
/* 239 */             ix = 0;
/*     */           }
/* 241 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 244 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 246 */       return (byte[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     private final byte[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 252 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 254 */         if (jp.getText().length() == 0) {
/* 255 */           return null;
/*     */         }
/*     */       }
/* 258 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 259 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/*     */       
/* 262 */       JsonToken t = jp.getCurrentToken();
/* 263 */       byte value; byte value; if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*     */       {
/* 265 */         value = jp.getByteValue();
/*     */       }
/*     */       else {
/* 268 */         if (t != JsonToken.VALUE_NULL) {
/* 269 */           throw ctxt.mappingException(this._valueClass.getComponentType());
/*     */         }
/* 271 */         value = 0;
/*     */       }
/* 273 */       return new byte[] { value };
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class ShortDeser extends PrimitiveArrayDeserializers<short[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public ShortDeser() {
/* 283 */       super();
/*     */     }
/*     */     
/*     */     public short[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 288 */       if (!jp.isExpectedStartArrayToken()) {
/* 289 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 291 */       ArrayBuilders.ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
/* 292 */       short[] chunk = (short[])builder.resetAndStart();
/* 293 */       int ix = 0;
/*     */       try
/*     */       {
/* 296 */         while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 297 */           short value = _parseShortPrimitive(jp, ctxt);
/* 298 */           if (ix >= chunk.length) {
/* 299 */             chunk = (short[])builder.appendCompletedChunk(chunk, ix);
/* 300 */             ix = 0;
/*     */           }
/* 302 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 305 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 307 */       return (short[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     private final short[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 313 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 315 */         if (jp.getText().length() == 0) {
/* 316 */           return null;
/*     */         }
/*     */       }
/* 319 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 320 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 322 */       return new short[] { _parseShortPrimitive(jp, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class IntDeser
/*     */     extends PrimitiveArrayDeserializers<int[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 332 */     public static final IntDeser instance = new IntDeser();
/*     */     
/* 334 */     public IntDeser() { super(); }
/*     */     
/*     */     public int[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 339 */       if (!jp.isExpectedStartArrayToken()) {
/* 340 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 342 */       com.fasterxml.jackson.databind.util.ArrayBuilders.IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
/* 343 */       int[] chunk = (int[])builder.resetAndStart();
/* 344 */       int ix = 0;
/*     */       try
/*     */       {
/* 347 */         while (jp.nextToken() != JsonToken.END_ARRAY)
/*     */         {
/* 349 */           int value = _parseIntPrimitive(jp, ctxt);
/* 350 */           if (ix >= chunk.length) {
/* 351 */             chunk = (int[])builder.appendCompletedChunk(chunk, ix);
/* 352 */             ix = 0;
/*     */           }
/* 354 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 357 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 359 */       return (int[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     private final int[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 365 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 367 */         if (jp.getText().length() == 0) {
/* 368 */           return null;
/*     */         }
/*     */       }
/* 371 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 372 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 374 */       return new int[] { _parseIntPrimitive(jp, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class LongDeser
/*     */     extends PrimitiveArrayDeserializers<long[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 384 */     public static final LongDeser instance = new LongDeser();
/*     */     
/* 386 */     public LongDeser() { super(); }
/*     */     
/*     */     public long[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 391 */       if (!jp.isExpectedStartArrayToken()) {
/* 392 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 394 */       com.fasterxml.jackson.databind.util.ArrayBuilders.LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
/* 395 */       long[] chunk = (long[])builder.resetAndStart();
/* 396 */       int ix = 0;
/*     */       try
/*     */       {
/* 399 */         while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 400 */           long value = _parseLongPrimitive(jp, ctxt);
/* 401 */           if (ix >= chunk.length) {
/* 402 */             chunk = (long[])builder.appendCompletedChunk(chunk, ix);
/* 403 */             ix = 0;
/*     */           }
/* 405 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 408 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 410 */       return (long[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     private final long[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 416 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 418 */         if (jp.getText().length() == 0) {
/* 419 */           return null;
/*     */         }
/*     */       }
/* 422 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 423 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 425 */       return new long[] { _parseLongPrimitive(jp, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class FloatDeser extends PrimitiveArrayDeserializers<float[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public FloatDeser() {
/* 435 */       super();
/*     */     }
/*     */     
/*     */     public float[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonProcessingException
/*     */     {
/* 441 */       if (!jp.isExpectedStartArrayToken()) {
/* 442 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 444 */       com.fasterxml.jackson.databind.util.ArrayBuilders.FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
/* 445 */       float[] chunk = (float[])builder.resetAndStart();
/* 446 */       int ix = 0;
/*     */       try
/*     */       {
/* 449 */         while (jp.nextToken() != JsonToken.END_ARRAY)
/*     */         {
/* 451 */           float value = _parseFloatPrimitive(jp, ctxt);
/* 452 */           if (ix >= chunk.length) {
/* 453 */             chunk = (float[])builder.appendCompletedChunk(chunk, ix);
/* 454 */             ix = 0;
/*     */           }
/* 456 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 459 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 461 */       return (float[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */ 
/*     */     private final float[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonProcessingException
/*     */     {
/* 468 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 470 */         if (jp.getText().length() == 0) {
/* 471 */           return null;
/*     */         }
/*     */       }
/* 474 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 475 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 477 */       return new float[] { _parseFloatPrimitive(jp, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class DoubleDeser extends PrimitiveArrayDeserializers<double[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public DoubleDeser() {
/* 487 */       super();
/*     */     }
/*     */     
/*     */     public double[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 492 */       if (!jp.isExpectedStartArrayToken()) {
/* 493 */         return handleNonArray(jp, ctxt);
/*     */       }
/* 495 */       ArrayBuilders.DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
/* 496 */       double[] chunk = (double[])builder.resetAndStart();
/* 497 */       int ix = 0;
/*     */       try
/*     */       {
/* 500 */         while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 501 */           double value = _parseDoublePrimitive(jp, ctxt);
/* 502 */           if (ix >= chunk.length) {
/* 503 */             chunk = (double[])builder.appendCompletedChunk(chunk, ix);
/* 504 */             ix = 0;
/*     */           }
/* 506 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 509 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 511 */       return (double[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     private final double[] handleNonArray(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 517 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */       {
/* 519 */         if (jp.getText().length() == 0) {
/* 520 */           return null;
/*     */         }
/*     */       }
/* 523 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 524 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 526 */       return new double[] { _parseDoublePrimitive(jp, ctxt) };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\PrimitiveArrayDeserializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */