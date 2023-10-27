/*    */ package com.fasterxml.jackson.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonParseException
/*    */   extends JsonProcessingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonParseException(String msg, JsonLocation loc)
/*    */   {
/* 17 */     super(msg, loc);
/*    */   }
/*    */   
/*    */   public JsonParseException(String msg, JsonLocation loc, Throwable root) {
/* 21 */     super(msg, loc, root);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */