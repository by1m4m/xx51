/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
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
/*     */ public abstract class MessageAggregator<I, S, C extends ByteBufHolder, O extends ByteBufHolder>
/*     */   extends MessageToMessageDecoder<I>
/*     */ {
/*     */   private static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
/*     */   private final int maxContentLength;
/*     */   private O currentMessage;
/*     */   private boolean handlingOversizedMessage;
/*  60 */   private int maxCumulationBufferComponents = 1024;
/*     */   
/*     */ 
/*     */ 
/*     */   private ChannelHandlerContext ctx;
/*     */   
/*     */ 
/*     */   private ChannelFutureListener continueResponseWriteListener;
/*     */   
/*     */ 
/*     */ 
/*     */   protected MessageAggregator(int maxContentLength)
/*     */   {
/*  73 */     validateMaxContentLength(maxContentLength);
/*  74 */     this.maxContentLength = maxContentLength;
/*     */   }
/*     */   
/*     */   protected MessageAggregator(int maxContentLength, Class<? extends I> inboundMessageType) {
/*  78 */     super(inboundMessageType);
/*  79 */     validateMaxContentLength(maxContentLength);
/*  80 */     this.maxContentLength = maxContentLength;
/*     */   }
/*     */   
/*     */   private static void validateMaxContentLength(int maxContentLength) {
/*  84 */     if (maxContentLength < 0) {
/*  85 */       throw new IllegalArgumentException("maxContentLength: " + maxContentLength + " (expected: >= 0)");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean acceptInboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/*  92 */     if (!super.acceptInboundMessage(msg)) {
/*  93 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  97 */     I in = (I)msg;
/*     */     
/*  99 */     return ((isContentMessage(in)) || (isStartMessage(in))) && (!isAggregated(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isStartMessage(I paramI)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isContentMessage(I paramI)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isLastContentMessage(C paramC)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isAggregated(I paramI)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int maxContentLength()
/*     */   {
/* 143 */     return this.maxContentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int maxCumulationBufferComponents()
/*     */   {
/* 153 */     return this.maxCumulationBufferComponents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setMaxCumulationBufferComponents(int maxCumulationBufferComponents)
/*     */   {
/* 164 */     if (maxCumulationBufferComponents < 2) {
/* 165 */       throw new IllegalArgumentException("maxCumulationBufferComponents: " + maxCumulationBufferComponents + " (expected: >= 2)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 170 */     if (this.ctx == null) {
/* 171 */       this.maxCumulationBufferComponents = maxCumulationBufferComponents;
/*     */     } else {
/* 173 */       throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean isHandlingOversizedMessage()
/*     */   {
/* 183 */     return this.handlingOversizedMessage;
/*     */   }
/*     */   
/*     */   protected final ChannelHandlerContext ctx() {
/* 187 */     if (this.ctx == null) {
/* 188 */       throw new IllegalStateException("not added to a pipeline yet");
/*     */     }
/* 190 */     return this.ctx;
/*     */   }
/*     */   
/*     */   protected void decode(final ChannelHandlerContext ctx, I msg, List<Object> out) throws Exception
/*     */   {
/* 195 */     if (isStartMessage(msg)) {
/* 196 */       this.handlingOversizedMessage = false;
/* 197 */       if (this.currentMessage != null) {
/* 198 */         this.currentMessage.release();
/* 199 */         this.currentMessage = null;
/* 200 */         throw new MessageAggregationException();
/*     */       }
/*     */       
/*     */ 
/* 204 */       S m = msg;
/*     */       
/*     */ 
/*     */ 
/* 208 */       Object continueResponse = newContinueResponse(m, this.maxContentLength, ctx.pipeline());
/* 209 */       if (continueResponse != null)
/*     */       {
/* 211 */         ChannelFutureListener listener = this.continueResponseWriteListener;
/* 212 */         if (listener == null) {
/* 213 */           this.continueResponseWriteListener = ( = new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 216 */               if (!future.isSuccess()) {
/* 217 */                 ctx.fireExceptionCaught(future.cause());
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */         
/*     */ 
/* 224 */         boolean closeAfterWrite = closeAfterContinueResponse(continueResponse);
/* 225 */         this.handlingOversizedMessage = ignoreContentAfterContinueResponse(continueResponse);
/*     */         
/* 227 */         ChannelFuture future = ctx.writeAndFlush(continueResponse).addListener(listener);
/*     */         
/* 229 */         if (closeAfterWrite) {
/* 230 */           future.addListener(ChannelFutureListener.CLOSE);
/* 231 */           return;
/*     */         }
/* 233 */         if (this.handlingOversizedMessage) {
/* 234 */           return;
/*     */         }
/* 236 */       } else if (isContentLengthInvalid(m, this.maxContentLength))
/*     */       {
/* 238 */         invokeHandleOversizedMessage(ctx, m);
/* 239 */         return;
/*     */       }
/*     */       
/* 242 */       if (((m instanceof DecoderResultProvider)) && (!((DecoderResultProvider)m).decoderResult().isSuccess())) { O aggregated;
/*     */         O aggregated;
/* 244 */         if ((m instanceof ByteBufHolder)) {
/* 245 */           aggregated = beginAggregation(m, ((ByteBufHolder)m).content().retain());
/*     */         } else {
/* 247 */           aggregated = beginAggregation(m, Unpooled.EMPTY_BUFFER);
/*     */         }
/* 249 */         finishAggregation(aggregated);
/* 250 */         out.add(aggregated);
/* 251 */         return;
/*     */       }
/*     */       
/*     */ 
/* 255 */       CompositeByteBuf content = ctx.alloc().compositeBuffer(this.maxCumulationBufferComponents);
/* 256 */       if ((m instanceof ByteBufHolder)) {
/* 257 */         appendPartialContent(content, ((ByteBufHolder)m).content());
/*     */       }
/* 259 */       this.currentMessage = beginAggregation(m, content);
/* 260 */     } else if (isContentMessage(msg)) {
/* 261 */       if (this.currentMessage == null)
/*     */       {
/*     */ 
/* 264 */         return;
/*     */       }
/*     */       
/*     */ 
/* 268 */       CompositeByteBuf content = (CompositeByteBuf)this.currentMessage.content();
/*     */       
/*     */ 
/* 271 */       C m = (ByteBufHolder)msg;
/*     */       
/* 273 */       if (content.readableBytes() > this.maxContentLength - m.content().readableBytes())
/*     */       {
/*     */ 
/* 276 */         S s = this.currentMessage;
/* 277 */         invokeHandleOversizedMessage(ctx, s);
/* 278 */         return;
/*     */       }
/*     */       
/*     */ 
/* 282 */       appendPartialContent(content, m.content());
/*     */       
/*     */ 
/* 285 */       aggregate(this.currentMessage, m);
/*     */       boolean last;
/*     */       boolean last;
/* 288 */       if ((m instanceof DecoderResultProvider)) {
/* 289 */         DecoderResult decoderResult = ((DecoderResultProvider)m).decoderResult();
/* 290 */         boolean last; if (!decoderResult.isSuccess()) {
/* 291 */           if ((this.currentMessage instanceof DecoderResultProvider)) {
/* 292 */             ((DecoderResultProvider)this.currentMessage).setDecoderResult(
/* 293 */               DecoderResult.failure(decoderResult.cause()));
/*     */           }
/* 295 */           last = true;
/*     */         } else {
/* 297 */           last = isLastContentMessage(m);
/*     */         }
/*     */       } else {
/* 300 */         last = isLastContentMessage(m);
/*     */       }
/*     */       
/* 303 */       if (last) {
/* 304 */         finishAggregation(this.currentMessage);
/*     */         
/*     */ 
/* 307 */         out.add(this.currentMessage);
/* 308 */         this.currentMessage = null;
/*     */       }
/*     */     } else {
/* 311 */       throw new MessageAggregationException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void appendPartialContent(CompositeByteBuf content, ByteBuf partialContent) {
/* 316 */     if (partialContent.isReadable()) {
/* 317 */       content.addComponent(true, partialContent.retain());
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract boolean isContentLengthInvalid(S paramS, int paramInt)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract Object newContinueResponse(S paramS, int paramInt, ChannelPipeline paramChannelPipeline)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract boolean closeAfterContinueResponse(Object paramObject)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract boolean ignoreContentAfterContinueResponse(Object paramObject)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract O beginAggregation(S paramS, ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */   protected void aggregate(O aggregated, C content)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   protected void finishAggregation(O aggregated)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   /* Error */
/*     */   private void invokeHandleOversizedMessage(ChannelHandlerContext ctx, S oversized)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_1
/*     */     //   2: putfield 26	io/netty/handler/codec/MessageAggregator:handlingOversizedMessage	Z
/*     */     //   5: aload_0
/*     */     //   6: aconst_null
/*     */     //   7: putfield 28	io/netty/handler/codec/MessageAggregator:currentMessage	Lio/netty/buffer/ByteBufHolder;
/*     */     //   10: aload_0
/*     */     //   11: aload_1
/*     */     //   12: aload_2
/*     */     //   13: invokevirtual 67	io/netty/handler/codec/MessageAggregator:handleOversizedMessage	(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V
/*     */     //   16: aload_2
/*     */     //   17: invokestatic 68	io/netty/util/ReferenceCountUtil:release	(Ljava/lang/Object;)Z
/*     */     //   20: pop
/*     */     //   21: goto +11 -> 32
/*     */     //   24: astore_3
/*     */     //   25: aload_2
/*     */     //   26: invokestatic 68	io/netty/util/ReferenceCountUtil:release	(Ljava/lang/Object;)Z
/*     */     //   29: pop
/*     */     //   30: aload_3
/*     */     //   31: athrow
/*     */     //   32: return
/*     */     // Line number table:
/*     */     //   Java source line #380	-> byte code offset #0
/*     */     //   Java source line #381	-> byte code offset #5
/*     */     //   Java source line #383	-> byte code offset #10
/*     */     //   Java source line #386	-> byte code offset #16
/*     */     //   Java source line #387	-> byte code offset #21
/*     */     //   Java source line #386	-> byte code offset #24
/*     */     //   Java source line #387	-> byte code offset #30
/*     */     //   Java source line #388	-> byte code offset #32
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	33	0	this	MessageAggregator<I, S, C, O>
/*     */     //   0	33	1	ctx	ChannelHandlerContext
/*     */     //   0	33	2	oversized	S
/*     */     //   24	7	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   10	16	24	finally
/*     */   }
/*     */   
/*     */   protected void handleOversizedMessage(ChannelHandlerContext ctx, S oversized)
/*     */     throws Exception
/*     */   {
/* 398 */     ctx.fireExceptionCaught(new TooLongFrameException("content length exceeded " + 
/* 399 */       maxContentLength() + " bytes."));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void channelReadComplete(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 407 */     if ((this.currentMessage != null) && (!ctx.channel().config().isAutoRead())) {
/* 408 */       ctx.read();
/*     */     }
/* 410 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void channelInactive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial 80	io/netty/handler/codec/MessageToMessageDecoder:channelInactive	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   5: aload_0
/*     */     //   6: invokespecial 81	io/netty/handler/codec/MessageAggregator:releaseCurrentMessage	()V
/*     */     //   9: goto +10 -> 19
/*     */     //   12: astore_2
/*     */     //   13: aload_0
/*     */     //   14: invokespecial 81	io/netty/handler/codec/MessageAggregator:releaseCurrentMessage	()V
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     //   19: return
/*     */     // Line number table:
/*     */     //   Java source line #417	-> byte code offset #0
/*     */     //   Java source line #419	-> byte code offset #5
/*     */     //   Java source line #420	-> byte code offset #9
/*     */     //   Java source line #419	-> byte code offset #12
/*     */     //   Java source line #420	-> byte code offset #17
/*     */     //   Java source line #421	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	20	0	this	MessageAggregator<I, S, C, O>
/*     */     //   0	20	1	ctx	ChannelHandlerContext
/*     */     //   12	6	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	5	12	finally
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 425 */     this.ctx = ctx;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial 82	io/netty/handler/codec/MessageToMessageDecoder:handlerRemoved	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   5: aload_0
/*     */     //   6: invokespecial 81	io/netty/handler/codec/MessageAggregator:releaseCurrentMessage	()V
/*     */     //   9: goto +10 -> 19
/*     */     //   12: astore_2
/*     */     //   13: aload_0
/*     */     //   14: invokespecial 81	io/netty/handler/codec/MessageAggregator:releaseCurrentMessage	()V
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     //   19: return
/*     */     // Line number table:
/*     */     //   Java source line #431	-> byte code offset #0
/*     */     //   Java source line #435	-> byte code offset #5
/*     */     //   Java source line #436	-> byte code offset #9
/*     */     //   Java source line #435	-> byte code offset #12
/*     */     //   Java source line #436	-> byte code offset #17
/*     */     //   Java source line #437	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	20	0	this	MessageAggregator<I, S, C, O>
/*     */     //   0	20	1	ctx	ChannelHandlerContext
/*     */     //   12	6	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	5	12	finally
/*     */   }
/*     */   
/*     */   private void releaseCurrentMessage()
/*     */   {
/* 440 */     if (this.currentMessage != null) {
/* 441 */       this.currentMessage.release();
/* 442 */       this.currentMessage = null;
/* 443 */       this.handlingOversizedMessage = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\MessageAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */