/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class Http2MultiplexCodecBuilder
/*     */   extends AbstractHttp2ConnectionHandlerBuilder<Http2MultiplexCodec, Http2MultiplexCodecBuilder>
/*     */ {
/*     */   final ChannelHandler childHandler;
/*     */   private ChannelHandler upgradeStreamHandler;
/*     */   
/*     */   Http2MultiplexCodecBuilder(boolean server, ChannelHandler childHandler)
/*     */   {
/*  35 */     server(server);
/*  36 */     this.childHandler = checkSharable((ChannelHandler)ObjectUtil.checkNotNull(childHandler, "childHandler"));
/*     */   }
/*     */   
/*     */   private static ChannelHandler checkSharable(ChannelHandler handler) {
/*  40 */     if (((handler instanceof ChannelHandlerAdapter)) && (!((ChannelHandlerAdapter)handler).isSharable()) && 
/*  41 */       (!handler.getClass().isAnnotationPresent(ChannelHandler.Sharable.class))) {
/*  42 */       throw new IllegalArgumentException("The handler must be Sharable");
/*     */     }
/*  44 */     return handler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Http2MultiplexCodecBuilder forClient(ChannelHandler childHandler)
/*     */   {
/*  54 */     return new Http2MultiplexCodecBuilder(false, childHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Http2MultiplexCodecBuilder forServer(ChannelHandler childHandler)
/*     */   {
/*  64 */     return new Http2MultiplexCodecBuilder(true, childHandler);
/*     */   }
/*     */   
/*     */   public Http2Settings initialSettings()
/*     */   {
/*  69 */     return super.initialSettings();
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder initialSettings(Http2Settings settings)
/*     */   {
/*  74 */     return (Http2MultiplexCodecBuilder)super.initialSettings(settings);
/*     */   }
/*     */   
/*     */   public long gracefulShutdownTimeoutMillis()
/*     */   {
/*  79 */     return super.gracefulShutdownTimeoutMillis();
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis)
/*     */   {
/*  84 */     return (Http2MultiplexCodecBuilder)super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder withUpgradeStreamHandler(ChannelHandler upgradeStreamHandler) {
/*  88 */     if (isServer()) {
/*  89 */       throw new IllegalArgumentException("Server codecs don't use an extra handler for the upgrade stream");
/*     */     }
/*  91 */     this.upgradeStreamHandler = upgradeStreamHandler;
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isServer()
/*     */   {
/*  97 */     return super.isServer();
/*     */   }
/*     */   
/*     */   public int maxReservedStreams()
/*     */   {
/* 102 */     return super.maxReservedStreams();
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder maxReservedStreams(int maxReservedStreams)
/*     */   {
/* 107 */     return (Http2MultiplexCodecBuilder)super.maxReservedStreams(maxReservedStreams);
/*     */   }
/*     */   
/*     */   public boolean isValidateHeaders()
/*     */   {
/* 112 */     return super.isValidateHeaders();
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder validateHeaders(boolean validateHeaders)
/*     */   {
/* 117 */     return (Http2MultiplexCodecBuilder)super.validateHeaders(validateHeaders);
/*     */   }
/*     */   
/*     */   public Http2FrameLogger frameLogger()
/*     */   {
/* 122 */     return super.frameLogger();
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder frameLogger(Http2FrameLogger frameLogger)
/*     */   {
/* 127 */     return (Http2MultiplexCodecBuilder)super.frameLogger(frameLogger);
/*     */   }
/*     */   
/*     */   public boolean encoderEnforceMaxConcurrentStreams()
/*     */   {
/* 132 */     return super.encoderEnforceMaxConcurrentStreams();
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams)
/*     */   {
/* 137 */     return (Http2MultiplexCodecBuilder)super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
/*     */   }
/*     */   
/*     */   public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector()
/*     */   {
/* 142 */     return super.headerSensitivityDetector();
/*     */   }
/*     */   
/*     */ 
/*     */   public Http2MultiplexCodecBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector)
/*     */   {
/* 148 */     return (Http2MultiplexCodecBuilder)super.headerSensitivityDetector(headerSensitivityDetector);
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder encoderIgnoreMaxHeaderListSize(boolean ignoreMaxHeaderListSize)
/*     */   {
/* 153 */     return (Http2MultiplexCodecBuilder)super.encoderIgnoreMaxHeaderListSize(ignoreMaxHeaderListSize);
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodecBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity)
/*     */   {
/* 158 */     return (Http2MultiplexCodecBuilder)super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
/*     */   }
/*     */   
/*     */   public Http2MultiplexCodec build()
/*     */   {
/* 163 */     return (Http2MultiplexCodec)super.build();
/*     */   }
/*     */   
/*     */ 
/*     */   protected Http2MultiplexCodec build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings)
/*     */   {
/* 169 */     return new Http2MultiplexCodec(encoder, decoder, initialSettings, this.childHandler, this.upgradeStreamHandler);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2MultiplexCodecBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */