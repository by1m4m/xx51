/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ public abstract class FromStringDeserializer<T>
/*     */   extends StdScalarDeserializer<T>
/*     */ {
/*     */   public static Class<?>[] types()
/*     */   {
/*  30 */     return new Class[] { File.class, URL.class, URI.class, Class.class, JavaType.class, Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class };
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
/*     */   protected FromStringDeserializer(Class<?> vc)
/*     */   {
/*  53 */     super(vc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Std findDeserializer(Class<?> rawType)
/*     */   {
/*  62 */     int kind = 0;
/*  63 */     if (rawType == File.class) {
/*  64 */       kind = 1;
/*  65 */     } else if (rawType == URL.class) {
/*  66 */       kind = 2;
/*  67 */     } else if (rawType == URI.class) {
/*  68 */       kind = 3;
/*  69 */     } else if (rawType == Class.class) {
/*  70 */       kind = 4;
/*  71 */     } else if (rawType == JavaType.class) {
/*  72 */       kind = 5;
/*  73 */     } else if (rawType == Currency.class) {
/*  74 */       kind = 6;
/*  75 */     } else if (rawType == Pattern.class) {
/*  76 */       kind = 7;
/*  77 */     } else if (rawType == Locale.class) {
/*  78 */       kind = 8;
/*  79 */     } else if (rawType == Charset.class) {
/*  80 */       kind = 9;
/*  81 */     } else if (rawType == TimeZone.class) {
/*  82 */       kind = 10;
/*  83 */     } else if (rawType == InetAddress.class) {
/*  84 */       kind = 11;
/*  85 */     } else if (rawType == InetSocketAddress.class) {
/*  86 */       kind = 12;
/*     */     } else {
/*  88 */       return null;
/*     */     }
/*  90 */     return new Std(rawType, kind);
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
/*     */   public T deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 104 */     if ((jp.getCurrentToken() == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 105 */       jp.nextToken();
/* 106 */       T value = deserialize(jp, ctxt);
/* 107 */       if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 108 */         throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '" + this._valueClass.getName() + "' value but there was more than a single value in the array");
/*     */       }
/*     */       
/* 111 */       return value;
/*     */     }
/*     */     
/* 114 */     String text = jp.getValueAsString();
/* 115 */     if (text != null) {
/* 116 */       if ((text.length() == 0) || ((text = text.trim()).length() == 0))
/*     */       {
/* 118 */         return (T)_deserializeFromEmptyString();
/*     */       }
/* 120 */       Exception cause = null;
/*     */       try {
/* 122 */         T result = _deserialize(text, ctxt);
/* 123 */         if (result != null) {
/* 124 */           return result;
/*     */         }
/*     */       } catch (IllegalArgumentException iae) {
/* 127 */         cause = iae;
/*     */       }
/* 129 */       String msg = "not a valid textual representation";
/* 130 */       if (cause != null) {
/* 131 */         String m2 = cause.getMessage();
/* 132 */         if (m2 != null) {
/* 133 */           msg = msg + ", problem: " + m2;
/*     */         }
/*     */       }
/* 136 */       JsonMappingException e = ctxt.weirdStringException(text, this._valueClass, msg);
/* 137 */       if (cause != null) {
/* 138 */         e.initCause(cause);
/*     */       }
/* 140 */       throw e;
/*     */     }
/*     */     
/* 143 */     if (jp.getCurrentToken() == JsonToken.VALUE_EMBEDDED_OBJECT)
/*     */     {
/* 145 */       Object ob = jp.getEmbeddedObject();
/* 146 */       if (ob == null) {
/* 147 */         return null;
/*     */       }
/* 149 */       if (this._valueClass.isAssignableFrom(ob.getClass())) {
/* 150 */         return (T)ob;
/*     */       }
/* 152 */       return (T)_deserializeEmbedded(ob, ctxt);
/*     */     }
/* 154 */     throw ctxt.mappingException(this._valueClass);
/*     */   }
/*     */   
/*     */   protected abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext) throws IOException;
/*     */   
/*     */   protected T _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException
/*     */   {
/* 161 */     throw ctxt.mappingException("Don't know how to convert embedded Object of type " + ob.getClass().getName() + " into " + this._valueClass.getName());
/*     */   }
/*     */   
/*     */   protected T _deserializeFromEmptyString() throws IOException {
/* 165 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Std
/*     */     extends FromStringDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public static final int STD_FILE = 1;
/*     */     
/*     */     public static final int STD_URL = 2;
/*     */     
/*     */     public static final int STD_URI = 3;
/*     */     
/*     */     public static final int STD_CLASS = 4;
/*     */     
/*     */     public static final int STD_JAVA_TYPE = 5;
/*     */     
/*     */     public static final int STD_CURRENCY = 6;
/*     */     
/*     */     public static final int STD_PATTERN = 7;
/*     */     
/*     */     public static final int STD_LOCALE = 8;
/*     */     
/*     */     public static final int STD_CHARSET = 9;
/*     */     
/*     */     public static final int STD_TIME_ZONE = 10;
/*     */     
/*     */     public static final int STD_INET_ADDRESS = 11;
/*     */     public static final int STD_INET_SOCKET_ADDRESS = 12;
/*     */     protected final int _kind;
/*     */     
/*     */     protected Std(Class<?> valueType, int kind)
/*     */     {
/* 200 */       super();
/* 201 */       this._kind = kind;
/*     */     }
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 207 */       switch (this._kind) {
/*     */       case 1: 
/* 209 */         return new File(value);
/*     */       case 2: 
/* 211 */         return new URL(value);
/*     */       case 3: 
/* 213 */         return URI.create(value);
/*     */       case 4: 
/*     */         try {
/* 216 */           return ctxt.findClass(value);
/*     */         } catch (Exception e) {
/* 218 */           throw ctxt.instantiationException(this._valueClass, ClassUtil.getRootCause(e));
/*     */         }
/*     */       case 5: 
/* 221 */         return ctxt.getTypeFactory().constructFromCanonical(value);
/*     */       
/*     */       case 6: 
/* 224 */         return Currency.getInstance(value);
/*     */       
/*     */       case 7: 
/* 227 */         return Pattern.compile(value);
/*     */       
/*     */       case 8: 
/* 230 */         int ix = value.indexOf('_');
/* 231 */         if (ix < 0) {
/* 232 */           return new Locale(value);
/*     */         }
/* 234 */         String first = value.substring(0, ix);
/* 235 */         value = value.substring(ix + 1);
/* 236 */         ix = value.indexOf('_');
/* 237 */         if (ix < 0) {
/* 238 */           return new Locale(first, value);
/*     */         }
/* 240 */         String second = value.substring(0, ix);
/* 241 */         return new Locale(first, second, value.substring(ix + 1));
/*     */       
/*     */       case 9: 
/* 244 */         return Charset.forName(value);
/*     */       case 10: 
/* 246 */         return TimeZone.getTimeZone(value);
/*     */       case 11: 
/* 248 */         return InetAddress.getByName(value);
/*     */       case 12: 
/* 250 */         if (value.startsWith("["))
/*     */         {
/*     */ 
/* 253 */           int i = value.lastIndexOf(']');
/* 254 */           if (i == -1) {
/* 255 */             throw new InvalidFormatException("Bracketed IPv6 address must contain closing bracket", value, InetSocketAddress.class);
/*     */           }
/*     */           
/*     */ 
/* 259 */           int j = value.indexOf(':', i);
/* 260 */           int port = j > -1 ? Integer.parseInt(value.substring(j + 1)) : 0;
/* 261 */           return new InetSocketAddress(value.substring(0, i + 1), port);
/*     */         }
/* 263 */         int ix = value.indexOf(':');
/* 264 */         if ((ix >= 0) && (value.indexOf(':', ix + 1) < 0))
/*     */         {
/* 266 */           int port = Integer.parseInt(value.substring(ix + 1));
/* 267 */           return new InetSocketAddress(value.substring(0, ix), port);
/*     */         }
/*     */         
/* 270 */         return new InetSocketAddress(value, 0);
/*     */       }
/*     */       
/* 273 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */     protected Object _deserializeFromEmptyString()
/*     */       throws IOException
/*     */     {
/* 279 */       if (this._kind == 3) {
/* 280 */         return URI.create("");
/*     */       }
/* 282 */       return super._deserializeFromEmptyString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\FromStringDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */