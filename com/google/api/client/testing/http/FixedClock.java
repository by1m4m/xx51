/*    */ package com.google.api.client.testing.http;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import com.google.api.client.util.Clock;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class FixedClock
/*    */   implements Clock
/*    */ {
/*    */   private AtomicLong currentTime;
/*    */   
/*    */   public FixedClock()
/*    */   {
/* 41 */     this(0L);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FixedClock(long startTime)
/*    */   {
/* 49 */     this.currentTime = new AtomicLong(startTime);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FixedClock setTime(long newTime)
/*    */   {
/* 57 */     this.currentTime.set(newTime);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public long currentTimeMillis() {
/* 62 */     return this.currentTime.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\FixedClock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */