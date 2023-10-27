/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.util.ArrayTrie;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.Trie;
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
/*     */ public enum HttpHeader
/*     */ {
/*  33 */   CONNECTION("Connection"), 
/*  34 */   CACHE_CONTROL("Cache-Control"), 
/*  35 */   DATE("Date"), 
/*  36 */   PRAGMA("Pragma"), 
/*  37 */   PROXY_CONNECTION("Proxy-Connection"), 
/*  38 */   TRAILER("Trailer"), 
/*  39 */   TRANSFER_ENCODING("Transfer-Encoding"), 
/*  40 */   UPGRADE("Upgrade"), 
/*  41 */   VIA("Via"), 
/*  42 */   WARNING("Warning"), 
/*  43 */   NEGOTIATE("Negotiate"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   ALLOW("Allow"), 
/*  49 */   CONTENT_ENCODING("Content-Encoding"), 
/*  50 */   CONTENT_LANGUAGE("Content-Language"), 
/*  51 */   CONTENT_LENGTH("Content-Length"), 
/*  52 */   CONTENT_LOCATION("Content-Location"), 
/*  53 */   CONTENT_MD5("Content-MD5"), 
/*  54 */   CONTENT_RANGE("Content-Range"), 
/*  55 */   CONTENT_TYPE("Content-Type"), 
/*  56 */   EXPIRES("Expires"), 
/*  57 */   LAST_MODIFIED("Last-Modified"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   ACCEPT("Accept"), 
/*  63 */   ACCEPT_CHARSET("Accept-Charset"), 
/*  64 */   ACCEPT_ENCODING("Accept-Encoding"), 
/*  65 */   ACCEPT_LANGUAGE("Accept-Language"), 
/*  66 */   AUTHORIZATION("Authorization"), 
/*  67 */   EXPECT("Expect"), 
/*  68 */   FORWARDED("Forwarded"), 
/*  69 */   FROM("From"), 
/*  70 */   HOST("Host"), 
/*  71 */   IF_MATCH("If-Match"), 
/*  72 */   IF_MODIFIED_SINCE("If-Modified-Since"), 
/*  73 */   IF_NONE_MATCH("If-None-Match"), 
/*  74 */   IF_RANGE("If-Range"), 
/*  75 */   IF_UNMODIFIED_SINCE("If-Unmodified-Since"), 
/*  76 */   KEEP_ALIVE("Keep-Alive"), 
/*  77 */   MAX_FORWARDS("Max-Forwards"), 
/*  78 */   PROXY_AUTHORIZATION("Proxy-Authorization"), 
/*  79 */   RANGE("Range"), 
/*  80 */   REQUEST_RANGE("Request-Range"), 
/*  81 */   REFERER("Referer"), 
/*  82 */   TE("TE"), 
/*  83 */   USER_AGENT("User-Agent"), 
/*  84 */   X_FORWARDED_FOR("X-Forwarded-For"), 
/*  85 */   X_FORWARDED_PROTO("X-Forwarded-Proto"), 
/*  86 */   X_FORWARDED_SERVER("X-Forwarded-Server"), 
/*  87 */   X_FORWARDED_HOST("X-Forwarded-Host"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   ACCEPT_RANGES("Accept-Ranges"), 
/*  93 */   AGE("Age"), 
/*  94 */   ETAG("ETag"), 
/*  95 */   LOCATION("Location"), 
/*  96 */   PROXY_AUTHENTICATE("Proxy-Authenticate"), 
/*  97 */   RETRY_AFTER("Retry-After"), 
/*  98 */   SERVER("Server"), 
/*  99 */   SERVLET_ENGINE("Servlet-Engine"), 
/* 100 */   VARY("Vary"), 
/* 101 */   WWW_AUTHENTICATE("WWW-Authenticate"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 106 */   ORIGIN("Origin"), 
/* 107 */   SEC_WEBSOCKET_KEY("Sec-WebSocket-Key"), 
/* 108 */   SEC_WEBSOCKET_VERSION("Sec-WebSocket-Version"), 
/* 109 */   SEC_WEBSOCKET_EXTENSIONS("Sec-WebSocket-Extensions"), 
/* 110 */   SEC_WEBSOCKET_SUBPROTOCOL("Sec-WebSocket-Protocol"), 
/* 111 */   SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 116 */   COOKIE("Cookie"), 
/* 117 */   SET_COOKIE("Set-Cookie"), 
/* 118 */   SET_COOKIE2("Set-Cookie2"), 
/* 119 */   MIME_VERSION("MIME-Version"), 
/* 120 */   IDENTITY("identity"), 
/*     */   
/* 122 */   X_POWERED_BY("X-Powered-By"), 
/* 123 */   HTTP2_SETTINGS("HTTP2-Settings"), 
/*     */   
/* 125 */   STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 130 */   C_METHOD(":method"), 
/* 131 */   C_SCHEME(":scheme"), 
/* 132 */   C_AUTHORITY(":authority"), 
/* 133 */   C_PATH(":path"), 
/* 134 */   C_STATUS(":status"), 
/*     */   
/* 136 */   UNKNOWN("::UNKNOWN::");
/*     */   
/*     */   public static final Trie<HttpHeader> CACHE;
/*     */   
/* 140 */   static { CACHE = new ArrayTrie(630);
/*     */     
/*     */ 
/* 143 */     for (HttpHeader header : values()) {
/* 144 */       if ((header != UNKNOWN) && 
/* 145 */         (!CACHE.put(header.toString(), header))) {
/* 146 */         throw new IllegalStateException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final String _string;
/*     */   private final byte[] _bytes;
/*     */   private final byte[] _bytesColonSpace;
/*     */   private final ByteBuffer _buffer;
/*     */   private HttpHeader(String s)
/*     */   {
/* 157 */     this._string = s;
/* 158 */     this._bytes = StringUtil.getBytes(s);
/* 159 */     this._bytesColonSpace = StringUtil.getBytes(s + ": ");
/* 160 */     this._buffer = ByteBuffer.wrap(this._bytes);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer toBuffer()
/*     */   {
/* 166 */     return this._buffer.asReadOnlyBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 172 */     return this._bytes;
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getBytesColonSpace()
/*     */   {
/* 178 */     return this._bytesColonSpace;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean is(String s)
/*     */   {
/* 184 */     return this._string.equalsIgnoreCase(s);
/*     */   }
/*     */   
/*     */ 
/*     */   public String asString()
/*     */   {
/* 190 */     return this._string;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 197 */     return this._string;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */