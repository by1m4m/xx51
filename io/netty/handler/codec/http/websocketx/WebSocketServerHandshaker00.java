/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class WebSocketServerHandshaker00
/*     */   extends WebSocketServerHandshaker
/*     */ {
/*  47 */   private static final Pattern BEGINNING_DIGIT = Pattern.compile("[^0-9]");
/*  48 */   private static final Pattern BEGINNING_SPACE = Pattern.compile("[^ ]");
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
/*     */   public WebSocketServerHandshaker00(String webSocketURL, String subprotocols, int maxFramePayloadLength)
/*     */   {
/*  63 */     super(WebSocketVersion.V00, webSocketURL, subprotocols, maxFramePayloadLength);
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
/*     */   protected FullHttpResponse newHandshakeResponse(FullHttpRequest req, HttpHeaders headers)
/*     */   {
/* 110 */     if ((!req.headers().containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true)) || 
/* 111 */       (!HttpHeaderValues.WEBSOCKET.contentEqualsIgnoreCase(req.headers().get(HttpHeaderNames.UPGRADE)))) {
/* 112 */       throw new WebSocketHandshakeException("not a WebSocket handshake request: missing upgrade");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 117 */     boolean isHixie76 = (req.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1)) && (req.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2));
/*     */     
/*     */ 
/* 120 */     FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, isHixie76 ? "WebSocket Protocol Handshake" : "Web Socket Protocol Handshake"));
/*     */     
/* 122 */     if (headers != null) {
/* 123 */       res.headers().add(headers);
/*     */     }
/*     */     
/* 126 */     res.headers().add(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
/* 127 */     res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/*     */     
/*     */ 
/* 130 */     if (isHixie76)
/*     */     {
/* 132 */       res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, req.headers().get(HttpHeaderNames.ORIGIN));
/* 133 */       res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_LOCATION, uri());
/*     */       
/* 135 */       String subprotocols = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 136 */       if (subprotocols != null) {
/* 137 */         String selectedSubprotocol = selectSubprotocol(subprotocols);
/* 138 */         if (selectedSubprotocol == null) {
/* 139 */           if (logger.isDebugEnabled()) {
/* 140 */             logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
/*     */           }
/*     */         } else {
/* 143 */           res.headers().add(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 148 */       String key1 = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY1);
/* 149 */       String key2 = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY2);
/*     */       
/* 151 */       int a = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(key1).replaceAll("")) / BEGINNING_SPACE.matcher(key1).replaceAll("").length());
/*     */       
/* 153 */       int b = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(key2).replaceAll("")) / BEGINNING_SPACE.matcher(key2).replaceAll("").length());
/* 154 */       long c = req.content().readLong();
/* 155 */       ByteBuf input = Unpooled.buffer(16);
/* 156 */       input.writeInt(a);
/* 157 */       input.writeInt(b);
/* 158 */       input.writeLong(c);
/* 159 */       res.content().writeBytes(WebSocketUtil.md5(input.array()));
/*     */     }
/*     */     else {
/* 162 */       res.headers().add(HttpHeaderNames.WEBSOCKET_ORIGIN, req.headers().get(HttpHeaderNames.ORIGIN));
/* 163 */       res.headers().add(HttpHeaderNames.WEBSOCKET_LOCATION, uri());
/*     */       
/* 165 */       String protocol = req.headers().get(HttpHeaderNames.WEBSOCKET_PROTOCOL);
/* 166 */       if (protocol != null) {
/* 167 */         res.headers().add(HttpHeaderNames.WEBSOCKET_PROTOCOL, selectSubprotocol(protocol));
/*     */       }
/*     */     }
/* 170 */     return res;
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise)
/*     */   {
/* 183 */     return channel.writeAndFlush(frame, promise);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 188 */     return new WebSocket00FrameDecoder(maxFramePayloadLength());
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 193 */     return new WebSocket00FrameEncoder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */