/*     */ package oshi;
/*     */ 
/*     */ import com.sun.jna.Platform;
/*     */ import java.io.Serializable;
/*     */ import oshi.hardware.HardwareAbstractionLayer;
/*     */ import oshi.hardware.platform.linux.LinuxHardwareAbstractionLayer;
/*     */ import oshi.hardware.platform.mac.MacHardwareAbstractionLayer;
/*     */ import oshi.hardware.platform.unix.freebsd.FreeBsdHardwareAbstractionLayer;
/*     */ import oshi.hardware.platform.unix.solaris.SolarisHardwareAbstractionLayer;
/*     */ import oshi.hardware.platform.windows.WindowsHardwareAbstractionLayer;
/*     */ import oshi.software.os.OperatingSystem;
/*     */ import oshi.software.os.linux.LinuxOperatingSystem;
/*     */ import oshi.software.os.mac.MacOperatingSystem;
/*     */ import oshi.software.os.unix.freebsd.FreeBsdOperatingSystem;
/*     */ import oshi.software.os.unix.solaris.SolarisOperatingSystem;
/*     */ import oshi.software.os.windows.WindowsOperatingSystem;
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
/*     */ public class SystemInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  50 */   private OperatingSystem os = null;
/*     */   
/*  52 */   private HardwareAbstractionLayer hardware = null;
/*     */   
/*     */   private static final PlatformEnum currentPlatformEnum;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  59 */     if (Platform.isWindows()) {
/*  60 */       currentPlatformEnum = PlatformEnum.WINDOWS;
/*  61 */     } else if (Platform.isLinux()) {
/*  62 */       currentPlatformEnum = PlatformEnum.LINUX;
/*  63 */     } else if (Platform.isMac()) {
/*  64 */       currentPlatformEnum = PlatformEnum.MACOSX;
/*  65 */     } else if (Platform.isSolaris()) {
/*  66 */       currentPlatformEnum = PlatformEnum.SOLARIS;
/*  67 */     } else if (Platform.isFreeBSD()) {
/*  68 */       currentPlatformEnum = PlatformEnum.FREEBSD;
/*     */     } else {
/*  70 */       currentPlatformEnum = PlatformEnum.UNKNOWN;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static PlatformEnum getCurrentPlatformEnum()
/*     */   {
/*  78 */     return currentPlatformEnum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OperatingSystem getOperatingSystem()
/*     */   {
/*  88 */     if (this.os == null) {
/*  89 */       switch (currentPlatformEnum)
/*     */       {
/*     */       case WINDOWS: 
/*  92 */         this.os = new WindowsOperatingSystem();
/*  93 */         break;
/*     */       case LINUX: 
/*  95 */         this.os = new LinuxOperatingSystem();
/*  96 */         break;
/*     */       case MACOSX: 
/*  98 */         this.os = new MacOperatingSystem();
/*  99 */         break;
/*     */       case SOLARIS: 
/* 101 */         this.os = new SolarisOperatingSystem();
/* 102 */         break;
/*     */       case FREEBSD: 
/* 104 */         this.os = new FreeBsdOperatingSystem();
/* 105 */         break;
/*     */       default: 
/* 107 */         throw new UnsupportedOperationException("Operating system not supported: " + Platform.getOSType());
/*     */       }
/*     */     }
/* 110 */     return this.os;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HardwareAbstractionLayer getHardware()
/*     */   {
/* 120 */     if (this.hardware == null) {
/* 121 */       switch (currentPlatformEnum)
/*     */       {
/*     */       case WINDOWS: 
/* 124 */         this.hardware = new WindowsHardwareAbstractionLayer();
/* 125 */         break;
/*     */       case LINUX: 
/* 127 */         this.hardware = new LinuxHardwareAbstractionLayer();
/* 128 */         break;
/*     */       case MACOSX: 
/* 130 */         this.hardware = new MacHardwareAbstractionLayer();
/* 131 */         break;
/*     */       case SOLARIS: 
/* 133 */         this.hardware = new SolarisHardwareAbstractionLayer();
/* 134 */         break;
/*     */       case FREEBSD: 
/* 136 */         this.hardware = new FreeBsdHardwareAbstractionLayer();
/* 137 */         break;
/*     */       default: 
/* 139 */         throw new UnsupportedOperationException("Operating system not supported: " + Platform.getOSType());
/*     */       }
/*     */     }
/* 142 */     return this.hardware;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\SystemInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */