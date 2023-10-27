/*    */ package oshi.hardware.platform.windows;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.time.ZoneOffset;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import oshi.hardware.common.AbstractFirmware;
/*    */ import oshi.util.platform.windows.WmiUtil;
/*    */ import oshi.util.platform.windows.WmiUtil.ValueType;
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
/*    */ final class WindowsFirmware
/*    */   extends AbstractFirmware
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 39 */   private static final WmiUtil.ValueType[] BIOS_TYPES = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.DATETIME };
/*    */   
/*    */   WindowsFirmware()
/*    */   {
/* 43 */     init();
/*    */   }
/*    */   
/*    */   private void init()
/*    */   {
/* 48 */     Map<String, List<Object>> win32BIOS = WmiUtil.selectObjectsFrom(null, "Win32_BIOS", "Manufacturer,Name,Description,Version,ReleaseDate", "where PrimaryBIOS=true", BIOS_TYPES);
/*    */     
/*    */ 
/* 51 */     List<Object> manufacturers = (List)win32BIOS.get("Manufacturer");
/* 52 */     if ((manufacturers != null) && (manufacturers.size() == 1)) {
/* 53 */       setManufacturer((String)manufacturers.get(0));
/*    */     }
/*    */     
/* 56 */     List<Object> names = (List)win32BIOS.get("Name");
/* 57 */     if ((names != null) && (names.size() == 1)) {
/* 58 */       setName((String)names.get(0));
/*    */     }
/*    */     
/* 61 */     List<Object> descriptions = (List)win32BIOS.get("Description");
/* 62 */     if ((descriptions != null) && (descriptions.size() == 1)) {
/* 63 */       setDescription((String)descriptions.get(0));
/*    */     }
/*    */     
/* 66 */     List<Object> version = (List)win32BIOS.get("Version");
/* 67 */     if ((version != null) && (version.size() == 1)) {
/* 68 */       setVersion((String)version.get(0));
/*    */     }
/*    */     
/* 71 */     List<Object> releaseDate = (List)win32BIOS.get("ReleaseDate");
/* 72 */     if ((releaseDate != null) && (releaseDate.size() == 1)) {
/* 73 */       setReleaseDate(Instant.ofEpochMilli(((Long)releaseDate.get(0)).longValue()).atZone(ZoneOffset.UTC).toLocalDate());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsFirmware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */