/*    */ package oshi.hardware.common;
/*    */ 
/*    */ import oshi.hardware.GlobalMemory;
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
/*    */ public abstract class AbstractGlobalMemory
/*    */   implements GlobalMemory
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 33 */   protected long memTotal = 0L;
/* 34 */   protected long memAvailable = 0L;
/* 35 */   protected long swapTotal = 0L;
/* 36 */   protected long swapUsed = 0L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract void updateMeminfo();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract void updateSwap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getAvailable()
/*    */   {
/* 53 */     updateMeminfo();
/* 54 */     return this.memAvailable;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getTotal()
/*    */   {
/* 62 */     if (this.memTotal == 0L) {
/* 63 */       updateMeminfo();
/*    */     }
/* 65 */     return this.memTotal;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getSwapUsed()
/*    */   {
/* 73 */     updateSwap();
/* 74 */     return this.swapUsed;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getSwapTotal()
/*    */   {
/* 82 */     updateSwap();
/* 83 */     return this.swapTotal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractGlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */