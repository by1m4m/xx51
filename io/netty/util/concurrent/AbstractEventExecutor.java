/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.AbstractExecutorService;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.RunnableFuture;
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
/*     */ public abstract class AbstractEventExecutor
/*     */   extends AbstractExecutorService
/*     */   implements EventExecutor
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractEventExecutor.class);
/*     */   
/*     */   static final long DEFAULT_SHUTDOWN_QUIET_PERIOD = 2L;
/*     */   
/*     */   static final long DEFAULT_SHUTDOWN_TIMEOUT = 15L;
/*     */   private final EventExecutorGroup parent;
/*  40 */   private final Collection<EventExecutor> selfCollection = Collections.singleton(this);
/*     */   
/*     */   protected AbstractEventExecutor() {
/*  43 */     this(null);
/*     */   }
/*     */   
/*     */   protected AbstractEventExecutor(EventExecutorGroup parent) {
/*  47 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public EventExecutorGroup parent()
/*     */   {
/*  52 */     return this.parent;
/*     */   }
/*     */   
/*     */   public EventExecutor next()
/*     */   {
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   public boolean inEventLoop()
/*     */   {
/*  62 */     return inEventLoop(Thread.currentThread());
/*     */   }
/*     */   
/*     */   public Iterator<EventExecutor> iterator()
/*     */   {
/*  67 */     return this.selfCollection.iterator();
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully()
/*     */   {
/*  72 */     return shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public abstract void shutdown();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public List<Runnable> shutdownNow()
/*     */   {
/*  88 */     shutdown();
/*  89 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public <V> Promise<V> newPromise()
/*     */   {
/*  94 */     return new DefaultPromise(this);
/*     */   }
/*     */   
/*     */   public <V> ProgressivePromise<V> newProgressivePromise()
/*     */   {
/*  99 */     return new DefaultProgressivePromise(this);
/*     */   }
/*     */   
/*     */   public <V> Future<V> newSucceededFuture(V result)
/*     */   {
/* 104 */     return new SucceededFuture(this, result);
/*     */   }
/*     */   
/*     */   public <V> Future<V> newFailedFuture(Throwable cause)
/*     */   {
/* 109 */     return new FailedFuture(this, cause);
/*     */   }
/*     */   
/*     */   public Future<?> submit(Runnable task)
/*     */   {
/* 114 */     return (Future)super.submit(task);
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, T result)
/*     */   {
/* 119 */     return (Future)super.submit(task, result);
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task)
/*     */   {
/* 124 */     return (Future)super.submit(task);
/*     */   }
/*     */   
/*     */   protected final <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
/*     */   {
/* 129 */     return new PromiseTask(this, runnable, value);
/*     */   }
/*     */   
/*     */   protected final <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
/*     */   {
/* 134 */     return new PromiseTask(this, callable);
/*     */   }
/*     */   
/*     */ 
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
/*     */   {
/* 140 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
/*     */   {
/* 145 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
/*     */   {
/* 150 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
/*     */   {
/* 155 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   protected static void safeExecute(Runnable task)
/*     */   {
/*     */     try
/*     */     {
/* 163 */       task.run();
/*     */     } catch (Throwable t) {
/* 165 */       logger.warn("A task raised an exception. Task: {}", task, t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\AbstractEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */