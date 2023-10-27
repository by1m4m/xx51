/*    */ package oshi.hardware.platform.linux;
/*    */ 
/*    */ import oshi.hardware.NetworkIF;
/*    */ import oshi.hardware.common.AbstractNetworks;
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
/*    */ 
/*    */ 
/*    */ public class LinuxNetworks
/*    */   extends AbstractNetworks
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static void updateNetworkStats(NetworkIF netIF)
/*    */   {
/* 40 */     String txBytesPath = String.format("/sys/class/net/%s/statistics/tx_bytes", new Object[] { netIF.getName() });
/* 41 */     String rxBytesPath = String.format("/sys/class/net/%s/statistics/rx_bytes", new Object[] { netIF.getName() });
/* 42 */     String txPacketsPath = String.format("/sys/class/net/%s/statistics/tx_packets", new Object[] { netIF.getName() });
/* 43 */     String rxPacketsPath = String.format("/sys/class/net/%s/statistics/rx_packets", new Object[] { netIF.getName() });
/* 44 */     String txErrorsPath = String.format("/sys/class/net/%s/statistics/tx_errors", new Object[] { netIF.getName() });
/* 45 */     String rxErrorsPath = String.format("/sys/class/net/%s/statistics/rx_errors", new Object[] { netIF.getName() });
/* 46 */     String speed = String.format("/sys/class/net/%s/speed", new Object[] { netIF.getName() });
/*    */     
/* 48 */     netIF.setTimeStamp(System.currentTimeMillis());
/* 49 */     netIF.setBytesSent(FileUtil.getLongFromFile(txBytesPath));
/* 50 */     netIF.setBytesRecv(FileUtil.getLongFromFile(rxBytesPath));
/* 51 */     netIF.setPacketsSent(FileUtil.getLongFromFile(txPacketsPath));
/* 52 */     netIF.setPacketsRecv(FileUtil.getLongFromFile(rxPacketsPath));
/* 53 */     netIF.setOutErrors(FileUtil.getLongFromFile(txErrorsPath));
/* 54 */     netIF.setInErrors(FileUtil.getLongFromFile(rxErrorsPath));
/* 55 */     netIF.setSpeed(FileUtil.getLongFromFile(speed));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxNetworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */