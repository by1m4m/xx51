/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.NioSocketUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel;
/*     */ import io.netty.channel.nio.AbstractNioChannel.NioUnsafe;
/*     */ import io.netty.channel.udt.DefaultUdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtChannelConfig;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
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
/*     */ @Deprecated
/*     */ public class NioUdtByteConnectorChannel
/*     */   extends AbstractNioByteChannel
/*     */   implements UdtChannel
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtByteConnectorChannel.class);
/*     */   private final UdtChannelConfig config;
/*     */   
/*     */   public NioUdtByteConnectorChannel()
/*     */   {
/*  57 */     this(TypeUDT.STREAM);
/*     */   }
/*     */   
/*     */   public NioUdtByteConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
/*  61 */     super(parent, channelUDT);
/*     */     try {
/*  63 */       channelUDT.configureBlocking(false);
/*  64 */       switch (channelUDT.socketUDT().status()) {
/*     */       case INIT: 
/*     */       case OPENED: 
/*  67 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, true);
/*  68 */         break;
/*     */       default: 
/*  70 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, false);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*     */       try {
/*  75 */         channelUDT.close();
/*     */       } catch (Exception e2) {
/*  77 */         if (logger.isWarnEnabled()) {
/*  78 */           logger.warn("Failed to close channel.", e2);
/*     */         }
/*     */       }
/*  81 */       throw new ChannelException("Failed to configure channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public NioUdtByteConnectorChannel(SocketChannelUDT channelUDT) {
/*  86 */     this(null, channelUDT);
/*     */   }
/*     */   
/*     */   public NioUdtByteConnectorChannel(TypeUDT type) {
/*  90 */     this(NioUdtProvider.newConnectorChannelUDT(type));
/*     */   }
/*     */   
/*     */   public UdtChannelConfig config()
/*     */   {
/*  95 */     return this.config;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 100 */     privilegedBind(javaChannel(), localAddress);
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 105 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 111 */     doBind(localAddress != null ? localAddress : new InetSocketAddress(0));
/* 112 */     boolean success = false;
/*     */     try {
/* 114 */       boolean connected = SocketUtils.connect(javaChannel(), remoteAddress);
/* 115 */       if (!connected) {
/* 116 */         selectionKey().interestOps(
/* 117 */           selectionKey().interestOps() | 0x8);
/*     */       }
/* 119 */       success = true;
/* 120 */       return connected;
/*     */     } finally {
/* 122 */       if (!success) {
/* 123 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 130 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 135 */     if (javaChannel().finishConnect()) {
/* 136 */       selectionKey().interestOps(
/* 137 */         selectionKey().interestOps() & 0xFFFFFFF7);
/*     */     } else {
/* 139 */       throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected int doReadBytes(ByteBuf byteBuf)
/*     */     throws Exception
/*     */   {
/* 146 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 147 */     allocHandle.attemptedBytesRead(byteBuf.writableBytes());
/* 148 */     return byteBuf.writeBytes(javaChannel(), allocHandle.attemptedBytesRead());
/*     */   }
/*     */   
/*     */   protected int doWriteBytes(ByteBuf byteBuf) throws Exception
/*     */   {
/* 153 */     int expectedWrittenBytes = byteBuf.readableBytes();
/* 154 */     return byteBuf.readBytes(javaChannel(), expectedWrittenBytes);
/*     */   }
/*     */   
/*     */   protected ChannelFuture shutdownInput()
/*     */   {
/* 159 */     return newFailedFuture(new UnsupportedOperationException("shutdownInput"));
/*     */   }
/*     */   
/*     */   protected long doWriteFileRegion(FileRegion region) throws Exception
/*     */   {
/* 164 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 169 */     SocketChannelUDT channelUDT = javaChannel();
/* 170 */     return (channelUDT.isOpen()) && (channelUDT.isConnectFinished());
/*     */   }
/*     */   
/*     */   protected SocketChannelUDT javaChannel()
/*     */   {
/* 175 */     return (SocketChannelUDT)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 180 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 185 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 190 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 195 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   private static void privilegedBind(SocketChannelUDT socketChannel, final SocketAddress localAddress) throws IOException
/*     */   {
/*     */     try {
/* 201 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/* 204 */           this.val$socketChannel.bind(localAddress);
/* 205 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 209 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\udt\nio\NioUdtByteConnectorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */