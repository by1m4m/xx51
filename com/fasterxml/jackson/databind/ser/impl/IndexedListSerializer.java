/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class IndexedListSerializer
/*     */   extends AsArraySerializerBase<List<?>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public IndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer)
/*     */   {
/*  28 */     super(List.class, elemType, staticTyping, vts, property, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public IndexedListSerializer(IndexedListSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer)
/*     */   {
/*  34 */     super(src, property, vts, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public IndexedListSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/*  40 */     return new IndexedListSerializer(this, property, vts, elementSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, List<?> value)
/*     */   {
/*  51 */     return (value == null) || (value.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(List<?> value)
/*     */   {
/*  56 */     return value.size() == 1;
/*     */   }
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  61 */     return new IndexedListSerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
/*     */   }
/*     */   
/*     */   public final void serialize(List<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  67 */     int len = value.size();
/*  68 */     if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/*  69 */       serializeContents(value, jgen, provider);
/*  70 */       return;
/*     */     }
/*  72 */     jgen.writeStartArray(len);
/*  73 */     serializeContents(value, jgen, provider);
/*  74 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContents(List<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  81 */     if (this._elementSerializer != null) {
/*  82 */       serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/*  83 */       return;
/*     */     }
/*  85 */     if (this._valueTypeSerializer != null) {
/*  86 */       serializeTypedContents(value, jgen, provider);
/*  87 */       return;
/*     */     }
/*  89 */     int len = value.size();
/*  90 */     if (len == 0) {
/*  91 */       return;
/*     */     }
/*  93 */     int i = 0;
/*     */     try {
/*  95 */       PropertySerializerMap serializers = this._dynamicSerializers;
/*  96 */       for (; i < len; i++) {
/*  97 */         Object elem = value.get(i);
/*  98 */         if (elem == null) {
/*  99 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 101 */           Class<?> cc = elem.getClass();
/* 102 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 103 */           if (serializer == null)
/*     */           {
/* 105 */             if (this._elementType.hasGenericTypes()) {
/* 106 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
/*     */             }
/*     */             else {
/* 109 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 111 */             serializers = this._dynamicSerializers;
/*     */           }
/* 113 */           serializer.serialize(elem, jgen, provider);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 117 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContentsUsing(List<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 125 */     int len = value.size();
/* 126 */     if (len == 0) {
/* 127 */       return;
/*     */     }
/* 129 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 130 */     for (int i = 0; i < len; i++) {
/* 131 */       Object elem = value.get(i);
/*     */       try {
/* 133 */         if (elem == null) {
/* 134 */           provider.defaultSerializeNull(jgen);
/* 135 */         } else if (typeSer == null) {
/* 136 */           ser.serialize(elem, jgen, provider);
/*     */         } else {
/* 138 */           ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 142 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeTypedContents(List<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 150 */     int len = value.size();
/* 151 */     if (len == 0) {
/* 152 */       return;
/*     */     }
/* 154 */     int i = 0;
/*     */     try {
/* 156 */       TypeSerializer typeSer = this._valueTypeSerializer;
/* 157 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 158 */       for (; i < len; i++) {
/* 159 */         Object elem = value.get(i);
/* 160 */         if (elem == null) {
/* 161 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 163 */           Class<?> cc = elem.getClass();
/* 164 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 165 */           if (serializer == null)
/*     */           {
/* 167 */             if (this._elementType.hasGenericTypes()) {
/* 168 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
/*     */             }
/*     */             else {
/* 171 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 173 */             serializers = this._dynamicSerializers;
/*     */           }
/* 175 */           serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 180 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\IndexedListSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */