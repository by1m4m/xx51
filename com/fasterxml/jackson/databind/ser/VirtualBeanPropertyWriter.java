/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
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
/*     */ public abstract class VirtualBeanPropertyWriter
/*     */   extends BeanPropertyWriter
/*     */ {
/*     */   protected VirtualBeanPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType)
/*     */   {
/*  32 */     this(propDef, contextAnnotations, declaredType, null, null, null, propDef.findInclusion());
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
/*     */   protected VirtualBeanPropertyWriter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected VirtualBeanPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, JsonInclude.Include inclusion)
/*     */   {
/*  54 */     super(propDef, propDef.getPrimaryMember(), contextAnnotations, declaredType, ser, typeSer, serType, _suppressNulls(inclusion), _suppressableValue(inclusion));
/*     */   }
/*     */   
/*     */ 
/*     */   protected VirtualBeanPropertyWriter(VirtualBeanPropertyWriter base)
/*     */   {
/*  60 */     super(base);
/*     */   }
/*     */   
/*     */   protected VirtualBeanPropertyWriter(VirtualBeanPropertyWriter base, PropertyName name) {
/*  64 */     super(base, name);
/*     */   }
/*     */   
/*     */   protected static boolean _suppressNulls(JsonInclude.Include inclusion) {
/*  68 */     return inclusion != JsonInclude.Include.ALWAYS;
/*     */   }
/*     */   
/*     */   protected static Object _suppressableValue(JsonInclude.Include inclusion) {
/*  72 */     if ((inclusion == JsonInclude.Include.NON_EMPTY) || (inclusion == JsonInclude.Include.NON_EMPTY))
/*     */     {
/*  74 */       return MARKER_FOR_EMPTY;
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVirtual()
/*     */   {
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   public Class<?> getPropertyType() {
/*  90 */     return this._declaredType.getRawClass();
/*     */   }
/*     */   
/*     */   public Type getGenericPropertyType()
/*     */   {
/*  95 */     return getPropertyType();
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
/*     */   protected abstract Object value(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws Exception;
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
/*     */   public abstract VirtualBeanPropertyWriter withConfig(MapperConfig<?> paramMapperConfig, AnnotatedClass paramAnnotatedClass, BeanPropertyDefinition paramBeanPropertyDefinition, JavaType paramJavaType);
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
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 139 */     Object value = value(bean, gen, prov);
/*     */     
/* 141 */     if (value == null) {
/* 142 */       if (this._nullSerializer != null) {
/* 143 */         gen.writeFieldName(this._name);
/* 144 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       }
/* 146 */       return;
/*     */     }
/* 148 */     JsonSerializer<Object> ser = this._serializer;
/* 149 */     if (ser == null) {
/* 150 */       Class<?> cls = value.getClass();
/* 151 */       PropertySerializerMap m = this._dynamicSerializers;
/* 152 */       ser = m.serializerFor(cls);
/* 153 */       if (ser == null) {
/* 154 */         ser = _findAndAddDynamic(m, cls, prov);
/*     */       }
/*     */     }
/* 157 */     if (this._suppressableValue != null) {
/* 158 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 159 */         if (!ser.isEmpty(prov, value)) {}
/*     */ 
/*     */       }
/* 162 */       else if (this._suppressableValue.equals(value)) {
/* 163 */         return;
/*     */       }
/*     */     }
/* 166 */     if (value == bean)
/*     */     {
/* 168 */       if (_handleSelfReference(bean, gen, prov, ser)) {
/* 169 */         return;
/*     */       }
/*     */     }
/* 172 */     gen.writeFieldName(this._name);
/* 173 */     if (this._typeSerializer == null) {
/* 174 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 176 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 188 */     Object value = value(bean, gen, prov);
/*     */     
/* 190 */     if (value == null) {
/* 191 */       if (this._nullSerializer != null) {
/* 192 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       } else {
/* 194 */         gen.writeNull();
/*     */       }
/* 196 */       return;
/*     */     }
/* 198 */     JsonSerializer<Object> ser = this._serializer;
/* 199 */     if (ser == null) {
/* 200 */       Class<?> cls = value.getClass();
/* 201 */       PropertySerializerMap map = this._dynamicSerializers;
/* 202 */       ser = map.serializerFor(cls);
/* 203 */       if (ser == null) {
/* 204 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/* 207 */     if (this._suppressableValue != null) {
/* 208 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 209 */         if (ser.isEmpty(prov, value)) {
/* 210 */           serializeAsPlaceholder(bean, gen, prov);
/*     */         }
/*     */       }
/* 213 */       else if (this._suppressableValue.equals(value)) {
/* 214 */         serializeAsPlaceholder(bean, gen, prov);
/* 215 */         return;
/*     */       }
/*     */     }
/* 218 */     if ((value == bean) && 
/* 219 */       (_handleSelfReference(bean, gen, prov, ser))) {
/* 220 */       return;
/*     */     }
/*     */     
/* 223 */     if (this._typeSerializer == null) {
/* 224 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 226 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\VirtualBeanPropertyWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */