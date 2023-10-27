/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Objects;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.Connection;
/*     */ import org.eclipse.jetty.io.Connection.Listener;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.component.Dumpable;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.ThreadClassLoaderScope;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.CloseException;
/*     */ import org.eclipse.jetty.websocket.api.CloseStatus;
/*     */ import org.eclipse.jetty.websocket.api.RemoteEndpoint;
/*     */ import org.eclipse.jetty.websocket.api.Session;
/*     */ import org.eclipse.jetty.websocket.api.SuspendToken;
/*     */ import org.eclipse.jetty.websocket.api.UpgradeRequest;
/*     */ import org.eclipse.jetty.websocket.api.UpgradeResponse;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketBehavior;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketException;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
/*     */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*     */ import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
/*     */ import org.eclipse.jetty.websocket.common.events.EventDriver;
/*     */ import org.eclipse.jetty.websocket.common.frames.CloseFrame;
/*     */ import org.eclipse.jetty.websocket.common.io.IOState;
/*     */ import org.eclipse.jetty.websocket.common.io.IOState.ConnectionStateListener;
/*     */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
/*     */ import org.eclipse.jetty.websocket.common.scopes.WebSocketSessionScope;
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
/*     */ @ManagedObject("A Jetty WebSocket Session")
/*     */ public class WebSocketSession
/*     */   extends ContainerLifeCycle
/*     */   implements Session, RemoteEndpointFactory, WebSocketSessionScope, IncomingFrames, Connection.Listener, IOState.ConnectionStateListener
/*     */ {
/*     */   public static class OnCloseLocalCallback
/*     */     implements WriteCallback
/*     */   {
/*     */     private final Callback callback;
/*     */     private final LogicalConnection connection;
/*     */     private final CloseInfo close;
/*     */     
/*     */     public OnCloseLocalCallback(Callback callback, LogicalConnection connection, CloseInfo close)
/*     */     {
/*  79 */       this.callback = callback;
/*  80 */       this.connection = connection;
/*  81 */       this.close = close;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void writeSuccess()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:callback	Lorg/eclipse/jetty/util/Callback;
/*     */       //   4: ifnull +12 -> 16
/*     */       //   7: aload_0
/*     */       //   8: getfield 2	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:callback	Lorg/eclipse/jetty/util/Callback;
/*     */       //   11: invokeinterface 5 1 0
/*     */       //   16: aload_0
/*     */       //   17: getfield 3	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:connection	Lorg/eclipse/jetty/websocket/common/LogicalConnection;
/*     */       //   20: aload_0
/*     */       //   21: getfield 4	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:close	Lorg/eclipse/jetty/websocket/common/CloseInfo;
/*     */       //   24: invokeinterface 6 2 0
/*     */       //   29: goto +19 -> 48
/*     */       //   32: astore_1
/*     */       //   33: aload_0
/*     */       //   34: getfield 3	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:connection	Lorg/eclipse/jetty/websocket/common/LogicalConnection;
/*     */       //   37: aload_0
/*     */       //   38: getfield 4	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:close	Lorg/eclipse/jetty/websocket/common/CloseInfo;
/*     */       //   41: invokeinterface 6 2 0
/*     */       //   46: aload_1
/*     */       //   47: athrow
/*     */       //   48: return
/*     */       // Line number table:
/*     */       //   Java source line #89	-> byte code offset #0
/*     */       //   Java source line #91	-> byte code offset #7
/*     */       //   Java source line #96	-> byte code offset #16
/*     */       //   Java source line #97	-> byte code offset #29
/*     */       //   Java source line #96	-> byte code offset #32
/*     */       //   Java source line #98	-> byte code offset #48
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	49	0	this	OnCloseLocalCallback
/*     */       //   32	15	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	16	32	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void writeFailed(Throwable x)
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:callback	Lorg/eclipse/jetty/util/Callback;
/*     */       //   4: ifnull +13 -> 17
/*     */       //   7: aload_0
/*     */       //   8: getfield 2	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:callback	Lorg/eclipse/jetty/util/Callback;
/*     */       //   11: aload_1
/*     */       //   12: invokeinterface 7 2 0
/*     */       //   17: aload_0
/*     */       //   18: getfield 3	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:connection	Lorg/eclipse/jetty/websocket/common/LogicalConnection;
/*     */       //   21: aload_0
/*     */       //   22: getfield 4	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:close	Lorg/eclipse/jetty/websocket/common/CloseInfo;
/*     */       //   25: invokeinterface 6 2 0
/*     */       //   30: goto +19 -> 49
/*     */       //   33: astore_2
/*     */       //   34: aload_0
/*     */       //   35: getfield 3	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:connection	Lorg/eclipse/jetty/websocket/common/LogicalConnection;
/*     */       //   38: aload_0
/*     */       //   39: getfield 4	org/eclipse/jetty/websocket/common/WebSocketSession$OnCloseLocalCallback:close	Lorg/eclipse/jetty/websocket/common/CloseInfo;
/*     */       //   42: invokeinterface 6 2 0
/*     */       //   47: aload_2
/*     */       //   48: athrow
/*     */       //   49: return
/*     */       // Line number table:
/*     */       //   Java source line #105	-> byte code offset #0
/*     */       //   Java source line #107	-> byte code offset #7
/*     */       //   Java source line #112	-> byte code offset #17
/*     */       //   Java source line #113	-> byte code offset #30
/*     */       //   Java source line #112	-> byte code offset #33
/*     */       //   Java source line #114	-> byte code offset #49
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	50	0	this	OnCloseLocalCallback
/*     */       //   0	50	1	x	Throwable
/*     */       //   33	15	2	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	17	33	finally
/*     */     }
/*     */   }
/*     */   
/*     */   public class DisconnectCallback
/*     */     implements Callback
/*     */   {
/*     */     public DisconnectCallback() {}
/*     */     
/*     */     public void failed(Throwable x)
/*     */     {
/* 122 */       WebSocketSession.this.disconnect();
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 128 */       WebSocketSession.this.disconnect();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 133 */   private static final Logger LOG = Log.getLogger(WebSocketSession.class);
/* 134 */   private static final Logger LOG_OPEN = Log.getLogger(WebSocketSession.class.getName() + "_OPEN");
/*     */   private final WebSocketContainerScope containerScope;
/*     */   private final URI requestURI;
/*     */   private final LogicalConnection connection;
/*     */   private final EventDriver websocket;
/*     */   private final Executor executor;
/*     */   private final WebSocketPolicy policy;
/* 141 */   private final AtomicBoolean closed = new AtomicBoolean();
/*     */   private ClassLoader classLoader;
/*     */   private ExtensionFactory extensionFactory;
/*     */   private RemoteEndpointFactory remoteEndpointFactory;
/*     */   private String protocolVersion;
/* 146 */   private Map<String, String[]> parameterMap = new HashMap();
/*     */   private RemoteEndpoint remote;
/*     */   private IncomingFrames incomingHandler;
/*     */   private OutgoingFrames outgoingHandler;
/*     */   private UpgradeRequest upgradeRequest;
/*     */   private UpgradeResponse upgradeResponse;
/*     */   private CompletableFuture<Session> openFuture;
/*     */   
/*     */   public WebSocketSession(WebSocketContainerScope containerScope, URI requestURI, EventDriver websocket, LogicalConnection connection)
/*     */   {
/* 156 */     Objects.requireNonNull(containerScope, "Container Scope cannot be null");
/* 157 */     Objects.requireNonNull(requestURI, "Request URI cannot be null");
/*     */     
/* 159 */     this.classLoader = Thread.currentThread().getContextClassLoader();
/* 160 */     this.containerScope = containerScope;
/* 161 */     this.requestURI = requestURI;
/* 162 */     this.websocket = websocket;
/* 163 */     this.connection = connection;
/* 164 */     this.executor = connection.getExecutor();
/* 165 */     this.outgoingHandler = connection;
/* 166 */     this.incomingHandler = websocket;
/* 167 */     this.connection.getIOState().addListener(this);
/* 168 */     this.policy = websocket.getPolicy();
/*     */     
/* 170 */     this.connection.setSession(this);
/*     */     
/* 172 */     addBean(this.connection);
/* 173 */     addBean(this.websocket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void abort(int statusCode, String reason)
/*     */   {
/* 183 */     close(new CloseInfo(statusCode, reason), new DisconnectCallback());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 190 */     close(new CloseInfo(1000), null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close(CloseStatus closeStatus)
/*     */   {
/* 196 */     close(new CloseInfo(closeStatus.getCode(), closeStatus.getPhrase()), null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close(int statusCode, String reason)
/*     */   {
/* 202 */     close(new CloseInfo(statusCode, reason), null);
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
/*     */   private void close(CloseInfo closeInfo, Callback callback)
/*     */   {
/* 217 */     if (LOG.isDebugEnabled()) {
/* 218 */       LOG.debug("close({})", new Object[] { closeInfo });
/*     */     }
/* 220 */     if (this.closed.compareAndSet(false, true))
/*     */     {
/* 222 */       CloseFrame frame = closeInfo.asFrame();
/* 223 */       this.connection.outgoingFrame(frame, new OnCloseLocalCallback(callback, this.connection, closeInfo), BatchMode.OFF);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 233 */     this.connection.disconnect();
/*     */     
/*     */ 
/* 236 */     notifyClose(1006, "Harsh disconnect");
/*     */   }
/*     */   
/*     */   public void dispatch(Runnable runnable)
/*     */   {
/* 241 */     this.executor.execute(runnable);
/*     */   }
/*     */   
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/* 247 */     if (LOG.isDebugEnabled()) {
/* 248 */       LOG.debug("starting - {}", new Object[] { this });
/*     */     }
/* 250 */     Iterator<RemoteEndpointFactory> iter = ServiceLoader.load(RemoteEndpointFactory.class).iterator();
/* 251 */     if (iter.hasNext()) {
/* 252 */       this.remoteEndpointFactory = ((RemoteEndpointFactory)iter.next());
/*     */     }
/* 254 */     if (this.remoteEndpointFactory == null) {
/* 255 */       this.remoteEndpointFactory = this;
/*     */     }
/* 257 */     if (LOG.isDebugEnabled()) {
/* 258 */       LOG.debug("Using RemoteEndpointFactory: {}", new Object[] { this.remoteEndpointFactory });
/*     */     }
/* 260 */     super.doStart();
/*     */   }
/*     */   
/*     */   protected void doStop()
/*     */     throws Exception
/*     */   {
/* 266 */     if (LOG.isDebugEnabled()) {
/* 267 */       LOG.debug("stopping - {}", new Object[] { this });
/*     */     }
/*     */     try {
/* 270 */       close(1001, "Shutdown");
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 274 */       LOG.debug("During Connection Shutdown", t);
/*     */     }
/* 276 */     super.doStop();
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 282 */     dumpThis(out);
/* 283 */     out.append(indent).append(" +- incomingHandler : ");
/* 284 */     if ((this.incomingHandler instanceof Dumpable))
/*     */     {
/* 286 */       ((Dumpable)this.incomingHandler).dump(out, indent + "    ");
/*     */     }
/*     */     else
/*     */     {
/* 290 */       out.append(this.incomingHandler.toString()).append(System.lineSeparator());
/*     */     }
/*     */     
/* 293 */     out.append(indent).append(" +- outgoingHandler : ");
/* 294 */     if ((this.outgoingHandler instanceof Dumpable))
/*     */     {
/* 296 */       ((Dumpable)this.outgoingHandler).dump(out, indent + "    ");
/*     */     }
/*     */     else
/*     */     {
/* 300 */       out.append(this.outgoingHandler.toString()).append(System.lineSeparator());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 307 */     if (this == obj)
/*     */     {
/* 309 */       return true;
/*     */     }
/* 311 */     if (obj == null)
/*     */     {
/* 313 */       return false;
/*     */     }
/* 315 */     if (getClass() != obj.getClass())
/*     */     {
/* 317 */       return false;
/*     */     }
/* 319 */     WebSocketSession other = (WebSocketSession)obj;
/* 320 */     if (this.connection == null)
/*     */     {
/* 322 */       if (other.connection != null)
/*     */       {
/* 324 */         return false;
/*     */       }
/*     */     }
/* 327 */     else if (!this.connection.equals(other.connection))
/*     */     {
/* 329 */       return false;
/*     */     }
/* 331 */     return true;
/*     */   }
/*     */   
/*     */   public ByteBufferPool getBufferPool()
/*     */   {
/* 336 */     return this.connection.getBufferPool();
/*     */   }
/*     */   
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 341 */     return getClass().getClassLoader();
/*     */   }
/*     */   
/*     */   public LogicalConnection getConnection()
/*     */   {
/* 346 */     return this.connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketContainerScope getContainerScope()
/*     */   {
/* 352 */     return this.containerScope;
/*     */   }
/*     */   
/*     */   public ExtensionFactory getExtensionFactory()
/*     */   {
/* 357 */     return this.extensionFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getIdleTimeout()
/*     */   {
/* 366 */     return this.connection.getMaxIdleTimeout();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(readonly=true)
/*     */   public IncomingFrames getIncomingHandler()
/*     */   {
/* 372 */     return this.incomingHandler;
/*     */   }
/*     */   
/*     */ 
/*     */   public InetSocketAddress getLocalAddress()
/*     */   {
/* 378 */     return this.connection.getLocalAddress();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(readonly=true)
/*     */   public OutgoingFrames getOutgoingHandler()
/*     */   {
/* 384 */     return this.outgoingHandler;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/* 390 */     return this.policy;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProtocolVersion()
/*     */   {
/* 396 */     return this.protocolVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   public RemoteEndpoint getRemote()
/*     */   {
/* 402 */     if (LOG_OPEN.isDebugEnabled())
/* 403 */       LOG_OPEN.debug("[{}] {}.getRemote()", new Object[] { this.policy.getBehavior(), getClass().getSimpleName() });
/* 404 */     ConnectionState state = this.connection.getIOState().getConnectionState();
/*     */     
/* 406 */     if ((state == ConnectionState.OPEN) || (state == ConnectionState.CONNECTED))
/*     */     {
/* 408 */       return this.remote;
/*     */     }
/*     */     
/* 411 */     throw new WebSocketException("RemoteEndpoint unavailable, current state [" + state + "], expecting [OPEN or CONNECTED]");
/*     */   }
/*     */   
/*     */ 
/*     */   public InetSocketAddress getRemoteAddress()
/*     */   {
/* 417 */     return this.remote.getInetSocketAddress();
/*     */   }
/*     */   
/*     */   public URI getRequestURI()
/*     */   {
/* 422 */     return this.requestURI;
/*     */   }
/*     */   
/*     */ 
/*     */   public UpgradeRequest getUpgradeRequest()
/*     */   {
/* 428 */     return this.upgradeRequest;
/*     */   }
/*     */   
/*     */ 
/*     */   public UpgradeResponse getUpgradeResponse()
/*     */   {
/* 434 */     return this.upgradeResponse;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WebSocketSession getWebSocketSession()
/*     */   {
/* 441 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 447 */     int prime = 31;
/* 448 */     int result = 1;
/* 449 */     result = 31 * result + (this.connection == null ? 0 : this.connection.hashCode());
/* 450 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void incomingError(Throwable t)
/*     */   {
/* 460 */     this.websocket.incomingError(t);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void incomingFrame(org.eclipse.jetty.websocket.api.extensions.Frame frame)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: invokevirtual 12	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   6: astore_2
/*     */     //   7: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   10: aload_0
/*     */     //   11: getfield 13	org/eclipse/jetty/websocket/common/WebSocketSession:classLoader	Ljava/lang/ClassLoader;
/*     */     //   14: invokevirtual 108	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   17: aload_0
/*     */     //   18: getfield 17	org/eclipse/jetty/websocket/common/WebSocketSession:connection	Lorg/eclipse/jetty/websocket/common/LogicalConnection;
/*     */     //   21: invokeinterface 22 1 0
/*     */     //   26: invokevirtual 109	org/eclipse/jetty/websocket/common/io/IOState:isInputAvailable	()Z
/*     */     //   29: ifeq +13 -> 42
/*     */     //   32: aload_0
/*     */     //   33: getfield 21	org/eclipse/jetty/websocket/common/WebSocketSession:incomingHandler	Lorg/eclipse/jetty/websocket/api/extensions/IncomingFrames;
/*     */     //   36: aload_1
/*     */     //   37: invokeinterface 110 2 0
/*     */     //   42: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   45: aload_2
/*     */     //   46: invokevirtual 108	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   49: goto +13 -> 62
/*     */     //   52: astore_3
/*     */     //   53: invokestatic 11	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   56: aload_2
/*     */     //   57: invokevirtual 108	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   60: aload_3
/*     */     //   61: athrow
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #469	-> byte code offset #0
/*     */     //   Java source line #472	-> byte code offset #7
/*     */     //   Java source line #473	-> byte code offset #17
/*     */     //   Java source line #476	-> byte code offset #32
/*     */     //   Java source line #481	-> byte code offset #42
/*     */     //   Java source line #482	-> byte code offset #49
/*     */     //   Java source line #481	-> byte code offset #52
/*     */     //   Java source line #483	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	WebSocketSession
/*     */     //   0	63	1	frame	org.eclipse.jetty.websocket.api.extensions.Frame
/*     */     //   6	51	2	old	ClassLoader
/*     */     //   52	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	42	52	finally
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 488 */     if (this.connection == null)
/*     */     {
/* 490 */       return false;
/*     */     }
/* 492 */     return (!this.closed.get()) && (this.connection.isOpen());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSecure()
/*     */   {
/* 498 */     if (this.upgradeRequest == null)
/*     */     {
/* 500 */       throw new IllegalStateException("No valid UpgradeRequest yet");
/*     */     }
/*     */     
/* 503 */     URI requestURI = this.upgradeRequest.getRequestURI();
/*     */     
/* 505 */     return "wss".equalsIgnoreCase(requestURI.getScheme());
/*     */   }
/*     */   
/*     */   public void notifyClose(int statusCode, String reason)
/*     */   {
/* 510 */     if (LOG.isDebugEnabled())
/*     */     {
/* 512 */       LOG.debug("notifyClose({},{})", new Object[] { Integer.valueOf(statusCode), reason });
/*     */     }
/* 514 */     this.websocket.onClose(new CloseInfo(statusCode, reason));
/*     */   }
/*     */   
/*     */   public void notifyError(Throwable cause)
/*     */   {
/* 519 */     if ((this.openFuture != null) && (!this.openFuture.isDone()))
/* 520 */       this.openFuture.completeExceptionally(cause);
/* 521 */     incomingError(cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onClosed(Connection connection) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onOpened(Connection connection)
/*     */   {
/* 542 */     if (LOG_OPEN.isDebugEnabled())
/* 543 */       LOG_OPEN.debug("[{}] {}.onOpened()", new Object[] { this.policy.getBehavior(), getClass().getSimpleName() });
/* 544 */     open();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onConnectionStateChange(ConnectionState state)
/*     */   {
/* 551 */     switch (state)
/*     */     {
/*     */     case CLOSED: 
/* 554 */       IOState ioState = this.connection.getIOState();
/* 555 */       CloseInfo close = ioState.getCloseInfo();
/*     */       
/* 557 */       notifyClose(close.getStatusCode(), close.getReason());
/*     */       try
/*     */       {
/* 560 */         if (LOG.isDebugEnabled())
/* 561 */           LOG.debug("{}.onSessionClosed()", new Object[] { this.containerScope.getClass().getSimpleName() });
/* 562 */         this.containerScope.onSessionClosed(this);
/*     */       }
/*     */       catch (Throwable t)
/*     */       {
/* 566 */         LOG.ignore(t);
/*     */       }
/*     */     
/*     */ 
/*     */     case CONNECTED: 
/*     */       try
/*     */       {
/* 573 */         if (LOG.isDebugEnabled())
/* 574 */           LOG.debug("{}.onSessionOpened()", new Object[] { this.containerScope.getClass().getSimpleName() });
/* 575 */         this.containerScope.onSessionOpened(this);
/*     */       }
/*     */       catch (Throwable t)
/*     */       {
/* 579 */         LOG.ignore(t);
/*     */       }
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketRemoteEndpoint newRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoingFrames, BatchMode batchMode)
/*     */   {
/* 588 */     return new WebSocketRemoteEndpoint(connection, this.outgoingHandler, getBatchMode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void open()
/*     */   {
/* 596 */     if (LOG_OPEN.isDebugEnabled()) {
/* 597 */       LOG_OPEN.debug("[{}] {}.open()", new Object[] { this.policy.getBehavior(), getClass().getSimpleName() });
/*     */     }
/* 599 */     if (this.remote != null)
/*     */     {
/*     */ 
/* 602 */       return;
/*     */     }
/*     */     try {
/* 605 */       ThreadClassLoaderScope scope = new ThreadClassLoaderScope(this.classLoader);Throwable localThrowable4 = null;
/*     */       try
/*     */       {
/* 608 */         this.connection.getIOState().onConnected();
/*     */         
/*     */ 
/* 611 */         this.remote = this.remoteEndpointFactory.newRemoteEndpoint(this.connection, this.outgoingHandler, getBatchMode());
/* 612 */         if (LOG_OPEN.isDebugEnabled()) {
/* 613 */           LOG_OPEN.debug("[{}] {}.open() remote={}", new Object[] { this.policy.getBehavior(), getClass().getSimpleName(), this.remote });
/*     */         }
/*     */         
/* 616 */         this.websocket.openSession(this);
/*     */         
/*     */ 
/* 619 */         this.connection.getIOState().onOpened();
/*     */         
/* 621 */         if (LOG.isDebugEnabled())
/*     */         {
/* 623 */           LOG.debug("open -> {}", new Object[] { dump() });
/*     */         }
/*     */         
/* 626 */         if (this.openFuture != null)
/*     */         {
/* 628 */           this.openFuture.complete(this);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 605 */         localThrowable4 = localThrowable2;throw localThrowable2;
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
/*     */       }
/*     */       finally
/*     */       {
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
/* 630 */         if (localThrowable4 != null) try { scope.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else scope.close();
/*     */       }
/*     */     } catch (CloseException ce) {
/* 633 */       LOG.warn(ce);
/* 634 */       close(ce.getStatusCode(), ce.getMessage());
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 638 */       LOG.warn(t);
/*     */       
/*     */ 
/* 641 */       int statusCode = 1011;
/* 642 */       if (this.policy.getBehavior() == WebSocketBehavior.CLIENT)
/*     */       {
/* 644 */         statusCode = 1008;
/*     */       }
/* 646 */       close(statusCode, t.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setExtensionFactory(ExtensionFactory extensionFactory)
/*     */   {
/* 652 */     this.extensionFactory = extensionFactory;
/*     */   }
/*     */   
/*     */   public void setFuture(CompletableFuture<Session> fut)
/*     */   {
/* 657 */     this.openFuture = fut;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIdleTimeout(long ms)
/*     */   {
/* 666 */     this.connection.setMaxIdleTimeout(ms);
/*     */   }
/*     */   
/*     */   public void setOutgoingHandler(OutgoingFrames outgoing)
/*     */   {
/* 671 */     this.outgoingHandler = outgoing;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setPolicy(WebSocketPolicy policy) {}
/*     */   
/*     */ 
/*     */   public void setUpgradeRequest(UpgradeRequest request)
/*     */   {
/* 682 */     this.upgradeRequest = request;
/* 683 */     this.protocolVersion = request.getProtocolVersion();
/* 684 */     this.parameterMap.clear();
/* 685 */     if (request.getParameterMap() != null)
/*     */     {
/* 687 */       for (Map.Entry<String, List<String>> entry : request.getParameterMap().entrySet())
/*     */       {
/* 689 */         List<String> values = (List)entry.getValue();
/* 690 */         if (values != null)
/*     */         {
/* 692 */           this.parameterMap.put((String)entry.getKey(), (String[])values.toArray(new String[values.size()]));
/*     */         }
/*     */         else
/*     */         {
/* 696 */           this.parameterMap.put((String)entry.getKey(), new String[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setUpgradeResponse(UpgradeResponse response)
/*     */   {
/* 704 */     this.upgradeResponse = response;
/*     */   }
/*     */   
/*     */ 
/*     */   public SuspendToken suspend()
/*     */   {
/* 710 */     return this.connection.suspend();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BatchMode getBatchMode()
/*     */   {
/* 718 */     return BatchMode.AUTO;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 724 */     StringBuilder builder = new StringBuilder();
/* 725 */     builder.append("WebSocketSession[");
/* 726 */     builder.append("websocket=").append(this.websocket);
/* 727 */     builder.append(",behavior=").append(this.policy.getBehavior());
/* 728 */     builder.append(",connection=").append(this.connection);
/* 729 */     builder.append(",remote=").append(this.remote);
/* 730 */     builder.append(",incoming=").append(this.incomingHandler);
/* 731 */     builder.append(",outgoing=").append(this.outgoingHandler);
/* 732 */     builder.append("]");
/* 733 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static abstract interface Listener
/*     */   {
/*     */     public abstract void onOpened(WebSocketSession paramWebSocketSession);
/*     */     
/*     */     public abstract void onClosed(WebSocketSession paramWebSocketSession);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\WebSocketSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */