/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import java.io.IOException;
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
/*     */ public class AsExternalTypeSerializer
/*     */   extends TypeSerializerBase
/*     */ {
/*     */   protected final String _typePropertyName;
/*     */   
/*     */   public AsExternalTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*     */   {
/*  30 */     super(idRes, property);
/*  31 */     this._typePropertyName = propName;
/*     */   }
/*     */   
/*     */   public AsExternalTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  36 */     return this._property == prop ? this : new AsExternalTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*     */   }
/*     */   
/*     */   public String getPropertyName() {
/*  40 */     return this._typePropertyName;
/*     */   }
/*     */   
/*  43 */   public JsonTypeInfo.As getTypeInclusion() { return JsonTypeInfo.As.EXTERNAL_PROPERTY; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/*  53 */     _writeObjectPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type) throws IOException
/*     */   {
/*  58 */     _writeObjectPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/*  63 */     _writeArrayPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type) throws IOException
/*     */   {
/*  68 */     _writeArrayPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/*  73 */     _writeScalarPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type) throws IOException
/*     */   {
/*  78 */     _writeScalarPrefix(value, jgen);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/*  89 */     _writeObjectSuffix(value, jgen, idFromValue(value));
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForArray(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/*  94 */     _writeArraySuffix(value, jgen, idFromValue(value));
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/*  99 */     _writeScalarSuffix(value, jgen, idFromValue(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeCustomTypePrefixForScalar(Object value, JsonGenerator jgen, String typeId)
/*     */     throws IOException
/*     */   {
/* 110 */     _writeScalarPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 115 */     _writeObjectPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 120 */     _writeArrayPrefix(value, jgen);
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 125 */     _writeScalarSuffix(value, jgen, typeId);
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 130 */     _writeObjectSuffix(value, jgen, typeId);
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 135 */     _writeArraySuffix(value, jgen, typeId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void _writeScalarPrefix(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */   protected final void _writeObjectPrefix(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/* 148 */     jgen.writeStartObject();
/*     */   }
/*     */   
/*     */   protected final void _writeArrayPrefix(Object value, JsonGenerator jgen) throws IOException {
/* 152 */     jgen.writeStartArray();
/*     */   }
/*     */   
/*     */   protected final void _writeScalarSuffix(Object value, JsonGenerator jgen, String typeId) throws IOException {
/* 156 */     if (typeId != null) {
/* 157 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void _writeObjectSuffix(Object value, JsonGenerator jgen, String typeId) throws IOException {
/* 162 */     jgen.writeEndObject();
/* 163 */     if (typeId != null) {
/* 164 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void _writeArraySuffix(Object value, JsonGenerator jgen, String typeId) throws IOException {
/* 169 */     jgen.writeEndArray();
/* 170 */     if (typeId != null) {
/* 171 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExternalTypeSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */