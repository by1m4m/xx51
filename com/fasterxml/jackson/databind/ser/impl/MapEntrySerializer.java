/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapEntrySerializer
/*     */   extends ContainerSerializer<Map.Entry<?, ?>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _valueTypeIsStatic;
/*     */   protected final JavaType _entryType;
/*     */   protected final JavaType _keyType;
/*     */   protected final JavaType _valueType;
/*     */   protected JsonSerializer<Object> _keySerializer;
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected PropertySerializerMap _dynamicValueSerializers;
/*     */   
/*     */   public MapEntrySerializer(JavaType type, JavaType keyType, JavaType valueType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  69 */     super(type);
/*  70 */     this._entryType = type;
/*  71 */     this._keyType = keyType;
/*  72 */     this._valueType = valueType;
/*  73 */     this._valueTypeIsStatic = staticTyping;
/*  74 */     this._valueTypeSerializer = vts;
/*  75 */     this._property = property;
/*  76 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapEntrySerializer(MapEntrySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> keySer, JsonSerializer<?> valueSer)
/*     */   {
/*  84 */     super(Map.class, false);
/*  85 */     this._entryType = src._entryType;
/*  86 */     this._keyType = src._keyType;
/*  87 */     this._valueType = src._valueType;
/*  88 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  89 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  90 */     this._keySerializer = keySer;
/*  91 */     this._valueSerializer = valueSer;
/*  92 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/*  93 */     this._property = src._property;
/*     */   }
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  98 */     return new MapEntrySerializer(this, this._property, vts, this._keySerializer, this._valueSerializer);
/*     */   }
/*     */   
/*     */   public MapEntrySerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer)
/*     */   {
/* 103 */     return new MapEntrySerializer(this, property, this._valueTypeSerializer, keySerializer, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 110 */     JsonSerializer<?> ser = null;
/* 111 */     JsonSerializer<?> keySer = null;
/* 112 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 113 */     AnnotatedMember propertyAcc = property == null ? null : property.getMember();
/*     */     
/*     */ 
/* 116 */     if ((propertyAcc != null) && (intr != null)) {
/* 117 */       Object serDef = intr.findKeySerializer(propertyAcc);
/* 118 */       if (serDef != null) {
/* 119 */         keySer = provider.serializerInstance(propertyAcc, serDef);
/*     */       }
/* 121 */       serDef = intr.findContentSerializer(propertyAcc);
/* 122 */       if (serDef != null) {
/* 123 */         ser = provider.serializerInstance(propertyAcc, serDef);
/*     */       }
/*     */     }
/* 126 */     if (ser == null) {
/* 127 */       ser = this._valueSerializer;
/*     */     }
/*     */     
/* 130 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 131 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/* 135 */       if (((this._valueTypeIsStatic) && (this._valueType.getRawClass() != Object.class)) || (hasContentTypeAnnotation(provider, property)))
/*     */       {
/* 137 */         ser = provider.findValueSerializer(this._valueType, property);
/*     */       }
/*     */     } else {
/* 140 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/* 142 */     if (keySer == null) {
/* 143 */       keySer = this._keySerializer;
/*     */     }
/* 145 */     if (keySer == null) {
/* 146 */       keySer = provider.findKeySerializer(this._keyType, property);
/*     */     } else {
/* 148 */       keySer = provider.handleSecondaryContextualization(keySer, property);
/*     */     }
/* 150 */     MapEntrySerializer mser = withResolved(property, keySer, ser);
/*     */     
/* 152 */     return mser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 163 */     return this._valueType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 168 */     return this._valueSerializer;
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Map.Entry<?, ?> value)
/*     */   {
/* 173 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Map.Entry<?, ?> value)
/*     */   {
/* 178 */     return value == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Map.Entry<?, ?> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 191 */     gen.writeStartObject();
/*     */     
/* 193 */     gen.setCurrentValue(value);
/* 194 */     if (this._valueSerializer != null) {
/* 195 */       serializeUsing(value, gen, provider, this._valueSerializer);
/*     */     } else {
/* 197 */       serializeDynamic(value, gen, provider);
/*     */     }
/* 199 */     gen.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(Map.Entry<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 206 */     typeSer.writeTypePrefixForObject(value, gen);
/*     */     
/* 208 */     gen.setCurrentValue(value);
/* 209 */     if (this._valueSerializer != null) {
/* 210 */       serializeUsing(value, gen, provider, this._valueSerializer);
/*     */     } else {
/* 212 */       serializeDynamic(value, gen, provider);
/*     */     }
/* 214 */     typeSer.writeTypeSuffixForObject(value, gen);
/*     */   }
/*     */   
/*     */   protected void serializeDynamic(Map.Entry<?, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 220 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/* 221 */     boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
/* 222 */     TypeSerializer vts = this._valueTypeSerializer;
/*     */     
/* 224 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*     */     
/* 226 */     Object valueElem = value.getValue();
/* 227 */     Object keyElem = value.getKey();
/* 228 */     if (keyElem == null) {
/* 229 */       provider.findNullKeySerializer(this._keyType, this._property).serialize(null, jgen, provider);
/*     */     }
/*     */     else {
/* 232 */       if ((skipNulls) && (valueElem == null)) return;
/* 233 */       keySerializer.serialize(keyElem, jgen, provider);
/*     */     }
/*     */     
/* 236 */     if (valueElem == null) {
/* 237 */       provider.defaultSerializeNull(jgen);
/*     */     } else {
/* 239 */       Class<?> cc = valueElem.getClass();
/* 240 */       JsonSerializer<Object> ser = serializers.serializerFor(cc);
/* 241 */       if (ser == null) {
/* 242 */         if (this._valueType.hasGenericTypes()) {
/* 243 */           ser = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*     */         }
/*     */         else {
/* 246 */           ser = _findAndAddDynamic(serializers, cc, provider);
/*     */         }
/* 248 */         serializers = this._dynamicValueSerializers;
/*     */       }
/*     */       try {
/* 251 */         if (vts == null) {
/* 252 */           ser.serialize(valueElem, jgen, provider);
/*     */         } else {
/* 254 */           ser.serializeWithType(valueElem, jgen, provider, vts);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 258 */         String keyDesc = "" + keyElem;
/* 259 */         wrapAndThrow(provider, e, value, keyDesc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void serializeUsing(Map.Entry<?, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 273 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/* 274 */     TypeSerializer vts = this._valueTypeSerializer;
/* 275 */     boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
/*     */     
/* 277 */     Object valueElem = value.getValue();
/* 278 */     Object keyElem = value.getKey();
/* 279 */     if (keyElem == null) {
/* 280 */       provider.findNullKeySerializer(this._keyType, this._property).serialize(null, jgen, provider);
/*     */     }
/*     */     else {
/* 283 */       if ((skipNulls) && (valueElem == null)) return;
/* 284 */       keySerializer.serialize(keyElem, jgen, provider);
/*     */     }
/* 286 */     if (valueElem == null) {
/* 287 */       provider.defaultSerializeNull(jgen);
/*     */     } else {
/*     */       try {
/* 290 */         if (vts == null) {
/* 291 */           ser.serialize(valueElem, jgen, provider);
/*     */         } else {
/* 293 */           ser.serializeWithType(valueElem, jgen, provider, vts);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 297 */         String keyDesc = "" + keyElem;
/* 298 */         wrapAndThrow(provider, e, value, keyDesc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 312 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 313 */     if (map != result.map) {
/* 314 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 316 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 322 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 323 */     if (map != result.map) {
/* 324 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 326 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\MapEntrySerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */