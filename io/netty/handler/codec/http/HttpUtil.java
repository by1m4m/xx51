/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public final class HttpUtil
/*     */ {
/*  35 */   private static final AsciiString CHARSET_EQUALS = AsciiString.of(HttpHeaderValues.CHARSET + "=");
/*  36 */   private static final AsciiString SEMICOLON = AsciiString.cached(";");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isOriginForm(URI uri)
/*     */   {
/*  45 */     return (uri.getScheme() == null) && (uri.getSchemeSpecificPart() == null) && 
/*  46 */       (uri.getHost() == null) && (uri.getAuthority() == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAsteriskForm(URI uri)
/*     */   {
/*  54 */     return ("*".equals(uri.getPath())) && 
/*  55 */       (uri.getScheme() == null) && (uri.getSchemeSpecificPart() == null) && 
/*  56 */       (uri.getHost() == null) && (uri.getAuthority() == null) && (uri.getQuery() == null) && 
/*  57 */       (uri.getFragment() == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isKeepAlive(HttpMessage message)
/*     */   {
/*  68 */     CharSequence connection = message.headers().get(HttpHeaderNames.CONNECTION);
/*  69 */     if (HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
/*  70 */       return false;
/*     */     }
/*     */     
/*  73 */     if (message.protocolVersion().isKeepAliveDefault()) {
/*  74 */       return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
/*     */     }
/*  76 */     return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
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
/*     */   public static void setKeepAlive(HttpMessage message, boolean keepAlive)
/*     */   {
/* 101 */     setKeepAlive(message.headers(), message.protocolVersion(), keepAlive);
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
/*     */   public static void setKeepAlive(HttpHeaders h, HttpVersion httpVersion, boolean keepAlive)
/*     */   {
/* 124 */     if (httpVersion.isKeepAliveDefault()) {
/* 125 */       if (keepAlive) {
/* 126 */         h.remove(HttpHeaderNames.CONNECTION);
/*     */       } else {
/* 128 */         h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
/*     */       }
/*     */     }
/* 131 */     else if (keepAlive) {
/* 132 */       h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
/*     */     } else {
/* 134 */       h.remove(HttpHeaderNames.CONNECTION);
/*     */     }
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
/*     */   public static long getContentLength(HttpMessage message)
/*     */   {
/* 152 */     String value = message.headers().get(HttpHeaderNames.CONTENT_LENGTH);
/* 153 */     if (value != null) {
/* 154 */       return Long.parseLong(value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 159 */     long webSocketContentLength = getWebSocketContentLength(message);
/* 160 */     if (webSocketContentLength >= 0L) {
/* 161 */       return webSocketContentLength;
/*     */     }
/*     */     
/*     */ 
/* 165 */     throw new NumberFormatException("header not found: " + HttpHeaderNames.CONTENT_LENGTH);
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
/*     */   public static long getContentLength(HttpMessage message, long defaultValue)
/*     */   {
/* 179 */     String value = message.headers().get(HttpHeaderNames.CONTENT_LENGTH);
/* 180 */     if (value != null) {
/* 181 */       return Long.parseLong(value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 186 */     long webSocketContentLength = getWebSocketContentLength(message);
/* 187 */     if (webSocketContentLength >= 0L) {
/* 188 */       return webSocketContentLength;
/*     */     }
/*     */     
/*     */ 
/* 192 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getContentLength(HttpMessage message, int defaultValue)
/*     */   {
/* 203 */     return (int)Math.min(2147483647L, getContentLength(message, defaultValue));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getWebSocketContentLength(HttpMessage message)
/*     */   {
/* 212 */     HttpHeaders h = message.headers();
/* 213 */     if ((message instanceof HttpRequest)) {
/* 214 */       HttpRequest req = (HttpRequest)message;
/* 215 */       if ((HttpMethod.GET.equals(req.method())) && 
/* 216 */         (h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1)) && 
/* 217 */         (h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2))) {
/* 218 */         return 8;
/*     */       }
/* 220 */     } else if ((message instanceof HttpResponse)) {
/* 221 */       HttpResponse res = (HttpResponse)message;
/* 222 */       if ((res.status().code() == 101) && 
/* 223 */         (h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN)) && 
/* 224 */         (h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION))) {
/* 225 */         return 16;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 230 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setContentLength(HttpMessage message, long length)
/*     */   {
/* 237 */     message.headers().set(HttpHeaderNames.CONTENT_LENGTH, Long.valueOf(length));
/*     */   }
/*     */   
/*     */   public static boolean isContentLengthSet(HttpMessage m) {
/* 241 */     return m.headers().contains(HttpHeaderNames.CONTENT_LENGTH);
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
/*     */   public static boolean is100ContinueExpected(HttpMessage message)
/*     */   {
/* 254 */     if (!isExpectHeaderValid(message)) {
/* 255 */       return false;
/*     */     }
/*     */     
/* 258 */     String expectValue = message.headers().get(HttpHeaderNames.EXPECT);
/*     */     
/* 260 */     return HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean isUnsupportedExpectation(HttpMessage message)
/*     */   {
/* 272 */     if (!isExpectHeaderValid(message)) {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     String expectValue = message.headers().get(HttpHeaderNames.EXPECT);
/* 277 */     return (expectValue != null) && (!HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isExpectHeaderValid(HttpMessage message)
/*     */   {
/* 286 */     return ((message instanceof HttpRequest)) && 
/* 287 */       (message.protocolVersion().compareTo(HttpVersion.HTTP_1_1) >= 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void set100ContinueExpected(HttpMessage message, boolean expected)
/*     */   {
/* 298 */     if (expected) {
/* 299 */       message.headers().set(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
/*     */     } else {
/* 301 */       message.headers().remove(HttpHeaderNames.EXPECT);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isTransferEncodingChunked(HttpMessage message)
/*     */   {
/* 312 */     return message.headers().contains(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setTransferEncodingChunked(HttpMessage m, boolean chunked)
/*     */   {
/* 324 */     if (chunked) {
/* 325 */       m.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/* 326 */       m.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */     } else {
/* 328 */       List<String> encodings = m.headers().getAll(HttpHeaderNames.TRANSFER_ENCODING);
/* 329 */       if (encodings.isEmpty()) {
/* 330 */         return;
/*     */       }
/* 332 */       List<CharSequence> values = new ArrayList(encodings);
/* 333 */       Iterator<CharSequence> valuesIt = values.iterator();
/* 334 */       while (valuesIt.hasNext()) {
/* 335 */         CharSequence value = (CharSequence)valuesIt.next();
/* 336 */         if (HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(value)) {
/* 337 */           valuesIt.remove();
/*     */         }
/*     */       }
/* 340 */       if (values.isEmpty()) {
/* 341 */         m.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
/*     */       } else {
/* 343 */         m.headers().set(HttpHeaderNames.TRANSFER_ENCODING, values);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Charset getCharset(HttpMessage message)
/*     */   {
/* 356 */     return getCharset(message, CharsetUtil.ISO_8859_1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Charset getCharset(CharSequence contentTypeValue)
/*     */   {
/* 367 */     if (contentTypeValue != null) {
/* 368 */       return getCharset(contentTypeValue, CharsetUtil.ISO_8859_1);
/*     */     }
/* 370 */     return CharsetUtil.ISO_8859_1;
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
/*     */   public static Charset getCharset(HttpMessage message, Charset defaultCharset)
/*     */   {
/* 383 */     CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
/* 384 */     if (contentTypeValue != null) {
/* 385 */       return getCharset(contentTypeValue, defaultCharset);
/*     */     }
/* 387 */     return defaultCharset;
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
/*     */   public static Charset getCharset(CharSequence contentTypeValue, Charset defaultCharset)
/*     */   {
/* 400 */     if (contentTypeValue != null) {
/* 401 */       CharSequence charsetCharSequence = getCharsetAsSequence(contentTypeValue);
/* 402 */       if (charsetCharSequence != null) {
/*     */         try {
/* 404 */           return Charset.forName(charsetCharSequence.toString());
/*     */         } catch (UnsupportedCharsetException ignored) {
/* 406 */           return defaultCharset;
/*     */         }
/*     */       }
/* 409 */       return defaultCharset;
/*     */     }
/*     */     
/* 412 */     return defaultCharset;
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
/*     */   @Deprecated
/*     */   public static CharSequence getCharsetAsString(HttpMessage message)
/*     */   {
/* 429 */     return getCharsetAsSequence(message);
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
/*     */   public static CharSequence getCharsetAsSequence(HttpMessage message)
/*     */   {
/* 442 */     CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
/* 443 */     if (contentTypeValue != null) {
/* 444 */       return getCharsetAsSequence(contentTypeValue);
/*     */     }
/* 446 */     return null;
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
/*     */   public static CharSequence getCharsetAsSequence(CharSequence contentTypeValue)
/*     */   {
/* 462 */     if (contentTypeValue == null) {
/* 463 */       throw new NullPointerException("contentTypeValue");
/*     */     }
/*     */     
/* 466 */     int indexOfCharset = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, CHARSET_EQUALS, 0);
/* 467 */     if (indexOfCharset == -1) {
/* 468 */       return null;
/*     */     }
/*     */     
/* 471 */     int indexOfEncoding = indexOfCharset + CHARSET_EQUALS.length();
/* 472 */     if (indexOfEncoding < contentTypeValue.length()) {
/* 473 */       CharSequence charsetCandidate = contentTypeValue.subSequence(indexOfEncoding, contentTypeValue.length());
/* 474 */       int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(charsetCandidate, SEMICOLON, 0);
/* 475 */       if (indexOfSemicolon == -1) {
/* 476 */         return charsetCandidate;
/*     */       }
/*     */       
/* 479 */       return charsetCandidate.subSequence(0, indexOfSemicolon);
/*     */     }
/*     */     
/* 482 */     return null;
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
/*     */   public static CharSequence getMimeType(HttpMessage message)
/*     */   {
/* 497 */     CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
/* 498 */     if (contentTypeValue != null) {
/* 499 */       return getMimeType(contentTypeValue);
/*     */     }
/* 501 */     return null;
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
/*     */   public static CharSequence getMimeType(CharSequence contentTypeValue)
/*     */   {
/* 518 */     if (contentTypeValue == null) {
/* 519 */       throw new NullPointerException("contentTypeValue");
/*     */     }
/*     */     
/* 522 */     int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, SEMICOLON, 0);
/* 523 */     if (indexOfSemicolon != -1) {
/* 524 */       return contentTypeValue.subSequence(0, indexOfSemicolon);
/*     */     }
/* 526 */     return contentTypeValue.length() > 0 ? contentTypeValue : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatHostnameForHttp(InetSocketAddress addr)
/*     */   {
/* 538 */     String hostString = NetUtil.getHostname(addr);
/* 539 */     if (NetUtil.isValidIpV6Address(hostString)) {
/* 540 */       if (!addr.isUnresolved()) {
/* 541 */         hostString = NetUtil.toAddressString(addr.getAddress());
/*     */       }
/* 543 */       return '[' + hostString + ']';
/*     */     }
/* 545 */     return hostString;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */