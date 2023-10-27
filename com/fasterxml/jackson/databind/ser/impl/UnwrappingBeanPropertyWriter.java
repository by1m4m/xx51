/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper.Base;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.util.Iterator;
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
/*     */ public class UnwrappingBeanPropertyWriter
/*     */   extends BeanPropertyWriter
/*     */ {
/*     */   protected final NameTransformer _nameTransformer;
/*     */   
/*     */   public UnwrappingBeanPropertyWriter(BeanPropertyWriter base, NameTransformer unwrapper)
/*     */   {
/*  40 */     super(base);
/*  41 */     this._nameTransformer = unwrapper;
/*     */   }
/*     */   
/*     */   private UnwrappingBeanPropertyWriter(UnwrappingBeanPropertyWriter base, NameTransformer transformer, SerializedString name)
/*     */   {
/*  46 */     super(base, name);
/*  47 */     this._nameTransformer = transformer;
/*     */   }
/*     */   
/*     */ 
/*     */   public UnwrappingBeanPropertyWriter rename(NameTransformer transformer)
/*     */   {
/*  53 */     String oldName = this._name.getValue();
/*  54 */     String newName = transformer.transform(oldName);
/*     */     
/*     */ 
/*  57 */     transformer = NameTransformer.chainedTransformer(transformer, this._nameTransformer);
/*     */     
/*  59 */     return new UnwrappingBeanPropertyWriter(this, transformer, new SerializedString(newName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUnwrapping()
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/*  77 */     Object value = get(bean);
/*  78 */     if (value == null)
/*     */     {
/*     */ 
/*  81 */       return;
/*     */     }
/*  83 */     JsonSerializer<Object> ser = this._serializer;
/*  84 */     if (ser == null) {
/*  85 */       Class<?> cls = value.getClass();
/*  86 */       PropertySerializerMap map = this._dynamicSerializers;
/*  87 */       ser = map.serializerFor(cls);
/*  88 */       if (ser == null) {
/*  89 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/*  92 */     if (this._suppressableValue != null) {
/*  93 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/*  94 */         if (!ser.isEmpty(prov, value)) {}
/*     */ 
/*     */       }
/*  97 */       else if (this._suppressableValue.equals(value)) {
/*  98 */         return;
/*     */       }
/*     */     }
/*     */     
/* 102 */     if ((value == bean) && 
/* 103 */       (_handleSelfReference(bean, jgen, prov, ser))) {
/* 104 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 109 */     if (!ser.isUnwrappingSerializer()) {
/* 110 */       jgen.writeFieldName(this._name);
/*     */     }
/*     */     
/* 113 */     if (this._typeSerializer == null) {
/* 114 */       ser.serialize(value, jgen, prov);
/*     */     } else {
/* 116 */       ser.serializeWithType(value, jgen, prov, this._typeSerializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void assignSerializer(JsonSerializer<Object> ser)
/*     */   {
/* 124 */     super.assignSerializer(ser);
/* 125 */     if (this._serializer != null) {
/* 126 */       NameTransformer t = this._nameTransformer;
/* 127 */       if (this._serializer.isUnwrappingSerializer()) {
/* 128 */         t = NameTransformer.chainedTransformer(t, ((UnwrappingBeanSerializer)this._serializer)._nameTransformer);
/*     */       }
/* 130 */       this._serializer = this._serializer.unwrappingSerializer(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void depositSchemaProperty(final JsonObjectFormatVisitor visitor)
/*     */     throws JsonMappingException
/*     */   {
/* 143 */     SerializerProvider provider = visitor.getProvider();
/* 144 */     JsonSerializer<Object> ser = provider.findValueSerializer(getType(), this).unwrappingSerializer(this._nameTransformer);
/*     */     
/*     */ 
/*     */ 
/* 148 */     if (ser.isUnwrappingSerializer()) {
/* 149 */       ser.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper.Base(provider)
/*     */       {
/*     */ 
/*     */ 
/*     */         public JsonObjectFormatVisitor expectObjectFormat(JavaType type)
/*     */           throws JsonMappingException {
/* 155 */           return visitor; } }, getType());
/*     */     }
/*     */     else
/*     */     {
/* 159 */       super.depositSchemaProperty(visitor);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void _depositSchemaProperty(ObjectNode propertiesNode, JsonNode schemaNode)
/*     */   {
/* 167 */     JsonNode props = schemaNode.get("properties");
/* 168 */     if (props != null) {
/* 169 */       Iterator<Map.Entry<String, JsonNode>> it = props.fields();
/* 170 */       while (it.hasNext()) {
/* 171 */         Map.Entry<String, JsonNode> entry = (Map.Entry)it.next();
/* 172 */         String name = (String)entry.getKey();
/* 173 */         if (this._nameTransformer != null) {
/* 174 */           name = this._nameTransformer.transform(name);
/*     */         }
/* 176 */         propertiesNode.set(name, (JsonNode)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonSerializer<Object> serializer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 193 */     if (this._nonTrivialBaseType != null) {
/* 194 */       JavaType subtype = provider.constructSpecializedType(this._nonTrivialBaseType, type);
/* 195 */       serializer = provider.findValueSerializer(subtype, this);
/*     */     } else {
/* 197 */       serializer = provider.findValueSerializer(type, this);
/*     */     }
/* 199 */     NameTransformer t = this._nameTransformer;
/* 200 */     if (serializer.isUnwrappingSerializer()) {
/* 201 */       t = NameTransformer.chainedTransformer(t, ((UnwrappingBeanSerializer)serializer)._nameTransformer);
/*     */     }
/* 203 */     JsonSerializer<Object> serializer = serializer.unwrappingSerializer(t);
/*     */     
/* 205 */     this._dynamicSerializers = this._dynamicSerializers.newWith(type, serializer);
/* 206 */     return serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\UnwrappingBeanPropertyWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */