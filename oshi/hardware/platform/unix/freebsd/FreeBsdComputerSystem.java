/*     */ package oshi.hardware.platform.unix.freebsd;
/*     */ 
/*     */ import oshi.hardware.common.AbstractComputerSystem;
/*     */ import oshi.util.ExecutingCommand;
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
/*     */ final class FreeBsdComputerSystem
/*     */   extends AbstractComputerSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   FreeBsdComputerSystem()
/*     */   {
/*  35 */     init();
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
/*     */   private void init()
/*     */   {
/*  61 */     String manufacturer = "";
/*  62 */     String manufacturerMarker = "Manufacturer:";
/*  63 */     String productName = "";
/*  64 */     String productNameMarker = "Product Name:";
/*  65 */     String serialNumber = "";
/*  66 */     String serialNumMarker = "Serial Number:";
/*     */     
/*     */ 
/*  69 */     for (String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
/*  70 */       if (checkLine.contains("Manufacturer:")) {
/*  71 */         manufacturer = checkLine.split("Manufacturer:")[1].trim();
/*     */       }
/*  73 */       if (checkLine.contains("Product Name:")) {
/*  74 */         productName = checkLine.split("Product Name:")[1].trim();
/*     */       }
/*  76 */       if (checkLine.contains("Serial Number:")) {
/*  77 */         serialNumber = checkLine.split("Serial Number:")[1].trim();
/*     */       }
/*     */     }
/*  80 */     if (!manufacturer.isEmpty()) {
/*  81 */       setManufacturer(manufacturer);
/*     */     }
/*  83 */     if (!productName.isEmpty()) {
/*  84 */       setModel(productName);
/*     */     }
/*  86 */     if (serialNumber.isEmpty()) {
/*  87 */       serialNumber = getSystemSerialNumber();
/*     */     }
/*  89 */     setSerialNumber(serialNumber);
/*     */     
/*  91 */     setFirmware(new FreeBsdFirmware());
/*     */     
/*  93 */     setBaseboard(new FreeBsdBaseboard());
/*     */   }
/*     */   
/*     */   private String getSystemSerialNumber() {
/*  97 */     String marker = "system.hardware.serial =";
/*  98 */     for (String checkLine : ExecutingCommand.runNative("lshal")) {
/*  99 */       if (checkLine.contains(marker)) {
/* 100 */         return ParseUtil.getSingleQuoteStringValue(checkLine);
/*     */       }
/*     */     }
/* 103 */     return "unknown";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */