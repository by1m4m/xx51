/*     */ package oshi.util.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.mac.CoreFoundation;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFMutableDictionaryRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFStringRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFTypeRef;
/*     */ import oshi.jna.platform.mac.IOKit;
/*     */ import oshi.jna.platform.mac.IOKit.MachPort;
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
/*     */ 
/*     */ 
/*     */ public class IOKitUtil
/*     */ {
/*  40 */   private static final Logger LOG = LoggerFactory.getLogger(IOKitUtil.class);
/*     */   
/*  42 */   private static IOKit.MachPort masterPort = new IOKit.MachPort();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int setMasterPort()
/*     */   {
/*  53 */     if (masterPort.getValue() == 0) {
/*  54 */       int result = IOKit.INSTANCE.IOMasterPort(0, masterPort);
/*  55 */       if (result != 0) {
/*  56 */         LOG.error(String.format("Error: IOMasterPort() = %08x", new Object[] { Integer.valueOf(result) }));
/*  57 */         return result;
/*     */       }
/*     */     }
/*  60 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getRoot()
/*     */   {
/*  69 */     if (setMasterPort() == 0) {
/*  70 */       int root = IOKit.INSTANCE.IORegistryGetRootEntry(masterPort.getValue());
/*  71 */       if (root == 0) {
/*  72 */         LOG.error("No IO Root found.");
/*     */       }
/*  74 */       return root;
/*     */     }
/*  76 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getMatchingService(String serviceName)
/*     */   {
/*  87 */     if (setMasterPort() == 0) {
/*  88 */       int service = IOKit.INSTANCE.IOServiceGetMatchingService(masterPort.getValue(), IOKit.INSTANCE
/*  89 */         .IOServiceMatching(serviceName));
/*  90 */       if (service == 0) {
/*  91 */         LOG.error("No service found: {}", serviceName);
/*     */       }
/*  93 */       return service;
/*     */     }
/*  95 */     return 0;
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
/*     */   public static int getMatchingServices(String serviceName, IntByReference serviceIterator)
/*     */   {
/* 108 */     int setMasterPort = setMasterPort();
/* 109 */     if (setMasterPort == 0) {
/* 110 */       return IOKit.INSTANCE.IOServiceGetMatchingServices(masterPort.getValue(), IOKit.INSTANCE
/* 111 */         .IOServiceMatching(serviceName), serviceIterator);
/*     */     }
/* 113 */     return setMasterPort;
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
/*     */   public static int getMatchingServices(CoreFoundation.CFMutableDictionaryRef matchingDictionary, IntByReference serviceIterator)
/*     */   {
/* 126 */     int setMasterPort = setMasterPort();
/* 127 */     if (setMasterPort == 0) {
/* 128 */       return IOKit.INSTANCE.IOServiceGetMatchingServices(masterPort.getValue(), matchingDictionary, serviceIterator);
/*     */     }
/*     */     
/* 131 */     return setMasterPort;
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
/*     */   public static String getIORegistryStringProperty(int entry, String key)
/*     */   {
/* 144 */     String value = null;
/* 145 */     CoreFoundation.CFStringRef keyAsCFString = CfUtil.getCFString(key);
/* 146 */     CoreFoundation.CFTypeRef valueAsCFString = IOKit.INSTANCE.IORegistryEntryCreateCFProperty(entry, keyAsCFString, CoreFoundation.INSTANCE
/* 147 */       .CFAllocatorGetDefault(), 0);
/* 148 */     if ((valueAsCFString != null) && (valueAsCFString.getPointer() != null)) {
/* 149 */       value = CfUtil.cfPointerToString(valueAsCFString.getPointer());
/*     */     }
/* 151 */     CfUtil.release(valueAsCFString);
/* 152 */     return value;
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
/*     */   public static long getIORegistryLongProperty(int entry, String key)
/*     */   {
/* 165 */     long value = 0L;
/* 166 */     CoreFoundation.CFStringRef keyAsCFString = CfUtil.getCFString(key);
/* 167 */     CoreFoundation.CFTypeRef valueAsCFNumber = IOKit.INSTANCE.IORegistryEntryCreateCFProperty(entry, keyAsCFString, CoreFoundation.INSTANCE
/* 168 */       .CFAllocatorGetDefault(), 0);
/* 169 */     if ((valueAsCFNumber != null) && (valueAsCFNumber.getPointer() != null)) {
/* 170 */       value = CfUtil.cfPointerToLong(valueAsCFNumber.getPointer());
/*     */     }
/* 172 */     CfUtil.release(valueAsCFNumber);
/* 173 */     return value;
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
/*     */   public static int getIORegistryIntProperty(int entry, String key)
/*     */   {
/* 186 */     int value = 0;
/* 187 */     CoreFoundation.CFStringRef keyAsCFString = CfUtil.getCFString(key);
/* 188 */     CoreFoundation.CFTypeRef valueAsCFNumber = IOKit.INSTANCE.IORegistryEntryCreateCFProperty(entry, keyAsCFString, CoreFoundation.INSTANCE
/* 189 */       .CFAllocatorGetDefault(), 0);
/* 190 */     if ((valueAsCFNumber != null) && (valueAsCFNumber.getPointer() != null)) {
/* 191 */       value = CfUtil.cfPointerToInt(valueAsCFNumber.getPointer());
/*     */     }
/* 193 */     CfUtil.release(valueAsCFNumber);
/* 194 */     return value;
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
/*     */   public static boolean getIORegistryBooleanProperty(int entry, String key)
/*     */   {
/* 207 */     boolean value = false;
/* 208 */     CoreFoundation.CFStringRef keyAsCFString = CfUtil.getCFString(key);
/* 209 */     CoreFoundation.CFTypeRef valueAsCFBoolean = IOKit.INSTANCE.IORegistryEntryCreateCFProperty(entry, keyAsCFString, CoreFoundation.INSTANCE
/* 210 */       .CFAllocatorGetDefault(), 0);
/* 211 */     if ((valueAsCFBoolean != null) && (valueAsCFBoolean.getPointer() != null)) {
/* 212 */       value = CfUtil.cfPointerToBoolean(valueAsCFBoolean.getPointer());
/*     */     }
/* 214 */     CfUtil.release(valueAsCFBoolean);
/* 215 */     return value;
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
/*     */   public static byte[] getIORegistryByteArrayProperty(int entry, String key)
/*     */   {
/* 228 */     byte[] value = null;
/* 229 */     CoreFoundation.CFStringRef keyAsCFString = CfUtil.getCFString(key);
/* 230 */     CoreFoundation.CFTypeRef valueAsCFData = IOKit.INSTANCE.IORegistryEntryCreateCFProperty(entry, keyAsCFString, CoreFoundation.INSTANCE
/* 231 */       .CFAllocatorGetDefault(), 0);
/* 232 */     if ((valueAsCFData != null) && (valueAsCFData.getPointer() != null)) {
/* 233 */       int length = CoreFoundation.INSTANCE.CFDataGetLength(valueAsCFData);
/* 234 */       PointerByReference p = CoreFoundation.INSTANCE.CFDataGetBytePtr(valueAsCFData);
/* 235 */       value = p.getPointer().getByteArray(0L, length);
/*     */     }
/* 237 */     CfUtil.release(valueAsCFData);
/* 238 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CoreFoundation.CFMutableDictionaryRef getBSDNameMatchingDict(String bsdName)
/*     */   {
/* 249 */     if (setMasterPort() == 0) {
/* 250 */       return IOKit.INSTANCE.IOBSDNameMatching(masterPort.getValue(), 0, bsdName);
/*     */     }
/* 252 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\platform\mac\IOKitUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */