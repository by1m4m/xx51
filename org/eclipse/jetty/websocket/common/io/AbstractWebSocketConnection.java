/*     */ package org.eclipse.jetty.websocket.common.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.eclipse.jetty.io.AbstractConnection;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.Connection.UpgradeTo;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.component.Dumpable;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Scheduler;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.CloseException;
/*     */ import org.eclipse.jetty.websocket.api.SuspendToken;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*     */ import org.eclipse.jetty.websocket.common.CloseInfo;
/*     */ import org.eclipse.jetty.websocket.common.ConnectionState;
/*     */ import org.eclipse.jetty.websocket.common.Generator;
/*     */ import org.eclipse.jetty.websocket.common.LogicalConnection;
/*     */ import org.eclipse.jetty.websocket.common.Parser;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketSession;
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
/*     */ public abstract class AbstractWebSocketConnection
/*     */   extends AbstractConnection
/*     */   implements LogicalConnection, Connection.UpgradeTo, IOState.ConnectionStateListener, Dumpable
/*     */ {
/*     */   private class Flusher
/*     */     extends FrameFlusher
/*     */   {
/*     */     private Flusher(ByteBufferPool bufferPool, Generator generator, EndPoint endpoint)
/*     */     {
/*  66 */       super(generator, endpoint, AbstractWebSocketConnection.this.getPolicy().getMaxBinaryMessageBufferSize(), 8);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onCompleteFailure(Throwable failure)
/*     */     {
/*  72 */       super.onCompleteFailure(failure);
/*  73 */       AbstractWebSocketConnection.this.notifyError(failure);
/*  74 */       if (AbstractWebSocketConnection.this.ioState.wasAbnormalClose())
/*     */       {
/*  76 */         AbstractWebSocketConnection.LOG.ignore(failure);
/*  77 */         return;
/*     */       }
/*  79 */       if (AbstractWebSocketConnection.LOG.isDebugEnabled())
/*  80 */         AbstractWebSocketConnection.LOG.debug("Write flush failure", failure);
/*  81 */       AbstractWebSocketConnection.this.ioState.onWriteFailure(failure);
/*  82 */       AbstractWebSocketConnection.this.disconnect();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Stats
/*     */   {
/*  88 */     private AtomicLong countFillInterestedEvents = new AtomicLong(0L);
/*  89 */     private AtomicLong countOnFillableEvents = new AtomicLong(0L);
/*  90 */     private AtomicLong countFillableErrors = new AtomicLong(0L);
/*     */     
/*     */     public long getFillableErrorCount()
/*     */     {
/*  94 */       return this.countFillableErrors.get();
/*     */     }
/*     */     
/*     */     public long getFillInterestedCount()
/*     */     {
/*  99 */       return this.countFillInterestedEvents.get();
/*     */     }
/*     */     
/*     */     public long getOnFillableCount()
/*     */     {
/* 104 */       return this.countOnFillableEvents.get();
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum ReadMode
/*     */   {
/* 110 */     PARSE, 
/* 111 */     DISCARD, 
/* 112 */     EOF;
/*     */     
/*     */     private ReadMode() {} }
/* 115 */   private static final Logger LOG = Log.getLogger(AbstractWebSocketConnection.class);
/* 116 */   private static final AtomicLong ID_GEN = new AtomicLong(0L);
/*     */   
/*     */   private static final int MIN_BUFFER_SIZE = 28;
/*     */   
/*     */   private final ByteBufferPool bufferPool;
/*     */   
/*     */   private final Scheduler scheduler;
/*     */   
/*     */   private final Generator generator;
/*     */   
/*     */   private final Parser parser;
/*     */   private final WebSocketPolicy policy;
/*     */   private final AtomicBoolean suspendToken;
/*     */   private final FrameFlusher flusher;
/*     */   private final String id;
/*     */   private WebSocketSession session;
/*     */   private List<ExtensionConfig> extensions;
/*     */   private boolean isFilling;
/*     */   private ByteBuffer prefillBuffer;
/* 135 */   private ReadMode readMode = ReadMode.PARSE;
/*     */   private IOState ioState;
/* 137 */   private Stats stats = new Stats();
/*     */   
/*     */   public AbstractWebSocketConnection(EndPoint endp, Executor executor, Scheduler scheduler, WebSocketPolicy policy, ByteBufferPool bufferPool)
/*     */   {
/* 141 */     super(endp, executor);
/*     */     
/* 143 */     this.id = Long.toString(ID_GEN.incrementAndGet());
/* 144 */     this.policy = policy;
/* 145 */     this.bufferPool = bufferPool;
/* 146 */     this.generator = new Generator(policy, bufferPool);
/* 147 */     this.parser = new Parser(policy, bufferPool);
/* 148 */     this.scheduler = scheduler;
/* 149 */     this.extensions = new ArrayList();
/* 150 */     this.suspendToken = new AtomicBoolean(false);
/* 151 */     this.ioState = new IOState();
/* 152 */     this.ioState.addListener(this);
/* 153 */     this.flusher = new Flusher(bufferPool, this.generator, endp, null);
/* 154 */     setInputBufferSize(policy.getInputBufferSize());
/* 155 */     setMaxIdleTimeout(policy.getIdleTimeout());
/*     */   }
/*     */   
/*     */ 
/*     */   public Executor getExecutor()
/*     */   {
/* 161 */     return super.getExecutor();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onLocalClose(CloseInfo close)
/*     */   {
/* 167 */     if (LOG.isDebugEnabled()) {
/* 168 */       LOG.debug("Local Close Confirmed {}", new Object[] { close });
/*     */     }
/* 170 */     if (close.isAbnormal())
/*     */     {
/* 172 */       this.ioState.onAbnormalClose(close);
/*     */     }
/*     */     else
/*     */     {
/* 176 */       this.ioState.onCloseLocal(close);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSession(WebSocketSession session)
/*     */   {
/* 183 */     this.session = session;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean onIdleExpired()
/*     */   {
/* 190 */     return super.onIdleExpired();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 199 */     this.session.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 205 */     if (LOG.isDebugEnabled())
/* 206 */       LOG.debug("{} disconnect()", new Object[] { this.policy.getBehavior() });
/* 207 */     this.flusher.terminate(new EOFException("Disconnected"), false);
/* 208 */     EndPoint endPoint = getEndPoint();
/*     */     
/*     */ 
/* 211 */     endPoint.shutdownOutput();
/* 212 */     endPoint.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public void fillInterested()
/*     */   {
/* 218 */     this.stats.countFillInterestedEvents.incrementAndGet();
/* 219 */     super.fillInterested();
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBufferPool getBufferPool()
/*     */   {
/* 225 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ExtensionConfig> getExtensions()
/*     */   {
/* 237 */     return this.extensions;
/*     */   }
/*     */   
/*     */   public Generator getGenerator()
/*     */   {
/* 242 */     return this.generator;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getId()
/*     */   {
/* 248 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getIdleTimeout()
/*     */   {
/* 254 */     return getEndPoint().getIdleTimeout();
/*     */   }
/*     */   
/*     */ 
/*     */   public IOState getIOState()
/*     */   {
/* 260 */     return this.ioState;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getMaxIdleTimeout()
/*     */   {
/* 266 */     return getEndPoint().getIdleTimeout();
/*     */   }
/*     */   
/*     */   public Parser getParser()
/*     */   {
/* 271 */     return this.parser;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/* 277 */     return this.policy;
/*     */   }
/*     */   
/*     */ 
/*     */   public InetSocketAddress getRemoteAddress()
/*     */   {
/* 283 */     return getEndPoint().getRemoteAddress();
/*     */   }
/*     */   
/*     */   public Scheduler getScheduler()
/*     */   {
/* 288 */     return this.scheduler;
/*     */   }
/*     */   
/*     */   public Stats getStats()
/*     */   {
/* 293 */     return this.stats;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 299 */     return getEndPoint().isOpen();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isReading()
/*     */   {
/* 305 */     return this.isFilling;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onClose()
/*     */   {
/* 316 */     if (LOG.isDebugEnabled())
/* 317 */       LOG.debug("{} onClose()", new Object[] { this.policy.getBehavior() });
/* 318 */     super.onClose();
/* 319 */     this.ioState.onDisconnected();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onConnectionStateChange(ConnectionState state)
/*     */   {
/* 325 */     if (LOG.isDebugEnabled()) {
/* 326 */       LOG.debug("{} Connection State Change: {}", new Object[] { this.policy.getBehavior(), state });
/*     */     }
/* 328 */     switch (state)
/*     */     {
/*     */     case OPEN: 
/* 331 */       if (BufferUtil.hasContent(this.prefillBuffer))
/*     */       {
/* 333 */         if (LOG.isDebugEnabled())
/*     */         {
/* 335 */           LOG.debug("Parsing Upgrade prefill buffer ({} remaining)", this.prefillBuffer.remaining());
/*     */         }
/* 337 */         this.parser.parse(this.prefillBuffer);
/*     */       }
/* 339 */       if (LOG.isDebugEnabled())
/*     */       {
/* 341 */         LOG.debug("OPEN: normal fillInterested", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 345 */       fillInterested();
/* 346 */       break;
/*     */     case CLOSED: 
/* 348 */       if (LOG.isDebugEnabled())
/* 349 */         LOG.debug("CLOSED - wasAbnormalClose: {}", new Object[] { Boolean.valueOf(this.ioState.wasAbnormalClose()) });
/* 350 */       if (this.ioState.wasAbnormalClose())
/*     */       {
/*     */ 
/* 353 */         this.session.close(1001, "Abnormal Close - " + this.ioState.getCloseInfo().getReason());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 358 */         disconnect();
/*     */       }
/* 360 */       break;
/*     */     case CLOSING: 
/* 362 */       if (LOG.isDebugEnabled()) {
/* 363 */         LOG.debug("CLOSING - wasRemoteCloseInitiated: {}", new Object[] { Boolean.valueOf(this.ioState.wasRemoteCloseInitiated()) });
/*     */       }
/*     */       
/* 366 */       if (this.ioState.wasRemoteCloseInitiated())
/*     */       {
/* 368 */         CloseInfo close = this.ioState.getCloseInfo();
/* 369 */         this.session.close(close.getStatusCode(), close.getReason());
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void onFillable()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 2	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   3: invokeinterface 40 1 0
/*     */     //   8: ifeq +27 -> 35
/*     */     //   11: getstatic 2	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   14: ldc 95
/*     */     //   16: iconst_1
/*     */     //   17: anewarray 42	java/lang/Object
/*     */     //   20: dup
/*     */     //   21: iconst_0
/*     */     //   22: aload_0
/*     */     //   23: getfield 15	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:policy	Lorg/eclipse/jetty/websocket/api/WebSocketPolicy;
/*     */     //   26: invokevirtual 51	org/eclipse/jetty/websocket/api/WebSocketPolicy:getBehavior	()Lorg/eclipse/jetty/websocket/api/WebSocketBehavior;
/*     */     //   29: aastore
/*     */     //   30: invokeinterface 43 3 0
/*     */     //   35: aload_0
/*     */     //   36: getfield 10	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:stats	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$Stats;
/*     */     //   39: invokestatic 96	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$Stats:access$500	(Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$Stats;)Ljava/util/concurrent/atomic/AtomicLong;
/*     */     //   42: invokevirtual 12	java/util/concurrent/atomic/AtomicLong:incrementAndGet	()J
/*     */     //   45: pop2
/*     */     //   46: aload_0
/*     */     //   47: getfield 16	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:bufferPool	Lorg/eclipse/jetty/io/ByteBufferPool;
/*     */     //   50: aload_0
/*     */     //   51: invokevirtual 97	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:getInputBufferSize	()I
/*     */     //   54: iconst_1
/*     */     //   55: invokeinterface 98 3 0
/*     */     //   60: astore_1
/*     */     //   61: aload_0
/*     */     //   62: iconst_1
/*     */     //   63: putfield 64	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:isFilling	Z
/*     */     //   66: aload_0
/*     */     //   67: getfield 7	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:readMode	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   70: getstatic 6	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode:PARSE	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   73: if_acmpne +15 -> 88
/*     */     //   76: aload_0
/*     */     //   77: aload_0
/*     */     //   78: aload_1
/*     */     //   79: invokespecial 99	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:readParse	(Ljava/nio/ByteBuffer;)Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   82: putfield 7	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:readMode	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   85: goto +12 -> 97
/*     */     //   88: aload_0
/*     */     //   89: aload_0
/*     */     //   90: aload_1
/*     */     //   91: invokespecial 100	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:readDiscard	(Ljava/nio/ByteBuffer;)Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   94: putfield 7	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:readMode	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   97: aload_0
/*     */     //   98: getfield 16	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:bufferPool	Lorg/eclipse/jetty/io/ByteBufferPool;
/*     */     //   101: aload_1
/*     */     //   102: invokeinterface 101 2 0
/*     */     //   107: goto +16 -> 123
/*     */     //   110: astore_2
/*     */     //   111: aload_0
/*     */     //   112: getfield 16	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:bufferPool	Lorg/eclipse/jetty/io/ByteBufferPool;
/*     */     //   115: aload_1
/*     */     //   116: invokeinterface 101 2 0
/*     */     //   121: aload_2
/*     */     //   122: athrow
/*     */     //   123: aload_0
/*     */     //   124: getfield 7	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:readMode	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   127: getstatic 102	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode:EOF	Lorg/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection$ReadMode;
/*     */     //   130: if_acmpeq +20 -> 150
/*     */     //   133: aload_0
/*     */     //   134: getfield 28	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:suspendToken	Ljava/util/concurrent/atomic/AtomicBoolean;
/*     */     //   137: invokevirtual 103	java/util/concurrent/atomic/AtomicBoolean:get	()Z
/*     */     //   140: ifne +10 -> 150
/*     */     //   143: aload_0
/*     */     //   144: invokevirtual 78	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:fillInterested	()V
/*     */     //   147: goto +8 -> 155
/*     */     //   150: aload_0
/*     */     //   151: iconst_0
/*     */     //   152: putfield 64	org/eclipse/jetty/websocket/common/io/AbstractWebSocketConnection:isFilling	Z
/*     */     //   155: return
/*     */     // Line number table:
/*     */     //   Java source line #379	-> byte code offset #0
/*     */     //   Java source line #380	-> byte code offset #11
/*     */     //   Java source line #381	-> byte code offset #35
/*     */     //   Java source line #383	-> byte code offset #46
/*     */     //   Java source line #387	-> byte code offset #61
/*     */     //   Java source line #389	-> byte code offset #66
/*     */     //   Java source line #391	-> byte code offset #76
/*     */     //   Java source line #395	-> byte code offset #88
/*     */     //   Java source line #400	-> byte code offset #97
/*     */     //   Java source line #401	-> byte code offset #107
/*     */     //   Java source line #400	-> byte code offset #110
/*     */     //   Java source line #403	-> byte code offset #123
/*     */     //   Java source line #405	-> byte code offset #143
/*     */     //   Java source line #409	-> byte code offset #150
/*     */     //   Java source line #411	-> byte code offset #155
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	156	0	this	AbstractWebSocketConnection
/*     */     //   60	56	1	buffer	ByteBuffer
/*     */     //   110	12	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   61	97	110	finally
/*     */   }
/*     */   
/*     */   protected void onFillInterestedFailed(Throwable cause)
/*     */   {
/* 416 */     LOG.ignore(cause);
/* 417 */     this.stats.countFillInterestedEvents.incrementAndGet();
/* 418 */     super.onFillInterestedFailed(cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setInitialBuffer(ByteBuffer prefilled)
/*     */   {
/* 429 */     if (LOG.isDebugEnabled())
/*     */     {
/* 431 */       LOG.debug("set Initial Buffer - {}", new Object[] { BufferUtil.toDetailString(prefilled) });
/*     */     }
/* 433 */     this.prefillBuffer = prefilled;
/*     */   }
/*     */   
/*     */   private void notifyError(Throwable t)
/*     */   {
/* 438 */     getParser().getIncomingFramesHandler().incomingError(t);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onOpen()
/*     */   {
/* 444 */     if (LOG.isDebugEnabled())
/* 445 */       LOG.debug("[{}] {}.onOpened()", new Object[] { this.policy.getBehavior(), getClass().getSimpleName() });
/* 446 */     super.onOpen();
/* 447 */     this.ioState.onOpened();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean onReadTimeout(Throwable timeout)
/*     */   {
/* 457 */     IOState state = getIOState();
/* 458 */     ConnectionState cstate = state.getConnectionState();
/* 459 */     if (LOG.isDebugEnabled()) {
/* 460 */       LOG.debug("{} Read Timeout - {}", new Object[] { this.policy.getBehavior(), cstate });
/*     */     }
/* 462 */     if (cstate == ConnectionState.CLOSED)
/*     */     {
/* 464 */       if (LOG.isDebugEnabled()) {
/* 465 */         LOG.debug("onReadTimeout - Connection Already CLOSED", new Object[0]);
/*     */       }
/*     */       
/* 468 */       return true;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 473 */       notifyError(timeout);
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 478 */       this.session.close(1001, "Idle Timeout");
/*     */     }
/*     */     
/* 481 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/* 490 */     if (LOG.isDebugEnabled())
/*     */     {
/* 492 */       LOG.debug("outgoingFrame({}, {})", new Object[] { frame, callback });
/*     */     }
/*     */     
/* 495 */     this.flusher.enqueue(frame, callback, batchMode);
/*     */   }
/*     */   
/*     */   private ReadMode readDiscard(ByteBuffer buffer)
/*     */   {
/* 500 */     EndPoint endPoint = getEndPoint();
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 505 */         int filled = endPoint.fill(buffer);
/* 506 */         if (filled == 0)
/*     */         {
/* 508 */           return ReadMode.DISCARD;
/*     */         }
/* 510 */         if (filled < 0)
/*     */         {
/* 512 */           if (LOG.isDebugEnabled())
/* 513 */             LOG.debug("read - EOF Reached (remote: {})", new Object[] { getRemoteAddress() });
/* 514 */           return ReadMode.EOF;
/*     */         }
/*     */         
/*     */ 
/* 518 */         if (LOG.isDebugEnabled()) {
/* 519 */           LOG.debug("Discarded {} bytes - {}", new Object[] { Integer.valueOf(filled), BufferUtil.toDetailString(buffer) });
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 531 */       return ReadMode.DISCARD;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 525 */       LOG.ignore(e);
/* 526 */       return ReadMode.EOF;
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 530 */       LOG.ignore(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ReadMode readParse(ByteBuffer buffer)
/*     */   {
/* 537 */     EndPoint endPoint = getEndPoint();
/*     */     
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 543 */         int filled = endPoint.fill(buffer);
/* 544 */         if (filled < 0)
/*     */         {
/* 546 */           LOG.debug("read - EOF Reached (remote: {})", new Object[] { getRemoteAddress() });
/* 547 */           this.ioState.onReadFailure(new EOFException("Remote Read EOF"));
/* 548 */           return ReadMode.EOF;
/*     */         }
/* 550 */         if (filled == 0)
/*     */         {
/*     */ 
/* 553 */           return ReadMode.PARSE;
/*     */         }
/*     */         
/* 556 */         if (LOG.isDebugEnabled())
/*     */         {
/* 558 */           LOG.debug("Filled {} bytes - {}", new Object[] { Integer.valueOf(filled), BufferUtil.toDetailString(buffer) });
/*     */         }
/* 560 */         this.parser.parse(buffer);
/*     */       }
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
/* 582 */       return ReadMode.DISCARD;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 565 */       LOG.warn(e);
/* 566 */       this.session.notifyError(e);
/* 567 */       this.session.abort(1002, e.getMessage());
/* 568 */       return ReadMode.DISCARD;
/*     */     }
/*     */     catch (CloseException e)
/*     */     {
/* 572 */       LOG.debug(e);
/* 573 */       this.session.notifyError(e);
/* 574 */       this.session.close(e.getStatusCode(), e.getMessage());
/* 575 */       return ReadMode.DISCARD;
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 579 */       LOG.warn(t);
/* 580 */       this.session.abort(1006, t.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resume()
/*     */   {
/* 589 */     if (this.suspendToken.getAndSet(false))
/*     */     {
/* 591 */       if (!isReading())
/*     */       {
/* 593 */         fillInterested();
/*     */       }
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
/*     */   public void setExtensions(List<ExtensionConfig> extensions)
/*     */   {
/* 608 */     this.extensions = extensions;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setInputBufferSize(int inputBufferSize)
/*     */   {
/* 614 */     if (inputBufferSize < 28)
/*     */     {
/* 616 */       throw new IllegalArgumentException("Cannot have buffer size less than 28");
/*     */     }
/* 618 */     super.setInputBufferSize(inputBufferSize);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxIdleTimeout(long ms)
/*     */   {
/* 624 */     getEndPoint().setIdleTimeout(ms);
/*     */   }
/*     */   
/*     */ 
/*     */   public SuspendToken suspend()
/*     */   {
/* 630 */     this.suspendToken.set(true);
/* 631 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String dump()
/*     */   {
/* 637 */     return ContainerLifeCycle.dump(this);
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 643 */     out.append(toString()).append(System.lineSeparator());
/*     */   }
/*     */   
/*     */ 
/*     */   public String toConnectionString()
/*     */   {
/* 649 */     return String.format("%s@%x[ios=%s,f=%s,g=%s,p=%s]", new Object[] {
/* 650 */       getClass().getSimpleName(), 
/* 651 */       Integer.valueOf(hashCode()), this.ioState, this.flusher, this.generator, this.parser });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 658 */     int prime = 31;
/* 659 */     int result = 1;
/*     */     
/* 661 */     EndPoint endp = getEndPoint();
/* 662 */     if (endp != null)
/*     */     {
/* 664 */       result = 31 * result + endp.getLocalAddress().hashCode();
/* 665 */       result = 31 * result + endp.getRemoteAddress().hashCode();
/*     */     }
/* 667 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 673 */     if (this == obj)
/* 674 */       return true;
/* 675 */     if (obj == null)
/* 676 */       return false;
/* 677 */     if (getClass() != obj.getClass())
/* 678 */       return false;
/* 679 */     AbstractWebSocketConnection other = (AbstractWebSocketConnection)obj;
/* 680 */     EndPoint endp = getEndPoint();
/* 681 */     EndPoint otherEndp = other.getEndPoint();
/* 682 */     if (endp == null)
/*     */     {
/* 684 */       if (otherEndp != null) {
/* 685 */         return false;
/*     */       }
/* 687 */     } else if (!endp.equals(otherEndp))
/* 688 */       return false;
/* 689 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpgradeTo(ByteBuffer prefilled)
/*     */   {
/* 700 */     if (LOG.isDebugEnabled())
/*     */     {
/* 702 */       LOG.debug("onUpgradeTo({})", new Object[] { BufferUtil.toDetailString(prefilled) });
/*     */     }
/*     */     
/* 705 */     setInitialBuffer(prefilled);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\AbstractWebSocketConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */