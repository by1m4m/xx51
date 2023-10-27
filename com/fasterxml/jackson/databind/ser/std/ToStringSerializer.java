/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
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
/*     */ @JacksonStdImpl
/*     */ public class ToStringSerializer
/*     */   extends StdSerializer<Object>
/*     */ {
/*  28 */   public static final ToStringSerializer instance = new ToStringSerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ToStringSerializer()
/*     */   {
/*  38 */     super(Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ToStringSerializer(Class<?> handledType)
/*     */   {
/*  46 */     super(handledType, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isEmpty(Object value)
/*     */   {
/*  52 */     return isEmpty(null, value);
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Object value)
/*     */   {
/*  57 */     if (value == null) {
/*  58 */       return true;
/*     */     }
/*  60 */     String str = value.toString();
/*  61 */     return str.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  68 */     gen.writeString(value.toString());
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
/*     */ 
/*     */   public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/*  87 */     typeSer.writeTypePrefixForScalar(value, gen);
/*  88 */     serialize(value, gen, provider);
/*  89 */     typeSer.writeTypeSuffixForScalar(value, gen);
/*     */   }
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException
/*     */   {
/*  94 */     return createSchemaNode("string", true);
/*     */   }
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 100 */     if (visitor != null) {
/* 101 */       visitor.expectStringFormat(typeHint);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\ToStringSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */