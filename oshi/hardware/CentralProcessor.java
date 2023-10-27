/*    */ package oshi.hardware;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface CentralProcessor
/*    */   extends Serializable
/*    */ {
/*    */   public abstract String getVendor();
/*    */   
/*    */   public abstract void setVendor(String paramString);
/*    */   
/*    */   public abstract String getName();
/*    */   
/*    */   public abstract void setName(String paramString);
/*    */   
/*    */   public abstract long getVendorFreq();
/*    */   
/*    */   public abstract void setVendorFreq(long paramLong);
/*    */   
/*    */   public abstract String getProcessorID();
/*    */   
/*    */   public abstract void setProcessorID(String paramString);
/*    */   
/*    */   public abstract String getIdentifier();
/*    */   
/*    */   public abstract void setIdentifier(String paramString);
/*    */   
/*    */   public abstract boolean isCpu64bit();
/*    */   
/*    */   public abstract void setCpu64(boolean paramBoolean);
/*    */   
/*    */   public abstract String getStepping();
/*    */   
/*    */   public abstract void setStepping(String paramString);
/*    */   
/*    */   public static enum TickType
/*    */   {
/* 41 */     USER(0), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 46 */     NICE(1), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 52 */     SYSTEM(2), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 57 */     IDLE(3), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 62 */     IOWAIT(4), 
/*    */     
/*    */ 
/*    */ 
/* 66 */     IRQ(5), 
/*    */     
/*    */ 
/*    */ 
/* 70 */     SOFTIRQ(6);
/*    */     
/*    */     private int index;
/*    */     
/*    */     private TickType(int value) {
/* 75 */       this.index = value;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public int getIndex()
/*    */     {
/* 83 */       return this.index;
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract String getModel();
/*    */   
/*    */   public abstract void setModel(String paramString);
/*    */   
/*    */   public abstract String getFamily();
/*    */   
/*    */   public abstract void setFamily(String paramString);
/*    */   
/*    */   public abstract double getSystemCpuLoadBetweenTicks();
/*    */   
/*    */   public abstract long[] getSystemCpuLoadTicks();
/*    */   
/*    */   public abstract double getSystemCpuLoad();
/*    */   
/*    */   public abstract double getSystemLoadAverage();
/*    */   
/*    */   public abstract double[] getSystemLoadAverage(int paramInt);
/*    */   
/*    */   public abstract double[] getProcessorCpuLoadBetweenTicks();
/*    */   
/*    */   public abstract long[][] getProcessorCpuLoadTicks();
/*    */   
/*    */   public abstract long getSystemUptime();
/*    */   
/*    */   @Deprecated
/*    */   public abstract String getSystemSerialNumber();
/*    */   
/*    */   public abstract int getLogicalProcessorCount();
/*    */   
/*    */   public abstract int getPhysicalProcessorCount();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\CentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */