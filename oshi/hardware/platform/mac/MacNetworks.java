/*     */ package oshi.hardware.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.NetworkIF;
/*     */ import oshi.hardware.common.AbstractNetworks;
/*     */ import oshi.jna.platform.mac.SystemB;
/*     */ import oshi.jna.platform.mac.SystemB.IFdata64;
/*     */ import oshi.jna.platform.mac.SystemB.IFmsgHdr;
/*     */ import oshi.jna.platform.mac.SystemB.IFmsgHdr2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacNetworks
/*     */   extends AbstractNetworks
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  44 */   private static final Logger LOG = LoggerFactory.getLogger(MacNetworks.class);
/*     */   
/*     */ 
/*     */   private static final int CTL_NET = 4;
/*     */   
/*     */ 
/*     */   private static final int PF_ROUTE = 17;
/*     */   
/*     */   private static final int NET_RT_IFLIST2 = 6;
/*     */   
/*     */   private static final int RTM_IFINFO2 = 18;
/*     */   
/*  56 */   private static Map<Integer, IFdata> ifMap = new HashMap();
/*  57 */   private static long lastIFmapTime = 0L;
/*     */   
/*     */ 
/*     */   private static class IFdata
/*     */   {
/*     */     private long oPackets;
/*     */     private long iPackets;
/*     */     private long oBytes;
/*     */     private long iBytes;
/*     */     private long oErrors;
/*     */     private long iErrors;
/*     */     private long speed;
/*     */     
/*     */     IFdata(long oPackets, long iPackets, long oBytes, long iBytes, long oErrors, long iErrors, long speed)
/*     */     {
/*  72 */       this.oPackets = oPackets;
/*  73 */       this.iPackets = iPackets;
/*  74 */       this.oBytes = oBytes;
/*  75 */       this.iBytes = iBytes;
/*  76 */       this.oErrors = oErrors;
/*  77 */       this.iErrors = iErrors;
/*  78 */       this.speed = speed;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized long mapIFs()
/*     */   {
/*  90 */     if (System.currentTimeMillis() - lastIFmapTime < 200L)
/*     */     {
/*  92 */       return lastIFmapTime;
/*     */     }
/*     */     
/*     */ 
/*  96 */     int[] mib = { 4, 17, 0, 0, 6, 0 };
/*  97 */     IntByReference len = new IntByReference();
/*  98 */     if (0 != SystemB.INSTANCE.sysctl(mib, 6, null, len, null, 0)) {
/*  99 */       LOG.error("Didn't get buffer length for IFLIST2");
/* 100 */       return lastIFmapTime;
/*     */     }
/* 102 */     Pointer buf = new Memory(len.getValue());
/* 103 */     if (0 != SystemB.INSTANCE.sysctl(mib, 6, buf, len, null, 0)) {
/* 104 */       LOG.error("Didn't get buffer for IFLIST2");
/* 105 */       return lastIFmapTime;
/*     */     }
/* 107 */     lastIFmapTime = System.currentTimeMillis();
/*     */     
/*     */ 
/* 110 */     ifMap.clear();
/*     */     
/*     */ 
/* 113 */     int lim = len.getValue();
/* 114 */     int next = 0;
/* 115 */     while (next < lim)
/*     */     {
/* 117 */       Pointer p = new Pointer(Pointer.nativeValue(buf) + next);
/*     */       
/* 119 */       SystemB.IFmsgHdr ifm = new SystemB.IFmsgHdr(p);
/* 120 */       ifm.read();
/*     */       
/* 122 */       next += ifm.ifm_msglen;
/*     */       
/* 124 */       if (ifm.ifm_type == 18)
/*     */       {
/*     */ 
/*     */ 
/* 128 */         SystemB.IFmsgHdr2 if2m = new SystemB.IFmsgHdr2(p);
/* 129 */         if2m.read();
/*     */         
/*     */ 
/* 132 */         ifMap.put(Integer.valueOf(if2m.ifm_index), new IFdata(if2m.ifm_data.ifi_opackets, if2m.ifm_data.ifi_ipackets, if2m.ifm_data.ifi_obytes, if2m.ifm_data.ifi_ibytes, if2m.ifm_data.ifi_oerrors, if2m.ifm_data.ifi_ierrors, if2m.ifm_data.ifi_baudrate));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 137 */     return lastIFmapTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized void updateNetworkStats(NetworkIF netIF)
/*     */   {
/* 149 */     long timeStamp = mapIFs();
/*     */     
/* 151 */     Integer index = Integer.valueOf(netIF.getNetworkInterface().getIndex());
/* 152 */     IFdata ifData = (IFdata)ifMap.get(index);
/* 153 */     if (ifData != null)
/*     */     {
/* 155 */       netIF.setBytesSent(ifData.oBytes);
/* 156 */       netIF.setBytesRecv(ifData.iBytes);
/* 157 */       netIF.setPacketsSent(ifData.oPackets);
/* 158 */       netIF.setPacketsRecv(ifData.iPackets);
/* 159 */       netIF.setOutErrors(ifData.oErrors);
/* 160 */       netIF.setInErrors(ifData.iErrors);
/* 161 */       netIF.setSpeed(ifData.speed);
/* 162 */       netIF.setTimeStamp(timeStamp);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacNetworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */