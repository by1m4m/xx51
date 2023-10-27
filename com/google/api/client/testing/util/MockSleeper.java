/*    */ package com.google.api.client.testing.util;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import com.google.api.client.util.Sleeper;
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
/*    */ @Beta
/*    */ public class MockSleeper
/*    */   implements Sleeper
/*    */ {
/*    */   private int count;
/*    */   private long lastMillis;
/*    */   
/*    */   public void sleep(long millis)
/*    */     throws InterruptedException
/*    */   {
/* 44 */     this.count += 1;
/* 45 */     this.lastMillis = millis;
/*    */   }
/*    */   
/*    */   public final int getCount()
/*    */   {
/* 50 */     return this.count;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final long getLastMillis()
/*    */   {
/* 58 */     return this.lastMillis;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\util\MockSleeper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */