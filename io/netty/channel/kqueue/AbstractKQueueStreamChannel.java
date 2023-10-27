/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.socket.DuplexChannel;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.channel.unix.SocketWritableByteChannel;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public abstract class AbstractKQueueStreamChannel
/*     */   extends AbstractKQueueChannel
/*     */   implements DuplexChannel
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractKQueueStreamChannel.class);
/*  53 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*  54 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  55 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  56 */     StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
/*     */   private WritableByteChannel byteChannel;
/*  58 */   private final Runnable flushTask = new Runnable()
/*     */   {
/*     */ 
/*     */     public void run()
/*     */     {
/*  63 */       ((AbstractKQueueChannel.AbstractKQueueUnsafe)AbstractKQueueStreamChannel.this.unsafe()).flush0();
/*     */     }
/*     */   };
/*     */   
/*     */   AbstractKQueueStreamChannel(Channel parent, BsdSocket fd, boolean active) {
/*  68 */     super(parent, fd, active);
/*     */   }
/*     */   
/*     */   AbstractKQueueStreamChannel(Channel parent, BsdSocket fd, SocketAddress remote) {
/*  72 */     super(parent, fd, remote);
/*     */   }
/*     */   
/*     */   AbstractKQueueStreamChannel(BsdSocket fd) {
/*  76 */     this(null, fd, isSoErrorZero(fd));
/*     */   }
/*     */   
/*     */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe()
/*     */   {
/*  81 */     return new KQueueStreamUnsafe();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  86 */     return METADATA;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writeBytes(ChannelOutboundBuffer in, ByteBuf buf)
/*     */     throws Exception
/*     */   {
/* 104 */     int readableBytes = buf.readableBytes();
/* 105 */     if (readableBytes == 0) {
/* 106 */       in.remove();
/* 107 */       return 0;
/*     */     }
/*     */     
/* 110 */     if ((buf.hasMemoryAddress()) || (buf.nioBufferCount() == 1)) {
/* 111 */       return doWriteBytes(in, buf);
/*     */     }
/* 113 */     ByteBuffer[] nioBuffers = buf.nioBuffers();
/* 114 */     return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, 
/* 115 */       config().getMaxBytesPerGatheringWrite());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void adjustMaxBytesPerGatheringWrite(long attempted, long written, long oldMaxBytesPerGatheringWrite)
/*     */   {
/* 123 */     if (attempted == written) {
/* 124 */       if (attempted << 1 > oldMaxBytesPerGatheringWrite) {
/* 125 */         config().setMaxBytesPerGatheringWrite(attempted << 1);
/*     */       }
/* 127 */     } else if ((attempted > 4096L) && (written < attempted >>> 1)) {
/* 128 */       config().setMaxBytesPerGatheringWrite(attempted >>> 1);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writeBytesMultiple(ChannelOutboundBuffer in, IovArray array)
/*     */     throws IOException
/*     */   {
/* 148 */     long expectedWrittenBytes = array.size();
/* 149 */     assert (expectedWrittenBytes != 0L);
/* 150 */     int cnt = array.count();
/* 151 */     assert (cnt != 0);
/*     */     
/* 153 */     long localWrittenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/* 154 */     if (localWrittenBytes > 0L) {
/* 155 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, array.maxBytes());
/* 156 */       in.removeBytes(localWrittenBytes);
/* 157 */       return 1;
/*     */     }
/* 159 */     return Integer.MAX_VALUE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, long maxBytesPerGatheringWrite)
/*     */     throws IOException
/*     */   {
/* 183 */     assert (expectedWrittenBytes != 0L);
/* 184 */     if (expectedWrittenBytes > maxBytesPerGatheringWrite) {
/* 185 */       expectedWrittenBytes = maxBytesPerGatheringWrite;
/*     */     }
/*     */     
/* 188 */     long localWrittenBytes = this.socket.writev(nioBuffers, 0, nioBufferCnt, expectedWrittenBytes);
/* 189 */     if (localWrittenBytes > 0L) {
/* 190 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, maxBytesPerGatheringWrite);
/* 191 */       in.removeBytes(localWrittenBytes);
/* 192 */       return 1;
/*     */     }
/* 194 */     return Integer.MAX_VALUE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writeDefaultFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region)
/*     */     throws Exception
/*     */   {
/* 212 */     long regionCount = region.count();
/* 213 */     if (region.transferred() >= regionCount) {
/* 214 */       in.remove();
/* 215 */       return 0;
/*     */     }
/*     */     
/* 218 */     long offset = region.transferred();
/* 219 */     long flushedAmount = this.socket.sendFile(region, region.position(), offset, regionCount - offset);
/* 220 */     if (flushedAmount > 0L) {
/* 221 */       in.progress(flushedAmount);
/* 222 */       if (region.transferred() >= regionCount) {
/* 223 */         in.remove();
/*     */       }
/* 225 */       return 1;
/*     */     }
/* 227 */     return Integer.MAX_VALUE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private int writeFileRegion(ChannelOutboundBuffer in, FileRegion region)
/*     */     throws Exception
/*     */   {
/* 245 */     if (region.transferred() >= region.count()) {
/* 246 */       in.remove();
/* 247 */       return 0;
/*     */     }
/*     */     
/* 250 */     if (this.byteChannel == null) {
/* 251 */       this.byteChannel = new KQueueSocketWritableByteChannel();
/*     */     }
/* 253 */     long flushedAmount = region.transferTo(this.byteChannel, region.transferred());
/* 254 */     if (flushedAmount > 0L) {
/* 255 */       in.progress(flushedAmount);
/* 256 */       if (region.transferred() >= region.count()) {
/* 257 */         in.remove();
/*     */       }
/* 259 */       return 1;
/*     */     }
/* 261 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 266 */     int writeSpinCount = config().getWriteSpinCount();
/*     */     do {
/* 268 */       int msgCount = in.size();
/*     */       
/* 270 */       if ((msgCount > 1) && ((in.current() instanceof ByteBuf))) {
/* 271 */         writeSpinCount -= doWriteMultiple(in);
/* 272 */       } else { if (msgCount == 0)
/*     */         {
/* 274 */           writeFilter(false);
/*     */           
/* 276 */           return;
/*     */         }
/* 278 */         writeSpinCount -= doWriteSingle(in);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 284 */     while (writeSpinCount > 0);
/*     */     
/* 286 */     if (writeSpinCount == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 291 */       writeFilter(false);
/*     */       
/*     */ 
/* 294 */       eventLoop().execute(this.flushTask);
/*     */     }
/*     */     else
/*     */     {
/* 298 */       writeFilter(true);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int doWriteSingle(ChannelOutboundBuffer in)
/*     */     throws Exception
/*     */   {
/* 318 */     Object msg = in.current();
/* 319 */     if ((msg instanceof ByteBuf))
/* 320 */       return writeBytes(in, (ByteBuf)msg);
/* 321 */     if ((msg instanceof DefaultFileRegion))
/* 322 */       return writeDefaultFileRegion(in, (DefaultFileRegion)msg);
/* 323 */     if ((msg instanceof FileRegion)) {
/* 324 */       return writeFileRegion(in, (FileRegion)msg);
/*     */     }
/*     */     
/* 327 */     throw new Error();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int doWriteMultiple(ChannelOutboundBuffer in)
/*     */     throws Exception
/*     */   {
/* 346 */     long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
/* 347 */     IovArray array = ((KQueueEventLoop)eventLoop()).cleanArray();
/* 348 */     array.maxBytes(maxBytesPerGatheringWrite);
/* 349 */     in.forEachFlushedMessage(array);
/*     */     
/* 351 */     if (array.count() >= 1)
/*     */     {
/* 353 */       return writeBytesMultiple(in, array);
/*     */     }
/*     */     
/* 356 */     in.removeBytes(0L);
/* 357 */     return 0;
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 362 */     if ((msg instanceof ByteBuf)) {
/* 363 */       ByteBuf buf = (ByteBuf)msg;
/* 364 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*     */     }
/*     */     
/* 367 */     if ((msg instanceof FileRegion)) {
/* 368 */       return msg;
/*     */     }
/*     */     
/*     */ 
/* 372 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */   protected final void doShutdownOutput()
/*     */     throws Exception
/*     */   {
/* 378 */     this.socket.shutdown(false, true);
/*     */   }
/*     */   
/*     */   public boolean isOutputShutdown()
/*     */   {
/* 383 */     return this.socket.isOutputShutdown();
/*     */   }
/*     */   
/*     */   public boolean isInputShutdown()
/*     */   {
/* 388 */     return this.socket.isInputShutdown();
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 393 */     return this.socket.isShutdown();
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput()
/*     */   {
/* 398 */     return shutdownOutput(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise)
/*     */   {
/* 403 */     EventLoop loop = eventLoop();
/* 404 */     if (loop.inEventLoop()) {
/* 405 */       ((AbstractChannel.AbstractUnsafe)unsafe()).shutdownOutput(promise);
/*     */     } else {
/* 407 */       loop.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 410 */           ((AbstractChannel.AbstractUnsafe)AbstractKQueueStreamChannel.this.unsafe()).shutdownOutput(promise);
/*     */         }
/*     */       });
/*     */     }
/* 414 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownInput()
/*     */   {
/* 419 */     return shutdownInput(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownInput(final ChannelPromise promise)
/*     */   {
/* 424 */     EventLoop loop = eventLoop();
/* 425 */     if (loop.inEventLoop()) {
/* 426 */       shutdownInput0(promise);
/*     */     } else {
/* 428 */       loop.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 431 */           AbstractKQueueStreamChannel.this.shutdownInput0(promise);
/*     */         }
/*     */       });
/*     */     }
/* 435 */     return promise;
/*     */   }
/*     */   
/*     */   private void shutdownInput0(ChannelPromise promise) {
/*     */     try {
/* 440 */       this.socket.shutdown(true, false);
/*     */     } catch (Throwable cause) {
/* 442 */       promise.setFailure(cause);
/* 443 */       return;
/*     */     }
/* 445 */     promise.setSuccess();
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdown()
/*     */   {
/* 450 */     return shutdown(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdown(final ChannelPromise promise)
/*     */   {
/* 455 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/* 456 */     if (shutdownOutputFuture.isDone()) {
/* 457 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*     */     } else {
/* 459 */       shutdownOutputFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/* 462 */           AbstractKQueueStreamChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*     */         }
/*     */       });
/*     */     }
/* 466 */     return promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/* 470 */     ChannelFuture shutdownInputFuture = shutdownInput();
/* 471 */     if (shutdownInputFuture.isDone()) {
/* 472 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */     } else {
/* 474 */       shutdownInputFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/* 477 */           AbstractKQueueStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise)
/*     */   {
/* 486 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/* 487 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/* 488 */     if (shutdownOutputCause != null) {
/* 489 */       if (shutdownInputCause != null) {
/* 490 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*     */       }
/*     */       
/* 493 */       promise.setFailure(shutdownOutputCause);
/* 494 */     } else if (shutdownInputCause != null) {
/* 495 */       promise.setFailure(shutdownInputCause);
/*     */     } else {
/* 497 */       promise.setSuccess();
/*     */     }
/*     */   }
/*     */   
/* 501 */   class KQueueStreamUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe { KQueueStreamUnsafe() { super(); }
/*     */     
/*     */     protected Executor prepareToClose()
/*     */     {
/* 505 */       return super.prepareToClose();
/*     */     }
/*     */     
/*     */     void readReady(KQueueRecvByteAllocatorHandle allocHandle)
/*     */     {
/* 510 */       ChannelConfig config = AbstractKQueueStreamChannel.this.config();
/* 511 */       if (AbstractKQueueStreamChannel.this.shouldBreakReadReady(config)) {
/* 512 */         clearReadFilter0();
/* 513 */         return;
/*     */       }
/* 515 */       ChannelPipeline pipeline = AbstractKQueueStreamChannel.this.pipeline();
/* 516 */       ByteBufAllocator allocator = config.getAllocator();
/* 517 */       allocHandle.reset(config);
/* 518 */       readReadyBefore();
/*     */       
/* 520 */       ByteBuf byteBuf = null;
/* 521 */       boolean close = false;
/*     */       try
/*     */       {
/*     */         do
/*     */         {
/* 526 */           byteBuf = allocHandle.allocate(allocator);
/* 527 */           allocHandle.lastBytesRead(AbstractKQueueStreamChannel.this.doReadBytes(byteBuf));
/* 528 */           if (allocHandle.lastBytesRead() <= 0)
/*     */           {
/* 530 */             byteBuf.release();
/* 531 */             byteBuf = null;
/* 532 */             close = allocHandle.lastBytesRead() < 0;
/* 533 */             if (!close)
/*     */               break;
/* 535 */             this.readPending = false; break;
/*     */           }
/*     */           
/*     */ 
/* 539 */           allocHandle.incMessagesRead(1);
/* 540 */           this.readPending = false;
/* 541 */           pipeline.fireChannelRead(byteBuf);
/* 542 */           byteBuf = null;
/*     */         }
/* 544 */         while ((!AbstractKQueueStreamChannel.this.shouldBreakReadReady(config)) && 
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
/* 558 */           (allocHandle.continueReading()));
/*     */         
/* 560 */         allocHandle.readComplete();
/* 561 */         pipeline.fireChannelReadComplete();
/*     */         
/* 563 */         if (close) {
/* 564 */           shutdownInput(false);
/*     */         }
/*     */       } catch (Throwable t) {
/* 567 */         handleReadException(pipeline, byteBuf, t, close, allocHandle);
/*     */       } finally {
/* 569 */         readReadyFinally(config);
/*     */       }
/*     */     }
/*     */     
/*     */     private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, KQueueRecvByteAllocatorHandle allocHandle)
/*     */     {
/* 575 */       if (byteBuf != null) {
/* 576 */         if (byteBuf.isReadable()) {
/* 577 */           this.readPending = false;
/* 578 */           pipeline.fireChannelRead(byteBuf);
/*     */         } else {
/* 580 */           byteBuf.release();
/*     */         }
/*     */       }
/* 583 */       if (!failConnectPromise(cause)) {
/* 584 */         allocHandle.readComplete();
/* 585 */         pipeline.fireChannelReadComplete();
/* 586 */         pipeline.fireExceptionCaught(cause);
/* 587 */         if ((close) || ((cause instanceof IOException))) {
/* 588 */           shutdownInput(false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class KQueueSocketWritableByteChannel extends SocketWritableByteChannel {
/*     */     KQueueSocketWritableByteChannel() {
/* 596 */       super();
/*     */     }
/*     */     
/*     */     protected ByteBufAllocator alloc()
/*     */     {
/* 601 */       return AbstractKQueueStreamChannel.this.alloc();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\AbstractKQueueStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */