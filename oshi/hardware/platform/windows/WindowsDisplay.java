/*    */ package oshi.hardware.platform.windows;
/*    */ 
/*    */ import com.sun.jna.platform.win32.Advapi32;
/*    */ import com.sun.jna.platform.win32.Guid.GUID;
/*    */ import com.sun.jna.platform.win32.SetupApi;
/*    */ import com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA;
/*    */ import com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA;
/*    */ import com.sun.jna.platform.win32.WinNT;
/*    */ import com.sun.jna.platform.win32.WinNT.HANDLE;
/*    */ import com.sun.jna.platform.win32.WinReg.HKEY;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.Display;
/*    */ import oshi.hardware.common.AbstractDisplay;
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
/*    */ 
/*    */ public class WindowsDisplay
/*    */   extends AbstractDisplay
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 49 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsDisplay.class);
/*    */   
/*    */   public WindowsDisplay(byte[] edid) {
/* 52 */     super(edid);
/* 53 */     LOG.debug("Initialized WindowsDisplay");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Display[] getDisplays()
/*    */   {
/* 62 */     List<Display> displays = new ArrayList();
/*    */     
/* 64 */     Guid.GUID monitorGuid = new Guid.GUID("E6F07B5F-EE97-4a90-B076-33F57BF4EAA7");
/* 65 */     WinNT.HANDLE hDevInfo = SetupApi.INSTANCE.SetupDiGetClassDevs(monitorGuid, null, null, 18);
/*    */     
/* 67 */     if (!hDevInfo.equals(WinNT.INVALID_HANDLE_VALUE)) {
/* 68 */       SetupApi.SP_DEVICE_INTERFACE_DATA deviceInterfaceData = new SetupApi.SP_DEVICE_INTERFACE_DATA();
/* 69 */       deviceInterfaceData.cbSize = deviceInterfaceData.size();
/*    */       
/*    */ 
/* 72 */       SetupApi.SP_DEVINFO_DATA info = new SetupApi.SP_DEVINFO_DATA();
/*    */       
/* 74 */       for (int memberIndex = 0; SetupApi.INSTANCE.SetupDiEnumDeviceInfo(hDevInfo, memberIndex, info); 
/* 75 */           memberIndex++) {
/* 76 */         WinReg.HKEY key = SetupApi.INSTANCE.SetupDiOpenDevRegKey(hDevInfo, info, 1, 0, 1, 1);
/*    */         
/*    */ 
/* 79 */         byte[] edid = new byte[1];
/* 80 */         Advapi32 advapi32 = Advapi32.INSTANCE;
/* 81 */         IntByReference pType = new IntByReference();
/* 82 */         IntByReference lpcbData = new IntByReference();
/*    */         
/* 84 */         if (advapi32.RegQueryValueEx(key, "EDID", 0, pType, edid, lpcbData) == 234) {
/* 85 */           edid = new byte[lpcbData.getValue()];
/* 86 */           if (advapi32.RegQueryValueEx(key, "EDID", 0, pType, edid, lpcbData) == 0) {
/* 87 */             Display display = new WindowsDisplay(edid);
/* 88 */             displays.add(display);
/*    */           }
/*    */         }
/* 91 */         Advapi32.INSTANCE.RegCloseKey(key);
/*    */       }
/*    */     }
/* 94 */     return (Display[])displays.toArray(new Display[displays.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsDisplay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */