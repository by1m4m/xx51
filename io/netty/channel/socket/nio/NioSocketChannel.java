/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel.NioByteUnsafe;
/*     */ import io.netty.channel.nio.AbstractNioChannel.AbstractNioUnsafe;
/*     */ import io.netty.channel.nio.AbstractNioChannel.NioUnsafe;
/*     */ import io.netty.channel.socket.DefaultSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Map;
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
/*     */ public class NioSocketChannel
/*     */   extends AbstractNioByteChannel
/*     */   implements io.netty.channel.socket.SocketChannel
/*     */ {
/*  57 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioSocketChannel.class);
/*  58 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*     */   
/*     */ 
/*     */   private final SocketChannelConfig config;
/*     */   
/*     */ 
/*     */   private static java.nio.channels.SocketChannel newSocket(SelectorProvider provider)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       return provider.openSocketChannel();
/*     */     } catch (IOException e) {
/*  70 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioSocketChannel()
/*     */   {
/*  80 */     this(DEFAULT_SELECTOR_PROVIDER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSocketChannel(SelectorProvider provider)
/*     */   {
/*  87 */     this(newSocket(provider));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSocketChannel(java.nio.channels.SocketChannel socket)
/*     */   {
/*  94 */     this(null, socket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioSocketChannel(Channel parent, java.nio.channels.SocketChannel socket)
/*     */   {
/* 104 */     super(parent, socket);
/* 105 */     this.config = new NioSocketChannelConfig(this, socket.socket(), null);
/*     */   }
/*     */   
/*     */   public ServerSocketChannel parent()
/*     */   {
/* 110 */     return (ServerSocketChannel)super.parent();
/*     */   }
/*     */   
/*     */   public SocketChannelConfig config()
/*     */   {
/* 115 */     return this.config;
/*     */   }
/*     */   
/*     */   protected java.nio.channels.SocketChannel javaChannel()
/*     */   {
/* 120 */     return (java.nio.channels.SocketChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 125 */     java.nio.channels.SocketChannel ch = javaChannel();
/* 126 */     return (ch.isOpen()) && (ch.isConnected());
/*     */   }
/*     */   
/*     */   public boolean isOutputShutdown()
/*     */   {
/* 131 */     return (javaChannel().socket().isOutputShutdown()) || (!isActive());
/*     */   }
/*     */   
/*     */   public boolean isInputShutdown()
/*     */   {
/* 136 */     return (javaChannel().socket().isInputShutdown()) || (!isActive());
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 141 */     Socket socket = javaChannel().socket();
/* 142 */     return ((socket.isInputShutdown()) && (socket.isOutputShutdown())) || (!isActive());
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 147 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 152 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   protected final void doShutdownOutput()
/*     */     throws Exception
/*     */   {
/* 158 */     if (PlatformDependent.javaVersion() >= 7) {
/* 159 */       javaChannel().shutdownOutput();
/*     */     } else {
/* 161 */       javaChannel().socket().shutdownOutput();
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput()
/*     */   {
/* 167 */     return shutdownOutput(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise)
/*     */   {
/* 172 */     EventLoop loop = eventLoop();
/* 173 */     if (loop.inEventLoop()) {
/* 174 */       ((AbstractChannel.AbstractUnsafe)unsafe()).shutdownOutput(promise);
/*     */     } else {
/* 176 */       loop.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 179 */           ((AbstractChannel.AbstractUnsafe)NioSocketChannel.this.unsafe()).shutdownOutput(promise);
/*     */         }
/*     */       });
/*     */     }
/* 183 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownInput()
/*     */   {
/* 188 */     return shutdownInput(newPromise());
/*     */   }
/*     */   
/*     */   protected boolean isInputShutdown0()
/*     */   {
/* 193 */     return isInputShutdown();
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownInput(final ChannelPromise promise)
/*     */   {
/* 198 */     EventLoop loop = eventLoop();
/* 199 */     if (loop.inEventLoop()) {
/* 200 */       shutdownInput0(promise);
/*     */     } else {
/* 202 */       loop.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 205 */           NioSocketChannel.this.shutdownInput0(promise);
/*     */         }
/*     */       });
/*     */     }
/* 209 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdown()
/*     */   {
/* 214 */     return shutdown(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdown(final ChannelPromise promise)
/*     */   {
/* 219 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/* 220 */     if (shutdownOutputFuture.isDone()) {
/* 221 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*     */     } else {
/* 223 */       shutdownOutputFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/* 226 */           NioSocketChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*     */         }
/*     */       });
/*     */     }
/* 230 */     return promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/* 234 */     ChannelFuture shutdownInputFuture = shutdownInput();
/* 235 */     if (shutdownInputFuture.isDone()) {
/* 236 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */     } else {
/* 238 */       shutdownInputFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/* 241 */           NioSocketChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise)
/*     */   {
/* 250 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/* 251 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/* 252 */     if (shutdownOutputCause != null) {
/* 253 */       if (shutdownInputCause != null) {
/* 254 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*     */       }
/*     */       
/* 257 */       promise.setFailure(shutdownOutputCause);
/* 258 */     } else if (shutdownInputCause != null) {
/* 259 */       promise.setFailure(shutdownInputCause);
/*     */     } else {
/* 261 */       promise.setSuccess();
/*     */     }
/*     */   }
/*     */   
/*     */   private void shutdownInput0(ChannelPromise promise) {
/* 266 */     try { shutdownInput0();
/* 267 */       promise.setSuccess();
/*     */     } catch (Throwable t) {
/* 269 */       promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */   private void shutdownInput0() throws Exception {
/* 274 */     if (PlatformDependent.javaVersion() >= 7) {
/* 275 */       javaChannel().shutdownInput();
/*     */     } else {
/* 277 */       javaChannel().socket().shutdownInput();
/*     */     }
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 283 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 288 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 293 */     doBind0(localAddress);
/*     */   }
/*     */   
/*     */   private void doBind0(SocketAddress localAddress) throws Exception {
/* 297 */     if (PlatformDependent.javaVersion() >= 7) {
/* 298 */       SocketUtils.bind(javaChannel(), localAddress);
/*     */     } else {
/* 300 */       SocketUtils.bind(javaChannel().socket(), localAddress);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/* 306 */     if (localAddress != null) {
/* 307 */       doBind0(localAddress);
/*     */     }
/*     */     
/* 310 */     boolean success = false;
/*     */     try {
/* 312 */       boolean connected = SocketUtils.connect(javaChannel(), remoteAddress);
/* 313 */       if (!connected) {
/* 314 */         selectionKey().interestOps(8);
/*     */       }
/* 316 */       success = true;
/* 317 */       return connected;
/*     */     } finally {
/* 319 */       if (!success) {
/* 320 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 327 */     if (!javaChannel().finishConnect()) {
/* 328 */       throw new Error();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 334 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 339 */     super.doClose();
/* 340 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadBytes(ByteBuf byteBuf) throws Exception
/*     */   {
/* 345 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 346 */     allocHandle.attemptedBytesRead(byteBuf.writableBytes());
/* 347 */     return byteBuf.writeBytes(javaChannel(), allocHandle.attemptedBytesRead());
/*     */   }
/*     */   
/*     */   protected int doWriteBytes(ByteBuf buf) throws Exception
/*     */   {
/* 352 */     int expectedWrittenBytes = buf.readableBytes();
/* 353 */     return buf.readBytes(javaChannel(), expectedWrittenBytes);
/*     */   }
/*     */   
/*     */   protected long doWriteFileRegion(FileRegion region) throws Exception
/*     */   {
/* 358 */     long position = region.transferred();
/* 359 */     return region.transferTo(javaChannel(), position);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void adjustMaxBytesPerGatheringWrite(int attempted, int written, int oldMaxBytesPerGatheringWrite)
/*     */   {
/* 366 */     if (attempted == written) {
/* 367 */       if (attempted << 1 > oldMaxBytesPerGatheringWrite) {
/* 368 */         ((NioSocketChannelConfig)this.config).setMaxBytesPerGatheringWrite(attempted << 1);
/*     */       }
/* 370 */     } else if ((attempted > 4096) && (written < attempted >>> 1)) {
/* 371 */       ((NioSocketChannelConfig)this.config).setMaxBytesPerGatheringWrite(attempted >>> 1);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 377 */     java.nio.channels.SocketChannel ch = javaChannel();
/* 378 */     int writeSpinCount = config().getWriteSpinCount();
/*     */     do {
/* 380 */       if (in.isEmpty())
/*     */       {
/* 382 */         clearOpWrite();
/*     */         
/* 384 */         return;
/*     */       }
/*     */       
/*     */ 
/* 388 */       int maxBytesPerGatheringWrite = ((NioSocketChannelConfig)this.config).getMaxBytesPerGatheringWrite();
/* 389 */       ByteBuffer[] nioBuffers = in.nioBuffers(1024, maxBytesPerGatheringWrite);
/* 390 */       int nioBufferCnt = in.nioBufferCount();
/*     */       
/*     */ 
/*     */ 
/* 394 */       switch (nioBufferCnt)
/*     */       {
/*     */       case 0: 
/* 397 */         writeSpinCount -= doWrite0(in);
/* 398 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 1: 
/* 403 */         ByteBuffer buffer = nioBuffers[0];
/* 404 */         int attemptedBytes = buffer.remaining();
/* 405 */         int localWrittenBytes = ch.write(buffer);
/* 406 */         if (localWrittenBytes <= 0) {
/* 407 */           incompleteWrite(true);
/* 408 */           return;
/*     */         }
/* 410 */         adjustMaxBytesPerGatheringWrite(attemptedBytes, localWrittenBytes, maxBytesPerGatheringWrite);
/* 411 */         in.removeBytes(localWrittenBytes);
/* 412 */         writeSpinCount--;
/* 413 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       default: 
/* 419 */         long attemptedBytes = in.nioBufferSize();
/* 420 */         long localWrittenBytes = ch.write(nioBuffers, 0, nioBufferCnt);
/* 421 */         if (localWrittenBytes <= 0L) {
/* 422 */           incompleteWrite(true);
/* 423 */           return;
/*     */         }
/*     */         
/* 426 */         adjustMaxBytesPerGatheringWrite((int)attemptedBytes, (int)localWrittenBytes, maxBytesPerGatheringWrite);
/*     */         
/* 428 */         in.removeBytes(localWrittenBytes);
/* 429 */         writeSpinCount--;
/*     */       
/*     */       }
/*     */       
/* 433 */     } while (writeSpinCount > 0);
/*     */     
/* 435 */     incompleteWrite(writeSpinCount < 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 440 */   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() { return new NioSocketChannelUnsafe(null); }
/*     */   
/*     */   private final class NioSocketChannelUnsafe extends AbstractNioByteChannel.NioByteUnsafe {
/* 443 */     private NioSocketChannelUnsafe() { super(); }
/*     */     
/*     */     protected Executor prepareToClose() {
/*     */       try {
/* 447 */         if ((NioSocketChannel.this.javaChannel().isOpen()) && (NioSocketChannel.this.config().getSoLinger() > 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 452 */           NioSocketChannel.this.doDeregister();
/* 453 */           return GlobalEventExecutor.INSTANCE;
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */       
/*     */ 
/*     */ 
/* 460 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class NioSocketChannelConfig extends DefaultSocketChannelConfig {
/* 465 */     private volatile int maxBytesPerGatheringWrite = Integer.MAX_VALUE;
/*     */     
/* 467 */     private NioSocketChannelConfig(NioSocketChannel channel, Socket javaSocket) { super(javaSocket);
/* 468 */       calculateMaxBytesPerGatheringWrite();
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 473 */       NioSocketChannel.this.clearReadPending();
/*     */     }
/*     */     
/*     */     public NioSocketChannelConfig setSendBufferSize(int sendBufferSize)
/*     */     {
/* 478 */       super.setSendBufferSize(sendBufferSize);
/* 479 */       calculateMaxBytesPerGatheringWrite();
/* 480 */       return this;
/*     */     }
/*     */     
/*     */     public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */     {
/* 485 */       if ((PlatformDependent.javaVersion() >= 7) && ((option instanceof NioChannelOption))) {
/* 486 */         return NioChannelOption.setOption(jdkChannel(), (NioChannelOption)option, value);
/*     */       }
/* 488 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*     */     public <T> T getOption(ChannelOption<T> option)
/*     */     {
/* 493 */       if ((PlatformDependent.javaVersion() >= 7) && ((option instanceof NioChannelOption))) {
/* 494 */         return (T)NioChannelOption.getOption(jdkChannel(), (NioChannelOption)option);
/*     */       }
/* 496 */       return (T)super.getOption(option);
/*     */     }
/*     */     
/*     */ 
/*     */     public Map<ChannelOption<?>, Object> getOptions()
/*     */     {
/* 502 */       if (PlatformDependent.javaVersion() >= 7) {
/* 503 */         return getOptions(super.getOptions(), NioChannelOption.getOptions(jdkChannel()));
/*     */       }
/* 505 */       return super.getOptions();
/*     */     }
/*     */     
/*     */     void setMaxBytesPerGatheringWrite(int maxBytesPerGatheringWrite) {
/* 509 */       this.maxBytesPerGatheringWrite = maxBytesPerGatheringWrite;
/*     */     }
/*     */     
/*     */     int getMaxBytesPerGatheringWrite() {
/* 513 */       return this.maxBytesPerGatheringWrite;
/*     */     }
/*     */     
/*     */     private void calculateMaxBytesPerGatheringWrite()
/*     */     {
/* 518 */       int newSendBufferSize = getSendBufferSize() << 1;
/* 519 */       if (newSendBufferSize > 0) {
/* 520 */         setMaxBytesPerGatheringWrite(getSendBufferSize() << 1);
/*     */       }
/*     */     }
/*     */     
/*     */     private java.nio.channels.SocketChannel jdkChannel() {
/* 525 */       return ((NioSocketChannel)this.channel).javaChannel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\socket\nio\NioSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */