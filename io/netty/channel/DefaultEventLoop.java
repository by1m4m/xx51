/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.DefaultThreadFactory;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ public class DefaultEventLoop
/*    */   extends SingleThreadEventLoop
/*    */ {
/*    */   public DefaultEventLoop()
/*    */   {
/* 26 */     this((EventLoopGroup)null);
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(ThreadFactory threadFactory) {
/* 30 */     this(null, threadFactory);
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(Executor executor) {
/* 34 */     this(null, executor);
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(EventLoopGroup parent) {
/* 38 */     this(parent, new DefaultThreadFactory(DefaultEventLoop.class));
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(EventLoopGroup parent, ThreadFactory threadFactory) {
/* 42 */     super(parent, threadFactory, true);
/*    */   }
/*    */   
/*    */   public DefaultEventLoop(EventLoopGroup parent, Executor executor) {
/* 46 */     super(parent, executor, true);
/*    */   }
/*    */   
/*    */   protected void run()
/*    */   {
/*    */     for (;;) {
/* 52 */       Runnable task = takeTask();
/* 53 */       if (task != null) {
/* 54 */         task.run();
/* 55 */         updateLastExecutionTime();
/*    */       }
/*    */       
/* 58 */       if (confirmShutdown()) {
/*    */         break;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\DefaultEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */