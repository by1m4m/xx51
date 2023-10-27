/*     */ package oshi.hardware.platform.linux;
/*     */ 
/*     */ import oshi.hardware.common.AbstractFirmware;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.FileUtil;
/*     */ import oshi.util.FormatUtil;
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
/*     */ final class LinuxFirmware
/*     */   extends AbstractFirmware
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String SYSFS_SERIAL_PATH = "/sys/devices/virtual/dmi/id/";
/*     */   
/*     */   LinuxFirmware()
/*     */   {
/*  41 */     init();
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
/*     */   private void init()
/*     */   {
/*  91 */     String biosVendor = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/bios_vendor");
/*  92 */     if ((biosVendor != null) && (!biosVendor.trim().isEmpty())) {
/*  93 */       setManufacturer(biosVendor.trim());
/*     */     }
/*     */     
/*  96 */     String modalias = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/modalias");
/*  97 */     if ((modalias != null) && (!modalias.trim().isEmpty())) {
/*  98 */       setDescription(modalias.trim());
/*     */     }
/*     */     
/* 101 */     String biosVersion = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/bios_version");
/* 102 */     if ((biosVersion != null) && (!biosVersion.trim().isEmpty())) {
/* 103 */       String marker = "Bios Revision:";
/* 104 */       String biosRevision = null;
/*     */       
/* 106 */       for (String checkLine : ExecutingCommand.runNative("dmidecode -t bios")) {
/* 107 */         if (checkLine.contains("Bios Revision:")) {
/* 108 */           biosRevision = checkLine.split("Bios Revision:")[1].trim();
/* 109 */           break;
/*     */         }
/*     */       }
/* 112 */       if ((biosRevision != null) && (!biosRevision.trim().isEmpty())) {
/* 113 */         setVersion(biosVersion.trim() + " (revision " + biosRevision + ")");
/*     */       } else {
/* 115 */         setVersion(biosVersion.trim());
/*     */       }
/*     */     }
/*     */     
/* 119 */     String biosDate = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/bios_date");
/* 120 */     if ((biosDate != null) && (!biosDate.trim().isEmpty())) {
/* 121 */       setReleaseDate(FormatUtil.formatStringDate(biosDate.trim()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxFirmware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */