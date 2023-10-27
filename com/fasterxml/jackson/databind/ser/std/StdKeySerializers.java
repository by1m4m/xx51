/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class StdKeySerializers
/*     */ {
/*  13 */   protected static final JsonSerializer<Object> DEFAULT_KEY_SERIALIZER = new StdKeySerializer();
/*     */   
/*     */ 
/*  16 */   protected static final JsonSerializer<Object> DEFAULT_STRING_SERIALIZER = new StringKeySerializer();
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
/*     */   public static JsonSerializer<Object> getStdKeySerializer(com.fasterxml.jackson.databind.SerializationConfig config, Class<?> rawKeyType, boolean useDefault)
/*     */   {
/*  32 */     if (rawKeyType != null) {
/*  33 */       if (rawKeyType == String.class) {
/*  34 */         return DEFAULT_STRING_SERIALIZER;
/*     */       }
/*  36 */       if ((rawKeyType == Object.class) || (rawKeyType.isPrimitive()) || (Number.class.isAssignableFrom(rawKeyType)))
/*     */       {
/*  38 */         return DEFAULT_KEY_SERIALIZER;
/*     */       }
/*  40 */       if (rawKeyType == Class.class) {
/*  41 */         return ClassKeySerializer.instance;
/*     */       }
/*  43 */       if (Date.class.isAssignableFrom(rawKeyType)) {
/*  44 */         return DateKeySerializer.instance;
/*     */       }
/*  46 */       if (Calendar.class.isAssignableFrom(rawKeyType)) {
/*  47 */         return CalendarKeySerializer.instance;
/*     */       }
/*     */     }
/*  50 */     return useDefault ? DEFAULT_KEY_SERIALIZER : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonSerializer<Object> getStdKeySerializer(com.fasterxml.jackson.databind.JavaType keyType)
/*     */   {
/*  58 */     return getStdKeySerializer(null, keyType.getRawClass(), true);
/*     */   }
/*     */   
/*     */   public static JsonSerializer<Object> getDefault() {
/*  62 */     return DEFAULT_KEY_SERIALIZER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class StringKeySerializer
/*     */     extends StdSerializer<String>
/*     */   {
/*     */     public StringKeySerializer()
/*     */     {
/*  73 */       super();
/*     */     }
/*     */     
/*     */     public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/*  77 */       jgen.writeFieldName(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DateKeySerializer extends StdSerializer<Date> {
/*  82 */     protected static final JsonSerializer<?> instance = new DateKeySerializer();
/*     */     
/*  84 */     public DateKeySerializer() { super(); }
/*     */     
/*     */     public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException
/*     */     {
/*  88 */       provider.defaultSerializeDateKey(value, jgen);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CalendarKeySerializer extends StdSerializer<Calendar> {
/*  93 */     protected static final JsonSerializer<?> instance = new CalendarKeySerializer();
/*     */     
/*  95 */     public CalendarKeySerializer() { super(); }
/*     */     
/*     */     public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException
/*     */     {
/*  99 */       provider.defaultSerializeDateKey(value.getTimeInMillis(), jgen);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ClassKeySerializer extends StdSerializer<Class<?>> {
/* 104 */     protected static final JsonSerializer<?> instance = new ClassKeySerializer();
/*     */     
/* 106 */     public ClassKeySerializer() { super(false); }
/*     */     
/*     */     public void serialize(Class<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException
/*     */     {
/* 110 */       jgen.writeFieldName(value.getName());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StdKeySerializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */