/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DatabindContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
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
/*    */ public abstract class TypeIdResolverBase
/*    */   implements TypeIdResolver
/*    */ {
/*    */   protected final TypeFactory _typeFactory;
/*    */   protected final JavaType _baseType;
/*    */   
/*    */   protected TypeIdResolverBase()
/*    */   {
/* 29 */     this(null, null);
/*    */   }
/*    */   
/*    */   protected TypeIdResolverBase(JavaType baseType, TypeFactory typeFactory) {
/* 33 */     this._baseType = baseType;
/* 34 */     this._typeFactory = typeFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void init(JavaType bt) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String idFromBaseType()
/*    */   {
/* 48 */     return idFromValueAndType(null, this._baseType.getRawClass());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public JavaType typeFromId(String id)
/*    */   {
/* 58 */     return typeFromId(null, id);
/*    */   }
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
/*    */   public JavaType typeFromId(DatabindContext context, String id)
/*    */   {
/* 72 */     return typeFromId(id);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescForKnownTypeIds()
/*    */   {
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeIdResolverBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */