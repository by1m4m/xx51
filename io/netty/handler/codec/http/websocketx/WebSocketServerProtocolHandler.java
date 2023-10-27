/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
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
/*     */ 
/*     */ 
/*     */ public class WebSocketServerProtocolHandler
/*     */   extends WebSocketProtocolHandler
/*     */ {
/*     */   public static enum ServerHandshakeStateEvent
/*     */   {
/*  66 */     HANDSHAKE_COMPLETE;
/*     */     
/*     */     private ServerHandshakeStateEvent() {}
/*     */   }
/*     */   
/*     */   public static final class HandshakeComplete
/*     */   {
/*     */     private final String requestUri;
/*     */     private final HttpHeaders requestHeaders;
/*     */     private final String selectedSubprotocol;
/*     */     
/*     */     HandshakeComplete(String requestUri, HttpHeaders requestHeaders, String selectedSubprotocol)
/*     */     {
/*  79 */       this.requestUri = requestUri;
/*  80 */       this.requestHeaders = requestHeaders;
/*  81 */       this.selectedSubprotocol = selectedSubprotocol;
/*     */     }
/*     */     
/*     */     public String requestUri() {
/*  85 */       return this.requestUri;
/*     */     }
/*     */     
/*     */     public HttpHeaders requestHeaders() {
/*  89 */       return this.requestHeaders;
/*     */     }
/*     */     
/*     */     public String selectedSubprotocol() {
/*  93 */       return this.selectedSubprotocol;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  98 */   private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
/*     */   private final String websocketPath;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadLength;
/*     */   private final boolean allowMaskMismatch;
/*     */   private final boolean checkStartsWith;
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath)
/*     */   {
/* 108 */     this(websocketPath, null, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, boolean checkStartsWith) {
/* 112 */     this(websocketPath, null, false, 65536, false, checkStartsWith);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols) {
/* 116 */     this(websocketPath, subprotocols, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions) {
/* 120 */     this(websocketPath, subprotocols, allowExtensions, 65536);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize)
/*     */   {
/* 125 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch)
/*     */   {
/* 130 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, boolean checkStartsWith)
/*     */   {
/* 135 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch, checkStartsWith, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, boolean checkStartsWith, boolean dropPongFrames)
/*     */   {
/* 141 */     super(dropPongFrames);
/* 142 */     this.websocketPath = websocketPath;
/* 143 */     this.subprotocols = subprotocols;
/* 144 */     this.allowExtensions = allowExtensions;
/* 145 */     this.maxFramePayloadLength = maxFrameSize;
/* 146 */     this.allowMaskMismatch = allowMaskMismatch;
/* 147 */     this.checkStartsWith = checkStartsWith;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */   {
/* 152 */     ChannelPipeline cp = ctx.pipeline();
/* 153 */     if (cp.get(WebSocketServerProtocolHandshakeHandler.class) == null)
/*     */     {
/* 155 */       ctx.pipeline().addBefore(ctx.name(), WebSocketServerProtocolHandshakeHandler.class.getName(), new WebSocketServerProtocolHandshakeHandler(this.websocketPath, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch, this.checkStartsWith));
/*     */     }
/*     */     
/*     */ 
/* 159 */     if (cp.get(Utf8FrameValidator.class) == null)
/*     */     {
/* 161 */       ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), new Utf8FrameValidator());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 168 */     if ((frame instanceof CloseWebSocketFrame)) {
/* 169 */       WebSocketServerHandshaker handshaker = getHandshaker(ctx.channel());
/* 170 */       if (handshaker != null) {
/* 171 */         frame.retain();
/* 172 */         handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame);
/*     */       } else {
/* 174 */         ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
/*     */       }
/* 176 */       return;
/*     */     }
/* 178 */     super.decode(ctx, frame, out);
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 183 */     if ((cause instanceof WebSocketHandshakeException))
/*     */     {
/* 185 */       FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer(cause.getMessage().getBytes()));
/* 186 */       ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
/*     */     } else {
/* 188 */       ctx.fireExceptionCaught(cause);
/* 189 */       ctx.close();
/*     */     }
/*     */   }
/*     */   
/*     */   static WebSocketServerHandshaker getHandshaker(Channel channel) {
/* 194 */     return (WebSocketServerHandshaker)channel.attr(HANDSHAKER_ATTR_KEY).get();
/*     */   }
/*     */   
/*     */   static void setHandshaker(Channel channel, WebSocketServerHandshaker handshaker) {
/* 198 */     channel.attr(HANDSHAKER_ATTR_KEY).set(handshaker);
/*     */   }
/*     */   
/*     */   static ChannelHandler forbiddenHttpRequestResponder() {
/* 202 */     new ChannelInboundHandlerAdapter()
/*     */     {
/*     */       public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 205 */         if ((msg instanceof FullHttpRequest)) {
/* 206 */           ((FullHttpRequest)msg).release();
/* 207 */           FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
/*     */           
/* 209 */           ctx.channel().writeAndFlush(response);
/*     */         } else {
/* 211 */           ctx.fireChannelRead(msg);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerProtocolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */