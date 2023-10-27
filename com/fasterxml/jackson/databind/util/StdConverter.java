/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
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
/*    */ 
/*    */ 
/*    */ public abstract class StdConverter<IN, OUT>
/*    */   implements Converter<IN, OUT>
/*    */ {
/*    */   public abstract OUT convert(IN paramIN);
/*    */   
/*    */   public JavaType getInputType(TypeFactory typeFactory)
/*    */   {
/* 28 */     JavaType[] types = typeFactory.findTypeParameters(getClass(), Converter.class);
/* 29 */     if ((types == null) || (types.length < 2)) {
/* 30 */       throw new IllegalStateException("Can not find OUT type parameter for Converter of type " + getClass().getName());
/*    */     }
/* 32 */     return types[0];
/*    */   }
/*    */   
/*    */ 
/*    */   public JavaType getOutputType(TypeFactory typeFactory)
/*    */   {
/* 38 */     JavaType[] types = typeFactory.findTypeParameters(getClass(), Converter.class);
/* 39 */     if ((types == null) || (types.length < 2)) {
/* 40 */       throw new IllegalStateException("Can not find OUT type parameter for Converter of type " + getClass().getName());
/*    */     }
/* 42 */     return types[1];
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\StdConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */