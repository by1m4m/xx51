/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.PriorityQueue;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ public abstract class SingleThreadEventExecutor
/*     */   extends AbstractScheduledEventExecutor
/*     */   implements OrderedEventExecutor
/*     */ {
/*  51 */   static final int DEFAULT_MAX_PENDING_EXECUTOR_TASKS = Math.max(16, 
/*  52 */     SystemPropertyUtil.getInt("io.netty.eventexecutor.maxPendingTasks", Integer.MAX_VALUE));
/*     */   
/*     */ 
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
/*     */   
/*     */   private static final int ST_NOT_STARTED = 1;
/*     */   
/*     */   private static final int ST_STARTED = 2;
/*     */   private static final int ST_SHUTTING_DOWN = 3;
/*     */   private static final int ST_SHUTDOWN = 4;
/*     */   private static final int ST_TERMINATED = 5;
/*  63 */   private static final Runnable WAKEUP_TASK = new Runnable()
/*     */   {
/*     */     public void run() {}
/*     */   };
/*     */   
/*     */ 
/*  69 */   private static final Runnable NOOP_TASK = new Runnable()
/*     */   {
/*     */     public void run() {}
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
/*     */   
/*  79 */   private static final AtomicReferenceFieldUpdater<SingleThreadEventExecutor, ThreadProperties> PROPERTIES_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SingleThreadEventExecutor.class, ThreadProperties.class, "threadProperties");
/*     */   
/*     */   private final Queue<Runnable> taskQueue;
/*     */   
/*     */   private volatile Thread thread;
/*     */   
/*     */   private volatile ThreadProperties threadProperties;
/*     */   
/*     */   private final Executor executor;
/*     */   
/*     */   private volatile boolean interrupted;
/*  90 */   private final Semaphore threadLock = new Semaphore(0);
/*  91 */   private final Set<Runnable> shutdownHooks = new LinkedHashSet();
/*     */   
/*     */   private final boolean addTaskWakesUp;
/*     */   
/*     */   private final int maxPendingTasks;
/*     */   private final RejectedExecutionHandler rejectedExecutionHandler;
/*     */   private long lastExecutionTime;
/*  98 */   private volatile int state = 1;
/*     */   
/*     */   private volatile long gracefulShutdownQuietPeriod;
/*     */   
/*     */   private volatile long gracefulShutdownTimeout;
/*     */   
/*     */   private long gracefulShutdownStartTime;
/* 105 */   private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp)
/*     */   {
/* 117 */     this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp);
/*     */   }
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
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler)
/*     */   {
/* 133 */     this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp, maxPendingTasks, rejectedHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp)
/*     */   {
/* 145 */     this(parent, executor, addTaskWakesUp, DEFAULT_MAX_PENDING_EXECUTOR_TASKS, RejectedExecutionHandlers.reject());
/*     */   }
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
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler)
/*     */   {
/* 161 */     super(parent);
/* 162 */     this.addTaskWakesUp = addTaskWakesUp;
/* 163 */     this.maxPendingTasks = Math.max(16, maxPendingTasks);
/* 164 */     this.executor = ((Executor)ObjectUtil.checkNotNull(executor, "executor"));
/* 165 */     this.taskQueue = newTaskQueue(this.maxPendingTasks);
/* 166 */     this.rejectedExecutionHandler = ((RejectedExecutionHandler)ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected Queue<Runnable> newTaskQueue()
/*     */   {
/* 174 */     return newTaskQueue(this.maxPendingTasks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks)
/*     */   {
/* 184 */     return new LinkedBlockingQueue(maxPendingTasks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void interruptThread()
/*     */   {
/* 191 */     Thread currentThread = this.thread;
/* 192 */     if (currentThread == null) {
/* 193 */       this.interrupted = true;
/*     */     } else {
/* 195 */       currentThread.interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Runnable pollTask()
/*     */   {
/* 203 */     assert (inEventLoop());
/* 204 */     return pollTaskFrom(this.taskQueue);
/*     */   }
/*     */   
/*     */   protected static Runnable pollTaskFrom(Queue<Runnable> taskQueue) {
/*     */     Runnable task;
/* 209 */     do { task = (Runnable)taskQueue.poll();
/* 210 */     } while (task == WAKEUP_TASK);
/*     */     
/*     */ 
/* 213 */     return task;
/*     */   }
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
/*     */   protected Runnable takeTask()
/*     */   {
/* 227 */     assert (inEventLoop());
/* 228 */     if (!(this.taskQueue instanceof BlockingQueue)) {
/* 229 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/* 232 */     BlockingQueue<Runnable> taskQueue = (BlockingQueue)this.taskQueue;
/*     */     for (;;) {
/* 234 */       ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 235 */       if (scheduledTask == null) {
/* 236 */         Runnable task = null;
/*     */         try {
/* 238 */           task = (Runnable)taskQueue.take();
/* 239 */           if (task == WAKEUP_TASK) {
/* 240 */             task = null;
/*     */           }
/*     */         }
/*     */         catch (InterruptedException localInterruptedException1) {}
/*     */         
/* 245 */         return task;
/*     */       }
/* 247 */       long delayNanos = scheduledTask.delayNanos();
/* 248 */       Runnable task = null;
/* 249 */       if (delayNanos > 0L) {
/*     */         try {
/* 251 */           task = (Runnable)taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
/*     */         }
/*     */         catch (InterruptedException e) {
/* 254 */           return null;
/*     */         }
/*     */       }
/* 257 */       if (task == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 262 */         fetchFromScheduledTaskQueue();
/* 263 */         task = (Runnable)taskQueue.poll();
/*     */       }
/*     */       
/* 266 */       if (task != null) {
/* 267 */         return task;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean fetchFromScheduledTaskQueue()
/*     */   {
/* 274 */     long nanoTime = AbstractScheduledEventExecutor.nanoTime();
/* 275 */     Runnable scheduledTask = pollScheduledTask(nanoTime);
/* 276 */     while (scheduledTask != null) {
/* 277 */       if (!this.taskQueue.offer(scheduledTask))
/*     */       {
/* 279 */         scheduledTaskQueue().add((ScheduledFutureTask)scheduledTask);
/* 280 */         return false;
/*     */       }
/* 282 */       scheduledTask = pollScheduledTask(nanoTime);
/*     */     }
/* 284 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Runnable peekTask()
/*     */   {
/* 291 */     assert (inEventLoop());
/* 292 */     return (Runnable)this.taskQueue.peek();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean hasTasks()
/*     */   {
/* 299 */     assert (inEventLoop());
/* 300 */     return !this.taskQueue.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int pendingTasks()
/*     */   {
/* 310 */     return this.taskQueue.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addTask(Runnable task)
/*     */   {
/* 318 */     if (task == null) {
/* 319 */       throw new NullPointerException("task");
/*     */     }
/* 321 */     if (!offerTask(task)) {
/* 322 */       reject(task);
/*     */     }
/*     */   }
/*     */   
/*     */   final boolean offerTask(Runnable task) {
/* 327 */     if (isShutdown()) {
/* 328 */       reject();
/*     */     }
/* 330 */     return this.taskQueue.offer(task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean removeTask(Runnable task)
/*     */   {
/* 337 */     if (task == null) {
/* 338 */       throw new NullPointerException("task");
/*     */     }
/* 340 */     return this.taskQueue.remove(task);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean runAllTasks()
/*     */   {
/* 349 */     assert (inEventLoop());
/*     */     
/* 351 */     boolean ranAtLeastOne = false;
/*     */     boolean fetchedAll;
/*     */     do {
/* 354 */       fetchedAll = fetchFromScheduledTaskQueue();
/* 355 */       if (runAllTasksFrom(this.taskQueue)) {
/* 356 */         ranAtLeastOne = true;
/*     */       }
/* 358 */     } while (!fetchedAll);
/*     */     
/* 360 */     if (ranAtLeastOne) {
/* 361 */       this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     }
/* 363 */     afterRunningAllTasks();
/* 364 */     return ranAtLeastOne;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean runAllTasksFrom(Queue<Runnable> taskQueue)
/*     */   {
/* 375 */     Runnable task = pollTaskFrom(taskQueue);
/* 376 */     if (task == null) {
/* 377 */       return false;
/*     */     }
/*     */     do {
/* 380 */       safeExecute(task);
/* 381 */       task = pollTaskFrom(taskQueue);
/* 382 */     } while (task != null);
/* 383 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean runAllTasks(long timeoutNanos)
/*     */   {
/* 393 */     fetchFromScheduledTaskQueue();
/* 394 */     Runnable task = pollTask();
/* 395 */     if (task == null) {
/* 396 */       afterRunningAllTasks();
/* 397 */       return false;
/*     */     }
/*     */     
/* 400 */     long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
/* 401 */     long runTasks = 0L;
/*     */     do
/*     */     {
/* 404 */       safeExecute(task);
/*     */       
/* 406 */       runTasks += 1L;
/*     */       
/*     */ 
/*     */ 
/* 410 */       if ((runTasks & 0x3F) == 0L) {
/* 411 */         long lastExecutionTime = ScheduledFutureTask.nanoTime();
/* 412 */         if (lastExecutionTime >= deadline) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/* 417 */       task = pollTask();
/* 418 */     } while (task != null);
/* 419 */     long lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 424 */     afterRunningAllTasks();
/* 425 */     this.lastExecutionTime = lastExecutionTime;
/* 426 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void afterRunningAllTasks() {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected long delayNanos(long currentTimeNanos)
/*     */   {
/* 438 */     ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 439 */     if (scheduledTask == null) {
/* 440 */       return SCHEDULE_PURGE_INTERVAL;
/*     */     }
/*     */     
/* 443 */     return scheduledTask.delayNanos(currentTimeNanos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long deadlineNanos()
/*     */   {
/* 452 */     ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 453 */     if (scheduledTask == null) {
/* 454 */       return nanoTime() + SCHEDULE_PURGE_INTERVAL;
/*     */     }
/* 456 */     return scheduledTask.deadlineNanos();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateLastExecutionTime()
/*     */   {
/* 467 */     this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void run();
/*     */   
/*     */ 
/*     */ 
/*     */   protected void cleanup() {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 483 */     if ((!inEventLoop) || (this.state == 3))
/*     */     {
/*     */ 
/* 486 */       this.taskQueue.offer(WAKEUP_TASK);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean inEventLoop(Thread thread)
/*     */   {
/* 492 */     return thread == this.thread;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addShutdownHook(final Runnable task)
/*     */   {
/* 499 */     if (inEventLoop()) {
/* 500 */       this.shutdownHooks.add(task);
/*     */     } else {
/* 502 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 505 */           SingleThreadEventExecutor.this.shutdownHooks.add(task);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeShutdownHook(final Runnable task)
/*     */   {
/* 515 */     if (inEventLoop()) {
/* 516 */       this.shutdownHooks.remove(task);
/*     */     } else {
/* 518 */       execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 521 */           SingleThreadEventExecutor.this.shutdownHooks.remove(task);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean runShutdownHooks() {
/* 528 */     boolean ran = false;
/*     */     
/* 530 */     while (!this.shutdownHooks.isEmpty()) {
/* 531 */       List<Runnable> copy = new ArrayList(this.shutdownHooks);
/* 532 */       this.shutdownHooks.clear();
/* 533 */       for (Runnable task : copy) {
/*     */         try {
/* 535 */           task.run();
/*     */         } catch (Throwable t) {
/* 537 */           logger.warn("Shutdown hook raised an exception.", t);
/*     */         } finally {
/* 539 */           ran = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 544 */     if (ran) {
/* 545 */       this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     }
/*     */     
/* 548 */     return ran;
/*     */   }
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
/*     */   {
/* 553 */     if (quietPeriod < 0L) {
/* 554 */       throw new IllegalArgumentException("quietPeriod: " + quietPeriod + " (expected >= 0)");
/*     */     }
/* 556 */     if (timeout < quietPeriod) {
/* 557 */       throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
/*     */     }
/*     */     
/* 560 */     if (unit == null) {
/* 561 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 564 */     if (isShuttingDown()) {
/* 565 */       return terminationFuture();
/*     */     }
/*     */     
/* 568 */     boolean inEventLoop = inEventLoop();
/*     */     boolean wakeup;
/*     */     int oldState;
/*     */     for (;;) {
/* 572 */       if (isShuttingDown()) {
/* 573 */         return terminationFuture();
/*     */       }
/*     */       
/* 576 */       wakeup = true;
/* 577 */       oldState = this.state;
/* 578 */       int newState; int newState; if (inEventLoop) {
/* 579 */         newState = 3;
/*     */       } else { int newState;
/* 581 */         switch (oldState) {
/*     */         case 1: 
/*     */         case 2: 
/* 584 */           newState = 3;
/* 585 */           break;
/*     */         default: 
/* 587 */           newState = oldState;
/* 588 */           wakeup = false;
/*     */         }
/*     */       }
/* 591 */       if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
/*     */         break;
/*     */       }
/*     */     }
/* 595 */     this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
/* 596 */     this.gracefulShutdownTimeout = unit.toNanos(timeout);
/*     */     
/* 598 */     if (oldState == 1) {
/*     */       try {
/* 600 */         doStartThread();
/*     */       } catch (Throwable cause) {
/* 602 */         STATE_UPDATER.set(this, 5);
/* 603 */         this.terminationFuture.tryFailure(cause);
/*     */         
/* 605 */         if (!(cause instanceof Exception))
/*     */         {
/* 607 */           PlatformDependent.throwException(cause);
/*     */         }
/* 609 */         return this.terminationFuture;
/*     */       }
/*     */     }
/*     */     
/* 613 */     if (wakeup) {
/* 614 */       wakeup(inEventLoop);
/*     */     }
/*     */     
/* 617 */     return terminationFuture();
/*     */   }
/*     */   
/*     */   public Future<?> terminationFuture()
/*     */   {
/* 622 */     return this.terminationFuture;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown()
/*     */   {
/* 628 */     if (isShutdown()) {
/* 629 */       return;
/*     */     }
/*     */     
/* 632 */     boolean inEventLoop = inEventLoop();
/*     */     boolean wakeup;
/*     */     int oldState;
/*     */     for (;;) {
/* 636 */       if (isShuttingDown()) {
/* 637 */         return;
/*     */       }
/*     */       
/* 640 */       wakeup = true;
/* 641 */       oldState = this.state;
/* 642 */       int newState; int newState; if (inEventLoop) {
/* 643 */         newState = 4;
/*     */       } else { int newState;
/* 645 */         switch (oldState) {
/*     */         case 1: 
/*     */         case 2: 
/*     */         case 3: 
/* 649 */           newState = 4;
/* 650 */           break;
/*     */         default: 
/* 652 */           newState = oldState;
/* 653 */           wakeup = false;
/*     */         }
/*     */       }
/* 656 */       if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 661 */     if (oldState == 1) {
/*     */       try {
/* 663 */         doStartThread();
/*     */       } catch (Throwable cause) {
/* 665 */         STATE_UPDATER.set(this, 5);
/* 666 */         this.terminationFuture.tryFailure(cause);
/*     */         
/* 668 */         if (!(cause instanceof Exception))
/*     */         {
/* 670 */           PlatformDependent.throwException(cause);
/*     */         }
/* 672 */         return;
/*     */       }
/*     */     }
/*     */     
/* 676 */     if (wakeup) {
/* 677 */       wakeup(inEventLoop);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown()
/*     */   {
/* 683 */     return this.state >= 3;
/*     */   }
/*     */   
/*     */   public boolean isShutdown()
/*     */   {
/* 688 */     return this.state >= 4;
/*     */   }
/*     */   
/*     */   public boolean isTerminated()
/*     */   {
/* 693 */     return this.state == 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean confirmShutdown()
/*     */   {
/* 700 */     if (!isShuttingDown()) {
/* 701 */       return false;
/*     */     }
/*     */     
/* 704 */     if (!inEventLoop()) {
/* 705 */       throw new IllegalStateException("must be invoked from an event loop");
/*     */     }
/*     */     
/* 708 */     cancelScheduledTasks();
/*     */     
/* 710 */     if (this.gracefulShutdownStartTime == 0L) {
/* 711 */       this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
/*     */     }
/*     */     
/* 714 */     if ((runAllTasks()) || (runShutdownHooks())) {
/* 715 */       if (isShutdown())
/*     */       {
/* 717 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 723 */       if (this.gracefulShutdownQuietPeriod == 0L) {
/* 724 */         return true;
/*     */       }
/* 726 */       wakeup(true);
/* 727 */       return false;
/*     */     }
/*     */     
/* 730 */     long nanoTime = ScheduledFutureTask.nanoTime();
/*     */     
/* 732 */     if ((isShutdown()) || (nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout)) {
/* 733 */       return true;
/*     */     }
/*     */     
/* 736 */     if (nanoTime - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod)
/*     */     {
/*     */ 
/* 739 */       wakeup(true);
/*     */       try {
/* 741 */         Thread.sleep(100L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */       
/*     */ 
/* 746 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 751 */     return true;
/*     */   }
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 756 */     if (unit == null) {
/* 757 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 760 */     if (inEventLoop()) {
/* 761 */       throw new IllegalStateException("cannot await termination of the current thread");
/*     */     }
/*     */     
/* 764 */     if (this.threadLock.tryAcquire(timeout, unit)) {
/* 765 */       this.threadLock.release();
/*     */     }
/*     */     
/* 768 */     return isTerminated();
/*     */   }
/*     */   
/*     */   public void execute(Runnable task)
/*     */   {
/* 773 */     if (task == null) {
/* 774 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 777 */     boolean inEventLoop = inEventLoop();
/* 778 */     addTask(task);
/* 779 */     if (!inEventLoop) {
/* 780 */       startThread();
/* 781 */       if ((isShutdown()) && (removeTask(task))) {
/* 782 */         reject();
/*     */       }
/*     */     }
/*     */     
/* 786 */     if ((!this.addTaskWakesUp) && (wakesUpForTask(task))) {
/* 787 */       wakeup(inEventLoop);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
/*     */   {
/* 793 */     throwIfInEventLoop("invokeAny");
/* 794 */     return (T)super.invokeAny(tasks);
/*     */   }
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 800 */     throwIfInEventLoop("invokeAny");
/* 801 */     return (T)super.invokeAny(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
/*     */     throws InterruptedException
/*     */   {
/* 807 */     throwIfInEventLoop("invokeAll");
/* 808 */     return super.invokeAll(tasks);
/*     */   }
/*     */   
/*     */   public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
/*     */     throws InterruptedException
/*     */   {
/* 814 */     throwIfInEventLoop("invokeAll");
/* 815 */     return super.invokeAll(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   private void throwIfInEventLoop(String method) {
/* 819 */     if (inEventLoop()) {
/* 820 */       throw new RejectedExecutionException("Calling " + method + " from within the EventLoop is not allowed");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ThreadProperties threadProperties()
/*     */   {
/* 830 */     ThreadProperties threadProperties = this.threadProperties;
/* 831 */     if (threadProperties == null) {
/* 832 */       Thread thread = this.thread;
/* 833 */       if (thread == null) {
/* 834 */         assert (!inEventLoop());
/* 835 */         submit(NOOP_TASK).syncUninterruptibly();
/* 836 */         thread = this.thread;
/* 837 */         assert (thread != null);
/*     */       }
/*     */       
/* 840 */       threadProperties = new DefaultThreadProperties(thread);
/* 841 */       if (!PROPERTIES_UPDATER.compareAndSet(this, null, threadProperties)) {
/* 842 */         threadProperties = this.threadProperties;
/*     */       }
/*     */     }
/*     */     
/* 846 */     return threadProperties;
/*     */   }
/*     */   
/*     */   protected boolean wakesUpForTask(Runnable task)
/*     */   {
/* 851 */     return true;
/*     */   }
/*     */   
/*     */   protected static void reject() {
/* 855 */     throw new RejectedExecutionException("event executor terminated");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void reject(Runnable task)
/*     */   {
/* 864 */     this.rejectedExecutionHandler.rejected(task, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 869 */   private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
/*     */   
/*     */   private void startThread() {
/* 872 */     if ((this.state == 1) && 
/* 873 */       (STATE_UPDATER.compareAndSet(this, 1, 2))) {
/*     */       try {
/* 875 */         doStartThread();
/*     */       } catch (Throwable cause) {
/* 877 */         STATE_UPDATER.set(this, 1);
/* 878 */         PlatformDependent.throwException(cause);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void doStartThread()
/*     */   {
/* 885 */     assert (this.thread == null);
/* 886 */     this.executor.execute(new Runnable()
/*     */     {
/*     */       /* Error */
/*     */       public void run()
/*     */       {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   4: invokestatic 31	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */         //   7: invokestatic 35	io/netty/util/concurrent/SingleThreadEventExecutor:access$102	(Lio/netty/util/concurrent/SingleThreadEventExecutor;Ljava/lang/Thread;)Ljava/lang/Thread;
/*     */         //   10: pop
/*     */         //   11: aload_0
/*     */         //   12: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   15: invokestatic 39	io/netty/util/concurrent/SingleThreadEventExecutor:access$200	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Z
/*     */         //   18: ifeq +13 -> 31
/*     */         //   21: aload_0
/*     */         //   22: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   25: invokestatic 43	io/netty/util/concurrent/SingleThreadEventExecutor:access$100	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/lang/Thread;
/*     */         //   28: invokevirtual 46	java/lang/Thread:interrupt	()V
/*     */         //   31: iconst_0
/*     */         //   32: istore_1
/*     */         //   33: aload_0
/*     */         //   34: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   37: invokevirtual 49	io/netty/util/concurrent/SingleThreadEventExecutor:updateLastExecutionTime	()V
/*     */         //   40: aload_0
/*     */         //   41: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   44: invokevirtual 51	io/netty/util/concurrent/SingleThreadEventExecutor:run	()V
/*     */         //   47: iconst_1
/*     */         //   48: istore_1
/*     */         //   49: aload_0
/*     */         //   50: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   53: invokestatic 55	io/netty/util/concurrent/SingleThreadEventExecutor:access$400	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)I
/*     */         //   56: istore_2
/*     */         //   57: iload_2
/*     */         //   58: iconst_3
/*     */         //   59: if_icmpge +24 -> 83
/*     */         //   62: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   65: aload_0
/*     */         //   66: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   69: iload_2
/*     */         //   70: iconst_3
/*     */         //   71: invokevirtual 65	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:compareAndSet	(Ljava/lang/Object;II)Z
/*     */         //   74: ifeq +6 -> 80
/*     */         //   77: goto +6 -> 83
/*     */         //   80: goto -31 -> 49
/*     */         //   83: iload_1
/*     */         //   84: ifeq +75 -> 159
/*     */         //   87: aload_0
/*     */         //   88: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   91: invokestatic 69	io/netty/util/concurrent/SingleThreadEventExecutor:access$600	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)J
/*     */         //   94: lconst_0
/*     */         //   95: lcmp
/*     */         //   96: ifne +63 -> 159
/*     */         //   99: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   102: invokeinterface 79 1 0
/*     */         //   107: ifeq +52 -> 159
/*     */         //   110: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   113: new 81	java/lang/StringBuilder
/*     */         //   116: dup
/*     */         //   117: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   120: ldc 84
/*     */         //   122: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   125: ldc 90
/*     */         //   127: invokevirtual 96	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */         //   130: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   133: ldc 98
/*     */         //   135: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   138: ldc 9
/*     */         //   140: invokevirtual 96	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */         //   143: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   146: ldc 100
/*     */         //   148: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   151: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   154: invokeinterface 107 2 0
/*     */         //   159: aload_0
/*     */         //   160: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   163: invokevirtual 110	io/netty/util/concurrent/SingleThreadEventExecutor:confirmShutdown	()Z
/*     */         //   166: ifeq -7 -> 159
/*     */         //   169: goto +3 -> 172
/*     */         //   172: aload_0
/*     */         //   173: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   176: invokevirtual 113	io/netty/util/concurrent/SingleThreadEventExecutor:cleanup	()V
/*     */         //   179: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   182: aload_0
/*     */         //   183: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   186: iconst_5
/*     */         //   187: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   190: aload_0
/*     */         //   191: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   194: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   197: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   200: aload_0
/*     */         //   201: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   204: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   207: invokeinterface 135 1 0
/*     */         //   212: ifne +57 -> 269
/*     */         //   215: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   218: invokeinterface 138 1 0
/*     */         //   223: ifeq +46 -> 269
/*     */         //   226: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   229: new 81	java/lang/StringBuilder
/*     */         //   232: dup
/*     */         //   233: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   236: ldc -116
/*     */         //   238: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   241: aload_0
/*     */         //   242: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   245: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   248: invokeinterface 144 1 0
/*     */         //   253: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   256: bipush 41
/*     */         //   258: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   261: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   264: invokeinterface 153 2 0
/*     */         //   269: aload_0
/*     */         //   270: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   273: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   276: aconst_null
/*     */         //   277: invokeinterface 163 2 0
/*     */         //   282: pop
/*     */         //   283: goto +110 -> 393
/*     */         //   286: astore_3
/*     */         //   287: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   290: aload_0
/*     */         //   291: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   294: iconst_5
/*     */         //   295: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   298: aload_0
/*     */         //   299: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   302: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   305: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   308: aload_0
/*     */         //   309: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   312: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   315: invokeinterface 135 1 0
/*     */         //   320: ifne +57 -> 377
/*     */         //   323: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   326: invokeinterface 138 1 0
/*     */         //   331: ifeq +46 -> 377
/*     */         //   334: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   337: new 81	java/lang/StringBuilder
/*     */         //   340: dup
/*     */         //   341: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   344: ldc -116
/*     */         //   346: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   349: aload_0
/*     */         //   350: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   353: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   356: invokeinterface 144 1 0
/*     */         //   361: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   364: bipush 41
/*     */         //   366: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   369: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   372: invokeinterface 153 2 0
/*     */         //   377: aload_0
/*     */         //   378: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   381: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   384: aconst_null
/*     */         //   385: invokeinterface 163 2 0
/*     */         //   390: pop
/*     */         //   391: aload_3
/*     */         //   392: athrow
/*     */         //   393: goto +231 -> 624
/*     */         //   396: astore 4
/*     */         //   398: aload_0
/*     */         //   399: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   402: invokevirtual 113	io/netty/util/concurrent/SingleThreadEventExecutor:cleanup	()V
/*     */         //   405: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   408: aload_0
/*     */         //   409: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   412: iconst_5
/*     */         //   413: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   416: aload_0
/*     */         //   417: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   420: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   423: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   426: aload_0
/*     */         //   427: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   430: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   433: invokeinterface 135 1 0
/*     */         //   438: ifne +57 -> 495
/*     */         //   441: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   444: invokeinterface 138 1 0
/*     */         //   449: ifeq +46 -> 495
/*     */         //   452: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   455: new 81	java/lang/StringBuilder
/*     */         //   458: dup
/*     */         //   459: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   462: ldc -116
/*     */         //   464: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   467: aload_0
/*     */         //   468: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   471: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   474: invokeinterface 144 1 0
/*     */         //   479: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   482: bipush 41
/*     */         //   484: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   487: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   490: invokeinterface 153 2 0
/*     */         //   495: aload_0
/*     */         //   496: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   499: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   502: aconst_null
/*     */         //   503: invokeinterface 163 2 0
/*     */         //   508: pop
/*     */         //   509: goto +112 -> 621
/*     */         //   512: astore 5
/*     */         //   514: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   517: aload_0
/*     */         //   518: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   521: iconst_5
/*     */         //   522: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   525: aload_0
/*     */         //   526: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   529: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   532: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   535: aload_0
/*     */         //   536: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   539: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   542: invokeinterface 135 1 0
/*     */         //   547: ifne +57 -> 604
/*     */         //   550: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   553: invokeinterface 138 1 0
/*     */         //   558: ifeq +46 -> 604
/*     */         //   561: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   564: new 81	java/lang/StringBuilder
/*     */         //   567: dup
/*     */         //   568: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   571: ldc -116
/*     */         //   573: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   576: aload_0
/*     */         //   577: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   580: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   583: invokeinterface 144 1 0
/*     */         //   588: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   591: bipush 41
/*     */         //   593: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   596: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   599: invokeinterface 153 2 0
/*     */         //   604: aload_0
/*     */         //   605: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   608: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   611: aconst_null
/*     */         //   612: invokeinterface 163 2 0
/*     */         //   617: pop
/*     */         //   618: aload 5
/*     */         //   620: athrow
/*     */         //   621: aload 4
/*     */         //   623: athrow
/*     */         //   624: goto +1180 -> 1804
/*     */         //   627: astore_2
/*     */         //   628: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   631: ldc -91
/*     */         //   633: aload_2
/*     */         //   634: invokeinterface 168 3 0
/*     */         //   639: aload_0
/*     */         //   640: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   643: invokestatic 55	io/netty/util/concurrent/SingleThreadEventExecutor:access$400	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)I
/*     */         //   646: istore_2
/*     */         //   647: iload_2
/*     */         //   648: iconst_3
/*     */         //   649: if_icmpge +24 -> 673
/*     */         //   652: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   655: aload_0
/*     */         //   656: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   659: iload_2
/*     */         //   660: iconst_3
/*     */         //   661: invokevirtual 65	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:compareAndSet	(Ljava/lang/Object;II)Z
/*     */         //   664: ifeq +6 -> 670
/*     */         //   667: goto +6 -> 673
/*     */         //   670: goto -31 -> 639
/*     */         //   673: iload_1
/*     */         //   674: ifeq +75 -> 749
/*     */         //   677: aload_0
/*     */         //   678: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   681: invokestatic 69	io/netty/util/concurrent/SingleThreadEventExecutor:access$600	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)J
/*     */         //   684: lconst_0
/*     */         //   685: lcmp
/*     */         //   686: ifne +63 -> 749
/*     */         //   689: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   692: invokeinterface 79 1 0
/*     */         //   697: ifeq +52 -> 749
/*     */         //   700: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   703: new 81	java/lang/StringBuilder
/*     */         //   706: dup
/*     */         //   707: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   710: ldc 84
/*     */         //   712: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   715: ldc 90
/*     */         //   717: invokevirtual 96	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */         //   720: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   723: ldc 98
/*     */         //   725: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   728: ldc 9
/*     */         //   730: invokevirtual 96	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */         //   733: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   736: ldc 100
/*     */         //   738: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   741: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   744: invokeinterface 107 2 0
/*     */         //   749: aload_0
/*     */         //   750: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   753: invokevirtual 110	io/netty/util/concurrent/SingleThreadEventExecutor:confirmShutdown	()Z
/*     */         //   756: ifeq -7 -> 749
/*     */         //   759: goto +3 -> 762
/*     */         //   762: aload_0
/*     */         //   763: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   766: invokevirtual 113	io/netty/util/concurrent/SingleThreadEventExecutor:cleanup	()V
/*     */         //   769: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   772: aload_0
/*     */         //   773: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   776: iconst_5
/*     */         //   777: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   780: aload_0
/*     */         //   781: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   784: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   787: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   790: aload_0
/*     */         //   791: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   794: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   797: invokeinterface 135 1 0
/*     */         //   802: ifne +57 -> 859
/*     */         //   805: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   808: invokeinterface 138 1 0
/*     */         //   813: ifeq +46 -> 859
/*     */         //   816: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   819: new 81	java/lang/StringBuilder
/*     */         //   822: dup
/*     */         //   823: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   826: ldc -116
/*     */         //   828: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   831: aload_0
/*     */         //   832: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   835: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   838: invokeinterface 144 1 0
/*     */         //   843: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   846: bipush 41
/*     */         //   848: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   851: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   854: invokeinterface 153 2 0
/*     */         //   859: aload_0
/*     */         //   860: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   863: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   866: aconst_null
/*     */         //   867: invokeinterface 163 2 0
/*     */         //   872: pop
/*     */         //   873: goto +112 -> 985
/*     */         //   876: astore 6
/*     */         //   878: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   881: aload_0
/*     */         //   882: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   885: iconst_5
/*     */         //   886: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   889: aload_0
/*     */         //   890: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   893: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   896: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   899: aload_0
/*     */         //   900: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   903: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   906: invokeinterface 135 1 0
/*     */         //   911: ifne +57 -> 968
/*     */         //   914: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   917: invokeinterface 138 1 0
/*     */         //   922: ifeq +46 -> 968
/*     */         //   925: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   928: new 81	java/lang/StringBuilder
/*     */         //   931: dup
/*     */         //   932: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   935: ldc -116
/*     */         //   937: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   940: aload_0
/*     */         //   941: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   944: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   947: invokeinterface 144 1 0
/*     */         //   952: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   955: bipush 41
/*     */         //   957: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   960: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   963: invokeinterface 153 2 0
/*     */         //   968: aload_0
/*     */         //   969: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   972: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   975: aconst_null
/*     */         //   976: invokeinterface 163 2 0
/*     */         //   981: pop
/*     */         //   982: aload 6
/*     */         //   984: athrow
/*     */         //   985: goto +231 -> 1216
/*     */         //   988: astore 7
/*     */         //   990: aload_0
/*     */         //   991: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   994: invokevirtual 113	io/netty/util/concurrent/SingleThreadEventExecutor:cleanup	()V
/*     */         //   997: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1000: aload_0
/*     */         //   1001: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1004: iconst_5
/*     */         //   1005: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   1008: aload_0
/*     */         //   1009: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1012: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   1015: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   1018: aload_0
/*     */         //   1019: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1022: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1025: invokeinterface 135 1 0
/*     */         //   1030: ifne +57 -> 1087
/*     */         //   1033: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1036: invokeinterface 138 1 0
/*     */         //   1041: ifeq +46 -> 1087
/*     */         //   1044: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1047: new 81	java/lang/StringBuilder
/*     */         //   1050: dup
/*     */         //   1051: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1054: ldc -116
/*     */         //   1056: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1059: aload_0
/*     */         //   1060: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1063: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1066: invokeinterface 144 1 0
/*     */         //   1071: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   1074: bipush 41
/*     */         //   1076: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   1079: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1082: invokeinterface 153 2 0
/*     */         //   1087: aload_0
/*     */         //   1088: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1091: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   1094: aconst_null
/*     */         //   1095: invokeinterface 163 2 0
/*     */         //   1100: pop
/*     */         //   1101: goto +112 -> 1213
/*     */         //   1104: astore 8
/*     */         //   1106: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1109: aload_0
/*     */         //   1110: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1113: iconst_5
/*     */         //   1114: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   1117: aload_0
/*     */         //   1118: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1121: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   1124: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   1127: aload_0
/*     */         //   1128: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1131: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1134: invokeinterface 135 1 0
/*     */         //   1139: ifne +57 -> 1196
/*     */         //   1142: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1145: invokeinterface 138 1 0
/*     */         //   1150: ifeq +46 -> 1196
/*     */         //   1153: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1156: new 81	java/lang/StringBuilder
/*     */         //   1159: dup
/*     */         //   1160: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1163: ldc -116
/*     */         //   1165: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1168: aload_0
/*     */         //   1169: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1172: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1175: invokeinterface 144 1 0
/*     */         //   1180: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   1183: bipush 41
/*     */         //   1185: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   1188: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1191: invokeinterface 153 2 0
/*     */         //   1196: aload_0
/*     */         //   1197: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1200: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   1203: aconst_null
/*     */         //   1204: invokeinterface 163 2 0
/*     */         //   1209: pop
/*     */         //   1210: aload 8
/*     */         //   1212: athrow
/*     */         //   1213: aload 7
/*     */         //   1215: athrow
/*     */         //   1216: goto +588 -> 1804
/*     */         //   1219: astore 9
/*     */         //   1221: aload_0
/*     */         //   1222: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1225: invokestatic 55	io/netty/util/concurrent/SingleThreadEventExecutor:access$400	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)I
/*     */         //   1228: istore 10
/*     */         //   1230: iload 10
/*     */         //   1232: iconst_3
/*     */         //   1233: if_icmpge +25 -> 1258
/*     */         //   1236: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1239: aload_0
/*     */         //   1240: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1243: iload 10
/*     */         //   1245: iconst_3
/*     */         //   1246: invokevirtual 65	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:compareAndSet	(Ljava/lang/Object;II)Z
/*     */         //   1249: ifeq +6 -> 1255
/*     */         //   1252: goto +6 -> 1258
/*     */         //   1255: goto -34 -> 1221
/*     */         //   1258: iload_1
/*     */         //   1259: ifeq +75 -> 1334
/*     */         //   1262: aload_0
/*     */         //   1263: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1266: invokestatic 69	io/netty/util/concurrent/SingleThreadEventExecutor:access$600	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)J
/*     */         //   1269: lconst_0
/*     */         //   1270: lcmp
/*     */         //   1271: ifne +63 -> 1334
/*     */         //   1274: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1277: invokeinterface 79 1 0
/*     */         //   1282: ifeq +52 -> 1334
/*     */         //   1285: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1288: new 81	java/lang/StringBuilder
/*     */         //   1291: dup
/*     */         //   1292: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1295: ldc 84
/*     */         //   1297: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1300: ldc 90
/*     */         //   1302: invokevirtual 96	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */         //   1305: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1308: ldc 98
/*     */         //   1310: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1313: ldc 9
/*     */         //   1315: invokevirtual 96	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */         //   1318: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1321: ldc 100
/*     */         //   1323: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1326: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1329: invokeinterface 107 2 0
/*     */         //   1334: aload_0
/*     */         //   1335: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1338: invokevirtual 110	io/netty/util/concurrent/SingleThreadEventExecutor:confirmShutdown	()Z
/*     */         //   1341: ifeq -7 -> 1334
/*     */         //   1344: goto +3 -> 1347
/*     */         //   1347: aload_0
/*     */         //   1348: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1351: invokevirtual 113	io/netty/util/concurrent/SingleThreadEventExecutor:cleanup	()V
/*     */         //   1354: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1357: aload_0
/*     */         //   1358: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1361: iconst_5
/*     */         //   1362: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   1365: aload_0
/*     */         //   1366: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1369: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   1372: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   1375: aload_0
/*     */         //   1376: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1379: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1382: invokeinterface 135 1 0
/*     */         //   1387: ifne +57 -> 1444
/*     */         //   1390: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1393: invokeinterface 138 1 0
/*     */         //   1398: ifeq +46 -> 1444
/*     */         //   1401: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1404: new 81	java/lang/StringBuilder
/*     */         //   1407: dup
/*     */         //   1408: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1411: ldc -116
/*     */         //   1413: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1416: aload_0
/*     */         //   1417: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1420: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1423: invokeinterface 144 1 0
/*     */         //   1428: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   1431: bipush 41
/*     */         //   1433: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   1436: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1439: invokeinterface 153 2 0
/*     */         //   1444: aload_0
/*     */         //   1445: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1448: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   1451: aconst_null
/*     */         //   1452: invokeinterface 163 2 0
/*     */         //   1457: pop
/*     */         //   1458: goto +112 -> 1570
/*     */         //   1461: astore 11
/*     */         //   1463: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1466: aload_0
/*     */         //   1467: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1470: iconst_5
/*     */         //   1471: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   1474: aload_0
/*     */         //   1475: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1478: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   1481: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   1484: aload_0
/*     */         //   1485: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1488: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1491: invokeinterface 135 1 0
/*     */         //   1496: ifne +57 -> 1553
/*     */         //   1499: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1502: invokeinterface 138 1 0
/*     */         //   1507: ifeq +46 -> 1553
/*     */         //   1510: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1513: new 81	java/lang/StringBuilder
/*     */         //   1516: dup
/*     */         //   1517: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1520: ldc -116
/*     */         //   1522: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1525: aload_0
/*     */         //   1526: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1529: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1532: invokeinterface 144 1 0
/*     */         //   1537: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   1540: bipush 41
/*     */         //   1542: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   1545: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1548: invokeinterface 153 2 0
/*     */         //   1553: aload_0
/*     */         //   1554: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1557: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   1560: aconst_null
/*     */         //   1561: invokeinterface 163 2 0
/*     */         //   1566: pop
/*     */         //   1567: aload 11
/*     */         //   1569: athrow
/*     */         //   1570: goto +231 -> 1801
/*     */         //   1573: astore 12
/*     */         //   1575: aload_0
/*     */         //   1576: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1579: invokevirtual 113	io/netty/util/concurrent/SingleThreadEventExecutor:cleanup	()V
/*     */         //   1582: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1585: aload_0
/*     */         //   1586: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1589: iconst_5
/*     */         //   1590: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   1593: aload_0
/*     */         //   1594: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1597: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   1600: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   1603: aload_0
/*     */         //   1604: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1607: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1610: invokeinterface 135 1 0
/*     */         //   1615: ifne +57 -> 1672
/*     */         //   1618: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1621: invokeinterface 138 1 0
/*     */         //   1626: ifeq +46 -> 1672
/*     */         //   1629: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1632: new 81	java/lang/StringBuilder
/*     */         //   1635: dup
/*     */         //   1636: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1639: ldc -116
/*     */         //   1641: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1644: aload_0
/*     */         //   1645: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1648: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1651: invokeinterface 144 1 0
/*     */         //   1656: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   1659: bipush 41
/*     */         //   1661: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   1664: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1667: invokeinterface 153 2 0
/*     */         //   1672: aload_0
/*     */         //   1673: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1676: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   1679: aconst_null
/*     */         //   1680: invokeinterface 163 2 0
/*     */         //   1685: pop
/*     */         //   1686: goto +112 -> 1798
/*     */         //   1689: astore 13
/*     */         //   1691: invokestatic 59	io/netty/util/concurrent/SingleThreadEventExecutor:access$500	()Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
/*     */         //   1694: aload_0
/*     */         //   1695: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1698: iconst_5
/*     */         //   1699: invokevirtual 117	java/util/concurrent/atomic/AtomicIntegerFieldUpdater:set	(Ljava/lang/Object;I)V
/*     */         //   1702: aload_0
/*     */         //   1703: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1706: invokestatic 121	io/netty/util/concurrent/SingleThreadEventExecutor:access$700	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/concurrent/Semaphore;
/*     */         //   1709: invokevirtual 126	java/util/concurrent/Semaphore:release	()V
/*     */         //   1712: aload_0
/*     */         //   1713: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1716: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1719: invokeinterface 135 1 0
/*     */         //   1724: ifne +57 -> 1781
/*     */         //   1727: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1730: invokeinterface 138 1 0
/*     */         //   1735: ifeq +46 -> 1781
/*     */         //   1738: invokestatic 73	io/netty/util/concurrent/SingleThreadEventExecutor:access$300	()Lio/netty/util/internal/logging/InternalLogger;
/*     */         //   1741: new 81	java/lang/StringBuilder
/*     */         //   1744: dup
/*     */         //   1745: invokespecial 82	java/lang/StringBuilder:<init>	()V
/*     */         //   1748: ldc -116
/*     */         //   1750: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   1753: aload_0
/*     */         //   1754: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1757: invokestatic 130	io/netty/util/concurrent/SingleThreadEventExecutor:access$800	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Ljava/util/Queue;
/*     */         //   1760: invokeinterface 144 1 0
/*     */         //   1765: invokevirtual 147	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */         //   1768: bipush 41
/*     */         //   1770: invokevirtual 150	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */         //   1773: invokevirtual 103	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   1776: invokeinterface 153 2 0
/*     */         //   1781: aload_0
/*     */         //   1782: getfield 18	io/netty/util/concurrent/SingleThreadEventExecutor$5:this$0	Lio/netty/util/concurrent/SingleThreadEventExecutor;
/*     */         //   1785: invokestatic 157	io/netty/util/concurrent/SingleThreadEventExecutor:access$900	(Lio/netty/util/concurrent/SingleThreadEventExecutor;)Lio/netty/util/concurrent/Promise;
/*     */         //   1788: aconst_null
/*     */         //   1789: invokeinterface 163 2 0
/*     */         //   1794: pop
/*     */         //   1795: aload 13
/*     */         //   1797: athrow
/*     */         //   1798: aload 12
/*     */         //   1800: athrow
/*     */         //   1801: aload 9
/*     */         //   1803: athrow
/*     */         //   1804: return
/*     */         // Line number table:
/*     */         //   Java source line #889	-> byte code offset #0
/*     */         //   Java source line #890	-> byte code offset #11
/*     */         //   Java source line #891	-> byte code offset #21
/*     */         //   Java source line #894	-> byte code offset #31
/*     */         //   Java source line #895	-> byte code offset #33
/*     */         //   Java source line #897	-> byte code offset #40
/*     */         //   Java source line #898	-> byte code offset #47
/*     */         //   Java source line #903	-> byte code offset #49
/*     */         //   Java source line #904	-> byte code offset #57
/*     */         //   Java source line #906	-> byte code offset #77
/*     */         //   Java source line #908	-> byte code offset #80
/*     */         //   Java source line #911	-> byte code offset #83
/*     */         //   Java source line #912	-> byte code offset #99
/*     */         //   Java source line #913	-> byte code offset #110
/*     */         //   Java source line #914	-> byte code offset #140
/*     */         //   Java source line #913	-> byte code offset #154
/*     */         //   Java source line #922	-> byte code offset #159
/*     */         //   Java source line #923	-> byte code offset #169
/*     */         //   Java source line #928	-> byte code offset #172
/*     */         //   Java source line #930	-> byte code offset #179
/*     */         //   Java source line #931	-> byte code offset #190
/*     */         //   Java source line #932	-> byte code offset #200
/*     */         //   Java source line #933	-> byte code offset #215
/*     */         //   Java source line #934	-> byte code offset #226
/*     */         //   Java source line #935	-> byte code offset #245
/*     */         //   Java source line #934	-> byte code offset #264
/*     */         //   Java source line #939	-> byte code offset #269
/*     */         //   Java source line #940	-> byte code offset #283
/*     */         //   Java source line #930	-> byte code offset #286
/*     */         //   Java source line #931	-> byte code offset #298
/*     */         //   Java source line #932	-> byte code offset #308
/*     */         //   Java source line #933	-> byte code offset #323
/*     */         //   Java source line #934	-> byte code offset #334
/*     */         //   Java source line #935	-> byte code offset #353
/*     */         //   Java source line #934	-> byte code offset #372
/*     */         //   Java source line #939	-> byte code offset #377
/*     */         //   Java source line #940	-> byte code offset #391
/*     */         //   Java source line #941	-> byte code offset #393
/*     */         //   Java source line #927	-> byte code offset #396
/*     */         //   Java source line #928	-> byte code offset #398
/*     */         //   Java source line #930	-> byte code offset #405
/*     */         //   Java source line #931	-> byte code offset #416
/*     */         //   Java source line #932	-> byte code offset #426
/*     */         //   Java source line #933	-> byte code offset #441
/*     */         //   Java source line #934	-> byte code offset #452
/*     */         //   Java source line #935	-> byte code offset #471
/*     */         //   Java source line #934	-> byte code offset #490
/*     */         //   Java source line #939	-> byte code offset #495
/*     */         //   Java source line #940	-> byte code offset #509
/*     */         //   Java source line #930	-> byte code offset #512
/*     */         //   Java source line #931	-> byte code offset #525
/*     */         //   Java source line #932	-> byte code offset #535
/*     */         //   Java source line #933	-> byte code offset #550
/*     */         //   Java source line #934	-> byte code offset #561
/*     */         //   Java source line #935	-> byte code offset #580
/*     */         //   Java source line #934	-> byte code offset #599
/*     */         //   Java source line #939	-> byte code offset #604
/*     */         //   Java source line #940	-> byte code offset #618
/*     */         //   Java source line #941	-> byte code offset #621
/*     */         //   Java source line #942	-> byte code offset #624
/*     */         //   Java source line #899	-> byte code offset #627
/*     */         //   Java source line #900	-> byte code offset #628
/*     */         //   Java source line #903	-> byte code offset #639
/*     */         //   Java source line #904	-> byte code offset #647
/*     */         //   Java source line #906	-> byte code offset #667
/*     */         //   Java source line #908	-> byte code offset #670
/*     */         //   Java source line #911	-> byte code offset #673
/*     */         //   Java source line #912	-> byte code offset #689
/*     */         //   Java source line #913	-> byte code offset #700
/*     */         //   Java source line #914	-> byte code offset #730
/*     */         //   Java source line #913	-> byte code offset #744
/*     */         //   Java source line #922	-> byte code offset #749
/*     */         //   Java source line #923	-> byte code offset #759
/*     */         //   Java source line #928	-> byte code offset #762
/*     */         //   Java source line #930	-> byte code offset #769
/*     */         //   Java source line #931	-> byte code offset #780
/*     */         //   Java source line #932	-> byte code offset #790
/*     */         //   Java source line #933	-> byte code offset #805
/*     */         //   Java source line #934	-> byte code offset #816
/*     */         //   Java source line #935	-> byte code offset #835
/*     */         //   Java source line #934	-> byte code offset #854
/*     */         //   Java source line #939	-> byte code offset #859
/*     */         //   Java source line #940	-> byte code offset #873
/*     */         //   Java source line #930	-> byte code offset #876
/*     */         //   Java source line #931	-> byte code offset #889
/*     */         //   Java source line #932	-> byte code offset #899
/*     */         //   Java source line #933	-> byte code offset #914
/*     */         //   Java source line #934	-> byte code offset #925
/*     */         //   Java source line #935	-> byte code offset #944
/*     */         //   Java source line #934	-> byte code offset #963
/*     */         //   Java source line #939	-> byte code offset #968
/*     */         //   Java source line #940	-> byte code offset #982
/*     */         //   Java source line #941	-> byte code offset #985
/*     */         //   Java source line #927	-> byte code offset #988
/*     */         //   Java source line #928	-> byte code offset #990
/*     */         //   Java source line #930	-> byte code offset #997
/*     */         //   Java source line #931	-> byte code offset #1008
/*     */         //   Java source line #932	-> byte code offset #1018
/*     */         //   Java source line #933	-> byte code offset #1033
/*     */         //   Java source line #934	-> byte code offset #1044
/*     */         //   Java source line #935	-> byte code offset #1063
/*     */         //   Java source line #934	-> byte code offset #1082
/*     */         //   Java source line #939	-> byte code offset #1087
/*     */         //   Java source line #940	-> byte code offset #1101
/*     */         //   Java source line #930	-> byte code offset #1104
/*     */         //   Java source line #931	-> byte code offset #1117
/*     */         //   Java source line #932	-> byte code offset #1127
/*     */         //   Java source line #933	-> byte code offset #1142
/*     */         //   Java source line #934	-> byte code offset #1153
/*     */         //   Java source line #935	-> byte code offset #1172
/*     */         //   Java source line #934	-> byte code offset #1191
/*     */         //   Java source line #939	-> byte code offset #1196
/*     */         //   Java source line #940	-> byte code offset #1210
/*     */         //   Java source line #941	-> byte code offset #1213
/*     */         //   Java source line #942	-> byte code offset #1216
/*     */         //   Java source line #902	-> byte code offset #1219
/*     */         //   Java source line #903	-> byte code offset #1221
/*     */         //   Java source line #904	-> byte code offset #1230
/*     */         //   Java source line #906	-> byte code offset #1252
/*     */         //   Java source line #908	-> byte code offset #1255
/*     */         //   Java source line #911	-> byte code offset #1258
/*     */         //   Java source line #912	-> byte code offset #1274
/*     */         //   Java source line #913	-> byte code offset #1285
/*     */         //   Java source line #914	-> byte code offset #1315
/*     */         //   Java source line #913	-> byte code offset #1329
/*     */         //   Java source line #922	-> byte code offset #1334
/*     */         //   Java source line #923	-> byte code offset #1344
/*     */         //   Java source line #928	-> byte code offset #1347
/*     */         //   Java source line #930	-> byte code offset #1354
/*     */         //   Java source line #931	-> byte code offset #1365
/*     */         //   Java source line #932	-> byte code offset #1375
/*     */         //   Java source line #933	-> byte code offset #1390
/*     */         //   Java source line #934	-> byte code offset #1401
/*     */         //   Java source line #935	-> byte code offset #1420
/*     */         //   Java source line #934	-> byte code offset #1439
/*     */         //   Java source line #939	-> byte code offset #1444
/*     */         //   Java source line #940	-> byte code offset #1458
/*     */         //   Java source line #930	-> byte code offset #1461
/*     */         //   Java source line #931	-> byte code offset #1474
/*     */         //   Java source line #932	-> byte code offset #1484
/*     */         //   Java source line #933	-> byte code offset #1499
/*     */         //   Java source line #934	-> byte code offset #1510
/*     */         //   Java source line #935	-> byte code offset #1529
/*     */         //   Java source line #934	-> byte code offset #1548
/*     */         //   Java source line #939	-> byte code offset #1553
/*     */         //   Java source line #940	-> byte code offset #1567
/*     */         //   Java source line #941	-> byte code offset #1570
/*     */         //   Java source line #927	-> byte code offset #1573
/*     */         //   Java source line #928	-> byte code offset #1575
/*     */         //   Java source line #930	-> byte code offset #1582
/*     */         //   Java source line #931	-> byte code offset #1593
/*     */         //   Java source line #932	-> byte code offset #1603
/*     */         //   Java source line #933	-> byte code offset #1618
/*     */         //   Java source line #934	-> byte code offset #1629
/*     */         //   Java source line #935	-> byte code offset #1648
/*     */         //   Java source line #934	-> byte code offset #1667
/*     */         //   Java source line #939	-> byte code offset #1672
/*     */         //   Java source line #940	-> byte code offset #1686
/*     */         //   Java source line #930	-> byte code offset #1689
/*     */         //   Java source line #931	-> byte code offset #1702
/*     */         //   Java source line #932	-> byte code offset #1712
/*     */         //   Java source line #933	-> byte code offset #1727
/*     */         //   Java source line #934	-> byte code offset #1738
/*     */         //   Java source line #935	-> byte code offset #1757
/*     */         //   Java source line #934	-> byte code offset #1776
/*     */         //   Java source line #939	-> byte code offset #1781
/*     */         //   Java source line #940	-> byte code offset #1795
/*     */         //   Java source line #941	-> byte code offset #1798
/*     */         //   Java source line #942	-> byte code offset #1801
/*     */         //   Java source line #943	-> byte code offset #1804
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	1805	0	this	5
/*     */         //   32	1227	1	success	boolean
/*     */         //   56	14	2	oldState	int
/*     */         //   627	7	2	t	Throwable
/*     */         //   646	14	2	oldState	int
/*     */         //   286	106	3	localObject1	Object
/*     */         //   396	226	4	localObject2	Object
/*     */         //   512	107	5	localObject3	Object
/*     */         //   876	107	6	localObject4	Object
/*     */         //   988	226	7	localObject5	Object
/*     */         //   1104	107	8	localObject6	Object
/*     */         //   1219	583	9	localObject7	Object
/*     */         //   1228	16	10	oldState	int
/*     */         //   1461	107	11	localObject8	Object
/*     */         //   1573	226	12	localObject9	Object
/*     */         //   1689	107	13	localObject10	Object
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   172	179	286	finally
/*     */         //   159	172	396	finally
/*     */         //   396	398	396	finally
/*     */         //   398	405	512	finally
/*     */         //   512	514	512	finally
/*     */         //   40	49	627	java/lang/Throwable
/*     */         //   762	769	876	finally
/*     */         //   876	878	876	finally
/*     */         //   749	762	988	finally
/*     */         //   988	990	988	finally
/*     */         //   990	997	1104	finally
/*     */         //   1104	1106	1104	finally
/*     */         //   40	49	1219	finally
/*     */         //   627	639	1219	finally
/*     */         //   1219	1221	1219	finally
/*     */         //   1347	1354	1461	finally
/*     */         //   1461	1463	1461	finally
/*     */         //   1334	1347	1573	finally
/*     */         //   1573	1575	1573	finally
/*     */         //   1575	1582	1689	finally
/*     */         //   1689	1691	1689	finally
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static final class DefaultThreadProperties
/*     */     implements ThreadProperties
/*     */   {
/*     */     private final Thread t;
/*     */     
/*     */     DefaultThreadProperties(Thread t)
/*     */     {
/* 951 */       this.t = t;
/*     */     }
/*     */     
/*     */     public Thread.State state()
/*     */     {
/* 956 */       return this.t.getState();
/*     */     }
/*     */     
/*     */     public int priority()
/*     */     {
/* 961 */       return this.t.getPriority();
/*     */     }
/*     */     
/*     */     public boolean isInterrupted()
/*     */     {
/* 966 */       return this.t.isInterrupted();
/*     */     }
/*     */     
/*     */     public boolean isDaemon()
/*     */     {
/* 971 */       return this.t.isDaemon();
/*     */     }
/*     */     
/*     */     public String name()
/*     */     {
/* 976 */       return this.t.getName();
/*     */     }
/*     */     
/*     */     public long id()
/*     */     {
/* 981 */       return this.t.getId();
/*     */     }
/*     */     
/*     */     public StackTraceElement[] stackTrace()
/*     */     {
/* 986 */       return this.t.getStackTrace();
/*     */     }
/*     */     
/*     */     public boolean isAlive()
/*     */     {
/* 991 */       return this.t.isAlive();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\SingleThreadEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */