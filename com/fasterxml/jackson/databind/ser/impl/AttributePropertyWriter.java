/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*    */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*    */ import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
/*    */ import com.fasterxml.jackson.databind.util.Annotations;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributePropertyWriter
/*    */   extends VirtualBeanPropertyWriter
/*    */ {
/*    */   protected final String _attrName;
/*    */   
/*    */   protected AttributePropertyWriter(String attrName, BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType)
/*    */   {
/* 33 */     this(attrName, propDef, contextAnnotations, declaredType, propDef.findInclusion());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected AttributePropertyWriter(String attrName, BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType, JsonInclude.Include inclusion)
/*    */   {
/* 40 */     super(propDef, contextAnnotations, declaredType, null, null, null, inclusion);
/*    */     
/*    */ 
/* 43 */     this._attrName = attrName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static AttributePropertyWriter construct(String attrName, BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType)
/*    */   {
/* 51 */     return new AttributePropertyWriter(attrName, propDef, contextAnnotations, declaredType);
/*    */   }
/*    */   
/*    */   protected AttributePropertyWriter(AttributePropertyWriter base)
/*    */   {
/* 56 */     super(base);
/* 57 */     this._attrName = base._attrName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType type)
/*    */   {
/* 67 */     throw new IllegalStateException("Should not be called on this type");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Object value(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*    */     throws Exception
/*    */   {
/* 78 */     return prov.getAttribute(this._attrName);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\AttributePropertyWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */