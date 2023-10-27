/*      */ package io.netty.resolver.dns;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufHolder;
/*      */ import io.netty.channel.AddressedEnvelope;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.EventLoop;
/*      */ import io.netty.handler.codec.CorruptedFrameException;
/*      */ import io.netty.handler.codec.dns.DefaultDnsQuestion;
/*      */ import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
/*      */ import io.netty.handler.codec.dns.DnsQuestion;
/*      */ import io.netty.handler.codec.dns.DnsRawRecord;
/*      */ import io.netty.handler.codec.dns.DnsRecord;
/*      */ import io.netty.handler.codec.dns.DnsRecordType;
/*      */ import io.netty.handler.codec.dns.DnsResponse;
/*      */ import io.netty.handler.codec.dns.DnsResponseCode;
/*      */ import io.netty.handler.codec.dns.DnsSection;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.FutureListener;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.util.AbstractList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
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
/*      */ abstract class DnsResolveContext<T>
/*      */ {
/*   64 */   private static final FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>> RELEASE_RESPONSE = new FutureListener()
/*      */   {
/*      */     public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future)
/*      */     {
/*   68 */       if (future.isSuccess()) {
/*   69 */         ((AddressedEnvelope)future.getNow()).release();
/*      */       }
/*      */     }
/*      */   };
/*   73 */   private static final RuntimeException NXDOMAIN_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No answer found and NXDOMAIN response code returned"), DnsResolveContext.class, "onResponse(..)");
/*      */   
/*      */ 
/*      */ 
/*   77 */   private static final RuntimeException CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No matching CNAME record found"), DnsResolveContext.class, "onResponseCNAME(..)");
/*      */   
/*      */ 
/*      */ 
/*   81 */   private static final RuntimeException NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No matching record type found"), DnsResolveContext.class, "onResponseAorAAAA(..)");
/*      */   
/*      */ 
/*      */ 
/*   85 */   private static final RuntimeException UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("Response type was unrecognized"), DnsResolveContext.class, "onResponse(..)");
/*      */   
/*      */ 
/*      */ 
/*   89 */   private static final RuntimeException NAME_SERVERS_EXHAUSTED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No name servers returned an answer"), DnsResolveContext.class, "tryToFinishResolve(..)");
/*      */   
/*      */   final DnsNameResolver parent;
/*      */   
/*      */   private final DnsServerAddressStream nameServerAddrs;
/*      */   
/*      */   private final String hostname;
/*      */   
/*      */   private final int dnsClass;
/*      */   
/*      */   private final DnsRecordType[] expectedTypes;
/*      */   
/*      */   private final int maxAllowedQueries;
/*      */   final DnsRecord[] additionals;
/*  103 */   private final Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> queriesInProgress = Collections.newSetFromMap(new IdentityHashMap());
/*      */   
/*      */   private List<T> finalResult;
/*      */   
/*      */   private int allowedQueries;
/*      */   
/*      */   private boolean triedCNAME;
/*      */   
/*      */ 
/*      */   DnsResolveContext(DnsNameResolver parent, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs)
/*      */   {
/*  114 */     assert (expectedTypes.length > 0);
/*      */     
/*  116 */     this.parent = parent;
/*  117 */     this.hostname = hostname;
/*  118 */     this.dnsClass = dnsClass;
/*  119 */     this.expectedTypes = expectedTypes;
/*  120 */     this.additionals = additionals;
/*      */     
/*  122 */     this.nameServerAddrs = ((DnsServerAddressStream)ObjectUtil.checkNotNull(nameServerAddrs, "nameServerAddrs"));
/*  123 */     this.maxAllowedQueries = parent.maxQueriesPerResolve();
/*  124 */     this.allowedQueries = this.maxAllowedQueries;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   DnsCache resolveCache()
/*      */   {
/*  131 */     return this.parent.resolveCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   DnsCnameCache cnameCache()
/*      */   {
/*  138 */     return this.parent.cnameCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   AuthoritativeDnsServerCache authoritativeDnsServerCache()
/*      */   {
/*  145 */     return this.parent.authoritativeDnsServerCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract DnsResolveContext<T> newResolverContext(DnsNameResolver paramDnsNameResolver, String paramString, int paramInt, DnsRecordType[] paramArrayOfDnsRecordType, DnsRecord[] paramArrayOfDnsRecord, DnsServerAddressStream paramDnsServerAddressStream);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract T convertRecord(DnsRecord paramDnsRecord, String paramString, DnsRecord[] paramArrayOfDnsRecord, EventLoop paramEventLoop);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract List<T> filterResults(List<T> paramList);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract void cache(String paramString, DnsRecord[] paramArrayOfDnsRecord, DnsRecord paramDnsRecord, T paramT);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract void cache(String paramString, DnsRecord[] paramArrayOfDnsRecord, UnknownHostException paramUnknownHostException);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void resolve(final Promise<List<T>> promise)
/*      */   {
/*  180 */     final String[] searchDomains = this.parent.searchDomains();
/*  181 */     if ((searchDomains.length == 0) || (this.parent.ndots() == 0) || (StringUtil.endsWith(this.hostname, '.'))) {
/*  182 */       internalResolve(this.hostname, promise);
/*      */     } else {
/*  184 */       final boolean startWithoutSearchDomain = hasNDots();
/*  185 */       String initialHostname = this.hostname + '.' + searchDomains[0];
/*  186 */       final int initialSearchDomainIdx = startWithoutSearchDomain ? 0 : 1;
/*      */       
/*  188 */       Promise<List<T>> searchDomainPromise = this.parent.executor().newPromise();
/*  189 */       searchDomainPromise.addListener(new FutureListener() {
/*  190 */         private int searchDomainIdx = initialSearchDomainIdx;
/*      */         
/*      */         public void operationComplete(Future<List<T>> future) {
/*  193 */           Throwable cause = future.cause();
/*  194 */           if (cause == null) {
/*  195 */             promise.trySuccess(future.getNow());
/*      */           }
/*  197 */           else if (DnsNameResolver.isTransportOrTimeoutError(cause)) {
/*  198 */             promise.tryFailure(new DnsResolveContext.SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
/*  199 */           } else if (this.searchDomainIdx < searchDomains.length) {
/*  200 */             Promise<List<T>> newPromise = DnsResolveContext.this.parent.executor().newPromise();
/*  201 */             newPromise.addListener(this);
/*  202 */             DnsResolveContext.this.doSearchDomainQuery(DnsResolveContext.this.hostname + '.' + searchDomains[(this.searchDomainIdx++)], newPromise);
/*  203 */           } else if (!startWithoutSearchDomain) {
/*  204 */             DnsResolveContext.this.internalResolve(DnsResolveContext.this.hostname, promise);
/*      */           } else {
/*  206 */             promise.tryFailure(new DnsResolveContext.SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
/*      */           }
/*      */           
/*      */         }
/*  210 */       });
/*  211 */       doSearchDomainQuery(initialHostname, searchDomainPromise);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean hasNDots() {
/*  216 */     int idx = this.hostname.length() - 1; for (int dots = 0; idx >= 0; idx--) {
/*  217 */       if (this.hostname.charAt(idx) == '.') { dots++; if (dots >= this.parent.ndots())
/*  218 */           return true;
/*      */       }
/*      */     }
/*  221 */     return false;
/*      */   }
/*      */   
/*      */   private static final class SearchDomainUnknownHostException extends UnknownHostException {
/*      */     private static final long serialVersionUID = -8573510133644997085L;
/*      */     
/*      */     SearchDomainUnknownHostException(Throwable cause, String originalHostname) {
/*  228 */       super();
/*  229 */       setStackTrace(cause.getStackTrace());
/*      */       
/*      */ 
/*  232 */       initCause(cause.getCause());
/*      */     }
/*      */     
/*      */     public Throwable fillInStackTrace()
/*      */     {
/*  237 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   void doSearchDomainQuery(String hostname, Promise<List<T>> nextPromise) {
/*  242 */     DnsResolveContext<T> nextContext = newResolverContext(this.parent, hostname, this.dnsClass, this.expectedTypes, this.additionals, this.nameServerAddrs);
/*      */     
/*  244 */     nextContext.internalResolve(hostname, nextPromise);
/*      */   }
/*      */   
/*      */   private static String hostnameWithDot(String name) {
/*  248 */     if (StringUtil.endsWith(name, '.')) {
/*  249 */       return name;
/*      */     }
/*  251 */     return name + '.';
/*      */   }
/*      */   
/*      */   private void internalResolve(String name, Promise<List<T>> promise)
/*      */   {
/*      */     for (;;) {
/*  257 */       String mapping = cnameCache().get(hostnameWithDot(name));
/*  258 */       if (mapping == null) {
/*      */         break;
/*      */       }
/*  261 */       name = mapping;
/*      */     }
/*      */     
/*  264 */     DnsServerAddressStream nameServerAddressStream = getNameServers(name);
/*      */     
/*  266 */     int end = this.expectedTypes.length - 1;
/*  267 */     for (int i = 0; i < end; i++) {
/*  268 */       if (!query(name, this.expectedTypes[i], nameServerAddressStream.duplicate(), promise)) {
/*  269 */         return;
/*      */       }
/*      */     }
/*  272 */     query(name, this.expectedTypes[end], nameServerAddressStream, promise);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private DnsServerAddressStream getNameServersFromCache(String hostname)
/*      */   {
/*  280 */     int len = hostname.length();
/*      */     
/*  282 */     if (len == 0)
/*      */     {
/*  284 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  288 */     if (hostname.charAt(len - 1) != '.') {
/*  289 */       hostname = hostname + ".";
/*      */     }
/*      */     
/*  292 */     int idx = hostname.indexOf('.');
/*  293 */     if (idx == hostname.length() - 1)
/*      */     {
/*  295 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/*  301 */       hostname = hostname.substring(idx + 1);
/*      */       
/*  303 */       int idx2 = hostname.indexOf('.');
/*  304 */       if ((idx2 <= 0) || (idx2 == hostname.length() - 1))
/*      */       {
/*  306 */         return null;
/*      */       }
/*  308 */       idx = idx2;
/*      */       
/*  310 */       DnsServerAddressStream entries = authoritativeDnsServerCache().get(hostname);
/*  311 */       if (entries != null)
/*      */       {
/*      */ 
/*  314 */         return entries;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void query(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, Promise<List<T>> promise, Throwable cause)
/*      */   {
/*  322 */     query(nameServerAddrStream, nameServerAddrStreamIndex, question, this.parent
/*  323 */       .dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question), promise, cause);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void query(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, Throwable cause)
/*      */   {
/*  332 */     if ((nameServerAddrStreamIndex >= nameServerAddrStream.size()) || (this.allowedQueries == 0) || (promise.isCancelled())) {
/*  333 */       tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
/*      */       
/*  335 */       return;
/*      */     }
/*      */     
/*  338 */     this.allowedQueries -= 1;
/*      */     
/*  340 */     InetSocketAddress nameServerAddr = nameServerAddrStream.next();
/*  341 */     if (nameServerAddr.isUnresolved()) {
/*  342 */       queryUnresolvedNameserver(nameServerAddr, nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
/*      */       
/*  344 */       return;
/*      */     }
/*  346 */     ChannelPromise writePromise = this.parent.ch.newPromise();
/*  347 */     Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = this.parent.query0(nameServerAddr, question, this.additionals, writePromise, this.parent.ch
/*      */     
/*  349 */       .eventLoop().newPromise());
/*  350 */     this.queriesInProgress.add(f);
/*      */     
/*  352 */     queryLifecycleObserver.queryWritten(nameServerAddr, writePromise);
/*      */     
/*  354 */     f.addListener(new FutureListener()
/*      */     {
/*      */       /* Error */
/*      */       public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 1	io/netty/resolver/dns/DnsResolveContext$3:this$0	Lio/netty/resolver/dns/DnsResolveContext;
/*      */         //   4: invokestatic 8	io/netty/resolver/dns/DnsResolveContext:access$200	(Lio/netty/resolver/dns/DnsResolveContext;)Ljava/util/Set;
/*      */         //   7: aload_1
/*      */         //   8: invokeinterface 9 2 0
/*      */         //   13: pop
/*      */         //   14: aload_0
/*      */         //   15: getfield 2	io/netty/resolver/dns/DnsResolveContext$3:val$promise	Lio/netty/util/concurrent/Promise;
/*      */         //   18: invokeinterface 10 1 0
/*      */         //   23: ifne +12 -> 35
/*      */         //   26: aload_1
/*      */         //   27: invokeinterface 11 1 0
/*      */         //   32: ifeq +41 -> 73
/*      */         //   35: aload_0
/*      */         //   36: getfield 3	io/netty/resolver/dns/DnsResolveContext$3:val$queryLifecycleObserver	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*      */         //   39: aload_0
/*      */         //   40: getfield 1	io/netty/resolver/dns/DnsResolveContext$3:this$0	Lio/netty/resolver/dns/DnsResolveContext;
/*      */         //   43: invokestatic 12	io/netty/resolver/dns/DnsResolveContext:access$300	(Lio/netty/resolver/dns/DnsResolveContext;)I
/*      */         //   46: invokeinterface 13 2 0
/*      */         //   51: aload_1
/*      */         //   52: invokeinterface 14 1 0
/*      */         //   57: checkcast 15	io/netty/channel/AddressedEnvelope
/*      */         //   60: astore_2
/*      */         //   61: aload_2
/*      */         //   62: ifnull +10 -> 72
/*      */         //   65: aload_2
/*      */         //   66: invokeinterface 16 1 0
/*      */         //   71: pop
/*      */         //   72: return
/*      */         //   73: aload_1
/*      */         //   74: invokeinterface 17 1 0
/*      */         //   79: astore_2
/*      */         //   80: aload_2
/*      */         //   81: ifnonnull +42 -> 123
/*      */         //   84: aload_0
/*      */         //   85: getfield 1	io/netty/resolver/dns/DnsResolveContext$3:this$0	Lio/netty/resolver/dns/DnsResolveContext;
/*      */         //   88: aload_0
/*      */         //   89: getfield 4	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStream	Lio/netty/resolver/dns/DnsServerAddressStream;
/*      */         //   92: aload_0
/*      */         //   93: getfield 5	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStreamIndex	I
/*      */         //   96: aload_0
/*      */         //   97: getfield 6	io/netty/resolver/dns/DnsResolveContext$3:val$question	Lio/netty/handler/codec/dns/DnsQuestion;
/*      */         //   100: aload_1
/*      */         //   101: invokeinterface 14 1 0
/*      */         //   106: checkcast 15	io/netty/channel/AddressedEnvelope
/*      */         //   109: aload_0
/*      */         //   110: getfield 3	io/netty/resolver/dns/DnsResolveContext$3:val$queryLifecycleObserver	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*      */         //   113: aload_0
/*      */         //   114: getfield 2	io/netty/resolver/dns/DnsResolveContext$3:val$promise	Lio/netty/util/concurrent/Promise;
/*      */         //   117: invokestatic 18	io/netty/resolver/dns/DnsResolveContext:access$400	(Lio/netty/resolver/dns/DnsResolveContext;Lio/netty/resolver/dns/DnsServerAddressStream;ILio/netty/handler/codec/dns/DnsQuestion;Lio/netty/channel/AddressedEnvelope;Lio/netty/resolver/dns/DnsQueryLifecycleObserver;Lio/netty/util/concurrent/Promise;)V
/*      */         //   120: goto +39 -> 159
/*      */         //   123: aload_0
/*      */         //   124: getfield 3	io/netty/resolver/dns/DnsResolveContext$3:val$queryLifecycleObserver	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*      */         //   127: aload_2
/*      */         //   128: invokeinterface 19 2 0
/*      */         //   133: aload_0
/*      */         //   134: getfield 1	io/netty/resolver/dns/DnsResolveContext$3:this$0	Lio/netty/resolver/dns/DnsResolveContext;
/*      */         //   137: aload_0
/*      */         //   138: getfield 4	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStream	Lio/netty/resolver/dns/DnsServerAddressStream;
/*      */         //   141: aload_0
/*      */         //   142: getfield 5	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStreamIndex	I
/*      */         //   145: iconst_1
/*      */         //   146: iadd
/*      */         //   147: aload_0
/*      */         //   148: getfield 6	io/netty/resolver/dns/DnsResolveContext$3:val$question	Lio/netty/handler/codec/dns/DnsQuestion;
/*      */         //   151: aload_0
/*      */         //   152: getfield 2	io/netty/resolver/dns/DnsResolveContext$3:val$promise	Lio/netty/util/concurrent/Promise;
/*      */         //   155: aload_2
/*      */         //   156: invokestatic 20	io/netty/resolver/dns/DnsResolveContext:access$500	(Lio/netty/resolver/dns/DnsResolveContext;Lio/netty/resolver/dns/DnsServerAddressStream;ILio/netty/handler/codec/dns/DnsQuestion;Lio/netty/util/concurrent/Promise;Ljava/lang/Throwable;)V
/*      */         //   159: aload_0
/*      */         //   160: getfield 1	io/netty/resolver/dns/DnsResolveContext$3:this$0	Lio/netty/resolver/dns/DnsResolveContext;
/*      */         //   163: aload_0
/*      */         //   164: getfield 4	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStream	Lio/netty/resolver/dns/DnsServerAddressStream;
/*      */         //   167: aload_0
/*      */         //   168: getfield 5	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStreamIndex	I
/*      */         //   171: aload_0
/*      */         //   172: getfield 6	io/netty/resolver/dns/DnsResolveContext$3:val$question	Lio/netty/handler/codec/dns/DnsQuestion;
/*      */         //   175: getstatic 21	io/netty/resolver/dns/NoopDnsQueryLifecycleObserver:INSTANCE	Lio/netty/resolver/dns/NoopDnsQueryLifecycleObserver;
/*      */         //   178: aload_0
/*      */         //   179: getfield 2	io/netty/resolver/dns/DnsResolveContext$3:val$promise	Lio/netty/util/concurrent/Promise;
/*      */         //   182: aload_2
/*      */         //   183: invokestatic 22	io/netty/resolver/dns/DnsResolveContext:access$600	(Lio/netty/resolver/dns/DnsResolveContext;Lio/netty/resolver/dns/DnsServerAddressStream;ILio/netty/handler/codec/dns/DnsQuestion;Lio/netty/resolver/dns/DnsQueryLifecycleObserver;Lio/netty/util/concurrent/Promise;Ljava/lang/Throwable;)V
/*      */         //   186: goto +33 -> 219
/*      */         //   189: astore_3
/*      */         //   190: aload_0
/*      */         //   191: getfield 1	io/netty/resolver/dns/DnsResolveContext$3:this$0	Lio/netty/resolver/dns/DnsResolveContext;
/*      */         //   194: aload_0
/*      */         //   195: getfield 4	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStream	Lio/netty/resolver/dns/DnsServerAddressStream;
/*      */         //   198: aload_0
/*      */         //   199: getfield 5	io/netty/resolver/dns/DnsResolveContext$3:val$nameServerAddrStreamIndex	I
/*      */         //   202: aload_0
/*      */         //   203: getfield 6	io/netty/resolver/dns/DnsResolveContext$3:val$question	Lio/netty/handler/codec/dns/DnsQuestion;
/*      */         //   206: getstatic 21	io/netty/resolver/dns/NoopDnsQueryLifecycleObserver:INSTANCE	Lio/netty/resolver/dns/NoopDnsQueryLifecycleObserver;
/*      */         //   209: aload_0
/*      */         //   210: getfield 2	io/netty/resolver/dns/DnsResolveContext$3:val$promise	Lio/netty/util/concurrent/Promise;
/*      */         //   213: aload_2
/*      */         //   214: invokestatic 22	io/netty/resolver/dns/DnsResolveContext:access$600	(Lio/netty/resolver/dns/DnsResolveContext;Lio/netty/resolver/dns/DnsServerAddressStream;ILio/netty/handler/codec/dns/DnsQuestion;Lio/netty/resolver/dns/DnsQueryLifecycleObserver;Lio/netty/util/concurrent/Promise;Ljava/lang/Throwable;)V
/*      */         //   217: aload_3
/*      */         //   218: athrow
/*      */         //   219: return
/*      */         // Line number table:
/*      */         //   Java source line #357	-> byte code offset #0
/*      */         //   Java source line #359	-> byte code offset #14
/*      */         //   Java source line #360	-> byte code offset #35
/*      */         //   Java source line #364	-> byte code offset #51
/*      */         //   Java source line #365	-> byte code offset #61
/*      */         //   Java source line #366	-> byte code offset #65
/*      */         //   Java source line #368	-> byte code offset #72
/*      */         //   Java source line #371	-> byte code offset #73
/*      */         //   Java source line #373	-> byte code offset #80
/*      */         //   Java source line #374	-> byte code offset #84
/*      */         //   Java source line #378	-> byte code offset #123
/*      */         //   Java source line #379	-> byte code offset #133
/*      */         //   Java source line #382	-> byte code offset #159
/*      */         //   Java source line #387	-> byte code offset #186
/*      */         //   Java source line #382	-> byte code offset #189
/*      */         //   Java source line #387	-> byte code offset #217
/*      */         //   Java source line #388	-> byte code offset #219
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	220	0	this	3
/*      */         //   0	220	1	future	Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>
/*      */         //   60	6	2	result	AddressedEnvelope<DnsResponse, InetSocketAddress>
/*      */         //   79	135	2	queryCause	Throwable
/*      */         //   189	29	3	localObject	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   80	159	189	finally
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private void queryUnresolvedNameserver(final InetSocketAddress nameServerAddr, final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, final Throwable cause)
/*      */   {
/*  400 */     String nameServerName = PlatformDependent.javaVersion() >= 7 ? nameServerAddr.getHostString() : nameServerAddr.getHostName();
/*  401 */     assert (nameServerName != null);
/*      */     
/*      */ 
/*      */ 
/*  405 */     final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> resolveFuture = this.parent.executor().newSucceededFuture(null);
/*  406 */     this.queriesInProgress.add(resolveFuture);
/*      */     
/*  408 */     Promise<List<InetAddress>> resolverPromise = this.parent.executor().newPromise();
/*  409 */     resolverPromise.addListener(new FutureListener()
/*      */     {
/*      */       public void operationComplete(Future<List<InetAddress>> future)
/*      */       {
/*  413 */         DnsResolveContext.this.queriesInProgress.remove(resolveFuture);
/*      */         
/*  415 */         if (future.isSuccess()) {
/*  416 */           List<InetAddress> resolvedAddresses = (List)future.getNow();
/*  417 */           DnsServerAddressStream addressStream = new DnsResolveContext.CombinedDnsServerAddressStream(DnsResolveContext.this, nameServerAddr, resolvedAddresses, nameServerAddrStream);
/*      */           
/*  419 */           DnsResolveContext.this.query(addressStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
/*      */         }
/*      */         else
/*      */         {
/*  423 */           DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, promise, cause);
/*      */         }
/*      */       }
/*      */     });
/*      */     
/*  428 */     if (!DnsNameResolver.doResolveAllCached(nameServerName, this.additionals, resolverPromise, resolveCache(), this.parent
/*  429 */       .resolvedInternetProtocolFamiliesUnsafe())) {
/*  430 */       final AuthoritativeDnsServerCache authoritativeDnsServerCache = authoritativeDnsServerCache();
/*  431 */       new DnsAddressResolveContext(this.parent, nameServerName, this.additionals, this.parent
/*  432 */         .newNameServerAddressStream(nameServerName), 
/*  433 */         resolveCache(), new AuthoritativeDnsServerCache()
/*      */         {
/*      */ 
/*      */           public DnsServerAddressStream get(String hostname)
/*      */           {
/*  438 */             return null;
/*      */           }
/*      */           
/*      */           public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop)
/*      */           {
/*  443 */             authoritativeDnsServerCache.cache(hostname, address, originalTtl, loop);
/*      */           }
/*      */           
/*      */           public void clear()
/*      */           {
/*  448 */             authoritativeDnsServerCache.clear();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  453 */           public boolean clear(String hostname) { return authoritativeDnsServerCache.clear(hostname); } })
/*      */         
/*  455 */         .resolve(resolverPromise);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void onResponse(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise)
/*      */   {
/*      */     try
/*      */     {
/*  464 */       DnsResponse res = (DnsResponse)envelope.content();
/*  465 */       DnsResponseCode code = res.code();
/*  466 */       if (code == DnsResponseCode.NOERROR) {
/*  467 */         if (handleRedirect(question, envelope, queryLifecycleObserver, promise))
/*      */         {
/*  469 */           return;
/*      */         }
/*  471 */         DnsRecordType type = question.type();
/*      */         
/*  473 */         if (type == DnsRecordType.CNAME) {
/*  474 */           onResponseCNAME(question, buildAliasMap((DnsResponse)envelope.content(), cnameCache(), this.parent.executor()), queryLifecycleObserver, promise);
/*      */           
/*  476 */           return;
/*      */         }
/*      */         
/*  479 */         for (DnsRecordType expectedType : this.expectedTypes) {
/*  480 */           if (type == expectedType) {
/*  481 */             onExpectedResponse(question, envelope, queryLifecycleObserver, promise);
/*  482 */             return;
/*      */           }
/*      */         }
/*      */         
/*  486 */         queryLifecycleObserver.queryFailed(UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION);
/*  487 */         return;
/*      */       }
/*      */       
/*      */ 
/*  491 */       if (code != DnsResponseCode.NXDOMAIN) {
/*  492 */         query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver
/*  493 */           .queryNoAnswer(code), promise, null);
/*      */       } else {
/*  495 */         queryLifecycleObserver.queryFailed(NXDOMAIN_QUERY_FAILED_EXCEPTION);
/*      */       }
/*      */     } finally {
/*  498 */       ReferenceCountUtil.safeRelease(envelope);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean handleRedirect(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise)
/*      */   {
/*  508 */     DnsResponse res = (DnsResponse)envelope.content();
/*      */     
/*      */ 
/*  511 */     if (res.count(DnsSection.ANSWER) == 0) {
/*  512 */       AuthoritativeNameServerList serverNames = extractAuthoritativeNameServers(question.name(), res);
/*  513 */       if (serverNames != null) {
/*  514 */         int additionalCount = res.count(DnsSection.ADDITIONAL);
/*      */         
/*  516 */         AuthoritativeDnsServerCache authoritativeDnsServerCache = authoritativeDnsServerCache();
/*  517 */         for (int i = 0; i < additionalCount; i++) {
/*  518 */           DnsRecord r = res.recordAt(DnsSection.ADDITIONAL, i);
/*      */           
/*  520 */           if (((r.type() != DnsRecordType.A) || (this.parent.supportsARecords())) && (
/*  521 */             (r.type() != DnsRecordType.AAAA) || (this.parent.supportsAAAARecords())))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  527 */             serverNames.handleWithAdditional(this.parent, r, authoritativeDnsServerCache);
/*      */           }
/*      */         }
/*      */         
/*  531 */         serverNames.handleWithoutAdditionals(this.parent, resolveCache(), authoritativeDnsServerCache);
/*      */         
/*  533 */         List<InetSocketAddress> addresses = serverNames.addressList();
/*      */         
/*      */ 
/*  536 */         DnsServerAddressStream serverStream = this.parent.newRedirectDnsServerStream(question
/*  537 */           .name(), addresses);
/*      */         
/*  539 */         if (serverStream != null) {
/*  540 */           query(serverStream, 0, question, queryLifecycleObserver
/*  541 */             .queryRedirected(new DnsAddressStreamList(serverStream)), promise, null);
/*      */           
/*  543 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  547 */     return false;
/*      */   }
/*      */   
/*      */   private static final class DnsAddressStreamList extends AbstractList<InetSocketAddress>
/*      */   {
/*      */     private final DnsServerAddressStream duplicate;
/*      */     private List<InetSocketAddress> addresses;
/*      */     
/*      */     DnsAddressStreamList(DnsServerAddressStream stream) {
/*  556 */       this.duplicate = stream.duplicate();
/*      */     }
/*      */     
/*      */     public InetSocketAddress get(int index)
/*      */     {
/*  561 */       if (this.addresses == null) {
/*  562 */         DnsServerAddressStream stream = this.duplicate.duplicate();
/*  563 */         this.addresses = new ArrayList(size());
/*  564 */         for (int i = 0; i < stream.size(); i++) {
/*  565 */           this.addresses.add(stream.next());
/*      */         }
/*      */       }
/*  568 */       return (InetSocketAddress)this.addresses.get(index);
/*      */     }
/*      */     
/*      */     public int size()
/*      */     {
/*  573 */       return this.duplicate.size();
/*      */     }
/*      */     
/*      */     public Iterator<InetSocketAddress> iterator()
/*      */     {
/*  578 */       new Iterator() {
/*  579 */         private final DnsServerAddressStream stream = DnsResolveContext.DnsAddressStreamList.this.duplicate.duplicate();
/*      */         private int i;
/*      */         
/*      */         public boolean hasNext()
/*      */         {
/*  584 */           return this.i < this.stream.size();
/*      */         }
/*      */         
/*      */         public InetSocketAddress next()
/*      */         {
/*  589 */           if (!hasNext()) {
/*  590 */             throw new NoSuchElementException();
/*      */           }
/*  592 */           this.i += 1;
/*  593 */           return this.stream.next();
/*      */         }
/*      */         
/*      */         public void remove()
/*      */         {
/*  598 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static AuthoritativeNameServerList extractAuthoritativeNameServers(String questionName, DnsResponse res)
/*      */   {
/*  609 */     int authorityCount = res.count(DnsSection.AUTHORITY);
/*  610 */     if (authorityCount == 0) {
/*  611 */       return null;
/*      */     }
/*      */     
/*  614 */     AuthoritativeNameServerList serverNames = new AuthoritativeNameServerList(questionName);
/*  615 */     for (int i = 0; i < authorityCount; i++) {
/*  616 */       serverNames.add(res.recordAt(DnsSection.AUTHORITY, i));
/*      */     }
/*  618 */     return serverNames.isEmpty() ? null : serverNames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void onExpectedResponse(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise)
/*      */   {
/*  626 */     DnsResponse response = (DnsResponse)envelope.content();
/*  627 */     Map<String, String> cnames = buildAliasMap(response, cnameCache(), this.parent.executor());
/*  628 */     int answerCount = response.count(DnsSection.ANSWER);
/*      */     
/*  630 */     boolean found = false;
/*  631 */     for (int i = 0; i < answerCount; i++) {
/*  632 */       DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
/*  633 */       DnsRecordType type = r.type();
/*  634 */       boolean matches = false;
/*  635 */       for (DnsRecordType expectedType : this.expectedTypes) {
/*  636 */         if (type == expectedType) {
/*  637 */           matches = true;
/*  638 */           break;
/*      */         }
/*      */       }
/*      */       
/*  642 */       if (matches)
/*      */       {
/*      */ 
/*      */ 
/*  646 */         String questionName = question.name().toLowerCase(Locale.US);
/*  647 */         String recordName = r.name().toLowerCase(Locale.US);
/*      */         
/*      */ 
/*  650 */         if (!recordName.equals(questionName))
/*      */         {
/*  652 */           String resolved = questionName;
/*      */           do {
/*  654 */             resolved = (String)cnames.get(resolved);
/*  655 */           } while ((!recordName.equals(resolved)) && 
/*      */           
/*      */ 
/*  658 */             (resolved != null));
/*      */           
/*  660 */           if (resolved == null) {}
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  665 */           Object converted = convertRecord(r, this.hostname, this.additionals, this.parent.executor());
/*  666 */           if (converted != null)
/*      */           {
/*      */ 
/*      */ 
/*  670 */             if (this.finalResult == null) {
/*  671 */               this.finalResult = new ArrayList(8);
/*      */             }
/*  673 */             this.finalResult.add(converted);
/*      */             
/*  675 */             cache(this.hostname, this.additionals, r, converted);
/*  676 */             found = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  681 */     if (cnames.isEmpty()) {
/*  682 */       if (found) {
/*  683 */         queryLifecycleObserver.querySucceed();
/*  684 */         return;
/*      */       }
/*  686 */       queryLifecycleObserver.queryFailed(NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION);
/*      */     } else {
/*  688 */       queryLifecycleObserver.querySucceed();
/*      */       
/*  690 */       onResponseCNAME(question, cnames, this.parent
/*  691 */         .dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question), promise);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void onResponseCNAME(DnsQuestion question, Map<String, String> cnames, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise)
/*      */   {
/*  701 */     String resolved = question.name().toLowerCase(Locale.US);
/*  702 */     boolean found = false;
/*  703 */     while (!cnames.isEmpty())
/*      */     {
/*      */ 
/*  706 */       String next = (String)cnames.remove(resolved);
/*  707 */       if (next == null) break;
/*  708 */       found = true;
/*  709 */       resolved = next;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  715 */     if (found) {
/*  716 */       followCname(question, resolved, queryLifecycleObserver, promise);
/*      */     } else {
/*  718 */       queryLifecycleObserver.queryFailed(CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION);
/*      */     }
/*      */   }
/*      */   
/*      */   private static Map<String, String> buildAliasMap(DnsResponse response, DnsCnameCache cache, EventLoop loop) {
/*  723 */     int answerCount = response.count(DnsSection.ANSWER);
/*  724 */     Map<String, String> cnames = null;
/*  725 */     for (int i = 0; i < answerCount; i++) {
/*  726 */       DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
/*  727 */       DnsRecordType type = r.type();
/*  728 */       if (type == DnsRecordType.CNAME)
/*      */       {
/*      */ 
/*      */ 
/*  732 */         if ((r instanceof DnsRawRecord))
/*      */         {
/*      */ 
/*      */ 
/*  736 */           ByteBuf recordContent = ((ByteBufHolder)r).content();
/*  737 */           String domainName = decodeDomainName(recordContent);
/*  738 */           if (domainName != null)
/*      */           {
/*      */ 
/*      */ 
/*  742 */             if (cnames == null) {
/*  743 */               cnames = new HashMap(Math.min(8, answerCount));
/*      */             }
/*      */             
/*  746 */             String name = r.name().toLowerCase(Locale.US);
/*  747 */             String mapping = domainName.toLowerCase(Locale.US);
/*      */             
/*      */ 
/*  750 */             cache.cache(hostnameWithDot(name), hostnameWithDot(mapping), r.timeToLive(), loop);
/*  751 */             cnames.put(name, mapping);
/*      */           }
/*      */         } } }
/*  754 */     return cnames != null ? cnames : Collections.emptyMap();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void tryToFinishResolve(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise, Throwable cause)
/*      */   {
/*  765 */     if (!this.queriesInProgress.isEmpty()) {
/*  766 */       queryLifecycleObserver.queryCancelled(this.allowedQueries);
/*      */       
/*      */ 
/*      */ 
/*  770 */       return;
/*      */     }
/*      */     
/*      */ 
/*  774 */     if (this.finalResult == null) {
/*  775 */       if (nameServerAddrStreamIndex < nameServerAddrStream.size()) {
/*  776 */         if (queryLifecycleObserver == NoopDnsQueryLifecycleObserver.INSTANCE)
/*      */         {
/*      */ 
/*  779 */           query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, promise, cause);
/*      */         } else {
/*  781 */           query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, promise, cause);
/*      */         }
/*      */         
/*  784 */         return;
/*      */       }
/*      */       
/*  787 */       queryLifecycleObserver.queryFailed(NAME_SERVERS_EXHAUSTED_EXCEPTION);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  794 */       if ((cause == null) && (!this.triedCNAME))
/*      */       {
/*  796 */         this.triedCNAME = true;
/*      */         
/*  798 */         query(this.hostname, DnsRecordType.CNAME, getNameServers(this.hostname), promise);
/*      */       }
/*      */     }
/*      */     else {
/*  802 */       queryLifecycleObserver.queryCancelled(this.allowedQueries);
/*      */     }
/*      */     
/*      */ 
/*  806 */     finishResolve(promise, cause);
/*      */   }
/*      */   
/*      */   private void finishResolve(Promise<List<T>> promise, Throwable cause) {
/*  810 */     if (!this.queriesInProgress.isEmpty())
/*      */     {
/*  812 */       Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = this.queriesInProgress.iterator();
/*  813 */       while (i.hasNext()) {
/*  814 */         Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = (Future)i.next();
/*  815 */         i.remove();
/*      */         
/*  817 */         if (!f.cancel(false)) {
/*  818 */           f.addListener(RELEASE_RESPONSE);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  823 */     if (this.finalResult != null)
/*      */     {
/*  825 */       DnsNameResolver.trySuccess(promise, filterResults(this.finalResult));
/*  826 */       return;
/*      */     }
/*      */     
/*      */ 
/*  830 */     int tries = this.maxAllowedQueries - this.allowedQueries;
/*  831 */     StringBuilder buf = new StringBuilder(64);
/*      */     
/*  833 */     buf.append("failed to resolve '").append(this.hostname).append('\'');
/*  834 */     if (tries > 1) {
/*  835 */       if (tries < this.maxAllowedQueries)
/*      */       {
/*      */ 
/*  838 */         buf.append(" after ").append(tries).append(" queries ");
/*      */       }
/*      */       else
/*      */       {
/*  842 */         buf.append(". Exceeded max queries per resolve ").append(this.maxAllowedQueries).append(' ');
/*      */       }
/*      */     }
/*  845 */     UnknownHostException unknownHostException = new UnknownHostException(buf.toString());
/*  846 */     if (cause == null)
/*      */     {
/*      */ 
/*  849 */       cache(this.hostname, this.additionals, unknownHostException);
/*      */     } else {
/*  851 */       unknownHostException.initCause(cause);
/*      */     }
/*  853 */     promise.tryFailure(unknownHostException);
/*      */   }
/*      */   
/*      */   static String decodeDomainName(ByteBuf in) {
/*  857 */     in.markReaderIndex();
/*      */     try {
/*  859 */       return DefaultDnsRecordDecoder.decodeName(in);
/*      */     }
/*      */     catch (CorruptedFrameException e) {
/*  862 */       return null;
/*      */     } finally {
/*  864 */       in.resetReaderIndex();
/*      */     }
/*      */   }
/*      */   
/*      */   private DnsServerAddressStream getNameServers(String hostname) {
/*  869 */     DnsServerAddressStream stream = getNameServersFromCache(hostname);
/*  870 */     return stream == null ? this.nameServerAddrs.duplicate() : stream;
/*      */   }
/*      */   
/*      */   private void followCname(DnsQuestion question, String cname, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise)
/*      */   {
/*      */     for (;;)
/*      */     {
/*  877 */       String mapping = cnameCache().get(hostnameWithDot(cname));
/*  878 */       if (mapping == null) {
/*      */         break;
/*      */       }
/*  881 */       cname = mapping;
/*      */     }
/*      */     
/*  884 */     DnsServerAddressStream stream = getNameServers(cname);
/*      */     
/*      */     try
/*      */     {
/*  888 */       cnameQuestion = new DefaultDnsQuestion(cname, question.type(), this.dnsClass);
/*      */     } catch (Throwable cause) { DnsQuestion cnameQuestion;
/*  890 */       queryLifecycleObserver.queryFailed(cause);
/*  891 */       PlatformDependent.throwException(cause); return;
/*      */     }
/*      */     DnsQuestion cnameQuestion;
/*  894 */     query(stream, 0, cnameQuestion, queryLifecycleObserver.queryCNAMEd(cnameQuestion), promise, null);
/*      */   }
/*      */   
/*      */   private boolean query(String hostname, DnsRecordType type, DnsServerAddressStream dnsServerAddressStream, Promise<List<T>> promise)
/*      */   {
/*      */     try
/*      */     {
/*  901 */       question = new DefaultDnsQuestion(hostname, type, this.dnsClass);
/*      */     }
/*      */     catch (Throwable cause) {
/*      */       DnsQuestion question;
/*  905 */       promise.tryFailure(new IllegalArgumentException("Unable to create DNS Question for: [" + hostname + ", " + type + ']', cause));
/*      */       
/*  907 */       return false; }
/*      */     DnsQuestion question;
/*  909 */     query(dnsServerAddressStream, 0, question, promise, null);
/*  910 */     return true;
/*      */   }
/*      */   
/*      */   private final class CombinedDnsServerAddressStream implements DnsServerAddressStream
/*      */   {
/*      */     private final InetSocketAddress replaced;
/*      */     private final DnsServerAddressStream originalStream;
/*      */     private final List<InetAddress> resolvedAddresses;
/*      */     private Iterator<InetAddress> resolved;
/*      */     
/*      */     CombinedDnsServerAddressStream(List<InetAddress> replaced, DnsServerAddressStream resolvedAddresses) {
/*  921 */       this.replaced = replaced;
/*  922 */       this.resolvedAddresses = resolvedAddresses;
/*  923 */       this.originalStream = originalStream;
/*  924 */       this.resolved = resolvedAddresses.iterator();
/*      */     }
/*      */     
/*      */     public InetSocketAddress next()
/*      */     {
/*  929 */       if (this.resolved.hasNext()) {
/*  930 */         return nextResolved0();
/*      */       }
/*  932 */       InetSocketAddress address = this.originalStream.next();
/*  933 */       if (address.equals(this.replaced)) {
/*  934 */         this.resolved = this.resolvedAddresses.iterator();
/*  935 */         return nextResolved0();
/*      */       }
/*  937 */       return address;
/*      */     }
/*      */     
/*      */     private InetSocketAddress nextResolved0() {
/*  941 */       return DnsResolveContext.this.parent.newRedirectServerAddress((InetAddress)this.resolved.next());
/*      */     }
/*      */     
/*      */     public int size()
/*      */     {
/*  946 */       return this.originalStream.size() + this.resolvedAddresses.size() - 1;
/*      */     }
/*      */     
/*      */     public DnsServerAddressStream duplicate()
/*      */     {
/*  951 */       return new CombinedDnsServerAddressStream(DnsResolveContext.this, this.replaced, this.resolvedAddresses, this.originalStream.duplicate());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class AuthoritativeNameServerList
/*      */   {
/*      */     private final String questionName;
/*      */     
/*      */     private DnsResolveContext.AuthoritativeNameServer head;
/*      */     
/*      */     private int nameServerCount;
/*      */     
/*      */ 
/*      */     AuthoritativeNameServerList(String questionName)
/*      */     {
/*  968 */       this.questionName = questionName.toLowerCase(Locale.US);
/*      */     }
/*      */     
/*      */     void add(DnsRecord r) {
/*  972 */       if ((r.type() != DnsRecordType.NS) || (!(r instanceof DnsRawRecord))) {
/*  973 */         return;
/*      */       }
/*      */       
/*      */ 
/*  977 */       if (this.questionName.length() < r.name().length()) {
/*  978 */         return;
/*      */       }
/*      */       
/*  981 */       String recordName = r.name().toLowerCase(Locale.US);
/*      */       
/*  983 */       int dots = 0;
/*  984 */       int a = recordName.length() - 1; for (int b = this.questionName.length() - 1; a >= 0; b--) {
/*  985 */         char c = recordName.charAt(a);
/*  986 */         if (this.questionName.charAt(b) != c) {
/*  987 */           return;
/*      */         }
/*  989 */         if (c == '.') {
/*  990 */           dots++;
/*      */         }
/*  984 */         a--;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  994 */       if ((this.head != null) && (this.head.dots > dots))
/*      */       {
/*  996 */         return;
/*      */       }
/*      */       
/*  999 */       ByteBuf recordContent = ((ByteBufHolder)r).content();
/* 1000 */       String domainName = DnsResolveContext.decodeDomainName(recordContent);
/* 1001 */       if (domainName == null)
/*      */       {
/* 1003 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1008 */       if ((this.head == null) || (this.head.dots < dots)) {
/* 1009 */         this.nameServerCount = 1;
/* 1010 */         this.head = new DnsResolveContext.AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
/* 1011 */       } else if (this.head.dots == dots) {
/* 1012 */         DnsResolveContext.AuthoritativeNameServer serverName = this.head;
/* 1013 */         while (serverName.next != null) {
/* 1014 */           serverName = serverName.next;
/*      */         }
/* 1016 */         serverName.next = new DnsResolveContext.AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
/* 1017 */         this.nameServerCount += 1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     void handleWithAdditional(DnsNameResolver parent, DnsRecord r, AuthoritativeDnsServerCache authoritativeCache)
/*      */     {
/* 1024 */       DnsResolveContext.AuthoritativeNameServer serverName = this.head;
/*      */       
/* 1026 */       String nsName = r.name();
/* 1027 */       InetAddress resolved = DnsAddressDecoder.decodeAddress(r, nsName, parent.isDecodeIdn());
/* 1028 */       if (resolved == null)
/*      */       {
/* 1030 */         return;
/*      */       }
/*      */       
/* 1033 */       while (serverName != null) {
/* 1034 */         if (serverName.nsName.equalsIgnoreCase(nsName)) {
/* 1035 */           if (serverName.address != null)
/*      */           {
/*      */ 
/* 1038 */             while ((serverName.next != null) && (serverName.next.isCopy)) {
/* 1039 */               serverName = serverName.next;
/*      */             }
/* 1041 */             DnsResolveContext.AuthoritativeNameServer server = new DnsResolveContext.AuthoritativeNameServer(serverName);
/* 1042 */             server.next = serverName.next;
/* 1043 */             serverName.next = server;
/* 1044 */             serverName = server;
/*      */             
/* 1046 */             this.nameServerCount += 1;
/*      */           }
/*      */           
/*      */ 
/* 1050 */           serverName.update(parent.newRedirectServerAddress(resolved), r.timeToLive());
/*      */           
/*      */ 
/* 1053 */           cache(serverName, authoritativeCache, parent.executor());
/* 1054 */           return;
/*      */         }
/* 1056 */         serverName = serverName.next;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     void handleWithoutAdditionals(DnsNameResolver parent, DnsCache cache, AuthoritativeDnsServerCache authoritativeCache)
/*      */     {
/* 1063 */       DnsResolveContext.AuthoritativeNameServer serverName = this.head;
/*      */       
/* 1065 */       while (serverName != null) {
/* 1066 */         if (serverName.address == null)
/*      */         {
/* 1068 */           cacheUnresolved(serverName, authoritativeCache, parent.executor());
/*      */           
/*      */ 
/*      */ 
/* 1072 */           List<? extends DnsCacheEntry> entries = cache.get(serverName.nsName, null);
/* 1073 */           if ((entries != null) && (!entries.isEmpty())) {
/* 1074 */             InetAddress address = ((DnsCacheEntry)entries.get(0)).address();
/*      */             
/*      */ 
/* 1077 */             if (address != null) {
/* 1078 */               serverName.update(parent.newRedirectServerAddress(address));
/*      */               
/* 1080 */               for (int i = 1; i < entries.size(); i++) {
/* 1081 */                 address = ((DnsCacheEntry)entries.get(i)).address();
/*      */                 
/* 1083 */                 assert (address != null) : "Cache returned a cached failure, should never return anything else";
/*      */                 
/*      */ 
/* 1086 */                 DnsResolveContext.AuthoritativeNameServer server = new DnsResolveContext.AuthoritativeNameServer(serverName);
/* 1087 */                 server.next = serverName.next;
/* 1088 */                 serverName.next = server;
/* 1089 */                 serverName = server;
/* 1090 */                 serverName.update(parent.newRedirectServerAddress(address));
/*      */                 
/* 1092 */                 this.nameServerCount += 1;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1097 */         serverName = serverName.next;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private static void cacheUnresolved(DnsResolveContext.AuthoritativeNameServer server, AuthoritativeDnsServerCache authoritativeCache, EventLoop loop)
/*      */     {
/* 1104 */       server.address = InetSocketAddress.createUnresolved(server.nsName, 53);
/*      */       
/*      */ 
/*      */ 
/* 1108 */       cache(server, authoritativeCache, loop);
/*      */     }
/*      */     
/*      */     private static void cache(DnsResolveContext.AuthoritativeNameServer server, AuthoritativeDnsServerCache cache, EventLoop loop)
/*      */     {
/* 1113 */       if (!server.isRootServer()) {
/* 1114 */         cache.cache(server.domainName, server.address, server.ttl, loop);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isEmpty()
/*      */     {
/* 1122 */       return this.nameServerCount == 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     List<InetSocketAddress> addressList()
/*      */     {
/* 1129 */       List<InetSocketAddress> addressList = new ArrayList(this.nameServerCount);
/*      */       
/* 1131 */       DnsResolveContext.AuthoritativeNameServer server = this.head;
/* 1132 */       while (server != null) {
/* 1133 */         if (server.address != null) {
/* 1134 */           addressList.add(server.address);
/*      */         }
/* 1136 */         server = server.next;
/*      */       }
/* 1138 */       return addressList;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AuthoritativeNameServer
/*      */   {
/*      */     private final int dots;
/*      */     private final String domainName;
/*      */     final boolean isCopy;
/*      */     final String nsName;
/*      */     private long ttl;
/*      */     private InetSocketAddress address;
/*      */     AuthoritativeNameServer next;
/*      */     
/*      */     AuthoritativeNameServer(int dots, long ttl, String domainName, String nsName)
/*      */     {
/* 1154 */       this.dots = dots;
/* 1155 */       this.ttl = ttl;
/* 1156 */       this.nsName = nsName;
/* 1157 */       this.domainName = domainName;
/* 1158 */       this.isCopy = false;
/*      */     }
/*      */     
/*      */     AuthoritativeNameServer(AuthoritativeNameServer server) {
/* 1162 */       this.dots = server.dots;
/* 1163 */       this.ttl = server.ttl;
/* 1164 */       this.nsName = server.nsName;
/* 1165 */       this.domainName = server.domainName;
/* 1166 */       this.isCopy = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean isRootServer()
/*      */     {
/* 1173 */       return this.dots == 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void update(InetSocketAddress address, long ttl)
/*      */     {
/* 1180 */       assert ((this.address == null) || (this.address.isUnresolved()));
/* 1181 */       this.address = address;
/* 1182 */       this.ttl = Math.min(ttl, ttl);
/*      */     }
/*      */     
/*      */     void update(InetSocketAddress address) {
/* 1186 */       update(address, Long.MAX_VALUE);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsResolveContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */