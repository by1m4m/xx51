/*    */ package oshi.software.os.unix.freebsd;
/*    */ 
/*    */ import oshi.software.common.AbstractOSVersionInfoEx;
/*    */ import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
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
/*    */ public class FreeBsdOSVersionInfoEx
/*    */   extends AbstractOSVersionInfoEx
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public FreeBsdOSVersionInfoEx()
/*    */   {
/* 29 */     setVersion(BsdSysctlUtil.sysctl("kern.osrelease", ""));
/* 30 */     String versionInfo = BsdSysctlUtil.sysctl("kern.version", "");
/* 31 */     String osType = BsdSysctlUtil.sysctl("kern.ostype", "FreeBSD");
/* 32 */     setBuildNumber(versionInfo.split(":")[0].replace(osType, "").replace(getVersion(), "").trim());
/* 33 */     setCodeName("");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\freebsd\FreeBsdOSVersionInfoEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */