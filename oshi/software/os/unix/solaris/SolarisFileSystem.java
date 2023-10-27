/*     */ package oshi.software.os.unix.solaris;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.OSFileStore;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.platform.unix.solaris.KstatUtil;
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
/*     */ public class SolarisFileSystem
/*     */   implements FileSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  45 */   private final List<String> pseudofs = Arrays.asList(new String[] { "proc", "devfs", "ctfs", "objfs", "mntfs", "sharefs", "lofs", "autofs" });
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
/*  60 */   private final List<String> tmpfsPaths = Arrays.asList(new String[] { "/system", "/tmp", "/dev/fd" });
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
/*     */   private boolean listElementStartsWith(List<String> aList, String charSeq)
/*     */   {
/*  73 */     for (String match : aList) {
/*  74 */       if ((charSeq.equals(match)) || (charSeq.startsWith(match + "/"))) {
/*  75 */         return true;
/*     */       }
/*     */     }
/*  78 */     return false;
/*     */   }
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
/*  90 */     List<OSFileStore> fsList = new ArrayList();
/*     */     
/*     */ 
/*  93 */     for (String fs : ExecutingCommand.runNative("cat /etc/mnttab")) {
/*  94 */       String[] split = fs.split("\\s+");
/*  95 */       if (split.length >= 5)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */         String volume = split[0];
/* 103 */         String path = split[1];
/* 104 */         String type = split[2];
/*     */         
/*     */ 
/* 107 */         if ((!this.pseudofs.contains(type)) && (!path.equals("/dev")) && (!listElementStartsWith(this.tmpfsPaths, path)) && (
/* 108 */           (!volume.startsWith("rpool")) || (path.equals("/"))))
/*     */         {
/*     */ 
/*     */ 
/* 112 */           String name = path.substring(path.lastIndexOf('/') + 1);
/*     */           
/* 114 */           if (name.isEmpty()) {
/* 115 */             name = volume.substring(volume.lastIndexOf('/') + 1);
/*     */           }
/* 117 */           long totalSpace = new File(path).getTotalSpace();
/* 118 */           long usableSpace = new File(path).getUsableSpace();
/*     */           String description;
/*     */           String description;
/* 121 */           if ((volume.startsWith("/dev")) || (path.equals("/"))) {
/* 122 */             description = "Local Disk"; } else { String description;
/* 123 */             if (volume.equals("tmpfs")) {
/* 124 */               description = "Ram Disk"; } else { String description;
/* 125 */               if ((type.startsWith("nfs")) || (type.equals("cifs"))) {
/* 126 */                 description = "Network Disk";
/*     */               } else
/* 128 */                 description = "Mount Point";
/*     */             }
/*     */           }
/* 131 */           OSFileStore osStore = new OSFileStore(name, volume, path, description, type, "", usableSpace, totalSpace);
/* 132 */           fsList.add(osStore);
/*     */         } } }
/* 134 */     return (OSFileStore[])fsList.toArray(new OSFileStore[fsList.size()]);
/*     */   }
/*     */   
/*     */   public long getOpenFileDescriptors()
/*     */   {
/* 139 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup(null, -1, "file_cache");
/*     */     
/* 141 */     if ((ksp != null) && (KstatUtil.kstatRead(ksp))) {
/* 142 */       return KstatUtil.kstatDataLookupLong(ksp, "buf_inuse");
/*     */     }
/* 144 */     return 0L;
/*     */   }
/*     */   
/*     */   public long getMaxFileDescriptors()
/*     */   {
/* 149 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup(null, -1, "file_cache");
/*     */     
/* 151 */     if ((ksp != null) && (KstatUtil.kstatRead(ksp))) {
/* 152 */       return KstatUtil.kstatDataLookupLong(ksp, "buf_max");
/*     */     }
/* 154 */     return 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\solaris\SolarisFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */