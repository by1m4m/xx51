/*    */ package oshi.hardware.platform.linux;
/*    */ 
/*    */ import oshi.hardware.common.AbstractBaseboard;
/*    */ import oshi.util.FileUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LinuxBaseboard
/*    */   extends AbstractBaseboard
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final String SYSFS_SERIAL_PATH = "/sys/devices/virtual/dmi/id/";
/*    */   
/*    */   LinuxBaseboard()
/*    */   {
/* 38 */     init();
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
/*    */   private void init()
/*    */   {
/* 51 */     String boardVendor = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_vendor");
/* 52 */     if ((boardVendor != null) && (!boardVendor.trim().isEmpty())) {
/* 53 */       setManufacturer(boardVendor.trim());
/*    */     }
/*    */     
/* 56 */     String boardName = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_name");
/* 57 */     if ((boardName != null) && (!boardName.trim().isEmpty())) {
/* 58 */       setModel(boardName.trim());
/*    */     }
/*    */     
/* 61 */     String boardVersion = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_version");
/* 62 */     if ((boardVersion != null) && (!boardVersion.trim().isEmpty())) {
/* 63 */       setVersion(boardVersion.trim());
/*    */     }
/*    */     
/* 66 */     String boardSerialNumber = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_serial");
/* 67 */     if ((boardSerialNumber != null) && (!boardSerialNumber.trim().isEmpty())) {
/* 68 */       setSerialNumber(boardSerialNumber.trim());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxBaseboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */