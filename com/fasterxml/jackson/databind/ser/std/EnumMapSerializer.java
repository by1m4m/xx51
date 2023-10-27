/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.EnumValues;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.EnumMap;
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
/*     */ @JacksonStdImpl
/*     */ @Deprecated
/*     */ public class EnumMapSerializer
/*     */   extends ContainerSerializer<EnumMap<? extends Enum<?>, ?>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final BeanProperty _property;
/*     */   protected final EnumValues _keyEnums;
/*     */   protected final JavaType _valueType;
/*     */   protected final JsonSerializer<Object> _valueSerializer;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   
/*     */   public EnumMapSerializer(JavaType valueType, boolean staticTyping, EnumValues keyEnums, TypeSerializer vts, JsonSerializer<Object> valueSerializer)
/*     */   {
/*  73 */     super(EnumMap.class, false);
/*  74 */     this._property = null;
/*  75 */     this._staticTyping = ((staticTyping) || ((valueType != null) && (valueType.isFinal())));
/*  76 */     this._valueType = valueType;
/*  77 */     this._keyEnums = keyEnums;
/*  78 */     this._valueTypeSerializer = vts;
/*  79 */     this._valueSerializer = valueSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumMapSerializer(EnumMapSerializer src, BeanProperty property, JsonSerializer<?> ser)
/*     */   {
/*  89 */     super(src);
/*  90 */     this._property = property;
/*  91 */     this._staticTyping = src._staticTyping;
/*  92 */     this._valueType = src._valueType;
/*  93 */     this._keyEnums = src._keyEnums;
/*  94 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  95 */     this._valueSerializer = ser;
/*     */   }
/*     */   
/*     */   public EnumMapSerializer _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/* 100 */     return new EnumMapSerializer(this._valueType, this._staticTyping, this._keyEnums, vts, this._valueSerializer);
/*     */   }
/*     */   
/*     */   public EnumMapSerializer withValueSerializer(BeanProperty prop, JsonSerializer<?> ser) {
/* 104 */     if ((this._property == prop) && (ser == this._valueSerializer)) {
/* 105 */       return this;
/*     */     }
/* 107 */     return new EnumMapSerializer(this, prop, ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 119 */     JsonSerializer<?> ser = null;
/*     */     
/* 121 */     if (property != null) {
/* 122 */       AnnotatedMember m = property.getMember();
/* 123 */       if (m != null) {
/* 124 */         Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
/* 125 */         if (serDef != null) {
/* 126 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/* 130 */     if (ser == null) {
/* 131 */       ser = this._valueSerializer;
/*     */     }
/*     */     
/* 134 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 135 */     if (ser == null) {
/* 136 */       if (this._staticTyping) {
/* 137 */         return withValueSerializer(property, provider.findValueSerializer(this._valueType, property));
/*     */       }
/*     */     } else {
/* 140 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/* 142 */     if (ser != this._valueSerializer) {
/* 143 */       return withValueSerializer(property, ser);
/*     */     }
/* 145 */     return this;
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
/* 156 */     return this._valueType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 161 */     return this._valueSerializer;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, EnumMap<? extends Enum<?>, ?> value)
/*     */   {
/* 166 */     return (value == null) || (value.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(EnumMap<? extends Enum<?>, ?> value)
/*     */   {
/* 171 */     return value.size() == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 184 */     jgen.writeStartObject();
/* 185 */     if (!value.isEmpty()) {
/* 186 */       serializeContents(value, jgen, provider);
/*     */     }
/* 188 */     jgen.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 196 */     typeSer.writeTypePrefixForObject(value, jgen);
/* 197 */     if (!value.isEmpty()) {
/* 198 */       serializeContents(value, jgen, provider);
/*     */     }
/* 200 */     typeSer.writeTypeSuffixForObject(value, jgen);
/*     */   }
/*     */   
/*     */   protected void serializeContents(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 206 */     if (this._valueSerializer != null) {
/* 207 */       serializeContentsUsing(value, jgen, provider, this._valueSerializer);
/* 208 */       return;
/*     */     }
/* 210 */     JsonSerializer<Object> prevSerializer = null;
/* 211 */     Class<?> prevClass = null;
/* 212 */     EnumValues keyEnums = this._keyEnums;
/* 213 */     boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
/* 214 */     boolean useToString = provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
/* 215 */     TypeSerializer vts = this._valueTypeSerializer;
/*     */     
/* 217 */     for (Map.Entry<? extends Enum<?>, ?> entry : value.entrySet()) {
/* 218 */       Object valueElem = entry.getValue();
/* 219 */       if ((!skipNulls) || (valueElem != null))
/*     */       {
/*     */ 
/*     */ 
/* 223 */         Enum<?> key = (Enum)entry.getKey();
/* 224 */         if (useToString) {
/* 225 */           jgen.writeFieldName(key.toString());
/*     */         } else {
/* 227 */           if (keyEnums == null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 233 */             StdSerializer<?> ser = (StdSerializer)provider.findValueSerializer(key.getDeclaringClass(), this._property);
/*     */             
/* 235 */             keyEnums = ((EnumSerializer)ser).getEnumValues();
/*     */           }
/* 237 */           jgen.writeFieldName(keyEnums.serializedValueFor(key));
/*     */         }
/* 239 */         if (valueElem == null) {
/* 240 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/* 243 */           Class<?> cc = valueElem.getClass();
/*     */           JsonSerializer<Object> currSerializer;
/* 245 */           JsonSerializer<Object> currSerializer; if (cc == prevClass) {
/* 246 */             currSerializer = prevSerializer;
/*     */           } else {
/* 248 */             currSerializer = provider.findValueSerializer(cc, this._property);
/* 249 */             prevSerializer = currSerializer;
/* 250 */             prevClass = cc;
/*     */           }
/*     */           try {
/* 253 */             if (vts == null) {
/* 254 */               currSerializer.serialize(valueElem, jgen, provider);
/*     */             } else {
/* 256 */               currSerializer.serializeWithType(valueElem, jgen, provider, vts);
/*     */             }
/*     */           }
/*     */           catch (Exception e) {
/* 260 */             wrapAndThrow(provider, e, value, ((Enum)entry.getKey()).name());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void serializeContentsUsing(EnumMap<? extends Enum<?>, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> valueSer) throws IOException, JsonGenerationException
/*     */   {
/* 269 */     EnumValues keyEnums = this._keyEnums;
/* 270 */     boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
/* 271 */     boolean useToString = provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
/* 272 */     TypeSerializer vts = this._valueTypeSerializer;
/*     */     
/* 274 */     for (Map.Entry<? extends Enum<?>, ?> entry : value.entrySet()) {
/* 275 */       Object valueElem = entry.getValue();
/* 276 */       if ((!skipNulls) || (valueElem != null))
/*     */       {
/*     */ 
/* 279 */         Enum<?> key = (Enum)entry.getKey();
/* 280 */         if (useToString) {
/* 281 */           jgen.writeFieldName(key.toString());
/*     */         } else {
/* 283 */           if (keyEnums == null)
/*     */           {
/* 285 */             StdSerializer<?> ser = (StdSerializer)provider.findValueSerializer(key.getDeclaringClass(), this._property);
/*     */             
/* 287 */             keyEnums = ((EnumSerializer)ser).getEnumValues();
/*     */           }
/* 289 */           jgen.writeFieldName(keyEnums.serializedValueFor(key));
/*     */         }
/* 291 */         if (valueElem == null) {
/* 292 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/*     */           try
/*     */           {
/* 296 */             if (vts == null) {
/* 297 */               valueSer.serialize(valueElem, jgen, provider);
/*     */             } else {
/* 299 */               valueSer.serializeWithType(valueElem, jgen, provider, vts);
/*     */             }
/*     */           } catch (Exception e) {
/* 302 */             wrapAndThrow(provider, e, value, ((Enum)entry.getKey()).name());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 312 */     ObjectNode o = createSchemaNode("object", true);
/* 313 */     if ((typeHint instanceof ParameterizedType)) {
/* 314 */       Type[] typeArgs = ((ParameterizedType)typeHint).getActualTypeArguments();
/* 315 */       if (typeArgs.length == 2) {
/* 316 */         JavaType enumType = provider.constructType(typeArgs[0]);
/* 317 */         JavaType valueType = provider.constructType(typeArgs[1]);
/* 318 */         ObjectNode propsNode = JsonNodeFactory.instance.objectNode();
/* 319 */         Class<Enum<?>> enumClass = enumType.getRawClass();
/* 320 */         for (Enum<?> enumValue : (Enum[])enumClass.getEnumConstants()) {
/* 321 */           JsonSerializer<Object> ser = provider.findValueSerializer(valueType.getRawClass(), this._property);
/* 322 */           JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
/*     */           
/*     */ 
/* 325 */           propsNode.put(provider.getConfig().getAnnotationIntrospector().findEnumValue(enumValue), schemaNode);
/*     */         }
/* 327 */         o.put("properties", propsNode);
/*     */       }
/*     */     }
/* 330 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 342 */     if (visitor == null) {
/* 343 */       return;
/*     */     }
/* 345 */     JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
/* 346 */     if (objectVisitor == null) {
/* 347 */       return;
/*     */     }
/* 349 */     JavaType valueType = typeHint.containedType(1);
/* 350 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 351 */     if ((ser == null) && (valueType != null)) {
/* 352 */       ser = visitor.getProvider().findValueSerializer(valueType, this._property);
/*     */     }
/* 354 */     if (valueType == null) {
/* 355 */       valueType = visitor.getProvider().constructType(Object.class);
/*     */     }
/* 357 */     EnumValues keyEnums = this._keyEnums;
/* 358 */     if (keyEnums == null) {
/* 359 */       JavaType enumType = typeHint.containedType(0);
/* 360 */       if (enumType == null) {
/* 361 */         throw new IllegalStateException("Can not resolve Enum type of EnumMap: " + typeHint);
/*     */       }
/* 363 */       JsonSerializer<?> enumSer = visitor.getProvider().findValueSerializer(enumType, this._property);
/* 364 */       if (!(enumSer instanceof EnumSerializer)) {
/* 365 */         throw new IllegalStateException("Can not resolve Enum type of EnumMap: " + typeHint);
/*     */       }
/* 367 */       keyEnums = ((EnumSerializer)enumSer).getEnumValues();
/*     */     }
/* 369 */     for (Map.Entry<?, SerializableString> entry : keyEnums.internalMap().entrySet()) {
/* 370 */       String name = ((SerializableString)entry.getValue()).getValue();
/*     */       
/* 372 */       if (ser == null) {
/* 373 */         ser = visitor.getProvider().findValueSerializer(entry.getKey().getClass(), this._property);
/*     */       }
/* 375 */       objectVisitor.property(name, ser, valueType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\EnumMapSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */