/*      */ package io.netty.channel.epoll;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.Channel.Unsafe;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelMetadata;
/*      */ import io.netty.channel.ChannelOutboundBuffer;
/*      */ import io.netty.channel.ChannelPipeline;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.DefaultFileRegion;
/*      */ import io.netty.channel.EventLoop;
/*      */ import io.netty.channel.FileRegion;
/*      */ import io.netty.channel.RecvByteBufAllocator.ExtendedHandle;
/*      */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*      */ import io.netty.channel.socket.DuplexChannel;
/*      */ import io.netty.channel.unix.FileDescriptor;
/*      */ import io.netty.channel.unix.IovArray;
/*      */ import io.netty.channel.unix.SocketWritableByteChannel;
/*      */ import io.netty.channel.unix.UnixChannelUtil;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.WritableByteChannel;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.Executor;
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
/*      */ public abstract class AbstractEpollStreamChannel
/*      */   extends AbstractEpollChannel
/*      */   implements DuplexChannel
/*      */ {
/*   59 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*   60 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*   61 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*   62 */     StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
/*   63 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractEpollStreamChannel.class);
/*      */   
/*   65 */   private static final ClosedChannelException CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "clearSpliceQueue()");
/*      */   
/*   67 */   private static final ClosedChannelException SPLICE_TO_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "spliceTo(...)");
/*      */   
/*      */ 
/*      */ 
/*   71 */   private static final ClosedChannelException FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "failSpliceIfClosed(...)");
/*      */   
/*   73 */   private final Runnable flushTask = new Runnable()
/*      */   {
/*      */ 
/*      */     public void run()
/*      */     {
/*   78 */       ((AbstractEpollChannel.AbstractEpollUnsafe)AbstractEpollStreamChannel.this.unsafe()).flush0();
/*      */     }
/*      */   };
/*      */   
/*      */   private Queue<SpliceInTask> spliceQueue;
/*      */   
/*      */   private FileDescriptor pipeIn;
/*      */   private FileDescriptor pipeOut;
/*      */   private WritableByteChannel byteChannel;
/*      */   
/*      */   protected AbstractEpollStreamChannel(Channel parent, int fd)
/*      */   {
/*   90 */     this(parent, new LinuxSocket(fd));
/*      */   }
/*      */   
/*      */   protected AbstractEpollStreamChannel(int fd) {
/*   94 */     this(new LinuxSocket(fd));
/*      */   }
/*      */   
/*      */   AbstractEpollStreamChannel(LinuxSocket fd) {
/*   98 */     this(fd, isSoErrorZero(fd));
/*      */   }
/*      */   
/*      */   AbstractEpollStreamChannel(Channel parent, LinuxSocket fd) {
/*  102 */     super(parent, fd, true);
/*      */     
/*  104 */     this.flags |= Native.EPOLLRDHUP;
/*      */   }
/*      */   
/*      */   AbstractEpollStreamChannel(Channel parent, LinuxSocket fd, SocketAddress remote) {
/*  108 */     super(parent, fd, remote);
/*      */     
/*  110 */     this.flags |= Native.EPOLLRDHUP;
/*      */   }
/*      */   
/*      */   protected AbstractEpollStreamChannel(LinuxSocket fd, boolean active) {
/*  114 */     super(null, fd, active);
/*      */     
/*  116 */     this.flags |= Native.EPOLLRDHUP;
/*      */   }
/*      */   
/*      */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*      */   {
/*  121 */     return new EpollStreamUnsafe();
/*      */   }
/*      */   
/*      */   public ChannelMetadata metadata()
/*      */   {
/*  126 */     return METADATA;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch, int len)
/*      */   {
/*  144 */     return spliceTo(ch, len, newPromise());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch, int len, ChannelPromise promise)
/*      */   {
/*  163 */     if (ch.eventLoop() != eventLoop()) {
/*  164 */       throw new IllegalArgumentException("EventLoops are not the same.");
/*      */     }
/*  166 */     if (len < 0) {
/*  167 */       throw new IllegalArgumentException("len: " + len + " (expected: >= 0)");
/*      */     }
/*  169 */     if ((ch.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) || 
/*  170 */       (config().getEpollMode() != EpollMode.LEVEL_TRIGGERED)) {
/*  171 */       throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
/*      */     }
/*  173 */     ObjectUtil.checkNotNull(promise, "promise");
/*  174 */     if (!isOpen()) {
/*  175 */       promise.tryFailure(SPLICE_TO_CLOSED_CHANNEL_EXCEPTION);
/*      */     } else {
/*  177 */       addToSpliceQueue(new SpliceInChannelTask(ch, len, promise));
/*  178 */       failSpliceIfClosed(promise);
/*      */     }
/*  180 */     return promise;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ChannelFuture spliceTo(FileDescriptor ch, int offset, int len)
/*      */   {
/*  198 */     return spliceTo(ch, offset, len, newPromise());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ChannelFuture spliceTo(FileDescriptor ch, int offset, int len, ChannelPromise promise)
/*      */   {
/*  217 */     if (len < 0) {
/*  218 */       throw new IllegalArgumentException("len: " + len + " (expected: >= 0)");
/*      */     }
/*  220 */     if (offset < 0) {
/*  221 */       throw new IllegalArgumentException("offset must be >= 0 but was " + offset);
/*      */     }
/*  223 */     if (config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
/*  224 */       throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
/*      */     }
/*  226 */     ObjectUtil.checkNotNull(promise, "promise");
/*  227 */     if (!isOpen()) {
/*  228 */       promise.tryFailure(SPLICE_TO_CLOSED_CHANNEL_EXCEPTION);
/*      */     } else {
/*  230 */       addToSpliceQueue(new SpliceFdTask(ch, offset, len, promise));
/*  231 */       failSpliceIfClosed(promise);
/*      */     }
/*  233 */     return promise;
/*      */   }
/*      */   
/*      */   private void failSpliceIfClosed(ChannelPromise promise) {
/*  237 */     if (!isOpen())
/*      */     {
/*      */ 
/*  240 */       if (promise.tryFailure(FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION)) {
/*  241 */         eventLoop().execute(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  245 */             AbstractEpollStreamChannel.this.clearSpliceQueue();
/*      */           }
/*      */         });
/*      */       }
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writeBytes(ChannelOutboundBuffer in, ByteBuf buf)
/*      */     throws Exception
/*      */   {
/*  267 */     int readableBytes = buf.readableBytes();
/*  268 */     if (readableBytes == 0) {
/*  269 */       in.remove();
/*  270 */       return 0;
/*      */     }
/*      */     
/*  273 */     if ((buf.hasMemoryAddress()) || (buf.nioBufferCount() == 1)) {
/*  274 */       return doWriteBytes(in, buf);
/*      */     }
/*  276 */     ByteBuffer[] nioBuffers = buf.nioBuffers();
/*  277 */     return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, 
/*  278 */       config().getMaxBytesPerGatheringWrite());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void adjustMaxBytesPerGatheringWrite(long attempted, long written, long oldMaxBytesPerGatheringWrite)
/*      */   {
/*  286 */     if (attempted == written) {
/*  287 */       if (attempted << 1 > oldMaxBytesPerGatheringWrite) {
/*  288 */         config().setMaxBytesPerGatheringWrite(attempted << 1);
/*      */       }
/*  290 */     } else if ((attempted > 4096L) && (written < attempted >>> 1)) {
/*  291 */       config().setMaxBytesPerGatheringWrite(attempted >>> 1);
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writeBytesMultiple(ChannelOutboundBuffer in, IovArray array)
/*      */     throws IOException
/*      */   {
/*  311 */     long expectedWrittenBytes = array.size();
/*  312 */     assert (expectedWrittenBytes != 0L);
/*  313 */     int cnt = array.count();
/*  314 */     assert (cnt != 0);
/*      */     
/*  316 */     long localWrittenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/*  317 */     if (localWrittenBytes > 0L) {
/*  318 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, array.maxBytes());
/*  319 */       in.removeBytes(localWrittenBytes);
/*  320 */       return 1;
/*      */     }
/*  322 */     return Integer.MAX_VALUE;
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
/*      */   private int writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, long maxBytesPerGatheringWrite)
/*      */     throws IOException
/*      */   {
/*  346 */     assert (expectedWrittenBytes != 0L);
/*  347 */     if (expectedWrittenBytes > maxBytesPerGatheringWrite) {
/*  348 */       expectedWrittenBytes = maxBytesPerGatheringWrite;
/*      */     }
/*      */     
/*  351 */     long localWrittenBytes = this.socket.writev(nioBuffers, 0, nioBufferCnt, expectedWrittenBytes);
/*  352 */     if (localWrittenBytes > 0L) {
/*  353 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, maxBytesPerGatheringWrite);
/*  354 */       in.removeBytes(localWrittenBytes);
/*  355 */       return 1;
/*      */     }
/*  357 */     return Integer.MAX_VALUE;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writeDefaultFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region)
/*      */     throws Exception
/*      */   {
/*  375 */     long regionCount = region.count();
/*  376 */     if (region.transferred() >= regionCount) {
/*  377 */       in.remove();
/*  378 */       return 0;
/*      */     }
/*      */     
/*  381 */     long offset = region.transferred();
/*  382 */     long flushedAmount = this.socket.sendFile(region, region.position(), offset, regionCount - offset);
/*  383 */     if (flushedAmount > 0L) {
/*  384 */       in.progress(flushedAmount);
/*  385 */       if (region.transferred() >= regionCount) {
/*  386 */         in.remove();
/*      */       }
/*  388 */       return 1;
/*      */     }
/*  390 */     return Integer.MAX_VALUE;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writeFileRegion(ChannelOutboundBuffer in, FileRegion region)
/*      */     throws Exception
/*      */   {
/*  408 */     if (region.transferred() >= region.count()) {
/*  409 */       in.remove();
/*  410 */       return 0;
/*      */     }
/*      */     
/*  413 */     if (this.byteChannel == null) {
/*  414 */       this.byteChannel = new EpollSocketWritableByteChannel();
/*      */     }
/*  416 */     long flushedAmount = region.transferTo(this.byteChannel, region.transferred());
/*  417 */     if (flushedAmount > 0L) {
/*  418 */       in.progress(flushedAmount);
/*  419 */       if (region.transferred() >= region.count()) {
/*  420 */         in.remove();
/*      */       }
/*  422 */       return 1;
/*      */     }
/*  424 */     return Integer.MAX_VALUE;
/*      */   }
/*      */   
/*      */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*      */   {
/*  429 */     int writeSpinCount = config().getWriteSpinCount();
/*      */     do {
/*  431 */       int msgCount = in.size();
/*      */       
/*  433 */       if ((msgCount > 1) && ((in.current() instanceof ByteBuf))) {
/*  434 */         writeSpinCount -= doWriteMultiple(in);
/*  435 */       } else { if (msgCount == 0)
/*      */         {
/*  437 */           clearFlag(Native.EPOLLOUT);
/*      */           
/*  439 */           return;
/*      */         }
/*  441 */         writeSpinCount -= doWriteSingle(in);
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  447 */     while (writeSpinCount > 0);
/*      */     
/*  449 */     if (writeSpinCount == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  454 */       clearFlag(Native.EPOLLOUT);
/*      */       
/*      */ 
/*  457 */       eventLoop().execute(this.flushTask);
/*      */     }
/*      */     else
/*      */     {
/*  461 */       setFlag(Native.EPOLLOUT);
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int doWriteSingle(ChannelOutboundBuffer in)
/*      */     throws Exception
/*      */   {
/*  481 */     Object msg = in.current();
/*  482 */     if ((msg instanceof ByteBuf))
/*  483 */       return writeBytes(in, (ByteBuf)msg);
/*  484 */     if ((msg instanceof DefaultFileRegion))
/*  485 */       return writeDefaultFileRegion(in, (DefaultFileRegion)msg);
/*  486 */     if ((msg instanceof FileRegion))
/*  487 */       return writeFileRegion(in, (FileRegion)msg);
/*  488 */     if ((msg instanceof SpliceOutTask)) {
/*  489 */       if (!((SpliceOutTask)msg).spliceOut()) {
/*  490 */         return Integer.MAX_VALUE;
/*      */       }
/*  492 */       in.remove();
/*  493 */       return 1;
/*      */     }
/*      */     
/*  496 */     throw new Error();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int doWriteMultiple(ChannelOutboundBuffer in)
/*      */     throws Exception
/*      */   {
/*  515 */     long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
/*  516 */     IovArray array = ((EpollEventLoop)eventLoop()).cleanIovArray();
/*  517 */     array.maxBytes(maxBytesPerGatheringWrite);
/*  518 */     in.forEachFlushedMessage(array);
/*      */     
/*  520 */     if (array.count() >= 1)
/*      */     {
/*  522 */       return writeBytesMultiple(in, array);
/*      */     }
/*      */     
/*  525 */     in.removeBytes(0L);
/*  526 */     return 0;
/*      */   }
/*      */   
/*      */   protected Object filterOutboundMessage(Object msg)
/*      */   {
/*  531 */     if ((msg instanceof ByteBuf)) {
/*  532 */       ByteBuf buf = (ByteBuf)msg;
/*  533 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*      */     }
/*      */     
/*  536 */     if (((msg instanceof FileRegion)) || ((msg instanceof SpliceOutTask))) {
/*  537 */       return msg;
/*      */     }
/*      */     
/*      */ 
/*  541 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*      */   }
/*      */   
/*      */   protected final void doShutdownOutput()
/*      */     throws Exception
/*      */   {
/*  547 */     this.socket.shutdown(false, true);
/*      */   }
/*      */   
/*      */   private void shutdownInput0(ChannelPromise promise) {
/*      */     try {
/*  552 */       this.socket.shutdown(true, false);
/*  553 */       promise.setSuccess();
/*      */     } catch (Throwable cause) {
/*  555 */       promise.setFailure(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isOutputShutdown()
/*      */   {
/*  561 */     return this.socket.isOutputShutdown();
/*      */   }
/*      */   
/*      */   public boolean isInputShutdown()
/*      */   {
/*  566 */     return this.socket.isInputShutdown();
/*      */   }
/*      */   
/*      */   public boolean isShutdown()
/*      */   {
/*  571 */     return this.socket.isShutdown();
/*      */   }
/*      */   
/*      */   public ChannelFuture shutdownOutput()
/*      */   {
/*  576 */     return shutdownOutput(newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture shutdownOutput(final ChannelPromise promise)
/*      */   {
/*  581 */     EventLoop loop = eventLoop();
/*  582 */     if (loop.inEventLoop()) {
/*  583 */       ((AbstractChannel.AbstractUnsafe)unsafe()).shutdownOutput(promise);
/*      */     } else {
/*  585 */       loop.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  588 */           ((AbstractChannel.AbstractUnsafe)AbstractEpollStreamChannel.this.unsafe()).shutdownOutput(promise);
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*  593 */     return promise;
/*      */   }
/*      */   
/*      */   public ChannelFuture shutdownInput()
/*      */   {
/*  598 */     return shutdownInput(newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture shutdownInput(final ChannelPromise promise)
/*      */   {
/*  603 */     Executor closeExecutor = ((EpollStreamUnsafe)unsafe()).prepareToClose();
/*  604 */     if (closeExecutor != null) {
/*  605 */       closeExecutor.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  608 */           AbstractEpollStreamChannel.this.shutdownInput0(promise);
/*      */         }
/*      */       });
/*      */     } else {
/*  612 */       EventLoop loop = eventLoop();
/*  613 */       if (loop.inEventLoop()) {
/*  614 */         shutdownInput0(promise);
/*      */       } else {
/*  616 */         loop.execute(new Runnable()
/*      */         {
/*      */           public void run() {
/*  619 */             AbstractEpollStreamChannel.this.shutdownInput0(promise);
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*  624 */     return promise;
/*      */   }
/*      */   
/*      */   public ChannelFuture shutdown()
/*      */   {
/*  629 */     return shutdown(newPromise());
/*      */   }
/*      */   
/*      */   public ChannelFuture shutdown(final ChannelPromise promise)
/*      */   {
/*  634 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/*  635 */     if (shutdownOutputFuture.isDone()) {
/*  636 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*      */     } else {
/*  638 */       shutdownOutputFuture.addListener(new ChannelFutureListener()
/*      */       {
/*      */         public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/*  641 */           AbstractEpollStreamChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*      */         }
/*      */       });
/*      */     }
/*  645 */     return promise;
/*      */   }
/*      */   
/*      */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/*  649 */     ChannelFuture shutdownInputFuture = shutdownInput();
/*  650 */     if (shutdownInputFuture.isDone()) {
/*  651 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*      */     } else {
/*  653 */       shutdownInputFuture.addListener(new ChannelFutureListener()
/*      */       {
/*      */         public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/*  656 */           AbstractEpollStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise)
/*      */   {
/*  665 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/*  666 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/*  667 */     if (shutdownOutputCause != null) {
/*  668 */       if (shutdownInputCause != null) {
/*  669 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*      */       }
/*      */       
/*  672 */       promise.setFailure(shutdownOutputCause);
/*  673 */     } else if (shutdownInputCause != null) {
/*  674 */       promise.setFailure(shutdownInputCause);
/*      */     } else {
/*  676 */       promise.setSuccess();
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected void doClose()
/*      */     throws Exception
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokespecial 163	io/netty/channel/epoll/AbstractEpollChannel:doClose	()V
/*      */     //   4: aload_0
/*      */     //   5: getfield 3	io/netty/channel/epoll/AbstractEpollStreamChannel:pipeIn	Lio/netty/channel/unix/FileDescriptor;
/*      */     //   8: invokestatic 2	io/netty/channel/epoll/AbstractEpollStreamChannel:safeClosePipe	(Lio/netty/channel/unix/FileDescriptor;)V
/*      */     //   11: aload_0
/*      */     //   12: getfield 4	io/netty/channel/epoll/AbstractEpollStreamChannel:pipeOut	Lio/netty/channel/unix/FileDescriptor;
/*      */     //   15: invokestatic 2	io/netty/channel/epoll/AbstractEpollStreamChannel:safeClosePipe	(Lio/netty/channel/unix/FileDescriptor;)V
/*      */     //   18: aload_0
/*      */     //   19: invokespecial 10	io/netty/channel/epoll/AbstractEpollStreamChannel:clearSpliceQueue	()V
/*      */     //   22: goto +24 -> 46
/*      */     //   25: astore_1
/*      */     //   26: aload_0
/*      */     //   27: getfield 3	io/netty/channel/epoll/AbstractEpollStreamChannel:pipeIn	Lio/netty/channel/unix/FileDescriptor;
/*      */     //   30: invokestatic 2	io/netty/channel/epoll/AbstractEpollStreamChannel:safeClosePipe	(Lio/netty/channel/unix/FileDescriptor;)V
/*      */     //   33: aload_0
/*      */     //   34: getfield 4	io/netty/channel/epoll/AbstractEpollStreamChannel:pipeOut	Lio/netty/channel/unix/FileDescriptor;
/*      */     //   37: invokestatic 2	io/netty/channel/epoll/AbstractEpollStreamChannel:safeClosePipe	(Lio/netty/channel/unix/FileDescriptor;)V
/*      */     //   40: aload_0
/*      */     //   41: invokespecial 10	io/netty/channel/epoll/AbstractEpollStreamChannel:clearSpliceQueue	()V
/*      */     //   44: aload_1
/*      */     //   45: athrow
/*      */     //   46: return
/*      */     // Line number table:
/*      */     //   Java source line #684	-> byte code offset #0
/*      */     //   Java source line #686	-> byte code offset #4
/*      */     //   Java source line #687	-> byte code offset #11
/*      */     //   Java source line #688	-> byte code offset #18
/*      */     //   Java source line #689	-> byte code offset #22
/*      */     //   Java source line #686	-> byte code offset #25
/*      */     //   Java source line #687	-> byte code offset #33
/*      */     //   Java source line #688	-> byte code offset #40
/*      */     //   Java source line #689	-> byte code offset #44
/*      */     //   Java source line #690	-> byte code offset #46
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	47	0	this	AbstractEpollStreamChannel
/*      */     //   25	20	1	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	4	25	finally
/*      */   }
/*      */   
/*      */   private void clearSpliceQueue()
/*      */   {
/*  693 */     if (this.spliceQueue == null) {
/*      */       return;
/*      */     }
/*      */     for (;;) {
/*  697 */       SpliceInTask task = (SpliceInTask)this.spliceQueue.poll();
/*  698 */       if (task == null) {
/*      */         break;
/*      */       }
/*  701 */       task.promise.tryFailure(CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void safeClosePipe(FileDescriptor fd) {
/*  706 */     if (fd != null)
/*      */       try {
/*  708 */         fd.close();
/*      */       } catch (IOException e) {
/*  710 */         if (logger.isWarnEnabled())
/*  711 */           logger.warn("Error while closing a pipe", e);
/*      */       }
/*      */   }
/*      */   
/*      */   class EpollStreamUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
/*      */     EpollStreamUnsafe() {
/*  717 */       super();
/*      */     }
/*      */     
/*      */     protected Executor prepareToClose() {
/*  721 */       return super.prepareToClose();
/*      */     }
/*      */     
/*      */     private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, EpollRecvByteAllocatorHandle allocHandle)
/*      */     {
/*  726 */       if (byteBuf != null) {
/*  727 */         if (byteBuf.isReadable()) {
/*  728 */           this.readPending = false;
/*  729 */           pipeline.fireChannelRead(byteBuf);
/*      */         } else {
/*  731 */           byteBuf.release();
/*      */         }
/*      */       }
/*  734 */       allocHandle.readComplete();
/*  735 */       pipeline.fireChannelReadComplete();
/*  736 */       pipeline.fireExceptionCaught(cause);
/*  737 */       if ((close) || ((cause instanceof IOException))) {
/*  738 */         shutdownInput(false);
/*      */       }
/*      */     }
/*      */     
/*      */     EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle)
/*      */     {
/*  744 */       return new EpollRecvByteAllocatorStreamingHandle(handle);
/*      */     }
/*      */     
/*      */     void epollInReady()
/*      */     {
/*  749 */       ChannelConfig config = AbstractEpollStreamChannel.this.config();
/*  750 */       if (AbstractEpollStreamChannel.this.shouldBreakEpollInReady(config)) {
/*  751 */         clearEpollIn0();
/*  752 */         return;
/*      */       }
/*  754 */       EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/*  755 */       allocHandle.edgeTriggered(AbstractEpollStreamChannel.this.isFlagSet(Native.EPOLLET));
/*      */       
/*  757 */       ChannelPipeline pipeline = AbstractEpollStreamChannel.this.pipeline();
/*  758 */       ByteBufAllocator allocator = config.getAllocator();
/*  759 */       allocHandle.reset(config);
/*  760 */       epollInBefore();
/*      */       
/*  762 */       ByteBuf byteBuf = null;
/*  763 */       boolean close = false;
/*      */       try {
/*      */         do {
/*  766 */           if (AbstractEpollStreamChannel.this.spliceQueue != null) {
/*  767 */             AbstractEpollStreamChannel.SpliceInTask spliceTask = (AbstractEpollStreamChannel.SpliceInTask)AbstractEpollStreamChannel.this.spliceQueue.peek();
/*  768 */             if (spliceTask != null) {
/*  769 */               if (!spliceTask.spliceIn(allocHandle)) {
/*      */                 break;
/*      */               }
/*  772 */               if (!AbstractEpollStreamChannel.this.isActive()) continue;
/*  773 */               AbstractEpollStreamChannel.this.spliceQueue.remove(); continue;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  784 */           byteBuf = allocHandle.allocate(allocator);
/*  785 */           allocHandle.lastBytesRead(AbstractEpollStreamChannel.this.doReadBytes(byteBuf));
/*  786 */           if (allocHandle.lastBytesRead() <= 0)
/*      */           {
/*  788 */             byteBuf.release();
/*  789 */             byteBuf = null;
/*  790 */             close = allocHandle.lastBytesRead() < 0;
/*  791 */             if (close)
/*      */             {
/*  793 */               this.readPending = false;
/*      */             }
/*      */           }
/*      */           else {
/*  797 */             allocHandle.incMessagesRead(1);
/*  798 */             this.readPending = false;
/*  799 */             pipeline.fireChannelRead(byteBuf);
/*  800 */             byteBuf = null;
/*      */             
/*  802 */             if (AbstractEpollStreamChannel.this.shouldBreakEpollInReady(config))
/*      */             {
/*      */ 
/*      */               break;
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*  816 */         while (allocHandle.continueReading());
/*      */         
/*  818 */         allocHandle.readComplete();
/*  819 */         pipeline.fireChannelReadComplete();
/*      */         
/*  821 */         if (close) {
/*  822 */           shutdownInput(false);
/*      */         }
/*      */       } catch (Throwable t) {
/*  825 */         handleReadException(pipeline, byteBuf, t, close, allocHandle);
/*      */       } finally {
/*  827 */         epollInFinally(config);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void addToSpliceQueue(final SpliceInTask task) {
/*  833 */     EventLoop eventLoop = eventLoop();
/*  834 */     if (eventLoop.inEventLoop()) {
/*  835 */       addToSpliceQueue0(task);
/*      */     } else {
/*  837 */       eventLoop.execute(new Runnable()
/*      */       {
/*      */         public void run() {
/*  840 */           AbstractEpollStreamChannel.this.addToSpliceQueue0(task);
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */   private void addToSpliceQueue0(SpliceInTask task) {
/*  847 */     if (this.spliceQueue == null) {
/*  848 */       this.spliceQueue = PlatformDependent.newMpscQueue();
/*      */     }
/*  850 */     this.spliceQueue.add(task);
/*      */   }
/*      */   
/*      */   protected abstract class SpliceInTask {
/*      */     final ChannelPromise promise;
/*      */     int len;
/*      */     
/*      */     protected SpliceInTask(int len, ChannelPromise promise) {
/*  858 */       this.promise = promise;
/*  859 */       this.len = len;
/*      */     }
/*      */     
/*      */     abstract boolean spliceIn(RecvByteBufAllocator.Handle paramHandle);
/*      */     
/*      */     protected final int spliceIn(FileDescriptor pipeOut, RecvByteBufAllocator.Handle handle) throws IOException
/*      */     {
/*  866 */       int length = Math.min(handle.guess(), this.len);
/*  867 */       int splicedIn = 0;
/*      */       for (;;)
/*      */       {
/*  870 */         int localSplicedIn = Native.splice(AbstractEpollStreamChannel.this.socket.intValue(), -1L, pipeOut.intValue(), -1L, length);
/*  871 */         if (localSplicedIn == 0) {
/*      */           break;
/*      */         }
/*  874 */         splicedIn += localSplicedIn;
/*  875 */         length -= localSplicedIn;
/*      */       }
/*      */       
/*  878 */       return splicedIn;
/*      */     }
/*      */   }
/*      */   
/*      */   private final class SpliceInChannelTask extends AbstractEpollStreamChannel.SpliceInTask implements ChannelFutureListener
/*      */   {
/*      */     private final AbstractEpollStreamChannel ch;
/*      */     
/*      */     SpliceInChannelTask(AbstractEpollStreamChannel ch, int len, ChannelPromise promise) {
/*  887 */       super(len, promise);
/*  888 */       this.ch = ch;
/*      */     }
/*      */     
/*      */     public void operationComplete(ChannelFuture future) throws Exception
/*      */     {
/*  893 */       if (!future.isSuccess()) {
/*  894 */         this.promise.setFailure(future.cause());
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean spliceIn(RecvByteBufAllocator.Handle handle)
/*      */     {
/*  900 */       assert (this.ch.eventLoop().inEventLoop());
/*  901 */       if (this.len == 0) {
/*  902 */         this.promise.setSuccess();
/*  903 */         return true;
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  909 */         FileDescriptor pipeOut = this.ch.pipeOut;
/*  910 */         if (pipeOut == null)
/*      */         {
/*  912 */           FileDescriptor[] pipe = FileDescriptor.pipe();
/*  913 */           this.ch.pipeIn = pipe[0];
/*  914 */           pipeOut = this.ch.pipeOut = pipe[1];
/*      */         }
/*      */         
/*  917 */         int splicedIn = spliceIn(pipeOut, handle);
/*  918 */         if (splicedIn > 0)
/*      */         {
/*  920 */           if (this.len != Integer.MAX_VALUE) {
/*  921 */             this.len -= splicedIn;
/*      */           }
/*      */           
/*      */           ChannelPromise splicePromise;
/*      */           
/*      */           ChannelPromise splicePromise;
/*  927 */           if (this.len == 0) {
/*  928 */             splicePromise = this.promise;
/*      */           } else {
/*  930 */             splicePromise = this.ch.newPromise().addListener(this);
/*      */           }
/*      */           
/*  933 */           boolean autoRead = AbstractEpollStreamChannel.this.config().isAutoRead();
/*      */           
/*      */ 
/*      */ 
/*  937 */           this.ch.unsafe().write(new AbstractEpollStreamChannel.SpliceOutTask(AbstractEpollStreamChannel.this, this.ch, splicedIn, autoRead), splicePromise);
/*  938 */           this.ch.unsafe().flush();
/*  939 */           if ((autoRead) && (!splicePromise.isDone()))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  944 */             AbstractEpollStreamChannel.this.config().setAutoRead(false);
/*      */           }
/*      */         }
/*      */         
/*  948 */         return this.len == 0;
/*      */       } catch (Throwable cause) {
/*  950 */         this.promise.setFailure(cause); }
/*  951 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   private final class SpliceOutTask
/*      */   {
/*      */     private final AbstractEpollStreamChannel ch;
/*      */     private final boolean autoRead;
/*      */     private int len;
/*      */     
/*      */     SpliceOutTask(AbstractEpollStreamChannel ch, int len, boolean autoRead) {
/*  962 */       this.ch = ch;
/*  963 */       this.len = len;
/*  964 */       this.autoRead = autoRead;
/*      */     }
/*      */     
/*      */     public boolean spliceOut() throws Exception {
/*  968 */       assert (this.ch.eventLoop().inEventLoop());
/*      */       try {
/*  970 */         int splicedOut = Native.splice(this.ch.pipeIn.intValue(), -1L, this.ch.socket.intValue(), -1L, this.len);
/*  971 */         this.len -= splicedOut;
/*  972 */         if (this.len == 0) {
/*  973 */           if (this.autoRead)
/*      */           {
/*  975 */             AbstractEpollStreamChannel.this.config().setAutoRead(true);
/*      */           }
/*  977 */           return true;
/*      */         }
/*  979 */         return false;
/*      */       } catch (IOException e) {
/*  981 */         if (this.autoRead)
/*      */         {
/*  983 */           AbstractEpollStreamChannel.this.config().setAutoRead(true);
/*      */         }
/*  985 */         throw e;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final class SpliceFdTask extends AbstractEpollStreamChannel.SpliceInTask {
/*      */     private final FileDescriptor fd;
/*      */     private final ChannelPromise promise;
/*      */     private final int offset;
/*      */     
/*      */     SpliceFdTask(FileDescriptor fd, int offset, int len, ChannelPromise promise) {
/*  996 */       super(len, promise);
/*  997 */       this.fd = fd;
/*  998 */       this.promise = promise;
/*  999 */       this.offset = offset;
/*      */     }
/*      */     
/*      */     public boolean spliceIn(RecvByteBufAllocator.Handle handle)
/*      */     {
/* 1004 */       assert (AbstractEpollStreamChannel.this.eventLoop().inEventLoop());
/* 1005 */       if (this.len == 0) {
/* 1006 */         this.promise.setSuccess();
/* 1007 */         return true;
/*      */       }
/*      */       try
/*      */       {
/* 1011 */         FileDescriptor[] pipe = FileDescriptor.pipe();
/* 1012 */         FileDescriptor pipeIn = pipe[0];
/* 1013 */         FileDescriptor pipeOut = pipe[1];
/*      */         try {
/* 1015 */           int splicedIn = spliceIn(pipeOut, handle);
/* 1016 */           int splicedOut; if (splicedIn > 0)
/*      */           {
/* 1018 */             if (this.len != Integer.MAX_VALUE) {
/* 1019 */               this.len -= splicedIn;
/*      */             }
/*      */             do {
/* 1022 */               splicedOut = Native.splice(pipeIn.intValue(), -1L, this.fd.intValue(), this.offset, splicedIn);
/* 1023 */               splicedIn -= splicedOut;
/* 1024 */             } while (splicedIn > 0);
/* 1025 */             if (this.len == 0) {
/* 1026 */               this.promise.setSuccess();
/* 1027 */               return 1;
/*      */             }
/*      */           }
/* 1030 */           return 0;
/*      */         } finally {
/* 1032 */           AbstractEpollStreamChannel.safeClosePipe(pipeIn);
/* 1033 */           AbstractEpollStreamChannel.safeClosePipe(pipeOut);
/*      */         }
/*      */         
/*      */ 
/* 1037 */         return true;
/*      */       }
/*      */       catch (Throwable cause)
/*      */       {
/* 1036 */         this.promise.setFailure(cause);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final class EpollSocketWritableByteChannel extends SocketWritableByteChannel
/*      */   {
/*      */     EpollSocketWritableByteChannel() {
/* 1044 */       super();
/*      */     }
/*      */     
/*      */     protected ByteBufAllocator alloc()
/*      */     {
/* 1049 */       return AbstractEpollStreamChannel.this.alloc();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\AbstractEpollStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */