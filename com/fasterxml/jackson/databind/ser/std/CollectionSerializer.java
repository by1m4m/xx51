/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public class CollectionSerializer
/*     */   extends AsArraySerializerBase<Collection<?>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer)
/*     */   {
/*  38 */     super(Collection.class, elemType, staticTyping, vts, property, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionSerializer(CollectionSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer)
/*     */   {
/*  44 */     super(src, property, vts, valueSerializer);
/*     */   }
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  49 */     return new CollectionSerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/*  55 */     return new CollectionSerializer(this, property, vts, elementSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, Collection<?> value)
/*     */   {
/*  66 */     return (value == null) || (value.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Collection<?> value)
/*     */   {
/*  71 */     Iterator<?> it = value.iterator();
/*  72 */     if (!it.hasNext()) {
/*  73 */       return false;
/*     */     }
/*  75 */     it.next();
/*  76 */     return !it.hasNext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(Collection<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  88 */     int len = value.size();
/*  89 */     if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/*  90 */       serializeContents(value, jgen, provider);
/*  91 */       return;
/*     */     }
/*  93 */     jgen.writeStartArray(len);
/*  94 */     serializeContents(value, jgen, provider);
/*  95 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */   public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 101 */     if (this._elementSerializer != null) {
/* 102 */       serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/* 103 */       return;
/*     */     }
/* 105 */     Iterator<?> it = value.iterator();
/* 106 */     if (!it.hasNext()) {
/* 107 */       return;
/*     */     }
/* 109 */     PropertySerializerMap serializers = this._dynamicSerializers;
/* 110 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     
/* 112 */     int i = 0;
/*     */     try {
/*     */       do {
/* 115 */         Object elem = it.next();
/* 116 */         if (elem == null) {
/* 117 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 119 */           Class<?> cc = elem.getClass();
/* 120 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 121 */           if (serializer == null)
/*     */           {
/* 123 */             if (this._elementType.hasGenericTypes()) {
/* 124 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
/*     */             }
/*     */             else {
/* 127 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 129 */             serializers = this._dynamicSerializers;
/*     */           }
/* 131 */           if (typeSer == null) {
/* 132 */             serializer.serialize(elem, jgen, provider);
/*     */           } else {
/* 134 */             serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */         }
/* 137 */         i++;
/* 138 */       } while (it.hasNext());
/*     */     }
/*     */     catch (Exception e) {
/* 141 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 149 */     Iterator<?> it = value.iterator();
/* 150 */     if (it.hasNext()) {
/* 151 */       TypeSerializer typeSer = this._valueTypeSerializer;
/* 152 */       int i = 0;
/*     */       do {
/* 154 */         Object elem = it.next();
/*     */         try {
/* 156 */           if (elem == null) {
/* 157 */             provider.defaultSerializeNull(jgen);
/*     */           }
/* 159 */           else if (typeSer == null) {
/* 160 */             ser.serialize(elem, jgen, provider);
/*     */           } else {
/* 162 */             ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */           
/* 165 */           i++;
/*     */         }
/*     */         catch (Exception e) {
/* 168 */           wrapAndThrow(provider, e, value, i);
/*     */         }
/* 170 */       } while (it.hasNext());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\CollectionSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */