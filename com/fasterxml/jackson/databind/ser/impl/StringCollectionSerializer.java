/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
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
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StringCollectionSerializer
/*     */   extends StaticListSerializerBase<Collection<String>>
/*     */   implements ContextualSerializer
/*     */ {
/*  29 */   public static final StringCollectionSerializer instance = new StringCollectionSerializer();
/*     */   
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<String> _serializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringCollectionSerializer()
/*     */   {
/*  40 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected StringCollectionSerializer(JsonSerializer<?> ser)
/*     */   {
/*  46 */     super(Collection.class);
/*  47 */     this._serializer = ser;
/*     */   }
/*     */   
/*     */   protected JsonNode contentSchema() {
/*  51 */     return createSchemaNode("string", true);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void acceptContentVisitor(JsonArrayFormatVisitor visitor)
/*     */     throws JsonMappingException
/*     */   {
/*  58 */     visitor.itemsFormat(JsonFormatTypes.STRING);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  76 */     JsonSerializer<?> ser = null;
/*     */     
/*  78 */     if (property != null) {
/*  79 */       AnnotatedMember m = property.getMember();
/*  80 */       if (m != null) {
/*  81 */         Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
/*  82 */         if (serDef != null) {
/*  83 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/*  87 */     if (ser == null) {
/*  88 */       ser = this._serializer;
/*     */     }
/*     */     
/*  91 */     ser = findConvertingContentSerializer(provider, property, ser);
/*  92 */     if (ser == null) {
/*  93 */       ser = provider.findValueSerializer(String.class, property);
/*     */     } else {
/*  95 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/*     */     
/*  98 */     if (isDefaultSerializer(ser)) {
/*  99 */       ser = null;
/*     */     }
/*     */     
/* 102 */     if (ser == this._serializer) {
/* 103 */       return this;
/*     */     }
/* 105 */     return new StringCollectionSerializer(ser);
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
/*     */   public void serialize(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 119 */     int len = value.size();
/* 120 */     if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 121 */       _serializeUnwrapped(value, jgen, provider);
/* 122 */       return;
/*     */     }
/* 124 */     jgen.writeStartArray(len);
/* 125 */     if (this._serializer == null) {
/* 126 */       serializeContents(value, jgen, provider);
/*     */     } else {
/* 128 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/* 130 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */   private final void _serializeUnwrapped(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 136 */     if (this._serializer == null) {
/* 137 */       serializeContents(value, jgen, provider);
/*     */     } else {
/* 139 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 148 */     typeSer.writeTypePrefixForArray(value, jgen);
/* 149 */     if (this._serializer == null) {
/* 150 */       serializeContents(value, jgen, provider);
/*     */     } else {
/* 152 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/* 154 */     typeSer.writeTypeSuffixForArray(value, jgen);
/*     */   }
/*     */   
/*     */   private final void serializeContents(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 160 */     if (this._serializer != null) {
/* 161 */       serializeUsingCustom(value, jgen, provider);
/* 162 */       return;
/*     */     }
/* 164 */     int i = 0;
/* 165 */     for (String str : value) {
/*     */       try {
/* 167 */         if (str == null) {
/* 168 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 170 */           jgen.writeString(str);
/*     */         }
/* 172 */         i++;
/*     */       } catch (Exception e) {
/* 174 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void serializeUsingCustom(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 182 */     JsonSerializer<String> ser = this._serializer;
/* 183 */     int i = 0;
/* 184 */     for (String str : value) {
/*     */       try {
/* 186 */         if (str == null) {
/* 187 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 189 */           ser.serialize(str, jgen, provider);
/*     */         }
/*     */       } catch (Exception e) {
/* 192 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\StringCollectionSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */