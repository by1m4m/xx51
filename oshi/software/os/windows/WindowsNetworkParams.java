/*     */ package oshi.software.os.windows;
/*     */ 
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.platform.win32.WinDef.ULONG;
/*     */ import com.sun.jna.platform.win32.WinDef.ULONGByReference;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.windows.IPHlpAPI;
/*     */ import oshi.jna.platform.windows.IPHlpAPI.FIXED_INFO;
/*     */ import oshi.jna.platform.windows.IPHlpAPI.IP_ADDRESS_STRING;
/*     */ import oshi.jna.platform.windows.IPHlpAPI.IP_ADDR_STRING;
/*     */ import oshi.jna.platform.windows.Kernel32;
/*     */ import oshi.software.common.AbstractNetworkParams;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.platform.windows.WmiUtil;
/*     */ import oshi.util.platform.windows.WmiUtil.ValueType;
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
/*     */ public class WindowsNetworkParams
/*     */   extends AbstractNetworkParams
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  43 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsNetworkParams.class);
/*     */   
/*  45 */   private static final WmiUtil.ValueType[] GATEWAY_TYPES = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT16 };
/*     */   
/*     */   private static final String IPV4_DEFAULT_DEST = "0.0.0.0/0";
/*     */   
/*     */   private static final String IPV6_DEFAULT_DEST = "::/0";
/*     */   
/*     */ 
/*     */   public String getDomainName()
/*     */   {
/*  54 */     char[] buffer = new char['Ā'];
/*  55 */     IntByReference bufferSize = new IntByReference(buffer.length);
/*  56 */     if (!Kernel32.INSTANCE.GetComputerNameEx(3, buffer, bufferSize)) {
/*  57 */       LOG.error("Failed to get dns domain name. Error code: {}", Integer.valueOf(Kernel32.INSTANCE.GetLastError()));
/*  58 */       return "";
/*     */     }
/*  60 */     return new String(buffer).trim();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getDnsServers()
/*     */   {
/*  73 */     WinDef.ULONGByReference bufferSize = new WinDef.ULONGByReference();
/*  74 */     int ret = IPHlpAPI.INSTANCE.GetNetworkParams(null, bufferSize);
/*  75 */     if (ret != 111) {
/*  76 */       LOG.error("Failed to get network parameters buffer size. Error code: {}", Integer.valueOf(ret));
/*  77 */       return new String[0];
/*     */     }
/*     */     
/*  80 */     IPHlpAPI.FIXED_INFO buffer = new IPHlpAPI.FIXED_INFO(new Memory(bufferSize.getValue().longValue()));
/*  81 */     ret = IPHlpAPI.INSTANCE.GetNetworkParams(buffer, bufferSize);
/*  82 */     if (ret != 0) {
/*  83 */       LOG.error("Failed to get network parameters. Error code: {}", Integer.valueOf(ret));
/*  84 */       return new String[0];
/*     */     }
/*     */     
/*  87 */     List<String> list = new ArrayList();
/*  88 */     IPHlpAPI.IP_ADDR_STRING dns = buffer.DnsServerList;
/*  89 */     while (dns != null) {
/*  90 */       String addr = new String(dns.IpAddress.String);
/*  91 */       int nullPos = addr.indexOf(0);
/*  92 */       if (nullPos != -1) {
/*  93 */         addr = addr.substring(0, nullPos);
/*     */       }
/*  95 */       list.add(addr);
/*  96 */       dns = dns.Next;
/*     */     }
/*     */     
/*  99 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIpv4DefaultGateway()
/*     */   {
/* 108 */     if (WmiUtil.hasNamespace("StandardCimv2")) {
/* 109 */       return getNextHop("0.0.0.0/0");
/*     */     }
/*     */     
/* 112 */     return getNextHopWin7("0.0.0.0/0".split("/")[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIpv6DefaultGateway()
/*     */   {
/* 121 */     if (WmiUtil.hasNamespace("StandardCimv2")) {
/* 122 */       return getNextHop("::/0");
/*     */     }
/* 124 */     return parseIpv6Route();
/*     */   }
/*     */   
/*     */   private String getNextHop(String dest) {
/* 128 */     Map<String, List<Object>> vals = WmiUtil.selectObjectsFrom("ROOT\\StandardCimv2", "MSFT_NetRoute", "NextHop,RouteMetric", "WHERE DestinationPrefix=\"" + dest + "\"", GATEWAY_TYPES);
/*     */     
/* 130 */     List<Object> metrics = (List)vals.get("RouteMetric");
/* 131 */     if (((List)vals.get("RouteMetric")).isEmpty()) {
/* 132 */       return "";
/*     */     }
/* 134 */     int index = 0;
/* 135 */     Long min = Long.valueOf(Long.MAX_VALUE);
/* 136 */     for (int i = 0; i < metrics.size(); i++) {
/* 137 */       Long metric = (Long)metrics.get(i);
/* 138 */       if (metric.longValue() < min.longValue()) {
/* 139 */         min = metric;
/* 140 */         index = i;
/*     */       }
/*     */     }
/* 143 */     return (String)((List)vals.get("NextHop")).get(index);
/*     */   }
/*     */   
/*     */   private String getNextHopWin7(String dest) {
/* 147 */     Map<String, List<Object>> vals = WmiUtil.selectObjectsFrom(null, "Win32_IP4RouteTable", "NextHop,Metric1", "WHERE Destination=\"" + dest + "\"", GATEWAY_TYPES);
/*     */     
/* 149 */     List<Object> metrics = (List)vals.get("Metric1");
/* 150 */     if (((List)vals.get("Metric1")).isEmpty()) {
/* 151 */       return "";
/*     */     }
/* 153 */     int index = 0;
/* 154 */     Long min = Long.valueOf(Long.MAX_VALUE);
/* 155 */     for (int i = 0; i < metrics.size(); i++) {
/* 156 */       Long metric = (Long)metrics.get(i);
/* 157 */       if (metric.longValue() < min.longValue()) {
/* 158 */         min = metric;
/* 159 */         index = i;
/*     */       }
/*     */     }
/* 162 */     return (String)((List)vals.get("NextHop")).get(index);
/*     */   }
/*     */   
/*     */   private String parseIpv6Route() {
/* 166 */     List<String> lines = ExecutingCommand.runNative("route print -6 ::/0");
/* 167 */     for (String line : lines) {
/* 168 */       String[] fields = line.trim().split("\\s+");
/* 169 */       if ((fields.length > 3) && ("::/0".equals(fields[2]))) {
/* 170 */         return fields[3];
/*     */       }
/*     */     }
/* 173 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\windows\WindowsNetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */