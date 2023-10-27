/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
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
/*    */ public class AsExternalTypeDeserializer
/*    */   extends AsArrayTypeDeserializer
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public AsExternalTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl)
/*    */   {
/* 25 */     super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
/*    */   }
/*    */   
/*    */   public AsExternalTypeDeserializer(AsExternalTypeDeserializer src, BeanProperty property) {
/* 29 */     super(src, property);
/*    */   }
/*    */   
/*    */   public TypeDeserializer forProperty(BeanProperty prop)
/*    */   {
/* 34 */     if (prop == this._property) {
/* 35 */       return this;
/*    */     }
/* 37 */     return new AsExternalTypeDeserializer(this, prop);
/*    */   }
/*    */   
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 41 */     return JsonTypeInfo.As.EXTERNAL_PROPERTY;
/*    */   }
/*    */   
/*    */   protected boolean _usesExternalId()
/*    */   {
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExternalTypeDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */