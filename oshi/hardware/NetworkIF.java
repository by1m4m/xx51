/*     */ package oshi.hardware;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.StringJoiner;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.platform.linux.LinuxNetworks;
/*     */ import oshi.hardware.platform.mac.MacNetworks;
/*     */ import oshi.hardware.platform.unix.freebsd.FreeBsdNetworks;
/*     */ import oshi.hardware.platform.unix.solaris.SolarisNetworks;
/*     */ import oshi.hardware.platform.windows.WindowsNetworks;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NetworkIF
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  49 */   private static final Logger LOG = LoggerFactory.getLogger(NetworkIF.class);
/*     */   
/*     */   private transient NetworkInterface networkInterface;
/*     */   
/*     */   private int mtu;
/*     */   
/*     */   private String mac;
/*     */   private String[] ipv4;
/*     */   private String[] ipv6;
/*     */   private long bytesRecv;
/*     */   private long bytesSent;
/*     */   private long packetsRecv;
/*     */   private long packetsSent;
/*     */   private long inErrors;
/*     */   private long outErrors;
/*     */   private long speed;
/*     */   private long timeStamp;
/*     */   
/*     */   public NetworkInterface getNetworkInterface()
/*     */   {
/*  69 */     return this.networkInterface;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNetworkInterface(NetworkInterface networkInterface)
/*     */   {
/*  80 */     this.networkInterface = networkInterface;
/*     */     try
/*     */     {
/*  83 */       this.mtu = networkInterface.getMTU();
/*     */       
/*  85 */       byte[] hwmac = networkInterface.getHardwareAddress();
/*  86 */       if (hwmac != null) {
/*  87 */         StringJoiner sj = new StringJoiner(":");
/*  88 */         for (byte b : hwmac) {
/*  89 */           sj.add(String.format("%02x", new Object[] { Byte.valueOf(b) }));
/*     */         }
/*  91 */         this.mac = sj.toString();
/*     */       } else {
/*  93 */         this.mac = "Unknown";
/*     */       }
/*     */       
/*  96 */       ArrayList<String> ipv4list = new ArrayList();
/*  97 */       Object ipv6list = new ArrayList();
/*  98 */       for (InetAddress address : Collections.list(networkInterface.getInetAddresses())) {
/*  99 */         if (address.getHostAddress().length() != 0)
/*     */         {
/* 101 */           if (address.getHostAddress().contains(":")) {
/* 102 */             ((ArrayList)ipv6list).add(address.getHostAddress().split("%")[0]);
/*     */           } else
/* 104 */             ipv4list.add(address.getHostAddress());
/*     */         }
/*     */       }
/* 107 */       this.ipv4 = ((String[])ipv4list.toArray(new String[ipv4list.size()]));
/* 108 */       this.ipv6 = ((String[])((ArrayList)ipv6list).toArray(new String[((ArrayList)ipv6list).size()]));
/*     */     } catch (SocketException e) {
/* 110 */       LOG.error("Socket exception: {}", e);
/* 111 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 119 */     return this.networkInterface.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 127 */     return this.networkInterface.getDisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMTU()
/*     */   {
/* 137 */     return this.mtu;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMacaddr()
/*     */   {
/* 146 */     return this.mac;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getIPv4addr()
/*     */   {
/* 155 */     return (String[])Arrays.copyOf(this.ipv4, this.ipv4.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getIPv6addr()
/*     */   {
/* 164 */     return (String[])Arrays.copyOf(this.ipv6, this.ipv6.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getBytesRecv()
/*     */   {
/* 173 */     return this.bytesRecv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBytesRecv(long bytesRecv)
/*     */   {
/* 181 */     this.bytesRecv = bytesRecv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getBytesSent()
/*     */   {
/* 190 */     return this.bytesSent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBytesSent(long bytesSent)
/*     */   {
/* 198 */     this.bytesSent = bytesSent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getPacketsRecv()
/*     */   {
/* 208 */     return this.packetsRecv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacketsRecv(long packetsRecv)
/*     */   {
/* 216 */     this.packetsRecv = packetsRecv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getPacketsSent()
/*     */   {
/* 225 */     return this.packetsSent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacketsSent(long packetsSent)
/*     */   {
/* 233 */     this.packetsSent = packetsSent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getInErrors()
/*     */   {
/* 242 */     return this.inErrors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInErrors(long inErrors)
/*     */   {
/* 250 */     this.inErrors = inErrors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getOutErrors()
/*     */   {
/* 259 */     return this.outErrors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOutErrors(long outErrors)
/*     */   {
/* 267 */     this.outErrors = outErrors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSpeed()
/*     */   {
/* 277 */     return this.speed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpeed(long speed)
/*     */   {
/* 285 */     this.speed = speed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getTimeStamp()
/*     */   {
/* 292 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeStamp(long timeStamp)
/*     */   {
/* 300 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateNetworkStats()
/*     */   {
/* 308 */     switch (oshi.SystemInfo.getCurrentPlatformEnum()) {
/*     */     case WINDOWS: 
/* 310 */       WindowsNetworks.updateNetworkStats(this);
/* 311 */       break;
/*     */     case LINUX: 
/* 313 */       LinuxNetworks.updateNetworkStats(this);
/* 314 */       break;
/*     */     case MACOSX: 
/* 316 */       MacNetworks.updateNetworkStats(this);
/* 317 */       break;
/*     */     case SOLARIS: 
/* 319 */       SolarisNetworks.updateNetworkStats(this);
/* 320 */       break;
/*     */     case FREEBSD: 
/* 322 */       FreeBsdNetworks.updateNetworkStats(this);
/* 323 */       break;
/*     */     default: 
/* 325 */       LOG.error("Unsupported platform. No update performed.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\NetworkIF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */