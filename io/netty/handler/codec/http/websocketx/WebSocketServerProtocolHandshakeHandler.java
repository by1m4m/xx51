/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.handler.ssl.SslHandler;
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
/*     */ class WebSocketServerProtocolHandshakeHandler
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*     */   private final String websocketPath;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadSize;
/*     */   private final boolean allowMaskMismatch;
/*     */   private final boolean checkStartsWith;
/*     */   
/*     */   WebSocketServerProtocolHandshakeHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch)
/*     */   {
/*  49 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch, false);
/*     */   }
/*     */   
/*     */   WebSocketServerProtocolHandshakeHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, boolean checkStartsWith)
/*     */   {
/*  54 */     this.websocketPath = websocketPath;
/*  55 */     this.subprotocols = subprotocols;
/*  56 */     this.allowExtensions = allowExtensions;
/*  57 */     this.maxFramePayloadSize = maxFrameSize;
/*  58 */     this.allowMaskMismatch = allowMaskMismatch;
/*  59 */     this.checkStartsWith = checkStartsWith;
/*     */   }
/*     */   
/*     */   public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/*  64 */     final FullHttpRequest req = (FullHttpRequest)msg;
/*  65 */     if (isNotWebSocketPath(req)) {
/*  66 */       ctx.fireChannelRead(msg);
/*  67 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  71 */       if (req.method() != HttpMethod.GET) {
/*  72 */         sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
/*  73 */         return;
/*     */       }
/*     */       
/*     */ 
/*  77 */       WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(ctx.pipeline(), req, this.websocketPath), this.subprotocols, this.allowExtensions, this.maxFramePayloadSize, this.allowMaskMismatch);
/*     */       
/*  79 */       final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
/*  80 */       if (handshaker == null) {
/*  81 */         WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
/*     */       } else {
/*  83 */         ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
/*  84 */         handshakeFuture.addListener(new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/*  87 */             if (!future.isSuccess()) {
/*  88 */               ctx.fireExceptionCaught(future.cause());
/*     */             }
/*     */             else {
/*  91 */               ctx.fireUserEventTriggered(WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
/*     */               
/*  93 */               ctx.fireUserEventTriggered(new WebSocketServerProtocolHandler.HandshakeComplete(req
/*     */               
/*  95 */                 .uri(), req.headers(), handshaker.selectedSubprotocol()));
/*     */             }
/*     */           }
/*  98 */         });
/*  99 */         WebSocketServerProtocolHandler.setHandshaker(ctx.channel(), handshaker);
/* 100 */         ctx.pipeline().replace(this, "WS403Responder", 
/* 101 */           WebSocketServerProtocolHandler.forbiddenHttpRequestResponder());
/*     */       }
/*     */     } finally {
/* 104 */       req.release();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isNotWebSocketPath(FullHttpRequest req) {
/* 109 */     return !req.uri().startsWith(this.websocketPath);
/*     */   }
/*     */   
/*     */   private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
/* 113 */     ChannelFuture f = ctx.channel().writeAndFlush(res);
/* 114 */     if ((!HttpUtil.isKeepAlive(req)) || (res.status().code() != 200)) {
/* 115 */       f.addListener(ChannelFutureListener.CLOSE);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
/* 120 */     String protocol = "ws";
/* 121 */     if (cp.get(SslHandler.class) != null)
/*     */     {
/* 123 */       protocol = "wss";
/*     */     }
/* 125 */     String host = req.headers().get(HttpHeaderNames.HOST);
/* 126 */     return protocol + "://" + host + path;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerProtocolHandshakeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */