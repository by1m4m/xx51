/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ReflectiveChannelFactory;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.resolver.HostsFileEntriesResolver;
/*     */ import io.netty.resolver.ResolvedAddressTypes;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DnsNameResolverBuilder
/*     */ {
/*     */   private EventLoop eventLoop;
/*     */   private ChannelFactory<? extends DatagramChannel> channelFactory;
/*     */   private DnsCache resolveCache;
/*     */   private DnsCnameCache cnameCache;
/*     */   private AuthoritativeDnsServerCache authoritativeDnsServerCache;
/*     */   private Integer minTtl;
/*     */   private Integer maxTtl;
/*     */   private Integer negativeTtl;
/*  48 */   private long queryTimeoutMillis = 5000L;
/*  49 */   private ResolvedAddressTypes resolvedAddressTypes = DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES;
/*  50 */   private boolean recursionDesired = true;
/*  51 */   private int maxQueriesPerResolve = 16;
/*     */   private boolean traceEnabled;
/*  53 */   private int maxPayloadSize = 4096;
/*  54 */   private boolean optResourceEnabled = true;
/*  55 */   private HostsFileEntriesResolver hostsFileEntriesResolver = HostsFileEntriesResolver.DEFAULT;
/*  56 */   private DnsServerAddressStreamProvider dnsServerAddressStreamProvider = DnsServerAddressStreamProviders.platformDefault();
/*  57 */   private DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory = NoopDnsQueryLifecycleObserverFactory.INSTANCE;
/*     */   
/*     */   private String[] searchDomains;
/*  60 */   private int ndots = -1;
/*  61 */   private boolean decodeIdn = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder(EventLoop eventLoop)
/*     */   {
/*  76 */     eventLoop(eventLoop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder eventLoop(EventLoop eventLoop)
/*     */   {
/*  86 */     this.eventLoop = eventLoop;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   protected ChannelFactory<? extends DatagramChannel> channelFactory() {
/*  91 */     return this.channelFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder channelFactory(ChannelFactory<? extends DatagramChannel> channelFactory)
/*     */   {
/* 101 */     this.channelFactory = channelFactory;
/* 102 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder channelType(Class<? extends DatagramChannel> channelType)
/*     */   {
/* 113 */     return channelFactory(new ReflectiveChannelFactory(channelType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder resolveCache(DnsCache resolveCache)
/*     */   {
/* 123 */     this.resolveCache = resolveCache;
/* 124 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder cnameCache(DnsCnameCache cnameCache)
/*     */   {
/* 134 */     this.cnameCache = cnameCache;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder dnsQueryLifecycleObserverFactory(DnsQueryLifecycleObserverFactory lifecycleObserverFactory)
/*     */   {
/* 145 */     this.dnsQueryLifecycleObserverFactory = ((DnsQueryLifecycleObserverFactory)ObjectUtil.checkNotNull(lifecycleObserverFactory, "lifecycleObserverFactory"));
/* 146 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public DnsNameResolverBuilder authoritativeDnsServerCache(DnsCache authoritativeDnsServerCache)
/*     */   {
/* 158 */     this.authoritativeDnsServerCache = new AuthoritativeDnsServerCacheAdapter(authoritativeDnsServerCache);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder authoritativeDnsServerCache(AuthoritativeDnsServerCache authoritativeDnsServerCache)
/*     */   {
/* 169 */     this.authoritativeDnsServerCache = authoritativeDnsServerCache;
/* 170 */     return this;
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
/*     */   public DnsNameResolverBuilder ttl(int minTtl, int maxTtl)
/*     */   {
/* 186 */     this.maxTtl = Integer.valueOf(maxTtl);
/* 187 */     this.minTtl = Integer.valueOf(minTtl);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder negativeTtl(int negativeTtl)
/*     */   {
/* 198 */     this.negativeTtl = Integer.valueOf(negativeTtl);
/* 199 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder queryTimeoutMillis(long queryTimeoutMillis)
/*     */   {
/* 209 */     this.queryTimeoutMillis = queryTimeoutMillis;
/* 210 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ResolvedAddressTypes computeResolvedAddressTypes(InternetProtocolFamily... internetProtocolFamilies)
/*     */   {
/* 221 */     if ((internetProtocolFamilies == null) || (internetProtocolFamilies.length == 0)) {
/* 222 */       return DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES;
/*     */     }
/* 224 */     if (internetProtocolFamilies.length > 2) {
/* 225 */       throw new IllegalArgumentException("No more than 2 InternetProtocolFamilies");
/*     */     }
/*     */     
/* 228 */     switch (internetProtocolFamilies[0]) {
/*     */     case IPv4: 
/* 230 */       return (internetProtocolFamilies.length >= 2) && (internetProtocolFamilies[1] == InternetProtocolFamily.IPv6) ? ResolvedAddressTypes.IPV4_PREFERRED : ResolvedAddressTypes.IPV4_ONLY;
/*     */     
/*     */ 
/*     */     case IPv6: 
/* 234 */       return (internetProtocolFamilies.length >= 2) && (internetProtocolFamilies[1] == InternetProtocolFamily.IPv4) ? ResolvedAddressTypes.IPV6_PREFERRED : ResolvedAddressTypes.IPV6_ONLY;
/*     */     }
/*     */     
/*     */     
/* 238 */     throw new IllegalArgumentException("Couldn't resolve ResolvedAddressTypes from InternetProtocolFamily array");
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
/*     */   public DnsNameResolverBuilder resolvedAddressTypes(ResolvedAddressTypes resolvedAddressTypes)
/*     */   {
/* 252 */     this.resolvedAddressTypes = resolvedAddressTypes;
/* 253 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder recursionDesired(boolean recursionDesired)
/*     */   {
/* 263 */     this.recursionDesired = recursionDesired;
/* 264 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder maxQueriesPerResolve(int maxQueriesPerResolve)
/*     */   {
/* 274 */     this.maxQueriesPerResolve = maxQueriesPerResolve;
/* 275 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder traceEnabled(boolean traceEnabled)
/*     */   {
/* 286 */     this.traceEnabled = traceEnabled;
/* 287 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder maxPayloadSize(int maxPayloadSize)
/*     */   {
/* 297 */     this.maxPayloadSize = maxPayloadSize;
/* 298 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder optResourceEnabled(boolean optResourceEnabled)
/*     */   {
/* 310 */     this.optResourceEnabled = optResourceEnabled;
/* 311 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder hostsFileEntriesResolver(HostsFileEntriesResolver hostsFileEntriesResolver)
/*     */   {
/* 320 */     this.hostsFileEntriesResolver = hostsFileEntriesResolver;
/* 321 */     return this;
/*     */   }
/*     */   
/*     */   protected DnsServerAddressStreamProvider nameServerProvider() {
/* 325 */     return this.dnsServerAddressStreamProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder nameServerProvider(DnsServerAddressStreamProvider dnsServerAddressStreamProvider)
/*     */   {
/* 335 */     this.dnsServerAddressStreamProvider = ((DnsServerAddressStreamProvider)ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider"));
/* 336 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder searchDomains(Iterable<String> searchDomains)
/*     */   {
/* 346 */     ObjectUtil.checkNotNull(searchDomains, "searchDomains");
/*     */     
/* 348 */     List<String> list = new ArrayList(4);
/*     */     
/* 350 */     for (String f : searchDomains) {
/* 351 */       if (f == null) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 356 */       if (!list.contains(f))
/*     */       {
/*     */ 
/*     */ 
/* 360 */         list.add(f);
/*     */       }
/*     */     }
/* 363 */     this.searchDomains = ((String[])list.toArray(new String[0]));
/* 364 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder ndots(int ndots)
/*     */   {
/* 375 */     this.ndots = ndots;
/* 376 */     return this;
/*     */   }
/*     */   
/*     */   private DnsCache newCache() {
/* 380 */     return new DefaultDnsCache(ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, Integer.MAX_VALUE), ObjectUtil.intValue(this.negativeTtl, 0));
/*     */   }
/*     */   
/*     */   private AuthoritativeDnsServerCache newAuthoritativeDnsServerCache() {
/* 384 */     return new DefaultAuthoritativeDnsServerCache(
/* 385 */       ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, Integer.MAX_VALUE), new NameServerComparator(
/*     */       
/*     */ 
/* 388 */       DnsNameResolver.preferredAddressType(this.resolvedAddressTypes).addressType()));
/*     */   }
/*     */   
/*     */   private DnsCnameCache newCnameCache() {
/* 392 */     return new DefaultDnsCnameCache(
/* 393 */       ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, Integer.MAX_VALUE));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolverBuilder decodeIdn(boolean decodeIdn)
/*     */   {
/* 404 */     this.decodeIdn = decodeIdn;
/* 405 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver build()
/*     */   {
/* 414 */     if (this.eventLoop == null) {
/* 415 */       throw new IllegalStateException("eventLoop should be specified to build a DnsNameResolver.");
/*     */     }
/*     */     
/* 418 */     if ((this.resolveCache != null) && ((this.minTtl != null) || (this.maxTtl != null) || (this.negativeTtl != null))) {
/* 419 */       throw new IllegalStateException("resolveCache and TTLs are mutually exclusive");
/*     */     }
/*     */     
/* 422 */     if ((this.authoritativeDnsServerCache != null) && ((this.minTtl != null) || (this.maxTtl != null) || (this.negativeTtl != null))) {
/* 423 */       throw new IllegalStateException("authoritativeDnsServerCache and TTLs are mutually exclusive");
/*     */     }
/*     */     
/* 426 */     DnsCache resolveCache = this.resolveCache != null ? this.resolveCache : newCache();
/* 427 */     DnsCnameCache cnameCache = this.cnameCache != null ? this.cnameCache : newCnameCache();
/*     */     
/* 429 */     AuthoritativeDnsServerCache authoritativeDnsServerCache = this.authoritativeDnsServerCache != null ? this.authoritativeDnsServerCache : newAuthoritativeDnsServerCache();
/* 430 */     return new DnsNameResolver(this.eventLoop, this.channelFactory, resolveCache, cnameCache, authoritativeDnsServerCache, this.dnsQueryLifecycleObserverFactory, this.queryTimeoutMillis, this.resolvedAddressTypes, this.recursionDesired, this.maxQueriesPerResolve, this.traceEnabled, this.maxPayloadSize, this.optResourceEnabled, this.hostsFileEntriesResolver, this.dnsServerAddressStreamProvider, this.searchDomains, this.ndots, this.decodeIdn);
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
/*     */   public DnsNameResolverBuilder copy()
/*     */   {
/* 457 */     DnsNameResolverBuilder copiedBuilder = new DnsNameResolverBuilder();
/*     */     
/* 459 */     if (this.eventLoop != null) {
/* 460 */       copiedBuilder.eventLoop(this.eventLoop);
/*     */     }
/*     */     
/* 463 */     if (this.channelFactory != null) {
/* 464 */       copiedBuilder.channelFactory(this.channelFactory);
/*     */     }
/*     */     
/* 467 */     if (this.resolveCache != null) {
/* 468 */       copiedBuilder.resolveCache(this.resolveCache);
/*     */     }
/*     */     
/* 471 */     if (this.cnameCache != null) {
/* 472 */       copiedBuilder.cnameCache(this.cnameCache);
/*     */     }
/* 474 */     if ((this.maxTtl != null) && (this.minTtl != null)) {
/* 475 */       copiedBuilder.ttl(this.minTtl.intValue(), this.maxTtl.intValue());
/*     */     }
/*     */     
/* 478 */     if (this.negativeTtl != null) {
/* 479 */       copiedBuilder.negativeTtl(this.negativeTtl.intValue());
/*     */     }
/*     */     
/* 482 */     if (this.authoritativeDnsServerCache != null) {
/* 483 */       copiedBuilder.authoritativeDnsServerCache(this.authoritativeDnsServerCache);
/*     */     }
/*     */     
/* 486 */     if (this.dnsQueryLifecycleObserverFactory != null) {
/* 487 */       copiedBuilder.dnsQueryLifecycleObserverFactory(this.dnsQueryLifecycleObserverFactory);
/*     */     }
/*     */     
/* 490 */     copiedBuilder.queryTimeoutMillis(this.queryTimeoutMillis);
/* 491 */     copiedBuilder.resolvedAddressTypes(this.resolvedAddressTypes);
/* 492 */     copiedBuilder.recursionDesired(this.recursionDesired);
/* 493 */     copiedBuilder.maxQueriesPerResolve(this.maxQueriesPerResolve);
/* 494 */     copiedBuilder.traceEnabled(this.traceEnabled);
/* 495 */     copiedBuilder.maxPayloadSize(this.maxPayloadSize);
/* 496 */     copiedBuilder.optResourceEnabled(this.optResourceEnabled);
/* 497 */     copiedBuilder.hostsFileEntriesResolver(this.hostsFileEntriesResolver);
/*     */     
/* 499 */     if (this.dnsServerAddressStreamProvider != null) {
/* 500 */       copiedBuilder.nameServerProvider(this.dnsServerAddressStreamProvider);
/*     */     }
/*     */     
/* 503 */     if (this.searchDomains != null) {
/* 504 */       copiedBuilder.searchDomains(Arrays.asList(this.searchDomains));
/*     */     }
/*     */     
/* 507 */     copiedBuilder.ndots(this.ndots);
/* 508 */     copiedBuilder.decodeIdn(this.decodeIdn);
/*     */     
/* 510 */     return copiedBuilder;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsNameResolverBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */