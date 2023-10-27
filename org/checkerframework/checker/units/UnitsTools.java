/*     */ package org.checkerframework.checker.units;
/*     */ 
/*     */ 
/*     */ public class UnitsTools
/*     */ {
/*     */   public static final int mPERs2 = 1;
/*     */   
/*     */   public static final double rad = 1.0D;
/*     */   
/*     */   public static final double deg = 1.0D;
/*     */   
/*     */   public static final int mm2 = 1;
/*     */   
/*     */   public static final int m2 = 1;
/*     */   
/*     */   public static final int km2 = 1;
/*     */   
/*     */   public static final int A = 1;
/*     */   
/*     */   public static final int cd = 1;
/*     */   
/*     */   public static final int mm = 1;
/*     */   public static final int m = 1;
/*     */   public static final int km = 1;
/*     */   public static final int g = 1;
/*     */   public static final int kg = 1;
/*     */   public static final int mPERs = 1;
/*     */   public static final int kmPERh = 1;
/*     */   public static final int mol = 1;
/*     */   public static final int K = 1;
/*     */   public static final int C = 1;
/*     */   public static final int s = 1;
/*     */   public static final int min = 1;
/*     */   public static final int h = 1;
/*     */   
/*     */   public static double toRadians(double angdeg)
/*     */   {
/*  38 */     return Math.toRadians(angdeg);
/*     */   }
/*     */   
/*     */   public static double toDegrees(double angrad) {
/*  42 */     return Math.toDegrees(angrad);
/*     */   }
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
/*     */   public static int fromMilliMeterToMeter(int mm)
/*     */   {
/*  62 */     return mm / 1000;
/*     */   }
/*     */   
/*     */   public static int fromMeterToMilliMeter(int m) {
/*  66 */     return m * 1000;
/*     */   }
/*     */   
/*     */   public static int fromMeterToKiloMeter(int m) {
/*  70 */     return m / 1000;
/*     */   }
/*     */   
/*     */   public static int fromKiloMeterToMeter(int km) {
/*  74 */     return km * 1000;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int fromGramToKiloGram(int g)
/*     */   {
/*  82 */     return g / 1000;
/*     */   }
/*     */   
/*     */   public static int fromKiloGramToGram(int kg) {
/*  86 */     return kg * 1000;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double fromMeterPerSecondToKiloMeterPerHour(double mps)
/*     */   {
/*  94 */     return mps * 3.6D;
/*     */   }
/*     */   
/*     */   public static double fromKiloMeterPerHourToMeterPerSecond(double kmph) {
/*  98 */     return kmph / 3.6D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int fromKelvinToCelsius(int k)
/*     */   {
/* 109 */     return k - 273;
/*     */   }
/*     */   
/*     */   public static int fromCelsiusToKelvin(int c) {
/* 113 */     return c + 273;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int fromSecondToMinute(int s)
/*     */   {
/* 122 */     return s / 60;
/*     */   }
/*     */   
/*     */   public static int fromMinuteToSecond(int min) {
/* 126 */     return min * 60;
/*     */   }
/*     */   
/*     */   public static int fromMinuteToHour(int min) {
/* 130 */     return min / 60;
/*     */   }
/*     */   
/*     */   public static int fromHourToMinute(int h) {
/* 134 */     return h * 60;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\units\UnitsTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */