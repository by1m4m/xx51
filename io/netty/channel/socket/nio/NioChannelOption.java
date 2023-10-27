/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketOption;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.NetworkChannel;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public final class NioChannelOption<T>
/*     */   extends ChannelOption<T>
/*     */ {
/*     */   private final SocketOption<T> option;
/*     */   
/*     */   private NioChannelOption(SocketOption<T> option)
/*     */   {
/*  38 */     super(option.name());
/*  39 */     this.option = option;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T> ChannelOption<T> of(SocketOption<T> option)
/*     */   {
/*  46 */     return new NioChannelOption(option);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <T> boolean setOption(Channel jdkChannel, NioChannelOption<T> option, T value)
/*     */   {
/*  57 */     NetworkChannel channel = (NetworkChannel)jdkChannel;
/*  58 */     if (!channel.supportedOptions().contains(option.option)) {
/*  59 */       return false;
/*     */     }
/*  61 */     if (((channel instanceof ServerSocketChannel)) && (option.option == StandardSocketOptions.IP_TOS))
/*     */     {
/*     */ 
/*  64 */       return false;
/*     */     }
/*     */     try {
/*  67 */       channel.setOption(option.option, value);
/*  68 */       return true;
/*     */     } catch (IOException e) {
/*  70 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   static <T> T getOption(Channel jdkChannel, NioChannelOption<T> option) {
/*  75 */     NetworkChannel channel = (NetworkChannel)jdkChannel;
/*     */     
/*  77 */     if (!channel.supportedOptions().contains(option.option)) {
/*  78 */       return null;
/*     */     }
/*  80 */     if (((channel instanceof ServerSocketChannel)) && (option.option == StandardSocketOptions.IP_TOS))
/*     */     {
/*     */ 
/*  83 */       return null;
/*     */     }
/*     */     try {
/*  86 */       return (T)channel.getOption(option.option);
/*     */     } catch (IOException e) {
/*  88 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   static ChannelOption[] getOptions(Channel jdkChannel)
/*     */   {
/*  94 */     NetworkChannel channel = (NetworkChannel)jdkChannel;
/*  95 */     Set<SocketOption<?>> supportedOpts = channel.supportedOptions();
/*     */     SocketOption<?> opt;
/*  97 */     if ((channel instanceof ServerSocketChannel)) {
/*  98 */       List<ChannelOption<?>> extraOpts = new ArrayList(supportedOpts.size());
/*  99 */       for (Iterator localIterator = supportedOpts.iterator(); localIterator.hasNext();) { opt = (SocketOption)localIterator.next();
/* 100 */         if (opt != StandardSocketOptions.IP_TOS)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 105 */           extraOpts.add(new NioChannelOption(opt)); }
/*     */       }
/* 107 */       return (ChannelOption[])extraOpts.toArray(new ChannelOption[0]);
/*     */     }
/* 109 */     ChannelOption<?>[] extraOpts = new ChannelOption[supportedOpts.size()];
/*     */     
/* 111 */     int i = 0;
/* 112 */     for (SocketOption<?> opt : supportedOpts) {
/* 113 */       extraOpts[(i++)] = new NioChannelOption(opt);
/*     */     }
/* 115 */     return extraOpts;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\socket\nio\NioChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */