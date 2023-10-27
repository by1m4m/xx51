/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class DefaultPromise<V>
/*     */   extends AbstractFuture<V>
/*     */   implements Promise<V>
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
/*     */   
/*  36 */   private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
/*  37 */   private static final int MAX_LISTENER_STACK_DEPTH = Math.min(8, 
/*  38 */     SystemPropertyUtil.getInt("io.netty.defaultPromise.maxListenerStackDepth", 8));
/*     */   
/*     */ 
/*  41 */   private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, "result");
/*  42 */   private static final Object SUCCESS = new Object();
/*  43 */   private static final Object UNCANCELLABLE = new Object();
/*  44 */   private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(ThrowableUtil.unknownStackTrace(new CancellationException(), DefaultPromise.class, "cancel(...)"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile Object result;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final EventExecutor executor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object listeners;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private short waiters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean notifyingListeners;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPromise(EventExecutor executor)
/*     */   {
/*  80 */     this.executor = ((EventExecutor)ObjectUtil.checkNotNull(executor, "executor"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DefaultPromise()
/*     */   {
/*  88 */     this.executor = null;
/*     */   }
/*     */   
/*     */   public Promise<V> setSuccess(V result)
/*     */   {
/*  93 */     if (setSuccess0(result)) {
/*  94 */       notifyListeners();
/*  95 */       return this;
/*     */     }
/*  97 */     throw new IllegalStateException("complete already: " + this);
/*     */   }
/*     */   
/*     */   public boolean trySuccess(V result)
/*     */   {
/* 102 */     if (setSuccess0(result)) {
/* 103 */       notifyListeners();
/* 104 */       return true;
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   public Promise<V> setFailure(Throwable cause)
/*     */   {
/* 111 */     if (setFailure0(cause)) {
/* 112 */       notifyListeners();
/* 113 */       return this;
/*     */     }
/* 115 */     throw new IllegalStateException("complete already: " + this, cause);
/*     */   }
/*     */   
/*     */   public boolean tryFailure(Throwable cause)
/*     */   {
/* 120 */     if (setFailure0(cause)) {
/* 121 */       notifyListeners();
/* 122 */       return true;
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   public boolean setUncancellable()
/*     */   {
/* 129 */     if (RESULT_UPDATER.compareAndSet(this, null, UNCANCELLABLE)) {
/* 130 */       return true;
/*     */     }
/* 132 */     Object result = this.result;
/* 133 */     return (!isDone0(result)) || (!isCancelled0(result));
/*     */   }
/*     */   
/*     */   public boolean isSuccess()
/*     */   {
/* 138 */     Object result = this.result;
/* 139 */     return (result != null) && (result != UNCANCELLABLE) && (!(result instanceof CauseHolder));
/*     */   }
/*     */   
/*     */   public boolean isCancellable()
/*     */   {
/* 144 */     return this.result == null;
/*     */   }
/*     */   
/*     */   public Throwable cause()
/*     */   {
/* 149 */     Object result = this.result;
/* 150 */     return (result instanceof CauseHolder) ? ((CauseHolder)result).cause : null;
/*     */   }
/*     */   
/*     */   public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/* 155 */     ObjectUtil.checkNotNull(listener, "listener");
/*     */     
/* 157 */     synchronized (this) {
/* 158 */       addListener0(listener);
/*     */     }
/*     */     
/* 161 */     if (isDone()) {
/* 162 */       notifyListeners();
/*     */     }
/*     */     
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/* 170 */     ObjectUtil.checkNotNull(listeners, "listeners");
/*     */     
/* 172 */     synchronized (this) {
/* 173 */       for (GenericFutureListener<? extends Future<? super V>> listener : listeners) {
/* 174 */         if (listener == null) {
/*     */           break;
/*     */         }
/* 177 */         addListener0(listener);
/*     */       }
/*     */     }
/*     */     
/* 181 */     if (isDone()) {
/* 182 */       notifyListeners();
/*     */     }
/*     */     
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener)
/*     */   {
/* 190 */     ObjectUtil.checkNotNull(listener, "listener");
/*     */     
/* 192 */     synchronized (this) {
/* 193 */       removeListener0(listener);
/*     */     }
/*     */     
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
/*     */   {
/* 201 */     ObjectUtil.checkNotNull(listeners, "listeners");
/*     */     
/* 203 */     synchronized (this) {
/* 204 */       for (GenericFutureListener<? extends Future<? super V>> listener : listeners) {
/* 205 */         if (listener == null) {
/*     */           break;
/*     */         }
/* 208 */         removeListener0(listener);
/*     */       }
/*     */     }
/*     */     
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Promise<V> await()
/*     */     throws InterruptedException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 145	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   4: ifeq +5 -> 9
/*     */     //   7: aload_0
/*     */     //   8: areturn
/*     */     //   9: invokestatic 167	java/lang/Thread:interrupted	()Z
/*     */     //   12: ifeq +15 -> 27
/*     */     //   15: new 162	java/lang/InterruptedException
/*     */     //   18: dup
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual 168	io/netty/util/concurrent/DefaultPromise:toString	()Ljava/lang/String;
/*     */     //   23: invokespecial 169	java/lang/InterruptedException:<init>	(Ljava/lang/String;)V
/*     */     //   26: athrow
/*     */     //   27: aload_0
/*     */     //   28: invokevirtual 172	io/netty/util/concurrent/DefaultPromise:checkDeadLock	()V
/*     */     //   31: aload_0
/*     */     //   32: dup
/*     */     //   33: astore_1
/*     */     //   34: monitorenter
/*     */     //   35: aload_0
/*     */     //   36: invokevirtual 145	io/netty/util/concurrent/DefaultPromise:isDone	()Z
/*     */     //   39: ifne +28 -> 67
/*     */     //   42: aload_0
/*     */     //   43: invokespecial 175	io/netty/util/concurrent/DefaultPromise:incWaiters	()V
/*     */     //   46: aload_0
/*     */     //   47: invokevirtual 178	java/lang/Object:wait	()V
/*     */     //   50: aload_0
/*     */     //   51: invokespecial 181	io/netty/util/concurrent/DefaultPromise:decWaiters	()V
/*     */     //   54: goto +10 -> 64
/*     */     //   57: astore_2
/*     */     //   58: aload_0
/*     */     //   59: invokespecial 181	io/netty/util/concurrent/DefaultPromise:decWaiters	()V
/*     */     //   62: aload_2
/*     */     //   63: athrow
/*     */     //   64: goto -29 -> 35
/*     */     //   67: aload_1
/*     */     //   68: monitorexit
/*     */     //   69: goto +8 -> 77
/*     */     //   72: astore_3
/*     */     //   73: aload_1
/*     */     //   74: monitorexit
/*     */     //   75: aload_3
/*     */     //   76: athrow
/*     */     //   77: aload_0
/*     */     //   78: areturn
/*     */     // Line number table:
/*     */     //   Java source line #217	-> byte code offset #0
/*     */     //   Java source line #218	-> byte code offset #7
/*     */     //   Java source line #221	-> byte code offset #9
/*     */     //   Java source line #222	-> byte code offset #15
/*     */     //   Java source line #225	-> byte code offset #27
/*     */     //   Java source line #227	-> byte code offset #31
/*     */     //   Java source line #228	-> byte code offset #35
/*     */     //   Java source line #229	-> byte code offset #42
/*     */     //   Java source line #231	-> byte code offset #46
/*     */     //   Java source line #233	-> byte code offset #50
/*     */     //   Java source line #234	-> byte code offset #54
/*     */     //   Java source line #233	-> byte code offset #57
/*     */     //   Java source line #234	-> byte code offset #62
/*     */     //   Java source line #236	-> byte code offset #67
/*     */     //   Java source line #237	-> byte code offset #77
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	79	0	this	DefaultPromise<V>
/*     */     //   33	41	1	Ljava/lang/Object;	Object
/*     */     //   57	6	2	localObject1	Object
/*     */     //   72	4	3	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   46	50	57	finally
/*     */     //   35	69	72	finally
/*     */     //   72	75	72	finally
/*     */   }
/*     */   
/*     */   public Promise<V> awaitUninterruptibly()
/*     */   {
/* 242 */     if (isDone()) {
/* 243 */       return this;
/*     */     }
/*     */     
/* 246 */     checkDeadLock();
/*     */     
/* 248 */     boolean interrupted = false;
/* 249 */     synchronized (this) {
/* 250 */       while (!isDone()) {
/* 251 */         incWaiters();
/*     */         try {
/* 253 */           wait();
/*     */         }
/*     */         catch (InterruptedException e) {
/* 256 */           interrupted = true;
/*     */         } finally {
/* 258 */           decWaiters();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 263 */     if (interrupted) {
/* 264 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/* 267 */     return this;
/*     */   }
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 272 */     return await0(unit.toNanos(timeout), true);
/*     */   }
/*     */   
/*     */   public boolean await(long timeoutMillis) throws InterruptedException
/*     */   {
/* 277 */     return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
/*     */   {
/*     */     try {
/* 283 */       return await0(unit.toNanos(timeout), false);
/*     */     }
/*     */     catch (InterruptedException e) {
/* 286 */       throw new InternalError();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis)
/*     */   {
/*     */     try {
/* 293 */       return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
/*     */     }
/*     */     catch (InterruptedException e) {
/* 296 */       throw new InternalError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public V getNow()
/*     */   {
/* 303 */     Object result = this.result;
/* 304 */     if (((result instanceof CauseHolder)) || (result == SUCCESS) || (result == UNCANCELLABLE)) {
/* 305 */       return null;
/*     */     }
/* 307 */     return (V)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean cancel(boolean mayInterruptIfRunning)
/*     */   {
/* 317 */     if (RESULT_UPDATER.compareAndSet(this, null, CANCELLATION_CAUSE_HOLDER)) {
/* 318 */       checkNotifyWaiters();
/* 319 */       notifyListeners();
/* 320 */       return true;
/*     */     }
/* 322 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCancelled()
/*     */   {
/* 327 */     return isCancelled0(this.result);
/*     */   }
/*     */   
/*     */   public boolean isDone()
/*     */   {
/* 332 */     return isDone0(this.result);
/*     */   }
/*     */   
/*     */   public Promise<V> sync() throws InterruptedException
/*     */   {
/* 337 */     await();
/* 338 */     rethrowIfFailed();
/* 339 */     return this;
/*     */   }
/*     */   
/*     */   public Promise<V> syncUninterruptibly()
/*     */   {
/* 344 */     awaitUninterruptibly();
/* 345 */     rethrowIfFailed();
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 351 */     return toStringBuilder().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected StringBuilder toStringBuilder()
/*     */   {
/* 358 */     StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('@').append(Integer.toHexString(hashCode()));
/*     */     
/* 360 */     Object result = this.result;
/* 361 */     if (result == SUCCESS) {
/* 362 */       buf.append("(success)");
/* 363 */     } else if (result == UNCANCELLABLE) {
/* 364 */       buf.append("(uncancellable)");
/* 365 */     } else if ((result instanceof CauseHolder))
/*     */     {
/*     */ 
/* 368 */       buf.append("(failure: ").append(((CauseHolder)result).cause).append(')');
/* 369 */     } else if (result != null)
/*     */     {
/*     */ 
/* 372 */       buf.append("(success: ").append(result).append(')');
/*     */     } else {
/* 374 */       buf.append("(incomplete)");
/*     */     }
/*     */     
/* 377 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EventExecutor executor()
/*     */   {
/* 389 */     return this.executor;
/*     */   }
/*     */   
/*     */   protected void checkDeadLock() {
/* 393 */     EventExecutor e = executor();
/* 394 */     if ((e != null) && (e.inEventLoop())) {
/* 395 */       throw new BlockingOperationException(toString());
/*     */     }
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
/*     */   protected static void notifyListener(EventExecutor eventExecutor, Future<?> future, GenericFutureListener<?> listener)
/*     */   {
/* 410 */     ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
/* 411 */     ObjectUtil.checkNotNull(future, "future");
/* 412 */     ObjectUtil.checkNotNull(listener, "listener");
/* 413 */     notifyListenerWithStackOverFlowProtection(eventExecutor, future, listener);
/*     */   }
/*     */   
/*     */   private void notifyListeners() {
/* 417 */     EventExecutor executor = executor();
/* 418 */     if (executor.inEventLoop()) {
/* 419 */       InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 420 */       int stackDepth = threadLocals.futureListenerStackDepth();
/* 421 */       if (stackDepth < MAX_LISTENER_STACK_DEPTH) {
/* 422 */         threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */         try {
/* 424 */           notifyListenersNow();
/*     */         } finally {
/* 426 */           threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */         }
/* 428 */         return;
/*     */       }
/*     */     }
/*     */     
/* 432 */     safeExecute(executor, new Runnable()
/*     */     {
/*     */       public void run() {
/* 435 */         DefaultPromise.this.notifyListenersNow();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void notifyListenerWithStackOverFlowProtection(EventExecutor executor, Future<?> future, final GenericFutureListener<?> listener)
/*     */   {
/* 448 */     if (executor.inEventLoop()) {
/* 449 */       InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 450 */       int stackDepth = threadLocals.futureListenerStackDepth();
/* 451 */       if (stackDepth < MAX_LISTENER_STACK_DEPTH) {
/* 452 */         threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */         try {
/* 454 */           notifyListener0(future, listener);
/*     */         } finally {
/* 456 */           threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */         }
/* 458 */         return;
/*     */       }
/*     */     }
/*     */     
/* 462 */     safeExecute(executor, new Runnable()
/*     */     {
/*     */       public void run() {
/* 465 */         DefaultPromise.notifyListener0(this.val$future, listener);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private void notifyListenersNow()
/*     */   {
/* 472 */     synchronized (this)
/*     */     {
/* 474 */       if ((this.notifyingListeners) || (this.listeners == null)) {
/* 475 */         return;
/*     */       }
/* 477 */       this.notifyingListeners = true;
/* 478 */       Object listeners = this.listeners;
/* 479 */       this.listeners = null;
/*     */     }
/*     */     for (;;) { Object listeners;
/* 482 */       if ((listeners instanceof DefaultFutureListeners)) {
/* 483 */         notifyListeners0((DefaultFutureListeners)listeners);
/*     */       } else {
/* 485 */         notifyListener0(this, (GenericFutureListener)listeners);
/*     */       }
/* 487 */       synchronized (this) {
/* 488 */         if (this.listeners == null)
/*     */         {
/*     */ 
/* 491 */           this.notifyingListeners = false;
/* 492 */           return;
/*     */         }
/* 494 */         listeners = this.listeners;
/* 495 */         this.listeners = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyListeners0(DefaultFutureListeners listeners) {
/* 501 */     GenericFutureListener<?>[] a = listeners.listeners();
/* 502 */     int size = listeners.size();
/* 503 */     for (int i = 0; i < size; i++) {
/* 504 */       notifyListener0(this, a[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void notifyListener0(Future future, GenericFutureListener l)
/*     */   {
/*     */     try {
/* 511 */       l.operationComplete(future);
/*     */     } catch (Throwable t) {
/* 513 */       if (logger.isWarnEnabled()) {
/* 514 */         logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addListener0(GenericFutureListener<? extends Future<? super V>> listener) {
/* 520 */     if (this.listeners == null) {
/* 521 */       this.listeners = listener;
/* 522 */     } else if ((this.listeners instanceof DefaultFutureListeners)) {
/* 523 */       ((DefaultFutureListeners)this.listeners).add(listener);
/*     */     } else {
/* 525 */       this.listeners = new DefaultFutureListeners((GenericFutureListener)this.listeners, listener);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeListener0(GenericFutureListener<? extends Future<? super V>> listener) {
/* 530 */     if ((this.listeners instanceof DefaultFutureListeners)) {
/* 531 */       ((DefaultFutureListeners)this.listeners).remove(listener);
/* 532 */     } else if (this.listeners == listener) {
/* 533 */       this.listeners = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean setSuccess0(V result) {
/* 538 */     return setValue0(result == null ? SUCCESS : result);
/*     */   }
/*     */   
/*     */   private boolean setFailure0(Throwable cause) {
/* 542 */     return setValue0(new CauseHolder((Throwable)ObjectUtil.checkNotNull(cause, "cause")));
/*     */   }
/*     */   
/*     */   private boolean setValue0(Object objResult) {
/* 546 */     if ((RESULT_UPDATER.compareAndSet(this, null, objResult)) || 
/* 547 */       (RESULT_UPDATER.compareAndSet(this, UNCANCELLABLE, objResult))) {
/* 548 */       checkNotifyWaiters();
/* 549 */       return true;
/*     */     }
/* 551 */     return false;
/*     */   }
/*     */   
/*     */   private synchronized void checkNotifyWaiters() {
/* 555 */     if (this.waiters > 0) {
/* 556 */       notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */   private void incWaiters() {
/* 561 */     if (this.waiters == Short.MAX_VALUE) {
/* 562 */       throw new IllegalStateException("too many waiters: " + this);
/*     */     }
/* 564 */     this.waiters = ((short)(this.waiters + 1));
/*     */   }
/*     */   
/*     */   private void decWaiters() {
/* 568 */     this.waiters = ((short)(this.waiters - 1));
/*     */   }
/*     */   
/*     */   private void rethrowIfFailed() {
/* 572 */     Throwable cause = cause();
/* 573 */     if (cause == null) {
/* 574 */       return;
/*     */     }
/*     */     
/* 577 */     PlatformDependent.throwException(cause);
/*     */   }
/*     */   
/*     */   private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
/* 581 */     if (isDone()) {
/* 582 */       return true;
/*     */     }
/*     */     
/* 585 */     if (timeoutNanos <= 0L) {
/* 586 */       return isDone();
/*     */     }
/*     */     
/* 589 */     if ((interruptable) && (Thread.interrupted())) {
/* 590 */       throw new InterruptedException(toString());
/*     */     }
/*     */     
/* 593 */     checkDeadLock();
/*     */     
/* 595 */     long startTime = System.nanoTime();
/* 596 */     long waitTime = timeoutNanos;
/* 597 */     boolean interrupted = false;
/*     */     try {
/*     */       do {
/* 600 */         synchronized (this) {
/* 601 */           if (isDone()) {
/* 602 */             return true;
/*     */           }
/* 604 */           incWaiters();
/*     */           try {
/* 606 */             wait(waitTime / 1000000L, (int)(waitTime % 1000000L));
/*     */           } catch (InterruptedException e) {
/* 608 */             if (interruptable) {
/* 609 */               throw e;
/*     */             }
/* 611 */             interrupted = true;
/*     */           }
/*     */           finally {
/* 614 */             decWaiters();
/*     */           }
/*     */         }
/* 617 */         if (isDone()) {
/* 618 */           return (boolean)1;
/*     */         }
/* 620 */         waitTime = timeoutNanos - (System.nanoTime() - startTime);
/* 621 */       } while (waitTime > 0L);
/* 622 */       return isDone();
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 627 */       if (interrupted) {
/* 628 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
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
/*     */   void notifyProgressiveListeners(final long progress, long total)
/*     */   {
/* 645 */     Object listeners = progressiveListeners();
/* 646 */     if (listeners == null) {
/* 647 */       return;
/*     */     }
/*     */     
/* 650 */     final ProgressiveFuture<V> self = (ProgressiveFuture)this;
/*     */     
/* 652 */     EventExecutor executor = executor();
/* 653 */     if (executor.inEventLoop()) {
/* 654 */       if ((listeners instanceof GenericProgressiveFutureListener[])) {
/* 655 */         notifyProgressiveListeners0(self, (GenericProgressiveFutureListener[])listeners, progress, total);
/*     */       }
/*     */       else {
/* 658 */         notifyProgressiveListener0(self, (GenericProgressiveFutureListener)listeners, progress, total);
/*     */       }
/*     */       
/*     */     }
/* 662 */     else if ((listeners instanceof GenericProgressiveFutureListener[])) {
/* 663 */       final GenericProgressiveFutureListener<?>[] array = (GenericProgressiveFutureListener[])listeners;
/*     */       
/* 665 */       safeExecute(executor, new Runnable()
/*     */       {
/*     */         public void run() {
/* 668 */           DefaultPromise.notifyProgressiveListeners0(self, array, progress, this.val$total);
/*     */         }
/*     */       });
/*     */     } else {
/* 672 */       final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener)listeners;
/*     */       
/* 674 */       safeExecute(executor, new Runnable()
/*     */       {
/*     */         public void run() {
/* 677 */           DefaultPromise.notifyProgressiveListener0(self, l, progress, this.val$total);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized Object progressiveListeners()
/*     */   {
/* 689 */     Object listeners = this.listeners;
/* 690 */     if (listeners == null)
/*     */     {
/* 692 */       return null;
/*     */     }
/*     */     
/* 695 */     if ((listeners instanceof DefaultFutureListeners))
/*     */     {
/* 697 */       DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
/* 698 */       int progressiveSize = dfl.progressiveSize();
/* 699 */       switch (progressiveSize) {
/*     */       case 0: 
/* 701 */         return null;
/*     */       case 1: 
/* 703 */         for (GenericFutureListener<?> l : dfl.listeners()) {
/* 704 */           if ((l instanceof GenericProgressiveFutureListener)) {
/* 705 */             return l;
/*     */           }
/*     */         }
/* 708 */         return null;
/*     */       }
/*     */       
/* 711 */       Object array = dfl.listeners();
/* 712 */       Object copy = new GenericProgressiveFutureListener[progressiveSize];
/* 713 */       int i = 0; for (int j = 0; j < progressiveSize; i++) {
/* 714 */         GenericFutureListener<?> l = array[i];
/* 715 */         if ((l instanceof GenericProgressiveFutureListener)) {
/* 716 */           copy[(j++)] = ((GenericProgressiveFutureListener)l);
/*     */         }
/*     */       }
/*     */       
/* 720 */       return copy; }
/* 721 */     if ((listeners instanceof GenericProgressiveFutureListener)) {
/* 722 */       return listeners;
/*     */     }
/*     */     
/* 725 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void notifyProgressiveListeners0(ProgressiveFuture<?> future, GenericProgressiveFutureListener<?>[] listeners, long progress, long total)
/*     */   {
/* 731 */     for (GenericProgressiveFutureListener<?> l : listeners) {
/* 732 */       if (l == null) {
/*     */         break;
/*     */       }
/* 735 */       notifyProgressiveListener0(future, l, progress, total);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void notifyProgressiveListener0(ProgressiveFuture future, GenericProgressiveFutureListener l, long progress, long total)
/*     */   {
/*     */     try
/*     */     {
/* 743 */       l.operationProgressed(future, progress, total);
/*     */     } catch (Throwable t) {
/* 745 */       if (logger.isWarnEnabled()) {
/* 746 */         logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isCancelled0(Object result) {
/* 752 */     return ((result instanceof CauseHolder)) && ((((CauseHolder)result).cause instanceof CancellationException));
/*     */   }
/*     */   
/*     */   private static boolean isDone0(Object result) {
/* 756 */     return (result != null) && (result != UNCANCELLABLE);
/*     */   }
/*     */   
/*     */   private static final class CauseHolder {
/*     */     final Throwable cause;
/*     */     
/* 762 */     CauseHolder(Throwable cause) { this.cause = cause; }
/*     */   }
/*     */   
/*     */   private static void safeExecute(EventExecutor executor, Runnable task)
/*     */   {
/*     */     try {
/* 768 */       executor.execute(task);
/*     */     } catch (Throwable t) {
/* 770 */       rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\concurrent\DefaultPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */