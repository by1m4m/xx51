/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DateTimeSerializerBase<T>
/*     */   extends StdScalarSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final Boolean _useTimestamp;
/*     */   protected final DateFormat _customFormat;
/*     */   
/*     */   protected DateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat)
/*     */   {
/*  41 */     super(type);
/*  42 */     this._useTimestamp = useTimestamp;
/*  43 */     this._customFormat = customFormat;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract DateTimeSerializerBase<T> withFormat(Boolean paramBoolean, DateFormat paramDateFormat);
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  52 */     if (property != null) {
/*  53 */       JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat(property.getMember());
/*  54 */       if (format != null)
/*     */       {
/*     */ 
/*  57 */         if (format.getShape().isNumeric()) {
/*  58 */           return withFormat(Boolean.TRUE, null);
/*     */         }
/*     */         
/*  61 */         Boolean asNumber = format.getShape() == JsonFormat.Shape.STRING ? Boolean.FALSE : null;
/*     */         
/*  63 */         TimeZone tz = format.getTimeZone();
/*  64 */         if (format.hasPattern()) {
/*  65 */           String pattern = format.getPattern();
/*  66 */           Locale loc = format.hasLocale() ? format.getLocale() : prov.getLocale();
/*  67 */           SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
/*  68 */           if (tz == null) {
/*  69 */             tz = prov.getTimeZone();
/*     */           }
/*  71 */           df.setTimeZone(tz);
/*  72 */           return withFormat(asNumber, df);
/*     */         }
/*     */         
/*  75 */         if (tz != null) {
/*  76 */           DateFormat df = prov.getConfig().getDateFormat();
/*     */           
/*  78 */           if (df.getClass() == StdDateFormat.class) {
/*  79 */             Locale loc = format.hasLocale() ? format.getLocale() : prov.getLocale();
/*  80 */             df = StdDateFormat.getISO8601Format(tz, loc);
/*     */           }
/*     */           else {
/*  83 */             df = (DateFormat)df.clone();
/*  84 */             df.setTimeZone(tz);
/*     */           }
/*  86 */           return withFormat(asNumber, df);
/*     */         }
/*     */       }
/*     */     }
/*  90 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty(T value)
/*     */   {
/* 102 */     return (value == null) || (_timestamp(value) == 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract long _timestamp(T paramT);
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 110 */     return createSchemaNode(_asTimestamp(provider) ? "number" : "string", true);
/*     */   }
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 116 */     _acceptJsonFormatVisitor(visitor, typeHint, _asTimestamp(visitor.getProvider()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException, JsonGenerationException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _asTimestamp(SerializerProvider provider)
/*     */   {
/* 137 */     if (this._useTimestamp != null) {
/* 138 */       return this._useTimestamp.booleanValue();
/*     */     }
/* 140 */     if (this._customFormat == null) {
/* 141 */       if (provider != null) {
/* 142 */         return provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */       }
/*     */       
/* 145 */       throw new IllegalArgumentException("Null 'provider' passed for " + handledType().getName());
/*     */     }
/* 147 */     return false;
/*     */   }
/*     */   
/*     */   protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
/*     */     throws JsonMappingException
/*     */   {
/* 153 */     if (asNumber) {
/* 154 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 155 */       if (v2 != null) {
/* 156 */         v2.numberType(JsonParser.NumberType.LONG);
/* 157 */         v2.format(JsonValueFormat.UTC_MILLISEC);
/*     */       }
/*     */     } else {
/* 160 */       JsonStringFormatVisitor v2 = visitor.expectStringFormat(typeHint);
/* 161 */       if (v2 != null) {
/* 162 */         v2.format(JsonValueFormat.DATE_TIME);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\DateTimeSerializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */