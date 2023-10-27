/*    */ package oshi.hardware.platform.unix.freebsd;
/*    */ 
/*    */ import oshi.hardware.common.AbstractBaseboard;
/*    */ import oshi.util.ExecutingCommand;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FreeBsdBaseboard
/*    */   extends AbstractBaseboard
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   FreeBsdBaseboard()
/*    */   {
/* 29 */     init();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void init()
/*    */   {
/* 55 */     String manufacturer = "";
/* 56 */     String manufacturerMarker = "Manufacturer:";
/* 57 */     String model = "";
/* 58 */     String productNameMarker = "Product Name:";
/* 59 */     String version = "";
/* 60 */     String versionMarker = "Version:";
/* 61 */     String serialNumber = "";
/* 62 */     String serialNumMarker = "Serial Number:";
/*    */     
/*    */ 
/* 65 */     for (String checkLine : ExecutingCommand.runNative("dmidecode -t baseboard")) {
/* 66 */       if (checkLine.contains("Manufacturer:")) {
/* 67 */         manufacturer = checkLine.split("Manufacturer:")[1].trim();
/*    */       }
/* 69 */       if (checkLine.contains("Product Name:")) {
/* 70 */         model = checkLine.split("Product Name:")[1].trim();
/*    */       }
/* 72 */       if (checkLine.contains("Version:")) {
/* 73 */         version = checkLine.split("Version:")[1].trim();
/*    */       }
/* 75 */       if (checkLine.contains("Serial Number:")) {
/* 76 */         serialNumber = checkLine.split("Serial Number:")[1].trim();
/*    */       }
/*    */     }
/* 79 */     if (!manufacturer.isEmpty()) {
/* 80 */       setManufacturer(manufacturer);
/*    */     }
/* 82 */     if (!model.isEmpty()) {
/* 83 */       setModel(model);
/*    */     }
/* 85 */     if (!version.isEmpty()) {
/* 86 */       setVersion(version);
/*    */     }
/* 88 */     if (!serialNumber.isEmpty()) {
/* 89 */       setSerialNumber(serialNumber);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdBaseboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */