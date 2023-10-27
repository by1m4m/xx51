/*     */ package oshi.software.os.unix.freebsd;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.OSFileStore;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
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
/*     */ public class FreeBsdFileSystem
/*     */   implements FileSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  46 */   private final List<String> pseudofs = Arrays.asList(new String[] { "procfs", "devfs", "ctfs", "fdescfs", "objfs", "mntfs", "sharefs", "lofs", "autofs" });
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
/*  62 */   private final List<String> tmpfsPaths = Arrays.asList(new String[] { "/system", "/tmp", "/dev/fd" });
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
/*  75 */     for (String match : aList) {
/*  76 */       if ((charSeq.equals(match)) || (charSeq.startsWith(match + "/"))) {
/*  77 */         return true;
/*     */       }
/*     */     }
/*  80 */     return false;
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
/*     */   public OSFileStore[] getFileStores()
/*     */   {
/*  93 */     Map<String, String> uuidMap = new HashMap();
/*     */     
/*  95 */     String device = "";
/*  96 */     for (Iterator localIterator = ExecutingCommand.runNative("geom part list").iterator(); localIterator.hasNext();) { line = (String)localIterator.next();
/*  97 */       if (line.contains("Name: ")) {
/*  98 */         device = line.substring(line.lastIndexOf(' ') + 1);
/*     */       }
/*     */       
/* 101 */       if (!device.isEmpty())
/*     */       {
/*     */ 
/* 104 */         line = line.trim();
/* 105 */         if (line.startsWith("rawuuid:")) {
/* 106 */           uuidMap.put(device, line.substring(line.lastIndexOf(' ') + 1));
/* 107 */           device = "";
/*     */         }
/*     */       } }
/*     */     String line;
/* 111 */     Object fsList = new ArrayList();
/*     */     
/*     */ 
/* 114 */     for (String fs : ExecutingCommand.runNative("mount -p")) {
/* 115 */       String[] split = fs.split("\\s+");
/* 116 */       if (split.length >= 5)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */         String volume = split[0];
/* 124 */         String path = split[1];
/* 125 */         String type = split[2];
/*     */         
/*     */ 
/* 128 */         if ((!this.pseudofs.contains(type)) && (!path.equals("/dev")) && (!listElementStartsWith(this.tmpfsPaths, path)) && (
/* 129 */           (!volume.startsWith("rpool")) || (path.equals("/"))))
/*     */         {
/*     */ 
/*     */ 
/* 133 */           String name = path.substring(path.lastIndexOf('/') + 1);
/*     */           
/* 135 */           if (name.isEmpty()) {
/* 136 */             name = volume.substring(volume.lastIndexOf('/') + 1);
/*     */           }
/* 138 */           long totalSpace = new File(path).getTotalSpace();
/* 139 */           long usableSpace = new File(path).getUsableSpace();
/*     */           String description;
/*     */           String description;
/* 142 */           if ((volume.startsWith("/dev")) || (path.equals("/"))) {
/* 143 */             description = "Local Disk"; } else { String description;
/* 144 */             if (volume.equals("tmpfs")) {
/* 145 */               description = "Ram Disk"; } else { String description;
/* 146 */               if ((type.startsWith("nfs")) || (type.equals("cifs"))) {
/* 147 */                 description = "Network Disk";
/*     */               } else
/* 149 */                 description = "Mount Point";
/*     */             }
/*     */           }
/* 152 */           String uuid = (String)uuidMap.getOrDefault(name, "");
/* 153 */           OSFileStore osStore = new OSFileStore(name, volume, path, description, type, uuid, usableSpace, totalSpace);
/* 154 */           ((List)fsList).add(osStore);
/*     */         } } }
/* 156 */     return (OSFileStore[])((List)fsList).toArray(new OSFileStore[((List)fsList).size()]);
/*     */   }
/*     */   
/*     */   public long getOpenFileDescriptors()
/*     */   {
/* 161 */     return BsdSysctlUtil.sysctl("kern.openfiles", 0);
/*     */   }
/*     */   
/*     */   public long getMaxFileDescriptors()
/*     */   {
/* 166 */     return BsdSysctlUtil.sysctl("kern.maxfiles", 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\freebsd\FreeBsdFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */