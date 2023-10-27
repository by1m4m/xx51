/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.TypeParameterMatcher;
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
/*     */ public abstract class ByteToMessageCodec<I>
/*     */   extends ChannelDuplexHandler
/*     */ {
/*     */   private final TypeParameterMatcher outboundMsgMatcher;
/*     */   private final MessageToByteEncoder<I> encoder;
/*  39 */   private final ByteToMessageDecoder decoder = new ByteToMessageDecoder()
/*     */   {
/*     */     public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  42 */       ByteToMessageCodec.this.decode(ctx, in, out);
/*     */     }
/*     */     
/*     */     protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */     {
/*  47 */       ByteToMessageCodec.this.decodeLast(ctx, in, out);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec()
/*     */   {
/*  55 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec(Class<? extends I> outboundMessageType)
/*     */   {
/*  62 */     this(outboundMessageType, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec(boolean preferDirect)
/*     */   {
/*  73 */     ensureNotSharable();
/*  74 */     this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
/*  75 */     this.encoder = new Encoder(preferDirect);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteToMessageCodec(Class<? extends I> outboundMessageType, boolean preferDirect)
/*     */   {
/*  87 */     ensureNotSharable();
/*  88 */     this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
/*  89 */     this.encoder = new Encoder(preferDirect);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean acceptOutboundMessage(Object msg)
/*     */     throws Exception
/*     */   {
/*  98 */     return this.outboundMsgMatcher.match(msg);
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 103 */     this.decoder.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 108 */     this.encoder.write(ctx, msg, promise);
/*     */   }
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 113 */     this.decoder.channelReadComplete(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 118 */     this.decoder.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	io/netty/handler/codec/ByteToMessageCodec:decoder	Lio/netty/handler/codec/ByteToMessageDecoder;
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 21	io/netty/handler/codec/ByteToMessageDecoder:handlerAdded	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   8: aload_0
/*     */     //   9: getfield 14	io/netty/handler/codec/ByteToMessageCodec:encoder	Lio/netty/handler/codec/MessageToByteEncoder;
/*     */     //   12: aload_1
/*     */     //   13: invokevirtual 22	io/netty/handler/codec/MessageToByteEncoder:handlerAdded	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   16: goto +14 -> 30
/*     */     //   19: astore_2
/*     */     //   20: aload_0
/*     */     //   21: getfield 14	io/netty/handler/codec/ByteToMessageCodec:encoder	Lio/netty/handler/codec/MessageToByteEncoder;
/*     */     //   24: aload_1
/*     */     //   25: invokevirtual 22	io/netty/handler/codec/MessageToByteEncoder:handlerAdded	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   28: aload_2
/*     */     //   29: athrow
/*     */     //   30: return
/*     */     // Line number table:
/*     */     //   Java source line #124	-> byte code offset #0
/*     */     //   Java source line #126	-> byte code offset #8
/*     */     //   Java source line #127	-> byte code offset #16
/*     */     //   Java source line #126	-> byte code offset #19
/*     */     //   Java source line #127	-> byte code offset #28
/*     */     //   Java source line #128	-> byte code offset #30
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	ByteToMessageCodec<I>
/*     */     //   0	31	1	ctx	ChannelHandlerContext
/*     */     //   19	10	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	8	19	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	io/netty/handler/codec/ByteToMessageCodec:decoder	Lio/netty/handler/codec/ByteToMessageDecoder;
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 23	io/netty/handler/codec/ByteToMessageDecoder:handlerRemoved	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   8: aload_0
/*     */     //   9: getfield 14	io/netty/handler/codec/ByteToMessageCodec:encoder	Lio/netty/handler/codec/MessageToByteEncoder;
/*     */     //   12: aload_1
/*     */     //   13: invokevirtual 24	io/netty/handler/codec/MessageToByteEncoder:handlerRemoved	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   16: goto +14 -> 30
/*     */     //   19: astore_2
/*     */     //   20: aload_0
/*     */     //   21: getfield 14	io/netty/handler/codec/ByteToMessageCodec:encoder	Lio/netty/handler/codec/MessageToByteEncoder;
/*     */     //   24: aload_1
/*     */     //   25: invokevirtual 24	io/netty/handler/codec/MessageToByteEncoder:handlerRemoved	(Lio/netty/channel/ChannelHandlerContext;)V
/*     */     //   28: aload_2
/*     */     //   29: athrow
/*     */     //   30: return
/*     */     // Line number table:
/*     */     //   Java source line #133	-> byte code offset #0
/*     */     //   Java source line #135	-> byte code offset #8
/*     */     //   Java source line #136	-> byte code offset #16
/*     */     //   Java source line #135	-> byte code offset #19
/*     */     //   Java source line #136	-> byte code offset #28
/*     */     //   Java source line #137	-> byte code offset #30
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	ByteToMessageCodec<I>
/*     */     //   0	31	1	ctx	ChannelHandlerContext
/*     */     //   19	10	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	8	19	finally
/*     */   }
/*     */   
/*     */   protected abstract void encode(ChannelHandlerContext paramChannelHandlerContext, I paramI, ByteBuf paramByteBuf)
/*     */     throws Exception;
/*     */   
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList)
/*     */     throws Exception;
/*     */   
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 153 */     if (in.isReadable())
/*     */     {
/*     */ 
/* 156 */       decode(ctx, in, out);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Encoder extends MessageToByteEncoder<I> {
/*     */     Encoder(boolean preferDirect) {
/* 162 */       super();
/*     */     }
/*     */     
/*     */     public boolean acceptOutboundMessage(Object msg) throws Exception
/*     */     {
/* 167 */       return ByteToMessageCodec.this.acceptOutboundMessage(msg);
/*     */     }
/*     */     
/*     */     protected void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception
/*     */     {
/* 172 */       ByteToMessageCodec.this.encode(ctx, msg, out);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\ByteToMessageCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */