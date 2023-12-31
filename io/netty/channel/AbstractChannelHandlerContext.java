/*      */ package io.netty.channel;
/*      */ 
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.util.Attribute;
/*      */ import io.netty.util.AttributeKey;
/*      */ import io.netty.util.DefaultAttributeMap;
/*      */ import io.netty.util.Recycler;
/*      */ import io.netty.util.Recycler.Handle;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.ResourceLeakHint;
/*      */ import io.netty.util.concurrent.EventExecutor;
/*      */ import io.netty.util.concurrent.OrderedEventExecutor;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PromiseNotificationUtil;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.SystemPropertyUtil;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ abstract class AbstractChannelHandlerContext
/*      */   extends DefaultAttributeMap
/*      */   implements ChannelHandlerContext, ResourceLeakHint
/*      */ {
/*   41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannelHandlerContext.class);
/*      */   
/*      */   volatile AbstractChannelHandlerContext next;
/*      */   
/*      */   volatile AbstractChannelHandlerContext prev;
/*   46 */   private static final AtomicIntegerFieldUpdater<AbstractChannelHandlerContext> HANDLER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractChannelHandlerContext.class, "handlerState");
/*      */   
/*      */ 
/*      */   private static final int ADD_PENDING = 1;
/*      */   
/*      */ 
/*      */   private static final int ADD_COMPLETE = 2;
/*      */   
/*      */ 
/*      */   private static final int REMOVE_COMPLETE = 3;
/*      */   
/*      */ 
/*      */   private static final int INIT = 0;
/*      */   
/*      */ 
/*      */   private final boolean inbound;
/*      */   
/*      */ 
/*      */   private final boolean outbound;
/*      */   
/*      */   private final DefaultChannelPipeline pipeline;
/*      */   
/*      */   private final String name;
/*      */   
/*      */   private final boolean ordered;
/*      */   
/*      */   final EventExecutor executor;
/*      */   
/*      */   private ChannelFuture succeededFuture;
/*      */   
/*      */   private Runnable invokeChannelReadCompleteTask;
/*      */   
/*      */   private Runnable invokeReadTask;
/*      */   
/*      */   private Runnable invokeChannelWritableStateChangedTask;
/*      */   
/*      */   private Runnable invokeFlushTask;
/*      */   
/*   84 */   private volatile int handlerState = 0;
/*      */   
/*      */   AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name, boolean inbound, boolean outbound)
/*      */   {
/*   88 */     this.name = ((String)ObjectUtil.checkNotNull(name, "name"));
/*   89 */     this.pipeline = pipeline;
/*   90 */     this.executor = executor;
/*   91 */     this.inbound = inbound;
/*   92 */     this.outbound = outbound;
/*      */     
/*   94 */     this.ordered = ((executor == null) || ((executor instanceof OrderedEventExecutor)));
/*      */   }
/*      */   
/*      */   public Channel channel()
/*      */   {
/*   99 */     return this.pipeline.channel();
/*      */   }
/*      */   
/*      */   public ChannelPipeline pipeline()
/*      */   {
/*  104 */     return this.pipeline;
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc()
/*      */   {
/*  109 */     return channel().config().getAllocator();
/*      */   }
/*      */   
/*      */   public EventExecutor executor()
/*      */   {
/*  114 */     if (this.executor == null) {
/*  115 */       return channel().eventLoop();
/*      */     }
/*  117 */     return this.executor;
/*      */   }
/*      */   
/*      */ 
/*      */   public String name()
/*      */   {
/*  123 */     return this.name;
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelRegistered()
/*      */   {
/*  128 */     invokeChannelRegistered(findContextInbound());
/*  129 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelRegistered(AbstractChannelHandlerContext next) {
/*  133 */     EventExecutor executor = next.executor();
/*  134 */     if (executor.inEventLoop()) {
/*  135 */       next.invokeChannelRegistered();
/*      */     } else {
/*  137 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  140 */           this.val$next.invokeChannelRegistered();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelRegistered() {
/*  147 */     if (invokeHandler()) {
/*      */       try {
/*  149 */         ((ChannelInboundHandler)handler()).channelRegistered(this);
/*      */       } catch (Throwable t) {
/*  151 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  154 */       fireChannelRegistered();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelUnregistered()
/*      */   {
/*  160 */     invokeChannelUnregistered(findContextInbound());
/*  161 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelUnregistered(AbstractChannelHandlerContext next) {
/*  165 */     EventExecutor executor = next.executor();
/*  166 */     if (executor.inEventLoop()) {
/*  167 */       next.invokeChannelUnregistered();
/*      */     } else {
/*  169 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  172 */           this.val$next.invokeChannelUnregistered();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelUnregistered() {
/*  179 */     if (invokeHandler()) {
/*      */       try {
/*  181 */         ((ChannelInboundHandler)handler()).channelUnregistered(this);
/*      */       } catch (Throwable t) {
/*  183 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  186 */       fireChannelUnregistered();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelActive()
/*      */   {
/*  192 */     invokeChannelActive(findContextInbound());
/*  193 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelActive(AbstractChannelHandlerContext next) {
/*  197 */     EventExecutor executor = next.executor();
/*  198 */     if (executor.inEventLoop()) {
/*  199 */       next.invokeChannelActive();
/*      */     } else {
/*  201 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  204 */           this.val$next.invokeChannelActive();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelActive() {
/*  211 */     if (invokeHandler()) {
/*      */       try {
/*  213 */         ((ChannelInboundHandler)handler()).channelActive(this);
/*      */       } catch (Throwable t) {
/*  215 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  218 */       fireChannelActive();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelInactive()
/*      */   {
/*  224 */     invokeChannelInactive(findContextInbound());
/*  225 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelInactive(AbstractChannelHandlerContext next) {
/*  229 */     EventExecutor executor = next.executor();
/*  230 */     if (executor.inEventLoop()) {
/*  231 */       next.invokeChannelInactive();
/*      */     } else {
/*  233 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  236 */           this.val$next.invokeChannelInactive();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelInactive() {
/*  243 */     if (invokeHandler()) {
/*      */       try {
/*  245 */         ((ChannelInboundHandler)handler()).channelInactive(this);
/*      */       } catch (Throwable t) {
/*  247 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  250 */       fireChannelInactive();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireExceptionCaught(Throwable cause)
/*      */   {
/*  256 */     invokeExceptionCaught(this.next, cause);
/*  257 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeExceptionCaught(AbstractChannelHandlerContext next, final Throwable cause) {
/*  261 */     ObjectUtil.checkNotNull(cause, "cause");
/*  262 */     EventExecutor executor = next.executor();
/*  263 */     if (executor.inEventLoop()) {
/*  264 */       next.invokeExceptionCaught(cause);
/*      */     } else {
/*      */       try {
/*  267 */         executor.execute(new Runnable()
/*      */         {
/*      */           public void run() {
/*  270 */             this.val$next.invokeExceptionCaught(cause);
/*      */           }
/*      */         });
/*      */       } catch (Throwable t) {
/*  274 */         if (logger.isWarnEnabled()) {
/*  275 */           logger.warn("Failed to submit an exceptionCaught() event.", t);
/*  276 */           logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeExceptionCaught(Throwable cause) {
/*  283 */     if (invokeHandler()) {
/*      */       try {
/*  285 */         handler().exceptionCaught(this, cause);
/*      */       } catch (Throwable error) {
/*  287 */         if (logger.isDebugEnabled()) {
/*  288 */           logger.debug("An exception {}was thrown by a user handler's exceptionCaught() method while handling the following exception:", 
/*      */           
/*      */ 
/*      */ 
/*  292 */             ThrowableUtil.stackTraceToString(error), cause);
/*  293 */         } else if (logger.isWarnEnabled()) {
/*  294 */           logger.warn("An exception '{}' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception:", error, cause);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  301 */       fireExceptionCaught(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireUserEventTriggered(Object event)
/*      */   {
/*  307 */     invokeUserEventTriggered(findContextInbound(), event);
/*  308 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeUserEventTriggered(AbstractChannelHandlerContext next, final Object event) {
/*  312 */     ObjectUtil.checkNotNull(event, "event");
/*  313 */     EventExecutor executor = next.executor();
/*  314 */     if (executor.inEventLoop()) {
/*  315 */       next.invokeUserEventTriggered(event);
/*      */     } else {
/*  317 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  320 */           this.val$next.invokeUserEventTriggered(event);
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeUserEventTriggered(Object event) {
/*  327 */     if (invokeHandler()) {
/*      */       try {
/*  329 */         ((ChannelInboundHandler)handler()).userEventTriggered(this, event);
/*      */       } catch (Throwable t) {
/*  331 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  334 */       fireUserEventTriggered(event);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelRead(Object msg)
/*      */   {
/*  340 */     invokeChannelRead(findContextInbound(), msg);
/*  341 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelRead(AbstractChannelHandlerContext next, Object msg) {
/*  345 */     final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, "msg"), next);
/*  346 */     EventExecutor executor = next.executor();
/*  347 */     if (executor.inEventLoop()) {
/*  348 */       next.invokeChannelRead(m);
/*      */     } else {
/*  350 */       executor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  353 */           this.val$next.invokeChannelRead(m);
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelRead(Object msg) {
/*  360 */     if (invokeHandler()) {
/*      */       try {
/*  362 */         ((ChannelInboundHandler)handler()).channelRead(this, msg);
/*      */       } catch (Throwable t) {
/*  364 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  367 */       fireChannelRead(msg);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelReadComplete()
/*      */   {
/*  373 */     invokeChannelReadComplete(findContextInbound());
/*  374 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelReadComplete(AbstractChannelHandlerContext next) {
/*  378 */     EventExecutor executor = next.executor();
/*  379 */     if (executor.inEventLoop()) {
/*  380 */       next.invokeChannelReadComplete();
/*      */     } else {
/*  382 */       Runnable task = next.invokeChannelReadCompleteTask;
/*  383 */       if (task == null) {
/*  384 */         next.invokeChannelReadCompleteTask = ( = new Runnable()
/*      */         {
/*      */           public void run() {
/*  387 */             this.val$next.invokeChannelReadComplete();
/*      */           }
/*      */         });
/*      */       }
/*  391 */       executor.execute(task);
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelReadComplete() {
/*  396 */     if (invokeHandler()) {
/*      */       try {
/*  398 */         ((ChannelInboundHandler)handler()).channelReadComplete(this);
/*      */       } catch (Throwable t) {
/*  400 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  403 */       fireChannelReadComplete();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext fireChannelWritabilityChanged()
/*      */   {
/*  409 */     invokeChannelWritabilityChanged(findContextInbound());
/*  410 */     return this;
/*      */   }
/*      */   
/*      */   static void invokeChannelWritabilityChanged(AbstractChannelHandlerContext next) {
/*  414 */     EventExecutor executor = next.executor();
/*  415 */     if (executor.inEventLoop()) {
/*  416 */       next.invokeChannelWritabilityChanged();
/*      */     } else {
/*  418 */       Runnable task = next.invokeChannelWritableStateChangedTask;
/*  419 */       if (task == null) {
/*  420 */         next.invokeChannelWritableStateChangedTask = ( = new Runnable()
/*      */         {
/*      */           public void run() {
/*  423 */             this.val$next.invokeChannelWritabilityChanged();
/*      */           }
/*      */         });
/*      */       }
/*  427 */       executor.execute(task);
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeChannelWritabilityChanged() {
/*  432 */     if (invokeHandler()) {
/*      */       try {
/*  434 */         ((ChannelInboundHandler)handler()).channelWritabilityChanged(this);
/*      */       } catch (Throwable t) {
/*  436 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  439 */       fireChannelWritabilityChanged();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress)
/*      */   {
/*  445 */     return bind(localAddress, newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress)
/*      */   {
/*  450 */     return connect(remoteAddress, newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*      */   {
/*  455 */     return connect(remoteAddress, localAddress, newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect()
/*      */   {
/*  460 */     return disconnect(newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture close()
/*      */   {
/*  465 */     return close(newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister()
/*      */   {
/*  470 */     return deregister(newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise)
/*      */   {
/*  475 */     if (localAddress == null) {
/*  476 */       throw new NullPointerException("localAddress");
/*      */     }
/*  478 */     if (isNotValidPromise(promise, false))
/*      */     {
/*  480 */       return promise;
/*      */     }
/*      */     
/*  483 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  484 */     EventExecutor executor = next.executor();
/*  485 */     if (executor.inEventLoop()) {
/*  486 */       next.invokeBind(localAddress, promise);
/*      */     } else {
/*  488 */       safeExecute(executor, new Runnable()
/*      */       {
/*      */ 
/*  491 */         public void run() { next.invokeBind(localAddress, promise); } }, promise, null);
/*      */     }
/*      */     
/*      */ 
/*  495 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeBind(SocketAddress localAddress, ChannelPromise promise) {
/*  499 */     if (invokeHandler()) {
/*      */       try {
/*  501 */         ((ChannelOutboundHandler)handler()).bind(this, localAddress, promise);
/*      */       } catch (Throwable t) {
/*  503 */         notifyOutboundHandlerException(t, promise);
/*      */       }
/*      */     } else {
/*  506 */       bind(localAddress, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*      */   {
/*  512 */     return connect(remoteAddress, null, promise);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise)
/*      */   {
/*  519 */     if (remoteAddress == null) {
/*  520 */       throw new NullPointerException("remoteAddress");
/*      */     }
/*  522 */     if (isNotValidPromise(promise, false))
/*      */     {
/*  524 */       return promise;
/*      */     }
/*      */     
/*  527 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  528 */     EventExecutor executor = next.executor();
/*  529 */     if (executor.inEventLoop()) {
/*  530 */       next.invokeConnect(remoteAddress, localAddress, promise);
/*      */     } else {
/*  532 */       safeExecute(executor, new Runnable()
/*      */       {
/*      */ 
/*  535 */         public void run() { next.invokeConnect(remoteAddress, localAddress, promise); } }, promise, null);
/*      */     }
/*      */     
/*      */ 
/*  539 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeConnect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/*  543 */     if (invokeHandler()) {
/*      */       try {
/*  545 */         ((ChannelOutboundHandler)handler()).connect(this, remoteAddress, localAddress, promise);
/*      */       } catch (Throwable t) {
/*  547 */         notifyOutboundHandlerException(t, promise);
/*      */       }
/*      */     } else {
/*  550 */       connect(remoteAddress, localAddress, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect(final ChannelPromise promise)
/*      */   {
/*  556 */     if (isNotValidPromise(promise, false))
/*      */     {
/*  558 */       return promise;
/*      */     }
/*      */     
/*  561 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  562 */     EventExecutor executor = next.executor();
/*  563 */     if (executor.inEventLoop())
/*      */     {
/*      */ 
/*  566 */       if (!channel().metadata().hasDisconnect()) {
/*  567 */         next.invokeClose(promise);
/*      */       } else {
/*  569 */         next.invokeDisconnect(promise);
/*      */       }
/*      */     } else {
/*  572 */       safeExecute(executor, new Runnable()
/*      */       {
/*      */         public void run() {
/*  575 */           if (!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
/*  576 */             next.invokeClose(promise);
/*      */           } else
/*  578 */             next.invokeDisconnect(promise); } }, promise, null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  583 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeDisconnect(ChannelPromise promise) {
/*  587 */     if (invokeHandler()) {
/*      */       try {
/*  589 */         ((ChannelOutboundHandler)handler()).disconnect(this, promise);
/*      */       } catch (Throwable t) {
/*  591 */         notifyOutboundHandlerException(t, promise);
/*      */       }
/*      */     } else {
/*  594 */       disconnect(promise);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture close(final ChannelPromise promise)
/*      */   {
/*  600 */     if (isNotValidPromise(promise, false))
/*      */     {
/*  602 */       return promise;
/*      */     }
/*      */     
/*  605 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  606 */     EventExecutor executor = next.executor();
/*  607 */     if (executor.inEventLoop()) {
/*  608 */       next.invokeClose(promise);
/*      */     } else {
/*  610 */       safeExecute(executor, new Runnable()
/*      */       {
/*      */ 
/*  613 */         public void run() { next.invokeClose(promise); } }, promise, null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  618 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeClose(ChannelPromise promise) {
/*  622 */     if (invokeHandler()) {
/*      */       try {
/*  624 */         ((ChannelOutboundHandler)handler()).close(this, promise);
/*      */       } catch (Throwable t) {
/*  626 */         notifyOutboundHandlerException(t, promise);
/*      */       }
/*      */     } else {
/*  629 */       close(promise);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister(final ChannelPromise promise)
/*      */   {
/*  635 */     if (isNotValidPromise(promise, false))
/*      */     {
/*  637 */       return promise;
/*      */     }
/*      */     
/*  640 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  641 */     EventExecutor executor = next.executor();
/*  642 */     if (executor.inEventLoop()) {
/*  643 */       next.invokeDeregister(promise);
/*      */     } else {
/*  645 */       safeExecute(executor, new Runnable()
/*      */       {
/*      */ 
/*  648 */         public void run() { next.invokeDeregister(promise); } }, promise, null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  653 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeDeregister(ChannelPromise promise) {
/*  657 */     if (invokeHandler()) {
/*      */       try {
/*  659 */         ((ChannelOutboundHandler)handler()).deregister(this, promise);
/*      */       } catch (Throwable t) {
/*  661 */         notifyOutboundHandlerException(t, promise);
/*      */       }
/*      */     } else {
/*  664 */       deregister(promise);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext read()
/*      */   {
/*  670 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  671 */     EventExecutor executor = next.executor();
/*  672 */     if (executor.inEventLoop()) {
/*  673 */       next.invokeRead();
/*      */     } else {
/*  675 */       Runnable task = next.invokeReadTask;
/*  676 */       if (task == null) {
/*  677 */         next.invokeReadTask = ( = new Runnable()
/*      */         {
/*      */           public void run() {
/*  680 */             next.invokeRead();
/*      */           }
/*      */         });
/*      */       }
/*  684 */       executor.execute(task);
/*      */     }
/*      */     
/*  687 */     return this;
/*      */   }
/*      */   
/*      */   private void invokeRead() {
/*  691 */     if (invokeHandler()) {
/*      */       try {
/*  693 */         ((ChannelOutboundHandler)handler()).read(this);
/*      */       } catch (Throwable t) {
/*  695 */         notifyHandlerException(t);
/*      */       }
/*      */     } else {
/*  698 */       read();
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg)
/*      */   {
/*  704 */     return write(msg, newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg, ChannelPromise promise)
/*      */   {
/*  709 */     if (msg == null) {
/*  710 */       throw new NullPointerException("msg");
/*      */     }
/*      */     try
/*      */     {
/*  714 */       if (isNotValidPromise(promise, true)) {
/*  715 */         ReferenceCountUtil.release(msg);
/*      */         
/*  717 */         return promise;
/*      */       }
/*      */     } catch (RuntimeException e) {
/*  720 */       ReferenceCountUtil.release(msg);
/*  721 */       throw e;
/*      */     }
/*  723 */     write(msg, false, promise);
/*      */     
/*  725 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeWrite(Object msg, ChannelPromise promise) {
/*  729 */     if (invokeHandler()) {
/*  730 */       invokeWrite0(msg, promise);
/*      */     } else {
/*  732 */       write(msg, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeWrite0(Object msg, ChannelPromise promise) {
/*      */     try {
/*  738 */       ((ChannelOutboundHandler)handler()).write(this, msg, promise);
/*      */     } catch (Throwable t) {
/*  740 */       notifyOutboundHandlerException(t, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelHandlerContext flush()
/*      */   {
/*  746 */     final AbstractChannelHandlerContext next = findContextOutbound();
/*  747 */     EventExecutor executor = next.executor();
/*  748 */     if (executor.inEventLoop()) {
/*  749 */       next.invokeFlush();
/*      */     } else {
/*  751 */       Runnable task = next.invokeFlushTask;
/*  752 */       if (task == null) {
/*  753 */         next.invokeFlushTask = ( = new Runnable()
/*      */         {
/*      */           public void run() {
/*  756 */             next.invokeFlush();
/*      */           }
/*      */         });
/*      */       }
/*  760 */       safeExecute(executor, task, channel().voidPromise(), null);
/*      */     }
/*      */     
/*  763 */     return this;
/*      */   }
/*      */   
/*      */   private void invokeFlush() {
/*  767 */     if (invokeHandler()) {
/*  768 */       invokeFlush0();
/*      */     } else {
/*  770 */       flush();
/*      */     }
/*      */   }
/*      */   
/*      */   private void invokeFlush0() {
/*      */     try {
/*  776 */       ((ChannelOutboundHandler)handler()).flush(this);
/*      */     } catch (Throwable t) {
/*  778 */       notifyHandlerException(t);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*      */   {
/*  784 */     if (msg == null) {
/*  785 */       throw new NullPointerException("msg");
/*      */     }
/*      */     
/*  788 */     if (isNotValidPromise(promise, true)) {
/*  789 */       ReferenceCountUtil.release(msg);
/*      */       
/*  791 */       return promise;
/*      */     }
/*      */     
/*  794 */     write(msg, true, promise);
/*      */     
/*  796 */     return promise;
/*      */   }
/*      */   
/*      */   private void invokeWriteAndFlush(Object msg, ChannelPromise promise) {
/*  800 */     if (invokeHandler()) {
/*  801 */       invokeWrite0(msg, promise);
/*  802 */       invokeFlush0();
/*      */     } else {
/*  804 */       writeAndFlush(msg, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   private void write(Object msg, boolean flush, ChannelPromise promise) {
/*  809 */     AbstractChannelHandlerContext next = findContextOutbound();
/*  810 */     Object m = this.pipeline.touch(msg, next);
/*  811 */     EventExecutor executor = next.executor();
/*  812 */     if (executor.inEventLoop()) {
/*  813 */       if (flush) {
/*  814 */         next.invokeWriteAndFlush(m, promise);
/*      */       } else
/*  816 */         next.invokeWrite(m, promise);
/*      */     } else {
/*      */       AbstractWriteTask task;
/*      */       AbstractWriteTask task;
/*  820 */       if (flush) {
/*  821 */         task = WriteAndFlushTask.newInstance(next, m, promise);
/*      */       } else {
/*  823 */         task = WriteTask.newInstance(next, m, promise);
/*      */       }
/*  825 */       safeExecute(executor, task, promise, m);
/*      */     }
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg)
/*      */   {
/*  831 */     return writeAndFlush(msg, newPromise());
/*      */   }
/*      */   
/*      */ 
/*      */   private static void notifyOutboundHandlerException(Throwable cause, ChannelPromise promise)
/*      */   {
/*  837 */     PromiseNotificationUtil.tryFailure(promise, cause, (promise instanceof VoidChannelPromise) ? null : logger);
/*      */   }
/*      */   
/*      */   private void notifyHandlerException(Throwable cause) {
/*  841 */     if (inExceptionCaught(cause)) {
/*  842 */       if (logger.isWarnEnabled()) {
/*  843 */         logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause);
/*      */       }
/*      */       
/*      */ 
/*  847 */       return;
/*      */     }
/*      */     
/*  850 */     invokeExceptionCaught(cause);
/*      */   }
/*      */   
/*      */   private static boolean inExceptionCaught(Throwable cause) {
/*      */     do {
/*  855 */       StackTraceElement[] trace = cause.getStackTrace();
/*  856 */       if (trace != null) {
/*  857 */         for (StackTraceElement t : trace) {
/*  858 */           if (t == null) {
/*      */             break;
/*      */           }
/*  861 */           if ("exceptionCaught".equals(t.getMethodName())) {
/*  862 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  867 */       cause = cause.getCause();
/*  868 */     } while (cause != null);
/*      */     
/*  870 */     return false;
/*      */   }
/*      */   
/*      */   public ChannelPromise newPromise()
/*      */   {
/*  875 */     return new DefaultChannelPromise(channel(), executor());
/*      */   }
/*      */   
/*      */   public ChannelProgressivePromise newProgressivePromise()
/*      */   {
/*  880 */     return new DefaultChannelProgressivePromise(channel(), executor());
/*      */   }
/*      */   
/*      */   public ChannelFuture newSucceededFuture()
/*      */   {
/*  885 */     ChannelFuture succeededFuture = this.succeededFuture;
/*  886 */     if (succeededFuture == null) {
/*  887 */       this.succeededFuture = (succeededFuture = new SucceededChannelFuture(channel(), executor()));
/*      */     }
/*  889 */     return succeededFuture;
/*      */   }
/*      */   
/*      */   public ChannelFuture newFailedFuture(Throwable cause)
/*      */   {
/*  894 */     return new FailedChannelFuture(channel(), executor(), cause);
/*      */   }
/*      */   
/*      */   private boolean isNotValidPromise(ChannelPromise promise, boolean allowVoidPromise) {
/*  898 */     if (promise == null) {
/*  899 */       throw new NullPointerException("promise");
/*      */     }
/*      */     
/*  902 */     if (promise.isDone())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  907 */       if (promise.isCancelled()) {
/*  908 */         return true;
/*      */       }
/*  910 */       throw new IllegalArgumentException("promise already done: " + promise);
/*      */     }
/*      */     
/*  913 */     if (promise.channel() != channel()) {
/*  914 */       throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", new Object[] {promise
/*  915 */         .channel(), channel() }));
/*      */     }
/*      */     
/*  918 */     if (promise.getClass() == DefaultChannelPromise.class) {
/*  919 */       return false;
/*      */     }
/*      */     
/*  922 */     if ((!allowVoidPromise) && ((promise instanceof VoidChannelPromise)))
/*      */     {
/*  924 */       throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
/*      */     }
/*      */     
/*  927 */     if ((promise instanceof AbstractChannel.CloseFuture))
/*      */     {
/*  929 */       throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
/*      */     }
/*  931 */     return false;
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext findContextInbound() {
/*  935 */     AbstractChannelHandlerContext ctx = this;
/*      */     do {
/*  937 */       ctx = ctx.next;
/*  938 */     } while (!ctx.inbound);
/*  939 */     return ctx;
/*      */   }
/*      */   
/*      */   private AbstractChannelHandlerContext findContextOutbound() {
/*  943 */     AbstractChannelHandlerContext ctx = this;
/*      */     do {
/*  945 */       ctx = ctx.prev;
/*  946 */     } while (!ctx.outbound);
/*  947 */     return ctx;
/*      */   }
/*      */   
/*      */   public ChannelPromise voidPromise()
/*      */   {
/*  952 */     return channel().voidPromise();
/*      */   }
/*      */   
/*      */   final void setRemoved() {
/*  956 */     this.handlerState = 3;
/*      */   }
/*      */   
/*      */   final void setAddComplete() {
/*      */     for (;;) {
/*  961 */       int oldState = this.handlerState;
/*      */       
/*      */ 
/*      */ 
/*  965 */       if ((oldState == 3) || (HANDLER_STATE_UPDATER.compareAndSet(this, oldState, 2))) {
/*  966 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   final void setAddPending() {
/*  972 */     boolean updated = HANDLER_STATE_UPDATER.compareAndSet(this, 0, 1);
/*  973 */     assert (updated);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean invokeHandler()
/*      */   {
/*  986 */     int handlerState = this.handlerState;
/*  987 */     return (handlerState == 2) || ((!this.ordered) && (handlerState == 1));
/*      */   }
/*      */   
/*      */   public boolean isRemoved()
/*      */   {
/*  992 */     return this.handlerState == 3;
/*      */   }
/*      */   
/*      */   public <T> Attribute<T> attr(AttributeKey<T> key)
/*      */   {
/*  997 */     return channel().attr(key);
/*      */   }
/*      */   
/*      */   public <T> boolean hasAttr(AttributeKey<T> key)
/*      */   {
/* 1002 */     return channel().hasAttr(key);
/*      */   }
/*      */   
/*      */   private static void safeExecute(EventExecutor executor, Runnable runnable, ChannelPromise promise, Object msg) {
/*      */     try {
/* 1007 */       executor.execute(runnable);
/*      */     } catch (Throwable cause) {
/*      */       try {
/* 1010 */         promise.setFailure(cause);
/*      */       } finally {
/* 1012 */         if (msg != null) {
/* 1013 */           ReferenceCountUtil.release(msg);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public String toHintString()
/*      */   {
/* 1021 */     return '\'' + this.name + "' will handle the message from this point.";
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1026 */     return StringUtil.simpleClassName(ChannelHandlerContext.class) + '(' + this.name + ", " + channel() + ')';
/*      */   }
/*      */   
/*      */   static abstract class AbstractWriteTask
/*      */     implements Runnable
/*      */   {
/* 1032 */     private static final boolean ESTIMATE_TASK_SIZE_ON_SUBMIT = SystemPropertyUtil.getBoolean("io.netty.transport.estimateSizeOnSubmit", true);
/*      */     
/*      */ 
/*      */ 
/* 1036 */     private static final int WRITE_TASK_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.writeTaskSizeOverhead", 48);
/*      */     
/*      */     private final Recycler.Handle<AbstractWriteTask> handle;
/*      */     private AbstractChannelHandlerContext ctx;
/*      */     private Object msg;
/*      */     private ChannelPromise promise;
/*      */     private int size;
/*      */     
/*      */     private AbstractWriteTask(Recycler.Handle<? extends AbstractWriteTask> handle)
/*      */     {
/* 1046 */       this.handle = handle;
/*      */     }
/*      */     
/*      */     protected static void init(AbstractWriteTask task, AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*      */     {
/* 1051 */       task.ctx = ctx;
/* 1052 */       task.msg = msg;
/* 1053 */       task.promise = promise;
/*      */       
/* 1055 */       if (ESTIMATE_TASK_SIZE_ON_SUBMIT) {
/* 1056 */         task.size = (ctx.pipeline.estimatorHandle().size(msg) + WRITE_TASK_OVERHEAD);
/* 1057 */         ctx.pipeline.incrementPendingOutboundBytes(task.size);
/*      */       } else {
/* 1059 */         task.size = 0;
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public final void run()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: getstatic 7	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:ESTIMATE_TASK_SIZE_ON_SUBMIT	Z
/*      */       //   3: ifeq +18 -> 21
/*      */       //   6: aload_0
/*      */       //   7: getfield 4	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:ctx	Lio/netty/channel/AbstractChannelHandlerContext;
/*      */       //   10: invokestatic 8	io/netty/channel/AbstractChannelHandlerContext:access$1800	(Lio/netty/channel/AbstractChannelHandlerContext;)Lio/netty/channel/DefaultChannelPipeline;
/*      */       //   13: aload_0
/*      */       //   14: getfield 12	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:size	I
/*      */       //   17: i2l
/*      */       //   18: invokevirtual 14	io/netty/channel/DefaultChannelPipeline:decrementPendingOutboundBytes	(J)V
/*      */       //   21: aload_0
/*      */       //   22: aload_0
/*      */       //   23: getfield 4	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:ctx	Lio/netty/channel/AbstractChannelHandlerContext;
/*      */       //   26: aload_0
/*      */       //   27: getfield 5	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:msg	Ljava/lang/Object;
/*      */       //   30: aload_0
/*      */       //   31: getfield 6	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:promise	Lio/netty/channel/ChannelPromise;
/*      */       //   34: invokevirtual 15	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:write	(Lio/netty/channel/AbstractChannelHandlerContext;Ljava/lang/Object;Lio/netty/channel/ChannelPromise;)V
/*      */       //   37: aload_0
/*      */       //   38: aconst_null
/*      */       //   39: putfield 4	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:ctx	Lio/netty/channel/AbstractChannelHandlerContext;
/*      */       //   42: aload_0
/*      */       //   43: aconst_null
/*      */       //   44: putfield 5	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:msg	Ljava/lang/Object;
/*      */       //   47: aload_0
/*      */       //   48: aconst_null
/*      */       //   49: putfield 6	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:promise	Lio/netty/channel/ChannelPromise;
/*      */       //   52: aload_0
/*      */       //   53: getfield 3	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:handle	Lio/netty/util/Recycler$Handle;
/*      */       //   56: aload_0
/*      */       //   57: invokeinterface 16 2 0
/*      */       //   62: goto +31 -> 93
/*      */       //   65: astore_1
/*      */       //   66: aload_0
/*      */       //   67: aconst_null
/*      */       //   68: putfield 4	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:ctx	Lio/netty/channel/AbstractChannelHandlerContext;
/*      */       //   71: aload_0
/*      */       //   72: aconst_null
/*      */       //   73: putfield 5	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:msg	Ljava/lang/Object;
/*      */       //   76: aload_0
/*      */       //   77: aconst_null
/*      */       //   78: putfield 6	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:promise	Lio/netty/channel/ChannelPromise;
/*      */       //   81: aload_0
/*      */       //   82: getfield 3	io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask:handle	Lio/netty/util/Recycler$Handle;
/*      */       //   85: aload_0
/*      */       //   86: invokeinterface 16 2 0
/*      */       //   91: aload_1
/*      */       //   92: athrow
/*      */       //   93: return
/*      */       // Line number table:
/*      */       //   Java source line #1067	-> byte code offset #0
/*      */       //   Java source line #1068	-> byte code offset #6
/*      */       //   Java source line #1070	-> byte code offset #21
/*      */       //   Java source line #1073	-> byte code offset #37
/*      */       //   Java source line #1074	-> byte code offset #42
/*      */       //   Java source line #1075	-> byte code offset #47
/*      */       //   Java source line #1076	-> byte code offset #52
/*      */       //   Java source line #1077	-> byte code offset #62
/*      */       //   Java source line #1073	-> byte code offset #65
/*      */       //   Java source line #1074	-> byte code offset #71
/*      */       //   Java source line #1075	-> byte code offset #76
/*      */       //   Java source line #1076	-> byte code offset #81
/*      */       //   Java source line #1077	-> byte code offset #91
/*      */       //   Java source line #1078	-> byte code offset #93
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	94	0	this	AbstractWriteTask
/*      */       //   65	27	1	localObject	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   0	37	65	finally
/*      */     }
/*      */     
/*      */     protected void write(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*      */     {
/* 1081 */       ctx.invokeWrite(msg, promise);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WriteTask extends AbstractChannelHandlerContext.AbstractWriteTask implements SingleThreadEventLoop.NonWakeupRunnable
/*      */   {
/* 1087 */     private static final Recycler<WriteTask> RECYCLER = new Recycler()
/*      */     {
/*      */       protected AbstractChannelHandlerContext.WriteTask newObject(Recycler.Handle<AbstractChannelHandlerContext.WriteTask> handle) {
/* 1090 */         return new AbstractChannelHandlerContext.WriteTask(handle, null);
/*      */       }
/*      */     };
/*      */     
/*      */     private static WriteTask newInstance(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*      */     {
/* 1096 */       WriteTask task = (WriteTask)RECYCLER.get();
/* 1097 */       init(task, ctx, msg, promise);
/* 1098 */       return task;
/*      */     }
/*      */     
/*      */     private WriteTask(Recycler.Handle<WriteTask> handle) {
/* 1102 */       super(null);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WriteAndFlushTask extends AbstractChannelHandlerContext.AbstractWriteTask
/*      */   {
/* 1108 */     private static final Recycler<WriteAndFlushTask> RECYCLER = new Recycler()
/*      */     {
/*      */       protected AbstractChannelHandlerContext.WriteAndFlushTask newObject(Recycler.Handle<AbstractChannelHandlerContext.WriteAndFlushTask> handle) {
/* 1111 */         return new AbstractChannelHandlerContext.WriteAndFlushTask(handle, null);
/*      */       }
/*      */     };
/*      */     
/*      */     private static WriteAndFlushTask newInstance(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*      */     {
/* 1117 */       WriteAndFlushTask task = (WriteAndFlushTask)RECYCLER.get();
/* 1118 */       init(task, ctx, msg, promise);
/* 1119 */       return task;
/*      */     }
/*      */     
/*      */     private WriteAndFlushTask(Recycler.Handle<WriteAndFlushTask> handle) {
/* 1123 */       super(null);
/*      */     }
/*      */     
/*      */     public void write(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*      */     {
/* 1128 */       super.write(ctx, msg, promise);
/* 1129 */       ctx.invokeFlush();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\AbstractChannelHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */