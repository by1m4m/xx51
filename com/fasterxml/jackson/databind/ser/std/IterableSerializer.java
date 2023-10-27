/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class IterableSerializer extends AsArraySerializerBase<Iterable<?>>
/*     */ {
/*     */   public IterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  20 */     super(Iterable.class, elemType, staticTyping, vts, property, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public IterableSerializer(IterableSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer)
/*     */   {
/*  26 */     super(src, property, vts, valueSerializer);
/*     */   }
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  31 */     return new IterableSerializer(this._elementType, this._staticTyping, vts, this._property);
/*     */   }
/*     */   
/*     */ 
/*     */   public IterableSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/*  37 */     return new IterableSerializer(this, property, vts, elementSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, Iterable<?> value)
/*     */   {
/*  43 */     return (value == null) || (!value.iterator().hasNext());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasSingleElement(Iterable<?> value)
/*     */   {
/*  49 */     if (value != null) {
/*  50 */       Iterator<?> it = value.iterator();
/*  51 */       if (it.hasNext()) {
/*  52 */         it.next();
/*  53 */         if (!it.hasNext()) {
/*  54 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  58 */     return false;
/*     */   }
/*     */   
/*     */   public final void serialize(Iterable<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  64 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(value))) {
/*  65 */       serializeContents(value, jgen, provider);
/*  66 */       return;
/*     */     }
/*  68 */     jgen.writeStartArray();
/*  69 */     serializeContents(value, jgen, provider);
/*  70 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContents(Iterable<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */   {
/*  77 */     Iterator<?> it = value.iterator();
/*  78 */     if (it.hasNext()) {
/*  79 */       TypeSerializer typeSer = this._valueTypeSerializer;
/*  80 */       JsonSerializer<Object> prevSerializer = null;
/*  81 */       Class<?> prevClass = null;
/*     */       do
/*     */       {
/*  84 */         Object elem = it.next();
/*  85 */         if (elem == null) {
/*  86 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/*  89 */           JsonSerializer<Object> currSerializer = this._elementSerializer;
/*  90 */           if (currSerializer == null)
/*     */           {
/*  92 */             Class<?> cc = elem.getClass();
/*  93 */             if (cc == prevClass) {
/*  94 */               currSerializer = prevSerializer;
/*     */             } else {
/*  96 */               currSerializer = provider.findValueSerializer(cc, this._property);
/*  97 */               prevSerializer = currSerializer;
/*  98 */               prevClass = cc;
/*     */             }
/*     */           }
/* 101 */           if (typeSer == null) {
/* 102 */             currSerializer.serialize(elem, jgen, provider);
/*     */           } else
/* 104 */             currSerializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/* 106 */       } while (it.hasNext());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\IterableSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */