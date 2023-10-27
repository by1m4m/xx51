/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException.Reference;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
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
/*     */ public class BeanAsArraySerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = -893701886577615846L;
/*     */   protected final BeanSerializerBase _defaultSerializer;
/*     */   
/*     */   public BeanAsArraySerializer(BeanSerializerBase src)
/*     */   {
/*  61 */     super(src, (ObjectIdWriter)null);
/*  62 */     this._defaultSerializer = src;
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer(BeanSerializerBase src, String[] toIgnore) {
/*  66 */     super(src, toIgnore);
/*  67 */     this._defaultSerializer = src;
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer(BeanSerializerBase src, ObjectIdWriter oiw, Object filterId)
/*     */   {
/*  72 */     super(src, oiw, filterId);
/*  73 */     this._defaultSerializer = src;
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
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer)
/*     */   {
/*  87 */     return this._defaultSerializer.unwrappingSerializer(transformer);
/*     */   }
/*     */   
/*     */   public boolean isUnwrappingSerializer()
/*     */   {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter)
/*     */   {
/*  98 */     return this._defaultSerializer.withObjectIdWriter(objectIdWriter);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withFilterId(Object filterId)
/*     */   {
/* 103 */     return new BeanAsArraySerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer withIgnorals(String[] toIgnore)
/*     */   {
/* 108 */     return new BeanAsArraySerializer(this, toIgnore);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanSerializerBase asArraySerializer()
/*     */   {
/* 114 */     return this;
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
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 132 */     if (this._objectIdWriter != null) {
/* 133 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/* 134 */       return;
/*     */     }
/* 136 */     String typeStr = this._typeId == null ? null : _customTypeId(bean);
/* 137 */     if (typeStr == null) {
/* 138 */       typeSer.writeTypePrefixForArray(bean, gen);
/*     */     } else {
/* 140 */       typeSer.writeCustomTypePrefixForArray(bean, gen, typeStr);
/*     */     }
/* 142 */     serializeAsArray(bean, gen, provider);
/* 143 */     if (typeStr == null) {
/* 144 */       typeSer.writeTypeSuffixForArray(bean, gen);
/*     */     } else {
/* 146 */       typeSer.writeCustomTypeSuffixForArray(bean, gen, typeStr);
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 160 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(provider)))
/*     */     {
/* 162 */       serializeAsArray(bean, gen, provider);
/* 163 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 169 */     gen.writeStartArray();
/*     */     
/* 171 */     gen.setCurrentValue(bean);
/* 172 */     serializeAsArray(bean, gen, provider);
/* 173 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasSingleElement(SerializerProvider provider)
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */     BeanPropertyWriter[] props;
/*     */     
/* 184 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 185 */       props = this._filteredProps;
/*     */     } else {
/* 187 */       props = this._props;
/*     */     }
/* 189 */     return props.length == 1;
/*     */   }
/*     */   
/*     */   protected final void serializeAsArray(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     BeanPropertyWriter[] props;
/* 196 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 197 */       props = this._filteredProps;
/*     */     } else {
/* 199 */       props = this._props;
/*     */     }
/*     */     
/* 202 */     int i = 0;
/*     */     try {
/* 204 */       for (int len = props.length; i < len; i++) {
/* 205 */         BeanPropertyWriter prop = props[i];
/* 206 */         if (prop == null) {
/* 207 */           gen.writeNull();
/*     */         } else {
/* 209 */           prop.serializeAsElement(bean, gen, provider);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 217 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 218 */       wrapAndThrow(provider, e, bean, name);
/*     */     } catch (StackOverflowError e) {
/* 220 */       JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e);
/* 221 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 222 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 223 */       throw mapE;
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
/* 234 */     return "BeanAsArraySerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\BeanAsArraySerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */