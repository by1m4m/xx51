/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsExistingPropertyTypeSerializer
/*    */   extends AsPropertyTypeSerializer
/*    */ {
/*    */   public AsExistingPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*    */   {
/* 23 */     super(idRes, property, propName);
/*    */   }
/*    */   
/*    */   public AsExistingPropertyTypeSerializer forProperty(BeanProperty prop)
/*    */   {
/* 28 */     return this._property == prop ? this : new AsExistingPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*    */   }
/*    */   
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 32 */     return JsonTypeInfo.As.EXISTING_PROPERTY;
/*    */   }
/*    */   
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen) throws IOException
/*    */   {
/* 37 */     String typeId = idFromValue(value);
/* 38 */     if ((typeId != null) && (jgen.canWriteTypeId())) {
/* 39 */       jgen.writeTypeId(typeId);
/*    */     }
/* 41 */     jgen.writeStartObject();
/*    */   }
/*    */   
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type)
/*    */     throws IOException
/*    */   {
/* 47 */     String typeId = idFromValueAndType(value, type);
/* 48 */     if ((typeId != null) && (jgen.canWriteTypeId())) {
/* 49 */       jgen.writeTypeId(typeId);
/*    */     }
/* 51 */     jgen.writeStartObject();
/*    */   }
/*    */   
/*    */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId)
/*    */     throws IOException
/*    */   {
/* 57 */     if ((typeId != null) && (jgen.canWriteTypeId())) {
/* 58 */       jgen.writeTypeId(typeId);
/*    */     }
/* 60 */     jgen.writeStartObject();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExistingPropertyTypeSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */