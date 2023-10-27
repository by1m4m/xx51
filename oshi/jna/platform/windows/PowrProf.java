/*    */ package oshi.jna.platform.windows;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.Structure;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public abstract interface PowrProf
/*    */   extends Library
/*    */ {
/* 37 */   public static final PowrProf INSTANCE = (PowrProf)Native.loadLibrary("PowrProf", PowrProf.class);
/*    */   
/*    */   public static final int SYSTEM_BATTERY_STATE = 5;
/*    */   
/*    */   public abstract int CallNtPowerInformation(int paramInt, Pointer paramPointer, NativeLong paramNativeLong1, Structure paramStructure, NativeLong paramNativeLong2);
/*    */   
/*    */   public static class SystemBatteryState
/*    */     extends Structure
/*    */   {
/*    */     public byte acOnLine;
/*    */     public byte batteryPresent;
/*    */     public byte charging;
/*    */     public byte discharging;
/* 50 */     public byte[] spare1 = new byte[4];
/*    */     
/*    */     public int maxCapacity;
/*    */     
/*    */     public int remainingCapacity;
/*    */     
/*    */     public int rate;
/*    */     
/*    */     public int estimatedTime;
/*    */     
/*    */     public int defaultAlert1;
/*    */     
/*    */     public int defaultAlert2;
/*    */     
/*    */     protected List<String> getFieldOrder()
/*    */     {
/* 66 */       return Arrays.asList(new String[] { "acOnLine", "batteryPresent", "charging", "discharging", "spare1", "maxCapacity", "remainingCapacity", "rate", "estimatedTime", "defaultAlert1", "defaultAlert2" });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\PowrProf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */