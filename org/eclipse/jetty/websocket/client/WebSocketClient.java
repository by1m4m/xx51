/*     */ package org.eclipse.jetty.websocket.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.CookieStore;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import org.eclipse.jetty.client.HttpClient;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.MappedByteBufferPool;
/*     */ import org.eclipse.jetty.util.DecoratedObjectFactory;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.component.LifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.ssl.SslContextFactory;
/*     */ import org.eclipse.jetty.util.thread.Scheduler;
/*     */ import org.eclipse.jetty.util.thread.ShutdownThread;
/*     */ import org.eclipse.jetty.websocket.api.Session;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketBehavior;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
/*     */ import org.eclipse.jetty.websocket.client.io.ConnectionManager;
/*     */ import org.eclipse.jetty.websocket.client.io.UpgradeListener;
/*     */ import org.eclipse.jetty.websocket.client.masks.Masker;
/*     */ import org.eclipse.jetty.websocket.client.masks.RandomMasker;
/*     */ import org.eclipse.jetty.websocket.common.SessionFactory;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketSession;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketSessionFactory;
/*     */ import org.eclipse.jetty.websocket.common.events.EventDriverFactory;
/*     */ import org.eclipse.jetty.websocket.common.extensions.WebSocketExtensionFactory;
/*     */ import org.eclipse.jetty.websocket.common.scopes.DelegatedContainerScope;
/*     */ import org.eclipse.jetty.websocket.common.scopes.SimpleContainerScope;
/*     */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
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
/*     */ public class WebSocketClient
/*     */   extends ContainerLifeCycle
/*     */   implements WebSocketContainerScope
/*     */ {
/*  68 */   private static final Logger LOG = Log.getLogger(WebSocketClient.class);
/*     */   
/*     */   private final HttpClient httpClient;
/*     */   
/*     */   private final WebSocketContainerScope containerScope;
/*     */   
/*     */   private final WebSocketExtensionFactory extensionRegistry;
/*     */   
/*     */   private final EventDriverFactory eventDriverFactory;
/*     */   
/*     */   private final SessionFactory sessionFactory;
/*  79 */   private final int id = ThreadLocalRandom.current().nextInt();
/*     */   
/*     */ 
/*  82 */   private boolean stopAtShutdown = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClient()
/*     */   {
/*  90 */     this(HttpClientProvider.get(null));
/*  91 */     addBean(this.httpClient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClient(HttpClient httpClient)
/*     */   {
/* 102 */     this(httpClient, new DecoratedObjectFactory());
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
/*     */   public WebSocketClient(HttpClient httpClient, DecoratedObjectFactory objectFactory)
/*     */   {
/* 115 */     this.containerScope = new SimpleContainerScope(WebSocketPolicy.newClientPolicy(), new MappedByteBufferPool(), objectFactory);
/* 116 */     this.httpClient = httpClient;
/* 117 */     this.extensionRegistry = new WebSocketExtensionFactory(this.containerScope);
/* 118 */     this.eventDriverFactory = new EventDriverFactory(this.containerScope);
/* 119 */     this.sessionFactory = new WebSocketSessionFactory(this.containerScope);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public WebSocketClient(Executor executor)
/*     */   {
/* 132 */     this(null, executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClient(ByteBufferPool bufferPool)
/*     */   {
/* 143 */     this(null, null, bufferPool);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClient(SslContextFactory sslContextFactory)
/*     */   {
/* 154 */     this(sslContextFactory, null);
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
/*     */   @Deprecated
/*     */   public WebSocketClient(SslContextFactory sslContextFactory, Executor executor)
/*     */   {
/* 169 */     this(sslContextFactory, executor, new MappedByteBufferPool());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClient(WebSocketContainerScope scope)
/*     */   {
/* 181 */     this(scope.getSslContextFactory(), scope.getExecutor(), scope.getBufferPool(), scope.getObjectFactory());
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
/*     */   public WebSocketClient(WebSocketContainerScope scope, SslContextFactory sslContextFactory)
/*     */   {
/* 196 */     this(sslContextFactory, scope.getExecutor(), scope.getBufferPool(), scope.getObjectFactory());
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
/*     */   public WebSocketClient(SslContextFactory sslContextFactory, Executor executor, ByteBufferPool bufferPool)
/*     */   {
/* 212 */     this(sslContextFactory, executor, bufferPool, new DecoratedObjectFactory());
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
/*     */   private WebSocketClient(SslContextFactory sslContextFactory, Executor executor, ByteBufferPool bufferPool, DecoratedObjectFactory objectFactory)
/*     */   {
/* 230 */     this.httpClient = new HttpClient(sslContextFactory);
/* 231 */     this.httpClient.setExecutor(executor);
/* 232 */     this.httpClient.setByteBufferPool(bufferPool);
/* 233 */     addBean(this.httpClient);
/*     */     
/* 235 */     this.containerScope = new SimpleContainerScope(WebSocketPolicy.newClientPolicy(), bufferPool, objectFactory);
/*     */     
/* 237 */     this.extensionRegistry = new WebSocketExtensionFactory(this.containerScope);
/*     */     
/* 239 */     this.eventDriverFactory = new EventDriverFactory(this.containerScope);
/* 240 */     this.sessionFactory = new WebSocketSessionFactory(this.containerScope);
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
/*     */   public WebSocketClient(WebSocketContainerScope scope, EventDriverFactory eventDriverFactory, SessionFactory sessionFactory)
/*     */   {
/* 256 */     this(scope, eventDriverFactory, sessionFactory, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClient(WebSocketContainerScope scope, EventDriverFactory eventDriverFactory, SessionFactory sessionFactory, HttpClient httpClient)
/*     */   {
/*     */     WebSocketContainerScope clientScope;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     WebSocketContainerScope clientScope;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 275 */     if (scope.getPolicy().getBehavior() == WebSocketBehavior.CLIENT)
/*     */     {
/* 277 */       clientScope = scope;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 282 */       clientScope = new DelegatedContainerScope(WebSocketPolicy.newClientPolicy(), scope);
/*     */     }
/*     */     
/* 285 */     this.containerScope = clientScope;
/*     */     
/* 287 */     if (httpClient == null)
/*     */     {
/* 289 */       this.httpClient = HttpClientProvider.get(scope);
/* 290 */       addBean(this.httpClient);
/*     */     }
/*     */     else
/*     */     {
/* 294 */       this.httpClient = httpClient;
/*     */     }
/*     */     
/* 297 */     this.extensionRegistry = new WebSocketExtensionFactory(this.containerScope);
/*     */     
/* 299 */     this.eventDriverFactory = eventDriverFactory;
/* 300 */     this.sessionFactory = sessionFactory;
/*     */   }
/*     */   
/*     */   public Future<Session> connect(Object websocket, URI toUri) throws IOException
/*     */   {
/* 305 */     ClientUpgradeRequest request = new ClientUpgradeRequest(toUri);
/* 306 */     request.setRequestURI(toUri);
/* 307 */     request.setLocalEndpoint(websocket);
/*     */     
/* 309 */     return connect(websocket, toUri, request);
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
/*     */   public Future<Session> connect(Object websocket, URI toUri, ClientUpgradeRequest request)
/*     */     throws IOException
/*     */   {
/* 327 */     return connect(websocket, toUri, request, (UpgradeListener)null);
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
/*     */   public Future<Session> connect(Object websocket, URI toUri, ClientUpgradeRequest request, UpgradeListener upgradeListener)
/*     */     throws IOException
/*     */   {
/* 350 */     if (!isStarted())
/*     */     {
/* 352 */       throw new IllegalStateException(WebSocketClient.class.getSimpleName() + "@" + hashCode() + " is not started");
/*     */     }
/*     */     
/*     */ 
/* 356 */     if (!toUri.isAbsolute())
/*     */     {
/* 358 */       throw new IllegalArgumentException("WebSocket URI must be absolute");
/*     */     }
/*     */     
/* 361 */     if (StringUtil.isBlank(toUri.getScheme()))
/*     */     {
/* 363 */       throw new IllegalArgumentException("WebSocket URI must include a scheme");
/*     */     }
/*     */     
/* 366 */     String scheme = toUri.getScheme().toLowerCase(Locale.ENGLISH);
/* 367 */     if ((!"ws".equals(scheme)) && (!"wss".equals(scheme)))
/*     */     {
/* 369 */       throw new IllegalArgumentException("WebSocket URI scheme only supports [ws] and [wss], not [" + scheme + "]");
/*     */     }
/*     */     
/* 372 */     if ("wss".equals(scheme))
/*     */     {
/*     */ 
/* 375 */       if (this.httpClient.getSslContextFactory() == null)
/*     */       {
/* 377 */         throw new IllegalStateException("HttpClient has no SslContextFactory, wss:// URI's are not supported in this configuration");
/*     */       }
/*     */     }
/*     */     
/* 381 */     request.setRequestURI(toUri);
/* 382 */     request.setLocalEndpoint(websocket);
/*     */     
/*     */ 
/* 385 */     for (ExtensionConfig reqExt : request.getExtensions())
/*     */     {
/* 387 */       if (!this.extensionRegistry.isAvailable(reqExt.getName()))
/*     */       {
/* 389 */         throw new IllegalArgumentException("Requested extension [" + reqExt.getName() + "] is not installed");
/*     */       }
/*     */     }
/*     */     
/* 393 */     if (LOG.isDebugEnabled()) {
/* 394 */       LOG.debug("connect websocket {} to {}", new Object[] { websocket, toUri });
/*     */     }
/* 396 */     init();
/*     */     
/* 398 */     WebSocketUpgradeRequest wsReq = new WebSocketUpgradeRequest(this, this.httpClient, request);
/*     */     
/* 400 */     wsReq.setUpgradeListener(upgradeListener);
/* 401 */     return wsReq.sendAsync();
/*     */   }
/*     */   
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/* 407 */     Objects.requireNonNull(this.httpClient, "Provided HttpClient is null");
/*     */     
/* 409 */     super.doStart();
/*     */     
/* 411 */     if (!this.httpClient.isRunning()) {
/* 412 */       throw new IllegalStateException("HttpClient is not running (did you forget to start it?): " + this.httpClient);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doStop() throws Exception
/*     */   {
/* 418 */     if (LOG.isDebugEnabled()) {
/* 419 */       LOG.debug("Stopping {}", new Object[] { this });
/*     */     }
/* 421 */     ShutdownThread.deregister(this);
/*     */     
/* 423 */     super.doStop();
/*     */     
/* 425 */     if (LOG.isDebugEnabled()) {
/* 426 */       LOG.debug("Stopped {}", new Object[] { this });
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isDispatchIO() {
/* 432 */     return this.httpClient.isDispatchIO();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getAsyncWriteTimeout()
/*     */   {
/* 442 */     return this.containerScope.getPolicy().getAsyncWriteTimeout();
/*     */   }
/*     */   
/*     */   public SocketAddress getBindAddress()
/*     */   {
/* 447 */     return this.httpClient.getBindAddress();
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBufferPool getBufferPool()
/*     */   {
/* 453 */     return this.httpClient.getByteBufferPool();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public ConnectionManager getConnectionManager()
/*     */   {
/* 459 */     throw new UnsupportedOperationException("ConnectionManager is no longer supported");
/*     */   }
/*     */   
/*     */   public long getConnectTimeout()
/*     */   {
/* 464 */     return this.httpClient.getConnectTimeout();
/*     */   }
/*     */   
/*     */   public CookieStore getCookieStore()
/*     */   {
/* 469 */     return this.httpClient.getCookieStore();
/*     */   }
/*     */   
/*     */   public EventDriverFactory getEventDriverFactory()
/*     */   {
/* 474 */     return this.eventDriverFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   public Executor getExecutor()
/*     */   {
/* 480 */     return this.httpClient.getExecutor();
/*     */   }
/*     */   
/*     */   public ExtensionFactory getExtensionFactory()
/*     */   {
/* 485 */     return this.extensionRegistry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Masker getMasker()
/*     */   {
/* 495 */     return new RandomMasker();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxBinaryMessageBufferSize()
/*     */   {
/* 505 */     return getPolicy().getMaxBinaryMessageBufferSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getMaxBinaryMessageSize()
/*     */   {
/* 515 */     return getPolicy().getMaxBinaryMessageSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getMaxIdleTimeout()
/*     */   {
/* 525 */     return getPolicy().getIdleTimeout();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxTextMessageBufferSize()
/*     */   {
/* 535 */     return getPolicy().getMaxTextMessageBufferSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getMaxTextMessageSize()
/*     */   {
/* 545 */     return getPolicy().getMaxTextMessageSize();
/*     */   }
/*     */   
/*     */ 
/*     */   public DecoratedObjectFactory getObjectFactory()
/*     */   {
/* 551 */     return this.containerScope.getObjectFactory();
/*     */   }
/*     */   
/*     */   public Set<WebSocketSession> getOpenSessions()
/*     */   {
/* 556 */     return Collections.unmodifiableSet(new HashSet(getBeans(WebSocketSession.class)));
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/* 562 */     return this.containerScope.getPolicy();
/*     */   }
/*     */   
/*     */   public Scheduler getScheduler()
/*     */   {
/* 567 */     return this.httpClient.getScheduler();
/*     */   }
/*     */   
/*     */   public SessionFactory getSessionFactory()
/*     */   {
/* 572 */     return this.sessionFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SslContextFactory getSslContextFactory()
/*     */   {
/* 582 */     return this.httpClient.getSslContextFactory();
/*     */   }
/*     */   
/*     */   private synchronized void init() throws IOException
/*     */   {
/* 587 */     if ((isStopAtShutdown()) && (!ShutdownThread.isRegistered(this)))
/*     */     {
/* 589 */       ShutdownThread.register(new LifeCycle[] { this });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected ConnectionManager newConnectionManager()
/*     */   {
/* 602 */     throw new UnsupportedOperationException("ConnectionManager is no longer supported");
/*     */   }
/*     */   
/*     */ 
/*     */   public void onSessionClosed(WebSocketSession session)
/*     */   {
/* 608 */     if (LOG.isDebugEnabled())
/* 609 */       LOG.debug("Session Closed: {}", new Object[] { session });
/* 610 */     removeBean(session);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onSessionOpened(WebSocketSession session)
/*     */   {
/* 616 */     if (LOG.isDebugEnabled())
/* 617 */       LOG.debug("Session Opened: {}", new Object[] { session });
/* 618 */     addManaged(session);
/* 619 */     LOG.debug("post-onSessionOpened() - {}", new Object[] { this });
/*     */   }
/*     */   
/*     */   public void setAsyncWriteTimeout(long ms)
/*     */   {
/* 624 */     getPolicy().setAsyncWriteTimeout(ms);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setBindAdddress(SocketAddress bindAddress)
/*     */   {
/* 635 */     setBindAddress(bindAddress);
/*     */   }
/*     */   
/*     */   public void setBindAddress(SocketAddress bindAddress)
/*     */   {
/* 640 */     this.httpClient.setBindAddress(bindAddress);
/*     */   }
/*     */   
/*     */   public void setBufferPool(ByteBufferPool bufferPool)
/*     */   {
/* 645 */     this.httpClient.setByteBufferPool(bufferPool);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectTimeout(long ms)
/*     */   {
/* 656 */     this.httpClient.setConnectTimeout(ms);
/*     */   }
/*     */   
/*     */   public void setCookieStore(CookieStore cookieStore)
/*     */   {
/* 661 */     this.httpClient.setCookieStore(cookieStore);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setDaemon(boolean daemon) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setDispatchIO(boolean dispatchIO)
/*     */   {
/* 677 */     this.httpClient.setDispatchIO(dispatchIO);
/*     */   }
/*     */   
/*     */   public void setExecutor(Executor executor)
/*     */   {
/* 682 */     this.httpClient.setExecutor(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setMasker(Masker masker) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxBinaryMessageBufferSize(int max)
/*     */   {
/* 697 */     getPolicy().setMaxBinaryMessageBufferSize(max);
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
/*     */   public void setMaxIdleTimeout(long ms)
/*     */   {
/* 710 */     getPolicy().setIdleTimeout(ms);
/* 711 */     this.httpClient.setIdleTimeout(ms);
/*     */   }
/*     */   
/*     */   public void setMaxTextMessageBufferSize(int max)
/*     */   {
/* 716 */     getPolicy().setMaxTextMessageBufferSize(max);
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 722 */     dumpThis(out);
/* 723 */     dump(out, indent, new Collection[] { getOpenSessions() });
/*     */   }
/*     */   
/*     */   public HttpClient getHttpClient()
/*     */   {
/* 728 */     return this.httpClient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setStopAtShutdown(boolean stop)
/*     */   {
/* 740 */     if (stop)
/*     */     {
/* 742 */       if ((!this.stopAtShutdown) && (isStarted()) && (!ShutdownThread.isRegistered(this))) {
/* 743 */         ShutdownThread.register(new LifeCycle[] { this });
/*     */       }
/*     */     } else {
/* 746 */       ShutdownThread.deregister(this);
/*     */     }
/* 748 */     this.stopAtShutdown = stop;
/*     */   }
/*     */   
/*     */   public boolean isStopAtShutdown()
/*     */   {
/* 753 */     return this.stopAtShutdown;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 759 */     StringBuilder sb = new StringBuilder("WebSocketClient@");
/* 760 */     sb.append(Integer.toHexString(this.id));
/* 761 */     sb.append("[httpClient=").append(this.httpClient);
/* 762 */     sb.append(",openSessions.size=");
/* 763 */     sb.append(getOpenSessions().size());
/* 764 */     sb.append(']');
/* 765 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\WebSocketClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */