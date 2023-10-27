/*    */ package oshi.hardware.platform.unix.freebsd;
/*    */ 
/*    */ import oshi.hardware.common.AbstractFirmware;
/*    */ import oshi.util.ExecutingCommand;
/*    */ import oshi.util.FormatUtil;
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
/*    */ final class FreeBsdFirmware
/*    */   extends AbstractFirmware
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   FreeBsdFirmware()
/*    */   {
/* 30 */     init();
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
/*    */   private void init()
/*    */   {
/* 49 */     String manufacturer = "";
/* 50 */     String manufacturerMarker = "Vendor:";
/* 51 */     String version = "";
/* 52 */     String versionMarker = "Version:";
/* 53 */     String releaseDate = "";
/* 54 */     String releaseDateMarker = "Release Date:";
/*    */     
/*    */ 
/* 57 */     for (String checkLine : ExecutingCommand.runNative("dmidecode -t bios")) {
/* 58 */       if (checkLine.contains("Vendor:")) {
/* 59 */         manufacturer = checkLine.split("Vendor:")[1].trim();
/* 60 */       } else if (checkLine.contains("Version:")) {
/* 61 */         version = checkLine.split("Version:")[1].trim();
/* 62 */       } else if (checkLine.contains("Release Date:")) {
/* 63 */         releaseDate = checkLine.split("Release Date:")[1].trim();
/*    */       }
/*    */     }
/* 66 */     if (!manufacturer.isEmpty()) {
/* 67 */       setManufacturer(manufacturer);
/*    */     }
/* 69 */     if (!version.isEmpty()) {
/* 70 */       setVersion(version);
/*    */     }
/* 72 */     if (!releaseDate.isEmpty()) {
/* 73 */       setReleaseDate(FormatUtil.formatStringDate(releaseDate));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdFirmware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */