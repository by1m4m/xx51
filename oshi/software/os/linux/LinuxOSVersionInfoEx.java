/*     */ package oshi.software.os.linux;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.software.common.AbstractOSVersionInfoEx;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.FileUtil;
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
/*     */ public class LinuxOSVersionInfoEx
/*     */   extends AbstractOSVersionInfoEx
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  35 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxOSVersionInfoEx.class);
/*     */   
/*     */   public LinuxOSVersionInfoEx() {
/*  38 */     this(null, null);
/*     */   }
/*     */   
/*     */   protected LinuxOSVersionInfoEx(String versionId, String codeName) {
/*  42 */     setVersion(versionId);
/*  43 */     setCodeName(codeName);
/*  44 */     if (getVersion() == null) {
/*  45 */       setVersionFromReleaseFiles();
/*     */     }
/*  47 */     if (getCodeName() == null) {
/*  48 */       setCodeName("");
/*     */     }
/*  50 */     List<String> procVersion = null;
/*  51 */     procVersion = FileUtil.readFile("/proc/version");
/*  52 */     if (!procVersion.isEmpty()) {
/*  53 */       String[] split = ((String)procVersion.get(0)).split("\\s+");
/*  54 */       for (String s : split) {
/*  55 */         if ((!"Linux".equals(s)) && (!"version".equals(s))) {
/*  56 */           setBuildNumber(s);
/*  57 */           return;
/*     */         }
/*     */       }
/*     */     }
/*  61 */     setBuildNumber("");
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
/*     */ 
/*     */   private void setVersionFromReleaseFiles()
/*     */   {
/*  87 */     if (readOsRelease())
/*     */     {
/*     */ 
/*  90 */       return;
/*     */     }
/*     */     
/*     */ 
/*  94 */     if (execLsbRelease())
/*     */     {
/*     */ 
/*  97 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 103 */     if (readLsbRelease())
/*     */     {
/*     */ 
/* 106 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */     String etcDistribRelease = LinuxOperatingSystem.getReleaseFilename();
/* 114 */     if (readDistribRelease(etcDistribRelease))
/*     */     {
/*     */ 
/* 117 */       return;
/*     */     }
/*     */     
/* 120 */     if (getVersion() == null) {
/* 121 */       setVersion(System.getProperty("os.version"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean readOsRelease()
/*     */   {
/* 131 */     if (new File("/etc/os-release").exists()) {
/* 132 */       List<String> osRelease = FileUtil.readFile("/etc/os-release");
/*     */       
/* 134 */       for (String line : osRelease) {
/* 135 */         if (line.startsWith("VERSION=")) {
/* 136 */           LOG.debug("os-release: {}", line);
/*     */           
/*     */ 
/*     */ 
/* 140 */           line = line.replace("VERSION=", "").replaceAll("^\"|\"$", "").trim();
/* 141 */           String[] split = line.split("[()]");
/* 142 */           if (split.length <= 1)
/*     */           {
/* 144 */             split = line.split(", ");
/*     */           }
/* 146 */           if (split.length > 0) {
/* 147 */             this.version = split[0].trim();
/*     */           }
/* 149 */           if (split.length > 1) {
/* 150 */             this.codeName = split[1].trim();
/*     */           }
/* 152 */         } else if ((line.startsWith("VERSION_ID=")) && (this.version == null)) {
/* 153 */           LOG.debug("os-release: {}", line);
/*     */           
/*     */ 
/* 156 */           this.version = line.replace("VERSION_ID=", "").replaceAll("^\"|\"$", "").trim();
/*     */         }
/*     */       }
/*     */     }
/* 160 */     return this.version != null;
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
/*     */   private boolean execLsbRelease()
/*     */   {
/* 173 */     for (String line : ExecutingCommand.runNative("lsb_release -a")) {
/* 174 */       if (line.startsWith("Description:")) {
/* 175 */         LOG.debug("lsb_release -a: {}", line);
/* 176 */         line = line.replace("Description:", "").trim();
/* 177 */         if (line.contains(" release ")) {
/* 178 */           this.version = parseRelease(line, " release ");
/*     */         }
/* 180 */       } else if ((line.startsWith("Release:")) && (this.version == null)) {
/* 181 */         LOG.debug("lsb_release -a: {}", line);
/* 182 */         this.version = line.replace("Release:", "").trim();
/* 183 */       } else if ((line.startsWith("Codename:")) && (this.codeName == null)) {
/* 184 */         LOG.debug("lsb_release -a: {}", line);
/* 185 */         this.codeName = line.replace("Codename:", "").trim();
/*     */       }
/*     */     }
/* 188 */     return this.version != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean readLsbRelease()
/*     */   {
/* 198 */     if (new File("/etc/lsb-release").exists()) {
/* 199 */       List<String> osRelease = FileUtil.readFile("/etc/lsb-release");
/*     */       
/* 201 */       for (String line : osRelease) {
/* 202 */         if (line.startsWith("DISTRIB_DESCRIPTION=")) {
/* 203 */           LOG.debug("lsb-release: {}", line);
/* 204 */           line = line.replace("DISTRIB_DESCRIPTION=", "").replaceAll("^\"|\"$", "").trim();
/* 205 */           if (line.contains(" release ")) {
/* 206 */             this.version = parseRelease(line, " release ");
/*     */           }
/* 208 */         } else if ((line.startsWith("DISTRIB_RELEASE=")) && (this.version == null)) {
/* 209 */           LOG.debug("lsb-release: {}", line);
/* 210 */           this.version = line.replace("DISTRIB_RELEASE=", "").replaceAll("^\"|\"$", "").trim();
/* 211 */         } else if ((line.startsWith("DISTRIB_CODENAME=")) && (this.codeName == null)) {
/* 212 */           LOG.debug("lsb-release: {}", line);
/* 213 */           this.codeName = line.replace("DISTRIB_CODENAME=", "").replaceAll("^\"|\"$", "").trim();
/*     */         }
/*     */       }
/*     */     }
/* 217 */     return this.version != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean readDistribRelease(String filename)
/*     */   {
/* 227 */     if (new File(filename).exists()) {
/* 228 */       List<String> osRelease = FileUtil.readFile(filename);
/*     */       
/* 230 */       for (String line : osRelease) {
/* 231 */         LOG.debug("{}: {}", filename, line);
/* 232 */         if (line.contains(" release ")) {
/* 233 */           this.version = parseRelease(line, " release ");
/*     */           
/* 235 */           break; }
/* 236 */         if (line.contains(" VERSION ")) {
/* 237 */           this.version = parseRelease(line, " VERSION ");
/*     */           
/* 239 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 243 */     return this.version != null;
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
/*     */   private String parseRelease(String line, String splitLine)
/*     */   {
/* 256 */     String[] split = line.split(splitLine);
/* 257 */     if (split.length > 1) {
/* 258 */       split = split[1].split("[()]");
/* 259 */       if (split.length > 0) {
/* 260 */         this.version = split[0].trim();
/*     */       }
/* 262 */       if (split.length > 1) {
/* 263 */         this.codeName = split[1].trim();
/*     */       }
/*     */     }
/* 266 */     return this.version;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\linux\LinuxOSVersionInfoEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */