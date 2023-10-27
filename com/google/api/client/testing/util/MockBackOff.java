/*    */ package com.google.api.client.testing.util;
/*    */ 
/*    */ import com.google.api.client.util.BackOff;
/*    */ import com.google.api.client.util.Beta;
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import java.io.IOException;
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
/*    */ public class MockBackOff
/*    */   implements BackOff
/*    */ {
/*    */   private long backOffMillis;
/* 41 */   private int maxTries = 10;
/*    */   private int numTries;
/*    */   
/*    */   public void reset()
/*    */     throws IOException
/*    */   {
/* 47 */     this.numTries = 0;
/*    */   }
/*    */   
/*    */   public long nextBackOffMillis() throws IOException {
/* 51 */     if ((this.numTries >= this.maxTries) || (this.backOffMillis == -1L)) {
/* 52 */       return -1L;
/*    */     }
/* 54 */     this.numTries += 1;
/* 55 */     return this.backOffMillis;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MockBackOff setBackOffMillis(long backOffMillis)
/*    */   {
/* 67 */     Preconditions.checkArgument((backOffMillis == -1L) || (backOffMillis >= 0L));
/* 68 */     this.backOffMillis = backOffMillis;
/* 69 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MockBackOff setMaxTries(int maxTries)
/*    */   {
/* 81 */     Preconditions.checkArgument(maxTries >= 0);
/* 82 */     this.maxTries = maxTries;
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public final int getMaxTries()
/*    */   {
/* 88 */     return this.numTries;
/*    */   }
/*    */   
/*    */   public final int getNumberOfTries()
/*    */   {
/* 93 */     return this.numTries;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\util\MockBackOff.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */