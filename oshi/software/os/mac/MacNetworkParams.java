/*    */ package oshi.software.os.mac;
/*    */ 
/*    */ import com.sun.jna.ptr.PointerByReference;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.jna.platform.mac.SystemB;
/*    */ import oshi.jna.platform.unix.CLibrary.Addrinfo;
/*    */ import oshi.software.common.AbstractNetworkParams;
/*    */ import oshi.util.ExecutingCommand;
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
/*    */ public class MacNetworkParams
/*    */   extends AbstractNetworkParams
/*    */ {
/* 36 */   private static final Logger LOG = LoggerFactory.getLogger(MacNetworkParams.class);
/*    */   
/*    */ 
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */   private static final String IPV6_ROUTE_HEADER = "Internet6:";
/*    */   
/*    */   private static final String DEFAULT_GATEWAY = "default";
/*    */   
/*    */ 
/*    */   public String getDomainName()
/*    */   {
/* 49 */     CLibrary.Addrinfo hint = new CLibrary.Addrinfo();
/* 50 */     hint.ai_flags = 2;
/* 51 */     String hostname = "";
/*    */     try {
/* 53 */       hostname = InetAddress.getLocalHost().getHostName();
/*    */     } catch (UnknownHostException e) {
/* 55 */       LOG.error("Unknown host exception when getting address of local host: {}", e);
/* 56 */       return "";
/*    */     }
/* 58 */     PointerByReference ptr = new PointerByReference();
/* 59 */     int res = SystemB.INSTANCE.getaddrinfo(hostname, null, hint, ptr);
/* 60 */     if (res > 0) {
/* 61 */       LOG.error("Failed getaddrinfo(): {}", SystemB.INSTANCE.gai_strerror(res));
/* 62 */       return "";
/*    */     }
/* 64 */     CLibrary.Addrinfo info = new CLibrary.Addrinfo(ptr.getValue());
/* 65 */     String canonname = info.ai_canonname.trim();
/* 66 */     SystemB.INSTANCE.freeaddrinfo(ptr.getValue());
/* 67 */     return canonname;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIpv4DefaultGateway()
/*    */   {
/* 75 */     return searchGateway(ExecutingCommand.runNative("route -n get default"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIpv6DefaultGateway()
/*    */   {
/* 83 */     List<String> lines = ExecutingCommand.runNative("netstat -nr");
/* 84 */     boolean v6Table = false;
/* 85 */     for (String line : lines) {
/* 86 */       if ((v6Table) && (line.startsWith("default"))) {
/* 87 */         String[] fields = line.split("\\s+");
/* 88 */         if ((fields.length > 2) && (fields[2].contains("G"))) {
/* 89 */           return fields[1].split("%")[0];
/*    */         }
/* 91 */       } else if (line.startsWith("Internet6:")) {
/* 92 */         v6Table = true;
/*    */       }
/*    */     }
/* 95 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\mac\MacNetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */