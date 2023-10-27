/*     */ package oshi.hardware.platform.mac;
/*     */ 
/*     */ import oshi.hardware.common.AbstractComputerSystem;
/*     */ import oshi.jna.platform.mac.IOKit;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.platform.mac.IOKitUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MacComputerSystem
/*     */   extends AbstractComputerSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String APPLE = "Apple Inc.";
/*     */   
/*     */   MacComputerSystem()
/*     */   {
/*  41 */     init();
/*     */   }
/*     */   
/*     */   private void init()
/*     */   {
/*  46 */     setManufacturer("Apple Inc.");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */     String modelName = "";
/*  70 */     String modelNameMarker = "Model Name:";
/*  71 */     String modelIdentifier = "";
/*  72 */     String modelIdMarker = "Model Identifier:";
/*  73 */     String serialNumberSystem = "";
/*  74 */     String serialNumMarker = "Serial Number (system):";
/*  75 */     String smcVersion = "";
/*  76 */     String smcMarker = "SMC Version (system):";
/*  77 */     String bootRomVersion = "";
/*  78 */     String bootRomMarker = "Boot ROM Version:";
/*     */     
/*  80 */     MacFirmware firmware = new MacFirmware();
/*  81 */     firmware.setManufacturer("Apple Inc.");
/*  82 */     firmware.setName("EFI");
/*     */     
/*  84 */     MacBaseboard baseboard = new MacBaseboard();
/*  85 */     baseboard.setManufacturer("Apple Inc.");
/*  86 */     baseboard.setModel("SMC");
/*     */     
/*     */ 
/*  89 */     for (String checkLine : ExecutingCommand.runNative("system_profiler SPHardwareDataType")) {
/*  90 */       if (checkLine.contains("Model Name:")) {
/*  91 */         modelName = checkLine.split("Model Name:")[1].trim();
/*     */       }
/*  93 */       if (checkLine.contains("Model Identifier:")) {
/*  94 */         modelIdentifier = checkLine.split("Model Identifier:")[1].trim();
/*     */       }
/*  96 */       if (checkLine.contains("Boot ROM Version:")) {
/*  97 */         bootRomVersion = checkLine.split("Boot ROM Version:")[1].trim();
/*     */       }
/*  99 */       if (checkLine.contains("SMC Version (system):")) {
/* 100 */         smcVersion = checkLine.split(java.util.regex.Pattern.quote("SMC Version (system):"))[1].trim();
/*     */       }
/* 102 */       if (checkLine.contains("Serial Number (system):")) {
/* 103 */         serialNumberSystem = checkLine.split(java.util.regex.Pattern.quote("Serial Number (system):"))[1].trim();
/*     */       }
/*     */     }
/*     */     
/* 107 */     if (!modelName.isEmpty()) {
/* 108 */       if (!modelIdentifier.isEmpty()) {
/* 109 */         setModel(modelName + " (" + modelIdentifier + ")");
/*     */       } else {
/* 111 */         setModel(modelName);
/*     */       }
/*     */     }
/* 114 */     else if (!modelIdentifier.isEmpty()) {
/* 115 */       setModel(modelIdentifier);
/*     */     }
/*     */     
/* 118 */     if (serialNumberSystem.isEmpty()) {
/* 119 */       serialNumberSystem = getSystemSerialNumber();
/*     */     }
/* 121 */     setSerialNumber(serialNumberSystem);
/* 122 */     baseboard.setSerialNumber(serialNumberSystem);
/* 123 */     if (!smcVersion.isEmpty()) {
/* 124 */       baseboard.setVersion(smcVersion);
/*     */     }
/* 126 */     if ((bootRomVersion != null) && (!bootRomVersion.isEmpty())) {
/* 127 */       firmware.setVersion(bootRomVersion);
/*     */     }
/*     */     
/* 130 */     setFirmware(firmware);
/* 131 */     setBaseboard(baseboard);
/*     */   }
/*     */   
/*     */   private String getSystemSerialNumber() {
/* 135 */     String serialNumber = null;
/* 136 */     int service = IOKitUtil.getMatchingService("IOPlatformExpertDevice");
/* 137 */     if (service != 0)
/*     */     {
/* 139 */       serialNumber = IOKitUtil.getIORegistryStringProperty(service, "IOPlatformSerialNumber");
/* 140 */       IOKit.INSTANCE.IOObjectRelease(service);
/*     */     }
/* 142 */     if (serialNumber == null) {
/* 143 */       serialNumber = "unknown";
/*     */     }
/* 145 */     return serialNumber;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */