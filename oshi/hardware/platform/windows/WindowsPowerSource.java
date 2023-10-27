/*    */ package oshi.hardware.platform.windows;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.PowerSource;
/*    */ import oshi.hardware.common.AbstractPowerSource;
/*    */ import oshi.jna.platform.windows.PowrProf;
/*    */ import oshi.jna.platform.windows.PowrProf.SystemBatteryState;
/*    */ import oshi.util.FormatUtil;
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
/*    */ public class WindowsPowerSource
/*    */   extends AbstractPowerSource
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 41 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsPowerSource.class);
/*    */   
/*    */   public WindowsPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining) {
/* 44 */     super(newName, newRemainingCapacity, newTimeRemaining);
/* 45 */     LOG.debug("Initialized WindowsPowerSource");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static PowerSource[] getPowerSources()
/*    */   {
/* 55 */     String name = "System Battery";
/* 56 */     WindowsPowerSource[] psArray = new WindowsPowerSource[1];
/*    */     
/* 58 */     PowrProf.SystemBatteryState batteryState = new PowrProf.SystemBatteryState();
/* 59 */     if ((0 != PowrProf.INSTANCE.CallNtPowerInformation(5, null, new NativeLong(0L), batteryState, new NativeLong(batteryState
/* 60 */       .size()))) || (batteryState.batteryPresent == 0))
/*    */     {
/* 61 */       psArray[0] = new WindowsPowerSource("Unknown", 0.0D, -1.0D);
/*    */     } else {
/* 63 */       int estimatedTime = -2;
/* 64 */       if ((batteryState.acOnLine == 0) && (batteryState.charging == 0) && (batteryState.discharging > 0)) {
/* 65 */         estimatedTime = batteryState.estimatedTime;
/*    */       }
/* 67 */       long maxCapacity = FormatUtil.getUnsignedInt(batteryState.maxCapacity);
/* 68 */       long remainingCapacity = FormatUtil.getUnsignedInt(batteryState.remainingCapacity);
/*    */       
/* 70 */       psArray[0] = new WindowsPowerSource(name, remainingCapacity / maxCapacity, estimatedTime);
/*    */     }
/*    */     
/* 73 */     return psArray;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsPowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */