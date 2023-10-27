/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.IOException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class DateDeserializers
/*     */ {
/*  30 */   private static final HashSet<String> _classNames = new HashSet();
/*     */   
/*  32 */   static { Class<?>[] numberTypes = { Calendar.class, GregorianCalendar.class, java.sql.Date.class, java.util.Date.class, Timestamp.class };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */     for (Class<?> cls : numberTypes) {
/*  40 */       _classNames.add(cls.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static JsonDeserializer<?> find(Class<?> rawType, String clsName)
/*     */   {
/*  46 */     if (_classNames.contains(clsName))
/*     */     {
/*  48 */       if (rawType == Calendar.class) {
/*  49 */         return new CalendarDeserializer();
/*     */       }
/*  51 */       if (rawType == java.util.Date.class) {
/*  52 */         return DateDeserializer.instance;
/*     */       }
/*  54 */       if (rawType == java.sql.Date.class) {
/*  55 */         return new SqlDateDeserializer();
/*     */       }
/*  57 */       if (rawType == Timestamp.class) {
/*  58 */         return new TimestampDeserializer();
/*     */       }
/*  60 */       if (rawType == GregorianCalendar.class) {
/*  61 */         return new CalendarDeserializer(GregorianCalendar.class);
/*     */       }
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static abstract class DateBasedDeserializer<T>
/*     */     extends StdScalarDeserializer<T>
/*     */     implements ContextualDeserializer
/*     */   {
/*     */     protected final DateFormat _customFormat;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final String _formatString;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected DateBasedDeserializer(Class<?> clz)
/*     */     {
/*  89 */       super();
/*  90 */       this._customFormat = null;
/*  91 */       this._formatString = null;
/*     */     }
/*     */     
/*     */     protected DateBasedDeserializer(DateBasedDeserializer<T> base, DateFormat format, String formatStr)
/*     */     {
/*  96 */       super();
/*  97 */       this._customFormat = format;
/*  98 */       this._formatString = formatStr;
/*     */     }
/*     */     
/*     */ 
/*     */     protected abstract DateBasedDeserializer<T> withDateFormat(DateFormat paramDateFormat, String paramString);
/*     */     
/*     */     public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/* 107 */       if (property != null) {
/* 108 */         JsonFormat.Value format = ctxt.getAnnotationIntrospector().findFormat(property.getMember());
/* 109 */         if (format != null) {
/* 110 */           TimeZone tz = format.getTimeZone();
/*     */           
/* 112 */           if (format.hasPattern()) {
/* 113 */             String pattern = format.getPattern();
/* 114 */             Locale loc = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
/* 115 */             SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
/* 116 */             if (tz == null) {
/* 117 */               tz = ctxt.getTimeZone();
/*     */             }
/* 119 */             df.setTimeZone(tz);
/* 120 */             return withDateFormat(df, pattern);
/*     */           }
/*     */           
/* 123 */           if (tz != null) {
/* 124 */             DateFormat df = ctxt.getConfig().getDateFormat();
/*     */             
/* 126 */             if (df.getClass() == StdDateFormat.class) {
/* 127 */               Locale loc = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
/* 128 */               StdDateFormat std = (StdDateFormat)df;
/* 129 */               std = std.withTimeZone(tz);
/* 130 */               std = std.withLocale(loc);
/* 131 */               df = std;
/*     */             }
/*     */             else {
/* 134 */               df = (DateFormat)df.clone();
/* 135 */               df.setTimeZone(tz);
/*     */             }
/* 137 */             return withDateFormat(df, this._formatString);
/*     */           }
/*     */         }
/*     */       }
/* 141 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     protected java.util.Date _parseDate(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 148 */       if (this._customFormat != null) {
/* 149 */         JsonToken t = jp.getCurrentToken();
/* 150 */         if (t == JsonToken.VALUE_STRING) {
/* 151 */           String str = jp.getText().trim();
/* 152 */           if (str.length() == 0) {
/* 153 */             return (java.util.Date)getEmptyValue();
/*     */           }
/* 155 */           synchronized (this._customFormat) {
/*     */             try {
/* 157 */               return this._customFormat.parse(str);
/*     */             } catch (ParseException e) {
/* 159 */               throw new IllegalArgumentException("Failed to parse Date value '" + str + "' (format: \"" + this._formatString + "\"): " + e.getMessage());
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 165 */         if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 166 */           jp.nextToken();
/* 167 */           java.util.Date parsed = _parseDate(jp, ctxt);
/* 168 */           t = jp.nextToken();
/* 169 */           if (t != JsonToken.END_ARRAY) {
/* 170 */             throw ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single 'java.util.Date' value but there was more than a single value in the array");
/*     */           }
/*     */           
/* 173 */           return parsed;
/*     */         }
/*     */       }
/* 176 */       return super._parseDate(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CalendarDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<Calendar>
/*     */   {
/*     */     protected final Class<? extends Calendar> _calendarClass;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CalendarDeserializer()
/*     */     {
/* 196 */       super();
/* 197 */       this._calendarClass = null;
/*     */     }
/*     */     
/*     */     public CalendarDeserializer(Class<? extends Calendar> cc) {
/* 201 */       super();
/* 202 */       this._calendarClass = cc;
/*     */     }
/*     */     
/*     */     public CalendarDeserializer(CalendarDeserializer src, DateFormat df, String formatString) {
/* 206 */       super(df, formatString);
/* 207 */       this._calendarClass = src._calendarClass;
/*     */     }
/*     */     
/*     */     protected CalendarDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 212 */       return new CalendarDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public Calendar deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 218 */       java.util.Date d = _parseDate(jp, ctxt);
/* 219 */       if (d == null) {
/* 220 */         return null;
/*     */       }
/* 222 */       if (this._calendarClass == null) {
/* 223 */         return ctxt.constructCalendar(d);
/*     */       }
/*     */       try {
/* 226 */         Calendar c = (Calendar)this._calendarClass.newInstance();
/* 227 */         c.setTimeInMillis(d.getTime());
/* 228 */         TimeZone tz = ctxt.getTimeZone();
/* 229 */         if (tz != null) {
/* 230 */           c.setTimeZone(tz);
/*     */         }
/* 232 */         return c;
/*     */       } catch (Exception e) {
/* 234 */         throw ctxt.instantiationException(this._calendarClass, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class DateDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<java.util.Date>
/*     */   {
/* 248 */     public static final DateDeserializer instance = new DateDeserializer();
/*     */     
/* 250 */     public DateDeserializer() { super(); }
/*     */     
/* 252 */     public DateDeserializer(DateDeserializer base, DateFormat df, String formatString) { super(df, formatString); }
/*     */     
/*     */ 
/*     */     protected DateDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 257 */       return new DateDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public java.util.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 262 */       return _parseDate(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SqlDateDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<java.sql.Date>
/*     */   {
/* 273 */     public SqlDateDeserializer() { super(); }
/*     */     
/* 275 */     public SqlDateDeserializer(SqlDateDeserializer src, DateFormat df, String formatString) { super(df, formatString); }
/*     */     
/*     */ 
/*     */     protected SqlDateDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 280 */       return new SqlDateDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public java.sql.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 285 */       java.util.Date d = _parseDate(jp, ctxt);
/* 286 */       return d == null ? null : new java.sql.Date(d.getTime());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class TimestampDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<Timestamp>
/*     */   {
/* 299 */     public TimestampDeserializer() { super(); }
/*     */     
/* 301 */     public TimestampDeserializer(TimestampDeserializer src, DateFormat df, String formatString) { super(df, formatString); }
/*     */     
/*     */ 
/*     */     protected TimestampDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 306 */       return new TimestampDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 312 */       return new Timestamp(_parseDate(jp, ctxt).getTime());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\DateDeserializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */