/*    */ package com.google.api.client.util;
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
/*    */ public final class Joiner
/*    */ {
/*    */   private final com.google.api.client.repackaged.com.google.common.base.Joiner wrapped;
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
/*    */   public static Joiner on(char separator)
/*    */   {
/* 39 */     return new Joiner(com.google.api.client.repackaged.com.google.common.base.Joiner.on(separator));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private Joiner(com.google.api.client.repackaged.com.google.common.base.Joiner wrapped)
/*    */   {
/* 46 */     this.wrapped = wrapped;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final String join(Iterable<?> parts)
/*    */   {
/* 54 */     return this.wrapped.join(parts);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Joiner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */