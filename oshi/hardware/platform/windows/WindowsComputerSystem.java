/*    */ package oshi.hardware.platform.windows;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import oshi.hardware.common.AbstractComputerSystem;
/*    */ import oshi.util.platform.windows.WmiUtil;
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
/*    */ final class WindowsComputerSystem
/*    */   extends AbstractComputerSystem
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   WindowsComputerSystem()
/*    */   {
/* 38 */     init();
/*    */   }
/*    */   
/*    */   private void init()
/*    */   {
/* 43 */     Map<String, List<String>> win32ComputerSystem = WmiUtil.selectStringsFrom(null, "Win32_ComputerSystem", "Manufacturer,Model", null);
/*    */     
/*    */ 
/* 46 */     List<String> manufacturers = (List)win32ComputerSystem.get("Manufacturer");
/* 47 */     if ((manufacturers != null) && (!manufacturers.isEmpty())) {
/* 48 */       setManufacturer((String)manufacturers.get(0));
/*    */     }
/*    */     
/* 51 */     List<String> models = (List)win32ComputerSystem.get("Model");
/* 52 */     if ((models != null) && (!models.isEmpty())) {
/* 53 */       setModel((String)models.get(0));
/*    */     }
/*    */     
/* 56 */     setSerialNumber(getSystemSerialNumber());
/*    */     
/* 58 */     setFirmware(new WindowsFirmware());
/*    */     
/* 60 */     setBaseboard(new WindowsBaseboard());
/*    */   }
/*    */   
/*    */   private String getSystemSerialNumber()
/*    */   {
/* 65 */     String serialNumber = WmiUtil.selectStringFrom(null, "Win32_BIOS", "SerialNumber", "where PrimaryBIOS=true");
/*    */     
/* 67 */     if ("".equals(serialNumber)) {
/* 68 */       serialNumber = WmiUtil.selectStringFrom(null, "Win32_Csproduct", "IdentifyingNumber", null);
/*    */     }
/* 70 */     if ("".equals(serialNumber)) {
/* 71 */       serialNumber = "unknown";
/*    */     }
/* 73 */     return serialNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */