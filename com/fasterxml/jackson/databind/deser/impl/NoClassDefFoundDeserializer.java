/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoClassDefFoundDeserializer<T>
/*    */   extends JsonDeserializer<T>
/*    */ {
/*    */   private final NoClassDefFoundError _cause;
/*    */   
/*    */   public NoClassDefFoundDeserializer(NoClassDefFoundError cause)
/*    */   {
/* 21 */     this._cause = cause;
/*    */   }
/*    */   
/*    */ 
/*    */   public T deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 28 */     throw this._cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\NoClassDefFoundDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */