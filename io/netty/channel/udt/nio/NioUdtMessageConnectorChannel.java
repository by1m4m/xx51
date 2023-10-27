/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.NioSocketUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.udt.DefaultUdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtChannelConfig;
/*     */ import io.netty.channel.udt.UdtMessage;
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
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class NioUdtMessageConnectorChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements UdtChannel
/*     */ {
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtMessageConnectorChannel.class);
/*     */   
/*  57 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   private final UdtChannelConfig config;
/*     */   
/*     */   public NioUdtMessageConnectorChannel()
/*     */   {
/*  62 */     this(TypeUDT.DATAGRAM);
/*     */   }
/*     */   
/*     */   public NioUdtMessageConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
/*  66 */     super(parent, channelUDT, 1);
/*     */     try {
/*  68 */       channelUDT.configureBlocking(false);
/*  69 */       switch (channelUDT.socketUDT().status()) {
/*     */       case INIT: 
/*     */       case OPENED: 
/*  72 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, true);
/*  73 */         break;
/*     */       default: 
/*  75 */         this.config = new DefaultUdtChannelConfig(this, channelUDT, false);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*     */       try {
/*  80 */         channelUDT.close();
/*     */       } catch (Exception e2) {
/*  82 */         if (logger.isWarnEnabled()) {
/*  83 */           logger.warn("Failed to close channel.", e2);
/*     */         }
/*     */       }
/*  86 */       throw new ChannelException("Failed to configure channel.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public NioUdtMessageConnectorChannel(SocketChannelUDT channelUDT) {
/*  91 */     this(null, channelUDT);
/*     */   }
/*     */   
/*     */   public NioUdtMessageConnectorChannel(TypeUDT type) {
/*  95 */     this(NioUdtProvider.newConnectorChannelUDT(type));
/*     */   }
/*     */   
/*     */   public UdtChannelConfig config()
/*     */   {
/* 100 */     return this.config;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 105 */     privilegedBind(javaChannel(), localAddress);
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 110 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 116 */     doBind(localAddress != null ? localAddress : new InetSocketAddress(0));
/* 117 */     boolean success = false;
/*     */     try {
/* 119 */       boolean connected = SocketUtils.connect(javaChannel(), remoteAddress);
/* 120 */       if (!connected) {
/* 121 */         selectionKey().interestOps(
/* 122 */           selectionKey().interestOps() | 0x8);
/*     */       }
/* 124 */       success = true;
/* 125 */       return connected;
/*     */     } finally {
/* 127 */       if (!success) {
/* 128 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 135 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 140 */     if (javaChannel().finishConnect()) {
/* 141 */       selectionKey().interestOps(
/* 142 */         selectionKey().interestOps() & 0xFFFFFFF7);
/*     */     } else {
/* 144 */       throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected int doReadMessages(List<Object> buf)
/*     */     throws Exception
/*     */   {
/* 152 */     int maximumMessageSize = this.config.getReceiveBufferSize();
/*     */     
/* 154 */     ByteBuf byteBuf = this.config.getAllocator().directBuffer(maximumMessageSize);
/*     */     
/*     */ 
/* 157 */     int receivedMessageSize = byteBuf.writeBytes(javaChannel(), maximumMessageSize);
/*     */     
/*     */ 
/* 160 */     if (receivedMessageSize <= 0) {
/* 161 */       byteBuf.release();
/* 162 */       return 0;
/*     */     }
/*     */     
/* 165 */     if (receivedMessageSize >= maximumMessageSize) {
/* 166 */       javaChannel().close();
/* 167 */       throw new ChannelException("Invalid config : increase receive buffer size to avoid message truncation");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 172 */     buf.add(new UdtMessage(byteBuf));
/*     */     
/* 174 */     return 1;
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in)
/*     */     throws Exception
/*     */   {
/* 180 */     UdtMessage message = (UdtMessage)msg;
/*     */     
/* 182 */     ByteBuf byteBuf = message.content();
/*     */     
/* 184 */     int messageSize = byteBuf.readableBytes();
/* 185 */     if (messageSize == 0) {
/* 186 */       return true;
/*     */     }
/*     */     long writtenBytes;
/*     */     long writtenBytes;
/* 190 */     if (byteBuf.nioBufferCount() == 1) {
/* 191 */       writtenBytes = javaChannel().write(byteBuf.nioBuffer());
/*     */     } else {
/* 193 */       writtenBytes = javaChannel().write(byteBuf.nioBuffers());
/*     */     }
/*     */     
/*     */ 
/* 197 */     if ((writtenBytes > 0L) && (writtenBytes != messageSize)) {
/* 198 */       throw new Error("Provider error: failed to write message. Provider library should be upgraded.");
/*     */     }
/*     */     
/*     */ 
/* 202 */     return writtenBytes > 0L;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 207 */     SocketChannelUDT channelUDT = javaChannel();
/* 208 */     return (channelUDT.isOpen()) && (channelUDT.isConnectFinished());
/*     */   }
/*     */   
/*     */   protected SocketChannelUDT javaChannel()
/*     */   {
/* 213 */     return (SocketChannelUDT)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 218 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 223 */     return METADATA;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 228 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 233 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 238 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   private static void privilegedBind(SocketChannelUDT socketChannel, final SocketAddress localAddress) throws IOException
/*     */   {
/*     */     try {
/* 244 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/* 247 */           this.val$socketChannel.bind(localAddress);
/* 248 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 252 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\udt\nio\NioUdtMessageConnectorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */