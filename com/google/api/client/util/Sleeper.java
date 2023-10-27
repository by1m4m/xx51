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
/*    */ 
/*    */ public abstract interface Sleeper
/*    */ {
/* 40 */   public static final Sleeper DEFAULT = new Sleeper()
/*    */   {
/*    */     public void sleep(long millis) throws InterruptedException {
/* 43 */       Thread.sleep(millis);
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract void sleep(long paramLong)
/*    */     throws InterruptedException;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Sleeper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */