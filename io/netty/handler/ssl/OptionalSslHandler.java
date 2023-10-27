/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class OptionalSslHandler
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final SslContext sslContext;
/*     */   
/*     */   public OptionalSslHandler(SslContext sslContext)
/*     */   {
/*  39 */     this.sslContext = ((SslContext)ObjectUtil.checkNotNull(sslContext, "sslContext"));
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  44 */     if (in.readableBytes() < 5) {
/*  45 */       return;
/*     */     }
/*  47 */     if (SslHandler.isEncrypted(in)) {
/*  48 */       handleSsl(context);
/*     */     } else {
/*  50 */       handleNonSsl(context);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void handleSsl(ChannelHandlerContext context)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aload_0
/*     */     //   3: aload_1
/*     */     //   4: aload_0
/*     */     //   5: getfield 5	io/netty/handler/ssl/OptionalSslHandler:sslContext	Lio/netty/handler/ssl/SslContext;
/*     */     //   8: invokevirtual 11	io/netty/handler/ssl/OptionalSslHandler:newSslHandler	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/handler/ssl/SslContext;)Lio/netty/handler/ssl/SslHandler;
/*     */     //   11: astore_2
/*     */     //   12: aload_1
/*     */     //   13: invokeinterface 12 1 0
/*     */     //   18: aload_0
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual 13	io/netty/handler/ssl/OptionalSslHandler:newSslHandlerName	()Ljava/lang/String;
/*     */     //   23: aload_2
/*     */     //   24: invokeinterface 14 4 0
/*     */     //   29: pop
/*     */     //   30: aconst_null
/*     */     //   31: astore_2
/*     */     //   32: aload_2
/*     */     //   33: ifnull +27 -> 60
/*     */     //   36: aload_2
/*     */     //   37: invokevirtual 15	io/netty/handler/ssl/SslHandler:engine	()Ljavax/net/ssl/SSLEngine;
/*     */     //   40: invokestatic 16	io/netty/util/ReferenceCountUtil:safeRelease	(Ljava/lang/Object;)V
/*     */     //   43: goto +17 -> 60
/*     */     //   46: astore_3
/*     */     //   47: aload_2
/*     */     //   48: ifnull +10 -> 58
/*     */     //   51: aload_2
/*     */     //   52: invokevirtual 15	io/netty/handler/ssl/SslHandler:engine	()Ljavax/net/ssl/SSLEngine;
/*     */     //   55: invokestatic 16	io/netty/util/ReferenceCountUtil:safeRelease	(Ljava/lang/Object;)V
/*     */     //   58: aload_3
/*     */     //   59: athrow
/*     */     //   60: return
/*     */     // Line number table:
/*     */     //   Java source line #55	-> byte code offset #0
/*     */     //   Java source line #57	-> byte code offset #2
/*     */     //   Java source line #58	-> byte code offset #12
/*     */     //   Java source line #59	-> byte code offset #30
/*     */     //   Java source line #63	-> byte code offset #32
/*     */     //   Java source line #64	-> byte code offset #36
/*     */     //   Java source line #63	-> byte code offset #46
/*     */     //   Java source line #64	-> byte code offset #51
/*     */     //   Java source line #66	-> byte code offset #58
/*     */     //   Java source line #67	-> byte code offset #60
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	61	0	this	OptionalSslHandler
/*     */     //   0	61	1	context	ChannelHandlerContext
/*     */     //   1	51	2	sslHandler	SslHandler
/*     */     //   46	13	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   2	32	46	finally
/*     */   }
/*     */   
/*     */   private void handleNonSsl(ChannelHandlerContext context)
/*     */   {
/*  70 */     ChannelHandler handler = newNonSslHandler(context);
/*  71 */     if (handler != null) {
/*  72 */       context.pipeline().replace(this, newNonSslHandlerName(), handler);
/*     */     } else {
/*  74 */       context.pipeline().remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String newSslHandlerName()
/*     */   {
/*  83 */     return null;
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
/*     */   protected SslHandler newSslHandler(ChannelHandlerContext context, SslContext sslContext)
/*     */   {
/*  97 */     return sslContext.newHandler(context.alloc());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String newNonSslHandlerName()
/*     */   {
/* 105 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChannelHandler newNonSslHandler(ChannelHandlerContext context)
/*     */   {
/* 115 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OptionalSslHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */