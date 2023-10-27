/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
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
/*     */ public class StdDelegatingSerializer
/*     */   extends StdSerializer<Object>
/*     */   implements ContextualSerializer, ResolvableSerializer, JsonFormatVisitable, SchemaAware
/*     */ {
/*     */   protected final Converter<Object, ?> _converter;
/*     */   protected final JavaType _delegateType;
/*     */   protected final JsonSerializer<Object> _delegateSerializer;
/*     */   
/*     */   public StdDelegatingSerializer(Converter<?, ?> converter)
/*     */   {
/*  53 */     super(Object.class);
/*  54 */     this._converter = converter;
/*  55 */     this._delegateType = null;
/*  56 */     this._delegateSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> StdDelegatingSerializer(Class<T> cls, Converter<T, ?> converter)
/*     */   {
/*  62 */     super(cls, false);
/*  63 */     this._converter = converter;
/*  64 */     this._delegateType = null;
/*  65 */     this._delegateSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StdDelegatingSerializer(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer)
/*     */   {
/*  72 */     super(delegateType);
/*  73 */     this._converter = converter;
/*  74 */     this._delegateType = delegateType;
/*  75 */     this._delegateSerializer = delegateSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer)
/*     */   {
/*  85 */     if (getClass() != StdDelegatingSerializer.class) {
/*  86 */       throw new IllegalStateException("Sub-class " + getClass().getName() + " must override 'withDelegate'");
/*     */     }
/*  88 */     return new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 100 */     if ((this._delegateSerializer != null) && ((this._delegateSerializer instanceof ResolvableSerializer)))
/*     */     {
/* 102 */       ((ResolvableSerializer)this._delegateSerializer).resolve(provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 110 */     JsonSerializer<?> delSer = this._delegateSerializer;
/* 111 */     JavaType delegateType = this._delegateType;
/*     */     
/* 113 */     if (delSer == null)
/*     */     {
/* 115 */       if (delegateType == null) {
/* 116 */         delegateType = this._converter.getOutputType(provider.getTypeFactory());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 121 */       if (!delegateType.isJavaLangObject()) {
/* 122 */         delSer = provider.findValueSerializer(delegateType);
/*     */       }
/*     */     }
/* 125 */     if ((delSer instanceof ContextualSerializer)) {
/* 126 */       delSer = provider.handleSecondaryContextualization(delSer, property);
/*     */     }
/* 128 */     if ((delSer == this._delegateSerializer) && (delegateType == this._delegateType)) {
/* 129 */       return this;
/*     */     }
/* 131 */     return withDelegate(this._converter, delegateType, delSer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Converter<Object, ?> getConverter()
/*     */   {
/* 141 */     return this._converter;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getDelegatee()
/*     */   {
/* 146 */     return this._delegateSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 158 */     Object delegateValue = convertValue(value);
/*     */     
/* 160 */     if (delegateValue == null) {
/* 161 */       provider.defaultSerializeNull(gen);
/* 162 */       return;
/*     */     }
/*     */     
/* 165 */     JsonSerializer<Object> ser = this._delegateSerializer;
/* 166 */     if (ser == null) {
/* 167 */       ser = _findSerializer(delegateValue, provider);
/*     */     }
/* 169 */     ser.serialize(delegateValue, gen, provider);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 179 */     Object delegateValue = convertValue(value);
/* 180 */     JsonSerializer<Object> ser = this._delegateSerializer;
/* 181 */     if (ser == null) {
/* 182 */       ser = _findSerializer(value, provider);
/*     */     }
/* 184 */     ser.serializeWithType(delegateValue, gen, provider, typeSer);
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isEmpty(Object value)
/*     */   {
/* 191 */     Object delegateValue = convertValue(value);
/* 192 */     if (this._delegateSerializer == null) {
/* 193 */       return value == null;
/*     */     }
/* 195 */     return this._delegateSerializer.isEmpty(delegateValue);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, Object value)
/*     */   {
/* 201 */     Object delegateValue = convertValue(value);
/* 202 */     if (this._delegateSerializer == null) {
/* 203 */       return value == null;
/*     */     }
/* 205 */     return this._delegateSerializer.isEmpty(prov, delegateValue);
/*     */   }
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
/* 218 */     if ((this._delegateSerializer instanceof SchemaAware)) {
/* 219 */       return ((SchemaAware)this._delegateSerializer).getSchema(provider, typeHint);
/*     */     }
/* 221 */     return super.getSchema(provider, typeHint);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
/*     */     throws JsonMappingException
/*     */   {
/* 228 */     if ((this._delegateSerializer instanceof SchemaAware)) {
/* 229 */       return ((SchemaAware)this._delegateSerializer).getSchema(provider, typeHint, isOptional);
/*     */     }
/* 231 */     return super.getSchema(provider, typeHint);
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
/* 242 */     if (this._delegateSerializer != null) {
/* 243 */       this._delegateSerializer.acceptJsonFormatVisitor(visitor, typeHint);
/*     */     }
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
/*     */   protected Object convertValue(Object value)
/*     */   {
/* 265 */     return this._converter.convert(value);
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
/*     */   protected JsonSerializer<Object> _findSerializer(Object value, SerializerProvider serializers)
/*     */     throws JsonMappingException
/*     */   {
/* 280 */     return serializers.findValueSerializer(value.getClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\StdDelegatingSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */