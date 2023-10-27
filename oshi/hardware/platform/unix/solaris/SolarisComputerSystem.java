/*     */ package oshi.hardware.platform.unix.solaris;
/*     */ 
/*     */ import oshi.hardware.common.AbstractComputerSystem;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.FormatUtil;
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
/*     */ final class SolarisComputerSystem
/*     */   extends AbstractComputerSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String UNKNOWN = "unknown";
/*     */   
/*     */   SolarisComputerSystem()
/*     */   {
/*  38 */     init();
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
/*     */   private void init()
/*     */   {
/*  79 */     String vendor = "";
/*  80 */     String vendorMarker = "Vendor:";
/*  81 */     String biosDate = "";
/*  82 */     String biosDateMarker = "Release Date:";
/*  83 */     String biosVersion = "";
/*  84 */     String biosVersionMarker = "VersionString:";
/*     */     
/*  86 */     String manufacturer = "";
/*  87 */     String boardManufacturer = "";
/*  88 */     String manufacturerMarker = "Manufacturer:";
/*  89 */     String product = "";
/*  90 */     String model = "";
/*  91 */     String productMarker = "Product:";
/*  92 */     String version = "";
/*  93 */     String versionMarker = "Version:";
/*  94 */     String serialNumber = "";
/*  95 */     String boardSerialNumber = "";
/*  96 */     String serialNumMarker = "Serial Number:";
/*     */     
/*  98 */     SolarisFirmware firmware = new SolarisFirmware();
/*  99 */     SolarisBaseboard baseboard = new SolarisBaseboard();
/*     */     
/* 101 */     boolean smbTypeBIOS = false;
/* 102 */     boolean smbTypeSystem = false;
/* 103 */     boolean smbTypeBaseboard = false;
/*     */     
/* 105 */     for (String checkLine : ExecutingCommand.runNative("smbios"))
/*     */     {
/* 107 */       if (checkLine.contains("SMB_TYPE_")) {
/* 108 */         if (checkLine.contains("SMB_TYPE_BIOS")) {
/* 109 */           smbTypeBIOS = true;
/* 110 */           smbTypeSystem = false;
/* 111 */           smbTypeBaseboard = false;
/* 112 */         } else if (checkLine.contains("SMB_TYPE_SYSTEM")) {
/* 113 */           smbTypeBIOS = false;
/* 114 */           smbTypeSystem = true;
/* 115 */           smbTypeBaseboard = false;
/* 116 */         } else { if (!checkLine.contains("SMB_TYPE_BASEBOARD")) break;
/* 117 */           smbTypeBIOS = false;
/* 118 */           smbTypeSystem = false;
/* 119 */           smbTypeBaseboard = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 125 */       if (smbTypeBIOS) {
/* 126 */         if (checkLine.contains("Vendor:")) {
/* 127 */           vendor = checkLine.split("Vendor:")[1].trim();
/* 128 */         } else if (checkLine.contains("VersionString:")) {
/* 129 */           biosVersion = checkLine.split("VersionString:")[1].trim();
/* 130 */         } else if (checkLine.contains("Release Date:")) {
/* 131 */           biosDate = checkLine.split("Release Date:")[1].trim();
/*     */         }
/* 133 */       } else if (smbTypeSystem) {
/* 134 */         if (checkLine.contains("Manufacturer:")) {
/* 135 */           manufacturer = checkLine.split("Manufacturer:")[1].trim();
/* 136 */         } else if (checkLine.contains("Product:")) {
/* 137 */           product = checkLine.split("Product:")[1].trim();
/* 138 */         } else if (checkLine.contains("Serial Number:")) {
/* 139 */           serialNumber = checkLine.split("Serial Number:")[1].trim();
/*     */         }
/* 141 */       } else if (smbTypeBaseboard) {
/* 142 */         if (checkLine.contains("Manufacturer:")) {
/* 143 */           boardManufacturer = checkLine.split("Manufacturer:")[1].trim();
/* 144 */         } else if (checkLine.contains("Product:")) {
/* 145 */           model = checkLine.split("Product:")[1].trim();
/* 146 */         } else if (checkLine.contains("Version:")) {
/* 147 */           version = checkLine.split("Version:")[1].trim();
/* 148 */         } else if (checkLine.contains("Serial Number:")) {
/* 149 */           boardSerialNumber = checkLine.split("Serial Number:")[1].trim();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 154 */     if (!vendor.isEmpty()) {
/* 155 */       firmware.setManufacturer(vendor);
/*     */     }
/* 157 */     if (!biosVersion.isEmpty()) {
/* 158 */       firmware.setVersion(biosVersion);
/*     */     }
/* 160 */     if (!biosDate.isEmpty()) {
/* 161 */       firmware.setReleaseDate(FormatUtil.formatStringDate(biosDate));
/*     */     }
/*     */     
/* 164 */     if (!manufacturer.isEmpty()) {
/* 165 */       setManufacturer(manufacturer);
/*     */     }
/* 167 */     if (!product.isEmpty()) {
/* 168 */       setModel(product);
/*     */     }
/* 170 */     if (serialNumber.isEmpty()) {
/* 171 */       serialNumber = getSystemSerialNumber();
/*     */     }
/* 173 */     setSerialNumber(serialNumber);
/*     */     
/* 175 */     if (!boardManufacturer.isEmpty()) {
/* 176 */       baseboard.setManufacturer(boardManufacturer);
/*     */     }
/* 178 */     if (!model.isEmpty()) {
/* 179 */       baseboard.setModel(model);
/*     */     }
/* 181 */     if (!version.isEmpty()) {
/* 182 */       baseboard.setVersion(version);
/*     */     }
/* 184 */     if (!boardSerialNumber.isEmpty()) {
/* 185 */       baseboard.setSerialNumber(boardSerialNumber);
/*     */     }
/*     */     
/* 188 */     setFirmware(firmware);
/* 189 */     setBaseboard(baseboard);
/*     */   }
/*     */   
/*     */   private String getSystemSerialNumber()
/*     */   {
/* 194 */     String serialNumber = ExecutingCommand.getFirstAnswer("sneep");
/*     */     String marker;
/* 196 */     if (serialNumber.isEmpty()) {
/* 197 */       marker = "chassis-sn:";
/* 198 */       for (String checkLine : ExecutingCommand.runNative("prtconf -pv")) {
/* 199 */         if (checkLine.contains(marker)) {
/* 200 */           serialNumber = ParseUtil.getSingleQuoteStringValue(checkLine);
/* 201 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 205 */     if (serialNumber.isEmpty()) {
/* 206 */       serialNumber = "unknown";
/*     */     }
/* 208 */     return serialNumber;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */