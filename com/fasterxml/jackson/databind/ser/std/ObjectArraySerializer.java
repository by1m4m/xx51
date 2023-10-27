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
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
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
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ @JacksonStdImpl
/*     */ public class ObjectArraySerializer
/*     */   extends ArraySerializerBase<Object[]>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final JavaType _elementType;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected JsonSerializer<Object> _elementSerializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */   public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> elementSerializer)
/*     */   {
/*  67 */     super(Object[].class, null);
/*  68 */     this._elementType = elemType;
/*  69 */     this._staticTyping = staticTyping;
/*  70 */     this._valueTypeSerializer = vts;
/*  71 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*  72 */     this._elementSerializer = elementSerializer;
/*     */   }
/*     */   
/*     */   public ObjectArraySerializer(ObjectArraySerializer src, TypeSerializer vts)
/*     */   {
/*  77 */     super(src);
/*  78 */     this._elementType = src._elementType;
/*  79 */     this._valueTypeSerializer = vts;
/*  80 */     this._staticTyping = src._staticTyping;
/*  81 */     this._dynamicSerializers = src._dynamicSerializers;
/*  82 */     this._elementSerializer = src._elementSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectArraySerializer(ObjectArraySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/*  89 */     super(src, property);
/*  90 */     this._elementType = src._elementType;
/*  91 */     this._valueTypeSerializer = vts;
/*  92 */     this._staticTyping = src._staticTyping;
/*  93 */     this._dynamicSerializers = src._dynamicSerializers;
/*  94 */     this._elementSerializer = elementSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/* 100 */     return new ObjectArraySerializer(this._elementType, this._staticTyping, vts, this._elementSerializer);
/*     */   }
/*     */   
/*     */   public ObjectArraySerializer withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> ser)
/*     */   {
/* 105 */     if ((this._property == prop) && (ser == this._elementSerializer) && (this._valueTypeSerializer == vts)) {
/* 106 */       return this;
/*     */     }
/* 108 */     return new ObjectArraySerializer(this, prop, vts, ser);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 122 */     TypeSerializer vts = this._valueTypeSerializer;
/* 123 */     if (vts != null) {
/* 124 */       vts = vts.forProperty(property);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 130 */     JsonSerializer<?> ser = null;
/*     */     
/* 132 */     if (property != null) {
/* 133 */       AnnotatedMember m = property.getMember();
/* 134 */       if (m != null) {
/* 135 */         Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
/* 136 */         if (serDef != null) {
/* 137 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/* 141 */     if (ser == null) {
/* 142 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 145 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 146 */     if (ser == null)
/*     */     {
/*     */ 
/* 149 */       if ((this._elementType != null) && (
/* 150 */         (this._staticTyping) || (hasContentTypeAnnotation(provider, property)))) {
/* 151 */         ser = provider.findValueSerializer(this._elementType, property);
/*     */       }
/*     */     }
/*     */     else {
/* 155 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/* 157 */     return withResolved(property, vts, ser);
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
/* 168 */     return this._elementType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 173 */     return this._elementSerializer;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Object[] value)
/*     */   {
/* 178 */     return (value == null) || (value.length == 0);
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Object[] value)
/*     */   {
/* 183 */     return value.length == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(Object[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 195 */     int len = value.length;
/* 196 */     if ((len == 1) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) {
/* 197 */       serializeContents(value, jgen, provider);
/* 198 */       return;
/*     */     }
/* 200 */     jgen.writeStartArray(len);
/* 201 */     serializeContents(value, jgen, provider);
/* 202 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */   public void serializeContents(Object[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 208 */     int len = value.length;
/* 209 */     if (len == 0) {
/* 210 */       return;
/*     */     }
/* 212 */     if (this._elementSerializer != null) {
/* 213 */       serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/* 214 */       return;
/*     */     }
/* 216 */     if (this._valueTypeSerializer != null) {
/* 217 */       serializeTypedContents(value, jgen, provider);
/* 218 */       return;
/*     */     }
/* 220 */     int i = 0;
/* 221 */     Object elem = null;
/*     */     try {
/* 223 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 224 */       for (; i < len; i++) {
/* 225 */         elem = value[i];
/* 226 */         if (elem == null) {
/* 227 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/* 230 */           Class<?> cc = elem.getClass();
/* 231 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 232 */           if (serializer == null)
/*     */           {
/* 234 */             if (this._elementType.hasGenericTypes()) {
/* 235 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
/*     */             }
/*     */             else {
/* 238 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/*     */           }
/* 241 */           serializer.serialize(elem, jgen, provider);
/*     */         }
/*     */       }
/* 244 */     } catch (IOException ioe) { throw ioe;
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 251 */       Throwable t = e;
/* 252 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 253 */         t = t.getCause();
/*     */       }
/* 255 */       if ((t instanceof Error)) {
/* 256 */         throw ((Error)t);
/*     */       }
/* 258 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeContentsUsing(Object[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 265 */     int len = value.length;
/* 266 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     
/* 268 */     int i = 0;
/* 269 */     Object elem = null;
/*     */     try {
/* 271 */       for (; i < len; i++) {
/* 272 */         elem = value[i];
/* 273 */         if (elem == null) {
/* 274 */           provider.defaultSerializeNull(jgen);
/*     */ 
/*     */         }
/* 277 */         else if (typeSer == null) {
/* 278 */           ser.serialize(elem, jgen, provider);
/*     */         } else {
/* 280 */           ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/*     */     } catch (IOException ioe) {
/* 284 */       throw ioe;
/*     */     } catch (Exception e) {
/* 286 */       Throwable t = e;
/* 287 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 288 */         t = t.getCause();
/*     */       }
/* 290 */       if ((t instanceof Error)) {
/* 291 */         throw ((Error)t);
/*     */       }
/* 293 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeTypedContents(Object[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */   {
/* 299 */     int len = value.length;
/* 300 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 301 */     int i = 0;
/* 302 */     Object elem = null;
/*     */     try {
/* 304 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 305 */       for (; i < len; i++) {
/* 306 */         elem = value[i];
/* 307 */         if (elem == null) {
/* 308 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/* 311 */           Class<?> cc = elem.getClass();
/* 312 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 313 */           if (serializer == null) {
/* 314 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 316 */           serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/* 319 */     } catch (IOException ioe) { throw ioe;
/*     */     } catch (Exception e) {
/* 321 */       Throwable t = e;
/* 322 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 323 */         t = t.getCause();
/*     */       }
/* 325 */       if ((t instanceof Error)) {
/* 326 */         throw ((Error)t);
/*     */       }
/* 328 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 337 */     ObjectNode o = createSchemaNode("array", true);
/* 338 */     if (typeHint != null) {
/* 339 */       JavaType javaType = provider.constructType(typeHint);
/* 340 */       if (javaType.isArrayType()) {
/* 341 */         Class<?> componentType = ((ArrayType)javaType).getContentType().getRawClass();
/*     */         
/* 343 */         if (componentType == Object.class) {
/* 344 */           o.put("items", JsonSchema.getDefaultSchemaNode());
/*     */         } else {
/* 346 */           JsonSerializer<Object> ser = provider.findValueSerializer(componentType, this._property);
/* 347 */           JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
/*     */           
/*     */ 
/* 350 */           o.put("items", schemaNode);
/*     */         }
/*     */       }
/*     */     }
/* 354 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 361 */     JsonArrayFormatVisitor arrayVisitor = visitor.expectArrayFormat(typeHint);
/* 362 */     if (arrayVisitor != null) {
/* 363 */       TypeFactory tf = visitor.getProvider().getTypeFactory();
/* 364 */       JavaType contentType = tf.moreSpecificType(this._elementType, typeHint.getContentType());
/* 365 */       if (contentType == null) {
/* 366 */         throw new JsonMappingException("Could not resolve type");
/*     */       }
/* 368 */       JsonSerializer<?> valueSer = this._elementSerializer;
/* 369 */       if (valueSer == null) {
/* 370 */         valueSer = visitor.getProvider().findValueSerializer(contentType, this._property);
/*     */       }
/* 372 */       arrayVisitor.itemsFormat(valueSer, contentType);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 379 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 381 */     if (map != result.map) {
/* 382 */       this._dynamicSerializers = result.map;
/*     */     }
/* 384 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 390 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 392 */     if (map != result.map) {
/* 393 */       this._dynamicSerializers = result.map;
/*     */     }
/* 395 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\ObjectArraySerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */