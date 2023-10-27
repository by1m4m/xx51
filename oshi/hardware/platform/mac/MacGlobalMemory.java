/*    */ package oshi.hardware.platform.mac;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.platform.mac.SystemB;
/*    */ import com.sun.jna.platform.mac.SystemB.VMStatistics;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import com.sun.jna.ptr.LongByReference;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.common.AbstractGlobalMemory;
/*    */ import oshi.jna.platform.mac.SystemB.XswUsage;
/*    */ import oshi.util.platform.mac.SysctlUtil;
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
/*    */ public class MacGlobalMemory
/*    */   extends AbstractGlobalMemory
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 43 */   private static final Logger LOG = LoggerFactory.getLogger(MacGlobalMemory.class);
/*    */   
/* 45 */   private transient SystemB.XswUsage xswUsage = new SystemB.XswUsage();
/* 46 */   private long lastUpdateSwap = 0L;
/*    */   
/* 48 */   private transient SystemB.VMStatistics vmStats = new SystemB.VMStatistics();
/* 49 */   private long lastUpdateAvail = 0L;
/*    */   
/* 51 */   private long pageSize = 4096L;
/*    */   
/*    */   public MacGlobalMemory() {
/* 54 */     long memory = SysctlUtil.sysctl("hw.memsize", -1L);
/* 55 */     if (memory >= 0L) {
/* 56 */       this.memTotal = memory;
/*    */     }
/*    */     
/* 59 */     LongByReference pPageSize = new LongByReference();
/* 60 */     if (0 != SystemB.INSTANCE.host_page_size(SystemB.INSTANCE.mach_host_self(), pPageSize)) {
/* 61 */       LOG.error("Failed to get host page size. Error code: " + Native.getLastError());
/* 62 */       return;
/*    */     }
/* 64 */     this.pageSize = pPageSize.getValue();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateMeminfo()
/*    */   {
/* 72 */     long now = System.currentTimeMillis();
/* 73 */     if (now - this.lastUpdateAvail > 100L) {
/* 74 */       if (0 != SystemB.INSTANCE.host_statistics(SystemB.INSTANCE.mach_host_self(), 2, this.vmStats, new IntByReference(this.vmStats
/* 75 */         .size() / SystemB.INT_SIZE))) {
/* 76 */         LOG.error("Failed to get host VM info. Error code: " + Native.getLastError());
/* 77 */         return;
/*    */       }
/* 79 */       this.memAvailable = ((this.vmStats.free_count + this.vmStats.inactive_count) * this.pageSize);
/* 80 */       this.lastUpdateAvail = now;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateSwap()
/*    */   {
/* 89 */     long now = System.currentTimeMillis();
/* 90 */     if (now - this.lastUpdateSwap > 100L) {
/* 91 */       if (!SysctlUtil.sysctl("vm.swapusage", this.xswUsage)) {
/* 92 */         return;
/*    */       }
/* 94 */       this.swapUsed = this.xswUsage.xsu_used;
/* 95 */       this.swapTotal = this.xswUsage.xsu_total;
/* 96 */       this.lastUpdateSwap = now;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacGlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */