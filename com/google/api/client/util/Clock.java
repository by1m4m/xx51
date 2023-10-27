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
/*    */ public abstract interface Clock
/*    */ {
/* 39 */   public static final Clock SYSTEM = new Clock() {
/*    */     public long currentTimeMillis() {
/* 41 */       return System.currentTimeMillis();
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract long currentTimeMillis();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Clock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */