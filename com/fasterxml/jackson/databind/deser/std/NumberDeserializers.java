/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumberDeserializers
/*     */ {
/*  26 */   private static final HashSet<String> _classNames = new HashSet();
/*     */   
/*     */   static {
/*  29 */     Class<?>[] numberTypes = { Boolean.class, Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class, Number.class, BigDecimal.class, BigInteger.class };
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
/*  41 */     for (Class<?> cls : numberTypes) {
/*  42 */       _classNames.add(cls.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static JsonDeserializer<?> find(Class<?> rawType, String clsName) {
/*  47 */     if (rawType.isPrimitive()) {
/*  48 */       if (rawType == Integer.TYPE) {
/*  49 */         return IntegerDeserializer.primitiveInstance;
/*     */       }
/*  51 */       if (rawType == Boolean.TYPE) {
/*  52 */         return BooleanDeserializer.primitiveInstance;
/*     */       }
/*  54 */       if (rawType == Long.TYPE) {
/*  55 */         return LongDeserializer.primitiveInstance;
/*     */       }
/*  57 */       if (rawType == Double.TYPE) {
/*  58 */         return DoubleDeserializer.primitiveInstance;
/*     */       }
/*  60 */       if (rawType == Character.TYPE) {
/*  61 */         return CharacterDeserializer.primitiveInstance;
/*     */       }
/*  63 */       if (rawType == Byte.TYPE) {
/*  64 */         return ByteDeserializer.primitiveInstance;
/*     */       }
/*  66 */       if (rawType == Short.TYPE) {
/*  67 */         return ShortDeserializer.primitiveInstance;
/*     */       }
/*  69 */       if (rawType == Float.TYPE) {
/*  70 */         return FloatDeserializer.primitiveInstance;
/*     */       }
/*  72 */     } else if (_classNames.contains(clsName))
/*     */     {
/*  74 */       if (rawType == Integer.class) {
/*  75 */         return IntegerDeserializer.wrapperInstance;
/*     */       }
/*  77 */       if (rawType == Boolean.class) {
/*  78 */         return BooleanDeserializer.wrapperInstance;
/*     */       }
/*  80 */       if (rawType == Long.class) {
/*  81 */         return LongDeserializer.wrapperInstance;
/*     */       }
/*  83 */       if (rawType == Double.class) {
/*  84 */         return DoubleDeserializer.wrapperInstance;
/*     */       }
/*  86 */       if (rawType == Character.class) {
/*  87 */         return CharacterDeserializer.wrapperInstance;
/*     */       }
/*  89 */       if (rawType == Byte.class) {
/*  90 */         return ByteDeserializer.wrapperInstance;
/*     */       }
/*  92 */       if (rawType == Short.class) {
/*  93 */         return ShortDeserializer.wrapperInstance;
/*     */       }
/*  95 */       if (rawType == Float.class) {
/*  96 */         return FloatDeserializer.wrapperInstance;
/*     */       }
/*  98 */       if (rawType == Number.class) {
/*  99 */         return NumberDeserializer.instance;
/*     */       }
/* 101 */       if (rawType == BigDecimal.class) {
/* 102 */         return BigDecimalDeserializer.instance;
/*     */       }
/* 104 */       if (rawType == BigInteger.class) {
/* 105 */         return BigIntegerDeserializer.instance;
/*     */       }
/*     */     } else {
/* 108 */       return null;
/*     */     }
/*     */     
/* 111 */     throw new IllegalArgumentException("Internal error: can't find deserializer for " + rawType.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static abstract class PrimitiveOrWrapperDeserializer<T>
/*     */     extends StdScalarDeserializer<T>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */     protected final T _nullValue;
/*     */     
/*     */ 
/*     */ 
/*     */     protected PrimitiveOrWrapperDeserializer(Class<T> vc, T nvl)
/*     */     {
/* 129 */       super();
/* 130 */       this._nullValue = nvl;
/*     */     }
/*     */     
/*     */     public final T getNullValue()
/*     */     {
/* 135 */       return (T)this._nullValue;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class BooleanDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Boolean>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/* 151 */     static final BooleanDeserializer primitiveInstance = new BooleanDeserializer(Boolean.class, Boolean.FALSE);
/* 152 */     static final BooleanDeserializer wrapperInstance = new BooleanDeserializer(Boolean.TYPE, null);
/*     */     
/*     */     public BooleanDeserializer(Class<Boolean> cls, Boolean nvl)
/*     */     {
/* 156 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Boolean deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 163 */       return _parseBoolean(jp, ctxt);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Boolean deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 173 */       return _parseBoolean(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class ByteDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Byte>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 183 */     static final ByteDeserializer primitiveInstance = new ByteDeserializer(Byte.TYPE, Byte.valueOf((byte)0));
/* 184 */     static final ByteDeserializer wrapperInstance = new ByteDeserializer(Byte.class, null);
/*     */     
/*     */     public ByteDeserializer(Class<Byte> cls, Byte nvl)
/*     */     {
/* 188 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Byte deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 195 */       return _parseByte(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class ShortDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Short>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 205 */     static final ShortDeserializer primitiveInstance = new ShortDeserializer(Short.class, Short.valueOf((short)0));
/* 206 */     static final ShortDeserializer wrapperInstance = new ShortDeserializer(Short.TYPE, null);
/*     */     
/*     */     public ShortDeserializer(Class<Short> cls, Short nvl)
/*     */     {
/* 210 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Short deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 217 */       return _parseShort(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CharacterDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Character>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 227 */     static final CharacterDeserializer primitiveInstance = new CharacterDeserializer(Character.class, Character.valueOf('\000'));
/* 228 */     static final CharacterDeserializer wrapperInstance = new CharacterDeserializer(Character.TYPE, null);
/*     */     
/*     */     public CharacterDeserializer(Class<Character> cls, Character nvl)
/*     */     {
/* 232 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Character deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 239 */       JsonToken t = jp.getCurrentToken();
/*     */       
/* 241 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 242 */         int value = jp.getIntValue();
/* 243 */         if ((value >= 0) && (value <= 65535)) {
/* 244 */           return Character.valueOf((char)value);
/*     */         }
/* 246 */       } else if (t == JsonToken.VALUE_STRING)
/*     */       {
/* 248 */         String text = jp.getText();
/* 249 */         if (text.length() == 1) {
/* 250 */           return Character.valueOf(text.charAt(0));
/*     */         }
/*     */         
/* 253 */         if (text.length() == 0) {
/* 254 */           return (Character)getEmptyValue();
/*     */         }
/* 256 */       } else if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)))
/*     */       {
/* 258 */         jp.nextToken();
/* 259 */         Character value = deserialize(jp, ctxt);
/* 260 */         if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 261 */           throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + this._valueClass.getName() + "' value but there was more than a single value in the array");
/*     */         }
/*     */         
/*     */ 
/* 265 */         return value;
/*     */       }
/* 267 */       throw ctxt.mappingException(this._valueClass, t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntegerDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Integer>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 277 */     static final IntegerDeserializer primitiveInstance = new IntegerDeserializer(Integer.class, Integer.valueOf(0));
/* 278 */     static final IntegerDeserializer wrapperInstance = new IntegerDeserializer(Integer.TYPE, null);
/*     */     
/*     */     public IntegerDeserializer(Class<Integer> cls, Integer nvl)
/*     */     {
/* 282 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Integer deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 289 */       return _parseInteger(jp, ctxt);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Integer deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 299 */       return _parseInteger(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class LongDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Long>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 309 */     static final LongDeserializer primitiveInstance = new LongDeserializer(Long.class, Long.valueOf(0L));
/* 310 */     static final LongDeserializer wrapperInstance = new LongDeserializer(Long.TYPE, null);
/*     */     
/*     */     public LongDeserializer(Class<Long> cls, Long nvl)
/*     */     {
/* 314 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Long deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 321 */       return _parseLong(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class FloatDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Float>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 331 */     static final FloatDeserializer primitiveInstance = new FloatDeserializer(Float.class, Float.valueOf(0.0F));
/* 332 */     static final FloatDeserializer wrapperInstance = new FloatDeserializer(Float.TYPE, null);
/*     */     
/*     */     public FloatDeserializer(Class<Float> cls, Float nvl)
/*     */     {
/* 336 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Float deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 346 */       return _parseFloat(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class DoubleDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Double>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 356 */     static final DoubleDeserializer primitiveInstance = new DoubleDeserializer(Double.class, Double.valueOf(0.0D));
/* 357 */     static final DoubleDeserializer wrapperInstance = new DoubleDeserializer(Double.TYPE, null);
/*     */     
/*     */     public DoubleDeserializer(Class<Double> cls, Double nvl)
/*     */     {
/* 361 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Double deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 368 */       return _parseDouble(jp, ctxt);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Double deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 378 */       return _parseDouble(jp, ctxt);
/*     */     }
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
/*     */   @JacksonStdImpl
/*     */   public static class NumberDeserializer
/*     */     extends StdScalarDeserializer<Number>
/*     */   {
/* 397 */     public static final NumberDeserializer instance = new NumberDeserializer();
/*     */     
/* 399 */     public NumberDeserializer() { super(); }
/*     */     
/*     */ 
/*     */     public Number deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 405 */       JsonToken t = jp.getCurrentToken();
/* 406 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 407 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 408 */           return jp.getBigIntegerValue();
/*     */         }
/* 410 */         return jp.getNumberValue(); }
/* 411 */       if (t == JsonToken.VALUE_NUMBER_FLOAT)
/*     */       {
/*     */ 
/*     */ 
/* 415 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 416 */           return jp.getDecimalValue();
/*     */         }
/* 418 */         return Double.valueOf(jp.getDoubleValue());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 424 */       if (t == JsonToken.VALUE_STRING) {
/* 425 */         String text = jp.getText().trim();
/* 426 */         if (text.length() == 0) {
/* 427 */           return (Number)getEmptyValue();
/*     */         }
/* 429 */         if (_hasTextualNull(text)) {
/* 430 */           return (Number)getNullValue();
/*     */         }
/* 432 */         if (_isPosInf(text)) {
/* 433 */           return Double.valueOf(Double.POSITIVE_INFINITY);
/*     */         }
/* 435 */         if (_isNegInf(text)) {
/* 436 */           return Double.valueOf(Double.NEGATIVE_INFINITY);
/*     */         }
/* 438 */         if (_isNaN(text)) {
/* 439 */           return Double.valueOf(NaN.0D);
/*     */         }
/*     */         try {
/* 442 */           if (text.indexOf('.') >= 0) {
/* 443 */             if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 444 */               return new BigDecimal(text);
/*     */             }
/* 446 */             return new Double(text);
/*     */           }
/* 448 */           if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 449 */             return new BigInteger(text);
/*     */           }
/* 451 */           long value = Long.parseLong(text);
/* 452 */           if ((value <= 2147483647L) && (value >= -2147483648L)) {
/* 453 */             return Integer.valueOf((int)value);
/*     */           }
/* 455 */           return Long.valueOf(value);
/*     */         } catch (IllegalArgumentException iae) {
/* 457 */           throw ctxt.weirdStringException(text, this._valueClass, "not a valid number");
/*     */         }
/*     */       }
/*     */       
/* 461 */       if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 462 */         jp.nextToken();
/* 463 */         Number value = deserialize(jp, ctxt);
/* 464 */         if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 465 */           throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + this._valueClass.getName() + "' value but there was more than a single value in the array");
/*     */         }
/*     */         
/*     */ 
/* 469 */         return value;
/*     */       }
/*     */       
/* 472 */       throw ctxt.mappingException(this._valueClass, t);
/*     */     }
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
/*     */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 487 */       switch (NumberDeserializers.1.$SwitchMap$com$fasterxml$jackson$core$JsonToken[jp.getCurrentToken().ordinal()])
/*     */       {
/*     */       case 1: 
/*     */       case 2: 
/*     */       case 3: 
/* 492 */         return deserialize(jp, ctxt);
/*     */       }
/* 494 */       return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
/*     */     }
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
/*     */   @JacksonStdImpl
/*     */   public static class BigIntegerDeserializer
/*     */     extends StdScalarDeserializer<BigInteger>
/*     */   {
/* 514 */     public static final BigIntegerDeserializer instance = new BigIntegerDeserializer();
/*     */     
/* 516 */     public BigIntegerDeserializer() { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */     public BigInteger deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 523 */       JsonToken t = jp.getCurrentToken();
/*     */       
/*     */ 
/* 526 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 527 */         switch (NumberDeserializers.1.$SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
/*     */         case 1: 
/*     */         case 2: 
/* 530 */           return BigInteger.valueOf(jp.getLongValue()); }
/*     */       } else {
/* 532 */         if (t == JsonToken.VALUE_NUMBER_FLOAT)
/*     */         {
/*     */ 
/*     */ 
/* 536 */           return jp.getDecimalValue().toBigInteger(); }
/* 537 */         if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 538 */           jp.nextToken();
/* 539 */           BigInteger value = deserialize(jp, ctxt);
/* 540 */           if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 541 */             throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'BigInteger' value but there was more than a single value in the array");
/*     */           }
/*     */           
/*     */ 
/* 545 */           return value; }
/* 546 */         if (t != JsonToken.VALUE_STRING)
/*     */         {
/* 548 */           throw ctxt.mappingException(this._valueClass, t); }
/*     */       }
/* 550 */       String text = jp.getText().trim();
/* 551 */       if (text.length() == 0) {
/* 552 */         return null;
/*     */       }
/*     */       try {
/* 555 */         return new BigInteger(text);
/*     */       } catch (IllegalArgumentException iae) {
/* 557 */         throw ctxt.weirdStringException(text, this._valueClass, "not a valid representation");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class BigDecimalDeserializer
/*     */     extends StdScalarDeserializer<BigDecimal>
/*     */   {
/* 567 */     public static final BigDecimalDeserializer instance = new BigDecimalDeserializer();
/*     */     
/* 569 */     public BigDecimalDeserializer() { super(); }
/*     */     
/*     */ 
/*     */     public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 575 */       JsonToken t = jp.getCurrentToken();
/* 576 */       if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 577 */         return jp.getDecimalValue();
/*     */       }
/*     */       
/* 580 */       if (t == JsonToken.VALUE_STRING) {
/* 581 */         String text = jp.getText().trim();
/* 582 */         if (text.length() == 0) {
/* 583 */           return null;
/*     */         }
/*     */         try {
/* 586 */           return new BigDecimal(text);
/*     */         } catch (IllegalArgumentException iae) {
/* 588 */           throw ctxt.weirdStringException(text, this._valueClass, "not a valid representation");
/*     */         }
/*     */       }
/*     */       
/* 592 */       if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 593 */         jp.nextToken();
/* 594 */         BigDecimal value = deserialize(jp, ctxt);
/* 595 */         if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 596 */           throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'BigDecimal' value but there was more than a single value in the array");
/*     */         }
/*     */         
/*     */ 
/* 600 */         return value;
/*     */       }
/*     */       
/* 603 */       throw ctxt.mappingException(this._valueClass, t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\NumberDeserializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */