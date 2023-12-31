/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class Http2InboundFrameLogger
/*     */   implements Http2FrameReader
/*     */ {
/*     */   private final Http2FrameReader reader;
/*     */   private final Http2FrameLogger logger;
/*     */   
/*     */   public Http2InboundFrameLogger(Http2FrameReader reader, Http2FrameLogger logger)
/*     */   {
/*  34 */     this.reader = ((Http2FrameReader)ObjectUtil.checkNotNull(reader, "reader"));
/*  35 */     this.logger = ((Http2FrameLogger)ObjectUtil.checkNotNull(logger, "logger"));
/*     */   }
/*     */   
/*     */   public void readFrame(ChannelHandlerContext ctx, ByteBuf input, final Http2FrameListener listener)
/*     */     throws Http2Exception
/*     */   {
/*  41 */     this.reader.readFrame(ctx, input, new Http2FrameListener()
/*     */     {
/*     */ 
/*     */       public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*     */         throws Http2Exception
/*     */       {
/*  47 */         Http2InboundFrameLogger.this.logger.logData(Http2FrameLogger.Direction.INBOUND, ctx, streamId, data, padding, endOfStream);
/*  48 */         return listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream)
/*     */         throws Http2Exception
/*     */       {
/*  55 */         Http2InboundFrameLogger.this.logger.logHeaders(Http2FrameLogger.Direction.INBOUND, ctx, streamId, headers, padding, endStream);
/*  56 */         listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream)
/*     */         throws Http2Exception
/*     */       {
/*  63 */         Http2InboundFrameLogger.this.logger.logHeaders(Http2FrameLogger.Direction.INBOUND, ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */         
/*  65 */         listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */         throws Http2Exception
/*     */       {
/*  72 */         Http2InboundFrameLogger.this.logger.logPriority(Http2FrameLogger.Direction.INBOUND, ctx, streamId, streamDependency, weight, exclusive);
/*  73 */         listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */       }
/*     */       
/*     */       public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode)
/*     */         throws Http2Exception
/*     */       {
/*  79 */         Http2InboundFrameLogger.this.logger.logRstStream(Http2FrameLogger.Direction.INBOUND, ctx, streamId, errorCode);
/*  80 */         listener.onRstStreamRead(ctx, streamId, errorCode);
/*     */       }
/*     */       
/*     */       public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception
/*     */       {
/*  85 */         Http2InboundFrameLogger.this.logger.logSettingsAck(Http2FrameLogger.Direction.INBOUND, ctx);
/*  86 */         listener.onSettingsAckRead(ctx);
/*     */       }
/*     */       
/*     */       public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings)
/*     */         throws Http2Exception
/*     */       {
/*  92 */         Http2InboundFrameLogger.this.logger.logSettings(Http2FrameLogger.Direction.INBOUND, ctx, settings);
/*  93 */         listener.onSettingsRead(ctx, settings);
/*     */       }
/*     */       
/*     */       public void onPingRead(ChannelHandlerContext ctx, long data) throws Http2Exception
/*     */       {
/*  98 */         Http2InboundFrameLogger.this.logger.logPing(Http2FrameLogger.Direction.INBOUND, ctx, data);
/*  99 */         listener.onPingRead(ctx, data);
/*     */       }
/*     */       
/*     */       public void onPingAckRead(ChannelHandlerContext ctx, long data) throws Http2Exception
/*     */       {
/* 104 */         Http2InboundFrameLogger.this.logger.logPingAck(Http2FrameLogger.Direction.INBOUND, ctx, data);
/* 105 */         listener.onPingAckRead(ctx, data);
/*     */       }
/*     */       
/*     */       public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */         throws Http2Exception
/*     */       {
/* 111 */         Http2InboundFrameLogger.this.logger.logPushPromise(Http2FrameLogger.Direction.INBOUND, ctx, streamId, promisedStreamId, headers, padding);
/* 112 */         listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */       }
/*     */       
/*     */       public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */         throws Http2Exception
/*     */       {
/* 118 */         Http2InboundFrameLogger.this.logger.logGoAway(Http2FrameLogger.Direction.INBOUND, ctx, lastStreamId, errorCode, debugData);
/* 119 */         listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */       }
/*     */       
/*     */       public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*     */         throws Http2Exception
/*     */       {
/* 125 */         Http2InboundFrameLogger.this.logger.logWindowsUpdate(Http2FrameLogger.Direction.INBOUND, ctx, streamId, windowSizeIncrement);
/* 126 */         listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */       }
/*     */       
/*     */       public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload)
/*     */         throws Http2Exception
/*     */       {
/* 132 */         Http2InboundFrameLogger.this.logger.logUnknownFrame(Http2FrameLogger.Direction.INBOUND, ctx, frameType, streamId, flags, payload);
/* 133 */         listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 140 */     this.reader.close();
/*     */   }
/*     */   
/*     */   public Http2FrameReader.Configuration configuration()
/*     */   {
/* 145 */     return this.reader.configuration();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2InboundFrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */