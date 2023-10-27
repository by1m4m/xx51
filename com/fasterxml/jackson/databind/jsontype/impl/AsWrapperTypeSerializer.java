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
/*     */ public class AsWrapperTypeSerializer
/*     */   extends TypeSerializerBase
/*     */ {
/*     */   public AsWrapperTypeSerializer(TypeIdResolver idRes, BeanProperty property)
/*     */   {
/*  22 */     super(idRes, property);
/*     */   }
/*     */   
/*     */   public AsWrapperTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  27 */     return this._property == prop ? this : new AsWrapperTypeSerializer(this._idResolver, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  31 */     return JsonTypeInfo.As.WRAPPER_OBJECT;
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/*  36 */     String typeId = idFromValue(value);
/*  37 */     if (jgen.canWriteTypeId()) {
/*  38 */       if (typeId != null) {
/*  39 */         jgen.writeTypeId(typeId);
/*     */       }
/*  41 */       jgen.writeStartObject();
/*     */     }
/*     */     else {
/*  44 */       jgen.writeStartObject();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  49 */       jgen.writeObjectFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type)
/*     */     throws IOException
/*     */   {
/*  56 */     String typeId = idFromValueAndType(value, type);
/*  57 */     if (jgen.canWriteTypeId()) {
/*  58 */       if (typeId != null) {
/*  59 */         jgen.writeTypeId(typeId);
/*     */       }
/*  61 */       jgen.writeStartObject();
/*     */     }
/*     */     else {
/*  64 */       jgen.writeStartObject();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  69 */       jgen.writeObjectFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/*  76 */     String typeId = idFromValue(value);
/*  77 */     if (jgen.canWriteTypeId()) {
/*  78 */       if (typeId != null) {
/*  79 */         jgen.writeTypeId(typeId);
/*     */       }
/*  81 */       jgen.writeStartArray();
/*     */     }
/*     */     else {
/*  84 */       jgen.writeStartObject();
/*  85 */       jgen.writeArrayFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator jgen, Class<?> type)
/*     */     throws IOException
/*     */   {
/*  92 */     String typeId = idFromValueAndType(value, type);
/*  93 */     if (jgen.canWriteTypeId()) {
/*  94 */       if (typeId != null) {
/*  95 */         jgen.writeTypeId(typeId);
/*     */       }
/*  97 */       jgen.writeStartArray();
/*     */     }
/*     */     else {
/* 100 */       jgen.writeStartObject();
/*     */       
/* 102 */       jgen.writeArrayFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/* 108 */     String typeId = idFromValue(value);
/* 109 */     if (jgen.canWriteTypeId()) {
/* 110 */       if (typeId != null) {
/* 111 */         jgen.writeTypeId(typeId);
/*     */       }
/*     */     }
/*     */     else {
/* 115 */       jgen.writeStartObject();
/* 116 */       jgen.writeFieldName(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator jgen, Class<?> type)
/*     */     throws IOException
/*     */   {
/* 123 */     String typeId = idFromValueAndType(value, type);
/* 124 */     if (jgen.canWriteTypeId()) {
/* 125 */       if (typeId != null) {
/* 126 */         jgen.writeTypeId(typeId);
/*     */       }
/*     */     }
/*     */     else {
/* 130 */       jgen.writeStartObject();
/* 131 */       jgen.writeFieldName(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/* 139 */     jgen.writeEndObject();
/* 140 */     if (!jgen.canWriteTypeId())
/*     */     {
/* 142 */       jgen.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeTypeSuffixForArray(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/* 150 */     jgen.writeEndArray();
/* 151 */     if (!jgen.canWriteTypeId())
/*     */     {
/* 153 */       jgen.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForScalar(Object value, JsonGenerator jgen) throws IOException
/*     */   {
/* 159 */     if (!jgen.canWriteTypeId())
/*     */     {
/* 161 */       jgen.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId)
/*     */     throws IOException
/*     */   {
/* 173 */     if (jgen.canWriteTypeId()) {
/* 174 */       if (typeId != null) {
/* 175 */         jgen.writeTypeId(typeId);
/*     */       }
/* 177 */       jgen.writeStartObject();
/*     */     } else {
/* 179 */       jgen.writeStartObject();
/* 180 */       jgen.writeObjectFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 186 */     if (jgen.canWriteTypeId()) {
/* 187 */       if (typeId != null) {
/* 188 */         jgen.writeTypeId(typeId);
/*     */       }
/* 190 */       jgen.writeStartArray();
/*     */     } else {
/* 192 */       jgen.writeStartObject();
/* 193 */       jgen.writeArrayFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 199 */     if (jgen.canWriteTypeId()) {
/* 200 */       if (typeId != null) {
/* 201 */         jgen.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/* 204 */       jgen.writeStartObject();
/* 205 */       jgen.writeFieldName(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 211 */     if (!jgen.canWriteTypeId()) {
/* 212 */       writeTypeSuffixForObject(value, jgen);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForArray(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 218 */     if (!jgen.canWriteTypeId()) {
/* 219 */       writeTypeSuffixForArray(value, jgen);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 225 */     if (!jgen.canWriteTypeId()) {
/* 226 */       writeTypeSuffixForScalar(value, jgen);
/*     */     }
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
/*     */   protected String _validTypeId(String typeId)
/*     */   {
/* 240 */     return typeId == null ? "" : typeId;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsWrapperTypeSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */