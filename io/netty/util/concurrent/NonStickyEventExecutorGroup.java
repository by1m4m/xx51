/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NonStickyEventExecutorGroup
/*     */   implements EventExecutorGroup
/*     */ {
/*     */   private final EventExecutorGroup group;
/*     */   private final int maxTaskExecutePerRun;
/*     */   
/*     */   public NonStickyEventExecutorGroup(EventExecutorGroup group)
/*     */   {
/*  50 */     this(group, 1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NonStickyEventExecutorGroup(EventExecutorGroup group, int maxTaskExecutePerRun)
/*     */   {
/*  58 */     this.group = verify(group);
/*  59 */     this.maxTaskExecutePerRun = ObjectUtil.checkPositive(maxTaskExecutePerRun, "maxTaskExecutePerRun");
/*     */   }
/*     */   
/*     */   private static EventExecutorGroup verify(EventExecutorGroup group) {
/*  63 */     Iterator<EventExecutor> executors = ((EventExecutorGroup)ObjectUtil.checkNotNull(group, "group")).iterator();
/*  64 */     while (executors.hasNext()) {
/*  65 */       EventExecutor executor = (EventExecutor)executors.next();
/*  66 */       if ((executor instanceof OrderedEventExecutor)) {
/*  67 */         throw new IllegalArgumentException("EventExecutorGroup " + group + " contains OrderedEventExecutors: " + executor);
/*     */       }
/*     */     }
/*     */     
/*  71 */     return group;
/*     */   }
/*     */   
/*     */   private NonStickyOrderedEventExecutor newExecutor(EventExecutor executor) {
/*  75 */     return new NonStickyOrderedEventExecutor(executor, this.maxTaskExecutePerRun);
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/*  80 */     return this.group.isShuttingDown();
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully()
/*     */   {
/*  85 */     return this.group.shutdownGracefully();
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/*  90 */     return this.group.shutdownGracefully(quietPeriod, timeout, unit);
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/*  95 */     return this.group.terminationFuture();
/*     */   }
/*     */   
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 101 */     this.group.shutdown();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<Runnable> shutdownNow()
/*     */   {
/* 107 */     return this.group.shutdownNow();
/*     */   }
/*     */   
/*     */   public EventExecutor next()
/*     */   {
/* 112 */     return newExecutor(this.group.next());
/*     */   }
/*     */   
/*     */   public Iterator<EventExecutor> iterator()
/*     */   {
/* 117 */     final Iterator<EventExecutor> itr = this.group.iterator();
/* 118 */     new Iterator()
/*     */     {
/*     */       public boolean hasNext() {
/* 121 */         return itr.hasNext();
/*     */       }
/*     */       
/*     */       public EventExecutor next()
/*     */       {
/* 126 */         return NonStickyEventExecutorGroup.this.newExecutor((EventExecutor)itr.next());
/*     */       }
/*     */       
/*     */       public void remove()
/*     */       {
/* 131 */         itr.remove();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public Future<?> submit(Runnable task)
/*     */   {
/* 138 */     return this.group.submit(task);
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, T result)
/*     */   {
/* 143 */     return this.group.submit(task, result);
/*     */   }
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task)
/*     */   {
/* 148 */     return this.group.submit(task);
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
/*     */   {
/* 153 */     return this.group.schedule(command, delay, unit);
/*     */   }
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
/*     */   {
/* 158 */     return this.group.schedule(callable, delay, unit);
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
/*     */   {
/* 163 */     return this.group.scheduleAtFixedRate(command, initialDelay, period, unit);
/*     */   }
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
/*     */   {
/* 168 */     return this.group.scheduleWithFixedDelay(command, initialDelay, delay, unit);
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 173 */     return this.group.isShutdown();
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 178 */     return this.group.isTerminated();
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 183 */     return this.group.awaitTermination(timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
/*     */     throws InterruptedException
/*     */   {
/* 189 */     return this.group.invokeAll(tasks);
/*     */   }
/*     */   
/*     */   public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 195 */     return this.group.invokeAll(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
/*     */   {
/* 200 */     return (T)this.group.invokeAny(tasks);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 206 */     return (T)this.group.invokeAny(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public void execute(Runnable command)
/*     */   {
/* 211 */     this.group.execute(command);
/*     */   }
/*     */   
/*     */   private static final class NonStickyOrderedEventExecutor extends AbstractEventExecutor implements Runnable, OrderedEventExecutor
/*     */   {
/*     */     private final EventExecutor executor;
/* 217 */     private final Queue<Runnable> tasks = PlatformDependent.newMpscQueue();
/*     */     
/*     */     private static final int NONE = 0;
/*     */     
/*     */     private static final int SUBMITTED = 1;
/*     */     private static final int RUNNING = 2;
/* 223 */     private final AtomicInteger state = new AtomicInteger();
/*     */     private final int maxTaskExecutePerRun;
/*     */     
/*     */     NonStickyOrderedEventExecutor(EventExecutor executor, int maxTaskExecutePerRun) {
/* 227 */       super();
/* 228 */       this.executor = executor;
/* 229 */       this.maxTaskExecutePerRun = maxTaskExecutePerRun;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 234 */       if (!this.state.compareAndSet(1, 2)) {
/* 235 */         return;
/*     */       }
/*     */       for (;;) {
/* 238 */         int i = 0;
/*     */         try {
/* 240 */           for (; i < this.maxTaskExecutePerRun; i++) {
/* 241 */             Runnable task = (Runnable)this.tasks.poll();
/* 242 */             if (task == null) {
/*     */               break;
/*     */             }
/* 245 */             safeExecute(task);
/*     */           }
/*     */         } finally {
/* 248 */           if (i == this.maxTaskExecutePerRun) {
/*     */             try {
/* 250 */               this.state.set(1);
/* 251 */               this.executor.execute(this);
/* 252 */               return;
/*     */             }
/*     */             catch (Throwable ignore) {
/* 255 */               this.state.set(2);
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 261 */             this.state.set(0);
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
/* 277 */             if ((this.tasks.peek() == null) || (!this.state.compareAndSet(0, 2))) {
/* 278 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean inEventLoop(Thread thread)
/*     */     {
/* 287 */       return false;
/*     */     }
/*     */     
/*     */     public boolean inEventLoop()
/*     */     {
/* 292 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isShuttingDown()
/*     */     {
/* 297 */       return this.executor.isShutdown();
/*     */     }
/*     */     
/*     */     public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */     {
/* 302 */       return this.executor.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/*     */     
/*     */     public Future<?> terminationFuture()
/*     */     {
/* 307 */       return this.executor.terminationFuture();
/*     */     }
/*     */     
/*     */     public void shutdown()
/*     */     {
/* 312 */       this.executor.shutdown();
/*     */     }
/*     */     
/*     */     public boolean isShutdown()
/*     */     {
/* 317 */       return this.executor.isShutdown();
/*     */     }
/*     */     
/*     */     public boolean isTerminated()
/*     */     {
/* 322 */       return this.executor.isTerminated();
/*     */     }
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 327 */       return this.executor.awaitTermination(timeout, unit);
/*     */     }
/*     */     
/*     */     public void execute(Runnable command)
/*     */     {
/* 332 */       if (!this.tasks.offer(command)) {
/* 333 */         throw new RejectedExecutionException();
/*     */       }
/* 335 */       if (this.state.compareAndSet(0, 1))
/*     */       {
/*     */         try
/*     */         {
/* 339 */           this.executor.execute(this);
/*     */         }
/*     */         catch (Throwable e) {
/* 342 */           this.tasks.remove(command);
/* 343 */           PlatformDependent.throwException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\NonStickyEventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */