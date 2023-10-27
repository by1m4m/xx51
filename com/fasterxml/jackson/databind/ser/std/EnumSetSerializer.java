/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public class EnumSetSerializer extends AsArraySerializerBase<EnumSet<? extends Enum<?>>>
/*    */ {
/*    */   public EnumSetSerializer(JavaType elemType, BeanProperty property)
/*    */   {
/* 16 */     super(EnumSet.class, elemType, true, null, property, null);
/*    */   }
/*    */   
/*    */ 
/*    */   public EnumSetSerializer(EnumSetSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer)
/*    */   {
/* 22 */     super(src, property, vts, valueSerializer);
/*    */   }
/*    */   
/*    */ 
/*    */   public EnumSetSerializer _withValueTypeSerializer(TypeSerializer vts)
/*    */   {
/* 28 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */   public EnumSetSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*    */   {
/* 34 */     return new EnumSetSerializer(this, property, vts, elementSerializer);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, EnumSet<? extends Enum<?>> value)
/*    */   {
/* 39 */     return (value == null) || (value.isEmpty());
/*    */   }
/*    */   
/*    */   public boolean hasSingleElement(EnumSet<? extends Enum<?>> value)
/*    */   {
/* 44 */     return value.size() == 1;
/*    */   }
/*    */   
/*    */   public final void serialize(EnumSet<? extends Enum<?>> value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 50 */     int len = value.size();
/* 51 */     if ((len == 1) && (provider.isEnabled(com.fasterxml.jackson.databind.SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 52 */       serializeContents(value, jgen, provider);
/* 53 */       return;
/*    */     }
/* 55 */     jgen.writeStartArray(len);
/* 56 */     serializeContents(value, jgen, provider);
/* 57 */     jgen.writeEndArray();
/*    */   }
/*    */   
/*    */ 
/*    */   public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*    */   {
/* 64 */     JsonSerializer<Object> enumSer = this._elementSerializer;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 69 */     for (Enum<?> en : value) {
/* 70 */       if (enumSer == null)
/*    */       {
/*    */ 
/*    */ 
/* 74 */         enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
/*    */       }
/* 76 */       enumSer.serialize(en, jgen, provider);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\EnumSetSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */