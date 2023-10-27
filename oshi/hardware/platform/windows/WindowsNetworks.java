/*     */ package oshi.hardware.platform.windows;
/*     */ 
/*     */ import com.sun.jna.platform.win32.WinDef.DWORD;
/*     */ import com.sun.jna.platform.win32.WinDef.ULONG;
/*     */ import com.sun.jna.platform.win32.WinNT.OSVERSIONINFO;
/*     */ import java.net.NetworkInterface;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.NetworkIF;
/*     */ import oshi.hardware.common.AbstractNetworks;
/*     */ import oshi.jna.platform.windows.IPHlpAPI;
/*     */ import oshi.jna.platform.windows.IPHlpAPI.MIB_IFROW;
/*     */ import oshi.jna.platform.windows.IPHlpAPI.MIB_IFROW2;
/*     */ import oshi.jna.platform.windows.Kernel32;
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
/*     */ public class WindowsNetworks
/*     */   extends AbstractNetworks
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsNetworks.class);
/*     */   private static final byte majorVersion;
/*     */   
/*     */   static
/*     */   {
/*  46 */     WinNT.OSVERSIONINFO lpVersionInfo = new WinNT.OSVERSIONINFO();
/*     */     
/*     */ 
/*  49 */     if (!Kernel32.INSTANCE.GetVersionEx(lpVersionInfo)) {
/*  50 */       majorVersion = lpVersionInfo.dwMajorVersion.byteValue();
/*     */     } else {
/*  52 */       majorVersion = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void updateNetworkStats(NetworkIF netIF)
/*     */   {
/*  65 */     if (majorVersion >= 6)
/*     */     {
/*  67 */       IPHlpAPI.MIB_IFROW2 ifRow = new IPHlpAPI.MIB_IFROW2();
/*  68 */       ifRow.InterfaceIndex = new WinDef.ULONG(netIF.getNetworkInterface().getIndex());
/*  69 */       if (0 != IPHlpAPI.INSTANCE.GetIfEntry2(ifRow))
/*     */       {
/*  71 */         LOG.error("Failed to retrieve data for interface {}, {}", Integer.valueOf(netIF.getNetworkInterface().getIndex()), netIF
/*  72 */           .getName());
/*  73 */         return;
/*     */       }
/*  75 */       netIF.setBytesSent(ifRow.OutOctets);
/*  76 */       netIF.setBytesRecv(ifRow.InOctets);
/*  77 */       netIF.setPacketsSent(ifRow.OutUcastPkts);
/*  78 */       netIF.setPacketsRecv(ifRow.InUcastPkts);
/*  79 */       netIF.setOutErrors(ifRow.OutErrors);
/*  80 */       netIF.setInErrors(ifRow.InErrors);
/*  81 */       netIF.setSpeed(ifRow.ReceiveLinkSpeed);
/*     */     }
/*     */     else {
/*  84 */       IPHlpAPI.MIB_IFROW ifRow = new IPHlpAPI.MIB_IFROW();
/*  85 */       ifRow.dwIndex = netIF.getNetworkInterface().getIndex();
/*  86 */       if (0 != IPHlpAPI.INSTANCE.GetIfEntry(ifRow))
/*     */       {
/*  88 */         LOG.error("Failed to retrieve data for interface {}, {}", Integer.valueOf(netIF.getNetworkInterface().getIndex()), netIF
/*  89 */           .getName());
/*  90 */         return;
/*     */       }
/*  92 */       netIF.setBytesSent(ifRow.dwOutOctets);
/*  93 */       netIF.setBytesRecv(ifRow.dwInOctets);
/*  94 */       netIF.setPacketsSent(ifRow.dwOutUcastPkts);
/*  95 */       netIF.setPacketsRecv(ifRow.dwInUcastPkts);
/*  96 */       netIF.setOutErrors(ifRow.dwOutErrors);
/*  97 */       netIF.setInErrors(ifRow.dwInErrors);
/*  98 */       netIF.setSpeed(ifRow.dwSpeed);
/*     */     }
/* 100 */     netIF.setTimeStamp(System.currentTimeMillis());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsNetworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */