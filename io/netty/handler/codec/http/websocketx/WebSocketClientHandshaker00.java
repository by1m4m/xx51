/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public class WebSocketClientHandshaker00
/*     */   extends WebSocketClientHandshaker
/*     */ {
/*  46 */   private static final AsciiString WEBSOCKET = AsciiString.cached("WebSocket");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBuf expectedChallengeResponseBytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebSocketClientHandshaker00(URI webSocketURL, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength)
/*     */   {
/*  67 */     super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
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
/*  91 */     int spaces1 = WebSocketUtil.randomNumber(1, 12);
/*  92 */     int spaces2 = WebSocketUtil.randomNumber(1, 12);
/*     */     
/*  94 */     int max1 = Integer.MAX_VALUE / spaces1;
/*  95 */     int max2 = Integer.MAX_VALUE / spaces2;
/*     */     
/*  97 */     int number1 = WebSocketUtil.randomNumber(0, max1);
/*  98 */     int number2 = WebSocketUtil.randomNumber(0, max2);
/*     */     
/* 100 */     int product1 = number1 * spaces1;
/* 101 */     int product2 = number2 * spaces2;
/*     */     
/* 103 */     String key1 = Integer.toString(product1);
/* 104 */     String key2 = Integer.toString(product2);
/*     */     
/* 106 */     key1 = insertRandomCharacters(key1);
/* 107 */     key2 = insertRandomCharacters(key2);
/*     */     
/* 109 */     key1 = insertSpaces(key1, spaces1);
/* 110 */     key2 = insertSpaces(key2, spaces2);
/*     */     
/* 112 */     byte[] key3 = WebSocketUtil.randomBytes(8);
/*     */     
/* 114 */     ByteBuffer buffer = ByteBuffer.allocate(4);
/* 115 */     buffer.putInt(number1);
/* 116 */     byte[] number1Array = buffer.array();
/* 117 */     buffer = ByteBuffer.allocate(4);
/* 118 */     buffer.putInt(number2);
/* 119 */     byte[] number2Array = buffer.array();
/*     */     
/* 121 */     byte[] challenge = new byte[16];
/* 122 */     System.arraycopy(number1Array, 0, challenge, 0, 4);
/* 123 */     System.arraycopy(number2Array, 0, challenge, 4, 4);
/* 124 */     System.arraycopy(key3, 0, challenge, 8, 8);
/* 125 */     this.expectedChallengeResponseBytes = Unpooled.wrappedBuffer(WebSocketUtil.md5(challenge));
/*     */     
/*     */ 
/* 128 */     URI wsURL = uri();
/* 129 */     String path = rawPath(wsURL);
/*     */     
/*     */ 
/* 132 */     FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
/* 133 */     HttpHeaders headers = request.headers();
/*     */     
/* 135 */     if (this.customHeaders != null) {
/* 136 */       headers.add(this.customHeaders);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */     headers.set(HttpHeaderNames.UPGRADE, WEBSOCKET).set(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE).set(HttpHeaderNames.HOST, websocketHostValue(wsURL)).set(HttpHeaderNames.ORIGIN, websocketOriginValue(wsURL)).set(HttpHeaderNames.SEC_WEBSOCKET_KEY1, key1).set(HttpHeaderNames.SEC_WEBSOCKET_KEY2, key2);
/*     */     
/* 146 */     String expectedSubprotocol = expectedSubprotocol();
/* 147 */     if ((expectedSubprotocol != null) && (!expectedSubprotocol.isEmpty())) {
/* 148 */       headers.set(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 153 */     headers.set(HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(key3.length));
/* 154 */     request.content().writeBytes(key3);
/* 155 */     return request;
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
/*     */   protected void verify(FullHttpResponse response)
/*     */   {
/* 180 */     if (!response.status().equals(HttpResponseStatus.SWITCHING_PROTOCOLS)) {
/* 181 */       throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.status());
/*     */     }
/*     */     
/* 184 */     HttpHeaders headers = response.headers();
/*     */     
/* 186 */     CharSequence upgrade = headers.get(HttpHeaderNames.UPGRADE);
/* 187 */     if (!WEBSOCKET.contentEqualsIgnoreCase(upgrade)) {
/* 188 */       throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade);
/*     */     }
/*     */     
/*     */ 
/* 192 */     if (!headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true))
/*     */     {
/* 194 */       throw new WebSocketHandshakeException("Invalid handshake response connection: " + headers.get(HttpHeaderNames.CONNECTION));
/*     */     }
/*     */     
/* 197 */     ByteBuf challenge = response.content();
/* 198 */     if (!challenge.equals(this.expectedChallengeResponseBytes)) {
/* 199 */       throw new WebSocketHandshakeException("Invalid challenge");
/*     */     }
/*     */   }
/*     */   
/*     */   private static String insertRandomCharacters(String key) {
/* 204 */     int count = WebSocketUtil.randomNumber(1, 12);
/*     */     
/* 206 */     char[] randomChars = new char[count];
/* 207 */     int randCount = 0;
/* 208 */     while (randCount < count) {
/* 209 */       int rand = (int)(Math.random() * 126.0D + 33.0D);
/* 210 */       if (((33 < rand) && (rand < 47)) || ((58 < rand) && (rand < 126))) {
/* 211 */         randomChars[randCount] = ((char)rand);
/* 212 */         randCount++;
/*     */       }
/*     */     }
/*     */     
/* 216 */     for (int i = 0; i < count; i++) {
/* 217 */       int split = WebSocketUtil.randomNumber(0, key.length());
/* 218 */       String part1 = key.substring(0, split);
/* 219 */       String part2 = key.substring(split);
/* 220 */       key = part1 + randomChars[i] + part2;
/*     */     }
/*     */     
/* 223 */     return key;
/*     */   }
/*     */   
/*     */   private static String insertSpaces(String key, int spaces) {
/* 227 */     for (int i = 0; i < spaces; i++) {
/* 228 */       int split = WebSocketUtil.randomNumber(1, key.length() - 1);
/* 229 */       String part1 = key.substring(0, split);
/* 230 */       String part2 = key.substring(split);
/* 231 */       key = part1 + ' ' + part2;
/*     */     }
/*     */     
/* 234 */     return key;
/*     */   }
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder()
/*     */   {
/* 239 */     return new WebSocket00FrameDecoder(maxFramePayloadLength());
/*     */   }
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder()
/*     */   {
/* 244 */     return new WebSocket00FrameEncoder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */