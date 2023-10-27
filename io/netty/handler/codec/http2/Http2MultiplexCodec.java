/*      */ package io.netty.handler.codec.http2;
/*      */ 
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.Channel.Unsafe;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelHandler;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.channel.ChannelId;
/*      */ import io.netty.channel.ChannelMetadata;
/*      */ import io.netty.channel.ChannelOutboundBuffer;
/*      */ import io.netty.channel.ChannelPipeline;
/*      */ import io.netty.channel.ChannelProgressivePromise;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.DefaultChannelConfig;
/*      */ import io.netty.channel.DefaultChannelPipeline;
/*      */ import io.netty.channel.EventLoop;
/*      */ import io.netty.channel.MessageSizeEstimator;
/*      */ import io.netty.channel.MessageSizeEstimator.Handle;
/*      */ import io.netty.channel.RecvByteBufAllocator;
/*      */ import io.netty.channel.RecvByteBufAllocator.ExtendedHandle;
/*      */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*      */ import io.netty.channel.VoidChannelPromise;
/*      */ import io.netty.channel.WriteBufferWaterMark;
/*      */ import io.netty.util.DefaultAttributeMap;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Http2MultiplexCodec
/*      */   extends Http2FrameCodec
/*      */ {
/*  109 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultHttp2StreamChannel.class);
/*      */   
/*  111 */   private static final ChannelFutureListener CHILD_CHANNEL_REGISTRATION_LISTENER = new ChannelFutureListener()
/*      */   {
/*      */     public void operationComplete(ChannelFuture future) {
/*  114 */       Http2MultiplexCodec.registerDone(future);
/*      */     }
/*      */   };
/*      */   
/*  118 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*  119 */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.class, "write(...)");
/*      */   
/*      */ 
/*      */   private static final int MIN_HTTP2_FRAME_SIZE = 9;
/*      */   
/*      */   private final ChannelHandler inboundStreamHandler;
/*      */   
/*      */   private final ChannelHandler upgradeStreamHandler;
/*      */   
/*      */ 
/*      */   private static final class FlowControlledFrameSizeEstimator
/*      */     implements MessageSizeEstimator
/*      */   {
/*  132 */     static final FlowControlledFrameSizeEstimator INSTANCE = new FlowControlledFrameSizeEstimator();
/*      */     
/*  134 */     static final MessageSizeEstimator.Handle HANDLE_INSTANCE = new MessageSizeEstimator.Handle()
/*      */     {
/*      */       public int size(Object msg) {
/*  137 */         return (msg instanceof Http2DataFrame) ? 
/*      */         
/*  139 */           (int)Math.min(2147483647L, ((Http2DataFrame)msg).initialFlowControlledBytes() + 9L) : 9;
/*      */       }
/*      */     };
/*      */     
/*      */ 
/*      */     public MessageSizeEstimator.Handle newHandle()
/*      */     {
/*  146 */       return HANDLE_INSTANCE;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  153 */   private int initialOutboundStreamWindow = 65535;
/*      */   
/*      */ 
/*      */   private boolean parentReadInProgress;
/*      */   
/*      */   private int idCount;
/*      */   
/*      */   private DefaultHttp2StreamChannel head;
/*      */   
/*      */   private DefaultHttp2StreamChannel tail;
/*      */   
/*      */   volatile ChannelHandlerContext ctx;
/*      */   
/*      */ 
/*      */   Http2MultiplexCodec(Http2ConnectionEncoder encoder, Http2ConnectionDecoder decoder, Http2Settings initialSettings, ChannelHandler inboundStreamHandler, ChannelHandler upgradeStreamHandler)
/*      */   {
/*  169 */     super(encoder, decoder, initialSettings);
/*  170 */     this.inboundStreamHandler = inboundStreamHandler;
/*  171 */     this.upgradeStreamHandler = upgradeStreamHandler;
/*      */   }
/*      */   
/*      */   public void onHttpClientUpgrade()
/*      */     throws Http2Exception
/*      */   {
/*  177 */     if (this.upgradeStreamHandler == null) {
/*  178 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Client is misconfigured for upgrade requests", new Object[0]);
/*      */     }
/*      */     
/*  181 */     super.onHttpClientUpgrade();
/*      */     
/*  183 */     Http2MultiplexCodecStream codecStream = newStream();
/*  184 */     codecStream.setStreamAndProperty(this.streamKey, connection().stream(1));
/*  185 */     onHttp2UpgradeStreamInitialized(this.ctx, codecStream);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void registerDone(ChannelFuture future)
/*      */   {
/*  192 */     if (!future.isSuccess()) {
/*  193 */       Channel childChannel = future.channel();
/*  194 */       if (childChannel.isRegistered()) {
/*  195 */         childChannel.close();
/*      */       } else {
/*  197 */         childChannel.unsafe().closeForcibly();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final void handlerAdded0(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  204 */     if (ctx.executor() != ctx.channel().eventLoop()) {
/*  205 */       throw new IllegalStateException("EventExecutor must be EventLoop of Channel");
/*      */     }
/*  207 */     this.ctx = ctx;
/*      */   }
/*      */   
/*      */   public final void handlerRemoved0(ChannelHandlerContext ctx) throws Exception
/*      */   {
/*  212 */     super.handlerRemoved0(ctx);
/*      */     
/*      */ 
/*  215 */     DefaultHttp2StreamChannel ch = this.head;
/*  216 */     while (ch != null) {
/*  217 */       DefaultHttp2StreamChannel curr = ch;
/*  218 */       ch = curr.next;
/*  219 */       curr.next = (curr.previous = null);
/*      */     }
/*  221 */     this.head = (this.tail = null);
/*      */   }
/*      */   
/*      */   Http2MultiplexCodecStream newStream()
/*      */   {
/*  226 */     return new Http2MultiplexCodecStream();
/*      */   }
/*      */   
/*      */   final void onHttp2Frame(ChannelHandlerContext ctx, Http2Frame frame)
/*      */   {
/*  231 */     if ((frame instanceof Http2StreamFrame)) {
/*  232 */       Http2StreamFrame streamFrame = (Http2StreamFrame)frame;
/*  233 */       ((Http2MultiplexCodecStream)streamFrame.stream()).channel.fireChildRead(streamFrame);
/*  234 */     } else if ((frame instanceof Http2GoAwayFrame)) {
/*  235 */       onHttp2GoAwayFrame(ctx, (Http2GoAwayFrame)frame);
/*      */       
/*  237 */       ctx.fireChannelRead(frame);
/*  238 */     } else if ((frame instanceof Http2SettingsFrame)) {
/*  239 */       Http2Settings settings = ((Http2SettingsFrame)frame).settings();
/*  240 */       if (settings.initialWindowSize() != null) {
/*  241 */         this.initialOutboundStreamWindow = settings.initialWindowSize().intValue();
/*      */       }
/*      */       
/*  244 */       ctx.fireChannelRead(frame);
/*      */     }
/*      */     else {
/*  247 */       ctx.fireChannelRead(frame);
/*      */     }
/*      */   }
/*      */   
/*      */   private void onHttp2UpgradeStreamInitialized(ChannelHandlerContext ctx, Http2MultiplexCodecStream stream) {
/*  252 */     assert (stream.state() == Http2Stream.State.HALF_CLOSED_LOCAL);
/*  253 */     DefaultHttp2StreamChannel ch = new DefaultHttp2StreamChannel(stream, true);
/*  254 */     ch.outboundClosed = true;
/*      */     
/*      */ 
/*      */ 
/*  258 */     ch.pipeline().addLast(new ChannelHandler[] { this.upgradeStreamHandler });
/*  259 */     ChannelFuture future = ctx.channel().eventLoop().register(ch);
/*  260 */     if (future.isDone()) {
/*  261 */       registerDone(future);
/*      */     } else {
/*  263 */       future.addListener(CHILD_CHANNEL_REGISTRATION_LISTENER);
/*      */     }
/*      */   }
/*      */   
/*      */   final void onHttp2StreamStateChanged(ChannelHandlerContext ctx, Http2FrameStream stream)
/*      */   {
/*  269 */     Http2MultiplexCodecStream s = (Http2MultiplexCodecStream)stream;
/*      */     
/*  271 */     switch (stream.state()) {
/*      */     case HALF_CLOSED_REMOTE: 
/*      */     case OPEN: 
/*  274 */       if (s.channel == null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  279 */         ChannelFuture future = ctx.channel().eventLoop().register(new DefaultHttp2StreamChannel(s, false));
/*  280 */         if (future.isDone()) {
/*  281 */           registerDone(future);
/*      */         } else
/*  283 */           future.addListener(CHILD_CHANNEL_REGISTRATION_LISTENER);
/*      */       }
/*  285 */       break;
/*      */     case CLOSED: 
/*  287 */       DefaultHttp2StreamChannel channel = s.channel;
/*  288 */       if (channel != null) {
/*  289 */         channel.streamClosed();
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */   final void onHttp2StreamWritabilityChanged(ChannelHandlerContext ctx, Http2FrameStream stream, boolean writable)
/*      */   {
/*  300 */     ((Http2MultiplexCodecStream)stream).channel.writabilityChanged(writable);
/*      */   }
/*      */   
/*      */   final Http2StreamChannel newOutboundStream()
/*      */   {
/*  305 */     return new DefaultHttp2StreamChannel(newStream(), true);
/*      */   }
/*      */   
/*      */   final void onHttp2FrameStreamException(ChannelHandlerContext ctx, Http2FrameStreamException cause)
/*      */   {
/*  310 */     Http2FrameStream stream = cause.stream();
/*  311 */     DefaultHttp2StreamChannel childChannel = ((Http2MultiplexCodecStream)stream).channel;
/*      */     try
/*      */     {
/*  314 */       childChannel.pipeline().fireExceptionCaught(cause.getCause());
/*      */     } finally {
/*  316 */       childChannel.unsafe().closeForcibly();
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isChildChannelInReadPendingQueue(DefaultHttp2StreamChannel childChannel) {
/*  321 */     return (childChannel.previous != null) || (childChannel.next != null) || (this.head == childChannel);
/*      */   }
/*      */   
/*      */   final void tryAddChildChannelToReadPendingQueue(DefaultHttp2StreamChannel childChannel) {
/*  325 */     if (!isChildChannelInReadPendingQueue(childChannel)) {
/*  326 */       addChildChannelToReadPendingQueue(childChannel);
/*      */     }
/*      */   }
/*      */   
/*      */   final void addChildChannelToReadPendingQueue(DefaultHttp2StreamChannel childChannel) {
/*  331 */     if (this.tail == null) {
/*  332 */       assert (this.head == null);
/*  333 */       this.tail = (this.head = childChannel);
/*      */     } else {
/*  335 */       childChannel.previous = this.tail;
/*  336 */       this.tail.next = childChannel;
/*  337 */       this.tail = childChannel;
/*      */     }
/*      */   }
/*      */   
/*      */   private void tryRemoveChildChannelFromReadPendingQueue(DefaultHttp2StreamChannel childChannel) {
/*  342 */     if (isChildChannelInReadPendingQueue(childChannel)) {
/*  343 */       removeChildChannelFromReadPendingQueue(childChannel);
/*      */     }
/*      */   }
/*      */   
/*      */   private void removeChildChannelFromReadPendingQueue(DefaultHttp2StreamChannel childChannel) {
/*  348 */     DefaultHttp2StreamChannel previous = childChannel.previous;
/*  349 */     if (childChannel.next != null) {
/*  350 */       childChannel.next.previous = previous;
/*      */     } else {
/*  352 */       this.tail = this.tail.previous;
/*      */     }
/*  354 */     if (previous != null) {
/*  355 */       previous.next = childChannel.next;
/*      */     } else {
/*  357 */       this.head = this.head.next;
/*      */     }
/*  359 */     childChannel.next = (childChannel.previous = null);
/*      */   }
/*      */   
/*      */   private void onHttp2GoAwayFrame(ChannelHandlerContext ctx, final Http2GoAwayFrame goAwayFrame) {
/*      */     try {
/*  364 */       forEachActiveStream(new Http2FrameStreamVisitor()
/*      */       {
/*      */         public boolean visit(Http2FrameStream stream) {
/*  367 */           int streamId = stream.id();
/*  368 */           Http2MultiplexCodec.DefaultHttp2StreamChannel childChannel = ((Http2MultiplexCodec.Http2MultiplexCodecStream)stream).channel;
/*  369 */           if ((streamId > goAwayFrame.lastStreamId()) && (Http2MultiplexCodec.this.connection().local().isValidStreamId(streamId))) {
/*  370 */             childChannel.pipeline().fireUserEventTriggered(goAwayFrame.retainedDuplicate());
/*      */           }
/*  372 */           return true;
/*      */         }
/*      */       });
/*      */     } catch (Http2Exception e) {
/*  376 */       ctx.fireExceptionCaught(e);
/*  377 */       ctx.close();
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public final void channelReadComplete(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: invokevirtual 90	io/netty/handler/codec/http2/Http2MultiplexCodec:onChannelReadComplete	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */     //   5: aload_0
/*      */     //   6: iconst_0
/*      */     //   7: putfield 5	io/netty/handler/codec/http2/Http2MultiplexCodec:parentReadInProgress	Z
/*      */     //   10: aload_0
/*      */     //   11: aload_0
/*      */     //   12: aconst_null
/*      */     //   13: dup_x1
/*      */     //   14: putfield 41	io/netty/handler/codec/http2/Http2MultiplexCodec:head	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */     //   17: putfield 44	io/netty/handler/codec/http2/Http2MultiplexCodec:tail	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */     //   20: aload_0
/*      */     //   21: aload_1
/*      */     //   22: invokevirtual 91	io/netty/handler/codec/http2/Http2MultiplexCodec:flush0	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */     //   25: goto +26 -> 51
/*      */     //   28: astore_2
/*      */     //   29: aload_0
/*      */     //   30: iconst_0
/*      */     //   31: putfield 5	io/netty/handler/codec/http2/Http2MultiplexCodec:parentReadInProgress	Z
/*      */     //   34: aload_0
/*      */     //   35: aload_0
/*      */     //   36: aconst_null
/*      */     //   37: dup_x1
/*      */     //   38: putfield 41	io/netty/handler/codec/http2/Http2MultiplexCodec:head	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */     //   41: putfield 44	io/netty/handler/codec/http2/Http2MultiplexCodec:tail	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */     //   44: aload_0
/*      */     //   45: aload_1
/*      */     //   46: invokevirtual 91	io/netty/handler/codec/http2/Http2MultiplexCodec:flush0	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */     //   49: aload_2
/*      */     //   50: athrow
/*      */     //   51: aload_0
/*      */     //   52: aload_1
/*      */     //   53: invokevirtual 92	io/netty/handler/codec/http2/Http2MultiplexCodec:channelReadComplete0	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */     //   56: return
/*      */     // Line number table:
/*      */     //   Java source line #387	-> byte code offset #0
/*      */     //   Java source line #389	-> byte code offset #5
/*      */     //   Java source line #390	-> byte code offset #10
/*      */     //   Java source line #392	-> byte code offset #20
/*      */     //   Java source line #393	-> byte code offset #25
/*      */     //   Java source line #389	-> byte code offset #28
/*      */     //   Java source line #390	-> byte code offset #34
/*      */     //   Java source line #392	-> byte code offset #44
/*      */     //   Java source line #393	-> byte code offset #49
/*      */     //   Java source line #394	-> byte code offset #51
/*      */     //   Java source line #395	-> byte code offset #56
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	57	0	this	Http2MultiplexCodec
/*      */     //   0	57	1	ctx	ChannelHandlerContext
/*      */     //   28	22	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	5	28	finally
/*      */   }
/*      */   
/*      */   public final void channelRead(ChannelHandlerContext ctx, Object msg)
/*      */     throws Exception
/*      */   {
/*  399 */     this.parentReadInProgress = true;
/*  400 */     super.channelRead(ctx, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final void onChannelReadComplete(ChannelHandlerContext ctx)
/*      */   {
/*  407 */     DefaultHttp2StreamChannel current = this.head;
/*  408 */     while (current != null) {
/*  409 */       DefaultHttp2StreamChannel childChannel = current;
/*      */       
/*  411 */       current = current.next;
/*  412 */       childChannel.next = (childChannel.previous = null);
/*  413 */       childChannel.fireChildReadComplete();
/*      */     }
/*      */   }
/*      */   
/*      */   void flush0(ChannelHandlerContext ctx)
/*      */   {
/*  419 */     flush(ctx);
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
/*      */   boolean onBytesConsumed(ChannelHandlerContext ctx, Http2FrameStream stream, int bytes)
/*      */     throws Http2Exception
/*      */   {
/*  434 */     return consumeBytes(stream.id(), bytes);
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
/*  446 */   private boolean initialWritability(Http2FrameCodec.DefaultHttp2FrameStream stream) { return (!Http2CodecUtil.isStreamIdValid(stream.id())) || (isWritable(stream)); }
/*      */   
/*      */   static class Http2MultiplexCodecStream extends Http2FrameCodec.DefaultHttp2FrameStream { Http2MultiplexCodec.DefaultHttp2StreamChannel channel;
/*      */   }
/*      */   
/*  451 */   private final class DefaultHttp2StreamChannel extends DefaultAttributeMap implements Http2StreamChannel { private final Http2StreamChannelConfig config = new Http2StreamChannelConfig(this);
/*  452 */     private final Http2ChannelUnsafe unsafe = new Http2ChannelUnsafe(null);
/*      */     
/*      */ 
/*      */     private final ChannelId channelId;
/*      */     
/*      */     private final ChannelPipeline pipeline;
/*      */     
/*      */     private final Http2FrameCodec.DefaultHttp2FrameStream stream;
/*      */     
/*      */     private final ChannelPromise closePromise;
/*      */     
/*      */     private final boolean outbound;
/*      */     
/*      */     private volatile boolean registered;
/*      */     
/*      */     private volatile boolean writable;
/*      */     
/*      */     private boolean outboundClosed;
/*      */     
/*      */     private boolean readInProgress;
/*      */     
/*      */     private Queue<Object> inboundBuffer;
/*      */     
/*      */     private boolean firstFrameWritten;
/*      */     
/*      */     DefaultHttp2StreamChannel next;
/*      */     
/*      */     DefaultHttp2StreamChannel previous;
/*      */     
/*      */ 
/*      */     DefaultHttp2StreamChannel(Http2FrameCodec.DefaultHttp2FrameStream stream, boolean outbound)
/*      */     {
/*  484 */       this.stream = stream;
/*  485 */       this.outbound = outbound;
/*  486 */       this.writable = Http2MultiplexCodec.this.initialWritability(stream);
/*  487 */       ((Http2MultiplexCodec.Http2MultiplexCodecStream)stream).channel = this;
/*  488 */       this.pipeline = new DefaultChannelPipeline(this)
/*      */       {
/*      */         protected void incrementPendingOutboundBytes(long size) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         protected void decrementPendingOutboundBytes(long size) {}
/*  498 */       };
/*  499 */       this.closePromise = this.pipeline.newPromise();
/*  500 */       this.channelId = new Http2StreamChannelId(parent().id(), Http2MultiplexCodec.access$404(Http2MultiplexCodec.this));
/*      */     }
/*      */     
/*      */     public Http2FrameStream stream()
/*      */     {
/*  505 */       return this.stream;
/*      */     }
/*      */     
/*      */     void streamClosed() {
/*  509 */       this.unsafe.readEOS();
/*      */       
/*      */ 
/*  512 */       this.unsafe.doBeginRead();
/*      */     }
/*      */     
/*      */     public ChannelMetadata metadata()
/*      */     {
/*  517 */       return Http2MultiplexCodec.METADATA;
/*      */     }
/*      */     
/*      */     public ChannelConfig config()
/*      */     {
/*  522 */       return this.config;
/*      */     }
/*      */     
/*      */     public boolean isOpen()
/*      */     {
/*  527 */       return !this.closePromise.isDone();
/*      */     }
/*      */     
/*      */     public boolean isActive()
/*      */     {
/*  532 */       return isOpen();
/*      */     }
/*      */     
/*      */     public boolean isWritable()
/*      */     {
/*  537 */       return this.writable;
/*      */     }
/*      */     
/*      */     public ChannelId id()
/*      */     {
/*  542 */       return this.channelId;
/*      */     }
/*      */     
/*      */     public EventLoop eventLoop()
/*      */     {
/*  547 */       return parent().eventLoop();
/*      */     }
/*      */     
/*      */     public Channel parent()
/*      */     {
/*  552 */       return Http2MultiplexCodec.this.ctx.channel();
/*      */     }
/*      */     
/*      */     public boolean isRegistered()
/*      */     {
/*  557 */       return this.registered;
/*      */     }
/*      */     
/*      */     public SocketAddress localAddress()
/*      */     {
/*  562 */       return parent().localAddress();
/*      */     }
/*      */     
/*      */     public SocketAddress remoteAddress()
/*      */     {
/*  567 */       return parent().remoteAddress();
/*      */     }
/*      */     
/*      */     public ChannelFuture closeFuture()
/*      */     {
/*  572 */       return this.closePromise;
/*      */     }
/*      */     
/*      */ 
/*      */     public long bytesBeforeUnwritable()
/*      */     {
/*  578 */       return config().getWriteBufferHighWaterMark();
/*      */     }
/*      */     
/*      */ 
/*      */     public long bytesBeforeWritable()
/*      */     {
/*  584 */       return 0L;
/*      */     }
/*      */     
/*      */     public Channel.Unsafe unsafe()
/*      */     {
/*  589 */       return this.unsafe;
/*      */     }
/*      */     
/*      */     public ChannelPipeline pipeline()
/*      */     {
/*  594 */       return this.pipeline;
/*      */     }
/*      */     
/*      */     public ByteBufAllocator alloc()
/*      */     {
/*  599 */       return config().getAllocator();
/*      */     }
/*      */     
/*      */     public Channel read()
/*      */     {
/*  604 */       pipeline().read();
/*  605 */       return this;
/*      */     }
/*      */     
/*      */     public Channel flush()
/*      */     {
/*  610 */       pipeline().flush();
/*  611 */       return this;
/*      */     }
/*      */     
/*      */     public ChannelFuture bind(SocketAddress localAddress)
/*      */     {
/*  616 */       return pipeline().bind(localAddress);
/*      */     }
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress)
/*      */     {
/*  621 */       return pipeline().connect(remoteAddress);
/*      */     }
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*      */     {
/*  626 */       return pipeline().connect(remoteAddress, localAddress);
/*      */     }
/*      */     
/*      */     public ChannelFuture disconnect()
/*      */     {
/*  631 */       return pipeline().disconnect();
/*      */     }
/*      */     
/*      */     public ChannelFuture close()
/*      */     {
/*  636 */       return pipeline().close();
/*      */     }
/*      */     
/*      */     public ChannelFuture deregister()
/*      */     {
/*  641 */       return pipeline().deregister();
/*      */     }
/*      */     
/*      */     public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
/*      */     {
/*  646 */       return pipeline().bind(localAddress, promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*      */     {
/*  651 */       return pipeline().connect(remoteAddress, promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */     {
/*  656 */       return pipeline().connect(remoteAddress, localAddress, promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture disconnect(ChannelPromise promise)
/*      */     {
/*  661 */       return pipeline().disconnect(promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture close(ChannelPromise promise)
/*      */     {
/*  666 */       return pipeline().close(promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture deregister(ChannelPromise promise)
/*      */     {
/*  671 */       return pipeline().deregister(promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture write(Object msg)
/*      */     {
/*  676 */       return pipeline().write(msg);
/*      */     }
/*      */     
/*      */     public ChannelFuture write(Object msg, ChannelPromise promise)
/*      */     {
/*  681 */       return pipeline().write(msg, promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*      */     {
/*  686 */       return pipeline().writeAndFlush(msg, promise);
/*      */     }
/*      */     
/*      */     public ChannelFuture writeAndFlush(Object msg)
/*      */     {
/*  691 */       return pipeline().writeAndFlush(msg);
/*      */     }
/*      */     
/*      */     public ChannelPromise newPromise()
/*      */     {
/*  696 */       return pipeline().newPromise();
/*      */     }
/*      */     
/*      */     public ChannelProgressivePromise newProgressivePromise()
/*      */     {
/*  701 */       return pipeline().newProgressivePromise();
/*      */     }
/*      */     
/*      */     public ChannelFuture newSucceededFuture()
/*      */     {
/*  706 */       return pipeline().newSucceededFuture();
/*      */     }
/*      */     
/*      */     public ChannelFuture newFailedFuture(Throwable cause)
/*      */     {
/*  711 */       return pipeline().newFailedFuture(cause);
/*      */     }
/*      */     
/*      */     public ChannelPromise voidPromise()
/*      */     {
/*  716 */       return pipeline().voidPromise();
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  721 */       return id().hashCode();
/*      */     }
/*      */     
/*      */     public boolean equals(Object o)
/*      */     {
/*  726 */       return this == o;
/*      */     }
/*      */     
/*      */     public int compareTo(Channel o)
/*      */     {
/*  731 */       if (this == o) {
/*  732 */         return 0;
/*      */       }
/*      */       
/*  735 */       return id().compareTo(o.id());
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  740 */       return parent().toString() + "(H2 - " + this.stream + ')';
/*      */     }
/*      */     
/*      */     void writabilityChanged(boolean writable) {
/*  744 */       assert (eventLoop().inEventLoop());
/*  745 */       if ((writable != this.writable) && (isActive()))
/*      */       {
/*  747 */         this.writable = writable;
/*  748 */         pipeline().fireChannelWritabilityChanged();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     void fireChildRead(Http2Frame frame)
/*      */     {
/*  757 */       assert (eventLoop().inEventLoop());
/*  758 */       if (!isActive()) {
/*  759 */         ReferenceCountUtil.release(frame);
/*  760 */       } else if (this.readInProgress)
/*      */       {
/*      */ 
/*  763 */         assert ((this.inboundBuffer == null) || (this.inboundBuffer.isEmpty()));
/*  764 */         RecvByteBufAllocator.Handle allocHandle = this.unsafe.recvBufAllocHandle();
/*  765 */         this.unsafe.doRead0(frame, allocHandle);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  770 */         if (allocHandle.continueReading()) {
/*  771 */           Http2MultiplexCodec.this.tryAddChildChannelToReadPendingQueue(this);
/*      */         } else {
/*  773 */           Http2MultiplexCodec.this.tryRemoveChildChannelFromReadPendingQueue(this);
/*  774 */           this.unsafe.notifyReadComplete(allocHandle);
/*      */         }
/*      */       } else {
/*  777 */         if (this.inboundBuffer == null) {
/*  778 */           this.inboundBuffer = new ArrayDeque(4);
/*      */         }
/*  780 */         this.inboundBuffer.add(frame);
/*      */       }
/*      */     }
/*      */     
/*      */     void fireChildReadComplete() {
/*  785 */       assert (eventLoop().inEventLoop());
/*  786 */       assert (this.readInProgress);
/*  787 */       this.unsafe.notifyReadComplete(this.unsafe.recvBufAllocHandle());
/*      */     }
/*      */     
/*      */     private final class Http2ChannelUnsafe implements Channel.Unsafe {
/*  791 */       private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(Http2MultiplexCodec.DefaultHttp2StreamChannel.this, false);
/*      */       
/*      */       private RecvByteBufAllocator.Handle recvHandle;
/*      */       private boolean writeDoneAndNoFlush;
/*      */       private boolean closeInitiated;
/*      */       private boolean readEOS;
/*      */       
/*      */       private Http2ChannelUnsafe() {}
/*      */       
/*      */       public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */       {
/*  802 */         if (!promise.setUncancellable()) {
/*  803 */           return;
/*      */         }
/*  805 */         promise.setFailure(new UnsupportedOperationException());
/*      */       }
/*      */       
/*      */       public RecvByteBufAllocator.Handle recvBufAllocHandle()
/*      */       {
/*  810 */         if (this.recvHandle == null) {
/*  811 */           this.recvHandle = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config().getRecvByteBufAllocator().newHandle();
/*  812 */           this.recvHandle.reset(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config());
/*      */         }
/*  814 */         return this.recvHandle;
/*      */       }
/*      */       
/*      */       public SocketAddress localAddress()
/*      */       {
/*  819 */         return Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().unsafe().localAddress();
/*      */       }
/*      */       
/*      */       public SocketAddress remoteAddress()
/*      */       {
/*  824 */         return Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().unsafe().remoteAddress();
/*      */       }
/*      */       
/*      */       public void register(EventLoop eventLoop, ChannelPromise promise)
/*      */       {
/*  829 */         if (!promise.setUncancellable()) {
/*  830 */           return;
/*      */         }
/*  832 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered) {
/*  833 */           throw new UnsupportedOperationException("Re-register is not supported");
/*      */         }
/*      */         
/*  836 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered = true;
/*      */         
/*  838 */         if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outbound)
/*      */         {
/*  840 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().addLast(new ChannelHandler[] { Http2MultiplexCodec.this.inboundStreamHandler });
/*      */         }
/*      */         
/*  843 */         promise.setSuccess();
/*      */         
/*  845 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelRegistered();
/*  846 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive()) {
/*  847 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelActive();
/*      */         }
/*      */       }
/*      */       
/*      */       public void bind(SocketAddress localAddress, ChannelPromise promise)
/*      */       {
/*  853 */         if (!promise.setUncancellable()) {
/*  854 */           return;
/*      */         }
/*  856 */         promise.setFailure(new UnsupportedOperationException());
/*      */       }
/*      */       
/*      */       public void disconnect(ChannelPromise promise)
/*      */       {
/*  861 */         close(promise);
/*      */       }
/*      */       
/*      */       public void close(final ChannelPromise promise)
/*      */       {
/*  866 */         if (!promise.setUncancellable()) {
/*  867 */           return;
/*      */         }
/*  869 */         if (this.closeInitiated) {
/*  870 */           if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.isDone())
/*      */           {
/*  872 */             promise.setSuccess();
/*  873 */           } else if (!(promise instanceof VoidChannelPromise))
/*      */           {
/*  875 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.addListener(new ChannelFutureListener()
/*      */             {
/*      */               public void operationComplete(ChannelFuture future) {
/*  878 */                 promise.setSuccess();
/*      */               }
/*      */             });
/*      */           }
/*  882 */           return;
/*      */         }
/*  884 */         this.closeInitiated = true;
/*      */         
/*  886 */         Http2MultiplexCodec.this.tryRemoveChildChannelFromReadPendingQueue(Http2MultiplexCodec.DefaultHttp2StreamChannel.this);
/*      */         
/*  888 */         boolean wasActive = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive();
/*      */         
/*      */ 
/*      */ 
/*  892 */         if ((Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().isActive()) && (!this.readEOS) && (Http2MultiplexCodec.this.connection().streamMayHaveExisted(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream().id()))) {
/*  893 */           Http2StreamFrame resetFrame = new DefaultHttp2ResetFrame(Http2Error.CANCEL).stream(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream());
/*  894 */           write(resetFrame, Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*  895 */           flush();
/*      */         }
/*      */         
/*  898 */         if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer != null) {
/*      */           for (;;) {
/*  900 */             Object msg = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll();
/*  901 */             if (msg == null) {
/*      */               break;
/*      */             }
/*  904 */             ReferenceCountUtil.release(msg);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  909 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed = true;
/*  910 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.closePromise.setSuccess();
/*  911 */         promise.setSuccess();
/*      */         
/*  913 */         fireChannelInactiveAndDeregister(voidPromise(), wasActive);
/*      */       }
/*      */       
/*      */       public void closeForcibly()
/*      */       {
/*  918 */         close(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe().voidPromise());
/*      */       }
/*      */       
/*      */       public void deregister(ChannelPromise promise)
/*      */       {
/*  923 */         fireChannelInactiveAndDeregister(promise, false);
/*      */       }
/*      */       
/*      */       private void fireChannelInactiveAndDeregister(final ChannelPromise promise, final boolean fireChannelInactive)
/*      */       {
/*  928 */         if (!promise.setUncancellable()) {
/*  929 */           return;
/*      */         }
/*      */         
/*  932 */         if (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered) {
/*  933 */           promise.setSuccess();
/*  934 */           return;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  944 */         invokeLater(new Runnable()
/*      */         {
/*      */           public void run() {
/*  947 */             if (fireChannelInactive) {
/*  948 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline.fireChannelInactive();
/*      */             }
/*      */             
/*      */ 
/*  952 */             if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered) {
/*  953 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.registered = false;
/*  954 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline.fireChannelUnregistered();
/*      */             }
/*  956 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.safeSetSuccess(promise);
/*      */           }
/*      */         });
/*      */       }
/*      */       
/*      */       private void safeSetSuccess(ChannelPromise promise) {
/*  962 */         if ((!(promise instanceof VoidChannelPromise)) && (!promise.trySuccess())) {
/*  963 */           Http2MultiplexCodec.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       private void invokeLater(Runnable task)
/*      */       {
/*      */         try
/*      */         {
/*  980 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.eventLoop().execute(task);
/*      */         } catch (RejectedExecutionException e) {
/*  982 */           Http2MultiplexCodec.logger.warn("Can't invoke task later as EventLoop rejected it", e);
/*      */         }
/*      */       }
/*      */       
/*      */       public void beginRead()
/*      */       {
/*  988 */         if ((Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress) || (!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive())) {
/*  989 */           return;
/*      */         }
/*  991 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress = true;
/*  992 */         doBeginRead();
/*      */       }
/*      */       
/*      */       void doBeginRead() {
/*      */         Object message;
/*  997 */         if ((Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer == null) || ((message = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll()) == null)) {
/*  998 */           if (this.readEOS)
/*  999 */             Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe.closeForcibly();
/*      */         } else {
/*      */           Object message;
/* 1002 */           RecvByteBufAllocator.Handle allocHandle = recvBufAllocHandle();
/* 1003 */           allocHandle.reset(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config());
/* 1004 */           boolean continueReading = false;
/*      */           do {
/* 1006 */             doRead0((Http2Frame)message, allocHandle);
/* 1007 */           } while (((this.readEOS) || ((continueReading = allocHandle.continueReading()))) && 
/* 1008 */             ((message = Http2MultiplexCodec.DefaultHttp2StreamChannel.this.inboundBuffer.poll()) != null));
/*      */           
/* 1010 */           if ((continueReading) && (Http2MultiplexCodec.this.parentReadInProgress) && (!this.readEOS))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1015 */             assert (!Http2MultiplexCodec.this.isChildChannelInReadPendingQueue(Http2MultiplexCodec.DefaultHttp2StreamChannel.this));
/* 1016 */             Http2MultiplexCodec.this.addChildChannelToReadPendingQueue(Http2MultiplexCodec.DefaultHttp2StreamChannel.this);
/*      */           } else {
/* 1018 */             notifyReadComplete(allocHandle);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       void readEOS() {
/* 1024 */         this.readEOS = true;
/*      */       }
/*      */       
/*      */       void notifyReadComplete(RecvByteBufAllocator.Handle allocHandle) {
/* 1028 */         assert ((Http2MultiplexCodec.DefaultHttp2StreamChannel.this.next == null) && (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.previous == null));
/* 1029 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.readInProgress = false;
/* 1030 */         allocHandle.readComplete();
/* 1031 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelReadComplete();
/*      */         
/*      */ 
/*      */ 
/* 1035 */         flush();
/* 1036 */         if (this.readEOS) {
/* 1037 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.unsafe.closeForcibly();
/*      */         }
/*      */       }
/*      */       
/*      */       void doRead0(Http2Frame frame, RecvByteBufAllocator.Handle allocHandle)
/*      */       {
/* 1043 */         Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireChannelRead(frame);
/* 1044 */         allocHandle.incMessagesRead(1);
/*      */         
/* 1046 */         if ((frame instanceof Http2DataFrame)) {
/* 1047 */           int numBytesToBeConsumed = ((Http2DataFrame)frame).initialFlowControlledBytes();
/* 1048 */           allocHandle.attemptedBytesRead(numBytesToBeConsumed);
/* 1049 */           allocHandle.lastBytesRead(numBytesToBeConsumed);
/* 1050 */           if (numBytesToBeConsumed != 0) {
/*      */             try {
/* 1052 */               this.writeDoneAndNoFlush |= Http2MultiplexCodec.this.onBytesConsumed(Http2MultiplexCodec.this.ctx, Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream, numBytesToBeConsumed);
/*      */             } catch (Http2Exception e) {
/* 1054 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.pipeline().fireExceptionCaught(e);
/*      */             }
/*      */           }
/*      */         } else {
/* 1058 */           allocHandle.attemptedBytesRead(9);
/* 1059 */           allocHandle.lastBytesRead(9);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       public void write(Object msg, final ChannelPromise promise)
/*      */       {
/* 1066 */         if (!promise.setUncancellable()) {
/* 1067 */           ReferenceCountUtil.release(msg);
/* 1068 */           return;
/*      */         }
/*      */         
/* 1071 */         if ((!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.isActive()) || (
/*      */         
/* 1073 */           (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed) && (((msg instanceof Http2HeadersFrame)) || ((msg instanceof Http2DataFrame))))) {
/* 1074 */           ReferenceCountUtil.release(msg);
/* 1075 */           promise.setFailure(Http2MultiplexCodec.CLOSED_CHANNEL_EXCEPTION);
/* 1076 */           return;
/*      */         }
/*      */         try
/*      */         {
/* 1080 */           if ((msg instanceof Http2StreamFrame)) {
/* 1081 */             Http2StreamFrame frame = validateStreamFrame((Http2StreamFrame)msg).stream(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream());
/* 1082 */             if ((!Http2MultiplexCodec.DefaultHttp2StreamChannel.this.firstFrameWritten) && (!Http2CodecUtil.isStreamIdValid(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream().id()))) {
/* 1083 */               if (!(frame instanceof Http2HeadersFrame)) {
/* 1084 */                 ReferenceCountUtil.release(frame);
/* 1085 */                 promise.setFailure(new IllegalArgumentException("The first frame must be a headers frame. Was: " + frame
/*      */                 
/* 1087 */                   .name()));
/* 1088 */                 return;
/*      */               }
/* 1090 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.firstFrameWritten = true;
/* 1091 */               ChannelFuture future = write0(frame);
/* 1092 */               if (future.isDone()) {
/* 1093 */                 firstWriteComplete(future, promise);
/*      */               } else {
/* 1095 */                 future.addListener(new ChannelFutureListener()
/*      */                 {
/*      */                   public void operationComplete(ChannelFuture future) {
/* 1098 */                     Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.firstWriteComplete(future, promise);
/*      */                   }
/*      */                 });
/*      */               }
/* 1102 */               return;
/*      */             }
/*      */           } else {
/* 1105 */             String msgStr = msg.toString();
/* 1106 */             ReferenceCountUtil.release(msg);
/* 1107 */             promise.setFailure(new IllegalArgumentException("Message must be an " + 
/* 1108 */               StringUtil.simpleClassName(Http2StreamFrame.class) + ": " + msgStr));
/*      */             
/* 1110 */             return;
/*      */           }
/*      */           
/* 1113 */           ChannelFuture future = write0(msg);
/* 1114 */           if (future.isDone()) {
/* 1115 */             writeComplete(future, promise);
/*      */           } else {
/* 1117 */             future.addListener(new ChannelFutureListener()
/*      */             {
/*      */               public void operationComplete(ChannelFuture future) {
/* 1120 */                 Http2MultiplexCodec.DefaultHttp2StreamChannel.Http2ChannelUnsafe.this.writeComplete(future, promise);
/*      */               }
/*      */             });
/*      */           }
/*      */         } catch (Throwable t) {
/* 1125 */           promise.tryFailure(t);
/*      */         } finally {
/* 1127 */           this.writeDoneAndNoFlush = true;
/*      */         }
/*      */       }
/*      */       
/*      */       private void firstWriteComplete(ChannelFuture future, ChannelPromise promise) {
/* 1132 */         Throwable cause = future.cause();
/* 1133 */         if (cause == null)
/*      */         {
/*      */ 
/* 1136 */           Http2MultiplexCodec.DefaultHttp2StreamChannel.this.writabilityChanged(Http2MultiplexCodec.this.isWritable(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream));
/* 1137 */           promise.setSuccess();
/*      */         }
/*      */         else {
/* 1140 */           closeForcibly();
/* 1141 */           promise.setFailure(wrapStreamClosedError(cause));
/*      */         }
/*      */       }
/*      */       
/*      */       private void writeComplete(ChannelFuture future, ChannelPromise promise) {
/* 1146 */         Throwable cause = future.cause();
/* 1147 */         if (cause == null) {
/* 1148 */           promise.setSuccess();
/*      */         } else {
/* 1150 */           Throwable error = wrapStreamClosedError(cause);
/* 1151 */           if ((error instanceof ClosedChannelException)) {
/* 1152 */             if (Http2MultiplexCodec.DefaultHttp2StreamChannel.this.config.isAutoClose())
/*      */             {
/* 1154 */               closeForcibly();
/*      */             } else {
/* 1156 */               Http2MultiplexCodec.DefaultHttp2StreamChannel.this.outboundClosed = true;
/*      */             }
/*      */           }
/* 1159 */           promise.setFailure(error);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       private Throwable wrapStreamClosedError(Throwable cause)
/*      */       {
/* 1166 */         if (((cause instanceof Http2Exception)) && (((Http2Exception)cause).error() == Http2Error.STREAM_CLOSED)) {
/* 1167 */           return new ClosedChannelException().initCause(cause);
/*      */         }
/* 1169 */         return cause;
/*      */       }
/*      */       
/*      */       private Http2StreamFrame validateStreamFrame(Http2StreamFrame frame) {
/* 1173 */         if ((frame.stream() != null) && (frame.stream() != Http2MultiplexCodec.DefaultHttp2StreamChannel.this.stream)) {
/* 1174 */           String msgString = frame.toString();
/* 1175 */           ReferenceCountUtil.release(frame);
/*      */           
/* 1177 */           throw new IllegalArgumentException("Stream " + frame.stream() + " must not be set on the frame: " + msgString);
/*      */         }
/* 1179 */         return frame;
/*      */       }
/*      */       
/*      */       private ChannelFuture write0(Object msg) {
/* 1183 */         ChannelPromise promise = Http2MultiplexCodec.this.ctx.newPromise();
/* 1184 */         Http2MultiplexCodec.this.write(Http2MultiplexCodec.this.ctx, msg, promise);
/* 1185 */         return promise;
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public void flush()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 104	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel$Http2ChannelUnsafe:writeDoneAndNoFlush	Z
/*      */         //   4: ifeq +16 -> 20
/*      */         //   7: aload_0
/*      */         //   8: getfield 6	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel$Http2ChannelUnsafe:this$1	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */         //   11: getfield 31	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel:this$0	Lio/netty/handler/codec/http2/Http2MultiplexCodec;
/*      */         //   14: invokestatic 87	io/netty/handler/codec/http2/Http2MultiplexCodec:access$1700	(Lio/netty/handler/codec/http2/Http2MultiplexCodec;)Z
/*      */         //   17: ifeq +4 -> 21
/*      */         //   20: return
/*      */         //   21: aload_0
/*      */         //   22: getfield 6	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel$Http2ChannelUnsafe:this$1	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */         //   25: getfield 31	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel:this$0	Lio/netty/handler/codec/http2/Http2MultiplexCodec;
/*      */         //   28: aload_0
/*      */         //   29: getfield 6	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel$Http2ChannelUnsafe:this$1	Lio/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel;
/*      */         //   32: getfield 31	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel:this$0	Lio/netty/handler/codec/http2/Http2MultiplexCodec;
/*      */         //   35: getfield 105	io/netty/handler/codec/http2/Http2MultiplexCodec:ctx	Lio/netty/channel/ChannelHandlerContext;
/*      */         //   38: invokevirtual 157	io/netty/handler/codec/http2/Http2MultiplexCodec:flush0	(Lio/netty/channel/ChannelHandlerContext;)V
/*      */         //   41: aload_0
/*      */         //   42: iconst_0
/*      */         //   43: putfield 104	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel$Http2ChannelUnsafe:writeDoneAndNoFlush	Z
/*      */         //   46: goto +11 -> 57
/*      */         //   49: astore_1
/*      */         //   50: aload_0
/*      */         //   51: iconst_0
/*      */         //   52: putfield 104	io/netty/handler/codec/http2/Http2MultiplexCodec$DefaultHttp2StreamChannel$Http2ChannelUnsafe:writeDoneAndNoFlush	Z
/*      */         //   55: aload_1
/*      */         //   56: athrow
/*      */         //   57: return
/*      */         // Line number table:
/*      */         //   Java source line #1194	-> byte code offset #0
/*      */         //   Java source line #1196	-> byte code offset #20
/*      */         //   Java source line #1199	-> byte code offset #21
/*      */         //   Java source line #1201	-> byte code offset #41
/*      */         //   Java source line #1202	-> byte code offset #46
/*      */         //   Java source line #1201	-> byte code offset #49
/*      */         //   Java source line #1202	-> byte code offset #55
/*      */         //   Java source line #1203	-> byte code offset #57
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	58	0	this	Http2ChannelUnsafe
/*      */         //   49	7	1	localObject	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   21	41	49	finally
/*      */       }
/*      */       
/*      */       public ChannelPromise voidPromise()
/*      */       {
/* 1207 */         return this.unsafeVoidPromise;
/*      */       }
/*      */       
/*      */ 
/*      */       public ChannelOutboundBuffer outboundBuffer()
/*      */       {
/* 1213 */         return null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private final class Http2StreamChannelConfig
/*      */       extends DefaultChannelConfig
/*      */     {
/*      */       Http2StreamChannelConfig(Channel channel)
/*      */       {
/* 1224 */         super();
/*      */       }
/*      */       
/*      */       public int getWriteBufferHighWaterMark()
/*      */       {
/* 1229 */         return Math.min(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().config().getWriteBufferHighWaterMark(), Http2MultiplexCodec.this.initialOutboundStreamWindow);
/*      */       }
/*      */       
/*      */       public int getWriteBufferLowWaterMark()
/*      */       {
/* 1234 */         return Math.min(Http2MultiplexCodec.DefaultHttp2StreamChannel.this.parent().config().getWriteBufferLowWaterMark(), Http2MultiplexCodec.this.initialOutboundStreamWindow);
/*      */       }
/*      */       
/*      */       public MessageSizeEstimator getMessageSizeEstimator()
/*      */       {
/* 1239 */         return Http2MultiplexCodec.FlowControlledFrameSizeEstimator.INSTANCE;
/*      */       }
/*      */       
/*      */       public WriteBufferWaterMark getWriteBufferWaterMark()
/*      */       {
/* 1244 */         int mark = getWriteBufferHighWaterMark();
/* 1245 */         return new WriteBufferWaterMark(mark, mark);
/*      */       }
/*      */       
/*      */       public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*      */       {
/* 1250 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       @Deprecated
/*      */       public ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*      */       {
/* 1256 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       @Deprecated
/*      */       public ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*      */       {
/* 1262 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       public ChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark)
/*      */       {
/* 1267 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*      */       {
/* 1272 */         if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
/* 1273 */           throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
/*      */         }
/*      */         
/* 1276 */         super.setRecvByteBufAllocator(allocator);
/* 1277 */         return this;
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2MultiplexCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */