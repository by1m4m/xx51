/*    */ package com.fasterxml.jackson.databind.jsonFormatVisitors;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ 
/*    */ public enum JsonFormatTypes
/*    */ {
/*  8 */   STRING, 
/*  9 */   NUMBER, 
/* 10 */   INTEGER, 
/* 11 */   BOOLEAN, 
/* 12 */   OBJECT, 
/* 13 */   ARRAY, 
/* 14 */   NULL, 
/* 15 */   ANY;
/*    */   
/*    */   private JsonFormatTypes() {}
/*    */   
/*    */   @JsonValue
/* 20 */   public String value() { return name().toLowerCase(); }
/*    */   
/*    */   @JsonCreator
/*    */   public static JsonFormatTypes forValue(String s)
/*    */   {
/* 25 */     return valueOf(s.toUpperCase());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonFormatTypes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */