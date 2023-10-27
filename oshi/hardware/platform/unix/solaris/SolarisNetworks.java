/*    */ package oshi.hardware.platform.unix.solaris;
/*    */ 
/*    */ import oshi.hardware.NetworkIF;
/*    */ import oshi.hardware.common.AbstractNetworks;
/*    */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*    */ import oshi.util.platform.unix.solaris.KstatUtil;
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
/*    */ 
/*    */ 
/*    */ public class SolarisNetworks
/*    */   extends AbstractNetworks
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static void updateNetworkStats(NetworkIF netIF)
/*    */   {
/* 41 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup("link", -1, netIF.getName());
/* 42 */     if ((ksp != null) && (KstatUtil.kstatRead(ksp))) {
/* 43 */       netIF.setBytesSent(KstatUtil.kstatDataLookupLong(ksp, "obytes64"));
/* 44 */       netIF.setBytesRecv(KstatUtil.kstatDataLookupLong(ksp, "rbytes64"));
/* 45 */       netIF.setPacketsSent(KstatUtil.kstatDataLookupLong(ksp, "opackets64"));
/* 46 */       netIF.setPacketsRecv(KstatUtil.kstatDataLookupLong(ksp, "ipackets64"));
/* 47 */       netIF.setOutErrors(KstatUtil.kstatDataLookupLong(ksp, "oerrors"));
/* 48 */       netIF.setInErrors(KstatUtil.kstatDataLookupLong(ksp, "ierrors"));
/* 49 */       netIF.setSpeed(KstatUtil.kstatDataLookupLong(ksp, "ifspeed"));
/*    */       
/* 51 */       netIF.setTimeStamp(ksp.ks_snaptime / 1000000L);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisNetworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */