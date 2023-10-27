/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import java.net.InetSocketAddress;
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
/*     */ public abstract class AbstractKQueueServerChannel
/*     */   extends AbstractKQueueChannel
/*     */   implements ServerChannel
/*     */ {
/*  32 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*     */   
/*     */   AbstractKQueueServerChannel(BsdSocket fd) {
/*  35 */     this(fd, isSoErrorZero(fd));
/*     */   }
/*     */   
/*     */   AbstractKQueueServerChannel(BsdSocket fd, boolean active) {
/*  39 */     super(null, fd, active);
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  44 */     return METADATA;
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/*  49 */     return loop instanceof KQueueEventLoop;
/*     */   }
/*     */   
/*     */   protected InetSocketAddress remoteAddress0()
/*     */   {
/*  54 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe()
/*     */   {
/*  59 */     return new KQueueServerSocketUnsafe();
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*  64 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/*  69 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   abstract Channel newChildChannel(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) throws Exception;
/*     */   
/*     */ 
/*  76 */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception { throw new UnsupportedOperationException(); }
/*     */   
/*     */   final class KQueueServerSocketUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
/*  79 */     KQueueServerSocketUnsafe() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  83 */     private final byte[] acceptedAddress = new byte[26];
/*     */     
/*     */     void readReady(KQueueRecvByteAllocatorHandle allocHandle)
/*     */     {
/*  87 */       assert (AbstractKQueueServerChannel.this.eventLoop().inEventLoop());
/*  88 */       ChannelConfig config = AbstractKQueueServerChannel.this.config();
/*  89 */       if (AbstractKQueueServerChannel.this.shouldBreakReadReady(config)) {
/*  90 */         clearReadFilter0();
/*  91 */         return;
/*     */       }
/*  93 */       ChannelPipeline pipeline = AbstractKQueueServerChannel.this.pipeline();
/*  94 */       allocHandle.reset(config);
/*  95 */       allocHandle.attemptedBytesRead(1);
/*  96 */       readReadyBefore();
/*     */       
/*  98 */       Throwable exception = null;
/*     */       try {
/*     */         try {
/*     */           do {
/* 102 */             int acceptFd = AbstractKQueueServerChannel.this.socket.accept(this.acceptedAddress);
/* 103 */             if (acceptFd == -1)
/*     */             {
/* 105 */               allocHandle.lastBytesRead(-1);
/* 106 */               break;
/*     */             }
/* 108 */             allocHandle.lastBytesRead(1);
/* 109 */             allocHandle.incMessagesRead(1);
/*     */             
/* 111 */             this.readPending = false;
/* 112 */             pipeline.fireChannelRead(AbstractKQueueServerChannel.this.newChildChannel(acceptFd, this.acceptedAddress, 1, this.acceptedAddress[0]));
/*     */           }
/* 114 */           while (allocHandle.continueReading());
/*     */         } catch (Throwable t) {
/* 116 */           exception = t;
/*     */         }
/* 118 */         allocHandle.readComplete();
/* 119 */         pipeline.fireChannelReadComplete();
/*     */         
/* 121 */         if (exception != null) {
/* 122 */           pipeline.fireExceptionCaught(exception);
/*     */         }
/*     */       } finally {
/* 125 */         readReadyFinally(config);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\AbstractKQueueServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */