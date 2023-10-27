/*    */ package com.fasterxml.jackson.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonGenerationException
/*    */   extends JsonProcessingException
/*    */ {
/*    */   private static final long serialVersionUID = 123L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonGenerationException(Throwable rootCause)
/*    */   {
/* 20 */     super(rootCause);
/*    */   }
/*    */   
/*    */   public JsonGenerationException(String msg)
/*    */   {
/* 25 */     super(msg, (JsonLocation)null);
/*    */   }
/*    */   
/*    */   public JsonGenerationException(String msg, Throwable rootCause)
/*    */   {
/* 30 */     super(msg, null, rootCause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonGenerationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */