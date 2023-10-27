/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyWriter;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
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
/*     */ public class MapProperty
/*     */   extends PropertyWriter
/*     */ {
/*     */   protected final TypeSerializer _typeSerializer;
/*     */   protected final BeanProperty _property;
/*     */   protected Object _key;
/*     */   protected JsonSerializer<Object> _keySerializer;
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   
/*     */   @Deprecated
/*     */   public MapProperty(TypeSerializer typeSer)
/*     */   {
/*  38 */     this(typeSer, null);
/*     */   }
/*     */   
/*     */   public MapProperty(TypeSerializer typeSer, BeanProperty prop)
/*     */   {
/*  43 */     this._typeSerializer = typeSer;
/*  44 */     this._property = prop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void reset(Object key, Object value, JsonSerializer<Object> keySer, JsonSerializer<Object> valueSer)
/*     */   {
/*  56 */     reset(key, keySer, valueSer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset(Object key, JsonSerializer<Object> keySer, JsonSerializer<Object> valueSer)
/*     */   {
/*  66 */     this._key = key;
/*  67 */     this._keySerializer = keySer;
/*  68 */     this._valueSerializer = valueSer;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  73 */     if ((this._key instanceof String)) {
/*  74 */       return (String)this._key;
/*     */     }
/*  76 */     return String.valueOf(this._key);
/*     */   }
/*     */   
/*     */   public PropertyName getFullName()
/*     */   {
/*  81 */     return new PropertyName(getName());
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  86 */     return this._property == null ? null : this._property.getAnnotation(acls);
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */   {
/*  91 */     return this._property == null ? null : this._property.getContextAnnotation(acls);
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeAsField(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  98 */     this._keySerializer.serialize(this._key, jgen, provider);
/*  99 */     if (this._typeSerializer == null) {
/* 100 */       this._valueSerializer.serialize(value, jgen, provider);
/*     */     } else {
/* 102 */       this._valueSerializer.serializeWithType(value, jgen, provider, this._typeSerializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeAsOmittedField(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws Exception
/*     */   {
/* 110 */     if (!jgen.canOmitFields()) {
/* 111 */       jgen.writeOmittedField(getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeAsElement(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws Exception
/*     */   {
/* 119 */     if (this._typeSerializer == null) {
/* 120 */       this._valueSerializer.serialize(value, jgen, provider);
/*     */     } else {
/* 122 */       this._valueSerializer.serializeWithType(value, jgen, provider, this._typeSerializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeAsPlaceholder(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws Exception
/*     */   {
/* 130 */     jgen.writeNull();
/*     */   }
/*     */   
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor)
/*     */     throws JsonMappingException
/*     */   {}
/*     */   
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\MapProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */