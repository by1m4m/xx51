/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2HeadersDecoder
/*     */   implements Http2HeadersDecoder, Http2HeadersDecoder.Configuration
/*     */ {
/*     */   private static final float HEADERS_COUNT_WEIGHT_NEW = 0.2F;
/*     */   private static final float HEADERS_COUNT_WEIGHT_HISTORICAL = 0.8F;
/*     */   private final HpackDecoder hpackDecoder;
/*     */   private final boolean validateHeaders;
/*     */   private long maxHeaderListSizeGoAway;
/*  41 */   private float headerArraySizeAccumulator = 8.0F;
/*     */   
/*     */   public DefaultHttp2HeadersDecoder() {
/*  44 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultHttp2HeadersDecoder(boolean validateHeaders) {
/*  48 */     this(validateHeaders, 8192L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize)
/*     */   {
/*  60 */     this(validateHeaders, maxHeaderListSize, 32);
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
/*     */   public DefaultHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize, int initialHuffmanDecodeCapacity)
/*     */   {
/*  74 */     this(validateHeaders, new HpackDecoder(maxHeaderListSize, initialHuffmanDecodeCapacity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   DefaultHttp2HeadersDecoder(boolean validateHeaders, HpackDecoder hpackDecoder)
/*     */   {
/*  82 */     this.hpackDecoder = ((HpackDecoder)ObjectUtil.checkNotNull(hpackDecoder, "hpackDecoder"));
/*  83 */     this.validateHeaders = validateHeaders;
/*     */     
/*  85 */     this.maxHeaderListSizeGoAway = Http2CodecUtil.calculateMaxHeaderListSizeGoAway(hpackDecoder.getMaxHeaderListSize());
/*     */   }
/*     */   
/*     */   public void maxHeaderTableSize(long max) throws Http2Exception
/*     */   {
/*  90 */     this.hpackDecoder.setMaxHeaderTableSize(max);
/*     */   }
/*     */   
/*     */   public long maxHeaderTableSize()
/*     */   {
/*  95 */     return this.hpackDecoder.getMaxHeaderTableSize();
/*     */   }
/*     */   
/*     */   public void maxHeaderListSize(long max, long goAwayMax) throws Http2Exception
/*     */   {
/* 100 */     if ((goAwayMax < max) || (goAwayMax < 0L)) {
/* 101 */       throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Header List Size GO_AWAY %d must be non-negative and >= %d", new Object[] {
/* 102 */         Long.valueOf(goAwayMax), Long.valueOf(max) });
/*     */     }
/* 104 */     this.hpackDecoder.setMaxHeaderListSize(max);
/* 105 */     this.maxHeaderListSizeGoAway = goAwayMax;
/*     */   }
/*     */   
/*     */   public long maxHeaderListSize()
/*     */   {
/* 110 */     return this.hpackDecoder.getMaxHeaderListSize();
/*     */   }
/*     */   
/*     */   public long maxHeaderListSizeGoAway()
/*     */   {
/* 115 */     return this.maxHeaderListSizeGoAway;
/*     */   }
/*     */   
/*     */   public Http2HeadersDecoder.Configuration configuration()
/*     */   {
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers decodeHeaders(int streamId, ByteBuf headerBlock) throws Http2Exception
/*     */   {
/*     */     try {
/* 126 */       Http2Headers headers = newHeaders();
/* 127 */       this.hpackDecoder.decode(streamId, headerBlock, headers, this.validateHeaders);
/* 128 */       this.headerArraySizeAccumulator = (0.2F * headers.size() + 0.8F * this.headerArraySizeAccumulator);
/*     */       
/* 130 */       return headers;
/*     */     } catch (Http2Exception e) {
/* 132 */       throw e;
/*     */ 
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 137 */       throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, e, e.getMessage(), new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int numberOfHeadersGuess()
/*     */   {
/* 146 */     return (int)this.headerArraySizeAccumulator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean validateHeaders()
/*     */   {
/* 154 */     return this.validateHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Http2Headers newHeaders()
/*     */   {
/* 162 */     return new DefaultHttp2Headers(this.validateHeaders, (int)this.headerArraySizeAccumulator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\DefaultHttp2HeadersDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */