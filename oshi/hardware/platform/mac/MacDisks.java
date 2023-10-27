/*     */ package oshi.hardware.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.Disks;
/*     */ import oshi.hardware.HWDiskStore;
/*     */ import oshi.hardware.HWPartition;
/*     */ import oshi.jna.platform.mac.CoreFoundation;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFBooleanRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFDictionaryRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFMutableDictionaryRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFNumberRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFStringRef;
/*     */ import oshi.jna.platform.mac.DiskArbitration;
/*     */ import oshi.jna.platform.mac.DiskArbitration.DADiskRef;
/*     */ import oshi.jna.platform.mac.DiskArbitration.DASessionRef;
/*     */ import oshi.jna.platform.mac.IOKit;
/*     */ import oshi.jna.platform.mac.SystemB;
/*     */ import oshi.jna.platform.mac.SystemB.Statfs;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.mac.CfUtil;
/*     */ import oshi.util.platform.mac.IOKitUtil;
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
/*     */ public class MacDisks
/*     */   implements Disks
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  65 */   private static final Logger LOG = LoggerFactory.getLogger(MacDisks.class);
/*     */   
/*  67 */   private static final Map<String, String> mountPointMap = new HashMap();
/*  68 */   private static final Map<String, String> logicalVolumeMap = new HashMap();
/*     */   
/*     */   public HWDiskStore[] getDisks()
/*     */   {
/*  72 */     mountPointMap.clear();
/*  73 */     logicalVolumeMap.clear();
/*  74 */     List<HWDiskStore> result = new ArrayList();
/*     */     
/*     */ 
/*  77 */     int numfs = SystemB.INSTANCE.getfsstat64(null, 0, 0);
/*     */     
/*  79 */     SystemB.Statfs[] fs = new SystemB.Statfs[numfs];
/*     */     
/*  81 */     SystemB.INSTANCE.getfsstat64(fs, numfs * new SystemB.Statfs().size(), 16);
/*     */     
/*  83 */     for (SystemB.Statfs f : fs) {
/*  84 */       String mntFrom = new String(f.f_mntfromname).trim();
/*  85 */       mountPointMap.put(mntFrom.replace("/dev/", ""), new String(f.f_mntonname).trim());
/*     */     }
/*     */     
/*     */ 
/*  89 */     Object physicalVolumes = new HashSet();
/*  90 */     boolean logicalVolume = false;
/*  91 */     for (String line : ExecutingCommand.runNative("diskutil cs list")) {
/*  92 */       if (line.contains("Logical Volume Group"))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */         ((Set)physicalVolumes).clear();
/*  99 */         logicalVolume = false;
/*     */       }
/* 101 */       else if (line.contains("Logical Volume Family"))
/*     */       {
/*     */ 
/* 104 */         logicalVolume = true;
/*     */       }
/* 106 */       else if (line.contains("Disk:")) {
/* 107 */         String volume = ParseUtil.parseLastString(line);
/* 108 */         if (logicalVolume)
/*     */         {
/*     */ 
/* 111 */           for (String pv : (Set)physicalVolumes) {
/* 112 */             logicalVolumeMap.put(pv, volume);
/*     */           }
/* 114 */           ((Set)physicalVolumes).clear();
/*     */         } else {
/* 116 */           ((Set)physicalVolumes).add(ParseUtil.parseLastString(line));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 122 */     DiskArbitration.DASessionRef session = DiskArbitration.INSTANCE.DASessionCreate(CfUtil.ALLOCATOR);
/* 123 */     if (session == null) {
/* 124 */       LOG.error("Unable to open session to DiskArbitration framework.");
/* 125 */       return new HWDiskStore[0];
/*     */     }
/*     */     
/*     */ 
/* 129 */     List<String> bsdNames = new ArrayList();
/* 130 */     IntByReference iter = new IntByReference();
/* 131 */     IOKitUtil.getMatchingServices("IOMedia", iter);
/* 132 */     int media = IOKit.INSTANCE.IOIteratorNext(iter.getValue());
/* 133 */     DiskArbitration.DADiskRef disk; while (media != 0) {
/* 134 */       if (IOKitUtil.getIORegistryBooleanProperty(media, "Whole")) {
/* 135 */         disk = DiskArbitration.INSTANCE.DADiskCreateFromIOMedia(CfUtil.ALLOCATOR, session, media);
/* 136 */         bsdNames.add(DiskArbitration.INSTANCE.DADiskGetBSDName(disk));
/*     */       }
/* 138 */       IOKit.INSTANCE.IOObjectRelease(media);
/* 139 */       media = IOKit.INSTANCE.IOIteratorNext(iter.getValue());
/*     */     }
/*     */     
/*     */ 
/* 143 */     for (String bsdName : bsdNames) {
/* 144 */       String model = "";
/* 145 */       String serial = "";
/* 146 */       long size = 0L;
/*     */       
/*     */ 
/*     */ 
/* 150 */       String path = "/dev/" + bsdName;
/*     */       
/*     */ 
/*     */ 
/* 154 */       DiskArbitration.DADiskRef disk = DiskArbitration.INSTANCE.DADiskCreateFromBSDName(CfUtil.ALLOCATOR, session, path);
/* 155 */       if (disk != null) {
/* 156 */         CoreFoundation.CFDictionaryRef diskInfo = DiskArbitration.INSTANCE.DADiskCopyDescription(disk);
/* 157 */         if (diskInfo != null)
/*     */         {
/* 159 */           Pointer modelPtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(diskInfo, 
/* 160 */             CfUtil.getCFString("DADeviceModel"));
/* 161 */           model = CfUtil.cfPointerToString(modelPtr);
/* 162 */           Pointer sizePtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(diskInfo, 
/* 163 */             CfUtil.getCFString("DAMediaSize"));
/* 164 */           size = CfUtil.cfPointerToLong(sizePtr);
/* 165 */           CfUtil.release(diskInfo);
/*     */           
/*     */ 
/* 168 */           if (!"Disk Image".equals(model)) {
/* 169 */             CoreFoundation.CFStringRef modelNameRef = CoreFoundation.CFStringRef.toCFString(model);
/*     */             
/* 171 */             CoreFoundation.CFMutableDictionaryRef propertyDict = CoreFoundation.INSTANCE.CFDictionaryCreateMutable(CfUtil.ALLOCATOR, 0, null, null);
/* 172 */             CoreFoundation.INSTANCE.CFDictionarySetValue(propertyDict, CfUtil.getCFString("Model"), modelNameRef);
/*     */             
/*     */ 
/* 175 */             CoreFoundation.CFMutableDictionaryRef matchingDict = CoreFoundation.INSTANCE.CFDictionaryCreateMutable(CfUtil.ALLOCATOR, 0, null, null);
/* 176 */             CoreFoundation.INSTANCE.CFDictionarySetValue(matchingDict, 
/* 177 */               CfUtil.getCFString("IOPropertyMatch"), propertyDict);
/*     */             
/*     */ 
/* 180 */             IntByReference serviceIterator = new IntByReference();
/* 181 */             IOKitUtil.getMatchingServices(matchingDict, serviceIterator);
/*     */             
/* 183 */             CfUtil.release(modelNameRef);
/* 184 */             CfUtil.release(propertyDict);
/* 185 */             int sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator.getValue());
/* 186 */             while (sdService != 0)
/*     */             {
/* 188 */               serial = IOKitUtil.getIORegistryStringProperty(sdService, "Serial Number");
/* 189 */               IOKit.INSTANCE.IOObjectRelease(sdService);
/* 190 */               if (serial != null) {
/*     */                 break;
/*     */               }
/*     */               
/* 194 */               sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator.getValue());
/*     */             }
/* 196 */             if (serial == null) {
/* 197 */               serial = "";
/*     */             }
/* 199 */             IOKit.INSTANCE.IOObjectRelease(serviceIterator.getValue());
/*     */           }
/*     */         }
/* 202 */         CfUtil.release(disk);
/*     */         
/*     */ 
/* 205 */         if (size > 0L)
/*     */         {
/*     */ 
/* 208 */           HWDiskStore diskStore = new HWDiskStore(bsdName, model.trim(), serial.trim(), size, 0L, 0L, 0L, 0L, 0L, new HWPartition[0], 0L);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 213 */           CoreFoundation.CFMutableDictionaryRef matchingDict = IOKitUtil.getBSDNameMatchingDict(bsdName);
/* 214 */           if (matchingDict != null)
/*     */           {
/* 216 */             IntByReference driveList = new IntByReference();
/* 217 */             IOKitUtil.getMatchingServices(matchingDict, driveList);
/*     */             
/* 219 */             int drive = IOKit.INSTANCE.IOIteratorNext(driveList.getValue());
/*     */             
/* 221 */             if (drive != 0)
/*     */             {
/*     */ 
/*     */ 
/* 225 */               IntByReference parent = new IntByReference();
/* 226 */               if ((IOKit.INSTANCE.IOObjectConformsTo(drive, "IOMedia")) && 
/* 227 */                 (IOKit.INSTANCE.IORegistryEntryGetParentEntry(drive, "IOService", parent) == 0)) {
/* 228 */                 PointerByReference propsPtr = new PointerByReference();
/* 229 */                 if ((IOKit.INSTANCE.IOObjectConformsTo(parent.getValue(), "IOBlockStorageDriver")) && 
/* 230 */                   (IOKit.INSTANCE.IORegistryEntryCreateCFProperties(parent.getValue(), propsPtr, CfUtil.ALLOCATOR, 0) == 0))
/*     */                 {
/* 232 */                   CoreFoundation.CFMutableDictionaryRef properties = new CoreFoundation.CFMutableDictionaryRef();
/* 233 */                   properties.setPointer(propsPtr.getValue());
/*     */                   
/*     */ 
/* 236 */                   Pointer statsPtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(properties, 
/* 237 */                     CfUtil.getCFString("Statistics"));
/* 238 */                   diskStore.setTimeStamp(System.currentTimeMillis());
/* 239 */                   CoreFoundation.CFDictionaryRef statistics = new CoreFoundation.CFDictionaryRef();
/* 240 */                   statistics.setPointer(statsPtr);
/*     */                   
/*     */ 
/* 243 */                   Pointer stat = CoreFoundation.INSTANCE.CFDictionaryGetValue(statistics, 
/* 244 */                     CfUtil.getCFString("Operations (Read)"));
/* 245 */                   diskStore.setReads(CfUtil.cfPointerToLong(stat));
/* 246 */                   stat = CoreFoundation.INSTANCE.CFDictionaryGetValue(statistics, 
/* 247 */                     CfUtil.getCFString("Bytes (Read)"));
/* 248 */                   diskStore.setReadBytes(CfUtil.cfPointerToLong(stat));
/*     */                   
/* 250 */                   stat = CoreFoundation.INSTANCE.CFDictionaryGetValue(statistics, 
/* 251 */                     CfUtil.getCFString("Operations (Write)"));
/* 252 */                   diskStore.setWrites(CfUtil.cfPointerToLong(stat));
/* 253 */                   stat = CoreFoundation.INSTANCE.CFDictionaryGetValue(statistics, 
/* 254 */                     CfUtil.getCFString("Bytes (Write)"));
/* 255 */                   diskStore.setWriteBytes(CfUtil.cfPointerToLong(stat));
/*     */                   
/*     */ 
/*     */ 
/* 259 */                   stat = CoreFoundation.INSTANCE.CFDictionaryGetValue(statistics, 
/* 260 */                     CfUtil.getCFString("Total Time (Read)"));
/* 261 */                   long xferTime = CfUtil.cfPointerToLong(stat);
/* 262 */                   stat = CoreFoundation.INSTANCE.CFDictionaryGetValue(statistics, 
/* 263 */                     CfUtil.getCFString("Total Time (Write)"));
/* 264 */                   xferTime += CfUtil.cfPointerToLong(stat);
/* 265 */                   diskStore.setTransferTime(xferTime / 10000L);
/*     */                   
/* 267 */                   CfUtil.release(properties);
/*     */                 }
/*     */                 else
/*     */                 {
/* 271 */                   LOG.debug("Unable to find block storage driver properties for {}", bsdName);
/*     */                 }
/*     */                 
/* 274 */                 List<HWPartition> partitions = new ArrayList();
/* 275 */                 if (IOKit.INSTANCE.IORegistryEntryCreateCFProperties(drive, propsPtr, CfUtil.ALLOCATOR, 0) == 0)
/*     */                 {
/* 277 */                   CoreFoundation.CFMutableDictionaryRef properties = new CoreFoundation.CFMutableDictionaryRef();
/* 278 */                   properties.setPointer(propsPtr.getValue());
/*     */                   
/* 280 */                   Pointer p = CoreFoundation.INSTANCE.CFDictionaryGetValue(properties, 
/* 281 */                     CfUtil.getCFString("BSD Unit"));
/* 282 */                   CoreFoundation.CFNumberRef bsdUnit = new CoreFoundation.CFNumberRef();
/* 283 */                   bsdUnit.setPointer(p);
/*     */                   
/*     */ 
/*     */ 
/* 287 */                   p = CoreFoundation.INSTANCE.CFDictionaryGetValue(properties, 
/* 288 */                     CfUtil.getCFString("Leaf"));
/* 289 */                   CoreFoundation.CFBooleanRef cfFalse = new CoreFoundation.CFBooleanRef();
/* 290 */                   cfFalse.setPointer(p);
/*     */                   
/*     */ 
/* 293 */                   CoreFoundation.CFMutableDictionaryRef propertyDict = CoreFoundation.INSTANCE.CFDictionaryCreateMutable(CfUtil.ALLOCATOR, 0, null, null);
/* 294 */                   CoreFoundation.INSTANCE.CFDictionarySetValue(propertyDict, 
/* 295 */                     CfUtil.getCFString("BSD Unit"), bsdUnit);
/* 296 */                   CoreFoundation.INSTANCE.CFDictionarySetValue(propertyDict, CfUtil.getCFString("Whole"), cfFalse);
/*     */                   
/* 298 */                   matchingDict = CoreFoundation.INSTANCE.CFDictionaryCreateMutable(CfUtil.ALLOCATOR, 0, null, null);
/*     */                   
/* 300 */                   CoreFoundation.INSTANCE.CFDictionarySetValue(matchingDict, 
/* 301 */                     CfUtil.getCFString("IOPropertyMatch"), propertyDict);
/*     */                   
/*     */ 
/*     */ 
/* 305 */                   IntByReference serviceIterator = new IntByReference();
/* 306 */                   IOKitUtil.getMatchingServices(matchingDict, serviceIterator);
/*     */                   
/* 308 */                   CfUtil.release(properties);
/* 309 */                   CfUtil.release(propertyDict);
/* 310 */                   int sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator.getValue());
/* 311 */                   while (sdService != 0)
/*     */                   {
/* 313 */                     String partBsdName = IOKitUtil.getIORegistryStringProperty(sdService, "BSD Name");
/* 314 */                     String name = partBsdName;
/* 315 */                     String type = "";
/*     */                     
/*     */ 
/* 318 */                     disk = DiskArbitration.INSTANCE.DADiskCreateFromBSDName(CfUtil.ALLOCATOR, session, partBsdName);
/*     */                     
/* 320 */                     if (disk != null) {
/* 321 */                       diskInfo = DiskArbitration.INSTANCE.DADiskCopyDescription(disk);
/* 322 */                       if (diskInfo != null)
/*     */                       {
/* 324 */                         Pointer volumePtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(diskInfo, 
/* 325 */                           CfUtil.getCFString("DAMediaName"));
/* 326 */                         type = CfUtil.cfPointerToString(volumePtr);
/* 327 */                         volumePtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(diskInfo, 
/* 328 */                           CfUtil.getCFString("DAVolumeName"));
/* 329 */                         if (volumePtr == null) {
/* 330 */                           name = type;
/*     */                         } else {
/* 332 */                           name = CfUtil.cfPointerToString(volumePtr);
/*     */                         }
/* 334 */                         CfUtil.release(diskInfo);
/*     */                       }
/* 336 */                       CfUtil.release(disk); }
/*     */                     String mountPoint;
/*     */                     String mountPoint;
/* 339 */                     if (logicalVolumeMap.containsKey(partBsdName)) {
/* 340 */                       mountPoint = "Logical Volume: " + (String)logicalVolumeMap.get(partBsdName);
/*     */                     } else {
/* 342 */                       mountPoint = (String)mountPointMap.getOrDefault(partBsdName, "");
/*     */                     }
/* 344 */                     partitions.add(new HWPartition(partBsdName, name, type, 
/* 345 */                       IOKitUtil.getIORegistryStringProperty(sdService, "UUID"), 
/* 346 */                       IOKitUtil.getIORegistryLongProperty(sdService, "Size"), 
/* 347 */                       IOKitUtil.getIORegistryIntProperty(sdService, "BSD Major"), 
/* 348 */                       IOKitUtil.getIORegistryIntProperty(sdService, "BSD Minor"), mountPoint));
/* 349 */                     IOKit.INSTANCE.IOObjectRelease(sdService);
/*     */                     
/* 351 */                     sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator.getValue());
/*     */                   }
/* 353 */                   IOKit.INSTANCE.IOObjectRelease(serviceIterator.getValue());
/*     */                 }
/*     */                 else {
/* 356 */                   LOG.error("Unable to find properties for {}", bsdName);
/*     */                 }
/* 358 */                 Collections.sort(partitions);
/* 359 */                 diskStore.setPartitions((HWPartition[])partitions.toArray(new HWPartition[partitions.size()]));
/* 360 */                 IOKit.INSTANCE.IOObjectRelease(parent.getValue());
/*     */               } else {
/* 362 */                 LOG.error("Unable to find IOMedia device or parent for ", bsdName);
/*     */               }
/* 364 */               IOKit.INSTANCE.IOObjectRelease(drive);
/*     */             }
/* 366 */             IOKit.INSTANCE.IOObjectRelease(driveList.getValue());
/*     */           }
/* 368 */           result.add(diskStore);
/*     */         }
/*     */       }
/*     */     }
/* 372 */     CfUtil.release(session);
/* 373 */     Collections.sort(result);
/* 374 */     return (HWDiskStore[])result.toArray(new HWDiskStore[result.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacDisks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */