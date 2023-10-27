/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.handler.codec.UnsupportedValueConverter;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.DefaultHttpRequest;
/*     */ import io.netty.handler.codec.http.DefaultHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpScheme;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpConversionUtil
/*     */ {
/*  74 */   private static final CharSequenceMap<AsciiString> HTTP_TO_HTTP2_HEADER_BLACKLIST = new CharSequenceMap();
/*     */   
/*     */   static {
/*  77 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.CONNECTION, AsciiString.EMPTY_STRING);
/*     */     
/*  79 */     AsciiString keepAlive = HttpHeaderNames.KEEP_ALIVE;
/*  80 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(keepAlive, AsciiString.EMPTY_STRING);
/*     */     
/*  82 */     AsciiString proxyConnection = HttpHeaderNames.PROXY_CONNECTION;
/*  83 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(proxyConnection, AsciiString.EMPTY_STRING);
/*  84 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.TRANSFER_ENCODING, AsciiString.EMPTY_STRING);
/*  85 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.HOST, AsciiString.EMPTY_STRING);
/*  86 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.UPGRADE, AsciiString.EMPTY_STRING);
/*  87 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.STREAM_ID.text(), AsciiString.EMPTY_STRING);
/*  88 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.SCHEME.text(), AsciiString.EMPTY_STRING);
/*  89 */     HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.PATH.text(), AsciiString.EMPTY_STRING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   public static final HttpMethod OUT_OF_MESSAGE_SEQUENCE_METHOD = HttpMethod.OPTIONS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String OUT_OF_MESSAGE_SEQUENCE_PATH = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   public static final HttpResponseStatus OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE = HttpResponseStatus.OK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private static final AsciiString EMPTY_REQUEST_PATH = AsciiString.cached("/");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ExtensionHeaderNames
/*     */   {
/* 129 */     STREAM_ID("x-http2-stream-id"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */     SCHEME("x-http2-scheme"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */     PATH("x-http2-path"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */     STREAM_PROMISE_ID("x-http2-stream-promise-id"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */     STREAM_DEPENDENCY_ID("x-http2-stream-dependency-id"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */     STREAM_WEIGHT("x-http2-stream-weight");
/*     */     
/*     */     private final AsciiString text;
/*     */     
/*     */     private ExtensionHeaderNames(String text) {
/* 169 */       this.text = AsciiString.cached(text);
/*     */     }
/*     */     
/*     */     public AsciiString text() {
/* 173 */       return this.text;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpResponseStatus parseStatus(CharSequence status)
/*     */     throws Http2Exception
/*     */   {
/*     */     try
/*     */     {
/* 187 */       HttpResponseStatus result = HttpResponseStatus.parseLine(status);
/* 188 */       if (result == HttpResponseStatus.SWITCHING_PROTOCOLS) {
/* 189 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 status code '%d'", new Object[] { Integer.valueOf(result.code()) });
/*     */       }
/*     */     } catch (Http2Exception e) {
/* 192 */       throw e;
/*     */     } catch (Throwable t) {
/* 194 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "Unrecognized HTTP status code '%s' encountered in translation to HTTP/1.x", new Object[] { status });
/*     */     }
/*     */     HttpResponseStatus result;
/* 197 */     return result;
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
/*     */   public static FullHttpResponse toFullHttpResponse(int streamId, Http2Headers http2Headers, ByteBufAllocator alloc, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 216 */     HttpResponseStatus status = parseStatus(http2Headers.status());
/*     */     
/*     */ 
/* 219 */     FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, alloc.buffer(), validateHttpHeaders);
/*     */     try
/*     */     {
/* 222 */       addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
/*     */     } catch (Http2Exception e) {
/* 224 */       msg.release();
/* 225 */       throw e;
/*     */     } catch (Throwable t) {
/* 227 */       msg.release();
/* 228 */       throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
/*     */     }
/* 230 */     return msg;
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
/*     */   public static FullHttpRequest toFullHttpRequest(int streamId, Http2Headers http2Headers, ByteBufAllocator alloc, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 250 */     CharSequence method = (CharSequence)ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
/*     */     
/* 252 */     CharSequence path = (CharSequence)ObjectUtil.checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
/*     */     
/*     */ 
/* 255 */     FullHttpRequest msg = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), alloc.buffer(), validateHttpHeaders);
/*     */     try {
/* 257 */       addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
/*     */     } catch (Http2Exception e) {
/* 259 */       msg.release();
/* 260 */       throw e;
/*     */     } catch (Throwable t) {
/* 262 */       msg.release();
/* 263 */       throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
/*     */     }
/* 265 */     return msg;
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
/*     */   public static HttpRequest toHttpRequest(int streamId, Http2Headers http2Headers, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 283 */     CharSequence method = (CharSequence)ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
/*     */     
/* 285 */     CharSequence path = (CharSequence)ObjectUtil.checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
/*     */     
/*     */ 
/* 288 */     HttpRequest msg = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), validateHttpHeaders);
/*     */     try {
/* 290 */       addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, true);
/*     */     } catch (Http2Exception e) {
/* 292 */       throw e;
/*     */     } catch (Throwable t) {
/* 294 */       throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
/*     */     }
/* 296 */     return msg;
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
/*     */   public static HttpResponse toHttpResponse(int streamId, Http2Headers http2Headers, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 315 */     HttpResponseStatus status = parseStatus(http2Headers.status());
/*     */     
/*     */ 
/* 318 */     HttpResponse msg = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status, validateHttpHeaders);
/*     */     try {
/* 320 */       addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, true);
/*     */     } catch (Http2Exception e) {
/* 322 */       throw e;
/*     */     } catch (Throwable t) {
/* 324 */       throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
/*     */     }
/* 326 */     return msg;
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
/*     */   public static void addHttp2ToHttpHeaders(int streamId, Http2Headers sourceHeaders, FullHttpMessage destinationMessage, boolean addToTrailer)
/*     */     throws Http2Exception
/*     */   {
/* 341 */     addHttp2ToHttpHeaders(streamId, sourceHeaders, addToTrailer ? destinationMessage
/* 342 */       .trailingHeaders() : destinationMessage.headers(), destinationMessage
/* 343 */       .protocolVersion(), addToTrailer, destinationMessage instanceof HttpRequest);
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
/*     */   public static void addHttp2ToHttpHeaders(int streamId, Http2Headers inputHeaders, HttpHeaders outputHeaders, HttpVersion httpVersion, boolean isTrailer, boolean isRequest)
/*     */     throws Http2Exception
/*     */   {
/* 361 */     Http2ToHttpHeaderTranslator translator = new Http2ToHttpHeaderTranslator(streamId, outputHeaders, isRequest);
/*     */     try {
/* 363 */       for (Map.Entry<CharSequence, CharSequence> entry : inputHeaders) {
/* 364 */         translator.translate(entry);
/*     */       }
/*     */     } catch (Http2Exception ex) {
/* 367 */       throw ex;
/*     */     } catch (Throwable t) {
/* 369 */       throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
/*     */     }
/*     */     
/* 372 */     outputHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
/* 373 */     outputHeaders.remove(HttpHeaderNames.TRAILER);
/* 374 */     if (!isTrailer) {
/* 375 */       outputHeaders.setInt(ExtensionHeaderNames.STREAM_ID.text(), streamId);
/* 376 */       HttpUtil.setKeepAlive(outputHeaders, httpVersion, true);
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
/*     */   public static Http2Headers toHttp2Headers(HttpMessage in, boolean validateHeaders)
/*     */   {
/* 390 */     HttpHeaders inHeaders = in.headers();
/* 391 */     Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
/* 392 */     if ((in instanceof HttpRequest)) {
/* 393 */       HttpRequest request = (HttpRequest)in;
/* 394 */       URI requestTargetUri = URI.create(request.uri());
/* 395 */       out.path(toHttp2Path(requestTargetUri));
/* 396 */       out.method(request.method().asciiName());
/* 397 */       setHttp2Scheme(inHeaders, requestTargetUri, out);
/*     */       
/* 399 */       if ((!HttpUtil.isOriginForm(requestTargetUri)) && (!HttpUtil.isAsteriskForm(requestTargetUri)))
/*     */       {
/* 401 */         String host = inHeaders.getAsString(HttpHeaderNames.HOST);
/* 402 */         setHttp2Authority((host == null) || (host.isEmpty()) ? requestTargetUri.getAuthority() : host, out);
/*     */       }
/* 404 */     } else if ((in instanceof HttpResponse)) {
/* 405 */       HttpResponse response = (HttpResponse)in;
/* 406 */       out.status(response.status().codeAsText());
/*     */     }
/*     */     
/*     */ 
/* 410 */     toHttp2Headers(inHeaders, out);
/* 411 */     return out;
/*     */   }
/*     */   
/*     */   public static Http2Headers toHttp2Headers(HttpHeaders inHeaders, boolean validateHeaders) {
/* 415 */     if (inHeaders.isEmpty()) {
/* 416 */       return EmptyHttp2Headers.INSTANCE;
/*     */     }
/*     */     
/* 419 */     Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
/* 420 */     toHttp2Headers(inHeaders, out);
/* 421 */     return out;
/*     */   }
/*     */   
/*     */   private static CharSequenceMap<AsciiString> toLowercaseMap(Iterator<? extends CharSequence> valuesIter, int arraySizeHint)
/*     */   {
/* 426 */     UnsupportedValueConverter<AsciiString> valueConverter = UnsupportedValueConverter.instance();
/* 427 */     CharSequenceMap<AsciiString> result = new CharSequenceMap(true, valueConverter, arraySizeHint);
/*     */     
/* 429 */     while (valuesIter.hasNext()) {
/* 430 */       AsciiString lowerCased = AsciiString.of((CharSequence)valuesIter.next()).toLowerCase();
/*     */       try {
/* 432 */         int index = lowerCased.forEachByte(ByteProcessor.FIND_COMMA);
/* 433 */         if (index != -1) {
/* 434 */           int start = 0;
/*     */           do {
/* 436 */             result.add(lowerCased.subSequence(start, index, false).trim(), AsciiString.EMPTY_STRING);
/* 437 */             start = index + 1;
/* 438 */           } while ((start < lowerCased.length()) && 
/* 439 */             ((index = lowerCased.forEachByte(start, lowerCased.length() - start, ByteProcessor.FIND_COMMA)) != -1));
/* 440 */           result.add(lowerCased.subSequence(start, lowerCased.length(), false).trim(), AsciiString.EMPTY_STRING);
/*     */         } else {
/* 442 */           result.add(lowerCased.trim(), AsciiString.EMPTY_STRING);
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 447 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/* 450 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void toHttp2HeadersFilterTE(Map.Entry<CharSequence, CharSequence> entry, Http2Headers out)
/*     */   {
/* 461 */     if (AsciiString.indexOf((CharSequence)entry.getValue(), ',', 0) == -1) {
/* 462 */       if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim((CharSequence)entry.getValue()), HttpHeaderValues.TRAILERS)) {
/* 463 */         out.add(HttpHeaderNames.TE, HttpHeaderValues.TRAILERS);
/*     */       }
/*     */     } else {
/* 466 */       List<CharSequence> teValues = StringUtil.unescapeCsvFields((CharSequence)entry.getValue());
/* 467 */       for (CharSequence teValue : teValues) {
/* 468 */         if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(teValue), HttpHeaderValues.TRAILERS)) {
/* 469 */           out.add(HttpHeaderNames.TE, HttpHeaderValues.TRAILERS);
/* 470 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void toHttp2Headers(HttpHeaders inHeaders, Http2Headers out) {
/* 477 */     Iterator<Map.Entry<CharSequence, CharSequence>> iter = inHeaders.iteratorCharSequence();
/*     */     
/*     */ 
/*     */ 
/* 481 */     CharSequenceMap<AsciiString> connectionBlacklist = toLowercaseMap(inHeaders.valueCharSequenceIterator(HttpHeaderNames.CONNECTION), 8);
/* 482 */     while (iter.hasNext()) {
/* 483 */       Map.Entry<CharSequence, CharSequence> entry = (Map.Entry)iter.next();
/* 484 */       AsciiString aName = AsciiString.of((CharSequence)entry.getKey()).toLowerCase();
/* 485 */       if ((!HTTP_TO_HTTP2_HEADER_BLACKLIST.contains(aName)) && (!connectionBlacklist.contains(aName)))
/*     */       {
/* 487 */         if (aName.contentEqualsIgnoreCase(HttpHeaderNames.TE)) {
/* 488 */           toHttp2HeadersFilterTE(entry, out);
/* 489 */         } else if (aName.contentEqualsIgnoreCase(HttpHeaderNames.COOKIE)) {
/* 490 */           AsciiString value = AsciiString.of((CharSequence)entry.getValue());
/*     */           
/*     */           try
/*     */           {
/* 494 */             int index = value.forEachByte(ByteProcessor.FIND_SEMI_COLON);
/* 495 */             if (index != -1) {
/* 496 */               int start = 0;
/*     */               do {
/* 498 */                 out.add(HttpHeaderNames.COOKIE, value.subSequence(start, index, false));
/*     */                 
/* 500 */                 start = index + 2;
/* 501 */               } while ((start < value.length()) && 
/* 502 */                 ((index = value.forEachByte(start, value.length() - start, ByteProcessor.FIND_SEMI_COLON)) != -1));
/* 503 */               if (start >= value.length()) {
/* 504 */                 throw new IllegalArgumentException("cookie value is of unexpected format: " + value);
/*     */               }
/* 506 */               out.add(HttpHeaderNames.COOKIE, value.subSequence(start, value.length(), false));
/*     */             } else {
/* 508 */               out.add(HttpHeaderNames.COOKIE, value);
/*     */             }
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 513 */             throw new IllegalStateException(e);
/*     */           }
/*     */         } else {
/* 516 */           out.add(aName, entry.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static AsciiString toHttp2Path(URI uri)
/*     */   {
/* 528 */     StringBuilder pathBuilder = new StringBuilder(StringUtil.length(uri.getRawPath()) + StringUtil.length(uri.getRawQuery()) + StringUtil.length(uri.getRawFragment()) + 2);
/* 529 */     if (!StringUtil.isNullOrEmpty(uri.getRawPath())) {
/* 530 */       pathBuilder.append(uri.getRawPath());
/*     */     }
/* 532 */     if (!StringUtil.isNullOrEmpty(uri.getRawQuery())) {
/* 533 */       pathBuilder.append('?');
/* 534 */       pathBuilder.append(uri.getRawQuery());
/*     */     }
/* 536 */     if (!StringUtil.isNullOrEmpty(uri.getRawFragment())) {
/* 537 */       pathBuilder.append('#');
/* 538 */       pathBuilder.append(uri.getRawFragment());
/*     */     }
/* 540 */     String path = pathBuilder.toString();
/* 541 */     return path.isEmpty() ? EMPTY_REQUEST_PATH : new AsciiString(path);
/*     */   }
/*     */   
/*     */ 
/*     */   static void setHttp2Authority(String authority, Http2Headers out)
/*     */   {
/* 547 */     if (authority != null) {
/* 548 */       if (authority.isEmpty()) {
/* 549 */         out.authority(AsciiString.EMPTY_STRING);
/*     */       } else {
/* 551 */         int start = authority.indexOf('@') + 1;
/* 552 */         int length = authority.length() - start;
/* 553 */         if (length == 0) {
/* 554 */           throw new IllegalArgumentException("authority: " + authority);
/*     */         }
/* 556 */         out.authority(new AsciiString(authority, start, length));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setHttp2Scheme(HttpHeaders in, URI uri, Http2Headers out) {
/* 562 */     String value = uri.getScheme();
/* 563 */     if (value != null) {
/* 564 */       out.scheme(new AsciiString(value));
/* 565 */       return;
/*     */     }
/*     */     
/*     */ 
/* 569 */     CharSequence cValue = in.get(ExtensionHeaderNames.SCHEME.text());
/* 570 */     if (cValue != null) {
/* 571 */       out.scheme(AsciiString.of(cValue));
/* 572 */       return;
/*     */     }
/*     */     
/* 575 */     if (uri.getPort() == HttpScheme.HTTPS.port()) {
/* 576 */       out.scheme(HttpScheme.HTTPS.name());
/* 577 */     } else if (uri.getPort() == HttpScheme.HTTP.port()) {
/* 578 */       out.scheme(HttpScheme.HTTP.name());
/*     */     } else {
/* 580 */       throw new IllegalArgumentException(":scheme must be specified. see https://tools.ietf.org/html/rfc7540#section-8.1.2.3");
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
/*     */   private static final class Http2ToHttpHeaderTranslator
/*     */   {
/* 593 */     private static final CharSequenceMap<AsciiString> REQUEST_HEADER_TRANSLATIONS = new CharSequenceMap();
/*     */     
/* 595 */     private static final CharSequenceMap<AsciiString> RESPONSE_HEADER_TRANSLATIONS = new CharSequenceMap();
/*     */     
/* 597 */     static { RESPONSE_HEADER_TRANSLATIONS.add(Http2Headers.PseudoHeaderName.AUTHORITY.value(), HttpHeaderNames.HOST);
/*     */       
/* 599 */       RESPONSE_HEADER_TRANSLATIONS.add(Http2Headers.PseudoHeaderName.SCHEME.value(), HttpConversionUtil.ExtensionHeaderNames.SCHEME
/* 600 */         .text());
/* 601 */       REQUEST_HEADER_TRANSLATIONS.add(RESPONSE_HEADER_TRANSLATIONS);
/* 602 */       RESPONSE_HEADER_TRANSLATIONS.add(Http2Headers.PseudoHeaderName.PATH.value(), HttpConversionUtil.ExtensionHeaderNames.PATH
/* 603 */         .text());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private final int streamId;
/*     */     
/*     */ 
/*     */     private final HttpHeaders output;
/*     */     
/*     */ 
/*     */     private final CharSequenceMap<AsciiString> translations;
/*     */     
/*     */     Http2ToHttpHeaderTranslator(int streamId, HttpHeaders output, boolean request)
/*     */     {
/* 618 */       this.streamId = streamId;
/* 619 */       this.output = output;
/* 620 */       this.translations = (request ? REQUEST_HEADER_TRANSLATIONS : RESPONSE_HEADER_TRANSLATIONS);
/*     */     }
/*     */     
/*     */     public void translate(Map.Entry<CharSequence, CharSequence> entry) throws Http2Exception {
/* 624 */       CharSequence name = (CharSequence)entry.getKey();
/* 625 */       CharSequence value = (CharSequence)entry.getValue();
/* 626 */       AsciiString translatedName = (AsciiString)this.translations.get(name);
/* 627 */       if (translatedName != null) {
/* 628 */         this.output.add(translatedName, AsciiString.of(value));
/* 629 */       } else if (!Http2Headers.PseudoHeaderName.isPseudoHeader(name))
/*     */       {
/*     */ 
/* 632 */         if ((name.length() == 0) || (name.charAt(0) == ':')) {
/* 633 */           throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 header '%s' encountered in translation to HTTP/1.x", new Object[] { name });
/*     */         }
/*     */         
/* 636 */         if (HttpHeaderNames.COOKIE.equals(name))
/*     */         {
/*     */ 
/* 639 */           String existingCookie = this.output.get(HttpHeaderNames.COOKIE);
/* 640 */           this.output.set(HttpHeaderNames.COOKIE, existingCookie != null ? existingCookie + "; " + value : value);
/*     */         }
/*     */         else {
/* 643 */           this.output.add(name, value);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\HttpConversionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */