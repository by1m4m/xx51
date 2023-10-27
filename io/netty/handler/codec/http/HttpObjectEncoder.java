/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public abstract class HttpObjectEncoder<H extends HttpMessage>
/*     */   extends MessageToMessageEncoder<Object>
/*     */ {
/*     */   static final int CRLF_SHORT = 3338;
/*     */   private static final int ZERO_CRLF_MEDIUM = 3149066;
/*  52 */   private static final byte[] ZERO_CRLF_CRLF = { 48, 13, 10, 13, 10 };
/*  53 */   private static final ByteBuf CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2).writeByte(13).writeByte(10));
/*  54 */   private static final ByteBuf ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length)
/*  55 */     .writeBytes(ZERO_CRLF_CRLF));
/*     */   
/*     */   private static final float HEADERS_WEIGHT_NEW = 0.2F;
/*     */   
/*     */   private static final float HEADERS_WEIGHT_HISTORICAL = 0.8F;
/*     */   private static final float TRAILERS_WEIGHT_NEW = 0.2F;
/*     */   private static final float TRAILERS_WEIGHT_HISTORICAL = 0.8F;
/*     */   private static final int ST_INIT = 0;
/*     */   private static final int ST_CONTENT_NON_CHUNK = 1;
/*     */   private static final int ST_CONTENT_CHUNK = 2;
/*     */   private static final int ST_CONTENT_ALWAYS_EMPTY = 3;
/*  66 */   private int state = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private float headersEncodedSizeAccumulator = 256.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private float trailersEncodedSizeAccumulator = 256.0F;
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception
/*     */   {
/*  83 */     ByteBuf buf = null;
/*  84 */     if ((msg instanceof HttpMessage)) {
/*  85 */       if (this.state != 0) {
/*  86 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg) + ", state: " + this.state);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  91 */       H m = (HttpMessage)msg;
/*     */       
/*  93 */       buf = ctx.alloc().buffer((int)this.headersEncodedSizeAccumulator);
/*     */       
/*  95 */       encodeInitialLine(buf, m);
/*     */       
/*  97 */       this.state = (HttpUtil.isTransferEncodingChunked(m) ? 2 : isContentAlwaysEmpty(m) ? 3 : 1);
/*     */       
/*  99 */       sanitizeHeadersBeforeEncode(m, this.state == 3);
/*     */       
/* 101 */       encodeHeaders(m.headers(), buf);
/* 102 */       ByteBufUtil.writeShortBE(buf, 3338);
/*     */       
/* 104 */       this.headersEncodedSizeAccumulator = (0.2F * padSizeForAccumulation(buf.readableBytes()) + 0.8F * this.headersEncodedSizeAccumulator);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */     if ((msg instanceof ByteBuf)) {
/* 114 */       ByteBuf potentialEmptyBuf = (ByteBuf)msg;
/* 115 */       if (!potentialEmptyBuf.isReadable()) {
/* 116 */         out.add(potentialEmptyBuf.retain());
/* 117 */         return;
/*     */       }
/*     */     }
/*     */     
/* 121 */     if (((msg instanceof HttpContent)) || ((msg instanceof ByteBuf)) || ((msg instanceof FileRegion))) {
/* 122 */       switch (this.state) {
/*     */       case 0: 
/* 124 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */       case 1: 
/* 126 */         long contentLength = contentLength(msg);
/* 127 */         if (contentLength > 0L) {
/* 128 */           if ((buf != null) && (buf.writableBytes() >= contentLength) && ((msg instanceof HttpContent)))
/*     */           {
/* 130 */             buf.writeBytes(((HttpContent)msg).content());
/* 131 */             out.add(buf);
/*     */           } else {
/* 133 */             if (buf != null) {
/* 134 */               out.add(buf);
/*     */             }
/* 136 */             out.add(encodeAndRetain(msg));
/*     */           }
/*     */           
/* 139 */           if (!(msg instanceof LastHttpContent)) break label476;
/* 140 */           this.state = 0;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         break;
/*     */       case 3: 
/* 149 */         if (buf != null)
/*     */         {
/* 151 */           out.add(buf);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 160 */           out.add(Unpooled.EMPTY_BUFFER);
/*     */         }
/*     */         
/* 163 */         break;
/*     */       case 2: 
/* 165 */         if (buf != null)
/*     */         {
/* 167 */           out.add(buf);
/*     */         }
/* 169 */         encodeChunkedContent(ctx, msg, contentLength(msg), out);
/*     */         
/* 171 */         break;
/*     */       }
/* 173 */       throw new Error();
/*     */       
/*     */       label476:
/* 176 */       if ((msg instanceof LastHttpContent)) {
/* 177 */         this.state = 0;
/*     */       }
/* 179 */     } else if (buf != null) {
/* 180 */       out.add(buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void encodeHeaders(HttpHeaders headers, ByteBuf buf)
/*     */   {
/* 188 */     Iterator<Map.Entry<CharSequence, CharSequence>> iter = headers.iteratorCharSequence();
/* 189 */     while (iter.hasNext()) {
/* 190 */       Map.Entry<CharSequence, CharSequence> header = (Map.Entry)iter.next();
/* 191 */       HttpHeadersEncoder.encoderHeader((CharSequence)header.getKey(), (CharSequence)header.getValue(), buf);
/*     */     }
/*     */   }
/*     */   
/*     */   private void encodeChunkedContent(ChannelHandlerContext ctx, Object msg, long contentLength, List<Object> out) {
/* 196 */     if (contentLength > 0L) {
/* 197 */       String lengthHex = Long.toHexString(contentLength);
/* 198 */       ByteBuf buf = ctx.alloc().buffer(lengthHex.length() + 2);
/* 199 */       buf.writeCharSequence(lengthHex, CharsetUtil.US_ASCII);
/* 200 */       ByteBufUtil.writeShortBE(buf, 3338);
/* 201 */       out.add(buf);
/* 202 */       out.add(encodeAndRetain(msg));
/* 203 */       out.add(CRLF_BUF.duplicate());
/*     */     }
/*     */     
/* 206 */     if ((msg instanceof LastHttpContent)) {
/* 207 */       HttpHeaders headers = ((LastHttpContent)msg).trailingHeaders();
/* 208 */       if (headers.isEmpty()) {
/* 209 */         out.add(ZERO_CRLF_CRLF_BUF.duplicate());
/*     */       } else {
/* 211 */         ByteBuf buf = ctx.alloc().buffer((int)this.trailersEncodedSizeAccumulator);
/* 212 */         ByteBufUtil.writeMediumBE(buf, 3149066);
/* 213 */         encodeHeaders(headers, buf);
/* 214 */         ByteBufUtil.writeShortBE(buf, 3338);
/* 215 */         this.trailersEncodedSizeAccumulator = (0.2F * padSizeForAccumulation(buf.readableBytes()) + 0.8F * this.trailersEncodedSizeAccumulator);
/*     */         
/* 217 */         out.add(buf);
/*     */       }
/* 219 */     } else if (contentLength == 0L)
/*     */     {
/*     */ 
/* 222 */       out.add(encodeAndRetain(msg));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sanitizeHeadersBeforeEncode(H msg, boolean isAlwaysEmpty) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isContentAlwaysEmpty(H msg)
/*     */   {
/* 241 */     return false;
/*     */   }
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception
/*     */   {
/* 246 */     return ((msg instanceof HttpObject)) || ((msg instanceof ByteBuf)) || ((msg instanceof FileRegion));
/*     */   }
/*     */   
/*     */   private static Object encodeAndRetain(Object msg) {
/* 250 */     if ((msg instanceof ByteBuf)) {
/* 251 */       return ((ByteBuf)msg).retain();
/*     */     }
/* 253 */     if ((msg instanceof HttpContent)) {
/* 254 */       return ((HttpContent)msg).content().retain();
/*     */     }
/* 256 */     if ((msg instanceof FileRegion)) {
/* 257 */       return ((FileRegion)msg).retain();
/*     */     }
/* 259 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */   
/*     */   private static long contentLength(Object msg) {
/* 263 */     if ((msg instanceof HttpContent)) {
/* 264 */       return ((HttpContent)msg).content().readableBytes();
/*     */     }
/* 266 */     if ((msg instanceof ByteBuf)) {
/* 267 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 269 */     if ((msg instanceof FileRegion)) {
/* 270 */       return ((FileRegion)msg).count();
/*     */     }
/* 272 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int padSizeForAccumulation(int readableBytes)
/*     */   {
/* 282 */     return (readableBytes << 2) / 3;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected static void encodeAscii(String s, ByteBuf buf) {
/* 287 */     buf.writeCharSequence(s, CharsetUtil.US_ASCII);
/*     */   }
/*     */   
/*     */   protected abstract void encodeInitialLine(ByteBuf paramByteBuf, H paramH)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */