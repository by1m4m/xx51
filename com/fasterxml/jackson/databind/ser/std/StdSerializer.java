/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
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
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class StdSerializer<T>
/*     */   extends JsonSerializer<T>
/*     */   implements JsonFormatVisitable, SchemaAware, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<T> _handledType;
/*     */   
/*     */   protected StdSerializer(Class<T> t)
/*     */   {
/*  46 */     this._handledType = t;
/*     */   }
/*     */   
/*     */   protected StdSerializer(JavaType type)
/*     */   {
/*  51 */     this._handledType = type.getRawClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdSerializer(Class<?> t, boolean dummy)
/*     */   {
/*  60 */     this._handledType = t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<T> handledType()
/*     */   {
/*  69 */     return this._handledType;
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
/*     */   public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException, JsonGenerationException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/*  95 */     return createSchemaNode("string");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
/*     */     throws JsonMappingException
/*     */   {
/* 106 */     ObjectNode schema = (ObjectNode)getSchema(provider, typeHint);
/* 107 */     if (!isOptional) {
/* 108 */       schema.put("required", !isOptional);
/*     */     }
/* 110 */     return schema;
/*     */   }
/*     */   
/*     */   protected ObjectNode createObjectNode() {
/* 114 */     return JsonNodeFactory.instance.objectNode();
/*     */   }
/*     */   
/*     */   protected ObjectNode createSchemaNode(String type)
/*     */   {
/* 119 */     ObjectNode schema = createObjectNode();
/* 120 */     schema.put("type", type);
/* 121 */     return schema;
/*     */   }
/*     */   
/*     */   protected ObjectNode createSchemaNode(String type, boolean isOptional)
/*     */   {
/* 126 */     ObjectNode schema = createSchemaNode(type);
/*     */     
/* 128 */     if (!isOptional) {
/* 129 */       schema.put("required", !isOptional);
/*     */     }
/* 131 */     return schema;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 142 */     visitor.expectAnyFormat(typeHint);
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
/*     */   public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, String fieldName)
/*     */     throws IOException
/*     */   {
/* 171 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 172 */       t = t.getCause();
/*     */     }
/*     */     
/* 175 */     if ((t instanceof Error)) {
/* 176 */       throw ((Error)t);
/*     */     }
/*     */     
/* 179 */     boolean wrap = (provider == null) || (provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS));
/* 180 */     if ((t instanceof IOException)) {
/* 181 */       if ((!wrap) || (!(t instanceof JsonMappingException))) {
/* 182 */         throw ((IOException)t);
/*     */       }
/* 184 */     } else if ((!wrap) && 
/* 185 */       ((t instanceof RuntimeException))) {
/* 186 */       throw ((RuntimeException)t);
/*     */     }
/*     */     
/*     */ 
/* 190 */     throw JsonMappingException.wrapWithPath(t, bean, fieldName);
/*     */   }
/*     */   
/*     */ 
/*     */   public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, int index)
/*     */     throws IOException
/*     */   {
/* 197 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 198 */       t = t.getCause();
/*     */     }
/*     */     
/* 201 */     if ((t instanceof Error)) {
/* 202 */       throw ((Error)t);
/*     */     }
/*     */     
/* 205 */     boolean wrap = (provider == null) || (provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS));
/* 206 */     if ((t instanceof IOException)) {
/* 207 */       if ((!wrap) || (!(t instanceof JsonMappingException))) {
/* 208 */         throw ((IOException)t);
/*     */       }
/* 210 */     } else if ((!wrap) && 
/* 211 */       ((t instanceof RuntimeException))) {
/* 212 */       throw ((RuntimeException)t);
/*     */     }
/*     */     
/*     */ 
/* 216 */     throw JsonMappingException.wrapWithPath(t, bean, index);
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
/*     */   protected boolean isDefaultSerializer(JsonSerializer<?> serializer)
/*     */   {
/* 232 */     return ClassUtil.isJacksonStdImpl(serializer);
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
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<?> findConvertingContentSerializer(SerializerProvider provider, BeanProperty prop, JsonSerializer<?> existingSerializer)
/*     */     throws JsonMappingException
/*     */   {
/* 253 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 254 */     if ((intr != null) && (prop != null)) {
/* 255 */       AnnotatedMember m = prop.getMember();
/* 256 */       if (m != null) {
/* 257 */         Object convDef = intr.findSerializationContentConverter(m);
/* 258 */         if (convDef != null) {
/* 259 */           Converter<Object, Object> conv = provider.converterInstance(prop.getMember(), convDef);
/* 260 */           JavaType delegateType = conv.getOutputType(provider.getTypeFactory());
/*     */           
/* 262 */           if ((existingSerializer == null) && (!delegateType.hasRawClass(Object.class))) {
/* 263 */             existingSerializer = provider.findValueSerializer(delegateType);
/*     */           }
/* 265 */           return new StdDelegatingSerializer(conv, delegateType, existingSerializer);
/*     */         }
/*     */       }
/*     */     }
/* 269 */     return existingSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyFilter findPropertyFilter(SerializerProvider provider, Object filterId, Object valueToFilter)
/*     */     throws JsonMappingException
/*     */   {
/* 282 */     FilterProvider filters = provider.getFilterProvider();
/*     */     
/* 284 */     if (filters == null) {
/* 285 */       throw new JsonMappingException("Can not resolve PropertyFilter with id '" + filterId + "'; no FilterProvider configured");
/*     */     }
/* 287 */     PropertyFilter filter = filters.findPropertyFilter(filterId, valueToFilter);
/*     */     
/* 289 */     return filter;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StdSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */