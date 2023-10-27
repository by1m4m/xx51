/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.ByteProcessor;
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
/*     */ public class LineBasedFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final int maxLength;
/*     */   private final boolean failFast;
/*     */   private final boolean stripDelimiter;
/*     */   private boolean discarding;
/*     */   private int discardedBytes;
/*     */   private int offset;
/*     */   
/*     */   public LineBasedFrameDecoder(int maxLength)
/*     */   {
/*  52 */     this(maxLength, true, false);
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
/*     */   public LineBasedFrameDecoder(int maxLength, boolean stripDelimiter, boolean failFast)
/*     */   {
/*  71 */     this.maxLength = maxLength;
/*  72 */     this.failFast = failFast;
/*  73 */     this.stripDelimiter = stripDelimiter;
/*     */   }
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  78 */     Object decoded = decode(ctx, in);
/*  79 */     if (decoded != null) {
/*  80 */       out.add(decoded);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer)
/*     */     throws Exception
/*     */   {
/*  93 */     int eol = findEndOfLine(buffer);
/*  94 */     if (!this.discarding) {
/*  95 */       if (eol >= 0)
/*     */       {
/*  97 */         int length = eol - buffer.readerIndex();
/*  98 */         int delimLength = buffer.getByte(eol) == 13 ? 2 : 1;
/*     */         
/* 100 */         if (length > this.maxLength) {
/* 101 */           buffer.readerIndex(eol + delimLength);
/* 102 */           fail(ctx, length);
/* 103 */           return null;
/*     */         }
/*     */         ByteBuf frame;
/* 106 */         if (this.stripDelimiter) {
/* 107 */           ByteBuf frame = buffer.readRetainedSlice(length);
/* 108 */           buffer.skipBytes(delimLength);
/*     */         } else {
/* 110 */           frame = buffer.readRetainedSlice(length + delimLength);
/*     */         }
/*     */         
/* 113 */         return frame;
/*     */       }
/* 115 */       int length = buffer.readableBytes();
/* 116 */       if (length > this.maxLength) {
/* 117 */         this.discardedBytes = length;
/* 118 */         buffer.readerIndex(buffer.writerIndex());
/* 119 */         this.discarding = true;
/* 120 */         this.offset = 0;
/* 121 */         if (this.failFast) {
/* 122 */           fail(ctx, "over " + this.discardedBytes);
/*     */         }
/*     */       }
/* 125 */       return null;
/*     */     }
/*     */     
/* 128 */     if (eol >= 0) {
/* 129 */       int length = this.discardedBytes + eol - buffer.readerIndex();
/* 130 */       int delimLength = buffer.getByte(eol) == 13 ? 2 : 1;
/* 131 */       buffer.readerIndex(eol + delimLength);
/* 132 */       this.discardedBytes = 0;
/* 133 */       this.discarding = false;
/* 134 */       if (!this.failFast) {
/* 135 */         fail(ctx, length);
/*     */       }
/*     */     } else {
/* 138 */       this.discardedBytes += buffer.readableBytes();
/* 139 */       buffer.readerIndex(buffer.writerIndex());
/*     */       
/* 141 */       this.offset = 0;
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, int length)
/*     */   {
/* 148 */     fail(ctx, String.valueOf(length));
/*     */   }
/*     */   
/*     */   private void fail(ChannelHandlerContext ctx, String length) {
/* 152 */     ctx.fireExceptionCaught(new TooLongFrameException("frame length (" + length + ") exceeds the allowed maximum (" + this.maxLength + ')'));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int findEndOfLine(ByteBuf buffer)
/*     */   {
/* 162 */     int totalLength = buffer.readableBytes();
/* 163 */     int i = buffer.forEachByte(buffer.readerIndex() + this.offset, totalLength - this.offset, ByteProcessor.FIND_LF);
/* 164 */     if (i >= 0) {
/* 165 */       this.offset = 0;
/* 166 */       if ((i > 0) && (buffer.getByte(i - 1) == 13)) {
/* 167 */         i--;
/*     */       }
/*     */     } else {
/* 170 */       this.offset = totalLength;
/*     */     }
/* 172 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\LineBasedFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */