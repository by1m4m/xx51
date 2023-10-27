/*    */ package oshi.software.os.unix.freebsd;
/*    */ 
/*    */ import com.sun.jna.ptr.PointerByReference;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.jna.platform.unix.CLibrary.Addrinfo;
/*    */ import oshi.jna.platform.unix.freebsd.Libc;
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
/*    */ 
/*    */ public class FreeBsdNetworkParams
/*    */   extends AbstractNetworkParams
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 37 */   private static final Logger LOG = LoggerFactory.getLogger(FreeBsdNetworkParams.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDomainName()
/*    */   {
/* 44 */     CLibrary.Addrinfo hint = new CLibrary.Addrinfo();
/* 45 */     hint.ai_flags = 2;
/* 46 */     String hostname = "";
/*    */     try {
/* 48 */       hostname = InetAddress.getLocalHost().getHostName();
/*    */     } catch (UnknownHostException e) {
/* 50 */       LOG.error("Unknown host exception when getting address of local host: {}", e);
/* 51 */       return "";
/*    */     }
/* 53 */     PointerByReference ptr = new PointerByReference();
/* 54 */     int res = Libc.INSTANCE.getaddrinfo(hostname, null, hint, ptr);
/* 55 */     if (res > 0) {
/* 56 */       LOG.error("Failed getaddrinfo(): {}", Libc.INSTANCE.gai_strerror(res));
/* 57 */       return "";
/*    */     }
/* 59 */     CLibrary.Addrinfo info = new CLibrary.Addrinfo(ptr.getValue());
/* 60 */     String canonname = info.ai_canonname.trim();
/* 61 */     Libc.INSTANCE.freeaddrinfo(ptr.getValue());
/* 62 */     return canonname;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIpv4DefaultGateway()
/*    */   {
/* 70 */     return searchGateway(ExecutingCommand.runNative("route -4 get default"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIpv6DefaultGateway()
/*    */   {
/* 78 */     return searchGateway(ExecutingCommand.runNative("route -6 get default"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\freebsd\FreeBsdNetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */