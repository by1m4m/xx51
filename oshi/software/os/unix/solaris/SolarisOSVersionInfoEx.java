/*    */ package oshi.software.os.unix.solaris;
/*    */ 
/*    */ import oshi.software.common.AbstractOSVersionInfoEx;
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
/*    */ 
/*    */ public class SolarisOSVersionInfoEx
/*    */   extends AbstractOSVersionInfoEx
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public SolarisOSVersionInfoEx()
/*    */   {
/* 30 */     String versionInfo = ExecutingCommand.getFirstAnswer("uname -rv");
/* 31 */     String[] split = versionInfo.split("\\s+");
/* 32 */     setVersion(split[0]);
/* 33 */     if (split.length > 1) {
/* 34 */       setBuildNumber(split[1]);
/*    */     }
/* 36 */     setCodeName("Solaris");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\solaris\SolarisOSVersionInfoEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */