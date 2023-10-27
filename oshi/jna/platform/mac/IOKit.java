/*     */ package oshi.jna.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Library;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.Structure;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.LongByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface IOKit
/*     */   extends Library
/*     */ {
/*  46 */   public static final IOKit INSTANCE = (IOKit)Native.loadLibrary("IOKit", IOKit.class);
/*     */   
/*  48 */   public static final CoreFoundation.CFStringRef IOPS_NAME_KEY = CoreFoundation.CFStringRef.toCFString("Name");
/*     */   
/*  50 */   public static final CoreFoundation.CFStringRef IOPS_IS_PRESENT_KEY = CoreFoundation.CFStringRef.toCFString("Is Present");
/*     */   
/*  52 */   public static final CoreFoundation.CFStringRef IOPS_CURRENT_CAPACITY_KEY = CoreFoundation.CFStringRef.toCFString("Current Capacity");
/*     */   
/*  54 */   public static final CoreFoundation.CFStringRef IOPS_MAX_CAPACITY_KEY = CoreFoundation.CFStringRef.toCFString("Max Capacity");
/*     */   public static final String SMC_KEY_FAN_NUM = "FNum";
/*     */   public static final String SMC_KEY_FAN_SPEED = "F%dAc";
/*     */   public static final String SMC_KEY_CPU_TEMP = "TC0P";
/*     */   
/*     */   public abstract CoreFoundation.CFTypeRef IOPSCopyPowerSourcesInfo();
/*     */   
/*     */   public abstract CoreFoundation.CFArrayRef IOPSCopyPowerSourcesList(CoreFoundation.CFTypeRef paramCFTypeRef);
/*     */   
/*     */   public abstract CoreFoundation.CFDictionaryRef IOPSGetPowerSourceDescription(CoreFoundation.CFTypeRef paramCFTypeRef1, CoreFoundation.CFTypeRef paramCFTypeRef2);
/*     */   
/*     */   public abstract double IOPSGetTimeRemainingEstimate();
/*     */   
/*     */   public abstract int IOMasterPort(int paramInt, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract CoreFoundation.CFMutableDictionaryRef IOServiceMatching(String paramString);
/*     */   
/*     */   public static class SMCKeyDataVers
/*     */     extends Structure
/*     */   {
/*     */     public byte major;
/*     */     public byte minor;
/*     */     public byte build;
/*  77 */     public byte[] reserved = new byte[1];
/*     */     
/*     */     public short release;
/*     */     
/*     */ 
/*  82 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "major", "minor", "build", "reserved", "release" }); } }
/*     */   
/*     */   public abstract CoreFoundation.CFMutableDictionaryRef IOBSDNameMatching(int paramInt1, int paramInt2, String paramString);
/*     */   
/*     */   public abstract int IOServiceGetMatchingService(int paramInt, CoreFoundation.CFMutableDictionaryRef paramCFMutableDictionaryRef);
/*     */   
/*     */   public abstract int IOServiceGetMatchingServices(int paramInt, CoreFoundation.CFMutableDictionaryRef paramCFMutableDictionaryRef, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int IOServiceOpen(int paramInt1, int paramInt2, int paramInt3, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int IOServiceClose(int paramInt);
/*     */   
/*     */   public abstract void IOObjectRelease(int paramInt);
/*     */   
/*     */   public static class SMCKeyDataPLimitData extends Structure { public short version;
/*     */     
/*  98 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "version", "length", "cpuPLimit", "gpuPLimit", "memPLimit" }); }
/*     */     
/*     */     public short length;
/*     */     public int cpuPLimit;
/*     */     public int gpuPLimit;
/*     */     public int memPLimit;
/*     */   }
/*     */   
/*     */   public static class SMCKeyDataKeyInfo
/*     */     extends Structure {
/*     */     public int dataSize;
/*     */     public int dataType;
/*     */     public byte dataAttributes;
/*     */     
/* 112 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "dataSize", "dataType", "dataAttributes" }); }
/*     */   }
/*     */   
/*     */   public static final String SMC_KEY_CPU_VOLTAGE = "VC0C";
/*     */   public static final byte SMC_CMD_READ_BYTES = 5;
/*     */   public static final byte SMC_CMD_READ_KEYINFO = 9;
/*     */   public static final int KERNEL_INDEX_SMC = 2;
/*     */   public static class SMCKeyData extends Structure {
/*     */     public int key;
/*     */     public IOKit.SMCKeyDataVers vers;
/*     */     public IOKit.SMCKeyDataPLimitData pLimitData;
/*     */     public IOKit.SMCKeyDataKeyInfo keyInfo;
/*     */     public byte result;
/*     */     public byte status;
/*     */     public byte data8;
/*     */     public int data32;
/* 128 */     public byte[] bytes = new byte[32];
/*     */     
/*     */ 
/*     */ 
/* 132 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "key", "vers", "pLimitData", "keyInfo", "result", "status", "data8", "data32", "bytes" }); }
/*     */   }
/*     */   
/*     */   public abstract int IOIteratorNext(int paramInt);
/*     */   
/*     */   public abstract boolean IOObjectConformsTo(int paramInt, String paramString);
/*     */   
/*     */   public abstract int IOConnectCallStructMethod(int paramInt1, int paramInt2, Structure paramStructure1, int paramInt3, Structure paramStructure2, IntByReference paramIntByReference);
/*     */   
/* 141 */   public static class SMCVal extends Structure { public byte[] key = new byte[5];
/*     */     public int dataSize;
/* 143 */     public byte[] dataType = new byte[5];
/* 144 */     public byte[] bytes = new byte[32];
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 148 */       return Arrays.asList(new String[] { "key", "dataSize", "dataType", "bytes" });
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract CoreFoundation.CFTypeRef IORegistryEntryCreateCFProperty(int paramInt1, CoreFoundation.CFStringRef paramCFStringRef, CoreFoundation.CFAllocatorRef paramCFAllocatorRef, int paramInt2);
/*     */   
/*     */   public abstract CoreFoundation.CFTypeRef IORegistryEntrySearchCFProperty(int paramInt1, String paramString, CoreFoundation.CFStringRef paramCFStringRef, CoreFoundation.CFAllocatorRef paramCFAllocatorRef, int paramInt2);
/*     */   
/*     */   public abstract int IORegistryEntryCreateCFProperties(int paramInt1, PointerByReference paramPointerByReference, CoreFoundation.CFAllocatorRef paramCFAllocatorRef, int paramInt2);
/*     */   
/*     */   public abstract CoreFoundation.CFStringRef CFCopyDescription(CoreFoundation.CFTypeRef paramCFTypeRef);
/*     */   
/*     */   public abstract int IORegistryEntryGetName(int paramInt, Pointer paramPointer);
/*     */   
/*     */   public abstract int IORegistryEntryGetRegistryEntryID(int paramInt, LongByReference paramLongByReference);
/*     */   
/*     */   public abstract int IORegistryEntryGetParentEntry(int paramInt, String paramString, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int IORegistryEntryGetChildEntry(int paramInt, String paramString, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int IORegistryEntryGetChildIterator(int paramInt, String paramString, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int IORegistryGetRootEntry(int paramInt);
/*     */   
/*     */   public static class IOConnect
/*     */     extends IntByReference
/*     */   {}
/*     */   
/*     */   public static class MachPort
/*     */     extends IntByReference
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\mac\IOKit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */