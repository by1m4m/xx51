/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombinedChannelDuplexHandler<I extends ChannelInboundHandler, O extends ChannelOutboundHandler>
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CombinedChannelDuplexHandler.class);
/*     */   
/*     */   private DelegatingChannelHandlerContext inboundCtx;
/*     */   
/*     */   private DelegatingChannelHandlerContext outboundCtx;
/*     */   
/*     */   private volatile boolean handlerAdded;
/*     */   
/*     */   private I inboundHandler;
/*     */   
/*     */   private O outboundHandler;
/*     */   
/*     */ 
/*     */   protected CombinedChannelDuplexHandler()
/*     */   {
/*  49 */     ensureNotSharable();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CombinedChannelDuplexHandler(I inboundHandler, O outboundHandler)
/*     */   {
/*  56 */     ensureNotSharable();
/*  57 */     init(inboundHandler, outboundHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void init(I inboundHandler, O outboundHandler)
/*     */   {
/*  69 */     validate(inboundHandler, outboundHandler);
/*  70 */     this.inboundHandler = inboundHandler;
/*  71 */     this.outboundHandler = outboundHandler;
/*     */   }
/*     */   
/*     */   private void validate(I inboundHandler, O outboundHandler) {
/*  75 */     if (this.inboundHandler != null)
/*     */     {
/*  77 */       throw new IllegalStateException("init() can not be invoked if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with non-default constructor.");
/*     */     }
/*     */     
/*     */ 
/*  81 */     if (inboundHandler == null) {
/*  82 */       throw new NullPointerException("inboundHandler");
/*     */     }
/*  84 */     if (outboundHandler == null) {
/*  85 */       throw new NullPointerException("outboundHandler");
/*     */     }
/*  87 */     if ((inboundHandler instanceof ChannelOutboundHandler))
/*     */     {
/*     */ 
/*  90 */       throw new IllegalArgumentException("inboundHandler must not implement " + ChannelOutboundHandler.class.getSimpleName() + " to get combined.");
/*     */     }
/*  92 */     if ((outboundHandler instanceof ChannelInboundHandler))
/*     */     {
/*     */ 
/*  95 */       throw new IllegalArgumentException("outboundHandler must not implement " + ChannelInboundHandler.class.getSimpleName() + " to get combined.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected final I inboundHandler() {
/* 100 */     return this.inboundHandler;
/*     */   }
/*     */   
/*     */   protected final O outboundHandler() {
/* 104 */     return this.outboundHandler;
/*     */   }
/*     */   
/*     */   private void checkAdded() {
/* 108 */     if (!this.handlerAdded) {
/* 109 */       throw new IllegalStateException("handler not added to pipeline yet");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void removeInboundHandler()
/*     */   {
/* 117 */     checkAdded();
/* 118 */     this.inboundCtx.remove();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void removeOutboundHandler()
/*     */   {
/* 125 */     checkAdded();
/* 126 */     this.outboundCtx.remove();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 8	io/netty/channel/CombinedChannelDuplexHandler:inboundHandler	Lio/netty/channel/ChannelInboundHandler;
/*     */     //   4: ifnonnull +52 -> 56
/*     */     //   7: new 9	java/lang/IllegalStateException
/*     */     //   10: dup
/*     */     //   11: new 10	java/lang/StringBuilder
/*     */     //   14: dup
/*     */     //   15: invokespecial 11	java/lang/StringBuilder:<init>	()V
/*     */     //   18: ldc 35
/*     */     //   20: invokevirtual 13	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   23: ldc 36
/*     */     //   25: invokevirtual 15	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */     //   28: invokevirtual 13	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   31: ldc 37
/*     */     //   33: invokevirtual 13	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   36: ldc 14
/*     */     //   38: invokevirtual 15	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */     //   41: invokevirtual 13	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   44: ldc 38
/*     */     //   46: invokevirtual 13	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   49: invokevirtual 17	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   52: invokespecial 18	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   55: athrow
/*     */     //   56: aload_0
/*     */     //   57: new 39	io/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext
/*     */     //   60: dup
/*     */     //   61: aload_1
/*     */     //   62: aload_0
/*     */     //   63: getfield 2	io/netty/channel/CombinedChannelDuplexHandler:outboundHandler	Lio/netty/channel/ChannelOutboundHandler;
/*     */     //   66: invokespecial 40	io/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext:<init>	(Lio/netty/channel/ChannelHandlerContext;Lio/netty/channel/ChannelHandler;)V
/*     */     //   69: putfield 3	io/netty/channel/CombinedChannelDuplexHandler:outboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   72: aload_0
/*     */     //   73: new 41	io/netty/channel/CombinedChannelDuplexHandler$1
/*     */     //   76: dup
/*     */     //   77: aload_0
/*     */     //   78: aload_1
/*     */     //   79: aload_0
/*     */     //   80: getfield 8	io/netty/channel/CombinedChannelDuplexHandler:inboundHandler	Lio/netty/channel/ChannelInboundHandler;
/*     */     //   83: invokespecial 42	io/netty/channel/CombinedChannelDuplexHandler$1:<init>	(Lio/netty/channel/CombinedChannelDuplexHandler;Lio/netty/channel/ChannelHandlerContext;Lio/netty/channel/ChannelHandler;)V
/*     */     //   86: putfield 33	io/netty/channel/CombinedChannelDuplexHandler:inboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   89: aload_0
/*     */     //   90: iconst_1
/*     */     //   91: putfield 30	io/netty/channel/CombinedChannelDuplexHandler:handlerAdded	Z
/*     */     //   94: aload_0
/*     */     //   95: getfield 8	io/netty/channel/CombinedChannelDuplexHandler:inboundHandler	Lio/netty/channel/ChannelInboundHandler;
/*     */     //   98: aload_0
/*     */     //   99: getfield 33	io/netty/channel/CombinedChannelDuplexHandler:inboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   102: invokeinterface 43 2 0
/*     */     //   107: aload_0
/*     */     //   108: getfield 2	io/netty/channel/CombinedChannelDuplexHandler:outboundHandler	Lio/netty/channel/ChannelOutboundHandler;
/*     */     //   111: aload_0
/*     */     //   112: getfield 3	io/netty/channel/CombinedChannelDuplexHandler:outboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   115: invokeinterface 44 2 0
/*     */     //   120: goto +19 -> 139
/*     */     //   123: astore_2
/*     */     //   124: aload_0
/*     */     //   125: getfield 2	io/netty/channel/CombinedChannelDuplexHandler:outboundHandler	Lio/netty/channel/ChannelOutboundHandler;
/*     */     //   128: aload_0
/*     */     //   129: getfield 3	io/netty/channel/CombinedChannelDuplexHandler:outboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   132: invokeinterface 44 2 0
/*     */     //   137: aload_2
/*     */     //   138: athrow
/*     */     //   139: return
/*     */     // Line number table:
/*     */     //   Java source line #131	-> byte code offset #0
/*     */     //   Java source line #132	-> byte code offset #7
/*     */     //   Java source line #133	-> byte code offset #25
/*     */     //   Java source line #134	-> byte code offset #38
/*     */     //   Java source line #138	-> byte code offset #56
/*     */     //   Java source line #139	-> byte code offset #72
/*     */     //   Java source line #171	-> byte code offset #89
/*     */     //   Java source line #174	-> byte code offset #94
/*     */     //   Java source line #176	-> byte code offset #107
/*     */     //   Java source line #177	-> byte code offset #120
/*     */     //   Java source line #176	-> byte code offset #123
/*     */     //   Java source line #177	-> byte code offset #137
/*     */     //   Java source line #178	-> byte code offset #139
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	140	0	this	CombinedChannelDuplexHandler<I, O>
/*     */     //   0	140	1	ctx	ChannelHandlerContext
/*     */     //   123	15	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   94	107	123	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void handlerRemoved(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 33	io/netty/channel/CombinedChannelDuplexHandler:inboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   4: invokevirtual 34	io/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext:remove	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 3	io/netty/channel/CombinedChannelDuplexHandler:outboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   11: invokevirtual 34	io/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext:remove	()V
/*     */     //   14: goto +13 -> 27
/*     */     //   17: astore_2
/*     */     //   18: aload_0
/*     */     //   19: getfield 3	io/netty/channel/CombinedChannelDuplexHandler:outboundCtx	Lio/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext;
/*     */     //   22: invokevirtual 34	io/netty/channel/CombinedChannelDuplexHandler$DelegatingChannelHandlerContext:remove	()V
/*     */     //   25: aload_2
/*     */     //   26: athrow
/*     */     //   27: return
/*     */     // Line number table:
/*     */     //   Java source line #183	-> byte code offset #0
/*     */     //   Java source line #185	-> byte code offset #7
/*     */     //   Java source line #186	-> byte code offset #14
/*     */     //   Java source line #185	-> byte code offset #17
/*     */     //   Java source line #186	-> byte code offset #25
/*     */     //   Java source line #187	-> byte code offset #27
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	28	0	this	CombinedChannelDuplexHandler<I, O>
/*     */     //   0	28	1	ctx	ChannelHandlerContext
/*     */     //   17	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	7	17	finally
/*     */   }
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 191 */     assert (ctx == this.inboundCtx.ctx);
/* 192 */     if (!this.inboundCtx.removed) {
/* 193 */       this.inboundHandler.channelRegistered(this.inboundCtx);
/*     */     } else {
/* 195 */       this.inboundCtx.fireChannelRegistered();
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 201 */     assert (ctx == this.inboundCtx.ctx);
/* 202 */     if (!this.inboundCtx.removed) {
/* 203 */       this.inboundHandler.channelUnregistered(this.inboundCtx);
/*     */     } else {
/* 205 */       this.inboundCtx.fireChannelUnregistered();
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 211 */     assert (ctx == this.inboundCtx.ctx);
/* 212 */     if (!this.inboundCtx.removed) {
/* 213 */       this.inboundHandler.channelActive(this.inboundCtx);
/*     */     } else {
/* 215 */       this.inboundCtx.fireChannelActive();
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 221 */     assert (ctx == this.inboundCtx.ctx);
/* 222 */     if (!this.inboundCtx.removed) {
/* 223 */       this.inboundHandler.channelInactive(this.inboundCtx);
/*     */     } else {
/* 225 */       this.inboundCtx.fireChannelInactive();
/*     */     }
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 231 */     assert (ctx == this.inboundCtx.ctx);
/* 232 */     if (!this.inboundCtx.removed) {
/* 233 */       this.inboundHandler.exceptionCaught(this.inboundCtx, cause);
/*     */     } else {
/* 235 */       this.inboundCtx.fireExceptionCaught(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
/*     */   {
/* 241 */     assert (ctx == this.inboundCtx.ctx);
/* 242 */     if (!this.inboundCtx.removed) {
/* 243 */       this.inboundHandler.userEventTriggered(this.inboundCtx, evt);
/*     */     } else {
/* 245 */       this.inboundCtx.fireUserEventTriggered(evt);
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/* 251 */     assert (ctx == this.inboundCtx.ctx);
/* 252 */     if (!this.inboundCtx.removed) {
/* 253 */       this.inboundHandler.channelRead(this.inboundCtx, msg);
/*     */     } else {
/* 255 */       this.inboundCtx.fireChannelRead(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 261 */     assert (ctx == this.inboundCtx.ctx);
/* 262 */     if (!this.inboundCtx.removed) {
/* 263 */       this.inboundHandler.channelReadComplete(this.inboundCtx);
/*     */     } else {
/* 265 */       this.inboundCtx.fireChannelReadComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 271 */     assert (ctx == this.inboundCtx.ctx);
/* 272 */     if (!this.inboundCtx.removed) {
/* 273 */       this.inboundHandler.channelWritabilityChanged(this.inboundCtx);
/*     */     } else {
/* 275 */       this.inboundCtx.fireChannelWritabilityChanged();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 283 */     assert (ctx == this.outboundCtx.ctx);
/* 284 */     if (!this.outboundCtx.removed) {
/* 285 */       this.outboundHandler.bind(this.outboundCtx, localAddress, promise);
/*     */     } else {
/* 287 */       this.outboundCtx.bind(localAddress, promise);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 296 */     assert (ctx == this.outboundCtx.ctx);
/* 297 */     if (!this.outboundCtx.removed) {
/* 298 */       this.outboundHandler.connect(this.outboundCtx, remoteAddress, localAddress, promise);
/*     */     } else {
/* 300 */       this.outboundCtx.connect(localAddress, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 306 */     assert (ctx == this.outboundCtx.ctx);
/* 307 */     if (!this.outboundCtx.removed) {
/* 308 */       this.outboundHandler.disconnect(this.outboundCtx, promise);
/*     */     } else {
/* 310 */       this.outboundCtx.disconnect(promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 316 */     assert (ctx == this.outboundCtx.ctx);
/* 317 */     if (!this.outboundCtx.removed) {
/* 318 */       this.outboundHandler.close(this.outboundCtx, promise);
/*     */     } else {
/* 320 */       this.outboundCtx.close(promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 326 */     assert (ctx == this.outboundCtx.ctx);
/* 327 */     if (!this.outboundCtx.removed) {
/* 328 */       this.outboundHandler.deregister(this.outboundCtx, promise);
/*     */     } else {
/* 330 */       this.outboundCtx.deregister(promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 336 */     assert (ctx == this.outboundCtx.ctx);
/* 337 */     if (!this.outboundCtx.removed) {
/* 338 */       this.outboundHandler.read(this.outboundCtx);
/*     */     } else {
/* 340 */       this.outboundCtx.read();
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 346 */     assert (ctx == this.outboundCtx.ctx);
/* 347 */     if (!this.outboundCtx.removed) {
/* 348 */       this.outboundHandler.write(this.outboundCtx, msg, promise);
/*     */     } else {
/* 350 */       this.outboundCtx.write(msg, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 356 */     assert (ctx == this.outboundCtx.ctx);
/* 357 */     if (!this.outboundCtx.removed) {
/* 358 */       this.outboundHandler.flush(this.outboundCtx);
/*     */     } else {
/* 360 */       this.outboundCtx.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DelegatingChannelHandlerContext implements ChannelHandlerContext
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelHandler handler;
/*     */     boolean removed;
/*     */     
/*     */     DelegatingChannelHandlerContext(ChannelHandlerContext ctx, ChannelHandler handler) {
/* 371 */       this.ctx = ctx;
/* 372 */       this.handler = handler;
/*     */     }
/*     */     
/*     */     public Channel channel()
/*     */     {
/* 377 */       return this.ctx.channel();
/*     */     }
/*     */     
/*     */     public EventExecutor executor()
/*     */     {
/* 382 */       return this.ctx.executor();
/*     */     }
/*     */     
/*     */     public String name()
/*     */     {
/* 387 */       return this.ctx.name();
/*     */     }
/*     */     
/*     */     public ChannelHandler handler()
/*     */     {
/* 392 */       return this.ctx.handler();
/*     */     }
/*     */     
/*     */     public boolean isRemoved()
/*     */     {
/* 397 */       return (this.removed) || (this.ctx.isRemoved());
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelRegistered()
/*     */     {
/* 402 */       this.ctx.fireChannelRegistered();
/* 403 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelUnregistered()
/*     */     {
/* 408 */       this.ctx.fireChannelUnregistered();
/* 409 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelActive()
/*     */     {
/* 414 */       this.ctx.fireChannelActive();
/* 415 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelInactive()
/*     */     {
/* 420 */       this.ctx.fireChannelInactive();
/* 421 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireExceptionCaught(Throwable cause)
/*     */     {
/* 426 */       this.ctx.fireExceptionCaught(cause);
/* 427 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireUserEventTriggered(Object event)
/*     */     {
/* 432 */       this.ctx.fireUserEventTriggered(event);
/* 433 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelRead(Object msg)
/*     */     {
/* 438 */       this.ctx.fireChannelRead(msg);
/* 439 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelReadComplete()
/*     */     {
/* 444 */       this.ctx.fireChannelReadComplete();
/* 445 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext fireChannelWritabilityChanged()
/*     */     {
/* 450 */       this.ctx.fireChannelWritabilityChanged();
/* 451 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelFuture bind(SocketAddress localAddress)
/*     */     {
/* 456 */       return this.ctx.bind(localAddress);
/*     */     }
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress)
/*     */     {
/* 461 */       return this.ctx.connect(remoteAddress);
/*     */     }
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     {
/* 466 */       return this.ctx.connect(remoteAddress, localAddress);
/*     */     }
/*     */     
/*     */     public ChannelFuture disconnect()
/*     */     {
/* 471 */       return this.ctx.disconnect();
/*     */     }
/*     */     
/*     */     public ChannelFuture close()
/*     */     {
/* 476 */       return this.ctx.close();
/*     */     }
/*     */     
/*     */     public ChannelFuture deregister()
/*     */     {
/* 481 */       return this.ctx.deregister();
/*     */     }
/*     */     
/*     */     public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 486 */       return this.ctx.bind(localAddress, promise);
/*     */     }
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*     */     {
/* 491 */       return this.ctx.connect(remoteAddress, promise);
/*     */     }
/*     */     
/*     */ 
/*     */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 497 */       return this.ctx.connect(remoteAddress, localAddress, promise);
/*     */     }
/*     */     
/*     */     public ChannelFuture disconnect(ChannelPromise promise)
/*     */     {
/* 502 */       return this.ctx.disconnect(promise);
/*     */     }
/*     */     
/*     */     public ChannelFuture close(ChannelPromise promise)
/*     */     {
/* 507 */       return this.ctx.close(promise);
/*     */     }
/*     */     
/*     */     public ChannelFuture deregister(ChannelPromise promise)
/*     */     {
/* 512 */       return this.ctx.deregister(promise);
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext read()
/*     */     {
/* 517 */       this.ctx.read();
/* 518 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelFuture write(Object msg)
/*     */     {
/* 523 */       return this.ctx.write(msg);
/*     */     }
/*     */     
/*     */     public ChannelFuture write(Object msg, ChannelPromise promise)
/*     */     {
/* 528 */       return this.ctx.write(msg, promise);
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext flush()
/*     */     {
/* 533 */       this.ctx.flush();
/* 534 */       return this;
/*     */     }
/*     */     
/*     */     public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*     */     {
/* 539 */       return this.ctx.writeAndFlush(msg, promise);
/*     */     }
/*     */     
/*     */     public ChannelFuture writeAndFlush(Object msg)
/*     */     {
/* 544 */       return this.ctx.writeAndFlush(msg);
/*     */     }
/*     */     
/*     */     public ChannelPipeline pipeline()
/*     */     {
/* 549 */       return this.ctx.pipeline();
/*     */     }
/*     */     
/*     */     public ByteBufAllocator alloc()
/*     */     {
/* 554 */       return this.ctx.alloc();
/*     */     }
/*     */     
/*     */     public ChannelPromise newPromise()
/*     */     {
/* 559 */       return this.ctx.newPromise();
/*     */     }
/*     */     
/*     */     public ChannelProgressivePromise newProgressivePromise()
/*     */     {
/* 564 */       return this.ctx.newProgressivePromise();
/*     */     }
/*     */     
/*     */     public ChannelFuture newSucceededFuture()
/*     */     {
/* 569 */       return this.ctx.newSucceededFuture();
/*     */     }
/*     */     
/*     */     public ChannelFuture newFailedFuture(Throwable cause)
/*     */     {
/* 574 */       return this.ctx.newFailedFuture(cause);
/*     */     }
/*     */     
/*     */     public ChannelPromise voidPromise()
/*     */     {
/* 579 */       return this.ctx.voidPromise();
/*     */     }
/*     */     
/*     */     public <T> Attribute<T> attr(AttributeKey<T> key)
/*     */     {
/* 584 */       return this.ctx.channel().attr(key);
/*     */     }
/*     */     
/*     */     public <T> boolean hasAttr(AttributeKey<T> key)
/*     */     {
/* 589 */       return this.ctx.channel().hasAttr(key);
/*     */     }
/*     */     
/*     */     final void remove() {
/* 593 */       EventExecutor executor = executor();
/* 594 */       if (executor.inEventLoop()) {
/* 595 */         remove0();
/*     */       } else {
/* 597 */         executor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 600 */             CombinedChannelDuplexHandler.DelegatingChannelHandlerContext.this.remove0();
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     
/*     */     private void remove0() {
/* 607 */       if (!this.removed) {
/* 608 */         this.removed = true;
/*     */         try {
/* 610 */           this.handler.handlerRemoved(this);
/*     */         } catch (Throwable cause) {
/* 612 */           fireExceptionCaught(new ChannelPipelineException(this.handler
/* 613 */             .getClass().getName() + ".handlerRemoved() has thrown an exception.", cause));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\CombinedChannelDuplexHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */