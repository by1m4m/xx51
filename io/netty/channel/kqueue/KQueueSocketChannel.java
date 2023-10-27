/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.socket.ServerSocketChannel;
/*    */ import io.netty.channel.socket.SocketChannel;
/*    */ import io.netty.util.concurrent.GlobalEventExecutor;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class KQueueSocketChannel
/*    */   extends AbstractKQueueStreamChannel
/*    */   implements SocketChannel
/*    */ {
/*    */   private final KQueueSocketChannelConfig config;
/*    */   
/*    */   public KQueueSocketChannel()
/*    */   {
/* 32 */     super(null, BsdSocket.newSocketStream(), false);
/* 33 */     this.config = new KQueueSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */   public KQueueSocketChannel(int fd) {
/* 37 */     super(new BsdSocket(fd));
/* 38 */     this.config = new KQueueSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */   KQueueSocketChannel(Channel parent, BsdSocket fd, InetSocketAddress remoteAddress) {
/* 42 */     super(parent, fd, remoteAddress);
/* 43 */     this.config = new KQueueSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */   public InetSocketAddress remoteAddress()
/*    */   {
/* 48 */     return (InetSocketAddress)super.remoteAddress();
/*    */   }
/*    */   
/*    */   public InetSocketAddress localAddress()
/*    */   {
/* 53 */     return (InetSocketAddress)super.localAddress();
/*    */   }
/*    */   
/*    */   public KQueueSocketChannelConfig config()
/*    */   {
/* 58 */     return this.config;
/*    */   }
/*    */   
/*    */   public ServerSocketChannel parent()
/*    */   {
/* 63 */     return (ServerSocketChannel)super.parent();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 68 */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() { return new KQueueSocketChannelUnsafe(null); }
/*    */   
/*    */   private final class KQueueSocketChannelUnsafe extends AbstractKQueueStreamChannel.KQueueStreamUnsafe {
/* 71 */     private KQueueSocketChannelUnsafe() { super(); }
/*    */     
/*    */     protected Executor prepareToClose()
/*    */     {
/*    */       try
/*    */       {
/* 77 */         if ((KQueueSocketChannel.this.isOpen()) && (KQueueSocketChannel.this.config().getSoLinger() > 0))
/*    */         {
/*    */ 
/*    */ 
/*    */ 
/* 82 */           ((KQueueEventLoop)KQueueSocketChannel.this.eventLoop()).remove(KQueueSocketChannel.this);
/* 83 */           return GlobalEventExecutor.INSTANCE;
/*    */         }
/*    */       }
/*    */       catch (Throwable localThrowable) {}
/*    */       
/*    */ 
/*    */ 
/* 90 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\kqueue\KQueueSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */