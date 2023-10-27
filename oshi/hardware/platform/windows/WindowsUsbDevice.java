/*     */ package oshi.hardware.platform.windows;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.NativeLong;
/*     */ import com.sun.jna.platform.win32.SetupApi;
/*     */ import com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA;
/*     */ import com.sun.jna.platform.win32.WinNT;
/*     */ import com.sun.jna.platform.win32.WinNT.HANDLE;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.NativeLongByReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.UsbDevice;
/*     */ import oshi.hardware.common.AbstractUsbDevice;
/*     */ import oshi.hardware.platform.mac.MacUsbDevice;
/*     */ import oshi.jna.platform.windows.Cfgmgr32;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.windows.WmiUtil;
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
/*     */ public class WindowsUsbDevice
/*     */   extends AbstractUsbDevice
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  51 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsUsbDevice.class);
/*     */   
/*     */ 
/*  54 */   private static final Pattern VENDOR_PRODUCT_ID = Pattern.compile(".*(?:VID|VEN)_(\\p{XDigit}{4})&(?:PID|DEV)_(\\p{XDigit}{4}).*");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private static Map<String, String> nameMap = new HashMap();
/*  60 */   private static Map<String, String> vendorMap = new HashMap();
/*  61 */   private static Map<String, String> serialMap = new HashMap();
/*  62 */   private static Map<String, List<String>> hubMap = new HashMap();
/*     */   
/*     */   public WindowsUsbDevice(String name, String vendor, String vendorId, String productId, String serialNumber, UsbDevice[] connectedDevices)
/*     */   {
/*  66 */     super(name, vendor, vendorId, productId, serialNumber, connectedDevices);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static UsbDevice[] getUsbDevices(boolean tree)
/*     */   {
/*  73 */     UsbDevice[] devices = getUsbDevices();
/*  74 */     if (tree) {
/*  75 */       return devices;
/*     */     }
/*  77 */     List<UsbDevice> deviceList = new ArrayList();
/*     */     
/*     */ 
/*  80 */     for (UsbDevice device : devices) {
/*  81 */       addDevicesToList(deviceList, device.getConnectedDevices());
/*     */     }
/*  83 */     return (UsbDevice[])deviceList.toArray(new UsbDevice[deviceList.size()]);
/*     */   }
/*     */   
/*     */   private static void addDevicesToList(List<UsbDevice> deviceList, UsbDevice[] connectedDevices) {
/*  87 */     for (UsbDevice device : connectedDevices) {
/*  88 */       deviceList.add(new WindowsUsbDevice(device.getName(), device.getVendor(), device.getVendorId(), device
/*  89 */         .getProductId(), device.getSerialNumber(), new MacUsbDevice[0]));
/*  90 */       addDevicesToList(deviceList, device.getConnectedDevices());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static UsbDevice[] getUsbDevices()
/*     */   {
/* 101 */     nameMap.clear();
/* 102 */     vendorMap.clear();
/* 103 */     serialMap.clear();
/* 104 */     hubMap.clear();
/*     */     
/*     */ 
/* 107 */     Map<String, List<String>> usbMap = WmiUtil.selectStringsFrom(null, "Win32_PnPEntity", "Name,Manufacturer,PnPDeviceID", null);
/*     */     String pnpDeviceID;
/* 109 */     for (int i = 0; i < ((List)usbMap.get("Name")).size(); i++) {
/* 110 */       pnpDeviceID = (String)((List)usbMap.get("PnPDeviceID")).get(i);
/* 111 */       nameMap.put(pnpDeviceID, ((List)usbMap.get("Name")).get(i));
/* 112 */       if (((String)((List)usbMap.get("Manufacturer")).get(i)).length() > 0) {
/* 113 */         vendorMap.put(pnpDeviceID, ((List)usbMap.get("Manufacturer")).get(i));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 118 */     usbMap = WmiUtil.selectStringsFrom(null, "Win32_DiskDrive", "PNPDeviceID,SerialNumber", null);
/* 119 */     for (int i = 0; i < ((List)usbMap.get("PNPDeviceID")).size(); i++) {
/* 120 */       serialMap.put(((List)usbMap.get("PNPDeviceID")).get(i), 
/* 121 */         ParseUtil.hexStringToString((String)((List)usbMap.get("PNPDeviceID")).get(i)));
/*     */     }
/* 123 */     usbMap = WmiUtil.selectStringsFrom(null, "Win32_PhysicalMedia", "PNPDeviceID,SerialNumber", null);
/* 124 */     for (int i = 0; i < ((List)usbMap.get("PNPDeviceID")).size(); i++) {
/* 125 */       serialMap.put(((List)usbMap.get("PNPDeviceID")).get(i), 
/* 126 */         ParseUtil.hexStringToString((String)((List)usbMap.get("PNPDeviceID")).get(i)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 131 */     usbMap = WmiUtil.selectStringsFrom(null, "Win32_USBController", "PNPDeviceID", null);
/* 132 */     List<UsbDevice> controllerDevices = new ArrayList();
/* 133 */     for (String controllerDeviceId : (List)usbMap.get("PNPDeviceID")) {
/* 134 */       putChildrenInDeviceTree(controllerDeviceId, 0);
/* 135 */       controllerDevices.add(getDeviceAndChildren(controllerDeviceId, "0000", "0000"));
/*     */     }
/* 137 */     return (UsbDevice[])controllerDevices.toArray(new UsbDevice[controllerDevices.size()]);
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
/*     */   private static void putChildrenInDeviceTree(String deviceId, int deviceInstance)
/*     */   {
/* 151 */     int devInst = deviceInstance;
/*     */     
/* 153 */     if (devInst == 0)
/*     */     {
/*     */ 
/* 156 */       WinNT.HANDLE hinfoSet = SetupApi.INSTANCE.SetupDiGetClassDevs(null, null, null, 4);
/* 157 */       if (hinfoSet == WinNT.INVALID_HANDLE_VALUE) {
/* 158 */         LOG.error("Invalid handle value for {}. Error code: {}", deviceId, Integer.valueOf(Native.getLastError()));
/* 159 */         return;
/*     */       }
/*     */       
/* 162 */       SetupApi.SP_DEVINFO_DATA dinfo = new SetupApi.SP_DEVINFO_DATA();
/* 163 */       dinfo.cbSize = dinfo.size();
/* 164 */       int i = 0;
/* 165 */       while (SetupApi.INSTANCE.SetupDiEnumDeviceInfo(hinfoSet, i++, dinfo)) {
/* 166 */         if (deviceId.equals(getDeviceId(dinfo.DevInst))) {
/* 167 */           devInst = dinfo.DevInst;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 172 */     if (devInst == 0) {
/* 173 */       LOG.error("Unable to find a devnode handle for {}.", deviceId);
/* 174 */       return;
/*     */     }
/*     */     
/* 177 */     IntByReference child = new IntByReference();
/* 178 */     if (0 == Cfgmgr32.INSTANCE.CM_Get_Child(child, devInst, 0))
/*     */     {
/* 180 */       List<String> childList = new ArrayList();
/* 181 */       String childId = getDeviceId(child.getValue());
/* 182 */       childList.add(childId);
/* 183 */       hubMap.put(deviceId, childList);
/* 184 */       putChildrenInDeviceTree(childId, child.getValue());
/*     */       
/* 186 */       IntByReference sibling = new IntByReference();
/* 187 */       while (0 == Cfgmgr32.INSTANCE.CM_Get_Sibling(sibling, child.getValue(), 0))
/*     */       {
/* 189 */         String siblingId = getDeviceId(sibling.getValue());
/* 190 */         ((List)hubMap.get(deviceId)).add(siblingId);
/* 191 */         putChildrenInDeviceTree(siblingId, sibling.getValue());
/*     */         
/* 193 */         child = sibling;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getDeviceId(int devInst)
/*     */   {
/* 206 */     NativeLongByReference ulLen = new NativeLongByReference();
/* 207 */     if (0 != Cfgmgr32.INSTANCE.CM_Get_Device_ID_Size(ulLen, devInst, 0)) {
/* 208 */       LOG.error("Couldn't get device string for device instance {}", Integer.valueOf(devInst));
/* 209 */       return "";
/*     */     }
/*     */     
/* 212 */     int size = ulLen.getValue().intValue() + 1;
/* 213 */     char[] buffer = new char[size];
/* 214 */     if (0 != Cfgmgr32.INSTANCE.CM_Get_Device_ID(devInst, buffer, size, 0)) {
/* 215 */       LOG.error("Couldn't get device string for device instance {} with size {}", Integer.valueOf(devInst), Integer.valueOf(size));
/* 216 */       return "";
/*     */     }
/* 218 */     return new String(buffer).trim();
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
/*     */   private static WindowsUsbDevice getDeviceAndChildren(String hubDeviceId, String vid, String pid)
/*     */   {
/* 234 */     String vendorId = vid;
/* 235 */     String productId = pid;
/* 236 */     Matcher m = VENDOR_PRODUCT_ID.matcher(hubDeviceId);
/* 237 */     if (m.matches()) {
/* 238 */       vendorId = m.group(1).toLowerCase();
/* 239 */       productId = m.group(2).toLowerCase();
/*     */     }
/* 241 */     List<String> pnpDeviceIds = (List)hubMap.getOrDefault(hubDeviceId, new ArrayList());
/* 242 */     List<WindowsUsbDevice> usbDevices = new ArrayList();
/* 243 */     for (String pnpDeviceId : pnpDeviceIds) {
/* 244 */       usbDevices.add(getDeviceAndChildren(pnpDeviceId, vendorId, productId));
/*     */     }
/* 246 */     Collections.sort(usbDevices);
/*     */     
/*     */ 
/* 249 */     return new WindowsUsbDevice((String)nameMap.getOrDefault(hubDeviceId, vendorId + ":" + productId), (String)vendorMap.getOrDefault(hubDeviceId, ""), vendorId, productId, (String)serialMap.getOrDefault(hubDeviceId, ""), (UsbDevice[])usbDevices.toArray(new UsbDevice[usbDevices.size()]));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsUsbDevice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */