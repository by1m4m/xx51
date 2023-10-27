/*    */ package oshi.hardware.platform.unix.freebsd;
/*    */ 
/*    */ import com.sun.jna.Memory;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import oshi.hardware.Sensors;
/*    */ import oshi.jna.platform.unix.freebsd.Libc;
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
/*    */ public class FreeBsdSensors
/*    */   implements Sensors
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public double getCpuTemperature()
/*    */   {
/* 38 */     double sumTemp = 0.0D;
/* 39 */     int cpu = 0;
/* 40 */     String name = "dev.cpu.%d.temperature";
/*    */     for (;;) {
/* 42 */       IntByReference size = new IntByReference(Libc.INT_SIZE);
/* 43 */       Pointer p = new Memory(size.getValue());
/* 44 */       if (0 != Libc.INSTANCE.sysctlbyname(String.format(name, new Object[] { Integer.valueOf(cpu) }), p, size, null, 0)) {
/*    */         break;
/*    */       }
/* 47 */       sumTemp += p.getInt(0L) / 10.0D - 273.15D;
/* 48 */       cpu++;
/*    */     }
/* 50 */     if (cpu > 0) {
/* 51 */       return sumTemp / cpu;
/*    */     }
/*    */     
/* 54 */     return 0.0D;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getFanSpeeds()
/*    */   {
/* 63 */     return new int[0];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getCpuVoltage()
/*    */   {
/* 72 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdSensors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */