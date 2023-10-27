/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.SocketAddress;
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
/*     */ public abstract class AbstractBootstrapConfig<B extends AbstractBootstrap<B, C>, C extends Channel>
/*     */ {
/*     */   protected final B bootstrap;
/*     */   
/*     */   protected AbstractBootstrapConfig(B bootstrap)
/*     */   {
/*  37 */     this.bootstrap = ((AbstractBootstrap)ObjectUtil.checkNotNull(bootstrap, "bootstrap"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final SocketAddress localAddress()
/*     */   {
/*  44 */     return this.bootstrap.localAddress();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ChannelFactory<? extends C> channelFactory()
/*     */   {
/*  52 */     return this.bootstrap.channelFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final ChannelHandler handler()
/*     */   {
/*  59 */     return this.bootstrap.handler();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Map<ChannelOption<?>, Object> options()
/*     */   {
/*  66 */     return this.bootstrap.options();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Map<AttributeKey<?>, Object> attrs()
/*     */   {
/*  73 */     return this.bootstrap.attrs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final EventLoopGroup group()
/*     */   {
/*  81 */     return this.bootstrap.group();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  88 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append('(');
/*  89 */     EventLoopGroup group = group();
/*  90 */     if (group != null)
/*     */     {
/*     */ 
/*  93 */       buf.append("group: ").append(StringUtil.simpleClassName(group)).append(", ");
/*     */     }
/*     */     
/*  96 */     ChannelFactory<? extends C> factory = channelFactory();
/*  97 */     if (factory != null)
/*     */     {
/*     */ 
/* 100 */       buf.append("channelFactory: ").append(factory).append(", ");
/*     */     }
/* 102 */     SocketAddress localAddress = localAddress();
/* 103 */     if (localAddress != null)
/*     */     {
/*     */ 
/* 106 */       buf.append("localAddress: ").append(localAddress).append(", ");
/*     */     }
/*     */     
/* 109 */     Map<ChannelOption<?>, Object> options = options();
/* 110 */     if (!options.isEmpty())
/*     */     {
/*     */ 
/* 113 */       buf.append("options: ").append(options).append(", ");
/*     */     }
/* 115 */     Map<AttributeKey<?>, Object> attrs = attrs();
/* 116 */     if (!attrs.isEmpty())
/*     */     {
/*     */ 
/* 119 */       buf.append("attrs: ").append(attrs).append(", ");
/*     */     }
/* 121 */     ChannelHandler handler = handler();
/* 122 */     if (handler != null)
/*     */     {
/*     */ 
/* 125 */       buf.append("handler: ").append(handler).append(", ");
/*     */     }
/* 127 */     if (buf.charAt(buf.length() - 1) == '(') {
/* 128 */       buf.append(')');
/*     */     } else {
/* 130 */       buf.setCharAt(buf.length() - 2, ')');
/* 131 */       buf.setLength(buf.length() - 1);
/*     */     }
/* 133 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\bootstrap\AbstractBootstrapConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */