/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NullProvider
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final Object _nullValue;
/*    */   private final boolean _isPrimitive;
/*    */   private final Class<?> _rawType;
/*    */   
/*    */   public NullProvider(JavaType type, Object nullValue)
/*    */   {
/* 25 */     this._nullValue = nullValue;
/*    */     
/* 27 */     this._isPrimitive = type.isPrimitive();
/* 28 */     this._rawType = type.getRawClass();
/*    */   }
/*    */   
/*    */   public Object nullValue(DeserializationContext ctxt) throws JsonProcessingException
/*    */   {
/* 33 */     if ((this._isPrimitive) && (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES))) {
/* 34 */       throw ctxt.mappingException("Can not map JSON null into type " + this._rawType.getName() + " (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)");
/*    */     }
/*    */     
/* 37 */     return this._nullValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\NullProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */