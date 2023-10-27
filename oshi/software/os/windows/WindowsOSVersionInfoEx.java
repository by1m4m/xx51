/*     */ package oshi.software.os.windows;
/*     */ 
/*     */ import com.sun.jna.platform.win32.User32;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.software.common.AbstractOSVersionInfoEx;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.windows.WmiUtil;
/*     */ import oshi.util.platform.windows.WmiUtil.ValueType;
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
/*     */ public class WindowsOSVersionInfoEx
/*     */   extends AbstractOSVersionInfoEx
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  42 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsOSVersionInfoEx.class);
/*     */   
/*  44 */   private static final WmiUtil.ValueType[] queryTypes = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT32 };
/*     */   
/*     */ 
/*  47 */   private transient Map<String, List<Object>> versionInfo = new HashMap();
/*     */   
/*     */   public WindowsOSVersionInfoEx()
/*     */   {
/*  51 */     this.versionInfo = WmiUtil.selectObjectsFrom(null, "Win32_OperatingSystem", "Version,ProductType,BuildNumber,CSDVersion,SuiteMask", null, queryTypes);
/*     */     
/*  53 */     if (((List)this.versionInfo.get("Version")).isEmpty()) {
/*  54 */       LOG.warn("No version data available.");
/*  55 */       setVersion(System.getProperty("os.version"));
/*  56 */       setCodeName("");
/*  57 */       setBuildNumber("");
/*     */     }
/*     */     else
/*     */     {
/*  61 */       setVersion(parseVersion());
/*  62 */       setCodeName(parseCodeName());
/*  63 */       setBuildNumber(parseBuildNumber());
/*  64 */       LOG.debug("Initialized OSVersionInfoEx");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String parseVersion()
/*     */   {
/*  76 */     String version = System.getProperty("os.version");
/*     */     
/*     */ 
/*     */ 
/*  80 */     String[] verSplit = ((String)((List)this.versionInfo.get("Version")).get(0)).split("\\D");
/*  81 */     int major = verSplit.length > 0 ? ParseUtil.parseIntOrDefault(verSplit[0], 0) : 0;
/*  82 */     int minor = verSplit.length > 1 ? ParseUtil.parseIntOrDefault(verSplit[1], 0) : 0;
/*     */     
/*     */ 
/*     */ 
/*  86 */     boolean ntWorkstation = ((Long)((List)this.versionInfo.get("ProductType")).get(0)).longValue() == 1L;
/*  87 */     if (major == 10) {
/*  88 */       if (minor == 0) {
/*  89 */         version = ntWorkstation ? "10" : "Server 2016";
/*     */       }
/*  91 */     } else if (major == 6) {
/*  92 */       if (minor == 3) {
/*  93 */         version = ntWorkstation ? "8.1" : "Server 2012 R2";
/*  94 */       } else if (minor == 2) {
/*  95 */         version = ntWorkstation ? "8" : "Server 2012";
/*  96 */       } else if (minor == 1) {
/*  97 */         version = ntWorkstation ? "7" : "Server 2008 R2";
/*  98 */       } else if (minor == 0) {
/*  99 */         version = ntWorkstation ? "Vista" : "Server 2008";
/*     */       }
/* 101 */     } else if (major == 5) {
/* 102 */       if (minor == 2) {
/* 103 */         if ((getSuiteMask() & 0x8000) != 0) {
/* 104 */           version = "Home Server";
/* 105 */         } else if (ntWorkstation) {
/* 106 */           version = "XP";
/*     */         } else {
/* 108 */           version = User32.INSTANCE.GetSystemMetrics(89) != 0 ? "Server 2003" : "Server 2003 R2";
/*     */         }
/*     */       }
/* 111 */       else if (minor == 1) {
/* 112 */         version = "XP";
/* 113 */       } else if (minor == 0) {
/* 114 */         version = "2000";
/*     */       }
/*     */     }
/*     */     
/* 118 */     String sp = (String)((List)this.versionInfo.get("CSDVersion")).get(0);
/* 119 */     if ((!sp.isEmpty()) && (!"unknown".equals(sp))) {
/* 120 */       version = version + " " + sp.replace("Service Pack ", "SP");
/*     */     }
/*     */     
/* 123 */     return version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String parseCodeName()
/*     */   {
/* 132 */     List<String> suites = new ArrayList();
/* 133 */     int bitmask = getSuiteMask();
/* 134 */     if ((bitmask & 0x2) != 0) {
/* 135 */       suites.add("Enterprise");
/*     */     }
/* 137 */     if ((bitmask & 0x4) != 0) {
/* 138 */       suites.add("BackOffice");
/*     */     }
/* 140 */     if ((bitmask & 0x8) != 0) {
/* 141 */       suites.add("Communication Server");
/*     */     }
/* 143 */     if ((bitmask & 0x80) != 0) {
/* 144 */       suites.add("Datacenter");
/*     */     }
/* 146 */     if ((bitmask & 0x200) != 0) {
/* 147 */       suites.add("Home");
/*     */     }
/* 149 */     if ((bitmask & 0x400) != 0) {
/* 150 */       suites.add("Web Server");
/*     */     }
/* 152 */     if ((bitmask & 0x2000) != 0) {
/* 153 */       suites.add("Storage Server");
/*     */     }
/* 155 */     if ((bitmask & 0x4000) != 0) {
/* 156 */       suites.add("Compute Cluster");
/*     */     }
/*     */     
/* 159 */     String separator = "";
/* 160 */     StringBuilder sb = new StringBuilder();
/* 161 */     for (String s : suites) {
/* 162 */       sb.append(separator).append(s);
/* 163 */       separator = ",";
/*     */     }
/* 165 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getSuiteMask()
/*     */   {
/* 177 */     return (int)((Long)((List)this.versionInfo.get("SuiteMask")).get(0)).longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String parseBuildNumber()
/*     */   {
/* 186 */     return (String)((List)this.versionInfo.get("BuildNumber")).get(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\windows\WindowsOSVersionInfoEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */