/*    */ package oshi.hardware.platform.unix.solaris;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import oshi.hardware.Sensors;
/*    */ import oshi.util.ExecutingCommand;
/*    */ import oshi.util.ParseUtil;
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
/*    */ public class SolarisSensors
/*    */   implements Sensors
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public double getCpuTemperature()
/*    */   {
/* 37 */     double maxTemp = 0.0D;
/*    */     
/* 39 */     for (String line : ExecutingCommand.runNative("/usr/sbin/prtpicl -v -c temperature-sensor")) {
/* 40 */       if (line.trim().startsWith("Temperature:")) {
/* 41 */         int temp = ParseUtil.parseLastInt(line, 0);
/* 42 */         if (temp > maxTemp) {
/* 43 */           maxTemp = temp;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 48 */     if (maxTemp > 1000.0D) {
/* 49 */       maxTemp /= 1000.0D;
/*    */     }
/* 51 */     return maxTemp;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getFanSpeeds()
/*    */   {
/* 59 */     List<Integer> speedList = new ArrayList();
/*    */     
/* 61 */     for (String line : ExecutingCommand.runNative("/usr/sbin/prtpicl -v -c fan")) {
/* 62 */       if (line.trim().startsWith("Speed:")) {
/* 63 */         speedList.add(Integer.valueOf(ParseUtil.parseLastInt(line, 0)));
/*    */       }
/*    */     }
/* 66 */     int[] fans = new int[speedList.size()];
/* 67 */     for (int i = 0; i < speedList.size(); i++) {
/* 68 */       fans[i] = ((Integer)speedList.get(i)).intValue();
/*    */     }
/* 70 */     return fans;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getCpuVoltage()
/*    */   {
/* 78 */     double voltage = 0.0D;
/*    */     
/* 80 */     for (String line : ExecutingCommand.runNative("/usr/sbin/prtpicl -v -c voltage-sensor")) {
/* 81 */       if (line.trim().startsWith("Voltage:")) {
/* 82 */         voltage = ParseUtil.parseDoubleOrDefault(line.replace("Voltage:", "").trim(), 0.0D);
/* 83 */         break;
/*    */       }
/*    */     }
/* 86 */     return voltage;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisSensors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */