/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.logging.LogLevel;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogLevel;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public class Http2FrameLogger
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private static final int BUFFER_LENGTH_THRESHOLD = 64;
/*     */   private final InternalLogger logger;
/*     */   private final InternalLogLevel level;
/*     */   
/*     */   public static enum Direction
/*     */   {
/*  37 */     INBOUND, 
/*  38 */     OUTBOUND;
/*     */     
/*     */ 
/*     */     private Direction() {}
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(LogLevel level)
/*     */   {
/*  46 */     this(level.toInternalLevel(), InternalLoggerFactory.getInstance(Http2FrameLogger.class));
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(LogLevel level, String name) {
/*  50 */     this(level.toInternalLevel(), InternalLoggerFactory.getInstance(name));
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(LogLevel level, Class<?> clazz) {
/*  54 */     this(level.toInternalLevel(), InternalLoggerFactory.getInstance(clazz));
/*     */   }
/*     */   
/*     */   private Http2FrameLogger(InternalLogLevel level, InternalLogger logger) {
/*  58 */     this.level = ((InternalLogLevel)ObjectUtil.checkNotNull(level, "level"));
/*  59 */     this.logger = ((InternalLogger)ObjectUtil.checkNotNull(logger, "logger"));
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  63 */     return this.logger.isEnabled(this.level);
/*     */   }
/*     */   
/*     */   public void logData(Direction direction, ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream)
/*     */   {
/*  68 */     if (isEnabled()) {
/*  69 */       this.logger.log(this.level, "{} {} DATA: streamId={} padding={} endStream={} length={} bytes={}", new Object[] { ctx.channel(), direction
/*  70 */         .name(), Integer.valueOf(streamId), Integer.valueOf(padding), Boolean.valueOf(endStream), Integer.valueOf(data.readableBytes()), toString(data) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logHeaders(Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream)
/*     */   {
/*  76 */     if (isEnabled()) {
/*  77 */       this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} padding={} endStream={}", new Object[] { ctx.channel(), direction
/*  78 */         .name(), Integer.valueOf(streamId), headers, Integer.valueOf(padding), Boolean.valueOf(endStream) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logHeaders(Direction direction, ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream)
/*     */   {
/*  84 */     if (isEnabled()) {
/*  85 */       this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} streamDependency={} weight={} exclusive={} padding={} endStream={}", new Object[] {ctx
/*  86 */         .channel(), direction
/*  87 */         .name(), Integer.valueOf(streamId), headers, Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive), Integer.valueOf(padding), Boolean.valueOf(endStream) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logPriority(Direction direction, ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */   {
/*  93 */     if (isEnabled()) {
/*  94 */       this.logger.log(this.level, "{} {} PRIORITY: streamId={} streamDependency={} weight={} exclusive={}", new Object[] { ctx.channel(), direction
/*  95 */         .name(), Integer.valueOf(streamId), Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logRstStream(Direction direction, ChannelHandlerContext ctx, int streamId, long errorCode) {
/* 100 */     if (isEnabled()) {
/* 101 */       this.logger.log(this.level, "{} {} RST_STREAM: streamId={} errorCode={}", new Object[] { ctx.channel(), direction
/* 102 */         .name(), Integer.valueOf(streamId), Long.valueOf(errorCode) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logSettingsAck(Direction direction, ChannelHandlerContext ctx) {
/* 107 */     this.logger.log(this.level, "{} {} SETTINGS: ack=true", ctx.channel(), direction.name());
/*     */   }
/*     */   
/*     */   public void logSettings(Direction direction, ChannelHandlerContext ctx, Http2Settings settings) {
/* 111 */     if (isEnabled()) {
/* 112 */       this.logger.log(this.level, "{} {} SETTINGS: ack=false settings={}", new Object[] { ctx.channel(), direction.name(), settings });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logPing(Direction direction, ChannelHandlerContext ctx, long data) {
/* 117 */     if (isEnabled()) {
/* 118 */       this.logger.log(this.level, "{} {} PING: ack=false bytes={}", new Object[] { ctx.channel(), direction
/* 119 */         .name(), Long.valueOf(data) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logPingAck(Direction direction, ChannelHandlerContext ctx, long data) {
/* 124 */     if (isEnabled()) {
/* 125 */       this.logger.log(this.level, "{} {} PING: ack=true bytes={}", new Object[] { ctx.channel(), direction
/* 126 */         .name(), Long.valueOf(data) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logPushPromise(Direction direction, ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */   {
/* 132 */     if (isEnabled()) {
/* 133 */       this.logger.log(this.level, "{} {} PUSH_PROMISE: streamId={} promisedStreamId={} headers={} padding={}", new Object[] {ctx
/* 134 */         .channel(), direction.name(), Integer.valueOf(streamId), Integer.valueOf(promisedStreamId), headers, Integer.valueOf(padding) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logGoAway(Direction direction, ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */   {
/* 140 */     if (isEnabled()) {
/* 141 */       this.logger.log(this.level, "{} {} GO_AWAY: lastStreamId={} errorCode={} length={} bytes={}", new Object[] { ctx.channel(), direction
/* 142 */         .name(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), Integer.valueOf(debugData.readableBytes()), toString(debugData) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logWindowsUpdate(Direction direction, ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*     */   {
/* 148 */     if (isEnabled()) {
/* 149 */       this.logger.log(this.level, "{} {} WINDOW_UPDATE: streamId={} windowSizeIncrement={}", new Object[] { ctx.channel(), direction
/* 150 */         .name(), Integer.valueOf(streamId), Integer.valueOf(windowSizeIncrement) });
/*     */     }
/*     */   }
/*     */   
/*     */   public void logUnknownFrame(Direction direction, ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf data)
/*     */   {
/* 156 */     if (isEnabled()) {
/* 157 */       this.logger.log(this.level, "{} {} UNKNOWN: frameType={} streamId={} flags={} length={} bytes={}", new Object[] { ctx.channel(), direction
/* 158 */         .name(), Integer.valueOf(frameType & 0xFF), Integer.valueOf(streamId), Short.valueOf(flags.value()), Integer.valueOf(data.readableBytes()), toString(data) });
/*     */     }
/*     */   }
/*     */   
/*     */   private String toString(ByteBuf buf) {
/* 163 */     if ((this.level == InternalLogLevel.TRACE) || (buf.readableBytes() <= 64))
/*     */     {
/* 165 */       return ByteBufUtil.hexDump(buf);
/*     */     }
/*     */     
/*     */ 
/* 169 */     int length = Math.min(buf.readableBytes(), 64);
/* 170 */     return ByteBufUtil.hexDump(buf, buf.readerIndex(), length) + "...";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2FrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */