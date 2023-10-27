/*     */ package oshi.software.os.linux;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.OSFileStore;
/*     */ import oshi.util.FileUtil;
/*     */ import oshi.util.ParseUtil;
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
/*     */ public class LinuxFileSystem
/*     */   implements FileSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  49 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxFileSystem.class);
/*     */   
/*     */ 
/*  52 */   private final List<String> pseudofs = Arrays.asList(new String[] { "rootfs", "sysfs", "proc", "devtmpfs", "devpts", "securityfs", "cgroup", "pstore", "hugetlbfs", "configfs", "selinuxfs", "systemd-1", "binfmt_misc", "mqueue", "debugfs", "nfsd", "sunrpc", "rpc_pipefs", "fusectl" });
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
/*  81 */   private final List<String> tmpfsPaths = Arrays.asList(new String[] { "/dev/shm", "/run", "/sys", "/proc" });
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
/*  94 */     for (String match : aList) {
/*  95 */       if ((charSeq.equals(match)) || (charSeq.startsWith(match + "/"))) {
/*  96 */         return true;
/*     */       }
/*     */     }
/*  99 */     return false;
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
/* 112 */     Map<String, String> uuidMap = new HashMap();
/* 113 */     File uuidDir = new File("/dev/disk/by-uuid");
/* 114 */     if ((uuidDir != null) && (uuidDir.listFiles() != null)) {
/* 115 */       for (File uuid : uuidDir.listFiles()) {
/*     */         try
/*     */         {
/* 118 */           uuidMap.put(uuid.getCanonicalPath(), uuid.getName().toLowerCase());
/*     */         } catch (IOException e) {
/* 120 */           LOG.error("Couldn't get canonical path for {}. {}", uuid.getName(), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 126 */     Object fsList = new ArrayList();
/*     */     
/*     */ 
/* 129 */     Object mounts = FileUtil.readFile("/proc/self/mounts");
/* 130 */     for (String mount : (List)mounts) {
/* 131 */       String[] split = mount.split(" ");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */       if (split.length >= 6)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 144 */         String path = split[1].replaceAll("\\\\040", " ");
/* 145 */         String type = split[2];
/* 146 */         if ((!this.pseudofs.contains(type)) && (!path.equals("/dev")) && (!listElementStartsWith(this.tmpfsPaths, path)))
/*     */         {
/*     */ 
/*     */ 
/* 150 */           String name = split[0].replaceAll("\\\\040", " ");
/* 151 */           if (path.equals("/")) {
/* 152 */             name = "/";
/*     */           }
/* 154 */           String volume = split[0].replaceAll("\\\\040", " ");
/* 155 */           String uuid = (String)uuidMap.getOrDefault(split[0], "");
/* 156 */           long totalSpace = new File(path).getTotalSpace();
/* 157 */           long usableSpace = new File(path).getUsableSpace();
/*     */           String description;
/*     */           String description;
/* 160 */           if (volume.startsWith("/dev")) {
/* 161 */             description = "Local Disk"; } else { String description;
/* 162 */             if (volume.equals("tmpfs")) {
/* 163 */               description = "Ram Disk"; } else { String description;
/* 164 */               if ((type.startsWith("nfs")) || (type.equals("cifs"))) {
/* 165 */                 description = "Network Disk";
/*     */               } else
/* 167 */                 description = "Mount Point";
/*     */             }
/*     */           }
/* 170 */           OSFileStore osStore = new OSFileStore(name, volume, path, description, type, uuid, usableSpace, totalSpace);
/* 171 */           ((List)fsList).add(osStore);
/*     */         }
/*     */       } }
/* 174 */     return (OSFileStore[])((List)fsList).toArray(new OSFileStore[((List)fsList).size()]);
/*     */   }
/*     */   
/*     */   public long getOpenFileDescriptors()
/*     */   {
/* 179 */     return getFileDescriptors(0);
/*     */   }
/*     */   
/*     */   public long getMaxFileDescriptors()
/*     */   {
/* 184 */     return getFileDescriptors(2);
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
/*     */   private long getFileDescriptors(int index)
/*     */   {
/* 199 */     String filename = "/proc/sys/fs/file-nr";
/* 200 */     if ((index < 0) || (index > 2)) {
/* 201 */       throw new IllegalArgumentException("Index must be between 0 and 2.");
/*     */     }
/* 203 */     List<String> osDescriptors = FileUtil.readFile(filename);
/* 204 */     if (!osDescriptors.isEmpty()) {
/* 205 */       String[] splittedLine = ((String)osDescriptors.get(0)).split("\\D+");
/* 206 */       return ParseUtil.parseLongOrDefault(splittedLine[index], 0L);
/*     */     }
/* 208 */     return 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\linux\LinuxFileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */