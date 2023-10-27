/*    */ package oshi.software.os.mac;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.software.common.AbstractOSVersionInfoEx;
/*    */ import oshi.util.ParseUtil;
/*    */ import oshi.util.platform.mac.SysctlUtil;
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
/*    */ public class MacOSVersionInfoEx
/*    */   extends AbstractOSVersionInfoEx
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 32 */   private static final Logger LOG = LoggerFactory.getLogger(MacOSVersionInfoEx.class);
/*    */   
/* 34 */   private int osxVersionNumber = -1;
/*    */   
/*    */   public MacOSVersionInfoEx() {
/* 37 */     setVersion(System.getProperty("os.version"));
/* 38 */     setCodeName(parseCodeName());
/* 39 */     setBuildNumber(SysctlUtil.sysctl("kern.osversion", ""));
/*    */   }
/*    */   
/*    */   public int getOsxVersionNumber() {
/* 43 */     return this.osxVersionNumber;
/*    */   }
/*    */   
/*    */   private String parseCodeName() {
/* 47 */     if (ParseUtil.getFirstIntValue(getVersion()) == 10) {
/* 48 */       this.osxVersionNumber = ParseUtil.getNthIntValue(getVersion(), 2);
/* 49 */       switch (this.osxVersionNumber)
/*    */       {
/*    */       case 12: 
/* 52 */         return "Sierra";
/*    */       
/*    */       case 11: 
/* 55 */         return "El Capitan";
/*    */       case 10: 
/* 57 */         return "Yosemite";
/*    */       case 9: 
/* 59 */         return "Mavericks";
/*    */       case 8: 
/* 61 */         return "Mountain Lion";
/*    */       case 7: 
/* 63 */         return "Lion";
/*    */       case 6: 
/* 65 */         return "Snow Leopard";
/*    */       case 5: 
/* 67 */         return "Leopard";
/*    */       case 4: 
/* 69 */         return "Tiger";
/*    */       case 3: 
/* 71 */         return "Panther";
/*    */       case 2: 
/* 73 */         return "Jaguar";
/*    */       case 1: 
/* 75 */         return "Puma";
/*    */       case 0: 
/* 77 */         return "Cheetah";
/*    */       }
/*    */       
/*    */     }
/*    */     
/* 82 */     LOG.warn("Unable to parse version {} to a codename.", getVersion());
/* 83 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\mac\MacOSVersionInfoEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */