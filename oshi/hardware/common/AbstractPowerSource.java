/*    */ package oshi.hardware.common;
/*    */ 
/*    */ import oshi.hardware.PowerSource;
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
/*    */ public abstract class AbstractPowerSource
/*    */   implements PowerSource
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String name;
/*    */   protected double remainingCapacity;
/*    */   protected double timeRemaining;
/*    */   
/*    */   public AbstractPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining)
/*    */   {
/* 50 */     this.name = newName;
/* 51 */     this.remainingCapacity = newRemainingCapacity;
/* 52 */     this.timeRemaining = newTimeRemaining;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 60 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getRemainingCapacity()
/*    */   {
/* 68 */     return this.remainingCapacity;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getTimeRemaining()
/*    */   {
/* 76 */     return this.timeRemaining;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractPowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */