/*    */ package com.google.api.client.util;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public final class BackOffUtils
/*    */ {
/*    */   public static boolean next(Sleeper sleeper, BackOff backOff)
/*    */     throws InterruptedException, IOException
/*    */   {
/* 46 */     long backOffTime = backOff.nextBackOffMillis();
/* 47 */     if (backOffTime == -1L) {
/* 48 */       return false;
/*    */     }
/* 50 */     sleeper.sleep(backOffTime);
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\BackOffUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */