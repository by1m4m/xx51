/*    */ package oshi.hardware.platform.windows;
/*    */ 
/*    */ import com.sun.jna.platform.win32.BaseTSD.SIZE_T;
/*    */ import com.sun.jna.platform.win32.Kernel32;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.common.AbstractGlobalMemory;
/*    */ import oshi.jna.platform.windows.Psapi;
/*    */ import oshi.jna.platform.windows.Psapi.PERFORMANCE_INFORMATION;
/*    */ import oshi.util.platform.windows.WmiUtil;
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
/*    */ public class WindowsGlobalMemory
/*    */   extends AbstractGlobalMemory
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 43 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsGlobalMemory.class);
/*    */   
/* 45 */   private transient Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();
/*    */   
/* 47 */   private long lastUpdate = 0L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateMeminfo()
/*    */   {
/* 54 */     long now = System.currentTimeMillis();
/* 55 */     if (now - this.lastUpdate > 100L) {
/* 56 */       if (!Psapi.INSTANCE.GetPerformanceInfo(this.perfInfo, this.perfInfo.size())) {
/* 57 */         LOG.error("Failed to get Performance Info. Error code: {}", Integer.valueOf(Kernel32.INSTANCE.GetLastError()));
/* 58 */         return;
/*    */       }
/* 60 */       this.memAvailable = (this.perfInfo.PageSize.longValue() * this.perfInfo.PhysicalAvailable.longValue());
/* 61 */       this.memTotal = (this.perfInfo.PageSize.longValue() * this.perfInfo.PhysicalTotal.longValue());
/*    */       
/* 63 */       this.swapTotal = (this.perfInfo.PageSize.longValue() * (this.perfInfo.CommitLimit.longValue() - this.perfInfo.PhysicalTotal.longValue()));
/* 64 */       this.lastUpdate = now;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateSwap()
/*    */   {
/* 73 */     updateMeminfo();
/* 74 */     Map<String, List<Long>> usage = WmiUtil.selectUint32sFrom(null, "Win32_PerfRawData_PerfOS_PagingFile", "PercentUsage,PercentUsage_Base", "WHERE Name=\"_Total\"");
/*    */     
/* 76 */     if (!((List)usage.get("PercentUsage")).isEmpty()) {
/* 77 */       this.swapUsed = (this.swapTotal * ((Long)((List)usage.get("PercentUsage")).get(0)).longValue() / ((Long)((List)usage.get("PercentUsage_Base")).get(0)).longValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsGlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */