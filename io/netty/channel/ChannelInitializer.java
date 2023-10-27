/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ChannelHandler.Sharable
/*     */ public abstract class ChannelInitializer<C extends Channel>
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);
/*     */   
/*     */ 
/*  58 */   private final ConcurrentMap<ChannelHandlerContext, Boolean> initMap = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void initChannel(C paramC)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  76 */     if (initChannel(ctx))
/*     */     {
/*     */ 
/*  79 */       ctx.pipeline().fireChannelRegistered();
/*     */     }
/*     */     else {
/*  82 */       ctx.fireChannelRegistered();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */     throws Exception
/*     */   {
/*  91 */     if (logger.isWarnEnabled()) {
/*  92 */       logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), cause);
/*     */     }
/*  94 */     ctx.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 102 */     if (ctx.channel().isRegistered())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 107 */       initChannel(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean initChannel(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 113 */     if (this.initMap.putIfAbsent(ctx, Boolean.TRUE) == null) {
/*     */       try {
/* 115 */         initChannel(ctx.channel());
/*     */       }
/*     */       catch (Throwable cause)
/*     */       {
/* 119 */         exceptionCaught(ctx, cause);
/*     */       } finally {
/* 121 */         remove(ctx);
/*     */       }
/* 123 */       return true;
/*     */     }
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void remove(ChannelHandlerContext ctx)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface 5 1 0
/*     */     //   6: astore_2
/*     */     //   7: aload_2
/*     */     //   8: aload_0
/*     */     //   9: invokeinterface 26 2 0
/*     */     //   14: ifnull +11 -> 25
/*     */     //   17: aload_2
/*     */     //   18: aload_0
/*     */     //   19: invokeinterface 27 2 0
/*     */     //   24: pop
/*     */     //   25: aload_0
/*     */     //   26: getfield 3	io/netty/channel/ChannelInitializer:initMap	Ljava/util/concurrent/ConcurrentMap;
/*     */     //   29: aload_1
/*     */     //   30: invokeinterface 28 2 0
/*     */     //   35: pop
/*     */     //   36: goto +17 -> 53
/*     */     //   39: astore_3
/*     */     //   40: aload_0
/*     */     //   41: getfield 3	io/netty/channel/ChannelInitializer:initMap	Ljava/util/concurrent/ConcurrentMap;
/*     */     //   44: aload_1
/*     */     //   45: invokeinterface 28 2 0
/*     */     //   50: pop
/*     */     //   51: aload_3
/*     */     //   52: athrow
/*     */     //   53: return
/*     */     // Line number table:
/*     */     //   Java source line #130	-> byte code offset #0
/*     */     //   Java source line #131	-> byte code offset #7
/*     */     //   Java source line #132	-> byte code offset #17
/*     */     //   Java source line #135	-> byte code offset #25
/*     */     //   Java source line #136	-> byte code offset #36
/*     */     //   Java source line #135	-> byte code offset #39
/*     */     //   Java source line #136	-> byte code offset #51
/*     */     //   Java source line #137	-> byte code offset #53
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	54	0	this	ChannelInitializer<C>
/*     */     //   0	54	1	ctx	ChannelHandlerContext
/*     */     //   6	12	2	pipeline	ChannelPipeline
/*     */     //   39	13	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	25	39	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\ChannelInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */