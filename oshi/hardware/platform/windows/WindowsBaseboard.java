/*    */ package oshi.hardware.platform.windows;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import oshi.hardware.common.AbstractBaseboard;
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
/*    */ public class WindowsBaseboard
/*    */   extends AbstractBaseboard
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   WindowsBaseboard()
/*    */   {
/* 37 */     init();
/*    */   }
/*    */   
/*    */   private void init()
/*    */   {
/* 42 */     Map<String, List<String>> win32BaseBoard = WmiUtil.selectStringsFrom(null, "Win32_BaseBoard", "Manufacturer,Model,Version,SerialNumber", null);
/*    */     
/*    */ 
/* 45 */     List<String> baseboardManufacturers = (List)win32BaseBoard.get("Manufacturer");
/* 46 */     if ((baseboardManufacturers != null) && (!baseboardManufacturers.isEmpty())) {
/* 47 */       setManufacturer((String)baseboardManufacturers.get(0));
/*    */     }
/*    */     
/* 50 */     List<String> baseboardModels = (List)win32BaseBoard.get("Model");
/* 51 */     if ((baseboardModels != null) && (!baseboardModels.isEmpty())) {
/* 52 */       setModel((String)baseboardModels.get(0));
/*    */     }
/*    */     
/* 55 */     List<String> baseboardVersions = (List)win32BaseBoard.get("Version");
/* 56 */     if ((baseboardVersions != null) && (!baseboardVersions.isEmpty())) {
/* 57 */       setVersion((String)baseboardVersions.get(0));
/*    */     }
/*    */     
/* 60 */     List<String> baseboardSerials = (List)win32BaseBoard.get("SerialNumber");
/* 61 */     if ((baseboardSerials != null) && (!baseboardSerials.isEmpty())) {
/* 62 */       setSerialNumber((String)baseboardSerials.get(0));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsBaseboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */