/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class IteratorSerializer extends AsArraySerializerBase<Iterator<?>>
/*    */ {
/*    */   public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*    */   {
/* 21 */     super(Iterator.class, elemType, staticTyping, vts, property, null);
/*    */   }
/*    */   
/*    */ 
/*    */   public IteratorSerializer(IteratorSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer)
/*    */   {
/* 27 */     super(src, property, vts, valueSerializer);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, Iterator<?> value)
/*    */   {
/* 32 */     return (value == null) || (!value.hasNext());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean hasSingleElement(Iterator<?> value)
/*    */   {
/* 38 */     return false;
/*    */   }
/*    */   
/*    */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*    */   {
/* 43 */     return new IteratorSerializer(this._elementType, this._staticTyping, vts, this._property);
/*    */   }
/*    */   
/*    */ 
/*    */   public IteratorSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*    */   {
/* 49 */     return new IteratorSerializer(this, property, vts, elementSerializer);
/*    */   }
/*    */   
/*    */   public final void serialize(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 55 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(value))) {
/* 56 */       serializeContents(value, jgen, provider);
/* 57 */       return;
/*    */     }
/* 59 */     jgen.writeStartArray();
/* 60 */     serializeContents(value, jgen, provider);
/* 61 */     jgen.writeEndArray();
/*    */   }
/*    */   
/*    */ 
/*    */   public void serializeContents(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 68 */     if (value.hasNext()) {
/* 69 */       TypeSerializer typeSer = this._valueTypeSerializer;
/* 70 */       JsonSerializer<Object> prevSerializer = null;
/* 71 */       Class<?> prevClass = null;
/*    */       do {
/* 73 */         Object elem = value.next();
/* 74 */         if (elem == null) {
/* 75 */           provider.defaultSerializeNull(jgen);
/*    */         }
/*    */         else {
/* 78 */           JsonSerializer<Object> currSerializer = this._elementSerializer;
/* 79 */           if (currSerializer == null)
/*    */           {
/* 81 */             Class<?> cc = elem.getClass();
/* 82 */             if (cc == prevClass) {
/* 83 */               currSerializer = prevSerializer;
/*    */             } else {
/* 85 */               currSerializer = provider.findValueSerializer(cc, this._property);
/* 86 */               prevSerializer = currSerializer;
/* 87 */               prevClass = cc;
/*    */             }
/*    */           }
/* 90 */           if (typeSer == null) {
/* 91 */             currSerializer.serialize(elem, jgen, provider);
/*    */           } else
/* 93 */             currSerializer.serializeWithType(elem, jgen, provider, typeSer);
/*    */         }
/* 95 */       } while (value.hasNext());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\IteratorSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */