/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
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
/*     */ public final class DefaultDnsServerAddressStreamProvider
/*     */   implements DnsServerAddressStreamProvider
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultDnsServerAddressStreamProvider.class);
/*  50 */   public static final DefaultDnsServerAddressStreamProvider INSTANCE = new DefaultDnsServerAddressStreamProvider();
/*     */   
/*     */   private static final List<InetSocketAddress> DEFAULT_NAME_SERVER_LIST;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  57 */     List<InetSocketAddress> defaultNameServers = new ArrayList(2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */     Hashtable<String, String> env = new Hashtable();
/*  65 */     env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
/*  66 */     env.put("java.naming.provider.url", "dns://");
/*     */     try {
/*  68 */       DirContext ctx = new InitialDirContext(env);
/*  69 */       String dnsUrls = (String)ctx.getEnvironment().get("java.naming.provider.url");
/*     */       
/*  71 */       if ((dnsUrls != null) && (!dnsUrls.isEmpty())) {
/*  72 */         String[] servers = dnsUrls.split(" ");
/*  73 */         for (String server : servers) {
/*     */           try {
/*  75 */             URI uri = new URI(server);
/*  76 */             String host = new URI(server).getHost();
/*     */             
/*  78 */             if ((host == null) || (host.isEmpty())) {
/*  79 */               logger.debug("Skipping a nameserver URI as host portion could not be extracted: {}", server);
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*  84 */               int port = uri.getPort();
/*  85 */               defaultNameServers.add(SocketUtils.socketAddress(uri.getHost(), port == -1 ? 53 : port));
/*     */             }
/*  87 */           } catch (URISyntaxException e) { logger.debug("Skipping a malformed nameserver URI: {}", server, e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException) {}
/*     */     
/*     */ 
/*  95 */     if (defaultNameServers.isEmpty()) {
/*     */       try {
/*  97 */         Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
/*  98 */         Method open = configClass.getMethod("open", new Class[0]);
/*  99 */         Method nameservers = configClass.getMethod("nameservers", new Class[0]);
/* 100 */         Object instance = open.invoke(null, new Object[0]);
/*     */         
/*     */ 
/* 103 */         Object list = (List)nameservers.invoke(instance, new Object[0]);
/* 104 */         for (String a : (List)list) {
/* 105 */           if (a != null) {
/* 106 */             defaultNameServers.add(new InetSocketAddress(SocketUtils.addressByName(a), 53));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 115 */     if (!defaultNameServers.isEmpty()) {
/* 116 */       if (logger.isDebugEnabled()) {
/* 117 */         logger.debug("Default DNS servers: {} (sun.net.dns.ResolverConfiguration)", defaultNameServers);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 124 */       if ((NetUtil.isIpV6AddressesPreferred()) || (((NetUtil.LOCALHOST instanceof Inet6Address)) && 
/* 125 */         (!NetUtil.isIpV4StackPreferred()))) {
/* 126 */         Collections.addAll(defaultNameServers, new InetSocketAddress[] {
/*     */         
/* 128 */           SocketUtils.socketAddress("2001:4860:4860::8888", 53), 
/* 129 */           SocketUtils.socketAddress("2001:4860:4860::8844", 53) });
/*     */       } else {
/* 131 */         Collections.addAll(defaultNameServers, new InetSocketAddress[] {
/*     */         
/* 133 */           SocketUtils.socketAddress("8.8.8.8", 53), 
/* 134 */           SocketUtils.socketAddress("8.8.4.4", 53) });
/*     */       }
/*     */       
/* 137 */       if (logger.isWarnEnabled()) {
/* 138 */         logger.warn("Default DNS servers: {} (Google Public DNS as a fallback)", defaultNameServers);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 143 */     DEFAULT_NAME_SERVER_LIST = Collections.unmodifiableList(defaultNameServers); }
/* 144 */   private static final DnsServerAddresses DEFAULT_NAME_SERVERS = DnsServerAddresses.sequential(DEFAULT_NAME_SERVER_LIST);
/*     */   
/*     */ 
/*     */   static final int DNS_PORT = 53;
/*     */   
/*     */ 
/*     */   public DnsServerAddressStream nameServerAddressStream(String hostname)
/*     */   {
/* 152 */     return DEFAULT_NAME_SERVERS.stream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<InetSocketAddress> defaultAddressList()
/*     */   {
/* 161 */     return DEFAULT_NAME_SERVER_LIST;
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
/*     */   public static DnsServerAddresses defaultAddresses()
/*     */   {
/* 176 */     return DEFAULT_NAME_SERVERS;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DefaultDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */