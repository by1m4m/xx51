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
/*     */ public class AsPropertyTypeSerializer
/*     */   extends AsArrayTypeSerializer
/*     */ {
/*     */   protected final String _typePropertyName;
/*     */   
/*     */   public AsPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*     */   {
/*  27 */     super(idRes, property);
/*  28 */     this._typePropertyName = propName;
/*     */   }
/*     */   
/*     */   public AsPropertyTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  33 */     return this._property == prop ? this : new AsPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*     */   }
/*     */   
/*     */   public String getPropertyName() {
/*  37 */     return this._typePropertyName;
/*     */   }
/*     */   
/*  40 */   public JsonTypeInfo.As getTypeInclusion() { return JsonTypeInfo.As.PROPERTY; }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/*  45 */     String typeId = idFromValue(value);
/*  46 */     if (typeId == null) {
/*  47 */       jgen.writeStartObject();
/*  48 */     } else if (jgen.canWriteTypeId()) {
/*  49 */       jgen.writeTypeId(typeId);
/*  50 */       jgen.writeStartObject();
/*     */     } else {
/*  52 */       jgen.writeStartObject();
/*  53 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type)
/*     */     throws IOException
/*     */   {
/*  60 */     String typeId = idFromValueAndType(value, type);
/*  61 */     if (typeId == null) {
/*  62 */       jgen.writeStartObject();
/*  63 */     } else if (jgen.canWriteTypeId()) {
/*  64 */       jgen.writeTypeId(typeId);
/*  65 */       jgen.writeStartObject();
/*     */     } else {
/*  67 */       jgen.writeStartObject();
/*  68 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
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
/*  80 */     jgen.writeEndObject();
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
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId)
/*     */     throws IOException
/*     */   {
/*  98 */     if (typeId == null) {
/*  99 */       jgen.writeStartObject();
/* 100 */     } else if (jgen.canWriteTypeId()) {
/* 101 */       jgen.writeTypeId(typeId);
/* 102 */       jgen.writeStartObject();
/*     */     } else {
/* 104 */       jgen.writeStartObject();
/* 105 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 111 */     jgen.writeEndObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsPropertyTypeSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */