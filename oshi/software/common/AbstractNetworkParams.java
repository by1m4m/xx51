/*     */ package oshi.software.common;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.software.os.NetworkParams;
/*     */ import oshi.util.FileUtil;
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
/*     */ public abstract class AbstractNetworkParams
/*     */   implements NetworkParams
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  39 */   private static final Logger LOG = LoggerFactory.getLogger(AbstractNetworkParams.class);
/*     */   
/*     */   private static final String NAMESERVER = "nameserver";
/*     */   
/*     */ 
/*     */   public String getDomainName()
/*     */   {
/*     */     try
/*     */     {
/*  48 */       return InetAddress.getLocalHost().getCanonicalHostName();
/*     */     } catch (UnknownHostException e) {
/*  50 */       LOG.error("Unknown host exception when getting address of local host: " + e); }
/*  51 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHostName()
/*     */   {
/*     */     try
/*     */     {
/*  61 */       String hn = InetAddress.getLocalHost().getHostName();
/*  62 */       int dot = hn.indexOf('.');
/*  63 */       if (dot == -1) {
/*  64 */         return hn;
/*     */       }
/*  66 */       return hn.substring(0, dot);
/*     */     }
/*     */     catch (UnknownHostException e) {
/*  69 */       LOG.error("Unknown host exception when getting address of local host: " + e); }
/*  70 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getDnsServers()
/*     */   {
/*  79 */     List<String> resolv = FileUtil.readFile("/etc/resolv.conf");
/*  80 */     String key = "nameserver";
/*  81 */     int maxNameServer = 3;
/*  82 */     List<String> servers = new ArrayList();
/*  83 */     for (int i = 0; (i < resolv.size()) && (servers.size() < maxNameServer); i++) {
/*  84 */       String line = (String)resolv.get(i);
/*  85 */       if (line.startsWith(key)) {
/*  86 */         String value = line.substring(key.length()).replaceFirst("^[ \t]+", "");
/*  87 */         if ((value.length() != 0) && (value.charAt(0) != '#') && (value.charAt(0) != ';')) {
/*  88 */           String val = value.split("[ \t#;]", 2)[0];
/*  89 */           servers.add(val);
/*     */         }
/*     */       }
/*     */     }
/*  93 */     return (String[])servers.toArray(new String[servers.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String searchGateway(List<String> lines)
/*     */   {
/* 105 */     for (String line : lines) {
/* 106 */       String leftTrimmed = line.replaceFirst("^\\s+", "");
/* 107 */       if (leftTrimmed.startsWith("gateway:")) {
/* 108 */         String[] split = leftTrimmed.split("\\s+");
/* 109 */         if (split.length < 2) {
/* 110 */           return "";
/*     */         }
/* 112 */         return split[1].split("%")[0];
/*     */       }
/*     */     }
/* 115 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\common\AbstractNetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */