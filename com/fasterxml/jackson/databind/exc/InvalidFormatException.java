/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidFormatException
/*    */   extends JsonMappingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final Object _value;
/*    */   protected final Class<?> _targetType;
/*    */   
/*    */   public InvalidFormatException(String msg, Object value, Class<?> targetType)
/*    */   {
/* 39 */     super(msg);
/* 40 */     this._value = value;
/* 41 */     this._targetType = targetType;
/*    */   }
/*    */   
/*    */ 
/*    */   public InvalidFormatException(String msg, JsonLocation loc, Object value, Class<?> targetType)
/*    */   {
/* 47 */     super(msg, loc);
/* 48 */     this._value = value;
/* 49 */     this._targetType = targetType;
/*    */   }
/*    */   
/*    */ 
/*    */   public static InvalidFormatException from(JsonParser jp, String msg, Object value, Class<?> targetType)
/*    */   {
/* 55 */     return new InvalidFormatException(msg, jp.getTokenLocation(), value, targetType);
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
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 72 */     return this._value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Class<?> getTargetType()
/*    */   {
/* 82 */     return this._targetType;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\exc\InvalidFormatException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */