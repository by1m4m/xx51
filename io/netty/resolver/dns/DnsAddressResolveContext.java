/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.handler.codec.dns.DnsRecordType;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ final class DnsAddressResolveContext
/*     */   extends DnsResolveContext<InetAddress>
/*     */ {
/*     */   private final DnsCache resolveCache;
/*     */   private final AuthoritativeDnsServerCache authoritativeDnsServerCache;
/*     */   
/*     */   DnsAddressResolveContext(DnsNameResolver parent, String hostname, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, DnsCache resolveCache, AuthoritativeDnsServerCache authoritativeDnsServerCache)
/*     */   {
/*  38 */     super(parent, hostname, 1, parent.resolveRecordTypes(), additionals, nameServerAddrs);
/*  39 */     this.resolveCache = resolveCache;
/*  40 */     this.authoritativeDnsServerCache = authoritativeDnsServerCache;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   DnsResolveContext<InetAddress> newResolverContext(DnsNameResolver parent, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs)
/*     */   {
/*  48 */     return new DnsAddressResolveContext(parent, hostname, additionals, nameServerAddrs, this.resolveCache, this.authoritativeDnsServerCache);
/*     */   }
/*     */   
/*     */ 
/*     */   InetAddress convertRecord(DnsRecord record, String hostname, DnsRecord[] additionals, EventLoop eventLoop)
/*     */   {
/*  54 */     return DnsAddressDecoder.decodeAddress(record, hostname, this.parent.isDecodeIdn());
/*     */   }
/*     */   
/*     */   List<InetAddress> filterResults(List<InetAddress> unfiltered)
/*     */   {
/*  59 */     Class<? extends InetAddress> inetAddressType = this.parent.preferredAddressType().addressType();
/*  60 */     int size = unfiltered.size();
/*  61 */     int numExpected = 0;
/*  62 */     for (int i = 0; i < size; i++) {
/*  63 */       InetAddress address = (InetAddress)unfiltered.get(i);
/*  64 */       if (inetAddressType.isInstance(address)) {
/*  65 */         numExpected++;
/*     */       }
/*     */     }
/*  68 */     if ((numExpected == size) || (numExpected == 0))
/*     */     {
/*  70 */       return unfiltered;
/*     */     }
/*  72 */     List<InetAddress> filtered = new ArrayList(numExpected);
/*  73 */     for (int i = 0; i < size; i++) {
/*  74 */       InetAddress address = (InetAddress)unfiltered.get(i);
/*  75 */       if (inetAddressType.isInstance(address)) {
/*  76 */         filtered.add(address);
/*     */       }
/*     */     }
/*  79 */     return filtered;
/*     */   }
/*     */   
/*     */ 
/*     */   void cache(String hostname, DnsRecord[] additionals, DnsRecord result, InetAddress convertedResult)
/*     */   {
/*  85 */     this.resolveCache.cache(hostname, additionals, convertedResult, result.timeToLive(), this.parent.ch.eventLoop());
/*     */   }
/*     */   
/*     */   void cache(String hostname, DnsRecord[] additionals, UnknownHostException cause)
/*     */   {
/*  90 */     this.resolveCache.cache(hostname, additionals, cause, this.parent.ch.eventLoop());
/*     */   }
/*     */   
/*     */ 
/*     */   void doSearchDomainQuery(String hostname, Promise<List<InetAddress>> nextPromise)
/*     */   {
/*  96 */     if (!DnsNameResolver.doResolveAllCached(hostname, this.additionals, nextPromise, this.resolveCache, this.parent
/*  97 */       .resolvedInternetProtocolFamiliesUnsafe())) {
/*  98 */       super.doSearchDomainQuery(hostname, nextPromise);
/*     */     }
/*     */   }
/*     */   
/*     */   DnsCache resolveCache()
/*     */   {
/* 104 */     return this.resolveCache;
/*     */   }
/*     */   
/*     */   AuthoritativeDnsServerCache authoritativeDnsServerCache()
/*     */   {
/* 109 */     return this.authoritativeDnsServerCache;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsAddressResolveContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */