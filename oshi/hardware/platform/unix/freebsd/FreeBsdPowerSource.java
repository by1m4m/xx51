/*    */ package oshi.hardware.platform.unix.freebsd;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.PowerSource;
/*    */ import oshi.hardware.common.AbstractPowerSource;
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
/*    */ public class FreeBsdPowerSource
/*    */   extends AbstractPowerSource
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 37 */   private static final Logger LOG = LoggerFactory.getLogger(FreeBsdPowerSource.class);
/*    */   
/*    */   public FreeBsdPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining) {
/* 40 */     super(newName, newRemainingCapacity, newTimeRemaining);
/* 41 */     LOG.debug("Initialized FreeBsdPowerSource");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static PowerSource[] getPowerSources()
/*    */   {
/* 50 */     FreeBsdPowerSource[] ps = new FreeBsdPowerSource[1];
/*    */     
/* 52 */     int state = BsdSysctlUtil.sysctl("hw.acpi.battery.state", 0);
/*    */     
/* 54 */     int time = BsdSysctlUtil.sysctl("hw.acpi.battery.time", -1);
/*    */     
/* 56 */     int life = BsdSysctlUtil.sysctl("hw.acpi.battery.life", 100);
/* 57 */     String name = "BAT0";
/* 58 */     ps[0] = new FreeBsdPowerSource(name, life / 100.0D, time == -1 ? -1.0D : state == 2 ? -2.0D : 60.0D * time);
/* 59 */     return ps;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdPowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */