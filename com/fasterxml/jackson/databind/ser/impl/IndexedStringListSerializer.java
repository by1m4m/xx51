/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class IndexedStringListSerializer
/*     */   extends StaticListSerializerBase<List<String>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  31 */   public static final IndexedStringListSerializer instance = new IndexedStringListSerializer();
/*     */   
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<String> _serializer;
/*     */   
/*     */ 
/*     */ 
/*     */   protected IndexedStringListSerializer()
/*     */   {
/*  41 */     this(null);
/*     */   }
/*     */   
/*     */   public IndexedStringListSerializer(JsonSerializer<?> ser) {
/*  45 */     super(List.class);
/*  46 */     this._serializer = ser;
/*     */   }
/*     */   
/*  49 */   protected JsonNode contentSchema() { return createSchemaNode("string", true); }
/*     */   
/*     */   protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException
/*     */   {
/*  53 */     visitor.itemsFormat(JsonFormatTypes.STRING);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  69 */     JsonSerializer<?> ser = null;
/*     */     
/*  71 */     if (property != null) {
/*  72 */       AnnotatedMember m = property.getMember();
/*  73 */       if (m != null) {
/*  74 */         Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
/*  75 */         if (serDef != null) {
/*  76 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/*  80 */     if (ser == null) {
/*  81 */       ser = this._serializer;
/*     */     }
/*     */     
/*  84 */     ser = findConvertingContentSerializer(provider, property, ser);
/*  85 */     if (ser == null) {
/*  86 */       ser = provider.findValueSerializer(String.class, property);
/*     */     } else {
/*  88 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/*     */     
/*  91 */     if (isDefaultSerializer(ser)) {
/*  92 */       ser = null;
/*     */     }
/*     */     
/*  95 */     if (ser == this._serializer) {
/*  96 */       return this;
/*     */     }
/*  98 */     return new IndexedStringListSerializer(ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(List<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 110 */     int len = value.size();
/*     */     
/* 112 */     if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 113 */       _serializeUnwrapped(value, jgen, provider);
/* 114 */       return;
/*     */     }
/*     */     
/* 117 */     jgen.writeStartArray(len);
/* 118 */     if (this._serializer == null) {
/* 119 */       serializeContents(value, jgen, provider, len);
/*     */     } else {
/* 121 */       serializeUsingCustom(value, jgen, provider, len);
/*     */     }
/* 123 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */   private final void _serializeUnwrapped(List<String> value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */   {
/* 128 */     if (this._serializer == null) {
/* 129 */       serializeContents(value, jgen, provider, 1);
/*     */     } else {
/* 131 */       serializeUsingCustom(value, jgen, provider, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(List<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 139 */     int len = value.size();
/* 140 */     typeSer.writeTypePrefixForArray(value, jgen);
/* 141 */     if (this._serializer == null) {
/* 142 */       serializeContents(value, jgen, provider, len);
/*     */     } else {
/* 144 */       serializeUsingCustom(value, jgen, provider, len);
/*     */     }
/* 146 */     typeSer.writeTypeSuffixForArray(value, jgen);
/*     */   }
/*     */   
/*     */   private final void serializeContents(List<String> value, JsonGenerator jgen, SerializerProvider provider, int len)
/*     */     throws IOException
/*     */   {
/* 152 */     int i = 0;
/*     */     try {
/* 154 */       for (; i < len; i++) {
/* 155 */         String str = (String)value.get(i);
/* 156 */         if (str == null) {
/* 157 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 159 */           jgen.writeString(str);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 163 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */   
/*     */   private final void serializeUsingCustom(List<String> value, JsonGenerator jgen, SerializerProvider provider, int len)
/*     */     throws IOException
/*     */   {
/* 170 */     int i = 0;
/*     */     try {
/* 172 */       JsonSerializer<String> ser = this._serializer;
/* 173 */       for (i = 0; i < len; i++) {
/* 174 */         String str = (String)value.get(i);
/* 175 */         if (str == null) {
/* 176 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 178 */           ser.serialize(str, jgen, provider);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 182 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\IndexedStringListSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */