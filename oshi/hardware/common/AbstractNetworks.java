/*    */ package oshi.hardware.common;
/*    */ 
/*    */ import java.net.NetworkInterface;
/*    */ import java.net.SocketException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Enumeration;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.NetworkIF;
/*    */ import oshi.hardware.Networks;
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
/*    */ public abstract class AbstractNetworks
/*    */   implements Networks
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 43 */   private static final Logger LOG = LoggerFactory.getLogger(AbstractNetworks.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public NetworkIF[] getNetworks()
/*    */   {
/* 50 */     List<NetworkIF> result = new ArrayList();
/*    */     try {
/* 52 */       Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/* 53 */       for (NetworkInterface netint : Collections.list(interfaces)) {
/* 54 */         if ((!netint.isLoopback()) && (netint.getHardwareAddress() != null)) {
/* 55 */           NetworkIF netIF = new NetworkIF();
/* 56 */           netIF.setNetworkInterface(netint);
/* 57 */           netIF.updateNetworkStats();
/* 58 */           result.add(netIF);
/*    */         }
/*    */       }
/*    */     } catch (SocketException ex) {
/* 62 */       LOG.error("Socket exception when retrieving network interfaces: " + ex);
/*    */     }
/* 64 */     return (NetworkIF[])result.toArray(new NetworkIF[result.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractNetworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */