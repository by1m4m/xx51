/*    */ package io.netty.channel;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ public class ThreadPerChannelEventLoop
/*    */   extends SingleThreadEventLoop
/*    */ {
/*    */   private final ThreadPerChannelEventLoopGroup parent;
/*    */   private Channel ch;
/*    */   
/*    */   public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup parent)
/*    */   {
/* 29 */     super(parent, parent.executor, true);
/* 30 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public ChannelFuture register(ChannelPromise promise)
/*    */   {
/* 35 */     super.register(promise).addListener(new ChannelFutureListener()
/*    */     {
/*    */       public void operationComplete(ChannelFuture future) throws Exception {
/* 38 */         if (future.isSuccess()) {
/* 39 */           ThreadPerChannelEventLoop.this.ch = future.channel();
/*    */         } else {
/* 41 */           ThreadPerChannelEventLoop.this.deregister();
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*    */   {
/* 50 */     super.register(channel, promise).addListener(new ChannelFutureListener()
/*    */     {
/*    */       public void operationComplete(ChannelFuture future) throws Exception {
/* 53 */         if (future.isSuccess()) {
/* 54 */           ThreadPerChannelEventLoop.this.ch = future.channel();
/*    */         } else {
/* 56 */           ThreadPerChannelEventLoop.this.deregister();
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   protected void run()
/*    */   {
/*    */     for (;;) {
/* 65 */       Runnable task = takeTask();
/* 66 */       if (task != null) {
/* 67 */         task.run();
/* 68 */         updateLastExecutionTime();
/*    */       }
/*    */       
/* 71 */       Channel ch = this.ch;
/* 72 */       if (isShuttingDown()) {
/* 73 */         if (ch != null) {
/* 74 */           ch.unsafe().close(ch.unsafe().voidPromise());
/*    */         }
/* 76 */         if (confirmShutdown()) {
/*    */           break;
/*    */         }
/*    */       }
/* 80 */       else if (ch != null)
/*    */       {
/* 82 */         if (!ch.isRegistered()) {
/* 83 */           runAllTasks();
/* 84 */           deregister();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected void deregister()
/*    */   {
/* 92 */     this.ch = null;
/* 93 */     this.parent.activeChildren.remove(this);
/* 94 */     this.parent.idleChildren.add(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\ThreadPerChannelEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */