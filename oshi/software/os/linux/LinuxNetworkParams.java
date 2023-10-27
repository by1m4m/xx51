/*     */ package oshi.software.os.linux;
/*     */ 
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.linux.Libc;
/*     */ import oshi.jna.platform.unix.CLibrary.Addrinfo;
/*     */ import oshi.software.common.AbstractNetworkParams;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.ParseUtil;
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
/*     */ public class LinuxNetworkParams
/*     */   extends AbstractNetworkParams
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  39 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxNetworkParams.class);
/*     */   
/*     */ 
/*     */   private static final String IPV4_DEFAULT_DEST = "0.0.0.0";
/*     */   
/*     */   private static final String IPV6_DEFAULT_DEST = "::/0";
/*     */   
/*     */ 
/*     */   public String getDomainName()
/*     */   {
/*  49 */     CLibrary.Addrinfo hint = new CLibrary.Addrinfo();
/*  50 */     hint.ai_flags = 2;
/*  51 */     String hostname = "";
/*     */     try {
/*  53 */       hostname = InetAddress.getLocalHost().getHostName();
/*     */     } catch (UnknownHostException e) {
/*  55 */       LOG.error("Unknown host exception when getting address of local host: {}", e);
/*  56 */       return "";
/*     */     }
/*  58 */     PointerByReference ptr = new PointerByReference();
/*  59 */     int res = Libc.INSTANCE.getaddrinfo(hostname, null, hint, ptr);
/*  60 */     if (res > 0) {
/*  61 */       LOG.error("Failed getaddrinfo(): {}", Libc.INSTANCE.gai_strerror(res));
/*  62 */       return "";
/*     */     }
/*  64 */     CLibrary.Addrinfo info = new CLibrary.Addrinfo(ptr.getValue());
/*  65 */     String canonname = info.ai_canonname.trim();
/*  66 */     Libc.INSTANCE.freeaddrinfo(ptr.getValue());
/*  67 */     return canonname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIpv4DefaultGateway()
/*     */   {
/*  75 */     List<String> routes = ExecutingCommand.runNative("route -A inet -n");
/*  76 */     if (routes.size() <= 2) {
/*  77 */       return "";
/*     */     }
/*     */     
/*  80 */     String gateway = "";
/*  81 */     int minMetric = Integer.MAX_VALUE;
/*     */     
/*  83 */     for (int i = 2; i < routes.size(); i++) {
/*  84 */       String[] fields = ((String)routes.get(i)).split("\\s+");
/*  85 */       if ((fields.length > 4) && (fields[0].equals("0.0.0.0"))) {
/*  86 */         boolean isGateway = fields[3].indexOf('G') != -1;
/*  87 */         int metric = ParseUtil.parseIntOrDefault(fields[4], Integer.MAX_VALUE);
/*  88 */         if ((isGateway) && (metric < minMetric)) {
/*  89 */           minMetric = metric;
/*  90 */           gateway = fields[1];
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return gateway;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIpv6DefaultGateway()
/*     */   {
/* 102 */     List<String> routes = ExecutingCommand.runNative("route -A inet6 -n");
/* 103 */     if (routes.size() <= 2) {
/* 104 */       return "";
/*     */     }
/*     */     
/* 107 */     String gateway = "";
/* 108 */     int minMetric = Integer.MAX_VALUE;
/*     */     
/* 110 */     for (int i = 2; i < routes.size(); i++) {
/* 111 */       String[] fields = ((String)routes.get(i)).split("\\s+");
/* 112 */       if ((fields.length > 3) && (fields[0].equals("::/0"))) {
/* 113 */         boolean isGateway = fields[2].indexOf('G') != -1;
/* 114 */         int metric = ParseUtil.parseIntOrDefault(fields[3], Integer.MAX_VALUE);
/* 115 */         if ((isGateway) && (metric < minMetric)) {
/* 116 */           minMetric = metric;
/* 117 */           gateway = fields[1];
/*     */         }
/*     */       }
/*     */     }
/* 121 */     return gateway;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\linux\LinuxNetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */