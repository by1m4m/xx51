/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.URI;
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
/*     */ public class WebSocketClientHandshaker07
/*     */   extends WebSocketClientHandshaker
/*     */ {
/*  42 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker07.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   
/*     */ 
/*     */ 
/*     */   private String expectedChallengeResponseString;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean allowExtensions;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean performMasking;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClientHandshaker07(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength)
/*     */   {
/*  70 */     this(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, true, false);
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
/*     */   public WebSocketClientHandshaker07(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean performMasking, boolean allowMaskMismatch)
/*     */   {
/* 100 */     super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
/* 101 */     this.allowExtensions = allowExtensions;
/* 102 */     this.performMasking = performMasking;
/* 103 */     this.allowMaskMismatch = allowMaskMismatch;
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
/*     */   protected FullHttpRequest newHandshakeRequest()
/*     */   {
/* 127 */     URI wsURL = uri();
/* 128 */     String path = rawPath(wsURL);
/*     */     
/*     */ 
/* 131 */     byte[] nonce = WebSocketUtil.randomBytes(16);
/* 132 */     String key = WebSocketUtil.base64(nonce);
/*     */     
/* 134 */     String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/* 135 */     byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
/* 136 */     this.expectedChallengeResponseString = WebSocketUtil.base64(sha1);
/*     */     
/* 138 */     if (logger.isDebugEnabled()) {
/* 139 */       logger.debug("WebSocket version 07 client handshake key: {}, expected response: {}", key, this.expectedChallengeResponseString);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 145 */     FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
/* 146 */     HttpHeaders headers = request.headers();
/*     */     
/* 148 */     if (this.customHeaders != null) {
/* 149 */       headers.add(this.customHeaders);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */     headers.set(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET).set(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE).set(HttpHeaderNames.SEC_WEBSOCKET_KEY, key).set(HttpHeaderNames.HOST, websocketHostValue(wsURL)).set(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN, websocketOriginValue(wsURL));
/*     */     
/* 158 */     String expectedSubprotocol = expectedSubprotocol();
/* 159 */     if ((expectedSubprotocol != null) && (!expectedSubprotocol.isEmpty())) {
/* 160 */       headers.set(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
/*     */     }
/*     */     
/* 163 */     headers.set(HttpHeaderNames.SEC_WEBSOCKET_VERSION, "7");
/* 164 */     return request;
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
/*     */   protected void verify(FullHttpResponse response)
/*     */   {
/* 186 */     HttpResponseStatus status = HttpResponseStatus.SWITCHING_PROTOCOLS;
/* 187 */     HttpHeaders headers = response.headers();
/*     */     
/* 189 */     if (!response.status().equals(status)) {
/* 190 */       throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.status());
/*     */     }
/*     */     
/* 193 */     CharSequence upgrade = headers.get(HttpHeaderNames.UPGRADE);
/* 194 */     if (!HttpHeaderValues.WEBSOCKET.contentEqualsIgnoreCase(upgrade)) {
/* 195 */       throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade);
/*     */     }
/*     */     
/* 198 */     if (!headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true))
/*     */     {
/* 200 */       throw new WebSocketHandshakeException("Invalid handshake response connection: " + headers.get(HttpHeaderNames.CONNECTION));
/*     */     }
/*     */     
/* 203 */     CharSequence accept = headers.get(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT);
/* 204 */     if ((accept == null) || (!accept.equals(this.expectedChallengeResponseString))) {
/* 205 */       throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", new Object[] { accept, this.expectedChallengeResponseString }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 212 */     return new WebSocket07FrameDecoder(false, this.allowExtensions, maxFramePayloadLength(), this.allowMaskMismatch);
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 217 */     return new WebSocket07FrameEncoder(this.performMasking);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker07.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */