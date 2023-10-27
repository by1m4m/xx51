/*      */ package io.netty.handler.ssl;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufUtil;
/*      */ import io.netty.buffer.CompositeByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.AbstractCoalescingBufferQueue;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelException;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.channel.ChannelOutboundHandler;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.ChannelPromiseNotifier;
/*      */ import io.netty.handler.codec.ByteToMessageDecoder;
/*      */ import io.netty.handler.codec.ByteToMessageDecoder.Cumulator;
/*      */ import io.netty.handler.codec.UnsupportedMessageTypeException;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.concurrent.DefaultPromise;
/*      */ import io.netty.util.concurrent.EventExecutor;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.FutureListener;
/*      */ import io.netty.util.concurrent.ImmediateExecutor;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.DatagramChannel;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*      */ import javax.net.ssl.SSLEngineResult.Status;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLSession;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SslHandler
/*      */   extends ByteToMessageDecoder
/*      */   implements ChannelOutboundHandler
/*      */ {
/*  168 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SslHandler.class);
/*      */   
/*  170 */   private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
/*      */   
/*  172 */   private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  180 */   private static final SSLException SSLENGINE_CLOSED = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("SSLEngine closed already"), SslHandler.class, "wrap(...)");
/*      */   
/*  182 */   private static final SSLException HANDSHAKE_TIMED_OUT = (SSLException)ThrowableUtil.unknownStackTrace(new SSLException("handshake timed out"), SslHandler.class, "handshake(...)");
/*      */   
/*  184 */   private static final ClosedChannelException CHANNEL_CLOSED = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), SslHandler.class, "channelInactive(...)");
/*      */   private static final int MAX_PLAINTEXT_LENGTH = 16384;
/*      */   private volatile ChannelHandlerContext ctx;
/*      */   private final SSLEngine engine;
/*      */   private final SslEngineType engineType;
/*      */   private final Executor delegatedTaskExecutor;
/*      */   private final boolean jdkCompatibilityMode;
/*      */   
/*      */   private static abstract enum SslEngineType
/*      */   {
/*  194 */     TCNATIVE(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR), 
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
/*  245 */     CONSCRYPT(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR), 
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
/*  287 */     JDK(false, ByteToMessageDecoder.MERGE_CUMULATOR);
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
/*      */     final boolean wantsDirectBuffer;
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
/*      */     final ByteToMessageDecoder.Cumulator cumulator;
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
/*      */     static SslEngineType forEngine(SSLEngine engine)
/*      */     {
/*  332 */       return (engine instanceof ConscryptAlpnSslEngine) ? CONSCRYPT : (engine instanceof ReferenceCountedOpenSslEngine) ? TCNATIVE : JDK;
/*      */     }
/*      */     
/*      */     private SslEngineType(boolean wantsDirectBuffer, ByteToMessageDecoder.Cumulator cumulator)
/*      */     {
/*  337 */       this.wantsDirectBuffer = wantsDirectBuffer;
/*  338 */       this.cumulator = cumulator;
/*      */     }
/*      */     
/*      */     int getPacketBufferSize(SslHandler handler) {
/*  342 */       return handler.engine.getSession().getPacketBufferSize();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract SSLEngineResult unwrap(SslHandler paramSslHandler, ByteBuf paramByteBuf1, int paramInt1, int paramInt2, ByteBuf paramByteBuf2)
/*      */       throws SSLException;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract int calculateWrapBufferCapacity(SslHandler paramSslHandler, int paramInt1, int paramInt2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract int calculatePendingData(SslHandler paramSslHandler, int paramInt);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract boolean jdkCompatibilityMode(SSLEngine paramSSLEngine);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  385 */   private final ByteBuffer[] singleBuffer = new ByteBuffer[1];
/*      */   
/*      */   private final boolean startTls;
/*      */   private boolean sentFirstMessage;
/*      */   private boolean flushedBeforeHandshake;
/*      */   private boolean readDuringHandshake;
/*      */   private boolean handshakeStarted;
/*      */   private SslHandlerCoalescingBufferQueue pendingUnencryptedWrites;
/*  393 */   private Promise<Channel> handshakePromise = new LazyChannelPromise(null);
/*  394 */   private final LazyChannelPromise sslClosePromise = new LazyChannelPromise(null);
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean needsFlush;
/*      */   
/*      */ 
/*      */   private boolean outboundClosed;
/*      */   
/*      */ 
/*      */   private boolean closeNotify;
/*      */   
/*      */ 
/*      */   private int packetLength;
/*      */   
/*      */ 
/*      */   private boolean firedChannelRead;
/*      */   
/*      */ 
/*  413 */   private volatile long handshakeTimeoutMillis = 10000L;
/*  414 */   private volatile long closeNotifyFlushTimeoutMillis = 3000L;
/*      */   private volatile long closeNotifyReadTimeoutMillis;
/*  416 */   volatile int wrapDataSize = 16384;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslHandler(SSLEngine engine)
/*      */   {
/*  424 */     this(engine, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslHandler(SSLEngine engine, boolean startTls)
/*      */   {
/*  436 */     this(engine, startTls, ImmediateExecutor.INSTANCE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor)
/*      */   {
/*  444 */     this(engine, false, delegatedTaskExecutor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor)
/*      */   {
/*  452 */     if (engine == null) {
/*  453 */       throw new NullPointerException("engine");
/*      */     }
/*  455 */     if (delegatedTaskExecutor == null) {
/*  456 */       throw new NullPointerException("delegatedTaskExecutor");
/*      */     }
/*  458 */     this.engine = engine;
/*  459 */     this.engineType = SslEngineType.forEngine(engine);
/*  460 */     this.delegatedTaskExecutor = delegatedTaskExecutor;
/*  461 */     this.startTls = startTls;
/*  462 */     this.jdkCompatibilityMode = this.engineType.jdkCompatibilityMode(engine);
/*  463 */     setCumulator(this.engineType.cumulator);
/*      */   }
/*      */   
/*      */   public long getHandshakeTimeoutMillis() {
/*  467 */     return this.handshakeTimeoutMillis;
/*      */   }
/*      */   
/*      */   public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
/*  471 */     if (unit == null) {
/*  472 */       throw new NullPointerException("unit");
/*      */     }
/*      */     
/*  475 */     setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
/*      */   }
/*      */   
/*      */   public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
/*  479 */     if (handshakeTimeoutMillis < 0L) {
/*  480 */       throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  483 */     this.handshakeTimeoutMillis = handshakeTimeoutMillis;
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
/*      */   public final void setWrapDataSize(int wrapDataSize)
/*      */   {
/*  508 */     this.wrapDataSize = wrapDataSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public long getCloseNotifyTimeoutMillis()
/*      */   {
/*  516 */     return getCloseNotifyFlushTimeoutMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit)
/*      */   {
/*  524 */     setCloseNotifyFlushTimeout(closeNotifyTimeout, unit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setCloseNotifyTimeoutMillis(long closeNotifyFlushTimeoutMillis)
/*      */   {
/*  532 */     setCloseNotifyFlushTimeoutMillis(closeNotifyFlushTimeoutMillis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final long getCloseNotifyFlushTimeoutMillis()
/*      */   {
/*  541 */     return this.closeNotifyFlushTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setCloseNotifyFlushTimeout(long closeNotifyFlushTimeout, TimeUnit unit)
/*      */   {
/*  550 */     setCloseNotifyFlushTimeoutMillis(unit.toMillis(closeNotifyFlushTimeout));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final void setCloseNotifyFlushTimeoutMillis(long closeNotifyFlushTimeoutMillis)
/*      */   {
/*  557 */     if (closeNotifyFlushTimeoutMillis < 0L) {
/*  558 */       throw new IllegalArgumentException("closeNotifyFlushTimeoutMillis: " + closeNotifyFlushTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  561 */     this.closeNotifyFlushTimeoutMillis = closeNotifyFlushTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final long getCloseNotifyReadTimeoutMillis()
/*      */   {
/*  570 */     return this.closeNotifyReadTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setCloseNotifyReadTimeout(long closeNotifyReadTimeout, TimeUnit unit)
/*      */   {
/*  579 */     setCloseNotifyReadTimeoutMillis(unit.toMillis(closeNotifyReadTimeout));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final void setCloseNotifyReadTimeoutMillis(long closeNotifyReadTimeoutMillis)
/*      */   {
/*  586 */     if (closeNotifyReadTimeoutMillis < 0L) {
/*  587 */       throw new IllegalArgumentException("closeNotifyReadTimeoutMillis: " + closeNotifyReadTimeoutMillis + " (expected: >= 0)");
/*      */     }
/*      */     
/*  590 */     this.closeNotifyReadTimeoutMillis = closeNotifyReadTimeoutMillis;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SSLEngine engine()
/*      */   {
/*  597 */     return this.engine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String applicationProtocol()
/*      */   {
/*  606 */     SSLEngine engine = engine();
/*  607 */     if (!(engine instanceof ApplicationProtocolAccessor)) {
/*  608 */       return null;
/*      */     }
/*      */     
/*  611 */     return ((ApplicationProtocolAccessor)engine).getNegotiatedApplicationProtocol();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<Channel> handshakeFuture()
/*      */   {
/*  621 */     return this.handshakePromise;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ChannelFuture close()
/*      */   {
/*  629 */     return closeOutbound();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ChannelFuture close(ChannelPromise promise)
/*      */   {
/*  637 */     return closeOutbound(promise);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChannelFuture closeOutbound()
/*      */   {
/*  647 */     return closeOutbound(this.ctx.newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChannelFuture closeOutbound(final ChannelPromise promise)
/*      */   {
/*  657 */     ChannelHandlerContext ctx = this.ctx;
/*  658 */     if (ctx.executor().inEventLoop()) {
/*  659 */       closeOutbound0(promise);
/*      */     } else {
/*  661 */       ctx.executor().execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  664 */           SslHandler.this.closeOutbound0(promise);
/*      */         }
/*      */       });
/*      */     }
/*  668 */     return promise;
/*      */   }
/*      */   
/*      */   private void closeOutbound0(ChannelPromise promise) {
/*  672 */     this.outboundClosed = true;
/*  673 */     this.engine.closeOutbound();
/*      */     try {
/*  675 */       flush(this.ctx, promise);
/*      */     } catch (Exception e) {
/*  677 */       if (!promise.tryFailure(e)) {
/*  678 */         logger.warn("{} flush() raised a masked exception.", this.ctx.channel(), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<Channel> sslCloseFuture()
/*      */   {
/*  691 */     return this.sslClosePromise;
/*      */   }
/*      */   
/*      */   public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  696 */     if (!this.pendingUnencryptedWrites.isEmpty())
/*      */     {
/*  698 */       this.pendingUnencryptedWrites.releaseAndFailAll(ctx, new ChannelException("Pending write on removal of SslHandler"));
/*      */     }
/*      */     
/*  701 */     this.pendingUnencryptedWrites = null;
/*  702 */     if ((this.engine instanceof ReferenceCounted)) {
/*  703 */       ((ReferenceCounted)this.engine).release();
/*      */     }
/*      */   }
/*      */   
/*      */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception
/*      */   {
/*  709 */     ctx.bind(localAddress, promise);
/*      */   }
/*      */   
/*      */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */     throws Exception
/*      */   {
/*  715 */     ctx.connect(remoteAddress, localAddress, promise);
/*      */   }
/*      */   
/*      */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*      */   {
/*  720 */     ctx.deregister(promise);
/*      */   }
/*      */   
/*      */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
/*      */     throws Exception
/*      */   {
/*  726 */     closeOutboundAndChannel(ctx, promise, true);
/*      */   }
/*      */   
/*      */   public void close(ChannelHandlerContext ctx, ChannelPromise promise)
/*      */     throws Exception
/*      */   {
/*  732 */     closeOutboundAndChannel(ctx, promise, false);
/*      */   }
/*      */   
/*      */   public void read(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  737 */     if (!this.handshakePromise.isDone()) {
/*  738 */       this.readDuringHandshake = true;
/*      */     }
/*      */     
/*  741 */     ctx.read();
/*      */   }
/*      */   
/*      */   private static IllegalStateException newPendingWritesNullException() {
/*  745 */     return new IllegalStateException("pendingUnencryptedWrites is null, handlerRemoved0 called?");
/*      */   }
/*      */   
/*      */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*      */   {
/*  750 */     if (!(msg instanceof ByteBuf)) {
/*  751 */       UnsupportedMessageTypeException exception = new UnsupportedMessageTypeException(msg, new Class[] { ByteBuf.class });
/*  752 */       ReferenceCountUtil.safeRelease(msg);
/*  753 */       promise.setFailure(exception);
/*  754 */     } else if (this.pendingUnencryptedWrites == null) {
/*  755 */       ReferenceCountUtil.safeRelease(msg);
/*  756 */       promise.setFailure(newPendingWritesNullException());
/*      */     } else {
/*  758 */       this.pendingUnencryptedWrites.add((ByteBuf)msg, promise);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void flush(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/*  766 */     if ((this.startTls) && (!this.sentFirstMessage)) {
/*  767 */       this.sentFirstMessage = true;
/*  768 */       this.pendingUnencryptedWrites.writeAndRemoveAll(ctx);
/*  769 */       forceFlush(ctx);
/*  770 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  774 */       wrapAndFlush(ctx);
/*      */     } catch (Throwable cause) {
/*  776 */       setHandshakeFailure(ctx, cause);
/*  777 */       PlatformDependent.throwException(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private void wrapAndFlush(ChannelHandlerContext ctx)
/*      */     throws SSLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 79	io/netty/handler/ssl/SslHandler:pendingUnencryptedWrites	Lio/netty/handler/ssl/SslHandler$SslHandlerCoalescingBufferQueue;
/*      */     //   4: invokevirtual 80	io/netty/handler/ssl/SslHandler$SslHandlerCoalescingBufferQueue:isEmpty	()Z
/*      */     //   7: ifeq +19 -> 26
/*      */     //   10: aload_0
/*      */     //   11: getfield 79	io/netty/handler/ssl/SslHandler:pendingUnencryptedWrites	Lio/netty/handler/ssl/SslHandler$SslHandlerCoalescingBufferQueue;
/*      */     //   14: getstatic 112	io/netty/buffer/Unpooled:EMPTY_BUFFER	Lio/netty/buffer/ByteBuf;
/*      */     //   17: aload_1
/*      */     //   18: invokeinterface 65 1 0
/*      */     //   23: invokevirtual 104	io/netty/handler/ssl/SslHandler$SslHandlerCoalescingBufferQueue:add	(Lio/netty/buffer/ByteBuf;Lio/netty/channel/ChannelPromise;)V
/*      */     //   26: aload_0
/*      */     //   27: getfield 7	io/netty/handler/ssl/SslHandler:handshakePromise	Lio/netty/util/concurrent/Promise;
/*      */     //   30: invokeinterface 91 1 0
/*      */     //   35: ifne +8 -> 43
/*      */     //   38: aload_0
/*      */     //   39: iconst_1
/*      */     //   40: putfield 113	io/netty/handler/ssl/SslHandler:flushedBeforeHandshake	Z
/*      */     //   43: aload_0
/*      */     //   44: aload_1
/*      */     //   45: iconst_0
/*      */     //   46: invokespecial 114	io/netty/handler/ssl/SslHandler:wrap	(Lio/netty/channel/ChannelHandlerContext;Z)V
/*      */     //   49: aload_0
/*      */     //   50: aload_1
/*      */     //   51: invokespecial 107	io/netty/handler/ssl/SslHandler:forceFlush	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */     //   54: goto +11 -> 65
/*      */     //   57: astore_2
/*      */     //   58: aload_0
/*      */     //   59: aload_1
/*      */     //   60: invokespecial 107	io/netty/handler/ssl/SslHandler:forceFlush	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */     //   63: aload_2
/*      */     //   64: athrow
/*      */     //   65: return
/*      */     // Line number table:
/*      */     //   Java source line #782	-> byte code offset #0
/*      */     //   Java source line #787	-> byte code offset #10
/*      */     //   Java source line #789	-> byte code offset #26
/*      */     //   Java source line #790	-> byte code offset #38
/*      */     //   Java source line #793	-> byte code offset #43
/*      */     //   Java source line #797	-> byte code offset #49
/*      */     //   Java source line #798	-> byte code offset #54
/*      */     //   Java source line #797	-> byte code offset #57
/*      */     //   Java source line #798	-> byte code offset #63
/*      */     //   Java source line #799	-> byte code offset #65
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	66	0	this	SslHandler
/*      */     //   0	66	1	ctx	ChannelHandlerContext
/*      */     //   57	7	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   43	49	57	finally
/*      */   }
/*      */   
/*      */   private void wrap(ChannelHandlerContext ctx, boolean inUnwrap)
/*      */     throws SSLException
/*      */   {
/*  803 */     ByteBuf out = null;
/*  804 */     ChannelPromise promise = null;
/*  805 */     ByteBufAllocator alloc = ctx.alloc();
/*  806 */     boolean needUnwrap = false;
/*  807 */     ByteBuf buf = null;
/*      */     try {
/*  809 */       int wrapDataSize = this.wrapDataSize;
/*      */       
/*      */ 
/*  812 */       while (!ctx.isRemoved()) {
/*  813 */         promise = ctx.newPromise();
/*      */         
/*      */ 
/*  816 */         buf = wrapDataSize > 0 ? this.pendingUnencryptedWrites.remove(alloc, wrapDataSize, promise) : this.pendingUnencryptedWrites.removeFirst(promise);
/*  817 */         if (buf == null) {
/*      */           break;
/*      */         }
/*      */         
/*  821 */         if (out == null) {
/*  822 */           out = allocateOutNetBuf(ctx, buf.readableBytes(), buf.nioBufferCount());
/*      */         }
/*      */         
/*  825 */         SSLEngineResult result = wrap(alloc, this.engine, buf, out);
/*      */         
/*  827 */         if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
/*  828 */           buf.release();
/*  829 */           buf = null;
/*  830 */           promise.tryFailure(SSLENGINE_CLOSED);
/*  831 */           promise = null;
/*      */           
/*      */ 
/*  834 */           this.pendingUnencryptedWrites.releaseAndFailAll(ctx, SSLENGINE_CLOSED);
/*  835 */           return;
/*      */         }
/*  837 */         if (buf.isReadable()) {
/*  838 */           this.pendingUnencryptedWrites.addFirst(buf, promise);
/*      */           
/*      */ 
/*  841 */           promise = null;
/*      */         } else {
/*  843 */           buf.release();
/*      */         }
/*  845 */         buf = null;
/*      */         
/*  847 */         switch (result.getHandshakeStatus()) {
/*      */         case NEED_TASK: 
/*  849 */           runDelegatedTasks();
/*  850 */           break;
/*      */         case FINISHED: 
/*  852 */           setHandshakeSuccess();
/*      */         
/*      */         case NOT_HANDSHAKING: 
/*  855 */           setHandshakeSuccessIfStillHandshaking();
/*      */         
/*      */         case NEED_WRAP: 
/*  858 */           finishWrap(ctx, out, promise, inUnwrap, false);
/*  859 */           promise = null;
/*  860 */           out = null;
/*  861 */           break;
/*      */         case NEED_UNWRAP: 
/*  863 */           needUnwrap = true;
/*  864 */           return;
/*      */         
/*      */         default: 
/*  867 */           throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
/*      */         }
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*  873 */       if (buf != null) {
/*  874 */         buf.release();
/*      */       }
/*  876 */       finishWrap(ctx, out, promise, inUnwrap, needUnwrap);
/*      */     }
/*      */   }
/*      */   
/*      */   private void finishWrap(ChannelHandlerContext ctx, ByteBuf out, ChannelPromise promise, boolean inUnwrap, boolean needUnwrap)
/*      */   {
/*  882 */     if (out == null) {
/*  883 */       out = Unpooled.EMPTY_BUFFER;
/*  884 */     } else if (!out.isReadable()) {
/*  885 */       out.release();
/*  886 */       out = Unpooled.EMPTY_BUFFER;
/*      */     }
/*      */     
/*  889 */     if (promise != null) {
/*  890 */       ctx.write(out, promise);
/*      */     } else {
/*  892 */       ctx.write(out);
/*      */     }
/*      */     
/*  895 */     if (inUnwrap) {
/*  896 */       this.needsFlush = true;
/*      */     }
/*      */     
/*  899 */     if (needUnwrap)
/*      */     {
/*      */ 
/*  902 */       readIfNeeded(ctx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean wrapNonAppData(ChannelHandlerContext ctx, boolean inUnwrap)
/*      */     throws SSLException
/*      */   {
/*  913 */     ByteBuf out = null;
/*  914 */     ByteBufAllocator alloc = ctx.alloc();
/*      */     
/*      */     try
/*      */     {
/*  918 */       while (!ctx.isRemoved()) {
/*  919 */         if (out == null)
/*      */         {
/*      */ 
/*      */ 
/*  923 */           out = allocateOutNetBuf(ctx, 2048, 1);
/*      */         }
/*  925 */         SSLEngineResult result = wrap(alloc, this.engine, Unpooled.EMPTY_BUFFER, out);
/*      */         
/*  927 */         if (result.bytesProduced() > 0) {
/*  928 */           ctx.write(out);
/*  929 */           if (inUnwrap) {
/*  930 */             this.needsFlush = true;
/*      */           }
/*  932 */           out = null;
/*      */         }
/*      */         boolean bool;
/*  935 */         switch (result.getHandshakeStatus()) {
/*      */         case FINISHED: 
/*  937 */           setHandshakeSuccess();
/*  938 */           return false;
/*      */         case NEED_TASK: 
/*  940 */           runDelegatedTasks();
/*  941 */           break;
/*      */         case NEED_UNWRAP: 
/*  943 */           if (inUnwrap)
/*      */           {
/*      */ 
/*      */ 
/*  947 */             return false;
/*      */           }
/*      */           
/*  950 */           unwrapNonAppData(ctx);
/*  951 */           break;
/*      */         case NEED_WRAP: 
/*      */           break;
/*      */         case NOT_HANDSHAKING: 
/*  955 */           setHandshakeSuccessIfStillHandshaking();
/*      */           
/*      */ 
/*  958 */           if (!inUnwrap) {
/*  959 */             unwrapNonAppData(ctx);
/*      */           }
/*  961 */           return true;
/*      */         default: 
/*  963 */           throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
/*      */         }
/*      */         
/*  966 */         if (result.bytesProduced() == 0) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  972 */         if ((result.bytesConsumed() == 0) && (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     } finally {
/*  977 */       if (out != null) {
/*  978 */         out.release();
/*      */       }
/*      */     }
/*  981 */     return false;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private SSLEngineResult wrap(ByteBufAllocator alloc, SSLEngine engine, ByteBuf in, ByteBuf out)
/*      */     throws SSLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore 5
/*      */     //   3: aload_3
/*      */     //   4: invokevirtual 146	io/netty/buffer/ByteBuf:readerIndex	()I
/*      */     //   7: istore 6
/*      */     //   9: aload_3
/*      */     //   10: invokevirtual 119	io/netty/buffer/ByteBuf:readableBytes	()I
/*      */     //   13: istore 7
/*      */     //   15: aload_3
/*      */     //   16: invokevirtual 147	io/netty/buffer/ByteBuf:isDirect	()Z
/*      */     //   19: ifne +13 -> 32
/*      */     //   22: aload_0
/*      */     //   23: getfield 35	io/netty/handler/ssl/SslHandler:engineType	Lio/netty/handler/ssl/SslHandler$SslEngineType;
/*      */     //   26: getfield 148	io/netty/handler/ssl/SslHandler$SslEngineType:wantsDirectBuffer	Z
/*      */     //   29: ifne +48 -> 77
/*      */     //   32: aload_3
/*      */     //   33: instanceof 149
/*      */     //   36: ifne +32 -> 68
/*      */     //   39: aload_3
/*      */     //   40: invokevirtual 120	io/netty/buffer/ByteBuf:nioBufferCount	()I
/*      */     //   43: iconst_1
/*      */     //   44: if_icmpne +24 -> 68
/*      */     //   47: aload_0
/*      */     //   48: getfield 13	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   51: astore 8
/*      */     //   53: aload 8
/*      */     //   55: iconst_0
/*      */     //   56: aload_3
/*      */     //   57: iload 6
/*      */     //   59: iload 7
/*      */     //   61: invokevirtual 150	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   64: aastore
/*      */     //   65: goto +55 -> 120
/*      */     //   68: aload_3
/*      */     //   69: invokevirtual 151	io/netty/buffer/ByteBuf:nioBuffers	()[Ljava/nio/ByteBuffer;
/*      */     //   72: astore 8
/*      */     //   74: goto +46 -> 120
/*      */     //   77: aload_1
/*      */     //   78: iload 7
/*      */     //   80: invokeinterface 152 2 0
/*      */     //   85: astore 5
/*      */     //   87: aload 5
/*      */     //   89: aload_3
/*      */     //   90: iload 6
/*      */     //   92: iload 7
/*      */     //   94: invokevirtual 153	io/netty/buffer/ByteBuf:writeBytes	(Lio/netty/buffer/ByteBuf;II)Lio/netty/buffer/ByteBuf;
/*      */     //   97: pop
/*      */     //   98: aload_0
/*      */     //   99: getfield 13	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   102: astore 8
/*      */     //   104: aload 8
/*      */     //   106: iconst_0
/*      */     //   107: aload 5
/*      */     //   109: aload 5
/*      */     //   111: invokevirtual 146	io/netty/buffer/ByteBuf:readerIndex	()I
/*      */     //   114: iload 7
/*      */     //   116: invokevirtual 150	io/netty/buffer/ByteBuf:internalNioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   119: aastore
/*      */     //   120: aload 4
/*      */     //   122: aload 4
/*      */     //   124: invokevirtual 154	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   127: aload 4
/*      */     //   129: invokevirtual 155	io/netty/buffer/ByteBuf:writableBytes	()I
/*      */     //   132: invokevirtual 156	io/netty/buffer/ByteBuf:nioBuffer	(II)Ljava/nio/ByteBuffer;
/*      */     //   135: astore 9
/*      */     //   137: aload_2
/*      */     //   138: aload 8
/*      */     //   140: aload 9
/*      */     //   142: invokevirtual 157	javax/net/ssl/SSLEngine:wrap	([Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljavax/net/ssl/SSLEngineResult;
/*      */     //   145: astore 10
/*      */     //   147: aload_3
/*      */     //   148: aload 10
/*      */     //   150: invokevirtual 144	javax/net/ssl/SSLEngineResult:bytesConsumed	()I
/*      */     //   153: invokevirtual 158	io/netty/buffer/ByteBuf:skipBytes	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   156: pop
/*      */     //   157: aload 4
/*      */     //   159: aload 4
/*      */     //   161: invokevirtual 154	io/netty/buffer/ByteBuf:writerIndex	()I
/*      */     //   164: aload 10
/*      */     //   166: invokevirtual 142	javax/net/ssl/SSLEngineResult:bytesProduced	()I
/*      */     //   169: iadd
/*      */     //   170: invokevirtual 159	io/netty/buffer/ByteBuf:writerIndex	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   173: pop
/*      */     //   174: getstatic 160	io/netty/handler/ssl/SslHandler$10:$SwitchMap$javax$net$ssl$SSLEngineResult$Status	[I
/*      */     //   177: aload 10
/*      */     //   179: invokevirtual 123	javax/net/ssl/SSLEngineResult:getStatus	()Ljavax/net/ssl/SSLEngineResult$Status;
/*      */     //   182: invokevirtual 161	javax/net/ssl/SSLEngineResult$Status:ordinal	()I
/*      */     //   185: iaload
/*      */     //   186: lookupswitch	default:+36->222, 1:+18->204
/*      */     //   204: aload 4
/*      */     //   206: aload_2
/*      */     //   207: invokevirtual 162	javax/net/ssl/SSLEngine:getSession	()Ljavax/net/ssl/SSLSession;
/*      */     //   210: invokeinterface 163 1 0
/*      */     //   215: invokevirtual 164	io/netty/buffer/ByteBuf:ensureWritable	(I)Lio/netty/buffer/ByteBuf;
/*      */     //   218: pop
/*      */     //   219: goto +28 -> 247
/*      */     //   222: aload 10
/*      */     //   224: astore 11
/*      */     //   226: aload_0
/*      */     //   227: getfield 13	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   230: iconst_0
/*      */     //   231: aconst_null
/*      */     //   232: aastore
/*      */     //   233: aload 5
/*      */     //   235: ifnull +9 -> 244
/*      */     //   238: aload 5
/*      */     //   240: invokevirtual 125	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   243: pop
/*      */     //   244: aload 11
/*      */     //   246: areturn
/*      */     //   247: goto -127 -> 120
/*      */     //   250: astore 12
/*      */     //   252: aload_0
/*      */     //   253: getfield 13	io/netty/handler/ssl/SslHandler:singleBuffer	[Ljava/nio/ByteBuffer;
/*      */     //   256: iconst_0
/*      */     //   257: aconst_null
/*      */     //   258: aastore
/*      */     //   259: aload 5
/*      */     //   261: ifnull +9 -> 270
/*      */     //   264: aload 5
/*      */     //   266: invokevirtual 125	io/netty/buffer/ByteBuf:release	()Z
/*      */     //   269: pop
/*      */     //   270: aload 12
/*      */     //   272: athrow
/*      */     // Line number table:
/*      */     //   Java source line #986	-> byte code offset #0
/*      */     //   Java source line #988	-> byte code offset #3
/*      */     //   Java source line #989	-> byte code offset #9
/*      */     //   Java source line #994	-> byte code offset #15
/*      */     //   Java source line #999	-> byte code offset #32
/*      */     //   Java source line #1000	-> byte code offset #47
/*      */     //   Java source line #1003	-> byte code offset #53
/*      */     //   Java source line #1005	-> byte code offset #68
/*      */     //   Java source line #1011	-> byte code offset #77
/*      */     //   Java source line #1012	-> byte code offset #87
/*      */     //   Java source line #1013	-> byte code offset #98
/*      */     //   Java source line #1014	-> byte code offset #104
/*      */     //   Java source line #1018	-> byte code offset #120
/*      */     //   Java source line #1019	-> byte code offset #137
/*      */     //   Java source line #1020	-> byte code offset #147
/*      */     //   Java source line #1021	-> byte code offset #157
/*      */     //   Java source line #1023	-> byte code offset #174
/*      */     //   Java source line #1025	-> byte code offset #204
/*      */     //   Java source line #1026	-> byte code offset #219
/*      */     //   Java source line #1028	-> byte code offset #222
/*      */     //   Java source line #1033	-> byte code offset #226
/*      */     //   Java source line #1035	-> byte code offset #233
/*      */     //   Java source line #1036	-> byte code offset #238
/*      */     //   Java source line #1028	-> byte code offset #244
/*      */     //   Java source line #1030	-> byte code offset #247
/*      */     //   Java source line #1033	-> byte code offset #250
/*      */     //   Java source line #1035	-> byte code offset #259
/*      */     //   Java source line #1036	-> byte code offset #264
/*      */     //   Java source line #1038	-> byte code offset #270
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	273	0	this	SslHandler
/*      */     //   0	273	1	alloc	ByteBufAllocator
/*      */     //   0	273	2	engine	SSLEngine
/*      */     //   0	273	3	in	ByteBuf
/*      */     //   0	273	4	out	ByteBuf
/*      */     //   1	264	5	newDirectIn	ByteBuf
/*      */     //   7	84	6	readerIndex	int
/*      */     //   13	102	7	readableBytes	int
/*      */     //   51	3	8	in0	ByteBuffer[]
/*      */     //   72	3	8	in0	ByteBuffer[]
/*      */     //   102	37	8	in0	ByteBuffer[]
/*      */     //   135	6	9	out0	ByteBuffer
/*      */     //   145	78	10	result	SSLEngineResult
/*      */     //   224	21	11	localSSLEngineResult1	SSLEngineResult
/*      */     //   250	21	12	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   3	226	250	finally
/*      */     //   247	252	250	finally
/*      */   }
/*      */   
/*      */   public void channelInactive(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/* 1045 */     setHandshakeFailure(ctx, CHANNEL_CLOSED, !this.outboundClosed, this.handshakeStarted, false);
/*      */     
/*      */ 
/* 1048 */     notifyClosePromise(CHANNEL_CLOSED);
/*      */     
/* 1050 */     super.channelInactive(ctx);
/*      */   }
/*      */   
/*      */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*      */   {
/* 1055 */     if (ignoreException(cause))
/*      */     {
/*      */ 
/* 1058 */       if (logger.isDebugEnabled()) {
/* 1059 */         logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx
/*      */         
/* 1061 */           .channel(), cause);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1066 */       if (ctx.channel().isActive()) {
/* 1067 */         ctx.close();
/*      */       }
/*      */     } else {
/* 1070 */       ctx.fireExceptionCaught(cause);
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
/*      */ 
/*      */   private boolean ignoreException(Throwable t)
/*      */   {
/* 1084 */     if ((!(t instanceof SSLException)) && ((t instanceof IOException)) && (this.sslClosePromise.isDone())) {
/* 1085 */       String message = t.getMessage();
/*      */       
/*      */ 
/*      */ 
/* 1089 */       if ((message != null) && (IGNORABLE_ERROR_MESSAGE.matcher(message).matches())) {
/* 1090 */         return true;
/*      */       }
/*      */       
/*      */ 
/* 1094 */       StackTraceElement[] elements = t.getStackTrace();
/* 1095 */       for (StackTraceElement element : elements) {
/* 1096 */         String classname = element.getClassName();
/* 1097 */         String methodname = element.getMethodName();
/*      */         
/*      */ 
/* 1100 */         if (!classname.startsWith("io.netty."))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1105 */           if ("read".equals(methodname))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */             if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
/* 1112 */               return true;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 1119 */               Class<?> clazz = PlatformDependent.getClassLoader(getClass()).loadClass(classname);
/*      */               
/* 1121 */               if ((SocketChannel.class.isAssignableFrom(clazz)) || 
/* 1122 */                 (DatagramChannel.class.isAssignableFrom(clazz))) {
/* 1123 */                 return true;
/*      */               }
/*      */               
/*      */ 
/* 1127 */               if ((PlatformDependent.javaVersion() >= 7) && 
/* 1128 */                 ("com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName()))) {
/* 1129 */                 return true;
/*      */               }
/*      */             } catch (Throwable cause) {
/* 1132 */               logger.debug("Unexpected exception while loading class {} classname {}", new Object[] {
/* 1133 */                 getClass(), classname, cause });
/*      */             }
/*      */           } }
/*      */       }
/*      */     }
/* 1138 */     return false;
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
/*      */   public static boolean isEncrypted(ByteBuf buffer)
/*      */   {
/* 1154 */     if (buffer.readableBytes() < 5) {
/* 1155 */       throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
/*      */     }
/*      */     
/* 1158 */     return SslUtils.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -2;
/*      */   }
/*      */   
/*      */   private void decodeJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) throws NotSslRecordException {
/* 1162 */     int packetLength = this.packetLength;
/*      */     
/* 1164 */     if (packetLength > 0) {
/* 1165 */       if (in.readableBytes() >= packetLength) {}
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1170 */       int readableBytes = in.readableBytes();
/* 1171 */       if (readableBytes < 5) {
/* 1172 */         return;
/*      */       }
/* 1174 */       packetLength = SslUtils.getEncryptedPacketLength(in, in.readerIndex());
/* 1175 */       if (packetLength == -2)
/*      */       {
/*      */ 
/* 1178 */         NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
/* 1179 */         in.skipBytes(in.readableBytes());
/*      */         
/*      */ 
/*      */ 
/* 1183 */         setHandshakeFailure(ctx, e);
/*      */         
/* 1185 */         throw e;
/*      */       }
/* 1187 */       assert (packetLength > 0);
/* 1188 */       if (packetLength > readableBytes)
/*      */       {
/* 1190 */         this.packetLength = packetLength;
/* 1191 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1197 */     this.packetLength = 0;
/*      */     try {
/* 1199 */       int bytesConsumed = unwrap(ctx, in, in.readerIndex(), packetLength);
/* 1200 */       assert ((bytesConsumed == packetLength) || (this.engine.isInboundDone())) : ("we feed the SSLEngine a packets worth of data: " + packetLength + " but it only consumed: " + bytesConsumed);
/*      */       
/*      */ 
/* 1203 */       in.skipBytes(bytesConsumed);
/*      */     } catch (Throwable cause) {
/* 1205 */       handleUnwrapThrowable(ctx, cause);
/*      */     }
/*      */   }
/*      */   
/*      */   private void decodeNonJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) {
/*      */     try {
/* 1211 */       in.skipBytes(unwrap(ctx, in, in.readerIndex(), in.readableBytes()));
/*      */     } catch (Throwable cause) {
/* 1213 */       handleUnwrapThrowable(ctx, cause);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void handleUnwrapThrowable(ChannelHandlerContext ctx, Throwable cause)
/*      */   {
/*      */     try
/*      */     {
/* 1223 */       if (this.handshakePromise.tryFailure(cause)) {
/* 1224 */         ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1229 */       wrapAndFlush(ctx);
/*      */     } catch (SSLException ex) {
/* 1231 */       logger.debug("SSLException during trying to call SSLEngine.wrap(...) because of an previous SSLException, ignoring...", ex);
/*      */     }
/*      */     finally
/*      */     {
/* 1235 */       setHandshakeFailure(ctx, cause, true, false, true);
/*      */     }
/* 1237 */     PlatformDependent.throwException(cause);
/*      */   }
/*      */   
/*      */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException
/*      */   {
/* 1242 */     if (this.jdkCompatibilityMode) {
/* 1243 */       decodeJdkCompatible(ctx, in);
/*      */     } else {
/* 1245 */       decodeNonJdkCompatible(ctx, in);
/*      */     }
/*      */   }
/*      */   
/*      */   public void channelReadComplete(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/* 1252 */     discardSomeReadBytes();
/*      */     
/* 1254 */     flushIfNeeded(ctx);
/* 1255 */     readIfNeeded(ctx);
/*      */     
/* 1257 */     this.firedChannelRead = false;
/* 1258 */     ctx.fireChannelReadComplete();
/*      */   }
/*      */   
/*      */   private void readIfNeeded(ChannelHandlerContext ctx)
/*      */   {
/* 1263 */     if ((!ctx.channel().config().isAutoRead()) && ((!this.firedChannelRead) || (!this.handshakePromise.isDone())))
/*      */     {
/*      */ 
/* 1266 */       ctx.read();
/*      */     }
/*      */   }
/*      */   
/*      */   private void flushIfNeeded(ChannelHandlerContext ctx) {
/* 1271 */     if (this.needsFlush) {
/* 1272 */       forceFlush(ctx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void unwrapNonAppData(ChannelHandlerContext ctx)
/*      */     throws SSLException
/*      */   {
/* 1280 */     unwrap(ctx, Unpooled.EMPTY_BUFFER, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int unwrap(ChannelHandlerContext ctx, ByteBuf packet, int offset, int length)
/*      */     throws SSLException
/*      */   {
/* 1288 */     int originalLength = length;
/* 1289 */     boolean wrapLater = false;
/* 1290 */     boolean notifyClosure = false;
/* 1291 */     int overflowReadableBytes = -1;
/* 1292 */     ByteBuf decodeOut = allocate(ctx, length);
/*      */     
/*      */     try
/*      */     {
/* 1296 */       while (!ctx.isRemoved()) {
/* 1297 */         SSLEngineResult result = this.engineType.unwrap(this, packet, offset, length, decodeOut);
/* 1298 */         SSLEngineResult.Status status = result.getStatus();
/* 1299 */         SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
/* 1300 */         int produced = result.bytesProduced();
/* 1301 */         int consumed = result.bytesConsumed();
/*      */         
/*      */ 
/* 1304 */         offset += consumed;
/* 1305 */         length -= consumed;
/*      */         
/* 1307 */         switch (status) {
/*      */         case BUFFER_OVERFLOW: 
/* 1309 */           int readableBytes = decodeOut.readableBytes();
/* 1310 */           int previousOverflowReadableBytes = overflowReadableBytes;
/* 1311 */           overflowReadableBytes = readableBytes;
/* 1312 */           int bufferSize = this.engine.getSession().getApplicationBufferSize() - readableBytes;
/* 1313 */           if (readableBytes > 0) {
/* 1314 */             this.firedChannelRead = true;
/* 1315 */             ctx.fireChannelRead(decodeOut);
/*      */             
/*      */ 
/* 1318 */             decodeOut = null;
/* 1319 */             if (bufferSize <= 0)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 1324 */               bufferSize = this.engine.getSession().getApplicationBufferSize();
/*      */             }
/*      */           }
/*      */           else {
/* 1328 */             decodeOut.release();
/* 1329 */             decodeOut = null;
/*      */           }
/* 1331 */           if ((readableBytes == 0) && (previousOverflowReadableBytes == 0))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1336 */             throw new IllegalStateException("Two consecutive overflows but no content was consumed. " + SSLSession.class.getSimpleName() + " getApplicationBufferSize: " + this.engine.getSession().getApplicationBufferSize() + " maybe too small.");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1341 */           decodeOut = allocate(ctx, this.engineType.calculatePendingData(this, bufferSize));
/* 1342 */           break;
/*      */         
/*      */         case CLOSED: 
/* 1345 */           notifyClosure = true;
/* 1346 */           overflowReadableBytes = -1;
/* 1347 */           break;
/*      */         default: 
/* 1349 */           overflowReadableBytes = -1;
/*      */           
/*      */ 
/*      */ 
/* 1353 */           switch (handshakeStatus)
/*      */           {
/*      */           case NEED_UNWRAP: 
/*      */             break;
/*      */           
/*      */ 
/*      */           case NEED_WRAP: 
/* 1360 */             if ((!wrapNonAppData(ctx, true)) || (length != 0)) {}
/* 1361 */             break;
/*      */           
/*      */ 
/*      */           case NEED_TASK: 
/* 1365 */             runDelegatedTasks();
/* 1366 */             break;
/*      */           case FINISHED: 
/* 1368 */             setHandshakeSuccess();
/* 1369 */             wrapLater = true;
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
/* 1382 */             break;
/*      */           case NOT_HANDSHAKING: 
/* 1384 */             if (setHandshakeSuccessIfStillHandshaking()) {
/* 1385 */               wrapLater = true;
/*      */             }
/*      */             else {
/* 1388 */               if (this.flushedBeforeHandshake)
/*      */               {
/*      */ 
/*      */ 
/* 1392 */                 this.flushedBeforeHandshake = false;
/* 1393 */                 wrapLater = true;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 1398 */               if (length != 0) {} }
/* 1399 */             break;
/*      */           
/*      */ 
/*      */           default: 
/* 1403 */             throw new IllegalStateException("unknown handshake status: " + handshakeStatus);
/*      */             
/*      */ 
/* 1406 */             if ((status == SSLEngineResult.Status.BUFFER_UNDERFLOW) || ((consumed == 0) && (produced == 0))) {
/* 1407 */               if (handshakeStatus != SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
/*      */                 break label490;
/*      */               }
/* 1410 */               readIfNeeded(ctx);
/*      */               break label490;
/*      */             }
/*      */             break; }
/*      */           break; }
/*      */       }
/*      */       label490:
/* 1417 */       if (wrapLater) {
/* 1418 */         wrap(ctx, true);
/*      */       }
/*      */       
/* 1421 */       if (notifyClosure) {
/* 1422 */         notifyClosePromise(null);
/*      */       }
/*      */     } finally {
/* 1425 */       if (decodeOut != null) {
/* 1426 */         if (decodeOut.isReadable()) {
/* 1427 */           this.firedChannelRead = true;
/*      */           
/* 1429 */           ctx.fireChannelRead(decodeOut);
/*      */         } else {
/* 1431 */           decodeOut.release();
/*      */         }
/*      */       }
/*      */     }
/* 1435 */     return originalLength - length;
/*      */   }
/*      */   
/*      */   private static ByteBuffer toByteBuffer(ByteBuf out, int index, int len) {
/* 1439 */     return out.nioBufferCount() == 1 ? out.internalNioBuffer(index, len) : out
/* 1440 */       .nioBuffer(index, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void runDelegatedTasks()
/*      */   {
/* 1450 */     if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
/*      */       for (;;) {
/* 1452 */         Runnable task = this.engine.getDelegatedTask();
/* 1453 */         if (task == null) {
/*      */           break;
/*      */         }
/*      */         
/* 1457 */         task.run();
/*      */       }
/*      */     }
/* 1460 */     final List<Runnable> tasks = new ArrayList(2);
/*      */     for (;;) {
/* 1462 */       Runnable task = this.engine.getDelegatedTask();
/* 1463 */       if (task == null) {
/*      */         break;
/*      */       }
/*      */       
/* 1467 */       tasks.add(task);
/*      */     }
/*      */     
/* 1470 */     if (tasks.isEmpty()) {
/* 1471 */       return;
/*      */     }
/*      */     
/* 1474 */     final CountDownLatch latch = new CountDownLatch(1);
/* 1475 */     this.delegatedTaskExecutor.execute(new Runnable()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 1479 */           for (Runnable task : tasks) {
/* 1480 */             task.run();
/*      */           }
/*      */         } catch (Exception e) {
/* 1483 */           SslHandler.this.ctx.fireExceptionCaught(e);
/*      */         } finally {
/* 1485 */           latch.countDown();
/*      */         }
/*      */         
/*      */       }
/* 1489 */     });
/* 1490 */     boolean interrupted = false;
/* 1491 */     while (latch.getCount() != 0L) {
/*      */       try {
/* 1493 */         latch.await();
/*      */       }
/*      */       catch (InterruptedException e) {
/* 1496 */         interrupted = true;
/*      */       }
/*      */     }
/*      */     
/* 1500 */     if (interrupted) {
/* 1501 */       Thread.currentThread().interrupt();
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
/*      */   private boolean setHandshakeSuccessIfStillHandshaking()
/*      */   {
/* 1514 */     if (!this.handshakePromise.isDone()) {
/* 1515 */       setHandshakeSuccess();
/* 1516 */       return true;
/*      */     }
/* 1518 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setHandshakeSuccess()
/*      */   {
/* 1525 */     this.handshakePromise.trySuccess(this.ctx.channel());
/*      */     
/* 1527 */     if (logger.isDebugEnabled()) {
/* 1528 */       logger.debug("{} HANDSHAKEN: {}", this.ctx.channel(), this.engine.getSession().getCipherSuite());
/*      */     }
/* 1530 */     this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
/*      */     
/* 1532 */     if ((this.readDuringHandshake) && (!this.ctx.channel().config().isAutoRead())) {
/* 1533 */       this.readDuringHandshake = false;
/* 1534 */       this.ctx.read();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause)
/*      */   {
/* 1542 */     setHandshakeFailure(ctx, cause, true, true, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause, boolean closeInbound, boolean notify, boolean alwaysFlushAndClose)
/*      */   {
/*      */     try
/*      */     {
/* 1553 */       this.outboundClosed = true;
/* 1554 */       this.engine.closeOutbound();
/*      */       
/* 1556 */       if (closeInbound) {
/*      */         try {
/* 1558 */           this.engine.closeInbound();
/*      */         } catch (SSLException e) {
/* 1560 */           if (logger.isDebugEnabled())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1565 */             String msg = e.getMessage();
/* 1566 */             if ((msg == null) || (!msg.contains("possible truncation attack"))) {
/* 1567 */               logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1572 */       if ((this.handshakePromise.tryFailure(cause)) || (alwaysFlushAndClose)) {
/* 1573 */         SslUtils.handleHandshakeFailure(ctx, cause, notify);
/*      */       }
/*      */     }
/*      */     finally {
/* 1577 */       releaseAndFailAll(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   private void releaseAndFailAll(Throwable cause) {
/* 1582 */     if (this.pendingUnencryptedWrites != null) {
/* 1583 */       this.pendingUnencryptedWrites.releaseAndFailAll(this.ctx, cause);
/*      */     }
/*      */   }
/*      */   
/*      */   private void notifyClosePromise(Throwable cause) {
/* 1588 */     if (cause == null) {
/* 1589 */       if (this.sslClosePromise.trySuccess(this.ctx.channel())) {
/* 1590 */         this.ctx.fireUserEventTriggered(SslCloseCompletionEvent.SUCCESS);
/*      */       }
/*      */     }
/* 1593 */     else if (this.sslClosePromise.tryFailure(cause)) {
/* 1594 */       this.ctx.fireUserEventTriggered(new SslCloseCompletionEvent(cause));
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private void closeOutboundAndChannel(ChannelHandlerContext ctx, final ChannelPromise promise, boolean disconnect)
/*      */     throws Exception
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iconst_1
/*      */     //   2: putfield 71	io/netty/handler/ssl/SslHandler:outboundClosed	Z
/*      */     //   5: aload_0
/*      */     //   6: getfield 14	io/netty/handler/ssl/SslHandler:engine	Ljavax/net/ssl/SSLEngine;
/*      */     //   9: invokevirtual 72	javax/net/ssl/SSLEngine:closeOutbound	()V
/*      */     //   12: aload_1
/*      */     //   13: invokeinterface 77 1 0
/*      */     //   18: invokeinterface 174 1 0
/*      */     //   23: ifne +27 -> 50
/*      */     //   26: iload_3
/*      */     //   27: ifeq +14 -> 41
/*      */     //   30: aload_1
/*      */     //   31: aload_2
/*      */     //   32: invokeinterface 282 2 0
/*      */     //   37: pop
/*      */     //   38: goto +11 -> 49
/*      */     //   41: aload_1
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 283 2 0
/*      */     //   48: pop
/*      */     //   49: return
/*      */     //   50: aload_1
/*      */     //   51: invokeinterface 65 1 0
/*      */     //   56: astore 4
/*      */     //   58: aload_0
/*      */     //   59: aload_1
/*      */     //   60: aload 4
/*      */     //   62: invokespecial 73	io/netty/handler/ssl/SslHandler:flush	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/channel/ChannelPromise;)V
/*      */     //   65: aload_0
/*      */     //   66: getfield 284	io/netty/handler/ssl/SslHandler:closeNotify	Z
/*      */     //   69: ifne +45 -> 114
/*      */     //   72: aload_0
/*      */     //   73: iconst_1
/*      */     //   74: putfield 284	io/netty/handler/ssl/SslHandler:closeNotify	Z
/*      */     //   77: aload_0
/*      */     //   78: aload_1
/*      */     //   79: aload 4
/*      */     //   81: aload_1
/*      */     //   82: invokeinterface 65 1 0
/*      */     //   87: new 285	io/netty/channel/ChannelPromiseNotifier
/*      */     //   90: dup
/*      */     //   91: iconst_0
/*      */     //   92: iconst_1
/*      */     //   93: anewarray 286	io/netty/channel/ChannelPromise
/*      */     //   96: dup
/*      */     //   97: iconst_0
/*      */     //   98: aload_2
/*      */     //   99: aastore
/*      */     //   100: invokespecial 287	io/netty/channel/ChannelPromiseNotifier:<init>	(Z[Lio/netty/channel/ChannelPromise;)V
/*      */     //   103: invokeinterface 288 2 0
/*      */     //   108: invokespecial 289	io/netty/handler/ssl/SslHandler:safeClose	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/channel/ChannelFuture;Lio/netty/channel/ChannelPromise;)V
/*      */     //   111: goto +94 -> 205
/*      */     //   114: aload_0
/*      */     //   115: getfield 2	io/netty/handler/ssl/SslHandler:sslClosePromise	Lio/netty/handler/ssl/SslHandler$LazyChannelPromise;
/*      */     //   118: new 290	io/netty/handler/ssl/SslHandler$3
/*      */     //   121: dup
/*      */     //   122: aload_0
/*      */     //   123: aload_2
/*      */     //   124: invokespecial 291	io/netty/handler/ssl/SslHandler$3:<init>	(Lio/netty/handler/ssl/SslHandler;Lio/netty/channel/ChannelPromise;)V
/*      */     //   127: invokevirtual 292	io/netty/handler/ssl/SslHandler$LazyChannelPromise:addListener	(Lio/netty/util/concurrent/GenericFutureListener;)Lio/netty/util/concurrent/Promise;
/*      */     //   130: pop
/*      */     //   131: goto +74 -> 205
/*      */     //   134: astore 5
/*      */     //   136: aload_0
/*      */     //   137: getfield 284	io/netty/handler/ssl/SslHandler:closeNotify	Z
/*      */     //   140: ifne +45 -> 185
/*      */     //   143: aload_0
/*      */     //   144: iconst_1
/*      */     //   145: putfield 284	io/netty/handler/ssl/SslHandler:closeNotify	Z
/*      */     //   148: aload_0
/*      */     //   149: aload_1
/*      */     //   150: aload 4
/*      */     //   152: aload_1
/*      */     //   153: invokeinterface 65 1 0
/*      */     //   158: new 285	io/netty/channel/ChannelPromiseNotifier
/*      */     //   161: dup
/*      */     //   162: iconst_0
/*      */     //   163: iconst_1
/*      */     //   164: anewarray 286	io/netty/channel/ChannelPromise
/*      */     //   167: dup
/*      */     //   168: iconst_0
/*      */     //   169: aload_2
/*      */     //   170: aastore
/*      */     //   171: invokespecial 287	io/netty/channel/ChannelPromiseNotifier:<init>	(Z[Lio/netty/channel/ChannelPromise;)V
/*      */     //   174: invokeinterface 288 2 0
/*      */     //   179: invokespecial 289	io/netty/handler/ssl/SslHandler:safeClose	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/channel/ChannelFuture;Lio/netty/channel/ChannelPromise;)V
/*      */     //   182: goto +20 -> 202
/*      */     //   185: aload_0
/*      */     //   186: getfield 2	io/netty/handler/ssl/SslHandler:sslClosePromise	Lio/netty/handler/ssl/SslHandler$LazyChannelPromise;
/*      */     //   189: new 290	io/netty/handler/ssl/SslHandler$3
/*      */     //   192: dup
/*      */     //   193: aload_0
/*      */     //   194: aload_2
/*      */     //   195: invokespecial 291	io/netty/handler/ssl/SslHandler$3:<init>	(Lio/netty/handler/ssl/SslHandler;Lio/netty/channel/ChannelPromise;)V
/*      */     //   198: invokevirtual 292	io/netty/handler/ssl/SslHandler$LazyChannelPromise:addListener	(Lio/netty/util/concurrent/GenericFutureListener;)Lio/netty/util/concurrent/Promise;
/*      */     //   201: pop
/*      */     //   202: aload 5
/*      */     //   204: athrow
/*      */     //   205: return
/*      */     // Line number table:
/*      */     //   Java source line #1601	-> byte code offset #0
/*      */     //   Java source line #1602	-> byte code offset #5
/*      */     //   Java source line #1604	-> byte code offset #12
/*      */     //   Java source line #1605	-> byte code offset #26
/*      */     //   Java source line #1606	-> byte code offset #30
/*      */     //   Java source line #1608	-> byte code offset #41
/*      */     //   Java source line #1610	-> byte code offset #49
/*      */     //   Java source line #1613	-> byte code offset #50
/*      */     //   Java source line #1615	-> byte code offset #58
/*      */     //   Java source line #1617	-> byte code offset #65
/*      */     //   Java source line #1618	-> byte code offset #72
/*      */     //   Java source line #1627	-> byte code offset #77
/*      */     //   Java source line #1631	-> byte code offset #114
/*      */     //   Java source line #1638	-> byte code offset #131
/*      */     //   Java source line #1617	-> byte code offset #134
/*      */     //   Java source line #1618	-> byte code offset #143
/*      */     //   Java source line #1627	-> byte code offset #148
/*      */     //   Java source line #1631	-> byte code offset #185
/*      */     //   Java source line #1638	-> byte code offset #202
/*      */     //   Java source line #1639	-> byte code offset #205
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	206	0	this	SslHandler
/*      */     //   0	206	1	ctx	ChannelHandlerContext
/*      */     //   0	206	2	promise	ChannelPromise
/*      */     //   0	206	3	disconnect	boolean
/*      */     //   56	95	4	closeNotifyPromise	ChannelPromise
/*      */     //   134	69	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   58	65	134	finally
/*      */     //   134	136	134	finally
/*      */   }
/*      */   
/*      */   private void flush(ChannelHandlerContext ctx, ChannelPromise promise)
/*      */     throws Exception
/*      */   {
/* 1642 */     if (this.pendingUnencryptedWrites != null) {
/* 1643 */       this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, promise);
/*      */     } else {
/* 1645 */       promise.setFailure(newPendingWritesNullException());
/*      */     }
/* 1647 */     flush(ctx);
/*      */   }
/*      */   
/*      */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*      */   {
/* 1652 */     this.ctx = ctx;
/*      */     
/* 1654 */     this.pendingUnencryptedWrites = new SslHandlerCoalescingBufferQueue(ctx.channel(), 16);
/* 1655 */     if (ctx.channel().isActive()) {
/* 1656 */       startHandshakeProcessing();
/*      */     }
/*      */   }
/*      */   
/*      */   private void startHandshakeProcessing() {
/* 1661 */     this.handshakeStarted = true;
/* 1662 */     if (this.engine.getUseClientMode())
/*      */     {
/*      */ 
/*      */ 
/* 1666 */       handshake(null);
/*      */     } else {
/* 1668 */       applyHandshakeTimeout(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Future<Channel> renegotiate()
/*      */   {
/* 1676 */     ChannelHandlerContext ctx = this.ctx;
/* 1677 */     if (ctx == null) {
/* 1678 */       throw new IllegalStateException();
/*      */     }
/*      */     
/* 1681 */     return renegotiate(ctx.executor().newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Future<Channel> renegotiate(final Promise<Channel> promise)
/*      */   {
/* 1688 */     if (promise == null) {
/* 1689 */       throw new NullPointerException("promise");
/*      */     }
/*      */     
/* 1692 */     ChannelHandlerContext ctx = this.ctx;
/* 1693 */     if (ctx == null) {
/* 1694 */       throw new IllegalStateException();
/*      */     }
/*      */     
/* 1697 */     EventExecutor executor = ctx.executor();
/* 1698 */     if (!executor.inEventLoop()) {
/* 1699 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/* 1702 */           SslHandler.this.handshake(promise);
/*      */         }
/* 1704 */       });
/* 1705 */       return promise;
/*      */     }
/*      */     
/* 1708 */     handshake(promise);
/* 1709 */     return promise;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handshake(final Promise<Channel> newHandshakePromise)
/*      */   {
/*      */     Promise<Channel> p;
/*      */     
/*      */ 
/*      */ 
/* 1721 */     if (newHandshakePromise != null) {
/* 1722 */       Promise<Channel> oldHandshakePromise = this.handshakePromise;
/* 1723 */       if (!oldHandshakePromise.isDone())
/*      */       {
/*      */ 
/* 1726 */         oldHandshakePromise.addListener(new FutureListener()
/*      */         {
/*      */           public void operationComplete(Future<Channel> future) throws Exception {
/* 1729 */             if (future.isSuccess()) {
/* 1730 */               newHandshakePromise.setSuccess(future.getNow());
/*      */             } else {
/* 1732 */               newHandshakePromise.setFailure(future.cause());
/*      */             }
/*      */           }
/*      */         }); return;
/*      */       }
/*      */       
/*      */       Promise<Channel> p;
/* 1739 */       this.handshakePromise = (p = newHandshakePromise);
/* 1740 */     } else { if (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)
/*      */       {
/*      */ 
/* 1743 */         return;
/*      */       }
/*      */       
/* 1746 */       p = this.handshakePromise;
/* 1747 */       assert (!p.isDone());
/*      */     }
/*      */     
/*      */ 
/* 1751 */     ChannelHandlerContext ctx = this.ctx;
/*      */     try {
/* 1753 */       this.engine.beginHandshake();
/* 1754 */       wrapNonAppData(ctx, false);
/*      */     } catch (Throwable e) {
/* 1756 */       setHandshakeFailure(ctx, e);
/*      */     } finally {
/* 1758 */       forceFlush(ctx);
/*      */     }
/* 1760 */     applyHandshakeTimeout(p);
/*      */   }
/*      */   
/*      */   private void applyHandshakeTimeout(Promise<Channel> p) {
/* 1764 */     final Promise<Channel> promise = p == null ? this.handshakePromise : p;
/*      */     
/* 1766 */     long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
/* 1767 */     if ((handshakeTimeoutMillis <= 0L) || (promise.isDone())) {
/* 1768 */       return;
/*      */     }
/*      */     
/* 1771 */     final ScheduledFuture<?> timeoutFuture = this.ctx.executor().schedule(new Runnable()
/*      */     {
/*      */       /* Error */
/*      */       public void run()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 2	io/netty/handler/ssl/SslHandler$6:val$promise	Lio/netty/util/concurrent/Promise;
/*      */         //   4: invokeinterface 4 1 0
/*      */         //   9: ifeq +4 -> 13
/*      */         //   12: return
/*      */         //   13: aload_0
/*      */         //   14: getfield 1	io/netty/handler/ssl/SslHandler$6:this$0	Lio/netty/handler/ssl/SslHandler;
/*      */         //   17: invokestatic 5	io/netty/handler/ssl/SslHandler:access$900	(Lio/netty/handler/ssl/SslHandler;)Lio/netty/util/concurrent/Promise;
/*      */         //   20: invokestatic 6	io/netty/handler/ssl/SslHandler:access$800	()Ljavax/net/ssl/SSLException;
/*      */         //   23: invokeinterface 7 2 0
/*      */         //   28: ifeq +17 -> 45
/*      */         //   31: aload_0
/*      */         //   32: getfield 1	io/netty/handler/ssl/SslHandler$6:this$0	Lio/netty/handler/ssl/SslHandler;
/*      */         //   35: invokestatic 8	io/netty/handler/ssl/SslHandler:access$600	(Lio/netty/handler/ssl/SslHandler;)Lio/netty/channel/ChannelHandlerContext;
/*      */         //   38: invokestatic 6	io/netty/handler/ssl/SslHandler:access$800	()Ljavax/net/ssl/SSLException;
/*      */         //   41: iconst_1
/*      */         //   42: invokestatic 9	io/netty/handler/ssl/SslUtils:handleHandshakeFailure	(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;Z)V
/*      */         //   45: aload_0
/*      */         //   46: getfield 1	io/netty/handler/ssl/SslHandler$6:this$0	Lio/netty/handler/ssl/SslHandler;
/*      */         //   49: invokestatic 6	io/netty/handler/ssl/SslHandler:access$800	()Ljavax/net/ssl/SSLException;
/*      */         //   52: invokestatic 10	io/netty/handler/ssl/SslHandler:access$1000	(Lio/netty/handler/ssl/SslHandler;Ljava/lang/Throwable;)V
/*      */         //   55: goto +16 -> 71
/*      */         //   58: astore_1
/*      */         //   59: aload_0
/*      */         //   60: getfield 1	io/netty/handler/ssl/SslHandler$6:this$0	Lio/netty/handler/ssl/SslHandler;
/*      */         //   63: invokestatic 6	io/netty/handler/ssl/SslHandler:access$800	()Ljavax/net/ssl/SSLException;
/*      */         //   66: invokestatic 10	io/netty/handler/ssl/SslHandler:access$1000	(Lio/netty/handler/ssl/SslHandler;Ljava/lang/Throwable;)V
/*      */         //   69: aload_1
/*      */         //   70: athrow
/*      */         //   71: return
/*      */         // Line number table:
/*      */         //   Java source line #1774	-> byte code offset #0
/*      */         //   Java source line #1775	-> byte code offset #12
/*      */         //   Java source line #1778	-> byte code offset #13
/*      */         //   Java source line #1779	-> byte code offset #31
/*      */         //   Java source line #1782	-> byte code offset #45
/*      */         //   Java source line #1783	-> byte code offset #55
/*      */         //   Java source line #1782	-> byte code offset #58
/*      */         //   Java source line #1783	-> byte code offset #69
/*      */         //   Java source line #1784	-> byte code offset #71
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	72	0	this	6
/*      */         //   58	12	1	localObject	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   13	45	58	finally
/*      */       }
/* 1771 */     }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
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
/* 1788 */     promise.addListener(new FutureListener()
/*      */     {
/*      */       public void operationComplete(Future<Channel> f) throws Exception {
/* 1791 */         timeoutFuture.cancel(false);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private void forceFlush(ChannelHandlerContext ctx) {
/* 1797 */     this.needsFlush = false;
/* 1798 */     ctx.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void channelActive(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/* 1806 */     if (!this.startTls) {
/* 1807 */       startHandshakeProcessing();
/*      */     }
/* 1809 */     ctx.fireChannelActive();
/*      */   }
/*      */   
/*      */ 
/*      */   private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise)
/*      */   {
/* 1815 */     if (!ctx.channel().isActive()) {
/* 1816 */       ctx.close(promise); return;
/*      */     }
/*      */     
/*      */     ScheduledFuture<?> timeoutFuture;
/*      */     final ScheduledFuture<?> timeoutFuture;
/* 1821 */     if (!flushFuture.isDone()) {
/* 1822 */       long closeNotifyTimeout = this.closeNotifyFlushTimeoutMillis;
/* 1823 */       ScheduledFuture<?> timeoutFuture; if (closeNotifyTimeout > 0L)
/*      */       {
/* 1825 */         timeoutFuture = ctx.executor().schedule(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/* 1829 */             if (!flushFuture.isDone()) {
/* 1830 */               SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx
/* 1831 */                 .channel());
/* 1832 */               SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise); } } }, closeNotifyTimeout, TimeUnit.MILLISECONDS);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1837 */         timeoutFuture = null;
/*      */       }
/*      */     } else {
/* 1840 */       timeoutFuture = null;
/*      */     }
/*      */     
/*      */ 
/* 1844 */     flushFuture.addListener(new ChannelFutureListener()
/*      */     {
/*      */       public void operationComplete(ChannelFuture f) throws Exception
/*      */       {
/* 1848 */         if (timeoutFuture != null) {
/* 1849 */           timeoutFuture.cancel(false);
/*      */         }
/* 1851 */         final long closeNotifyReadTimeout = SslHandler.this.closeNotifyReadTimeoutMillis;
/* 1852 */         if (closeNotifyReadTimeout <= 0L)
/*      */         {
/*      */ 
/* 1855 */           SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
/*      */         } else {
/*      */           ScheduledFuture<?> closeNotifyReadTimeoutFuture;
/*      */           final ScheduledFuture<?> closeNotifyReadTimeoutFuture;
/* 1859 */           if (!SslHandler.this.sslClosePromise.isDone()) {
/* 1860 */             closeNotifyReadTimeoutFuture = ctx.executor().schedule(new Runnable()
/*      */             {
/*      */               public void run() {
/* 1863 */                 if (!SslHandler.this.sslClosePromise.isDone()) {
/* 1864 */                   SslHandler.logger.debug("{} did not receive close_notify in {}ms; force-closing the connection.", SslHandler.9.this.val$ctx
/*      */                   
/* 1866 */                     .channel(), Long.valueOf(closeNotifyReadTimeout));
/*      */                   
/*      */ 
/* 1869 */                   SslHandler.addCloseListener(SslHandler.9.this.val$ctx.close(SslHandler.9.this.val$ctx.newPromise()), SslHandler.9.this.val$promise); } } }, closeNotifyReadTimeout, TimeUnit.MILLISECONDS);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1874 */             closeNotifyReadTimeoutFuture = null;
/*      */           }
/*      */           
/*      */ 
/* 1878 */           SslHandler.this.sslClosePromise.addListener(new FutureListener()
/*      */           {
/*      */             public void operationComplete(Future<Channel> future) throws Exception {
/* 1881 */               if (closeNotifyReadTimeoutFuture != null) {
/* 1882 */                 closeNotifyReadTimeoutFuture.cancel(false);
/*      */               }
/* 1884 */               SslHandler.addCloseListener(SslHandler.9.this.val$ctx.close(SslHandler.9.this.val$ctx.newPromise()), SslHandler.9.this.val$promise);
/*      */             }
/*      */           });
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void addCloseListener(ChannelFuture future, ChannelPromise promise)
/*      */   {
/* 1899 */     future.addListener(new ChannelPromiseNotifier(false, new ChannelPromise[] { promise }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf allocate(ChannelHandlerContext ctx, int capacity)
/*      */   {
/* 1907 */     ByteBufAllocator alloc = ctx.alloc();
/* 1908 */     if (this.engineType.wantsDirectBuffer) {
/* 1909 */       return alloc.directBuffer(capacity);
/*      */     }
/* 1911 */     return alloc.buffer(capacity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf allocateOutNetBuf(ChannelHandlerContext ctx, int pendingBytes, int numComponents)
/*      */   {
/* 1920 */     return allocate(ctx, this.engineType.calculateWrapBufferCapacity(this, pendingBytes, numComponents));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final class SslHandlerCoalescingBufferQueue
/*      */     extends AbstractCoalescingBufferQueue
/*      */   {
/*      */     SslHandlerCoalescingBufferQueue(Channel channel, int initSize)
/*      */     {
/* 1931 */       super(initSize);
/*      */     }
/*      */     
/*      */     protected ByteBuf compose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next)
/*      */     {
/* 1936 */       int wrapDataSize = SslHandler.this.wrapDataSize;
/* 1937 */       if ((cumulation instanceof CompositeByteBuf)) {
/* 1938 */         CompositeByteBuf composite = (CompositeByteBuf)cumulation;
/* 1939 */         int numComponents = composite.numComponents();
/* 1940 */         if ((numComponents == 0) || 
/* 1941 */           (!SslHandler.attemptCopyToCumulation(composite.internalComponent(numComponents - 1), next, wrapDataSize))) {
/* 1942 */           composite.addComponent(true, next);
/*      */         }
/* 1944 */         return composite;
/*      */       }
/* 1946 */       return SslHandler.attemptCopyToCumulation(cumulation, next, wrapDataSize) ? cumulation : 
/* 1947 */         copyAndCompose(alloc, cumulation, next);
/*      */     }
/*      */     
/*      */     protected ByteBuf composeFirst(ByteBufAllocator allocator, ByteBuf first)
/*      */     {
/* 1952 */       if ((first instanceof CompositeByteBuf)) {
/* 1953 */         CompositeByteBuf composite = (CompositeByteBuf)first;
/* 1954 */         first = allocator.directBuffer(composite.readableBytes());
/*      */         try {
/* 1956 */           first.writeBytes(composite);
/*      */         } catch (Throwable cause) {
/* 1958 */           first.release();
/* 1959 */           PlatformDependent.throwException(cause);
/*      */         }
/* 1961 */         composite.release();
/*      */       }
/* 1963 */       return first;
/*      */     }
/*      */     
/*      */     protected ByteBuf removeEmptyValue()
/*      */     {
/* 1968 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean attemptCopyToCumulation(ByteBuf cumulation, ByteBuf next, int wrapDataSize) {
/* 1973 */     int inReadableBytes = next.readableBytes();
/* 1974 */     int cumulationCapacity = cumulation.capacity();
/* 1975 */     if (wrapDataSize - cumulation.readableBytes() >= inReadableBytes)
/*      */     {
/*      */ 
/*      */ 
/* 1979 */       if ((!cumulation.isWritable(inReadableBytes)) || (cumulationCapacity < wrapDataSize)) { if (cumulationCapacity < wrapDataSize)
/*      */         {
/* 1981 */           if (!ByteBufUtil.ensureWritableSuccess(cumulation.ensureWritable(inReadableBytes, false))) {} }
/* 1982 */       } else { cumulation.writeBytes(next);
/* 1983 */         next.release();
/* 1984 */         return true;
/*      */       } }
/* 1986 */     return false;
/*      */   }
/*      */   
/*      */   private final class LazyChannelPromise extends DefaultPromise<Channel> {
/*      */     private LazyChannelPromise() {}
/*      */     
/*      */     protected EventExecutor executor() {
/* 1993 */       if (SslHandler.this.ctx == null) {
/* 1994 */         throw new IllegalStateException();
/*      */       }
/* 1996 */       return SslHandler.this.ctx.executor();
/*      */     }
/*      */     
/*      */     protected void checkDeadLock()
/*      */     {
/* 2001 */       if (SslHandler.this.ctx == null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2008 */         return;
/*      */       }
/* 2010 */       super.checkDeadLock();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\SslHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */