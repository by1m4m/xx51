/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.net.Inet4Address;
/*    */ import java.net.Inet6Address;
/*    */ import java.net.InetAddress;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
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
/*    */ public final class DefaultHostsFileEntriesResolver
/*    */   implements HostsFileEntriesResolver
/*    */ {
/*    */   private final Map<String, Inet4Address> inet4Entries;
/*    */   private final Map<String, Inet6Address> inet6Entries;
/*    */   
/*    */   public DefaultHostsFileEntriesResolver()
/*    */   {
/* 39 */     this(parseEntries());
/*    */   }
/*    */   
/*    */   DefaultHostsFileEntriesResolver(HostsFileEntries entries)
/*    */   {
/* 44 */     this.inet4Entries = entries.inet4Entries();
/* 45 */     this.inet6Entries = entries.inet6Entries();
/*    */   }
/*    */   
/*    */   public InetAddress address(String inetHost, ResolvedAddressTypes resolvedAddressTypes)
/*    */   {
/* 50 */     String normalized = normalize(inetHost);
/* 51 */     switch (resolvedAddressTypes) {
/*    */     case IPV4_ONLY: 
/* 53 */       return (InetAddress)this.inet4Entries.get(normalized);
/*    */     case IPV6_ONLY: 
/* 55 */       return (InetAddress)this.inet6Entries.get(normalized);
/*    */     case IPV4_PREFERRED: 
/* 57 */       Inet4Address inet4Address = (Inet4Address)this.inet4Entries.get(normalized);
/* 58 */       return inet4Address != null ? inet4Address : (InetAddress)this.inet6Entries.get(normalized);
/*    */     case IPV6_PREFERRED: 
/* 60 */       Inet6Address inet6Address = (Inet6Address)this.inet6Entries.get(normalized);
/* 61 */       return inet6Address != null ? inet6Address : (InetAddress)this.inet4Entries.get(normalized);
/*    */     }
/* 63 */     throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
/*    */   }
/*    */   
/*    */ 
/*    */   String normalize(String inetHost)
/*    */   {
/* 69 */     return inetHost.toLowerCase(Locale.ENGLISH);
/*    */   }
/*    */   
/*    */   private static HostsFileEntries parseEntries() {
/* 73 */     if (PlatformDependent.isWindows())
/*    */     {
/*    */ 
/*    */ 
/* 77 */       return HostsFileParser.parseSilently(new Charset[] { Charset.defaultCharset(), CharsetUtil.UTF_16, CharsetUtil.UTF_8 });
/*    */     }
/* 79 */     return HostsFileParser.parseSilently();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\DefaultHostsFileEntriesResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */