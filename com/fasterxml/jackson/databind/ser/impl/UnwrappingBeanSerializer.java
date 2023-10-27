/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
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
/*     */ public class UnwrappingBeanSerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final NameTransformer _nameTransformer;
/*     */   
/*     */   public UnwrappingBeanSerializer(BeanSerializerBase src, NameTransformer transformer)
/*     */   {
/*  35 */     super(src, transformer);
/*  36 */     this._nameTransformer = transformer;
/*     */   }
/*     */   
/*     */   public UnwrappingBeanSerializer(UnwrappingBeanSerializer src, ObjectIdWriter objectIdWriter)
/*     */   {
/*  41 */     super(src, objectIdWriter);
/*  42 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */   public UnwrappingBeanSerializer(UnwrappingBeanSerializer src, ObjectIdWriter objectIdWriter, Object filterId)
/*     */   {
/*  47 */     super(src, objectIdWriter, filterId);
/*  48 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */   protected UnwrappingBeanSerializer(UnwrappingBeanSerializer src, String[] toIgnore) {
/*  52 */     super(src, toIgnore);
/*  53 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer)
/*     */   {
/*  65 */     return new UnwrappingBeanSerializer(this, transformer);
/*     */   }
/*     */   
/*     */   public boolean isUnwrappingSerializer()
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter)
/*     */   {
/*  75 */     return new UnwrappingBeanSerializer(this, objectIdWriter);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withFilterId(Object filterId)
/*     */   {
/*  80 */     return new UnwrappingBeanSerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withIgnorals(String[] toIgnore)
/*     */   {
/*  85 */     return new UnwrappingBeanSerializer(this, toIgnore);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase asArraySerializer()
/*     */   {
/*  94 */     return this;
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
/*     */   public final void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 112 */     if (this._objectIdWriter != null) {
/* 113 */       _serializeWithObjectId(bean, jgen, provider, false);
/* 114 */       return;
/*     */     }
/* 116 */     if (this._propertyFilterId != null) {
/* 117 */       serializeFieldsFiltered(bean, jgen, provider);
/*     */     } else {
/* 119 */       serializeFields(bean, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 127 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS)) {
/* 128 */       throw new JsonMappingException("Unwrapped property requires use of type information: can not serialize without disabling `SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS`");
/*     */     }
/*     */     
/* 131 */     if (this._objectIdWriter != null) {
/* 132 */       _serializeWithObjectId(bean, jgen, provider, typeSer);
/* 133 */       return;
/*     */     }
/*     */     
/* 136 */     if (this._propertyFilterId != null) {
/* 137 */       serializeFieldsFiltered(bean, jgen, provider);
/*     */     } else {
/* 139 */       serializeFields(bean, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 150 */     return "UnwrappingBeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\UnwrappingBeanSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */