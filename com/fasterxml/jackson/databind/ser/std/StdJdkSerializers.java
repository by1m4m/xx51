/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.Currency;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class StdJdkSerializers
/*     */ {
/*     */   public static Collection<java.util.Map.Entry<Class<?>, Object>> all()
/*     */   {
/*  28 */     HashMap<Class<?>, Object> sers = new HashMap();
/*     */     
/*     */ 
/*  31 */     ToStringSerializer sls = ToStringSerializer.instance;
/*     */     
/*  33 */     sers.put(java.net.URL.class, sls);
/*  34 */     sers.put(java.net.URI.class, sls);
/*     */     
/*  36 */     sers.put(Currency.class, sls);
/*  37 */     sers.put(java.util.UUID.class, new UUIDSerializer());
/*  38 */     sers.put(Pattern.class, sls);
/*  39 */     sers.put(Locale.class, sls);
/*     */     
/*     */ 
/*  42 */     sers.put(Locale.class, sls);
/*     */     
/*     */ 
/*  45 */     sers.put(AtomicReference.class, AtomicReferenceSerializer.class);
/*  46 */     sers.put(AtomicBoolean.class, AtomicBooleanSerializer.class);
/*  47 */     sers.put(AtomicInteger.class, AtomicIntegerSerializer.class);
/*  48 */     sers.put(AtomicLong.class, AtomicLongSerializer.class);
/*     */     
/*     */ 
/*  51 */     sers.put(java.io.File.class, FileSerializer.class);
/*  52 */     sers.put(Class.class, ClassSerializer.class);
/*     */     
/*     */ 
/*  55 */     sers.put(Void.class, NullSerializer.instance);
/*  56 */     sers.put(Void.TYPE, NullSerializer.instance);
/*     */     
/*  58 */     return sers.entrySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class AtomicBooleanSerializer
/*     */     extends StdScalarSerializer<AtomicBoolean>
/*     */   {
/*     */     public AtomicBooleanSerializer()
/*     */     {
/*  70 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicBoolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/*  74 */       jgen.writeBoolean(value.get());
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/*  79 */       return createSchemaNode("boolean", true);
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
/*     */     {
/*  84 */       visitor.expectBooleanFormat(typeHint);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AtomicIntegerSerializer extends StdScalarSerializer<AtomicInteger>
/*     */   {
/*     */     public AtomicIntegerSerializer() {
/*  91 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicInteger value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/*  95 */       jgen.writeNumber(value.get());
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 100 */       return createSchemaNode("integer", true);
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 106 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 107 */       if (v2 != null) {
/* 108 */         v2.numberType(JsonParser.NumberType.INT);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AtomicLongSerializer extends StdScalarSerializer<AtomicLong>
/*     */   {
/*     */     public AtomicLongSerializer() {
/* 116 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicLong value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 120 */       jgen.writeNumber(value.get());
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 125 */       return createSchemaNode("integer", true);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 132 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 133 */       if (v2 != null) {
/* 134 */         v2.numberType(JsonParser.NumberType.LONG);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AtomicReferenceSerializer extends StdSerializer<AtomicReference<?>>
/*     */   {
/*     */     public AtomicReferenceSerializer() {
/* 142 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicReference<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 146 */       provider.defaultSerializeValue(value.get(), jgen);
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 151 */       return createSchemaNode("any", true);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 158 */       visitor.expectAnyFormat(typeHint);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StdJdkSerializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */