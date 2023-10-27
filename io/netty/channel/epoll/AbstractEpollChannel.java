/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ConnectTimeoutException;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.RecvByteBufAllocator.ExtendedHandle;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.socket.ChannelInputShutdownEvent;
/*     */ import io.netty.channel.socket.ChannelInputShutdownReadComplete;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.channel.unix.UnixChannel;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.UnresolvedAddressException;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ abstract class AbstractEpollChannel
/*     */   extends AbstractChannel
/*     */   implements UnixChannel
/*     */ {
/*  60 */   private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollChannel.class, "doClose()");
/*     */   
/*  62 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*     */   final LinuxSocket socket;
/*     */   
/*     */   private ChannelPromise connectPromise;
/*     */   
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*     */   
/*     */   private SocketAddress requestedRemoteAddress;
/*     */   
/*     */   private volatile SocketAddress local;
/*     */   
/*     */   private volatile SocketAddress remote;
/*  75 */   protected int flags = Native.EPOLLET;
/*     */   boolean inputClosedSeenErrorOnRead;
/*     */   boolean epollInReadyRunnablePending;
/*     */   protected volatile boolean active;
/*     */   
/*     */   AbstractEpollChannel(LinuxSocket fd)
/*     */   {
/*  82 */     this(null, fd, false);
/*     */   }
/*     */   
/*     */   AbstractEpollChannel(Channel parent, LinuxSocket fd, boolean active) {
/*  86 */     super(parent);
/*  87 */     this.socket = ((LinuxSocket)ObjectUtil.checkNotNull(fd, "fd"));
/*  88 */     this.active = active;
/*  89 */     if (active)
/*     */     {
/*     */ 
/*  92 */       this.local = fd.localAddress();
/*  93 */       this.remote = fd.remoteAddress();
/*     */     }
/*     */   }
/*     */   
/*     */   AbstractEpollChannel(Channel parent, LinuxSocket fd, SocketAddress remote) {
/*  98 */     super(parent);
/*  99 */     this.socket = ((LinuxSocket)ObjectUtil.checkNotNull(fd, "fd"));
/* 100 */     this.active = true;
/*     */     
/*     */ 
/* 103 */     this.remote = remote;
/* 104 */     this.local = fd.localAddress();
/*     */   }
/*     */   
/*     */   static boolean isSoErrorZero(Socket fd) {
/*     */     try {
/* 109 */       return fd.getSoError() == 0;
/*     */     } catch (IOException e) {
/* 111 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   void setFlag(int flag) throws IOException {
/* 116 */     if (!isFlagSet(flag)) {
/* 117 */       this.flags |= flag;
/* 118 */       modifyEvents();
/*     */     }
/*     */   }
/*     */   
/*     */   void clearFlag(int flag) throws IOException {
/* 123 */     if (isFlagSet(flag)) {
/* 124 */       this.flags &= (flag ^ 0xFFFFFFFF);
/* 125 */       modifyEvents();
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isFlagSet(int flag) {
/* 130 */     return (this.flags & flag) != 0;
/*     */   }
/*     */   
/*     */   public final FileDescriptor fd()
/*     */   {
/* 135 */     return this.socket;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract EpollChannelConfig config();
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 143 */     return this.active;
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 148 */     return METADATA;
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 153 */     this.active = false;
/*     */     
/*     */ 
/* 156 */     this.inputClosedSeenErrorOnRead = true;
/*     */     try {
/* 158 */       ChannelPromise promise = this.connectPromise;
/* 159 */       if (promise != null)
/*     */       {
/* 161 */         promise.tryFailure(DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
/* 162 */         this.connectPromise = null;
/*     */       }
/*     */       
/* 165 */       ScheduledFuture<?> future = this.connectTimeoutFuture;
/* 166 */       if (future != null) {
/* 167 */         future.cancel(false);
/* 168 */         this.connectTimeoutFuture = null;
/*     */       }
/*     */       
/* 171 */       if (isRegistered())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 176 */         EventLoop loop = eventLoop();
/* 177 */         if (loop.inEventLoop()) {
/* 178 */           doDeregister();
/*     */         } else {
/* 180 */           loop.execute(new Runnable()
/*     */           {
/*     */             public void run() {
/*     */               try {
/* 184 */                 AbstractEpollChannel.this.doDeregister();
/*     */               } catch (Throwable cause) {
/* 186 */                 AbstractEpollChannel.this.pipeline().fireExceptionCaught(cause);
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */     } finally {
/* 193 */       this.socket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 199 */     doClose();
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/* 204 */     return loop instanceof EpollEventLoop;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 209 */     return this.socket.isOpen();
/*     */   }
/*     */   
/*     */   protected void doDeregister() throws Exception
/*     */   {
/* 214 */     ((EpollEventLoop)eventLoop()).remove(this);
/*     */   }
/*     */   
/*     */   protected final void doBeginRead()
/*     */     throws Exception
/*     */   {
/* 220 */     AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)unsafe();
/* 221 */     unsafe.readPending = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 226 */     setFlag(Native.EPOLLIN);
/*     */     
/*     */ 
/*     */ 
/* 230 */     if (unsafe.maybeMoreDataToRead) {
/* 231 */       unsafe.executeEpollInReadyRunnable(config());
/*     */     }
/*     */   }
/*     */   
/*     */   final boolean shouldBreakEpollInReady(ChannelConfig config) {
/* 236 */     return (this.socket.isInputShutdown()) && ((this.inputClosedSeenErrorOnRead) || (!isAllowHalfClosure(config)));
/*     */   }
/*     */   
/*     */   private static boolean isAllowHalfClosure(ChannelConfig config) {
/* 240 */     return ((config instanceof SocketChannelConfig)) && 
/* 241 */       (((SocketChannelConfig)config).isAllowHalfClosure());
/*     */   }
/*     */   
/*     */   final void clearEpollIn()
/*     */   {
/* 246 */     if (isRegistered()) {
/* 247 */       EventLoop loop = eventLoop();
/* 248 */       final AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)unsafe();
/* 249 */       if (loop.inEventLoop()) {
/* 250 */         unsafe.clearEpollIn0();
/*     */       }
/*     */       else {
/* 253 */         loop.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 256 */             if ((!unsafe.readPending) && (!AbstractEpollChannel.this.config().isAutoRead()))
/*     */             {
/* 258 */               unsafe.clearEpollIn0();
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 266 */       this.flags &= (Native.EPOLLIN ^ 0xFFFFFFFF);
/*     */     }
/*     */   }
/*     */   
/*     */   private void modifyEvents() throws IOException {
/* 271 */     if ((isOpen()) && (isRegistered())) {
/* 272 */       ((EpollEventLoop)eventLoop()).modify(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doRegister()
/*     */     throws Exception
/*     */   {
/* 281 */     this.epollInReadyRunnablePending = false;
/* 282 */     ((EpollEventLoop)eventLoop()).add(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract AbstractEpollUnsafe newUnsafe();
/*     */   
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf)
/*     */   {
/* 292 */     return newDirectBuffer(buf, buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(Object holder, ByteBuf buf)
/*     */   {
/* 301 */     int readableBytes = buf.readableBytes();
/* 302 */     if (readableBytes == 0) {
/* 303 */       ReferenceCountUtil.release(holder);
/* 304 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 307 */     ByteBufAllocator alloc = alloc();
/* 308 */     if (alloc.isDirectBufferPooled()) {
/* 309 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 312 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 313 */     if (directBuf == null) {
/* 314 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 317 */     directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 318 */     ReferenceCountUtil.safeRelease(holder);
/* 319 */     return directBuf;
/*     */   }
/*     */   
/*     */   private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
/* 323 */     ByteBuf directBuf = alloc.directBuffer(capacity);
/* 324 */     directBuf.writeBytes(buf, buf.readerIndex(), capacity);
/* 325 */     ReferenceCountUtil.safeRelease(holder);
/* 326 */     return directBuf;
/*     */   }
/*     */   
/*     */   protected static void checkResolvable(InetSocketAddress addr) {
/* 330 */     if (addr.isUnresolved()) {
/* 331 */       throw new UnresolvedAddressException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected final int doReadBytes(ByteBuf byteBuf)
/*     */     throws Exception
/*     */   {
/* 339 */     int writerIndex = byteBuf.writerIndex();
/*     */     
/* 341 */     unsafe().recvBufAllocHandle().attemptedBytesRead(byteBuf.writableBytes());
/* 342 */     int localReadAmount; int localReadAmount; if (byteBuf.hasMemoryAddress()) {
/* 343 */       localReadAmount = this.socket.readAddress(byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
/*     */     } else {
/* 345 */       ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
/* 346 */       localReadAmount = this.socket.read(buf, buf.position(), buf.limit());
/*     */     }
/* 348 */     if (localReadAmount > 0) {
/* 349 */       byteBuf.writerIndex(writerIndex + localReadAmount);
/*     */     }
/* 351 */     return localReadAmount;
/*     */   }
/*     */   
/*     */   protected final int doWriteBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
/* 355 */     if (buf.hasMemoryAddress()) {
/* 356 */       int localFlushedAmount = this.socket.writeAddress(buf.memoryAddress(), buf.readerIndex(), buf.writerIndex());
/* 357 */       if (localFlushedAmount > 0) {
/* 358 */         in.removeBytes(localFlushedAmount);
/* 359 */         return 1;
/*     */       }
/*     */     }
/*     */     else {
/* 363 */       ByteBuffer nioBuf = buf.nioBufferCount() == 1 ? buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()) : buf.nioBuffer();
/* 364 */       int localFlushedAmount = this.socket.write(nioBuf, nioBuf.position(), nioBuf.limit());
/* 365 */       if (localFlushedAmount > 0) {
/* 366 */         nioBuf.position(nioBuf.position() + localFlushedAmount);
/* 367 */         in.removeBytes(localFlushedAmount);
/* 368 */         return 1;
/*     */       }
/*     */     }
/* 371 */     return Integer.MAX_VALUE; }
/*     */   
/*     */   protected abstract class AbstractEpollUnsafe extends AbstractChannel.AbstractUnsafe { boolean readPending; boolean maybeMoreDataToRead;
/* 374 */     protected AbstractEpollUnsafe() { super(); }
/*     */     
/*     */ 
/*     */     private EpollRecvByteAllocatorHandle allocHandle;
/* 378 */     private final Runnable epollInReadyRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/* 381 */         AbstractEpollChannel.this.epollInReadyRunnablePending = false;
/* 382 */         AbstractEpollChannel.AbstractEpollUnsafe.this.epollInReady();
/*     */       }
/*     */     };
/*     */     
/*     */ 
/*     */ 
/*     */     abstract void epollInReady();
/*     */     
/*     */ 
/* 391 */     final void epollInBefore() { this.maybeMoreDataToRead = false; }
/*     */     
/*     */     final void epollInFinally(ChannelConfig config) {
/* 394 */       this.maybeMoreDataToRead = this.allocHandle.maybeMoreDataToRead();
/*     */       
/* 396 */       if ((this.allocHandle.isReceivedRdHup()) || ((this.readPending) && (this.maybeMoreDataToRead)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 404 */         executeEpollInReadyRunnable(config);
/* 405 */       } else if ((!this.readPending) && (!config.isAutoRead()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 412 */         AbstractEpollChannel.this.clearEpollIn();
/*     */       }
/*     */     }
/*     */     
/*     */     final void executeEpollInReadyRunnable(ChannelConfig config) {
/* 417 */       if ((AbstractEpollChannel.this.epollInReadyRunnablePending) || (!AbstractEpollChannel.this.isActive()) || (AbstractEpollChannel.this.shouldBreakEpollInReady(config))) {
/* 418 */         return;
/*     */       }
/* 420 */       AbstractEpollChannel.this.epollInReadyRunnablePending = true;
/* 421 */       AbstractEpollChannel.this.eventLoop().execute(this.epollInReadyRunnable);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final void epollRdHupReady()
/*     */     {
/* 429 */       recvBufAllocHandle().receivedRdHup();
/*     */       
/* 431 */       if (AbstractEpollChannel.this.isActive())
/*     */       {
/*     */ 
/*     */ 
/* 435 */         epollInReady();
/*     */       }
/*     */       else {
/* 438 */         shutdownInput(true);
/*     */       }
/*     */       
/*     */ 
/* 442 */       clearEpollRdHup();
/*     */     }
/*     */     
/*     */ 
/*     */     private void clearEpollRdHup()
/*     */     {
/*     */       try
/*     */       {
/* 450 */         AbstractEpollChannel.this.clearFlag(Native.EPOLLRDHUP);
/*     */       } catch (IOException e) {
/* 452 */         AbstractEpollChannel.this.pipeline().fireExceptionCaught(e);
/* 453 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     void shutdownInput(boolean rdHup)
/*     */     {
/* 461 */       if (!AbstractEpollChannel.this.socket.isInputShutdown()) {
/* 462 */         if (AbstractEpollChannel.isAllowHalfClosure(AbstractEpollChannel.this.config())) {
/*     */           try {
/* 464 */             AbstractEpollChannel.this.socket.shutdown(true, false);
/*     */           }
/*     */           catch (IOException ignored)
/*     */           {
/* 468 */             fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
/* 469 */             return;
/*     */           }
/*     */           catch (NotYetConnectedException localNotYetConnectedException) {}
/*     */           
/*     */ 
/* 474 */           AbstractEpollChannel.this.clearEpollIn();
/* 475 */           AbstractEpollChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */         } else {
/* 477 */           close(voidPromise());
/*     */         }
/* 479 */       } else if (!rdHup) {
/* 480 */         AbstractEpollChannel.this.inputClosedSeenErrorOnRead = true;
/* 481 */         AbstractEpollChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownReadComplete.INSTANCE);
/*     */       }
/*     */     }
/*     */     
/*     */     private void fireEventAndClose(Object evt) {
/* 486 */       AbstractEpollChannel.this.pipeline().fireUserEventTriggered(evt);
/* 487 */       close(voidPromise());
/*     */     }
/*     */     
/*     */     public EpollRecvByteAllocatorHandle recvBufAllocHandle()
/*     */     {
/* 492 */       if (this.allocHandle == null) {
/* 493 */         this.allocHandle = newEpollHandle((RecvByteBufAllocator.ExtendedHandle)super.recvBufAllocHandle());
/*     */       }
/* 495 */       return this.allocHandle;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle)
/*     */     {
/* 503 */       return new EpollRecvByteAllocatorHandle(handle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final void flush0()
/*     */     {
/* 511 */       if (!AbstractEpollChannel.this.isFlagSet(Native.EPOLLOUT)) {
/* 512 */         super.flush0();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     final void epollOutReady()
/*     */     {
/* 520 */       if (AbstractEpollChannel.this.connectPromise != null)
/*     */       {
/* 522 */         finishConnect();
/* 523 */       } else if (!AbstractEpollChannel.this.socket.isOutputShutdown())
/*     */       {
/* 525 */         super.flush0();
/*     */       }
/*     */     }
/*     */     
/*     */     protected final void clearEpollIn0() {
/* 530 */       assert (AbstractEpollChannel.this.eventLoop().inEventLoop());
/*     */       try {
/* 532 */         this.readPending = false;
/* 533 */         AbstractEpollChannel.this.clearFlag(Native.EPOLLIN);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 537 */         AbstractEpollChannel.this.pipeline().fireExceptionCaught(e);
/* 538 */         AbstractEpollChannel.this.unsafe().close(AbstractEpollChannel.this.unsafe().voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 545 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/* 546 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 550 */         if (AbstractEpollChannel.this.connectPromise != null) {
/* 551 */           throw new ConnectionPendingException();
/*     */         }
/*     */         
/* 554 */         boolean wasActive = AbstractEpollChannel.this.isActive();
/* 555 */         if (AbstractEpollChannel.this.doConnect(remoteAddress, localAddress)) {
/* 556 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 558 */           AbstractEpollChannel.this.connectPromise = promise;
/* 559 */           AbstractEpollChannel.this.requestedRemoteAddress = remoteAddress;
/*     */           
/*     */ 
/* 562 */           int connectTimeoutMillis = AbstractEpollChannel.this.config().getConnectTimeoutMillis();
/* 563 */           if (connectTimeoutMillis > 0) {
/* 564 */             AbstractEpollChannel.this.connectTimeoutFuture = AbstractEpollChannel.this.eventLoop().schedule(new Runnable()
/*     */             {
/*     */               public void run() {
/* 567 */                 ChannelPromise connectPromise = AbstractEpollChannel.this.connectPromise;
/* 568 */                 ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                 
/* 570 */                 if ((connectPromise != null) && (connectPromise.tryFailure(cause)))
/* 571 */                   AbstractEpollChannel.AbstractEpollUnsafe.this.close(AbstractEpollChannel.AbstractEpollUnsafe.this.voidPromise()); } }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 577 */           promise.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 580 */               if (future.isCancelled()) {
/* 581 */                 if (AbstractEpollChannel.this.connectTimeoutFuture != null) {
/* 582 */                   AbstractEpollChannel.this.connectTimeoutFuture.cancel(false);
/*     */                 }
/* 584 */                 AbstractEpollChannel.this.connectPromise = null;
/* 585 */                 AbstractEpollChannel.AbstractEpollUnsafe.this.close(AbstractEpollChannel.AbstractEpollUnsafe.this.voidPromise());
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */       } catch (Throwable t) {
/* 591 */         closeIfClosed();
/* 592 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 597 */       if (promise == null)
/*     */       {
/* 599 */         return;
/*     */       }
/* 601 */       AbstractEpollChannel.this.active = true;
/*     */       
/*     */ 
/*     */ 
/* 605 */       boolean active = AbstractEpollChannel.this.isActive();
/*     */       
/*     */ 
/* 608 */       boolean promiseSet = promise.trySuccess();
/*     */       
/*     */ 
/*     */ 
/* 612 */       if ((!wasActive) && (active)) {
/* 613 */         AbstractEpollChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */       
/*     */ 
/* 617 */       if (!promiseSet) {
/* 618 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 623 */       if (promise == null)
/*     */       {
/* 625 */         return;
/*     */       }
/*     */       
/*     */ 
/* 629 */       promise.tryFailure(cause);
/* 630 */       closeIfClosed();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void finishConnect()
/*     */     {
/* 637 */       assert (AbstractEpollChannel.this.eventLoop().inEventLoop());
/*     */       
/* 639 */       boolean connectStillInProgress = false;
/*     */       try {
/* 641 */         boolean wasActive = AbstractEpollChannel.this.isActive();
/* 642 */         if (!doFinishConnect()) {
/* 643 */           connectStillInProgress = true;
/* 644 */           return;
/*     */         }
/* 646 */         fulfillConnectPromise(AbstractEpollChannel.this.connectPromise, wasActive);
/*     */       } catch (Throwable t) {
/* 648 */         fulfillConnectPromise(AbstractEpollChannel.this.connectPromise, annotateConnectException(t, AbstractEpollChannel.this.requestedRemoteAddress));
/*     */       } finally {
/* 650 */         if (!connectStillInProgress)
/*     */         {
/*     */ 
/* 653 */           if (AbstractEpollChannel.this.connectTimeoutFuture != null) {
/* 654 */             AbstractEpollChannel.this.connectTimeoutFuture.cancel(false);
/*     */           }
/* 656 */           AbstractEpollChannel.this.connectPromise = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private boolean doFinishConnect()
/*     */       throws Exception
/*     */     {
/* 665 */       if (AbstractEpollChannel.this.socket.finishConnect()) {
/* 666 */         AbstractEpollChannel.this.clearFlag(Native.EPOLLOUT);
/* 667 */         if ((AbstractEpollChannel.this.requestedRemoteAddress instanceof InetSocketAddress)) {
/* 668 */           AbstractEpollChannel.this.remote = UnixChannelUtil.computeRemoteAddr((InetSocketAddress)AbstractEpollChannel.this.requestedRemoteAddress, AbstractEpollChannel.this.socket.remoteAddress());
/*     */         }
/* 670 */         AbstractEpollChannel.this.requestedRemoteAddress = null;
/*     */         
/* 672 */         return true;
/*     */       }
/* 674 */       AbstractEpollChannel.this.setFlag(Native.EPOLLOUT);
/* 675 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress local) throws Exception
/*     */   {
/* 681 */     if ((local instanceof InetSocketAddress)) {
/* 682 */       checkResolvable((InetSocketAddress)local);
/*     */     }
/* 684 */     this.socket.bind(local);
/* 685 */     this.local = this.socket.localAddress();
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 692 */     if ((localAddress instanceof InetSocketAddress)) {
/* 693 */       checkResolvable((InetSocketAddress)localAddress);
/*     */     }
/*     */     
/* 696 */     InetSocketAddress remoteSocketAddr = (remoteAddress instanceof InetSocketAddress) ? (InetSocketAddress)remoteAddress : null;
/*     */     
/* 698 */     if (remoteSocketAddr != null) {
/* 699 */       checkResolvable(remoteSocketAddr);
/*     */     }
/*     */     
/* 702 */     if (this.remote != null)
/*     */     {
/*     */ 
/*     */ 
/* 706 */       throw new AlreadyConnectedException();
/*     */     }
/*     */     
/* 709 */     if (localAddress != null) {
/* 710 */       this.socket.bind(localAddress);
/*     */     }
/*     */     
/* 713 */     boolean connected = doConnect0(remoteAddress);
/* 714 */     if (connected)
/*     */     {
/* 716 */       this.remote = (remoteSocketAddr == null ? remoteAddress : UnixChannelUtil.computeRemoteAddr(remoteSocketAddr, this.socket.remoteAddress()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 721 */     this.local = this.socket.localAddress();
/* 722 */     return connected;
/*     */   }
/*     */   
/*     */   private boolean doConnect0(SocketAddress remote) throws Exception {
/* 726 */     boolean success = false;
/*     */     try {
/* 728 */       boolean connected = this.socket.connect(remote);
/* 729 */       if (!connected) {
/* 730 */         setFlag(Native.EPOLLOUT);
/*     */       }
/* 732 */       success = true;
/* 733 */       return connected;
/*     */     } finally {
/* 735 */       if (!success) {
/* 736 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 743 */     return this.local;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 748 */     return this.remote;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\AbstractEpollChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */