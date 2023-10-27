/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ImmediateEventExecutor
/*     */   extends AbstractEventExecutor
/*     */ {
/*  33 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ImmediateEventExecutor.class);
/*  34 */   public static final ImmediateEventExecutor INSTANCE = new ImmediateEventExecutor();
/*     */   
/*     */ 
/*     */ 
/*  38 */   private static final FastThreadLocal<Queue<Runnable>> DELAYED_RUNNABLES = new FastThreadLocal()
/*     */   {
/*     */     protected Queue<Runnable> initialValue() throws Exception {
/*  41 */       return new ArrayDeque();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*  47 */   private static final FastThreadLocal<Boolean> RUNNING = new FastThreadLocal()
/*     */   {
/*     */     protected Boolean initialValue() throws Exception {
/*  50 */       return Boolean.valueOf(false);
/*     */     }
/*     */   };
/*     */   
/*  54 */   private final Future<?> terminationFuture = new FailedFuture(GlobalEventExecutor.INSTANCE, new UnsupportedOperationException());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean inEventLoop()
/*     */   {
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/*  66 */     return true;
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/*  71 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/*  76 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void shutdown() {}
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/*  90 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit)
/*     */   {
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/* 105 */     if (command == null) {
/* 106 */       throw new NullPointerException("command");
/*     */     }
/* 108 */     if (!((Boolean)RUNNING.get()).booleanValue()) {
/* 109 */       RUNNING.set(Boolean.valueOf(true));
/*     */       try {
/* 111 */         command.run(); } catch (Throwable cause) { Queue<Runnable> delayedRunnables;
/*     */         Runnable runnable;
/* 113 */         logger.info("Throwable caught while executing Runnable {}", command, cause); } finally { Queue<Runnable> delayedRunnables;
/*     */         Runnable runnable;
/* 115 */         Queue<Runnable> delayedRunnables = (Queue)DELAYED_RUNNABLES.get();
/*     */         Runnable runnable;
/* 117 */         while ((runnable = (Runnable)delayedRunnables.poll()) != null) {
/*     */           try {
/* 119 */             runnable.run();
/*     */           } catch (Throwable cause) {
/* 121 */             logger.info("Throwable caught while executing Runnable {}", runnable, cause);
/*     */           }
/*     */         }
/* 124 */         RUNNING.set(Boolean.valueOf(false));
/*     */       }
/*     */     } else {
/* 127 */       ((Queue)DELAYED_RUNNABLES.get()).add(command);
/*     */     }
/*     */   }
/*     */   
/*     */   public <V> Promise<V> newPromise()
/*     */   {
/* 133 */     return new ImmediatePromise(this);
/*     */   }
/*     */   
/*     */   public <V> ProgressivePromise<V> newProgressivePromise()
/*     */   {
/* 138 */     return new ImmediateProgressivePromise(this);
/*     */   }
/*     */   
/*     */   static class ImmediatePromise<V> extends DefaultPromise<V> {
/*     */     ImmediatePromise(EventExecutor executor) {
/* 143 */       super();
/*     */     }
/*     */     
/*     */     protected void checkDeadLock() {}
/*     */   }
/*     */   
/*     */   static class ImmediateProgressivePromise<V>
/*     */     extends DefaultProgressivePromise<V>
/*     */   {
/*     */     ImmediateProgressivePromise(EventExecutor executor)
/*     */     {
/* 154 */       super();
/*     */     }
/*     */     
/*     */     protected void checkDeadLock() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\ImmediateEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */