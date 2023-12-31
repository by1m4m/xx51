/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.handler.codec.http.HttpServerUpgradeHandler;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public final class CleartextHttp2ServerUpgradeHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  43 */   private static final ByteBuf CONNECTION_PREFACE = Unpooled.unreleasableBuffer(Http2CodecUtil.connectionPrefaceBuf());
/*     */   
/*     */ 
/*     */ 
/*     */   private final HttpServerCodec httpServerCodec;
/*     */   
/*     */ 
/*     */ 
/*     */   private final HttpServerUpgradeHandler httpServerUpgradeHandler;
/*     */   
/*     */ 
/*     */ 
/*     */   private final ChannelHandler http2ServerHandler;
/*     */   
/*     */ 
/*     */ 
/*     */   public CleartextHttp2ServerUpgradeHandler(HttpServerCodec httpServerCodec, HttpServerUpgradeHandler httpServerUpgradeHandler, ChannelHandler http2ServerHandler)
/*     */   {
/*  61 */     this.httpServerCodec = ((HttpServerCodec)ObjectUtil.checkNotNull(httpServerCodec, "httpServerCodec"));
/*  62 */     this.httpServerUpgradeHandler = ((HttpServerUpgradeHandler)ObjectUtil.checkNotNull(httpServerUpgradeHandler, "httpServerUpgradeHandler"));
/*  63 */     this.http2ServerHandler = ((ChannelHandler)ObjectUtil.checkNotNull(http2ServerHandler, "http2ServerHandler"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  71 */     ctx.pipeline().addBefore(ctx.name(), null, new PriorKnowledgeHandler(null)).addBefore(ctx.name(), null, this.httpServerCodec).replace(this, null, this.httpServerUpgradeHandler);
/*     */   }
/*     */   
/*     */   private final class PriorKnowledgeHandler
/*     */     extends ByteToMessageDecoder
/*     */   {
/*     */     private PriorKnowledgeHandler() {}
/*     */     
/*     */     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */     {
/*  81 */       int prefaceLength = CleartextHttp2ServerUpgradeHandler.CONNECTION_PREFACE.readableBytes();
/*  82 */       int bytesRead = Math.min(in.readableBytes(), prefaceLength);
/*     */       
/*  84 */       if (!ByteBufUtil.equals(CleartextHttp2ServerUpgradeHandler.CONNECTION_PREFACE, CleartextHttp2ServerUpgradeHandler.CONNECTION_PREFACE.readerIndex(), in, in
/*  85 */         .readerIndex(), bytesRead)) {
/*  86 */         ctx.pipeline().remove(this);
/*  87 */       } else if (bytesRead == prefaceLength)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  92 */         ctx.pipeline().remove(CleartextHttp2ServerUpgradeHandler.this.httpServerCodec).remove(CleartextHttp2ServerUpgradeHandler.this.httpServerUpgradeHandler);
/*     */         
/*  94 */         ctx.pipeline().addAfter(ctx.name(), null, CleartextHttp2ServerUpgradeHandler.this.http2ServerHandler);
/*  95 */         ctx.pipeline().remove(this);
/*     */         
/*  97 */         ctx.fireUserEventTriggered(CleartextHttp2ServerUpgradeHandler.PriorKnowledgeUpgradeEvent.INSTANCE);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class PriorKnowledgeUpgradeEvent
/*     */   {
/* 106 */     private static final PriorKnowledgeUpgradeEvent INSTANCE = new PriorKnowledgeUpgradeEvent();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\CleartextHttp2ServerUpgradeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */