/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
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
/*     */ public abstract class AsArraySerializerBase<T>
/*     */   extends ContainerSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final JavaType _elementType;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected final JsonSerializer<Object> _elementSerializer;
/*     */   protected final BeanProperty _property;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */   protected AsArraySerializerBase(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> elementSerializer)
/*     */   {
/*  63 */     super(cls, false);
/*  64 */     this._elementType = et;
/*     */     
/*  66 */     this._staticTyping = ((staticTyping) || ((et != null) && (et.isFinal())));
/*  67 */     this._valueTypeSerializer = vts;
/*  68 */     this._property = property;
/*  69 */     this._elementSerializer = elementSerializer;
/*  70 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected AsArraySerializerBase(AsArraySerializerBase<?> src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/*  77 */     super(src);
/*  78 */     this._elementType = src._elementType;
/*  79 */     this._staticTyping = src._staticTyping;
/*  80 */     this._valueTypeSerializer = vts;
/*  81 */     this._property = property;
/*  82 */     this._elementSerializer = elementSerializer;
/*  83 */     this._dynamicSerializers = src._dynamicSerializers;
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
/*     */   public abstract AsArraySerializerBase<T> withResolved(BeanProperty paramBeanProperty, TypeSerializer paramTypeSerializer, JsonSerializer<?> paramJsonSerializer);
/*     */   
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
/* 106 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 107 */     if (typeSer != null) {
/* 108 */       typeSer = typeSer.forProperty(property);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 114 */     JsonSerializer<?> ser = null;
/*     */     
/* 116 */     if (property != null) {
/* 117 */       AnnotatedMember m = property.getMember();
/* 118 */       if (m != null) {
/* 119 */         Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
/* 120 */         if (serDef != null) {
/* 121 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/* 125 */     if (ser == null) {
/* 126 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 129 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 130 */     if (ser == null)
/*     */     {
/*     */ 
/* 133 */       if (this._elementType != null)
/*     */       {
/* 135 */         if (((this._staticTyping) && (this._elementType.getRawClass() != Object.class)) || (hasContentTypeAnnotation(provider, property)))
/*     */         {
/* 137 */           ser = provider.findValueSerializer(this._elementType, property);
/*     */         }
/*     */       }
/*     */     } else {
/* 141 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/* 143 */     if ((ser != this._elementSerializer) || (property != this._property) || (this._valueTypeSerializer != typeSer)) {
/* 144 */       return withResolved(property, typeSer, ser);
/*     */     }
/* 146 */     return this;
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
/* 157 */     return this._elementType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 162 */     return this._elementSerializer;
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
/*     */   public void serialize(T value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 177 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(value)))
/*     */     {
/* 179 */       serializeContents(value, gen, provider);
/* 180 */       return;
/*     */     }
/* 182 */     gen.writeStartArray();
/*     */     
/* 184 */     gen.setCurrentValue(value);
/* 185 */     serializeContents(value, gen, provider);
/* 186 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(T value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 194 */     typeSer.writeTypePrefixForArray(value, gen);
/*     */     
/* 196 */     gen.setCurrentValue(value);
/* 197 */     serializeContents(value, gen, provider);
/* 198 */     typeSer.writeTypeSuffixForArray(value, gen);
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 209 */     ObjectNode o = createSchemaNode("array", true);
/* 210 */     JavaType contentType = this._elementType;
/* 211 */     if (contentType != null) {
/* 212 */       JsonNode schemaNode = null;
/*     */       
/* 214 */       if (contentType.getRawClass() != Object.class) {
/* 215 */         JsonSerializer<Object> ser = provider.findValueSerializer(contentType, this._property);
/* 216 */         if ((ser instanceof SchemaAware)) {
/* 217 */           schemaNode = ((SchemaAware)ser).getSchema(provider, null);
/*     */         }
/*     */       }
/* 220 */       if (schemaNode == null) {
/* 221 */         schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */       }
/* 223 */       o.put("items", schemaNode);
/*     */     }
/* 225 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 232 */     JsonArrayFormatVisitor arrayVisitor = visitor == null ? null : visitor.expectArrayFormat(typeHint);
/* 233 */     if (arrayVisitor != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 239 */       JsonSerializer<?> valueSer = this._elementSerializer;
/* 240 */       if (valueSer == null) {
/* 241 */         valueSer = visitor.getProvider().findValueSerializer(this._elementType, this._property);
/*     */       }
/* 243 */       arrayVisitor.itemsFormat(valueSer, this._elementType);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 250 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 252 */     if (map != result.map) {
/* 253 */       this._dynamicSerializers = result.map;
/*     */     }
/* 255 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 261 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 262 */     if (map != result.map) {
/* 263 */       this._dynamicSerializers = result.map;
/*     */     }
/* 265 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\AsArraySerializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */