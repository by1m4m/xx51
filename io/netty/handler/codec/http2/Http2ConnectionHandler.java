/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class Http2ConnectionHandler
/*     */   extends ByteToMessageDecoder
/*     */   implements Http2LifecycleManager, ChannelOutboundHandler
/*     */ {
/*  69 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Http2ConnectionHandler.class);
/*     */   
/*  71 */   private static final Http2Headers HEADERS_TOO_LARGE_HEADERS = ReadOnlyHttp2Headers.serverHeaders(false, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE
/*  72 */     .codeAsText(), new AsciiString[0]);
/*  73 */   private static final ByteBuf HTTP_1_X_BUF = Unpooled.unreleasableBuffer(
/*  74 */     Unpooled.wrappedBuffer(new byte[] { 72, 84, 84, 80, 47, 49, 46 })).asReadOnly();
/*     */   
/*     */   private final Http2ConnectionDecoder decoder;
/*     */   private final Http2ConnectionEncoder encoder;
/*     */   private final Http2Settings initialSettings;
/*     */   private ChannelFutureListener closeListener;
/*     */   private BaseDecoder byteDecoder;
/*     */   private long gracefulShutdownTimeoutMillis;
/*     */   
/*     */   protected Http2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings)
/*     */   {
/*  85 */     this.initialSettings = ((Http2Settings)ObjectUtil.checkNotNull(initialSettings, "initialSettings"));
/*  86 */     this.decoder = ((Http2ConnectionDecoder)ObjectUtil.checkNotNull(decoder, "decoder"));
/*  87 */     this.encoder = ((Http2ConnectionEncoder)ObjectUtil.checkNotNull(encoder, "encoder"));
/*  88 */     if (encoder.connection() != decoder.connection()) {
/*  89 */       throw new IllegalArgumentException("Encoder and Decoder do not share the same connection object");
/*     */     }
/*     */   }
/*     */   
/*     */   Http2ConnectionHandler(boolean server, Http2FrameWriter frameWriter, Http2FrameLogger frameLogger, Http2Settings initialSettings)
/*     */   {
/*  95 */     this.initialSettings = ((Http2Settings)ObjectUtil.checkNotNull(initialSettings, "initialSettings"));
/*     */     
/*  97 */     Http2Connection connection = new DefaultHttp2Connection(server);
/*     */     
/*  99 */     Long maxHeaderListSize = initialSettings.maxHeaderListSize();
/*     */     
/*     */ 
/* 102 */     Http2FrameReader frameReader = new DefaultHttp2FrameReader(maxHeaderListSize == null ? new DefaultHttp2HeadersDecoder(true) : new DefaultHttp2HeadersDecoder(true, maxHeaderListSize.longValue()));
/*     */     
/* 104 */     if (frameLogger != null) {
/* 105 */       frameWriter = new Http2OutboundFrameLogger(frameWriter, frameLogger);
/* 106 */       frameReader = new Http2InboundFrameLogger(frameReader, frameLogger);
/*     */     }
/* 108 */     this.encoder = new DefaultHttp2ConnectionEncoder(connection, frameWriter);
/* 109 */     this.decoder = new DefaultHttp2ConnectionDecoder(connection, this.encoder, frameReader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long gracefulShutdownTimeoutMillis()
/*     */   {
/* 118 */     return this.gracefulShutdownTimeoutMillis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis)
/*     */   {
/* 128 */     if (gracefulShutdownTimeoutMillis < -1L) {
/* 129 */       throw new IllegalArgumentException("gracefulShutdownTimeoutMillis: " + gracefulShutdownTimeoutMillis + " (expected: -1 for indefinite or >= 0)");
/*     */     }
/*     */     
/* 132 */     this.gracefulShutdownTimeoutMillis = gracefulShutdownTimeoutMillis;
/*     */   }
/*     */   
/*     */   public Http2Connection connection() {
/* 136 */     return this.encoder.connection();
/*     */   }
/*     */   
/*     */   public Http2ConnectionDecoder decoder() {
/* 140 */     return this.decoder;
/*     */   }
/*     */   
/*     */   public Http2ConnectionEncoder encoder() {
/* 144 */     return this.encoder;
/*     */   }
/*     */   
/*     */   private boolean prefaceSent() {
/* 148 */     return (this.byteDecoder != null) && (this.byteDecoder.prefaceSent());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHttpClientUpgrade()
/*     */     throws Http2Exception
/*     */   {
/* 156 */     if (connection().isServer()) {
/* 157 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client-side HTTP upgrade requested for a server", new Object[0]);
/*     */     }
/* 159 */     if (!prefaceSent())
/*     */     {
/*     */ 
/* 162 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "HTTP upgrade must occur after preface was sent", new Object[0]);
/*     */     }
/* 164 */     if (this.decoder.prefaceReceived()) {
/* 165 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is received", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 169 */     connection().local().createStream(1, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHttpServerUpgrade(Http2Settings settings)
/*     */     throws Http2Exception
/*     */   {
/* 177 */     if (!connection().isServer()) {
/* 178 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server-side HTTP upgrade requested for a client", new Object[0]);
/*     */     }
/* 180 */     if (!prefaceSent())
/*     */     {
/*     */ 
/* 183 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "HTTP upgrade must occur after preface was sent", new Object[0]);
/*     */     }
/* 185 */     if (this.decoder.prefaceReceived()) {
/* 186 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is received", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 190 */     this.encoder.remoteSettings(settings);
/*     */     
/*     */ 
/* 193 */     connection().remote().createStream(1, true);
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx)
/*     */   {
/*     */     try
/*     */     {
/* 200 */       this.encoder.flowController().writePendingBytes();
/* 201 */       ctx.flush();
/*     */     } catch (Http2Exception e) {
/* 203 */       onError(ctx, true, e);
/*     */     } catch (Throwable cause) {
/* 205 */       onError(ctx, true, Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, cause, "Error flushing", new Object[0])); } }
/*     */   
/*     */   private abstract class BaseDecoder { private BaseDecoder() {}
/*     */     
/*     */     public abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception;
/*     */     
/*     */     public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
/*     */     {}
/*     */     
/*     */     public void channelActive(ChannelHandlerContext ctx) throws Exception
/*     */     {}
/* 216 */     public void channelInactive(ChannelHandlerContext ctx) throws Exception { Http2ConnectionHandler.this.encoder().close();
/* 217 */       Http2ConnectionHandler.this.decoder().close();
/*     */       
/*     */ 
/*     */ 
/* 221 */       Http2ConnectionHandler.this.connection().close(ctx.voidPromise());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     public boolean prefaceSent() { return true; }
/*     */   }
/*     */   
/*     */   private final class PrefaceDecoder extends Http2ConnectionHandler.BaseDecoder {
/*     */     private ByteBuf clientPrefaceString;
/*     */     private boolean prefaceSent;
/*     */     
/*     */     public PrefaceDecoder(ChannelHandlerContext ctx) throws Exception {
/* 236 */       super(null);
/* 237 */       this.clientPrefaceString = Http2ConnectionHandler.clientPrefaceString(Http2ConnectionHandler.this.encoder.connection());
/*     */       
/*     */ 
/* 240 */       sendPreface(ctx);
/*     */     }
/*     */     
/*     */     public boolean prefaceSent()
/*     */     {
/* 245 */       return this.prefaceSent;
/*     */     }
/*     */     
/*     */     public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */     {
/*     */       try {
/* 251 */         if ((ctx.channel().isActive()) && (readClientPrefaceString(in)) && (verifyFirstFrameIsSettings(in)))
/*     */         {
/* 253 */           Http2ConnectionHandler.this.byteDecoder = new Http2ConnectionHandler.FrameDecoder(Http2ConnectionHandler.this, null);
/* 254 */           Http2ConnectionHandler.this.byteDecoder.decode(ctx, in, out);
/*     */         }
/*     */       } catch (Throwable e) {
/* 257 */         Http2ConnectionHandler.this.onError(ctx, false, e);
/*     */       }
/*     */     }
/*     */     
/*     */     public void channelActive(ChannelHandlerContext ctx)
/*     */       throws Exception
/*     */     {
/* 264 */       sendPreface(ctx);
/*     */     }
/*     */     
/*     */     public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */     {
/* 269 */       cleanup();
/* 270 */       super.channelInactive(ctx);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void handlerRemoved(ChannelHandlerContext ctx)
/*     */       throws Exception
/*     */     {
/* 278 */       cleanup();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void cleanup()
/*     */     {
/* 285 */       if (this.clientPrefaceString != null) {
/* 286 */         this.clientPrefaceString.release();
/* 287 */         this.clientPrefaceString = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean readClientPrefaceString(ByteBuf in)
/*     */       throws Http2Exception
/*     */     {
/* 298 */       if (this.clientPrefaceString == null) {
/* 299 */         return true;
/*     */       }
/*     */       
/* 302 */       int prefaceRemaining = this.clientPrefaceString.readableBytes();
/* 303 */       int bytesRead = Math.min(in.readableBytes(), prefaceRemaining);
/*     */       
/*     */ 
/* 306 */       if ((bytesRead == 0) || (!ByteBufUtil.equals(in, in.readerIndex(), this.clientPrefaceString, this.clientPrefaceString
/* 307 */         .readerIndex(), bytesRead)))
/*     */       {
/* 309 */         int maxSearch = 1024;
/*     */         
/* 311 */         int http1Index = ByteBufUtil.indexOf(Http2ConnectionHandler.HTTP_1_X_BUF, in.slice(in.readerIndex(), Math.min(in.readableBytes(), maxSearch)));
/* 312 */         if (http1Index != -1) {
/* 313 */           String chunk = in.toString(in.readerIndex(), http1Index - in.readerIndex(), CharsetUtil.US_ASCII);
/* 314 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Unexpected HTTP/1.x request: %s", new Object[] { chunk });
/*     */         }
/* 316 */         String receivedBytes = ByteBufUtil.hexDump(in, in.readerIndex(), 
/* 317 */           Math.min(in.readableBytes(), this.clientPrefaceString.readableBytes()));
/* 318 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP/2 client preface string missing or corrupt. Hex dump for received bytes: %s", new Object[] { receivedBytes });
/*     */       }
/*     */       
/* 321 */       in.skipBytes(bytesRead);
/* 322 */       this.clientPrefaceString.skipBytes(bytesRead);
/*     */       
/* 324 */       if (!this.clientPrefaceString.isReadable())
/*     */       {
/* 326 */         this.clientPrefaceString.release();
/* 327 */         this.clientPrefaceString = null;
/* 328 */         return true;
/*     */       }
/* 330 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean verifyFirstFrameIsSettings(ByteBuf in)
/*     */       throws Http2Exception
/*     */     {
/* 342 */       if (in.readableBytes() < 5)
/*     */       {
/* 344 */         return false;
/*     */       }
/*     */       
/* 347 */       short frameType = in.getUnsignedByte(in.readerIndex() + 3);
/* 348 */       short flags = in.getUnsignedByte(in.readerIndex() + 4);
/* 349 */       if ((frameType != 4) || ((flags & 0x1) != 0)) {
/* 350 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "First received frame was not SETTINGS. Hex dump for first 5 bytes: %s", new Object[] {
/*     */         
/* 352 */           ByteBufUtil.hexDump(in, in.readerIndex(), 5) });
/*     */       }
/* 354 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     private void sendPreface(ChannelHandlerContext ctx)
/*     */       throws Exception
/*     */     {
/* 361 */       if ((this.prefaceSent) || (!ctx.channel().isActive())) {
/* 362 */         return;
/*     */       }
/*     */       
/* 365 */       this.prefaceSent = true;
/*     */       
/* 367 */       boolean isClient = !Http2ConnectionHandler.this.connection().isServer();
/* 368 */       if (isClient)
/*     */       {
/* 370 */         ctx.write(Http2CodecUtil.connectionPrefaceBuf()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */       }
/*     */       
/*     */ 
/* 374 */       Http2ConnectionHandler.this.encoder.writeSettings(ctx, Http2ConnectionHandler.this.initialSettings, ctx.newPromise()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */       
/*     */ 
/* 377 */       if (isClient)
/*     */       {
/*     */ 
/*     */ 
/* 381 */         Http2ConnectionHandler.this.userEventTriggered(ctx, Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE); }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class FrameDecoder extends Http2ConnectionHandler.BaseDecoder {
/* 386 */     private FrameDecoder() { super(null); }
/*     */     
/*     */     public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */       try {
/* 390 */         Http2ConnectionHandler.this.decoder.decodeFrame(ctx, in, out);
/*     */       } catch (Throwable e) {
/* 392 */         Http2ConnectionHandler.this.onError(ctx, false, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 400 */     this.encoder.lifecycleManager(this);
/* 401 */     this.decoder.lifecycleManager(this);
/* 402 */     this.encoder.flowController().channelHandlerContext(ctx);
/* 403 */     this.decoder.flowController().channelHandlerContext(ctx);
/* 404 */     this.byteDecoder = new PrefaceDecoder(ctx);
/*     */   }
/*     */   
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 409 */     if (this.byteDecoder != null) {
/* 410 */       this.byteDecoder.handlerRemoved(ctx);
/* 411 */       this.byteDecoder = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 417 */     if (this.byteDecoder == null) {
/* 418 */       this.byteDecoder = new PrefaceDecoder(ctx);
/*     */     }
/* 420 */     this.byteDecoder.channelActive(ctx);
/* 421 */     super.channelActive(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 427 */     super.channelInactive(ctx);
/* 428 */     if (this.byteDecoder != null) {
/* 429 */       this.byteDecoder.channelInactive(ctx);
/* 430 */       this.byteDecoder = null;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface 89 1 0
/*     */     //   6: invokeinterface 90 1 0
/*     */     //   11: ifeq +8 -> 19
/*     */     //   14: aload_0
/*     */     //   15: aload_1
/*     */     //   16: invokevirtual 91	io/netty/handler/codec/http2/Http2ConnectionHandler:flush	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   19: aload_0
/*     */     //   20: getfield 11	io/netty/handler/codec/http2/Http2ConnectionHandler:encoder	Lio/netty/handler/codec/http2/Http2ConnectionEncoder;
/*     */     //   23: invokeinterface 69 1 0
/*     */     //   28: invokeinterface 92 1 0
/*     */     //   33: aload_0
/*     */     //   34: aload_1
/*     */     //   35: invokespecial 93	io/netty/handler/codec/ByteToMessageDecoder:channelWritabilityChanged	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   38: goto +11 -> 49
/*     */     //   41: astore_2
/*     */     //   42: aload_0
/*     */     //   43: aload_1
/*     */     //   44: invokespecial 93	io/netty/handler/codec/ByteToMessageDecoder:channelWritabilityChanged	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   47: aload_2
/*     */     //   48: athrow
/*     */     //   49: return
/*     */     // Line number table:
/*     */     //   Java source line #439	-> byte code offset #0
/*     */     //   Java source line #440	-> byte code offset #14
/*     */     //   Java source line #442	-> byte code offset #19
/*     */     //   Java source line #444	-> byte code offset #33
/*     */     //   Java source line #445	-> byte code offset #38
/*     */     //   Java source line #444	-> byte code offset #41
/*     */     //   Java source line #445	-> byte code offset #47
/*     */     //   Java source line #446	-> byte code offset #49
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	50	0	this	Http2ConnectionHandler
/*     */     //   0	50	1	ctx	ChannelHandlerContext
/*     */     //   41	7	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	33	41	finally
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 450 */     this.byteDecoder.decode(ctx, in, out);
/*     */   }
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception
/*     */   {
/* 455 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 461 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 466 */     ctx.disconnect(promise);
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 471 */     promise = promise.unvoid();
/*     */     
/* 473 */     if (!ctx.channel().isActive()) {
/* 474 */       ctx.close(promise);
/* 475 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 483 */     ChannelFuture future = connection().goAwaySent() ? ctx.write(Unpooled.EMPTY_BUFFER) : goAway(ctx, null);
/* 484 */     ctx.flush();
/* 485 */     doGracefulShutdown(ctx, future, promise);
/*     */   }
/*     */   
/*     */   private void doGracefulShutdown(ChannelHandlerContext ctx, ChannelFuture future, ChannelPromise promise) {
/* 489 */     if (isGracefulShutdownComplete())
/*     */     {
/* 491 */       future.addListener(new ClosingChannelFutureListener(ctx, promise));
/*     */ 
/*     */     }
/* 494 */     else if (this.gracefulShutdownTimeoutMillis < 0L) {
/* 495 */       this.closeListener = new ClosingChannelFutureListener(ctx, promise);
/*     */     } else {
/* 497 */       this.closeListener = new ClosingChannelFutureListener(ctx, promise, this.gracefulShutdownTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 505 */     ctx.deregister(promise);
/*     */   }
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 510 */     ctx.read();
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 515 */     ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void channelReadComplete(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual 116	io/netty/handler/codec/http2/Http2ConnectionHandler:channelReadComplete0	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   5: aload_0
/*     */     //   6: aload_1
/*     */     //   7: invokevirtual 91	io/netty/handler/codec/http2/Http2ConnectionHandler:flush	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   10: goto +11 -> 21
/*     */     //   13: astore_2
/*     */     //   14: aload_0
/*     */     //   15: aload_1
/*     */     //   16: invokevirtual 91	io/netty/handler/codec/http2/Http2ConnectionHandler:flush	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   19: aload_2
/*     */     //   20: athrow
/*     */     //   21: return
/*     */     // Line number table:
/*     */     //   Java source line #524	-> byte code offset #0
/*     */     //   Java source line #526	-> byte code offset #5
/*     */     //   Java source line #527	-> byte code offset #10
/*     */     //   Java source line #526	-> byte code offset #13
/*     */     //   Java source line #527	-> byte code offset #19
/*     */     //   Java source line #528	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	Http2ConnectionHandler
/*     */     //   0	22	1	ctx	ChannelHandlerContext
/*     */     //   13	7	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	5	13	finally
/*     */   }
/*     */   
/*     */   void channelReadComplete0(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 531 */     super.channelReadComplete(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */     throws Exception
/*     */   {
/* 539 */     if (Http2CodecUtil.getEmbeddedHttp2Exception(cause) != null)
/*     */     {
/* 541 */       onError(ctx, false, cause);
/*     */     } else {
/* 543 */       super.exceptionCaught(ctx, cause);
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
/*     */   public void closeStreamLocal(Http2Stream stream, ChannelFuture future)
/*     */   {
/* 556 */     switch (stream.state()) {
/*     */     case HALF_CLOSED_LOCAL: 
/*     */     case OPEN: 
/* 559 */       stream.closeLocalSide();
/* 560 */       break;
/*     */     default: 
/* 562 */       closeStream(stream, future);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeStreamRemote(Http2Stream stream, ChannelFuture future)
/*     */   {
/* 576 */     switch (stream.state()) {
/*     */     case OPEN: 
/*     */     case HALF_CLOSED_REMOTE: 
/* 579 */       stream.closeRemoteSide();
/* 580 */       break;
/*     */     default: 
/* 582 */       closeStream(stream, future);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public void closeStream(Http2Stream stream, ChannelFuture future)
/*     */   {
/* 589 */     stream.close();
/*     */     
/* 591 */     if (future.isDone()) {
/* 592 */       checkCloseConnection(future);
/*     */     } else {
/* 594 */       future.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 597 */           Http2ConnectionHandler.this.checkCloseConnection(future);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onError(ChannelHandlerContext ctx, boolean outbound, Throwable cause)
/*     */   {
/* 608 */     Http2Exception embedded = Http2CodecUtil.getEmbeddedHttp2Exception(cause);
/* 609 */     if (Http2Exception.isStreamError(embedded)) {
/* 610 */       onStreamError(ctx, outbound, cause, (Http2Exception.StreamException)embedded);
/* 611 */     } else if ((embedded instanceof Http2Exception.CompositeStreamException)) {
/* 612 */       Http2Exception.CompositeStreamException compositException = (Http2Exception.CompositeStreamException)embedded;
/* 613 */       for (Http2Exception.StreamException streamException : compositException) {
/* 614 */         onStreamError(ctx, outbound, cause, streamException);
/*     */       }
/*     */     } else {
/* 617 */       onConnectionError(ctx, outbound, cause, embedded);
/*     */     }
/* 619 */     ctx.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isGracefulShutdownComplete()
/*     */   {
/* 628 */     return connection().numActiveStreams() == 0;
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
/*     */   protected void onConnectionError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception http2Ex)
/*     */   {
/* 643 */     if (http2Ex == null) {
/* 644 */       http2Ex = new Http2Exception(Http2Error.INTERNAL_ERROR, cause.getMessage(), cause);
/*     */     }
/*     */     
/* 647 */     ChannelPromise promise = ctx.newPromise();
/* 648 */     ChannelFuture future = goAway(ctx, http2Ex);
/* 649 */     switch (http2Ex.shutdownHint()) {
/*     */     case GRACEFUL_SHUTDOWN: 
/* 651 */       doGracefulShutdown(ctx, future, promise);
/* 652 */       break;
/*     */     default: 
/* 654 */       future.addListener(new ClosingChannelFutureListener(ctx, promise));
/*     */     }
/*     */     
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
/*     */   protected void onStreamError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception.StreamException http2Ex)
/*     */   {
/* 670 */     int streamId = http2Ex.streamId();
/* 671 */     Http2Stream stream = connection().stream(streamId);
/*     */     
/*     */ 
/* 674 */     if (((http2Ex instanceof Http2Exception.HeaderListSizeException)) && 
/* 675 */       (((Http2Exception.HeaderListSizeException)http2Ex).duringDecode()) && 
/* 676 */       (connection().isServer()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 684 */       if (stream == null) {
/*     */         try {
/* 686 */           stream = this.encoder.connection().remote().createStream(streamId, true);
/*     */         } catch (Http2Exception e) {
/* 688 */           resetUnknownStream(ctx, streamId, http2Ex.error().code(), ctx.newPromise());
/* 689 */           return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 694 */       if ((stream != null) && (!stream.isHeadersSent())) {
/*     */         try {
/* 696 */           handleServerHeaderDecodeSizeError(ctx, stream);
/*     */         } catch (Throwable cause2) {
/* 698 */           onError(ctx, outbound, Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, cause2, "Error DecodeSizeError", new Object[0]));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 703 */     if (stream == null) {
/* 704 */       if ((!outbound) || (connection().local().mayHaveCreatedStream(streamId))) {
/* 705 */         resetUnknownStream(ctx, streamId, http2Ex.error().code(), ctx.newPromise());
/*     */       }
/*     */     } else {
/* 708 */       resetStream(ctx, stream, http2Ex.error().code(), ctx.newPromise());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleServerHeaderDecodeSizeError(ChannelHandlerContext ctx, Http2Stream stream)
/*     */   {
/* 720 */     encoder().writeHeaders(ctx, stream.id(), HEADERS_TOO_LARGE_HEADERS, 0, true, ctx.newPromise());
/*     */   }
/*     */   
/*     */   protected Http2FrameWriter frameWriter() {
/* 724 */     return encoder().frameWriter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChannelFuture resetUnknownStream(final ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise)
/*     */   {
/* 734 */     ChannelFuture future = frameWriter().writeRstStream(ctx, streamId, errorCode, promise);
/* 735 */     if (future.isDone()) {
/* 736 */       closeConnectionOnError(ctx, future);
/*     */     } else {
/* 738 */       future.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 741 */           Http2ConnectionHandler.this.closeConnectionOnError(ctx, future);
/*     */         }
/*     */       });
/*     */     }
/* 745 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture resetStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise)
/*     */   {
/* 751 */     Http2Stream stream = connection().stream(streamId);
/* 752 */     if (stream == null) {
/* 753 */       return resetUnknownStream(ctx, streamId, errorCode, promise.unvoid());
/*     */     }
/*     */     
/* 756 */     return resetStream(ctx, stream, errorCode, promise);
/*     */   }
/*     */   
/*     */   private ChannelFuture resetStream(final ChannelHandlerContext ctx, final Http2Stream stream, long errorCode, ChannelPromise promise)
/*     */   {
/* 761 */     promise = promise.unvoid();
/* 762 */     if (stream.isResetSent())
/*     */     {
/* 764 */       return promise.setSuccess();
/*     */     }
/*     */     
/*     */     ChannelFuture future;
/*     */     ChannelFuture future;
/* 769 */     if ((stream.state() == Http2Stream.State.IDLE) || (
/* 770 */       (connection().local().created(stream)) && (!stream.isHeadersSent()) && (!stream.isPushPromiseSent()))) {
/* 771 */       future = promise.setSuccess();
/*     */     } else {
/* 773 */       future = frameWriter().writeRstStream(ctx, stream.id(), errorCode, promise);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 778 */     stream.resetSent();
/*     */     
/* 780 */     if (future.isDone()) {
/* 781 */       processRstStreamWriteResult(ctx, stream, future);
/*     */     } else {
/* 783 */       future.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 786 */           Http2ConnectionHandler.this.processRstStreamWriteResult(ctx, stream, future);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 791 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture goAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, ByteBuf debugData, ChannelPromise promise)
/*     */   {
/* 797 */     promise = promise.unvoid();
/* 798 */     Http2Connection connection = connection();
/*     */     try {
/* 800 */       if (!connection.goAwaySent(lastStreamId, errorCode, debugData)) {
/* 801 */         debugData.release();
/* 802 */         promise.trySuccess();
/* 803 */         return promise;
/*     */       }
/*     */     } catch (Throwable cause) {
/* 806 */       debugData.release();
/* 807 */       promise.tryFailure(cause);
/* 808 */       return promise;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 813 */     debugData.retain();
/* 814 */     ChannelFuture future = frameWriter().writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */     
/* 816 */     if (future.isDone()) {
/* 817 */       processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, future);
/*     */     } else {
/* 819 */       future.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 822 */           Http2ConnectionHandler.processGoAwayWriteResult(ctx, lastStreamId, errorCode, this.val$debugData, future);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 827 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkCloseConnection(ChannelFuture future)
/*     */   {
/* 837 */     if ((this.closeListener != null) && (isGracefulShutdownComplete())) {
/* 838 */       ChannelFutureListener closeListener = this.closeListener;
/*     */       
/*     */ 
/* 841 */       this.closeListener = null;
/*     */       try {
/* 843 */         closeListener.operationComplete(future);
/*     */       } catch (Exception e) {
/* 845 */         throw new IllegalStateException("Close listener threw an unexpected exception", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChannelFuture goAway(ChannelHandlerContext ctx, Http2Exception cause)
/*     */   {
/* 855 */     long errorCode = cause != null ? cause.error().code() : Http2Error.NO_ERROR.code();
/* 856 */     int lastKnownStream = connection().remote().lastStreamCreated();
/* 857 */     return goAway(ctx, lastKnownStream, errorCode, Http2CodecUtil.toByteBuf(ctx, cause), ctx.newPromise());
/*     */   }
/*     */   
/*     */   private void processRstStreamWriteResult(ChannelHandlerContext ctx, Http2Stream stream, ChannelFuture future) {
/* 861 */     if (future.isSuccess()) {
/* 862 */       closeStream(stream, future);
/*     */     }
/*     */     else {
/* 865 */       onConnectionError(ctx, true, future.cause(), null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeConnectionOnError(ChannelHandlerContext ctx, ChannelFuture future) {
/* 870 */     if (!future.isSuccess()) {
/* 871 */       onConnectionError(ctx, true, future.cause(), null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ByteBuf clientPrefaceString(Http2Connection connection)
/*     */   {
/* 879 */     return connection.isServer() ? Http2CodecUtil.connectionPrefaceBuf() : null;
/*     */   }
/*     */   
/*     */   private static void processGoAwayWriteResult(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelFuture future)
/*     */   {
/*     */     try {
/* 885 */       if (future.isSuccess()) {
/* 886 */         if (errorCode != Http2Error.NO_ERROR.code()) {
/* 887 */           if (logger.isDebugEnabled()) {
/* 888 */             logger.debug("{} Sent GOAWAY: lastStreamId '{}', errorCode '{}', debugData '{}'. Forcing shutdown of the connection.", new Object[] {ctx
/*     */             
/* 890 */               .channel(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), debugData.toString(CharsetUtil.UTF_8), future.cause() });
/*     */           }
/* 892 */           ctx.close();
/*     */         }
/*     */       } else {
/* 895 */         if (logger.isDebugEnabled()) {
/* 896 */           logger.debug("{} Sending GOAWAY failed: lastStreamId '{}', errorCode '{}', debugData '{}'. Forcing shutdown of the connection.", new Object[] {ctx
/*     */           
/* 898 */             .channel(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), debugData.toString(CharsetUtil.UTF_8), future.cause() });
/*     */         }
/* 900 */         ctx.close();
/*     */       }
/*     */     }
/*     */     finally {
/* 904 */       debugData.release();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ClosingChannelFutureListener
/*     */     implements ChannelFutureListener
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelPromise promise;
/*     */     private final ScheduledFuture<?> timeoutTask;
/*     */     
/*     */     ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     {
/* 917 */       this.ctx = ctx;
/* 918 */       this.promise = promise;
/* 919 */       this.timeoutTask = null;
/*     */     }
/*     */     
/*     */     ClosingChannelFutureListener(final ChannelHandlerContext ctx, final ChannelPromise promise, long timeout, TimeUnit unit)
/*     */     {
/* 924 */       this.ctx = ctx;
/* 925 */       this.promise = promise;
/* 926 */       this.timeoutTask = ctx.executor().schedule(new Runnable()
/*     */       {
/*     */ 
/* 929 */         public void run() { ctx.close(promise); } }, timeout, unit);
/*     */     }
/*     */     
/*     */ 
/*     */     public void operationComplete(ChannelFuture sentGoAwayFuture)
/*     */       throws Exception
/*     */     {
/* 936 */       if (this.timeoutTask != null) {
/* 937 */         this.timeoutTask.cancel(false);
/*     */       }
/* 939 */       this.ctx.close(this.promise);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2ConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */