/*    */ package oshi.software.os.windows;
/*    */ 
/*    */ import com.sun.jna.platform.win32.Kernel32;
/*    */ import com.sun.jna.platform.win32.WinBase.SYSTEM_INFO;
/*    */ import com.sun.jna.platform.win32.WinDef.DWORD;
/*    */ import com.sun.jna.platform.win32.WinNT.HANDLE;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class WindowsOSSystemInfo
/*    */ {
/* 35 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsOSSystemInfo.class);
/*    */   
/* 37 */   private WinBase.SYSTEM_INFO _si = null;
/*    */   
/*    */   public WindowsOSSystemInfo()
/*    */   {
/* 41 */     WinBase.SYSTEM_INFO si = new WinBase.SYSTEM_INFO();
/* 42 */     Kernel32.INSTANCE.GetSystemInfo(si);
/*    */     try
/*    */     {
/* 45 */       IntByReference isWow64 = new IntByReference();
/* 46 */       WinNT.HANDLE hProcess = Kernel32.INSTANCE.GetCurrentProcess();
/* 47 */       if ((Kernel32.INSTANCE.IsWow64Process(hProcess, isWow64)) && (isWow64.getValue() > 0)) {
/* 48 */         Kernel32.INSTANCE.GetNativeSystemInfo(si);
/*    */       }
/*    */     }
/*    */     catch (UnsatisfiedLinkError e) {
/* 52 */       LOG.trace("", e);
/*    */     }
/*    */     
/* 55 */     this._si = si;
/* 56 */     LOG.debug("Initialized OSNativeSystemInfo");
/*    */   }
/*    */   
/*    */   public WindowsOSSystemInfo(WinBase.SYSTEM_INFO si) {
/* 60 */     this._si = si;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getNumberOfProcessors()
/*    */   {
/* 69 */     return this._si.dwNumberOfProcessors.intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\windows\WindowsOSSystemInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */