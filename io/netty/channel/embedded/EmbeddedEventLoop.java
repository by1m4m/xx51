/*     */ package io.netty.channel.embedded;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.util.concurrent.AbstractScheduledEventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ final class EmbeddedEventLoop
/*     */   extends AbstractScheduledEventExecutor
/*     */   implements EventLoop
/*     */ {
/*  34 */   private final Queue<Runnable> tasks = new ArrayDeque(2);
/*     */   
/*     */   public EventLoopGroup parent()
/*     */   {
/*  38 */     return (EventLoopGroup)super.parent();
/*     */   }
/*     */   
/*     */   public EventLoop next()
/*     */   {
/*  43 */     return (EventLoop)super.next();
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/*  48 */     if (command == null) {
/*  49 */       throw new NullPointerException("command");
/*     */     }
/*  51 */     this.tasks.add(command);
/*     */   }
/*     */   
/*     */   void runTasks() {
/*     */     for (;;) {
/*  56 */       Runnable task = (Runnable)this.tasks.poll();
/*  57 */       if (task == null) {
/*     */         break;
/*     */       }
/*     */       
/*  61 */       task.run();
/*     */     }
/*     */   }
/*     */   
/*     */   long runScheduledTasks() {
/*  66 */     long time = AbstractScheduledEventExecutor.nanoTime();
/*     */     for (;;) {
/*  68 */       Runnable task = pollScheduledTask(time);
/*  69 */       if (task == null) {
/*  70 */         return nextScheduledTaskNano();
/*     */       }
/*     */       
/*  73 */       task.run();
/*     */     }
/*     */   }
/*     */   
/*     */   long nextScheduledTask() {
/*  78 */     return nextScheduledTaskNano();
/*     */   }
/*     */   
/*     */   protected void cancelScheduledTasks()
/*     */   {
/*  83 */     super.cancelScheduledTasks();
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/*  93 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   public ChannelFuture register(Channel channel)
/*     */   {
/* 124 */     return register(new DefaultChannelPromise(channel, this));
/*     */   }
/*     */   
/*     */   public ChannelFuture register(ChannelPromise promise)
/*     */   {
/* 129 */     ObjectUtil.checkNotNull(promise, "promise");
/* 130 */     promise.channel().unsafe().register(this, promise);
/* 131 */     return promise;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public ChannelFuture register(Channel channel, ChannelPromise promise)
/*     */   {
/* 137 */     channel.unsafe().register(this, promise);
/* 138 */     return promise;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop()
/*     */   {
/* 143 */     return true;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/* 148 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\embedded\EmbeddedEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */