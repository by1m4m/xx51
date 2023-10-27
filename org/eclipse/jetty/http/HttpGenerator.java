/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.Supplier;
/*     */ import org.eclipse.jetty.util.ArrayTrie;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.Trie;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpGenerator
/*     */ {
/*  47 */   private static final Logger LOG = Log.getLogger(HttpGenerator.class);
/*     */   
/*  49 */   public static final boolean __STRICT = Boolean.getBoolean("org.eclipse.jetty.http.HttpGenerator.STRICT");
/*     */   
/*  51 */   private static final byte[] __colon_space = { 58, 32 };
/*  52 */   public static final MetaData.Response CONTINUE_100_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 100, null, null, -1L);
/*  53 */   public static final MetaData.Response PROGRESS_102_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 102, null, null, -1L);
/*  54 */   public static final MetaData.Response RESPONSE_500_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 500, null, new HttpFields() {}, 0L);
/*     */   
/*     */   public static final int CHUNK_SIZE = 12;
/*     */   
/*     */   public static enum State
/*     */   {
/*  60 */     START, 
/*  61 */     COMMITTED, 
/*  62 */     COMPLETING, 
/*  63 */     COMPLETING_1XX, 
/*  64 */     END;
/*     */     
/*     */     private State() {} }
/*     */   
/*  68 */   public static enum Result { NEED_CHUNK, 
/*  69 */     NEED_INFO, 
/*  70 */     NEED_HEADER, 
/*  71 */     NEED_CHUNK_TRAILER, 
/*  72 */     FLUSH, 
/*  73 */     CONTINUE, 
/*  74 */     SHUTDOWN_OUT, 
/*  75 */     DONE;
/*     */     
/*     */ 
/*     */     private Result() {}
/*     */   }
/*     */   
/*  81 */   private State _state = State.START;
/*  82 */   private HttpTokens.EndOfContent _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
/*     */   
/*  84 */   private long _contentPrepared = 0L;
/*  85 */   private boolean _noContentResponse = false;
/*  86 */   private Boolean _persistent = null;
/*  87 */   private Supplier<HttpFields> _trailers = null;
/*     */   
/*     */   private final int _send;
/*     */   private static final int SEND_SERVER = 1;
/*     */   private static final int SEND_XPOWEREDBY = 2;
/*  92 */   private static final Trie<Boolean> __assumedContentMethods = new ArrayTrie(8);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setJettyVersion(String serverVersion)
/*     */   {
/* 102 */     SEND[1] = StringUtil.getBytes("Server: " + serverVersion + "\r\n");
/* 103 */     SEND[2] = StringUtil.getBytes("X-Powered-By: " + serverVersion + "\r\n");
/* 104 */     SEND[3] = StringUtil.getBytes("Server: " + serverVersion + "\r\nX-Powered-By: " + serverVersion + "\r\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 110 */   private boolean _needCRLF = false;
/*     */   private static final byte[] ZERO_CHUNK;
/*     */   private static final byte[] LAST_CHUNK;
/*     */   
/*     */   public HttpGenerator() {
/* 115 */     this(false, false);
/*     */   }
/*     */   
/*     */   private static final byte[] CONTENT_LENGTH_0;
/*     */   private static final byte[] CONNECTION_CLOSE;
/*     */   public HttpGenerator(boolean sendServerVersion, boolean sendXPoweredBy) {
/* 121 */     this._send = ((sendServerVersion ? 1 : 0) | (sendXPoweredBy ? 2 : 0));
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */   {
/* 127 */     this._state = State.START;
/* 128 */     this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
/* 129 */     this._noContentResponse = false;
/* 130 */     this._persistent = null;
/* 131 */     this._contentPrepared = 0L;
/* 132 */     this._needCRLF = false;
/* 133 */     this._trailers = null;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public boolean getSendServerVersion()
/*     */   {
/* 140 */     return (this._send & 0x1) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void setSendServerVersion(boolean sendServerVersion)
/*     */   {
/* 147 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   public State getState()
/*     */   {
/* 153 */     return this._state;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isState(State state)
/*     */   {
/* 159 */     return this._state == state;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isIdle()
/*     */   {
/* 165 */     return this._state == State.START;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEnd()
/*     */   {
/* 171 */     return this._state == State.END;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isCommitted()
/*     */   {
/* 177 */     return this._state.ordinal() >= State.COMMITTED.ordinal();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isChunking()
/*     */   {
/* 183 */     return this._endOfContent == HttpTokens.EndOfContent.CHUNKED_CONTENT;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isNoContent()
/*     */   {
/* 189 */     return this._noContentResponse;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPersistent(boolean persistent)
/*     */   {
/* 195 */     this._persistent = Boolean.valueOf(persistent);
/*     */   }
/*     */   
/*     */   private static final byte[] HTTP_1_1_SPACE;
/*     */   private static final byte[] TRANSFER_ENCODING_CHUNKED;
/*     */   private static final byte[][] SEND;
/*     */   private static final PreparedResponse[] __preprepared;
/*     */   public boolean isPersistent()
/*     */   {
/* 204 */     return Boolean.TRUE.equals(this._persistent);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isWritten()
/*     */   {
/* 210 */     return this._contentPrepared > 0L;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getContentPrepared()
/*     */   {
/* 216 */     return this._contentPrepared;
/*     */   }
/*     */   
/*     */ 
/*     */   public void abort()
/*     */   {
/* 222 */     this._persistent = Boolean.valueOf(false);
/* 223 */     this._state = State.END;
/* 224 */     this._endOfContent = null;
/*     */   }
/*     */   
/*     */   public Result generateRequest(MetaData.Request info, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last)
/*     */     throws IOException
/*     */   {
/* 230 */     switch (this._state)
/*     */     {
/*     */ 
/*     */     case START: 
/* 234 */       if (info == null) {
/* 235 */         return Result.NEED_INFO;
/*     */       }
/* 237 */       if (header == null) {
/* 238 */         return Result.NEED_HEADER;
/*     */       }
/*     */       
/* 241 */       if (this._persistent == null)
/*     */       {
/* 243 */         this._persistent = Boolean.valueOf(info.getHttpVersion().ordinal() > HttpVersion.HTTP_1_0.ordinal());
/* 244 */         if ((!this._persistent.booleanValue()) && (HttpMethod.CONNECT.is(info.getMethod()))) {
/* 245 */           this._persistent = Boolean.valueOf(true);
/*     */         }
/*     */       }
/*     */       
/* 249 */       int pos = BufferUtil.flipToFill(header);
/*     */       
/*     */       try
/*     */       {
/* 253 */         generateRequestLine(info, header);
/*     */         
/* 255 */         if (info.getHttpVersion() == HttpVersion.HTTP_0_9) {
/* 256 */           throw new BadMessageException(500, "HTTP/0.9 not supported");
/*     */         }
/* 258 */         generateHeaders(info, header, content, last);
/*     */         
/* 260 */         boolean expect100 = info.getFields().contains(HttpHeader.EXPECT, HttpHeaderValue.CONTINUE.asString());
/*     */         int len;
/* 262 */         if (expect100)
/*     */         {
/* 264 */           this._state = State.COMMITTED;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 269 */           len = BufferUtil.length(content);
/* 270 */           if (len > 0)
/*     */           {
/* 272 */             this._contentPrepared += len;
/* 273 */             if (isChunking())
/* 274 */               prepareChunk(header, len);
/*     */           }
/* 276 */           this._state = (last ? State.COMPLETING : State.COMMITTED);
/*     */         }
/*     */         
/* 279 */         return Result.FLUSH;
/*     */       }
/*     */       catch (BadMessageException e)
/*     */       {
/* 283 */         throw e;
/*     */       }
/*     */       catch (BufferOverflowException e)
/*     */       {
/* 287 */         throw new BadMessageException(500, "Request header too large", e);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 291 */         throw new BadMessageException(500, e.getMessage(), e);
/*     */       }
/*     */       finally
/*     */       {
/* 295 */         BufferUtil.flipToFlush(header, pos);
/*     */       }
/*     */     
/*     */ 
/*     */ 
/*     */     case COMMITTED: 
/* 301 */       return committed(chunk, content, last);
/*     */     
/*     */ 
/*     */ 
/*     */     case COMPLETING: 
/* 306 */       return completing(chunk, content);
/*     */     
/*     */ 
/*     */     case END: 
/* 310 */       if (BufferUtil.hasContent(content))
/*     */       {
/* 312 */         if (LOG.isDebugEnabled())
/* 313 */           LOG.debug("discarding content in COMPLETING", new Object[0]);
/* 314 */         BufferUtil.clear(content);
/*     */       }
/* 316 */       return Result.DONE;
/*     */     }
/*     */     
/* 319 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */   private Result committed(ByteBuffer chunk, ByteBuffer content, boolean last)
/*     */   {
/* 325 */     int len = BufferUtil.length(content);
/*     */     
/*     */ 
/* 328 */     if (len > 0)
/*     */     {
/* 330 */       if (isChunking())
/*     */       {
/* 332 */         if (chunk == null)
/* 333 */           return Result.NEED_CHUNK;
/* 334 */         BufferUtil.clearToFill(chunk);
/* 335 */         prepareChunk(chunk, len);
/* 336 */         BufferUtil.flipToFlush(chunk, 0);
/*     */       }
/* 338 */       this._contentPrepared += len;
/*     */     }
/*     */     
/* 341 */     if (last)
/*     */     {
/* 343 */       this._state = State.COMPLETING;
/* 344 */       return len > 0 ? Result.FLUSH : Result.CONTINUE;
/*     */     }
/* 346 */     return len > 0 ? Result.FLUSH : Result.DONE;
/*     */   }
/*     */   
/*     */   private Result completing(ByteBuffer chunk, ByteBuffer content)
/*     */   {
/* 351 */     if (BufferUtil.hasContent(content))
/*     */     {
/* 353 */       if (LOG.isDebugEnabled())
/* 354 */         LOG.debug("discarding content in COMPLETING", new Object[0]);
/* 355 */       BufferUtil.clear(content);
/*     */     }
/*     */     
/* 358 */     if (isChunking())
/*     */     {
/* 360 */       if (this._trailers != null)
/*     */       {
/*     */ 
/* 363 */         if ((chunk == null) || (chunk.capacity() <= 12)) {
/* 364 */           return Result.NEED_CHUNK_TRAILER;
/*     */         }
/* 366 */         HttpFields trailers = (HttpFields)this._trailers.get();
/*     */         
/* 368 */         if (trailers != null)
/*     */         {
/*     */ 
/* 371 */           BufferUtil.clearToFill(chunk);
/* 372 */           generateTrailers(chunk, trailers);
/* 373 */           BufferUtil.flipToFlush(chunk, 0);
/* 374 */           this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
/* 375 */           return Result.FLUSH;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 380 */       if (chunk == null) {
/* 381 */         return Result.NEED_CHUNK;
/*     */       }
/*     */       
/* 384 */       BufferUtil.clearToFill(chunk);
/* 385 */       prepareChunk(chunk, 0);
/* 386 */       BufferUtil.flipToFlush(chunk, 0);
/* 387 */       this._endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
/* 388 */       return Result.FLUSH;
/*     */     }
/*     */     
/* 391 */     this._state = State.END;
/* 392 */     return Boolean.TRUE.equals(this._persistent) ? Result.DONE : Result.SHUTDOWN_OUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Result generateResponse(MetaData.Response info, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last)
/*     */     throws IOException
/*     */   {
/* 401 */     return generateResponse(info, false, header, chunk, content, last);
/*     */   }
/*     */   
/*     */   public Result generateResponse(MetaData.Response info, boolean head, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last)
/*     */     throws IOException
/*     */   {
/* 407 */     switch (this._state)
/*     */     {
/*     */ 
/*     */     case START: 
/* 411 */       if (info == null)
/* 412 */         return Result.NEED_INFO;
/* 413 */       HttpVersion version = info.getHttpVersion();
/* 414 */       if (version == null)
/* 415 */         throw new BadMessageException(500, "No version");
/* 416 */       switch (version)
/*     */       {
/*     */       case HTTP_1_0: 
/* 419 */         if (this._persistent == null) {
/* 420 */           this._persistent = Boolean.FALSE;
/*     */         }
/*     */         break;
/*     */       case HTTP_1_1: 
/* 424 */         if (this._persistent == null) {
/* 425 */           this._persistent = Boolean.TRUE;
/*     */         }
/*     */         break;
/*     */       default: 
/* 429 */         this._persistent = Boolean.valueOf(false);
/* 430 */         this._endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
/* 431 */         if (BufferUtil.hasContent(content))
/* 432 */           this._contentPrepared += content.remaining();
/* 433 */         this._state = (last ? State.COMPLETING : State.COMMITTED);
/* 434 */         return Result.FLUSH;
/*     */       }
/*     */       
/*     */       
/* 438 */       if (header == null) {
/* 439 */         return Result.NEED_HEADER;
/*     */       }
/*     */       
/* 442 */       int pos = BufferUtil.flipToFill(header);
/*     */       
/*     */       try
/*     */       {
/* 446 */         generateResponseLine(info, header);
/*     */         
/*     */ 
/* 449 */         int status = info.getStatus();
/* 450 */         if ((status >= 100) && (status < 200))
/*     */         {
/* 452 */           this._noContentResponse = true;
/*     */           
/* 454 */           if (status != 101)
/*     */           {
/* 456 */             header.put(HttpTokens.CRLF);
/* 457 */             this._state = State.COMPLETING_1XX;
/* 458 */             return Result.FLUSH;
/*     */           }
/*     */         }
/* 461 */         else if ((status == 204) || (status == 304))
/*     */         {
/* 463 */           this._noContentResponse = true;
/*     */         }
/*     */         
/* 466 */         generateHeaders(info, header, content, last);
/*     */         
/*     */ 
/* 469 */         int len = BufferUtil.length(content);
/* 470 */         if (len > 0)
/*     */         {
/* 472 */           this._contentPrepared += len;
/* 473 */           if ((isChunking()) && (!head))
/* 474 */             prepareChunk(header, len);
/*     */         }
/* 476 */         this._state = (last ? State.COMPLETING : State.COMMITTED);
/*     */       }
/*     */       catch (BadMessageException e)
/*     */       {
/* 480 */         throw e;
/*     */       }
/*     */       catch (BufferOverflowException e)
/*     */       {
/* 484 */         throw new BadMessageException(500, "Response header too large", e);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 488 */         throw new BadMessageException(500, e.getMessage(), e);
/*     */       }
/*     */       finally
/*     */       {
/* 492 */         BufferUtil.flipToFlush(header, pos);
/*     */       }
/*     */       
/* 495 */       return Result.FLUSH;
/*     */     
/*     */ 
/*     */ 
/*     */     case COMMITTED: 
/* 500 */       return committed(chunk, content, last);
/*     */     
/*     */ 
/*     */ 
/*     */     case COMPLETING_1XX: 
/* 505 */       reset();
/* 506 */       return Result.DONE;
/*     */     
/*     */ 
/*     */ 
/*     */     case COMPLETING: 
/* 511 */       return completing(chunk, content);
/*     */     
/*     */ 
/*     */     case END: 
/* 515 */       if (BufferUtil.hasContent(content))
/*     */       {
/* 517 */         if (LOG.isDebugEnabled())
/* 518 */           LOG.debug("discarding content in COMPLETING", new Object[0]);
/* 519 */         BufferUtil.clear(content);
/*     */       }
/* 521 */       return Result.DONE;
/*     */     }
/*     */     
/* 524 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void prepareChunk(ByteBuffer chunk, int remaining)
/*     */   {
/* 532 */     if (this._needCRLF) {
/* 533 */       BufferUtil.putCRLF(chunk);
/*     */     }
/*     */     
/* 536 */     if (remaining > 0)
/*     */     {
/* 538 */       BufferUtil.putHexInt(chunk, remaining);
/* 539 */       BufferUtil.putCRLF(chunk);
/* 540 */       this._needCRLF = true;
/*     */     }
/*     */     else
/*     */     {
/* 544 */       chunk.put(LAST_CHUNK);
/* 545 */       this._needCRLF = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void generateTrailers(ByteBuffer buffer, HttpFields trailer)
/*     */   {
/* 553 */     if (this._needCRLF) {
/* 554 */       BufferUtil.putCRLF(buffer);
/*     */     }
/*     */     
/* 557 */     buffer.put(ZERO_CHUNK);
/*     */     
/* 559 */     int n = trailer.size();
/* 560 */     for (int f = 0; f < n; f++)
/*     */     {
/* 562 */       HttpField field = trailer.getField(f);
/* 563 */       putTo(field, buffer);
/*     */     }
/*     */     
/* 566 */     BufferUtil.putCRLF(buffer);
/*     */   }
/*     */   
/*     */ 
/*     */   private void generateRequestLine(MetaData.Request request, ByteBuffer header)
/*     */   {
/* 572 */     header.put(StringUtil.getBytes(request.getMethod()));
/* 573 */     header.put((byte)32);
/* 574 */     header.put(StringUtil.getBytes(request.getURIString()));
/* 575 */     header.put((byte)32);
/* 576 */     header.put(request.getHttpVersion().toBytes());
/* 577 */     header.put(HttpTokens.CRLF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void generateResponseLine(MetaData.Response response, ByteBuffer header)
/*     */   {
/* 584 */     int status = response.getStatus();
/* 585 */     PreparedResponse preprepared = status < __preprepared.length ? __preprepared[status] : null;
/* 586 */     String reason = response.getReason();
/* 587 */     if (preprepared != null)
/*     */     {
/* 589 */       if (reason == null) {
/* 590 */         header.put(preprepared._responseLine);
/*     */       }
/*     */       else {
/* 593 */         header.put(preprepared._schemeCode);
/* 594 */         header.put(getReasonBytes(reason));
/* 595 */         header.put(HttpTokens.CRLF);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 600 */       header.put(HTTP_1_1_SPACE);
/* 601 */       header.put((byte)(48 + status / 100));
/* 602 */       header.put((byte)(48 + status % 100 / 10));
/* 603 */       header.put((byte)(48 + status % 10));
/* 604 */       header.put((byte)32);
/* 605 */       if (reason == null)
/*     */       {
/* 607 */         header.put((byte)(48 + status / 100));
/* 608 */         header.put((byte)(48 + status % 100 / 10));
/* 609 */         header.put((byte)(48 + status % 10));
/*     */       }
/*     */       else {
/* 612 */         header.put(getReasonBytes(reason)); }
/* 613 */       header.put(HttpTokens.CRLF);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private byte[] getReasonBytes(String reason)
/*     */   {
/* 620 */     if (reason.length() > 1024)
/* 621 */       reason = reason.substring(0, 1024);
/* 622 */     byte[] _bytes = StringUtil.getBytes(reason);
/*     */     
/* 624 */     for (int i = _bytes.length; i-- > 0;)
/* 625 */       if ((_bytes[i] == 13) || (_bytes[i] == 10))
/* 626 */         _bytes[i] = 63;
/* 627 */     return _bytes;
/*     */   }
/*     */   
/*     */ 
/*     */   private void generateHeaders(MetaData info, ByteBuffer header, ByteBuffer content, boolean last)
/*     */   {
/* 633 */     MetaData.Request request = (info instanceof MetaData.Request) ? (MetaData.Request)info : null;
/* 634 */     MetaData.Response response = (info instanceof MetaData.Response) ? (MetaData.Response)info : null;
/*     */     
/* 636 */     if (LOG.isDebugEnabled())
/*     */     {
/* 638 */       LOG.debug("generateHeaders {} last={} content={}", new Object[] { info, Boolean.valueOf(last), BufferUtil.toDetailString(content) });
/* 639 */       LOG.debug(info.getFields().toString(), new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 643 */     int send = this._send;
/* 644 */     HttpField transfer_encoding = null;
/* 645 */     boolean http11 = info.getHttpVersion() == HttpVersion.HTTP_1_1;
/* 646 */     boolean close = false;
/* 647 */     this._trailers = (http11 ? info.getTrailerSupplier() : null);
/* 648 */     boolean chunked_hint = this._trailers != null;
/* 649 */     boolean content_type = false;
/* 650 */     long content_length = info.getContentLength();
/* 651 */     boolean content_length_field = false;
/*     */     
/*     */ 
/* 654 */     HttpFields fields = info.getFields();
/* 655 */     if (fields != null)
/*     */     {
/* 657 */       int n = fields.size();
/* 658 */       for (int f = 0; f < n; f++)
/*     */       {
/* 660 */         HttpField field = fields.getField(f);
/* 661 */         HttpHeader h = field.getHeader();
/* 662 */         if (h == null) {
/* 663 */           putTo(field, header);
/*     */         }
/*     */         else {
/* 666 */           switch (h)
/*     */           {
/*     */           case CONTENT_LENGTH: 
/* 669 */             if (content_length < 0L) {
/* 670 */               content_length = field.getLongValue();
/* 671 */             } else if (content_length != field.getLongValue())
/* 672 */               throw new BadMessageException(500, String.format("Incorrect Content-Length %d!=%d", new Object[] { Long.valueOf(content_length), Long.valueOf(field.getLongValue()) }));
/* 673 */             content_length_field = true;
/* 674 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case CONTENT_TYPE: 
/* 679 */             content_type = true;
/* 680 */             putTo(field, header);
/* 681 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case TRANSFER_ENCODING: 
/* 686 */             if (http11)
/*     */             {
/*     */ 
/*     */ 
/* 690 */               transfer_encoding = field;
/* 691 */               chunked_hint = field.contains(HttpHeaderValue.CHUNKED.asString());
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case CONNECTION: 
/* 698 */             putTo(field, header);
/* 699 */             if (field.contains(HttpHeaderValue.CLOSE.asString()))
/*     */             {
/* 701 */               close = true;
/* 702 */               this._persistent = Boolean.valueOf(false);
/*     */             }
/*     */             
/* 705 */             if ((!http11) && (field.contains(HttpHeaderValue.KEEP_ALIVE.asString())))
/*     */             {
/* 707 */               this._persistent = Boolean.valueOf(true);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case SERVER: 
/* 714 */             send &= 0xFFFFFFFE;
/* 715 */             putTo(field, header);
/* 716 */             break;
/*     */           
/*     */ 
/*     */           default: 
/* 720 */             putTo(field, header);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 727 */     if ((last) && (content_length < 0L) && (this._trailers == null)) {
/* 728 */       content_length = this._contentPrepared + BufferUtil.length(content);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 733 */     boolean assumed_content_request = (request != null) && (Boolean.TRUE.equals(__assumedContentMethods.get(request.getMethod())));
/* 734 */     boolean assumed_content = (assumed_content_request) || (content_type) || (chunked_hint);
/* 735 */     boolean nocontent_request = (request != null) && (content_length <= 0L) && (!assumed_content);
/*     */     
/*     */ 
/* 738 */     if ((this._noContentResponse) || (nocontent_request))
/*     */     {
/*     */ 
/* 741 */       this._endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
/*     */       
/*     */ 
/* 744 */       if ((this._contentPrepared > 0L) || (content_length > 0L))
/*     */       {
/* 746 */         if ((this._contentPrepared == 0L) && (last))
/*     */         {
/*     */ 
/*     */ 
/* 750 */           content.clear();
/* 751 */           content_length = 0L;
/*     */         }
/*     */         else {
/* 754 */           throw new BadMessageException(500, "Content for no content response");
/*     */         }
/*     */         
/*     */       }
/*     */     }
/* 759 */     else if ((http11) && ((chunked_hint) || ((content_length < 0L) && ((this._persistent.booleanValue()) || (assumed_content_request)))))
/*     */     {
/*     */ 
/* 762 */       this._endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
/*     */       
/*     */ 
/* 765 */       if (transfer_encoding == null) {
/* 766 */         header.put(TRANSFER_ENCODING_CHUNKED);
/* 767 */       } else if (transfer_encoding.toString().endsWith(HttpHeaderValue.CHUNKED.toString()))
/*     */       {
/* 769 */         putTo(transfer_encoding, header);
/* 770 */         transfer_encoding = null;
/*     */       }
/* 772 */       else if (!chunked_hint)
/*     */       {
/* 774 */         putTo(new HttpField(HttpHeader.TRANSFER_ENCODING, transfer_encoding.getValue() + ",chunked"), header);
/* 775 */         transfer_encoding = null;
/*     */       }
/*     */       else {
/* 778 */         throw new BadMessageException(500, "Bad Transfer-Encoding");
/*     */       }
/*     */     }
/* 781 */     else if ((content_length >= 0L) && ((request != null) || (this._persistent.booleanValue())))
/*     */     {
/*     */ 
/* 784 */       this._endOfContent = HttpTokens.EndOfContent.CONTENT_LENGTH;
/* 785 */       putContentLength(header, content_length);
/*     */ 
/*     */     }
/* 788 */     else if (response != null)
/*     */     {
/*     */ 
/* 791 */       this._endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
/* 792 */       this._persistent = Boolean.valueOf(false);
/* 793 */       if ((content_length >= 0L) && ((content_length > 0L) || (assumed_content) || (content_length_field))) {
/* 794 */         putContentLength(header, content_length);
/*     */       }
/* 796 */       if ((http11) && (!close)) {
/* 797 */         header.put(CONNECTION_CLOSE);
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 803 */       throw new BadMessageException(500, "Unknown content length for request");
/*     */     }
/*     */     
/* 806 */     if (LOG.isDebugEnabled()) {
/* 807 */       LOG.debug(this._endOfContent.toString(), new Object[0]);
/*     */     }
/*     */     
/* 810 */     if (transfer_encoding != null)
/*     */     {
/* 812 */       if (chunked_hint)
/*     */       {
/* 814 */         String v = transfer_encoding.getValue();
/* 815 */         int c = v.lastIndexOf(',');
/* 816 */         if ((c > 0) && (v.lastIndexOf(HttpHeaderValue.CHUNKED.toString(), c) > c)) {
/* 817 */           putTo(new HttpField(HttpHeader.TRANSFER_ENCODING, v.substring(0, c).trim()), header);
/*     */         }
/*     */       }
/*     */       else {
/* 821 */         putTo(transfer_encoding, header);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 826 */     int status = response != null ? response.getStatus() : -1;
/* 827 */     if (status > 199) {
/* 828 */       header.put(SEND[send]);
/*     */     }
/*     */     
/* 831 */     header.put(HttpTokens.CRLF);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void putContentLength(ByteBuffer header, long contentLength)
/*     */   {
/* 837 */     if (contentLength == 0L) {
/* 838 */       header.put(CONTENT_LENGTH_0);
/*     */     }
/*     */     else {
/* 841 */       header.put(HttpHeader.CONTENT_LENGTH.getBytesColonSpace());
/* 842 */       BufferUtil.putDecLong(header, contentLength);
/* 843 */       header.put(HttpTokens.CRLF);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte[] getReasonBuffer(int code)
/*     */   {
/* 850 */     PreparedResponse status = code < __preprepared.length ? __preprepared[code] : null;
/* 851 */     if (status != null)
/* 852 */       return status._reason;
/* 853 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 860 */     return String.format("%s@%x{s=%s}", new Object[] {
/* 861 */       getClass().getSimpleName(), 
/* 862 */       Integer.valueOf(hashCode()), this._state });
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  95 */     __assumedContentMethods.put(HttpMethod.POST.asString(), Boolean.TRUE);
/*  96 */     __assumedContentMethods.put(HttpMethod.PUT.asString(), Boolean.TRUE);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 870 */     ZERO_CHUNK = new byte[] { 48, 13, 10 };
/* 871 */     LAST_CHUNK = new byte[] { 48, 13, 10, 13, 10 };
/* 872 */     CONTENT_LENGTH_0 = StringUtil.getBytes("Content-Length: 0\r\n");
/* 873 */     CONNECTION_CLOSE = StringUtil.getBytes("Connection: close\r\n");
/* 874 */     HTTP_1_1_SPACE = StringUtil.getBytes(HttpVersion.HTTP_1_1 + " ");
/* 875 */     TRANSFER_ENCODING_CHUNKED = StringUtil.getBytes("Transfer-Encoding: chunked\r\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 880 */     SEND = new byte[][] { new byte[0], StringUtil.getBytes("Server: Jetty(9.x.x)\r\n"), StringUtil.getBytes("X-Powered-By: Jetty(9.x.x)\r\n"), StringUtil.getBytes("Server: Jetty(9.x.x)\r\nX-Powered-By: Jetty(9.x.x)\r\n") };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 893 */     __preprepared = new PreparedResponse['Ȁ'];
/*     */     
/*     */ 
/* 896 */     int versionLength = HttpVersion.HTTP_1_1.toString().length();
/*     */     
/* 898 */     for (int i = 0; i < __preprepared.length; i++)
/*     */     {
/* 900 */       HttpStatus.Code code = HttpStatus.getCode(i);
/* 901 */       if (code != null)
/*     */       {
/* 903 */         String reason = code.getMessage();
/* 904 */         byte[] line = new byte[versionLength + 5 + reason.length() + 2];
/* 905 */         HttpVersion.HTTP_1_1.toBuffer().get(line, 0, versionLength);
/* 906 */         line[(versionLength + 0)] = 32;
/* 907 */         line[(versionLength + 1)] = ((byte)(48 + i / 100));
/* 908 */         line[(versionLength + 2)] = ((byte)(48 + i % 100 / 10));
/* 909 */         line[(versionLength + 3)] = ((byte)(48 + i % 10));
/* 910 */         line[(versionLength + 4)] = 32;
/* 911 */         for (int j = 0; j < reason.length(); j++)
/* 912 */           line[(versionLength + 5 + j)] = ((byte)reason.charAt(j));
/* 913 */         line[(versionLength + 5 + reason.length())] = 13;
/* 914 */         line[(versionLength + 6 + reason.length())] = 10;
/*     */         
/* 916 */         __preprepared[i] = new PreparedResponse(null);
/* 917 */         __preprepared[i]._schemeCode = Arrays.copyOfRange(line, 0, versionLength + 5);
/* 918 */         __preprepared[i]._reason = Arrays.copyOfRange(line, versionLength + 5, line.length - 2);
/* 919 */         __preprepared[i]._responseLine = line;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void putSanitisedName(String s, ByteBuffer buffer) {
/* 925 */     int l = s.length();
/* 926 */     for (int i = 0; i < l; i++)
/*     */     {
/* 928 */       char c = s.charAt(i);
/*     */       
/* 930 */       if ((c < 0) || (c > 'ÿ') || (c == '\r') || (c == '\n') || (c == ':')) {
/* 931 */         buffer.put((byte)63);
/*     */       } else {
/* 933 */         buffer.put((byte)(0xFF & c));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void putSanitisedValue(String s, ByteBuffer buffer) {
/* 939 */     int l = s.length();
/* 940 */     for (int i = 0; i < l; i++)
/*     */     {
/* 942 */       char c = s.charAt(i);
/*     */       
/* 944 */       if ((c < 0) || (c > 'ÿ') || (c == '\r') || (c == '\n')) {
/* 945 */         buffer.put((byte)32);
/*     */       } else {
/* 947 */         buffer.put((byte)(0xFF & c));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void putTo(HttpField field, ByteBuffer bufferInFillMode) {
/* 953 */     if ((field instanceof PreEncodedHttpField))
/*     */     {
/* 955 */       ((PreEncodedHttpField)field).putTo(bufferInFillMode, HttpVersion.HTTP_1_0);
/*     */     }
/*     */     else
/*     */     {
/* 959 */       HttpHeader header = field.getHeader();
/* 960 */       if (header != null)
/*     */       {
/* 962 */         bufferInFillMode.put(header.getBytesColonSpace());
/* 963 */         putSanitisedValue(field.getValue(), bufferInFillMode);
/*     */       }
/*     */       else
/*     */       {
/* 967 */         putSanitisedName(field.getName(), bufferInFillMode);
/* 968 */         bufferInFillMode.put(__colon_space);
/* 969 */         putSanitisedValue(field.getValue(), bufferInFillMode);
/*     */       }
/*     */       
/* 972 */       BufferUtil.putCRLF(bufferInFillMode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void putTo(HttpFields fields, ByteBuffer bufferInFillMode)
/*     */   {
/* 978 */     for (HttpField field : fields)
/*     */     {
/* 980 */       if (field != null)
/* 981 */         putTo(field, bufferInFillMode);
/*     */     }
/* 983 */     BufferUtil.putCRLF(bufferInFillMode);
/*     */   }
/*     */   
/*     */   private static class PreparedResponse
/*     */   {
/*     */     byte[] _reason;
/*     */     byte[] _schemeCode;
/*     */     byte[] _responseLine;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */