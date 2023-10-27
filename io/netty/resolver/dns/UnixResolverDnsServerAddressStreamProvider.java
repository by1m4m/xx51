/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class UnixResolverDnsServerAddressStreamProvider
/*     */   implements DnsServerAddressStreamProvider
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(UnixResolverDnsServerAddressStreamProvider.class);
/*     */   
/*     */   private static final String ETC_RESOLV_CONF_FILE = "/etc/resolv.conf";
/*     */   
/*     */   private static final String ETC_RESOLVER_DIR = "/etc/resolver";
/*     */   private static final String NAMESERVER_ROW_LABEL = "nameserver";
/*     */   private static final String SORTLIST_ROW_LABEL = "sortlist";
/*     */   private static final String OPTIONS_ROW_LABEL = "options";
/*     */   private static final String DOMAIN_ROW_LABEL = "domain";
/*     */   private static final String SEARCH_ROW_LABEL = "search";
/*     */   private static final String PORT_ROW_LABEL = "port";
/*     */   private static final String NDOTS_LABEL = "ndots:";
/*     */   static final int DEFAULT_NDOTS = 1;
/*     */   private final DnsServerAddresses defaultNameServerAddresses;
/*     */   private final Map<String, DnsServerAddresses> domainToNameServerStreamMap;
/*     */   
/*     */   static DnsServerAddressStreamProvider parseSilently()
/*     */   {
/*     */     try
/*     */     {
/*  69 */       UnixResolverDnsServerAddressStreamProvider nameServerCache = new UnixResolverDnsServerAddressStreamProvider("/etc/resolv.conf", "/etc/resolver");
/*     */       
/*  71 */       return nameServerCache.mayOverrideNameServers() ? nameServerCache : DefaultDnsServerAddressStreamProvider.INSTANCE;
/*     */     }
/*     */     catch (Exception e) {
/*  74 */       logger.debug("failed to parse {} and/or {}", new Object[] { "/etc/resolv.conf", "/etc/resolver", e }); }
/*  75 */     return DefaultDnsServerAddressStreamProvider.INSTANCE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnixResolverDnsServerAddressStreamProvider(File etcResolvConf, File... etcResolverFiles)
/*     */     throws IOException
/*     */   {
/*  93 */     Map<String, DnsServerAddresses> etcResolvConfMap = parse(new File[] { (File)ObjectUtil.checkNotNull(etcResolvConf, "etcResolvConf") });
/*  94 */     boolean useEtcResolverFiles = (etcResolverFiles != null) && (etcResolverFiles.length != 0);
/*  95 */     this.domainToNameServerStreamMap = (useEtcResolverFiles ? parse(etcResolverFiles) : etcResolvConfMap);
/*     */     
/*  97 */     DnsServerAddresses defaultNameServerAddresses = (DnsServerAddresses)etcResolvConfMap.get(etcResolvConf.getName());
/*  98 */     if (defaultNameServerAddresses == null) {
/*  99 */       Collection<DnsServerAddresses> values = etcResolvConfMap.values();
/* 100 */       if (values.isEmpty()) {
/* 101 */         throw new IllegalArgumentException(etcResolvConf + " didn't provide any name servers");
/*     */       }
/* 103 */       this.defaultNameServerAddresses = ((DnsServerAddresses)values.iterator().next());
/*     */     } else {
/* 105 */       this.defaultNameServerAddresses = defaultNameServerAddresses;
/*     */     }
/*     */     
/* 108 */     if (useEtcResolverFiles) {
/* 109 */       this.domainToNameServerStreamMap.putAll(etcResolvConfMap);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnixResolverDnsServerAddressStreamProvider(String etcResolvConf, String etcResolverDir)
/*     */     throws IOException
/*     */   {
/* 127 */     this(etcResolvConf == null ? null : new File(etcResolvConf), etcResolverDir == null ? null : new File(etcResolverDir)
/* 128 */       .listFiles());
/*     */   }
/*     */   
/*     */   public DnsServerAddressStream nameServerAddressStream(String hostname)
/*     */   {
/*     */     for (;;) {
/* 134 */       int i = hostname.indexOf('.', 1);
/* 135 */       if ((i < 0) || (i == hostname.length() - 1)) {
/* 136 */         return this.defaultNameServerAddresses.stream();
/*     */       }
/*     */       
/* 139 */       DnsServerAddresses addresses = (DnsServerAddresses)this.domainToNameServerStreamMap.get(hostname);
/* 140 */       if (addresses != null) {
/* 141 */         return addresses.stream();
/*     */       }
/*     */       
/* 144 */       hostname = hostname.substring(i + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean mayOverrideNameServers() {
/* 149 */     return (!this.domainToNameServerStreamMap.isEmpty()) || (this.defaultNameServerAddresses.stream().next() != null);
/*     */   }
/*     */   
/*     */   private static Map<String, DnsServerAddresses> parse(File... etcResolverFiles) throws IOException {
/* 153 */     Map<String, DnsServerAddresses> domainToNameServerStreamMap = new HashMap(etcResolverFiles.length << 1);
/*     */     
/* 155 */     for (File etcResolverFile : etcResolverFiles) {
/* 156 */       if (etcResolverFile.isFile())
/*     */       {
/*     */ 
/* 159 */         FileReader fr = new FileReader(etcResolverFile);
/* 160 */         BufferedReader br = null;
/*     */         try {
/* 162 */           br = new BufferedReader(fr);
/* 163 */           List<InetSocketAddress> addresses = new ArrayList(2);
/* 164 */           String domainName = etcResolverFile.getName();
/* 165 */           int port = 53;
/*     */           String line;
/* 167 */           while ((line = br.readLine()) != null) {
/* 168 */             line = line.trim();
/*     */             char c;
/* 170 */             if ((!line.isEmpty()) && ((c = line.charAt(0)) != '#') && (c != ';'))
/*     */             {
/*     */ 
/* 173 */               if (line.startsWith("nameserver")) {
/* 174 */                 int i = StringUtil.indexOfNonWhiteSpace(line, "nameserver".length());
/* 175 */                 if (i < 0) {
/* 176 */                   throw new IllegalArgumentException("error parsing label nameserver in file " + etcResolverFile + ". value: " + line);
/*     */                 }
/*     */                 
/* 179 */                 String maybeIP = line.substring(i);
/*     */                 
/* 181 */                 if ((!NetUtil.isValidIpV4Address(maybeIP)) && (!NetUtil.isValidIpV6Address(maybeIP))) {
/* 182 */                   i = maybeIP.lastIndexOf('.');
/* 183 */                   if (i + 1 >= maybeIP.length()) {
/* 184 */                     throw new IllegalArgumentException("error parsing label nameserver in file " + etcResolverFile + ". invalid IP value: " + line);
/*     */                   }
/*     */                   
/* 187 */                   port = Integer.parseInt(maybeIP.substring(i + 1));
/* 188 */                   maybeIP = maybeIP.substring(0, i);
/*     */                 }
/* 190 */                 addresses.add(SocketUtils.socketAddress(maybeIP, port));
/* 191 */               } else if (line.startsWith("domain")) {
/* 192 */                 int i = StringUtil.indexOfNonWhiteSpace(line, "domain".length());
/* 193 */                 if (i < 0) {
/* 194 */                   throw new IllegalArgumentException("error parsing label domain in file " + etcResolverFile + " value: " + line);
/*     */                 }
/*     */                 
/* 197 */                 domainName = line.substring(i);
/* 198 */                 if (!addresses.isEmpty()) {
/* 199 */                   putIfAbsent(domainToNameServerStreamMap, domainName, addresses);
/*     */                 }
/* 201 */                 addresses = new ArrayList(2);
/* 202 */               } else if (line.startsWith("port")) {
/* 203 */                 int i = StringUtil.indexOfNonWhiteSpace(line, "port".length());
/* 204 */                 if (i < 0) {
/* 205 */                   throw new IllegalArgumentException("error parsing label port in file " + etcResolverFile + " value: " + line);
/*     */                 }
/*     */                 
/* 208 */                 port = Integer.parseInt(line.substring(i));
/* 209 */               } else if (line.startsWith("sortlist")) {
/* 210 */                 logger.info("row type {} not supported. ignoring line: {}", "sortlist", line);
/*     */               } }
/*     */           }
/* 213 */           if (!addresses.isEmpty()) {
/* 214 */             putIfAbsent(domainToNameServerStreamMap, domainName, addresses);
/*     */           }
/*     */         } finally {
/* 217 */           if (br == null) {
/* 218 */             fr.close();
/*     */           } else
/* 220 */             br.close();
/*     */         }
/*     */       }
/*     */     }
/* 224 */     return domainToNameServerStreamMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, List<InetSocketAddress> addresses)
/*     */   {
/* 231 */     putIfAbsent(domainToNameServerStreamMap, domainName, DnsServerAddresses.sequential(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */   private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, DnsServerAddresses addresses)
/*     */   {
/* 237 */     DnsServerAddresses existingAddresses = (DnsServerAddresses)domainToNameServerStreamMap.put(domainName, addresses);
/* 238 */     if (existingAddresses != null) {
/* 239 */       domainToNameServerStreamMap.put(domainName, existingAddresses);
/* 240 */       logger.debug("Domain name {} already maps to addresses {} so new addresses {} will be discarded", new Object[] { domainName, existingAddresses, addresses });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int parseEtcResolverFirstNdots()
/*     */     throws IOException
/*     */   {
/* 253 */     return parseEtcResolverFirstNdots(new File("/etc/resolv.conf"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int parseEtcResolverFirstNdots(File etcResolvConf)
/*     */     throws IOException
/*     */   {
/* 265 */     FileReader fr = new FileReader(etcResolvConf);
/* 266 */     BufferedReader br = null;
/*     */     try {
/* 268 */       br = new BufferedReader(fr);
/*     */       String line;
/* 270 */       while ((line = br.readLine()) != null) {
/* 271 */         if (line.startsWith("options")) {
/* 272 */           int i = line.indexOf("ndots:");
/* 273 */           if (i >= 0) {
/* 274 */             i += "ndots:".length();
/* 275 */             int j = line.indexOf(' ', i);
/* 276 */             return Integer.parseInt(line.substring(i, j < 0 ? line.length() : j));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 282 */       if (br == null) {
/* 283 */         fr.close();
/*     */       } else {
/* 285 */         br.close();
/*     */       }
/*     */     }
/* 288 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static List<String> parseEtcResolverSearchDomains()
/*     */     throws IOException
/*     */   {
/* 298 */     return parseEtcResolverSearchDomains(new File("/etc/resolv.conf"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static List<String> parseEtcResolverSearchDomains(File etcResolvConf)
/*     */     throws IOException
/*     */   {
/* 309 */     String localDomain = null;
/* 310 */     List<String> searchDomains = new ArrayList();
/*     */     
/* 312 */     FileReader fr = new FileReader(etcResolvConf);
/* 313 */     BufferedReader br = null;
/*     */     try {
/* 315 */       br = new BufferedReader(fr);
/*     */       String line;
/* 317 */       while ((line = br.readLine()) != null) {
/* 318 */         if ((localDomain == null) && (line.startsWith("domain"))) {
/* 319 */           int i = StringUtil.indexOfNonWhiteSpace(line, "domain".length());
/* 320 */           if (i >= 0) {
/* 321 */             localDomain = line.substring(i);
/*     */           }
/* 323 */         } else if (line.startsWith("search")) {
/* 324 */           int i = StringUtil.indexOfNonWhiteSpace(line, "search".length());
/* 325 */           if (i >= 0) {
/* 326 */             searchDomains.add(line.substring(i));
/*     */           }
/*     */         }
/*     */       }
/*     */     } finally {
/* 331 */       if (br == null) {
/* 332 */         fr.close();
/*     */       } else {
/* 334 */         br.close();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 339 */     return (localDomain != null) && (searchDomains.isEmpty()) ? 
/* 340 */       Collections.singletonList(localDomain) : searchDomains;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\UnixResolverDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */