/*    */ package oshi.jna.platform.windows;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.Structure;
/*    */ import com.sun.jna.platform.win32.BaseTSD.SIZE_T;
/*    */ import com.sun.jna.platform.win32.WinDef.DWORD;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public abstract interface Psapi
/*    */   extends com.sun.jna.platform.win32.Psapi
/*    */ {
/* 36 */   public static final Psapi INSTANCE = (Psapi)Native.loadLibrary("Psapi", Psapi.class);
/*    */   
/*    */   public abstract boolean GetPerformanceInfo(PERFORMANCE_INFORMATION paramPERFORMANCE_INFORMATION, int paramInt);
/*    */   
/*    */   public static class PERFORMANCE_INFORMATION extends Structure {
/*    */     public WinDef.DWORD cb;
/*    */     public BaseTSD.SIZE_T CommitTotal;
/*    */     public BaseTSD.SIZE_T CommitLimit;
/*    */     public BaseTSD.SIZE_T CommitPeak;
/*    */     public BaseTSD.SIZE_T PhysicalTotal;
/*    */     public BaseTSD.SIZE_T PhysicalAvailable;
/*    */     public BaseTSD.SIZE_T SystemCache;
/*    */     public BaseTSD.SIZE_T KernelTotal;
/*    */     public BaseTSD.SIZE_T KernelPaged;
/*    */     public BaseTSD.SIZE_T KernelNonpaged;
/*    */     public BaseTSD.SIZE_T PageSize;
/*    */     public WinDef.DWORD HandleCount;
/*    */     public WinDef.DWORD ProcessCount;
/*    */     public WinDef.DWORD ThreadCount;
/*    */     
/*    */     protected List<String> getFieldOrder() {
/* 57 */       return Arrays.asList(new String[] { "cb", "CommitTotal", "CommitLimit", "CommitPeak", "PhysicalTotal", "PhysicalAvailable", "SystemCache", "KernelTotal", "KernelPaged", "KernelNonpaged", "PageSize", "HandleCount", "ProcessCount", "ThreadCount" });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\Psapi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */