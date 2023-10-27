/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ 
/*    */ public class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public AtomicBooleanDeserializer()
/*    */   {
/* 13 */     super(AtomicBoolean.class);
/*    */   }
/*    */   
/*    */   public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt) throws java.io.IOException {
/* 17 */     return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\AtomicBooleanDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */