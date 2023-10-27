/*      */ package io.netty.resolver.dns;
/*      */ 
/*      */ import io.netty.bootstrap.Bootstrap;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.AddressedEnvelope;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelFactory;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelHandler;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*      */ import io.netty.channel.ChannelInitializer;
/*      */ import io.netty.channel.ChannelOption;
/*      */ import io.netty.channel.ChannelPipeline;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.EventLoop;
/*      */ import io.netty.channel.FixedRecvByteBufAllocator;
/*      */ import io.netty.channel.socket.DatagramChannel;
/*      */ import io.netty.channel.socket.InternetProtocolFamily;
/*      */ import io.netty.handler.codec.dns.DatagramDnsQueryEncoder;
/*      */ import io.netty.handler.codec.dns.DatagramDnsResponse;
/*      */ import io.netty.handler.codec.dns.DatagramDnsResponseDecoder;
/*      */ import io.netty.handler.codec.dns.DefaultDnsRawRecord;
/*      */ import io.netty.handler.codec.dns.DnsQuestion;
/*      */ import io.netty.handler.codec.dns.DnsRawRecord;
/*      */ import io.netty.handler.codec.dns.DnsRecord;
/*      */ import io.netty.handler.codec.dns.DnsRecordType;
/*      */ import io.netty.handler.codec.dns.DnsResponse;
/*      */ import io.netty.resolver.HostsFileEntriesResolver;
/*      */ import io.netty.resolver.InetNameResolver;
/*      */ import io.netty.resolver.ResolvedAddressTypes;
/*      */ import io.netty.util.NetUtil;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.concurrent.FastThreadLocal;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.FutureListener;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.IDN;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DnsNameResolver
/*      */   extends InetNameResolver
/*      */ {
/*      */   private static final InternalLogger logger;
/*      */   private static final String LOCALHOST = "localhost";
/*      */   private static final InetAddress LOCALHOST_ADDRESS;
/*      */   private static final DnsRecord[] EMPTY_ADDITIONALS;
/*      */   private static final DnsRecordType[] IPV4_ONLY_RESOLVED_RECORD_TYPES;
/*      */   private static final InternetProtocolFamily[] IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/*      */   private static final DnsRecordType[] IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
/*      */   private static final InternetProtocolFamily[] IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/*      */   private static final DnsRecordType[] IPV6_ONLY_RESOLVED_RECORD_TYPES;
/*      */   private static final InternetProtocolFamily[] IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/*      */   private static final DnsRecordType[] IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
/*      */   private static final InternetProtocolFamily[] IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/*      */   static final ResolvedAddressTypes DEFAULT_RESOLVE_ADDRESS_TYPES;
/*      */   static final String[] DEFAULT_SEARCH_DOMAINS;
/*      */   private static final int DEFAULT_NDOTS;
/*      */   
/*      */   static
/*      */   {
/*   85 */     logger = InternalLoggerFactory.getInstance(DnsNameResolver.class);
/*      */     
/*      */ 
/*   88 */     EMPTY_ADDITIONALS = new DnsRecord[0];
/*   89 */     IPV4_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A };
/*      */     
/*   91 */     IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4 };
/*      */     
/*   93 */     IPV4_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A, DnsRecordType.AAAA };
/*      */     
/*   95 */     IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4, InternetProtocolFamily.IPv6 };
/*      */     
/*   97 */     IPV6_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA };
/*      */     
/*   99 */     IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6 };
/*      */     
/*  101 */     IPV6_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA, DnsRecordType.A };
/*      */     
/*  103 */     IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6, InternetProtocolFamily.IPv4 };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  111 */     if (NetUtil.isIpV4StackPreferred()) {
/*  112 */       DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_ONLY;
/*  113 */       LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
/*      */     }
/*  115 */     else if (NetUtil.isIpV6AddressesPreferred()) {
/*  116 */       DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV6_PREFERRED;
/*  117 */       LOCALHOST_ADDRESS = NetUtil.LOCALHOST6;
/*      */     } else {
/*  119 */       DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_PREFERRED;
/*  120 */       LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     String[] searchDomains;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  130 */       List<String> list = PlatformDependent.isWindows() ? getSearchDomainsHack() : UnixResolverDnsServerAddressStreamProvider.parseEtcResolverSearchDomains();
/*  131 */       searchDomains = (String[])list.toArray(new String[0]);
/*      */     } catch (Exception ignore) {
/*      */       String[] searchDomains;
/*  134 */       searchDomains = EmptyArrays.EMPTY_STRINGS;
/*      */     }
/*  136 */     DEFAULT_SEARCH_DOMAINS = searchDomains;
/*      */     int ndots;
/*      */     try
/*      */     {
/*  140 */       ndots = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverFirstNdots();
/*      */     } catch (Exception ignore) { int ndots;
/*  142 */       ndots = 1;
/*      */     }
/*  144 */     DEFAULT_NDOTS = ndots;
/*      */   }
/*      */   
/*      */ 
/*      */   private static List<String> getSearchDomainsHack()
/*      */     throws Exception
/*      */   {
/*  151 */     Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
/*  152 */     Method open = configClass.getMethod("open", new Class[0]);
/*  153 */     Method nameservers = configClass.getMethod("searchlist", new Class[0]);
/*  154 */     Object instance = open.invoke(null, new Object[0]);
/*      */     
/*  156 */     return (List)nameservers.invoke(instance, new Object[0]);
/*      */   }
/*      */   
/*  159 */   private static final DatagramDnsResponseDecoder DECODER = new DatagramDnsResponseDecoder();
/*  160 */   private static final DatagramDnsQueryEncoder ENCODER = new DatagramDnsQueryEncoder();
/*      */   
/*      */ 
/*      */   final Future<Channel> channelFuture;
/*      */   
/*      */ 
/*      */   final Channel ch;
/*      */   
/*      */   private final Comparator<InetSocketAddress> nameServerComparator;
/*      */   
/*  170 */   final DnsQueryContextManager queryContextManager = new DnsQueryContextManager();
/*      */   
/*      */ 
/*      */   private final DnsCache resolveCache;
/*      */   
/*      */   private final AuthoritativeDnsServerCache authoritativeDnsServerCache;
/*      */   
/*      */   private final DnsCnameCache cnameCache;
/*      */   
/*  179 */   private final FastThreadLocal<DnsServerAddressStream> nameServerAddrStream = new FastThreadLocal()
/*      */   {
/*      */     protected DnsServerAddressStream initialValue()
/*      */     {
/*  183 */       return DnsNameResolver.this.dnsServerAddressStreamProvider.nameServerAddressStream("");
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */   private final long queryTimeoutMillis;
/*      */   
/*      */ 
/*      */ 
/*      */   private final int maxQueriesPerResolve;
/*      */   
/*      */ 
/*      */ 
/*      */   private final ResolvedAddressTypes resolvedAddressTypes;
/*      */   
/*      */ 
/*      */ 
/*      */   private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean recursionDesired;
/*      */   
/*      */ 
/*      */ 
/*      */   private final int maxPayloadSize;
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean optResourceEnabled;
/*      */   
/*      */ 
/*      */   private final HostsFileEntriesResolver hostsFileEntriesResolver;
/*      */   
/*      */ 
/*      */   private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
/*      */   
/*      */ 
/*      */   private final String[] searchDomains;
/*      */   
/*      */ 
/*      */   private final int ndots;
/*      */   
/*      */ 
/*      */   private final boolean supportsAAAARecords;
/*      */   
/*      */ 
/*      */   private final boolean supportsARecords;
/*      */   
/*      */ 
/*      */   private final InternetProtocolFamily preferredAddressType;
/*      */   
/*      */ 
/*      */   private final DnsRecordType[] resolveRecordTypes;
/*      */   
/*      */ 
/*      */   private final boolean decodeIdn;
/*      */   
/*      */ 
/*      */   private final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory;
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsCache resolveCache, DnsCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn)
/*      */   {
/*  250 */     this(eventLoop, channelFactory, resolveCache, new AuthoritativeDnsServerCacheAdapter(authoritativeDnsServerCache), dnsQueryLifecycleObserverFactory, queryTimeoutMillis, resolvedAddressTypes, recursionDesired, maxQueriesPerResolve, traceEnabled, maxPayloadSize, optResourceEnabled, hostsFileEntriesResolver, dnsServerAddressStreamProvider, searchDomains, ndots, decodeIdn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsCache resolveCache, AuthoritativeDnsServerCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn)
/*      */   {
/*  302 */     this(eventLoop, channelFactory, resolveCache, NoopDnsCnameCache.INSTANCE, authoritativeDnsServerCache, dnsQueryLifecycleObserverFactory, queryTimeoutMillis, resolvedAddressTypes, recursionDesired, maxQueriesPerResolve, traceEnabled, maxPayloadSize, optResourceEnabled, hostsFileEntriesResolver, dnsServerAddressStreamProvider, searchDomains, ndots, decodeIdn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, final DnsCache resolveCache, final DnsCnameCache cnameCache, final AuthoritativeDnsServerCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn)
/*      */   {
/*  327 */     super(eventLoop);
/*  328 */     this.queryTimeoutMillis = ObjectUtil.checkPositive(queryTimeoutMillis, "queryTimeoutMillis");
/*  329 */     this.resolvedAddressTypes = (resolvedAddressTypes != null ? resolvedAddressTypes : DEFAULT_RESOLVE_ADDRESS_TYPES);
/*  330 */     this.recursionDesired = recursionDesired;
/*  331 */     this.maxQueriesPerResolve = ObjectUtil.checkPositive(maxQueriesPerResolve, "maxQueriesPerResolve");
/*  332 */     this.maxPayloadSize = ObjectUtil.checkPositive(maxPayloadSize, "maxPayloadSize");
/*  333 */     this.optResourceEnabled = optResourceEnabled;
/*  334 */     this.hostsFileEntriesResolver = ((HostsFileEntriesResolver)ObjectUtil.checkNotNull(hostsFileEntriesResolver, "hostsFileEntriesResolver"));
/*  335 */     this.dnsServerAddressStreamProvider = 
/*  336 */       ((DnsServerAddressStreamProvider)ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider"));
/*  337 */     this.resolveCache = ((DnsCache)ObjectUtil.checkNotNull(resolveCache, "resolveCache"));
/*  338 */     this.cnameCache = ((DnsCnameCache)ObjectUtil.checkNotNull(cnameCache, "cnameCache"));
/*  339 */     this.dnsQueryLifecycleObserverFactory = (traceEnabled ? new BiDnsQueryLifecycleObserverFactory(new TraceDnsQueryLifeCycleObserverFactory(), dnsQueryLifecycleObserverFactory) : (dnsQueryLifecycleObserverFactory instanceof NoopDnsQueryLifecycleObserverFactory) ? new TraceDnsQueryLifeCycleObserverFactory() : 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  344 */       (DnsQueryLifecycleObserverFactory)ObjectUtil.checkNotNull(dnsQueryLifecycleObserverFactory, "dnsQueryLifecycleObserverFactory"));
/*  345 */     this.searchDomains = (searchDomains != null ? (String[])searchDomains.clone() : DEFAULT_SEARCH_DOMAINS);
/*  346 */     this.ndots = (ndots >= 0 ? ndots : DEFAULT_NDOTS);
/*  347 */     this.decodeIdn = decodeIdn;
/*      */     
/*  349 */     switch (this.resolvedAddressTypes) {
/*      */     case IPV4_ONLY: 
/*  351 */       this.supportsAAAARecords = false;
/*  352 */       this.supportsARecords = true;
/*  353 */       this.resolveRecordTypes = IPV4_ONLY_RESOLVED_RECORD_TYPES;
/*  354 */       this.resolvedInternetProtocolFamilies = IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/*  355 */       break;
/*      */     case IPV4_PREFERRED: 
/*  357 */       this.supportsAAAARecords = true;
/*  358 */       this.supportsARecords = true;
/*  359 */       this.resolveRecordTypes = IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
/*  360 */       this.resolvedInternetProtocolFamilies = IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/*  361 */       break;
/*      */     case IPV6_ONLY: 
/*  363 */       this.supportsAAAARecords = true;
/*  364 */       this.supportsARecords = false;
/*  365 */       this.resolveRecordTypes = IPV6_ONLY_RESOLVED_RECORD_TYPES;
/*  366 */       this.resolvedInternetProtocolFamilies = IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/*  367 */       break;
/*      */     case IPV6_PREFERRED: 
/*  369 */       this.supportsAAAARecords = true;
/*  370 */       this.supportsARecords = true;
/*  371 */       this.resolveRecordTypes = IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
/*  372 */       this.resolvedInternetProtocolFamilies = IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/*  373 */       break;
/*      */     default: 
/*  375 */       throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
/*      */     }
/*  377 */     this.preferredAddressType = preferredAddressType(resolvedAddressTypes);
/*  378 */     this.authoritativeDnsServerCache = ((AuthoritativeDnsServerCache)ObjectUtil.checkNotNull(authoritativeDnsServerCache, "authoritativeDnsServerCache"));
/*  379 */     this.nameServerComparator = new NameServerComparator(this.preferredAddressType.addressType());
/*      */     
/*  381 */     Bootstrap b = new Bootstrap();
/*  382 */     b.group(executor());
/*  383 */     b.channelFactory(channelFactory);
/*  384 */     b.option(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, Boolean.valueOf(true));
/*  385 */     final DnsResponseHandler responseHandler = new DnsResponseHandler(executor().newPromise());
/*  386 */     b.handler(new ChannelInitializer()
/*      */     {
/*      */       protected void initChannel(DatagramChannel ch) throws Exception {
/*  389 */         ch.pipeline().addLast(new ChannelHandler[] { DnsNameResolver.DECODER, DnsNameResolver.ENCODER, responseHandler });
/*      */       }
/*      */       
/*  392 */     });
/*  393 */     this.channelFuture = responseHandler.channelActivePromise;
/*  394 */     ChannelFuture future = b.register();
/*  395 */     Throwable cause = future.cause();
/*  396 */     if (cause != null) {
/*  397 */       if ((cause instanceof RuntimeException)) {
/*  398 */         throw ((RuntimeException)cause);
/*      */       }
/*  400 */       if ((cause instanceof Error)) {
/*  401 */         throw ((Error)cause);
/*      */       }
/*  403 */       throw new IllegalStateException("Unable to create / register Channel", cause);
/*      */     }
/*  405 */     this.ch = future.channel();
/*  406 */     this.ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(maxPayloadSize));
/*      */     
/*  408 */     this.ch.closeFuture().addListener(new ChannelFutureListener()
/*      */     {
/*      */       public void operationComplete(ChannelFuture future) {
/*  411 */         resolveCache.clear();
/*  412 */         cnameCache.clear();
/*  413 */         authoritativeDnsServerCache.clear();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   static InternetProtocolFamily preferredAddressType(ResolvedAddressTypes resolvedAddressTypes) {
/*  419 */     switch (resolvedAddressTypes) {
/*      */     case IPV4_ONLY: 
/*      */     case IPV4_PREFERRED: 
/*  422 */       return InternetProtocolFamily.IPv4;
/*      */     case IPV6_ONLY: 
/*      */     case IPV6_PREFERRED: 
/*  425 */       return InternetProtocolFamily.IPv6;
/*      */     }
/*  427 */     throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
/*      */   }
/*      */   
/*      */ 
/*      */   InetSocketAddress newRedirectServerAddress(InetAddress server)
/*      */   {
/*  433 */     return new InetSocketAddress(server, 53);
/*      */   }
/*      */   
/*      */   final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory() {
/*  437 */     return this.dnsQueryLifecycleObserverFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DnsServerAddressStream newRedirectDnsServerStream(String hostname, List<InetSocketAddress> nameservers)
/*      */   {
/*  453 */     DnsServerAddressStream cached = authoritativeDnsServerCache().get(hostname);
/*  454 */     if ((cached == null) || (cached.size() == 0))
/*      */     {
/*      */ 
/*  457 */       Collections.sort(nameservers, this.nameServerComparator);
/*  458 */       return new SequentialDnsServerAddressStream(nameservers, 0);
/*      */     }
/*  460 */     return cached;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DnsCache resolveCache()
/*      */   {
/*  467 */     return this.resolveCache;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   DnsCnameCache cnameCache()
/*      */   {
/*  474 */     return this.cnameCache;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AuthoritativeDnsServerCache authoritativeDnsServerCache()
/*      */   {
/*  481 */     return this.authoritativeDnsServerCache;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public long queryTimeoutMillis()
/*      */   {
/*  489 */     return this.queryTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResolvedAddressTypes resolvedAddressTypes()
/*      */   {
/*  497 */     return this.resolvedAddressTypes;
/*      */   }
/*      */   
/*      */   InternetProtocolFamily[] resolvedInternetProtocolFamiliesUnsafe() {
/*  501 */     return this.resolvedInternetProtocolFamilies;
/*      */   }
/*      */   
/*      */   final String[] searchDomains() {
/*  505 */     return this.searchDomains;
/*      */   }
/*      */   
/*      */   final int ndots() {
/*  509 */     return this.ndots;
/*      */   }
/*      */   
/*      */   final boolean supportsAAAARecords() {
/*  513 */     return this.supportsAAAARecords;
/*      */   }
/*      */   
/*      */   final boolean supportsARecords() {
/*  517 */     return this.supportsARecords;
/*      */   }
/*      */   
/*      */   final InternetProtocolFamily preferredAddressType() {
/*  521 */     return this.preferredAddressType;
/*      */   }
/*      */   
/*      */   final DnsRecordType[] resolveRecordTypes() {
/*  525 */     return this.resolveRecordTypes;
/*      */   }
/*      */   
/*      */   final boolean isDecodeIdn() {
/*  529 */     return this.decodeIdn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRecursionDesired()
/*      */   {
/*  537 */     return this.recursionDesired;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int maxQueriesPerResolve()
/*      */   {
/*  545 */     return this.maxQueriesPerResolve;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int maxPayloadSize()
/*      */   {
/*  552 */     return this.maxPayloadSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOptResourceEnabled()
/*      */   {
/*  560 */     return this.optResourceEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public HostsFileEntriesResolver hostsFileEntriesResolver()
/*      */   {
/*  568 */     return this.hostsFileEntriesResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void close()
/*      */   {
/*  578 */     if (this.ch.isOpen()) {
/*  579 */       this.ch.close();
/*      */     }
/*      */   }
/*      */   
/*      */   protected EventLoop executor()
/*      */   {
/*  585 */     return (EventLoop)super.executor();
/*      */   }
/*      */   
/*      */   private InetAddress resolveHostsFileEntry(String hostname) {
/*  589 */     if (this.hostsFileEntriesResolver == null) {
/*  590 */       return null;
/*      */     }
/*  592 */     InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
/*  593 */     if ((address == null) && (PlatformDependent.isWindows()) && ("localhost".equalsIgnoreCase(hostname)))
/*      */     {
/*      */ 
/*      */ 
/*  597 */       return LOCALHOST_ADDRESS;
/*      */     }
/*  599 */     return address;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals)
/*      */   {
/*  612 */     return resolve(inetHost, additionals, executor().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals, Promise<InetAddress> promise)
/*      */   {
/*  626 */     ObjectUtil.checkNotNull(promise, "promise");
/*  627 */     DnsRecord[] additionalsArray = toArray(additionals, true);
/*      */     try {
/*  629 */       doResolve(inetHost, additionalsArray, promise, this.resolveCache);
/*  630 */       return promise;
/*      */     } catch (Exception e) {
/*  632 */       return promise.setFailure(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals)
/*      */   {
/*  645 */     return resolveAll(inetHost, additionals, executor().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals, Promise<List<InetAddress>> promise)
/*      */   {
/*  659 */     ObjectUtil.checkNotNull(promise, "promise");
/*  660 */     DnsRecord[] additionalsArray = toArray(additionals, true);
/*      */     try {
/*  662 */       doResolveAll(inetHost, additionalsArray, promise, this.resolveCache);
/*  663 */       return promise;
/*      */     } catch (Exception e) {
/*  665 */       return promise.setFailure(e);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doResolve(String inetHost, Promise<InetAddress> promise) throws Exception
/*      */   {
/*  671 */     doResolve(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<List<DnsRecord>> resolveAll(DnsQuestion question)
/*      */   {
/*  686 */     return resolveAll(question, EMPTY_ADDITIONALS, executor().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals)
/*      */   {
/*  702 */     return resolveAll(question, additionals, executor().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals, Promise<List<DnsRecord>> promise)
/*      */   {
/*  720 */     DnsRecord[] additionalsArray = toArray(additionals, true);
/*  721 */     return resolveAll(question, additionalsArray, promise);
/*      */   }
/*      */   
/*      */   private Future<List<DnsRecord>> resolveAll(DnsQuestion question, DnsRecord[] additionals, Promise<List<DnsRecord>> promise)
/*      */   {
/*  726 */     ObjectUtil.checkNotNull(question, "question");
/*  727 */     ObjectUtil.checkNotNull(promise, "promise");
/*      */     
/*      */ 
/*  730 */     DnsRecordType type = question.type();
/*  731 */     String hostname = question.name();
/*      */     
/*  733 */     if ((type == DnsRecordType.A) || (type == DnsRecordType.AAAA)) {
/*  734 */       InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
/*  735 */       if (hostsFileEntry != null) {
/*  736 */         ByteBuf content = null;
/*  737 */         if ((hostsFileEntry instanceof Inet4Address)) {
/*  738 */           if (type == DnsRecordType.A) {
/*  739 */             content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
/*      */           }
/*  741 */         } else if (((hostsFileEntry instanceof Inet6Address)) && 
/*  742 */           (type == DnsRecordType.AAAA)) {
/*  743 */           content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
/*      */         }
/*      */         
/*      */ 
/*  747 */         if (content != null)
/*      */         {
/*      */ 
/*  750 */           trySuccess(promise, Collections.singletonList(new DefaultDnsRawRecord(hostname, type, 86400L, content)));
/*      */           
/*  752 */           return promise;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  759 */     DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
/*  760 */     new DnsRecordResolveContext(this, question, additionals, nameServerAddrs).resolve(promise);
/*  761 */     return promise;
/*      */   }
/*      */   
/*      */   private static DnsRecord[] toArray(Iterable<DnsRecord> additionals, boolean validateType) {
/*  765 */     ObjectUtil.checkNotNull(additionals, "additionals");
/*  766 */     if ((additionals instanceof Collection)) {
/*  767 */       Collection<DnsRecord> records = (Collection)additionals;
/*  768 */       for (DnsRecord r : additionals) {
/*  769 */         validateAdditional(r, validateType);
/*      */       }
/*  771 */       return (DnsRecord[])records.toArray(new DnsRecord[records.size()]);
/*      */     }
/*      */     
/*  774 */     Iterator<DnsRecord> additionalsIt = additionals.iterator();
/*  775 */     if (!additionalsIt.hasNext()) {
/*  776 */       return EMPTY_ADDITIONALS;
/*      */     }
/*  778 */     Object records = new ArrayList();
/*      */     do {
/*  780 */       DnsRecord r = (DnsRecord)additionalsIt.next();
/*  781 */       validateAdditional(r, validateType);
/*  782 */       ((List)records).add(r);
/*  783 */     } while (additionalsIt.hasNext());
/*      */     
/*  785 */     return (DnsRecord[])((List)records).toArray(new DnsRecord[((List)records).size()]);
/*      */   }
/*      */   
/*      */   private static void validateAdditional(DnsRecord record, boolean validateType) {
/*  789 */     ObjectUtil.checkNotNull(record, "record");
/*  790 */     if ((validateType) && ((record instanceof DnsRawRecord))) {
/*  791 */       throw new IllegalArgumentException("DnsRawRecord implementations not allowed: " + record);
/*      */     }
/*      */   }
/*      */   
/*      */   private InetAddress loopbackAddress() {
/*  796 */     return preferredAddressType().localhost();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doResolve(String inetHost, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache)
/*      */     throws Exception
/*      */   {
/*  807 */     if ((inetHost == null) || (inetHost.isEmpty()))
/*      */     {
/*  809 */       promise.setSuccess(loopbackAddress());
/*  810 */       return;
/*      */     }
/*  812 */     byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
/*  813 */     if (bytes != null)
/*      */     {
/*  815 */       promise.setSuccess(InetAddress.getByAddress(bytes));
/*  816 */       return;
/*      */     }
/*      */     
/*  819 */     String hostname = hostname(inetHost);
/*      */     
/*  821 */     InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
/*  822 */     if (hostsFileEntry != null) {
/*  823 */       promise.setSuccess(hostsFileEntry);
/*  824 */       return;
/*      */     }
/*      */     
/*  827 */     if (!doResolveCached(hostname, additionals, promise, resolveCache)) {
/*  828 */       doResolveUncached(hostname, additionals, promise, resolveCache);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean doResolveCached(String hostname, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache)
/*      */   {
/*  836 */     List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
/*  837 */     if ((cachedEntries == null) || (cachedEntries.isEmpty())) {
/*  838 */       return false;
/*      */     }
/*      */     
/*  841 */     Throwable cause = ((DnsCacheEntry)cachedEntries.get(0)).cause();
/*  842 */     if (cause == null) {
/*  843 */       int numEntries = cachedEntries.size();
/*      */       
/*  845 */       for (InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
/*  846 */         for (int i = 0; i < numEntries; i++) {
/*  847 */           DnsCacheEntry e = (DnsCacheEntry)cachedEntries.get(i);
/*  848 */           if (f.addressType().isInstance(e.address())) {
/*  849 */             trySuccess(promise, e.address());
/*  850 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*  854 */       return false;
/*      */     }
/*  856 */     tryFailure(promise, cause);
/*  857 */     return true;
/*      */   }
/*      */   
/*      */   static <T> void trySuccess(Promise<T> promise, T result)
/*      */   {
/*  862 */     if (!promise.trySuccess(result)) {
/*  863 */       logger.warn("Failed to notify success ({}) to a promise: {}", result, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void tryFailure(Promise<?> promise, Throwable cause) {
/*  868 */     if (!promise.tryFailure(cause)) {
/*  869 */       logger.warn("Failed to notify failure to a promise: {}", promise, cause);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void doResolveUncached(String hostname, DnsRecord[] additionals, final Promise<InetAddress> promise, DnsCache resolveCache)
/*      */   {
/*  877 */     Promise<List<InetAddress>> allPromise = executor().newPromise();
/*  878 */     doResolveAllUncached(hostname, additionals, allPromise, resolveCache);
/*  879 */     allPromise.addListener(new FutureListener()
/*      */     {
/*      */       public void operationComplete(Future<List<InetAddress>> future) {
/*  882 */         if (future.isSuccess()) {
/*  883 */           DnsNameResolver.trySuccess(promise, ((List)future.getNow()).get(0));
/*      */         } else {
/*  885 */           DnsNameResolver.tryFailure(promise, future.cause());
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected void doResolveAll(String inetHost, Promise<List<InetAddress>> promise) throws Exception
/*      */   {
/*  893 */     doResolveAll(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doResolveAll(String inetHost, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache)
/*      */     throws Exception
/*      */   {
/*  904 */     if ((inetHost == null) || (inetHost.isEmpty()))
/*      */     {
/*  906 */       promise.setSuccess(Collections.singletonList(loopbackAddress()));
/*  907 */       return;
/*      */     }
/*  909 */     byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
/*  910 */     if (bytes != null)
/*      */     {
/*  912 */       promise.setSuccess(Collections.singletonList(InetAddress.getByAddress(bytes)));
/*  913 */       return;
/*      */     }
/*      */     
/*  916 */     String hostname = hostname(inetHost);
/*      */     
/*  918 */     InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
/*  919 */     if (hostsFileEntry != null) {
/*  920 */       promise.setSuccess(Collections.singletonList(hostsFileEntry));
/*  921 */       return;
/*      */     }
/*      */     
/*  924 */     if (!doResolveAllCached(hostname, additionals, promise, resolveCache, this.resolvedInternetProtocolFamilies)) {
/*  925 */       doResolveAllUncached(hostname, additionals, promise, resolveCache);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean doResolveAllCached(String hostname, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache, InternetProtocolFamily[] resolvedInternetProtocolFamilies)
/*      */   {
/*  934 */     List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
/*  935 */     if ((cachedEntries == null) || (cachedEntries.isEmpty())) {
/*  936 */       return false;
/*      */     }
/*      */     
/*  939 */     Throwable cause = ((DnsCacheEntry)cachedEntries.get(0)).cause();
/*  940 */     if (cause == null) {
/*  941 */       List<InetAddress> result = null;
/*  942 */       int numEntries = cachedEntries.size();
/*  943 */       for (InternetProtocolFamily f : resolvedInternetProtocolFamilies) {
/*  944 */         for (int i = 0; i < numEntries; i++) {
/*  945 */           DnsCacheEntry e = (DnsCacheEntry)cachedEntries.get(i);
/*  946 */           if (f.addressType().isInstance(e.address())) {
/*  947 */             if (result == null) {
/*  948 */               result = new ArrayList(numEntries);
/*      */             }
/*  950 */             result.add(e.address());
/*      */           }
/*      */         }
/*      */       }
/*  954 */       if (result != null) {
/*  955 */         trySuccess(promise, result);
/*  956 */         return true;
/*      */       }
/*  958 */       return false;
/*      */     }
/*  960 */     tryFailure(promise, cause);
/*  961 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void doResolveAllUncached(String hostname, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache)
/*      */   {
/*  970 */     DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
/*  971 */     new DnsAddressResolveContext(this, hostname, additionals, nameServerAddrs, resolveCache, this.authoritativeDnsServerCache)
/*  972 */       .resolve(promise);
/*      */   }
/*      */   
/*      */   private static String hostname(String inetHost) {
/*  976 */     String hostname = IDN.toASCII(inetHost);
/*      */     
/*  978 */     if ((StringUtil.endsWith(inetHost, '.')) && (!StringUtil.endsWith(hostname, '.'))) {
/*  979 */       hostname = hostname + ".";
/*      */     }
/*  981 */     return hostname;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question)
/*      */   {
/*  988 */     return query(nextNameServerAddress(), question);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Iterable<DnsRecord> additionals)
/*      */   {
/*  996 */     return query(nextNameServerAddress(), question, additionals);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise)
/*      */   {
/* 1004 */     return query(nextNameServerAddress(), question, Collections.emptyList(), promise);
/*      */   }
/*      */   
/*      */   private InetSocketAddress nextNameServerAddress() {
/* 1008 */     return ((DnsServerAddressStream)this.nameServerAddrStream.get()).next();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question)
/*      */   {
/* 1017 */     return query0(nameServerAddr, question, EMPTY_ADDITIONALS, this.ch
/* 1018 */       .eventLoop().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals)
/*      */   {
/* 1027 */     return query0(nameServerAddr, question, toArray(additionals, false), this.ch
/* 1028 */       .eventLoop().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise)
/*      */   {
/* 1038 */     return query0(nameServerAddr, question, EMPTY_ADDITIONALS, promise);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise)
/*      */   {
/* 1049 */     return query0(nameServerAddr, question, toArray(additionals, false), promise);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isTransportOrTimeoutError(Throwable cause)
/*      */   {
/* 1058 */     return (cause != null) && ((cause.getCause() instanceof DnsNameResolverException));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isTimeoutError(Throwable cause)
/*      */   {
/* 1067 */     return (cause != null) && ((cause.getCause() instanceof DnsNameResolverTimeoutException));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise)
/*      */   {
/* 1074 */     return query0(nameServerAddr, question, additionals, this.ch.newPromise(), promise);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, ChannelPromise writePromise, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise)
/*      */   {
/* 1082 */     assert (!writePromise.isVoid());
/*      */     
/* 1084 */     Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise = cast(
/* 1085 */       (Promise)ObjectUtil.checkNotNull(promise, "promise"));
/*      */     try {
/* 1087 */       new DnsQueryContext(this, nameServerAddr, question, additionals, castPromise).query(writePromise);
/* 1088 */       return castPromise;
/*      */     } catch (Exception e) {
/* 1090 */       return castPromise.setFailure(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private static Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> cast(Promise<?> promise)
/*      */   {
/* 1096 */     return promise;
/*      */   }
/*      */   
/*      */   final DnsServerAddressStream newNameServerAddressStream(String hostname) {
/* 1100 */     return this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
/*      */   }
/*      */   
/*      */   private final class DnsResponseHandler extends ChannelInboundHandlerAdapter
/*      */   {
/*      */     private final Promise<Channel> channelActivePromise;
/*      */     
/*      */     DnsResponseHandler() {
/* 1108 */       this.channelActivePromise = channelActivePromise;
/*      */     }
/*      */     
/*      */     public void channelRead(ChannelHandlerContext ctx, Object msg)
/*      */     {
/*      */       try {
/* 1114 */         DatagramDnsResponse res = (DatagramDnsResponse)msg;
/* 1115 */         int queryId = res.id();
/*      */         
/* 1117 */         if (DnsNameResolver.logger.isDebugEnabled()) {
/* 1118 */           DnsNameResolver.logger.debug("{} RECEIVED: [{}: {}], {}", new Object[] { DnsNameResolver.this.ch, Integer.valueOf(queryId), res.sender(), res });
/*      */         }
/*      */         
/* 1121 */         DnsQueryContext qCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId);
/* 1122 */         if (qCtx == null) {
/* 1123 */           DnsNameResolver.logger.warn("{} Received a DNS response with an unknown ID: {}", DnsNameResolver.this.ch, Integer.valueOf(queryId));
/* 1124 */           return;
/*      */         }
/*      */         
/* 1127 */         qCtx.finish(res);
/*      */       } finally {
/* 1129 */         ReferenceCountUtil.safeRelease(msg);
/*      */       }
/*      */     }
/*      */     
/*      */     public void channelActive(ChannelHandlerContext ctx) throws Exception
/*      */     {
/* 1135 */       super.channelActive(ctx);
/* 1136 */       this.channelActivePromise.setSuccess(ctx.channel());
/*      */     }
/*      */     
/*      */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*      */     {
/* 1141 */       DnsNameResolver.logger.warn("{} Unexpected exception: ", DnsNameResolver.this.ch, cause);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */