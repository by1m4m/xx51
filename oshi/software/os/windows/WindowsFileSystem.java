/*     */ package oshi.software.os.windows;
/*     */ 
/*     */ import com.sun.jna.platform.win32.WinNT;
/*     */ import com.sun.jna.platform.win32.WinNT.HANDLE;
/*     */ import com.sun.jna.platform.win32.WinNT.LARGE_INTEGER;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import oshi.jna.platform.windows.Kernel32;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.OSFileStore;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WindowsFileSystem
/*     */   implements FileSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final int BUFSIZE = 255;
/*     */   
/*     */   public WindowsFileSystem()
/*     */   {
/*  50 */     Kernel32.INSTANCE.SetErrorMode(1);
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
/*     */   public OSFileStore[] getFileStores()
/*     */   {
/*  66 */     ArrayList<OSFileStore> result = getLocalVolumes();
/*     */     
/*     */ 
/*  69 */     Map<String, OSFileStore> volumeMap = new HashMap();
/*  70 */     for (OSFileStore volume : result) {
/*  71 */       volumeMap.put(volume.getMount(), volume);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  76 */     for (OSFileStore wmiVolume : getWmiVolumes()) {
/*  77 */       if (volumeMap.containsKey(wmiVolume.getMount()))
/*     */       {
/*     */ 
/*  80 */         ((OSFileStore)volumeMap.get(wmiVolume.getMount())).setName(wmiVolume.getName());
/*     */       }
/*     */       else {
/*  83 */         result.add(wmiVolume);
/*     */       }
/*     */     }
/*  86 */     return (OSFileStore[])result.toArray(new OSFileStore[result.size()]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArrayList<OSFileStore> getLocalVolumes()
/*     */   {
/* 111 */     ArrayList<OSFileStore> fs = new ArrayList();
/* 112 */     char[] aVolume = new char['ÿ'];
/*     */     
/* 114 */     WinNT.HANDLE hVol = Kernel32.INSTANCE.FindFirstVolume(aVolume, 255);
/* 115 */     if (hVol == WinNT.INVALID_HANDLE_VALUE) {
/* 116 */       return fs;
/*     */     }
/*     */     for (;;)
/*     */     {
/* 120 */       char[] fstype = new char[16];
/* 121 */       char[] name = new char['ÿ'];
/* 122 */       char[] mount = new char['ÿ'];
/*     */       
/* 124 */       WinNT.LARGE_INTEGER userFreeBytes = new WinNT.LARGE_INTEGER(0L);
/* 125 */       WinNT.LARGE_INTEGER totalBytes = new WinNT.LARGE_INTEGER(0L);
/* 126 */       WinNT.LARGE_INTEGER systemFreeBytes = new WinNT.LARGE_INTEGER(0L);
/*     */       
/* 128 */       String volume = new String(aVolume).trim();
/* 129 */       Kernel32.INSTANCE.GetVolumeInformation(volume, name, 255, null, null, null, fstype, 16);
/* 130 */       Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(volume, mount, 255, null);
/* 131 */       Kernel32.INSTANCE.GetDiskFreeSpaceEx(volume, userFreeBytes, totalBytes, systemFreeBytes);
/*     */       
/* 133 */       String strMount = new String(mount).trim();
/* 134 */       String strName = new String(name).trim();
/* 135 */       String strFsType = new String(fstype).trim();
/*     */       
/* 137 */       String uuid = ParseUtil.parseUuidOrDefault(volume, "");
/*     */       
/* 139 */       if (!strMount.isEmpty())
/*     */       {
/* 141 */         fs.add(new OSFileStore(String.format("%s (%s)", new Object[] { strName, strMount }), volume, strMount, 
/* 142 */           getDriveType(strMount), strFsType, uuid, systemFreeBytes.getValue(), totalBytes.getValue()));
/*     */       }
/* 144 */       boolean retVal = Kernel32.INSTANCE.FindNextVolume(hVol, aVolume, 255);
/* 145 */       if (!retVal) {
/* 146 */         Kernel32.INSTANCE.FindVolumeClose(hVol);
/* 147 */         break;
/*     */       }
/*     */     }
/*     */     
/* 151 */     return fs;
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
/*     */   private List<OSFileStore> getWmiVolumes()
/*     */   {
/* 167 */     List<OSFileStore> fs = new ArrayList();
/*     */     
/* 169 */     Map<String, List<String>> drives = WmiUtil.selectStringsFrom(null, "Win32_LogicalDisk", "Name,Description,ProviderName,FileSystem,Freespace,Size", null);
/*     */     
/*     */ 
/* 172 */     for (int i = 0; i < ((List)drives.get("Name")).size(); i++) {
/* 173 */       long free = ParseUtil.parseLongOrDefault((String)((List)drives.get("Freespace")).get(i), 0L);
/* 174 */       long total = ParseUtil.parseLongOrDefault((String)((List)drives.get("Size")).get(i), 0L);
/* 175 */       String description = (String)((List)drives.get("Description")).get(i);
/*     */       
/* 177 */       long type = WmiUtil.selectUint32From(null, "Win32_LogicalDisk", "DriveType", "WHERE Name = '" + 
/* 178 */         (String)((List)drives.get("Name")).get(i) + "'").longValue();
/*     */       String volume;
/* 179 */       String volume; if (type != 4L) {
/* 180 */         char[] chrVolume = new char['ÿ'];
/* 181 */         Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint((String)((List)drives.get("Name")).get(i) + "\\", chrVolume, 255);
/*     */         
/* 183 */         volume = new String(chrVolume).trim();
/*     */       } else {
/* 185 */         volume = (String)((List)drives.get("ProviderName")).get(i);
/* 186 */         String[] split = volume.split("\\\\");
/* 187 */         if ((split.length > 1) && (split[(split.length - 1)].length() > 0)) {
/* 188 */           description = split[(split.length - 1)];
/*     */         }
/*     */       }
/*     */       
/* 192 */       fs.add(new OSFileStore(String.format("%s (%s)", new Object[] { description, ((List)drives.get("Name")).get(i) }), volume, 
/* 193 */         (String)((List)drives.get("Name")).get(i) + "\\", getDriveType((String)((List)drives.get("Name")).get(i)), 
/* 194 */         (String)((List)drives.get("FileSystem")).get(i), "", free, total));
/*     */     }
/* 196 */     return fs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getDriveType(String drive)
/*     */   {
/* 207 */     switch (Kernel32.INSTANCE.GetDriveType(drive)) {
/*     */     case 2: 
/* 209 */       return "Removable drive";
/*     */     case 3: 
/* 211 */       return "Fixed drive";
/*     */     case 4: 
/* 213 */       return "Network drive";
/*     */     case 5: 
/* 215 */       return "CD-ROM";
/*     */     case 6: 
/* 217 */       return "RAM drive";
/*     */     }
/* 219 */     return "Unknown drive type";
/*     */   }
/*     */   
/*     */ 
/*     */   public long getOpenFileDescriptors()
/*     */   {
/* 225 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getMaxFileDescriptors()
/*     */   {
/* 230 */     return 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\windows\WindowsFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */