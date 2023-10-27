/*    */ package oshi.software.os;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface OperatingSystem
/*    */   extends Serializable
/*    */ {
/*    */   public abstract String getFamily();
/*    */   
/*    */   public abstract String getManufacturer();
/*    */   
/*    */   public abstract OperatingSystemVersion getVersion();
/*    */   
/*    */   public abstract FileSystem getFileSystem();
/*    */   
/*    */   public abstract OSProcess[] getProcesses(int paramInt, ProcessSort paramProcessSort);
/*    */   
/*    */   public abstract OSProcess getProcess(int paramInt);
/*    */   
/*    */   public abstract int getProcessId();
/*    */   
/*    */   public abstract int getProcessCount();
/*    */   
/*    */   public abstract int getThreadCount();
/*    */   
/*    */   public abstract NetworkParams getNetworkParams();
/*    */   
/*    */   public static enum ProcessSort
/*    */   {
/* 36 */     CPU,  MEMORY,  OLDEST,  NEWEST,  PID,  PARENTPID,  NAME;
/*    */     
/*    */     private ProcessSort() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\OperatingSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */