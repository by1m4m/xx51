/*      */ package org.eclipse.jetty.client;
/*      */ 
/*      */ import java.net.CookieManager;
/*      */ import java.net.CookiePolicy;
/*      */ import java.net.CookieStore;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.URI;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import org.eclipse.jetty.client.api.AuthenticationStore;
/*      */ import org.eclipse.jetty.client.api.Connection;
/*      */ import org.eclipse.jetty.client.api.ContentResponse;
/*      */ import org.eclipse.jetty.client.api.Destination;
/*      */ import org.eclipse.jetty.client.api.Request;
/*      */ import org.eclipse.jetty.client.api.Request.Listener;
/*      */ import org.eclipse.jetty.client.api.Response;
/*      */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*      */ import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
/*      */ import org.eclipse.jetty.client.util.FormContentProvider;
/*      */ import org.eclipse.jetty.http.HttpCompliance;
/*      */ import org.eclipse.jetty.http.HttpField;
/*      */ import org.eclipse.jetty.http.HttpFields;
/*      */ import org.eclipse.jetty.http.HttpHeader;
/*      */ import org.eclipse.jetty.http.HttpMethod;
/*      */ import org.eclipse.jetty.http.HttpScheme;
/*      */ import org.eclipse.jetty.io.ByteBufferPool;
/*      */ import org.eclipse.jetty.io.ClientConnectionFactory;
/*      */ import org.eclipse.jetty.io.MappedByteBufferPool;
/*      */ import org.eclipse.jetty.io.ssl.SslClientConnectionFactory;
/*      */ import org.eclipse.jetty.util.Fields;
/*      */ import org.eclipse.jetty.util.Jetty;
/*      */ import org.eclipse.jetty.util.ProcessorUtils;
/*      */ import org.eclipse.jetty.util.Promise;
/*      */ import org.eclipse.jetty.util.Promise.Wrapper;
/*      */ import org.eclipse.jetty.util.SocketAddressResolver;
/*      */ import org.eclipse.jetty.util.SocketAddressResolver.Async;
/*      */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*      */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*      */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*      */ import org.eclipse.jetty.util.log.Log;
/*      */ import org.eclipse.jetty.util.log.Logger;
/*      */ import org.eclipse.jetty.util.ssl.SslContextFactory;
/*      */ import org.eclipse.jetty.util.thread.QueuedThreadPool;
/*      */ import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
/*      */ import org.eclipse.jetty.util.thread.Scheduler;
/*      */ import org.eclipse.jetty.util.thread.ThreadPool.SizedThreadPool;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ManagedObject("The HTTP client")
/*      */ public class HttpClient
/*      */   extends ContainerLifeCycle
/*      */ {
/*  120 */   private static final Logger LOG = Log.getLogger(HttpClient.class);
/*      */   
/*  122 */   private final ConcurrentMap<Origin, HttpDestination> destinations = new ConcurrentHashMap();
/*  123 */   private final ProtocolHandlers handlers = new ProtocolHandlers();
/*  124 */   private final List<Request.Listener> requestListeners = new ArrayList();
/*  125 */   private final Set<ContentDecoder.Factory> decoderFactories = new ContentDecoderFactorySet(null);
/*  126 */   private final ProxyConfiguration proxyConfig = new ProxyConfiguration();
/*      */   private final HttpClientTransport transport;
/*      */   private final SslContextFactory sslContextFactory;
/*  129 */   private AuthenticationStore authenticationStore = new HttpAuthenticationStore();
/*      */   private CookieManager cookieManager;
/*      */   private CookieStore cookieStore;
/*      */   private Executor executor;
/*      */   private ByteBufferPool byteBufferPool;
/*      */   private Scheduler scheduler;
/*      */   private SocketAddressResolver resolver;
/*  136 */   private HttpField agentField = new HttpField(HttpHeader.USER_AGENT, "Jetty/" + Jetty.VERSION);
/*  137 */   private boolean followRedirects = true;
/*  138 */   private int maxConnectionsPerDestination = 64;
/*  139 */   private int maxRequestsQueuedPerDestination = 1024;
/*  140 */   private int requestBufferSize = 4096;
/*  141 */   private int responseBufferSize = 16384;
/*  142 */   private int maxRedirects = 8;
/*      */   private SocketAddress bindAddress;
/*  144 */   private long connectTimeout = 15000L;
/*  145 */   private long addressResolutionTimeout = 15000L;
/*      */   private long idleTimeout;
/*  147 */   private boolean tcpNoDelay = true;
/*  148 */   private boolean strictEventOrdering = false;
/*      */   private HttpField encodingField;
/*  150 */   private boolean removeIdleDestinations = false;
/*  151 */   private boolean connectBlocking = false;
/*  152 */   private String name = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
/*  153 */   private HttpCompliance httpCompliance = HttpCompliance.RFC7230;
/*  154 */   private String defaultRequestContentType = "application/octet-stream";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpClient()
/*      */   {
/*  164 */     this(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpClient(SslContextFactory sslContextFactory)
/*      */   {
/*  176 */     this(new HttpClientTransportOverHTTP(), sslContextFactory);
/*      */   }
/*      */   
/*      */   public HttpClient(HttpClientTransport transport, SslContextFactory sslContextFactory)
/*      */   {
/*  181 */     this.transport = transport;
/*  182 */     this.sslContextFactory = sslContextFactory;
/*      */   }
/*      */   
/*      */   public HttpClientTransport getTransport()
/*      */   {
/*  187 */     return this.transport;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslContextFactory getSslContextFactory()
/*      */   {
/*  196 */     return this.sslContextFactory;
/*      */   }
/*      */   
/*      */   protected void doStart()
/*      */     throws Exception
/*      */   {
/*  202 */     if (this.sslContextFactory != null) {
/*  203 */       addBean(this.sslContextFactory);
/*      */     }
/*  205 */     if (this.executor == null)
/*      */     {
/*  207 */       QueuedThreadPool threadPool = new QueuedThreadPool();
/*  208 */       threadPool.setName(this.name);
/*  209 */       this.executor = threadPool;
/*      */     }
/*  211 */     addBean(this.executor);
/*      */     
/*  213 */     if (this.byteBufferPool == null)
/*      */     {
/*      */ 
/*      */ 
/*  217 */       this.byteBufferPool = new MappedByteBufferPool(2048, (this.executor instanceof ThreadPool.SizedThreadPool) ? ((ThreadPool.SizedThreadPool)this.executor).getMaxThreads() / 2 : ProcessorUtils.availableProcessors() * 2); }
/*  218 */     addBean(this.byteBufferPool);
/*      */     
/*  220 */     if (this.scheduler == null)
/*  221 */       this.scheduler = new ScheduledExecutorScheduler(this.name + "-scheduler", false);
/*  222 */     addBean(this.scheduler);
/*      */     
/*  224 */     this.transport.setHttpClient(this);
/*  225 */     addBean(this.transport);
/*      */     
/*  227 */     if (this.resolver == null)
/*  228 */       this.resolver = new SocketAddressResolver.Async(this.executor, this.scheduler, getAddressResolutionTimeout());
/*  229 */     addBean(this.resolver);
/*      */     
/*  231 */     this.handlers.put(new ContinueProtocolHandler());
/*  232 */     this.handlers.put(new RedirectProtocolHandler(this));
/*  233 */     this.handlers.put(new WWWAuthenticationProtocolHandler(this));
/*  234 */     this.handlers.put(new ProxyAuthenticationProtocolHandler(this));
/*      */     
/*  236 */     this.decoderFactories.add(new GZIPContentDecoder.Factory(this.byteBufferPool));
/*      */     
/*  238 */     this.cookieManager = newCookieManager();
/*  239 */     this.cookieStore = this.cookieManager.getCookieStore();
/*      */     
/*  241 */     super.doStart();
/*      */   }
/*      */   
/*      */   private CookieManager newCookieManager()
/*      */   {
/*  246 */     return new CookieManager(getCookieStore(), CookiePolicy.ACCEPT_ALL);
/*      */   }
/*      */   
/*      */   protected void doStop()
/*      */     throws Exception
/*      */   {
/*  252 */     this.decoderFactories.clear();
/*  253 */     this.handlers.clear();
/*      */     
/*  255 */     for (HttpDestination destination : this.destinations.values())
/*  256 */       destination.close();
/*  257 */     this.destinations.clear();
/*      */     
/*  259 */     this.requestListeners.clear();
/*  260 */     this.authenticationStore.clearAuthentications();
/*  261 */     this.authenticationStore.clearAuthenticationResults();
/*      */     
/*  263 */     super.doStop();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Request.Listener> getRequestListeners()
/*      */   {
/*  274 */     return this.requestListeners;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public CookieStore getCookieStore()
/*      */   {
/*  282 */     return this.cookieStore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCookieStore(CookieStore cookieStore)
/*      */   {
/*  290 */     this.cookieStore = ((CookieStore)Objects.requireNonNull(cookieStore));
/*  291 */     this.cookieManager = newCookieManager();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   CookieManager getCookieManager()
/*      */   {
/*  302 */     return this.cookieManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AuthenticationStore getAuthenticationStore()
/*      */   {
/*  310 */     return this.authenticationStore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAuthenticationStore(AuthenticationStore authenticationStore)
/*      */   {
/*  318 */     this.authenticationStore = authenticationStore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<ContentDecoder.Factory> getContentDecoderFactories()
/*      */   {
/*  329 */     return this.decoderFactories;
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
/*      */   public ContentResponse GET(String uri)
/*      */     throws InterruptedException, ExecutionException, TimeoutException
/*      */   {
/*  344 */     return GET(URI.create(uri));
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
/*      */   public ContentResponse GET(URI uri)
/*      */     throws InterruptedException, ExecutionException, TimeoutException
/*      */   {
/*  359 */     return newRequest(uri).send();
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
/*      */   public ContentResponse FORM(String uri, Fields fields)
/*      */     throws InterruptedException, ExecutionException, TimeoutException
/*      */   {
/*  374 */     return FORM(URI.create(uri), fields);
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
/*      */   public ContentResponse FORM(URI uri, Fields fields)
/*      */     throws InterruptedException, ExecutionException, TimeoutException
/*      */   {
/*  389 */     return POST(uri).content(new FormContentProvider(fields)).send();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Request POST(String uri)
/*      */   {
/*  401 */     return POST(URI.create(uri));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Request POST(URI uri)
/*      */   {
/*  412 */     return newRequest(uri).method(HttpMethod.POST);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Request newRequest(String host, int port)
/*      */   {
/*  424 */     return newRequest(new Origin("http", host, port).asString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Request newRequest(String uri)
/*      */   {
/*  435 */     return newRequest(URI.create(uri));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Request newRequest(URI uri)
/*      */   {
/*  446 */     return newHttpRequest(newConversation(), uri);
/*      */   }
/*      */   
/*      */   protected Request copyRequest(HttpRequest oldRequest, URI newURI)
/*      */   {
/*  451 */     Request newRequest = newHttpRequest(oldRequest.getConversation(), newURI);
/*  452 */     newRequest.method(oldRequest.getMethod())
/*  453 */       .version(oldRequest.getVersion())
/*  454 */       .content(oldRequest.getContent())
/*  455 */       .idleTimeout(oldRequest.getIdleTimeout(), TimeUnit.MILLISECONDS)
/*  456 */       .timeout(oldRequest.getTimeout(), TimeUnit.MILLISECONDS)
/*  457 */       .followRedirects(oldRequest.isFollowRedirects());
/*  458 */     for (HttpField field : oldRequest.getHeaders())
/*      */     {
/*  460 */       HttpHeader header = field.getHeader();
/*      */       
/*  462 */       if ((HttpHeader.HOST != header) && 
/*      */       
/*      */ 
/*      */ 
/*  466 */         (HttpHeader.EXPECT != header) && 
/*      */         
/*      */ 
/*      */ 
/*  470 */         (HttpHeader.COOKIE != header) && 
/*      */         
/*      */ 
/*      */ 
/*  474 */         (HttpHeader.AUTHORIZATION != header) && (HttpHeader.PROXY_AUTHORIZATION != header))
/*      */       {
/*      */ 
/*      */ 
/*  478 */         String name = field.getName();
/*  479 */         String value = field.getValue();
/*  480 */         if (!newRequest.getHeaders().contains(name, value))
/*  481 */           newRequest.header(name, value);
/*      */       } }
/*  483 */     return newRequest;
/*      */   }
/*      */   
/*      */   protected HttpRequest newHttpRequest(HttpConversation conversation, URI uri)
/*      */   {
/*  488 */     return new HttpRequest(this, conversation, checkHost(uri));
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
/*      */   private URI checkHost(URI uri)
/*      */   {
/*  502 */     if (uri.getHost() == null)
/*  503 */       throw new IllegalArgumentException(String.format("Invalid URI host: null (authority: %s)", new Object[] { uri.getRawAuthority() }));
/*  504 */     return uri;
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
/*      */   public Destination getDestination(String scheme, String host, int port)
/*      */   {
/*  522 */     return destinationFor(scheme, host, port);
/*      */   }
/*      */   
/*      */   protected HttpDestination destinationFor(String scheme, String host, int port)
/*      */   {
/*  527 */     if ((!HttpScheme.HTTP.is(scheme)) && (!HttpScheme.HTTPS.is(scheme)) && 
/*  528 */       (!HttpScheme.WS.is(scheme)) && (!HttpScheme.WSS.is(scheme))) {
/*  529 */       throw new IllegalArgumentException("Invalid protocol " + scheme);
/*      */     }
/*  531 */     scheme = scheme.toLowerCase(Locale.ENGLISH);
/*  532 */     host = host.toLowerCase(Locale.ENGLISH);
/*  533 */     port = normalizePort(scheme, port);
/*      */     
/*  535 */     Origin origin = new Origin(scheme, host, port);
/*  536 */     HttpDestination destination = (HttpDestination)this.destinations.get(origin);
/*  537 */     if (destination == null)
/*      */     {
/*  539 */       destination = this.transport.newHttpDestination(origin);
/*  540 */       addManaged(destination);
/*  541 */       HttpDestination existing = (HttpDestination)this.destinations.putIfAbsent(origin, destination);
/*  542 */       if (existing != null)
/*      */       {
/*  544 */         removeBean(destination);
/*  545 */         destination = existing;
/*      */ 
/*      */ 
/*      */       }
/*  549 */       else if (LOG.isDebugEnabled()) {
/*  550 */         LOG.debug("Created {}", new Object[] { destination });
/*      */       }
/*      */     }
/*  553 */     return destination;
/*      */   }
/*      */   
/*      */   protected boolean removeDestination(HttpDestination destination)
/*      */   {
/*  558 */     removeBean(destination);
/*  559 */     return this.destinations.remove(destination.getOrigin(), destination);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Destination> getDestinations()
/*      */   {
/*  567 */     return new ArrayList(this.destinations.values());
/*      */   }
/*      */   
/*      */   protected void send(HttpRequest request, List<Response.ResponseListener> listeners)
/*      */   {
/*  572 */     HttpDestination destination = destinationFor(request.getScheme(), request.getHost(), request.getPort());
/*  573 */     destination.send(request, listeners);
/*      */   }
/*      */   
/*      */   protected void newConnection(final HttpDestination destination, final Promise<Connection> promise)
/*      */   {
/*  578 */     Origin.Address address = destination.getConnectAddress();
/*  579 */     this.resolver.resolve(address.getHost(), address.getPort(), new Promise()
/*      */     {
/*      */ 
/*      */       public void succeeded(List<InetSocketAddress> socketAddresses)
/*      */       {
/*  584 */         Map<String, Object> context = new HashMap();
/*  585 */         context.put("client.connector", HttpClient.this);
/*  586 */         context.put("http.destination", destination);
/*  587 */         connect(socketAddresses, 0, context);
/*      */       }
/*      */       
/*      */ 
/*      */       public void failed(Throwable x)
/*      */       {
/*  593 */         promise.failed(x);
/*      */       }
/*      */       
/*      */       private void connect(final List<InetSocketAddress> socketAddresses, final int index, final Map<String, Object> context)
/*      */       {
/*  598 */         context.put("http.connection.promise", new Promise.Wrapper(promise)
/*      */         {
/*      */ 
/*      */           public void failed(Throwable x)
/*      */           {
/*  603 */             int nextIndex = index + 1;
/*  604 */             if (nextIndex == socketAddresses.size()) {
/*  605 */               super.failed(x);
/*      */             } else
/*  607 */               HttpClient.1.this.connect(socketAddresses, nextIndex, context);
/*      */           }
/*  609 */         });
/*  610 */         HttpClient.this.transport.connect((InetSocketAddress)socketAddresses.get(index), context);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private HttpConversation newConversation()
/*      */   {
/*  617 */     return new HttpConversation();
/*      */   }
/*      */   
/*      */   public ProtocolHandlers getProtocolHandlers()
/*      */   {
/*  622 */     return this.handlers;
/*      */   }
/*      */   
/*      */   protected ProtocolHandler findProtocolHandler(Request request, Response response)
/*      */   {
/*  627 */     return this.handlers.find(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBufferPool getByteBufferPool()
/*      */   {
/*  635 */     return this.byteBufferPool;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setByteBufferPool(ByteBufferPool byteBufferPool)
/*      */   {
/*  643 */     this.byteBufferPool = byteBufferPool;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The name of this HttpClient")
/*      */   public String getName()
/*      */   {
/*  652 */     return this.name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setName(String name)
/*      */   {
/*  664 */     this.name = name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The timeout, in milliseconds, for connect() operations")
/*      */   public long getConnectTimeout()
/*      */   {
/*  673 */     return this.connectTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectTimeout(long connectTimeout)
/*      */   {
/*  682 */     this.connectTimeout = connectTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getAddressResolutionTimeout()
/*      */   {
/*  691 */     return this.addressResolutionTimeout;
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
/*      */   public void setAddressResolutionTimeout(long addressResolutionTimeout)
/*      */   {
/*  705 */     this.addressResolutionTimeout = addressResolutionTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The timeout, in milliseconds, to close idle connections")
/*      */   public long getIdleTimeout()
/*      */   {
/*  714 */     return this.idleTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIdleTimeout(long idleTimeout)
/*      */   {
/*  722 */     this.idleTimeout = idleTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SocketAddress getBindAddress()
/*      */   {
/*  731 */     return this.bindAddress;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBindAddress(SocketAddress bindAddress)
/*      */   {
/*  741 */     this.bindAddress = bindAddress;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpField getUserAgentField()
/*      */   {
/*  749 */     return this.agentField;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserAgentField(HttpField agent)
/*      */   {
/*  757 */     if ((agent != null) && (agent.getHeader() != HttpHeader.USER_AGENT))
/*  758 */       throw new IllegalArgumentException();
/*  759 */     this.agentField = agent;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether HTTP redirects are followed")
/*      */   public boolean isFollowRedirects()
/*      */   {
/*  769 */     return this.followRedirects;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFollowRedirects(boolean follow)
/*      */   {
/*  778 */     this.followRedirects = follow;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Executor getExecutor()
/*      */   {
/*  786 */     return this.executor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExecutor(Executor executor)
/*      */   {
/*  794 */     this.executor = executor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Scheduler getScheduler()
/*      */   {
/*  802 */     return this.scheduler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScheduler(Scheduler scheduler)
/*      */   {
/*  810 */     this.scheduler = scheduler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public SocketAddressResolver getSocketAddressResolver()
/*      */   {
/*  818 */     return this.resolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSocketAddressResolver(SocketAddressResolver resolver)
/*      */   {
/*  826 */     this.resolver = resolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The max number of connections per each destination")
/*      */   public int getMaxConnectionsPerDestination()
/*      */   {
/*  835 */     return this.maxConnectionsPerDestination;
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
/*      */   public void setMaxConnectionsPerDestination(int maxConnectionsPerDestination)
/*      */   {
/*  851 */     this.maxConnectionsPerDestination = maxConnectionsPerDestination;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The max number of requests queued per each destination")
/*      */   public int getMaxRequestsQueuedPerDestination()
/*      */   {
/*  860 */     return this.maxRequestsQueuedPerDestination;
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
/*      */   public void setMaxRequestsQueuedPerDestination(int maxRequestsQueuedPerDestination)
/*      */   {
/*  878 */     this.maxRequestsQueuedPerDestination = maxRequestsQueuedPerDestination;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The request buffer size")
/*      */   public int getRequestBufferSize()
/*      */   {
/*  887 */     return this.requestBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequestBufferSize(int requestBufferSize)
/*      */   {
/*  895 */     this.requestBufferSize = requestBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The response buffer size")
/*      */   public int getResponseBufferSize()
/*      */   {
/*  904 */     return this.responseBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setResponseBufferSize(int responseBufferSize)
/*      */   {
/*  912 */     this.responseBufferSize = responseBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxRedirects()
/*      */   {
/*  921 */     return this.maxRedirects;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxRedirects(int maxRedirects)
/*      */   {
/*  930 */     this.maxRedirects = maxRedirects;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute(value="Whether the TCP_NODELAY option is enabled", name="tcpNoDelay")
/*      */   public boolean isTCPNoDelay()
/*      */   {
/*  939 */     return this.tcpNoDelay;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTCPNoDelay(boolean tcpNoDelay)
/*      */   {
/*  948 */     this.tcpNoDelay = tcpNoDelay;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public boolean isDispatchIO()
/*      */   {
/*  959 */     return false;
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
/*      */   @Deprecated
/*      */   public void setDispatchIO(boolean dispatchIO) {}
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
/*      */   public HttpCompliance getHttpCompliance()
/*      */   {
/*  988 */     return this.httpCompliance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHttpCompliance(HttpCompliance httpCompliance)
/*      */   {
/*  998 */     this.httpCompliance = httpCompliance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether request/response events must be strictly ordered")
/*      */   public boolean isStrictEventOrdering()
/*      */   {
/* 1008 */     return this.strictEventOrdering;
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
/*      */   public void setStrictEventOrdering(boolean strictEventOrdering)
/*      */   {
/* 1039 */     this.strictEventOrdering = strictEventOrdering;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether idle destinations are removed")
/*      */   public boolean isRemoveIdleDestinations()
/*      */   {
/* 1049 */     return this.removeIdleDestinations;
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
/*      */   public void setRemoveIdleDestinations(boolean removeIdleDestinations)
/*      */   {
/* 1066 */     this.removeIdleDestinations = removeIdleDestinations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether the connect() operation is blocking")
/*      */   public boolean isConnectBlocking()
/*      */   {
/* 1075 */     return this.connectBlocking;
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
/*      */   public void setConnectBlocking(boolean connectBlocking)
/*      */   {
/* 1090 */     this.connectBlocking = connectBlocking;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The default content type for request content")
/*      */   public String getDefaultRequestContentType()
/*      */   {
/* 1099 */     return this.defaultRequestContentType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultRequestContentType(String contentType)
/*      */   {
/* 1107 */     this.defaultRequestContentType = contentType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ProxyConfiguration getProxyConfiguration()
/*      */   {
/* 1115 */     return this.proxyConfig;
/*      */   }
/*      */   
/*      */   protected HttpField getAcceptEncodingField()
/*      */   {
/* 1120 */     return this.encodingField;
/*      */   }
/*      */   
/*      */   protected String normalizeHost(String host)
/*      */   {
/* 1125 */     if ((host != null) && (host.matches("\\[.*\\]")))
/* 1126 */       return host.substring(1, host.length() - 1);
/* 1127 */     return host;
/*      */   }
/*      */   
/*      */   public static int normalizePort(String scheme, int port)
/*      */   {
/* 1132 */     if (port > 0)
/* 1133 */       return port;
/* 1134 */     if (isSchemeSecure(scheme)) {
/* 1135 */       return 443;
/*      */     }
/* 1137 */     return 80;
/*      */   }
/*      */   
/*      */   public boolean isDefaultPort(String scheme, int port)
/*      */   {
/* 1142 */     if (isSchemeSecure(scheme)) {
/* 1143 */       return port == 443;
/*      */     }
/* 1145 */     return port == 80;
/*      */   }
/*      */   
/*      */   static boolean isSchemeSecure(String scheme)
/*      */   {
/* 1150 */     return (HttpScheme.HTTPS.is(scheme)) || (HttpScheme.WSS.is(scheme));
/*      */   }
/*      */   
/*      */   protected ClientConnectionFactory newSslClientConnectionFactory(ClientConnectionFactory connectionFactory)
/*      */   {
/* 1155 */     return new SslClientConnectionFactory(getSslContextFactory(), getByteBufferPool(), getExecutor(), connectionFactory);
/*      */   }
/*      */   
/*      */   private class ContentDecoderFactorySet implements Set<ContentDecoder.Factory>
/*      */   {
/* 1160 */     private final Set<ContentDecoder.Factory> set = new HashSet();
/*      */     
/*      */     private ContentDecoderFactorySet() {}
/*      */     
/*      */     public boolean add(ContentDecoder.Factory e) {
/* 1165 */       boolean result = this.set.add(e);
/* 1166 */       invalidate();
/* 1167 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean addAll(Collection<? extends ContentDecoder.Factory> c)
/*      */     {
/* 1173 */       boolean result = this.set.addAll(c);
/* 1174 */       invalidate();
/* 1175 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean remove(Object o)
/*      */     {
/* 1181 */       boolean result = this.set.remove(o);
/* 1182 */       invalidate();
/* 1183 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean removeAll(Collection<?> c)
/*      */     {
/* 1189 */       boolean result = this.set.removeAll(c);
/* 1190 */       invalidate();
/* 1191 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean retainAll(Collection<?> c)
/*      */     {
/* 1197 */       boolean result = this.set.retainAll(c);
/* 1198 */       invalidate();
/* 1199 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public void clear()
/*      */     {
/* 1205 */       this.set.clear();
/* 1206 */       invalidate();
/*      */     }
/*      */     
/*      */ 
/*      */     public int size()
/*      */     {
/* 1212 */       return this.set.size();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isEmpty()
/*      */     {
/* 1218 */       return this.set.isEmpty();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean contains(Object o)
/*      */     {
/* 1224 */       return this.set.contains(o);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean containsAll(Collection<?> c)
/*      */     {
/* 1230 */       return this.set.containsAll(c);
/*      */     }
/*      */     
/*      */ 
/*      */     public Iterator<ContentDecoder.Factory> iterator()
/*      */     {
/* 1236 */       final Iterator<ContentDecoder.Factory> iterator = this.set.iterator();
/* 1237 */       new Iterator()
/*      */       {
/*      */ 
/*      */         public boolean hasNext()
/*      */         {
/* 1242 */           return iterator.hasNext();
/*      */         }
/*      */         
/*      */ 
/*      */         public ContentDecoder.Factory next()
/*      */         {
/* 1248 */           return (ContentDecoder.Factory)iterator.next();
/*      */         }
/*      */         
/*      */ 
/*      */         public void remove()
/*      */         {
/* 1254 */           iterator.remove();
/* 1255 */           HttpClient.ContentDecoderFactorySet.this.invalidate();
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*      */     public Object[] toArray()
/*      */     {
/* 1263 */       return this.set.toArray();
/*      */     }
/*      */     
/*      */ 
/*      */     public <T> T[] toArray(T[] a)
/*      */     {
/* 1269 */       return this.set.toArray(a);
/*      */     }
/*      */     
/*      */     private void invalidate()
/*      */     {
/* 1274 */       if (this.set.isEmpty())
/*      */       {
/* 1276 */         HttpClient.this.encodingField = null;
/*      */       }
/*      */       else
/*      */       {
/* 1280 */         StringBuilder value = new StringBuilder();
/* 1281 */         for (Iterator<ContentDecoder.Factory> iterator = this.set.iterator(); iterator.hasNext();)
/*      */         {
/* 1283 */           ContentDecoder.Factory decoderFactory = (ContentDecoder.Factory)iterator.next();
/* 1284 */           value.append(decoderFactory.getEncoding());
/* 1285 */           if (iterator.hasNext())
/* 1286 */             value.append(",");
/*      */         }
/* 1288 */         HttpClient.this.encodingField = new HttpField(HttpHeader.ACCEPT_ENCODING, value.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */