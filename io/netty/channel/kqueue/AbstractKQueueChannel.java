/*     */ package io.netty.channel.kqueue;
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
/*     */ import io.netty.channel.unix.UnixChannel;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AlreadyConnectedException;
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
/*     */ abstract class AbstractKQueueChannel
/*     */   extends AbstractChannel
/*     */   implements UnixChannel
/*     */ {
/*  58 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   
/*     */   private ChannelPromise connectPromise;
/*     */   
/*     */   private ScheduledFuture<?> connectTimeoutFuture;
/*     */   
/*     */   private SocketAddress requestedRemoteAddress;
/*     */   
/*     */   final BsdSocket socket;
/*     */   
/*     */   private boolean readFilterEnabled;
/*     */   
/*     */   private boolean writeFilterEnabled;
/*     */   
/*     */   boolean readReadyRunnablePending;
/*     */   
/*     */   boolean inputClosedSeenErrorOnRead;
/*     */   
/*     */   long jniSelfPtr;
/*     */   
/*     */   protected volatile boolean active;
/*     */   
/*     */   private volatile SocketAddress local;
/*     */   
/*     */   private volatile SocketAddress remote;
/*     */   
/*     */   AbstractKQueueChannel(Channel parent, BsdSocket fd, boolean active)
/*     */   {
/*  86 */     super(parent);
/*  87 */     this.socket = ((BsdSocket)ObjectUtil.checkNotNull(fd, "fd"));
/*  88 */     this.active = active;
/*  89 */     if (active)
/*     */     {
/*     */ 
/*  92 */       this.local = fd.localAddress();
/*  93 */       this.remote = fd.remoteAddress();
/*     */     }
/*     */   }
/*     */   
/*     */   AbstractKQueueChannel(Channel parent, BsdSocket fd, SocketAddress remote) {
/*  98 */     super(parent);
/*  99 */     this.socket = ((BsdSocket)ObjectUtil.checkNotNull(fd, "fd"));
/* 100 */     this.active = true;
/*     */     
/*     */ 
/* 103 */     this.remote = remote;
/* 104 */     this.local = fd.localAddress();
/*     */   }
/*     */   
/*     */   static boolean isSoErrorZero(BsdSocket fd) {
/*     */     try {
/* 109 */       return fd.getSoError() == 0;
/*     */     } catch (IOException e) {
/* 111 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final FileDescriptor fd()
/*     */   {
/* 117 */     return this.socket;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 122 */     return this.active;
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 127 */     return METADATA;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void doClose()
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_0
/*     */     //   2: putfield 13	io/netty/channel/kqueue/AbstractKQueueChannel:active	Z
/*     */     //   5: aload_0
/*     */     //   6: iconst_1
/*     */     //   7: putfield 22	io/netty/channel/kqueue/AbstractKQueueChannel:inputClosedSeenErrorOnRead	Z
/*     */     //   10: aload_0
/*     */     //   11: invokevirtual 23	io/netty/channel/kqueue/AbstractKQueueChannel:isRegistered	()Z
/*     */     //   14: ifeq +38 -> 52
/*     */     //   17: aload_0
/*     */     //   18: invokevirtual 24	io/netty/channel/kqueue/AbstractKQueueChannel:eventLoop	()Lio/netty/channel/EventLoop;
/*     */     //   21: astore_1
/*     */     //   22: aload_1
/*     */     //   23: invokeinterface 25 1 0
/*     */     //   28: ifeq +10 -> 38
/*     */     //   31: aload_0
/*     */     //   32: invokevirtual 26	io/netty/channel/kqueue/AbstractKQueueChannel:doDeregister	()V
/*     */     //   35: goto +17 -> 52
/*     */     //   38: aload_1
/*     */     //   39: new 27	io/netty/channel/kqueue/AbstractKQueueChannel$1
/*     */     //   42: dup
/*     */     //   43: aload_0
/*     */     //   44: invokespecial 28	io/netty/channel/kqueue/AbstractKQueueChannel$1:<init>	(Lio/netty/channel/kqueue/AbstractKQueueChannel;)V
/*     */     //   47: invokeinterface 29 2 0
/*     */     //   52: aload_0
/*     */     //   53: getfield 12	io/netty/channel/kqueue/AbstractKQueueChannel:socket	Lio/netty/channel/kqueue/BsdSocket;
/*     */     //   56: invokevirtual 30	io/netty/channel/kqueue/BsdSocket:close	()V
/*     */     //   59: goto +13 -> 72
/*     */     //   62: astore_2
/*     */     //   63: aload_0
/*     */     //   64: getfield 12	io/netty/channel/kqueue/AbstractKQueueChannel:socket	Lio/netty/channel/kqueue/BsdSocket;
/*     */     //   67: invokevirtual 30	io/netty/channel/kqueue/BsdSocket:close	()V
/*     */     //   70: aload_2
/*     */     //   71: athrow
/*     */     //   72: return
/*     */     // Line number table:
/*     */     //   Java source line #132	-> byte code offset #0
/*     */     //   Java source line #135	-> byte code offset #5
/*     */     //   Java source line #137	-> byte code offset #10
/*     */     //   Java source line #146	-> byte code offset #17
/*     */     //   Java source line #147	-> byte code offset #22
/*     */     //   Java source line #148	-> byte code offset #31
/*     */     //   Java source line #150	-> byte code offset #38
/*     */     //   Java source line #163	-> byte code offset #52
/*     */     //   Java source line #164	-> byte code offset #59
/*     */     //   Java source line #163	-> byte code offset #62
/*     */     //   Java source line #164	-> byte code offset #70
/*     */     //   Java source line #165	-> byte code offset #72
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	73	0	this	AbstractKQueueChannel
/*     */     //   21	18	1	loop	EventLoop
/*     */     //   62	9	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   10	52	62	finally
/*     */   }
/*     */   
/*     */   protected void doDisconnect()
/*     */     throws Exception
/*     */   {
/* 169 */     doClose();
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/* 174 */     return loop instanceof KQueueEventLoop;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 179 */     return this.socket.isOpen();
/*     */   }
/*     */   
/*     */   protected void doDeregister()
/*     */     throws Exception
/*     */   {
/* 185 */     readFilter(false);
/* 186 */     writeFilter(false);
/* 187 */     evSet0(Native.EVFILT_SOCK, Native.EV_DELETE, 0);
/*     */     
/* 189 */     ((KQueueEventLoop)eventLoop()).remove(this);
/*     */   }
/*     */   
/*     */   protected final void doBeginRead()
/*     */     throws Exception
/*     */   {
/* 195 */     AbstractKQueueUnsafe unsafe = (AbstractKQueueUnsafe)unsafe();
/* 196 */     unsafe.readPending = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 201 */     readFilter(true);
/*     */     
/*     */ 
/*     */ 
/* 205 */     if (unsafe.maybeMoreDataToRead) {
/* 206 */       unsafe.executeReadReadyRunnable(config());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doRegister()
/*     */     throws Exception
/*     */   {
/* 215 */     this.readReadyRunnablePending = false;
/*     */     
/* 217 */     if (this.writeFilterEnabled) {
/* 218 */       evSet0(Native.EVFILT_WRITE, Native.EV_ADD_CLEAR_ENABLE);
/*     */     }
/* 220 */     if (this.readFilterEnabled) {
/* 221 */       evSet0(Native.EVFILT_READ, Native.EV_ADD_CLEAR_ENABLE);
/*     */     }
/* 223 */     evSet0(Native.EVFILT_SOCK, Native.EV_ADD, Native.NOTE_RDHUP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract AbstractKQueueUnsafe newUnsafe();
/*     */   
/*     */ 
/*     */   public abstract KQueueChannelConfig config();
/*     */   
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf)
/*     */   {
/* 236 */     return newDirectBuffer(buf, buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(Object holder, ByteBuf buf)
/*     */   {
/* 245 */     int readableBytes = buf.readableBytes();
/* 246 */     if (readableBytes == 0) {
/* 247 */       ReferenceCountUtil.release(holder);
/* 248 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 251 */     ByteBufAllocator alloc = alloc();
/* 252 */     if (alloc.isDirectBufferPooled()) {
/* 253 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 256 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 257 */     if (directBuf == null) {
/* 258 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 261 */     directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 262 */     ReferenceCountUtil.safeRelease(holder);
/* 263 */     return directBuf;
/*     */   }
/*     */   
/*     */   private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
/* 267 */     ByteBuf directBuf = alloc.directBuffer(capacity);
/* 268 */     directBuf.writeBytes(buf, buf.readerIndex(), capacity);
/* 269 */     ReferenceCountUtil.safeRelease(holder);
/* 270 */     return directBuf;
/*     */   }
/*     */   
/*     */   protected static void checkResolvable(InetSocketAddress addr) {
/* 274 */     if (addr.isUnresolved()) {
/* 275 */       throw new UnresolvedAddressException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected final int doReadBytes(ByteBuf byteBuf)
/*     */     throws Exception
/*     */   {
/* 283 */     int writerIndex = byteBuf.writerIndex();
/*     */     
/* 285 */     unsafe().recvBufAllocHandle().attemptedBytesRead(byteBuf.writableBytes());
/* 286 */     int localReadAmount; int localReadAmount; if (byteBuf.hasMemoryAddress()) {
/* 287 */       localReadAmount = this.socket.readAddress(byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
/*     */     } else {
/* 289 */       ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
/* 290 */       localReadAmount = this.socket.read(buf, buf.position(), buf.limit());
/*     */     }
/* 292 */     if (localReadAmount > 0) {
/* 293 */       byteBuf.writerIndex(writerIndex + localReadAmount);
/*     */     }
/* 295 */     return localReadAmount;
/*     */   }
/*     */   
/*     */   protected final int doWriteBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
/* 299 */     if (buf.hasMemoryAddress()) {
/* 300 */       int localFlushedAmount = this.socket.writeAddress(buf.memoryAddress(), buf.readerIndex(), buf.writerIndex());
/* 301 */       if (localFlushedAmount > 0) {
/* 302 */         in.removeBytes(localFlushedAmount);
/* 303 */         return 1;
/*     */       }
/*     */     }
/*     */     else {
/* 307 */       ByteBuffer nioBuf = buf.nioBufferCount() == 1 ? buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()) : buf.nioBuffer();
/* 308 */       int localFlushedAmount = this.socket.write(nioBuf, nioBuf.position(), nioBuf.limit());
/* 309 */       if (localFlushedAmount > 0) {
/* 310 */         nioBuf.position(nioBuf.position() + localFlushedAmount);
/* 311 */         in.removeBytes(localFlushedAmount);
/* 312 */         return 1;
/*     */       }
/*     */     }
/* 315 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   final boolean shouldBreakReadReady(ChannelConfig config) {
/* 319 */     return (this.socket.isInputShutdown()) && ((this.inputClosedSeenErrorOnRead) || (!isAllowHalfClosure(config)));
/*     */   }
/*     */   
/*     */   private static boolean isAllowHalfClosure(ChannelConfig config) {
/* 323 */     return ((config instanceof SocketChannelConfig)) && 
/* 324 */       (((SocketChannelConfig)config).isAllowHalfClosure());
/*     */   }
/*     */   
/*     */   final void clearReadFilter()
/*     */   {
/* 329 */     if (isRegistered()) {
/* 330 */       EventLoop loop = eventLoop();
/* 331 */       final AbstractKQueueUnsafe unsafe = (AbstractKQueueUnsafe)unsafe();
/* 332 */       if (loop.inEventLoop()) {
/* 333 */         unsafe.clearReadFilter0();
/*     */       }
/*     */       else {
/* 336 */         loop.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 339 */             if ((!unsafe.readPending) && (!AbstractKQueueChannel.this.config().isAutoRead()))
/*     */             {
/* 341 */               unsafe.clearReadFilter0();
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 349 */       this.readFilterEnabled = false;
/*     */     }
/*     */   }
/*     */   
/*     */   void readFilter(boolean readFilterEnabled) throws IOException {
/* 354 */     if (this.readFilterEnabled != readFilterEnabled) {
/* 355 */       this.readFilterEnabled = readFilterEnabled;
/* 356 */       evSet(Native.EVFILT_READ, readFilterEnabled ? Native.EV_ADD_CLEAR_ENABLE : Native.EV_DELETE_DISABLE);
/*     */     }
/*     */   }
/*     */   
/*     */   void writeFilter(boolean writeFilterEnabled) throws IOException {
/* 361 */     if (this.writeFilterEnabled != writeFilterEnabled) {
/* 362 */       this.writeFilterEnabled = writeFilterEnabled;
/* 363 */       evSet(Native.EVFILT_WRITE, writeFilterEnabled ? Native.EV_ADD_CLEAR_ENABLE : Native.EV_DELETE_DISABLE);
/*     */     }
/*     */   }
/*     */   
/*     */   private void evSet(short filter, short flags) {
/* 368 */     if ((isOpen()) && (isRegistered())) {
/* 369 */       evSet0(filter, flags);
/*     */     }
/*     */   }
/*     */   
/*     */   private void evSet0(short filter, short flags) {
/* 374 */     evSet0(filter, flags, 0);
/*     */   }
/*     */   
/*     */ 
/* 378 */   private void evSet0(short filter, short flags, int fflags) { ((KQueueEventLoop)eventLoop()).evSet(this, filter, flags, fflags); }
/*     */   
/*     */   abstract class AbstractKQueueUnsafe extends AbstractChannel.AbstractUnsafe { boolean readPending; boolean maybeMoreDataToRead;
/* 381 */     AbstractKQueueUnsafe() { super(); }
/*     */     
/*     */ 
/*     */     private KQueueRecvByteAllocatorHandle allocHandle;
/* 385 */     private final Runnable readReadyRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/* 388 */         AbstractKQueueChannel.this.readReadyRunnablePending = false;
/* 389 */         AbstractKQueueChannel.AbstractKQueueUnsafe.this.readReady(AbstractKQueueChannel.AbstractKQueueUnsafe.this.recvBufAllocHandle());
/*     */       }
/*     */     };
/*     */     
/*     */     final void readReady(long numberBytesPending) {
/* 394 */       KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 395 */       allocHandle.numberBytesPending(numberBytesPending);
/* 396 */       readReady(allocHandle);
/*     */     }
/*     */     
/*     */     abstract void readReady(KQueueRecvByteAllocatorHandle paramKQueueRecvByteAllocatorHandle);
/*     */     
/* 401 */     final void readReadyBefore() { this.maybeMoreDataToRead = false; }
/*     */     
/*     */     final void readReadyFinally(ChannelConfig config) {
/* 404 */       this.maybeMoreDataToRead = this.allocHandle.maybeMoreDataToRead();
/*     */       
/* 406 */       if ((this.allocHandle.isReadEOF()) || ((this.readPending) && (this.maybeMoreDataToRead)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 414 */         executeReadReadyRunnable(config);
/* 415 */       } else if ((!this.readPending) && (!config.isAutoRead()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 422 */         clearReadFilter0();
/*     */       }
/*     */     }
/*     */     
/*     */     final boolean failConnectPromise(Throwable cause) {
/* 427 */       if (AbstractKQueueChannel.this.connectPromise != null)
/*     */       {
/*     */ 
/*     */ 
/* 431 */         ChannelPromise connectPromise = AbstractKQueueChannel.this.connectPromise;
/* 432 */         AbstractKQueueChannel.this.connectPromise = null;
/* 433 */         if (connectPromise.tryFailure((cause instanceof ConnectException) ? cause : new ConnectException("failed to connect")
/* 434 */           .initCause(cause))) {
/* 435 */           closeIfClosed();
/* 436 */           return true;
/*     */         }
/*     */       }
/* 439 */       return false;
/*     */     }
/*     */     
/*     */     final void writeReady() {
/* 443 */       if (AbstractKQueueChannel.this.connectPromise != null)
/*     */       {
/* 445 */         finishConnect();
/* 446 */       } else if (!AbstractKQueueChannel.this.socket.isOutputShutdown())
/*     */       {
/* 448 */         super.flush0();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void shutdownInput(boolean readEOF)
/*     */     {
/* 461 */       if ((readEOF) && (AbstractKQueueChannel.this.connectPromise != null)) {
/* 462 */         finishConnect();
/*     */       }
/* 464 */       if (!AbstractKQueueChannel.this.socket.isInputShutdown()) {
/* 465 */         if (AbstractKQueueChannel.isAllowHalfClosure(AbstractKQueueChannel.this.config())) {
/*     */           try {
/* 467 */             AbstractKQueueChannel.this.socket.shutdown(true, false);
/*     */           }
/*     */           catch (IOException ignored)
/*     */           {
/* 471 */             fireEventAndClose(ChannelInputShutdownEvent.INSTANCE);
/* 472 */             return;
/*     */           }
/*     */           catch (NotYetConnectedException localNotYetConnectedException) {}
/*     */           
/*     */ 
/* 477 */           AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
/*     */         } else {
/* 479 */           close(voidPromise());
/*     */         }
/* 481 */       } else if (!readEOF) {
/* 482 */         AbstractKQueueChannel.this.inputClosedSeenErrorOnRead = true;
/* 483 */         AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(ChannelInputShutdownReadComplete.INSTANCE);
/*     */       }
/*     */     }
/*     */     
/*     */     final void readEOF()
/*     */     {
/* 489 */       KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 490 */       allocHandle.readEOF();
/*     */       
/* 492 */       if (AbstractKQueueChannel.this.isActive())
/*     */       {
/*     */ 
/*     */ 
/* 496 */         readReady(allocHandle);
/*     */       }
/*     */       else {
/* 499 */         shutdownInput(true);
/*     */       }
/*     */     }
/*     */     
/*     */     public KQueueRecvByteAllocatorHandle recvBufAllocHandle()
/*     */     {
/* 505 */       if (this.allocHandle == null)
/*     */       {
/* 507 */         this.allocHandle = new KQueueRecvByteAllocatorHandle((RecvByteBufAllocator.ExtendedHandle)super.recvBufAllocHandle());
/*     */       }
/* 509 */       return this.allocHandle;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final void flush0()
/*     */     {
/* 517 */       if (!AbstractKQueueChannel.this.writeFilterEnabled) {
/* 518 */         super.flush0();
/*     */       }
/*     */     }
/*     */     
/*     */     final void executeReadReadyRunnable(ChannelConfig config) {
/* 523 */       if ((AbstractKQueueChannel.this.readReadyRunnablePending) || (!AbstractKQueueChannel.this.isActive()) || (AbstractKQueueChannel.this.shouldBreakReadReady(config))) {
/* 524 */         return;
/*     */       }
/* 526 */       AbstractKQueueChannel.this.readReadyRunnablePending = true;
/* 527 */       AbstractKQueueChannel.this.eventLoop().execute(this.readReadyRunnable);
/*     */     }
/*     */     
/*     */     protected final void clearReadFilter0() {
/* 531 */       assert (AbstractKQueueChannel.this.eventLoop().inEventLoop());
/*     */       try {
/* 533 */         this.readPending = false;
/* 534 */         AbstractKQueueChannel.this.readFilter(false);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 538 */         AbstractKQueueChannel.this.pipeline().fireExceptionCaught(e);
/* 539 */         AbstractKQueueChannel.this.unsafe().close(AbstractKQueueChannel.this.unsafe().voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fireEventAndClose(Object evt) {
/* 544 */       AbstractKQueueChannel.this.pipeline().fireUserEventTriggered(evt);
/* 545 */       close(voidPromise());
/*     */     }
/*     */     
/*     */ 
/*     */     public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     {
/* 551 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/* 552 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 556 */         if (AbstractKQueueChannel.this.connectPromise != null) {
/* 557 */           throw new ConnectionPendingException();
/*     */         }
/*     */         
/* 560 */         boolean wasActive = AbstractKQueueChannel.this.isActive();
/* 561 */         if (AbstractKQueueChannel.this.doConnect(remoteAddress, localAddress)) {
/* 562 */           fulfillConnectPromise(promise, wasActive);
/*     */         } else {
/* 564 */           AbstractKQueueChannel.this.connectPromise = promise;
/* 565 */           AbstractKQueueChannel.this.requestedRemoteAddress = remoteAddress;
/*     */           
/*     */ 
/* 568 */           int connectTimeoutMillis = AbstractKQueueChannel.this.config().getConnectTimeoutMillis();
/* 569 */           if (connectTimeoutMillis > 0) {
/* 570 */             AbstractKQueueChannel.this.connectTimeoutFuture = AbstractKQueueChannel.this.eventLoop().schedule(new Runnable()
/*     */             {
/*     */               public void run() {
/* 573 */                 ChannelPromise connectPromise = AbstractKQueueChannel.this.connectPromise;
/* 574 */                 ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
/*     */                 
/* 576 */                 if ((connectPromise != null) && (connectPromise.tryFailure(cause)))
/* 577 */                   AbstractKQueueChannel.AbstractKQueueUnsafe.this.close(AbstractKQueueChannel.AbstractKQueueUnsafe.this.voidPromise()); } }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 583 */           promise.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 586 */               if (future.isCancelled()) {
/* 587 */                 if (AbstractKQueueChannel.this.connectTimeoutFuture != null) {
/* 588 */                   AbstractKQueueChannel.this.connectTimeoutFuture.cancel(false);
/*     */                 }
/* 590 */                 AbstractKQueueChannel.this.connectPromise = null;
/* 591 */                 AbstractKQueueChannel.AbstractKQueueUnsafe.this.close(AbstractKQueueChannel.AbstractKQueueUnsafe.this.voidPromise());
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */       } catch (Throwable t) {
/* 597 */         closeIfClosed();
/* 598 */         promise.tryFailure(annotateConnectException(t, remoteAddress));
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
/* 603 */       if (promise == null)
/*     */       {
/* 605 */         return;
/*     */       }
/* 607 */       AbstractKQueueChannel.this.active = true;
/*     */       
/*     */ 
/*     */ 
/* 611 */       boolean active = AbstractKQueueChannel.this.isActive();
/*     */       
/*     */ 
/* 614 */       boolean promiseSet = promise.trySuccess();
/*     */       
/*     */ 
/*     */ 
/* 618 */       if ((!wasActive) && (active)) {
/* 619 */         AbstractKQueueChannel.this.pipeline().fireChannelActive();
/*     */       }
/*     */       
/*     */ 
/* 623 */       if (!promiseSet) {
/* 624 */         close(voidPromise());
/*     */       }
/*     */     }
/*     */     
/*     */     private void fulfillConnectPromise(ChannelPromise promise, Throwable cause) {
/* 629 */       if (promise == null)
/*     */       {
/* 631 */         return;
/*     */       }
/*     */       
/*     */ 
/* 635 */       promise.tryFailure(cause);
/* 636 */       closeIfClosed();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void finishConnect()
/*     */     {
/* 643 */       assert (AbstractKQueueChannel.this.eventLoop().inEventLoop());
/*     */       
/* 645 */       boolean connectStillInProgress = false;
/*     */       try {
/* 647 */         boolean wasActive = AbstractKQueueChannel.this.isActive();
/* 648 */         if (!doFinishConnect()) {
/* 649 */           connectStillInProgress = true;
/* 650 */           return;
/*     */         }
/* 652 */         fulfillConnectPromise(AbstractKQueueChannel.this.connectPromise, wasActive);
/*     */       } catch (Throwable t) {
/* 654 */         fulfillConnectPromise(AbstractKQueueChannel.this.connectPromise, annotateConnectException(t, AbstractKQueueChannel.this.requestedRemoteAddress));
/*     */       } finally {
/* 656 */         if (!connectStillInProgress)
/*     */         {
/*     */ 
/* 659 */           if (AbstractKQueueChannel.this.connectTimeoutFuture != null) {
/* 660 */             AbstractKQueueChannel.this.connectTimeoutFuture.cancel(false);
/*     */           }
/* 662 */           AbstractKQueueChannel.this.connectPromise = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean doFinishConnect() throws Exception {
/* 668 */       if (AbstractKQueueChannel.this.socket.finishConnect()) {
/* 669 */         AbstractKQueueChannel.this.writeFilter(false);
/* 670 */         if ((AbstractKQueueChannel.this.requestedRemoteAddress instanceof InetSocketAddress)) {
/* 671 */           AbstractKQueueChannel.this.remote = UnixChannelUtil.computeRemoteAddr((InetSocketAddress)AbstractKQueueChannel.this.requestedRemoteAddress, AbstractKQueueChannel.this.socket.remoteAddress());
/*     */         }
/* 673 */         AbstractKQueueChannel.this.requestedRemoteAddress = null;
/* 674 */         return true;
/*     */       }
/* 676 */       AbstractKQueueChannel.this.writeFilter(true);
/* 677 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress local) throws Exception
/*     */   {
/* 683 */     if ((local instanceof InetSocketAddress)) {
/* 684 */       checkResolvable((InetSocketAddress)local);
/*     */     }
/* 686 */     this.socket.bind(local);
/* 687 */     this.local = this.socket.localAddress();
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 694 */     if ((localAddress instanceof InetSocketAddress)) {
/* 695 */       checkResolvable((InetSocketAddress)localAddress);
/*     */     }
/*     */     
/* 698 */     InetSocketAddress remoteSocketAddr = (remoteAddress instanceof InetSocketAddress) ? (InetSocketAddress)remoteAddress : null;
/*     */     
/* 700 */     if (remoteSocketAddr != null) {
/* 701 */       checkResolvable(remoteSocketAddr);
/*     */     }
/*     */     
/* 704 */     if (this.remote != null)
/*     */     {
/*     */ 
/*     */ 
/* 708 */       throw new AlreadyConnectedException();
/*     */     }
/*     */     
/* 711 */     if (localAddress != null) {
/* 712 */       this.socket.bind(localAddress);
/*     */     }
/*     */     
/* 715 */     boolean connected = doConnect0(remoteAddress);
/* 716 */     if (connected)
/*     */     {
/* 718 */       this.remote = (remoteSocketAddr == null ? remoteAddress : UnixChannelUtil.computeRemoteAddr(remoteSocketAddr, this.socket.remoteAddress()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 723 */     this.local = this.socket.localAddress();
/* 724 */     return connected;
/*     */   }
/*     */   
/*     */   private boolean doConnect0(SocketAddress remote) throws Exception {
/* 728 */     boolean success = false;
/*     */     try {
/* 730 */       boolean connected = this.socket.connect(remote);
/* 731 */       if (!connected) {
/* 732 */         writeFilter(true);
/*     */       }
/* 734 */       success = true;
/* 735 */       return connected;
/*     */     } finally {
/* 737 */       if (!success) {
/* 738 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 745 */     return this.local;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 750 */     return this.remote;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\AbstractKQueueChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */