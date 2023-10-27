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
/*    */ public abstract interface NanoClock
/*    */ {
/* 39 */   public static final NanoClock SYSTEM = new NanoClock() {
/*    */     public long nanoTime() {
/* 41 */       return System.nanoTime();
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract long nanoTime();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\NanoClock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */