/*    */ package oshi.hardware.platform.unix.solaris;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import oshi.hardware.common.AbstractGlobalMemory;
/*    */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*    */ import oshi.util.ExecutingCommand;
/*    */ import oshi.util.ParseUtil;
/*    */ import oshi.util.platform.unix.solaris.KstatUtil;
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
/*    */ public class SolarisGlobalMemory
/*    */   extends AbstractGlobalMemory
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 40 */   private static final long PAGESIZE = ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("pagesize"), 4096L);
/*    */   
/*    */ 
/* 43 */   private static final Pattern SWAPINFO = Pattern.compile(".+\\s(\\d+)K\\s+(\\d+)K$");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateMeminfo()
/*    */   {
/* 51 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup(null, -1, "system_pages");
/*    */     
/* 53 */     if ((ksp != null) && (KstatUtil.kstatRead(ksp))) {
/* 54 */       this.memAvailable = (KstatUtil.kstatDataLookupLong(ksp, "availrmem") * PAGESIZE);
/* 55 */       this.memTotal = (KstatUtil.kstatDataLookupLong(ksp, "physmem") * PAGESIZE);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateSwap()
/*    */   {
/* 64 */     String swapInfo = ExecutingCommand.getAnswerAt("swap -lk", 1);
/* 65 */     Matcher m = SWAPINFO.matcher(swapInfo);
/* 66 */     if (m.matches()) {
/* 67 */       this.swapTotal = (ParseUtil.parseLongOrDefault(m.group(1), 0L) << 10);
/* 68 */       this.swapUsed = (this.swapTotal - (ParseUtil.parseLongOrDefault(m.group(2), 0L) << 10));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisGlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */