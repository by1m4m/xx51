/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StringArraySerializer
/*     */   extends ArraySerializerBase<String[]>
/*     */   implements ContextualSerializer
/*     */ {
/*  32 */   private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(String.class);
/*     */   
/*  34 */   public static final StringArraySerializer instance = new StringArraySerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<Object> _elementSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringArraySerializer()
/*     */   {
/*  49 */     super(String[].class, null);
/*  50 */     this._elementSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public StringArraySerializer(StringArraySerializer src, BeanProperty prop, JsonSerializer<?> ser)
/*     */   {
/*  56 */     super(src, prop);
/*  57 */     this._elementSerializer = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  66 */     return this;
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
/*  84 */     JsonSerializer<?> ser = null;
/*     */     
/*  86 */     if (property != null) {
/*  87 */       AnnotatedMember m = property.getMember();
/*  88 */       if (m != null) {
/*  89 */         Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
/*  90 */         if (serDef != null) {
/*  91 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/*  95 */     if (ser == null) {
/*  96 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/*  99 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 100 */     if (ser == null) {
/* 101 */       ser = provider.findValueSerializer(String.class, property);
/*     */     } else {
/* 103 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/*     */     
/* 106 */     if (isDefaultSerializer(ser)) {
/* 107 */       ser = null;
/*     */     }
/*     */     
/* 110 */     if (ser == this._elementSerializer) {
/* 111 */       return this;
/*     */     }
/* 113 */     return new StringArraySerializer(this, property, ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 124 */     return VALUE_TYPE;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 129 */     return this._elementSerializer;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, String[] value)
/*     */   {
/* 134 */     return (value == null) || (value.length == 0);
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(String[] value)
/*     */   {
/* 139 */     return value.length == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(String[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 152 */     int len = value.length;
/* 153 */     if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 154 */       serializeContents(value, jgen, provider);
/* 155 */       return;
/*     */     }
/* 157 */     jgen.writeStartArray(len);
/* 158 */     serializeContents(value, jgen, provider);
/* 159 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContents(String[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 166 */     int len = value.length;
/* 167 */     if (len == 0) {
/* 168 */       return;
/*     */     }
/* 170 */     if (this._elementSerializer != null) {
/* 171 */       serializeContentsSlow(value, jgen, provider, this._elementSerializer);
/* 172 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 182 */     for (int i = 0; i < len; i++) {
/* 183 */       String str = value[i];
/* 184 */       if (str == null) {
/* 185 */         jgen.writeNull();
/*     */       }
/*     */       else {
/* 188 */         jgen.writeString(value[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void serializeContentsSlow(String[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 196 */     int i = 0; for (int len = value.length; i < len; i++) {
/* 197 */       String str = value[i];
/* 198 */       if (str == null) {
/* 199 */         provider.defaultSerializeNull(jgen);
/*     */       } else {
/* 201 */         ser.serialize(value[i], jgen, provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 208 */     return createSchemaNode("array", true).set("items", createSchemaNode("string"));
/*     */   }
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 214 */     if (visitor != null) {
/* 215 */       JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 216 */       if (v2 != null) {
/* 217 */         v2.itemsFormat(JsonFormatTypes.STRING);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\StringArraySerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */