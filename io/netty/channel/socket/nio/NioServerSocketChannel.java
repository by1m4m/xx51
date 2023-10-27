/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.socket.DefaultServerSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class NioServerSocketChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements io.netty.channel.socket.ServerSocketChannel
/*     */ {
/*  48 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*  49 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*     */   
/*  51 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioServerSocketChannel.class);
/*     */   
/*     */ 
/*     */   private final ServerSocketChannelConfig config;
/*     */   
/*     */ 
/*     */   private static java.nio.channels.ServerSocketChannel newSocket(SelectorProvider provider)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       return provider.openServerSocketChannel();
/*     */     } catch (IOException e) {
/*  63 */       throw new ChannelException("Failed to open a server socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioServerSocketChannel()
/*     */   {
/*  74 */     this(newSocket(DEFAULT_SELECTOR_PROVIDER));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioServerSocketChannel(SelectorProvider provider)
/*     */   {
/*  81 */     this(newSocket(provider));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioServerSocketChannel(java.nio.channels.ServerSocketChannel channel)
/*     */   {
/*  88 */     super(null, channel, 16);
/*  89 */     this.config = new NioServerSocketChannelConfig(this, javaChannel().socket(), null);
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/*  94 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  99 */     return METADATA;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig config()
/*     */   {
/* 104 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 109 */     return javaChannel().socket().isBound();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   protected java.nio.channels.ServerSocketChannel javaChannel()
/*     */   {
/* 119 */     return (java.nio.channels.ServerSocketChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 124 */     return SocketUtils.localSocketAddress(javaChannel().socket());
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 129 */     if (PlatformDependent.javaVersion() >= 7) {
/* 130 */       javaChannel().bind(localAddress, this.config.getBacklog());
/*     */     } else {
/* 132 */       javaChannel().socket().bind(localAddress, this.config.getBacklog());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 138 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 143 */     SocketChannel ch = SocketUtils.accept(javaChannel());
/*     */     try
/*     */     {
/* 146 */       if (ch != null) {
/* 147 */         buf.add(new NioSocketChannel(this, ch));
/* 148 */         return 1;
/*     */       }
/*     */     } catch (Throwable t) {
/* 151 */       logger.warn("Failed to create a new channel from an accepted socket.", t);
/*     */       try
/*     */       {
/* 154 */         ch.close();
/*     */       } catch (Throwable t2) {
/* 156 */         logger.warn("Failed to close a socket.", t2);
/*     */       }
/*     */     }
/*     */     
/* 160 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 167 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 182 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 187 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected final Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 192 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private final class NioServerSocketChannelConfig extends DefaultServerSocketChannelConfig {
/*     */     private NioServerSocketChannelConfig(NioServerSocketChannel channel, ServerSocket javaSocket) {
/* 197 */       super(javaSocket);
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 202 */       NioServerSocketChannel.this.clearReadPending();
/*     */     }
/*     */     
/*     */     public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */     {
/* 207 */       if ((PlatformDependent.javaVersion() >= 7) && ((option instanceof NioChannelOption))) {
/* 208 */         return NioChannelOption.setOption(jdkChannel(), (NioChannelOption)option, value);
/*     */       }
/* 210 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*     */     public <T> T getOption(ChannelOption<T> option)
/*     */     {
/* 215 */       if ((PlatformDependent.javaVersion() >= 7) && ((option instanceof NioChannelOption))) {
/* 216 */         return (T)NioChannelOption.getOption(jdkChannel(), (NioChannelOption)option);
/*     */       }
/* 218 */       return (T)super.getOption(option);
/*     */     }
/*     */     
/*     */ 
/*     */     public Map<ChannelOption<?>, Object> getOptions()
/*     */     {
/* 224 */       if (PlatformDependent.javaVersion() >= 7) {
/* 225 */         return getOptions(super.getOptions(), NioChannelOption.getOptions(jdkChannel()));
/*     */       }
/* 227 */       return super.getOptions();
/*     */     }
/*     */     
/*     */     private java.nio.channels.ServerSocketChannel jdkChannel() {
/* 231 */       return ((NioServerSocketChannel)this.channel).javaChannel();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean closeOnReadError(Throwable cause)
/*     */   {
/* 238 */     return super.closeOnReadError(cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\socket\nio\NioServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */