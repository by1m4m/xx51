/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ @GwtCompatible
/*    */ public abstract class Ticker
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public abstract long read();
/*    */   
/*    */   public static Ticker systemTicker()
/*    */   {
/* 48 */     return SYSTEM_TICKER;
/*    */   }
/*    */   
/* 51 */   private static final Ticker SYSTEM_TICKER = new Ticker()
/*    */   {
/*    */     public long read()
/*    */     {
/* 55 */       return Platform.systemNanoTime();
/*    */     }
/*    */   };
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\Ticker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */