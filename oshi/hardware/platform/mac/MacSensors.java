/*     */ package oshi.hardware.platform.mac;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import oshi.hardware.Sensors;
/*     */ import oshi.util.platform.mac.SmcUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacSensors
/*     */   implements Sensors
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  32 */   private double lastTemp = 0.0D;
/*     */   
/*     */   private long lastTempTime;
/*     */   
/*  36 */   private int numFans = 0;
/*     */   
/*  38 */   private int[] lastFanSpeeds = new int[0];
/*     */   
/*     */   private long lastFanSpeedsTime;
/*     */   
/*  42 */   private double lastVolts = 0.0D;
/*     */   private long lastVoltsTime;
/*     */   
/*     */   public MacSensors()
/*     */   {
/*  47 */     SmcUtil.smcOpen();
/*     */     
/*     */ 
/*  50 */     this.lastTemp = getCpuTemperature();
/*  51 */     this.lastFanSpeeds = getFanSpeeds();
/*     */     
/*  53 */     Runtime.getRuntime().addShutdownHook(new Thread()
/*     */     {
/*     */       public void run() {
/*  56 */         SmcUtil.smcClose();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCpuTemperature()
/*     */   {
/*  67 */     if (System.currentTimeMillis() - this.lastTempTime > 900L) {
/*  68 */       double temp = SmcUtil.smcGetSp78("TC0P", 50);
/*  69 */       if (temp > 0.0D) {
/*  70 */         this.lastTemp = temp;
/*  71 */         this.lastTempTime = System.currentTimeMillis();
/*     */       }
/*     */     }
/*  74 */     return this.lastTemp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getFanSpeeds()
/*     */   {
/*  83 */     if (System.currentTimeMillis() - this.lastFanSpeedsTime > 900L)
/*     */     {
/*  85 */       if (this.numFans == 0) {
/*  86 */         this.numFans = ((int)SmcUtil.smcGetLong("FNum", 50));
/*  87 */         this.lastFanSpeeds = new int[this.numFans];
/*     */       }
/*  89 */       for (int i = 0; i < this.numFans; i++) {
/*  90 */         int speed = (int)SmcUtil.smcGetFpe2(String.format("F%dAc", new Object[] { Integer.valueOf(i) }), 50);
/*  91 */         if (speed > 0) {
/*  92 */           this.lastFanSpeeds[i] = speed;
/*  93 */           this.lastFanSpeedsTime = System.currentTimeMillis();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  98 */     return Arrays.copyOf(this.lastFanSpeeds, this.lastFanSpeeds.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCpuVoltage()
/*     */   {
/* 107 */     if (System.currentTimeMillis() - this.lastVoltsTime > 900L) {
/* 108 */       double kiloVolts = SmcUtil.smcGetFpe2("VC0C", 50);
/* 109 */       if (kiloVolts > 0.0D) {
/* 110 */         this.lastVolts = (kiloVolts / 1000.0D);
/* 111 */         this.lastVoltsTime = System.currentTimeMillis();
/*     */       }
/*     */     }
/* 114 */     return this.lastVolts;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacSensors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */