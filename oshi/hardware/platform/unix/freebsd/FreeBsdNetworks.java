/*    */ package oshi.hardware.platform.unix.freebsd;
/*    */ 
/*    */ import oshi.hardware.NetworkIF;
/*    */ import oshi.hardware.common.AbstractNetworks;
/*    */ import oshi.util.ExecutingCommand;
/*    */ import oshi.util.ParseUtil;
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
/*    */ public class FreeBsdNetworks
/*    */   extends AbstractNetworks
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static void updateNetworkStats(NetworkIF netIF)
/*    */   {
/* 41 */     String stats = ExecutingCommand.getAnswerAt("netstat -bI " + netIF.getName(), 1);
/* 42 */     netIF.setTimeStamp(System.currentTimeMillis());
/* 43 */     String[] split = stats.split("\\s+");
/* 44 */     if (split.length < 12)
/*    */     {
/* 46 */       return;
/*    */     }
/* 48 */     netIF.setBytesSent(ParseUtil.parseLongOrDefault(split[10], 0L));
/* 49 */     netIF.setBytesRecv(ParseUtil.parseLongOrDefault(split[7], 0L));
/* 50 */     netIF.setPacketsSent(ParseUtil.parseLongOrDefault(split[8], 0L));
/* 51 */     netIF.setPacketsRecv(ParseUtil.parseLongOrDefault(split[4], 0L));
/* 52 */     netIF.setOutErrors(ParseUtil.parseLongOrDefault(split[9], 0L));
/* 53 */     netIF.setInErrors(ParseUtil.parseLongOrDefault(split[5], 0L));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdNetworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */