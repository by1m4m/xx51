/*    */ package oshi.hardware.platform.unix.freebsd;
/*    */ 
/*    */ import oshi.hardware.common.AbstractGlobalMemory;
/*    */ import oshi.util.ExecutingCommand;
/*    */ import oshi.util.ParseUtil;
/*    */ import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
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
/*    */ public class FreeBsdGlobalMemory
/*    */   extends AbstractGlobalMemory
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 36 */   private static final long PAGESIZE = BsdSysctlUtil.sysctl("hw.pagesize", 4096);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateMeminfo()
/*    */   {
/* 43 */     if (this.memTotal == 0L) {
/* 44 */       this.memTotal = BsdSysctlUtil.sysctl("hw.physmem", 0L);
/*    */     }
/*    */     
/* 47 */     long inactive = BsdSysctlUtil.sysctl("vm.stats.vm.v_inactive_count", 0L);
/* 48 */     long cache = BsdSysctlUtil.sysctl("vm.stats.vm.v_cache_count", 0L);
/* 49 */     long free = BsdSysctlUtil.sysctl("vm.stats.vm.v_free_count", 0L);
/* 50 */     this.memAvailable = ((inactive + cache + free) * PAGESIZE);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateSwap()
/*    */   {
/* 58 */     this.swapTotal = BsdSysctlUtil.sysctl("vm.swap_total", 0L);
/* 59 */     String swapInfo = ExecutingCommand.getAnswerAt("swapinfo -k", 1);
/* 60 */     String[] split = swapInfo.split("\\s+");
/* 61 */     if (split.length < 5) {
/* 62 */       return;
/*    */     }
/* 64 */     this.swapUsed = (ParseUtil.parseLongOrDefault(split[2], 0L) << 10);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdGlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */