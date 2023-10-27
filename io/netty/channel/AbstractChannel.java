/*      */ package io.netty.channel;
/*      */ 
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.channel.socket.ChannelOutputShutdownEvent;
/*      */ import io.netty.channel.socket.ChannelOutputShutdownException;
/*      */ import io.netty.util.DefaultAttributeMap;
/*      */ import io.netty.util.ReferenceCountUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.net.ConnectException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.NoRouteToHostException;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketException;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.NotYetConnectedException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractChannel
/*      */   extends DefaultAttributeMap
/*      */   implements Channel
/*      */ {
/*   45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
/*      */   
/*   47 */   private static final ClosedChannelException FLUSH0_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "flush0()");
/*      */   
/*   49 */   private static final ClosedChannelException ENSURE_OPEN_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "ensureOpen(...)");
/*      */   
/*   51 */   private static final ClosedChannelException CLOSE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "close(...)");
/*      */   
/*   53 */   private static final ClosedChannelException WRITE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "write(...)");
/*      */   
/*   55 */   private static final NotYetConnectedException FLUSH0_NOT_YET_CONNECTED_EXCEPTION = (NotYetConnectedException)ThrowableUtil.unknownStackTrace(new NotYetConnectedException(), AbstractUnsafe.class, "flush0()");
/*      */   
/*      */   private final Channel parent;
/*      */   
/*      */   private final ChannelId id;
/*      */   private final Channel.Unsafe unsafe;
/*      */   private final DefaultChannelPipeline pipeline;
/*   62 */   private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(this, false);
/*   63 */   private final CloseFuture closeFuture = new CloseFuture(this);
/*      */   
/*      */   private volatile SocketAddress localAddress;
/*      */   
/*      */   private volatile SocketAddress remoteAddress;
/*      */   
/*      */   private volatile EventLoop eventLoop;
/*      */   
/*      */   private volatile boolean registered;
/*      */   
/*      */   private boolean closeInitiated;
/*      */   
/*      */   private boolean strValActive;
/*      */   
/*      */   private String strVal;
/*      */   
/*      */ 
/*      */   protected AbstractChannel(Channel parent)
/*      */   {
/*   82 */     this.parent = parent;
/*   83 */     this.id = newId();
/*   84 */     this.unsafe = newUnsafe();
/*   85 */     this.pipeline = newChannelPipeline();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AbstractChannel(Channel parent, ChannelId id)
/*      */   {
/*   95 */     this.parent = parent;
/*   96 */     this.id = id;
/*   97 */     this.unsafe = newUnsafe();
/*   98 */     this.pipeline = newChannelPipeline();
/*      */   }
/*      */   
/*      */   public final ChannelId id()
/*      */   {
/*  103 */     return this.id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ChannelId newId()
/*      */   {
/*  111 */     return DefaultChannelId.newInstance();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected DefaultChannelPipeline newChannelPipeline()
/*      */   {
/*  118 */     return new DefaultChannelPipeline(this);
/*      */   }
/*      */   
/*      */   public boolean isWritable()
/*      */   {
/*  123 */     ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
/*  124 */     return (buf != null) && (buf.isWritable());
/*      */   }
/*      */   
/*      */   public long bytesBeforeUnwritable()
/*      */   {
/*  129 */     ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
/*      */     
/*      */ 
/*  132 */     return buf != null ? buf.bytesBeforeUnwritable() : 0L;
/*      */   }
/*      */   
/*      */   public long bytesBeforeWritable()
/*      */   {
/*  137 */     ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
/*      */     
/*      */ 
/*  140 */     return buf != null ? buf.bytesBeforeWritable() : Long.MAX_VALUE;
/*      */   }
/*      */   
/*      */   public Channel parent()
/*      */   {
/*  145 */     return this.parent;
/*      */   }
/*      */   
/*      */   public ChannelPipeline pipeline()
/*      */   {
/*  150 */     return this.pipeline;
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc()
/*      */   {
/*  155 */     return config().getAllocator();
/*      */   }
/*      */   
/*      */   public EventLoop eventLoop()
/*      */   {
/*  160 */     EventLoop eventLoop = this.eventLoop;
/*  161 */     if (eventLoop == null) {
/*  162 */       throw new IllegalStateException("channel not registered to an event loop");
/*      */     }
/*  164 */     return eventLoop;
/*      */   }
/*      */   
/*      */   public SocketAddress localAddress()
/*      */   {
/*  169 */     SocketAddress localAddress = this.localAddress;
/*  170 */     if (localAddress == null) {
/*      */       try {
/*  172 */         this.localAddress = (localAddress = unsafe().localAddress());
/*      */       }
/*      */       catch (Throwable t) {
/*  175 */         return null;
/*      */       }
/*      */     }
/*  178 */     return localAddress;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected void invalidateLocalAddress()
/*      */   {
/*  186 */     this.localAddress = null;
/*      */   }
/*      */   
/*      */   public SocketAddress remoteAddress()
/*      */   {
/*  191 */     SocketAddress remoteAddress = this.remoteAddress;
/*  192 */     if (remoteAddress == null) {
/*      */       try {
/*  194 */         this.remoteAddress = (remoteAddress = unsafe().remoteAddress());
/*      */       }
/*      */       catch (Throwable t) {
/*  197 */         return null;
/*      */       }
/*      */     }
/*  200 */     return remoteAddress;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected void invalidateRemoteAddress()
/*      */   {
/*  208 */     this.remoteAddress = null;
/*      */   }
/*      */   
/*      */   public boolean isRegistered()
/*      */   {
/*  213 */     return this.registered;
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress)
/*      */   {
/*  218 */     return this.pipeline.bind(localAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress)
/*      */   {
/*  223 */     return this.pipeline.connect(remoteAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
/*      */   {
/*  228 */     return this.pipeline.connect(remoteAddress, localAddress);
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect()
/*      */   {
/*  233 */     return this.pipeline.disconnect();
/*      */   }
/*      */   
/*      */   public ChannelFuture close()
/*      */   {
/*  238 */     return this.pipeline.close();
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister()
/*      */   {
/*  243 */     return this.pipeline.deregister();
/*      */   }
/*      */   
/*      */   public Channel flush()
/*      */   {
/*  248 */     this.pipeline.flush();
/*  249 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
/*      */   {
/*  254 */     return this.pipeline.bind(localAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
/*      */   {
/*  259 */     return this.pipeline.connect(remoteAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*      */   {
/*  264 */     return this.pipeline.connect(remoteAddress, localAddress, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture disconnect(ChannelPromise promise)
/*      */   {
/*  269 */     return this.pipeline.disconnect(promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture close(ChannelPromise promise)
/*      */   {
/*  274 */     return this.pipeline.close(promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture deregister(ChannelPromise promise)
/*      */   {
/*  279 */     return this.pipeline.deregister(promise);
/*      */   }
/*      */   
/*      */   public Channel read()
/*      */   {
/*  284 */     this.pipeline.read();
/*  285 */     return this;
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg)
/*      */   {
/*  290 */     return this.pipeline.write(msg);
/*      */   }
/*      */   
/*      */   public ChannelFuture write(Object msg, ChannelPromise promise)
/*      */   {
/*  295 */     return this.pipeline.write(msg, promise);
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg)
/*      */   {
/*  300 */     return this.pipeline.writeAndFlush(msg);
/*      */   }
/*      */   
/*      */   public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
/*      */   {
/*  305 */     return this.pipeline.writeAndFlush(msg, promise);
/*      */   }
/*      */   
/*      */   public ChannelPromise newPromise()
/*      */   {
/*  310 */     return this.pipeline.newPromise();
/*      */   }
/*      */   
/*      */   public ChannelProgressivePromise newProgressivePromise()
/*      */   {
/*  315 */     return this.pipeline.newProgressivePromise();
/*      */   }
/*      */   
/*      */   public ChannelFuture newSucceededFuture()
/*      */   {
/*  320 */     return this.pipeline.newSucceededFuture();
/*      */   }
/*      */   
/*      */   public ChannelFuture newFailedFuture(Throwable cause)
/*      */   {
/*  325 */     return this.pipeline.newFailedFuture(cause);
/*      */   }
/*      */   
/*      */   public ChannelFuture closeFuture()
/*      */   {
/*  330 */     return this.closeFuture;
/*      */   }
/*      */   
/*      */   public Channel.Unsafe unsafe()
/*      */   {
/*  335 */     return this.unsafe;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract AbstractUnsafe newUnsafe();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int hashCode()
/*      */   {
/*  348 */     return this.id.hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean equals(Object o)
/*      */   {
/*  357 */     return this == o;
/*      */   }
/*      */   
/*      */   public final int compareTo(Channel o)
/*      */   {
/*  362 */     if (this == o) {
/*  363 */       return 0;
/*      */     }
/*      */     
/*  366 */     return id().compareTo(o.id());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  377 */     boolean active = isActive();
/*  378 */     if ((this.strValActive == active) && (this.strVal != null)) {
/*  379 */       return this.strVal;
/*      */     }
/*      */     
/*  382 */     SocketAddress remoteAddr = remoteAddress();
/*  383 */     SocketAddress localAddr = localAddress();
/*  384 */     if (remoteAddr != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  393 */       StringBuilder buf = new StringBuilder(96).append("[id: 0x").append(this.id.asShortText()).append(", L:").append(localAddr).append(active ? " - " : " ! ").append("R:").append(remoteAddr).append(']');
/*  394 */       this.strVal = buf.toString();
/*  395 */     } else if (localAddr != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  401 */       StringBuilder buf = new StringBuilder(64).append("[id: 0x").append(this.id.asShortText()).append(", L:").append(localAddr).append(']');
/*  402 */       this.strVal = buf.toString();
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  407 */       StringBuilder buf = new StringBuilder(16).append("[id: 0x").append(this.id.asShortText()).append(']');
/*  408 */       this.strVal = buf.toString();
/*      */     }
/*      */     
/*  411 */     this.strValActive = active;
/*  412 */     return this.strVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  417 */   public final ChannelPromise voidPromise() { return this.pipeline.voidPromise(); }
/*      */   
/*      */   protected abstract boolean isCompatible(EventLoop paramEventLoop);
/*      */   
/*      */   protected abstract SocketAddress localAddress0();
/*      */   
/*      */   protected abstract SocketAddress remoteAddress0();
/*      */   protected abstract class AbstractUnsafe implements Channel.Unsafe { protected AbstractUnsafe() {}
/*  425 */     private volatile ChannelOutboundBuffer outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
/*      */     private RecvByteBufAllocator.Handle recvHandle;
/*      */     private boolean inFlush0;
/*      */     
/*  429 */     private boolean neverRegistered = true;
/*      */     
/*      */     private void assertEventLoop() {
/*  432 */       assert ((!AbstractChannel.this.registered) || (AbstractChannel.this.eventLoop.inEventLoop())); }
/*      */     
/*      */ 
/*      */     public RecvByteBufAllocator.Handle recvBufAllocHandle()
/*      */     {
/*  437 */       if (this.recvHandle == null) {
/*  438 */         this.recvHandle = AbstractChannel.this.config().getRecvByteBufAllocator().newHandle();
/*      */       }
/*  440 */       return this.recvHandle;
/*      */     }
/*      */     
/*      */     public final ChannelOutboundBuffer outboundBuffer()
/*      */     {
/*  445 */       return this.outboundBuffer;
/*      */     }
/*      */     
/*      */     public final SocketAddress localAddress()
/*      */     {
/*  450 */       return AbstractChannel.this.localAddress0();
/*      */     }
/*      */     
/*      */     public final SocketAddress remoteAddress()
/*      */     {
/*  455 */       return AbstractChannel.this.remoteAddress0();
/*      */     }
/*      */     
/*      */     public final void register(EventLoop eventLoop, final ChannelPromise promise)
/*      */     {
/*  460 */       if (eventLoop == null) {
/*  461 */         throw new NullPointerException("eventLoop");
/*      */       }
/*  463 */       if (AbstractChannel.this.isRegistered()) {
/*  464 */         promise.setFailure(new IllegalStateException("registered to an event loop already"));
/*  465 */         return;
/*      */       }
/*  467 */       if (!AbstractChannel.this.isCompatible(eventLoop)) {
/*  468 */         promise.setFailure(new IllegalStateException("incompatible event loop type: " + eventLoop
/*  469 */           .getClass().getName()));
/*  470 */         return;
/*      */       }
/*      */       
/*  473 */       AbstractChannel.this.eventLoop = eventLoop;
/*      */       
/*  475 */       if (eventLoop.inEventLoop()) {
/*  476 */         register0(promise);
/*      */       } else {
/*      */         try {
/*  479 */           eventLoop.execute(new Runnable()
/*      */           {
/*      */             public void run() {
/*  482 */               AbstractChannel.AbstractUnsafe.this.register0(promise);
/*      */             }
/*      */           });
/*      */         } catch (Throwable t) {
/*  486 */           AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, t);
/*      */           
/*      */ 
/*  489 */           closeForcibly();
/*  490 */           AbstractChannel.this.closeFuture.setClosed();
/*  491 */           safeSetFailure(promise, t);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void register0(ChannelPromise promise)
/*      */     {
/*      */       try
/*      */       {
/*  500 */         if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/*  501 */           return;
/*      */         }
/*  503 */         boolean firstRegistration = this.neverRegistered;
/*  504 */         AbstractChannel.this.doRegister();
/*  505 */         this.neverRegistered = false;
/*  506 */         AbstractChannel.this.registered = true;
/*      */         
/*      */ 
/*      */ 
/*  510 */         AbstractChannel.this.pipeline.invokeHandlerAddedIfNeeded();
/*      */         
/*  512 */         safeSetSuccess(promise);
/*  513 */         AbstractChannel.this.pipeline.fireChannelRegistered();
/*      */         
/*      */ 
/*  516 */         if (AbstractChannel.this.isActive()) {
/*  517 */           if (firstRegistration) {
/*  518 */             AbstractChannel.this.pipeline.fireChannelActive();
/*  519 */           } else if (AbstractChannel.this.config().isAutoRead())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  524 */             beginRead();
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Throwable t) {
/*  529 */         closeForcibly();
/*  530 */         AbstractChannel.this.closeFuture.setClosed();
/*  531 */         safeSetFailure(promise, t);
/*      */       }
/*      */     }
/*      */     
/*      */     public final void bind(SocketAddress localAddress, ChannelPromise promise)
/*      */     {
/*  537 */       assertEventLoop();
/*      */       
/*  539 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/*  540 */         return;
/*      */       }
/*      */       
/*      */ 
/*  544 */       if ((Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST))) && ((localAddress instanceof InetSocketAddress)))
/*      */       {
/*  546 */         if ((!((InetSocketAddress)localAddress).getAddress().isAnyLocalAddress()) && 
/*  547 */           (!PlatformDependent.isWindows()) && (!PlatformDependent.maybeSuperUser()))
/*      */         {
/*      */ 
/*  550 */           AbstractChannel.logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested.");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  556 */       boolean wasActive = AbstractChannel.this.isActive();
/*      */       try {
/*  558 */         AbstractChannel.this.doBind(localAddress);
/*      */       } catch (Throwable t) {
/*  560 */         safeSetFailure(promise, t);
/*  561 */         closeIfClosed();
/*  562 */         return;
/*      */       }
/*      */       
/*  565 */       if ((!wasActive) && (AbstractChannel.this.isActive())) {
/*  566 */         invokeLater(new Runnable()
/*      */         {
/*      */           public void run() {
/*  569 */             AbstractChannel.this.pipeline.fireChannelActive();
/*      */           }
/*      */         });
/*      */       }
/*      */       
/*  574 */       safeSetSuccess(promise);
/*      */     }
/*      */     
/*      */     public final void disconnect(ChannelPromise promise)
/*      */     {
/*  579 */       assertEventLoop();
/*      */       
/*  581 */       if (!promise.setUncancellable()) {
/*  582 */         return;
/*      */       }
/*      */       
/*  585 */       boolean wasActive = AbstractChannel.this.isActive();
/*      */       try {
/*  587 */         AbstractChannel.this.doDisconnect();
/*      */       } catch (Throwable t) {
/*  589 */         safeSetFailure(promise, t);
/*  590 */         closeIfClosed();
/*  591 */         return;
/*      */       }
/*      */       
/*  594 */       if ((wasActive) && (!AbstractChannel.this.isActive())) {
/*  595 */         invokeLater(new Runnable()
/*      */         {
/*      */           public void run() {
/*  598 */             AbstractChannel.this.pipeline.fireChannelInactive();
/*      */           }
/*      */         });
/*      */       }
/*      */       
/*  603 */       safeSetSuccess(promise);
/*  604 */       closeIfClosed();
/*      */     }
/*      */     
/*      */     public final void close(ChannelPromise promise)
/*      */     {
/*  609 */       assertEventLoop();
/*      */       
/*  611 */       close(promise, AbstractChannel.CLOSE_CLOSED_CHANNEL_EXCEPTION, AbstractChannel.CLOSE_CLOSED_CHANNEL_EXCEPTION, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final void shutdownOutput(ChannelPromise promise)
/*      */     {
/*  620 */       assertEventLoop();
/*  621 */       shutdownOutput(promise, null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void shutdownOutput(final ChannelPromise promise, Throwable cause)
/*      */     {
/*  630 */       if (!promise.setUncancellable()) {
/*  631 */         return;
/*      */       }
/*      */       
/*  634 */       final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  635 */       if (outboundBuffer == null) {
/*  636 */         promise.setFailure(AbstractChannel.CLOSE_CLOSED_CHANNEL_EXCEPTION);
/*  637 */         return;
/*      */       }
/*  639 */       this.outboundBuffer = null;
/*      */       
/*  641 */       final Throwable shutdownCause = cause == null ? new ChannelOutputShutdownException("Channel output shutdown") : new ChannelOutputShutdownException("Channel output shutdown", cause);
/*      */       
/*      */ 
/*  644 */       Executor closeExecutor = prepareToClose();
/*  645 */       if (closeExecutor != null) {
/*  646 */         closeExecutor.execute(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*      */             try {
/*  651 */               AbstractChannel.this.doShutdownOutput();
/*  652 */               promise.setSuccess();
/*      */             } catch (Throwable err) {
/*  654 */               promise.setFailure(err);
/*      */             }
/*      */             finally {
/*  657 */               AbstractChannel.this.eventLoop().execute(new Runnable()
/*      */               {
/*      */                 public void run() {
/*  660 */                   AbstractChannel.AbstractUnsafe.this.closeOutboundBufferForShutdown(AbstractChannel.this.pipeline, AbstractChannel.AbstractUnsafe.4.this.val$outboundBuffer, AbstractChannel.AbstractUnsafe.4.this.val$shutdownCause);
/*      */                 }
/*      */               });
/*      */             }
/*      */           }
/*      */         });
/*      */       } else {
/*      */         try
/*      */         {
/*  669 */           AbstractChannel.this.doShutdownOutput();
/*  670 */           promise.setSuccess();
/*      */         } catch (Throwable err) {
/*  672 */           promise.setFailure(err);
/*      */         } finally {
/*  674 */           closeOutboundBufferForShutdown(AbstractChannel.this.pipeline, outboundBuffer, shutdownCause);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void closeOutboundBufferForShutdown(ChannelPipeline pipeline, ChannelOutboundBuffer buffer, Throwable cause)
/*      */     {
/*  681 */       buffer.failFlushed(cause, false);
/*  682 */       buffer.close(cause, true);
/*  683 */       pipeline.fireUserEventTriggered(ChannelOutputShutdownEvent.INSTANCE);
/*      */     }
/*      */     
/*      */     private void close(final ChannelPromise promise, final Throwable cause, final ClosedChannelException closeCause, final boolean notify)
/*      */     {
/*  688 */       if (!promise.setUncancellable()) {
/*  689 */         return;
/*      */       }
/*      */       
/*  692 */       if (AbstractChannel.this.closeInitiated) {
/*  693 */         if (AbstractChannel.this.closeFuture.isDone())
/*      */         {
/*  695 */           safeSetSuccess(promise);
/*  696 */         } else if (!(promise instanceof VoidChannelPromise))
/*      */         {
/*  698 */           AbstractChannel.this.closeFuture.addListener(new ChannelFutureListener()
/*      */           {
/*      */             public void operationComplete(ChannelFuture future) throws Exception {
/*  701 */               promise.setSuccess();
/*      */             }
/*      */           });
/*      */         }
/*  705 */         return;
/*      */       }
/*      */       
/*  708 */       AbstractChannel.this.closeInitiated = true;
/*      */       
/*  710 */       final boolean wasActive = AbstractChannel.this.isActive();
/*  711 */       final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  712 */       this.outboundBuffer = null;
/*  713 */       Executor closeExecutor = prepareToClose();
/*  714 */       if (closeExecutor != null) {
/*  715 */         closeExecutor.execute(new Runnable()
/*      */         {
/*      */           /* Error */
/*      */           public void run()
/*      */           {
/*      */             // Byte code:
/*      */             //   0: aload_0
/*      */             //   1: getfield 1	io/netty/channel/AbstractChannel$AbstractUnsafe$6:this$1	Lio/netty/channel/AbstractChannel$AbstractUnsafe;
/*      */             //   4: aload_0
/*      */             //   5: getfield 2	io/netty/channel/AbstractChannel$AbstractUnsafe$6:val$promise	Lio/netty/channel/ChannelPromise;
/*      */             //   8: invokestatic 9	io/netty/channel/AbstractChannel$AbstractUnsafe:access$900	(Lio/netty/channel/AbstractChannel$AbstractUnsafe;Lio/netty/channel/ChannelPromise;)V
/*      */             //   11: aload_0
/*      */             //   12: getfield 1	io/netty/channel/AbstractChannel$AbstractUnsafe$6:this$1	Lio/netty/channel/AbstractChannel$AbstractUnsafe;
/*      */             //   15: new 10	io/netty/channel/AbstractChannel$AbstractUnsafe$6$1
/*      */             //   18: dup
/*      */             //   19: aload_0
/*      */             //   20: invokespecial 11	io/netty/channel/AbstractChannel$AbstractUnsafe$6$1:<init>	(Lio/netty/channel/AbstractChannel$AbstractUnsafe$6;)V
/*      */             //   23: invokestatic 12	io/netty/channel/AbstractChannel$AbstractUnsafe:access$1100	(Lio/netty/channel/AbstractChannel$AbstractUnsafe;Ljava/lang/Runnable;)V
/*      */             //   26: goto +21 -> 47
/*      */             //   29: astore_1
/*      */             //   30: aload_0
/*      */             //   31: getfield 1	io/netty/channel/AbstractChannel$AbstractUnsafe$6:this$1	Lio/netty/channel/AbstractChannel$AbstractUnsafe;
/*      */             //   34: new 10	io/netty/channel/AbstractChannel$AbstractUnsafe$6$1
/*      */             //   37: dup
/*      */             //   38: aload_0
/*      */             //   39: invokespecial 11	io/netty/channel/AbstractChannel$AbstractUnsafe$6$1:<init>	(Lio/netty/channel/AbstractChannel$AbstractUnsafe$6;)V
/*      */             //   42: invokestatic 12	io/netty/channel/AbstractChannel$AbstractUnsafe:access$1100	(Lio/netty/channel/AbstractChannel$AbstractUnsafe;Ljava/lang/Runnable;)V
/*      */             //   45: aload_1
/*      */             //   46: athrow
/*      */             //   47: return
/*      */             // Line number table:
/*      */             //   Java source line #720	-> byte code offset #0
/*      */             //   Java source line #723	-> byte code offset #11
/*      */             //   Java source line #734	-> byte code offset #26
/*      */             //   Java source line #723	-> byte code offset #29
/*      */             //   Java source line #734	-> byte code offset #45
/*      */             //   Java source line #735	-> byte code offset #47
/*      */             // Local variable table:
/*      */             //   start	length	slot	name	signature
/*      */             //   0	48	0	this	6
/*      */             //   29	17	1	localObject	Object
/*      */             // Exception table:
/*      */             //   from	to	target	type
/*      */             //   0	11	29	finally
/*      */           }
/*      */         });
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/*  740 */           doClose0(promise);
/*      */         } finally {
/*  742 */           if (outboundBuffer != null)
/*      */           {
/*  744 */             outboundBuffer.failFlushed(cause, notify);
/*  745 */             outboundBuffer.close(closeCause);
/*      */           }
/*      */         }
/*  748 */         if (this.inFlush0) {
/*  749 */           invokeLater(new Runnable()
/*      */           {
/*      */             public void run() {
/*  752 */               AbstractChannel.AbstractUnsafe.this.fireChannelInactiveAndDeregister(wasActive);
/*      */             }
/*      */           });
/*      */         } else {
/*  756 */           fireChannelInactiveAndDeregister(wasActive);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void doClose0(ChannelPromise promise) {
/*      */       try {
/*  763 */         AbstractChannel.this.doClose();
/*  764 */         AbstractChannel.this.closeFuture.setClosed();
/*  765 */         safeSetSuccess(promise);
/*      */       } catch (Throwable t) {
/*  767 */         AbstractChannel.this.closeFuture.setClosed();
/*  768 */         safeSetFailure(promise, t);
/*      */       }
/*      */     }
/*      */     
/*      */     private void fireChannelInactiveAndDeregister(boolean wasActive) {
/*  773 */       deregister(voidPromise(), (wasActive) && (!AbstractChannel.this.isActive()));
/*      */     }
/*      */     
/*      */     public final void closeForcibly()
/*      */     {
/*  778 */       assertEventLoop();
/*      */       try
/*      */       {
/*  781 */         AbstractChannel.this.doClose();
/*      */       } catch (Exception e) {
/*  783 */         AbstractChannel.logger.warn("Failed to close a channel.", e);
/*      */       }
/*      */     }
/*      */     
/*      */     public final void deregister(ChannelPromise promise)
/*      */     {
/*  789 */       assertEventLoop();
/*      */       
/*  791 */       deregister(promise, false);
/*      */     }
/*      */     
/*      */     private void deregister(final ChannelPromise promise, final boolean fireChannelInactive) {
/*  795 */       if (!promise.setUncancellable()) {
/*  796 */         return;
/*      */       }
/*      */       
/*  799 */       if (!AbstractChannel.this.registered) {
/*  800 */         safeSetSuccess(promise);
/*  801 */         return;
/*      */       }
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
/*  813 */       invokeLater(new Runnable()
/*      */       {
/*      */         public void run() {
/*      */           try {
/*  817 */             AbstractChannel.this.doDeregister();
/*      */           } catch (Throwable t) {
/*  819 */             AbstractChannel.logger.warn("Unexpected exception occurred while deregistering a channel.", t);
/*      */           } finally {
/*  821 */             if (fireChannelInactive) {
/*  822 */               AbstractChannel.this.pipeline.fireChannelInactive();
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  828 */             if (AbstractChannel.this.registered) {
/*  829 */               AbstractChannel.this.registered = false;
/*  830 */               AbstractChannel.this.pipeline.fireChannelUnregistered();
/*      */             }
/*  832 */             AbstractChannel.AbstractUnsafe.this.safeSetSuccess(promise);
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */     public final void beginRead()
/*      */     {
/*  840 */       assertEventLoop();
/*      */       
/*  842 */       if (!AbstractChannel.this.isActive()) {
/*  843 */         return;
/*      */       }
/*      */       try
/*      */       {
/*  847 */         AbstractChannel.this.doBeginRead();
/*      */       } catch (Exception e) {
/*  849 */         invokeLater(new Runnable()
/*      */         {
/*      */           public void run() {
/*  852 */             AbstractChannel.this.pipeline.fireExceptionCaught(e);
/*      */           }
/*  854 */         });
/*  855 */         close(voidPromise());
/*      */       }
/*      */     }
/*      */     
/*      */     public final void write(Object msg, ChannelPromise promise)
/*      */     {
/*  861 */       assertEventLoop();
/*      */       
/*  863 */       ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  864 */       if (outboundBuffer == null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  869 */         safeSetFailure(promise, AbstractChannel.WRITE_CLOSED_CHANNEL_EXCEPTION);
/*      */         
/*  871 */         ReferenceCountUtil.release(msg);
/*  872 */         return;
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  877 */         msg = AbstractChannel.this.filterOutboundMessage(msg);
/*  878 */         int size = AbstractChannel.this.pipeline.estimatorHandle().size(msg);
/*  879 */         if (size < 0) {
/*  880 */           size = 0;
/*      */         }
/*      */       } catch (Throwable t) {
/*  883 */         safeSetFailure(promise, t);
/*  884 */         ReferenceCountUtil.release(msg); return;
/*      */       }
/*      */       
/*      */       int size;
/*  888 */       outboundBuffer.addMessage(msg, size, promise);
/*      */     }
/*      */     
/*      */     public final void flush()
/*      */     {
/*  893 */       assertEventLoop();
/*      */       
/*  895 */       ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
/*  896 */       if (outboundBuffer == null) {
/*  897 */         return;
/*      */       }
/*      */       
/*  900 */       outboundBuffer.addFlush();
/*  901 */       flush0();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     protected void flush0()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   4: ifeq +4 -> 8
/*      */       //   7: return
/*      */       //   8: aload_0
/*      */       //   9: getfield 10	io/netty/channel/AbstractChannel$AbstractUnsafe:outboundBuffer	Lio/netty/channel/ChannelOutboundBuffer;
/*      */       //   12: astore_1
/*      */       //   13: aload_1
/*      */       //   14: ifnull +10 -> 24
/*      */       //   17: aload_1
/*      */       //   18: invokevirtual 135	io/netty/channel/ChannelOutboundBuffer:isEmpty	()Z
/*      */       //   21: ifeq +4 -> 25
/*      */       //   24: return
/*      */       //   25: aload_0
/*      */       //   26: iconst_1
/*      */       //   27: putfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   30: aload_0
/*      */       //   31: getfield 6	io/netty/channel/AbstractChannel$AbstractUnsafe:this$0	Lio/netty/channel/AbstractChannel;
/*      */       //   34: invokevirtual 60	io/netty/channel/AbstractChannel:isActive	()Z
/*      */       //   37: ifne +49 -> 86
/*      */       //   40: aload_0
/*      */       //   41: getfield 6	io/netty/channel/AbstractChannel$AbstractUnsafe:this$0	Lio/netty/channel/AbstractChannel;
/*      */       //   44: invokevirtual 136	io/netty/channel/AbstractChannel:isOpen	()Z
/*      */       //   47: ifeq +14 -> 61
/*      */       //   50: aload_1
/*      */       //   51: invokestatic 137	io/netty/channel/AbstractChannel:access$1300	()Ljava/nio/channels/NotYetConnectedException;
/*      */       //   54: iconst_1
/*      */       //   55: invokevirtual 98	io/netty/channel/ChannelOutboundBuffer:failFlushed	(Ljava/lang/Throwable;Z)V
/*      */       //   58: goto +11 -> 69
/*      */       //   61: aload_1
/*      */       //   62: invokestatic 138	io/netty/channel/AbstractChannel:access$1400	()Ljava/nio/channels/ClosedChannelException;
/*      */       //   65: iconst_0
/*      */       //   66: invokevirtual 98	io/netty/channel/ChannelOutboundBuffer:failFlushed	(Ljava/lang/Throwable;Z)V
/*      */       //   69: aload_0
/*      */       //   70: iconst_0
/*      */       //   71: putfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   74: goto +11 -> 85
/*      */       //   77: astore_2
/*      */       //   78: aload_0
/*      */       //   79: iconst_0
/*      */       //   80: putfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   83: aload_2
/*      */       //   84: athrow
/*      */       //   85: return
/*      */       //   86: aload_0
/*      */       //   87: getfield 6	io/netty/channel/AbstractChannel$AbstractUnsafe:this$0	Lio/netty/channel/AbstractChannel;
/*      */       //   90: aload_1
/*      */       //   91: invokevirtual 139	io/netty/channel/AbstractChannel:doWrite	(Lio/netty/channel/ChannelOutboundBuffer;)V
/*      */       //   94: aload_0
/*      */       //   95: iconst_0
/*      */       //   96: putfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   99: goto +86 -> 185
/*      */       //   102: astore_2
/*      */       //   103: aload_2
/*      */       //   104: instanceof 140
/*      */       //   107: ifeq +34 -> 141
/*      */       //   110: aload_0
/*      */       //   111: getfield 6	io/netty/channel/AbstractChannel$AbstractUnsafe:this$0	Lio/netty/channel/AbstractChannel;
/*      */       //   114: invokevirtual 19	io/netty/channel/AbstractChannel:config	()Lio/netty/channel/ChannelConfig;
/*      */       //   117: invokeinterface 141 1 0
/*      */       //   122: ifeq +19 -> 141
/*      */       //   125: aload_0
/*      */       //   126: aload_0
/*      */       //   127: invokevirtual 116	io/netty/channel/AbstractChannel$AbstractUnsafe:voidPromise	()Lio/netty/channel/ChannelPromise;
/*      */       //   130: aload_2
/*      */       //   131: invokestatic 138	io/netty/channel/AbstractChannel:access$1400	()Ljava/nio/channels/ClosedChannelException;
/*      */       //   134: iconst_0
/*      */       //   135: invokespecial 86	io/netty/channel/AbstractChannel$AbstractUnsafe:close	(Lio/netty/channel/ChannelPromise;Ljava/lang/Throwable;Ljava/nio/channels/ClosedChannelException;Z)V
/*      */       //   138: goto +29 -> 167
/*      */       //   141: aload_0
/*      */       //   142: aload_0
/*      */       //   143: invokevirtual 116	io/netty/channel/AbstractChannel$AbstractUnsafe:voidPromise	()Lio/netty/channel/ChannelPromise;
/*      */       //   146: aload_2
/*      */       //   147: invokespecial 87	io/netty/channel/AbstractChannel$AbstractUnsafe:shutdownOutput	(Lio/netty/channel/ChannelPromise;Ljava/lang/Throwable;)V
/*      */       //   150: goto +17 -> 167
/*      */       //   153: astore_3
/*      */       //   154: aload_0
/*      */       //   155: aload_0
/*      */       //   156: invokevirtual 116	io/netty/channel/AbstractChannel$AbstractUnsafe:voidPromise	()Lio/netty/channel/ChannelPromise;
/*      */       //   159: aload_3
/*      */       //   160: invokestatic 138	io/netty/channel/AbstractChannel:access$1400	()Ljava/nio/channels/ClosedChannelException;
/*      */       //   163: iconst_0
/*      */       //   164: invokespecial 86	io/netty/channel/AbstractChannel$AbstractUnsafe:close	(Lio/netty/channel/ChannelPromise;Ljava/lang/Throwable;Ljava/nio/channels/ClosedChannelException;Z)V
/*      */       //   167: aload_0
/*      */       //   168: iconst_0
/*      */       //   169: putfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   172: goto +13 -> 185
/*      */       //   175: astore 4
/*      */       //   177: aload_0
/*      */       //   178: iconst_0
/*      */       //   179: putfield 112	io/netty/channel/AbstractChannel$AbstractUnsafe:inFlush0	Z
/*      */       //   182: aload 4
/*      */       //   184: athrow
/*      */       //   185: return
/*      */       // Line number table:
/*      */       //   Java source line #906	-> byte code offset #0
/*      */       //   Java source line #908	-> byte code offset #7
/*      */       //   Java source line #911	-> byte code offset #8
/*      */       //   Java source line #912	-> byte code offset #13
/*      */       //   Java source line #913	-> byte code offset #24
/*      */       //   Java source line #916	-> byte code offset #25
/*      */       //   Java source line #919	-> byte code offset #30
/*      */       //   Java source line #921	-> byte code offset #40
/*      */       //   Java source line #922	-> byte code offset #50
/*      */       //   Java source line #925	-> byte code offset #61
/*      */       //   Java source line #928	-> byte code offset #69
/*      */       //   Java source line #929	-> byte code offset #74
/*      */       //   Java source line #928	-> byte code offset #77
/*      */       //   Java source line #929	-> byte code offset #83
/*      */       //   Java source line #930	-> byte code offset #85
/*      */       //   Java source line #934	-> byte code offset #86
/*      */       //   Java source line #954	-> byte code offset #94
/*      */       //   Java source line #955	-> byte code offset #99
/*      */       //   Java source line #935	-> byte code offset #102
/*      */       //   Java source line #936	-> byte code offset #103
/*      */       //   Java source line #945	-> byte code offset #125
/*      */       //   Java source line #948	-> byte code offset #141
/*      */       //   Java source line #951	-> byte code offset #150
/*      */       //   Java source line #949	-> byte code offset #153
/*      */       //   Java source line #950	-> byte code offset #154
/*      */       //   Java source line #954	-> byte code offset #167
/*      */       //   Java source line #955	-> byte code offset #172
/*      */       //   Java source line #954	-> byte code offset #175
/*      */       //   Java source line #955	-> byte code offset #182
/*      */       //   Java source line #956	-> byte code offset #185
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	186	0	this	AbstractUnsafe
/*      */       //   12	79	1	outboundBuffer	ChannelOutboundBuffer
/*      */       //   77	7	2	localObject1	Object
/*      */       //   102	45	2	t	Throwable
/*      */       //   153	7	3	t2	Throwable
/*      */       //   175	8	4	localObject2	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   40	69	77	finally
/*      */       //   86	94	102	java/lang/Throwable
/*      */       //   141	150	153	java/lang/Throwable
/*      */       //   86	94	175	finally
/*      */       //   102	167	175	finally
/*      */       //   175	177	175	finally
/*      */     }
/*      */     
/*      */     public final ChannelPromise voidPromise()
/*      */     {
/*  960 */       assertEventLoop();
/*      */       
/*  962 */       return AbstractChannel.this.unsafeVoidPromise;
/*      */     }
/*      */     
/*      */     protected final boolean ensureOpen(ChannelPromise promise) {
/*  966 */       if (AbstractChannel.this.isOpen()) {
/*  967 */         return true;
/*      */       }
/*      */       
/*  970 */       safeSetFailure(promise, AbstractChannel.ENSURE_OPEN_CLOSED_CHANNEL_EXCEPTION);
/*  971 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final void safeSetSuccess(ChannelPromise promise)
/*      */     {
/*  978 */       if ((!(promise instanceof VoidChannelPromise)) && (!promise.trySuccess())) {
/*  979 */         AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final void safeSetFailure(ChannelPromise promise, Throwable cause)
/*      */     {
/*  987 */       if ((!(promise instanceof VoidChannelPromise)) && (!promise.tryFailure(cause))) {
/*  988 */         AbstractChannel.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
/*      */       }
/*      */     }
/*      */     
/*      */     protected final void closeIfClosed() {
/*  993 */       if (AbstractChannel.this.isOpen()) {
/*  994 */         return;
/*      */       }
/*  996 */       close(voidPromise());
/*      */     }
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
/*      */     private void invokeLater(Runnable task)
/*      */     {
/*      */       try
/*      */       {
/* 1012 */         AbstractChannel.this.eventLoop().execute(task);
/*      */       } catch (RejectedExecutionException e) {
/* 1014 */         AbstractChannel.logger.warn("Can't invoke task later as EventLoop rejected it", e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final Throwable annotateConnectException(Throwable cause, SocketAddress remoteAddress)
/*      */     {
/* 1022 */       if ((cause instanceof ConnectException)) {
/* 1023 */         return new AbstractChannel.AnnotatedConnectException((ConnectException)cause, remoteAddress);
/*      */       }
/* 1025 */       if ((cause instanceof NoRouteToHostException)) {
/* 1026 */         return new AbstractChannel.AnnotatedNoRouteToHostException((NoRouteToHostException)cause, remoteAddress);
/*      */       }
/* 1028 */       if ((cause instanceof SocketException)) {
/* 1029 */         return new AbstractChannel.AnnotatedSocketException((SocketException)cause, remoteAddress);
/*      */       }
/*      */       
/* 1032 */       return cause;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Executor prepareToClose()
/*      */     {
/* 1042 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doRegister()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doBind(SocketAddress paramSocketAddress)
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doDisconnect()
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doClose()
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doShutdownOutput()
/*      */     throws Exception
/*      */   {
/* 1091 */     doClose();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doDeregister()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doBeginRead()
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doWrite(ChannelOutboundBuffer paramChannelOutboundBuffer)
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */   protected Object filterOutboundMessage(Object msg)
/*      */     throws Exception
/*      */   {
/* 1118 */     return msg;
/*      */   }
/*      */   
/*      */   static final class CloseFuture extends DefaultChannelPromise
/*      */   {
/*      */     CloseFuture(AbstractChannel ch) {
/* 1124 */       super();
/*      */     }
/*      */     
/*      */     public ChannelPromise setSuccess()
/*      */     {
/* 1129 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     public ChannelPromise setFailure(Throwable cause)
/*      */     {
/* 1134 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     public boolean trySuccess()
/*      */     {
/* 1139 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     public boolean tryFailure(Throwable cause)
/*      */     {
/* 1144 */       throw new IllegalStateException();
/*      */     }
/*      */     
/*      */     boolean setClosed() {
/* 1148 */       return super.trySuccess();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AnnotatedConnectException extends ConnectException
/*      */   {
/*      */     private static final long serialVersionUID = 3901958112696433556L;
/*      */     
/*      */     AnnotatedConnectException(ConnectException exception, SocketAddress remoteAddress) {
/* 1157 */       super();
/* 1158 */       initCause(exception);
/* 1159 */       setStackTrace(exception.getStackTrace());
/*      */     }
/*      */     
/*      */     public Throwable fillInStackTrace()
/*      */     {
/* 1164 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AnnotatedNoRouteToHostException extends NoRouteToHostException
/*      */   {
/*      */     private static final long serialVersionUID = -6801433937592080623L;
/*      */     
/*      */     AnnotatedNoRouteToHostException(NoRouteToHostException exception, SocketAddress remoteAddress) {
/* 1173 */       super();
/* 1174 */       initCause(exception);
/* 1175 */       setStackTrace(exception.getStackTrace());
/*      */     }
/*      */     
/*      */     public Throwable fillInStackTrace()
/*      */     {
/* 1180 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AnnotatedSocketException extends SocketException
/*      */   {
/*      */     private static final long serialVersionUID = 3896743275010454039L;
/*      */     
/*      */     AnnotatedSocketException(SocketException exception, SocketAddress remoteAddress) {
/* 1189 */       super();
/* 1190 */       initCause(exception);
/* 1191 */       setStackTrace(exception.getStackTrace());
/*      */     }
/*      */     
/*      */     public Throwable fillInStackTrace()
/*      */     {
/* 1196 */       return this;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\AbstractChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */