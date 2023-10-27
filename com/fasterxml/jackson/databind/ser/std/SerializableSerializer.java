/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public class SerializableSerializer
/*     */   extends StdSerializer<JsonSerializable>
/*     */ {
/*  32 */   public static final SerializableSerializer instance = new SerializableSerializer();
/*     */   
/*     */ 
/*  35 */   private static final AtomicReference<ObjectMapper> _mapperReference = new AtomicReference();
/*     */   
/*  37 */   protected SerializableSerializer() { super(JsonSerializable.class); }
/*     */   
/*     */   public void serialize(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */   {
/*  41 */     value.serialize(jgen, provider);
/*     */   }
/*     */   
/*     */   public final void serializeWithType(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/*  47 */     value.serializeWithType(jgen, provider, typeSer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/*  55 */     ObjectNode objectNode = createObjectNode();
/*  56 */     String schemaType = "any";
/*  57 */     String objectProperties = null;
/*  58 */     String itemDefinition = null;
/*  59 */     if (typeHint != null) {
/*  60 */       Class<?> rawClass = TypeFactory.rawClass(typeHint);
/*  61 */       if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
/*  62 */         JsonSerializableSchema schemaInfo = (JsonSerializableSchema)rawClass.getAnnotation(JsonSerializableSchema.class);
/*  63 */         schemaType = schemaInfo.schemaType();
/*  64 */         if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
/*  65 */           objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
/*     */         }
/*  67 */         if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
/*  68 */           itemDefinition = schemaInfo.schemaItemDefinition();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  75 */     objectNode.put("type", schemaType);
/*  76 */     if (objectProperties != null) {
/*     */       try {
/*  78 */         objectNode.put("properties", _getObjectMapper().readTree(objectProperties));
/*     */       } catch (IOException e) {
/*  80 */         throw new JsonMappingException("Failed to parse @JsonSerializableSchema.schemaObjectPropertiesDefinition value");
/*     */       }
/*     */     }
/*  83 */     if (itemDefinition != null) {
/*     */       try {
/*  85 */         objectNode.put("items", _getObjectMapper().readTree(itemDefinition));
/*     */       } catch (IOException e) {
/*  87 */         throw new JsonMappingException("Failed to parse @JsonSerializableSchema.schemaItemDefinition value");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  92 */     return objectNode;
/*     */   }
/*     */   
/*     */   private static final synchronized ObjectMapper _getObjectMapper()
/*     */   {
/*  97 */     ObjectMapper mapper = (ObjectMapper)_mapperReference.get();
/*  98 */     if (mapper == null) {
/*  99 */       mapper = new ObjectMapper();
/* 100 */       _mapperReference.set(mapper);
/*     */     }
/* 102 */     return mapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 109 */     visitor.expectAnyFormat(typeHint);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\SerializableSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */