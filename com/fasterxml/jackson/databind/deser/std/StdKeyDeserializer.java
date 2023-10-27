/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Calendar;
/*     */ import java.util.Currency;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StdKeyDeserializer
/*     */   extends KeyDeserializer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int TYPE_BOOLEAN = 1;
/*     */   public static final int TYPE_BYTE = 2;
/*     */   public static final int TYPE_SHORT = 3;
/*     */   public static final int TYPE_CHAR = 4;
/*     */   public static final int TYPE_INT = 5;
/*     */   public static final int TYPE_LONG = 6;
/*     */   public static final int TYPE_FLOAT = 7;
/*     */   public static final int TYPE_DOUBLE = 8;
/*     */   public static final int TYPE_LOCALE = 9;
/*     */   public static final int TYPE_DATE = 10;
/*     */   public static final int TYPE_CALENDAR = 11;
/*     */   public static final int TYPE_UUID = 12;
/*     */   public static final int TYPE_URI = 13;
/*     */   public static final int TYPE_URL = 14;
/*     */   public static final int TYPE_CLASS = 15;
/*     */   public static final int TYPE_CURRENCY = 16;
/*     */   protected final int _kind;
/*     */   protected final Class<?> _keyClass;
/*     */   protected final FromStringDeserializer<?> _deser;
/*     */   
/*     */   protected StdKeyDeserializer(int kind, Class<?> cls)
/*     */   {
/*  61 */     this(kind, cls, null);
/*     */   }
/*     */   
/*     */   protected StdKeyDeserializer(int kind, Class<?> cls, FromStringDeserializer<?> deser) {
/*  65 */     this._kind = kind;
/*  66 */     this._keyClass = cls;
/*  67 */     this._deser = deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StdKeyDeserializer forType(Class<?> raw)
/*     */   {
/*  75 */     if ((raw == String.class) || (raw == Object.class))
/*  76 */       return StringKD.forType(raw);
/*  77 */     int kind; if (raw == UUID.class) {
/*  78 */       kind = 12; } else { int kind;
/*  79 */       if (raw == Integer.class) {
/*  80 */         kind = 5; } else { int kind;
/*  81 */         if (raw == Long.class) {
/*  82 */           kind = 6; } else { int kind;
/*  83 */           if (raw == Date.class) {
/*  84 */             kind = 10; } else { int kind;
/*  85 */             if (raw == Calendar.class) {
/*  86 */               kind = 11;
/*     */             } else { int kind;
/*  88 */               if (raw == Boolean.class) {
/*  89 */                 kind = 1; } else { int kind;
/*  90 */                 if (raw == Byte.class) {
/*  91 */                   kind = 2; } else { int kind;
/*  92 */                   if (raw == Character.class) {
/*  93 */                     kind = 4; } else { int kind;
/*  94 */                     if (raw == Short.class) {
/*  95 */                       kind = 3; } else { int kind;
/*  96 */                       if (raw == Float.class) {
/*  97 */                         kind = 7; } else { int kind;
/*  98 */                         if (raw == Double.class) {
/*  99 */                           kind = 8; } else { int kind;
/* 100 */                           if (raw == URI.class) {
/* 101 */                             kind = 13; } else { int kind;
/* 102 */                             if (raw == URL.class) {
/* 103 */                               kind = 14; } else { int kind;
/* 104 */                               if (raw == Class.class) {
/* 105 */                                 kind = 15;
/* 106 */                               } else { if (raw == Locale.class) {
/* 107 */                                   FromStringDeserializer<?> deser = FromStringDeserializer.findDeserializer(Locale.class);
/* 108 */                                   return new StdKeyDeserializer(9, raw, deser); }
/* 109 */                                 if (raw == Currency.class) {
/* 110 */                                   FromStringDeserializer<?> deser = FromStringDeserializer.findDeserializer(Currency.class);
/* 111 */                                   return new StdKeyDeserializer(16, raw, deser);
/*     */                                 }
/* 113 */                                 return null; } } } } } } } } } } } } } }
/*     */     int kind;
/* 115 */     return new StdKeyDeserializer(kind, raw);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeKey(String key, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 122 */     if (key == null) {
/* 123 */       return null;
/*     */     }
/*     */     try {
/* 126 */       Object result = _parse(key, ctxt);
/* 127 */       if (result != null) {
/* 128 */         return result;
/*     */       }
/*     */     } catch (Exception re) {
/* 131 */       throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
/*     */     }
/* 133 */     if ((this._keyClass.isEnum()) && (ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL))) {
/* 134 */       return null;
/*     */     }
/* 136 */     throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
/*     */   }
/*     */   
/* 139 */   public Class<?> getKeyClass() { return this._keyClass; }
/*     */   
/*     */   protected Object _parse(String key, DeserializationContext ctxt) throws Exception
/*     */   {
/* 143 */     switch (this._kind) {
/*     */     case 1: 
/* 145 */       if ("true".equals(key)) {
/* 146 */         return Boolean.TRUE;
/*     */       }
/* 148 */       if ("false".equals(key)) {
/* 149 */         return Boolean.FALSE;
/*     */       }
/* 151 */       throw ctxt.weirdKeyException(this._keyClass, key, "value not 'true' or 'false'");
/*     */     
/*     */     case 2: 
/* 154 */       int value = _parseInt(key);
/*     */       
/* 156 */       if ((value < -128) || (value > 255)) {
/* 157 */         throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 8-bit value");
/*     */       }
/* 159 */       return Byte.valueOf((byte)value);
/*     */     
/*     */ 
/*     */     case 3: 
/* 163 */       int value = _parseInt(key);
/* 164 */       if ((value < 32768) || (value > 32767)) {
/* 165 */         throw ctxt.weirdKeyException(this._keyClass, key, "overflow, value can not be represented as 16-bit value");
/*     */       }
/* 167 */       return Short.valueOf((short)value);
/*     */     
/*     */     case 4: 
/* 170 */       if (key.length() == 1) {
/* 171 */         return Character.valueOf(key.charAt(0));
/*     */       }
/* 173 */       throw ctxt.weirdKeyException(this._keyClass, key, "can only convert 1-character Strings");
/*     */     case 5: 
/* 175 */       return Integer.valueOf(_parseInt(key));
/*     */     
/*     */     case 6: 
/* 178 */       return Long.valueOf(_parseLong(key));
/*     */     
/*     */ 
/*     */     case 7: 
/* 182 */       return Float.valueOf((float)_parseDouble(key));
/*     */     case 8: 
/* 184 */       return Double.valueOf(_parseDouble(key));
/*     */     case 9: 
/*     */       try {
/* 187 */         return this._deser._deserialize(key, ctxt);
/*     */       } catch (IOException e) {
/* 189 */         throw ctxt.weirdKeyException(this._keyClass, key, "unable to parse key as locale");
/*     */       }
/*     */     case 16: 
/*     */       try {
/* 193 */         return this._deser._deserialize(key, ctxt);
/*     */       } catch (IOException e) {
/* 195 */         throw ctxt.weirdKeyException(this._keyClass, key, "unable to parse key as currency");
/*     */       }
/*     */     case 10: 
/* 198 */       return ctxt.parseDate(key);
/*     */     case 11: 
/* 200 */       Date date = ctxt.parseDate(key);
/* 201 */       return date == null ? null : ctxt.constructCalendar(date);
/*     */     case 12: 
/* 203 */       return UUID.fromString(key);
/*     */     case 13: 
/* 205 */       return URI.create(key);
/*     */     case 14: 
/* 207 */       return new URL(key);
/*     */     case 15: 
/*     */       try {
/* 210 */         return ctxt.findClass(key);
/*     */       } catch (Exception e) {
/* 212 */         throw ctxt.weirdKeyException(this._keyClass, key, "unable to parse key as Class");
/*     */       }
/*     */     }
/* 215 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _parseInt(String key)
/*     */     throws IllegalArgumentException
/*     */   {
/* 225 */     return Integer.parseInt(key);
/*     */   }
/*     */   
/*     */   protected long _parseLong(String key) throws IllegalArgumentException {
/* 229 */     return Long.parseLong(key);
/*     */   }
/*     */   
/*     */   protected double _parseDouble(String key) throws IllegalArgumentException {
/* 233 */     return NumberInput.parseDouble(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class StringKD
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/* 246 */     private static final StringKD sString = new StringKD(String.class);
/* 247 */     private static final StringKD sObject = new StringKD(Object.class);
/*     */     
/* 249 */     private StringKD(Class<?> nominalType) { super(nominalType); }
/*     */     
/*     */     public static StringKD forType(Class<?> nominalType)
/*     */     {
/* 253 */       if (nominalType == String.class) {
/* 254 */         return sString;
/*     */       }
/* 256 */       if (nominalType == Object.class) {
/* 257 */         return sObject;
/*     */       }
/* 259 */       return new StringKD(nominalType);
/*     */     }
/*     */     
/*     */     public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException
/*     */     {
/* 264 */       return key;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class DelegatingKD
/*     */     extends KeyDeserializer
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final Class<?> _keyClass;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final JsonDeserializer<?> _delegate;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected DelegatingKD(Class<?> cls, JsonDeserializer<?> deser)
/*     */     {
/* 290 */       this._keyClass = cls;
/* 291 */       this._delegate = deser;
/*     */     }
/*     */     
/*     */ 
/*     */     public final Object deserializeKey(String key, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 298 */       if (key == null) {
/* 299 */         return null;
/*     */       }
/*     */       try
/*     */       {
/* 303 */         Object result = this._delegate.deserialize(ctxt.getParser(), ctxt);
/* 304 */         if (result != null) {
/* 305 */           return result;
/*     */         }
/*     */       } catch (Exception re) {
/* 308 */         throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation: " + re.getMessage());
/*     */       }
/* 310 */       throw ctxt.weirdKeyException(this._keyClass, key, "not a valid representation");
/*     */     }
/*     */     
/* 313 */     public Class<?> getKeyClass() { return this._keyClass; }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class EnumKD
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     protected final EnumResolver<?> _resolver;
/*     */     protected final AnnotatedMethod _factory;
/*     */     
/*     */     protected EnumKD(EnumResolver<?> er, AnnotatedMethod factory)
/*     */     {
/* 326 */       super(er.getEnumClass());
/* 327 */       this._resolver = er;
/* 328 */       this._factory = factory;
/*     */     }
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws JsonMappingException
/*     */     {
/* 334 */       if (this._factory != null) {
/*     */         try {
/* 336 */           return this._factory.call1(key);
/*     */         } catch (Exception e) {
/* 338 */           ClassUtil.unwrapAndThrowAsIAE(e);
/*     */         }
/*     */       }
/* 341 */       Enum<?> e = this._resolver.findEnum(key);
/* 342 */       if ((e == null) && (!ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL))) {
/* 343 */         throw ctxt.weirdKeyException(this._keyClass, key, "not one of values for Enum class");
/*     */       }
/* 345 */       return e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class StringCtorKeyDeserializer
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected final Constructor<?> _ctor;
/*     */     
/*     */ 
/*     */     public StringCtorKeyDeserializer(Constructor<?> ctor)
/*     */     {
/* 360 */       super(ctor.getDeclaringClass());
/* 361 */       this._ctor = ctor;
/*     */     }
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws Exception
/*     */     {
/* 367 */       return this._ctor.newInstance(new Object[] { key });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class StringFactoryKeyDeserializer
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     final Method _factoryMethod;
/*     */     
/*     */ 
/*     */     public StringFactoryKeyDeserializer(Method fm)
/*     */     {
/* 382 */       super(fm.getDeclaringClass());
/* 383 */       this._factoryMethod = fm;
/*     */     }
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws Exception
/*     */     {
/* 389 */       return this._factoryMethod.invoke(null, new Object[] { key });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\StdKeyDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */