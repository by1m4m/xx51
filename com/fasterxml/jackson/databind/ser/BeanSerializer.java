/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
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
/*     */ public class BeanSerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = -4536893235025590367L;
/*     */   
/*     */   public BeanSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/*  45 */     super(type, builder, properties, filteredProperties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializer(BeanSerializerBase src)
/*     */   {
/*  54 */     super(src);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter)
/*     */   {
/*  59 */     super(src, objectIdWriter);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId)
/*     */   {
/*  64 */     super(src, objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, String[] toIgnore) {
/*  68 */     super(src, toIgnore);
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
/*     */   public static BeanSerializer createDummy(JavaType forType)
/*     */   {
/*  83 */     return new BeanSerializer(forType, null, NO_PROPS, null);
/*     */   }
/*     */   
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer unwrapper)
/*     */   {
/*  88 */     return new UnwrappingBeanSerializer(this, unwrapper);
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter)
/*     */   {
/*  93 */     return new BeanSerializer(this, objectIdWriter, this._propertyFilterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withFilterId(Object filterId)
/*     */   {
/*  98 */     return new BeanSerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withIgnorals(String[] toIgnore)
/*     */   {
/* 103 */     return new BeanSerializer(this, toIgnore);
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
/*     */   protected BeanSerializerBase asArraySerializer()
/*     */   {
/* 121 */     if ((this._objectIdWriter == null) && (this._anyGetterWriter == null) && (this._propertyFilterId == null))
/*     */     {
/*     */ 
/*     */ 
/* 125 */       return new BeanAsArraySerializer(this);
/*     */     }
/*     */     
/* 128 */     return this;
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 146 */     if (this._objectIdWriter != null) {
/* 147 */       _serializeWithObjectId(bean, gen, provider, true);
/* 148 */       return;
/*     */     }
/* 150 */     gen.writeStartObject();
/*     */     
/* 152 */     gen.setCurrentValue(bean);
/* 153 */     if (this._propertyFilterId != null) {
/* 154 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 156 */       serializeFields(bean, gen, provider);
/*     */     }
/* 158 */     gen.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     return "BeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\BeanSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */