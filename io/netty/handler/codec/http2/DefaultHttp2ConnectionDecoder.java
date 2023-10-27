/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.http.HttpStatusClass;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public class DefaultHttp2ConnectionDecoder
/*     */   implements Http2ConnectionDecoder
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultHttp2ConnectionDecoder.class);
/*  53 */   private Http2FrameListener internalFrameListener = new PrefaceFrameListener(null);
/*     */   
/*     */   private final Http2Connection connection;
/*     */   private Http2LifecycleManager lifecycleManager;
/*     */   private final Http2ConnectionEncoder encoder;
/*     */   private final Http2FrameReader frameReader;
/*     */   private Http2FrameListener listener;
/*     */   private final Http2PromisedRequestVerifier requestVerifier;
/*     */   
/*     */   public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader)
/*     */   {
/*  64 */     this(connection, encoder, frameReader, Http2PromisedRequestVerifier.ALWAYS_VERIFY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultHttp2ConnectionDecoder(Http2Connection connection, Http2ConnectionEncoder encoder, Http2FrameReader frameReader, Http2PromisedRequestVerifier requestVerifier)
/*     */   {
/*  71 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(connection, "connection"));
/*  72 */     this.frameReader = ((Http2FrameReader)ObjectUtil.checkNotNull(frameReader, "frameReader"));
/*  73 */     this.encoder = ((Http2ConnectionEncoder)ObjectUtil.checkNotNull(encoder, "encoder"));
/*  74 */     this.requestVerifier = ((Http2PromisedRequestVerifier)ObjectUtil.checkNotNull(requestVerifier, "requestVerifier"));
/*  75 */     if (connection.local().flowController() == null) {
/*  76 */       connection.local().flowController(new DefaultHttp2LocalFlowController(connection));
/*     */     }
/*  78 */     ((Http2LocalFlowController)connection.local().flowController()).frameWriter(encoder.frameWriter());
/*     */   }
/*     */   
/*     */   public void lifecycleManager(Http2LifecycleManager lifecycleManager)
/*     */   {
/*  83 */     this.lifecycleManager = ((Http2LifecycleManager)ObjectUtil.checkNotNull(lifecycleManager, "lifecycleManager"));
/*     */   }
/*     */   
/*     */   public Http2Connection connection()
/*     */   {
/*  88 */     return this.connection;
/*     */   }
/*     */   
/*     */   public final Http2LocalFlowController flowController()
/*     */   {
/*  93 */     return (Http2LocalFlowController)this.connection.local().flowController();
/*     */   }
/*     */   
/*     */   public void frameListener(Http2FrameListener listener)
/*     */   {
/*  98 */     this.listener = ((Http2FrameListener)ObjectUtil.checkNotNull(listener, "listener"));
/*     */   }
/*     */   
/*     */   public Http2FrameListener frameListener()
/*     */   {
/* 103 */     return this.listener;
/*     */   }
/*     */   
/*     */   Http2FrameListener internalFrameListener()
/*     */   {
/* 108 */     return this.internalFrameListener;
/*     */   }
/*     */   
/*     */   public boolean prefaceReceived()
/*     */   {
/* 113 */     return FrameReadListener.class == this.internalFrameListener.getClass();
/*     */   }
/*     */   
/*     */   public void decodeFrame(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Http2Exception
/*     */   {
/* 118 */     this.frameReader.readFrame(ctx, in, this.internalFrameListener);
/*     */   }
/*     */   
/*     */   public Http2Settings localSettings()
/*     */   {
/* 123 */     Http2Settings settings = new Http2Settings();
/* 124 */     Http2FrameReader.Configuration config = this.frameReader.configuration();
/* 125 */     Http2HeadersDecoder.Configuration headersConfig = config.headersConfiguration();
/* 126 */     Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
/* 127 */     settings.initialWindowSize(flowController().initialWindowSize());
/* 128 */     settings.maxConcurrentStreams(this.connection.remote().maxActiveStreams());
/* 129 */     settings.headerTableSize(headersConfig.maxHeaderTableSize());
/* 130 */     settings.maxFrameSize(frameSizePolicy.maxFrameSize());
/* 131 */     settings.maxHeaderListSize(headersConfig.maxHeaderListSize());
/* 132 */     if (!this.connection.isServer())
/*     */     {
/* 134 */       settings.pushEnabled(this.connection.local().allowPushTo());
/*     */     }
/* 136 */     return settings;
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 141 */     this.frameReader.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long calculateMaxHeaderListSizeGoAway(long maxHeaderListSize)
/*     */   {
/* 152 */     return Http2CodecUtil.calculateMaxHeaderListSizeGoAway(maxHeaderListSize);
/*     */   }
/*     */   
/*     */   private int unconsumedBytes(Http2Stream stream) {
/* 156 */     return flowController().unconsumedBytes(stream);
/*     */   }
/*     */   
/*     */   void onGoAwayRead0(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData) throws Http2Exception
/*     */   {
/* 161 */     this.connection.goAwayReceived(lastStreamId, errorCode, debugData);
/* 162 */     this.listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */   }
/*     */   
/*     */   void onUnknownFrame0(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload) throws Http2Exception
/*     */   {
/* 167 */     this.listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
/*     */   }
/*     */   
/*     */   private final class FrameReadListener
/*     */     implements Http2FrameListener
/*     */   {
/*     */     private FrameReadListener() {}
/*     */     
/*     */     public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception
/*     */     {
/* 177 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 178 */       Http2LocalFlowController flowController = DefaultHttp2ConnectionDecoder.this.flowController();
/* 179 */       int bytesToReturn = data.readableBytes() + padding;
/*     */       
/*     */       try
/*     */       {
/* 183 */         shouldIgnore = shouldIgnoreHeadersOrDataFrame(ctx, streamId, stream, "DATA");
/*     */       }
/*     */       catch (Http2Exception e) {
/*     */         boolean shouldIgnore;
/* 187 */         flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/* 188 */         flowController.consumeBytes(stream, bytesToReturn);
/* 189 */         throw e;
/*     */       } catch (Throwable t) {
/* 191 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Unhandled error on data stream id %d", new Object[] { Integer.valueOf(streamId) });
/*     */       }
/*     */       boolean shouldIgnore;
/* 194 */       if (shouldIgnore)
/*     */       {
/*     */ 
/* 197 */         flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/* 198 */         flowController.consumeBytes(stream, bytesToReturn);
/*     */         
/*     */ 
/* 201 */         verifyStreamMayHaveExisted(streamId);
/*     */         
/*     */ 
/* 204 */         return bytesToReturn;
/*     */       }
/*     */       
/* 207 */       Http2Exception error = null;
/* 208 */       switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[stream.state().ordinal()]) {
/*     */       case 1: 
/*     */       case 2: 
/*     */         break;
/*     */       case 3: 
/*     */       case 4: 
/* 214 */         error = Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] {
/* 215 */           Integer.valueOf(stream.id()), stream.state() });
/* 216 */         break;
/*     */       default: 
/* 218 */         error = Http2Exception.streamError(stream.id(), Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", new Object[] {
/* 219 */           Integer.valueOf(stream.id()), stream.state() });
/*     */       }
/*     */       
/*     */       
/* 223 */       int unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/*     */       try {
/* 225 */         flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/*     */         
/* 227 */         unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/*     */         
/*     */ 
/* 230 */         if (error != null) {
/* 231 */           throw error;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 236 */         bytesToReturn = DefaultHttp2ConnectionDecoder.this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/* 237 */         return bytesToReturn;
/*     */ 
/*     */       }
/*     */       catch (Http2Exception e)
/*     */       {
/* 242 */         int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 243 */         bytesToReturn -= delta;
/* 244 */         throw e;
/*     */ 
/*     */       }
/*     */       catch (RuntimeException e)
/*     */       {
/* 249 */         int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 250 */         bytesToReturn -= delta;
/* 251 */         throw e;
/*     */       }
/*     */       finally {
/* 254 */         flowController.consumeBytes(stream, bytesToReturn);
/*     */         
/* 256 */         if (endOfStream) {
/* 257 */           DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStreamRemote(stream, ctx.newSucceededFuture());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 265 */       onHeadersRead(ctx, streamId, headers, 0, (short)16, false, padding, endOfStream);
/*     */     }
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 271 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 272 */       boolean allowHalfClosedRemote = false;
/* 273 */       if ((stream == null) && (!DefaultHttp2ConnectionDecoder.this.connection.streamMayHaveExisted(streamId))) {
/* 274 */         stream = DefaultHttp2ConnectionDecoder.this.connection.remote().createStream(streamId, endOfStream);
/*     */         
/* 276 */         allowHalfClosedRemote = stream.state() == Http2Stream.State.HALF_CLOSED_REMOTE;
/*     */       }
/*     */       
/* 279 */       if (shouldIgnoreHeadersOrDataFrame(ctx, streamId, stream, "HEADERS")) {
/* 280 */         return;
/*     */       }
/*     */       
/*     */ 
/* 284 */       boolean isInformational = (!DefaultHttp2ConnectionDecoder.this.connection.isServer()) && (HttpStatusClass.valueOf(headers.status()) == HttpStatusClass.INFORMATIONAL);
/* 285 */       if (((!isInformational) && (endOfStream)) || ((stream.isHeadersReceived()) || (stream.isTrailersReceived()))) {
/* 286 */         throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Stream %d received too many headers EOS: %s state: %s", new Object[] {
/*     */         
/* 288 */           Integer.valueOf(streamId), Boolean.valueOf(endOfStream), stream.state() });
/*     */       }
/*     */       
/* 291 */       switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[stream.state().ordinal()]) {
/*     */       case 5: 
/* 293 */         stream.open(endOfStream);
/* 294 */         break;
/*     */       case 1: 
/*     */       case 2: 
/*     */         break;
/*     */       
/*     */       case 3: 
/* 300 */         if (!allowHalfClosedRemote) {
/* 301 */           throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] {
/* 302 */             Integer.valueOf(stream.id()), stream.state() });
/*     */         }
/*     */         break;
/*     */       case 4: 
/* 306 */         throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] {
/* 307 */           Integer.valueOf(stream.id()), stream.state() });
/*     */       
/*     */       default: 
/* 310 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream
/* 311 */           .state() });
/*     */       }
/*     */       
/* 314 */       stream.headersReceived(isInformational);
/* 315 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().updateDependencyTree(streamId, streamDependency, weight, exclusive);
/*     */       
/* 317 */       DefaultHttp2ConnectionDecoder.this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
/*     */       
/*     */ 
/* 320 */       if (endOfStream) {
/* 321 */         DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStreamRemote(stream, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/*     */     
/*     */     public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */       throws Http2Exception
/*     */     {
/* 328 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().updateDependencyTree(streamId, streamDependency, weight, exclusive);
/*     */       
/* 330 */       DefaultHttp2ConnectionDecoder.this.listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */     }
/*     */     
/*     */     public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception
/*     */     {
/* 335 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 336 */       if (stream == null) {
/* 337 */         verifyStreamMayHaveExisted(streamId);
/* 338 */         return;
/*     */       }
/*     */       
/* 341 */       switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[stream.state().ordinal()]) {
/*     */       case 6: 
/* 343 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "RST_STREAM received for IDLE stream %d", new Object[] { Integer.valueOf(streamId) });
/*     */       case 4: 
/* 345 */         return;
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 350 */       DefaultHttp2ConnectionDecoder.this.listener.onRstStreamRead(ctx, streamId, errorCode);
/*     */       
/* 352 */       DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStream(stream, ctx.newSucceededFuture());
/*     */     }
/*     */     
/*     */     public void onSettingsAckRead(ChannelHandlerContext ctx)
/*     */       throws Http2Exception
/*     */     {
/* 358 */       Http2Settings settings = DefaultHttp2ConnectionDecoder.this.encoder.pollSentSettings();
/*     */       
/* 360 */       if (settings != null) {
/* 361 */         applyLocalSettings(settings);
/*     */       }
/*     */       
/* 364 */       DefaultHttp2ConnectionDecoder.this.listener.onSettingsAckRead(ctx);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void applyLocalSettings(Http2Settings settings)
/*     */       throws Http2Exception
/*     */     {
/* 373 */       Boolean pushEnabled = settings.pushEnabled();
/* 374 */       Http2FrameReader.Configuration config = DefaultHttp2ConnectionDecoder.this.frameReader.configuration();
/* 375 */       Http2HeadersDecoder.Configuration headerConfig = config.headersConfiguration();
/* 376 */       Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
/* 377 */       if (pushEnabled != null) {
/* 378 */         if (DefaultHttp2ConnectionDecoder.this.connection.isServer()) {
/* 379 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */         }
/* 381 */         DefaultHttp2ConnectionDecoder.this.connection.local().allowPushTo(pushEnabled.booleanValue());
/*     */       }
/*     */       
/* 384 */       Long maxConcurrentStreams = settings.maxConcurrentStreams();
/* 385 */       if (maxConcurrentStreams != null) {
/* 386 */         DefaultHttp2ConnectionDecoder.this.connection.remote().maxActiveStreams((int)Math.min(maxConcurrentStreams.longValue(), 2147483647L));
/*     */       }
/*     */       
/* 389 */       Long headerTableSize = settings.headerTableSize();
/* 390 */       if (headerTableSize != null) {
/* 391 */         headerConfig.maxHeaderTableSize(headerTableSize.longValue());
/*     */       }
/*     */       
/* 394 */       Long maxHeaderListSize = settings.maxHeaderListSize();
/* 395 */       if (maxHeaderListSize != null) {
/* 396 */         headerConfig.maxHeaderListSize(maxHeaderListSize.longValue(), DefaultHttp2ConnectionDecoder.this.calculateMaxHeaderListSizeGoAway(maxHeaderListSize.longValue()));
/*     */       }
/*     */       
/* 399 */       Integer maxFrameSize = settings.maxFrameSize();
/* 400 */       if (maxFrameSize != null) {
/* 401 */         frameSizePolicy.maxFrameSize(maxFrameSize.intValue());
/*     */       }
/*     */       
/* 404 */       Integer initialWindowSize = settings.initialWindowSize();
/* 405 */       if (initialWindowSize != null) {
/* 406 */         DefaultHttp2ConnectionDecoder.this.flowController().initialWindowSize(initialWindowSize.intValue());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings)
/*     */       throws Http2Exception
/*     */     {
/* 415 */       DefaultHttp2ConnectionDecoder.this.encoder.writeSettingsAck(ctx, ctx.newPromise());
/*     */       
/* 417 */       DefaultHttp2ConnectionDecoder.this.encoder.remoteSettings(settings);
/*     */       
/* 419 */       DefaultHttp2ConnectionDecoder.this.listener.onSettingsRead(ctx, settings);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onPingRead(ChannelHandlerContext ctx, long data)
/*     */       throws Http2Exception
/*     */     {
/* 426 */       DefaultHttp2ConnectionDecoder.this.encoder.writePing(ctx, true, data, ctx.newPromise());
/*     */       
/* 428 */       DefaultHttp2ConnectionDecoder.this.listener.onPingRead(ctx, data);
/*     */     }
/*     */     
/*     */     public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception
/*     */     {
/* 433 */       DefaultHttp2ConnectionDecoder.this.listener.onPingAckRead(ctx, data);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */       throws Http2Exception
/*     */     {
/* 440 */       if (DefaultHttp2ConnectionDecoder.this.connection().isServer()) {
/* 441 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A client cannot push.", new Object[0]);
/*     */       }
/*     */       
/* 444 */       Http2Stream parentStream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/*     */       
/* 446 */       if (shouldIgnoreHeadersOrDataFrame(ctx, streamId, parentStream, "PUSH_PROMISE")) {
/* 447 */         return;
/*     */       }
/*     */       
/* 450 */       if (parentStream == null) {
/* 451 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d does not exist", new Object[] { Integer.valueOf(streamId) });
/*     */       }
/*     */       
/* 454 */       switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[parentStream.state().ordinal()])
/*     */       {
/*     */       case 1: 
/*     */       case 2: 
/*     */         break;
/*     */       
/*     */       default: 
/* 461 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state for receiving push promise: %s", new Object[] {
/*     */         
/* 463 */           Integer.valueOf(parentStream.id()), parentStream.state() });
/*     */       }
/*     */       
/* 466 */       if (!DefaultHttp2ConnectionDecoder.this.requestVerifier.isAuthoritative(ctx, headers)) {
/* 467 */         throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not authoritative", new Object[] {
/*     */         
/* 469 */           Integer.valueOf(streamId), Integer.valueOf(promisedStreamId) });
/*     */       }
/* 471 */       if (!DefaultHttp2ConnectionDecoder.this.requestVerifier.isCacheable(headers)) {
/* 472 */         throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not known to be cacheable", new Object[] {
/*     */         
/* 474 */           Integer.valueOf(streamId), Integer.valueOf(promisedStreamId) });
/*     */       }
/* 476 */       if (!DefaultHttp2ConnectionDecoder.this.requestVerifier.isSafe(headers)) {
/* 477 */         throw Http2Exception.streamError(promisedStreamId, Http2Error.PROTOCOL_ERROR, "Promised request on stream %d for promised stream %d is not known to be safe", new Object[] {
/*     */         
/* 479 */           Integer.valueOf(streamId), Integer.valueOf(promisedStreamId) });
/*     */       }
/*     */       
/*     */ 
/* 483 */       DefaultHttp2ConnectionDecoder.this.connection.remote().reservePushStream(promisedStreamId, parentStream);
/*     */       
/* 485 */       DefaultHttp2ConnectionDecoder.this.listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */     }
/*     */     
/*     */     public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */       throws Http2Exception
/*     */     {
/* 491 */       DefaultHttp2ConnectionDecoder.this.onGoAwayRead0(ctx, lastStreamId, errorCode, debugData);
/*     */     }
/*     */     
/*     */     public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*     */       throws Http2Exception
/*     */     {
/* 497 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 498 */       if ((stream == null) || (stream.state() == Http2Stream.State.CLOSED) || (streamCreatedAfterGoAwaySent(streamId)))
/*     */       {
/* 500 */         verifyStreamMayHaveExisted(streamId);
/* 501 */         return;
/*     */       }
/*     */       
/*     */ 
/* 505 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().incrementWindowSize(stream, windowSizeIncrement);
/*     */       
/* 507 */       DefaultHttp2ConnectionDecoder.this.listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */     }
/*     */     
/*     */     public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload)
/*     */       throws Http2Exception
/*     */     {
/* 513 */       DefaultHttp2ConnectionDecoder.this.onUnknownFrame0(ctx, frameType, streamId, flags, payload);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean shouldIgnoreHeadersOrDataFrame(ChannelHandlerContext ctx, int streamId, Http2Stream stream, String frameName)
/*     */       throws Http2Exception
/*     */     {
/* 522 */       if (stream == null) {
/* 523 */         if (streamCreatedAfterGoAwaySent(streamId)) {
/* 524 */           DefaultHttp2ConnectionDecoder.logger.info("{} ignoring {} frame for stream {}. Stream sent after GOAWAY sent", new Object[] {ctx
/* 525 */             .channel(), frameName, Integer.valueOf(streamId) });
/* 526 */           return true;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 531 */         throw Http2Exception.streamError(streamId, Http2Error.STREAM_CLOSED, "Received %s frame for an unknown stream %d", new Object[] { frameName, 
/* 532 */           Integer.valueOf(streamId) }); }
/* 533 */       if ((stream.isResetSent()) || (streamCreatedAfterGoAwaySent(streamId)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 539 */         if (DefaultHttp2ConnectionDecoder.logger.isInfoEnabled()) {
/* 540 */           DefaultHttp2ConnectionDecoder.logger.info("{} ignoring {} frame for stream {} {}", new Object[] { ctx.channel(), frameName, 
/* 541 */             "Stream created after GOAWAY sent. Last known stream by peer " + 
/*     */             
/* 543 */             DefaultHttp2ConnectionDecoder.this.connection.remote().lastStreamKnownByPeer() });
/*     */         }
/*     */         
/* 546 */         return true;
/*     */       }
/* 548 */       return false;
/*     */     }
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
/*     */     private boolean streamCreatedAfterGoAwaySent(int streamId)
/*     */     {
/* 564 */       Http2Connection.Endpoint<?> remote = DefaultHttp2ConnectionDecoder.this.connection.remote();
/* 565 */       return (DefaultHttp2ConnectionDecoder.this.connection.goAwaySent()) && (remote.isValidStreamId(streamId)) && 
/* 566 */         (streamId > remote.lastStreamKnownByPeer());
/*     */     }
/*     */     
/*     */     private void verifyStreamMayHaveExisted(int streamId) throws Http2Exception {
/* 570 */       if (!DefaultHttp2ConnectionDecoder.this.connection.streamMayHaveExisted(streamId)) {
/* 571 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d does not exist", new Object[] { Integer.valueOf(streamId) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PrefaceFrameListener
/*     */     implements Http2FrameListener
/*     */   {
/*     */     private PrefaceFrameListener() {}
/*     */     
/*     */     private void verifyPrefaceReceived()
/*     */       throws Http2Exception
/*     */     {
/* 584 */       if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived()) {
/* 585 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received non-SETTINGS as first frame.", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*     */     public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 592 */       verifyPrefaceReceived();
/* 593 */       return DefaultHttp2ConnectionDecoder.this.internalFrameListener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */     }
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 599 */       verifyPrefaceReceived();
/* 600 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onHeadersRead(ctx, streamId, headers, padding, endOfStream);
/*     */     }
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 606 */       verifyPrefaceReceived();
/* 607 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */       throws Http2Exception
/*     */     {
/* 614 */       verifyPrefaceReceived();
/* 615 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */     }
/*     */     
/*     */     public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception
/*     */     {
/* 620 */       verifyPrefaceReceived();
/* 621 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onRstStreamRead(ctx, streamId, errorCode);
/*     */     }
/*     */     
/*     */     public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception
/*     */     {
/* 626 */       verifyPrefaceReceived();
/* 627 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onSettingsAckRead(ctx);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings)
/*     */       throws Http2Exception
/*     */     {
/* 634 */       if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived()) {
/* 635 */         DefaultHttp2ConnectionDecoder.this.internalFrameListener = new DefaultHttp2ConnectionDecoder.FrameReadListener(DefaultHttp2ConnectionDecoder.this, null);
/*     */       }
/* 637 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onSettingsRead(ctx, settings);
/*     */     }
/*     */     
/*     */     public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception
/*     */     {
/* 642 */       verifyPrefaceReceived();
/* 643 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPingRead(ctx, data);
/*     */     }
/*     */     
/*     */     public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception
/*     */     {
/* 648 */       verifyPrefaceReceived();
/* 649 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPingAckRead(ctx, data);
/*     */     }
/*     */     
/*     */     public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */       throws Http2Exception
/*     */     {
/* 655 */       verifyPrefaceReceived();
/* 656 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */     }
/*     */     
/*     */     public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */       throws Http2Exception
/*     */     {
/* 662 */       DefaultHttp2ConnectionDecoder.this.onGoAwayRead0(ctx, lastStreamId, errorCode, debugData);
/*     */     }
/*     */     
/*     */     public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*     */       throws Http2Exception
/*     */     {
/* 668 */       verifyPrefaceReceived();
/* 669 */       DefaultHttp2ConnectionDecoder.this.internalFrameListener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */     }
/*     */     
/*     */     public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload)
/*     */       throws Http2Exception
/*     */     {
/* 675 */       DefaultHttp2ConnectionDecoder.this.onUnknownFrame0(ctx, frameType, streamId, flags, payload);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\DefaultHttp2ConnectionDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */