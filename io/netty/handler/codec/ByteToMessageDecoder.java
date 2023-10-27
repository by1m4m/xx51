/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ByteToMessageDecoder
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*  75 */   public static final Cumulator MERGE_CUMULATOR = new Cumulator() {
/*     */     public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
/*     */       try {
/*     */         ByteBuf buffer;
/*     */         ByteBuf buffer;
/*  80 */         if ((cumulation.writerIndex() > cumulation.maxCapacity() - in.readableBytes()) || 
/*  81 */           (cumulation.refCnt() > 1) || (cumulation.isReadOnly()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */           buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
/*     */         } else {
/*  91 */           buffer = cumulation;
/*     */         }
/*  93 */         buffer.writeBytes(in);
/*  94 */         return buffer;
/*     */       }
/*     */       finally
/*     */       {
/*  98 */         in.release();
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   public static final Cumulator COMPOSITE_CUMULATOR = new Cumulator() {
/*     */     public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
/*     */       try {
/*     */         CompositeByteBuf composite;
/*     */         ByteBuf buffer;
/* 113 */         if (cumulation.refCnt() > 1)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */           ByteBuf buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
/* 121 */           buffer.writeBytes(in);
/*     */         } else {
/*     */           CompositeByteBuf composite;
/* 124 */           if ((cumulation instanceof CompositeByteBuf)) {
/* 125 */             composite = (CompositeByteBuf)cumulation;
/*     */           } else {
/* 127 */             composite = alloc.compositeBuffer(Integer.MAX_VALUE);
/* 128 */             composite.addComponent(true, cumulation);
/*     */           }
/* 130 */           composite.addComponent(true, in);
/* 131 */           in = null;
/* 132 */           buffer = composite;
/*     */         }
/* 134 */         return buffer;
/*     */       } finally {
/* 136 */         if (in != null)
/*     */         {
/*     */ 
/* 139 */           in.release();
/*     */         }
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   private static final byte STATE_INIT = 0;
/*     */   
/*     */   private static final byte STATE_CALLING_CHILD_DECODE = 1;
/*     */   private static final byte STATE_HANDLER_REMOVED_PENDING = 2;
/*     */   ByteBuf cumulation;
/* 150 */   private Cumulator cumulator = MERGE_CUMULATOR;
/*     */   
/*     */ 
/*     */   private boolean singleDecode;
/*     */   
/*     */ 
/*     */   private boolean decodeWasNull;
/*     */   
/*     */ 
/*     */   private boolean first;
/*     */   
/*     */ 
/* 162 */   private byte decodeState = 0;
/* 163 */   private int discardAfterReads = 16;
/*     */   private int numReads;
/*     */   
/*     */   protected ByteToMessageDecoder() {
/* 167 */     ensureNotSharable();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSingleDecode(boolean singleDecode)
/*     */   {
/* 177 */     this.singleDecode = singleDecode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSingleDecode()
/*     */   {
/* 187 */     return this.singleDecode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCumulator(Cumulator cumulator)
/*     */   {
/* 194 */     if (cumulator == null) {
/* 195 */       throw new NullPointerException("cumulator");
/*     */     }
/* 197 */     this.cumulator = cumulator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDiscardAfterReads(int discardAfterReads)
/*     */   {
/* 205 */     if (discardAfterReads <= 0) {
/* 206 */       throw new IllegalArgumentException("discardAfterReads must be > 0");
/*     */     }
/* 208 */     this.discardAfterReads = discardAfterReads;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int actualReadableBytes()
/*     */   {
/* 218 */     return internalBuffer().readableBytes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuf internalBuffer()
/*     */   {
/* 227 */     if (this.cumulation != null) {
/* 228 */       return this.cumulation;
/*     */     }
/* 230 */     return Unpooled.EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */   public final void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 236 */     if (this.decodeState == 1) {
/* 237 */       this.decodeState = 2;
/* 238 */       return;
/*     */     }
/* 240 */     ByteBuf buf = this.cumulation;
/* 241 */     if (buf != null)
/*     */     {
/* 243 */       this.cumulation = null;
/*     */       
/* 245 */       int readable = buf.readableBytes();
/* 246 */       if (readable > 0) {
/* 247 */         ByteBuf bytes = buf.readBytes(readable);
/* 248 */         buf.release();
/* 249 */         ctx.fireChannelRead(bytes);
/*     */       } else {
/* 251 */         buf.release();
/*     */       }
/*     */       
/* 254 */       this.numReads = 0;
/* 255 */       ctx.fireChannelReadComplete();
/*     */     }
/* 257 */     handlerRemoved0(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*     */     throws Exception
/*     */   {
/* 268 */     if ((msg instanceof ByteBuf)) {
/* 269 */       CodecOutputList out = CodecOutputList.newInstance();
/*     */       try {
/* 271 */         ByteBuf data = (ByteBuf)msg;
/* 272 */         this.first = (this.cumulation == null);
/* 273 */         if (this.first) {
/* 274 */           this.cumulation = data;
/*     */         } else {
/* 276 */           this.cumulation = this.cumulator.cumulate(ctx.alloc(), this.cumulation, data);
/*     */         }
/* 278 */         callDecode(ctx, this.cumulation, out);
/*     */       } catch (DecoderException e) { int size;
/* 280 */         throw e;
/*     */       } catch (Exception e) {
/* 282 */         throw new DecoderException(e);
/*     */       } finally {
/* 284 */         if ((this.cumulation != null) && (!this.cumulation.isReadable())) {
/* 285 */           this.numReads = 0;
/* 286 */           this.cumulation.release();
/* 287 */           this.cumulation = null;
/* 288 */         } else if (++this.numReads >= this.discardAfterReads)
/*     */         {
/*     */ 
/* 291 */           this.numReads = 0;
/* 292 */           discardSomeReadBytes();
/*     */         }
/*     */         
/* 295 */         int size = out.size();
/* 296 */         this.decodeWasNull = (!out.insertSinceRecycled());
/* 297 */         fireChannelRead(ctx, out, size);
/* 298 */         out.recycle();
/*     */       }
/*     */     } else {
/* 301 */       ctx.fireChannelRead(msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void fireChannelRead(ChannelHandlerContext ctx, List<Object> msgs, int numElements)
/*     */   {
/* 309 */     if ((msgs instanceof CodecOutputList)) {
/* 310 */       fireChannelRead(ctx, (CodecOutputList)msgs, numElements);
/*     */     } else {
/* 312 */       for (int i = 0; i < numElements; i++) {
/* 313 */         ctx.fireChannelRead(msgs.get(i));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void fireChannelRead(ChannelHandlerContext ctx, CodecOutputList msgs, int numElements)
/*     */   {
/* 322 */     for (int i = 0; i < numElements; i++) {
/* 323 */       ctx.fireChannelRead(msgs.getUnsafe(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 329 */     this.numReads = 0;
/* 330 */     discardSomeReadBytes();
/* 331 */     if (this.decodeWasNull) {
/* 332 */       this.decodeWasNull = false;
/* 333 */       if (!ctx.channel().config().isAutoRead()) {
/* 334 */         ctx.read();
/*     */       }
/*     */     }
/* 337 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */   
/*     */   protected final void discardSomeReadBytes() {
/* 341 */     if ((this.cumulation != null) && (!this.first) && (this.cumulation.refCnt() == 1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 349 */       this.cumulation.discardSomeReadBytes();
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 355 */     channelInputClosed(ctx, true);
/*     */   }
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
/*     */   {
/* 360 */     if ((evt instanceof ChannelInputShutdownEvent))
/*     */     {
/*     */ 
/*     */ 
/* 364 */       channelInputClosed(ctx, false);
/*     */     }
/* 366 */     super.userEventTriggered(ctx, evt);
/*     */   }
/*     */   
/*     */   private void channelInputClosed(ChannelHandlerContext ctx, boolean callChannelInactive) throws Exception {
/* 370 */     CodecOutputList out = CodecOutputList.newInstance();
/*     */     try {
/* 372 */       channelInputClosed(ctx, out);
/*     */     } catch (DecoderException e) { int size;
/* 374 */       throw e;
/*     */     } catch (Exception e) {
/* 376 */       throw new DecoderException(e);
/*     */     } finally {
/*     */       try {
/* 379 */         if (this.cumulation != null) {
/* 380 */           this.cumulation.release();
/* 381 */           this.cumulation = null;
/*     */         }
/* 383 */         int size = out.size();
/* 384 */         fireChannelRead(ctx, out, size);
/* 385 */         if (size > 0)
/*     */         {
/* 387 */           ctx.fireChannelReadComplete();
/*     */         }
/* 389 */         if (callChannelInactive) {
/* 390 */           ctx.fireChannelInactive();
/*     */         }
/*     */       }
/*     */       finally {
/* 394 */         out.recycle();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void channelInputClosed(ChannelHandlerContext ctx, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 404 */     if (this.cumulation != null) {
/* 405 */       callDecode(ctx, this.cumulation, out);
/* 406 */       decodeLast(ctx, this.cumulation, out);
/*     */     } else {
/* 408 */       decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */   {
/*     */     try
/*     */     {
/* 422 */       while (in.isReadable()) {
/* 423 */         int outSize = out.size();
/*     */         
/* 425 */         if (outSize > 0) {
/* 426 */           fireChannelRead(ctx, out, outSize);
/* 427 */           out.clear();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 434 */           if (!ctx.isRemoved())
/*     */           {
/*     */ 
/* 437 */             outSize = 0;
/*     */           }
/*     */         } else {
/* 440 */           int oldInputLength = in.readableBytes();
/* 441 */           decodeRemovalReentryProtection(ctx, in, out);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 447 */           if (!ctx.isRemoved())
/*     */           {
/*     */ 
/*     */ 
/* 451 */             if (outSize == out.size()) {
/* 452 */               if (oldInputLength != in.readableBytes()) {
/*     */                 continue;
/*     */               }
/*     */               
/*     */             }
/*     */             else
/*     */             {
/* 459 */               if (oldInputLength == in.readableBytes())
/*     */               {
/* 461 */                 throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() did not read anything but decoded a message.");
/*     */               }
/*     */               
/*     */ 
/* 465 */               if (isSingleDecode())
/*     */                 break;
/*     */             } }
/*     */         }
/*     */       }
/* 470 */     } catch (DecoderException e) { throw e;
/*     */     } catch (Exception cause) {
/* 472 */       throw new DecoderException(cause);
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
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void decodeRemovalReentryProtection(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 500 */     this.decodeState = 1;
/*     */     try {
/* 502 */       decode(ctx, in, out);
/*     */     } finally { boolean removePending;
/* 504 */       boolean removePending = this.decodeState == 2;
/* 505 */       this.decodeState = 0;
/* 506 */       if (removePending) {
/* 507 */         handlerRemoved(ctx);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 520 */     if (in.isReadable())
/*     */     {
/*     */ 
/* 523 */       decodeRemovalReentryProtection(ctx, in, out);
/*     */     }
/*     */   }
/*     */   
/*     */   static ByteBuf expandCumulation(ByteBufAllocator alloc, ByteBuf cumulation, int readable) {
/* 528 */     ByteBuf oldCumulation = cumulation;
/* 529 */     cumulation = alloc.buffer(oldCumulation.readableBytes() + readable);
/* 530 */     cumulation.writeBytes(oldCumulation);
/* 531 */     oldCumulation.release();
/* 532 */     return cumulation;
/*     */   }
/*     */   
/*     */   public static abstract interface Cumulator
/*     */   {
/*     */     public abstract ByteBuf cumulate(ByteBufAllocator paramByteBufAllocator, ByteBuf paramByteBuf1, ByteBuf paramByteBuf2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\ByteToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */