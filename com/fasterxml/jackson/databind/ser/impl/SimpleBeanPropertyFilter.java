/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class SimpleBeanPropertyFilter
/*     */   implements BeanPropertyFilter, PropertyFilter
/*     */ {
/*     */   public static SimpleBeanPropertyFilter serializeAll(Set<String> properties)
/*     */   {
/*  40 */     return new FilterExceptFilter(properties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyFilter filterOutAllExcept(Set<String> properties)
/*     */   {
/*  48 */     return new FilterExceptFilter(properties);
/*     */   }
/*     */   
/*     */   public static SimpleBeanPropertyFilter filterOutAllExcept(String... propertyArray) {
/*  52 */     HashSet<String> properties = new HashSet(propertyArray.length);
/*  53 */     Collections.addAll(properties, propertyArray);
/*  54 */     return new FilterExceptFilter(properties);
/*     */   }
/*     */   
/*     */   public static SimpleBeanPropertyFilter serializeAllExcept(Set<String> properties) {
/*  58 */     return new SerializeExceptFilter(properties);
/*     */   }
/*     */   
/*     */   public static SimpleBeanPropertyFilter serializeAllExcept(String... propertyArray) {
/*  62 */     HashSet<String> properties = new HashSet(propertyArray.length);
/*  63 */     Collections.addAll(properties, propertyArray);
/*  64 */     return new SerializeExceptFilter(properties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyFilter from(BeanPropertyFilter src)
/*     */   {
/*  76 */     new PropertyFilter()
/*     */     {
/*     */       public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
/*     */         throws Exception
/*     */       {
/*  81 */         this.val$src.serializeAsField(pojo, jgen, prov, (BeanPropertyWriter)writer);
/*     */       }
/*     */       
/*     */ 
/*     */       public void depositSchemaProperty(PropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider)
/*     */         throws JsonMappingException
/*     */       {
/*  88 */         this.val$src.depositSchemaProperty((BeanPropertyWriter)writer, propertiesNode, provider);
/*     */       }
/*     */       
/*     */ 
/*     */       public void depositSchemaProperty(PropertyWriter writer, JsonObjectFormatVisitor objectVisitor, SerializerProvider provider)
/*     */         throws JsonMappingException
/*     */       {
/*  95 */         this.val$src.depositSchemaProperty((BeanPropertyWriter)writer, objectVisitor, provider);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void serializeAsElement(Object elementValue, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
/*     */         throws Exception
/*     */       {
/* 103 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
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
/*     */   protected boolean include(BeanPropertyWriter writer)
/*     */   {
/* 120 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean include(PropertyWriter writer)
/*     */   {
/* 130 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean includeElement(Object elementValue)
/*     */   {
/* 141 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer)
/*     */     throws Exception
/*     */   {
/* 155 */     if (include(writer)) {
/* 156 */       writer.serializeAsField(bean, jgen, provider);
/* 157 */     } else if (!jgen.canOmitFields()) {
/* 158 */       writer.serializeAsOmittedField(bean, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(BeanPropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 168 */     if (include(writer)) {
/* 169 */       writer.depositSchemaProperty(propertiesNode, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(BeanPropertyWriter writer, JsonObjectFormatVisitor objectVisitor, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 179 */     if (include(writer)) {
/* 180 */       writer.depositSchemaProperty(objectVisitor);
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
/*     */   public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
/*     */     throws Exception
/*     */   {
/* 195 */     if (include(writer)) {
/* 196 */       writer.serializeAsField(pojo, jgen, provider);
/* 197 */     } else if (!jgen.canOmitFields()) {
/* 198 */       writer.serializeAsOmittedField(pojo, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeAsElement(Object elementValue, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
/*     */     throws Exception
/*     */   {
/* 207 */     if (includeElement(elementValue)) {
/* 208 */       writer.serializeAsElement(elementValue, jgen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(PropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 218 */     if (include(writer)) {
/* 219 */       writer.depositSchemaProperty(propertiesNode, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void depositSchemaProperty(PropertyWriter writer, JsonObjectFormatVisitor objectVisitor, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 228 */     if (include(writer)) {
/* 229 */       writer.depositSchemaProperty(objectVisitor);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class FilterExceptFilter
/*     */     extends SimpleBeanPropertyFilter
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final Set<String> _propertiesToInclude;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public FilterExceptFilter(Set<String> properties)
/*     */     {
/* 255 */       this._propertiesToInclude = properties;
/*     */     }
/*     */     
/*     */     protected boolean include(BeanPropertyWriter writer)
/*     */     {
/* 260 */       return this._propertiesToInclude.contains(writer.getName());
/*     */     }
/*     */     
/*     */     protected boolean include(PropertyWriter writer)
/*     */     {
/* 265 */       return this._propertiesToInclude.contains(writer.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SerializeExceptFilter
/*     */     extends SimpleBeanPropertyFilter
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */     protected final Set<String> _propertiesToExclude;
/*     */     
/*     */ 
/*     */ 
/*     */     public SerializeExceptFilter(Set<String> properties)
/*     */     {
/* 285 */       this._propertiesToExclude = properties;
/*     */     }
/*     */     
/*     */     protected boolean include(BeanPropertyWriter writer)
/*     */     {
/* 290 */       return !this._propertiesToExclude.contains(writer.getName());
/*     */     }
/*     */     
/*     */     protected boolean include(PropertyWriter writer)
/*     */     {
/* 295 */       return !this._propertiesToExclude.contains(writer.getName());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\SimpleBeanPropertyFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */