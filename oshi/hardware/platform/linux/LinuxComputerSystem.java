/*     */ package oshi.hardware.platform.linux;
/*     */ 
/*     */ import oshi.hardware.common.AbstractComputerSystem;
/*     */ import oshi.util.ExecutingCommand;
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
/*     */ final class LinuxComputerSystem
/*     */   extends AbstractComputerSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String SYSFS_SERIAL_PATH = "/sys/devices/virtual/dmi/id/";
/*     */   private static final String UNKNOWN = "unknown";
/*     */   
/*     */   LinuxComputerSystem()
/*     */   {
/*  42 */     init();
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
/*     */   private void init()
/*     */   {
/*  55 */     String sysVendor = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/sys_vendor");
/*  56 */     if ((sysVendor != null) && (!sysVendor.trim().isEmpty())) {
/*  57 */       setManufacturer(sysVendor.trim());
/*     */     }
/*     */     
/*  60 */     String productName = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_name");
/*  61 */     String productVersion = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_version");
/*     */     
/*  63 */     if ((productName != null) && (!productName.trim().isEmpty()))
/*     */     {
/*  65 */       if ((productVersion != null) && (!productVersion.trim().isEmpty()) && (!"None".equals(productVersion.trim())))
/*     */       {
/*  67 */         setModel(productName.trim() + " (version: " + productVersion.trim() + ")");
/*     */       } else {
/*  69 */         setModel(productName.trim());
/*     */       }
/*     */     }
/*  72 */     else if ((productVersion != null) && (!productVersion.trim().isEmpty()))
/*     */     {
/*  74 */       setModel(productVersion.trim());
/*     */     }
/*     */     
/*     */ 
/*  78 */     setSerialNumber(getSystemSerialNumber());
/*     */     
/*  80 */     setFirmware(new LinuxFirmware());
/*     */     
/*  82 */     setBaseboard(new LinuxBaseboard());
/*     */   }
/*     */   
/*     */ 
/*     */   private String getSystemSerialNumber()
/*     */   {
/*  88 */     String serialNumber = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_serial");
/*  89 */     if ((serialNumber.isEmpty()) || ("None".equals(serialNumber))) {
/*  90 */       serialNumber = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_serial");
/*  91 */       if ((serialNumber.isEmpty()) || ("None".equals(serialNumber))) {
/*  92 */         serialNumber = "unknown";
/*     */       }
/*     */     }
/*     */     
/*  96 */     String marker = "Serial Number:";
/*  97 */     if ("unknown".equals(serialNumber)) {
/*  98 */       for (String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
/*  99 */         if (checkLine.contains(marker)) {
/* 100 */           serialNumber = checkLine.split(marker)[1].trim();
/* 101 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 106 */     if ("unknown".equals(serialNumber)) {
/* 107 */       marker = "system.hardware.serial =";
/* 108 */       for (String checkLine : ExecutingCommand.runNative("lshal")) {
/* 109 */         if (checkLine.contains(marker)) {
/* 110 */           serialNumber = ParseUtil.getSingleQuoteStringValue(checkLine);
/* 111 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 115 */     return serialNumber;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */