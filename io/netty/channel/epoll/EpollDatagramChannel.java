/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultAddressedEnvelope;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.unix.DatagramSocketAddress;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.channel.unix.UnixChannelUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public final class EpollDatagramChannel
/*     */   extends AbstractEpollChannel
/*     */   implements DatagramChannel
/*     */ {
/*  50 */   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
/*  51 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*  52 */     StringUtil.simpleClassName(DatagramPacket.class) + ", " + 
/*  53 */     StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + 
/*  54 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*  55 */     StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + 
/*  56 */     StringUtil.simpleClassName(ByteBuf.class) + ')';
/*     */   private final EpollDatagramChannelConfig config;
/*     */   private volatile boolean connected;
/*     */   
/*     */   public EpollDatagramChannel()
/*     */   {
/*  62 */     super(LinuxSocket.newSocketDgram());
/*  63 */     this.config = new EpollDatagramChannelConfig(this);
/*     */   }
/*     */   
/*     */   public EpollDatagramChannel(int fd) {
/*  67 */     this(new LinuxSocket(fd));
/*     */   }
/*     */   
/*     */   EpollDatagramChannel(LinuxSocket fd) {
/*  71 */     super(null, fd, true);
/*  72 */     this.config = new EpollDatagramChannelConfig(this);
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/*  77 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/*  82 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  87 */     return METADATA;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/*  93 */     return (this.socket.isOpen()) && (((this.config.getActiveOnOpen()) && (isRegistered())) || (this.active));
/*     */   }
/*     */   
/*     */   public boolean isConnected()
/*     */   {
/*  98 */     return this.connected;
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress)
/*     */   {
/* 103 */     return joinGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 109 */       return joinGroup(multicastAddress, 
/*     */       
/* 111 */         NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
/*     */     } catch (SocketException e) {
/* 113 */       promise.setFailure(e);
/*     */     }
/* 115 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 121 */     return joinGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 128 */     return joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 134 */     return joinGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/* 142 */     if (multicastAddress == null) {
/* 143 */       throw new NullPointerException("multicastAddress");
/*     */     }
/*     */     
/* 146 */     if (networkInterface == null) {
/* 147 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 150 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 151 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress)
/*     */   {
/* 156 */     return leaveGroup(multicastAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise)
/*     */   {
/*     */     try {
/* 162 */       return leaveGroup(multicastAddress, 
/* 163 */         NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
/*     */     } catch (SocketException e) {
/* 165 */       promise.setFailure(e);
/*     */     }
/* 167 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface)
/*     */   {
/* 173 */     return leaveGroup(multicastAddress, networkInterface, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise)
/*     */   {
/* 180 */     return leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source)
/*     */   {
/* 186 */     return leaveGroup(multicastAddress, networkInterface, source, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise)
/*     */   {
/* 193 */     if (multicastAddress == null) {
/* 194 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 196 */     if (networkInterface == null) {
/* 197 */       throw new NullPointerException("networkInterface");
/*     */     }
/*     */     
/* 200 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/*     */     
/* 202 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock)
/*     */   {
/* 209 */     return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/* 216 */     if (multicastAddress == null) {
/* 217 */       throw new NullPointerException("multicastAddress");
/*     */     }
/* 219 */     if (sourceToBlock == null) {
/* 220 */       throw new NullPointerException("sourceToBlock");
/*     */     }
/*     */     
/* 223 */     if (networkInterface == null) {
/* 224 */       throw new NullPointerException("networkInterface");
/*     */     }
/* 226 */     promise.setFailure(new UnsupportedOperationException("Multicast not supported"));
/* 227 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock)
/*     */   {
/* 232 */     return block(multicastAddress, sourceToBlock, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 239 */       return block(multicastAddress, 
/*     */       
/* 241 */         NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
/*     */     }
/*     */     catch (Throwable e) {
/* 244 */       promise.setFailure(e);
/*     */     }
/* 246 */     return promise;
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*     */   {
/* 251 */     return new EpollDatagramChannelUnsafe();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 256 */     super.doBind(localAddress);
/* 257 */     this.active = true;
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*     */     for (;;) {
/* 263 */       Object msg = in.current();
/* 264 */       if (msg == null)
/*     */       {
/* 266 */         clearFlag(Native.EPOLLOUT);
/* 267 */         break;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 272 */         if ((Native.IS_SUPPORTING_SENDMMSG) && (in.size() > 1)) {
/* 273 */           NativeDatagramPacketArray array = ((EpollEventLoop)eventLoop()).cleanDatagramPacketArray();
/* 274 */           in.forEachFlushedMessage(array);
/* 275 */           int cnt = array.count();
/*     */           
/* 277 */           if (cnt >= 1)
/*     */           {
/* 279 */             int offset = 0;
/* 280 */             NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
/*     */             
/* 282 */             while (cnt > 0) {
/* 283 */               int send = Native.sendmmsg(this.socket.intValue(), packets, offset, cnt);
/* 284 */               if (send == 0)
/*     */               {
/* 286 */                 setFlag(Native.EPOLLOUT);
/* 287 */                 return;
/*     */               }
/* 289 */               for (int i = 0; i < send; i++) {
/* 290 */                 in.remove();
/*     */               }
/* 292 */               cnt -= send;
/* 293 */               offset += send;
/*     */             }
/* 295 */             continue;
/*     */           }
/*     */         }
/* 298 */         boolean done = false;
/* 299 */         for (int i = config().getWriteSpinCount(); i > 0; i--) {
/* 300 */           if (doWriteMessage(msg)) {
/* 301 */             done = true;
/* 302 */             break;
/*     */           }
/*     */         }
/*     */         
/* 306 */         if (done) {
/* 307 */           in.remove();
/*     */         }
/*     */         else {
/* 310 */           setFlag(Native.EPOLLOUT);
/* 311 */           break;
/*     */         }
/*     */         
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 317 */         in.remove(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean doWriteMessage(Object msg) throws Exception { InetSocketAddress remoteAddress;
/*     */     ByteBuf data;
/*     */     InetSocketAddress remoteAddress;
/* 325 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 327 */       AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope)msg;
/*     */       
/* 329 */       ByteBuf data = (ByteBuf)envelope.content();
/* 330 */       remoteAddress = (InetSocketAddress)envelope.recipient();
/*     */     } else {
/* 332 */       data = (ByteBuf)msg;
/* 333 */       remoteAddress = null;
/*     */     }
/*     */     
/* 336 */     int dataLen = data.readableBytes();
/* 337 */     if (dataLen == 0) {
/* 338 */       return true;
/*     */     }
/*     */     long writtenBytes;
/*     */     long writtenBytes;
/* 342 */     if (data.hasMemoryAddress()) {
/* 343 */       long memoryAddress = data.memoryAddress();
/* 344 */       long writtenBytes; if (remoteAddress == null) {
/* 345 */         writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
/*     */       } else
/* 347 */         writtenBytes = this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress
/* 348 */           .getAddress(), remoteAddress.getPort());
/*     */     } else { long writtenBytes;
/* 350 */       if (data.nioBufferCount() > 1) {
/* 351 */         IovArray array = ((EpollEventLoop)eventLoop()).cleanIovArray();
/* 352 */         array.add(data);
/* 353 */         int cnt = array.count();
/* 354 */         assert (cnt != 0);
/*     */         long writtenBytes;
/* 356 */         if (remoteAddress == null) {
/* 357 */           writtenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/*     */         } else {
/* 359 */           writtenBytes = this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress
/* 360 */             .getAddress(), remoteAddress.getPort());
/*     */         }
/*     */       } else {
/* 363 */         ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
/* 364 */         long writtenBytes; if (remoteAddress == null) {
/* 365 */           writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
/*     */         } else {
/* 367 */           writtenBytes = this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress
/* 368 */             .getAddress(), remoteAddress.getPort());
/*     */         }
/*     */       }
/*     */     }
/* 372 */     return writtenBytes > 0L;
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 377 */     if ((msg instanceof DatagramPacket)) {
/* 378 */       DatagramPacket packet = (DatagramPacket)msg;
/* 379 */       ByteBuf content = (ByteBuf)packet.content();
/* 380 */       return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DatagramPacket(
/* 381 */         newDirectBuffer(packet, content), (InetSocketAddress)packet.recipient()) : msg;
/*     */     }
/*     */     
/* 384 */     if ((msg instanceof ByteBuf)) {
/* 385 */       ByteBuf buf = (ByteBuf)msg;
/* 386 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*     */     }
/*     */     
/* 389 */     if ((msg instanceof AddressedEnvelope))
/*     */     {
/* 391 */       AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope)msg;
/* 392 */       if (((e.content() instanceof ByteBuf)) && (
/* 393 */         (e.recipient() == null) || ((e.recipient() instanceof InetSocketAddress))))
/*     */       {
/* 395 */         ByteBuf content = (ByteBuf)e.content();
/* 396 */         return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DefaultAddressedEnvelope(
/*     */         
/* 398 */           newDirectBuffer(e, content), (InetSocketAddress)e.recipient()) : e;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 403 */     throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*     */   }
/*     */   
/*     */   public EpollDatagramChannelConfig config()
/*     */   {
/* 408 */     return this.config;
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 413 */     this.socket.disconnect();
/* 414 */     this.connected = (this.active = 0);
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/* 419 */     if (super.doConnect(remoteAddress, localAddress)) {
/* 420 */       this.connected = true;
/* 421 */       return true;
/*     */     }
/* 423 */     return false;
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 428 */     super.doClose();
/* 429 */     this.connected = false;
/*     */   }
/*     */   
/* 432 */   final class EpollDatagramChannelUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe { EpollDatagramChannelUnsafe() { super(); }
/*     */     
/*     */     void epollInReady()
/*     */     {
/* 436 */       assert (EpollDatagramChannel.this.eventLoop().inEventLoop());
/* 437 */       DatagramChannelConfig config = EpollDatagramChannel.this.config();
/* 438 */       if (EpollDatagramChannel.this.shouldBreakEpollInReady(config)) {
/* 439 */         clearEpollIn0();
/* 440 */         return;
/*     */       }
/* 442 */       EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 443 */       allocHandle.edgeTriggered(EpollDatagramChannel.this.isFlagSet(Native.EPOLLET));
/*     */       
/* 445 */       ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
/* 446 */       ByteBufAllocator allocator = config.getAllocator();
/* 447 */       allocHandle.reset(config);
/* 448 */       epollInBefore();
/*     */       
/* 450 */       Throwable exception = null;
/*     */       try {
/* 452 */         ByteBuf data = null;
/*     */         try {
/*     */           do {
/* 455 */             data = allocHandle.allocate(allocator);
/* 456 */             allocHandle.attemptedBytesRead(data.writableBytes());
/*     */             DatagramSocketAddress remoteAddress;
/* 458 */             DatagramSocketAddress remoteAddress; if (data.hasMemoryAddress())
/*     */             {
/* 460 */               remoteAddress = EpollDatagramChannel.this.socket.recvFromAddress(data.memoryAddress(), data.writerIndex(), data
/* 461 */                 .capacity());
/*     */             } else {
/* 463 */               ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
/* 464 */               remoteAddress = EpollDatagramChannel.this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
/*     */             }
/*     */             
/* 467 */             if (remoteAddress == null) {
/* 468 */               allocHandle.lastBytesRead(-1);
/* 469 */               data.release();
/* 470 */               data = null;
/* 471 */               break;
/*     */             }
/*     */             
/* 474 */             InetSocketAddress localAddress = remoteAddress.localAddress();
/* 475 */             if (localAddress == null) {
/* 476 */               localAddress = (InetSocketAddress)localAddress();
/*     */             }
/*     */             
/* 479 */             allocHandle.incMessagesRead(1);
/* 480 */             allocHandle.lastBytesRead(remoteAddress.receivedAmount());
/* 481 */             data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead());
/*     */             
/* 483 */             this.readPending = false;
/* 484 */             pipeline.fireChannelRead(new DatagramPacket(data, localAddress, remoteAddress));
/*     */             
/*     */ 
/* 487 */             data = null;
/* 488 */           } while (allocHandle.continueReading());
/*     */         } catch (Throwable t) {
/* 490 */           if (data != null) {
/* 491 */             data.release();
/*     */           }
/* 493 */           exception = t;
/*     */         }
/*     */         
/* 496 */         allocHandle.readComplete();
/* 497 */         pipeline.fireChannelReadComplete();
/*     */         
/* 499 */         if (exception != null) {
/* 500 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */       } finally {
/* 503 */         epollInFinally(config);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\EpollDatagramChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */