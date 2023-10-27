/*     */ package oshi.software.os.mac;
/*     */ 
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.mac.CoreFoundation;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFDictionaryRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFMutableDictionaryRef;
/*     */ import oshi.jna.platform.mac.DiskArbitration;
/*     */ import oshi.jna.platform.mac.DiskArbitration.DADiskRef;
/*     */ import oshi.jna.platform.mac.DiskArbitration.DASessionRef;
/*     */ import oshi.jna.platform.mac.IOKit;
/*     */ import oshi.jna.platform.mac.SystemB;
/*     */ import oshi.jna.platform.mac.SystemB.Statfs;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.OSFileStore;
/*     */ import oshi.util.platform.mac.CfUtil;
/*     */ import oshi.util.platform.mac.IOKitUtil;
/*     */ import oshi.util.platform.mac.SysctlUtil;
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
/*     */ public class MacFileSystem
/*     */   implements FileSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  59 */   private static final Logger LOG = LoggerFactory.getLogger(MacFileSystem.class);
/*     */   
/*     */ 
/*  62 */   private static final Pattern LOCAL_DISK = Pattern.compile("/dev/disk\\d");
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
/*     */   public OSFileStore[] getFileStores()
/*     */   {
/*  75 */     DiskArbitration.DASessionRef session = DiskArbitration.INSTANCE.DASessionCreate(CfUtil.ALLOCATOR);
/*  76 */     if (session == null) {
/*  77 */       LOG.error("Unable to open session to DiskArbitration framework.");
/*     */     }
/*     */     
/*     */ 
/*  81 */     List<OSFileStore> fsList = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*  85 */     int numfs = SystemB.INSTANCE.getfsstat64(null, 0, 0);
/*  86 */     if (numfs > 0)
/*     */     {
/*     */ 
/*  89 */       SystemB.Statfs[] fs = new SystemB.Statfs[numfs];
/*     */       
/*  91 */       numfs = SystemB.INSTANCE.getfsstat64(fs, numfs * new SystemB.Statfs().size(), 16);
/*  92 */       for (int f = 0; f < numfs; f++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */         String volume = new String(fs[f].f_mntfromname).trim();
/*     */         
/* 100 */         if ((!volume.equals("devfs")) && (!volume.startsWith("map ")))
/*     */         {
/*     */ 
/*     */ 
/* 104 */           String description = "Volume";
/* 105 */           if (LOCAL_DISK.matcher(volume).matches()) {
/* 106 */             description = "Local Disk";
/*     */           }
/* 108 */           if ((volume.startsWith("localhost:")) || (volume.startsWith("//"))) {
/* 109 */             description = "Network Drive";
/*     */           }
/*     */           
/* 112 */           String type = new String(fs[f].f_fstypename).trim();
/* 113 */           String path = new String(fs[f].f_mntonname).trim();
/*     */           
/*     */ 
/* 116 */           String name = "";
/* 117 */           String uuid = "";
/*     */           
/*     */ 
/* 120 */           String bsdName = volume.replace("/dev/disk", "disk");
/* 121 */           if (bsdName.startsWith("disk"))
/*     */           {
/*     */ 
/* 124 */             DiskArbitration.DADiskRef disk = DiskArbitration.INSTANCE.DADiskCreateFromBSDName(CfUtil.ALLOCATOR, session, volume);
/*     */             
/* 126 */             if (disk != null) {
/* 127 */               CoreFoundation.CFDictionaryRef diskInfo = DiskArbitration.INSTANCE.DADiskCopyDescription(disk);
/* 128 */               if (diskInfo != null)
/*     */               {
/* 130 */                 Pointer volumePtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(diskInfo, 
/* 131 */                   CfUtil.getCFString("DAVolumeName"));
/* 132 */                 name = CfUtil.cfPointerToString(volumePtr);
/* 133 */                 CfUtil.release(diskInfo);
/*     */               }
/* 135 */               CfUtil.release(disk);
/*     */             }
/*     */             
/* 138 */             CoreFoundation.CFMutableDictionaryRef matchingDict = IOKitUtil.getBSDNameMatchingDict(bsdName);
/* 139 */             if (matchingDict != null)
/*     */             {
/* 141 */               IntByReference fsIter = new IntByReference();
/* 142 */               IOKitUtil.getMatchingServices(matchingDict, fsIter);
/*     */               
/*     */ 
/* 145 */               int fsEntry = IOKit.INSTANCE.IOIteratorNext(fsIter.getValue());
/* 146 */               if ((fsEntry != 0) && (IOKit.INSTANCE.IOObjectConformsTo(fsEntry, "IOMedia")))
/*     */               {
/* 148 */                 uuid = IOKitUtil.getIORegistryStringProperty(fsEntry, "UUID");
/* 149 */                 if (uuid == null) {
/* 150 */                   uuid = "";
/*     */                 } else {
/* 152 */                   uuid = uuid.toLowerCase();
/*     */                 }
/* 154 */                 IOKit.INSTANCE.IOObjectRelease(fsEntry);
/*     */               }
/* 156 */               IOKit.INSTANCE.IOObjectRelease(fsIter.getValue());
/*     */             }
/*     */           }
/* 159 */           File file = new File(path);
/* 160 */           if (name.isEmpty()) {
/* 161 */             name = file.getName();
/*     */             
/* 163 */             if (name.isEmpty()) {
/* 164 */               name = file.getPath();
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 169 */           fsList.add(new OSFileStore(name, volume, path, description, type, uuid, file.getUsableSpace(), file
/* 170 */             .getTotalSpace()));
/*     */         }
/*     */       }
/*     */     }
/* 174 */     CfUtil.release(session);
/* 175 */     return (OSFileStore[])fsList.toArray(new OSFileStore[fsList.size()]);
/*     */   }
/*     */   
/*     */   public long getOpenFileDescriptors()
/*     */   {
/* 180 */     return SysctlUtil.sysctl("kern.num_files", 0);
/*     */   }
/*     */   
/*     */   public long getMaxFileDescriptors()
/*     */   {
/* 185 */     return SysctlUtil.sysctl("kern.maxfiles", 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\mac\MacFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */