/*    */ package oshi.jna.platform.windows;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.platform.win32.WinBase.FILETIME;
/*    */ import com.sun.jna.win32.W32APIOptions;
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
/*    */ public abstract interface Kernel32
/*    */   extends com.sun.jna.platform.win32.Kernel32
/*    */ {
/* 32 */   public static final Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
/*    */   public static final int SEM_FAILCRITICALERRORS = 1;
/*    */   public static final int ComputerNameDnsHostname = 1;
/*    */   public static final int ComputerNameDnsDomain = 2;
/*    */   public static final int ComputerNameDnsDomainFullyQualified = 3;
/*    */   
/*    */   public abstract boolean GetSystemTimes(WinBase.FILETIME paramFILETIME1, WinBase.FILETIME paramFILETIME2, WinBase.FILETIME paramFILETIME3);
/*    */   
/*    */   public abstract long GetTickCount64();
/*    */   
/*    */   public abstract int SetErrorMode(int paramInt);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\Kernel32.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */