/*     */ package io.netty.handler.codec.http2;
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
/*     */ public final class Http2ConnectionHandlerBuilder
/*     */   extends AbstractHttp2ConnectionHandlerBuilder<Http2ConnectionHandler, Http2ConnectionHandlerBuilder>
/*     */ {
/*     */   public Http2ConnectionHandlerBuilder validateHeaders(boolean validateHeaders)
/*     */   {
/*  31 */     return (Http2ConnectionHandlerBuilder)super.validateHeaders(validateHeaders);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder initialSettings(Http2Settings settings)
/*     */   {
/*  36 */     return (Http2ConnectionHandlerBuilder)super.initialSettings(settings);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder frameListener(Http2FrameListener frameListener)
/*     */   {
/*  41 */     return (Http2ConnectionHandlerBuilder)super.frameListener(frameListener);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis)
/*     */   {
/*  46 */     return (Http2ConnectionHandlerBuilder)super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder server(boolean isServer)
/*     */   {
/*  51 */     return (Http2ConnectionHandlerBuilder)super.server(isServer);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder connection(Http2Connection connection)
/*     */   {
/*  56 */     return (Http2ConnectionHandlerBuilder)super.connection(connection);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder maxReservedStreams(int maxReservedStreams)
/*     */   {
/*  61 */     return (Http2ConnectionHandlerBuilder)super.maxReservedStreams(maxReservedStreams);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder)
/*     */   {
/*  66 */     return (Http2ConnectionHandlerBuilder)super.codec(decoder, encoder);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder frameLogger(Http2FrameLogger frameLogger)
/*     */   {
/*  71 */     return (Http2ConnectionHandlerBuilder)super.frameLogger(frameLogger);
/*     */   }
/*     */   
/*     */ 
/*     */   public Http2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(boolean encoderEnforceMaxConcurrentStreams)
/*     */   {
/*  77 */     return (Http2ConnectionHandlerBuilder)super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder encoderIgnoreMaxHeaderListSize(boolean encoderIgnoreMaxHeaderListSize)
/*     */   {
/*  82 */     return (Http2ConnectionHandlerBuilder)super.encoderIgnoreMaxHeaderListSize(encoderIgnoreMaxHeaderListSize);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder headerSensitivityDetector(Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector)
/*     */   {
/*  87 */     return (Http2ConnectionHandlerBuilder)super.headerSensitivityDetector(headerSensitivityDetector);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandlerBuilder initialHuffmanDecodeCapacity(int initialHuffmanDecodeCapacity)
/*     */   {
/*  92 */     return (Http2ConnectionHandlerBuilder)super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandler build()
/*     */   {
/*  97 */     return super.build();
/*     */   }
/*     */   
/*     */ 
/*     */   protected Http2ConnectionHandler build(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings)
/*     */   {
/* 103 */     return new Http2ConnectionHandler(decoder, encoder, initialSettings);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2ConnectionHandlerBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */